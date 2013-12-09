--liquibase formatted sql

--changeset author:jdavisonc endDelimiter:; stripComments:true

UPDATE users_configs set name = 'TransferUrl' where name = 'FtpUrl';
UPDATE users_configs set name = 'TransferPassword' where name = 'FtpPassword';
UPDATE users_configs set name = 'TransferRemoteDir' where name = 'FtpRemoteDir';
UPDATE users_configs set name = 'TransferUsername' where name = 'FtpUsername';