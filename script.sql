create table user_crypto
(
    uuid     uuid not null
        primary key,
    price    numeric(19, 2),
    symbol   varchar(255),
    username varchar(255)
);

alter table user_crypto
    owner to postgres;

create table crypto_currency_entity
(
    id     bigint not null
        primary key,
    name   varchar(255),
    price  numeric(19, 2),
    symbol varchar(255)
);

alter table crypto_currency_entity
    owner to postgres;


