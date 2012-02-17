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
		/*IF salesCutoffDate != null */
		D.SALES_CUTOFF_DATE = /*salesCutoffDate*/NULL
		/*END*/
		/*IF salesCutoffDate == null */
		D.SALES_CUTOFF_DATE is null
		/*END*/
		/*IF customerCode != null */
		AND D.CUSTOMER_CODE = /*customerCode*/'C%'
		/*END*/
		/*IF depositDate != null */
		AND D.DEPOSIT_DATE <= /*depositDate*/'2009/12/01'
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
