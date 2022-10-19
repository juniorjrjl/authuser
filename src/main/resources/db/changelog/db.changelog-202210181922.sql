--liquibase formatted sql
--changeset junior:202210181922
--comment: roles table create

CREATE TABLE TB_ROLES(
    id uuid not null primary key,
    role_name varchar(30) not null
);

--rollback DROP TABLE TB_ROLES;