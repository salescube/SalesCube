SELECT
		PRODUCT_CODE
		,CASE WHEN PRODUCT_STATUS_CATEGORY = /*productStatusSaleCancel*/ THEN '1' ELSE '0' END AS DISCARDED
	FROM
		PRODUCT_MST_/*$domainId*/
	WHERE
		/*IF productCode != null*/
		PRODUCT_CODE IN /*$productCode*/
		/*END*/
		/*IF productCodeList != null */
		PRODUCT_CODE IN /*productCodeList*/('1','2')
		/*END*/