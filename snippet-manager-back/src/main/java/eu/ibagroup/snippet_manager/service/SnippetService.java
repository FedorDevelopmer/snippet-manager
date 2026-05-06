package eu.ibagroup.snippet_manager.service;

import eu.ibagroup.snippet_manager.dto.SnippetRequestTO;
import eu.ibagroup.snippet_manager.dto.SnippetResponseTO;
import eu.ibagroup.snippet_manager.dto.SnippetUpdateTO;
import eu.ibagroup.snippet_manager.entity.LangTag;
import eu.ibagroup.snippet_manager.entity.Snippet;
import eu.ibagroup.snippet_manager.enumeration.DevLang;
import eu.ibagroup.snippet_manager.exception.DuplicateSnippetException;
import eu.ibagroup.snippet_manager.exception.LangTagNotFoundException;
import eu.ibagroup.snippet_manager.exception.SnippetNotFoundException;
import eu.ibagroup.snippet_manager.mapper.SnippetMapper;
import eu.ibagroup.snippet_manager.repository.SnippetRepository;
import eu.ibagroup.snippet_manager.specification.SnippetSpecification;
import eu.ibagroup.snippet_manager.specification.criteria.SnippetSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SnippetService {

    private final SnippetRepository snippetRepository;

    private final LangTagService langTagService;

    private final SnippetMapper snippetMapper;

    @Autowired
    public SnippetService(final SnippetRepository repository, final LangTagService langTagService, final SnippetMapper snippetMapper) {
        this.snippetRepository = repository;
        this.langTagService = langTagService;
        this.snippetMapper = snippetMapper;
    }

    @Transactional
    public Page<SnippetResponseTO> getAllSnippets(int pageNumber, int pageSize, SnippetSearchCriteria criteria) {
        Page<Snippet> snippets;
        if(criteria.isEmpty()){
            snippets = snippetRepository.findAll(PageRequest.of(pageNumber, pageSize));
        } else {
            SnippetSpecification spec = new SnippetSpecification(criteria);
            snippets = snippetRepository.findAll(Specification.where(spec), PageRequest.of(pageNumber, pageSize));
        }
        return snippets.map(snippetMapper::toSnippetResponse);
    }

    @Transactional
    public Snippet createSnippet(SnippetRequestTO snippetRequestTO) {

        //Check if title if unique
        if(snippetRepository.existsByTitle(snippetRequestTO.getTitle())) {
            throw new DuplicateSnippetException();
        }

        //Search for existing lang tag
        DevLang devLang = DevLang.fromLanguage(snippetRequestTO.getLanguage());
        LangTag langTag = langTagService.findLangTagByLanguage(devLang);

        //Saving
        Snippet snippet = snippetMapper.toSnippet(snippetRequestTO);
        snippet.setLanguage(langTag);
        snippet.setCreationDate(LocalDateTime.now());
        return snippetRepository.save(snippet);
    }

    @Transactional
    public Snippet findSnippetById(UUID id) {
        return snippetRepository.findById(id)
                .orElseThrow(() -> new SnippetNotFoundException("Snippet with id " + id + " not found"));
    }

    @Transactional
    public Snippet updateSnippet(UUID id, SnippetUpdateTO snippetUpdateTO) {

        //Step 1 - Obtaining snippet, it should exist for update
        Snippet existingSnippet = snippetRepository.findById(id)
                .orElseThrow(()-> new SnippetNotFoundException("Snippet with id " + id + " not found"));

        //Step 2 - Updating snippet fields
        Snippet updatedSnippet = performSnippetUpdate(existingSnippet, snippetUpdateTO);

        return snippetRepository.save(updatedSnippet);
    }

    @Transactional
    public void deleteSnippet(UUID id) {
        if (!snippetRepository.existsById(id)) {
            throw new SnippetNotFoundException("Snippet with id " + id + " not found");
        }
        snippetRepository.deleteById(id);
    }


    private Snippet performSnippetUpdate(Snippet existingSnippet, SnippetUpdateTO snippetUpdateTO) {
        if (snippetUpdateTO.getTitle() != null) {
            existingSnippet.setTitle(snippetUpdateTO.getTitle());
        }
        if (snippetUpdateTO.getLanguage() != null) {
            DevLang devLang = DevLang.valueOf(snippetUpdateTO.getLanguage().toUpperCase());
            existingSnippet.setLanguage(langTagService.findLangTagByLanguage(devLang));
        }
        if (snippetUpdateTO.getCode() != null) {
            existingSnippet.setCode(snippetUpdateTO.getCode());
        }
        return existingSnippet;
    }

}
