DROP DATABASE journalDB;
DROP DATABASE messageDB;
DROP DATABASE accountDB;

CREATE DATABASE IF NOT EXISTS messageDB;
CREATE DATABASE IF NOT EXISTS journalDB;
CREATE DATABASE IF NOT EXISTS accountDB;
CREATE DATABASE IF NOT EXISTS searchDB;

#USE messageDB;
USE journalDB;
USE accountDB;
USE searchDB;

# MessageDB
SELECT * FROM Message;

# Journal DB
SELECT * FROM account;

SELECT * FROM session;

SELECT * FROM patient;

SELECT * FROM staff;

SELECT * FROM diagnosis;

SELECT * FROM chat;

SELECT * FROM encounter;

SELECT * FROM observation;

# Fruit Quarkus test
CREATE TABLE fruit (
  id INT PRIMARY KEY,
  name VARCHAR(100)
);
INSERT INTO fruit (id, name) VALUES (1, 'Cherry');
INSERT INTO fruit (id, name) VALUES (2, 'Apple');
INSERT INTO fruit (id, name) VALUES (3, 'Banana');


