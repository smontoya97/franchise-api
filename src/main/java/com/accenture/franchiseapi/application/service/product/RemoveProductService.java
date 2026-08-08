package com.accenture.franchiseapi.application.service.product;

import com.accenture.franchiseapi.application.command.product.RemoveProductCommand;
import com.accenture.franchiseapi.application.port.in.product.RemoveProductUseCase;
import com.accenture.franchiseapi.application.port.out.ProductRepositoryPort;
import com.accenture.franchiseapi.domain.exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class RemoveProductService implements RemoveProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public Mono<Void> execute(RemoveProductCommand command) {
        return productRepositoryPort.existsByIdAndBranchId(command.productId(), command.branchId())
                .flatMap(exists -> exists
                        ? productRepositoryPort.deleteById(command.productId())
                        : Mono.error(new ProductNotFoundException(command.productId())));
    }
}
