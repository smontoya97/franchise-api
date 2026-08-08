package com.accenture.franchiseapi.infrastructure.adapter.in.web.mapper;

import com.accenture.franchiseapi.application.command.branch.AddBranchCommand;
import com.accenture.franchiseapi.application.command.franchise.CreateFranchiseCommand;
import com.accenture.franchiseapi.application.command.product.AddProductCommand;
import com.accenture.franchiseapi.application.command.product.RemoveProductCommand;
import com.accenture.franchiseapi.application.command.product.UpdateProductStockCommand;
import com.accenture.franchiseapi.application.view.TopStockProductView;
import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.Franchise;
import com.accenture.franchiseapi.domain.model.Product;
import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import com.accenture.franchiseapi.domain.model.valueobject.ProductId;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.AddBranchRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.AddProductRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.CreateFranchiseRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.UpdateProductStockRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response.BranchResponse;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response.FranchiseResponse;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response.ProductResponse;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response.TopStockProductResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class WebMapper {

    public CreateFranchiseCommand toCommand(CreateFranchiseRequest request) {
        return new CreateFranchiseCommand(request.name());
    }

    public FranchiseResponse toResponse(Franchise franchise) {
        List<BranchResponse> branches = franchise.getBranches().stream()
                .map(this::toResponse)
                .toList();
        return new FranchiseResponse(
                franchise.getId().value(),
                franchise.getName(),
                branches
        );
    }

    public AddBranchCommand toCommand(UUID franchiseId, AddBranchRequest request) {
        return new AddBranchCommand(FranchiseId.of(franchiseId), request.name());
    }

    public BranchResponse toResponse(Branch branch) {
        List<ProductResponse> products = branch.getProducts().stream()
                .map(this::toResponse)
                .toList();
        return new BranchResponse(
                branch.getId().value(),
                branch.getName(),
                products
        );
    }

    public AddProductCommand toCommand(UUID branchId, AddProductRequest request) {
        return new AddProductCommand(BranchId.of(branchId), request.name(), request.initialStock());
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId().value(),
                product.getName(),
                product.getStock()
        );
    }

    public RemoveProductCommand toRemoveCommand(UUID branchId, UUID productId) {
        return new RemoveProductCommand(BranchId.of(branchId), ProductId.of(productId));
    }

    public UpdateProductStockCommand toUpdateProductStockCommand(
            UUID branchId, UUID productId, UpdateProductStockRequest request
    ) {
        return new UpdateProductStockCommand(
                BranchId.of(branchId), ProductId.of(productId), request.newStock()
        );
    }

    public TopStockProductResponse toResponse(TopStockProductView view) {
        return new TopStockProductResponse(
                UUID.fromString(view.branchId()),
                view.branchName(),
                UUID.fromString(view.productId()),
                view.productName(),
                view.stock()
        );
    }
}
