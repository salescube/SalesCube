<table style="width: 550px;">
	<tr>
		<td style="text-align: left;">検索結果件数: ${searchResultCount}件</td>
		<td style="text-align: right; white-space: normal;">
			<span style="color: red">
			 	<html:messages id="resultThreshold" message="true">
			 		<bean:write name="resultThreshold" filter="false"/>
			 	</html:messages>
			</span>
		</td>
	</tr>
</table>

<div id="${dialogId}_PORDER_ListContainer">
	<div style="padding: 0px; border: none; width: 550px; height: 250px; overflow: hidden;">
		<table id="${dialogId}_PORDER_List" summary="伝票検索結果" style="width: 540px;">
			<colgroup>
				<col span="1" style="width: 5%">
				<col span="1" style="width: 15%">
				<col span="1" style="width: 30%">
				<col span="1" style="width: 15%">
				<col span="1" style="width: 30%">
				<col span="1" style="width: 5%">
			</colgroup>
			<tr>
				<th>&nbsp;</th>
				<th>発注番号</th>
				<th>発注日</th>
				<th>仕入先コード</th>
				<th>仕入先名</th>
				<th>仕入済</th>
			</tr>
			<c:forEach var="bean" items="${searchResultList}" varStatus="status">
			<tr>
				<td style="text-align: center;">
					<input type="radio" name="${dialogId}_PORDER_selectedSlip" value="${bean.poSlipId}"
					tabindex="14100" onclick="$('#${dialogId}_copyButton').attr('disabled', false);">
				</td>
				<td style="text-align: center;">${bean.poSlipId}</td>
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
</div>