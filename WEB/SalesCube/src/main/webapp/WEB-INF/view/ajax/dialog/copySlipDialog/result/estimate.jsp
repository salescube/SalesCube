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
			<col span="1" style="width: 15%">
			<col span="1" style="width: 15%">
			<col span="1" style="width: 25%">
			<col span="1" style="width: 45%">
		</colgroup>
		<tr>
			<th>見積番号</th>
			<th>見積日</th>
			<th>提出先名</th>
			<th>件名</th>
		</tr>
		<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td style="text-align: center;">
				<a href="javascript:void(0)"  tabindex="14100" style="color: #1D9CCC" onclick="_copyLinkSlip('${dialogId}', $('#${dialogId}_slipName').val(),'${bean.estimateSheetId}');
				$('#${dialogId}').dialog('close');" >${bean.estimateSheetId}</a>
			</td>
			<td style="text-align: center;">${bean.estimateDate}</td>
			<td style="white-space: normal;">${f:h(bean.submitName)}</td>
			<td style="white-space: normal;">${f:h(bean.title)}</td>
		</tr>
		</c:forEach>
	</table>
</div>