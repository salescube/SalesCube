SELECT DISTINCT
	P.PRODUCT_CODE as SET_PRODUCT_CODE
	,P.PRODUCT_NAME as SET_PRODUCT_NAME
	/*IF resultContainChildren != null */
	,P2.PRODUCT_CODE
	,P2.PRODUCT_NAME
	,S.CRE_FUNC
	,S.CRE_DATETM
	,S.CRE_USER
	,S.UPD_FUNC
	,S.UPD_DATETM
	,S.UPD_USER
	/*END*/
FROM
	PRODUCT_MST_/*$domainId*/ P
	LEFT OUTER JOIN PRODUCT_SET_MST_/*$domainId*/ S
		ON S.SET_PRODUCT_CODE = P.PRODUCT_CODE
	LEFT OUTER JOIN PRODUCT_MST_/*$domainId*/ P2
		ON S.PRODUCT_CODE = P2.PRODUCT_CODE AND P2.SET_TYPE_CATEGORY = /*setTypeCategorySingle*/'0'
WHERE
	P.SET_TYPE_CATEGORY = /*setTypeCategorySet*/'1'

	/*IF setProductCode != null*/
	AND P.PRODUCT_CODE LIKE /*setProductCode*/'S%'
	/*END*/

	/*IF setProductName != null*/
	AND P.PRODUCT_NAME LIKE /*setProductName*/'%S%'
	/*END*/

	/*IF productCode != null*/
	AND P2.PRODUCT_CODE LIKE /*productCode*/'S%'
	/*END*/

	/*IF productName != null*/
	AND P2.PRODUCT_NAME LIKE /*productName*/'%S%'
	/*END*/
/*BEGIN*/
ORDER BY
	/*IF sortColumn != null */
	P./*$sortColumn*/
	/*END*/

	/*IF sortOrder != null*/
	/*$sortOrder*/
	/*END*/
/*END*/