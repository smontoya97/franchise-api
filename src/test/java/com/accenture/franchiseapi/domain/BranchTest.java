package com.accenture.franchiseapi.domain;

import com.accenture.franchiseapi.domain.exception.InvalidNameException;
import com.accenture.franchiseapi.domain.exception.ProductNotFoundException;
import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.Product;
import com.accenture.franchiseapi.domain.model.valueobject.ProductId;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BranchTest {

    private final String branchName = "Sucursal Poblado";

    @Test
    void shouldCreateBranchWithValidName() {
        Branch branch = Branch.create(branchName);

        assertEquals(branchName, branch.getName());
    }

    @Test
    void shouldRejectBlankName() {
        String emptyBranchName = "";

        assertThrows(InvalidNameException.class, () -> Branch.create(emptyBranchName));
    }

    @Test
    void shouldAddProductToBranch() {
        Branch branch = Branch.create(branchName);
        Product product = getProduct();
        int expectedSize = 1;

        branch.addProduct(product);

        assertEquals(expectedSize, branch.getProducts().size());
        assertEquals(product, branch.getProducts().getFirst());
    }

    @Test
    void shouldRemoveProductById() {
        Branch branch = Branch.create(branchName);
        Product product = getProduct();
        branch.addProduct(product);
        int expectedSize = 0;

        branch.removeProduct(product.getId());

        assertEquals(expectedSize, branch.getProducts().size());
    }

    @Test
    void shouldThrowWhenRemovingNonExistentProduct() {
        Branch branch = Branch.create(branchName);

        assertThrows(ProductNotFoundException.class, () -> branch.removeProduct(ProductId.newId()));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void shouldFindProductWithMostStock() {
        Branch branch = Branch.create(branchName);
        Product lowProduct = getProduct();
        Product highProduct = getProduct();
        highProduct.updateStock(highProduct.getStock() + 1);
        branch.addProduct(lowProduct);
        branch.addProduct(highProduct);

        Optional<Product> result = branch.findProductWithMostStock();

        assertEquals(highProduct, result.get());
    }

    @Test
    void shouldReturnEmptyWhenBranchHasNoProducts() {
        Branch branch = Branch.create(branchName);

        assertTrue(branch.getProducts().isEmpty());
    }

    @Test
    void getProductsShouldReturnImmutableList() {
        Branch branch = Branch.create(branchName);
        Product product = getProduct();
        branch.addProduct(product);

        assertThrows(UnsupportedOperationException.class, () -> branch.getProducts().add(getProduct()));
    }


    private Product getProduct() {
        String productName = "Coca-Cola";
        int productStock = 10;

        return Product.create(productName, productStock);
    }
}
