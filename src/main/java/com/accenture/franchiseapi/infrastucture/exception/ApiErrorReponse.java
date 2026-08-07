package com.accenture.franchiseapi.infrastucture.exception;

import java.time.LocalDateTime;

public record ApiErrorReponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp
) {
    public ApiErrorReponse(int status, String error, String message) {
        this(status, error, message, LocalDateTime.now());
    }
}
