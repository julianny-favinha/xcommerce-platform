SET SCHEMA 'xcommerce';

create table user_relation (
    id serial primary key,
    external_id text not null unique
);