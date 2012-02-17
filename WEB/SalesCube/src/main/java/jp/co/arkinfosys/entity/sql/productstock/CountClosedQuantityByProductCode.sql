SELECT
        PS.PRODUCT_CODE
        ,SUM(PS.STOCK_NUM) AS QUANTITY
    FROM
        PRODUCT_STOCK_TRN_/*$domainId*/ PS
        INNER JOIN (
                SELECT
                	PS2.PRODUCT_CODE
                	,MAX(PS2.STOCK_PDATE) AS LAST_PDATE
            	FROM
                	PRODUCT_STOCK_TRN_/*$domainId*/ PS2 INNER JOIN PRODUCT_MST_/*$domainId*/ P ON PS2.PRODUCT_CODE = P.PRODUCT_CODE
        		WHERE
            		PS2.STOCK_PDATE IS NOT NULL
					/*IF productCode != null */
					AND PS2.PRODUCT_CODE IN /*productCode*/('', '')
					/*END*/
        		GROUP BY
            		PS2.PRODUCT_CODE
        ) V ON PS.STOCK_PDATE = V.LAST_PDATE AND PS.PRODUCT_CODE = V.PRODUCT_CODE
        LEFT OUTER JOIN RACK_MST_/*$domainId*/ R ON PS.RACK_CODE = R.RACK_CODE /*IF rackCategory != null */AND R.RACK_CATEGORY = /*rackCategory*/''/*END*/
   	/*BEGIN*/
	WHERE
		/*IF rackCode != null */
		PS.RACK_CODE = /*rackCode*/''
		/*END*/
		/*IF productCode != null */
		AND PS.PRODUCT_CODE IN /*productCode*/('', '')
		/*END*/
	/*END*/
    GROUP BY
        PS.PRODUCT_CODE
    ORDER BY
        PS.PRODUCT_CODE