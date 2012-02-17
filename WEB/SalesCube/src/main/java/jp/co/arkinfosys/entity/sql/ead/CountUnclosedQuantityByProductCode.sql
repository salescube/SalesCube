SELECT
        A.PRODUCT_CODE
        ,SUM(A.QUANTITY) AS QUANTITY
    FROM
        (
            SELECT
                    EL.PRODUCT_CODE
                    ,CASE
                        WHEN ES.EAD_CATEGORY = /*eCategory*/ THEN EL.QUANTITY -- 入出庫区分「入庫」
                        WHEN ES.EAD_CATEGORY = /*dCategory*/ THEN (-1) * EL.QUANTITY -- 入出庫区分「出庫」
                    END AS QUANTITY
                FROM
                    EAD_LINE_TRN_/*$domainId*/ EL INNER JOIN EAD_SLIP_TRN_/*$domainId*/ ES
                        ON EL.EAD_SLIP_ID = ES.EAD_SLIP_ID INNER JOIN RACK_MST_/*$domainId*/ R
                        ON EL.RACK_CODE = R.RACK_CODE
                        INNER JOIN PRODUCT_MST_/*$domainId*/ P ON EL.PRODUCT_CODE=P.PRODUCT_CODE
            WHERE
                ES.STOCK_PDATE IS NULL
                AND ES.EAD_DATE <= CURDATE()
				/*IF rackCode != null */
				AND EL.RACK_CODE = /*rackCode*/''
				/*END*/
				/*IF rackCategory != null */
				AND R.RACK_CATEGORY = /*rackCategory*/''
				/*END*/
				/*IF productCode != null */
				AND EL.PRODUCT_CODE IN /*productCode*/('', '')
				/*END*/
        ) A
    GROUP BY
        A.PRODUCT_CODE
	ORDER BY
        A.PRODUCT_CODE
