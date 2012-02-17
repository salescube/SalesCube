SELECT COUNT(*)
    FROM
        SUPPLIER_MST_/*$domainId*/'DEFAULT' S
        LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ C
            	ON S.SUPPLIER_CM_CATEGORY = C.CATEGORY_CODE AND C.CATEGORY_ID=/*categoryId*/13
    /*BEGIN*/
	WHERE
		/*IF supplierCode != null */
		S.SUPPLIER_CODE LIKE /*supplierCode*/'S%'
		/*END*/
		/*IF supplierName != null */
		AND S.SUPPLIER_NAME LIKE /*supplierName*/'%S%'
		/*END*/
		/*IF supplierKana != null */
		AND S.SUPPLIER_KANA LIKE /*supplierKana*/'%S%'
		/*END*/
		/*IF remarks != null */
		AND S.REMARKS LIKE /*remarks*/'%S%'
		/*END*/
	/*END*/
