----------------------------------------------------------------------------------
-- Main Table
-- 
----------------------------------------------------------------------------------
DROP TABLE IF EXISTS SESSION CASCADE;
DROP TABLE IF EXISTS QUESTION CASCADE;
DROP TABLE IF EXISTS ANSWER CASCADE;
DROP TABLE IF EXISTS ATTENDEE CASCADE;

----------------------------------------------------------------------------------
-- TABLE SESSION
-- Create Table SESSION to store session information
----------------------------------------------------------------------------------
CREATE TABLE SESSION
(
SESSION_ID VARCHAR(60) NOT NULL UNIQUE,
SESSION_TOPIC VARCHAR (120) NOT NULL,
SESSION_DESCRIPTION VARCHAR (1000),
SESSION_STATUS VARCHAR (20) NOT NULL,
SESSION_MAX_QUESTION NUMERIC,
UPDATE_DATE DATE,
PRIMARY KEY (SESSION_ID)
);  -- USER_ACCOUNT

----------------------------------------------------------------------------------
-- TABLE QUESTION
-- Create Table QUESTION to store session information
----------------------------------------------------------------------------------
CREATE TABLE QUESTION
(
QUESTION_ID VARCHAR(60) NOT NULL UNIQUE,
QUESTION_CONTENT (2000),
SESSION_ID VARCHAR(60) NOT NULL  REFERENCES SESSION ON DELETE CASCADE ON UPDATE CASCADE, 
CREATED_BY VARCHAR(100) NOT NULL,
QUESTION_STATUS VARCHAR (20) NOT NULL,
UPDATE_DATE DATE,
PRIMARY KEY (QUESTION_ID),
FOREIGN KEY (SESSION_ID) REFERENCES SESSION (SESSION_ID)
);  -- QUESTION

----------------------------------------------------------------------------------
-- TABLE ANSWER
-- Create Table ANSWER to store session information
----------------------------------------------------------------------------------
CREATE TABLE ANSWER
(
ANSWER_ID VARCHAR(60) NOT NULL UNIQUE,
QUESTION_CONTENT (2000),
QUESTION_ID VARCHAR(60) NOT NULL REFERENCES QUESTION ON DELETE CASCADE ON UPDATE CASCADE, 
CREATED_BY VARCHAR(100) NOT NULL,
UPDATE_DATE DATE,
PRIMARY KEY (ANSWER_ID),
FOREIGN KEY (QUESTION_ID) REFERENCES QUESTION (QUESTION_ID)
);  -- QUESTION

----------------------------------------------------------------------------------
-- TABLE ATTENDEE
-- Create Table ATTENDEE to store session information
----------------------------------------------------------------------------------
CREATE TABLE ATTENDEE
(
SESSION_ID VARCHAR(60) NOT NULL REFERENCES SESSION ON DELETE CASCADE ON UPDATE CASCADE,
ATTENDEE_EMAIL VARCHAR(100) NOT NULL,
SESSION_ROLE VARCHAR (20) NOT NULL,
UPDATE_DATE DATE,
FOREIGN KEY (SESSION_ID) REFERENCES SESSION (SESSION_ID)
);  -- QUESTION