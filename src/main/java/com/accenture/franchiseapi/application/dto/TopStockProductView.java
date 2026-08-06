package com.accenture.franchiseapi.application.dto;

public record TopStockProductView(
        String branchId,
        String branchName,
        String productId,
        String productName,
        int stock
) {
}
