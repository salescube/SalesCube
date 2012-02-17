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
	SALES_SLIP_TRN_/*$domainId*/ S
		INNER JOIN SALES_LINE_TRN_/*$domainId*/ SL ON S.SALES_SLIP_ID = SL.SALES_SLIP_ID
WHERE
	SL.STATUS = /*status*/'0'
	AND S.STATUS = /*slipStatus*/'0'
	/*IF customerCode != null */
	AND S.CUSTOMER_CODE = /*customerCode*/'C%'
	/*END*/
	/*IF salesDate != null */
	AND S.SALES_DATE <= /*salesDate*/'2009/12/01'
	/*END*/
	/*IF salesCmCategory == "0" */	-- 売掛の場合「掛売：0」「現金：1」が対象
	AND ( S.SALES_CM_CATEGORY = '0' OR S.SALES_CM_CATEGORY = '1' )
	/*END*/
	/*IF salesCmCategory == "3" */
	AND ( S.SALES_CM_CATEGORY <> '0' AND S.SALES_CM_CATEGORY <> '1' )
	/*END*/
/*BEGIN*/
ORDER BY
	/*IF sortColumnSlipId != null */
	SL./*$sortColumnSlipId*/  ASC
	/*END*/
	/*IF sortColumnLineNo != null */
	,SL./*$sortColumnLineNo*/ ASC
	/*END*/
/*END*/
/*BEGIN*/
	LIMIT /*rowCount*/ OFFSET /*offsetRow*/
/*END*/
/*IF lockRecord != null */
/*$lockRecord*/
/*END*/
