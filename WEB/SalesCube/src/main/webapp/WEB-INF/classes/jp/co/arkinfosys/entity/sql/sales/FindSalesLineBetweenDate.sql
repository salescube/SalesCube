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
	/*IF customerCode != null */
	S.CUSTOMER_CODE = /*customerCode*/'C%'
	/*END*/
	/*IF salesDateFrom != null */
	AND S.SALES_DATE >= /*salesDateFrom*/'2009/12/01'
	/*END*/
	/*IF salesDateTo != null */
	AND S.SALES_DATE <= /*salesDateTo*/'2009/12/31'
	/*END*/
	/*IF salesCutoffDate != null */
	AND (
		S.SALES_CUTOFF_DATE > /*salesCutoffDate*/'2009/12/31'
		OR
		S.SALES_CUTOFF_DATE IS NULL
	)
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
