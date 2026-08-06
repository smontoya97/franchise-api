drop table if exists products;
drop table if exists branches;
drop table if exists franchises;

create table franchises (
  id UUID primary key,
  name varchar(150) not null
);

create table branches (
  id UUID primary key,
  franchise_id UUID not null,
  name varchar(150) not null,
  constraint fk_branches_franchises foreign key (franchise_id) references franchises(id)
);

create table products (
  id UUID primary key,
  branch_id UUID not null,
  name varchar(150) not null,
  stock int not null default 0,
  constraint fk_products_branches foreign key (branch_id) references branches(id)
);