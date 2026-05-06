package eu.ibagroup.snippet_manager.mapper;

import eu.ibagroup.snippet_manager.entity.LangTag;
import eu.ibagroup.snippet_manager.enumeration.DevLang;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class LanguageMapper {

    @Named(value = "languageConverterForResponse")
    public String convertLanguageForResponse(String source) {
        return DevLang.valueOf(source.toUpperCase()).getLanguageName();
    }

    @Named(value = "languageConverterFromInstance")
    public String convertLanguageFromInstance(LangTag source) {
        return source.getLanguage().getLanguageName();
    }
}
