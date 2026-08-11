-- RBAC permissions
CREATE TABLE IF NOT EXISTS permission (
    permission_id BIGSERIAL PRIMARY KEY,
    permission_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS role_permission (
    role_id BIGINT NOT NULL REFERENCES user_role(role_id),
    permission_id BIGINT NOT NULL REFERENCES permission(permission_id),
    PRIMARY KEY (role_id, permission_id)
);

-- Seed permissions
INSERT INTO permission (permission_name, description) VALUES
    ('USER_MODIFY_CART', 'Add/update/remove cart items'),
    ('USER_ADD_WISHLIST', 'Add artwork to wishlist'),
    ('USER_FETCH_WISHLIST', 'View wishlist'),
    ('USER_DELETE_WISHLIST', 'Remove from wishlist'),
    ('USER_ADD_ORDER', 'Create orders and checkout'),
    ('USER_VIEW_ORDERS', 'View own orders'),
    ('USER_MODIFY_PROFILE', 'Update user profile'),
    ('CUSTOMER_BROWSE_ARTWORK', 'Browse artwork catalog'),
    ('ARTIST_SUBMIT_ARTWORK', 'Submit artwork for review'),
    ('ARTIST_BROWSE_ARTWORK', 'Browse artwork catalog as artist'),
    ('ARTIST_VIEW_OWN_ARTWORK', 'View own submitted artwork'),
    ('USER_ADD_COMPETITOR', 'Enter competition as competitor'),
    ('USER_MODIFY_COMPETITOR', 'Update competition entry'),
    ('USER_RATE_ARTWORK', 'Rate purchased artwork'),
    ('ADMIN_MODIFY_USER', 'Modify user accounts'),
    ('ADMIN_FETCH_ARTWORK', 'View artwork for moderation'),
    ('ADMIN_MODIFY_ARTWORK', 'Approve/reject artwork'),
    ('ADMIN_DELETE_ARTWORK', 'Delete artwork'),
    ('ADMIN_FETCH_ORDERS', 'View all orders'),
    ('ADMIN_MODIFY_ORDER', 'Update order status'),
    ('ADMIN_DELETE_ORDER', 'Delete orders'),
    ('ADMIN_FETCH_REPORT', 'View reports'),
    ('ADMIN_DELETE_REPORT', 'Delete reports'),
    ('ADMIN_FETCH_COMPETITION', 'View competitions'),
    ('ADMIN_ADD_COMPETITION', 'Create competitions'),
    ('ADMIN_MODIFY_COMPETITION', 'Update competitions'),
    ('ADMIN_DELETE_COMPETITION', 'Delete competitions'),
    ('ADMIN_FETCH_COMPETITOR', 'View competitors'),
    ('ADMIN_DELETE_COMPETITOR', 'Delete competitors'),
    ('ADMIN_ADD_EVENT', 'Create events'),
    ('ADMIN_FETCH_EVENT', 'View events'),
    ('ADMIN_MODIFY_EVENT', 'Update events'),
    ('ADMIN_DELETE_EVENT', 'Delete events'),
    ('ADMIN_ADD_STANDARD', 'Create standards'),
    ('ADMIN_MODIFY_STANDARD', 'Update standards'),
    ('ADMIN_MANAGE_PERMISSIONS', 'Manage role permissions')
ON CONFLICT (permission_name) DO NOTHING;

-- ROLE_CUSTOMER permissions
INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM user_role r, permission p
WHERE r.role_name = 'ROLE_CUSTOMER'
  AND p.permission_name IN (
    'USER_MODIFY_CART', 'USER_ADD_WISHLIST', 'USER_FETCH_WISHLIST', 'USER_DELETE_WISHLIST',
    'USER_ADD_ORDER', 'USER_VIEW_ORDERS', 'USER_MODIFY_PROFILE', 'CUSTOMER_BROWSE_ARTWORK',
    'USER_ADD_COMPETITOR', 'USER_MODIFY_COMPETITOR', 'USER_RATE_ARTWORK'
)
ON CONFLICT DO NOTHING;

-- ROLE_ARTIST permissions (customer + artist)
INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM user_role r, permission p
WHERE r.role_name = 'ROLE_ARTIST'
  AND p.permission_name IN (
    'USER_MODIFY_CART', 'USER_ADD_WISHLIST', 'USER_FETCH_WISHLIST', 'USER_DELETE_WISHLIST',
    'USER_ADD_ORDER', 'USER_VIEW_ORDERS', 'USER_MODIFY_PROFILE', 'CUSTOMER_BROWSE_ARTWORK',
    'ARTIST_SUBMIT_ARTWORK', 'ARTIST_BROWSE_ARTWORK', 'ARTIST_VIEW_OWN_ARTWORK',
    'USER_ADD_COMPETITOR', 'USER_MODIFY_COMPETITOR', 'USER_RATE_ARTWORK'
)
ON CONFLICT DO NOTHING;

-- ROLE_ADMIN permissions (all ADMIN_* + USER_RATE)
INSERT INTO role_permission (role_id, permission_id)
SELECT r.role_id, p.permission_id
FROM user_role r, permission p
WHERE r.role_name = 'ROLE_ADMIN'
  AND (p.permission_name LIKE 'ADMIN_%' OR p.permission_name = 'USER_RATE_ARTWORK')
ON CONFLICT DO NOTHING;

-- Payment status: add FAILED and normalize INTIALIZED -> INITIALIZED
ALTER TABLE payment_log DROP CONSTRAINT IF EXISTS payment_log_status_check;
UPDATE payment_log SET status = 'INITIALIZED' WHERE status = 'INTIALIZED';
ALTER TABLE payment_log ADD CONSTRAINT payment_log_status_check
    CHECK (status IN ('VERIFIED', 'INITIALIZED', 'INTIALIZED', 'FAILED'));
