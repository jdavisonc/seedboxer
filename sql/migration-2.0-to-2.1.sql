update `seedboxer`.`users_configs` set name = 'NotificationGCM' where name = 'NotificationC2dm';
update `seedboxer`.`users_configs` set name = 'NotificationGCMRegistrationId' where name = 'NotificationC2dmRegistrationId';
ALTER TABLE `seedboxer`.`users` ADD `status` varchar(10) DEFAULT 'STARTED' AFTER `password`;


delete from `seedboxer`.`users_configs` where name = 'NotificationEmail';
update `seedboxer`.`users_configs` set name = 'NotificationEmail' where name = 'NotificationEmailEmail';


ALTER TABLE  `seedboxer`.`users` ADD  `admin` BOOLEAN NOT NULL AFTER  `password`;
ALTER TABLE  `seedboxer`.`users` ADD  `apikey` VARCHAR(15) AFTER `admin`;