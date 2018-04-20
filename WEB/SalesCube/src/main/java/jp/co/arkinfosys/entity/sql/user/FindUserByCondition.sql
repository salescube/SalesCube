SELECT DISTINCT
        U.USER_ID
        ,U.LOCK_FLG
        ,DATE_FORMAT(U.LOCK_DATETM,'%Y/%m/%d %k:%i') LOCK_DATETM
        ,U.NAME_KNJ
        ,U.NAME_KANA
        ,U.DEPT_ID
        ,U.EMAIL
        ,U.PASSWORD
        ,U.EXPIRE_DATE
        ,U.CRE_FUNC
        ,U.CRE_DATETM
        ,U.CRE_USER
        ,U.UPD_FUNC
        ,U.UPD_DATETM
        ,U.UPD_USER
        ,D.NAME AS DEPT_NAME
    FROM
        USER_MST_/*$domainId*/ U
            LEFT OUTER JOIN DEPT_MST_/*$domainId*/ D
                ON U.DEPT_ID = D.DEPT_ID
            LEFT OUTER JOIN GRANT_ROLE_/*$domainId*/ GR
                ON U.USER_ID = GR.USER_ID INNER JOIN ROLE_MST_/*$domainId*/ R
                ON GR.ROLE_ID = R.ROLE_ID
    /*BEGIN*/
    WHERE
    	/*IF userId != null */
        U.USER_ID LIKE /*userId*/'S%'
        /*END*/
        /*IF nameKnj != null */
        AND U.NAME_KNJ LIKE /*nameKnj*/'%S%'
        /*END*/
        /*IF nameKana != null */
        AND U.NAME_KANA LIKE /*nameKana*/'%S%'
        /*END*/
        /*IF deptId != null */
        AND U.DEPT_ID = /*deptId*/1
        /*END*/
        /*IF roleId != null */
        AND GR.ROLE_ID = /*roleId*/'000'
        /*END*/
        /*IF email != null */
        AND U.EMAIL LIKE /*email*/'%S%'
        /*END*/
	/*END*/
	/*BEGIN*/
	ORDER BY
		/*IF sortColumnUser != null */
		U./*$sortColumnUser*/
		/*END*/

		/*IF sortColumnDept != null */
		D./*$sortColumnDept*/
		/*END*/

		/*IF sortOrder != null*/
		/*$sortOrder*/
		/*END*/
	/*END*/