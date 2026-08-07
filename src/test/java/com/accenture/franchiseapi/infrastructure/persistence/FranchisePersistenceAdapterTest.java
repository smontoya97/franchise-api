package com.accenture.franchiseapi.infrastructure.persistence;

import com.accenture.franchiseapi.domain.model.Branch;
import com.accenture.franchiseapi.domain.model.Franchise;
import com.accenture.franchiseapi.domain.model.Product;
import com.accenture.franchiseapi.domain.model.valueobject.FranchiseId;
import com.accenture.franchiseapi.infrastucture.adapter.out.persistence.adapter.BranchRepositoryAdapter;
import com.accenture.franchiseapi.infrastucture.adapter.out.persistence.adapter.FranchiseRepositoryAdapter;
import com.accenture.franchiseapi.infrastucture.adapter.out.persistence.adapter.ProductRepositoryAdapter;
import com.accenture.franchiseapi.infrastucture.adapter.out.persistence.mapper.BranchMapper;
import com.accenture.franchiseapi.infrastucture.adapter.out.persistence.mapper.FranchiseMapper;
import com.accenture.franchiseapi.infrastucture.adapter.out.persistence.mapper.ProductMapper;
import com.accenture.franchiseapi.infrastucture.config.R2dbcConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.r2dbc.test.autoconfigure.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DataR2dbcTest
@Import({
        R2dbcConfig.class,
        FranchiseRepositoryAdapter.class,
        BranchRepositoryAdapter.class,
        ProductRepositoryAdapter.class,
        FranchiseMapper.class,
        BranchMapper.class,
        ProductMapper.class
})
public class FranchisePersistenceAdapterTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("franchisedb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () ->
                "r2dbc:mysql://" + mysql.getHost() + ":" + mysql.getFirstMappedPort() + "/" + mysql.getDatabaseName());
        registry.add("spring.r2dbc.username", mysql::getUsername);
        registry.add("spring.r2dbc.password", mysql::getPassword);
    }

    @Autowired
    private FranchiseRepositoryAdapter franchiseRepositoryAdapter;
    @Autowired
    private BranchRepositoryAdapter branchRepositoryAdapter;
    @Autowired
    private ProductRepositoryAdapter productRepositoryAdapter;
    @Autowired
    DatabaseClient databaseClient;

    @BeforeEach
    void createSchema() {
        String ddl = """
                create table if not exists franchises (id char(36) primary key, name varchar(150) not null);
                create table if not exists branches (id char(36) primary key, franchise_id char(36) not null, name varchar(150) not null,
                    constraint fk_branch_franchise foreign key (franchise_id) references franchises(id));
                create table if not exists products (id char(36) primary key, branch_id char(36) not null, name varchar(150) not null,
                    stock int not null default 0, constraint fk_product_branch foreign key (branch_id) references branches(id));
                """;
        for (String statement : ddl.split(";")) {
            if (!statement.isBlank()) {
                databaseClient.sql(statement).fetch().rowsUpdated().block();
            }
        }
    }

    @AfterEach
    void cleanUp() {
        databaseClient.sql("delete from products").fetch().rowsUpdated().block();
        databaseClient.sql("delete from branches").fetch().rowsUpdated().block();
        databaseClient.sql("delete from franchises").fetch().rowsUpdated().block();
    }

    @Test
    void shouldSaveAndRetrieveFranchiseWithBranchesAndProducts() {
        String franchiseName = "Franquicia Medellín";
        String branchName = "Sucursal Poblado";
        String productName = "Coca-Cola";
        int productStock = 10;
        Franchise franchise = Franchise.create(franchiseName);
        Franchise saved = franchiseRepositoryAdapter.save(franchise).block();
        assert saved != null;
        FranchiseId franchiseId = saved.getId();
        Branch branch = Branch.create(branchName);
        Branch savedBranch = branchRepositoryAdapter.save(branch, franchiseId).block();
        Product product = Product.create(productName, productStock);
        assert savedBranch != null;
        productRepositoryAdapter.save(product, savedBranch.getId()).block();
        int expectedSize = 1;

        StepVerifier.create(franchiseRepositoryAdapter.findById(franchiseId))
                .assertNext(result -> {
                    assertEquals(franchiseName, result.getName());
                    assertEquals(expectedSize, result.getBranches().size());
                    Branch resultBranch = result.getBranches().getFirst();
                    assertEquals(branchName, resultBranch.getName());
                    assertEquals(expectedSize, resultBranch.getProducts().size());
                    assertEquals(productName, resultBranch.getProducts().getFirst().getName());
                    assertEquals(productStock, resultBranch.getProducts().getFirst().getStock());
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyMonoWhenFranchiseDoesNotExist() {
        StepVerifier.create(franchiseRepositoryAdapter.findById(FranchiseId.newId()))
                .verifyComplete();
    }
}
