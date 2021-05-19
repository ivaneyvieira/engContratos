DO @LOC := :localizador;
DO @LOCLIKE := CONCAT('%', @LOC, '%');

SELECT no                            AS vendno,
       name                          AS nome,
       sname                         AS abreviacao,
       email                         AS email,
       auxChar1                      AS nomeFantasia,
       IFNULL(RV.rmk, '')            AS rmkVend,
       MAX(CAST(issue_date AS DATE)) AS ultimaData
FROM sqldados.vend             AS V
  INNER JOIN sqldados.inv       AS I
	      ON I.vendno = V.no
  LEFT JOIN sqldados.nfvendRmk AS RV
	      ON RV.vendno = V.no AND RV.tipo = '99'
WHERE terms LIKE 'CONTRATO%'
  AND (no = @LOC * 1 OR name LIKE @LOCLIKE OR sname LIKE @LOCLIKE OR auxChar1 LIKE @LOCLIKE OR
       TRIM(@LOC) = '')
  AND (I.bits & POW(2, 4) = 0)
GROUP BY no

