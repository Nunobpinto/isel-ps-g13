--------------------------
-- Delete Report Tables
--------------------------

DELETE FROM isel.organization_report;

DELETE FROM isel.programme_report;

DELETE FROM isel.course_report;

DELETE FROM isel.course_programme_report;

DELETE FROM isel.class_report;

DELETE FROM isel.work_assignment_report;

DELETE FROM isel.exam_report;

DELETE FROM isel.lecture_report;

DELETE FROM isel.homework_report;

DELETE FROM isel.user_report;

DELETE FROM isel.course_class_report;

--------------------------
-- Delete Staged Tables
--------------------------

DELETE FROM isel.programme_stage;

DELETE FROM isel.course_stage;

DELETE FROM isel.course_programme_stage;

DELETE FROM isel.class_stage;

DELETE FROM isel.course_class_stage;

DELETE FROM isel.work_assignment_stage;

DELETE FROM isel.exam_stage;

DELETE FROM isel.lecture_stage;

DELETE FROM isel.homework_stage;

DELETE FROM isel.course_misc_unit_stage;

DELETE FROM isel.class_misc_unit_stage;

--------------------------
-- Delete Version Tables
--------------------------

DELETE FROM isel.organization_version;

DELETE FROM isel.programme_version;

DELETE FROM isel.course_version;

DELETE FROM isel.course_programme_version;

DELETE FROM isel.class_version;

DELETE FROM isel.work_assignment_version;

DELETE FROM isel.exam_version;

DELETE FROM isel.lecture_version;

DELETE FROM isel.homework_version;

--------------------------
-- Delete Main Tables
--------------------------

DELETE FROM isel.course_programme;

DELETE FROM isel.validation_token;

DELETE FROM isel.work_assignment;

DELETE FROM isel.exam;

DELETE FROM isel.lecture;

DELETE FROM isel.homework;

DELETE FROM isel.reputation_log;

DELETE FROM isel.action_log;

DELETE FROM isel.class_misc_unit;

DELETE FROM isel.course_misc_unit;

DELETE FROM isel.user_course_class;

DELETE FROM isel.user_programme;

DELETE FROM isel.reputation;

DELETE FROM isel.course_term;

DELETE FROM isel.course_class;

DELETE FROM isel.class;

DELETE FROM isel.term;

DELETE FROM isel.programme;

DELETE FROM isel.course;

DELETE FROM isel.organization;

DELETE FROM isel.user_account;
