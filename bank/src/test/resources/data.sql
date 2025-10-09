CREATE SCHEMA IF NOT EXISTS accounts;
CREATE SCHEMA IF NOT EXISTS clients;
CREATE SCHEMA IF NOT EXISTS outbox;
CREATE TABLE IF NOT EXISTS accounts.client_kyc_view (
                                                        client_id UUID PRIMARY KEY,
                                                        kyc_status VARCHAR(32),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    last_event_id VARCHAR(64)
    );
INSERT INTO accounts.client_kyc_view (
    client_id,
    kyc_status,
    created_at,
    updated_at,
    last_event_id
) VALUES (
             'f106b7d8-d85a-4c31-a148-1a4f00a0d2ce',
             'APPROVED', -- or another valid KycStatus value
             CURRENT_TIMESTAMP,
             CURRENT_TIMESTAMP,
             'event-123'
         );