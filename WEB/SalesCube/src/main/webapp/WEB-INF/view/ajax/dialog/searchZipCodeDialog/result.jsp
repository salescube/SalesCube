<table style="width: 600px;">
	<tr>
		<td style="text-align: left;">
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

<div style="padding: 0px; border: none; width: 600px; height: 280px; overflow: hidden;">
	<table id="${dialogId}List" summary="住所検索結果" style="width: 590px">
		<colgroup>
			<col span="1" style="width: 10%">
			<col span="1" style="width: 15%">
			<col span="1" style="width: 40%">
			<col span="1" style="width: 35%">
		</colgroup>
		<tr>
			<th>&nbsp;</th>
			<th>郵便番号</th>
			<th>住所１</th>
			<th>住所２</th>
		</tr>
		<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td style="text-align: center;"><input type="radio"
				name="${dialogId}_selectedZipId" value="${f:h(bean.zipId)}"
				tabindex="8100"
				onclick="$('#${dialogId}_selectButton').attr('disabled', false);">
			</td>
			<td>${f:h(bean.zipCode)}</td>
			<td>${f:h(bean.zipAddress1)}</td>
			<td>${f:h(bean.zipAddress2)}</td>
		</tr>
		</c:forEach>
</table>
</div>

<c:forEach var="bean" items="${searchResultList}" varStatus="status">
	
	<input type="hidden" id="${dialogId}_${f:h(bean.zipId)}_zipId" name="${dialogId}_${f:h(bean.zipId)}_zipId" value="${f:h(bean.zipId)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.zipId)}_zipCode" name="${dialogId}_${f:h(bean.zipId)}_zipCode" value="${f:h(bean.zipCode)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.zipId)}_zipAddress1" name="${dialogId}_${f:h(bean.zipId)}_zipAddress1" value="${f:h(bean.zipAddress1)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.zipId)}_zipAddress2" name="${dialogId}_${f:h(bean.zipId)}_zipAddress2" value="${f:h(bean.zipAddress2)}">
</c:forEach>