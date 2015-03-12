----------------------------------------------------------------------------------
-- Main Table
-- 
----------------------------------------------------------------------------------
DROP TABLE IF EXISTS SESSION CASCADE;
DROP TABLE IF EXISTS ATTENDEE CASCADE;
DROP TABLE IF EXISTS QUESTION CASCADE;
DROP TABLE IF EXISTS ANSWER CASCADE;
DROP TABLE IF EXISTS USERTRANSLATE CASCADE;


----------------------------------------------------------------------------------
-- TABLE SESSION
-- Create Table SESSION to store session information
----------------------------------------------------------------------------------
CREATE TABLE SESSION
(
SESSION_ID VARCHAR(200) NOT NULL UNIQUE,
SESSION_TOPIC VARCHAR (200) NOT NULL,
SESSION_DESCRIPTION VARCHAR (2000),
SESSION_STATUS VARCHAR (60) NOT NULL,
SESSION_MAX_QUESTION NUMERIC,
UPDATE_DATE DATE,
PRIMARY KEY (SESSION_ID)
);  -- SESSION

----------------------------------------------------------------------------------
-- TABLE USERTRANSLATE
-- Create Table USERTRANSLATE to store session information
----------------------------------------------------------------------------------
CREATE TABLE USERTRANSLATE
(
USER_ID VARCHAR(200) NOT NULL UNIQUE,
FB_USER_ID VARCHAR(200) UNIQUE,
FIRST_NAME VARCHAR(200),
LAST_NAME VARCHAR(200),
PRIMARY KEY (USER_ID)
);  -- USERTRANSLATE

----------------------------------------------------------------------------------
-- TABLE ATTENDEE
-- Create Table ATTENDEE to store session information
----------------------------------------------------------------------------------
CREATE TABLE ATTENDEE
(
ATTENDEE_ID VARCHAR(200) NOT NULL UNIQUE,
SESSION_ID VARCHAR(200) NOT NULL REFERENCES SESSION ON DELETE CASCADE ON UPDATE CASCADE,
USER_ID VARCHAR(200) NOT NULL,
SESSION_ROLE VARCHAR (60) NOT NULL,
UPDATE_DATE DATE,
FOREIGN KEY (SESSION_ID) REFERENCES SESSION (SESSION_ID),
FOREIGN KEY (USER_ID) REFERENCES USERTRANSLATE (USER_ID),
PRIMARY KEY (ATTENDEE_ID)
);  -- ATTENDEE


----------------------------------------------------------------------------------
-- TABLE QUESTION
-- Create Table QUESTION to store session information
----------------------------------------------------------------------------------
CREATE TABLE QUESTION
(
QUESTION_ID VARCHAR(200) NOT NULL UNIQUE,
QUESTION_CONTENT VARCHAR(2000),
SESSION_ID VARCHAR(200) NOT NULL  REFERENCES SESSION ON DELETE CASCADE ON UPDATE CASCADE, 
CREATED_BY VARCHAR(200) NOT NULL  REFERENCES ATTENDEE,
QUESTION_STATUS VARCHAR (60) NOT NULL,
UPDATE_DATE DATE,
PRIMARY KEY (QUESTION_ID),
FOREIGN KEY (SESSION_ID) REFERENCES SESSION (SESSION_ID),
FOREIGN KEY (CREATED_BY) REFERENCES ATTENDEE (ATTENDEE_ID)
);  -- QUESTION

----------------------------------------------------------------------------------
-- TABLE ANSWER
-- Create Table ANSWER to store session information
----------------------------------------------------------------------------------
CREATE TABLE ANSWER
(
ANSWER_ID VARCHAR(200) NOT NULL UNIQUE,
ANSWER_CONTENT VARCHAR(2000),
QUESTION_ID VARCHAR(200) NOT NULL REFERENCES QUESTION ON DELETE CASCADE ON UPDATE CASCADE, 
CREATED_BY VARCHAR(200) NOT NULL  REFERENCES ATTENDEE,
UPDATE_DATE DATE,
PRIMARY KEY (ANSWER_ID),
FOREIGN KEY (QUESTION_ID) REFERENCES QUESTION (QUESTION_ID),
FOREIGN KEY (CREATED_BY) REFERENCES ATTENDEE (ATTENDEE_ID)
);  -- ANSWER




