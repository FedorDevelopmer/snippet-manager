package eu.ibagroup.snippet_manager.exception;

public class DuplicateLangTagException extends RuntimeException {

    public DuplicateLangTagException(String message) {
        super(message);
    }
    public DuplicateLangTagException() {
        super("Creation of tag with the same programming language is prohibited");
    }
}
