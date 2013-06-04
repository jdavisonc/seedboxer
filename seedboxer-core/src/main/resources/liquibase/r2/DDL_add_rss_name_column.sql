--liquibase formatted sql

--changeset author:The-Sultan endDelimiter:; stripComments:true

ALTER TABLE feeds ADD COLUMN 'name' varchar(100);