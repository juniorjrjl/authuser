--liquibase formatted sql
--changeset junior:202210071140
--comment: user course relation table drop

DROP TABLE TB_USERS_COURSES;

--rollback CREATE TABLE TB_USERS_COURSES( id uuid not null primary key, user_id uuid not null, course_id uuid not null, CONSTRAINT FK_USERS_COURSES_USERS FOREIGN KEY(user_id) REFERENCES TB_USERS(id) );