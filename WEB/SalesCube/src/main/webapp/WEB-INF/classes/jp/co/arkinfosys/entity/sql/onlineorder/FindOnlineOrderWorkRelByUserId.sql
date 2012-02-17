SELECT
	WK.ONLINE_ORDER_ID,
	WK.SUPPLIER_DATE,
	WK.CUSTOMER_NAME,
	REL.RO_SLIP_ID,
	MIN(WK.LOAD_DATE) LOAD_DATE
FROM
	ONLINE_ORDER_WORK_/*$domainId*/ WK
	LEFT OUTER JOIN ONLINE_ORDER_REL_/*$domainId*/ REL
		ON WK.ONLINE_ORDER_ID = REL.ONLINE_ORDER_ID
/*BEGIN*/
WHERE
	/*IF userId != null */
	WK.USER_ID = /*userId*/'S'
	/*END*/
/*END*/
GROUP BY
	WK.ONLINE_ORDER_ID,
	WK.SUPPLIER_DATE,
	WK.CUSTOMER_NAME,
	REL.RO_SLIP_ID
/*BEGIN*/
ORDER BY
	/*IF sortColumn != null */
	/*$sortColumn*/
	/*END*/
	/*IF sortColumn != null && sortOrder != null*/
	/*$sortOrder*/
	/*END*/
	/*IF sortColumn == 'LOAD_DATE' */
	,LINE_NO ASC,SUPPLIER_DATE ASC	-- 昔のデータは値が入っていないので、入っている項目を指定して順序を確定させる
	/*END*/
/*END*/
/*IF limitRow != null && offsetRow != null */
LIMIT /*limitRow*/10 OFFSET /*offsetRow*/0
/*END*/
