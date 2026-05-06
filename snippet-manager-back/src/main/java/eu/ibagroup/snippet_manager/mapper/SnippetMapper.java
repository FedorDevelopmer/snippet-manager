package eu.ibagroup.snippet_manager.mapper;

import eu.ibagroup.snippet_manager.dto.SnippetRequestTO;
import eu.ibagroup.snippet_manager.dto.SnippetResponseTO;
import eu.ibagroup.snippet_manager.entity.Snippet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = LanguageMapper.class)
public interface SnippetMapper {

    @Mapping(target = "language", source = "language", ignore = true)
    Snippet toSnippet(SnippetRequestTO request);

    @Mapping(target = "language", source = "language", qualifiedByName = "languageConverterFromInstance")
    SnippetResponseTO toSnippetResponse(Snippet snippet);

    @Mapping(target = "language", source = "language", qualifiedByName = "languageConverterForResponse")
    SnippetResponseTO toSnippetResponse(SnippetRequestTO snippetRequestTO);


}
