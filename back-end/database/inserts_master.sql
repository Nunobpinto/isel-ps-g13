CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO master.tenants(uuid, schema_name, email_pattern, created_at) VALUES ('4cd93a0f-5b5c-4902-ae0a-181c780fedb1', 'isel', '@alunos.isel.pt', current_timestamp);

INSERT INTO master.tenants(uuid, schema_name, email_pattern, created_at) VALUES ('d39c1e3a-eec7-447a-8c63-52fda106c5e9', 'iseg', '@alunos.iseg.pt', current_timestamp);

INSERT INTO master.registered_users(user_username, tenant_uuid) VALUES('ze', '4cd93a0f-5b5c-4902-ae0a-181c780fedb1');

INSERT INTO master.registered_users(user_username, tenant_uuid) VALUES('bruno', '4cd93a0f-5b5c-4902-ae0a-181c780fedb1');

INSERT INTO master.registered_users(user_username, tenant_uuid) VALUES('jg', '4cd93a0f-5b5c-4902-ae0a-181c780fedb1');

INSERT INTO master.registered_users(user_username, tenant_uuid) VALUES('luis', 'd39c1e3a-eec7-447a-8c63-52fda106c5e9');