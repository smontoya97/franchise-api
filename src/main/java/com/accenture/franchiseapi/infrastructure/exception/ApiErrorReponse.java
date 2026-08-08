package com.accenture.franchiseapi.infrastructure.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorReponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp,
        List<String> details
) {
    public static ApiErrorReponse of(int status, String error, String message) {
        return new ApiErrorReponse(status, error, message, LocalDateTime.now(ZoneId.of("America/Bogota")), null);
    }

    public static ApiErrorReponse of(int status, String error, String message, List<String> details) {
        return new ApiErrorReponse(status, error, message, LocalDateTime.now(ZoneId.of("America/Bogota")), details);
    }
}
