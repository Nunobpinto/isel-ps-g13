--------------------------
-- Auxiliary Types
--------------------------

CREATE TYPE term_type AS ENUM ('WINTER', 'SUMMER');

CREATE TYPE course_misc_unit_type AS ENUM ('WORK_ASSIGNMENT', 'EXAM_TEST');

CREATE TYPE class_misc_unit_type AS ENUM ('HOMEWORK', 'LECTURE');

CREATE TYPE exam_type AS ENUM ('EXAM', 'TEST');

CREATE TYPE weekday AS ENUM ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY');

CREATE TYPE action_Type AS ENUM ('CREATE', 'ALTER', 'DELETE', 'VOTE_UP', 'VOTE_DOWN', 'APPROVE_REPORT', 'APPROVE_STAGE', 'REJECT_REPORT', 'REJECT_STAGE');

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
	tenant_full_name VARCHAR(100) NOT NULL,
	tenant_short_name VARCHAR(10) NOT NULL,
	tenant_address VARCHAR (100) NOT NULL,
	tenant_contact VARCHAR (15) NOT NULL,
	tenant_email_pattern VARCHAR(30) UNIQUE,
	time_stamp TIMESTAMP NOT NULL,
	PRIMARY KEY(uuid)
);

CREATE TABLE IF NOT EXISTS master.pending_tenant_creators (
	user_username VARCHAR(20),
	pending_tenant_uuid VARCHAR(255) REFERENCES master.pending_tenants,
	user_given_name VARCHAR(15) NOT NULL,
	user_family_name VARCHAR(15) NOT NULL,
	user_organization_email varchar(35) UNIQUE NOT NULL,
	PRIMARY KEY(user_username, pending_tenant_uuid)
);
