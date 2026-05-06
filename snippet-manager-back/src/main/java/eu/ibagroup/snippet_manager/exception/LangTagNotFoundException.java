package eu.ibagroup.snippet_manager.exception;

public class LangTagNotFoundException extends RuntimeException {

    public LangTagNotFoundException() {
        super();
    }

    public LangTagNotFoundException(String message) {
        super(message);
    }
}
