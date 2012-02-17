SELECT
        B.BILL_ID
        ,B.STATUS
        ,B.BILL_YEAR
        ,B.BILL_MONTH
        ,B.BILL_YM
        ,B.BILL_CUTOFF_DATE
        ,B.CUTOFF_GROUP
        ,B.PAYBACK_CYCLE_CATEGORY
        ,B.CUTOFF_PDATE
        ,B.REMARKS
        ,B.BA_CODE
        ,B.CUSTOMER_CODE
		,B.LAST_BILL_PRICE
		,B.DEPOSIT_PRICE
		,B.ADJ_PRICE
		,B.COV_PRICE
		,B.SALES_PRICE
		,B.CTAX_PRICE
		,B.RGU_PRICE
		,B.DCT_PRICE
		,B.ETC_PRICE
		,B.THIS_BILL_PRICE
		,B.SLIP_NUM
		,B.COD_LAST_BILL_PRICE
		,B.COD_DEPOSIT_PRICE
		,B.COD_ADJ_PRICE
		,B.COD_COV_PRICE
		,B.COD_SALES_PRICE
		,B.COD_CTAX_PRICE
		,B.COD_RGU_PRICE
		,B.COD_DCT_PRICE
		,B.COD_ETC_PRICE
		,B.COD_THIS_BILL_PRICE
		,B.COD_SLIP_NUM
		,B.USER_ID
		,B.USER_NAME
		,B.PAYBACK_PLAN_DATE
		,B.LAST_PRINT_DATE
		,B.BILL_PRINT_COUNT
		,B.BILL_CRT_CATEGORY
		,B.LAST_SALES_DATE
		,B.CRE_FUNC
		,B.CRE_DATETM
		,B.CRE_USER
		,B.UPD_FUNC
		,B.UPD_DATETM
		,B.UPD_USER
    FROM
        BILL_TRN_/*$domainId*/ B
    /*BEGIN*/
	WHERE
		/*IF customerCode != null */
		B.CUSTOMER_CODE = /*customerCode*/'S%'
		/*END*/
		/*IF billYear != null */
		AND B.BILL_YEAR = /*billYear*/'2010'
		/*END*/
		/*IF billMonth != null */
		AND B.BILL_MONTH = /*billMonth*/'01'
		/*END*/
		/*IF billCrtCategory != null */
		AND B.BILL_CRT_CATEGORY = /*billCrtCategory*/'2'
		/*END*/
	/*END*/
	/*BEGIN*/
	ORDER BY
		/*IF sortColumnCustomerCode != null */
		B./*$sortColumnCustomerCode*/
		/*END*/
		/*IF sortOrder != null*/
		/*$sortOrder*/
		/*END*/
	/*END*/
	/*BEGIN*/
		LIMIT /*rowCount*/ OFFSET /*offsetRow*/
	/*END*/
