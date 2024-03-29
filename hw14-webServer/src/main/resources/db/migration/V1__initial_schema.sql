-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence address_SEQ start with 1 increment by 1;
create sequence client_SEQ start with 1 increment by 1;
create sequence phone_SEQ start with 1 increment by 1;
create sequence user_SEQ start with 1 increment by 1;
create sequence role_SEQ start with 1 increment by 1;

create table address
(
    id   bigint not null primary key,
    street varchar(50)
);

create table client
(
    id   bigint not null primary key,
    name varchar(50) not null,
    address_id bigint REFERENCES address(id)
);

create table phone
(
    id   bigint not null primary key,
    number varchar(50),
    client_id bigint not null REFERENCES client(id)
);

create table users
(
    id bigint not null primary key,
    login varchar(50) not null unique ,
    password varchar(50) not null
);

create table roles
(
    id bigint not null primary key,
    name varchar(50) unique not null
);

create table user_roles
(
    user_id bigint REFERENCES users(id),
    role_id bigint REFERENCES roles(id)
);