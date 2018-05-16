--------------------------
-- Create Report Tables
--------------------------

CREATE TABLE organization_report (
  report_id SERIAL,
  organization_id INTEGER REFERENCES organization,
  organization_full_name varchar(100),
  organization_short_name varchar(10),
  organization_address varchar(100),
  organization_contact INTEGER,
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  PRIMARY KEY (report_id)
);

CREATE TABLE programme_report (
    report_id SERIAL, 
    programme_id INTEGER REFERENCES programme,
    programme_full_name varchar(100),
    programme_short_name varchar(10),
    programme_academic_degree varchar(50),
    programme_total_credits INTEGER,
    programme_duration INTEGER,
    created_by VARCHAR(20) NOT NULL,
    votes INTEGER DEFAULT 0,
    PRIMARY KEY (report_id)
);

CREATE TABLE course_report (
    report_id SERIAL, 
    course_id INTEGER REFERENCES course,
    course_full_name varchar(100),
    course_short_name varchar(10),
    created_by VARCHAR(20) NOT NULL,
    votes INTEGER DEFAULT 0,
    PRIMARY KEY (report_id)
);

CREATE TABLE class_report (
  report_id SERIAL,
  class_id INTEGER,
  term_id INTEGER,
  class_name VARCHAR(10),
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  FOREIGN KEY (class_id, term_id) REFERENCES class(class_id, term_id),
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
  created_by VARCHAR(20) NOT NULL,
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
  created_by VARCHAR(20) NOT NULL,
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
  created_by VARCHAR(20) NOT NULL,
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
  created_by VARCHAR(20) NOT NULL,
  votes INTEGER DEFAULT 0,
  PRIMARY KEY (report_id)
);

CREATE TABLE student_report (
  report_id SERIAL,
  student_username VARCHAR(20) REFERENCES student,
  reason VARCHAR(200) NOT NULL,
  created_by VARCHAR(20) NOT NULL,
  PRIMARY KEY (report_id)
);