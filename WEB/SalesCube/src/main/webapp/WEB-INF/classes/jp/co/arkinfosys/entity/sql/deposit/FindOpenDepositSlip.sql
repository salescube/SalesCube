SELECT
	D.DEPOSIT_SLIP_ID
	,D.STATUS
	,D.DEPOSIT_DATE
	,D.INPUT_PDATE
	,D.DEPOSIT_ANNUAL
	,D.DEPOSIT_MONTHLY
	,D.DEPOSIT_YM
	,D.USER_ID
	,D.USER_NAME
	,D.DEPOSIT_ABSTRACT
	,D.REMARKS
	,D.CUSTOMER_CODE
	,D.CUSTOMER_NAME
	,D.CUSTOMER_REMARKS
	,D.CUSTOMER_COMMENT_DATA
    ,D.CUTOFF_GROUP
    ,D.PAYBACK_CYCLE_CATEGORY
	,D.BA_CODE
	,D.BA_NAME
	,D.BA_KANA
	,D.BA_OFFICE_NAME
	,D.BA_OFFICE_KANA
	,D.BA_DEPT_NAME
	,D.BA_ZIP_CODE
	,D.BA_ADDRESS_1
	,D.BA_ADDRESS_2
	,D.BA_PC_NAME
	,D.BA_PC_KANA
	,D.BA_PC_PRE_CATRGORY
	,D.BA_PC_PRE
	,D.BA_TEL
	,D.BA_FAX
	,D.BA_EMAIL
	,D.BA_URL
	,D.SALES_CM_CATEGORY
	,D.DEPOSIT_CATEGORY
	,D.DEPOSIT_TOTAL
	,D.BILL_ID
	,D.BILL_CUTOFF_DATE
	,D.BILL_CUTOFF_PDATE
	,D.ART_ID
	,D.SALES_SLIP_ID
	,D.DEPOSIT_METHOD_TYPE_CATEGORY
	,D.TAX_FRACT_CATEGORY
	,D.PRICE_FRACT_CATEGORY
	,D.CRE_FUNC
	,D.CRE_DATETM
	,D.CRE_USER
	,D.UPD_FUNC
	,D.UPD_DATETM
	,D.UPD_USER
	,D.SALES_CUTOFF_DATE
	,D.SALES_CUTOFF_PDATE
    FROM
        DEPOSIT_SLIP_TRN_/*$domainId*/ D
	WHERE
		D.STATUS = /*status*/'0'
		/*IF customerCode != null */
		AND D.CUSTOMER_CODE = /*customerCode*/'C%'
		/*END*/
		/*IF depositDate != null */
		AND D.DEPOSIT_DATE <= /*depositDate*/'2009/12/01'
		/*END*/
		/*IF billCutoffDate != null */
		AND D.BILL_CUTOFF_DATE = /*billCutoffDate*/'2009/12/01'
		/*END*/
		/*IF depositCategory == '03' */	-- 売掛の対象は「振込:03」「現金:01」「切手:09」「その他:06」
		AND ( D.DEPOSIT_CATEGORY = '03' OR D.DEPOSIT_CATEGORY = '01' OR D.DEPOSIT_CATEGORY = '09' OR D.DEPOSIT_CATEGORY = '06' )
		/*END*/
		/*IF depositCategory == '10' */
		AND ( D.DEPOSIT_CATEGORY <> '03' AND D.DEPOSIT_CATEGORY <> '01' AND D.DEPOSIT_CATEGORY <> '09' AND D.DEPOSIT_CATEGORY <> '06' )
		/*END*/
	/*BEGIN*/
	ORDER BY
		/*IF sortColumnDepositDate != null */
		D./*$sortColumnDepositDate*/
		/*END*/

		/*IF sortOrder != null*/
		/*$sortOrder*/
		/*END*/
	/*END*/
	/*BEGIN*/
		LIMIT /*rowCount*/ OFFSET /*offsetRow*/
	/*END*/
	/*IF lockRecord != null */
	/*$lockRecord*/
	/*END*/

