<table style="width: 550px;">
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

<div
	style="padding: 0px; border: none; width: 550px; height: 220px; overflow: hidden;">
<table id="${dialogId}List" summary="倉庫検索結果" style="width: 540px;">
	<colgroup>
		<col span="1" style="width: 10%">
		<col span="1" style="width: 15%">
		<col span="1" style="width: 35%">
		<col span="1" style="width: 30%">
	</colgroup>
	<tr>
		<th>&nbsp;</th>
		<th>倉庫コード</th>
		<th>倉庫名</th>
		<th>倉庫状態</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td style="text-align: center;"><input type="radio"
				name="${dialogId}_selectedWarehouse" value="${f:h(bean.warehouseCode)}"
				tabindex="10100"
				onclick="$('#${dialogId}_selectButton').attr('disabled', false);">
			</td>
			<td>${f:h(bean.warehouseCode)}</td>
			<td>${f:h(bean.warehouseName)}</td>
			<td>${f:h(bean.warehouseState)}</td>
		</tr>
	</c:forEach>
</table>
</div>

<c:forEach var="bean" items="${searchResultList}" varStatus="status">

<input type="hidden" id="${dialogId}_${f:h(bean.warehouseCode)}_warehouseCode" name="${dialogId}_${f:h(bean.warehouseCode)}_warehouseCode" value="${f:h(bean.warehouseCode)}">
<input type="hidden" id="${dialogId}_${f:h(bean.warehouseCode)}_warehouseName" name="${dialogId}_${f:h(bean.warehouseCode)}_warehouseName" value="${f:h(bean.warehouseName)}">
<input type="hidden" id="${dialogId}_${f:h(bean.warehouseCode)}_warehouseState" name="${dialogId}_${f:h(bean.warehouseCode)}_warehouseState" value="${f:h(bean.warehouseState)}">
</c:forEach>