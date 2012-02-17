SELECT
		CFG.USER_ID
		,CFG.DETAIL_ID
		,CFG.TARGET
		,CFG.ITEM_ID
		,CFG.SEQ
		,MST.ITEM_NAME
		,MST.SORT_FLAG
		,MST.DETAIL_FLAG
		,MST.COL_WIDTH
		,MST.TEXT_ALIGN
		,MST.FORMAT_TYPE
		,CFG.CRE_FUNC
		,CFG.CRE_DATETM
		,CFG.CRE_USER
		,CFG.UPD_FUNC
		,CFG.UPD_DATETM
		,CFG.UPD_USER
    FROM
        DETAIL_DISP_ITEM_CFG_/*$domainId*/ CFG,
        DETAIL_DISP_ITEM_MST_/*$domainId*/ MST
    /*BEGIN*/
	WHERE
		/*IF detailId != null */
		CFG.DETAIL_ID = /*detailId*/''
		/*END*/

		/*IF userId != null */
		AND CFG.USER_ID = /*userId*/''
		/*END*/

		/*IF target != null */
		AND CFG.TARGET = /*target*/''
		/*END*/

		AND CFG.DETAIL_ID = MST.DETAIL_ID
		AND CFG.ITEM_ID = MST.ITEM_ID
		AND CFG.TARGET = MST.TARGET


	/*END*/

		ORDER BY
		CFG.SEQ

