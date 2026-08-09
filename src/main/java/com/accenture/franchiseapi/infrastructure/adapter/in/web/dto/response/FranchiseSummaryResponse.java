package com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response;

import java.util.UUID;

public record FranchiseSummaryResponse(
        UUID franchiseId,
        String name
) {
}
