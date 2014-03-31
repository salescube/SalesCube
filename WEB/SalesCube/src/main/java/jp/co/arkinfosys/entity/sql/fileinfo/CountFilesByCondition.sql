SELECT
	COUNT(FILE_ID)
    FROM
        FILE_INFO_/*$domainId*/ F LEFT OUTER JOIN USER_MST_/*$domainId*/ U
        ON F.CRE_USER=U.USER_ID
   	/*BEGIN*/
	WHERE
   	/*IF openLevel != null */
    	F.OPEN_LEVEL = /*openLevel*/'0'
    	/*END*/
    	/*IF title != null */
		AND F.TITLE LIKE /*title*/'%S%'
		/*END*/

		/*IF fileName != null */
		AND F.FILE_NAME LIKE /*fileName*/'%S%'
		/*END*/
    /*END*/