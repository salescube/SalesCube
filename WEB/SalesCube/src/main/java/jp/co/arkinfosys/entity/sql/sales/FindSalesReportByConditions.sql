SELECT
	SLIP.RO_SLIP_ID,
	SLIP.SALES_SLIP_ID,
	SLIP.SALES_DATE,
	SLIP.CUSTOMER_CODE,
	SLIP.CUSTOMER_NAME,
	SLIP.BILL_PRINT_COUNT,
	SLIP.DELIVERY_PRINT_COUNT,
	SLIP.TEMP_DELIVERY_PRINT_COUNT,
	SLIP.SHIPPING_PRINT_COUNT,
	SLIP.ESTIMATE_PRINT_COUNT,
	SLIP.DELBOR_PRINT_COUNT,
	SLIP.DELIVERY_CODE,
	SLIP.SALES_CM_CATEGORY,
	CUST_MST.BILL_PRINT_UNIT,
	CUST_MST.BILL_DATE_PRINT,
	CUST_MST.TEMP_DELIVERY_SLIP_FLAG,
	CASE
		WHEN SLIP.DELIVERY_NAME = CUST_MST.CUSTOMER_NAME
			AND SLIP.DELIVERY_OFFICE_NAME = CUST_MST.CUSTOMER_OFFICE_NAME
			AND SLIP.DELIVERY_DEPT_NAME = CUST_MST.CUSTOMER_DEPT_NAME
			AND SLIP.DELIVERY_ZIP_CODE = CUST_MST.CUSTOMER_ZIP_CODE
			AND SLIP.DELIVERY_ADDRESS_1 = CUST_MST.CUSTOMER_ADDRESS_1
			AND SLIP.DELIVERY_ADDRESS_2 = CUST_MST.CUSTOMER_ADDRESS_2
			AND SLIP.DELIVERY_PC_NAME = CUST_MST.CUSTOMER_PC_NAME
			AND SLIP.DELIVERY_PC_PRE_CATEGORY = CUST_MST.CUSTOMER_PC_PRE_CATEGORY
			AND SLIP.DELIVERY_TEL = CUST_MST.CUSTOMER_TEL
			AND SLIP.DELIVERY_FAX = CUST_MST.CUSTOMER_FAX
			THEN 'true'
		ELSE	'false'
	END FIRST_DELIVERY_CODE
FROM
	SALES_SLIP_TRN_/*$domainId*/ SLIP
		INNER JOIN CUSTOMER_MST_/*$domainId*/ CUST_MST ON SLIP.CUSTOMER_CODE = CUST_MST.CUSTOMER_CODE
/*BEGIN*/
WHERE
	/*IF salesDateFrom != null && salesDateTo != null */
	AND SLIP.SALES_DATE BETWEEN /*salesDateFrom*/ AND /*salesDateTo*/
	/*END*/
	/*IF salesDateFrom != null && salesDateTo == null */
	AND SLIP.SALES_DATE >= /*salesDateFrom*/
	/*END*/
	/*IF salesDateFrom == null && salesDateTo != null */
	AND SLIP.SALES_DATE <= /*salesDateTo*/
	/*END*/
	/*IF roSlipIdFrom != null && roSlipIdTo != null */
	AND SLIP.RO_SLIP_ID BETWEEN /*roSlipIdFrom*/ AND /*roSlipIdTo*/
	/*END*/
	/*IF roSlipIdFrom != null && roSlipIdTo == null */
	AND SLIP.RO_SLIP_ID >= /*roSlipIdFrom*/
	/*END*/
	/*IF roSlipIdFrom == null && roSlipIdTo != null */
	AND SLIP.RO_SLIP_ID <= /*roSlipIdTo*/
	/*END*/
	/*IF salesSlipIdFrom != null && salesSlipIdTo != null*/
	AND SLIP.SALES_SLIP_ID BETWEEN /*salesSlipIdFrom*/ AND /*salesSlipIdTo*/
	/*END*/
	/*IF salesSlipIdFrom != null && salesSlipIdTo == null*/
	AND SLIP.SALES_SLIP_ID >= /*salesSlipIdFrom*/
	/*END*/
	/*IF salesSlipIdFrom == null && salesSlipIdTo != null*/
	AND SLIP.SALES_SLIP_ID <= /*salesSlipIdTo*/
	/*END*/
	/*IF receptNo != null */
	AND SLIP.RECEPT_NO LIKE /*receptNo*/
	/*END*/
	/*IF salesCategoryList.size() > 0 */
	AND SLIP.SALES_CM_CATEGORY IN /*salesCategoryList*/('', '')
	/*END*/

	/*IF excludingOutputAll != null*/
	AND (
		-- ピッキングリストと納品書は全てのケースにおいて存在するので、これらが印刷されていない場合は検索ヒットさせる
		(SLIP.SHIPPING_PRINT_COUNT = 0 OR SLIP.DELIVERY_PRINT_COUNT = 0)
		OR (
			-- 請求書発行単位が「請求書なし」の場合納品書兼領収書を発行する仕様なので、納品書兼領収書が発行されていない場合検索ヒットさせる
			CUST_MST.BILL_PRINT_UNIT = '0' AND SLIP.DELBOR_PRINT_COUNT = 0
		)
		OR (
			-- 請求書発行単位が「売上伝票単位」の場合売上単位の請求書を発行する仕様なので、売上単位の請求書が発行されていない場合検索ヒットさせる
			CUST_MST.BILL_PRINT_UNIT = '2' AND BILL_PRINT_COUNT = 0
		)
		OR (
			-- 納入先情報と顧客情報が異なる場合のみ、仮納品書を発行するので、仮納品書が発行されていない場合検索ヒットさせる
			(SLIP.DELIVERY_NAME <> CUST_MST.CUSTOMER_NAME
				OR SLIP.DELIVERY_OFFICE_NAME <> CUST_MST.CUSTOMER_OFFICE_NAME
				OR SLIP.DELIVERY_DEPT_NAME <> CUST_MST.CUSTOMER_DEPT_NAME
				OR SLIP.DELIVERY_ZIP_CODE <> CUST_MST.CUSTOMER_ZIP_CODE
				OR SLIP.DELIVERY_ADDRESS_1 <> CUST_MST.CUSTOMER_ADDRESS_1
				OR SLIP.DELIVERY_ADDRESS_2 <> CUST_MST.CUSTOMER_ADDRESS_2
				OR SLIP.DELIVERY_PC_NAME <> CUST_MST.CUSTOMER_PC_NAME
				OR SLIP.DELIVERY_PC_PRE_CATEGORY <> CUST_MST.CUSTOMER_PC_PRE_CATEGORY
				OR SLIP.DELIVERY_TEL <> CUST_MST.CUSTOMER_TEL
				OR SLIP.DELIVERY_FAX <> CUST_MST.CUSTOMER_FAX
			)
			AND SLIP.TEMP_DELIVERY_PRINT_COUNT = 0
		)
	)
	/*END*/

/*END*/
/*BEGIN*/
	ORDER BY
		/*IF sortColumn != null */
			SLIP./*$sortColumn*/
			/*IF sortOrderAsc != null*/
			/*$sortOrderAsc*/
			-- ELSE ASC
			/*END*/
			/*IF sortColumn != 'SALES_SLIP_ID'*/
			,
			/*END*/
		/*END*/
		/*IF sortColumn != 'SALES_SLIP_ID'*/
		SLIP.SALES_SLIP_ID ASC
		/*END*/
/*END*/
/*IF rowCount != null && offsetRow != null*/
LIMIT /*rowCount*/ OFFSET /*offsetRow*/
/*END*/