-- ISEL SCHEMA DROPS --

--------------------------
-- Drop Report Tables
--------------------------

DROP TABLE IF EXISTS isel.organization_report;

DROP TABLE IF EXISTS isel.programme_report;

DROP TABLE IF EXISTS isel.course_report;

DROP TABLE IF EXISTS isel.course_programme_report;

DROP TABLE IF EXISTS isel.class_report;

DROP TABLE IF EXISTS isel.work_assignment_report;

DROP TABLE IF EXISTS isel.exam_report;

DROP TABLE IF EXISTS isel.lecture_report;

DROP TABLE IF EXISTS isel.homework_report;

DROP TABLE IF EXISTS isel.user_report;

DROP TABLE IF EXISTS isel.course_class_report;

--------------------------
-- Drop Staged Tables
--------------------------

DROP TABLE IF EXISTS isel.programme_stage;

DROP TABLE IF EXISTS isel.course_stage;

DROP TABLE IF EXISTS isel.course_programme_stage;

DROP TABLE IF EXISTS isel.class_stage;

DROP TABLE IF EXISTS isel.course_class_stage;

DROP TABLE IF EXISTS isel.work_assignment_stage;

DROP TABLE IF EXISTS isel.exam_stage;

DROP TABLE IF EXISTS isel.lecture_stage;

DROP TABLE IF EXISTS isel.homework_stage;

DROP TABLE IF EXISTS isel.course_misc_unit_stage;

DROP TABLE IF EXISTS isel.class_misc_unit_stage;

--------------------------
-- Drop Version Tables
--------------------------

DROP TABLE IF EXISTS isel.organization_version;

DROP TABLE IF EXISTS isel.programme_version;

DROP TABLE IF EXISTS isel.course_version;

DROP TABLE IF EXISTS isel.course_programme_version;

DROP TABLE IF EXISTS isel.class_version;

DROP TABLE IF EXISTS isel.work_assignment_version;

DROP TABLE IF EXISTS isel.exam_version;

DROP TABLE IF EXISTS isel.lecture_version;

DROP TABLE IF EXISTS isel.homework_version;

--------------------------
-- Drop Main Tables
--------------------------

DROP TABLE IF EXISTS isel.course_programme;

DROP TABLE IF EXISTS isel.work_assignment;

DROP TABLE IF EXISTS isel.exam;

DROP TABLE IF EXISTS isel.lecture;

DROP TABLE IF EXISTS isel.homework;

DROP TABLE IF EXISTS isel.reputation_log;

DROP TABLE IF EXISTS isel.action_log;

DROP TABLE IF EXISTS isel.class_misc_unit;

DROP TABLE IF EXISTS isel.course_misc_unit;

DROP TABLE IF EXISTS isel.user_course_class;

DROP TABLE IF EXISTS isel.user_programme;

DROP TABLE IF EXISTS isel.reputation;

DROP TABLE IF EXISTS isel.course_term;

DROP TABLE IF EXISTS isel.course_class;

DROP TABLE IF EXISTS isel.class;

DROP TABLE IF EXISTS isel.term;

DROP TABLE IF EXISTS isel.programme;

DROP TABLE IF EXISTS isel.course;

DROP TABLE IF EXISTS isel.organization;

DROP TABLE IF EXISTS isel.user_account;

DROP TABLE IF EXISTS isel.validation_token;

--------------------------
-- Drop Auxiliary Types
--------------------------

DROP TYPE IF EXISTS isel.term_type;

DROP TYPE IF EXISTS isel.action_type;

DROP TYPE IF EXISTS isel.course_misc_unit_type;

DROP TYPE IF EXISTS isel.class_misc_unit_type;

DROP TYPE IF EXISTS isel.exam_type;

DROP TYPE IF EXISTS isel.weekday;

DROP SCHEMA isel;

