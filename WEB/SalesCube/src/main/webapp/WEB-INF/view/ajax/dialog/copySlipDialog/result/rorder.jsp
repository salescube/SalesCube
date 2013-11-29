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

<div id="${dialogId}_RORDER_ListContainer"
	style="border: none; width: 600px; height: 230px; overflow: hidden;">
	<table class="dialog_resultList"  id="${dialogId}_RORDER_List" summary="伝票検索結果" style="width: 96%;">
		<colgroup>
			<col span="1" style="width: 18%">
			<col span="1" style="width: 15%">
			<col span="1" style="width: 17%">
			<col span="1" style="width: 50%">
		</colgroup>
		<tr>
			<th>受注番号</th>
			<th>受注日</th>
			<th>顧客コード</th>
			<th>顧客名</th>
		</tr>
		<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td style="text-align: center;">
				<a href="javascript:void(0)"  tabindex="14100" style="color: #1D9CCC" onclick="_copyLinkSlip('${dialogId}', $('#${dialogId}_slipName').val(),'${bean.roSlipId}');
				$('#${dialogId}').dialog('close');" >${bean.roSlipId}</a>
			</td>
			<td style="text-align: center;">${bean.roDate}</td>
			<td style="text-align: center;">${f:h(bean.customerCode)}</td>
			<td style="white-space: normal;">${f:h(bean.customerName)}</td>
		</tr>
		</c:forEach>
	</table>
</div>