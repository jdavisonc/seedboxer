--liquibase formatted sql

--changeset author:The-Sultan endDelimiter:; stripComments:true

CREATE USER 'seedboxer'@'%' IDENTIFIED BY  'seedboxer';
SET PASSWORD FOR 'seedboxer' = PASSWORD('seedboxer');

GRANT SELECT, UPDATE ON  `seedboxer`.`users` TO  'seedboxer'@'%';
GRANT SELECT, INSERT, UPDATE ON  `seedboxer`.`users_configs` TO  'seedboxer'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON  `seedboxer`.`downloads_queue` TO  'seedboxer'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON  `seedboxer`.`content` TO  'seedboxer'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON  `seedboxer`.`tv_show` TO  'seedboxer'@'%';
GRANT SELECT ON  `seedboxer`.`feeds` TO  'seedboxer'@'%';
