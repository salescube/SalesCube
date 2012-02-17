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
		/*IF customerCode != null */
		D.CUSTOMER_CODE = /*customerCode*/'C%'
		/*END*/
		/*IF depositDateFrom != null */
		AND D.DEPOSIT_DATE >= /*depositDateFrom*/'2009/12/01'
		/*END*/
		/*IF depositDateTo != null */
		AND D.DEPOSIT_DATE <= /*depositDateTo*/'2009/12/31'
		/*END*/
		/*IF salesCutoffDate != null */
		AND (
			D.SALES_CUTOFF_DATE > /*salesCutoffDate*/'2009/12/31'
			OR
			D.SALES_CUTOFF_DATE IS NULL
		)
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
