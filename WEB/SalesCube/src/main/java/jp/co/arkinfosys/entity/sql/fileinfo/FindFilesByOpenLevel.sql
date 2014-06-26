SELECT
        F.FILE_ID
        ,F.TITLE
        ,F.FILE_NAME
        ,F.REAL_FILE_NAME
        ,F.FILE_SIZE
        ,F.OPEN_LEVEL
        ,F.CRE_FUNC
        ,F.CRE_DATETM
        ,F.CRE_USER
        ,U.NAME_KNJ AS CRE_USER_NAME
        ,F.UPD_FUNC
        ,F.UPD_DATETM
        ,F.UPD_USER
    FROM
        FILE_INFO_/*$domainId*/ F LEFT OUTER JOIN USER_MST_/*$domainId*/ U
        ON F.CRE_USER=U.USER_ID
    /*BEGIN*/
    WHERE
    	/*IF openLevel != null */
    	F.OPEN_LEVEL <= /*openLevel*/'0'
    	/*END*/
    /*END*/
	/*BEGIN*/
	ORDER BY
		/*IF sortColumn != null */
		F./*$sortColumn*/
		/*END*/

		/*IF sortOrder != null*/
		/*$sortOrder*/
		/*END*/
	/*END*/