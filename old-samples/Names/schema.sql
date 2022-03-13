
CREATE SCHEMA convz;

CREATE TABLESPACE optim_fio;
CREATE TABLESPACE demo_fio;

/*
DROP TABLE convz.optim_names_m;
DROP TABLE convz.optim_names_f;
DROP TABLE convz.optim_fam_m;
DROP TABLE convz.optim_fam_f;
DROP TABLE convz.demo_first;
DROP TABLE convz.demo_middle;
DROP TABLE convz.demo_last;
*/

-- Имена мужские плюс производные отчества в мужском и женском роде
CREATE TABLE convz.optim_names_m (
  id INTEGER NOT NULL,
  name VARCHAR(50) NOT NULL,
  pm VARCHAR(50) NOT NULL,
  pf VARCHAR(50) NOT NULL,
  CONSTRAINT optim_names_m__pk PRIMARY KEY(id),
  CONSTRAINT optim_names_m__ak1 UNIQUE(name)
) IN optim_fio;

-- Имена женские
CREATE TABLE convz.optim_names_f (
  id INTEGER NOT NULL,
  name VARCHAR(50) NOT NULL,
  CONSTRAINT optim_names_f__pk PRIMARY KEY(id),
  CONSTRAINT optim_names_f__ak1 UNIQUE(name)
) IN optim_fio;

-- Фамилии в мужской форме
CREATE TABLE convz.optim_fam_m (
  id INTEGER NOT NULL,
  name VARCHAR(50) NOT NULL,
  CONSTRAINT optim_fam_m__pk PRIMARY KEY(id),
  CONSTRAINT optim_fam_m__ak1 UNIQUE(name)
) IN optim_fio;

-- Фамилии в женской форме
CREATE TABLE convz.optim_fam_f (
  id INTEGER NOT NULL,
  name VARCHAR(50) NOT NULL,
  CONSTRAINT optim_fam_f__pk PRIMARY KEY(id),
  CONSTRAINT optim_fam_f__ak1 UNIQUE(name)
) IN optim_fio;

-- Таблица подстановок для мужских ФИО
CREATE TABLE convz.optim_fio_m (
  id INTEGER NOT NULL,
  fio VARCHAR(150) NOT NULL,
  nf VARCHAR(50) NOT NULL,
  ns VARCHAR(50) NOT NULL,
  nl VARCHAR(50) NOT NULL,
  CONSTRAINT optim_fio_m__pk PRIMARY KEY(id)
) IN optim_fio;

-- Таблица подстановок для женских ФИО
CREATE TABLE convz.optim_fio_f (
  id INTEGER NOT NULL,
  fio VARCHAR(150) NOT NULL,
  nf VARCHAR(50) NOT NULL,
  ns VARCHAR(50) NOT NULL,
  nl VARCHAR(50) NOT NULL,
  CONSTRAINT optim_fio_f__pk PRIMARY KEY(id)
) IN optim_fio;



CREATE TABLE convz.demo_first (
  id INTEGER NOT NULL,
  val VARCHAR(50) NOT NULL,
  val_lat VARCHAR(80) NOT NULL,
  CONSTRAINT demo_first__pk PRIMARY KEY(id)
) IN demo_fio;

CREATE TABLE convz.demo_middle (
  id INTEGER NOT NULL,
  val VARCHAR(50) NOT NULL,
  val_lat VARCHAR(80) NOT NULL,
  CONSTRAINT demo_middle__pk PRIMARY KEY(id)
) IN demo_fio;

CREATE TABLE convz.demo_last (
  id INTEGER NOT NULL,
  val VARCHAR(50) NOT NULL,
  val_lat VARCHAR(80) NOT NULL,
  CONSTRAINT demo_last__pk PRIMARY KEY(id)
) IN demo_fio;


-- End Of File
