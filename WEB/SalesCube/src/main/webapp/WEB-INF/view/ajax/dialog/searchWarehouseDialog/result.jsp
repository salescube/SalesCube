<table style="width: 550px;">
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

<div id="${dialogId}ListContainer"
	style="border: none; width: 550px; height: 220px; overflow: hidden;">
<table class="dialog_resultList"  id="${dialogId}List" summary="倉庫検索結果" style="width: 100%;">
	<colgroup>
		<col span="1" style="width: 30%">
		<col span="1" style="width: 40%">
		<col span="1" style="width: 30%">
	</colgroup>
	<tr>
		<th>倉庫コード</th>
		<th>倉庫名</th>
		<th>倉庫状態</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td><a href="javascript:void(0)"  tabindex="10100" style="color: #1D9CCC" onclick="_selectLinkSearchResult( '${dialogId}', '${f:h(bean.warehouseCode)}');
				$('#${dialogId}').dialog('close');" >${f:h(bean.warehouseCode)}</a>
			</td>
			<td>${f:h(bean.warehouseName)}</td>
			<td>${f:h(bean.warehouseState)}</td>
		</tr>
	</c:forEach>
</table>
</div>

<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<%-- 倉庫マスタの属性値をhiddenに保持する --%>
<input type="hidden" id="${dialogId}_${f:h(bean.warehouseCode)}_warehouseCode" name="${dialogId}_${f:h(bean.warehouseCode)}_warehouseCode" value="${f:h(bean.warehouseCode)}">
<input type="hidden" id="${dialogId}_${f:h(bean.warehouseCode)}_warehouseName" name="${dialogId}_${f:h(bean.warehouseCode)}_warehouseName" value="${f:h(bean.warehouseName)}">
<input type="hidden" id="${dialogId}_${f:h(bean.warehouseCode)}_warehouseState" name="${dialogId}_${f:h(bean.warehouseCode)}_warehouseState" value="${f:h(bean.warehouseState)}">
</c:forEach>