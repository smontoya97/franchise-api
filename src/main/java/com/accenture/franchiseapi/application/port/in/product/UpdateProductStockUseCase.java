package com.accenture.franchiseapi.application.port.in.product;

import com.accenture.franchiseapi.application.command.product.UpdateProductStockCommand;
import com.accenture.franchiseapi.domain.model.Product;
import reactor.core.publisher.Mono;

public interface UpdateProductStockUseCase {
    Mono<Product> execute(UpdateProductStockCommand command);
}
