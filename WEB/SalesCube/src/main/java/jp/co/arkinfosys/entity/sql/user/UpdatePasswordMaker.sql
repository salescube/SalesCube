UPDATE PASSWORD_MAKER_/*$domainId*/ SET
		/*IF password != null */
		PASSWORD=/*password*/NULL,
		PASS_UPD_DATETM=now(),
		/*END*/

        UPD_FUNC=/*updFunc*/NULL,
        UPD_DATETM=now(),
        UPD_USER=/*updUser*/NULL
WHERE
	PASS_UPD_DATETM =
		(SELECT
			OLDDATE
		FROM
			((SELECT MIN(PASS_UPD_DATETM) AS OLDDATE
			FROM
				PASSWORD_MAKER_/*$domainId*/
			WHERE
				USER_ID=/*userId*/
			GROUP BY USER_ID)
			temp1)
		)
