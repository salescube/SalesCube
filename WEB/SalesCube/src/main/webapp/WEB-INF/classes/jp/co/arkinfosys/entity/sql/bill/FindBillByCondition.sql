(
SELECT
		/*IF billCrtCategory != '01' */
		concat( cast(B.BILL_ID as char ), '-' , cast(B.BILL_ID as char ) ) BILL_ID
		,B.BILL_ID SORT_BILL
		/*END*/
		/*IF billCrtCategory == '01' */
		concat( cast(S.SALES_SLIP_ID as char ), '-' , cast(B.BILL_ID as char ) ) BILL_ID
		,S.SALES_SLIP_ID SORT_BILL
		/*END*/
		,B.LAST_PRINT_DATE
		,CT.CATEGORY_CODE_NAME AS BILL_CRT_NAME
		,B.COV_PRICE
		,B.SALES_PRICE
		,B.CTAX_PRICE
		,( B.SALES_PRICE + B.CTAX_PRICE ) AS TAX_IN_PRICE
		,B.THIS_BILL_PRICE
		,B.CUSTOMER_CODE
		,C.CUSTOMER_NAME
		,D.DELIVERY_NAME
        ,1 AS DOA
    FROM
        BILL_TRN_/*$domainId*/ B
		LEFT OUTER JOIN CUSTOMER_MST_/*$domainId*/ C
			ON B.CUSTOMER_CODE = C.CUSTOMER_CODE
		LEFT OUTER JOIN DELIVERY_MST_/*$domainId*/ D
			ON B.BA_CODE = D.DELIVERY_CODE
		LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CT
			ON B.BILL_CRT_CATEGORY = CT.CATEGORY_CODE AND CT.CATEGORY_ID = /*billCrtCategoryCode*/
		/*IF billCrtCategory == '01' */
		INNER JOIN SALES_SLIP_TRN_/*$domainId*/ S
			ON B.BILL_ID = S.SALES_BILL_ID
		/*END*/
    /*BEGIN*/
	WHERE
		/*IF billId != null */
		/*IF billCrtCategory != '01' */
		B.BILL_ID = /*billId*/0
		/*END*/
		/*IF billCrtCategory == '01' */
		S.SALES_SLIP_ID = /*billId*/0
		/*END*/
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
)
UNION ALL
(
SELECT
		/*IF billCrtCategory != '01' */
		concat( cast(Z.BILL_ID as char ), '-' , cast(Z.BILL_ID as char ) ) BILL_ID
		,Z.BILL_ID SORT_BILL
		/*END*/
		/*IF billCrtCategory == '01' */
		concat( cast(S.SALES_SLIP_ID as char ), '-' , cast(Z.BILL_ID as char ) ) BILL_ID
		,S.SALES_SLIP_ID SORT_BILL
		/*END*/
		,Z.LAST_PRINT_DATE
		,CT.CATEGORY_CODE_NAME AS BILL_CRT_NAME
		,Z.COV_PRICE
		,Z.SALES_PRICE
		,Z.CTAX_PRICE
		,( Z.SALES_PRICE + Z.CTAX_PRICE ) AS TAX_IN_PRICE
		,Z.THIS_BILL_PRICE
		,Z.CUSTOMER_CODE
		,C.CUSTOMER_NAME
		,D.DELIVERY_NAME
        ,2 AS DOA
    FROM
        BILL_OLD_/*$domainId*/ Z
		LEFT OUTER JOIN CUSTOMER_MST_/*$domainId*/ C
			ON Z.CUSTOMER_CODE = C.CUSTOMER_CODE
		LEFT OUTER JOIN DELIVERY_MST_/*$domainId*/ D
			ON Z.BA_CODE = D.DELIVERY_CODE
		LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CT
			ON Z.BILL_CRT_CATEGORY = CT.CATEGORY_CODE AND CT.CATEGORY_ID = /*billCrtCategoryCode*/
		/*IF billCrtCategory == '01' */
		INNER JOIN SALES_SLIP_TRN_/*$domainId*/ S
			ON Z.BILL_ID = S.SALES_BILL_ID
		/*END*/
    /*BEGIN*/
	WHERE
		/*IF billId != null */
		/*IF billCrtCategory != '01' */
		Z.BILL_ID = /*billId*/0
		/*END*/
		/*IF billCrtCategory == '01' */
		S.SALES_SLIP_ID = /*billId*/0
		/*END*/
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
)
	/*BEGIN*/
ORDER BY

	/*IF sortColumn != null */
		/*IF sortColumn == 'BILL_ID' */
			SORT_BILL
		/*END*/
		/*IF sortColumn != 'BILL_ID' */
			/*$sortColumn*/
		/*END*/
		/*IF sortOrder != null*/
		/*$sortOrder*/
		-- ELSE ASC
		/*END*/
	/*END*/
/*END*/
/*IF rowCount != null && offsetRow != null */
LIMIT /*rowCount*/ OFFSET /*offsetRow*/
/*END*/




