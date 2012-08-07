CREATE USER 'proeasy'@'%' IDENTIFIED BY  'proeasy';

GRANT SELECT ON  `proeasy`.`users` TO  'proeasy'@'%';
GRANT SELECT, INSERT, UPDATE ON  `proeasy`.`users_configs` TO  'proeasy'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON  `proeasy`.`downloads_queue` TO  'proeasy'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON  `proeasy`.`content` TO  'proeasy'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON  `proeasy`.`tv_show` TO  'proeasy'@'%';