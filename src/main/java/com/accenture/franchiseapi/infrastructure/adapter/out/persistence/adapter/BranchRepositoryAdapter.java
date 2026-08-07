package com.accenture.franchiseapi.infrastructure.adapter.out.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.accenture.franchiseapi.application.port.out.BranchRepositoryPort;
import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.valueobject.BranchId;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.entity.BranchEntity;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.mapper.BranchMapper;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.mapper.ProductMapper;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.r2dbc.BranchR2dbcRepository;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.r2dbc.ProductR2dbcRepository;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class BranchRepositoryAdapter implements BranchRepositoryPort {

    private final BranchR2dbcRepository branchRepository;
    private final ProductR2dbcRepository productRepository;
    private final BranchMapper branchMapper;
    private final ProductMapper productMapper;

    @Override
    public Mono<Branch> save(Branch branch, FranchiseId franchiseId) {
        BranchEntity entity = branchMapper.toNewEntity(branch, franchiseId);
        return branchRepository.save(entity)
                .map(saved -> branchMapper.toDomain(saved, List.of()));
    }

    @Override
    public Mono<Branch> findById(BranchId id) {
        return branchRepository.findById(id.value())
                .flatMap(this::assembleBranch);
    }

    @Override
    public Flux<Branch> findByFranchiseId(FranchiseId franchiseId) {
        return branchRepository.findByFranchiseId(franchiseId.value())
                .flatMap(this::assembleBranch);
    }

    @Override
    public Mono<Boolean> existsbyId(BranchId id) {
        return branchRepository.existsById(id.value());
    }

    private Mono<Branch> assembleBranch(BranchEntity entity) {
        return productRepository.findByBranchId(entity.getId())
                .map(productMapper::toDomain)
                .collectList()
                .map(products -> branchMapper.toDomain(entity, products));
    }
}
