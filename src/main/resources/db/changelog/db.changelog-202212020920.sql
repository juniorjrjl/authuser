--liquibase formatted sql
--changeset junior:202210182006
--comment: insert roles

INSERT INTO TB_ROLES (
    id,
    role_name
) VALUES (
    'e29daa9f-0a28-4451-b64e-1d6228159bca',
    'ROLE_USER'
),(
    '994392a1-69e7-480d-ad2d-3958729db41a',
    'ROLE_STUDENT'
),(
    '95c145ca-7cea-41a1-a572-48d2602e52bb',
    'ROLE_INSTRUCTOR'
),(
    '0294553f-b994-4626-a511-1a9fe4327529',
    'ROLE_ADMIN'
);

--rollback DELETE FROM TB_ROLES WHERE id IN ("e29daa9f-0a28-4451-b64e-1d6228159bca", "994392a1-69e7-480d-ad2d-3958729db41a", "95c145ca-7cea-41a1-a572-48d2602e52bb", "0294553f-b994-4626-a511-1a9fe4327529")