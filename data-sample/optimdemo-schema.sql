CREATE SCHEMA optim1;

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
  pe_num_pay VARCHAR(50) NULL,
  pe_num_soc VARCHAR(50) NULL,
  CONSTRAINT pe_pk PRIMARY KEY(custid),
  CONSTRAINT pe_fk1 FOREIGN KEY(custid) REFERENCES optim1.customer
);
