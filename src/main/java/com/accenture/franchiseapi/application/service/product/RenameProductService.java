package com.accenture.franchiseapi.application.service.product;

import com.accenture.franchiseapi.application.command.product.RenameProductCommand;
import com.accenture.franchiseapi.application.port.in.product.RenameProductUseCase;
import com.accenture.franchiseapi.application.port.out.ProductRepositoryPort;
import com.accenture.franchiseapi.domain.exception.ProductNotFoundException;
import com.accenture.franchiseapi.domain.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class RenameProductService implements RenameProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public Mono<Product> execute(RenameProductCommand command) {
        return productRepositoryPort.existsByIdAndBranchId(command.productId(), command.branchId())
                .flatMap(exists -> exists
                    ? productRepositoryPort.findById(command.productId())
                    : Mono.error(new ProductNotFoundException(command.productId())))
                .map(product -> {
                    product.rename(command.newName());
                    return product;
                })
                .flatMap(productRepositoryPort::update);
    }
}
