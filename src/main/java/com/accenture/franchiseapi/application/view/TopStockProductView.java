package com.accenture.franchiseapi.application.view;

public record TopStockProductView(
        String branchId,
        String branchName,
        String productId,
        String productName,
        int stock
) {
}
