SET SCHEMA 'xcommerce';

CREATE TABLE user_token (
    user_id BIGINT primary key,
    token TEXT not null,
    expire_at timestamp not null
);

ALTER TABLE user_token ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES user_relation(id);
