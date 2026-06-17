package eu.ibagroup.snippet_manager.business;

import eu.ibagroup.snippet_manager.dto.LangTagRequestTO;
import eu.ibagroup.snippet_manager.dto.LangTagUpdateTO;
import eu.ibagroup.snippet_manager.entity.LangTag;
import eu.ibagroup.snippet_manager.enumeration.DevLang;
import eu.ibagroup.snippet_manager.exception.DuplicateLangTagException;
import eu.ibagroup.snippet_manager.exception.LangTagNotFoundException;
import eu.ibagroup.snippet_manager.mapper.LangTagMapper;
import eu.ibagroup.snippet_manager.repository.LangTagRepository;
import eu.ibagroup.snippet_manager.service.LangTagService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class LagTagBusinessLogicTest {

    @Mock
    private LangTagRepository langTagRepository;

    @Mock
    private LangTagMapper langTagMapper;

    @InjectMocks
    private LangTagService langTagService;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @BeforeEach
    void setUp() {

    }

    //non-existing tag update throws exception
    @Test
    void whenNonExistingLangTagUpdate_ThenThrowException() {
        LangTagUpdateTO langTagUpdateTO = new LangTagUpdateTO("Text", "#000000");
        Mockito.when(langTagRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(LangTagNotFoundException.class, () -> {
            langTagService.updateLangTag(UUID.randomUUID(), langTagUpdateTO);
        });
        Mockito.verify(langTagRepository).findById(any());
    }

    //check color column constraints
    @Test
    void whenColorFieldIsEmpty_ThenThrowException() {
        LangTagRequestTO langTagRequestTO = new LangTagRequestTO("Text", "");
        Set<ConstraintViolation<LangTagRequestTO>> violations = validator.validate(langTagRequestTO);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void whenColorFieldIsTooShort_ThenThrowException() {
        LangTagRequestTO langTagRequestTO = new LangTagRequestTO("Text", "#FFFF");
        Set<ConstraintViolation<LangTagRequestTO>> violations = validator.validate(langTagRequestTO);
        Assertions.assertFalse(violations.isEmpty());
    }

    //ensure tag with same language cannot be created
    @Test
    void whenTagWithLangExists_ThenSameLangTagThrowException() {
        LangTagRequestTO langTagRequestTO = new LangTagRequestTO("Text", "#111111");
        LangTag langTag = new LangTag(UUID.randomUUID(), DevLang.TEXT, "#111111");
        Mockito.when(langTagRepository.findByLanguage(any(DevLang.class))).thenReturn(Optional.of(langTag));
        Assertions.assertThrows(DuplicateLangTagException.class, () -> {
            langTagService.createLangTag(langTagRequestTO);
        });
        Mockito.verify(langTagRepository).findByLanguage(any(DevLang.class));
        Mockito.verify(langTagRepository, Mockito.times(0)).save(any(LangTag.class));
    }

    @Test
    void whenValidLangTagCreate_ThenSavedSuccessfully() {
        LangTagRequestTO request = new LangTagRequestTO("Text", "#111111");

        Mockito.when(langTagMapper.toLangTag(any())).thenReturn(new LangTag());
        Mockito.when(langTagRepository.findByLanguage(any(DevLang.class))).thenReturn(Optional.empty());
        langTagService.createLangTag(request);
        Mockito.verify(langTagRepository).save(any(LangTag.class));
    }

}
