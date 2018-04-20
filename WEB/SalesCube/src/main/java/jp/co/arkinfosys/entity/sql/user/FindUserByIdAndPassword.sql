SELECT
        U.USER_ID
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
        ,U.FAIL_COUNT
        ,U.LOCK_FLG
        ,DATE_FORMAT(U.LOCK_DATETM,'%Y/%m/%d %k:%i') LOCK_DATETM
        ,D.NAME
    FROM
        USER_MST_/*$domainId*/ U
            LEFT OUTER JOIN DEPT_MST_/*$domainId*/ D
                ON U.DEPT_ID = D.DEPT_ID
	WHERE
		USER_ID=/*userId*/'' AND
		PASSWORD=/*password*/''
