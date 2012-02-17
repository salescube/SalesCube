INSERT INTO ONLINE_ORDER_WORK_/*$domainId*/
(
	USER_ID,
	ONLINE_ORDER_ID,
	ONLINE_ITEM_ID,
	SUPPLIER_DATE,
	PAYMENT_DATE,
	CUSTOMER_EMAIL,
	CUSTOMER_NAME,
	CUSTOMER_TEL,
	SKU,
	PRODUCT_NAME,
	QUANTITY,
	CURRENCY,
	PRICE,
	TAX_PRICE,
	SHIPPING_PRICE,
	SHIPPING_TAX,
	SHIP_SERVICE_LEVEL,
	RECIPIENT_NAME,
	ADDRESS_1,
	ADDRESS_2,
	ADDRESS_3,
	CITY,
	STATE,
	ZIP_CODE,
	COUNTRY,
	SHIP_TEL,
	DELIVERY_START_DATE,
	DELIVERY_END_DATE,
	DELIVERY_TIME_ZONE,
	DELIVERY_INST,
	LINE_NO,
	LOAD_DATE
)
VALUES
(
	'ALL',
	/*onlineOrderId*/,
	/*onlineItemId*/,
	/*IF supplierDate != "" */
	CAST(/*supplierDate*/ AS DATETIME),
	-- ELSE null,
	/*END*/
	/*IF paymentDate != "" */
	CAST(/*paymentDate*/ AS DATETIME),
	-- ELSE null,
	/*END*/
	/*customerEmail*/,
	/*customerName*/,
	/*customerTel*/,
	/*sku*/,
	/*productName*/,
	/*quantity*/,
	/*currency*/,
	/*price*/,
	/*taxPrice*/,
	/*shippingPrice*/,
	/*shippingTax*/,
	/*shipServiceLevel*/,
	/*recipientName*/,
	/*address1*/,
	/*address2*/,
	/*address3*/,
	/*city*/,
	/*state*/,
	/*zipCode*/,
	/*country*/,
	/*shipTel*/,
	/*IF deliveryStartDate != "" */
	CAST(/*deliveryStartDate*/ AS DATETIME),
	-- ELSE null,
	/*END*/
	/*IF deliveryEndDate != "" */
	CAST(/*deliveryEndDate*/ AS DATETIME),
	-- ELSE null,
	/*END*/
	/*deliveryTimeZone*/,
	/*deliveryInst*/,
	/*lineNo*/,
	now()
)
