CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO master.tenants(uuid, schema_name, email_pattern, created_at, created_by) VALUES ('1ed95f93-5533-47b8-81d3-369c8c30ff80', 'master', '', '2018-07-14 11:30:00.000000', 'nuno');

INSERT INTO master.tenants(uuid, schema_name, email_pattern, created_at, created_by) VALUES ('4cd93a0f-5b5c-4902-ae0a-181c780fedb1', 'isel', '@isel.pt', '2018-07-14 13:30:00.000000', 'nuno');

INSERT INTO master.pending_tenants (uuid, tenant_full_name, tenant_short_name, tenant_address, tenant_contact, tenant_website, tenant_email_pattern, tenant_organization_summary, tenant_timestamp)
VALUES ('9c509b5e-2451-408b-a245-fa0476570132', 'faculdade do porto', 'fp', 'porto', '236946789', 'www.fp.pt', '@fp.pt', 'faculdade situada no porto', '2018-08-15 09:00:00.000000' );

INSERT INTO master.pending_tenants (uuid, tenant_full_name, tenant_short_name, tenant_address, tenant_contact, tenant_website, tenant_email_pattern, tenant_organization_summary, tenant_timestamp)
VALUES ('9c509b5e-2451-408b-a245-fa0476570141', 'faculdade de letras', 'flul', 'lisboa', '216446790', 'www.flul.pt', '@flul.pt', 'faculdade de letras em lisboa', '2018-08-16 09:00:00.000000'  );

INSERT INTO master.pending_tenant_creators (user_username, pending_tenant_uuid, user_email, user_given_name, user_family_name, principal_user)
VALUES ('fausto', '9c509b5e-2451-408b-a245-fa0476570132', 'fausto@fp.pt', 'fausto', 'rodrigues', true);

INSERT INTO master.pending_tenant_creators (user_username, pending_tenant_uuid, user_email, user_given_name, user_family_name, principal_user)
VALUES ('miguel', '9c509b5e-2451-408b-a245-fa0476570132', 'miguel@fp.pt', 'miguel', 'alcides', false);

INSERT INTO master.registered_users(user_username, user_confirmed, tenant_uuid) VALUES('ze', true, '4cd93a0f-5b5c-4902-ae0a-181c780fedb1');

INSERT INTO master.registered_users(user_username, user_confirmed, tenant_uuid) VALUES('bruno', true, '4cd93a0f-5b5c-4902-ae0a-181c780fedb1');

INSERT INTO master.registered_users(user_username, user_confirmed, tenant_uuid) VALUES('jg', true, '4cd93a0f-5b5c-4902-ae0a-181c780fedb1');

INSERT INTO master.user_account (user_username, user_password, user_given_name, user_family_name, user_email)
    values ('nuno', 1234, 'Nuno', 'Pinto', 'nunobernardopinto@gmail.com');

INSERT INTO master.reputation (role, user_username)
  values ('ROLE_DEV', 'nuno');

insert into master.validation_token (token, validation_date)
  values ('04805b5d-e089-48e0-b7da-68a6321a17ff', '2018-07-09 14:00:00.000000');