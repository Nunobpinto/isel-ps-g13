-- Organization Insert
insert into organization (created_by, organization_full_name, organization_short_name, organization_address, organization_contact, time_stamp)
    values ('ze','Instituto Superior de Engenharia de Lisboa','ISEL','Rua Emídio Navarro','218 317 000',current_timestamp);

-- Programmes Insert
insert into programme (created_by, programme_full_name, programme_short_name, programme_academic_degree, programme_total_credits, programme_duration, time_stamp)
    values ('ze', 'Licenciatura em Engenharia Informática e Computadores','LEIC','Licenciatura',180,6,current_timestamp);

insert into programme (created_by, programme_full_name, programme_short_name, programme_academic_degree, programme_total_credits, programme_duration, time_stamp)
    values ('ze', 'Licenciatura em Engenharia Informática e Multimédia','LEIM','Licenciatura',180,6,current_timestamp);

-- Courses Insert

insert into course (organization_id, created_by, course_full_name, course_short_name, time_stamp)
    values (1,'ze', 'Programação na Internet', 'PI', current_timestamp);

insert into course (organization_id, created_by, course_full_name, course_short_name, time_stamp)
    values (1,'ze', 'Redes de Computadores', 'RCP', current_timestamp);

insert into course (organization_id, created_by, course_full_name, course_short_name, time_stamp)
    values (1,'ze', 'Modelação de Ambientes Virtuais', 'MAV', current_timestamp);

-- Course-Programme Insert

insert into course_programme (course_id, programme_id, course_lectured_term, course_optional, course_credits, time_stamp)
    values(1,1,'quinto',false,6,current_timestamp);

insert into course_programme (course_id, programme_id, course_lectured_term, course_optional, course_credits, time_stamp)
    values(2,1,'quarto',false,6,current_timestamp);

insert into course_programme (course_id, programme_id, course_lectured_term, course_optional, course_credits, time_stamp)
    values(2,2,'quarto',false,6,current_timestamp);

insert into course_programme (course_id, programme_id, course_lectured_term, course_optional, course_credits, time_stamp)
    values(3,2,'quarto',false,6,current_timestamp);

-- Terms Insert

insert into term (term_short_name, term_year, term_type, time_stamp)
  values ('1718v',2018,'Summer',current_timestamp);

insert into term (term_short_name, term_year, term_type, time_stamp)
  values ('1718i',2017,'Winter',current_timestamp);

-- Classes Insert

insert into class (created_by, class_name, term_id, time_stamp)
  values ('ze','LI51D',1,current_timestamp);

insert into class (created_by, class_name, term_id, time_stamp)
  values ('ze','LI41D',2,current_timestamp);

insert into class (created_by, class_name, term_id, time_stamp)
  values ('ze','LEIM41D',2,current_timestamp);

-- Course-Term Insert

insert into course_term (course_id, term_id, time_stamp)
  values (1,1,current_timestamp);

insert into course_term (course_id, term_id, time_stamp)
  values (1,2,current_timestamp);

insert into course_term (course_id, term_id, time_stamp)
  values (2,1,current_timestamp);

insert into course_term (course_id, term_id, time_stamp)
  values (2,2,current_timestamp);

insert into course_term (course_id, term_id, time_stamp)
  values (3,2,current_timestamp);

-- Work-Assignment Insert

insert into course_misc_unit (misc_type, course_id, term_id, time_stamp)
    values ('Work Assignment', 1, 1, current_timestamp);

insert into work_assignment (id, created_by, sheet, supplement, due_date, individual, late_delivery, multiple_deliveries, requires_report, time_stamp)
    values (1,'ze','Exemplo-PI','Apoio',current_date,false ,true ,true ,false ,current_timestamp);

insert into course_misc_unit (misc_type, course_id, term_id, time_stamp)
    values ('Work Assignment', 1, 2, current_timestamp);

insert into work_assignment (id, created_by, sheet, supplement, due_date, individual, late_delivery, multiple_deliveries, requires_report, time_stamp)
    values (2,'ze','Exemplo-PI-2','Apoio',current_date,false ,true ,true ,false ,current_timestamp);

insert into course_misc_unit (misc_type, course_id, term_id, time_stamp)
    values ('Work Assignment', 3, 2, current_timestamp);

insert into work_assignment (id, created_by, sheet, supplement, due_date, individual, late_delivery, multiple_deliveries, requires_report, time_stamp)
    values (3,'ze','Exemplo-MAV','Apoio',current_date,false ,true ,true ,false ,current_timestamp);

-- Exam Insert

insert into course_misc_unit (misc_type, course_id, term_id, time_stamp)
    values ('Exam/Test', 1, 1, current_timestamp);

insert into exam (id, created_by, sheet, due_date, exam_type, phase, location, time_stamp)
    values (4, 'ze', '1ºexame de PI 1718v',current_date,'Exam', '1ª','A.2.14',current_timestamp);

insert into course_misc_unit (misc_type, course_id, term_id, time_stamp)
    values ('Exam/Test', 2, 1, current_timestamp);

insert into exam (id, created_by, sheet, due_date, exam_type, phase, location, time_stamp)
    values (5, 'ze', '1ºteste de RCP 1718v',current_date,'Test', '1ª','A.2.14',current_timestamp);

insert into course_misc_unit (misc_type, course_id, term_id, time_stamp)
    values ('Exam/Test', 2, 2, current_timestamp);

insert into exam (id, created_by, sheet, due_date, exam_type, phase, location, time_stamp)
    values (6, 'ze', '2ºexame de RCP 1718i',current_date,'Exam', '2ª','A.2.14',current_timestamp);

-- Course-class Insert

insert into course_class (course_id, class_id, term_id, time_stamp)
    values (1,1,1,current_timestamp);

insert into course_class (course_id, class_id, term_id, time_stamp)
    values (2,2,2,current_timestamp);

insert into course_class (course_id, class_id, term_id, time_stamp)
    values (3,2,2,current_timestamp);

-- Lecture Insert

insert into class_misc_unit (misc_type, course_id, class_id, term_id, time_stamp)
    values ('Lecture',1,1,1,current_timestamp);

insert into lecture (id, created_by, weekday, begins, duration, location, time_stamp)
    values (1, 'bruno', 'Monday', current_time, 120, 'E.1.07', current_timestamp);

insert into class_misc_unit (misc_type, course_id, class_id, term_id, time_stamp)
    values ('Lecture',2,2,2,current_timestamp);

insert into lecture (id, created_by, weekday, begins, duration, location, time_stamp)
    values (2, 'bruno', 'Tuesday', current_time, 120, 'G.1.07', current_timestamp);

insert into class_misc_unit (misc_type, course_id, class_id, term_id, time_stamp)
    values ('Lecture',3,2,2,current_timestamp);

insert into lecture (id, created_by, weekday, begins, duration, location, time_stamp)
    values (3, 'bruno', 'Friday', current_time, 120, 'G.0.14', current_timestamp);

-- Homework Insert

insert into class_misc_unit (misc_type, course_id, class_id, term_id, time_stamp)
    values ('Homework',1,1,1,current_timestamp);

insert into homework (id, created_by, sheet, due_date, late_delivery, multiple_deliveries, time_stamp)
    values (4,'bruno','Fazer router', current_date, true, true, current_timestamp );

insert into class_misc_unit (misc_type, course_id, class_id, term_id, time_stamp)
    values ('Homework',2,2,2,current_timestamp);

insert into homework (id, created_by, sheet, due_date, late_delivery, multiple_deliveries, time_stamp)
    values (5, 'ze', 'Criar switchs e routers nos nós',current_date, false , true , current_timestamp);

insert into class_misc_unit (misc_type, course_id, class_id, term_id, time_stamp)
    values ('Homework',3,2,2,current_timestamp);

insert into homework(id, created_by, sheet, due_date, late_delivery, multiple_deliveries, time_stamp)
    values (6, 'ze','Desenhar modelo do boneco', current_date, true , true , current_timestamp);

-- Student Insert

insert into student (student_username, student_given_name, student_family_name, student_personal_email, student_organization_email)
    values ('ze', 'José', 'Antunes', 'ze@gmail.com', 'ze@isel.pt');

insert into student (student_username, student_given_name, student_family_name, student_personal_email, student_organization_email)
    values ('bruno', 'Bruno', 'Filipe', 'bruno@gmail.com', 'bruno@isel.pt');

insert into student (student_username, student_given_name, student_family_name, student_personal_email, student_organization_email)
    values ('jg', 'João', 'Gameiro', 'jg@gmail.com', 'jg@isel.pt');

-- Reputation Insert

insert into reputation (reputation_points, reputation_rank, student)
  values (10,'Admin', 'ze');

insert into reputation (reputation_points, reputation_rank, student)
  values (5,'Admin', 'bruno');

insert into reputation (reputation_points, reputation_rank, student)
  values (1,'Beginner', 'jg');

-- Reputation Log Insert

insert into reputation_log (reputation_log_action, reputation_log_given_by, reputation_log_points, reputation_id, student)
  values ('insert course','bruno',5,1,'ze');

-- Student Course Class

insert into student_course_class (username, course_id, class_id)
  values ('ze', 3, 3);

insert into student_course_class (username, course_id, class_id)
  values ('ze', 2, 3);

insert into student_course_class (username, course_id, class_id)
  values ('bruno', 1, 1);
