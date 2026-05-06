package eu.ibagroup.snippet_manager.exception.handler;

import eu.ibagroup.snippet_manager.exception.LangTagNotFoundException;
import eu.ibagroup.snippet_manager.exception.SnippetNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleServerError(Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().body("Internal server error: " + e.getCause().getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(LangTagNotFoundException.class)
    public ResponseEntity<String> handleTagNotFoundException(LangTagNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(SnippetNotFoundException.class)
    public ResponseEntity<String> handleSnippetNotFoundException(SnippetNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
