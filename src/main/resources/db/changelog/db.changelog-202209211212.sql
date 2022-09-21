--liquibase formatted sql
--changeset junior:202209211212
--comment: user course relation table create

CREATE TABLE TB_USERS_COURSES(
                                 id uuid not null primary key,
                                 user_id uuid not null,
                                 course_id uuid not null,
                                 CONSTRAINT FK_USERS_COURSES_USERS FOREIGN KEY(user_id) REFERENCES TB_USERS(id)
);

--rollback DROP TABLE TB_USERS_COURSES;