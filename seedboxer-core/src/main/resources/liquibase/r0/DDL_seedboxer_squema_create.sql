--liquibase formatted sql

--changeset author:The-Sultan endDelimiter:; stripComments:true

--
-- Table structure for table `configurations`
--

CREATE TABLE IF NOT EXISTS `users_configs` (
  `id` INTEGER PRIMARY KEY,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL DEFAULT '',
  `value` varchar(301) NOT NULL
);

--
-- Dumping data for table `configurations`
--


-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` INTEGER PRIMARY KEY,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `admin` tinyint(1) NOT NULL,
  `apikey` varchar(15),
  `status` varchar(10) NOT NULL
);

--
-- Dumping data for table `users`
--


-- --------------------------------------------------------

--
-- Table structure for table `downloads_queue`
--

CREATE TABLE IF NOT EXISTS `downloads_queue` (
`id` INTEGER PRIMARY KEY,
`user_id` int NOT NULL ,
`queue_order` int NOT NULL ,
`download` varchar( 200 ) NOT NULL ,
`created_on` datetime NOT NULL ,
`in_progress` boolean NOT NULL DEFAULT 0
);


-- --------------------------------------------------------
--
-- Table structure for table `content`
--


CREATE TABLE IF NOT EXISTS `content` (
  `id` INTEGER PRIMARY KEY,
  `name` varchar(200) NOT NULL,
  `history` bit(1) NOT NULL,
  `user_id` int(11) NOT NULL
);

-- --------------------------------------------------------
--
-- Table structure for table `tv_show`
--


CREATE TABLE IF NOT EXISTS `tv_show` (
  `content_id` INTEGER PRIMARY KEY,
  `season` int(11) DEFAULT NULL,
  `episode` int(11) DEFAULT NULL,
  `quality` varchar(20) DEFAULT NULL
);

-- --------------------------------------------------------
--
-- Table structure for table `feeds`
--


CREATE TABLE IF NOT EXISTS `feeds` (
  `id` INTEGER PRIMARY KEY,
  `url` varchar(250) NOT NULL
);
