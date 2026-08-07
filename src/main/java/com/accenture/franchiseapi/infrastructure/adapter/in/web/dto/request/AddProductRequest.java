package com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddProductRequest(
        @NotBlank(message = "Product name is required")
        @Size(min = 3, max = 150, message = "Product name must be at least 3 characters and must not exceed 150")
        String name,
        @Min(value = 0, message = "Initial stock must not be negative")
        @JsonProperty("initial_stock")
        int initialStock
) {
}
