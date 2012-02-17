SELECT
		DETAIL_ID
		,TARGET
		,ITEM_ID
		,ITEM_NAME
		,SEQ
		,ESSL_FLAG
		,DISP_FLAG
		,SORT_FLAG
		,DETAIL_FLAG
		,COL_WIDTH
		,TEXT_ALIGN
		,FORMAT_TYPE
		,CRE_FUNC
		,CRE_DATETM
		,CRE_USER
		,UPD_FUNC
		,UPD_DATETM
		,UPD_USER
    FROM
        DETAIL_DISP_ITEM_MST_/*$domainId*/
    /*BEGIN*/
	WHERE
		/*IF detailId != null */
		DETAIL_ID = /*detailId*/''
		/*END*/

		/*IF target != null */
		AND TARGET = /*target*/''
		/*END*/

		/*IF dispFlag != null */
		AND DISP_FLAG = /*dispFlag*/''
		/*END*/
	/*END*/

		ORDER BY
		SEQ

