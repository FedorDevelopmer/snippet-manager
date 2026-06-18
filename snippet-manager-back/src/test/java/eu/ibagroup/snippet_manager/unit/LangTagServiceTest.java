package eu.ibagroup.snippet_manager.unit;


import eu.ibagroup.snippet_manager.dto.LangTagRequestTO;
import eu.ibagroup.snippet_manager.dto.LangTagUpdateTO;
import eu.ibagroup.snippet_manager.entity.LangTag;
import eu.ibagroup.snippet_manager.enumeration.DevLang;
import eu.ibagroup.snippet_manager.exception.LangTagNotFoundException;
import eu.ibagroup.snippet_manager.repository.LangTagRepository;
import eu.ibagroup.snippet_manager.service.LangTagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LangTagServiceTest {

    @Mock
    private LangTagRepository langTagRepository;

    @InjectMocks
    private LangTagService langTagService;

    @Test
    void whenLangTagDataIsReady_ThenSaveLangTagEntity(){

        UUID id = UUID.randomUUID();
        LangTag langTag = new LangTag(id, DevLang.JAVA, "#111111");

        LangTagRequestTO langTagRequestTO = new LangTagRequestTO();
        langTagRequestTO.setColor("#111111");
        langTagRequestTO.setLanguage("java");

        when(langTagRepository.save(any())).thenReturn(langTag);

        LangTag savedLangTag = langTagService.createLangTag(langTagRequestTO);

        assertEquals(id, savedLangTag.getId());
        assertEquals(langTag.getLanguage(), savedLangTag.getLanguage());
        assertEquals(langTag.getColor(), savedLangTag.getColor());
    }

    @Test
    void givenLangTagUpdatedColor_whenColorIsValid_ThenUpdateLangTagEntity(){

        UUID id = UUID.randomUUID();
        LangTag langTag = new LangTag(UUID.randomUUID(), DevLang.JAVA, "#000000");
        LangTagUpdateTO updateTO = new LangTagUpdateTO();
        updateTO.setColor("#222222");

        when(langTagRepository.findById(any())).thenReturn(Optional.of(langTag));

        langTagService.updateLangTag(id, updateTO);

        assertEquals(updateTO.getColor(), langTag.getColor());
        assertEquals(DevLang.JAVA, langTag.getLanguage());
        verify(langTagRepository).save(langTag);

    }

    @Test
    void givenLangTagId_whenUpdatingNonExisting_ThenThrowException() {
        UUID id = UUID.randomUUID();

        when(langTagRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(LangTagNotFoundException.class,
                () -> langTagService.updateLangTag(id, new LangTagUpdateTO()));
    }

    @Test
    void givenLangTagId_whenLangTagExists_ThenReturnSavedLangTagEntity(){

        UUID id = UUID.randomUUID();
        LangTagRequestTO langTagRequestTO = new LangTagRequestTO();
        LangTag langTag = new LangTag(id, DevLang.JAVA, "#111111");
        langTagRequestTO.setColor(langTag.getColor());
        langTagRequestTO.setLanguage(langTag.getLanguage().getLanguageName());

        when(langTagRepository.findById(any())).thenReturn(Optional.of(langTag));

        LangTag saved = langTagService.findLangTagById(id);

        assertEquals(id, saved.getId());
        assertEquals(langTag.getLanguage(), saved.getLanguage());
        assertEquals(langTag.getColor(), saved.getColor());
    }

    @Test
    void givenLangTagId_whenLangTagDoesNotExists_ThenThrowNotFoundException() {

        UUID id = UUID.randomUUID();

        when(langTagRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(LangTagNotFoundException.class, () -> langTagService.findLangTagById(id));
    }

    @Test
    void givenLangTagId_whenLangTagExists_ThenDeleteLangTagEntity(){

        UUID id = UUID.randomUUID();

        when(langTagRepository.existsById(id)).thenReturn(true);

        langTagService.deleteLangTag(id);

        verify(langTagRepository).deleteById(id);
    }

    @Test
    void givenLangTagId_whenDeleteNonExisting_ThenThrowException() {
        UUID id = UUID.randomUUID();

        when(langTagRepository.existsById(id)).thenReturn(false);

        assertThrows(LangTagNotFoundException.class,
                () -> langTagService.deleteLangTag(id));
    }
}
