SET SCHEMA 'xcommerce';

create table category_relation (
    id serial primary key,
    external_id text not null unique
);