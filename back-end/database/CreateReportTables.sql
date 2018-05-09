--------------------------
-- Create Report Tables
--------------------------

CREATE TABLE organization_report (
  report_id SERIAL,
  organization_id INTEGER REFERENCES organization,
  organization_full_name varchar(100) UNIQUE NOT NULL,
  organization_short_name varchar(10) UNIQUE NOT NULL,
  organization_address varchar(100) NOT NULL,
  organization_contact INTEGER UNIQUE NOT NULL,
  made_by VARCHAR(20),
  votes INTEGER DEFAULT 0,
  PRIMARY KEY (report_id)
);

CREATE TABLE programme_report (
    report_id SERIAL, 
    programme_id INTEGER REFERENCES programme,
    programme_full_name varchar(100) UNIQUE NOT NULL,
    programme_short_name varchar(10) UNIQUE NOT NULL,
    programme_academic_degree varchar(50) NOT NULL,
    programme_total_credits INTEGER,
    programme_duration INTEGER,
    made_by VARCHAR(20),
    votes INTEGER DEFAULT 0,
    PRIMARY KEY (report_id)
);

CREATE TABLE course_report (
    report_id SERIAL, 
    course_id INTEGER REFERENCES course,
    course_full_name varchar(100) UNIQUE NOT NULL,
    course_short_name varchar(10) UNIQUE NOT NULL,
    made_by VARCHAR(20),
    votes INTEGER DEFAULT 0,
    PRIMARY KEY (report_id)
);

CREATE TABLE term_report (
  report_id SERIAL, 
  term_id INTEGER REFERENCES term,
  term_short_name CHAR(5) UNIQUE NOT NULL,
  term_year INTEGER NOT NULL,
  term_type term_type NOT NULL,
  made_by VARCHAR(20),
  votes INTEGER DEFAULT 0,
  PRIMARY KEY (report_id)
);

CREATE TABLE class_report (
  report_id SERIAL,
  class_id INTEGER REFERENCES class,
  class_name VARCHAR(10),
  term_id INTEGER REFERENCES term,
  made_by VARCHAR(20),
  votes INTEGER DEFAULT 0,
  PRIMARY KEY (report_id)
);

CREATE TABLE work_assignment_report (
  report_id SERIAL,
  id INTEGER REFERENCES course_misc_unit,
  sheet VARCHAR(100),
  supplement VARCHAR(100),
  due_date date,
  individual BOOLEAN,
  late_delivery BOOLEAN,
  multiple_deliveries BOOLEAN,
  requires_report BOOLEAN,
  made_by VARCHAR(20),
  votes INTEGER DEFAULT 0,
  PRIMARY KEY (report_id)
);

CREATE TABLE exam_report (
  report_id SERIAL,
  id INTEGER REFERENCES course_misc_unit,
  sheet VARCHAR(100),
  due_date date,
  exam_type exam_type,
  phase VARCHAR(30),
  location varchar(30),
  made_by VARCHAR(20),
  votes INTEGER DEFAULT 0,
  PRIMARY KEY (report_id)
);

CREATE TABLE lecture_report (
  report_id SERIAL,
  id INTEGER REFERENCES class_misc_unit,
  weekday weekday,
  begins TIME,
  duration INTERVAL,
  location varchar(30),
  made_by VARCHAR(20),
  votes INTEGER DEFAULT 0,
  PRIMARY KEY (report_id)
);

CREATE TABLE homework_report (
  report_id SERIAL,
  id INTEGER REFERENCES class_misc_unit,
  sheet VARCHAR(100),
  due_date DATE,
  late_delivery BOOLEAN,
  multiple_deliveries BOOLEAN,
  made_by VARCHAR(20),
  votes INTEGER DEFAULT 0,
  PRIMARY KEY (report_id)
);

CREATE TABLE student_report (
  report_id SERIAL,
  student_username VARCHAR(20),
  student_given_name VARCHAR(15) NOT NULL,
  student_family_name VARCHAR(15) NOT NULL,
  student_personal_email varchar(35) UNIQUE NOT NULL,
  student_organization_email varchar(35) UNIQUE NOT NULL,
  student_gender gender, 
  made_by VARCHAR(20),
  votes INTEGER DEFAULT 0,
  PRIMARY KEY (report_id)
);