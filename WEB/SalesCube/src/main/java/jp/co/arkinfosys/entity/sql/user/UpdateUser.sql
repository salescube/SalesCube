UPDATE USER_MST_/*$domainId*/ SET
		/*IF nameKnj != null */
		NAME_KNJ=/*nameKnj*/'',
		/*END*/

		/*IF nameKana != null */
		NAME_KANA=/*nameKana*/NULL,
		/*END*/

		/*IF deptId != null */
		DEPT_ID=/*deptId*/NULL,
		/*END*/

		/*IF email != null */
		EMAIL=/*email*/NULL,
		/*END*/

		/*IF password != null */
		PASSWORD=/*password*/NULL,

		/*END*/

		/*IF passwordValidDays != null */
		EXPIRE_DATE=DATE_ADD(now(), INTERVAL /*passwordValidDays*/ DAY),
		/*END*/
		/*IF passwordValidDays == null */
		EXPIRE_DATE=NULL,
		/*END*/
		/*IF failCount != null */
		FAIL_COUNT=/*failCount*/,
		/*END*/

		/*IF lockflg != null */
		LOCK_FLG=/*lockflg*/,
			/*IF lockflg == 1 */
            LOCK_DATETM=DATE_FORMAT(now(),'%Y/%m/%d %k:%i'),
        	/*END*/
            /*IF lockflg != 1 */
            	LOCK_DATETM=NULL,
        	/*END*/
		/*END*/

        UPD_FUNC=/*updFunc*/NULL,
        UPD_DATETM=now(),
        UPD_USER=/*updUser*/NULL
	WHERE
		USER_ID=/*userId*/''
