SELECT COUNT(*)
    FROM
        WAREHOUSE_MST_/*$domainId*/ R
    /*BEGIN*/
    WHERE
    	/*IF warehouseCode != null*/
    	WAREHOUSE_CODE LIKE /*warehouseCode*/'S%'
    	/*END*/
    	/*IF warehouseName != null*/
    	AND WAREHOUSE_NAME LIKE /*warehouseName*/'%S%'
    	/*END*/
    	/*IF warehouseState != null*/
    	AND WAREHOUSE_STATE = /*warehouseState*/'S'
    	/*END*/
    /*END*/
