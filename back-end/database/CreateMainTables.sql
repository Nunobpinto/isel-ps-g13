--------------------------
-- Create Main Tables
--------------------------

CREATE TABLE organization (
  organization_id SERIAL,
  organization_version INTEGER UNIQUE NOT NULL,
  created_by VARCHAR(20),
  organization_full_name varchar(100) UNIQUE NOT NULL,
  organization_short_name varchar(10) UNIQUE NOT NULL,
  organization_address varchar(100) NOT NULL,
  organization_contact INTEGER UNIQUE NOT NULL,
  PRIMARY KEY (organization_id)
);

CREATE TABLE programme (
  programme_id SERIAL,
  programme_version INTEGER UNIQUE NOT NULL,
  created_by VARCHAR(20),
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
  created_by VARCHAR(20),
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
  term_short_name CHAR(5) UNIQUE NOT NULL,
  term_year INTEGER NOT NULL,
  term_type term_type NOT NULL,
  term_version INTEGER UNIQUE NOT NULL,
  created_by VARCHAR(20),
  PRIMARY KEY (term_id)
);

CREATE TABLE class (
  class_id SERIAL,
  class_version INTEGER UNIQUE NOT NULL,
  created_by VARCHAR(20),
  class_name VARCHAR(10),
  term_id INTEGER REFERENCES term,
  PRIMARY KEY (class_id, term_id)
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
  work_assignment_version INTEGER UNIQUE NOT NULL,
  created_by VARCHAR(20),
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
  exam_version INTEGER UNIQUE NOT NULL,
  created_by VARCHAR(20),
  sheet VARCHAR(100),
  due_date date,
  exam_type exam_type,
  phase VARCHAR(30),
  location varchar(30),
  PRIMARY KEY (id)
);

CREATE TABLE course_class (
  course_id INTEGER REFERENCES course,
  class_id INTEGER,
  term_id INTEGER,
  FOREIGN KEY (class_id, term_id) REFERENCES class(class_id, term_id),
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
  lecture_version INTEGER UNIQUE NOT NULL,
  created_by VARCHAR(20),
  weekday weekday,
  begins TIME,
  duration INTERVAL,
  location varchar(30),
  PRIMARY KEY (id)
);

CREATE TABLE homework (
  id INTEGER REFERENCES class_misc_unit,
  homework_version INTEGER UNIQUE NOT NULL,	
  created_by VARCHAR(20),
  sheet VARCHAR(100),
  due_date DATE,
  late_delivery BOOLEAN,
  multiple_deliveries BOOLEAN,
  PRIMARY KEY (id)
);

CREATE TABLE student (
  student_username VARCHAR(20),
  student_given_name VARCHAR(15) NOT NULL,
  student_family_name VARCHAR(15) NOT NULL,
  student_personal_email varchar(35) UNIQUE NOT NULL,
  student_organization_email varchar(35) UNIQUE NOT NULL,
  student_gender gender, 
  student_version INTEGER UNIQUE NOT NULL,
  PRIMARY KEY (student_username)
);

CREATE TABLE reputation (
  reputation_id SERIAL,
  reputation_points INTEGER NOT NULL,
  reputation_rank student_rank NOT NULL, 
  reputation_version INTEGER UNIQUE NOT NULL,
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