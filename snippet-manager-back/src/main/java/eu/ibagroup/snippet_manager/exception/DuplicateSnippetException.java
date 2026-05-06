package eu.ibagroup.snippet_manager.exception;

public class DuplicateSnippetException extends RuntimeException {

    public DuplicateSnippetException(String message) {
        super(message);
    }
    public DuplicateSnippetException() {
        super("Snippets with the same title are prohibited");
    }
}
