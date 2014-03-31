UPDATE MINE_MST_/*$domainId*/ SET
        PASSWORD_VALID_DAYS=/*passwordValidDays*/NULL
        ,TOTAL_FAIL_COUNT=/*totalFailCount*/NULL
        ,PASSWORD_HIST_COUNT =/*passwordHistCount*/NULL
        ,PASSWORD_LENGTH  =/*passwordLength*/NULL
        ,PASSWORD_CHAR_TYPE =/*passwordCharType*/NULL
        ,UPD_FUNC=/*updFunc*/NULL
        ,UPD_DATETM=now()
        ,UPD_USER=/*updUser*/NULL

