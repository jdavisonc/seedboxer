CREATE USER 'proeasy'@'%' IDENTIFIED BY  'proeasy';

GRANT SELECT ON  `proeasy` .  `users` TO  'proeasy'@'%';
GRANT SELECT, INSERT ON  `proeasy` .  `configurations` TO  'proeasy'@'%';