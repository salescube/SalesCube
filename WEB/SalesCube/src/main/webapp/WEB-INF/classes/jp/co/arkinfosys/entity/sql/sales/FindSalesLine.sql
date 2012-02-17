SELECT
	SL.SALES_LINE_ID
	,SL.STATUS
	,SL.SALES_SLIP_ID
	,SL.LINE_NO
	,SL.RO_LINE_ID
	,SL.SALES_DETAIL_CATEGORY
	,SL.PRODUCT_CODE
	,SL.CUSTOMER_PCODE
	,SL.PRODUCT_ABSTRACT
	,SL.QUANTITY
	,SL.DELIVERY_PROCESS_CATEGORY
	,SL.UNIT_PRICE
	,SL.UNIT_CATEGORY
	,SL.UNIT_NAME
	,SL.PACK_QUANTITY
	,SL.UNIT_RETAIL_PRICE
	,SL.RETAIL_PRICE
	,SL.UNIT_COST
	,SL.COST
	,SL.TAX_CATEGORY
	,SL.CTAX_RATE
	,SL.CTAX_PRICE
	,SL.GM
	,SL.REMARKS
	,SL.EAD_REMARKS
	,SL.PRODUCT_REMARKS
	,SL.RACK_CODE_SRC
	,SL.CRE_FUNC
	,SL.CRE_DATETM
	,SL.CRE_USER
	,SL.UPD_FUNC
	,SL.UPD_DATETM
	,SL.UPD_USER
FROM
    SALES_LINE_TRN_/*$domainId*/ SL
/*BEGIN*/
WHERE
	/*IF salesLineId != null */
	SL.SALES_LINE_ID = /*salesLineId*/0
	/*END*/
	/*IF salesSlipId != null */
	AND SL.SALES_SLIP_ID = /*salesSlipId*/0
	/*END*/
/*END*/
/*BEGIN*/
ORDER BY
	/*IF sortColumnLineNo != null */
	SL./*$sortColumnLineNo*/
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
