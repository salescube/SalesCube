<table style="width: 500px;">
	<tr>
		<td style="text-align: left; color: #FFFFFF;">検索結果件数: ${searchResultCount}件</td>
		<td style="text-align: right;">
			<span style="color: red">
			 	<html:messages id="resultThreshold" message="true">
			 		<bean:write name="resultThreshold" filter="false"/>
			 	</html:messages>
			</span>
		</td>
	</tr>
</table>

<script type="text/javascript">
<!--
// 数量割引詳細を表示する
function showBalloon(id, text) {
	$(".discount" + id).showBalloon({
		contents: text, 
		position: "right",
		minLifetime: 0,
		offsetX: -40,
		css: {
			backgroundColor: '#fff',
			opacity: '0.99'
		}
	});
	return;
}

//数量割引詳細を非表示にする
function hideBalloon(id) {
	$(".discount" + id).showBalloon().hideBalloon();
	return;
}
-->
</script>

<div id="${dialogId}ListContainer"
	style="border: none; width: 780px; height: 220px; overflow: hidden;">
<table class="dialog_resultList"  id="${dialogId}List" summary="数量割引検索結果" style="width: 99%;">
	<colgroup>
		<col span="1" style="width: 5%">
		<col span="1" style="width: 20%">
		<col span="1" style="width: 65%">
		<col span="1" style="width: 10%">
	</colgroup>
	<tr>
		<th>割引詳細</th>
		<th>割引コード</th>
		<th>割引名</th>
		<th>割引有効</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td style="text-align: center; width: 50px;">
				<div class="discount${f:h(bean.discountId)}"
					onmouseover="showBalloon('${f:h(bean.discountId)}', _updateDiscountData('${dialogId}', 'selectedDiscount', '${f:h(bean.discountId)}'));" 
					onmouseout="hideBalloon('${f:h(bean.discountId)}');">
					<input type="image" name="fileIcon" src="${f:url('/images//customize/file.png')}" style="height:30px; width:30px; margin:0; padding:0;"/>
				</div>
			</td>
			<td>
				<a href="javascript:void(0)"  tabindex="16200" style="color: #1D9CCC" 
					onclick="_selectLinkSearchResult( '${dialogId}', '${f:h(bean.discountId)}');
					$('#${dialogId}').dialog('close');" >${f:h(bean.discountId)}
				</a>
			</td>
			<td>${f:h(bean.discountName)}</td>
			<td>${f:h(bean.useFlagName)}</td>
		</tr>
	</c:forEach>
</table>
</div>

<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<%-- 数量割引マスタの属性値をhiddenに保持する --%>
<input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_discountId" name="${dialogId}_${f:h(bean.discountId)}_discountId" value="${f:h(bean.discountId)}">
<input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_discountName" name="${dialogId}_${f:h(bean.discountId)}_discountName" value="${f:h(bean.discountName)}">
<input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_remarks" name="${dialogId}_${f:h(bean.discountId)}_remarks" value="${f:h(bean.remarks)}">
<input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_useFlag" name="${dialogId}_${f:h(bean.discountId)}_useFlag" value="${f:h(bean.useFlag)}">
<input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_useFlagName" name="${dialogId}_${f:h(bean.discountId)}_useFlagName" value="${f:h(bean.useFlagName)}">

<c:forEach var="trn" items="${bean.discountTrnList}" varStatus="status2">
	<input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_discountDataId" name="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_discountDataId" value="${f:h(trn.discountDataId)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_lineNo" name="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_lineNo" value="${f:h(trn.lineNo)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_dataFrom" name="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_dataFrom" value="${f:h(trn.dataFrom)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_dataTo" name="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_dataTo" value="${f:h(trn.dataTo)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_discountRate" name="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_discountRate" value="${f:h(trn.discountRate)}">
</c:forEach>
</c:forEach>