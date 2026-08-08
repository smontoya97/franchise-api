package com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RenameRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 150, message = "Name must be at least 3 characters and must not exceed 150")
        String name
) {
}
