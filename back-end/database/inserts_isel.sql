CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Organization Insert
insert into isel.organization (organization_full_name, organization_short_name, organization_address, organization_contact, organization_website ,time_stamp)
    values ('Instituto Superior de Engenharia de Lisboa','ISEL','Rua Emídio Navarro','218 317 000', 'https://www.isel.pt/', current_timestamp);

insert into isel.organization_version(organization_version, created_by, organization_full_name, organization_short_name, organization_address, organization_contact, organization_website, time_stamp)
	values(1, 'ze', 'Instituto Superior de Engenharia de Lisboa', 'ISEL', 'Rua Emídio Navarro', '218 317 000', 'https://www.isel.pt/', current_timestamp);

-- Programmes Insert
insert into isel.programme (created_by, programme_full_name, programme_short_name, programme_academic_degree, programme_total_credits, programme_duration, time_stamp)
    values ('ze', 'Licenciatura em Engenharia Informática e Computadores','LEIC','Licenciatura',180,6,current_timestamp);

insert into isel.programme_version (programme_id, programme_version, created_by, programme_full_name, programme_short_name, programme_academic_degree, programme_total_credits, programme_duration, time_stamp)
    values (1, 1, 'ze', 'Licenciatura em Engenharia Informática e Computadores','LEIC','Licenciatura',180,6,current_timestamp);

insert into isel.programme (created_by, programme_full_name, programme_short_name, programme_academic_degree, programme_total_credits, programme_duration, time_stamp)
    values ('ze', 'Licenciatura em Engenharia Informática e Multimédia','LEIM','Licenciatura',180,6,current_timestamp);

insert into isel.programme_version (programme_id, programme_version, created_by, programme_full_name, programme_short_name, programme_academic_degree, programme_total_credits, programme_duration, time_stamp)
    values (2, 1, 'ze', 'Licenciatura em Engenharia Informática e Multimédia','LEIM','Licenciatura',180,6,current_timestamp);

-- Courses Insert

insert into isel.course (created_by, course_full_name, course_short_name, time_stamp)
    values ('ze', 'Programação na Internet', 'PI', current_timestamp);

insert into isel.course_version (course_id, course_version, created_by, course_full_name, course_short_name, time_stamp)
    values (1, 1, 'ze', 'Programação na Internet', 'PI', current_timestamp);

insert into isel.course_version (course_id, course_version, created_by, course_full_name, course_short_name, time_stamp)
    values (1, 2, 'jg', 'Programacao na Internet', 'PI', current_timestamp);

insert into isel.course (created_by, course_full_name, course_short_name, time_stamp)
    values ('ze', 'Redes de Computadores', 'RCP', current_timestamp);

insert into isel.course_version (course_id, course_version, created_by, course_full_name, course_short_name, time_stamp)
    values (2, 1, 'ze', 'Redes de Computadores', 'RCP', current_timestamp);

insert into isel.course (created_by, course_full_name, course_short_name, time_stamp)
    values ('ze', 'Modelação de Ambientes Virtuais', 'MAV', current_timestamp);

insert into isel.course_version (course_id, course_version, created_by, course_full_name, course_short_name, time_stamp)
    values (3, 1, 'ze', 'Modelação de Ambientes Virtuais', 'MAV', current_timestamp);

insert into isel.course_stage (course_full_name, course_short_name, created_by, votes, time_stamp)
    values ('Programação', 'PG', 'jg', 0, current_timestamp);

insert into isel.course_stage (course_full_name, course_short_name, created_by, votes, time_stamp)
    values ('Quimica I', 'QI', 'jg', 0, current_timestamp);

insert into isel.course_stage (course_full_name, course_short_name, created_by, votes, time_stamp)
    values ('Estruturas II', 'EII', 'jg', 0, current_timestamp);

insert into isel.course_report (course_id, course_full_name, course_short_name, reported_by, votes, time_stamp)
    values (3, 'Modelacao Ambientes Virtuais', 'MAV', 'Gedson', 2, current_timestamp);

insert into isel.course_report (course_id, course_full_name, course_short_name, reported_by, votes, time_stamp)
    values (3, 'Modelação Ambientes Virtuais', 'MAV', 'Paulo', 7, current_timestamp);



-- Course-Programme Insert

insert into isel.course_programme (course_id, programme_id, course_lectured_term, course_optional, course_credits, time_stamp, created_by)
    values(1,1,'quinto',false,6,current_timestamp,'ze');

insert into isel.course_programme_version(course_id, programme_id, course_programme_version, course_lectured_term, course_optional, course_credits, time_stamp, created_by)
	values(1, 1, 1, 'quinto', false, 6, current_timestamp, 'ze');

insert into isel.course_programme_version(course_id, programme_id, course_programme_version, course_lectured_term, course_optional, course_credits, time_stamp, created_by)
	values(1, 1, 2, 'sexto', true, 6, current_timestamp, 'ze');

insert into isel.course_programme (course_id, programme_id, course_lectured_term, course_optional, course_credits, time_stamp, created_by)
    values(2,1,'quarto',false,6,current_timestamp,'ze');

insert into isel.course_programme_version(course_id, programme_id, course_programme_version, course_lectured_term, course_optional, course_credits, time_stamp, created_by)
	values(2, 1, 1, 'quinto', false, 6, current_timestamp, 'ze');

insert into isel.course_programme (course_id, programme_id, course_lectured_term, course_optional, course_credits, time_stamp, created_by)
    values(2,2,'quarto',false,6,current_timestamp,'ze');

insert into isel.course_programme_version(course_id, programme_id, course_programme_version, course_lectured_term, course_optional, course_credits, time_stamp, created_by)
	values(2, 2, 1, 'quinto', false, 6, current_timestamp, 'ze');

insert into isel.course_programme (course_id, programme_id, course_lectured_term, course_optional, course_credits, time_stamp, created_by)
    values(3,2,'quarto',false,6,current_timestamp,'ze');

insert into isel.course_programme_version(course_id, programme_id, course_programme_version, course_lectured_term, course_optional, course_credits, time_stamp, created_by)
	values(3, 2, 1, 'quinto', false, 6, current_timestamp, 'ze');

insert into isel.course_programme_stage(course_id, programme_id, course_lectured_term, course_optional,course_credits, created_by,votes,time_stamp)
  values(1, 1, 'sixth', false, 7, 'ruben', 3, current_timestamp);

insert into isel.course_programme_stage(course_id, programme_id, course_lectured_term, course_optional,course_credits, created_by,votes,time_stamp)
  values(3, 1, 'thirth', true, 6, 'rui', 2, current_timestamp);

insert into isel.course_programme_stage(course_id, programme_id, course_lectured_term, course_optional,course_credits, created_by,votes,time_stamp)
  values(1, 2, 'second', true, 5, 'igor', 1, current_timestamp);

insert into isel.course_programme_report(course_id, programme_id, course_lectured_term, course_optional, course_credits, to_delete, time_stamp, reported_by, votes)
  values(2, 1, 'first', true, 6, false, current_timestamp, 'miguel', 24);

insert into isel.course_programme_report(course_id, programme_id, course_lectured_term, course_optional, course_credits, to_delete, time_stamp, reported_by, votes)
  values(2, 1, 'fourth', false, 6, false, current_timestamp, 'cristiano', 31);


-- Terms Insert

insert into isel.term (term_short_name, term_year, term_type, time_stamp)
  values ('1718v',2018,'SUMMER',current_timestamp);

insert into isel.term (term_short_name, term_year, term_type, time_stamp)
  values ('1718i',2017,'WINTER',current_timestamp);

-- Classes Insert

insert into isel.class (created_by, class_name, term_id, programme_id, time_stamp)
  values ('ze','LI51D',1, 1, current_timestamp);

insert into isel.class_version (class_id, class_version, created_by, class_name, term_id, programme_id, time_stamp)
  values (1, 1, 'ze','LI51D',1, 1, current_timestamp);

insert into isel.class (created_by, class_name, term_id, programme_id, time_stamp)
  values ('ze','LI41D',2,1,current_timestamp);

insert into isel.class_version (class_id, class_version, created_by, class_name, term_id, programme_id, time_stamp)
  values (2, 1, 'ze','LI41D',2,1,current_timestamp);

insert into isel.class (created_by, class_name, term_id, programme_id, time_stamp)
  values ('ze','LEIM41D',2,2,current_timestamp);

insert into isel.class_version (class_id, class_version, created_by, class_name, term_id, programme_id, time_stamp)
  values (3, 1, 'ze','LEIM41D',2,2,current_timestamp);

-- Course-Term Insert

insert into isel.course_term (course_id, term_id, time_stamp)
  values (1,1,current_timestamp);

insert into isel.course_term (course_id, term_id, time_stamp)
  values (1,2,current_timestamp);

insert into isel.course_term (course_id, term_id, time_stamp)
  values (2,1,current_timestamp);

insert into isel.course_term (course_id, term_id, time_stamp)
  values (2,2,current_timestamp);

insert into isel.course_term (course_id, term_id, time_stamp)
  values (3,2,current_timestamp);

-- Work-Assignment Insert

insert into isel.course_misc_unit (misc_type, course_id, term_id)
    values ('WORK_ASSIGNMENT', 1, 1);

insert into isel.course_misc_unit_stage(misc_type, course_id, term_id)
    values ('EXAM_TEST', 1, 1);

insert into isel.work_assignment (work_assignment_id, created_by, phase, sheet_id, supplement_id, due_date, individual, late_delivery, multiple_deliveries, requires_report, time_stamp)
    values (1,'ze', '1º',gen_random_uuid(),gen_random_uuid(),current_date,false ,true ,true ,false ,current_timestamp);

insert into isel.work_assignment_version (work_assignment_id, work_assignment_version, phase, created_by, sheet_id, supplement_id, due_date, individual, late_delivery, multiple_deliveries, requires_report, time_stamp)
    values (1, 1, '1º', 'ze', gen_random_uuid(),gen_random_uuid(),current_date,false ,true ,true ,false ,current_timestamp);

insert into isel.course_misc_unit (misc_type, course_id, term_id)
    values ('WORK_ASSIGNMENT', 1, 2);

insert into isel.work_assignment (work_assignment_id, created_by, phase, sheet_id, supplement_id, due_date, individual, late_delivery, multiple_deliveries, requires_report, time_stamp)
    values (2,'ze', '2º', gen_random_uuid(), gen_random_uuid(), current_date,false ,true ,true ,false ,current_timestamp);

insert into isel.work_assignment_version (work_assignment_id, work_assignment_version, phase, created_by, sheet_id, supplement_id, due_date, individual, late_delivery, multiple_deliveries, requires_report, time_stamp)
    values (2, 1, '2º' ,'ze', gen_random_uuid(),gen_random_uuid(),current_date,false ,true ,true ,false ,current_timestamp);

insert into isel.course_misc_unit (misc_type, course_id, term_id)
    values ('WORK_ASSIGNMENT', 3, 2);

insert into isel.work_assignment (work_assignment_id, created_by, phase, sheet_id, supplement_id, due_date, individual, late_delivery, multiple_deliveries, requires_report, time_stamp)
    values (3,'ze', '3º',gen_random_uuid(),gen_random_uuid(),current_date,false ,true ,true ,false ,current_timestamp);

insert into isel.work_assignment_version (work_assignment_id, work_assignment_version, created_by, phase, sheet_id, supplement_id, due_date, individual, late_delivery, multiple_deliveries, requires_report, time_stamp)
    values (3, 1, 'ze', '3º', gen_random_uuid(),gen_random_uuid(),current_date,false ,true ,true ,false ,current_timestamp);

-- Exam Insert

insert into isel.course_misc_unit (misc_type, course_id, term_id)
    values ('EXAM_TEST', 1, 1);

insert into isel.exam (exam_id, created_by, sheet_id, due_date, exam_type, phase, location, time_stamp)
    values (4, 'ze', gen_random_uuid() ,current_date,'EXAM', '1ª','A.2.14',current_timestamp);

insert into isel.exam_version (exam_id, exam_version, created_by, sheet_id, due_date, exam_type, phase, location, time_stamp)
    values (4, 1, 'ze', gen_random_uuid() ,current_date,'EXAM', '1ª','A.2.14',current_timestamp);

insert into isel.course_misc_unit (misc_type, course_id, term_id)
    values ('EXAM_TEST', 2, 1);

insert into isel.exam (exam_id, created_by, sheet_id, due_date, exam_type, phase, location, time_stamp)
    values (5, 'ze', gen_random_uuid() ,current_date,'TEST', '1ª','A.2.14',current_timestamp);

insert into isel.exam_version (exam_id, exam_version, created_by, sheet_id, due_date, exam_type, phase, location, time_stamp)
    values (5, 1, 'ze', gen_random_uuid(),current_date,'TEST', '1ª','A.2.14',current_timestamp);

insert into isel.course_misc_unit (misc_type, course_id, term_id)
    values ('EXAM_TEST', 2, 2);

insert into isel.exam (exam_id, created_by, sheet_id, due_date, exam_type, phase, location, time_stamp)
    values (6, 'ze', gen_random_uuid() ,current_date,'EXAM', '2ª','A.2.14',current_timestamp);

insert into isel.exam_version (exam_id, exam_version, created_by, sheet_id, due_date, exam_type, phase, location, time_stamp)
    values (6, 1, 'ze', gen_random_uuid(),current_date,'EXAM', '2ª','A.2.14',current_timestamp);

-- Course-class Insert

insert into isel.course_class (course_id, class_id, term_id, created_by, votes, time_stamp)
	values (1,1,1, 'rui', 0, current_timestamp);

insert into isel.course_class (course_id, class_id, term_id, created_by, votes, time_stamp)
    values (2,2,2, 'igor', 1, current_timestamp);

insert into isel.course_class (course_id, class_id, term_id, created_by, votes, time_stamp)
    values (3,2,2,'andre', 2, current_timestamp);

-- Lecture Insert

insert into isel.class_misc_unit (misc_type, course_class_id)
    values ('LECTURE', 1);

insert into isel.lecture (lecture_id, created_by, weekday, begins, duration, location, time_stamp)
    values (1, 'bruno', 'MONDAY', current_time, '02:00:00', 'E.1.07', current_timestamp);

insert into isel.lecture_version (lecture_id, lecture_version, created_by, weekday, begins, duration, location, time_stamp)
    values (1, 1, 'bruno', 'MONDAY', current_time, '02:00:00', 'E.1.07', current_timestamp);

insert into isel.class_misc_unit (misc_type, course_class_id)
    values ('LECTURE', 2);

insert into isel.lecture (lecture_id, created_by, weekday, begins, duration, location, time_stamp)
    values (2, 'bruno', 'TUESDAY', current_time, '02:00:00', 'G.1.07', current_timestamp);

insert into isel.lecture_version (lecture_id, lecture_version, created_by, weekday, begins, duration, location, time_stamp)
    values (2, 1, 'bruno', 'TUESDAY', current_time, '02:00:00', 'G.1.07', current_timestamp);

insert into isel.class_misc_unit (misc_type, course_class_id)
    values ('LECTURE', 3);

insert into isel.lecture (lecture_id, created_by, weekday, begins, duration, location, time_stamp)
    values (3, 'bruno', 'FRIDAY', current_time, '02:00:00', 'G.0.14', current_timestamp);

insert into isel.lecture_version (lecture_id, lecture_version, created_by, weekday, begins, duration, location, time_stamp)
    values (3, 1, 'bruno', 'FRIDAY', current_time, '02:00:00', 'G.0.14', current_timestamp);

-- Homework Insert

insert into isel.class_misc_unit (misc_type, course_class_id)
    values ('HOMEWORK', 1);

insert into isel.homework (homework_id, created_by, homework_name, sheet_id, due_date, late_delivery, multiple_deliveries, time_stamp)
    values (4,'bruno', 'TPC01',gen_random_uuid(), current_date, true, true, current_timestamp);

insert into isel.homework_version (homework_id, homework_version, homework_name, created_by, sheet_id, due_date, late_delivery, multiple_deliveries, time_stamp)
    values (4, 1, 'TPC01', 'bruno',gen_random_uuid(), current_date, true, true, current_timestamp);

insert into isel.class_misc_unit (misc_type, course_class_id)
    values ('HOMEWORK', 2);

insert into isel.homework (homework_id, created_by, homework_name, sheet_id, due_date, late_delivery, multiple_deliveries, time_stamp)
    values (5, 'ze', 'TPC02', gen_random_uuid(),current_date, false , true , current_timestamp);

insert into isel.homework_version (homework_id, homework_version, homework_name, created_by, sheet_id, due_date, late_delivery, multiple_deliveries, time_stamp)
    values (5, 1, 'TPC02', 'ze', gen_random_uuid(),current_date, false , true , current_timestamp);

insert into isel.class_misc_unit (misc_type, course_class_id)
    values ('HOMEWORK', 1);

insert into isel.homework(homework_id, created_by, homework_name, sheet_id, due_date, late_delivery, multiple_deliveries, time_stamp)
    values (6, 'ze', 'TPC03', gen_random_uuid(), current_date, true , true , current_timestamp);

insert into isel.homework_version (homework_id, homework_version, homework_name, created_by, sheet_id, due_date, late_delivery, multiple_deliveries, time_stamp)
    values (6, 1, 'TPC03', 'ze', gen_random_uuid(), current_date, true , true , current_timestamp);

-- Student Insert

insert into isel.user_account (user_username, user_password, user_given_name, user_family_name, user_confirmed, user_email, user_locked)
    values ('ze', 1234, 'José', 'Antunes', true, 'ze@isel.pt', false);

insert into isel.user_account (user_username, user_password, user_given_name, user_family_name, user_confirmed, user_email, user_locked)
    values ('bruno', 1234, 'Bruno', 'Filipe', true, 'bruno@isel.pt', false);

insert into isel.user_account (user_username, user_password, user_given_name, user_family_name, user_confirmed, user_email, user_locked)
    values ('jg', 1234, 'João', 'Gameiro', true, 'jg@isel.pt', false);

-- Reputation Insert

insert into isel.reputation (points, role, user_username)
  values (10, 'ROLE_BEGINNER', 'ze');

insert into isel.reputation (points, role, user_username)
  values (5, 'ROLE_BEGINNER', 'bruno');

insert into isel.reputation (points, role, user_username)
  values (55, 'ROLE_ADMIN', 'jg');

-- Reputation Log Insert

insert into isel.action_log (user_username, action, entity, log_id, time_stamp)
  values ('bruno','CREATE','course',2,current_timestamp);
insert into isel.action_log (user_username, action, entity, log_id, time_stamp)
  values ('bruno','VOTE_UP','course',1,current_timestamp);

-- Reputation Log Insert

insert into isel.reputation_log (reputation_log_action, reputation_log_given_by, reputation_log_points, reputation_id, user_username)
  values (2,'bruno',5,1,'ze');

-- Student Course Class

insert into isel.user_course_class (user_username, course_id, course_class_id)
  values ('ze', 3, 3);

insert into isel.user_course_class (user_username, course_id, course_class_id)
  values ('ze', 2, 3);

insert into isel.user_course_class (user_username, course_id, course_class_id)
  values ('bruno', 1, 1);

-- Student Programme

insert into isel.user_programme (user_username, programme_id)
  values ('bruno', 1);

insert into isel.user_programme (user_username, programme_id)
  values ('ze', 2);