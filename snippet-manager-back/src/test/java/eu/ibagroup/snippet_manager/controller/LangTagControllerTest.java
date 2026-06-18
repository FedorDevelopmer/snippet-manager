package eu.ibagroup.snippet_manager.controller;

import eu.ibagroup.snippet_manager.dto.LangTagRequestTO;
import eu.ibagroup.snippet_manager.dto.LangTagResponseTO;
import eu.ibagroup.snippet_manager.dto.LangTagUpdateTO;
import eu.ibagroup.snippet_manager.entity.LangTag;
import eu.ibagroup.snippet_manager.enumeration.DevLang;
import eu.ibagroup.snippet_manager.mapper.LangTagMapper;
import eu.ibagroup.snippet_manager.service.LangTagService;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@WebMvcTest(LangTagController.class)
public class LangTagControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @MockitoBean
    private LangTagService langTagService;

    @MockitoBean
    private LangTagMapper langTagMapper;

    @Autowired
    private LangTagControllerTest(final MockMvc mockMvc, final ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void whenGetTags_ReturnTagsPage() throws Exception {
        LangTagResponseTO response1 = new LangTagResponseTO(
                        UUID.randomUUID(),
                        DevLang.JAVA.getLanguageName(),
                        "#f89820");
        LangTagResponseTO response2 = new LangTagResponseTO(
                        UUID.randomUUID(),
                        DevLang.JS.getLanguageName(),
                        "#f7df1e");
        when(langTagService.getAllLangTags(anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of(response1, response2)));
        mockMvc.perform(get("/api/v1/tags")
                        .param("pageNumber", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()")
                        .value(2))
                .andExpect(jsonPath("$.content[0].language")
                        .value("Java"))
                .andExpect(jsonPath("$.content[1].language")
                        .value("JavaScript"));
        verify(langTagService).getAllLangTags(0, 10);
    }

    @Test
    void whenGetTagById_ReturnTag() throws Exception {
        UUID id = UUID.randomUUID();
        LangTag tag = new LangTag();
        tag.setId(id);
        LangTagResponseTO response = new LangTagResponseTO(
                        id,
                        DevLang.JAVA.getLanguageName(),
                        "#f89820");
        when(langTagService.findLangTagById(id))
                .thenReturn(tag);
        when(langTagMapper.toLangTagResponse(tag))
                .thenReturn(response);
        mockMvc.perform(get("/api/v1/tags/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id")
                        .value(id.toString()))
                .andExpect(jsonPath("$.language")
                        .value("Java"));
        verify(langTagService).findLangTagById(id);
        verify(langTagMapper).toLangTagResponse(tag);
    }

    @Test
    void whenGetTagByName_ReturnTag() throws Exception {
        String language = "Java";
        LangTag tag = new LangTag();
        tag.setLanguage(DevLang.JAVA);
        LangTagResponseTO response = new LangTagResponseTO(
                        UUID.randomUUID(),
                        DevLang.JAVA.getLanguageName(),
                        "#f89820");
        when(langTagService.findLangTagByLanguage(DevLang.JAVA))
                .thenReturn(tag);
        when(langTagMapper.toLangTagResponse(tag))
                .thenReturn(response);
        mockMvc.perform(get("/api/v1/tags/name")
                        .param("name", language))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.language")
                        .value("Java"));
        verify(langTagService).findLangTagByLanguage(DevLang.JAVA);
        verify(langTagMapper).toLangTagResponse(tag);
    }

    @Test
    void whenCreateTag_ReturnCreatedTag() throws Exception {
        UUID id = UUID.randomUUID();
        LangTagRequestTO request = new LangTagRequestTO(
                        DevLang.JAVA.getLanguageName(),
                        "#f89820");
        LangTag savedTag = new LangTag();
        savedTag.setId(id);
        LangTagResponseTO response = new LangTagResponseTO(
                        id,
                        DevLang.JAVA.getLanguageName(),
                        "#f89820");
        when(langTagService.createLangTag(any(LangTagRequestTO.class)))
                .thenReturn(savedTag);
        when(langTagMapper.toLangTagResponse(savedTag))
                .thenReturn(response);
        mockMvc.perform(post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.language")
                        .value("Java"));
        verify(langTagService).createLangTag(any(LangTagRequestTO.class));
    }

    @Test
    void whenUpdateTag_ReturnUpdatedTag() throws Exception {
        UUID id = UUID.randomUUID();
        LangTagUpdateTO request = new LangTagUpdateTO(
                        DevLang.JAVA.getLanguageName(),
                        "#000000");
        LangTag updatedTag = new LangTag();
        updatedTag.setId(id);
        LangTagResponseTO response = new LangTagResponseTO(
                        id,
                        DevLang.JAVA.getLanguageName(),
                        "#000000");
        when(langTagService.updateLangTag(eq(id), any(LangTagUpdateTO.class)))
                .thenReturn(updatedTag);
        when(langTagMapper.toLangTagResponse(updatedTag))
                .thenReturn(response);
        mockMvc.perform(patch("/api/v1/tags/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color")
                        .value("#000000"));
        verify(langTagService).updateLangTag(eq(id), any(LangTagUpdateTO.class));
    }

    @Test
    void whenDeleteTag_ReturnNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(langTagService).deleteLangTag(id);
        mockMvc.perform(delete("/api/v1/tags/{id}", id))
                .andExpect(status().isNoContent());
        verify(langTagService).deleteLangTag(id);
    }

    @Test
    void whenCreateTagInvalidRequest_Return400() throws Exception {
        LangTagRequestTO invalidRequest = new LangTagRequestTO();
        mockMvc.perform(post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        verify(langTagService, never()).createLangTag(any());
    }

}
