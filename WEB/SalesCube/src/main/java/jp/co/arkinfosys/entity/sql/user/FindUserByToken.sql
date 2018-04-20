SELECT
        U.USER_ID
        ,U.PASSWORD
        ,U.TOKEN_KEY
        ,U.TOKEN_IV
        ,U.TOKEN_EXPIRE_DATETM
    FROM
        USER_MST_/*$domainId*/ U
	WHERE
		TOKEN=/*token*/''
