package com.accenture.franchiseapi.infrastructure.adapter.out.persistence.adapter;

import com.accenture.franchiseapi.application.port.out.FranchiseRepositoryPort;
import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.Franchise;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.entity.BranchEntity;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.entity.FranchiseEntity;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.mapper.BranchMapper;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.mapper.FranchiseMapper;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.mapper.ProductMapper;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.r2dbc.BranchR2dbcRepository;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.r2dbc.FranchiseR2dbcRepository;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.r2dbc.ProductR2dbcRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@AllArgsConstructor
public class FranchiseRepositoryAdapter implements FranchiseRepositoryPort {

    private final FranchiseR2dbcRepository franchiseRepository;
    private final BranchR2dbcRepository branchRepository;
    private final ProductR2dbcRepository productRepository;
    private final FranchiseMapper franchiseMapper;
    private final BranchMapper branchMapper;
    private final ProductMapper productMapper;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        FranchiseEntity entity = franchiseMapper.toNewEntity(franchise);
        return franchiseRepository.save(entity)
                .map(saved -> franchiseMapper.toDomain(saved, List.of()));
    }

    @Override
    public Mono<Franchise> findById(FranchiseId id) {
        return franchiseRepository.findById(id.value())
                .flatMap(this::assembleFranchise);
    }

    @Override
    public Mono<Boolean> existsById(FranchiseId id) {
        return franchiseRepository.existsById(id.value());
    }

    @Override
    public Mono<Franchise> update(Franchise franchise) {
        return franchiseRepository.findById(franchise.getId().value())
                .flatMap(existing -> {
                    FranchiseEntity updated = FranchiseEntity.createExisting(existing.getId(), franchise.getName());
                    return franchiseRepository.save(updated);
                })
                .map(saved -> franchiseMapper.toDomain(saved, franchise.getBranches()));
    }

    private Mono<Franchise> assembleFranchise(FranchiseEntity franchiseEntity) {
        return branchRepository.findByFranchiseId(franchiseEntity.getId())
                .flatMap(this::assembleBranch)
                .collectList()
                .map(branches -> franchiseMapper.toDomain(franchiseEntity, branches));
    }

    private Mono<Branch> assembleBranch(BranchEntity branchEntity) {
        return productRepository.findByBranchId(branchEntity.getId())
                .map(productMapper::toDomain)
                .collectList()
                .map(products -> branchMapper.toDomain(branchEntity, products));
    }
}
