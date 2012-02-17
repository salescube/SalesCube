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
        DEPOSIT_LINE_TRN_/*$domainId*/ DL
    /*BEGIN*/
	WHERE
		/*IF depositSlipId != null */
		DL.DEPOSIT_SLIP_ID = /*depositSlipId*/1
		/*END*/
		/*IF depositLineId != null */
		DL.DEPOSIT_LINE_ID = /*depositLineId*/1
		/*END*/
	/*END*/
	/*BEGIN*/
	ORDER BY
		/*IF sortColumnDepositLine != null */
		DL./*$sortColumnDepositLine*/
		/*END*/

		/*IF sortOrder != null*/
		/*$sortOrder*/
		/*END*/
	/*END*/
	/*BEGIN*/
		LIMIT /*rowCount*/ OFFSET /*offsetRow*/
	/*END*/
