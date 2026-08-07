package com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateFranchiseRequest(
        @NotBlank(message = "Franchise name is required")
        @Size(min = 3, max = 150, message = "Franchise name must be at least 3 characters and must not exceed 150")
        String name
) {
}
