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
  organization_contact VARCHAR (15) NOT NULL
  votes INTEGER DEFAULT 0,
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
  PRIMARY KEY (programme_id)
);

-- Add column column restricting the terms where it can be lectured (winter only, summer only, both)
CREATE TABLE IF NOT EXISTS course (
  course_id SERIAL,
  organization_id INTEGER REFERENCES organization,
  course_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20) NOT NULL,
  course_full_name VARCHAR(100) UNIQUE NOT NULL,
  course_short_name VARCHAR(10) UNIQUE NOT NULL,
  votes INTEGER DEFAULT 0,
  PRIMARY KEY (course_id)
);

CREATE TABLE IF NOT EXISTS course_programme (
  course_id INTEGER REFERENCES course,
  programme_id INTEGER REFERENCES programme,
  course_programme_version INTEGER NOT NULL DEFAULT 1,
  course_lectured_term varchar(50) NOT NULL,
  course_optional BOOLEAN NOT NULL,
  course_credits INTEGER NOT NULL,
  votes INTEGER DEFAULT 0,
  PRIMARY KEY (course_id, programme_id)
);

CREATE TABLE IF NOT EXISTS term (
  term_id SERIAL,
  term_short_name CHAR(5) UNIQUE NOT NULL,
  term_year INTEGER NOT NULL,
  term_type term_type NOT NULL,
  PRIMARY KEY (term_id)
);

CREATE TABLE IF NOT EXISTS class (
  class_id SERIAL,
  class_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20) NOT NULL,
  class_name VARCHAR(10) NOT NULL,
  term_id INTEGER REFERENCES term,
  votes INTEGER DEFAULT 0,
  PRIMARY KEY (class_id, term_id)
);

CREATE TABLE IF NOT EXISTS course_term (
  course_id INTEGER REFERENCES course,
  term_id INTEGER REFERENCES term,
  PRIMARY KEY (course_id, term_id)
);

CREATE TABLE IF NOT EXISTS course_misc_unit (
  id SERIAL,
  misc_type course_misc_unit_type NOT NULL,
  course_id INTEGER,
  term_id INTEGER,
  FOREIGN KEY (course_id, term_id) REFERENCES course_term(course_id, term_id),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS work_assignment (
  id INTEGER REFERENCES course_misc_unit,
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
  PRIMARY KEY (id)
);

CREATE TABLE exam (
  id INTEGER REFERENCES course_misc_unit,
  exam_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20) NOT NULL,
  sheet VARCHAR(100) NOT NULL,
  due_date date NOT NULL,
  exam_type exam_type NOT NULL,
  phase VARCHAR(30) NOT NULL,
  location varchar(30) NOT NULL,
  votes INTEGER DEFAULT 0,
  PRIMARY KEY (id)
);

CREATE TABLE course_class (
  course_id INTEGER REFERENCES course,
  class_id INTEGER,
  term_id INTEGER,
  votes INTEGER DEFAULT 0,
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
  lecture_version INTEGER NOT NULL DEFAULT 1,
  created_by VARCHAR(20),
  weekday weekday,
  begins TIME,
  duration INTERVAL,
  location varchar(30),
  votes INTEGER DEFAULT 0,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS homework (
  id INTEGER REFERENCES class_misc_unit,
  homework_version INTEGER NOT NULL DEFAULT 1,	
  created_by VARCHAR(20),
  sheet VARCHAR(100),
  due_date DATE,
  late_delivery BOOLEAN,
  multiple_deliveries BOOLEAN,
  votes INTEGER DEFAULT 0,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS student (
  student_username VARCHAR(20),
  student_given_name VARCHAR(15) NOT NULL,
  student_family_name VARCHAR(15) NOT NULL,
  student_personal_email varchar(35) UNIQUE NOT NULL,
  student_organization_email varchar(35) UNIQUE NOT NULL,
  student_version INTEGER UNIQUE NOT NULL DEFAULT 1,
  PRIMARY KEY (student_username)
);

CREATE TABLE IF NOT EXISTS reputation (
  reputation_id SERIAL,
  reputation_points INTEGER NOT NULL,
  reputation_rank student_rank NOT NULL, 
  reputation_version INTEGER UNIQUE NOT NULL DEFAULT 1,
  student varchar(20) REFERENCES student,
  PRIMARY KEY (reputation_id, student)
);

CREATE TABLE IF NOT EXISTS reputation_log (
  reputation_log_id SERIAL,
  reputation_log_action VARCHAR(150) NOT NULL,
  reputation_log_given_by varchar(15) NOT NULL,
  reputation_log_points INTEGER NOT NULL,
  reputation_id INTEGER,
  student VARCHAR(20),
  FOREIGN KEY (reputation_id, student) REFERENCES reputation(reputation_id, student),
  PRIMARY KEY (reputation_log_id)
);

CREATE TABLE IF NOT EXISTS student_course_class (
	username VARCHAR(20) NOT NULL REFERENCES student,
	course_id INTEGER NOT NULL REFERENCES course,
	class_id INTEGER,
	PRIMARY KEY (username, course_id)
);