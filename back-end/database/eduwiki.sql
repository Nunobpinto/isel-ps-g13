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
-- Create Main Tables
--------------------------

CREATE TABLE IF NOT EXISTS validation_token (
	token_id SERIAL,
	token UUID NOT NULL,
	validation_date TIMESTAMP NOT NULL,
	PRIMARY KEY (token_id)
);


CREATE TABLE IF NOT EXISTS organization (
  organization_id SERIAL,
  organization_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20) NOT NULL,
  organization_full_name VARCHAR(100) NOT NULL,
  organization_short_name VARCHAR(10) NOT NULL,
  organization_address VARCHAR (100) NOT NULL,
  organization_contact VARCHAR (15) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (organization_id)
);

CREATE TABLE IF NOT EXISTS programme (
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
  PRIMARY KEY (programme_id)
);

-- Add column column restricting the terms where it can be lectured (winter only, summer only, both)
CREATE TABLE IF NOT EXISTS course (
  course_id SERIAL,
  organization_id INTEGER REFERENCES organization ON DELETE CASCADE,
  course_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20) NOT NULL,
  course_full_name VARCHAR(100) UNIQUE NOT NULL,
  course_short_name VARCHAR(10) UNIQUE NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (course_id)
);

CREATE TABLE IF NOT EXISTS course_programme (
  course_id INTEGER REFERENCES course ON DELETE CASCADE,
  programme_id INTEGER REFERENCES programme ON DELETE CASCADE,
  course_programme_version INTEGER NOT NULL DEFAULT 1,
  course_lectured_term varchar(50) NOT NULL,
  course_optional BOOLEAN NOT NULL,
  course_credits INTEGER NOT NULL,
  time_stamp timestamp NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  PRIMARY KEY (course_id, programme_id)
);

CREATE TABLE IF NOT EXISTS term (
  term_id SERIAL,
  term_short_name CHAR(5) UNIQUE NOT NULL,
  term_year INTEGER NOT NULL,
  term_type term_type NOT NULL,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (term_id)
);

CREATE TABLE IF NOT EXISTS class (
  class_id SERIAL,
  class_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20) NOT NULL,
  class_name VARCHAR(10) NOT NULL,
  term_id INTEGER REFERENCES term ON DELETE RESTRICT,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (class_id, term_id)
);

CREATE TABLE IF NOT EXISTS course_term (
  course_id INTEGER REFERENCES course ON DELETE CASCADE,
  term_id INTEGER REFERENCES term ON DELETE RESTRICT,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (course_id, term_id)
);

CREATE TABLE IF NOT EXISTS course_misc_unit (
  course_misc_unit_id SERIAL,
  misc_type course_misc_unit_type NOT NULL,
  course_id INTEGER,
  term_id INTEGER,
  FOREIGN KEY (course_id, term_id) REFERENCES course_term(course_id, term_id) ON DELETE CASCADE,
  PRIMARY KEY (course_misc_unit_id)
);

CREATE TABLE IF NOT EXISTS work_assignment (
  work_assignment_id INTEGER REFERENCES course_misc_unit ON DELETE CASCADE,
  work_assignment_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20) NOT NULL,
  sheet_id UUID NOT NULL,
  supplement_id UUID,
  due_date date NOT NULL,
  individual BOOLEAN NOT NULL,
  late_delivery BOOLEAN NOT NULL,
  multiple_deliveries BOOLEAN NOT NULL,
  requires_report BOOLEAN NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (work_assignment_id)
);

CREATE TABLE IF NOT EXISTS exam (
  exam_id INTEGER REFERENCES course_misc_unit ON DELETE CASCADE,
  exam_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20) NOT NULL,
  sheet_id UUID NOT NULL,
  due_date date NOT NULL,
  exam_type exam_type NOT NULL,
  phase VARCHAR(30) NOT NULL,
  location VARCHAR(30) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (exam_id)
);

CREATE TABLE IF NOT EXISTS course_class (
  course_class_id SERIAL, 
  course_id INTEGER REFERENCES course ON DELETE CASCADE NOT NULL,
  class_id INTEGER NOT NULL,
  term_id INTEGER NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  FOREIGN KEY (class_id, term_id) REFERENCES class(class_id, term_id) ON DELETE CASCADE,
  PRIMARY KEY (course_class_id)
);

CREATE TABLE IF NOT EXISTS class_misc_unit (
  class_misc_unit_id SERIAL,
  misc_type class_misc_unit_type,
  course_class_id INTEGER REFERENCES course_class ON DELETE CASCADE,
  PRIMARY KEY (class_misc_unit_id)
);

CREATE TABLE IF NOT EXISTS lecture (
  lecture_id INTEGER REFERENCES class_misc_unit ON DELETE CASCADE,
  lecture_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20),
  weekday weekday,
  begins TIME,
  duration INTERVAL,
  location varchar(30),
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (lecture_id)
);

CREATE TABLE IF NOT EXISTS homework (
  homework_id INTEGER REFERENCES class_misc_unit ON DELETE CASCADE,
  homework_version INTEGER NOT NULL DEFAULT 1,  
  created_by VARCHAR(20),
  sheet_id UUID NOT NULL,
  due_date DATE,
  late_delivery BOOLEAN,
  multiple_deliveries BOOLEAN,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (homework_id)
);

CREATE TABLE IF NOT EXISTS user_account (
  user_username VARCHAR(20),
  user_password VARCHAR(50),
  user_given_name VARCHAR(15) NOT NULL,
  user_family_name VARCHAR(15) NOT NULL,
  user_confirmed BOOLEAN NOT NULL,
  user_personal_email varchar(35) UNIQUE NOT NULL,
  user_organization_email varchar(35) UNIQUE NOT NULL,
  PRIMARY KEY (user_username)
);

CREATE TABLE IF NOT EXISTS reputation_role (
  reputation_role_id VARCHAR,
  max_points INTEGER NOT NULL, 
  min_points INTEGER NOT NULL, 
  hierarchy_level INTEGER NOT NULL,
  PRIMARY KEY (reputation_role_id)
);

CREATE TABLE IF NOT EXISTS reputation_matcher (
  uri_match VARCHAR NOT NULL,
  reputation_role_id VARCHAR REFERENCES reputation_role,
  PRIMARY KEY (uri_match, reputation_role_id)
);

CREATE TABLE IF NOT EXISTS reputation (
  reputation_id SERIAL,
  points INTEGER NOT NULL,
  role VARCHAR REFERENCES reputation_role NOT NULL, 
  user_username varchar(20) REFERENCES user_account ON DELETE CASCADE,
  PRIMARY KEY (reputation_id, user_username)
);

CREATE TABLE IF NOT EXISTS action_log (
	action_id SERIAL,
	user_username VARCHAR(20) REFERENCES user_account ON DELETE CASCADE,
	action action_type NOT NULL,
	entity VARCHAR NOT NULL,
	log_id INTEGER NOT NULL,
	time_stamp TIMESTAMP NOT NULL,
	PRIMARY KEY(action_id)
);

CREATE TABLE IF NOT EXISTS reputation_log (
  reputation_log_id SERIAL,
  reputation_log_action INTEGER REFERENCES action_log,
  reputation_log_given_by varchar(15) NOT NULL,
  reputation_log_points INTEGER NOT NULL,
  reputation_id INTEGER,
  user_username VARCHAR(20),
  FOREIGN KEY (reputation_id, user_username) REFERENCES reputation(reputation_id, user_username) ON DELETE CASCADE,
  PRIMARY KEY (reputation_log_id)
);

CREATE TABLE IF NOT EXISTS user_course_class (
  user_username VARCHAR(20) REFERENCES user_account ON DELETE CASCADE,
  course_id INTEGER REFERENCES course ON DELETE CASCADE,
  course_class_id INTEGER REFERENCES course_class ON DELETE SET NULL,
  PRIMARY KEY (user_username, course_id)
);

CREATE TABLE IF NOT EXISTS user_programme (
  user_username VARCHAR(20) REFERENCES user_account ON DELETE CASCADE,
  programme_id INTEGER REFERENCES programme ON DELETE CASCADE,
  PRIMARY KEY (user_username)
);

CREATE TABLE IF NOT EXISTS resource (
   uuid UUID NOT NULL,
	byte_sequence bytea NOT NULL,
   content_type VARCHAR(100) NOT NULL,
   original_filename VARCHAR(50),
   size BIGINT,
   PRIMARY KEY(uuid)
);

--------------------------
-- Create Stage Tables
--------------------------

CREATE TABLE IF NOT EXISTS programme_stage (
  programme_stage_id SERIAL,
  programme_full_name VARCHAR(100) NOT NULL,
  programme_short_name VARCHAR(10) NOT NULL,
  programme_academic_degree VARCHAR(50) NOT NULL,
  programme_total_credits INTEGER NOT NULL,
  programme_duration INTEGER NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  log_id SERIAL UNIQUE NOT NULL,
  PRIMARY KEY (programme_stage_id)
);

CREATE TABLE IF NOT EXISTS course_stage (
  course_stage_id SERIAL,
  organization_id INTEGER REFERENCES organization ON DELETE CASCADE,
  course_full_name VARCHAR(100) NOT NULL,
  course_short_name VARCHAR(10) NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (course_stage_id)
);

CREATE TABLE IF NOT EXISTS course_programme_stage (
  course_programme_stage_id SERIAL,
  course_id INTEGER REFERENCES course ON DELETE CASCADE,
  programme_id INTEGER REFERENCES programme ON DELETE CASCADE,
  course_lectured_term VARCHAR(50) NOT NULL,
  course_optional BOOLEAN NOT NULL,
  course_credits INTEGER NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (course_programme_stage_id)
);

CREATE TABLE IF NOT EXISTS class_stage (
  class_stage_id SERIAL,
  term_id INTEGER REFERENCES term ON DELETE CASCADE,
  class_name VARCHAR(10) NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (class_stage_id, term_id)
);

CREATE TABLE IF NOT EXISTS course_class_stage (
  course_class_stage_id SERIAL,	
  course_id INTEGER REFERENCES course ON DELETE CASCADE NOT NULL,
  class_id INTEGER NOT NULL,
  term_id INTEGER NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  FOREIGN KEY (class_id, term_id) REFERENCES class(class_id, term_id) ON DELETE CASCADE,
  PRIMARY KEY (course_class_stage_id)
);

CREATE TABLE IF NOT EXISTS course_misc_unit_stage (
  course_misc_unit_stage_id SERIAL,
  course_id INTEGER,
  term_id INTEGER,
  misc_type course_misc_unit_type NOT NULL,
  FOREIGN KEY (course_id, term_id) REFERENCES course_term(course_id, term_id) ON DELETE CASCADE,
  PRIMARY KEY (course_misc_unit_stage_id)
);

CREATE TABLE IF NOT EXISTS class_misc_unit_stage (
  class_misc_unit_stage_id SERIAL,
  misc_type class_misc_unit_type NOT NULL,
  course_class_id INTEGER REFERENCES course_class ON DELETE CASCADE,
  FOREIGN KEY (course_class_id) REFERENCES course_class ON DELETE CASCADE,
  PRIMARY KEY (class_misc_unit_stage_id)
);

CREATE TABLE IF NOT EXISTS work_assignment_stage (
  work_assignment_stage_id INTEGER REFERENCES course_misc_unit_stage ON DELETE CASCADE,
  sheet_id UUID NOT NULL,
  supplement_id UUID NOT NULL,
  due_date date NOT NULL,
  individual BOOLEAN NOT NULL,
  late_delivery BOOLEAN NOT NULL,
  multiple_deliveries BOOLEAN NOT NULL,
  requires_report BOOLEAN NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (work_assignment_stage_id)
);

CREATE TABLE IF NOT EXISTS exam_stage (
  exam_stage_id INTEGER REFERENCES course_misc_unit_stage ON DELETE CASCADE,
  sheet_id UUID NOT NULL,
  due_date date NOT NULL,
  exam_type  exam_type NOT NULL,
  phase VARCHAR(30) NOT NULL,
  location VARCHAR(30) NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (exam_stage_id)
);

CREATE TABLE IF NOT EXISTS lecture_stage (
  lecture_stage_id INTEGER REFERENCES class_misc_unit_stage ON DELETE CASCADE,
  weekday weekday NOT NULL,
  begins TIME NOT NULL,
  duration INTERVAL NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  time_stamp timestamp NOT NULL,
  votes INTEGER DEFAULT 0,
  location VARCHAR(30) NOT NULL,
  PRIMARY KEY (lecture_stage_id)
);

CREATE TABLE IF NOT EXISTS homework_stage (
  homework_stage_id INTEGER REFERENCES class_misc_unit_stage ON DELETE CASCADE,
  sheet_id UUID NOT NULL,
  due_date DATE NOT NULL,
  late_delivery BOOLEAN NOT NULL,
  multiple_deliveries BOOLEAN NOT NULL,
  time_stamp timestamp NOT NULL,
  votes INTEGER DEFAULT 0,
  created_by VARCHAR(20) NOT NULL,
  PRIMARY KEY (homework_stage_id)
);

--------------------------
-- Create Report Tables
--------------------------

CREATE TABLE IF NOT EXISTS organization_report (
  organization_report_id SERIAL,
  organization_id INTEGER REFERENCES organization ON DELETE CASCADE,
  organization_full_name varchar(100),
  organization_short_name varchar(10),
  organization_address varchar(100),
  organization_contact varchar(15),
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (organization_report_id)
);

CREATE TABLE IF NOT EXISTS programme_report (
    programme_report_id SERIAL, 
    programme_id INTEGER REFERENCES programme ON DELETE CASCADE,
    programme_full_name varchar(100),
    programme_short_name varchar(10),
    programme_academic_degree varchar(50),
    programme_total_credits INTEGER,
    programme_duration INTEGER,
    reported_by VARCHAR(20) NOT NULL,
    votes INTEGER DEFAULT 0,
    time_stamp timestamp NOT NULL,
    PRIMARY KEY (programme_report_id)
);

CREATE TABLE IF NOT EXISTS course_programme_report (
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
	FOREIGN KEY (course_id, programme_id) REFERENCES course_programme(course_id, programme_id) ON DELETE CASCADE,
	PRIMARY KEY (course_programme_report_id)
);

CREATE TABLE IF NOT EXISTS course_report (
    course_report_id SERIAL, 
    course_id INTEGER REFERENCES course ON DELETE CASCADE,
    course_full_name varchar(100),
    course_short_name varchar(10),
    reported_by VARCHAR(20) NOT NULL,
    votes INTEGER DEFAULT 0,
    time_stamp timestamp NOT NULL,
    PRIMARY KEY (course_report_id)
);

CREATE TABLE IF NOT EXISTS class_report (
  class_report_id SERIAL,
  class_id INTEGER NOT NULL,
  term_id INTEGER NOT NULL,
  class_name VARCHAR(10),
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  FOREIGN KEY (class_id, term_id) REFERENCES class(class_id, term_id) ON DELETE CASCADE,
  PRIMARY KEY (class_report_id)
);

CREATE TABLE IF NOT EXISTS course_class_report (
  course_class_report_id SERIAL,
  course_class_id INTEGER REFERENCES course_class NOT NULL,
  course_id INTEGER,
  class_id INTEGER,
  term_id INTEGER,
  delete_permanently BOOLEAN,
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (course_class_report_id)
);

CREATE TABLE IF NOT EXISTS work_assignment_report (
  work_assignment_report_id SERIAL,
  work_assignment_id INTEGER REFERENCES course_misc_unit ON DELETE CASCADE,
  sheet_id UUID,
  supplement_id UUID,
  due_date date,
  individual BOOLEAN,
  late_delivery BOOLEAN,
  multiple_deliveries BOOLEAN,
  requires_report BOOLEAN,
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (work_assignment_report_id)
);

CREATE TABLE IF NOT EXISTS exam_report (
  exam_report_id SERIAL,
  exam_id INTEGER REFERENCES course_misc_unit ON DELETE CASCADE,
  sheet_id UUID,
  due_date date,
  exam_type exam_type,
  phase VARCHAR(30),
  location varchar(30),
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (exam_report_id)
);

CREATE TABLE IF NOT EXISTS lecture_report (
  lecture_report_id SERIAL,
  lecture_id INTEGER REFERENCES class_misc_unit ON DELETE CASCADE,
  weekday weekday,
  begins TIME,
  duration INTERVAL,
  location varchar(30),
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (lecture_report_id)
);

CREATE TABLE IF NOT EXISTS homework_report (
  homework_report_id SERIAL,
  homework_id INTEGER REFERENCES class_misc_unit ON DELETE CASCADE,
  sheet_id UUID,
  due_date DATE,
  late_delivery BOOLEAN,
  multiple_deliveries BOOLEAN,
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (homework_report_id)
);

CREATE TABLE IF NOT EXISTS user_report (
  report_id SERIAL,
  user_username VARCHAR(20) REFERENCES user_account ON DELETE CASCADE,
  reason VARCHAR(200) NOT NULL,
  reported_by VARCHAR(20) NOT NULL,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (report_id)
);

--------------------------
-- Create Version Tables
--------------------------

CREATE TABLE IF NOT EXISTS organization_version (
  organization_id INTEGER,
  organization_version INTEGER,
  created_by VARCHAR(20) NOT NULL,
  organization_full_name VARCHAR(100) NOT NULL,
  organization_short_name VARCHAR(10) NOT NULL,
  organization_address VARCHAR(100) NOT NULL,
  organization_contact VARCHAR(15) NOT NULL,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (organization_id, organization_version)
);


CREATE TABLE IF NOT EXISTS programme_version (
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

CREATE TABLE IF NOT EXISTS course_version (
  course_id INTEGER,
  organization_id INTEGER,
  course_version INTEGER,
  course_full_name VARCHAR(100) NOT NULL,
  course_short_name VARCHAR(10) NOT NULL,
  created_by VARCHAR(20),
  time_stamp timestamp,
  PRIMARY KEY (course_id, course_version)
);

CREATE TABLE IF NOT EXISTS course_programme_version (
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

CREATE TABLE IF NOT EXISTS class_version (
  class_id INTEGER,
  term_id INTEGER,
  class_version INTEGER,
  class_name VARCHAR(10) NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (class_id, term_id, class_version)
);

CREATE TABLE IF NOT EXISTS work_assignment_version (
	work_assignment_id INTEGER,
	work_assignment_version INTEGER,
	sheet_id UUID NOT NULL,
   supplement_id UUID NOT NULL,
   due_date date NOT NULL,
   individual BOOLEAN NOT NULL,
   late_delivery BOOLEAN NOT NULL,
   multiple_deliveries BOOLEAN NOT NULL,
   requires_report BOOLEAN NOT NULL,
	created_by VARCHAR(20) NOT NULL,
	time_stamp timestamp NOT NULL,
	PRIMARY KEY (work_assignment_id, work_assignment_version)
);

CREATE TABLE IF NOT EXISTS exam_version (
    exam_id INTEGER,
    exam_version INTEGER,
    sheet_id UUID NOT NULL,
    due_date date NOT NULL,
    exam_type exam_type NOT NULL,
    phase VARCHAR(30) NOT NULL,
    location varchar(30) NOT NULL,
    created_by VARCHAR(20) NOT NULL,
    time_stamp timestamp NOT NULL,
    PRIMARY KEY (exam_id, exam_version)
);

CREATE TABLE IF NOT EXISTS lecture_version (
  lecture_id INTEGER,
  lecture_version INTEGER,
  created_by VARCHAR(20) NOT NULL,
  weekday weekday NOT NULL,
  begins TIME NOT NULL,
  duration INTERVAL NOT NULL,
  time_stamp timestamp NOT NULL,
  location varchar(30) NOT NULL,
  PRIMARY KEY (lecture_id, lecture_version)
);

CREATE TABLE IF NOT EXISTS homework_version (
  homework_id INTEGER,
  homework_version INTEGER,
  sheet_id UUID NOT NULL,
  due_date DATE NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  late_delivery BOOLEAN NOT NULL,
  time_stamp timestamp NOT NULL,
  multiple_deliveries BOOLEAN NOT NULL,
  PRIMARY KEY (homework_id, homework_version)
);