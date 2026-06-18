package eu.ibagroup.snippet_manager.controller;

import eu.ibagroup.snippet_manager.dto.SnippetRequestTO;
import eu.ibagroup.snippet_manager.dto.SnippetResponseTO;
import eu.ibagroup.snippet_manager.dto.SnippetUpdateTO;
import eu.ibagroup.snippet_manager.entity.Snippet;
import eu.ibagroup.snippet_manager.exception.SnippetNotFoundException;
import eu.ibagroup.snippet_manager.mapper.SnippetMapper;
import eu.ibagroup.snippet_manager.service.SnippetService;
import eu.ibagroup.snippet_manager.specification.criteria.SnippetSearchCriteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@WebMvcTest(controllers = SnippetController.class)
public class SnippetControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @MockitoBean
    private SnippetService snippetService;

    @MockitoBean
    private SnippetMapper snippetMapper;

    @Autowired
    public SnippetControllerTest(final MockMvc mockMvc,
                                 final ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void whenGetSnippetsNoFilter_ReturnSnippetsPage() throws Exception {
        SnippetResponseTO responseEntity1 = new SnippetResponseTO(
                UUID.randomUUID(),
                "<p>Test</p>","Test HTML Tag",
                "HTML",
                LocalDateTime.now());
        SnippetResponseTO responseEntity2 =  new SnippetResponseTO(
                UUID.randomUUID(),
                "public void check() { ... }",
                "Function to check complex condition",
                "Java",
                LocalDateTime.now());
        when(snippetService.getAllSnippets(anyInt(), anyInt(), any()))
                .thenReturn(new PageImpl<>(List.of(responseEntity1, responseEntity2)));
        mockMvc.perform(get("/api/v1/snippets").param("pageNumber", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title")
                        .value("Test HTML Tag"))
                .andExpect(jsonPath("$.content[1].title")
                        .value("Function to check complex condition"));
        verify(snippetService, times(1)).getAllSnippets(eq(0), eq(10),
                argThat(criteria ->
                        criteria.getCode() == null
                        && criteria.getLangTags() == null
                        && criteria.getTitle() == null
                ));
    }

    @Test
    void whenGetSnippetsWithFilter_ReturnFilteredSnippets() throws Exception {
        SnippetResponseTO response = new SnippetResponseTO(
                        UUID.randomUUID(),
                        "public void quickSort() {...}",
                        "Quick sort algorithm",
                        "Java",
                        LocalDateTime.now());
        when(snippetService.getAllSnippets(anyInt(), anyInt(), any(SnippetSearchCriteria.class)))
                .thenReturn(new PageImpl<>(List.of(response)));
        mockMvc.perform(get("/api/v1/snippets")
                        .param("pageNumber", "0")
                        .param("size", "10")
                        .param("title", "Quick")
                        .param("langTags", "Java"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.content.length()")
                        .value(1))
                .andExpect(jsonPath("$.content[0].title")
                        .value("Quick sort algorithm"))
                .andExpect(jsonPath("$.content[0].language")
                        .value("Java"));
        verify(snippetService).getAllSnippets(eq(0), eq(10),
                argThat(criteria ->
                        "Quick".equals(criteria.getTitle())
                                && criteria.getLangTags() != null
                                && criteria.getLangTags().contains("Java")

                )
        );
    }

    @Test
    void whenGetSnippetById_ReturnSnippet() throws Exception {
        UUID id = UUID.randomUUID();
        Snippet snippet = new Snippet();
        snippet.setId(id);
        SnippetResponseTO response = new SnippetResponseTO(
                        id,
                        "<p>Test</p>",
                        "Test snippet",
                        "HTML",
                        LocalDateTime.now());
        when(snippetService.findSnippetById(id)).thenReturn(snippet);
        when(snippetMapper.toSnippetResponse(snippet)).thenReturn(response);
        mockMvc.perform(get("/api/v1/snippets/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("Test snippet"))
                .andExpect(jsonPath("$.language").value("HTML"));
        verify(snippetService).findSnippetById(id);
        verify(snippetMapper).toSnippetResponse(snippet);
    }

    @Test
    void whenCreateSnippet_ReturnCreatedSnippet() throws Exception {
        UUID id = UUID.randomUUID();
        SnippetRequestTO request = new SnippetRequestTO(
                        "public void quickSort(T[] arr) {...}",
                        "Quick sort",
                        "Java");
        Snippet savedSnippet = new Snippet();
        savedSnippet.setId(id);
        SnippetResponseTO response = new SnippetResponseTO(
                        id,
                        request.getCode(),
                        request.getTitle(),
                        request.getLanguage(),
                        LocalDateTime.now());
        when(snippetService.createSnippet(any(SnippetRequestTO.class)))
                .thenReturn(savedSnippet);
        when(snippetMapper.toSnippetResponse(savedSnippet))
                .thenReturn(response);
        mockMvc.perform(post("/api/v1/snippets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Quick sort"))
                .andExpect(jsonPath("$.language").value("Java"));
        verify(snippetService).createSnippet(any(SnippetRequestTO.class));
    }

    @Test
    void whenUpdateSnippet_ReturnUpdatedSnippet() throws Exception {
        UUID id = UUID.randomUUID();
        SnippetUpdateTO request =  new SnippetUpdateTO(
                "public void fastSort(T[] arr) {...}",
                 "Updated title",
                "Java",
                LocalDateTime.now());
        Snippet updatedSnippet = new Snippet();
        updatedSnippet.setId(id);
        SnippetResponseTO response = new SnippetResponseTO(
                        id,
                       "public void fastSort(T[] arr) {...}",
                        "Updated title",
                        "Java",
                        LocalDateTime.now());
        when(snippetService.updateSnippet(eq(id), any(SnippetUpdateTO.class)))
                .thenReturn(updatedSnippet);
        when(snippetMapper.toSnippetResponse(updatedSnippet))
                .thenReturn(response);
        mockMvc.perform(patch("/api/v1/snippets/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated title"));
        verify(snippetService).updateSnippet(eq(id), any(SnippetUpdateTO.class));
    }

    @Test
    void whenDeleteSnippet_ReturnNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(snippetService).deleteSnippet(id);
        mockMvc.perform(delete("/api/v1/snippets/{id}", id))
                .andExpect(status().isNoContent());
        verify(snippetService).deleteSnippet(id);
    }

    @Test
    void whenGetSnippetByIdNotFound_Return404() throws Exception {
        UUID id = UUID.randomUUID();
        when(snippetService.findSnippetById(id))
                .thenThrow(new SnippetNotFoundException("Snippet not found"));
        mockMvc.perform(get("/api/v1/snippets/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenCreateSnippetInvalidRequest_Return400() throws Exception {
        SnippetRequestTO invalidRequest = new SnippetRequestTO(
                        "",
                        "",
                        "");
        mockMvc.perform(post("/api/v1/snippets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        verify(snippetService, never()).createSnippet(any());
    }

}
