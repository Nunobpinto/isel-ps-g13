create DATABASE eduwikidb;

--------------------------
-- Auxiliary Types
--------------------------

  CREATE TYPE term_type AS ENUM ('Winter', 'Summer');

  CREATE TYPE student_rank AS ENUM ('Beginner', 'Admin');

  CREATE TYPE course_misc_unit_type AS ENUM ('Work Assignment', 'Exam/Test');

  CREATE TYPE class_misc_unit_type AS ENUM ('Homework', 'Lecture');

  CREATE TYPE exam_type AS ENUM ('Exam', 'Test');

  CREATE TYPE weekday AS ENUM ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday');

--------------------------
-- Create Main Tables
--------------------------

CREATE TABLE organization (
  organization_id SERIAL,
  organization_version INTEGER UNIQUE NOT NULL,
  organization_full_name varchar(100) UNIQUE NOT NULL,
  organization_short_name varchar(10) UNIQUE NOT NULL,
  organization_address varchar(100) NOT NULL,
  organization_contact INTEGER UNIQUE NOT NULL,
  PRIMARY KEY (organization_id)
);

CREATE TABLE programme (
  programme_id SERIAL,
  programme_version INTEGER UNIQUE NOT NULL,
  programme_full_name varchar(100) UNIQUE NOT NULL,
  programme_short_name varchar(10) UNIQUE NOT NULL,
  programme_academic_degree varchar(50) NOT NULL,
  programme_total_credits INTEGER,
  programme_duration INTEGER,
  PRIMARY KEY (programme_id)
);

-- Add column column restricting the terms where it can be lectured (winter only, summer only, both)
CREATE TABLE course (
  course_id SERIAL,
  organization_id INTEGER REFERENCES organization,
  course_version INTEGER UNIQUE NOT NULL,
  course_full_name varchar(100) UNIQUE NOT NULL,
  course_short_name varchar(10) UNIQUE NOT NULL,
  PRIMARY KEY (course_id)
);

CREATE TABLE course_programme (
  course_id INTEGER REFERENCES course,
  programme_id INTERVAL REFERENCES programme,
  course_lectured_term varchar(50) NOT NULL,
  course_optional BOOLEAN NOT NULL,
  course_credits INTEGER NOT NULL,
  PRIMARY KEY (course_id, programme_id)
);

CREATE TABLE term (
  term_id SERIAL,
  term_short_name CHAR(5) NOT NULL couUNIQUE,
  term_year INTEGER NOT NULL,
  term_type term_type NOT NULL,
  PRIMARY KEY (term_id)
);

CREATE TABLE class (
  class_id SERIAL,
  class_version INTEGER UNIQUE NOT NULL,
  class_name VARCHAR(10),
  term INTEGER REFERENCES term,
  PRIMARY KEY (class_id, term)
);

CREATE TABLE course_term (
  course_id INTEGER REFERENCES course,
  term_id INTEGER REFERENCES term,
  PRIMARY KEY (course_id, term_id)
);

CREATE TABLE course_misc_unit (
  id SERIAL,
  misc_type course_misc_unit_type,
  course_id INTEGER,
  term_id INTEGER,
  FOREIGN KEY (course_id, term_id) REFERENCES course_term(course_id, term_id),
  PRIMARY KEY (id)
);

CREATE TABLE work_assignment (
  id INTEGER REFERENCES course_misc_unit,
  sheet VARCHAR(100),
  supplement VARCHAR(100),
  due_date date,
  individual BOOLEAN,
  late_delivery BOOLEAN,
  multiple_deliveries BOOLEAN,
  requires_report BOOLEAN,
  PRIMARY KEY (id)
);

CREATE TABLE exam (
  id INTEGER REFERENCES course_misc_unit,
  sheet VARCHAR(100),
  due_date date,
  type exam_type,
  phase VARCHAR(30),
  location varchar(30),
  PRIMARY KEY (id)
);

CREATE TABLE course_class (
  course_id INTEGER REFERENCES course,
  class_id INTEGER,
  term_id INTEGER,
  FOREIGN KEY (class_id, term_id) REFERENCES class(class_id, term),
  PRIMARY KEY (course_id, class_id, term_id)
);

CREATE TABLE class_misc_unit (
  id SERIAL,
  misc_type class_misc_unit_type,
  course_id INTEGER,
  class_id INTEGER,
  term_id INTEGER,
  FOREIGN KEY (course_id, class_id, term_id) REFERENCES course_class(course_id, class_id, term_id),
  PRIMARY KEY (id)
);

CREATE TABLE lecture (
  id INTEGER REFERENCES class_misc_unit,
  weekday weekday,
  begins TIME,
  duration INTERVAL,
  location varchar(30),
  PRIMARY KEY (id)
);

CREATE TABLE homework (
  id INTEGER REFERENCES class_misc_unit,
  sheet VARCHAR(100),
  due_date DATE,
  late_delivery BOOLEAN,
  multiple_deliveries BOOLEAN,
  requires_report BOOLEAN,
  PRIMARY KEY (id)
);

CREATE TABLE student (
  student_username VARCHAR(20),
  student_given_name VARCHAR(15) NOT NULL,
  student_family_name VARCHAR(15) NOT NULL,
  student_personal_email varchar(35) UNIQUE NOT NULL,
  student_organization_email varchar(35) UNIQUE NOT NULL,
  PRIMARY KEY (student_username)
);

CREATE TABLE reputation (
  reputation_id SERIAL,
  reputation_points INTEGER NOT NULL,
  reputation_rank student_rank NOT NULL,
  student varchar(20) REFERENCES student,
  PRIMARY KEY (reputation_id, student)
);

CREATE TABLE reputation_log (
  reputation_log_id SERIAL,
  reputation_log_action VARCHAR(150) NOT NULL,
  reputation_log_given_by varchar(15) NOT NULL,
  reputation_log_points INTEGER NOT NULL,
  reputation_id INTEGER,
  student VARCHAR(20),
  FOREIGN KEY (reputation_id, student) REFERENCES reputation(reputation_id, student),
  PRIMARY KEY (reputation_log_id)
);

CREATE TABLE student_course_class (
	username VARCHAR(20) NOT NULL REFERENCES student,
	course_id INTEGER NOT NULL REFERENCES course,
	class_id INTEGER,
	PRIMARY KEY (username, course_id)
);

--------------------------
-- Create Staging Tables
--------------------------

CREATE TABLE programme_stage (
	programme_id SERIAL,
	programme_full_name varchar(100) UNIQUE NOT NULL,
	programme_short_name varchar(10) UNIQUE NOT NULL,
	programme_academic_degree varchar(50) NOT NULL,
	programme_total_credits INTEGER,
	programme_duration INTEGER,
	created_by VARCHAR(20),
	vote_count INTEGER DEFAULT 0,
	PRIMARY KEY (programme_id)
);

CREATE TABLE course_stage (
	course_id SERIAL,
	organization_id INTEGER REFERENCES organization,
	course_full_name varchar(100) UNIQUE NOT NULL,
	course_short_name varchar(10) UNIQUE NOT NULL,
	created_by VARCHAR(20),
	vote_count INTEGER DEFAULT 0,
	PRIMARY KEY (course_id)
);

CREATE TABLE course_programme_stage (
  course_id INTEGER REFERENCES course,
  programme_id INTERVAL REFERENCES programme,
  course_lectured_term varchar(50) NOT NULL,
  course_optional BOOLEAN NOT NULL,
  course_credits INTEGER NOT NULL,
	created_by VARCHAR(20),
	vote_count INTEGER DEFAULT 0,
  PRIMARY KEY (course_id, programme_id)
);

CREATE TABLE class_stage (
	class_id SERIAL,
	class_name VARCHAR(10),
	term INTEGER REFERENCES term,
	created_by VARCHAR(20),
	vote_count INTEGER DEFAULT 0,
	PRIMARY KEY (class_id, term)
);

CREATE TABLE course_class_stage (
	course_id INTEGER REFERENCES course,
	class_id INTEGER,
	term_id INTEGER,
	created_by VARCHAR(20),
	vote_count INTEGER DEFAULT 0,
	FOREIGN KEY (class_id, term_id) REFERENCES class(class_id, term),
	PRIMARY KEY (course_id, class_id, term_id)
);

-- CREATE TABLE course_term_stage (
--   course_id INTEGER REFERENCES course,
--   term_id INTEGER REFERENCES term,
--   PRIMARY KEY (course_id, term_id)
-- );

CREATE TABLE course_misc_unit_stage (
  id SERIAL,
  misc_type course_misc_unit_type,
  course_id INTEGER,
  term_id INTEGER,
  FOREIGN KEY (course_id, term_id) REFERENCES course_term(course_id, term_id),
  PRIMARY KEY (id)
);

CREATE TABLE work_assignment_stage (
  	id INTEGER REFERENCES course_misc_unit,
  	sheet VARCHAR(100),
  	supplement VARCHAR(100),
  	due_date date,
  	individual BOOLEAN,
  	late_delivery BOOLEAN,
  	multiple_deliveries BOOLEAN,
  	requires_report BOOLEAN,
	created_by VARCHAR(20),
	vote_count INTEGER DEFAULT 0,
 	PRIMARY KEY (id)
);

CREATE TABLE exam_stage (
  	id INTEGER REFERENCES course_misc_unit,
  	sheet VARCHAR(100),
  	due_date date,
  	type exam_type,
  	phase VARCHAR(30),
  	location varchar(30),
	created_by VARCHAR(20),
	vote_count INTEGER DEFAULT 0
  	PRIMARY KEY (id)
);

CREATE TABLE lecture (
  id INTEGER REFERENCES class_misc_unit,
  weekday weekday,
  begins TIME,
  duration INTERVAL,
  location varchar(30),
  PRIMARY KEY (id)
);

CREATE TABLE homework (
  id INTEGER REFERENCES class_misc_unit,
  sheet VARCHAR(100),
  due_date DATE,
  late_delivery BOOLEAN,
  multiple_deliveries BOOLEAN,
  requires_report BOOLEAN,
  PRIMARY KEY (id)
);
-- Falta alguns stages, reports e versions