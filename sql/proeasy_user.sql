CREATE USER 'proeasy'@'%' IDENTIFIED BY  'proeasy';

GRANT SELECT ON  `proeasy` .  `users` TO  'proeasy'@'%';
GRANT SELECT ON  `proeasy` .  `configurations` TO  'proeasy'@'%';