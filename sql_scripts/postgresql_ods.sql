----------------------------------------------------------------------------------
-- Main Table
-- 
----------------------------------------------------------------------------------
DROP TABLE IF EXISTS qasession CASCADE;
DROP TABLE IF EXISTS attendee CASCADE;
DROP TABLE IF EXISTS question CASCADE;
DROP TABLE IF EXISTS answer CASCADE;
DROP TABLE IF EXISTS usertranslate CASCADE;


----------------------------------------------------------------------------------
-- TABLE usertranslate
-- Create Table usertranslate to store user mapping information
----------------------------------------------------------------------------------
CREATE TABLE usertranslate
(
user_id VARCHAR(200) NOT NULL UNIQUE,
facebook_user_id VARCHAR(200) UNIQUE,
google_user_id VARCHAR(200) UNIQUE,
twitter_user_id VARCHAR(200) UNIQUE,
name VARCHAR(200) NOT NULL,
login_user_id_type VARCHAR(200) NOT NULL,
create_timestamp TIMESTAMP,
update_timestamp TIMESTAMP,
PRIMARY KEY (user_id)
);  -- USERTRANSLATE

----------------------------------------------------------------------------------
-- TABLE qasession
-- Create Table session to store session information
----------------------------------------------------------------------------------
CREATE TABLE qasession
(
qasession_id VARCHAR(200) NOT NULL UNIQUE,
qasession_topic VARCHAR (200) NOT NULL,
qasession_description VARCHAR (2000),
qasession_status VARCHAR (60) NOT NULL,
qasession_max_question NUMERIC  NOT NULL,
created_by VARCHAR(200) NOT NULL REFERENCES usertranslate ON DELETE CASCADE ON UPDATE CASCADE,
updated_by VARCHAR(200) NOT NULL REFERENCES usertranslate,
create_timestamp TIMESTAMP NOT NULL,
update_timestamp TIMESTAMP NOT NULL,
PRIMARY KEY (qasession_id),
FOREIGN KEY (created_by) REFERENCES usertranslate (user_id),
FOREIGN KEY (updated_by) REFERENCES usertranslate (user_id)
);  -- SESSION

----------------------------------------------------------------------------------
-- TABLE attendee
-- Create Table attendee to store attendee information
----------------------------------------------------------------------------------
CREATE TABLE attendee
(
attendee_id VARCHAR(200) NOT NULL UNIQUE,
qasession_id VARCHAR(200) NOT NULL REFERENCES qasession ON DELETE CASCADE ON UPDATE CASCADE,
user_id VARCHAR(200) NOT NULL  REFERENCES usertranslate ON DELETE CASCADE ON UPDATE CASCADE,
qasession_role VARCHAR (60) NOT NULL,
create_timestamp TIMESTAMP NOT NULL,
update_timestamp TIMESTAMP NOT NULL,
FOREIGN KEY (qasession_id) REFERENCES qasession (qasession_id),
FOREIGN KEY (user_id) REFERENCES usertranslate (user_id)
);  -- ATTENDEE


----------------------------------------------------------------------------------
-- TABLE question
-- Create Table question to store question information
----------------------------------------------------------------------------------
CREATE TABLE question
(
qasession_id VARCHAR(200) NOT NULL REFERENCES qasession ON DELETE CASCADE ON UPDATE CASCADE,
question_id VARCHAR(200) NOT NULL UNIQUE,
question_content VARCHAR(2000),
created_by VARCHAR(200) NOT NULL REFERENCES usertranslate,
updated_by VARCHAR(200) NOT NULL REFERENCES usertranslate,
question_status VARCHAR (60) NOT NULL,
create_timestamp TIMESTAMP NOT NULL,
update_timestamp TIMESTAMP NOT NULL,
PRIMARY KEY (question_id),
FOREIGN KEY (qasession_id) REFERENCES qasession (qasession_id),
FOREIGN KEY (created_by) REFERENCES usertranslate (user_id),
FOREIGN KEY (updated_by) REFERENCES usertranslate (user_id)
);  -- question

----------------------------------------------------------------------------------
-- TABLE answer
-- Create Table answer to store answer information
----------------------------------------------------------------------------------
CREATE TABLE answer
(
answer_id VARCHAR(200) NOT NULL UNIQUE,
answer_content VARCHAR(2000),
question_id VARCHAR(200) NOT NULL REFERENCES question ON DELETE CASCADE ON UPDATE CASCADE, 
created_by VARCHAR(200) NOT NULL,
updated_by VARCHAR(200) NOT NULL,
create_timestamp TIMESTAMP NOT NULL,
update_timestamp TIMESTAMP NOT NULL,
PRIMARY KEY (answer_id),
FOREIGN KEY (question_id) REFERENCES question (question_id),
FOREIGN KEY (created_by) REFERENCES usertranslate (user_id),
FOREIGN KEY (updated_by) REFERENCES usertranslate (user_id)
);  -- answer




