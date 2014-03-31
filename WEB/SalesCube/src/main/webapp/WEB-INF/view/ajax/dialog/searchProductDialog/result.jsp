<table style="width: 750px;">
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

<div id="${dialogId}Div"
	style="border: none; width: 730px; height: 220px; overflow: hidden;">
<table class="dialog_resultList"  id="${dialogId}List" summary="商品検索結果" style="width: 100%;">
	<colgroup>
		<col span="1" style="width: 30%">
		<col span="1" style="width: 50%">
		<col span="1" style="width: 20%">
	</colgroup>
	<tr>
		<th>商品コード</th>
		<th>商品名</th>
		<th>仕入先名</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td>
				<a href="javascript:void(0)"  tabindex="5100" style="color: #1D9CCC" onclick="_selectLinkSearchResultAjax( '${dialogId}', '${f:h(bean.productCode)}', ProductParams, 'productCode' );
				$('#${dialogId}').dialog('close');" >${f:h(bean.productCode)}</a>
			</td>
			<td>${f:h(bean.productName)}</td>
			<td>${f:h(bean.supplierName)}</td>
		</tr>
	</c:forEach>
</table>
</div>
