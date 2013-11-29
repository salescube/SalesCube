<table style="width: 600px;">
	<tr>
		<td style="text-align: left; color: #FFFFFF;">
			検索結果件数: ${searchResultCount}件
			<input type="hidden" id="${dialogId}_searchResultCount" name="${dialogId}_searchResultCount" value="${searchResultCount}"/>
		</td>
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
	style="border: none; width: 600px; height: 260px; overflow: hidden;">
<table class="dialog_resultList"  id="${dialogId}List" summary="住所検索結果" style="width: 100%;">
	<colgroup>
		<col span="1" style="width: 20%">
		<col span="1" style="width: 40%">
		<col span="1" style="width: 40%">
	</colgroup>
	<tr>
		<th>郵便番号</th>
		<th>住所１</th>
		<th>住所２</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
	<tr>
		<td><a href="javascript:void(0)"  tabindex="8100" style="color: #1D9CCC" onclick="_selectLinkSearchResult( '${dialogId}', '${f:h(bean.zipId)}');
				$('#${dialogId}').dialog('close');" >${f:h(bean.zipCode)}</a>
		</td>
		<td>${f:h(bean.zipAddress1)}</td>
		<td>${f:h(bean.zipAddress2)}</td>
	</tr>
	</c:forEach>
</table>
</div>

<c:forEach var="bean" items="${searchResultList}" varStatus="status">
	<%-- 顧客マスタの属性値をhiddenに保持する --%>
	<input type="hidden" id="${dialogId}_${f:h(bean.zipId)}_zipId" name="${dialogId}_${f:h(bean.zipId)}_zipId" value="${f:h(bean.zipId)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.zipId)}_zipCode" name="${dialogId}_${f:h(bean.zipId)}_zipCode" value="${f:h(bean.zipCode)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.zipId)}_zipAddress1" name="${dialogId}_${f:h(bean.zipId)}_zipAddress1" value="${f:h(bean.zipAddress1)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.zipId)}_zipAddress2" name="${dialogId}_${f:h(bean.zipId)}_zipAddress2" value="${f:h(bean.zipAddress2)}">
</c:forEach>