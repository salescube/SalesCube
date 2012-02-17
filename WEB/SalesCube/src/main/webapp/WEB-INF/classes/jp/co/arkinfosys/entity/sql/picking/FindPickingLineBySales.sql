SELECT
	PL.PICKING_LINE_ID
	,PL.PICKING_LIST_ID
	,PL.SALES_LINE_ID
	,PL.RO_LINE_ID
	,PL.LINE_NO
	,PL.SALES_DETAIL_CATEGORY
	,PL.PRODUCT_CODE
	,PL.CUSTOMER_PCODE
	,PL.PRODUCT_ABSTRACT
	,PL.QUANTITY
	,PL.UNIT_PRICE
	,PL.UNIT_CATEGORY
	,PL.UNIT_NAME
	,PL.PACK_QUANTITY
	,PL.UNIT_RETAIL_PRICE
	,PL.RETAIL_PRICE
	,PL.UNIT_COST
	,PL.COST
	,PL.TAX_CATEGORY
	,PL.CTAX_RATE
	,PL.CTAX_PRICE
	,PL.GM
	,PL.RACK_CODE_SRC
	,PL.STOCK_NUM
	,PL.REMARKS
	,PL.CRE_FUNC
	,PL.CRE_DATETM
	,PL.CRE_USER
	,PL.UPD_FUNC
	,PL.UPD_DATETM
	,PL.UPD_USER
	,PL.PICKING_REMARKS
	,PL.SET_TYPE_CATEGORY
FROM
	PICKING_LIST_TRN_/*$domainId*/ PS
	LEFT OUTER JOIN PICKING_LINE_TRN_/*$domainId*/ PL
		ON PS.PICKING_LIST_ID = PL.PICKING_LIST_ID
    /*BEGIN*/
	WHERE
		/*IF pickingListId != null */
		PS.PICKING_LIST_ID = /*pickingListId*/0
		/*END*/
		/*IF salesSlipId != null */
		AND PS.SALES_SLIP_ID = /*salesSlipId*/0
		/*END*/
		/*IF setTypeCategory != null */
		AND PL.SET_TYPE_CATEGORY = /*setTypeCategory*/0
		/*END*/
	/*END*/
	/*BEGIN*/
	ORDER BY
		/*IF sortColumnLineNo != null */
		PL./*$sortColumnLineNo*/
		/*END*/

		/*IF sortOrder != null*/
		/*$sortOrder*/
		/*END*/
	/*END*/
	/*BEGIN*/
		LIMIT /*rowCount*/ OFFSET /*offsetRow*/
	/*END*/
	/*IF lockRecord != null */
	/*$lockRecord*/
	/*END*/
