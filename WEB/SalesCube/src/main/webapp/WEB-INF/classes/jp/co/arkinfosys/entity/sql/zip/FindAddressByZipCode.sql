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
	WHERE
		REPLACE(ZIP_CODE,'-','') LIKE /*zipCode*/'S%'
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