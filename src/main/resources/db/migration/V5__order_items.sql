-- Order line items
CREATE TABLE IF NOT EXISTS order_item (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id),
    artwork_id BIGINT NOT NULL REFERENCES artwork(id),
    artist_id BIGINT NOT NULL REFERENCES users(id),
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(12,2) NOT NULL,
    line_total NUMERIC(12,2) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_order_item_order_id ON order_item(order_id);
