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
            ,FAIL_COUNT
            ,LOCK_FLG
            ,LOCK_DATETM
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

			/*IF passwordValidDays != null */
			,DATE_ADD(now(), INTERVAL /*passwordValidDays*/ DAY)
			/*END*/

            /*IF passwordValidDays == null */
            	,NULL
            /*END*/

			,'0'
            ,/*lockflg*/''
			/*IF lockflg == 1 */
            	,DATE_FORMAT(now(),'%Y/%m/%d %k:%i')
            /*END*/
			/*IF lockflg != 1 */
            	,NULL
            /*END*/

            ,/*creFunc*/NULL
            ,now()
            ,/*creUser*/NULL
            ,/*updFunc*/NULL
            ,now()
            ,/*updUser*/NULL
        )