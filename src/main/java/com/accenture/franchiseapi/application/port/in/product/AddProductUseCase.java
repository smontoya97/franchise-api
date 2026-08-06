package com.accenture.franchiseapi.application.port.in.product;

import com.accenture.franchiseapi.application.command.product.AddProductCommand;
import com.accenture.franchiseapi.domain.model.Product;
import reactor.core.publisher.Mono;

public interface AddProductUseCase {
    Mono<Product> execute(AddProductCommand command);
}
