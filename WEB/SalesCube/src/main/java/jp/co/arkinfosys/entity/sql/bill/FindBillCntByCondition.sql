SELECT
	BL.CNT + ZB.CNT
FROM
(
SELECT COUNT(*) CNT
    FROM
        BILL_TRN_/*$domainId*/ B
		LEFT OUTER JOIN CUSTOMER_MST_/*$domainId*/ C
			ON B.CUSTOMER_CODE = C.CUSTOMER_CODE
		/*IF billCrtCategory == '01' */
		INNER JOIN SALES_SLIP_TRN_/*$domainId*/ S
			ON B.BILL_ID = S.SALES_BILL_ID
		/*END*/
    /*BEGIN*/
	WHERE
		/*IF billId != null */
		B.BILL_ID = /*billId*/0
		/*END*/
		/*IF lastSalesDateFrom != null */
		AND B.LAST_SALES_DATE >= CAST(/*lastSalesDateFrom*/ AS DATE)
		/*END*/
		/*IF lastSalesDateTo != null */
		AND B.LAST_SALES_DATE <= CAST(/*lastSalesDateTo*/ AS DATE)
		/*END*/
		/*IF lastPrintDateFrom != null */
		AND B.LAST_PRINT_DATE >= CAST(/*lastPrintDateFrom*/ AS DATE)
		/*END*/
		/*IF lastPrintDateTo != null */
		AND B.LAST_PRINT_DATE <= CAST(/*lastPrintDateTo*/ AS DATE)
		/*END*/
		/*IF cutoffGroup != null */
		AND B.CUTOFF_GROUP = /*cutoffGroup*/'1'
		/*END*/
		/*IF paybackCycleCategory != null */
		AND B.PAYBACK_CYCLE_CATEGORY = /*paybackCycleCategory*/'1'
		/*END*/
		/*IF cutoffGroupCategory != null */
		AND concat( B.CUTOFF_GROUP , B.PAYBACK_CYCLE_CATEGORY ) = /*cutoffGroupCategory*/'1'
		/*END*/
		/*IF billCrtCategory != '01' */
			/*IF billCutoffDateFrom != null */
			AND B.BILL_CUTOFF_DATE >= CAST(/*billCutoffDateFrom*/ AS DATE)
			/*END*/
			/*IF billCutoffDateTo != null */
			AND B.BILL_CUTOFF_DATE <= CAST(/*billCutoffDateTo*/ AS DATE)
			/*END*/
			/*IF billCrtCategory != null */
			AND B.BILL_CRT_CATEGORY = /*billCrtCategory*/'1'
			/*END*/
		/*END*/
		/*IF billCrtCategory == '01' */
		AND B.BILL_CRT_CATEGORY = /*billCrtCategory*/'1'
		/*END*/
		/*IF customerCode != null */
		AND B.CUSTOMER_CODE LIKE /*customerCode*/'S%'
		/*END*/
		/*IF customerName != null */
		AND C.CUSTOMER_NAME LIKE /*customerName*/'%S%'
		/*END*/

		/*IF covPriceZero != null || covPriceMinus != null || covPricePlus != null*/
		AND (
		/*BEGIN*/
			/*IF covPriceZero != null*/
			B.COV_PRICE = 0
			/*END*/
			/*IF covPriceMinus != null*/
			OR B.COV_PRICE < 0
			/*END*/
			/*IF covPricePlus != null*/
			OR B.COV_PRICE > 0
			/*END*/
		/*END*/
		)
		/*END*/

		/*IF thisBillPricePlus != null || thisBillPriceZero != null || thisBillPriceMinus != null*/
		AND (
		/*BEGIN*/
			/*IF thisBillPricePlus != null*/
			B.THIS_BILL_PRICE > 0
			/*END*/
			/*IF thisBillPriceZero != null*/
			OR B.THIS_BILL_PRICE = 0
			/*END*/
			/*IF thisBillPriceMinus != null*/
			OR B.THIS_BILL_PRICE < 0
			/*END*/
		/*END*/
		)
		/*END*/

	/*END*/
) BL,
(
SELECT COUNT(*) CNT
    FROM
        BILL_OLD_/*$domainId*/ Z
		LEFT OUTER JOIN CUSTOMER_MST_/*$domainId*/ C
			ON Z.CUSTOMER_CODE = C.CUSTOMER_CODE
		/*IF billCrtCategory == '01' */
		INNER JOIN SALES_SLIP_TRN_/*$domainId*/ S
			ON Z.BILL_ID = S.SALES_BILL_ID
		/*END*/
    /*BEGIN*/
	WHERE
		/*IF billId != null */
		Z.BILL_ID = /*billId*/0
		/*END*/
		/*IF lastSalesDateFrom != null */
		AND Z.LAST_SALES_DATE >= CAST(/*lastSalesDateFrom*/ AS DATE)
		/*END*/
		/*IF lastSalesDateTo != null */
		AND Z.LAST_SALES_DATE <= CAST(/*lastSalesDateTo*/ AS DATE)
		/*END*/
		/*IF lastPrintDateFrom != null */
		AND Z.LAST_PRINT_DATE >= CAST(/*lastPrintDateFrom*/ AS DATE)
		/*END*/
		/*IF lastPrintDateTo != null */
		AND Z.LAST_PRINT_DATE <= CAST(/*lastPrintDateTo*/ AS DATE)
		/*END*/
		/*IF cutoffGroup != null */
		AND Z.CUTOFF_GROUP = /*cutoffGroup*/'1'
		/*END*/
		/*IF paybackCycleCategory != null */
		AND Z.PAYBACK_CYCLE_CATEGORY = /*paybackCycleCategory*/'1'
		/*END*/
		/*IF cutoffGroupCategory != null */
		AND concat( Z.CUTOFF_GROUP , Z.PAYBACK_CYCLE_CATEGORY ) = /*cutoffGroupCategory*/'1'
		/*END*/
		/*IF billCrtCategory != '01' */
			/*IF billCutoffDateFrom != null */
			AND Z.BILL_CUTOFF_DATE >= CAST(/*billCutoffDateFrom*/ AS DATE)
			/*END*/
			/*IF billCutoffDateTo != null */
			AND Z.BILL_CUTOFF_DATE <= CAST(/*billCutoffDateTo*/ AS DATE)
			/*END*/
			/*IF billCrtCategory != null */
			AND Z.BILL_CRT_CATEGORY = /*billCrtCategory*/'1'
			/*END*/
		/*END*/
		/*IF billCrtCategory == '01' */
		AND Z.BILL_CRT_CATEGORY = /*billCrtCategory*/'1'
		/*END*/
		/*IF customerCode != null */
		AND Z.CUSTOMER_CODE LIKE /*customerCode*/'S%'
		/*END*/
		/*IF customerName != null */
		AND C.CUSTOMER_NAME LIKE /*customerName*/'%S%'
		/*END*/

		/*IF covPriceZero != null || covPriceMinus != null || covPricePlus != null*/
		AND (
		/*BEGIN*/
			/*IF covPriceZero != null*/
			Z.COV_PRICE = 0
			/*END*/
			/*IF covPriceMinus != null*/
			OR Z.COV_PRICE < 0
			/*END*/
			/*IF covPricePlus != null*/
			OR Z.COV_PRICE > 0
			/*END*/
		/*END*/
		)
		/*END*/

		/*IF thisBillPricePlus != null || thisBillPriceZero != null || thisBillPriceMinus != null*/
		AND (
		/*BEGIN*/
			/*IF thisBillPricePlus != null*/
			Z.THIS_BILL_PRICE > 0
			/*END*/
			/*IF thisBillPriceZero != null*/
			OR Z.THIS_BILL_PRICE = 0
			/*END*/
			/*IF thisBillPriceMinus != null*/
			OR Z.THIS_BILL_PRICE < 0
			/*END*/
		/*END*/
		)
		/*END*/

	/*END*/
) ZB
