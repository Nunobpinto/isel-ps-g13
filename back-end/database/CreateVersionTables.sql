--------------------------
-- Create Version Tables
--------------------------

CREATE TABLE organization_version (
  organization_id SERIAL,
  organization_version INTEGER UNIQUE NOT NULL,
  created_by VARCHAR(20),
  organization_full_name varchar(100) UNIQUE NOT NULL,
  organization_short_name varchar(10) UNIQUE NOT NULL,
  organization_address varchar(100) NOT NULL,
  organization_contact INTEGER UNIQUE NOT NULL,
  vote_count INTEGER DEFAULT 0,
  time_stamp timestamp,
  PRIMARY KEY (organization_id, organization_version)
)


CREATE TABLE programme_version (
	programme_id SERIAL,
	programme_full_name varchar(100) UNIQUE NOT NULL,
	programme_short_name varchar(10) UNIQUE NOT NULL,
	programme_academic_degree varchar(50) NOT NULL,
	programme_total_credits INTEGER,
	programme_duration INTEGER,
	created_by VARCHAR(20),
	vote_count INTEGER DEFAULT 0,
	time_stamp timestamp,
	programme_version INTEGER UNIQUE NOT NULL,
	PRIMARY KEY (programme_id, programme_version)
);

CREATE TABLE course_version (
	course_id SERIAL,
	organization_id INTEGER REFERENCES organization,
	course_full_name varchar(100) UNIQUE NOT NULL,
	course_short_name varchar(10) UNIQUE NOT NULL,
	created_by VARCHAR(20),
	vote_count INTEGER DEFAULT 0,
	time_stamp timestamp,
	course_version INTEGER UNIQUE NOT NULL,
	PRIMARY KEY (course_id, course_version)
);

CREATE TABLE class_version (
	class_id SERIAL,
	class_name VARCHAR(10),
	term INTEGER REFERENCES term,
	created_by VARCHAR(20),
	vote_count INTEGER DEFAULT 0,
	time_stamp timestamp,
	class_version INTEGER UNIQUE NOT NULL,
	PRIMARY KEY (class_id, term, class_version)
);

CREATE TABLE work_assignment_version (
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
	work_assignment_version INTEGER UNIQUE NOT NULL,
 	PRIMARY KEY (id, work_assignment_version)
);

CREATE TABLE exam_version (
  	id INTEGER REFERENCES course_misc_unit,
  	sheet VARCHAR(100),
  	due_date date,
  	type exam_type,
  	phase VARCHAR(30),
  	location varchar(30),
	created_by VARCHAR(20),
	exam_version INTEGER UNIQUE NOT NULL,
	vote_count INTEGER DEFAULT 0,
	time_stamp timestamp,
  	PRIMARY KEY (id, exam_version)
);

CREATE TABLE lecture_version (
  id INTEGER REFERENCES class_misc_unit,
  created_by VARCHAR(20),
  lecture_version INTEGER UNIQUE NOT NULL,
  weekday weekday,
  begins TIME,
  duration INTERVAL,
  location varchar(30),
  PRIMARY KEY (id, lecture_version)
);

CREATE TABLE homework_version (
  id INTEGER REFERENCES class_misc_unit,
  sheet VARCHAR(100),
  due_date DATE,
  created_by VARCHAR(20),
  homework_version INTEGER UNIQUE NOT NULL,
  late_delivery BOOLEAN,
  multiple_deliveries BOOLEAN,
  PRIMARY KEY (id, homework_version)
);
