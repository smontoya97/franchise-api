package com.accenture.franchiseapi.infrastucture.exception;

import com.accenture.franchiseapi.domain.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public Mono<ResponseEntity<ApiErrorReponse>> domainException(DomainException ex) {
        ApiErrorReponse apiErrorReponse = new ApiErrorReponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
        return Mono.just(ResponseEntity.badRequest().body(apiErrorReponse));
    }
}
