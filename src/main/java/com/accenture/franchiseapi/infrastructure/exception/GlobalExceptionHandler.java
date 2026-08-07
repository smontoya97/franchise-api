package com.accenture.franchiseapi.infrastructure.exception;

import com.accenture.franchiseapi.domain.exception.BranchNotFoundException;
import com.accenture.franchiseapi.domain.exception.DomainException;
import com.accenture.franchiseapi.domain.exception.FranchiseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({FranchiseNotFoundException.class, BranchNotFoundException.class})
    public Mono<ResponseEntity<ApiErrorReponse>> notFoundException(DomainException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody(HttpStatus.NOT_FOUND, ex)));
    }

    @ExceptionHandler(DomainException.class)
    public Mono<ResponseEntity<ApiErrorReponse>> domainException(DomainException ex) {
        return Mono.just(ResponseEntity.badRequest().body(errorBody(HttpStatus.BAD_REQUEST, ex)));
    }

    private ApiErrorReponse errorBody(HttpStatus status, DomainException ex) {
        return new ApiErrorReponse(
                status.value(),
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
    }
}
