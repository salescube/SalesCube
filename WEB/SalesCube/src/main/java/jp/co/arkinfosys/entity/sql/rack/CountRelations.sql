SELECT
        (
        	SELECT
        			COUNT(P.RACK_CODE)
        		FROM
        			PRODUCT_MST_/*$domainId*/ P
        		WHERE
        			P.RACK_CODE=R.RACK_CODE
        			-- 商品マスタに存在する
        ) AS RACK_REL_PRODUCT
    FROM
        RACK_MST_/*$domainId*/ R
    WHERE
        R.RACK_CODE=/*rackCode*/