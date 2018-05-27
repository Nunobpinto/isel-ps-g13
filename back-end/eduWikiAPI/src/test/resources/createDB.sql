--------------------------
-- Auxiliary Types
--------------------------

  CREATE TYPE term_type AS ENUM ('Winter', 'Summer');

  CREATE TYPE student_rank AS ENUM ('Beginner', 'Admin');

  CREATE TYPE course_misc_unit_type AS ENUM ('Work Assignment', 'Exam/Test');

  CREATE TYPE class_misc_unit_type AS ENUM ('Homework', 'Lecture');

  CREATE TYPE exam_type AS ENUM ('Exam', 'Test');

  CREATE TYPE weekday AS ENUM ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday');

  CREATE TYPE gender AS ENUM ('Male', 'Female');

--------------------------
-- Create Main Tables
--------------------------

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
  organization_id INTEGER REFERENCES organization ON DELETE RESTRICT,
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
  id SERIAL,
  misc_type course_misc_unit_type NOT NULL,
  course_id INTEGER,
  term_id INTEGER,
  time_stamp timestamp NOT NULL,
  FOREIGN KEY (course_id, term_id) REFERENCES course_term(course_id, term_id) ON DELETE CASCADE,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS work_assignment (
  id INTEGER REFERENCES course_misc_unit ON DELETE CASCADE,
  work_assignment_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20) NOT NULL,
  sheet VARCHAR(100) NOT NULL,
  supplement VARCHAR(100),
  due_date date NOT NULL,
  individual BOOLEAN NOT NULL,
  late_delivery BOOLEAN NOT NULL,
  multiple_deliveries BOOLEAN NOT NULL,
  requires_report BOOLEAN NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS exam (
  id INTEGER REFERENCES course_misc_unit ON DELETE CASCADE,
  exam_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20) NOT NULL,
  sheet VARCHAR(100) NOT NULL,
  due_date date NOT NULL,
  exam_type exam_type NOT NULL,
  phase VARCHAR(30) NOT NULL,
  location varchar(30) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS course_class (
  course_id INTEGER REFERENCES course ON DELETE CASCADE,
  class_id INTEGER,
  term_id INTEGER,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  FOREIGN KEY (class_id, term_id) REFERENCES class(class_id, term_id) ON DELETE CASCADE,
  PRIMARY KEY (course_id, class_id, term_id)
);

CREATE TABLE IF NOT EXISTS class_misc_unit (
  id SERIAL,
  misc_type class_misc_unit_type,
  course_id INTEGER,
  class_id INTEGER,
  term_id INTEGER,
  time_stamp timestamp NOT NULL,
  FOREIGN KEY (course_id, class_id, term_id) REFERENCES course_class(course_id, class_id, term_id) ON DELETE CASCADE,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS lecture (
  id INTEGER REFERENCES class_misc_unit ON DELETE CASCADE,
  lecture_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20),
  weekday weekday,
  begins TIME,
  duration INTEGER ,
  location varchar(30),
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS homework (
  id INTEGER REFERENCES class_misc_unit ON DELETE CASCADE,
  homework_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20),
  sheet VARCHAR(100),
  due_date DATE,
  late_delivery BOOLEAN,
  multiple_deliveries BOOLEAN,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS student (
  student_username VARCHAR(20),
  student_given_name VARCHAR(15) NOT NULL,
  student_family_name VARCHAR(15) NOT NULL,
  student_personal_email varchar(35) UNIQUE NOT NULL,
  student_organization_email varchar(35) UNIQUE NOT NULL,
  PRIMARY KEY (student_username)
);

CREATE TABLE IF NOT EXISTS reputation (
  reputation_id SERIAL,
  reputation_points INTEGER NOT NULL,
  reputation_rank student_rank NOT NULL,
  student varchar(20) REFERENCES student ON DELETE CASCADE,
  PRIMARY KEY (reputation_id, student)
);

CREATE TABLE IF NOT EXISTS reputation_log (
  reputation_log_id SERIAL,
  reputation_log_action VARCHAR(150) NOT NULL,
  reputation_log_given_by varchar(15) NOT NULL,
  reputation_log_points INTEGER NOT NULL,
  reputation_id INTEGER,
  student VARCHAR(20),
  FOREIGN KEY (reputation_id, student) REFERENCES reputation(reputation_id, student) ON DELETE CASCADE,
  PRIMARY KEY (reputation_log_id)
);

CREATE TABLE IF NOT EXISTS student_course_class (
  username VARCHAR(20) REFERENCES student ON DELETE CASCADE,
  course_id INTEGER REFERENCES course ON DELETE CASCADE,
  class_id INTEGER,
  term_id INTEGER,
  FOREIGN KEY (class_id, term_id) REFERENCES class (class_id, term_id) ON DELETE SET NULL,
  PRIMARY KEY (username, course_id)
);

--------------------------
-- Create Stage Tables
--------------------------

CREATE TABLE IF NOT EXISTS programme_stage (
  programme_id SERIAL,
  programme_full_name VARCHAR(100) NOT NULL,
  programme_short_name VARCHAR(10) NOT NULL,
  programme_academic_degree VARCHAR(50) NOT NULL,
  programme_total_credits INTEGER NOT NULL,
  programme_duration INTEGER NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (programme_id)
);

CREATE TABLE IF NOT EXISTS course_stage (
  course_id SERIAL,
  organization_id INTEGER REFERENCES organization ON DELETE CASCADE,
  course_full_name VARCHAR(100) NOT NULL,
  course_short_name VARCHAR(10) NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (course_id)
);

CREATE TABLE IF NOT EXISTS course_programme_stage (
  course_id INTEGER REFERENCES course ON DELETE CASCADE,
  programme_id INTEGER REFERENCES programme ON DELETE CASCADE,
  course_lectured_term VARCHAR(50) NOT NULL,
  course_optional BOOLEAN NOT NULL,
  course_credits INTEGER NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (course_id, programme_id)
);

CREATE TABLE IF NOT EXISTS class_stage (
  class_id SERIAL,
  term_id INTEGER REFERENCES term ON DELETE CASCADE,
  class_name VARCHAR(10) NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (class_id, term_id)
);

CREATE TABLE IF NOT EXISTS course_class_stage (
  course_id INTEGER REFERENCES course ON DELETE CASCADE,
  class_id INTEGER,
  term_id INTEGER,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  FOREIGN KEY (class_id, term_id) REFERENCES class(class_id, term_id) ON DELETE CASCADE,
  PRIMARY KEY (course_id, class_id, term_id)
);

-- not sure how staging course specific misc units are going to be staged, it might be through references to this table
-- CREATE TABLE IF NOT EXISTS course_term_stage (
--   course_id INTEGER REFERENCES course,
--   term_id INTEGER REFERENCES term,
--   PRIMARY KEY (course_id, term_id)
-- );

CREATE TABLE IF NOT EXISTS course_misc_unit_stage (
  id SERIAL,
  course_id INTEGER,
  term_id INTEGER,
  misc_type course_misc_unit_type NOT NULL,
  FOREIGN KEY (course_id, term_id) REFERENCES course_term(course_id, term_id) ON DELETE CASCADE,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS class_misc_unit_stage (
  id SERIAL,
  misc_type class_misc_unit_type NOT NULL,
  course_id INTEGER,
  class_id INTEGER,
  term_id INTEGER,
  FOREIGN KEY (course_id, class_id, term_id) REFERENCES course_class(course_id, class_id, term_id) ON DELETE CASCADE,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS work_assignment_stage (
  id INTEGER REFERENCES course_misc_unit_stage ON DELETE CASCADE,
  sheet VARCHAR(100) NOT NULL,
  supplement VARCHAR(100) NOT NULL,
  due_date date NOT NULL,
  individual BOOLEAN NOT NULL,
  late_delivery BOOLEAN NOT NULL,
  multiple_deliveries BOOLEAN NOT NULL,
  requires_report BOOLEAN NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS exam_stage (
  id INTEGER REFERENCES course_misc_unit_stage ON DELETE CASCADE,
  sheet VARCHAR(100) NOT NULL,
  due_date date NOT NULL,
  exam_type  exam_type NOT NULL,
  phase VARCHAR(30) NOT NULL,
  location VARCHAR(30) NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS lecture_stage (
  id INTEGER REFERENCES class_misc_unit_stage ON DELETE CASCADE,
  weekday weekday NOT NULL,
  begins TIME NOT NULL,
  duration INTEGER NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  time_stamp timestamp NOT NULL,
  votes INTEGER DEFAULT 0,
  location VARCHAR(30) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS homework_stage (
  id INTEGER REFERENCES class_misc_unit_stage ON DELETE CASCADE,
  sheet VARCHAR(100) NOT NULL,
  due_date DATE NOT NULL,
  late_delivery BOOLEAN NOT NULL,
  multiple_deliveries BOOLEAN NOT NULL,
  time_stamp timestamp NOT NULL,
  votes INTEGER DEFAULT 0,
  created_by VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
);

--------------------------
-- Create Report Tables
--------------------------

CREATE TABLE IF NOT EXISTS organization_report (
  report_id SERIAL,
  organization_id INTEGER REFERENCES organization ON DELETE CASCADE,
  organization_full_name varchar(100),
  organization_short_name varchar(10),
  organization_address varchar(100),
  organization_contact varchar(15),
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (report_id)
);

CREATE TABLE IF NOT EXISTS programme_report (
    report_id SERIAL,
    programme_id INTEGER REFERENCES programme ON DELETE CASCADE,
    programme_full_name varchar(100),
    programme_short_name varchar(10),
    programme_academic_degree varchar(50),
    programme_total_credits INTEGER,
    programme_duration INTEGER,
    reported_by VARCHAR(20) NOT NULL,
    votes INTEGER DEFAULT 0,
    time_stamp timestamp NOT NULL,
    PRIMARY KEY (report_id)
);

CREATE TABLE IF NOT EXISTS course_report (
    report_id SERIAL,
    course_id INTEGER REFERENCES course ON DELETE CASCADE,
    course_full_name varchar(100),
    course_short_name varchar(10),
    reported_by VARCHAR(20) NOT NULL,
    votes INTEGER DEFAULT 0,
    time_stamp timestamp NOT NULL,
    PRIMARY KEY (report_id)
);

CREATE TABLE IF NOT EXISTS class_report (
  report_id SERIAL,
  class_id INTEGER,
  term_id INTEGER,
  class_name VARCHAR(10),
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  FOREIGN KEY (class_id, term_id) REFERENCES class(class_id, term_id) ON DELETE CASCADE,
  PRIMARY KEY (report_id)
);

CREATE TABLE IF NOT EXISTS work_assignment_report (
  report_id SERIAL,
  id INTEGER REFERENCES course_misc_unit ON DELETE CASCADE,
  sheet VARCHAR(100),
  supplement VARCHAR(100),
  due_date date,
  individual BOOLEAN,
  late_delivery BOOLEAN,
  multiple_deliveries BOOLEAN,
  requires_report BOOLEAN,
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (report_id)
);

CREATE TABLE IF NOT EXISTS exam_report (
  report_id SERIAL,
  id INTEGER REFERENCES course_misc_unit ON DELETE CASCADE,
  sheet VARCHAR(100),
  due_date date,
  exam_type exam_type,
  phase VARCHAR(30),
  location varchar(30),
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (report_id)
);

CREATE TABLE IF NOT EXISTS lecture_report (
  report_id SERIAL,
  id INTEGER REFERENCES class_misc_unit ON DELETE CASCADE,
  weekday weekday,
  begins TIME,
  duration INTEGER ,
  location varchar(30),
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (report_id)
);

CREATE TABLE IF NOT EXISTS homework_report (
  report_id SERIAL,
  id INTEGER REFERENCES class_misc_unit ON DELETE CASCADE,
  sheet VARCHAR(100),
  due_date DATE,
  late_delivery BOOLEAN,
  multiple_deliveries BOOLEAN,
  reported_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (report_id)
);

CREATE TABLE IF NOT EXISTS student_report (
  report_id SERIAL,
  student_username VARCHAR(20) REFERENCES student ON DELETE CASCADE,
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

-- Information stored in association between course and programme might be stored here, not sure though
CREATE TABLE IF NOT EXISTS course_version (
  course_id INTEGER,
  organization_id INTEGER,
  course_version INTEGER NOT NULL,
  course_full_name VARCHAR(100) NOT NULL,
  course_short_name VARCHAR(10) NOT NULL,
  created_by VARCHAR(20),
  time_stamp timestamp,
  PRIMARY KEY (course_id, course_version)
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
	id INTEGER,
	work_assignment_version INTEGER,
	sheet VARCHAR(100) NOT NULL,
   supplement VARCHAR(100) NOT NULL,
   due_date date NOT NULL,
   individual BOOLEAN NOT NULL,
   late_delivery BOOLEAN NOT NULL,
   multiple_deliveries BOOLEAN NOT NULL,
   requires_report BOOLEAN NOT NULL,
	created_by VARCHAR(20) NOT NULL,
	time_stamp timestamp NOT NULL,
	PRIMARY KEY (id, work_assignment_version)
);

CREATE TABLE IF NOT EXISTS exam_version (
    id INTEGER,
  exam_version INTEGER,
    sheet VARCHAR(100) NOT NULL,
    due_date date NOT NULL,
    exam_type exam_type NOT NULL,
    phase VARCHAR(30) NOT NULL,
    location varchar(30) NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  time_stamp timestamp NOT NULL,
    PRIMARY KEY (id, exam_version)
);

CREATE TABLE IF NOT EXISTS lecture_version (
  id INTEGER,
  lecture_version INTEGER,
  created_by VARCHAR(20) NOT NULL,
  weekday weekday NOT NULL,
  begins TIME NOT NULL,
  duration INTEGER NOT NULL,
  time_stamp timestamp NOT NULL,
  location varchar(30) NOT NULL,
  PRIMARY KEY (id, lecture_version)
);

CREATE TABLE IF NOT EXISTS homework_version (
  id INTEGER,
  homework_version INTEGER,
  sheet VARCHAR(100) NOT NULL,
  due_date DATE NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  late_delivery BOOLEAN NOT NULL,
  time_stamp timestamp NOT NULL,
  multiple_deliveries BOOLEAN NOT NULL,
  PRIMARY KEY (id, homework_version)
);