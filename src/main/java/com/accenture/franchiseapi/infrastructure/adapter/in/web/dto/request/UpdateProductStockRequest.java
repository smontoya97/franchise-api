package com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;

public record UpdateProductStockRequest(
        @Min(value = 0, message = "Stock must noy be negative")
        @JsonProperty("new_stock")
        int newStock
) {
}
