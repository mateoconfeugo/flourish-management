DROP DATABASE management;
CREATE DATABASE management;
USE management;

DROP TABLE IF EXISTS version;
CREATE TABLE version (
id          INTEGER AUTO_INCREMENT PRIMARY KEY,
deployed_on DATETIME  NOT NULL DEFAULT '0000-00-00 00:00:00',
version     INTEGER(10)
);

DROP TABLE IF EXISTS user;
CREATE TABLE user (
id          INTEGER AUTO_INCREMENT PRIMARY KEY,
created DATETIME  NOT NULL DEFAULT '0000-00-00 00:00:00',
first_name VARCHAR(20),
password TEXT,
roles TEXT,
last_name  VARCHAR(30),
moniker     VARCHAR(120) NOT NULL,
status ENUM('new', 'active', 'inactive', 'suspended', 'expired', 'banded', 'banished')
);

DROP TABLE IF EXISTS profile;
CREATE TABLE profile (
id          INTEGER AUTO_INCREMENT PRIMARY KEY,
id_user INTEGER NULL,
tag_name VARCHAR(30),
query_uri TEXT);

/*

DROP TABLE IF EXISTS release;
CREATE TABLE release (
id          INTEGER AUTO_INCREMENT PRIMARY KEY,
released_on DATETIME  NOT NULL DEFAULT '0000-00-00 00:00:00',
tag     VARCHAR(50)
);

DROP TABLE IF EXISTS supported_release;
CREATE TABLE supported_release (
id          INTEGER AUTO_INCREMENT PRIMARY KEY,
deployed_on DATETIME  NOT NULL DEFAULT '0000-00-00 00:00:00',
released_on DATETIME  NOT NULL DEFAULT '0000-00-00 00:00:00',
version_id INTEGER(10)
);




DROP TABLE IF EXISTS user_email;
CREATE TABLE user_email (
user_id  INTEGER NOT NULL,
email_id INTEGER NOT NULL
);

DROP TABLE IF EXISTS user_group;
CREATE TABLE user_group (
user_id  INTEGER NOT NULL,
group_id INTEGER NOT NULL
);

DROP TABLE IF EXISTS user_authorization_role;
CREATE TABLE user_authorization_role (
user_id  INTEGER NOT NULL,
authorization_role_id INTEGER NOT NULL
);

DROP TABLE IF EXISTS business_role;
CREATE TABLE business_role (
user_id  INTEGER NOT NULL,
business_id INTEGER NOT NULL
);

DROP TABLE IF EXISTS business_account;
CREATE TABLE business_account (
user_id  INTEGER NOT NULL,
account_id INTEGER NOT NULL
);

DROP TABLE IF EXISTS user_landing_site;
CREATE TABLE business_account (
user_id  INTEGER NOT NULL,
landing_site_id INTEGER NOT NULL
);

DROP TABLE IF EXISTS population
CREATE TABLE population (
id       INTEGER AUTO_INCREMENT PRIMARY KEY,
admin-id INTEGER NOT NULL
);




description TEXT,
name VARCHAR(25)
);

DROP TABLE IF EXISTS lead_log;
CREATE TABLE lead_log (
id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
event_time  DATETIME,
email TEXT,
full_name  VARCHAR(50),
first_name VARCHAR(20),
last_name  VARCHAR(30),
postal_code   INT(20),
offer  INT(15),
phone TEXT,
adnetwork INT(11),
adgroup INT(11),
listing INT(15),
profile INT(10),
campaign INT(11),
market_vector INT(11),
landing_site INT(15),
user_agent TEXT
);

DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `log_id` INT(11) NOT NULL AUTO_INCREMENT,
  `log_source` VARCHAR(100) NOT NULL DEFAULT '',
  `log_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `log_level` VARCHAR(10) NOT NULL DEFAULT '',
  `log_mesg` VARCHAR(200) NOT NULL DEFAULT '',
  PRIMARY KEY  (`log_id`),
  KEY `log_date_idx` (`log_date`),
  KEY `log_source_idx` (`log_source`),
  KEY `log_mesg_idx` (`log_mesg`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

ALTER TABLE lead_log AUTO_INCREMENT = 1;
ALTER TABLE log AUTO_INCREMENT = 1;
*/
