package eu.ibagroup.snippet_manager.exception;

public class SnippetNotFoundException extends RuntimeException {

    public SnippetNotFoundException() {
        super();
    }

    public SnippetNotFoundException(String message) {
        super(message);
    }
}
