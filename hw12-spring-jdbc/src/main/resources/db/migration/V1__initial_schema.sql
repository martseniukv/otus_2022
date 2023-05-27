create table client
(
    id   bigserial not null primary key,
    name varchar(50) not null
);
create table address
(
    id   bigserial not null primary key,
    street varchar(50),
    client_id bigint not null REFERENCES client(id)
);

create table phone
(
    id   bigserial not null primary key,
    number varchar(50),
    client_id bigint not null REFERENCES client(id)
);