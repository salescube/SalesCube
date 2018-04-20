UPDATE USER_MST_/*$domainId*/ SET
		TOKEN=/*token*/,
		/*IF passwordValidDays != null */
		TOKEN_EXPIRE_DATETM=DATE_ADD(now(), INTERVAL /*passwordValidDays*/ DAY),
		/*END*/
		/*IF passwordValidDays == null */
		TOKEN_EXPIRE_DATETM=null,
		/*END*/
		TOKEN_KEY=/*tokenKey*/,
		TOKEN_IV=/*tokenIv*/,
        UPD_FUNC=/*updFunc*/NULL,
        UPD_DATETM=now(),
        UPD_USER=/*updUser*/NULL
	WHERE
		USER_ID=/*userId*/''
