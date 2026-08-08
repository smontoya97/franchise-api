drop table if exists products;
drop table if exists branches;
drop table if exists franchises;

create table franchises (
  id char(36) primary key,
  name varchar(150) not null,
  constraint uk_franchise_name unique (name)
);

create table branches (
  id CHAR(36) primary key,
  franchise_id CHAR(36) not null,
  name varchar(150) not null,
  constraint fk_branch_franchise foreign key (franchise_id) references franchises(id),
  constraint uk_branch_name_per_franchise unique (franchise_id, name)
);

create table products (
  id CHAR(36) primary key,
  branch_id CHAR(36) not null,
  name varchar(150) not null,
  stock int not null default 0,
  constraint fk_product_branch foreign key (branch_id) references branches(id),
  constraint uk_product_name_per_branch unique (branch_id, name)
);