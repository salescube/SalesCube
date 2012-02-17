<table style="width: 750px;">
	<tr>
		<td style="text-align: left;">検索結果件数: ${searchResultCount}件</td>
		<td style="text-align: right;">
			<span style="color: red">
			 	<html:messages id="resultThreshold" message="true">
			 		<bean:write name="resultThreshold" filter="false"/>
			 	</html:messages>
			</span>
		</td>
	</tr>
</table>

<div id="${dialogId}Div"
	style="padding: 0px; border: none; width: 750px; height: 220px; overflow: hidden;">
<table id="${dialogId}List" summary="商品検索結果" style="width: 740px;">
	<colgroup>
		<col span="1" style="width: 10%">
		<col span="1" style="width: 25%">
		<col span="1" style="width: 45%">
		<col span="1" style="width: 20%">
	</colgroup>
	<tr>
		<th>&nbsp;</th>
		<th>商品コード</th>
		<th>商品名</th>
		<th>長さ</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td style="text-align: center;"><input type="radio"
				name="${dialogId}_selectedProduct" value="${f:h(bean.productCode)}"
				tabindex="5100"
				onclick="$('#${dialogId}_selectButton').attr('disabled', false);">
			</td>
			<td>${f:h(bean.productCode)}</td>
			<td>${f:h(bean.productName)}</td>
			<td>${f:h(bean.length)}${f:h(bean.lengthUnitSizeCategoryName)}</td>
		</tr>
	</c:forEach>
</table>
</div>
