package com.accenture.franchiseapi.infrastructure.persistence;

import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.adapter.BranchRepositoryAdapter;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.adapter.FranchiseRepositoryAdapter;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.adapter.ProductRepositoryAdapter;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.mapper.BranchMapper;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.mapper.FranchiseMapper;
import com.accenture.franchiseapi.infrastructure.adapter.out.persistence.mapper.ProductMapper;
import com.accenture.franchiseapi.infrastructure.config.R2dbcConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.r2dbc.test.autoconfigure.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

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
public abstract class AbstractPersistenceTest {

    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("franchisedb")
            .withUsername("test")
            .withPassword("test");

    static {
        mysql.start();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () ->
                "r2dbc:mysql://" + mysql.getHost() + ":" + mysql.getFirstMappedPort() + "/" + mysql.getDatabaseName());
        registry.add("spring.r2dbc.username", mysql::getUsername);
        registry.add("spring.r2dbc.password", mysql::getPassword);
    }

    @Autowired
    protected DatabaseClient databaseClient;
    @Autowired
    protected FranchiseRepositoryAdapter franchiseRepositoryAdapter;
    @Autowired
    protected BranchRepositoryAdapter branchRepositoryAdapter;
    @Autowired
    protected ProductRepositoryAdapter productRepositoryAdapter;

    @BeforeEach
    void createSchema() {
        String ddl = """
        create table if not exists franchises (
            id char(36) primary key,
            name varchar(150) not null,
            constraint uk_franchise_name unique (name)
        );

        create table if not exists branches (
            id char(36) primary key,
            franchise_id char(36) not null,
            name varchar(150) not null,
            constraint fk_branch_franchise
                foreign key (franchise_id) references franchises(id),
            constraint uk_branch_name_per_franchise
                unique (franchise_id, name)
        );

        create table if not exists products (
            id char(36) primary key,
            branch_id char(36) not null,
            name varchar(150) not null,
            stock int not null default 0,
            constraint fk_product_branch
                foreign key (branch_id) references branches(id),
            constraint uk_product_name_per_branch
                unique (branch_id, name)
        );
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
}
