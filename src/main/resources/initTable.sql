DROP TABLE IF EXISTS BPI;
CREATE TABLE BPI(
ID INT PRIMARY KEY,
CODE CHAR(3),
NAME NVARCHAR(5),
SYMBOL VARCHAR(10),
RATE FLOAT,
DESCRIPTION VARCHAR(30),
RATE_FLOAT FLOAT
);