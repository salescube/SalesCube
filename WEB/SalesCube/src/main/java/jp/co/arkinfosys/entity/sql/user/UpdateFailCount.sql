UPDATE USER_MST_/*$domainId*/ SET
		FAIL_COUNT=/*failCount*/,
		LOCK_FLG=/*lockflg*/,
		/*IF lockflg == 1 */
           LOCK_DATETM=DATE_FORMAT(now(),'%Y/%m/%d %k:%i'),
        /*END*/
        UPD_FUNC=/*updFunc*/NULL,
        UPD_DATETM=now(),
        UPD_USER=/*updUser*/NULL
	WHERE
		USER_ID=/*userId*/''
