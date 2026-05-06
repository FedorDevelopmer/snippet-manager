package eu.ibagroup.snippet_manager.configuration;

import eu.ibagroup.snippet_manager.dto.LangTagRequestTO;
import eu.ibagroup.snippet_manager.entity.LangTag;
import eu.ibagroup.snippet_manager.enumeration.DevLang;
import eu.ibagroup.snippet_manager.exception.DuplicateLangTagException;
import eu.ibagroup.snippet_manager.repository.LangTagRepository;
import eu.ibagroup.snippet_manager.service.LangTagService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.Random;

@Configuration
public class TagsLoadingConfiguration {

    private Random random = new Random();

    private LangTagService langTagService;

    private LangTagRepository langTagRepository;

    @Autowired
    public TagsLoadingConfiguration(final LangTagService tagService, final LangTagRepository langTagRepository) {
        this.langTagService = tagService;
        this.langTagRepository = langTagRepository;
    }

    @PostConstruct
    public void loadDefaultTags() {
        for (DevLang val : DevLang.values()) {
            String name = val.getLanguageName();
            LangTagRequestTO requestTO = new LangTagRequestTO();
            requestTO.setLanguage(name);
            int colorInt = random.nextInt(100_000, 999_999);
            requestTO.setColor("#" + colorInt);
            Optional<LangTag> existingLangTag = langTagRepository.findByLanguage(DevLang.fromLanguage(requestTO.getLanguage()));
            if(existingLangTag.isEmpty()){
                langTagService.createLangTag(requestTO);
            }
        }
    }


}
