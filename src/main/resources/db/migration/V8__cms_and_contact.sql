-- CMS key/value and public contact form persistence
CREATE TABLE IF NOT EXISTS cms_setting (
    setting_key VARCHAR(128) PRIMARY KEY,
    setting_value TEXT,
    last_update_date TIMESTAMP(6) DEFAULT NOW()
);

INSERT INTO cms_setting (setting_key, setting_value)
VALUES ('siteName', 'KELEM'), ('currency', 'ETB')
ON CONFLICT (setting_key) DO NOTHING;

CREATE TABLE IF NOT EXISTS contact_message (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    creation_date TIMESTAMP(6) DEFAULT NOW()
);
