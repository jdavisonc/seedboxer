update `proeasy`.`users_configs` set name = 'NotificationGCM' where name = 'NotificationC2dm';
update `proeasy`.`users_configs` set name = 'NotificationGCMRegistrationId' where name = 'NotificationC2dmRegistrationId';
ALTER TABLE `proeasy`.`users` ADD `status` varchar(10) DEFAULT 'STARTED' AFTER `password`;
