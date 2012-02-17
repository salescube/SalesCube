DELETE FROM ONLINE_ORDER_WORK_/*$domainId*/
/*BEGIN*/
WHERE
	/*IF userId != null */
	AND USER_ID=/*userId*/'S'
	/*END*/
	/*IF onlineOrderId != null */
	AND ONLINE_ORDER_ID=/*onlineOrderId*/'S'
	/*END*/
	/*IF onlineItemId != null */
	AND ONLINE_ITEM_ID=/*onlineItemId*/'S'
	/*END*/
/*END*/
