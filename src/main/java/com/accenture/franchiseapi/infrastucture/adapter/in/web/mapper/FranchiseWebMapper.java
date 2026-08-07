package com.accenture.franchiseapi.infrastucture.adapter.in.web.mapper;

import com.accenture.franchiseapi.application.command.franchise.CreateFranchiseCommand;
import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.Franchise;
import com.accenture.franchiseapi.domain.model.Product;
import com.accenture.franchiseapi.infrastucture.adapter.in.web.dto.request.CreateFranchiseRequest;
import com.accenture.franchiseapi.infrastucture.adapter.in.web.dto.response.BranchResponse;
import com.accenture.franchiseapi.infrastucture.adapter.in.web.dto.response.FranchiseResponse;
import com.accenture.franchiseapi.infrastucture.adapter.in.web.dto.response.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FranchiseWebMapper {

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

    private BranchResponse toResponse(Branch branch) {
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
