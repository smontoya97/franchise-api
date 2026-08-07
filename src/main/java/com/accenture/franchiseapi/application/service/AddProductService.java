package com.accenture.franchiseapi.application.service;

import com.accenture.franchiseapi.application.command.product.AddProductCommand;
import com.accenture.franchiseapi.application.port.in.product.AddProductUseCase;
import com.accenture.franchiseapi.application.port.out.BranchRepositoryPort;
import com.accenture.franchiseapi.application.port.out.ProductRepositoryPort;
import com.accenture.franchiseapi.domain.exception.BranchNotFoundException;
import com.accenture.franchiseapi.domain.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AddProductService implements AddProductUseCase {

    private final BranchRepositoryPort branchRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public Mono<Product> execute(AddProductCommand command) {
        return branchRepositoryPort.existsbyId(command.branchId())
                .flatMap(exists -> exists
                        ? Mono.defer(() -> Mono.just(Product.create(command.productName(), command.initialStock())))
                        : Mono.error(new BranchNotFoundException(command.branchId())))
                .flatMap(product -> productRepositoryPort.save(product, command.branchId()));
    }
}
