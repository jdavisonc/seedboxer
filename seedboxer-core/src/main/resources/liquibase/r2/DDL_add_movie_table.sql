--liquibase formatted sql

--changeset author:The-Sultan endDelimiter:; stripComments:true

CREATE TABLE  `movie` (
  `content_id` INTEGER PRIMARY KEY,
  `year` int(11) DEFAULT NULL,
  `quality` varchar(20) DEFAULT NULL
);