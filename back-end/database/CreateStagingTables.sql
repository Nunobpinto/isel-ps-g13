--------------------------
-- Create Staging Tables
--------------------------

CREATE TABLE programme_stage (
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

CREATE TABLE course_stage (
	course_id SERIAL,
	organization_id INTEGER REFERENCES organization,
	course_full_name VARCHAR(100) NOT NULL,
	course_short_name VARCHAR(10) NOT NULL,
	created_by VARCHAR(20) NOT NULL,
	votes INTEGER DEFAULT 0,
	time_stamp timestamp NOT NULL,
	PRIMARY KEY (course_id)
);

CREATE TABLE course_programme_stage (
  course_id INTEGER REFERENCES course,
  programme_id INTEGER REFERENCES programme,
  course_lectured_term VARCHAR(50) NOT NULL,
  course_optional BOOLEAN NOT NULL,
  course_credits INTEGER NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  time_stamp timestamp NOT NULL,
  PRIMARY KEY (course_id, programme_id)
);

CREATE TABLE class_stage (
	class_id SERIAL,
	term_id INTEGER REFERENCES term,
	class_name VARCHAR(10) NOT NULL,
	created_by VARCHAR(20) NOT NULL,
	votes INTEGER DEFAULT 0,
	time_stamp timestamp NOT NULL,
	PRIMARY KEY (class_id, term_id)
);

CREATE TABLE course_class_stage (
	course_id INTEGER REFERENCES course,
	class_id INTEGER,
	term_id INTEGER,
	created_by VARCHAR(20) NOT NULL,
	votes INTEGER DEFAULT 0,
	time_stamp timestamp NOT NULL,
	FOREIGN KEY (class_id, term_id) REFERENCES class(class_id, term_id),
	PRIMARY KEY (course_id, class_id, term_id)
);

-- not sure how staging course specific misc units are going to be staged, it might be through references to this table
-- CREATE TABLE course_term_stage (
--   course_id INTEGER REFERENCES course,
--   term_id INTEGER REFERENCES term,
--   PRIMARY KEY (course_id, term_id)
-- );

CREATE TABLE course_misc_unit_stage (
  id SERIAL,
  course_id INTEGER,
  term_id INTEGER,
  misc_type course_misc_unit_type NOT NULL,
  FOREIGN KEY (course_id, term_id) REFERENCES course_term(course_id, term_id),
  PRIMARY KEY (id)
);

CREATE TABLE work_assignment_stage (
  	id INTEGER REFERENCES course_misc_unit_stage,
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

CREATE TABLE exam_stage (
  	id INTEGER REFERENCES course_misc_unit,
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

CREATE TABLE lecture_stage (
  id INTEGER REFERENCES class_misc_unit,
  weekday weekday NOT NULL,
  begins TIME NOT NULL,
  duration INTERVAL NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  time_stamp timestamp NOT NULL,
  votes INTEGER DEFAULT 0,
  location VARCHAR(30) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE homework_stage (
  id INTEGER REFERENCES class_misc_unit,
  sheet VARCHAR(100) NOT NULL,
  due_date DATE NOT NULL,
  late_delivery BOOLEAN NOT NULL,
  multiple_deliveries BOOLEAN NOT NULL,
  time_stamp timestamp NOT NULL,
  votes INTEGER DEFAULT 0,
  created_by VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
);