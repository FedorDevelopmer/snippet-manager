package eu.ibagroup.snippet_manager.controller;

import eu.ibagroup.snippet_manager.dto.LangTagRequestTO;
import eu.ibagroup.snippet_manager.dto.LangTagResponseTO;
import eu.ibagroup.snippet_manager.dto.LangTagUpdateTO;
import eu.ibagroup.snippet_manager.dto.SnippetRequestTO;
import eu.ibagroup.snippet_manager.entity.LangTag;
import eu.ibagroup.snippet_manager.enumeration.DevLang;
import eu.ibagroup.snippet_manager.mapper.LangTagMapper;
import eu.ibagroup.snippet_manager.service.LangTagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tags")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.HEAD})
public class LangTagController {

    private LangTagService langTagService;

    private LangTagMapper langTagMapper;

    @Autowired
    public LangTagController(final LangTagService service, final LangTagMapper langTagMapper) {
        this.langTagService = service;
        this.langTagMapper = langTagMapper;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LangTagResponseTO> getTagById(@PathVariable UUID id) {
        LangTag langTag = langTagService.findLangTagById(id);
        return new ResponseEntity<>(langTagMapper.toLangTagResponse(langTag), HttpStatus.OK);
    }

    @GetMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LangTagResponseTO> getTagByName(@RequestParam String name) {
        LangTag langTag = langTagService.findLangTagByLanguage(DevLang.fromLanguage(name));
        return new ResponseEntity<>(langTagMapper.toLangTagResponse(langTag), HttpStatus.OK);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Page<LangTagResponseTO>> getTags(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                           @RequestParam(defaultValue = "15") Integer size) {
        return new ResponseEntity<>(langTagService.getAllLangTags(pageNumber, size), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LangTagResponseTO> createTag(@Valid @RequestBody LangTagRequestTO langTagRequestTO) {
        LangTag savedlangTag = langTagService.createLangTag(langTagRequestTO);
        return new ResponseEntity<>(langTagMapper.toLangTagResponse(savedlangTag), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LangTagResponseTO> updateTag(@PathVariable UUID id, @Valid @RequestBody LangTagUpdateTO langTagUpdateTO) {
        LangTag updatedLangTag = langTagService.updateLangTag(id, langTagUpdateTO);
        return new ResponseEntity<>(langTagMapper.toLangTagResponse(updatedLangTag), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        langTagService.deleteLangTag(id);
        return ResponseEntity.noContent().build();
    }
}
