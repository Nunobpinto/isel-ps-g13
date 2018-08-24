--------------------------
-- Create Schema
--------------------------

CREATE SCHEMA IF NOT EXISTS master;

--------------------------
-- Create Main Tables
--------------------------

CREATE TABLE IF NOT EXISTS master.tenants (
	uuid VARCHAR(255),
	schema_name VARCHAR(255) UNIQUE,
	email_pattern VARCHAR(30) UNIQUE,
	created_at TIMESTAMP,
	PRIMARY KEY(uuid)
);

CREATE TABLE IF NOT EXISTS master.registered_users (
	user_username VARCHAR(20),
	tenant_uuid VARCHAR(255) REFERENCES master.tenants,
	PRIMARY KEY(user_username, tenant_uuid)
);

CREATE TABLE IF NOT EXISTS master.pending_tenants (
	uuid VARCHAR(255),
	tenant_full_name VARCHAR(100) NOT NULL UNIQUE,
	tenant_short_name VARCHAR(10) NOT NULL UNIQUE,
	tenant_address VARCHAR (100) NOT NULL UNIQUE,
	tenant_contact VARCHAR (15) NOT NULL UNIQUE,
	tenant_website VARCHAR(150) NOT NULL UNIQUE,
	tenant_email_pattern VARCHAR(30) UNIQUE,
	tenant_organization_summary VARCHAR(500) NOT NULL,
	tenant_timestamp TIMESTAMP NOT NULL,
	PRIMARY KEY(uuid)
);

CREATE TABLE IF NOT EXISTS master.pending_tenant_creators (
	user_username VARCHAR(20),
	pending_tenant_uuid VARCHAR(255) REFERENCES master.pending_tenants,
	user_organization_email varchar(35) NOT NULL UNIQUE,
	user_given_name VARCHAR(15) NOT NULL,
	user_family_name VARCHAR(15) NOT NULL,
	principal_user BOOLEAN NOT NULL,
	PRIMARY KEY(user_username, pending_tenant_uuid)
);

CREATE TABLE IF NOT EXISTS master.user_account (
  user_username VARCHAR(20),
  user_password VARCHAR(60),
  user_given_name VARCHAR(15) NOT NULL,
  user_confirmed BOOLEAN NOT NULL DEFAULT true,
  user_family_name VARCHAR(15) NOT NULL,
  user_personal_email varchar(35) UNIQUE NOT NULL,
  PRIMARY KEY (user_username)
);

CREATE TABLE IF NOT EXISTS master.reputation (
  reputation_id SERIAL,
  role VARCHAR NOT NULL, 
  user_username varchar(20) REFERENCES master.user_account ON DELETE CASCADE,
  PRIMARY KEY (reputation_id, user_username)
);
















