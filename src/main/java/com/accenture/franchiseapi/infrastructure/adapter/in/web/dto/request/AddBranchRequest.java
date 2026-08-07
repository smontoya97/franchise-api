package com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddBranchRequest(
        @NotBlank(message = "Branch name is required")
        @Size(min = 3, max = 150, message = "Branch name must be at least 3 characters and must not exceed 150")
        String name
) {
}
