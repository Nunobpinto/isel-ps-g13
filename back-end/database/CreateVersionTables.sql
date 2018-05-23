--------------------------
-- Create Version Tables
--------------------------

CREATE TABLE IF NOT EXISTS organization_version (
  organization_id INTEGER REFERENCES organization,
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
	programme_id INTEGER REFERENCES programme,
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
	course_id INTEGER REFERENCES course,
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
	FOREIGN KEY (class_id, term_id) REFERENCES class(class_id, term_id),
	PRIMARY KEY (class_id, term_id, class_version)
);

CREATE TABLE IF NOT EXISTS work_assignment_version (
  	id INTEGER REFERENCES course_misc_unit,
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
  	id INTEGER REFERENCES course_misc_unit,
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
  id INTEGER REFERENCES class_misc_unit,
  lecture_version INTEGER,
  created_by VARCHAR(20) NOT NULL,
  weekday weekday NOT NULL,
  begins TIME NOT NULL,
  duration INTERVAL NOT NULL,
  time_stamp timestamp NOT NULL,
  location varchar(30) NOT NULL,
  PRIMARY KEY (id, lecture_version)
);

CREATE TABLE IF NOT EXISTS homework_version (
  id INTEGER REFERENCES class_misc_unit,
  homework_version INTEGER,
  sheet VARCHAR(100) NOT NULL,
  due_date DATE NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  late_delivery BOOLEAN NOT NULL,
  time_stamp timestamp NOT NULL,
  multiple_deliveries BOOLEAN NOT NULL,
  PRIMARY KEY (id, homework_version)
);