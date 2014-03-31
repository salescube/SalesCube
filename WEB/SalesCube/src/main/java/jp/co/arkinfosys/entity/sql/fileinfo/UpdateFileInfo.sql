UPDATE
        FILE_INFO_/*$domainId*/
    SET
         TITLE = /*title*/NULL
        ,OPEN_LEVEL = /*openLevel*/NULL
        ,UPD_FUNC = /*updFunc*/NULL
        ,UPD_DATETM = now()
        ,UPD_USER = /*updUser*/NULL
    WHERE
        FILE_ID = /*fileId*/