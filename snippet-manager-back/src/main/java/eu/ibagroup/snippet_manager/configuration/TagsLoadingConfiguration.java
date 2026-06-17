package eu.ibagroup.snippet_manager.configuration;

import eu.ibagroup.snippet_manager.dto.LangTagRequestTO;
import eu.ibagroup.snippet_manager.entity.LangTag;
import eu.ibagroup.snippet_manager.enumeration.DevLang;
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
            long colorInt = 0;
            for(int i = 0; i < 3; i++){
                colorInt = (colorInt << 8) + random.nextInt(0, 255);

            }
            requestTO.setColor("#" + Long.toHexString(colorInt));
            Optional<LangTag> existingLangTag = langTagRepository.findByLanguage(DevLang.fromLanguage(requestTO.getLanguage()));
            if(existingLangTag.isEmpty()){
                langTagService.createLangTag(requestTO);
            }
        }
    }


}
