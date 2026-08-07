package com.accenture.franchiseapi.infrastructure.adapter.in.web.mapper;

import com.accenture.franchiseapi.application.command.branch.AddBranchCommand;
import com.accenture.franchiseapi.application.command.franchise.CreateFranchiseCommand;
import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.Franchise;
import com.accenture.franchiseapi.domain.model.Product;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.AddBranchRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.request.CreateFranchiseRequest;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response.BranchResponse;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response.FranchiseResponse;
import com.accenture.franchiseapi.infrastructure.adapter.in.web.dto.response.ProductResponse;
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

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId().value(),
                product.getName(),
                product.getStock()
        );
    }
}
