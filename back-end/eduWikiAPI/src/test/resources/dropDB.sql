--------------------------
-- Drop Report Tables
--------------------------

Drop Table IF EXISTS organization_report;

Drop Table IF EXISTS programme_report;

Drop Table IF EXISTS course_report;

DROP TABLE IF EXISTS course_programme_report;

Drop Table IF EXISTS class_report;

Drop Table IF EXISTS work_assignment_report;

Drop Table IF EXISTS exam_report;

Drop Table IF EXISTS lecture_report;

Drop Table IF EXISTS homework_report;

Drop Table IF EXISTS student_report;

--------------------------
-- Drop Staged Tables
--------------------------

Drop Table IF EXISTS programme_stage;

Drop Table IF EXISTS course_stage;

Drop Table IF EXISTS course_programme_stage;

Drop Table IF EXISTS class_stage;

Drop Table IF EXISTS course_class_stage;

Drop Table IF EXISTS work_assignment_stage;

Drop Table IF EXISTS exam_stage;

Drop Table IF EXISTS lecture_stage;

Drop Table IF EXISTS homework_stage;

Drop Table IF EXISTS course_misc_unit_stage;

Drop Table IF EXISTS class_misc_unit_stage;

--------------------------
-- Drop Version Tables
--------------------------

Drop Table IF EXISTS organization_version;

Drop Table IF EXISTS programme_version;

Drop Table IF EXISTS course_version;

DROP TABLE IF EXISTS course_programme_version;

Drop Table IF EXISTS class_version;

Drop Table IF EXISTS work_assignment_version;

Drop Table IF EXISTS exam_version;

Drop Table IF EXISTS lecture_version;

Drop Table IF EXISTS homework_version;

--------------------------
-- Drop Main Tables
--------------------------

Drop Table IF EXISTS course_programme;

Drop Table IF EXISTS work_assignment;

Drop Table IF EXISTS exam;

Drop Table IF EXISTS lecture;

Drop Table IF EXISTS homework;

Drop Table IF EXISTS reputation_log;

Drop Table IF EXISTS class_misc_unit;

Drop Table IF EXISTS course_misc_unit;

Drop Table IF EXISTS student_course_class;

Drop Table IF EXISTS reputation;

Drop Table IF EXISTS course_term;

Drop Table IF EXISTS course_class;

Drop Table IF EXISTS class;

Drop Table IF EXISTS term;

Drop Table IF EXISTS programme;

Drop Table IF EXISTS course;

Drop Table IF EXISTS organization;

Drop Table IF EXISTS student;

--------------------------
-- Drop Auxiliary Types
--------------------------

Drop Type IF EXISTS term_type;

Drop Type IF EXISTS student_rank;

Drop Type IF EXISTS course_misc_unit_type;

Drop Type IF EXISTS class_misc_unit_type;

Drop Type IF EXISTS exam_type;

Drop Type IF EXISTS weekday;

Drop Type IF EXISTS gender;

