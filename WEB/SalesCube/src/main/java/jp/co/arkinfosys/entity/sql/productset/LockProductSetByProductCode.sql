SELECT
        PRODUCT_CODE
        ,UPD_DATETM
        ,UPD_USER
    FROM
    	PRODUCT_SET_MST_/*$domainId*/
    WHERE
    	SET_PRODUCT_CODE = /*setProductCode*/'S'
        AND PRODUCT_CODE = /*productCode*/'S'
	FOR UPDATE