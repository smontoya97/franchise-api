package com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record TopStockProductResponse(
        @JsonProperty("branch_id")
        UUID branchId,
        @JsonProperty("branch_name")
        String branchName,
        @JsonProperty("product_id")
        UUID productId,
        @JsonProperty("product_name")
        String productName,
        int stock
) {
}
