package eu.ibagroup.snippet_manager.integration;

import eu.ibagroup.snippet_manager.dto.LangTagRequestTO;
import eu.ibagroup.snippet_manager.dto.SnippetRequestTO;
import eu.ibagroup.snippet_manager.dto.SnippetUpdateTO;
import eu.ibagroup.snippet_manager.entity.LangTag;
import eu.ibagroup.snippet_manager.enumeration.DevLang;
import eu.ibagroup.snippet_manager.mapper.LangTagMapper;
import eu.ibagroup.snippet_manager.repository.LangTagRepository;
import eu.ibagroup.snippet_manager.repository.SnippetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = eu.ibagroup.snippet_manager.SnippetManagerApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SnippetsIntegrationTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final SnippetRepository snippetRepository;

    private final LangTagRepository langTagRepository;

    private final LangTagMapper langTagMapper;

    @Autowired
    public SnippetsIntegrationTest(final MockMvc mockMvc,
                                   final ObjectMapper objectMapper,
                                   final SnippetRepository snippetRepository,
                                   final LangTagRepository langTagRepository,
                                   final LangTagMapper langTagMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.snippetRepository = snippetRepository;
        this.langTagRepository = langTagRepository;
        this.langTagMapper = langTagMapper;
    }



    @BeforeEach
    void cleanup() {
        snippetRepository.deleteAll();
        langTagRepository.deleteAll();
        createDefaultTags();
    }

    @Test
    void snippetCrudLifecycleTest() throws Exception {
        SnippetRequestTO createRequest = new SnippetRequestTO(
                "public void quickSort(T[] arr) {...}",
                "Quick sort",
                "Java"
        );
        String createResponse = mockMvc.perform(post("/api/v1/snippets")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title")
                        .value("Quick sort"))
                .andExpect(jsonPath("$.language")
                        .value("Java"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        UUID snippetId = UUID.fromString(objectMapper.readTree(createResponse)
                        .get("id").asString());

        mockMvc.perform(get("/api/v1/snippets/{id}", snippetId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Quick sort"))
                .andExpect(jsonPath("$.language")
                        .value("Java"));

        SnippetUpdateTO updateRequest = new SnippetUpdateTO(
                "public void quickSortOptimized() {...}",
                "Quick sort optimized",
                "Java",
                LocalDateTime.now()
        );
        mockMvc.perform(patch("/api/v1/snippets/{id}", snippetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Quick sort optimized"));

        mockMvc.perform(get("/api/v1/snippets/{id}", snippetId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title")
                        .value("Quick sort optimized"))
                .andExpect(jsonPath("$.code")
                        .value("public void quickSortOptimized() {...}"));

        mockMvc.perform(delete("/api/v1/snippets/{id}", snippetId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/snippets/{id}", snippetId))
                .andExpect(status().isNotFound());
    }

    @Test
    void snippetsSearchByTitleTest() throws Exception {
        createSnippet("public void quickSort(T[] arr) {...}","Quick sort Java","Java");
        createSnippet("function quickSort(arr: Any[]) {...}","Quick sort JS","JavaScript");
        createSnippet("public void run() throws InterruptionException {...}","Thread run method impl", "Java");
        mockMvc.perform(get("/api/v1/snippets")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .param("title", "Quick sort JS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Quick sort JS"))
                .andExpect(jsonPath("$.content[0].language").value("JavaScript"));

    }

    @Test
    void snippetsSearchByLanguageTest() throws Exception {
        createSnippet("public void quickSort(T[] arr) {...}","Quick sort Java","Java");
        createSnippet("function quickSort(arr: Any[]) {...}","Quick sort JS","JavaScript");
        createSnippet("public void run() throws InterruptionException {...}","Thread run method impl", "Java");
        mockMvc.perform(get("/api/v1/snippets")
                        .param("pageNumber", "0")
                        .param("size", "10")
                        .param("langTags", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[*].title",
                        containsInAnyOrder(
                                "Quick sort Java",
                                "Thread run method impl"
                        )
                ))
                .andExpect(jsonPath("$.content[0].language").value("Java"))
                .andExpect(jsonPath("$.content[1].language").value("Java"));
    }

    @Test
    void snippetValidationTest() throws Exception {
        SnippetRequestTO invalidRequest = new SnippetRequestTO(
                "",
                "",
                ""
        );
        mockMvc.perform(post("/api/v1/snippets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void snippetNotFoundTest() throws Exception {
        UUID nonExistingId = UUID.randomUUID();
        mockMvc.perform(get("/api/v1/snippets/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }

    private void createSnippet(String code, String title, String language) throws Exception {
        SnippetRequestTO createEntity = new SnippetRequestTO(code, title, language);
        mockMvc.perform(post("/api/v1/snippets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEntity)))
                .andExpect(status().isCreated());
    }

    private void createDefaultTags() {
        Random random = new Random();
        for (DevLang val : DevLang.values()) {
            String name = val.getLanguageName();
            LangTagRequestTO requestTO = new LangTagRequestTO();
            requestTO.setLanguage(name);
            long colorInt = 0;
            for(int i = 0; i < 3; i++){
                colorInt = (colorInt << 8) + random.nextInt(0, 255);

            }
            requestTO.setColor("#" + Long.toHexString(colorInt));
            Optional<LangTag> existingLangTag = langTagRepository.findByLanguage(DevLang.fromLanguage(requestTO.getLanguage()));
            if(existingLangTag.isEmpty()){
                langTagRepository.save(langTagMapper.toLangTag(requestTO));
            }
        }
    }
}
