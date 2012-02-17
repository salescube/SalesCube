SELECT
		DL.DEPOSIT_LINE_ID
		,DL.STATUS
		,DL.DEPOSIT_SLIP_ID
		,DL.LINE_NO
		,DL.DEPOSIT_CATEGORY
		,DL.PRICE
		,DL.INST_DATE
		,DL.INST_NO
		,DL.BANK_ID
		,DL.BANK_INFO
		,DL.REMARKS
		,DL.SALES_LINE_ID
		,DL.CRE_FUNC
		,DL.CRE_DATETM
		,DL.CRE_USER
		,DL.UPD_FUNC
		,DL.UPD_DATETM
		,DL.UPD_USER
    FROM
		DEPOSIT_SLIP_TRN_/*$domainId*/ D
			INNER JOIN DEPOSIT_LINE_TRN_/*$domainId*/ DL ON D.DEPOSIT_SLIP_ID = DL.DEPOSIT_SLIP_ID
	WHERE
		DL.STATUS = /*status*/'0'
		AND D.STATUS = /*slipStatus*/'0'
		/*IF customerCode != null */
		AND D.CUSTOMER_CODE = /*customerCode*/'C%'
		/*END*/
		/*IF depositDate != null */
		AND D.DEPOSIT_DATE <= /*depositDate*/'2009/12/01'
		/*END*/
		/*IF depositCategory == '03' */	-- 売掛の対象は「振込:03」「現金:01」「切手:09」「その他:06」
		AND ( D.DEPOSIT_CATEGORY = '03' OR D.DEPOSIT_CATEGORY = '01' OR D.DEPOSIT_CATEGORY = '09' OR D.DEPOSIT_CATEGORY = '06' )
		/*END*/
		/*IF depositCategory == '10' */
		AND ( D.DEPOSIT_CATEGORY <> '03' AND D.DEPOSIT_CATEGORY <> '01' AND D.DEPOSIT_CATEGORY <> '09' AND D.DEPOSIT_CATEGORY <> '06' )
		/*END*/
	/*BEGIN*/
	ORDER BY

		/*IF sortColumnSlipId != null */
		DL./*$sortColumnSlipId*/ ASC
		/*END*/
		/*IF sortColumnDepositLine != null */
		,DL./*$sortColumnDepositLine*/ ASC
		/*END*/
	/*END*/
	/*BEGIN*/
		LIMIT /*rowCount*/ OFFSET /*offsetRow*/
	/*END*/
