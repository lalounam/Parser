CREATE DATABASE parser;

USE parser;

CREATE TABLE requests(
       date DATETIME NOT NULL,
       ip VARCHAR(15) NOT NULL,
       request VARCHAR(500) NOT NULL,
       status INT NOT NULL,
       useragent VARCHAR(500) NOT NULL
)ENGINE = MyISAM;

CREATE TABLE blocked(
	date_blocked DATETIME NOT NULL,
	ip_blocked VARCHAR(15) NOT NULL,
	requests_made INT NOT NULL,
	interval_duration VARCHAR(6) NOT NULL,
	description VARCHAR(255) NOT NULL
)ENGINE = MyISAM;