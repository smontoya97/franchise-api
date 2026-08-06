package com.accenture.franchiseapi.application.port.in.product;

import com.accenture.franchiseapi.application.command.product.RenameProductCommand;
import com.accenture.franchiseapi.domain.model.Product;
import reactor.core.publisher.Mono;

public interface RenameProductUseCase {
    Mono<Product> execute(RenameProductCommand command);
}
