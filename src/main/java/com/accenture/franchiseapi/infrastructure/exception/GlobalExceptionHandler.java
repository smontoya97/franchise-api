package com.accenture.franchiseapi.infrastructure.exception;

import com.accenture.franchiseapi.domain.exception.BranchNotFoundException;
import com.accenture.franchiseapi.domain.exception.DomainException;
import com.accenture.franchiseapi.domain.exception.FranchiseNotFoundException;
import com.accenture.franchiseapi.domain.exception.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({FranchiseNotFoundException.class, BranchNotFoundException.class, ProductNotFoundException.class})
    public Mono<ResponseEntity<ApiErrorReponse>> notFoundException(DomainException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return response(HttpStatus.NOT_FOUND, ex.getClass().getSimpleName(), ex.getMessage());
    }

    @ExceptionHandler(DomainException.class)
    public Mono<ResponseEntity<ApiErrorReponse>> domainException(DomainException ex) {
        log.warn("Domain rule violated: {}", ex.getMessage());
        return response(HttpStatus.BAD_REQUEST, ex.getClass().getSimpleName(), ex.getMessage());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ApiErrorReponse>> validationException(WebExchangeBindException ex) {
        String errorName = "ValidationError";
        String errorMessage = "Request validation failed";
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .toList();
        log.warn("Validation failed: {}", details);
        ApiErrorReponse body = ApiErrorReponse.of(
                HttpStatus.BAD_REQUEST.value(),
                errorName,
                errorMessage,
                details
        );
        return Mono.just(ResponseEntity.badRequest().body(body));
    }

    @ExceptionHandler({ServerWebInputException.class, IllegalArgumentException.class})
    public Mono<ResponseEntity<ApiErrorReponse>> serverWebInputException(ServerWebInputException ex) {
        log.warn("Malformed request: {}", ex.getMessage());
        String errorName = "InvalidRequest";
        String errorMessage = "Malformed request or invalid parameter type";
        return response(HttpStatus.BAD_REQUEST, errorName, errorMessage);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Mono<ResponseEntity<ApiErrorReponse>> noResourceFound(NoResourceFoundException ex) {
        log.warn("No resource found for path: {}", ex.getReason());
        String errorName = "NotFound";
        String errorMessage = "The requested resource does not exist";
        return response(HttpStatus.NOT_FOUND, errorName, errorMessage);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<ApiErrorReponse>> responseStatusException(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        String errorName = status.getReasonPhrase().replace(" ", "");
        String errorMessage = ex.getReason() != null ? ex.getReason() : status.getReasonPhrase();

        if (status.is5xxServerError()) {
            log.error("Unhandled server-side ResponseStatusException: {}", errorMessage, ex);
        } else {
            log.warn("ResponseStatusException [{}]: {}", status.value(), errorMessage);
        }

        return response(status, errorName, errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiErrorReponse>> unexpectedException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        String errorName = "InternalServerError";
        String errorMessage = "An unexpected error occurred";
        return response(HttpStatus.INTERNAL_SERVER_ERROR, errorName, errorMessage);
    }

    private String formatFieldError(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }

    private Mono<ResponseEntity<ApiErrorReponse>> response(HttpStatus status, String error, String message) {
        return Mono.just(ResponseEntity.status(status).body(ApiErrorReponse.of(status.value(), error, message)));
    }
}
