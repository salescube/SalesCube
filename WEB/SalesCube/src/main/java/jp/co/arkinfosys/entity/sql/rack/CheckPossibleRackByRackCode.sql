SELECT
	R.RACK_CODE
FROM
	RACK_MST_/*$domainId*/ R
WHERE
	R.RACK_CODE=/*rackCode*/
	AND (
		R.MULTI_FLAG='1' -- 重複登録可
		OR
		NOT EXISTS( SELECT 1 FROM PRODUCT_MST_/*$domainId*/ P WHERE P.RACK_CODE=R.RACK_CODE /*IF productCode != null */AND P.PRODUCT_CODE <> /*productCode*//*END*/ ) -- 商品マスタに存在しない
	)