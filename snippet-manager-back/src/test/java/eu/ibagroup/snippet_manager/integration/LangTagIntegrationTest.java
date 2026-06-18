package eu.ibagroup.snippet_manager.integration;

import eu.ibagroup.snippet_manager.dto.LangTagRequestTO;
import eu.ibagroup.snippet_manager.dto.LangTagUpdateTO;
import eu.ibagroup.snippet_manager.repository.LangTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LangTagIntegrationTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final LangTagRepository langTagRepository;

    @Autowired
    public LangTagIntegrationTest(final MockMvc mockMvc,
                                  final ObjectMapper objectMapper,
                                  final LangTagRepository langTagRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.langTagRepository = langTagRepository;
    }


    @BeforeEach
    void cleanup() {
        langTagRepository.deleteAll();
    }

    @Test
    void createTagTest() throws Exception {
        LangTagRequestTO request = new LangTagRequestTO("Java", "#f89820");
        mockMvc.perform(post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.language").value("Java"))
                .andExpect(jsonPath("$.color").value("#f89820"));
    }

    @Test
    void getTagByIdTest() throws Exception {
        String response = mockMvc.perform(post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LangTagRequestTO("Java", "#f89820")
                        )))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        UUID id = UUID.fromString(objectMapper.readTree(response).get("id").asString());
        mockMvc.perform(get("/api/v1/tags/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.language").value("Java"))
                .andExpect(jsonPath("$.color").value("#f89820"));
    }

    @Test
    void getTagByNameTest() throws Exception {
        mockMvc.perform(post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LangTagRequestTO("JavaScript", "#f0db4f")
                        )))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/api/v1/tags/name")
                        .param("name", "JavaScript"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.language").value("JavaScript"))
                .andExpect(jsonPath("$.color").value("#f0db4f"));
    }

    @Test
    void updateTagTest() throws Exception {
        String response = mockMvc.perform(post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LangTagRequestTO("Java", "#f89820")
                        )))
                .andReturn()
                .getResponse()
                .getContentAsString();
        UUID id = UUID.fromString(objectMapper.readTree(response).get("id").asString());
        LangTagUpdateTO update = new LangTagUpdateTO("JavaScript", "#f0db4f");
        mockMvc.perform(patch("/api/v1/tags/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.language").value("JavaScript"))
                .andExpect(jsonPath("$.color").value("#f0db4f"));
    }

    @Test
    void deleteTagTest() throws Exception {
        String response = mockMvc.perform(post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new LangTagRequestTO("Java", "#f89820")
                        )))
                .andReturn()
                .getResponse()
                .getContentAsString();
        UUID id = UUID.fromString(objectMapper.readTree(response).get("id").asString());
        mockMvc.perform(delete("/api/v1/tags/{id}", id))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/tags/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void tagValidationTest() throws Exception {
        LangTagRequestTO invalid = new LangTagRequestTO("", "");
        mockMvc.perform(post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

}
