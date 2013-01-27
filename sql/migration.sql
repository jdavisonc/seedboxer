delete from `seedboxer`.`users_configs` where name = 'NotificationEmail';
update `seedboxer`.`users_configs` set name = 'NotificationEmail' where name = 'NotificationEmailEmail';


ALTER TABLE  `seedboxer`.`users` ADD  `admin` BOOLEAN NOT NULL AFTER  `password`;
ALTER TABLE  `seedboxer`.`users` ADD  `apikey` VARCHAR(15) NOT NULL AFTER `admin`;