package eu.ibagroup.snippet_manager.controller;

import eu.ibagroup.snippet_manager.dto.SnippetRequestTO;
import eu.ibagroup.snippet_manager.dto.SnippetResponseTO;
import eu.ibagroup.snippet_manager.dto.SnippetUpdateTO;
import eu.ibagroup.snippet_manager.entity.Snippet;
import eu.ibagroup.snippet_manager.mapper.SnippetMapper;
import eu.ibagroup.snippet_manager.service.SnippetService;
import eu.ibagroup.snippet_manager.specification.criteria.SnippetSearchCriteria;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1/snippets")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.HEAD})
public class SnippetController {

    private SnippetService snippetService;

    private SnippetMapper snippetMapper;

    @Autowired
    public SnippetController(final SnippetService service, final SnippetMapper mapper) {
        this.snippetService = service;
        this.snippetMapper = mapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SnippetResponseTO> getSnippetById(@PathVariable UUID id) {
        Snippet snippet = snippetService.findSnippetById(id);
        return new ResponseEntity<>(snippetMapper.toSnippetResponse(snippet), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<SnippetResponseTO>> getSnippets(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                               @RequestParam(defaultValue = "15") Integer size,
                                                               @ModelAttribute SnippetSearchCriteria criteria) {
        return new ResponseEntity<>(snippetService.getAllSnippets(pageNumber, size, criteria), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SnippetResponseTO> createSnippet(@Valid @RequestBody SnippetRequestTO snippetRequestTO) {
        Snippet savedSnippet = snippetService.createSnippet(snippetRequestTO);
        return new ResponseEntity<>(snippetMapper.toSnippetResponse(savedSnippet), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SnippetResponseTO> updateSnippet(@PathVariable UUID id, @Valid @RequestBody SnippetUpdateTO snippetUpdateTO) {
        Snippet updatedSnippet = snippetService.updateSnippet(id, snippetUpdateTO);
        return new ResponseEntity<>(snippetMapper.toSnippetResponse(updatedSnippet), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSnippet(@PathVariable UUID id) {
        snippetService.deleteSnippet(id);
        return ResponseEntity.noContent().build();
    }
}
