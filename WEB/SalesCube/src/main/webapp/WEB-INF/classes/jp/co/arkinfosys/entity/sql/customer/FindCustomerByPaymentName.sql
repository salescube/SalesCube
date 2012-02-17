SELECT
        CUSTOMER_CODE
        ,CUSTOMER_NAME
        ,SALES_CM_CATEGORY
    FROM
        CUSTOMER_MST_/*$domainId*/
	WHERE
		PAYMENT_NAME=/*paymentName*/
