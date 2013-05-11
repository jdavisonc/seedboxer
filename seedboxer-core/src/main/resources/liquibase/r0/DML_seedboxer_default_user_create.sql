--liquibase formatted sql

--changeset author:The-Sultan endDelimiter:;

INSERT INTO users (username, password, admin, status) VALUES ("admin","d033e22ae348aeb5660fc2140aec35850c4da997","1","STARTED");