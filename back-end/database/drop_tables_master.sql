--------------------------
-- Drop Report Tables
--------------------------

DROP TABLE IF EXISTS master.registered_users;

DROP TABLE IF EXISTS master.tenants;

DROP TABLE IF EXISTS master.pending_tenant_creators;

DROP TABLE IF EXISTS master.pending_tenants;

--------------------------
-- Drop Auxiliary Types
--------------------------

DROP TYPE IF EXISTS term_type;

DROP TYPE IF EXISTS action_type;

DROP TYPE IF EXISTS course_misc_unit_type;

DROP TYPE IF EXISTS class_misc_unit_type;

DROP TYPE IF EXISTS exam_type;

DROP TYPE IF EXISTS weekday;