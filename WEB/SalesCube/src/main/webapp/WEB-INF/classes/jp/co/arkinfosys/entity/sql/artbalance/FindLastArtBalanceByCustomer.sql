SELECT
		S.ART_BALANCE_ID
		,S.ART_ANNUAL
		,S.ART_MONTHLY
		,S.ART_YM
		,S.ART_CUTOFF_DATE
		,S.USER_ID
		,S.USER_NAME
		,S.BA_CODE
		,S.BA_NAME
		,S.CUSTOMER_CODE
		,S.CUSTOMER_NAME
		,S.SALES_CM_CATEGORY
		,S.LAST_ART_PRICE
		,S.DEPOSIT_PRICE
		,S.ADJ_PRICE
		,S.COV_PRICE
		,S.SALES_PRICE
		,S.CTAX_PRICE
		,S.RGU_PRICE
		,S.DCT_PRICE
		,S.ETC_PRICE
		,S.THIS_ART_PRICE
		,S.GM_PRICE
		,S.ART_CUTOFF_GROUP
		,S.PAYBACK_CYCLE_CATEGORY
		,S.SALES_SLIP_NUM
		,S.ART_CUTOFF_PDATE
		,S.DEPOSIT_CASH
		,S.DEPOSIT_CHECK
		,S.DEPOSIT_TRANSFER
		,S.DEPOSIT_SC
		,S.DEPOSIT_INST
		,S.DEPOSIT_SETOFF
		,S.DEPOSIT_ETC
		,S.FAMILY_CATEGORY
		,S.DELIVERY_PLACE_NUM
		,S.REMARKS
		,S.CRE_FUNC
		,S.CRE_DATETM
		,S.CRE_USER
		,S.UPD_FUNC
		,S.UPD_DATETM
		,S.UPD_USER
    FROM
        ART_BALANCE_TRN_/*$domainId*/ S
    /*BEGIN*/
	WHERE
		/*IF customerCode != null */
		S.CUSTOMER_CODE = /*customerCode*/'S%'
		/*END*/
		/*IF billCrtCategory != null */
		AND S.BILL_CRT_CATEGORY = /*billCrtCategory*/'2'
		/*END*/
		/*IF artCutoffDate != null */
		AND S.ART_CUTOFF_DATE = /*artCutoffDate*/'2010/01/10'
		/*END*/
		/*IF artBalanceId != null */
		AND S.ART_BALANCE_ID = /*artBalanceId*/0
		/*END*/
		/*IF artCutoffDateTo != null */
		AND S.ART_CUTOFF_DATE <= /*artCutoffDateTo*/'2010/01/10'
		/*END*/
	/*END*/
	/*BEGIN*/
	ORDER BY
		/*IF sortColumnCutoffDate != null */
		S./*$sortColumnCutoffDate*/
		/*END*/

		/*IF sortOrder != null*/
		/*$sortOrder*/
		/*END*/
	/*END*/
	/*BEGIN*/
		LIMIT /*rowCount*/ OFFSET /*offsetRow*/
	/*END*/
