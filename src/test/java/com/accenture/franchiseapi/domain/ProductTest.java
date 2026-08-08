package com.accenture.franchiseapi.domain;

import com.accenture.franchiseapi.domain.exception.InvalidNameException;
import com.accenture.franchiseapi.domain.exception.InvalidStockException;
import com.accenture.franchiseapi.domain.model.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {
    private final String productName = "Coca-Cola";
    private final int productStock = 10;

    @Test
    void shouldCreateProductWithValidNameAndStock() {
        Product product = Product.create(productName, productStock);

        assertEquals(productName, product.getName());
        assertEquals(productStock, product.getStock());
        assertNotNull(product.getId());
    }

    @Test
    void shouldRejectBlankName() {
        String emptyProductName = "";

        assertThrows(InvalidNameException.class, () -> Product.create(emptyProductName, productStock));
    }

    @Test
    void shouldRejectNegativeInitialStock() {
        int negativeProductStock = -10;

        assertThrows(InvalidStockException.class, () -> Product.create(productName, negativeProductStock));
    }

    @Test
    void shouldUpdateStock() {
        int newStock = 20;
        Product product = Product.create(productName, productStock);

        product.updateStock(newStock);

        assertEquals(newStock, product.getStock());
    }

    @Test
    void shouldRenameProduct() {
        String newName = "Coca-Cola Zero";
        Product product = Product.create(productName, productStock);

        product.rename(newName);

        assertEquals(newName, product.getName());
    }
}
