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