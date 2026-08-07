package com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response;

import java.util.List;
import java.util.UUID;

public record FranchiseResponse(
        UUID id,
        String name,
        List<BranchResponse> branches
) {
}
