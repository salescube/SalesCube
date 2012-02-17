SELECT
        COUNT(U.USER_ID) AS COUNT
    FROM
        USER_MST_/*$domainId*/ U
            LEFT OUTER JOIN DEPT_MST_/*$domainId*/ D
                ON U.DEPT_ID = D.DEPT_ID
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