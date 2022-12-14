--liquibase formatted sql
--changeset junior:202209011411
--comment: user table create

CREATE TABLE TB_USERS(
                         id uuid not null primary key,
                         username varchar(50) unique not null,
                         email varchar(50) unique not null,
                         "password" varchar(255) not null,
                         full_name varchar(50) not null,
                         user_status varchar(20) not null,
                         user_type varchar(20) not null,
                         phone_number varchar(20),
                         cpf varchar(20),
                         image_url varchar(255),
                         creation_date timestamp not null,
                         last_update_date timestamp not null
);

--rollback DROP TABLE TB_USERS;