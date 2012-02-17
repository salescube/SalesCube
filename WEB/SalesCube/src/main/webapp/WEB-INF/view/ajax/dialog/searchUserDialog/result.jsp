<table style="width: 650px;">
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
	style="padding: 0px; border: none; width: 650px; height: 240px; overflow: hidden">
<table id="${dialogId}List" summary="担当者検索結果" style="width: 640px;">
	<colgroup>
		<col span="1" style="width: 64px"><col span="1" style="width: 128px"><col span="1" style="width: 224px"><col span="1" style="width: 224px">
	</colgroup>
	<tr>
		<th>&nbsp;</th><th>担当者コード</th><th>担当者名</th><th>部門</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td style="text-align: center;"><input type="radio" name="${dialogId}_selectedUserId" value="${f:h(bean.userId)}"
				tabindex="9100" onclick="$('#${dialogId}_selectButton').attr('disabled', false);">
			</td>
			<td>${f:h(bean.userId)}</td><td>${f:h(bean.nameKnj)}</td><td>${f:h(bean.deptName)}</td>
		</tr>
	</c:forEach>
</table>
</div>

<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<input type="hidden" id="${dialogId}_${f:h(bean.userId)}_userId" name="${dialogId}_${f:h(bean.userId)}_userId" value="${f:h(bean.userId)}"><input type="hidden" id="${dialogId}_${f:h(bean.userId)}_nameKnj" name="${dialogId}_${f:h(bean.userId)}_nameKnj" value="${f:h(bean.nameKnj)}"><input type="hidden" id="${dialogId}_${f:h(bean.userId)}_nameKana" name="${dialogId}_${f:h(bean.userId)}_nameKana" value="${f:h(bean.nameKana)}"><input type="hidden" id="${dialogId}_${f:h(bean.userId)}_deptId" name="${dialogId}_${f:h(bean.userId)}_deptId" value="${f:h(bean.deptId)}"><input type="hidden" id="${dialogId}_${f:h(bean.userId)}_deptName" name="${dialogId}_${f:h(bean.userId)}_deptName" value="${f:h(bean.deptName)}"><input type="hidden" id="${dialogId}_${f:h(bean.userId)}_email" name="${dialogId}_${f:h(bean.userId)}_email" value="${f:h(bean.email)}"><input type="hidden" id="${dialogId}_${f:h(bean.userId)}_expireDate" name="${dialogId}_${f:h(bean.userId)}_expireDate" value="${f:h(bean.expireDate)}">
</c:forEach>