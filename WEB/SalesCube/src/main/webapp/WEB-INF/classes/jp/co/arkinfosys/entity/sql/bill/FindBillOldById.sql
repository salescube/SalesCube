SELECT
        Z.BILL_ID
        ,Z.STATUS
        ,Z.BILL_YEAR
        ,Z.BILL_MONTH
        ,Z.BILL_YM
        ,Z.BILL_CUTOFF_DATE
        ,Z.CUTOFF_GROUP
        ,Z.PAYBACK_CYCLE_CATEGORY
        ,Z.CUTOFF_PDATE
        ,Z.REMARKS
        ,Z.BA_CODE
        ,Z.CUSTOMER_CODE
		,Z.LAST_BILL_PRICE
		,Z.DEPOSIT_PRICE
		,Z.ADJ_PRICE
		,Z.COV_PRICE
		,Z.SALES_PRICE
		,Z.CTAX_PRICE
		,Z.RGU_PRICE
		,Z.DCT_PRICE
		,Z.ETC_PRICE
		,Z.THIS_BILL_PRICE
		,Z.SLIP_NUM
		,Z.COD_LAST_BILL_PRICE
		,Z.COD_DEPOSIT_PRICE
		,Z.COD_ADJ_PRICE
		,Z.COD_COV_PRICE
		,Z.COD_SALES_PRICE
		,Z.COD_CTAX_PRICE
		,Z.COD_RGU_PRICE
		,Z.COD_DCT_PRICE
		,Z.COD_ETC_PRICE
		,Z.COD_THIS_BILL_PRICE
		,Z.COD_SLIP_NUM
		,Z.USER_ID
		,Z.USER_NAME
		,Z.PAYBACK_PLAN_DATE
		,Z.LAST_PRINT_DATE
		,Z.BILL_PRINT_COUNT
		,Z.BILL_CRT_CATEGORY
		,Z.LAST_SALES_DATE
		,Z.CRE_FUNC
		,Z.CRE_DATETM
		,Z.CRE_USER
		,Z.UPD_FUNC
		,Z.UPD_DATETM
		,Z.UPD_USER
    FROM
        BILL_OLD_/*$domainId*/ Z
    /*BEGIN*/
	WHERE
		/*IF customerCode != null */
		Z.CUSTOMER_CODE = /*customerCode*/'S%'
		/*END*/
		/*IF billCrtCategory != null */
		AND Z.BILL_CRT_CATEGORY = /*billCrtCategory*/'2'
		/*END*/
		/*IF billId != null */
		AND Z.BILL_ID = /*billId*/'1'
		/*END*/
	/*END*/
	/*BEGIN*/
	ORDER BY
		/*IF sortColumnBillCutoffDate != null */
		Z./*$sortColumnBillCutoffDate*/
		/*END*/

		/*IF sortOrder != null*/
		/*$sortOrder*/
		/*END*/
	/*END*/
	/*BEGIN*/
		LIMIT /*rowCount*/ OFFSET /*offsetRow*/
	/*END*/
