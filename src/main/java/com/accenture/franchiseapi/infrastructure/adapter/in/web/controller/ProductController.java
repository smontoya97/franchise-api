package com.accenture.franchiseapi.infrastructure.adapter.in.web.controller;

import com.accenture.franchiseapi.application.port.in.product.AddProductUseCase;
import com.accenture.franchiseapi.application.port.in.product.RemoveProductUseCase;
import com.accenture.franchiseapi.application.port.in.product.RenameProductUseCase;
import com.accenture.franchiseapi.application.port.in.product.UpdateProductStockUseCase;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.AddProductRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.RenameRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.UpdateProductStockRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response.ProductResponse;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.mapper.WebMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final RenameProductUseCase renameProductUseCase;
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

    @PatchMapping("{productId}/stock")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductResponse> updateProductStock(
            @PathVariable UUID branchId,
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateProductStockRequest request
    ) {
        return updateProductStockUseCase.execute(
                        mapper.toUpdateProductStockCommand(branchId, productId, request)
                )
                .map(mapper::toResponse);
    }

    @PatchMapping("{productId}/name")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProductResponse> renameProduct(
            @PathVariable UUID branchId,
            @PathVariable UUID productId,
            @Valid @RequestBody RenameRequest request
    ) {
        return renameProductUseCase.execute(
                        mapper.toRenameProductCommand(branchId, productId, request)
                )
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
