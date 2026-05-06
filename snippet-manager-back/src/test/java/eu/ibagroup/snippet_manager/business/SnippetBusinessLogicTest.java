package eu.ibagroup.snippet_manager.business;

import eu.ibagroup.snippet_manager.dto.SnippetRequestTO;
import eu.ibagroup.snippet_manager.dto.SnippetUpdateTO;
import eu.ibagroup.snippet_manager.entity.LangTag;
import eu.ibagroup.snippet_manager.entity.Snippet;
import eu.ibagroup.snippet_manager.exception.DuplicateSnippetException;
import eu.ibagroup.snippet_manager.exception.SnippetNotFoundException;
import eu.ibagroup.snippet_manager.mapper.LangTagMapper;
import eu.ibagroup.snippet_manager.mapper.SnippetMapper;
import eu.ibagroup.snippet_manager.repository.LangTagRepository;
import eu.ibagroup.snippet_manager.repository.SnippetRepository;
import eu.ibagroup.snippet_manager.service.LangTagService;
import eu.ibagroup.snippet_manager.service.SnippetService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class SnippetBusinessLogicTest {

    @Mock
    private LangTagService langTagService;

    @Mock
    private LangTagRepository langTagRepository;

    @Mock
    private LangTagMapper langTagMapper;

    @Mock
    private SnippetRepository snippetRepository;

    @Mock
    private SnippetMapper snippetMapper;

    @InjectMocks
    private SnippetService snippetService;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    //exception when updating non-existing snippet

    @Test
    void whenUpdatingNonExistingSnippet_ThenThrowException() {
        Mockito.when(snippetRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                SnippetNotFoundException.class,
                () -> snippetService.updateSnippet(UUID.randomUUID(),
                        new SnippetUpdateTO("code", "title", "JAVA", LocalDateTime.now()))
        );

        Mockito.verify(snippetRepository).findById(any(UUID.class));
        Mockito.verify(snippetRepository, Mockito.never()).save(any());
    }

    @Test
    void whenValidUpdate_ThenSnippetUpdated() {
        UUID id = UUID.randomUUID();

        Snippet existing = new Snippet(UUID.randomUUID(), "old", "oldCode", LocalDateTime.now(), new LangTag());

        Mockito.when(snippetRepository.findById(id))
                .thenReturn(Optional.of(existing));

        snippetService.updateSnippet(id,
                new SnippetUpdateTO("code", "title", "Java", LocalDateTime.now()));

        Mockito.verify(snippetRepository).save(existing);
    }

    @Test
    void whenDeletingNonExistingSnippet_ThenThrowException() {
        Mockito.when(snippetRepository.existsById(any(UUID.class)))
                .thenReturn(false);

        Assertions.assertThrows(
                SnippetNotFoundException.class,
                () -> snippetService.deleteSnippet(UUID.randomUUID())
        );

        Mockito.verify(snippetRepository).existsById(any(UUID.class));
    }

    //check that snippet title is unique
    @Test
    void whenSnippetWithSameTitleExists_ThenThrowException() {
        Mockito.when(snippetRepository.existsByTitle(any()))
                .thenReturn(true);
        Assertions.assertThrows(
                DuplicateSnippetException.class,
                () -> snippetService.createSnippet(
                        new SnippetRequestTO(UUID.randomUUID(), "title", "code", "JAVA", LocalDateTime.now())
                )
        );

        Mockito.verify(snippetRepository).existsByTitle(any());
        Mockito.verify(snippetRepository, Mockito.never()).save(any());
    }

    @Test
    void whenValidSnippetCreate_ThenSavedSuccessfully() {
        Mockito.when(snippetMapper.toSnippet(any(SnippetRequestTO.class))).thenReturn(new Snippet());

        snippetService.createSnippet(
                new SnippetRequestTO(UUID.randomUUID(), "title", "code", "JAVA", LocalDateTime.now())
        );

        Mockito.verify(snippetRepository).save(any(Snippet.class));
    }

    //check snippets constraints

    @Test
    void whenSnippetIsEmpty_ThenValidationFails() {
        SnippetRequestTO request = new SnippetRequestTO(UUID.randomUUID(), "", "", "JAVA", LocalDateTime.now());

        Set<ConstraintViolation<SnippetRequestTO>> violations = validator.validate(request);

        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void whenCodeTooLong_ThenValidationFails() {
        String longCode = "a".repeat(10001);

        SnippetRequestTO request = new SnippetRequestTO(UUID.randomUUID(), "title", longCode, "JAVA", LocalDateTime.now());

        Set<ConstraintViolation<SnippetRequestTO>> violations = validator.validate(request);

        Assertions.assertFalse(violations.isEmpty());
    }
}