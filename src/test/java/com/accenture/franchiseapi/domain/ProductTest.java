package com.accenture.franchiseapi.domain;

import com.accenture.franchiseapi.domain.exception.InvalidNameException;
import com.accenture.franchiseapi.domain.exception.InvalidStockException;
import com.accenture.franchiseapi.domain.model.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTest {

    @Test
    void shouldCreateProductWithValidNameAndStock() {
        String productName = "Coca-Cola";
        int productStock = 10;

        Product product = Product.create(productName, productStock);

        assertEquals(productName, product.getName());
        assertEquals(productStock, product.getStock());
        assertNotNull(product.getId());
    }

    @Test
    void shouldRejectBlankName() {
        String productName = "";
        int productStock = 10;

        assertThrows(InvalidNameException.class, () -> Product.create(productName, productStock));
    }

    @Test
    void shouldRejectNegativeInitialStock() {
        String productName = "Coca-Cola";
        int productStock = -10;

        assertThrows(InvalidStockException.class, () -> Product.create(productName, productStock));
    }

    @Test
    void shouldUpdateStock() {
        String productName = "Coca-Cola";
        int productStock = 10;
        int newStock = 20;
        Product product = Product.create(productName, productStock);

        product.updateStock(newStock);

        assertEquals(newStock, product.getStock());
    }

    @Test
    void shouldRenameProduct() {
        String productName = "Coca-Cola";
        int productStock = 10;
        String newName = "Coca-Cola Zero";
        Product product = Product.create(productName, productStock);

        product.rename(newName);

        assertEquals(newName, product.getName());
    }
}
