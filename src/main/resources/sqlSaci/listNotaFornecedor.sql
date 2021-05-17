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
       remarks                                                                    AS remarks,
       IFNULL(R.rmk, '')                                                          AS rmk
FROM sqldados.inv              AS N
  LEFT JOIN  sqldados.nfdevRmk AS R
	       ON N.storeno = R.storeno AND R.pdvno = 9999 AND N.invno = R.xano
  INNER JOIN sqldados.store    AS S
	       ON S.no = N.storeno
WHERE (N.bits & POW(2, 4) = 0)
  AND N.vendno = @VENDNO
GROUP BY invno