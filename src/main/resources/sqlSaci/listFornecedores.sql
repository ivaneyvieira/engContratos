DO @LOC := :localizador;
DO @LOCLIKE := CONCAT('%', @LOC, '%');

SELECT no                            AS vendno,
       name                          AS nome,
       sname                         AS abreviacao,
       vendcust                      AS custno,
       auxChar1                      AS nomeFantasia,
       MAX(CAST(issue_date AS DATE)) AS ultimaData
FROM sqldados.vend
  LEFT JOIN sqldados.inv
	      ON inv.vendno = vend.no AND (inv.bits & POW(2, 4) = 0)
WHERE terms LIKE 'CONTRATO%'
  AND (no = @LOC * 1 OR name LIKE @LOCLIKE OR sname LIKE @LOCLIKE OR auxChar1 LIKE @LOCLIKE OR
       TRIM(@LOC) = '')
GROUP BY no

