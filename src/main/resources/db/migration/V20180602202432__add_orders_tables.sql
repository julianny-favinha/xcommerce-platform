SET SCHEMA 'xcommerce';

CREATE TABLE order (
    id SERIAL PRIMARY KEY,
    user_id BIGINT,
    freight_price INTEGER,
    products_price INTEGER,
    payment_code TEXT,
    payment_type INTEGER,
    payment_status INTEGER,
    shipment_id BIGINT,
    shipment_status INTEGER,
    shipment_info JSON,
    created_at TIMESTAMP
);

ALTER TABLE order ADD CONSTRAINT user_order_fk FOREIGN KEY (user_id) REFERENCES user_relation(id);
ALTER TABLE order ADD CONSTRAINT logistic_order_fk FOREIGN (shipment_id) REFERENCES logistic_relation(id);

CREATE TABLE order_item (
  id SERIAL PRIMARY KEY,
  order_id BIGINT,
  product_id BIGINT,
  product_name TEXT,
  product_price BIGINT
);

ALTER TABLE order_item ADD CONSTRAINT order_item_order_fk FOREIGN (order_id) REFERENCES order(id);
ALTER TABLE order_item add constraint order_item_product_fk FOREIGN (product_id) references product_relation(id);
