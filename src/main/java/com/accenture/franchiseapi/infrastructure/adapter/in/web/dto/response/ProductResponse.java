package com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response;

import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        int stock
) {
}
