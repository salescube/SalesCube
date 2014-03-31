<table id="search_result" summary="searchResult" class="forms detail_info" style="table-layout: auto; margin-top: 20px;">
	<colgroup>
		<c:forEach var="colInfo" items="${columnInfoList}" varStatus="status">
			<col span="1" style="min-width: ${f:h(colInfo.colWidth)}px">
		</c:forEach>
	</colgroup>
	<tr>
		<c:forEach var="colInfo" items="${columnInfoList}" varStatus="status">
			<logic:equal name="colInfo" property="sortFlag" value="1">
				<c:choose>
					<c:when test="${status.first}" >
						<th class="rd_top_left"  style='cursor: pointer; height:30px;' onclick="sort('${f:h(colInfo.itemId)}')">
					</c:when>
					<c:when test="${status.last}" >
						<th class="rd_top_right"  style='cursor: pointer; height:30px;' onclick="sort('${f:h(colInfo.itemId)}')">
					</c:when>
					<c:otherwise>
						<th class=xl64  style='cursor: pointer; height:30px;' onclick="sort('${f:h(colInfo.itemId)}')">
					</c:otherwise>
				</c:choose>
					${f:h(colInfo.itemName)}
					<c:if test='${!outputExcel}'>
						<logic:equal name="colInfo" property="itemId" value="${sortColumn}">
							<span style="color: white">
							<logic:equal name="sortOrderAsc" value="true"><bean:message key='labels.asc'/></logic:equal>
							<logic:equal name="sortOrderAsc" value="false"><bean:message key='labels.desc'/></logic:equal>
							</span>
						</logic:equal>
					</c:if>
				</th>
			</logic:equal>
			<logic:equal name="colInfo" property="sortFlag" value="0">
				<c:choose>
					<c:when test="${status.first}" >
						<th class="rd_top_left"  style="height:30px;">${f:h(colInfo.itemName)}</th>
					</c:when>
					<c:when test="${status.last}" >
						<th class="rd_top_right" style="height:30px;">${f:h(colInfo.itemName)}</th>
					</c:when>
					<c:otherwise>
						<th class=xl64  style="height:30px;">${f:h(colInfo.itemName)}</th>
					</c:otherwise>
				</c:choose>
			</logic:equal>
		</c:forEach>
	</tr>

	<c:forEach var="rowData" items="${searchResultList}" varStatus="status">
		<c:if test="${rowData['doa'] == '1'}">
			<c:set var="doaCol" value="" />
			<c:set var="doaClass" value="" />
		</c:if>
		<c:if test="${rowData['doa'] == '2'}">
			<c:set var="doaCol" value="background-color: #C0C0C0" />
			<c:set var="doaClass" value="g" />
		</c:if>

		<tr>
			<c:forEach var="value" items="${columnInfoList}" varStatus="statusCol">
					<c:choose>
						<c:when test="${rowData[value.itemId] == null}">
							<td class="xl70${doaClass}" style="text-align: ${columnInfoList[statusCol.index].textAlign}; ${doaCol}">
								&nbsp;
							</td>
						</c:when>
						<c:when test="${columnInfoList[statusCol.index].itemId == 'billId'}">
							<c:forTokens var="divId" items="${rowData[value.itemId]}" delims="-" varStatus="divStatus" >
								<c:if test="${divStatus.index == 0}" >
									<c:set var="divDIspId" value="${divId}" />
								</c:if>
								<c:if test="${divStatus.index == 1}" >
									<c:set var="divBillId" value="${divId}" />
								</c:if>
							</c:forTokens>
							<td class="xl70${doaClass}" style="text-align: ${columnInfoList[statusCol.index].textAlign}; ${doaCol}">
								<c:if test='${outputExcel}'>
									&nbsp;${f:h(divDIspId)}&nbsp;
								</c:if>
								<c:if test='${!outputExcel}'>
									<c:if test="${isInputDepositValid}" >
										<bean:define id="concatUrl" value="/deposit/inputDeposit/inputByBill/?inputBillId=${divBillId}" />
										<a href="javascript:location.doHref('${f:url(concatUrl)}')" tabindex="1000">${f:h(divDIspId)}</a>
									</c:if>
									<c:if test="${!isInputDepositValid}" >
										&nbsp;${f:h(divDIspId)}&nbsp;
									</c:if>
								</c:if>
							</td>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${columnInfoList[statusCol.index].formatType == 1}">
									<td class="xl65${doaClass}" style="text-align: ${columnInfoList[statusCol.index].textAlign}; ${doaCol}">
										&nbsp;<fmt:formatNumber value="${rowData[value.itemId]}" pattern="###,###" />&nbsp;
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 2}">
									<td class="xl66${doaClass}" style="text-align: ${columnInfoList[statusCol.index].textAlign}; ${doaCol}">
										&nbsp;<fmt:formatNumber value="${rowData[value.itemId]}" pattern="${mineDto.unitPriceDecAlignFormat}" />&nbsp;
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 3}">
									<td class="xl67${doaClass}" style="text-align: ${columnInfoList[statusCol.index].textAlign}; ${doaCol}">
										&nbsp;<fmt:formatNumber value="${rowData[value.itemId]}" pattern="${mineDto.numDecAlignFormat}" />&nbsp;
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 4}">
									<td class="xl71${doaClass}" style="text-align: ${columnInfoList[statusCol.index].textAlign}; ${doaCol}">
										&nbsp;<fmt:formatNumber value="${rowData[value.itemId]}" pattern="${mineDto.statsDecAlignFormat}" />&nbsp;
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 10}">
									<td class="xl68${doaClass}" style="text-align: ${columnInfoList[statusCol.index].textAlign}; ${doaCol}">
 										&nbsp;<fmt:formatDate value="${rowData[value.itemId]}" pattern="yyyy/MM/dd" />&nbsp;
 									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 11}">
									<td class="xl69${doaClass}" style="text-align: ${columnInfoList[statusCol.index].textAlign}; ${doaCol}">
										&nbsp;<fmt:formatDate value="${rowData[value.itemId]}" pattern="yyyy/MM/dd HH:mm:ss.S" />&nbsp;
									</td>
								</c:when>
								<c:otherwise>
									<td class="xl70${doaClass}" style="text-align: ${columnInfoList[statusCol.index].textAlign}; ${doaCol}">
										&nbsp;${f:h(rowData[value.itemId])}&nbsp;
									</td>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
			</c:forEach>
		</tr>
	</c:forEach>
</table>
