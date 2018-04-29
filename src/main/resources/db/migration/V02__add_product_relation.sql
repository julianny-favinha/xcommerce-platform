SET SCHEMA 'xcommerce';

create table product_relation (
    id serial primary key,
    external_id bigint not null unique
);