<div id="${dialogId}" title="商品在庫情報" style="display: none;">
	<span id="listContainer">
		<%@ include file="/WEB-INF/view/stock/dispProductStockList/dispStockInfo.jsp" %>
	</span>
	<div style="width: 700px; text-align: right">
		<button type="button" style="width: 70px" tabindex="11000"
			onclick="$('#${dialogId}').dialog('close');">閉じる</button>
	</div>
</div>