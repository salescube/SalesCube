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

<div id="${dialogId}_PORDER_ListContainer"
	style="border: none; width: 600px; height: 230px; overflow: hidden;">
	<table class="dialog_resultList"  id="${dialogId}_PORDER_List" summary="伝票検索結果" style="width: 96%;">
		<colgroup>
			<col span="1" style="width: 18%">
			<col span="1" style="width: 30%">
			<col span="1" style="width: 17%">
			<col span="1" style="width: 30%">
			<col span="1" style="width: 5%">
		</colgroup>
		<tr>
			<th>発注番号</th>
			<th>発注日</th>
			<th>仕入先コード</th>
			<th>仕入先名</th>
			<th>仕入済</th>
		</tr>
		<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td style="text-align: center;">
				<a href="javascript:void(0)"  tabindex="14100" style="color: #1D9CCC" onclick="_copyLinkSlip('${dialogId}', $('#${dialogId}_slipName').val(),'${bean.poSlipId}');
				$('#${dialogId}').dialog('close');" >${bean.poSlipId}</a>
			</td>
			<td style="text-align: center;">${bean.poDate}</td>
			<td style="text-align: center;">${f:h(bean.supplierCode)}</td>
			<td style="white-space: normal">${f:h(bean.supplierName)}</td>
			<td style="text-align: center;">
				<c:if test="${bean.status}">済</c:if>
				<c:if test="${!bean.status}">未</c:if>
			</td>
		</tr>
		</c:forEach>
	</table>
</div>