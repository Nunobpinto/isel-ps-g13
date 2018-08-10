--------------------------
-- Drop Report Tables
--------------------------

DROP TABLE IF EXISTS validation_token;

DROP TABLE IF EXISTS organization_report;

DROP TABLE IF EXISTS programme_report;

DROP TABLE IF EXISTS course_report;

DROP TABLE IF EXISTS course_programme_report;

DROP TABLE IF EXISTS class_report;

DROP TABLE IF EXISTS work_assignment_report;

DROP TABLE IF EXISTS exam_report;

DROP TABLE IF EXISTS lecture_report;

DROP TABLE IF EXISTS homework_report;

DROP TABLE IF EXISTS user_report;

DROP TABLE IF EXISTS course_class_report;

--------------------------
-- Drop Staged Tables
--------------------------

DROP TABLE IF EXISTS programme_stage;

DROP TABLE IF EXISTS course_stage;

DROP TABLE IF EXISTS course_programme_stage;

DROP TABLE IF EXISTS class_stage;

DROP TABLE IF EXISTS course_class_stage;

DROP TABLE IF EXISTS work_assignment_stage;

DROP TABLE IF EXISTS exam_stage;

DROP TABLE IF EXISTS lecture_stage;

DROP TABLE IF EXISTS homework_stage;

DROP TABLE IF EXISTS course_misc_unit_stage;

DROP TABLE IF EXISTS class_misc_unit_stage;

--------------------------
-- Drop Version Tables
--------------------------

DROP TABLE IF EXISTS organization_version;

DROP TABLE IF EXISTS programme_version;

DROP TABLE IF EXISTS course_version;

DROP TABLE IF EXISTS course_programme_version;

DROP TABLE IF EXISTS class_version;

DROP TABLE IF EXISTS work_assignment_version;

DROP TABLE IF EXISTS exam_version;

DROP TABLE IF EXISTS lecture_version;

DROP TABLE IF EXISTS homework_version;

--------------------------
-- Drop Main Tables
--------------------------

DROP TABLE IF EXISTS resource_validator;  

DROP TABLE IF EXISTS confirm;

DROP TABLE IF EXISTS resource;

DROP TABLE IF EXISTS course_programme;

DROP TABLE IF EXISTS work_assignment;

DROP TABLE IF EXISTS exam;

DROP TABLE IF EXISTS lecture;

DROP TABLE IF EXISTS homework;

DROP TABLE IF EXISTS reputation_log;

DROP TABLE IF EXISTS action_log;

DROP TABLE IF EXISTS class_misc_unit;

DROP TABLE IF EXISTS course_misc_unit;

DROP TABLE IF EXISTS user_course_class;

DROP TABLE IF EXISTS user_programme;

DROP TABLE IF EXISTS reputation;

DROP TABLE IF EXISTS reputation_matcher;

DROP TABLE IF EXISTS reputation_role;

DROP TABLE IF EXISTS course_term;

DROP TABLE IF EXISTS course_class;

DROP TABLE IF EXISTS class;

DROP TABLE IF EXISTS term;

DROP TABLE IF EXISTS programme;

DROP TABLE IF EXISTS course;

DROP TABLE IF EXISTS organization;

DROP TABLE IF EXISTS user_account;

--------------------------
-- Drop Auxiliary Types
--------------------------

DROP TYPE IF EXISTS term_type;

DROP TYPE IF EXISTS action_type;

DROP TYPE IF EXISTS course_misc_unit_type;

DROP TYPE IF EXISTS class_misc_unit_type;

DROP TYPE IF EXISTS exam_type;

DROP TYPE IF EXISTS weekday;
