CREATE SCHEMA optim1;

CREATE SEQUENCE optim1.customer_seq;

CREATE TABLE optim1.customer (
  custid INTEGER NOT NULL,
  custmode CHAR(1) NOT NULL,
  CONSTRAINT customer_pk PRIMARY KEY(custid),
  CONSTRAINT customer_ck1 CHECK(custmode IN ('P', 'L'))
);

CREATE TABLE optim1.legal_entity (
  custid INTEGER NOT NULL,
  le_name VARCHAR(200) NOT NULL,
  le_num_reg VARCHAR(50) NULL,
  le_num_pay VARCHAR(50) NULL,
  CONSTRAINT le_pk PRIMARY KEY(custid),
  CONSTRAINT le_fk1 FOREIGN KEY(custid) REFERENCES optim1.customer
);

CREATE TABLE optim1.physical_entity (
  custid INTEGER NOT NULL,
  pe_name_full VARCHAR(200) NOT NULL,
  pe_name_first VARCHAR(100) NOT NULL,
  pe_name_middle VARCHAR(100) NULL,
  pe_name_last VARCHAR(100) NULL,
  pe_sex CHAR(1) NOT NULL,
  pe_num_pay VARCHAR(50) NULL,
  pe_num_soc VARCHAR(50) NULL,
  pe_birthday DATE NULL,
  pe_passp_num VARCHAR(50) NULL,
  pe_passp_iss_date DATE NULL,
  pe_forg_num VARCHAR(50) NULL,
  pe_forg_iss_date DATE NULL,
  pe_forg_exp_date DATE NULL,
  CONSTRAINT pe_pk PRIMARY KEY(custid),
  CONSTRAINT pe_fk1 FOREIGN KEY(custid) REFERENCES optim1.customer,
  CONSTRAINT pe_ck1 CHECK(pe_sex IN ('M','F'))
);

CREATE SEQUENCE optim1.contact_seq;

CREATE TABLE optim1.contact (
  contid INTEGER NOT NULL,
  custid INTEGER NOT NULL,
  contmode CHAR(1) NOT NULL,
  CONSTRAINT contact_pk PRIMARY KEY(contid),
  CONSTRAINT contact_fk1 FOREIGN KEY(custid) REFERENCES optim1.customer,
  CONSTRAINT contact_ck1 CHECK(contmode IN ('P', 'E'))
);

CREATE TABLE optim1.contact_phone (
  contid INTEGER NOT NULL,
  phoneval VARCHAR(50) NOT NULL,
  CONSTRAINT phone_pk PRIMARY KEY(contid),
  CONSTRAINT phone_fk1 FOREIGN KEY(contid) REFERENCES optim1.contact
);

CREATE TABLE optim1.contact_email (
  contid INTEGER NOT NULL,
  emailval VARCHAR(100) NOT NULL,
  CONSTRAINT email_pk PRIMARY KEY(contid),
  CONSTRAINT email_fk1 FOREIGN KEY(contid) REFERENCES optim1.contact
);

/*

CREATE VIEW optim1.onerow AS SELECT CHAR('A', 1) AS X;

db2 -td@

CREATE VIEW optim1.onerow AS SELECT CHAR('A', 1) AS X FROM (VALUES(1)) y @

CREATE OR REPLACE FUNCTION nextval(IN seq VARCHAR(100)) RETURNS BIGINT
  LANGUAGE SQL READS SQL DATA
BEGIN
  DECLARE retval BIGINT;
  DECLARE stmt1 STATEMENT;
  DECLARE cur1 CURSOR FOR stmt1;
  PREPARE stmt1 FROM ('values next value for ' || seq);
  OPEN cur1;
  FETCH FROM cur1 INTO retval;
  CLOSE cur1;
  RETURN retval;
END @

*/


/*

TRUNCATE TABLE optim1.contact_email;
TRUNCATE TABLE optim1.contact_phone;
TRUNCATE TABLE optim1.contact CASCADE;
TRUNCATE TABLE optim1.physical_entity;
TRUNCATE TABLE optim1.legal_entity;
TRUNCATE TABLE optim1.customer CASCADE;

*/
