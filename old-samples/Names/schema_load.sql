
LOAD FROM names_m.txt OF DEL MODIFIED BY COLDELX09 codepage=1208 REPLACE INTO convz.optim_names_m;
LOAD FROM names_f.txt OF DEL MODIFIED BY COLDELX09 codepage=1208 REPLACE INTO convz.optim_names_f;
LOAD FROM fam_m.txt OF DEL MODIFIED BY COLDELX09 codepage=1208 REPLACE INTO convz.optim_fam_m;
LOAD FROM fam_f.txt OF DEL MODIFIED BY COLDELX09 codepage=1208 REPLACE INTO convz.optim_fam_f;

SELECT c1, c2, c3, c1*c2*c3 FROM
(SELECT COUNT(*) AS c1 FROM convz.optim_fam_f) nf,
(SELECT COUNT(*) AS c2 FROM convz.optim_names_f) nl,
(SELECT COUNT(*) AS c3 FROM convz.optim_names_m) ns;


DECLARE x1 CURSOR FOR SELECT ROW_NUMBER() OVER() AS rn, 
       nl.name||' '||nf.name||' '||ns.pf AS fio,
	   nf.name, ns.pf, nl.name
FROM convz.optim_fam_f nl, convz.optim_names_f nf, convz.optim_names_m ns
WHERE RAND()<0.01;

LOAD FROM x1 OF CURSOR REPLACE INTO convz.optim_fio_f;

DECLARE x1 CURSOR FOR SELECT ROW_NUMBER() OVER() AS rn, 
       nl.name||' '||nf.name||' '||ns.pm AS fio,
	   nf.name, ns.pm, nl.name
FROM convz.optim_fam_f nl, convz.optim_names_m nf, convz.optim_names_m ns
WHERE RAND()<0.01;

LOAD FROM x1 OF CURSOR REPLACE INTO convz.optim_fio_m;

SELECT MIN(id), MAX(id), COUNT(*) FROM convz.optim_fio_f;
SELECT MIN(id), MAX(id), COUNT(*) FROM convz.optim_fio_m

DELETE FROM convz.demo_last;
INSERT INTO convz.demo_last VALUES(0, '', '');
INSERT INTO convz.demo_last VALUES(-1, '', '');
INSERT INTO convz.demo_last VALUES(-2, ' ', ' ');
INSERT INTO convz.demo_last 
  SELECT ROW_NUMBER() OVER() AS rn, name, convz.translit(name) FROM 
  (SELECT * FROM convz.optim_fam_m ORDER BY RAND());

DELETE FROM convz.demo_first;
INSERT INTO convz.demo_first VALUES(0, '', '');
INSERT INTO convz.demo_first VALUES(-1, '', '');
INSERT INTO convz.demo_first VALUES(-2, ' ', ' ');
INSERT INTO convz.demo_first 
  SELECT ROW_NUMBER() OVER() AS rn, name, convz.translit(name) FROM 
  (SELECT * FROM convz.optim_names_m ORDER BY RAND());

DELETE FROM convz.demo_middle;
INSERT INTO convz.demo_middle VALUES(0, '', '');
INSERT INTO convz.demo_middle VALUES(-1, '', '');
INSERT INTO convz.demo_middle VALUES(-2, ' ', ' ');
INSERT INTO convz.demo_middle 
  SELECT ROW_NUMBER() OVER() AS rn, pm, convz.translit(pm) FROM 
  (SELECT * FROM convz.optim_names_m ORDER BY RAND());


-- End Of File
