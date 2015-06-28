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
	screen_name VARCHAR(20),
	description VARCHAR(255),
	access_token VARCHAR(255),
	access_token_secret VARCHAR(255)	
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS twitter_group (
	id INT auto_increment PRIMARY KEY, 
	title VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS twitter_account_in_group (
	account_id VARCHAR(20),
	group_id INT, 
	primary key(account_id, group_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS action_follow (
	id INT auto_increment PRIMARY KEY,
	screen_name VARCHAR(20),
	target_screen_name VARCHAR(20),
	status INT DEFAULT 0,
	status_text TEXT
) ENGINE=InnoDB;
CREATE INDEX action_follow_screen_name 
ON action_follow(screen_name);





CREATE TABLE IF NOT EXISTS twitter_message (
	id INT auto_increment PRIMARY KEY,
	account_id VARCHAR(20), 
	message_text VARCHAR(255)
) ENGINE=InnoDB;

