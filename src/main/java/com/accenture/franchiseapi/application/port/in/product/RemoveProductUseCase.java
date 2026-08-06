package com.accenture.franchiseapi.application.port.in.product;

import com.accenture.franchiseapi.application.command.product.RemoveProductCommand;
import reactor.core.publisher.Mono;

public interface RemoveProductUseCase {
    Mono<Void> execute(RemoveProductCommand command);
}
