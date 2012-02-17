SELECT
		USER_ID
		,DETAIL_ID
		,TARGET
		,ITEM_ID
		,SEQ
		,CRE_FUNC
		,CRE_DATETM
		,CRE_USER
		,UPD_FUNC
		,UPD_DATETM
		,UPD_USER
    FROM
        DETAIL_DISP_ITEM_CFG_/*$domainId*/
	WHERE
		USER_ID = /*userId*/
		AND DETAIL_ID = /*detailId*/
		AND TARGET = /*target*/
		AND ITEM_ID = /*itemId*/
	FOR UPDATE

