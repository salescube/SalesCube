SELECT
		CATEGORY_CODE_NAME
    FROM
        CATEGORY_TRN_/*$domainId*/
    WHERE
        CATEGORY_ID=/*categoryId*/
        AND
        CATEGORY_CODE=/*categoryCode*/
