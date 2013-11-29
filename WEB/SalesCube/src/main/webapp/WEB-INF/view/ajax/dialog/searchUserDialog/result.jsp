<table style="width: 650px;">
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
	style="border: none; width: 650px; height: 220px; overflow: hidden;">
<table class="dialog_resultList"  id="${dialogId}List" summary="担当者検索結果" style="width: 100%;">
	<colgroup>
		<col span="1" style="width: 30%">
		<col span="1" style="width: 35%">
		<col span="1" style="width: 35%">
	</colgroup>
	<tr>
		<th>担当者コード</th>
		<th>担当者名</th>
		<th>部門</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td>
				<a href="javascript:void(0)"  tabindex="9100" style="color: #1D9CCC" onclick="_selectLinkSearchResult( '${dialogId}', '${f:h(bean.userId)}');
				$('#${dialogId}').dialog('close');" >${f:h(bean.userId)}</a>
			</td>
			<td>${f:h(bean.nameKnj)}</td>
			<td>${f:h(bean.deptName)}</td>
		</tr>
	</c:forEach>
</table>
</div>

<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<input type="hidden" id="${dialogId}_${f:h(bean.userId)}_userId" name="${dialogId}_${f:h(bean.userId)}_userId" value="${f:h(bean.userId)}"><input type="hidden" id="${dialogId}_${f:h(bean.userId)}_nameKnj" name="${dialogId}_${f:h(bean.userId)}_nameKnj" value="${f:h(bean.nameKnj)}"><input type="hidden" id="${dialogId}_${f:h(bean.userId)}_nameKana" name="${dialogId}_${f:h(bean.userId)}_nameKana" value="${f:h(bean.nameKana)}"><input type="hidden" id="${dialogId}_${f:h(bean.userId)}_deptId" name="${dialogId}_${f:h(bean.userId)}_deptId" value="${f:h(bean.deptId)}"><input type="hidden" id="${dialogId}_${f:h(bean.userId)}_deptName" name="${dialogId}_${f:h(bean.userId)}_deptName" value="${f:h(bean.deptName)}"><input type="hidden" id="${dialogId}_${f:h(bean.userId)}_email" name="${dialogId}_${f:h(bean.userId)}_email" value="${f:h(bean.email)}"><input type="hidden" id="${dialogId}_${f:h(bean.userId)}_expireDate" name="${dialogId}_${f:h(bean.userId)}_expireDate" value="${f:h(bean.expireDate)}">
</c:forEach>