SET SCHEMA 'xcommerce';

ALTER TABLE order_item ADD COLUMN quantity INTEGER;

CREATE TABLE product_expiration (
  product_id BIGINT PRIMARY KEY,
  quantity INTEGER,
  created_at TIMESTAMP,
  valid_until TIMESTAMP
)