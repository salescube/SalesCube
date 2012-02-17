SELECT
		ZIP_ID
        ,ZIP_CODE
        ,ZIP_ADDRESS_1
        ,ZIP_ADDRESS_2
        ,CRE_FUNC
        ,CRE_DATETM
        ,CRE_USER
        ,UPD_FUNC
        ,UPD_DATETM
        ,UPD_USER
    FROM
        ZIP_MST_/*$domainId*/
    /*BEGIN*/
	WHERE
		/*IF zipCode != null */
		ZIP_CODE LIKE /*zipCode*/'S%'
		/*END*/
		/*IF zipAddress1 != null */
		AND ZIP_ADDRESS_1 LIKE /*zipAddress1*/'%S%'
		/*END*/
		/*IF zipAddress2 != null */
		AND ZIP_ADDRESS_2 LIKE /*zipAddress2*/'%S%'
		/*END*/
	/*END*/
	/*BEGIN*/
		ORDER BY
			/*IF sortColumn != null */
				/*$sortColumn*/
				/*IF sortOrderAsc != null*/
				/*$sortOrderAsc*/
				-- ELSE ASC
				/*END*/
			/*END*/
	/*END*/