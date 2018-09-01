CREATE SCHEMA IF NOT EXISTS isel;

--------------------------
-- Auxiliary Types
--------------------------

CREATE TYPE isel.term_type AS ENUM ('WINTER', 'SUMMER');

CREATE TYPE isel.course_misc_unit_type AS ENUM ('WORK_ASSIGNMENT', 'EXAM_TEST');

CREATE TYPE isel.class_misc_unit_type AS ENUM ('HOMEWORK', 'LECTURE');

CREATE TYPE isel.exam_type AS ENUM ('EXAM', 'TEST');

CREATE TYPE isel.weekday AS ENUM ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY');

CREATE TYPE isel.action_Type AS ENUM ('CREATE', 'ALTER', 'DELETE', 'VOTE_UP', 'VOTE_DOWN', 'APPROVE_REPORT', 'APPROVE_STAGE', 'REJECT_REPORT', 'REJECT_STAGE');

--------------------------
-- Create Main Tables
--------------------------

CREATE TABLE IF NOT EXISTS isel.validation_token (
  token_id SERIAL,
  token UUID NOT NULL,
  validation_date TIMESTAMP NOT NULL,
  PRIMARY KEY (token_id)
);


CREATE TABLE IF NOT EXISTS isel.organization (
  organization_id CHAR(1) DEFAULT 'X' CHECK(organization_id='X'),
  organization_version INTEGER NOT NULL DEFAULT 1,
  organization_full_name VARCHAR(100) NOT NULL,
  organization_short_name VARCHAR(10) NOT NULL,
  organization_address VARCHAR (100) NOT NULL,
  organization_contact VARCHAR (20) NOT NULL,
  organization_website VARCHAR (50) NOT NULL,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (organization_id)
);

CREATE TABLE IF NOT EXISTS isel.programme (
  programme_id SERIAL,
  programme_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20) NOT NULL,
  programme_full_name VARCHAR(100) UNIQUE NOT NULL,
  programme_short_name VARCHAR(10) UNIQUE NOT NULL,
  programme_academic_degree VARCHAR(50) NOT NULL,
  programme_total_credits INTEGER NOT NULL,
  programme_duration INTEGER NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (programme_id)
);

-- Add column column restricting the terms where it can be lectured (winter only, summer only, both)
CREATE TABLE IF NOT EXISTS isel.course (
  course_id SERIAL,
  course_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20) NOT NULL,
  course_full_name VARCHAR(100) UNIQUE NOT NULL,
  course_short_name VARCHAR(10) UNIQUE NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (course_id)
);

CREATE TABLE IF NOT EXISTS isel.course_programme (
  course_id INTEGER REFERENCES isel.course ON DELETE CASCADE,
  programme_id INTEGER REFERENCES isel.programme ON DELETE CASCADE,
  course_programme_version INTEGER NOT NULL DEFAULT 1,
  course_lectured_term varchar(50) NOT NULL,
  course_optional BOOLEAN NOT NULL,
  course_credits INTEGER NOT NULL,
  time_stamp timestamp NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  log_id SERIAL,
  PRIMARY KEY (course_id, programme_id)
);

CREATE TABLE IF NOT EXISTS isel.term (
  term_id SERIAL,
  term_short_name CHAR(5) UNIQUE NOT NULL,
  term_year INTEGER NOT NULL,
  term_type isel.term_type NOT NULL,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (term_id)
);

CREATE TABLE IF NOT EXISTS isel.class (
  class_id SERIAL,
  class_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20) NOT NULL,
  class_name VARCHAR(10) NOT NULL,
  programme_id INTEGER REFERENCES isel.programme NOT NULL,
  term_id INTEGER REFERENCES isel.term ON DELETE RESTRICT,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (class_id, term_id)
);

CREATE TABLE IF NOT EXISTS isel.course_term (
  course_id INTEGER REFERENCES isel.course ON DELETE CASCADE,
  term_id INTEGER REFERENCES isel.term ON DELETE RESTRICT,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (course_id, term_id)
);

CREATE TABLE IF NOT EXISTS isel.course_misc_unit (
  course_misc_unit_id SERIAL,
  misc_type isel.course_misc_unit_type NOT NULL,
  course_id INTEGER,
  term_id INTEGER,
  FOREIGN KEY (course_id, term_id) REFERENCES isel.course_term(course_id, term_id) ON DELETE CASCADE,
  PRIMARY KEY (course_misc_unit_id)
);

CREATE TABLE IF NOT EXISTS isel.work_assignment (
  work_assignment_id INTEGER REFERENCES isel.course_misc_unit ON DELETE CASCADE,
  work_assignment_version INTEGER NOT NULL DEFAULT 1,
  phase VARCHAR(20) NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  sheet_id UUID,
  supplement_id UUID,
  due_date date NOT NULL,
  individual BOOLEAN NOT NULL,
  late_delivery BOOLEAN NOT NULL,
  multiple_deliveries BOOLEAN NOT NULL,
  requires_report BOOLEAN NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (work_assignment_id)
);

CREATE TABLE IF NOT EXISTS isel.exam (
  exam_id INTEGER REFERENCES isel.course_misc_unit ON DELETE CASCADE,
  exam_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20) NOT NULL,
  sheet_id UUID,
  due_date date NOT NULL,
  exam_type isel.exam_type NOT NULL,
  phase VARCHAR(20) NOT NULL,
  location VARCHAR(30) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (exam_id)
);

CREATE TABLE IF NOT EXISTS isel.course_class (
  course_class_id SERIAL,
  course_id INTEGER REFERENCES isel.course ON DELETE CASCADE NOT NULL,
  class_id INTEGER NOT NULL,
  term_id INTEGER NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  FOREIGN KEY (class_id, term_id) REFERENCES isel.class(class_id, term_id) ON DELETE CASCADE,
  PRIMARY KEY (course_class_id)
);

CREATE TABLE IF NOT EXISTS isel.class_misc_unit (
  class_misc_unit_id SERIAL,
  misc_type isel.class_misc_unit_type,
  course_class_id INTEGER REFERENCES isel.course_class ON DELETE CASCADE,
  PRIMARY KEY (class_misc_unit_id)
);

CREATE TABLE IF NOT EXISTS isel.lecture (
  lecture_id INTEGER REFERENCES isel.class_misc_unit ON DELETE CASCADE,
  lecture_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20) NOT NULL,
  weekday isel.weekday NOT NULL,
  begins TIME NOT NULL,
  duration INTERVAL NOT NULL,
  location varchar(30) NOT NULL,
  votes INTEGER DEFAULT 0 NOT NULL,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (lecture_id)
);

CREATE TABLE IF NOT EXISTS isel.homework (
  homework_id INTEGER REFERENCES isel.class_misc_unit ON DELETE CASCADE,
  homework_version INTEGER NOT NULL DEFAULT 1,
  homework_name VARCHAR(20) NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  sheet_id UUID,
  due_date DATE NOT NULL,
  late_delivery BOOLEAN NOT NULL,
  multiple_deliveries BOOLEAN NOT NULL,
  votes INTEGER DEFAULT 0 NOT NULL,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (homework_id)
);

CREATE TABLE IF NOT EXISTS isel.user_account (
  user_username VARCHAR(20),
  user_password VARCHAR(60),
  user_given_name VARCHAR(15) NOT NULL,
  user_family_name VARCHAR(15) NOT NULL,
  user_locked BOOLEAN NOT NULL,
  user_confirmed BOOLEAN NOT NULL,
  user_email varchar(35) UNIQUE NOT NULL,
  PRIMARY KEY (user_username)
);

CREATE TABLE IF NOT EXISTS isel.reputation (
  reputation_id SERIAL,
  points INTEGER NOT NULL,
  role VARCHAR NOT NULL,
  user_username varchar(20) REFERENCES isel.user_account ON DELETE CASCADE,
  PRIMARY KEY (reputation_id, user_username)
);

CREATE TABLE IF NOT EXISTS isel.action_log (
  action_id SERIAL,
  user_username VARCHAR(20) REFERENCES isel.user_account ON DELETE CASCADE,
  action isel.action_type NOT NULL,
  entity VARCHAR NOT NULL,
  log_id INTEGER,
  time_stamp TIMESTAMP NOT NULL,
  PRIMARY KEY(action_id)
);

CREATE TABLE IF NOT EXISTS isel.reputation_log (
  reputation_log_id SERIAL,
  reputation_log_action INTEGER REFERENCES isel.action_log,
  reputation_log_given_by varchar(15) NOT NULL,
  reputation_log_points INTEGER NOT NULL,
  reputation_id INTEGER,
  user_username VARCHAR(20),
  FOREIGN KEY (reputation_id, user_username) REFERENCES isel.reputation(reputation_id, user_username) ON DELETE CASCADE,
  PRIMARY KEY (reputation_log_id)
);

CREATE TABLE IF NOT EXISTS isel.user_course_class (
  user_username VARCHAR(20) REFERENCES isel.user_account ON DELETE CASCADE,
  course_id INTEGER REFERENCES isel.course ON DELETE CASCADE,
  course_class_id INTEGER REFERENCES isel.course_class ON DELETE SET NULL DEFAULT NULL,
  PRIMARY KEY (user_username, course_id)
);

CREATE TABLE IF NOT EXISTS isel.user_programme (
  user_username VARCHAR(20) REFERENCES isel.user_account ON DELETE CASCADE,
  programme_id INTEGER REFERENCES isel.programme ON DELETE CASCADE,
  PRIMARY KEY (user_username)
);

--------------------------
-- Create Stage Tables
--------------------------

CREATE TABLE IF NOT EXISTS isel.programme_stage (
  programme_stage_id SERIAL,
  programme_full_name VARCHAR(100) NOT NULL,
  programme_short_name VARCHAR(10) NOT NULL,
  programme_academic_degree VARCHAR(50) NOT NULL,
  programme_total_credits INTEGER NOT NULL,
  programme_duration INTEGER NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (programme_stage_id)
);

CREATE TABLE IF NOT EXISTS isel.course_stage (
  course_stage_id SERIAL,
  course_full_name VARCHAR(100) NOT NULL,
  course_short_name VARCHAR(10) NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (course_stage_id)
);

CREATE TABLE IF NOT EXISTS isel.course_programme_stage (
  course_programme_stage_id SERIAL,
  course_id INTEGER REFERENCES isel.course ON DELETE CASCADE,
  programme_id INTEGER REFERENCES isel.programme ON DELETE CASCADE,
  course_lectured_term VARCHAR(50) NOT NULL,
  course_optional BOOLEAN NOT NULL,
  course_credits INTEGER NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (course_programme_stage_id)
);

CREATE TABLE IF NOT EXISTS isel.class_stage (
  class_stage_id SERIAL,
  term_id INTEGER REFERENCES isel.term ON DELETE CASCADE,
  programme_id INTEGER REFERENCES isel.programme NOT NULL,
  class_name VARCHAR(10) NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (class_stage_id, term_id)
);

CREATE TABLE IF NOT EXISTS isel.course_class_stage (
  course_class_stage_id SERIAL,
  course_id INTEGER REFERENCES isel.course ON DELETE CASCADE NOT NULL,
  class_id INTEGER NOT NULL,
  term_id INTEGER NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  FOREIGN KEY (class_id, term_id) REFERENCES isel.class(class_id, term_id) ON DELETE CASCADE,
  PRIMARY KEY (course_class_stage_id)
);

CREATE TABLE IF NOT EXISTS isel.course_misc_unit_stage (
  course_misc_unit_stage_id SERIAL,
  course_id INTEGER,
  term_id INTEGER,
  misc_type isel.course_misc_unit_type NOT NULL,
  FOREIGN KEY (course_id, term_id) REFERENCES isel.course_term(course_id, term_id) ON DELETE CASCADE,
  PRIMARY KEY (course_misc_unit_stage_id)
);

CREATE TABLE IF NOT EXISTS isel.class_misc_unit_stage (
  class_misc_unit_stage_id SERIAL,
  misc_type isel.class_misc_unit_type NOT NULL,
  course_class_id INTEGER REFERENCES isel.course_class ON DELETE CASCADE,
  FOREIGN KEY (course_class_id) REFERENCES isel.course_class ON DELETE CASCADE,
  PRIMARY KEY (class_misc_unit_stage_id)
);

CREATE TABLE IF NOT EXISTS isel.work_assignment_stage (
  work_assignment_stage_id INTEGER REFERENCES isel.course_misc_unit_stage ON DELETE CASCADE,
  sheet_id UUID,
  phase VARCHAR(20) NOT NULL,
  supplement_id UUID,
  due_date date NOT NULL,
  individual BOOLEAN NOT NULL,
  late_delivery BOOLEAN NOT NULL,
  multiple_deliveries BOOLEAN NOT NULL,
  requires_report BOOLEAN NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (work_assignment_stage_id)
);

CREATE TABLE IF NOT EXISTS isel.exam_stage (
  exam_stage_id INTEGER REFERENCES isel.course_misc_unit_stage ON DELETE CASCADE,
  sheet_id UUID,
  due_date date NOT NULL,
  exam_type isel.exam_type NOT NULL,
  phase VARCHAR(20) NOT NULL,
  location VARCHAR(30) NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (exam_stage_id)
);

CREATE TABLE IF NOT EXISTS isel.lecture_stage (
  lecture_stage_id INTEGER REFERENCES isel.class_misc_unit_stage ON DELETE CASCADE,
  weekday isel.weekday NOT NULL,
  begins TIME NOT NULL,
  duration INTERVAL NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  time_stamp timestamp NOT NULL,
  votes INTEGER DEFAULT 0,
  location VARCHAR(30) NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (lecture_stage_id)
);

CREATE TABLE IF NOT EXISTS isel.homework_stage (
  homework_stage_id INTEGER REFERENCES isel.class_misc_unit_stage ON DELETE CASCADE,
  sheet_id UUID,
  homework_name VARCHAR(20) NOT NULL,
  due_date DATE NOT NULL,
  late_delivery BOOLEAN NOT NULL,
  multiple_deliveries BOOLEAN NOT NULL,
  time_stamp timestamp NOT NULL,
  votes INTEGER DEFAULT 0,
  created_by VARCHAR(20) NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (homework_stage_id)
);

--------------------------
-- Create Report Tables
--------------------------

CREATE TABLE IF NOT EXISTS isel.organization_report (
  organization_report_id SERIAL,
  organization_full_name varchar(100),
  organization_short_name varchar(10),
  organization_address varchar(100),
  organization_contact varchar(20),
  organization_website VARCHAR (50),
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (organization_report_id)
);

CREATE TABLE IF NOT EXISTS isel.programme_report (
  programme_report_id SERIAL,
  programme_id INTEGER REFERENCES isel.programme ON DELETE CASCADE,
  programme_full_name varchar(100),
  programme_short_name varchar(10),
  programme_academic_degree varchar(50),
  programme_total_credits INTEGER,
  programme_duration INTEGER,
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (programme_report_id)
);

CREATE TABLE IF NOT EXISTS isel.course_programme_report (
  course_programme_report_id SERIAL,
  course_id INTEGER NOT NULL,
  programme_id INTEGER NOT NULL,
  course_lectured_term varchar(50),
  course_optional BOOLEAN,
  course_credits INTEGER,
  to_delete BOOLEAN,
  time_stamp timestamp NOT NULL,
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  log_id SERIAL,
  FOREIGN KEY (course_id, programme_id) REFERENCES isel.course_programme(course_id, programme_id) ON DELETE CASCADE,
  PRIMARY KEY (course_programme_report_id)
);

CREATE TABLE IF NOT EXISTS isel.course_report (
  course_report_id SERIAL,
  course_id INTEGER REFERENCES isel.course ON DELETE CASCADE,
  course_full_name varchar(100),
  course_short_name varchar(10),
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (course_report_id)
);

CREATE TABLE IF NOT EXISTS isel.class_report (
  class_report_id SERIAL,
  class_id INTEGER NOT NULL,
  term_id INTEGER NOT NULL,
  programme_id INTEGER,
  class_name VARCHAR(10),
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  FOREIGN KEY (class_id, term_id) REFERENCES isel.class(class_id, term_id) ON DELETE CASCADE,
  PRIMARY KEY (class_report_id)
);

CREATE TABLE IF NOT EXISTS isel.course_class_report (
  course_class_report_id SERIAL,
  course_class_id INTEGER REFERENCES isel.course_class NOT NULL,
  course_id INTEGER,
  class_id INTEGER,
  term_id INTEGER,
  to_delete BOOLEAN,
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (course_class_report_id)
);

CREATE TABLE IF NOT EXISTS isel.work_assignment_report (
  work_assignment_report_id SERIAL,
  work_assignment_id INTEGER REFERENCES isel.course_misc_unit ON DELETE CASCADE,
  sheet_id UUID,
  phase VARCHAR(20) NOT NULL,
  supplement_id UUID,
  due_date date,
  individual BOOLEAN,
  late_delivery BOOLEAN,
  multiple_deliveries BOOLEAN,
  requires_report BOOLEAN,
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (work_assignment_report_id)
);

CREATE TABLE IF NOT EXISTS isel.exam_report (
  exam_report_id SERIAL,
  exam_id INTEGER REFERENCES isel.course_misc_unit ON DELETE CASCADE,
  sheet_id UUID,
  due_date date,
  exam_type isel.exam_type,
  phase VARCHAR(20),
  location varchar(30),
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (exam_report_id)
);

CREATE TABLE IF NOT EXISTS isel.lecture_report (
  lecture_report_id SERIAL,
  lecture_id INTEGER REFERENCES isel.class_misc_unit ON DELETE CASCADE,
  weekday isel.weekday,
  begins TIME,
  duration INTERVAL,
  location varchar(30),
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (lecture_report_id)
);

CREATE TABLE IF NOT EXISTS isel.homework_report (
  homework_report_id SERIAL,
  homework_id INTEGER REFERENCES isel.class_misc_unit ON DELETE CASCADE,
  homework_name VARCHAR(20),
  sheet_id UUID,
  due_date DATE,
  late_delivery BOOLEAN,
  multiple_deliveries BOOLEAN,
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (homework_report_id)
);

CREATE TABLE IF NOT EXISTS isel.user_report (
  report_id SERIAL,
  user_username VARCHAR(20) REFERENCES isel.user_account ON DELETE CASCADE,
  reason VARCHAR(200) NOT NULL,
  reported_by VARCHAR(20) NOT NULL,
  time_stamp timestamp NOT NULL,
  log_id SERIAL,
  PRIMARY KEY (report_id)
);

--------------------------
-- Create Version Tables
--------------------------

CREATE TABLE IF NOT EXISTS isel.organization_version (
  organization_version INTEGER,
  created_by VARCHAR(20) NOT NULL,
  organization_full_name VARCHAR(100) NOT NULL,
  organization_short_name VARCHAR(10) NOT NULL,
  organization_address VARCHAR(100) NOT NULL,
  organization_contact VARCHAR(20) NOT NULL,
  organization_website VARCHAR (50) NOT NULL,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (organization_version)
);


CREATE TABLE IF NOT EXISTS isel.programme_version (
  programme_id INTEGER,
  programme_version INTEGER,
  programme_full_name VARCHAR(100) NOT NULL,
  programme_short_name VARCHAR(10) NOT NULL,
  programme_academic_degree VARCHAR(50) NOT NULL,
  programme_total_credits INTEGER NOT NULL,
  programme_duration INTEGER NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (programme_id, programme_version)
);

CREATE TABLE IF NOT EXISTS isel.course_version (
  course_id INTEGER,
  course_version INTEGER,
  course_full_name VARCHAR(100) NOT NULL,
  course_short_name VARCHAR(10) NOT NULL,
  created_by VARCHAR(20),
  time_stamp timestamp,
  PRIMARY KEY (course_id, course_version)
);

CREATE TABLE IF NOT EXISTS isel.course_programme_version (
  course_id INTEGER,
  programme_id INTEGER,
  course_programme_version INTEGER,
  course_lectured_term varchar(50) NOT NULL,
  course_optional BOOLEAN NOT NULL,
  course_credits INTEGER NOT NULL,
  time_stamp timestamp NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  PRIMARY KEY (course_id, programme_id, course_programme_version)
);

CREATE TABLE IF NOT EXISTS isel.class_version (
  class_id INTEGER,
  term_id INTEGER,
  class_version INTEGER,
  programme_id INTEGER NOT NULL,
  class_name VARCHAR(10) NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (class_id, term_id, class_version)
);

CREATE TABLE IF NOT EXISTS isel.work_assignment_version (
  work_assignment_id INTEGER,
  work_assignment_version INTEGER,
  sheet_id UUID,
  phase VARCHAR(20) NOT NULL,
  supplement_id UUID,
  due_date date NOT NULL,
  individual BOOLEAN NOT NULL,
  late_delivery BOOLEAN NOT NULL,
  multiple_deliveries BOOLEAN NOT NULL,
  requires_report BOOLEAN NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (work_assignment_id, work_assignment_version)
);

CREATE TABLE IF NOT EXISTS isel.exam_version (
  exam_id INTEGER,
  exam_version INTEGER,
  sheet_id UUID,
  due_date date NOT NULL,
  exam_type isel.exam_type NOT NULL,
  phase VARCHAR(20) NOT NULL,
  location varchar(30) NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (exam_id, exam_version)
);

CREATE TABLE IF NOT EXISTS isel.lecture_version (
  lecture_id INTEGER,
  lecture_version INTEGER,
  created_by VARCHAR(20) NOT NULL,
  weekday isel.weekday NOT NULL,
  begins TIME NOT NULL,
  duration INTERVAL NOT NULL,
  time_stamp timestamp NOT NULL,
  location varchar(30) NOT NULL,
  PRIMARY KEY (lecture_id, lecture_version)
);

CREATE TABLE IF NOT EXISTS isel.homework_version (
  homework_id INTEGER,
  homework_version INTEGER,
  sheet_id UUID,
  homework_name VARCHAR(20) NOT NULL,
  due_date DATE NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  late_delivery BOOLEAN NOT NULL,
  time_stamp timestamp NOT NULL,
  multiple_deliveries BOOLEAN NOT NULL,
  PRIMARY KEY (homework_id, homework_version)
);

