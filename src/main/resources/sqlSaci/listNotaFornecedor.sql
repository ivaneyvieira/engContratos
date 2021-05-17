DO @VENDNO := :vendno;

SELECT N.storeno                                                                  AS loja,
       S.sname                                                                    AS sigla,
       N.invno                                                                    AS ni,
       CAST(CONCAT(N.nfname, IF(N.invse = '', '', CONCAT('/', N.invse))) AS CHAR) AS nota,
       ''                                                                         AS fatura,
       CAST(N.issue_date AS DATE)                                                 AS dataNota,
       N.vendno                                                                   AS vendno,
       SUM(N.grossamt / 100)                                                      AS valor,
       IFNULL(N.remarks, '')                                                      AS obsNota,
       remarks                                                                    AS remarks
FROM sqldados.inv           AS N
  INNER JOIN sqldados.store AS S
	       ON S.no = N.storeno
WHERE (N.bits & POW(2, 4) = 0)
  AND N.vendno = @VENDNO
GROUP BY invno