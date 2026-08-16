-- KELEM API contract v1.0 schema alignment

-- Slugs
ALTER TABLE users ADD COLUMN IF NOT EXISTS slug VARCHAR(255);
UPDATE users SET slug = CONCAT(LOWER(REGEXP_REPLACE(COALESCE(first_name, 'user'), '[^a-zA-Z0-9]+', '-', 'g')), '-', id)
WHERE slug IS NULL;
CREATE UNIQUE INDEX IF NOT EXISTS uq_users_slug ON users (slug);

ALTER TABLE collection ADD COLUMN IF NOT EXISTS slug VARCHAR(255);
UPDATE collection SET slug = CONCAT(LOWER(REGEXP_REPLACE(COALESCE(title, 'collection'), '[^a-zA-Z0-9]+', '-', 'g')), '-', id)
WHERE slug IS NULL;
CREATE UNIQUE INDEX IF NOT EXISTS uq_collection_slug ON collection (slug);

-- Optimistic locking
ALTER TABLE artwork ADD COLUMN IF NOT EXISTS version BIGINT NOT NULL DEFAULT 0;
ALTER TABLE auction ADD COLUMN IF NOT EXISTS version BIGINT NOT NULL DEFAULT 0;
ALTER TABLE artist_wallet ADD COLUMN IF NOT EXISTS version BIGINT NOT NULL DEFAULT 0;

-- Cart merge uniqueness
CREATE UNIQUE INDEX IF NOT EXISTS uq_cart_user_artwork ON cart (user_id, artwork_id);

-- Wishlist: many artworks per user
ALTER TABLE wishlist DROP CONSTRAINT IF EXISTS wishlist_user_id_key;
ALTER TABLE wishlist DROP CONSTRAINT IF EXISTS uk_trd6335blsefl2gxpb8lr0gr7;
CREATE UNIQUE INDEX IF NOT EXISTS uq_wishlist_user_artwork ON wishlist (user_id, artwork_id);

-- Reviews
ALTER TABLE rating ADD COLUMN IF NOT EXISTS comment TEXT;

-- Order fulfill idempotency
ALTER TABLE orders ADD COLUMN IF NOT EXISTS fulfilled BOOLEAN NOT NULL DEFAULT FALSE;

-- Payouts: never mark PAID without external ref
ALTER TABLE payout_request ADD COLUMN IF NOT EXISTS external_ref VARCHAR(255);
ALTER TABLE payout_request ADD COLUMN IF NOT EXISTS manual BOOLEAN NOT NULL DEFAULT TRUE;

-- Event images as filesystem paths (drop postgres OID)
ALTER TABLE event ALTER COLUMN image TYPE VARCHAR(1024) USING NULL;

-- Auction watch list
CREATE TABLE IF NOT EXISTS auction_watch (
    id BIGSERIAL PRIMARY KEY,
    auction_id BIGINT NOT NULL REFERENCES auction(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    creation_date TIMESTAMP(6) DEFAULT NOW(),
    UNIQUE (auction_id, user_id)
);

-- Messaging
CREATE TABLE IF NOT EXISTS message_thread (
    id BIGSERIAL PRIMARY KEY,
    buyer_id BIGINT NOT NULL REFERENCES users(id),
    artist_id BIGINT NOT NULL REFERENCES users(id),
    artwork_id BIGINT REFERENCES artwork(id),
    creation_date TIMESTAMP(6) DEFAULT NOW(),
    UNIQUE (buyer_id, artist_id, artwork_id)
);

CREATE TABLE IF NOT EXISTS message (
    id BIGSERIAL PRIMARY KEY,
    thread_id BIGINT NOT NULL REFERENCES message_thread(id),
    sender_id BIGINT NOT NULL REFERENCES users(id),
    body TEXT NOT NULL,
    read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP(6) DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_message_thread_id ON message (thread_id);

-- Roles
INSERT INTO user_role (is_admin, role_name, creation_date, last_update_date)
SELECT FALSE, 'ROLE_MANAGER', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM user_role WHERE role_name = 'ROLE_MANAGER');

INSERT INTO user_role (is_admin, role_name, creation_date, last_update_date)
SELECT FALSE, 'ROLE_ORGANIZATION', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM user_role WHERE role_name = 'ROLE_ORGANIZATION');

INSERT INTO permission (permission_name, description) VALUES
    ('USER_MESSAGE', 'Collector-artist messaging'),
    ('ARTIST_ACCEPT_OFFER', 'Accept or reject purchase offers on own artwork'),
    ('ORG_ADD_EVENT', 'Create organization events'),
    ('ORG_MODIFY_OWN_EVENT', 'Update own organization events')
ON CONFLICT (permission_name) DO NOTHING;

INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id FROM user_role r, permission p
WHERE r.role_name = 'ROLE_CUSTOMER' AND p.permission_name IN ('USER_MESSAGE')
ON CONFLICT DO NOTHING;

INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id FROM user_role r, permission p
WHERE r.role_name = 'ROLE_ARTIST' AND p.permission_name IN ('USER_MESSAGE', 'ARTIST_ACCEPT_OFFER')
ON CONFLICT DO NOTHING;

INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id FROM user_role r, permission p
WHERE r.role_name = 'ROLE_MANAGER' AND p.permission_name IN (
    'ADMIN_FETCH_ARTWORK', 'ADMIN_MODIFY_ARTWORK',
    'ADMIN_FETCH_COMPETITION', 'ADMIN_ADD_COMPETITION', 'ADMIN_MODIFY_COMPETITION', 'ADMIN_DELETE_COMPETITION',
    'ADMIN_FETCH_COMPETITOR',
    'ADMIN_ADD_STANDARD', 'ADMIN_MODIFY_STANDARD',
    'ADMIN_FETCH_EVENT', 'ADMIN_MODIFY_EVENT'
)
ON CONFLICT DO NOTHING;

INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id FROM user_role r, permission p
WHERE r.role_name = 'ROLE_ORGANIZATION' AND p.permission_name IN (
    'ORG_ADD_EVENT', 'ORG_MODIFY_OWN_EVENT', 'ADMIN_FETCH_EVENT', 'ADMIN_ADD_EVENT', 'ADMIN_MODIFY_EVENT',
    'USER_MODIFY_PROFILE'
)
ON CONFLICT DO NOTHING;

INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id FROM user_role r, permission p
WHERE r.role_name = 'ROLE_ADMIN' AND p.permission_name IN (
    'USER_MESSAGE', 'ARTIST_ACCEPT_OFFER', 'ORG_ADD_EVENT', 'ORG_MODIFY_OWN_EVENT'
)
ON CONFLICT DO NOTHING;
