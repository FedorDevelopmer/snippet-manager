package eu.ibagroup.snippet_manager.mapper;


import ch.qos.logback.core.model.ComponentModel;
import eu.ibagroup.snippet_manager.dto.LangTagRequestTO;
import eu.ibagroup.snippet_manager.dto.LangTagResponseTO;
import eu.ibagroup.snippet_manager.entity.LangTag;
import eu.ibagroup.snippet_manager.enumeration.DevLang;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
public interface LangTagMapper {

    @Mapping(target = "language", qualifiedByName = "languageConverterForRequest")
    LangTag toLangTag(LangTagRequestTO requestTO);

    @Mapping(target = "language", qualifiedByName = "languageConverterForResponse")
    LangTagResponseTO toLangTagResponse(LangTag langTag);

    LangTagResponseTO toLangTagResponse(LangTagRequestTO langTagRequestTO);

    @Named(value = "languageConverterForResponse")
    default String convertLanguageForResponse(String source) {
        return DevLang.valueOf(source).getLanguageName();
    }

    @Named(value = "languageConverterForRequest")
    default DevLang convertLanguageForRequest(String source) {
        return DevLang.fromLanguage(source);
    }
}
