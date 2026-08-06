package com.accenture.franchiseapi.application.port.out;

import com.accenture.franchiseapi.domain.model.Product;
import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import com.accenture.franchiseapi.domain.model.valueobject.ProductId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepositoryPort {
    Mono<Product> save(Product product, BranchId branchId);
    Mono<Product> findById(ProductId id);
    Flux<Product> findByBranchId(BranchId branchId);
    Mono<Void> deleteById(ProductId id);
}
