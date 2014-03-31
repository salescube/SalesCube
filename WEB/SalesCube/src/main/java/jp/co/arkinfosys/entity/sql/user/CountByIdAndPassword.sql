	SELECT
		COUNT(*)
  	FROM
		(SELECT *
		FROM
			PASSWORD_MAKER_/*$domainId*/
		WHERE
			USER_ID=/*userId*/''
		ORDER BY PASS_UPD_DATETM DESC
		LIMIT /*passwordHistCount*/) LimitHistPassword
	WHERE
		LimitHistPassword.USER_ID=/*userId*/''
		AND LimitHistPassword.PASSWORD =/*password*/''

