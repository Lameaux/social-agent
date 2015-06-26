CREATE DATABASE social_agent CHARACTER SET utf8 COLLATE utf8_general_ci;

-- DROP TABLE IF EXISTS mail_account;
-- DROP INDEX IF EXISTS mail_account_unique;
-- DROP TABLE IF EXISTS mail_message;
-- DROP TABLE IF EXISTS twitter_account;
-- DROP TABLE IF EXISTS twitter_message;


CREATE TABLE IF NOT EXISTS mail_account (
	id INT auto_increment PRIMARY KEY, 
	login VARCHAR(255),
	domain VARCHAR(255),
	active INT DEFAULT 1
) ENGINE=InnoDB;
CREATE UNIQUE INDEX mail_account_unique 
ON mail_account(login, domain);

CREATE TABLE IF NOT EXISTS mail_message (
	id INT auto_increment PRIMARY KEY, 
	account_id INT,
	sender VARCHAR(255),
	subject TEXT,	
	size INT,
	created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS twitter_account (
	id VARCHAR(20) PRIMARY KEY, 
	screen_name VARCHAR(255),
	description VARCHAR(255),
	access_token VARCHAR(255),
	access_token_secret VARCHAR(255)	
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS twitter_message (
	id INT auto_increment PRIMARY KEY,
	account_id VARCHAR(20), 
	message_text VARCHAR(255)
) ENGINE=InnoDB;

