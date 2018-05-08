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
	time_stamp timestamp,
	PRIMARY KEY (programme_id)
);

CREATE TABLE course_stage (
	course_id SERIAL,
	organization_id INTEGER REFERENCES organization,
	course_full_name varchar(100) UNIQUE NOT NULL,
	course_short_name varchar(10) UNIQUE NOT NULL,
	created_by VARCHAR(20),
	vote_count INTEGER DEFAULT 0,
	time_stamp timestamp,
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
  time_stamp timestamp,
  PRIMARY KEY (course_id, programme_id)
);

CREATE TABLE class_stage (
	class_id SERIAL,
	class_name VARCHAR(10),
	term INTEGER REFERENCES term,
	created_by VARCHAR(20),
	vote_count INTEGER DEFAULT 0,
	time_stamp timestamp,
	PRIMARY KEY (class_id, term)
);

CREATE TABLE course_class_stage (
	course_id INTEGER REFERENCES course,
	class_id INTEGER,
	term_id INTEGER,
	created_by VARCHAR(20),
	vote_count INTEGER DEFAULT 0,
	time_stamp timestamp,
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
  time_stamp timestamp,
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
	time_stamp timestamp,
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
	vote_count INTEGER DEFAULT 0,
	time_stamp timestamp,
  	PRIMARY KEY (id)
);

CREATE TABLE lecture_stage (
  id INTEGER REFERENCES class_misc_unit,
  weekday weekday,
  begins TIME,
  duration INTERVAL,
  created_by VARCHAR(20),
  location varchar(30),
  PRIMARY KEY (id)
);

CREATE TABLE homework_stage (
  id INTEGER REFERENCES class_misc_unit,
  sheet VARCHAR(100),
  due_date DATE,
  late_delivery BOOLEAN,
  multiple_deliveries BOOLEAN,
  created_by VARCHAR(20),
  PRIMARY KEY (id)
);