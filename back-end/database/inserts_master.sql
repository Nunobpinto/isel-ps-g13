CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO master.tenants(uuid, schema_name, email_pattern, created_at, created_by) VALUES ('1ed95f93-5533-47b8-81d3-369c8c30ff80', 'master', '', current_timestamp, 'nuno');

INSERT INTO master.tenants(uuid, schema_name, email_pattern, created_at, created_by) VALUES ('4cd93a0f-5b5c-4902-ae0a-181c780fedb1', 'isel', '@alunos.isel.pt', current_timestamp, 'nuno');

INSERT INTO master.tenants(uuid, schema_name, email_pattern, created_at, created_by) VALUES ('d39c1e3a-eec7-447a-8c63-52fda106c5e9', 'iseg', '@alunos.iseg.pt', current_timestamp, 'nuno');

INSERT INTO master.registered_users(user_username, tenant_uuid) VALUES('ze', '4cd93a0f-5b5c-4902-ae0a-181c780fedb1');

INSERT INTO master.registered_users(user_username, tenant_uuid) VALUES('bruno', '4cd93a0f-5b5c-4902-ae0a-181c780fedb1');

INSERT INTO master.registered_users(user_username, tenant_uuid) VALUES('jg', '4cd93a0f-5b5c-4902-ae0a-181c780fedb1');

INSERT INTO master.registered_users(user_username, tenant_uuid) VALUES('luis', 'd39c1e3a-eec7-447a-8c63-52fda106c5e9');

INSERT INTO master.user_account (user_username, user_password, user_given_name, user_family_name, user_email)
    values ('nuno', 1234, 'Nuno', 'Pinto', 'nunobernardopinto@gmail.com');
    
INSERT INTO master.reputation (role, user_username)
  values ('ROLE_DEV', 'nuno');