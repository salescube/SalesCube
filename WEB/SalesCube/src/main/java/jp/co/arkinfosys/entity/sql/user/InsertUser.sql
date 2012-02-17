INSERT
    INTO
        USER_MST_/*$domainId*/ (
            USER_ID
            ,NAME_KNJ
            ,NAME_KANA
            ,DEPT_ID
            ,EMAIL
            ,PASSWORD
            ,EXPIRE_DATE
            ,CRE_FUNC
            ,CRE_DATETM
            ,CRE_USER
            ,UPD_FUNC
            ,UPD_DATETM
            ,UPD_USER
        )
    VALUES
        (
            /*userId*/''
            ,/*nameKnj*/''
            ,/*nameKana*/''
            ,/*deptId*/''
            ,/*email*/''
            ,/*password*/''
            ,/*IF expireDate != null */
            	/*expireDate*/
            /*END*/
            /*IF expireDate == null */
            	NULL
            /*END*/
            ,/*creFunc*/NULL
            ,now()
            ,/*creUser*/NULL
            ,/*updFunc*/NULL
            ,now()
            ,/*updUser*/NULL
        )