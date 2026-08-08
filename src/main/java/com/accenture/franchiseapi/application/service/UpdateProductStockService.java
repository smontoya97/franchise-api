package com.accenture.franchiseapi.application.service;

import com.accenture.franchiseapi.application.command.product.UpdateProductStockCommand;
import com.accenture.franchiseapi.application.port.in.product.UpdateProductStockUseCase;
import com.accenture.franchiseapi.application.port.out.ProductRepositoryPort;
import com.accenture.franchiseapi.domain.exception.ProductNotFoundException;
import com.accenture.franchiseapi.domain.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UpdateProductStockService implements UpdateProductStockUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public Mono<Product> execute(UpdateProductStockCommand command) {
        return productRepositoryPort.existsByIdAndBranchId(command.productId(), command.branchId())
                .flatMap(exists -> exists
                    ? productRepositoryPort.findById(command.productId())
                    : Mono.error(new ProductNotFoundException(command.productId())))
                .map(product -> {
                    product.updateStock(command.newStock());
                    return product;
                })
                .flatMap(productRepositoryPort::update);
    }
}
