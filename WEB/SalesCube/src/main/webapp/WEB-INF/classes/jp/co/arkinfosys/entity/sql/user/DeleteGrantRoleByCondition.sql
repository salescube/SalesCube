DELETE
    FROM
        GRANT_ROLE_/*$domainId*/
    WHERE
        USER_ID = /*userId*/''
        /*IF menuId != null && validFlag != null*/
        AND ROLE_ID = (
                SELECT
                        R.ROLE_ID
                    FROM
                        ROLE_MST_/*$domainId*/ R INNER JOIN ROLE_CFG_/*$domainId*/ C
                            ON R.ROLE_ID = C.ROLE_ID
                WHERE
                    C.MENU_ID =/*menuId*/''
                    AND C.VALID_FLAG =/*validFlag*/''
        )
        /*END*/