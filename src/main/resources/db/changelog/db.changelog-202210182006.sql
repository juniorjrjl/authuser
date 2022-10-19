--liquibase formatted sql
--changeset junior:202210182006
--comment: roles table create

CREATE TABLE TB_USERS_ROLES(
    user_id uuid not null,
    role_id uuid not null,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT FK_USERS_USERS_ROLES FOREIGN KEY(user_id) REFERENCES TB_USERS(id),
    CONSTRAINT FK_ROLES_USERS_ROLES FOREIGN KEY(role_id) REFERENCES TB_ROLES(id)
);

--rollback DROP TABLE TB_USERS_ROLES;