--liquibase formatted sql

--changeset author:jdavisonc endDelimiter:; stripComments:true

DROP TABLE `downloads_queue`;

CREATE TABLE `downloads_queue` (
`id` INTEGER PRIMARY KEY AUTOINCREMENT,
`user_id` int NOT NULL ,
`queue_order` int NOT NULL ,
`download` varchar( 200 ) NOT NULL ,
`created_on` datetime NOT NULL ,
`in_progress` boolean NOT NULL DEFAULT 0
);