package dev.learning;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.OptimisticLockingFailureException;
@RestControllerAdvice
public class ApiErrors {
    @ExceptionHandler(WebExchangeBindException.class)
    ResponseEntity<Map<String,String>> validation(WebExchangeBindException error) {
        return ResponseEntity.badRequest().body(Map.of("error", "check product, quantity, and status"));
    }
    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<Map<String,String>> status(ResponseStatusException error) {
        return ResponseEntity.status(error.getStatusCode()).body(Map.of("error", Objects.requireNonNullElse(error.getReason(), "request failed")));
    }
    @ExceptionHandler(OptimisticLockingFailureException.class)
    ResponseEntity<Map<String,String>> conflict(OptimisticLockingFailureException error) {
        return ResponseEntity.status(409).body(Map.of("error", "order changed, read it again before updating"));
    }
}
