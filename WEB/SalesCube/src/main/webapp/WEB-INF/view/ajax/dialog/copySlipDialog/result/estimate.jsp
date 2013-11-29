<table style="width: 600px;">
	<tr>
		<td style="text-align: left; color: #FFFFFF;">検索結果件数: ${searchResultCount}件</td>
		<td style="text-align: right; white-space: normal;">
			<span style="color: red">
			 	<html:messages id="resultThreshold" message="true">
			 		<bean:write name="resultThreshold" filter="false"/>
			 	</html:messages>
			</span>
		</td>
	</tr>
</table>

<div id="${dialogId}_ESTIMATE_ListContainer"
	style="border: none; width: 600px; height: 230px; overflow: hidden;">
	<table class="dialog_resultList"  id="${dialogId}_ESTIMATE_List" summary="伝票検索結果" style="width: 96%;">
		<colgroup>
			<col span="1" style="width: 5%">
			<col span="1" style="width: 15%">
			<col span="1" style="width: 15%">
			<col span="1" style="width: 15%">
			<col span="1" style="width: 50%">
		</colgroup>
		<tr>
			<th>&nbsp;</th>
			<th>見積番号</th>
			<th>見積日</th>
			<th>提出先名</th>
			<th>件名</th>
		</tr>
		<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td style="text-align: center;">
				<input type="radio" name="${dialogId}_ESTIMATE_selectedSlip" value="${bean.estimateSheetId}"
				tabindex="14100" onclick="$('#${dialogId}_copyButton').attr('disabled', false);">
			</td>
			<td style="text-align: center;">${bean.estimateSheetId}</td>
			<td style="text-align: center;">${bean.estimateDate}</td>
			<td style="white-space: normal;">${f:h(bean.submitName)}</td>
			<td style="white-space: normal;">${f:h(bean.title)}</td>
		</tr>
		</c:forEach>
	</table>
</div>