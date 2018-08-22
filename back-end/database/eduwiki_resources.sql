--------------------------
-- Create Schema
--------------------------

CREATE SCHEMA IF NOT EXISTS resourcesSchema;

--------------------------
-- Create Main Tables
--------------------------

CREATE TABLE IF NOT EXISTS resourcesSchema.resource (
  uuid UUID NOT NULL,
  byte_sequence bytea NOT NULL,
  content_type VARCHAR(100) NOT NULL,
  original_filename VARCHAR(50),
  size BIGINT,
  PRIMARY KEY(uuid)
);
