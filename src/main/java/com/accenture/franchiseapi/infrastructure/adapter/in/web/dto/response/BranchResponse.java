package com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response;

import java.util.List;
import java.util.UUID;

public record BranchResponse(
        UUID id,
        String name,
        List<ProductResponse> products
) {
}
