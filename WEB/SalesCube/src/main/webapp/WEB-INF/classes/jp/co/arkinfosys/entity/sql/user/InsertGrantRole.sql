INSERT
    INTO
        GRANT_ROLE_/*$domainId*/ (
            USER_ID
            ,ROLE_ID
            ,CRE_FUNC
            ,CRE_DATETM
            ,CRE_USER
            ,UPD_FUNC
            ,UPD_DATETM
            ,UPD_USER
        )
        (SELECT
        	/*userId*/''
        	,R.ROLE_ID
            ,/*creFunc*/NULL
            ,now()
            ,/*creUser*/NULL
            ,/*updFunc*/NULL
            ,now()
            ,/*updUser*/NULL
        FROM
        	ROLE_MST_/*$domainId*/ R INNER JOIN ROLE_CFG_/*$domainId*/ C
        	ON R.ROLE_ID=C.ROLE_ID
        WHERE
        	C.MENU_ID=/*menuId*/''
        	AND C.VALID_FLAG=/*validFlag*/''
        )