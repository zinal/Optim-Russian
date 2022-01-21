-- Table structure for data dictionaries.
-- Checked on Db2 and H2.

/*
DROP TABLE dict_fio;
DROP TABLE dict_fio_male;
DROP TABLE dict_fio_female;
DROP TABLE dict_name_last;
DROP TABLE dict_name_first;
DROP TABLE dict_name_middle;
*/

CREATE TABLE dict_fio(
   id INTEGER NOT NULL PRIMARY KEY, 
   nfull VARCHAR(150) NOT NULL, 
   nfirst VARCHAR(40) NOT NULL, 
   nmiddle VARCHAR(40) NOT NULL, 
   nlast VARCHAR(40) NOT NULL,
   sex CHAR(1) NOT NULL
);

CREATE TABLE dict_fio_male(
   id INTEGER NOT NULL PRIMARY KEY, 
   nfull VARCHAR(150) NOT NULL, 
   nfirst VARCHAR(40) NOT NULL, 
   nmiddle VARCHAR(40) NOT NULL, 
   nlast VARCHAR(40) NOT NULL
);

CREATE TABLE dict_fio_female(
   id INTEGER NOT NULL PRIMARY KEY, 
   nfull VARCHAR(150) NOT NULL, 
   nfirst VARCHAR(40) NOT NULL, 
   nmiddle VARCHAR(40) NOT NULL, 
   nlast VARCHAR(40) NOT NULL
);

CREATE TABLE dict_name_last(
   id INTEGER NOT NULL PRIMARY KEY, 
   val VARCHAR(40) NOT NULL, 
   sex CHAR(1) NOT NULL
);

CREATE TABLE dict_name_first(
   id INTEGER NOT NULL PRIMARY KEY, 
   val VARCHAR(40) NOT NULL, 
   sex CHAR(1) NOT NUL
);

CREATE INDEX dict_name_first_ix1 
   ON dict_name_first(val);

CREATE TABLE dict_name_middle(
   id INTEGER NOT NULL PRIMARY KEY, 
   val VARCHAR(40) NOT NULL, 
   sex CHAR(1) NOT NULL
);

-- End Of File
