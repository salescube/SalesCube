SELECT
	COUNT(DISTINCT SLIP.DEPOSIT_SLIP_ID) AS CNT
	,SUM(DEPOSIT_TOTAL) AS TOTAL
FROM
	DEPOSIT_SLIP_TRN_/*$domainId*/ SLIP
	LEFT OUTER JOIN CUSTOMER_MST_/*$domainId*/ CUST ON SLIP.CUSTOMER_CODE = CUST.CUSTOMER_CODE
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT1 ON (SLIP.DEPOSIT_CATEGORY = CAT1.CATEGORY_CODE AND CAT1.CATEGORY_ID = /*depositCategoryMst*/14)
	LEFT OUTER JOIN CATEGORY_TRN_/*$domainId*/ CAT2 ON (SLIP.DEPOSIT_METHOD_TYPE_CATEGORY = CAT2.CATEGORY_CODE AND CAT2.CATEGORY_ID = /*depositMethodCategory*/62)
/*BEGIN*/
WHERE
	/*IF depositSlipId != null */
	AND SLIP.DEPOSIT_SLIP_ID = /*depositSlipId*/1
	/*END*/
	/*IF userId != null */
	AND SLIP.USER_ID like /*userId*/'1%'
	/*END*/
	/*IF userName != null */
	AND SLIP.USER_NAME like /*userName*/'%1%'
	/*END*/
	/*IF depositDateFrom != null */
	AND SLIP.DEPOSIT_DATE >= CAST(/*depositDateFrom*/'2000/01/01' AS DATE)
	/*END*/
	/*IF depositDateTo != null */
	AND SLIP.DEPOSIT_DATE <= CAST(/*depositDateTo*/'2000/01/01' AS DATE)
	/*END*/
	/*IF inputPdateFrom != null */
	AND SLIP.INPUT_PDATE >= CAST(/*inputPdateFrom*/'2000/01/01' AS DATE)
	/*END*/
	/*IF inputPdateTo != null */
	AND SLIP.INPUT_PDATE <= CAST(/*inputPdateTo*/'2000/01/01' AS DATE)
	/*END*/
	/*IF depositTotalFrom != null */
	AND SLIP.DEPOSIT_TOTAL >= /*depositTotalFrom*/1
	/*END*/
	/*IF depositTotalTo != null */
	AND SLIP.DEPOSIT_TOTAL <= /*depositTotalTo*/2
	/*END*/
	/*IF depositAbstract != null */
	AND SLIP.DEPOSIT_ABSTRACT like /*depositAbstract*/'%1%'
	/*END*/
	/*IF customerCode != null */
	AND SLIP.CUSTOMER_CODE LIKE /*customerCode*/'1%'
	/*END*/
	/*IF customerName != null */
	AND SLIP.CUSTOMER_NAME LIKE /*customerName*/'%1%'
	/*END*/
	/*IF paymentName != null */
	AND CUST.PAYMENT_NAME LIKE /*paymentName*/'%1%'
	/*END*/
	/*IF depositMethodTypeCategory != null */
	AND SLIP.DEPOSIT_METHOD_TYPE_CATEGORY = /*depositMethodTypeCategory*/'1'
	/*END*/
	/*IF depositCategory != null */
	AND SLIP.DEPOSIT_CATEGORY IN /*depositCategory*/('1','2')
	/*END*/
/*END*/
