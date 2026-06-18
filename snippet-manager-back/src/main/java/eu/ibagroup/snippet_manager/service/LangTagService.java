package eu.ibagroup.snippet_manager.service;

import eu.ibagroup.snippet_manager.dto.LangTagRequestTO;
import eu.ibagroup.snippet_manager.dto.LangTagResponseTO;
import eu.ibagroup.snippet_manager.dto.LangTagUpdateTO;
import eu.ibagroup.snippet_manager.entity.LangTag;
import eu.ibagroup.snippet_manager.enumeration.DevLang;
import eu.ibagroup.snippet_manager.exception.DuplicateLangTagException;
import eu.ibagroup.snippet_manager.exception.LangTagNotFoundException;
import eu.ibagroup.snippet_manager.mapper.LangTagMapper;
import eu.ibagroup.snippet_manager.repository.LangTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class LangTagService {

    private final LangTagRepository langTagRepository;

    private final LangTagMapper langTagMapper;

    @Autowired
    public LangTagService(final LangTagRepository repository, final LangTagMapper mapper){
        this.langTagRepository = repository;
        this.langTagMapper = mapper;
    }

    @Transactional(readOnly = true)
    public Page<LangTagResponseTO> getAllLangTags(int pageNumber, int pageSize){
        Page<LangTag> langTags = langTagRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return langTags.map(langTagMapper::toLangTagResponse);
    }

    @Transactional(readOnly = true)
    public LangTag findLangTagById(UUID id){
        return langTagRepository.findById(id)
                .orElseThrow(() -> new LangTagNotFoundException("Language tag with id " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public LangTag findLangTagByLanguage(DevLang devLang){
        return langTagRepository.findByLanguage(devLang)
                .orElseThrow(() -> new LangTagNotFoundException("Language tag with language " + devLang.getLanguageName() + " not found"));
    }

    @Transactional
    public LangTag createLangTag(LangTagRequestTO langTagRequestTO){
        //Step 1 - Obtaining lang tag, it should exist
        Optional<LangTag> existingLangTag = langTagRepository.findByLanguage(DevLang.fromLanguage(langTagRequestTO.getLanguage()));
        if(existingLangTag.isPresent()){
            throw new DuplicateLangTagException();
        }
        //Step 2 - Save only if tag with programming language is unique
        return langTagRepository.save(langTagMapper.toLangTag(langTagRequestTO));
    }

    @Transactional
    public LangTag updateLangTag(UUID id, LangTagUpdateTO langTagUpdateTO) throws  LangTagNotFoundException {

        //Step 1 - Obtaining lang tag, it should exist for update
        LangTag existingLangTag = langTagRepository.findById(id)
                .orElseThrow(() -> new LangTagNotFoundException("Language tag with id " + id + " not found"));

        //Step 2 - Validating lang tag by id
        LangTag updatedLangTag = performLangTagUpdate(existingLangTag, langTagUpdateTO);

        return langTagRepository.save(updatedLangTag);
    }

    @Transactional
    public void deleteLangTag(UUID id){
        if(!langTagRepository.existsById(id)){
            throw new LangTagNotFoundException("Language tag with id " + id + " not found");
        }
        langTagRepository.deleteById(id);
    }

    private LangTag performLangTagUpdate(LangTag existingLangTag, LangTagUpdateTO langTagUpdateTO){
        if(langTagUpdateTO.getColor() != null){
            existingLangTag.setColor(langTagUpdateTO.getColor());
        }
        if(langTagUpdateTO.getLanguage() != null){
            DevLang devLang = DevLang.fromLanguage(langTagUpdateTO.getLanguage());
            existingLangTag.setLanguage(devLang);
        }
        return existingLangTag;
    }
}
