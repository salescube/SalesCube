SELECT
  WAREHOUSE_CODE
  ,WAREHOUSE_NAME
  ,WAREHOUSE_ZIP_CODE
  ,WAREHOUSE_ADDRESS_1
  ,WAREHOUSE_ADDRESS_2
  ,WAREHOUSE_TEL
  ,WAREHOUSE_FAX
  ,MANAGER_NAME
  ,MANAGER_KANA
  ,MANAGER_TEL
  ,MANAGER_FAX
  ,MANAGER_EMAIL
  ,WAREHOUSE_STATE
  ,CRE_FUNC
  ,CRE_DATETM
  ,CRE_USER
  ,UPD_FUNC
  ,UPD_DATETM
  ,UPD_USER
  ,DEL_FUNC
  ,DEL_DATETM
  ,DEL_USER
	FROM
		WAREHOUSE_MST_/*$domainId*/
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
   	/*BEGIN*/
    ORDER BY
		/*IF sortColumnWarehouse != null */
    	/*$sortColumnWarehouse*/
		/*END*/

		/*IF sortOrder != null*/
		/*$sortOrder*/
		/*END*/
   	/*END*/
	/*BEGIN*/
	/*IF rowCount != null*/
	LIMIT /*rowCount*/
	/*IF offsetRow != null*/
	OFFSET /*offsetRow*/
	/*END*/
	/*END*/
	/*END*/
