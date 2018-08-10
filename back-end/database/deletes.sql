--------------------------
-- Delete Report 
--------------------------

DELETE FROM validation_token;

DELETE FROM organization_report;

DELETE FROM programme_report;

DELETE FROM course_report;

DELETE FROM course_programme_report;

DELETE FROM class_report;

DELETE FROM work_assignment_report;

DELETE FROM exam_report;

DELETE FROM lecture_report;

DELETE FROM homework_report;

DELETE FROM user_report;

DELETE FROM course_class_report;

--------------------------
-- Delete Staged 
--------------------------

DELETE FROM programme_stage;

DELETE FROM course_stage;

DELETE FROM course_programme_stage;

DELETE FROM class_stage;

DELETE FROM course_class_stage;

DELETE FROM work_assignment_stage;

DELETE FROM exam_stage;

DELETE FROM lecture_stage;

DELETE FROM homework_stage;

DELETE FROM course_misc_unit_stage;

DELETE FROM class_misc_unit_stage;

--------------------------
-- Delete Version from
--------------------------

DELETE FROM organization_version;

DELETE FROM programme_version;

DELETE FROM course_version;

DELETE FROM course_programme_version;

DELETE FROM class_version;

DELETE FROM work_assignment_version;

DELETE FROM exam_version;

DELETE FROM lecture_version;

DELETE FROM homework_version;

--------------------------
-- Delete Main from
--------------------------

DELETE FROM resource_validator;  

DELETE FROM confirm;

DELETE FROM resource;

DELETE FROM course_programme;

DELETE FROM work_assignment;

DELETE FROM exam;

DELETE FROM lecture;

DELETE FROM homework;

DELETE FROM reputation_log;

DELETE FROM action_log;

DELETE FROM class_misc_unit;

DELETE FROM course_misc_unit;

DELETE FROM user_course_class;

DELETE FROM user_programme;

DELETE FROM reputation;

DELETE FROM reputation_matcher;

DELETE FROM reputation_role;

DELETE FROM course_term;

DELETE FROM course_class;

DELETE FROM class;

DELETE FROM term;

DELETE FROM programme;

DELETE FROM course;

DELETE FROM organization;

DELETE FROM user_account;
