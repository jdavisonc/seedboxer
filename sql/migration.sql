insert into `proeasy`.`users_configs` (user_id, name, value) select user_id, name, value from configurations;

update `proeasy`.`users_configs` set name = replace(name, ‘proEasy’, ‘proeasy’);