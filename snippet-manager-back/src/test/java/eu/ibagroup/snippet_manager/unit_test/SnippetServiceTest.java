package eu.ibagroup.snippet_manager.unit_test;

import eu.ibagroup.snippet_manager.dto.SnippetRequestTO;
import eu.ibagroup.snippet_manager.dto.SnippetUpdateTO;
import eu.ibagroup.snippet_manager.entity.LangTag;
import eu.ibagroup.snippet_manager.entity.Snippet;
import eu.ibagroup.snippet_manager.enumeration.DevLang;
import eu.ibagroup.snippet_manager.exception.SnippetNotFoundException;
import eu.ibagroup.snippet_manager.mapper.SnippetMapper;
import eu.ibagroup.snippet_manager.repository.SnippetRepository;
import eu.ibagroup.snippet_manager.service.LangTagService;
import eu.ibagroup.snippet_manager.service.SnippetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SnippetServiceTest {

	@Mock
	private SnippetRepository snippetRepository;

	@Mock
	private LangTagService langTagService;

	@Mock
	private SnippetMapper snippetMapper;

	@InjectMocks
	private SnippetService snippetService;

	@Test
	void whenSnippetDataIsReady_ThenSaveSnippetEntity(){

		Snippet snippet = new Snippet();
		LangTag langTag = new LangTag(UUID.randomUUID(), DevLang.JAVA, "#111111");
		UUID id = UUID.randomUUID();
		snippet.setId(id);
		snippet.setTitle("Title");
		snippet.setCode("Code");
		snippet.setLanguage(langTag);
		snippet.setCreationDate(LocalDateTime.now());

		SnippetRequestTO snippetRequestTO = new SnippetRequestTO();
		snippetRequestTO.setTitle("Title");
		snippetRequestTO.setCode("Code");
		snippetRequestTO.setLanguage("Java");

		when(langTagService.findLangTagByLanguage(any())).thenReturn(langTag);
		when(snippetMapper.toSnippet(any())).thenReturn(snippet);
		when(snippetRepository.save(any())).thenReturn(snippet);

		snippetService.createSnippet(snippetRequestTO);

		assertEquals(id, snippet.getId());
		assertEquals("Title", snippet.getTitle());
		assertEquals("Code", snippet.getCode());
		assertEquals(langTag, snippet.getLanguage());
	}

	@Test
	void givenSnippetUpdatedTitle_whenTitleIsValid_ThenUpdateSnippetTitle(){
		Snippet snippet = new Snippet();
		LangTag langTag = new LangTag(UUID.randomUUID(), DevLang.JAVA, "#000000");
		UUID id = UUID.randomUUID();
		snippet.setId(id);
		snippet.setTitle("Old");
		snippet.setCode("Code");
		snippet.setLanguage(langTag);
		snippet.setCreationDate(LocalDateTime.now());
		SnippetUpdateTO updateTO = new SnippetUpdateTO();
		updateTO.setTitle("New");

		when(snippetRepository.findById(id)).thenReturn(Optional.of(snippet));
		when(snippetRepository.save(any())).thenReturn(snippet);

		snippetService.updateSnippet(id, updateTO);

		assertEquals("New", snippet.getTitle());
		assertEquals("Code", snippet.getCode());

	}

	@Test
	void givenSnippetId_whenUpdatingNonExisting_ThenThrowException() {
		UUID id = UUID.randomUUID();

		when(snippetRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(SnippetNotFoundException.class,
				() -> snippetService.updateSnippet(id, new SnippetUpdateTO()));
	}

	@Test
	void givenSnippetId_whenSnippetExists_ThenReturnSavedSnippetEntity(){

		Snippet snippet = new Snippet();
		LangTag langTag = new LangTag(UUID.randomUUID(), DevLang.JAVA, "#111111");
		UUID id = UUID.randomUUID();
		snippet.setId(id);
		snippet.setTitle("Title");
		snippet.setCode("Code");
		snippet.setLanguage(langTag);
		snippet.setCreationDate(LocalDateTime.now());

		when(snippetRepository.findById(id)).thenReturn(Optional.of(snippet));

		snippetService.findSnippetById(id);

		assertEquals(id, snippet.getId());
		assertEquals("Title", snippet.getTitle());
		assertEquals("Code", snippet.getCode());
		assertEquals(langTag, snippet.getLanguage());
	}

	@Test
	void givenSnippetId_whenSnippetDoesNotExists_ThenThrowNotFoundException(){
		UUID id = UUID.randomUUID();

		when(snippetRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(SnippetNotFoundException.class, () -> snippetService.findSnippetById(id));
	}

	@Test
	void givenSnippetId_whenSnippetExists_ThenDeleteSnippetEntity(){

		UUID id = UUID.randomUUID();

		when(snippetRepository.existsById(id)).thenReturn(true);

		snippetService.deleteSnippet(id);

		verify(snippetRepository).deleteById(id);
	}

	@Test
	void givenSnippetId_whenDeleteNonExisting_ThenThrowException() {
		UUID id = UUID.randomUUID();

		when(snippetRepository.existsById(id)).thenReturn(false);

		assertThrows(SnippetNotFoundException.class,
				() -> snippetService.deleteSnippet(id));
	}
}
