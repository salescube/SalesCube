<div id="${dialogId}" title="商品在庫情報" style="display: none; background-color: #FFFFFF;">
	<div style="padding: 20px 20px 0 20px; color: #000000;">
		<span id="listContainer">
			<%@ include file="/WEB-INF/view/stock/dispProductStockList/dispStockInfo.jsp" %>
		</span>
	</div>
	<div style="width: 96%; text-align: right; margin-top: 20px;">
		<button type="button" style="width: 70px" tabindex="11000"
			onclick="$('#${dialogId}').dialog('close');">閉じる</button>
	</div>
</div>