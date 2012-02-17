SELECT
	COUNT(DEPT_ID)
    FROM
        DEPT_MST_/*$domainId*/
   	/*BEGIN*/
	WHERE
		/*IF deptId != null */
		DEPT_ID LIKE /*deptId*/'S%'
		/*END*/

		/*IF parentId != null */
		AND PARENT_ID = /*parentId*/'S'
		/*END*/

		/*IF name != null */
		AND NAME LIKE /*name*/'%S%'
		/*END*/
	/*END*/