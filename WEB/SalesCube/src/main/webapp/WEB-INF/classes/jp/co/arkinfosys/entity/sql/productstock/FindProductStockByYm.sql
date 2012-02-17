SELECT
        MAIN.PRODUCT_CODE
        ,MAIN.PRODUCT_NAME
        ,MAIN.RACK_CODE
        ,MAIN.SUPPLIER_PRICE_YEN
        ,MAIN.ALL_STOCK_NUM
FROM (
	SELECT
	        P.PRODUCT_CODE
	        ,P.PRODUCT_NAME
	        ,P.RACK_CODE
	        ,P.SUPPLIER_PRICE_YEN
	        ,IFNULL(SUM(PS.STOCK_NUM), 0) + (
	        						SELECT
	        							IFNULL( SUM( IF(ES.EAD_CATEGORY = '1', EL.QUANTITY, -EL.QUANTITY) ), 0 )
	        						FROM EAD_SLIP_TRN_/*$domainId*/ ES INNER JOIN EAD_LINE_TRN_/*$domainId*/ EL
	        							ON ES.EAD_SLIP_ID = EL.EAD_SLIP_ID
	        						WHERE
	        							P.PRODUCT_CODE = EL.PRODUCT_CODE
	        							AND EAD_DATE <= DATE_ADD( date(CONCAT(CAST( /*stockYm*/ +1 AS CHAR), '01')), INTERVAL -1 DAY)
	        							AND (
	        									-- 調べたい年月度の最終日時点で、在庫締されていない入出庫のみが検索対象
	        									(ES.STOCK_PDATE IS NULL)
	        									OR
	        									(ES.STOCK_PDATE > DATE_ADD( date(CONCAT(CAST( /*stockYm*/ +1 AS CHAR), '01')), INTERVAL -1 DAY) )
	        								)
	        					)
	        	AS ALL_STOCK_NUM
	    FROM
	    	PRODUCT_MST_/*$domainId*/ P
	    	LEFT OUTER JOIN (
	    		SELECT
	    			PS1.PRODUCT_CODE
	    			,PS1.STOCK_NUM
	    		FROM
			    	PRODUCT_STOCK_TRN_/*$domainId*/ PS1
			        INNER JOIN (
			            SELECT
			                    RACK_CODE
			                    ,PRODUCT_CODE
			                    ,MAX(STOCK_PDATE) AS LAST_PDATE
			                FROM
			                    PRODUCT_STOCK_TRN_/*$domainId*/ PS2
			                WHERE
								PS2.STOCK_YM <= /*stockYm*/
			                GROUP BY
			                    RACK_CODE
			                    ,PRODUCT_CODE
			        ) A
			            ON A.RACK_CODE = PS1.RACK_CODE
			            AND A.PRODUCT_CODE = PS1.PRODUCT_CODE
			            AND A.LAST_PDATE = PS1.STOCK_PDATE
			 ) PS
		    	ON P.PRODUCT_CODE = PS.PRODUCT_CODE

	WHERE
		P.STOCK_CTL_CATEGORY = '1'				-- 在庫管理する商品のみが検索対象
		AND P.PRODUCT_STOCK_CATEGORY = '1'		-- 分類保管=自社在庫
		AND P.PRODUCT_STANDARD_CATEGORY = '0'	-- 分類標準=標準品
	    AND P.SET_TYPE_CATEGORY <> /*setTypeCategory*/	-- セット商品フラグ=単品
	GROUP BY
	    P.PRODUCT_CODE
	    ,P.PRODUCT_NAME
	    ,P.RACK_CODE
	    ,P.SUPPLIER_PRICE_YEN
	ORDER BY
		P.PRODUCT_CODE
) MAIN
WHERE
	MAIN.ALL_STOCK_NUM <> 0