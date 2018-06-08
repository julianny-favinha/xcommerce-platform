SET SCHEMA 'xcommerce';

create table logistic_relation (
    id serial primary key,
    external_id json not null
);