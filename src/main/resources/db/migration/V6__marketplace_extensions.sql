-- Artwork catalog extensions
ALTER TABLE artwork ADD COLUMN IF NOT EXISTS medium VARCHAR(255);
ALTER TABLE artwork ADD COLUMN IF NOT EXISTS year_created INTEGER;
ALTER TABLE artwork ADD COLUMN IF NOT EXISTS dimensions VARCHAR(255);
ALTER TABLE artwork ADD COLUMN IF NOT EXISTS framing VARCHAR(255);
ALTER TABLE artwork ADD COLUMN IF NOT EXISTS edition_number INTEGER;
ALTER TABLE artwork ADD COLUMN IF NOT EXISTS edition_size INTEGER;
ALTER TABLE artwork ADD COLUMN IF NOT EXISTS rejection_reason TEXT;

ALTER TABLE users ADD COLUMN IF NOT EXISTS verified_artist BOOLEAN DEFAULT FALSE;

-- Artist follow
CREATE TABLE IF NOT EXISTS artist_follow (
    id BIGSERIAL PRIMARY KEY,
    follower_id BIGINT NOT NULL REFERENCES users(id),
    artist_id BIGINT NOT NULL REFERENCES users(id),
    creation_date TIMESTAMP(6) DEFAULT NOW(),
    UNIQUE(follower_id, artist_id)
);

-- Artwork views for trending
CREATE TABLE IF NOT EXISTS artwork_view (
    id BIGSERIAL PRIMARY KEY,
    artwork_id BIGINT NOT NULL REFERENCES artwork(id),
    user_id BIGINT REFERENCES users(id),
    viewed_at TIMESTAMP(6) DEFAULT NOW()
);

-- Curated collections
CREATE TABLE IF NOT EXISTS collection (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    featured BOOLEAN DEFAULT FALSE,
    creation_date TIMESTAMP(6) DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS collection_artwork (
    collection_id BIGINT NOT NULL REFERENCES collection(id),
    artwork_id BIGINT NOT NULL REFERENCES artwork(id),
    PRIMARY KEY (collection_id, artwork_id)
);

-- Shipments
CREATE TABLE IF NOT EXISTS shipment (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL UNIQUE REFERENCES orders(id),
    carrier VARCHAR(255),
    tracking_number VARCHAR(255),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    shipped_at TIMESTAMP(6),
    delivered_at TIMESTAMP(6)
);

-- Platform economics
CREATE TABLE IF NOT EXISTS platform_config (
    id BIGSERIAL PRIMARY KEY,
    commission_rate NUMERIC(5,2) NOT NULL DEFAULT 15.00
);

INSERT INTO platform_config (commission_rate) SELECT 15.00 WHERE NOT EXISTS (SELECT 1 FROM platform_config);

CREATE TABLE IF NOT EXISTS artist_wallet (
    id BIGSERIAL PRIMARY KEY,
    artist_id BIGINT NOT NULL UNIQUE REFERENCES users(id),
    balance NUMERIC(14,2) NOT NULL DEFAULT 0,
    pending_balance NUMERIC(14,2) NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS payout_request (
    id BIGSERIAL PRIMARY KEY,
    artist_id BIGINT NOT NULL REFERENCES users(id),
    amount NUMERIC(14,2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    requested_at TIMESTAMP(6) DEFAULT NOW(),
    processed_at TIMESTAMP(6)
);

-- Certificate of authenticity
CREATE TABLE IF NOT EXISTS certificate_of_authenticity (
    id BIGSERIAL PRIMARY KEY,
    order_item_id BIGINT NOT NULL UNIQUE REFERENCES order_item(id),
    verification_code VARCHAR(64) NOT NULL UNIQUE,
    issued_at TIMESTAMP(6) DEFAULT NOW()
);

-- Auctions and offers
CREATE TABLE IF NOT EXISTS auction (
    id BIGSERIAL PRIMARY KEY,
    artwork_id BIGINT NOT NULL REFERENCES artwork(id),
    start_time TIMESTAMP(6) NOT NULL,
    end_time TIMESTAMP(6) NOT NULL,
    reserve_price NUMERIC(12,2),
    current_bid NUMERIC(12,2),
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    winner_id BIGINT REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS bid (
    id BIGSERIAL PRIMARY KEY,
    auction_id BIGINT NOT NULL REFERENCES auction(id),
    bidder_id BIGINT NOT NULL REFERENCES users(id),
    amount NUMERIC(12,2) NOT NULL,
    bid_time TIMESTAMP(6) DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS offer (
    id BIGSERIAL PRIMARY KEY,
    artwork_id BIGINT NOT NULL REFERENCES artwork(id),
    buyer_id BIGINT NOT NULL REFERENCES users(id),
    amount NUMERIC(12,2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP(6) DEFAULT NOW()
);

-- Event tickets
CREATE TABLE IF NOT EXISTS event_ticket (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL REFERENCES event(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    order_id BIGINT REFERENCES orders(id),
    purchased_at TIMESTAMP(6) DEFAULT NOW()
);

-- Vote deduplication
CREATE UNIQUE INDEX IF NOT EXISTS uq_vote_user_competition ON vote(user_id, competition_id);

-- Audit log
CREATE TABLE IF NOT EXISTS audit_log (
    id BIGSERIAL PRIMARY KEY,
    admin_id BIGINT REFERENCES users(id),
    action VARCHAR(255) NOT NULL,
    entity_type VARCHAR(100),
    entity_id BIGINT,
    details TEXT,
    created_at TIMESTAMP(6) DEFAULT NOW()
);

INSERT INTO permission (permission_name, description) VALUES
    ('USER_FOLLOW_ARTIST', 'Follow artists'),
    ('USER_MAKE_OFFER', 'Make purchase offers'),
    ('USER_PLACE_BID', 'Place auction bids'),
    ('ARTIST_REQUEST_PAYOUT', 'Request wallet payout'),
    ('ADMIN_MANAGE_PAYOUTS', 'Approve payouts'),
    ('ADMIN_VIEW_DASHBOARD', 'View admin dashboard')
ON CONFLICT (permission_name) DO NOTHING;

INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id FROM user_role r, permission p
WHERE r.role_name = 'ROLE_CUSTOMER' AND p.permission_name IN ('USER_FOLLOW_ARTIST', 'USER_MAKE_OFFER', 'USER_PLACE_BID')
ON CONFLICT DO NOTHING;

INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id FROM user_role r, permission p
WHERE r.role_name = 'ROLE_ARTIST' AND p.permission_name IN ('USER_FOLLOW_ARTIST', 'USER_MAKE_OFFER', 'USER_PLACE_BID', 'ARTIST_REQUEST_PAYOUT')
ON CONFLICT DO NOTHING;

INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id FROM user_role r, permission p
WHERE r.role_name = 'ROLE_ADMIN' AND p.permission_name IN ('ADMIN_MANAGE_PAYOUTS', 'ADMIN_VIEW_DASHBOARD')
ON CONFLICT DO NOTHING;
