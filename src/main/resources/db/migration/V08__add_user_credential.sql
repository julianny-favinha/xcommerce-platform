SET SCHEMA 'xcommerce';

CREATE TABLE user_credential (
    id serial primary key,
    user_id BIGINT not null,
    email TEXT unique,
    password TEXT not null
);

ALTER TABLE user_credential ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES user_relation(id);