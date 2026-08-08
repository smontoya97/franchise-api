package com.accenture.franchiseapi.infrastructure.adapter.in.web.controller;

import com.accenture.franchiseapi.application.port.in.product.AddProductUseCase;
import com.accenture.franchiseapi.application.port.in.product.RemoveProductUseCase;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.AddProductRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response.ProductResponse;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.mapper.WebMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/branches/{branchId}/products")
@AllArgsConstructor
public class ProductController {

    private final AddProductUseCase addProductUseCase;
    private final RemoveProductUseCase removeProductUseCase;
    private final WebMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductResponse> addProduct(
            @PathVariable UUID branchId,
            @Valid @RequestBody AddProductRequest request
    ) {
        return addProductUseCase.execute(mapper.toCommand(branchId, request))
                .map(mapper::toResponse);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> removeProduct(
            @PathVariable UUID branchId,
            @PathVariable UUID productId
    ) {
        return removeProductUseCase.execute(mapper.toRemoveCommand(branchId, productId));
    }
}
