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
        BILL_TRN_/*$domainId*/ B INNER JOIN (
			SELECT
				CUTOFF_GROUP,
				PAYBACK_CYCLE_CATEGORY,
				MAX(B2.BILL_CUTOFF_DATE) AS LAST_CUTOFF_DATE
			FROM
				BILL_TRN_/*$domainId*/ B2
		    /*BEGIN*/
			WHERE
				/*IF customerCode != null */
				B2.CUSTOMER_CODE IN /*customerCode*/('S', 'S')
				/*END*/
				/*IF billCrtCategory != null */
				AND B2.BILL_CRT_CATEGORY = /*billCrtCategory*/'2'
				/*END*/
				/*IF billCutoffDate != null */
				AND B2.BILL_CUTOFF_DATE < DATE_SUB(/*billCutoffDate*/'', INTERVAL (CASE WHEN B2.CUTOFF_GROUP = '31' AND B2.PAYBACK_CYCLE_CATEGORY = '2' THEN 1 ELSE 0 END) MONTH)
				/*END*/
				/*IF billId != null */
				AND B.BILL_ID = /*billId*/'1'
				/*END*/
			/*END*/
			GROUP BY
				CUTOFF_GROUP,
				PAYBACK_CYCLE_CATEGORY
		) V ON B.BILL_CUTOFF_DATE = V.LAST_CUTOFF_DATE AND B.CUTOFF_GROUP = V.CUTOFF_GROUP AND B.PAYBACK_CYCLE_CATEGORY = V.PAYBACK_CYCLE_CATEGORY
    /*BEGIN*/
	WHERE
		/*IF customerCode != null */
		B.CUSTOMER_CODE IN /*customerCode*/('S', 'S')
		/*END*/
		/*IF billCrtCategory != null */
		AND B.BILL_CRT_CATEGORY = /*billCrtCategory*/'2'
		/*END*/
		/*IF billCutoffDate != null */
		AND B.BILL_CUTOFF_DATE < DATE_SUB(/*billCutoffDate*/'', INTERVAL (CASE WHEN B.CUTOFF_GROUP = '31' AND B.PAYBACK_CYCLE_CATEGORY = '2' THEN 1 ELSE 0 END) MONTH)
		/*END*/
		/*IF billId != null */
		AND B.BILL_ID = /*billId*/'1'
		/*END*/
	/*END*/
