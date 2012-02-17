<table id="header" summary="header" class="forms" style="table-layout: fixed;">
	<c:forEach var="item" items="${headerList}" varStatus="status">
		<tr>
			<c:forEach var="colInfo" items="${columnInfoList}" varStatus="internalStatus">
				<c:choose>
					<c:when test="${internalStatus.index == 0}">
						<th class="xl72" style="text-align: right;">${f:h(item.label)}</th>
					</c:when>
					<c:when test="${internalStatus.index == 1}">
						<td class="xl72" style="text-align: left;">${f:h(item.value)}</td>
					</c:when>
					<c:when test="${status.first && internalStatus.last}">
						<td class="xl72" style="text-align: right;">${f:h(exceloutputdate)}</td>
					</c:when>
					<c:otherwise>
						<td></td>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</tr>
	</c:forEach>
</table>
<BR>
<table id="search_result" summary="searchResult" class="forms" style="table-layout: auto;">
	<colgroup>
		<c:forEach var="colInfo" items="${columnInfoList}" varStatus="status">
			<col span="1" style="min-width: ${f:h(colInfo.colWidth)}px">
		</c:forEach>
	</colgroup>
	<tr>
		<c:forEach var="colInfo" items="${columnInfoList}" varStatus="status">
			<th class="xl64" style="text-align: center;">${f:h(colInfo.itemName)}</th>
		</c:forEach>
	</tr>

	<c:forEach var="rowData" items="${searchResultList}" varStatus="status">
		<tr>
			<c:forEach var="value" items="${rowData}" varStatus="statusCol">
					<c:choose>
						<c:when test="${value == null}">
							<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
								&nbsp;
							</td>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${columnInfoList[statusCol.index].formatType == 1}">
									<td class="xl65" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										<fmt:formatNumber value="${value}" pattern="\\###,##0" />
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 2}">
									<td class="xl66" style="text-align: ${columnInfoList[statusCol.index].textAlign};mso-number-format:'\0022${f:h(sign)}\0022\#\,\#\#0.00_ ';">
										${f:h(value)}
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 3}">
									<td class="xl67" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										<fmt:formatNumber value="${value}" pattern="###,##0" />
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 4}">
									<td class="xl71" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										${f:h(value)}
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 10}">
									<td class="xl68" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
 										<fmt:formatDate value="${value}" pattern="yyyy/MM/dd" />
 									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 11}">
									<td class="xl69" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										<fmt:formatDate value="${value}" pattern="yyyy/MM/dd HH:mm:ss.S" />
									</td>
								</c:when>
								<c:otherwise>
									<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										${f:h(value)}
									</td>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
			</c:forEach>
		</tr>
	</c:forEach>
</table>
