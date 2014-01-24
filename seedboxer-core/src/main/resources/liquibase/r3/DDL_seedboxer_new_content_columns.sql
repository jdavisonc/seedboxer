--liquibase formatted sql

--changeset author:jdavisonc endDelimiter:; stripComments:true

ALTER TABLE `content` ADD COLUMN url VARCHAR(255);
ALTER TABLE `content` ADD COLUMN external_id VARCHAR(255);