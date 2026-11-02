package dev.learning;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
@RestControllerAdvice
public class ApiErrors {
    @ExceptionHandler(WebExchangeBindException.class)
    ResponseEntity<Map<String,String>> invalid(WebExchangeBindException error) {
        return ResponseEntity.badRequest().body(Map.of("error", "product is required and quantity must be positive"));
    }
    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<Map<String,String>> missing(ResponseStatusException error) {
        return ResponseEntity.status(error.getStatusCode()).body(Map.of("error", Objects.requireNonNullElse(error.getReason(), "request failed")));
    }
}
