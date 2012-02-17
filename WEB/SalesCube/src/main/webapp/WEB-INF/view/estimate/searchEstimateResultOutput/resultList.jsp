<table id="search_result" summary="searchResult" class="forms" style="table-layout: auto;">
	<colgroup>
		<c:forEach var="colInfo" items="${columnInfoList}" varStatus="status">
			<col span="1" style="min-width: ${f:h(colInfo.colWidth)}px">
		</c:forEach>
	</colgroup>
	<tr>
		<c:forEach var="colInfo" items="${columnInfoList}" varStatus="status">
			<logic:equal name="colInfo" property="sortFlag" value="1">
				<th class=xl64 style='cursor: pointer' onclick="sort('${f:h(colInfo.itemId)}')">
					${f:h(colInfo.itemName)}
					<c:if test='${!outputExcel}'>
						<logic:equal name="colInfo" property="itemId" value="${sortColumn}">
							<span style="color: blue">
							<logic:equal name="sortOrderAsc" value="true"><bean:message key='labels.asc'/></logic:equal>
							<logic:equal name="sortOrderAsc" value="false"><bean:message key='labels.desc'/></logic:equal>
							</span>
						</logic:equal>
					</c:if>
				</th>
			</logic:equal>
			<logic:equal name="colInfo" property="sortFlag" value="0">
				<th class=xl64>${f:h(colInfo.itemName)}</th>
			</logic:equal>
		</c:forEach>
	</tr>
	<c:forEach var="rowData" items="${searchResultList}" varStatus="status">
		<tr>
			<c:forEach var="value" items="${rowData}" varStatus="statusCol">
					<c:choose>
						<c:when test="${value == null}">
							<td class=xl70 style="text-align: ${columnInfoList[statusCol.index].textAlign}">
								&nbsp
							</td>
						</c:when>
						<c:when test="${isInputValid && columnInfoList[statusCol.index].itemId == 'estimateSheetId'}">
							<td class=xl70 style="text-align: ${columnInfoList[statusCol.index].textAlign}">
								<c:if test='${outputExcel}'>
									${f:h(value)}
								</c:if>
								<c:if test='${!outputExcel}'>
									<input type="hidden" id="val${status.index}" value="${sw:u(value)}" />
									<bean:define id="concatUrl" value="${'/estimate/inputEstimate/load/?estimateSheetId='}${f:h(value)}" />
									<a href="javascript:window.location.doHref('${f:url(concatUrl)}');" tabindex="1000">${f:h(value)}</a>
								</c:if>
							</td>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${columnInfoList[statusCol.index].formatType == 1}">
									<td class=xl65 style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										<fmt:formatNumber value="${value}" pattern="###,##0" />
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 2}">
									<td class=xl66 style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										<fmt:formatNumber value="${value}" pattern="${mineDto.unitPriceDecAlignFormat}" />
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 3}">
									<td class=xl67 style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										<fmt:formatNumber value="${value}" pattern="${mineDto.numDecAlignFormat}" />
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 4}">
									<td class=xl71 style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										<fmt:formatNumber value="${value}" pattern="${mineDto.statsDecAlignFormat}" />
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 10}">
									<td class=xl68 style="text-align: ${columnInfoList[statusCol.index].textAlign}">
 										<fmt:formatDate value="${value}" pattern="yyyy/MM/dd" />
 									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 11}">
									<td class=xl69 style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										<fmt:formatDate value="${value}" pattern="yyyy/MM/dd HH:mm:ss.S" />
									</td>
								</c:when>
								<c:otherwise>
									<td class=xl70 style="text-align: ${columnInfoList[statusCol.index].textAlign}">
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
