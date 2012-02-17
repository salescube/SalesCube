SELECT
		R.WAREHOUSE_CODE
		,W.WAREHOUSE_NAME
		,W.WAREHOUSE_STATE
		,R.RACK_CODE
		,R.RACK_NAME
		,R.RACK_CATEGORY
		,C.CATEGORY_CODE_NAME AS RACK_CATEGORY_NAME
		,R.MULTI_FLAG
		,R.ZIP_CODE
		,R.ADDRESS_1
		,R.ADDRESS_2
		,R.RACK_PC_NAME
		,R.RACK_TEL
		,R.RACK_FAX
		,R.RACK_EMAIL
		,R.CRE_FUNC
		,R.CRE_DATETM
		,R.CRE_USER
		,R.UPD_FUNC
		,R.UPD_DATETM
		,R.UPD_USER
    FROM
        RACK_MST_/*$domainId*/ R LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ C
        ON R.RACK_CATEGORY = C.CATEGORY_CODE AND C.CATEGORY_ID = /*categoryId*/0
        LEFT OUTER JOIN WAREHOUSE_MST_/*$domainId*/ W
        ON R.WAREHOUSE_CODE = W.WAREHOUSE_CODE
    /*BEGIN*/
    WHERE
    	/*IF warehouseCode != null*/
    	R.WAREHOUSE_CODE LIKE /*warehouseCode*/'S%'
    	/*END*/
    	/*IF warehouseName != null*/
    	AND W.WAREHOUSE_NAME LIKE /*warehouseName*/'S%'
    	/*END*/
    	/*IF warehouseState != null*/
    	AND W.WAREHOUSE_STATE = /*warehouseState*/'S'
    	/*END*/
    	/*IF rackCode != null*/
    	AND RACK_CODE LIKE /*rackCode*/'S%'
    	/*END*/
    	/*IF rackName != null*/
    	AND RACK_NAME LIKE /*rackName*/'%S%'
    	/*END*/
    	/*IF rackCategory != null*/
    	AND RACK_CATEGORY = /*rackCategory*/'0'
    	/*END*/
    	/*IF emptyRack == true */
    	AND NOT EXISTS( SELECT 1 FROM PRODUCT_MST_/*$domainId*/ P WHERE P.RACK_CODE=R.RACK_CODE )
    	AND R.MULTI_FLAG='0'
    	/*END*/
    /*END*/
   	/*BEGIN*/
    ORDER BY
		/*IF sortColumnRack != null */
    	/*$sortColumnRack*/
		/*END*/

		/*IF sortOrder != null*/
		/*$sortOrder*/
		/*END*/
   	/*END*/