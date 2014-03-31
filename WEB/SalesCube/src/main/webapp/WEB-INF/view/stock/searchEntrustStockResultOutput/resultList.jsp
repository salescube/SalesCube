<table id="search_result" summary="検索結果" class="forms detail_info" style="table-layout: auto; margin-top: 10px;">
	<colgroup>
		<c:forEach var="colInfo" items="${columnInfoList}" varStatus="status">
			<col span="1" style="min-width: ${f:h(colInfo.colWidth)}px">
		</c:forEach>
	</colgroup>
	<thead>
		<tr>
			<c:forEach var="colInfo" items="${columnInfoList}" varStatus="status">
				<c:choose>
					<c:when test="${status.first}" >
						<th class="rd_top_left" id='result_${f:h(colInfo.itemId)}'
							<c:if test='${colInfo.sortFlag=="1"}'>
								style='cursor: pointer; height:30px;' onclick="sort('${f:h(colInfo.itemId)}')"
							</c:if>
						>
					</c:when>
					<c:when test="${status.last}" >
						<th class='rd_top_right' id='result_${f:h(colInfo.itemId)}'
							<c:if test='${colInfo.sortFlag=="1"}'>
								style='cursor: pointer; height:30px;' onclick="sort('${f:h(colInfo.itemId)}')"
							</c:if>
						>
					</c:when>
					<c:otherwise>
						<th class='xl64' id='result_${f:h(colInfo.itemId)}'
							<c:if test='${colInfo.sortFlag=="1"}'>
								style='cursor: pointer; height:30px;' onclick="sort('${f:h(colInfo.itemId)}')"
							</c:if>
						>
					</c:otherwise>
				</c:choose>
					${f:h(colInfo.itemName)}
					<span id="sortStatus_${f:h(colInfo.itemId)}" style="color: white">
						<c:if test='${sortColumn==colInfo.itemId}'>
							<c:if test='${!outputExcel && sortOrderAsc}'>
								<bean:message key='labels.asc'/>
							</c:if>
							<c:if test='${!outputExcel && !sortOrderAsc}'>
								<bean:message key='labels.desc'/>
							</c:if>
						</c:if>
					</span>
				</th>
			</c:forEach>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="rowData" items="${searchResultList}" varStatus="statusRow">
			<tr>
				<c:forEach var="value" items="${rowData}" varStatus="statusCol">
					<c:choose>

						<c:when test='${columnInfoList[statusCol.index].itemId=="entrustEadSlipId"}'>
							<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">&nbsp;
								<c:if test='${!outputExcel && isInputValid}'>
									<a
										<bean:define id="concatUrl" value="${'/stock/inputEntrustStock/load/?entrustEadSlipId='}${value.entrustEadSlipId}" />
										href="javascript:doHref('${f:url(concatUrl)}');"
									 	tabindex="${statusRow.index+1000}">
										${f:h(value.entrustEadSlipId)}
									</a>
								</c:if>
								<c:if test='${outputExcel||!isInputValid}'>
									${f:h(value.entrustEadSlipId)}
								</c:if>&nbsp;
							</td>
						</c:when>

						<c:when test='${columnInfoList[statusCol.index].itemId=="entrustEadLineIdNo"}'>
							<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">&nbsp;
								<c:if test='${!outputExcel && isInputValid}'>
									<a
										<bean:define id="concatUrl" value="${'/stock/inputEntrustStock/load/?entrustEadSlipId='}${value.entrustEadSlipId}" />
										href="javascript:doHref('${f:url(concatUrl)}');"
									 	tabindex="${statusRow.index+1000}">
										${f:h(value.entrustEadSlipId)} - ${f:h(value.lineNo)}
									</a>
								</c:if>
								<c:if test='${outputExcel||!isInputValid}'>
									${f:h(value.entrustEadSlipId)} - ${f:h(value.lineNo)}
								</c:if>&nbsp;
							</td>
						</c:when>

						<c:when test='${columnInfoList[statusCol.index].itemId=="entrustEadCategory"}'>
							<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
								&nbsp;${f:h(value.entrustEadCategoryName)}&nbsp;
							</td>
						</c:when>

						<c:when test='${columnInfoList[statusCol.index].itemId=="relEntrustEadSlipId"}'>
							<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">&nbsp;
								<c:if test='${!outputExcel && isInputValid}'>
									<a
										<bean:define id="concatUrl" value="${'/stock/inputEntrustStock/load/?entrustEadSlipId='}${value.relEntrustEadSlipId}" />
										href="javascript:doHref('${f:url(concatUrl)}');"
									 	tabindex="${statusRow.index+1000}">
										${f:h(value.relEntrustEadSlipId)}
									</a>
								</c:if>
								<c:if test='${outputExcel||!isInputValid}'>
									${f:h(value.relEntrustEadSlipId)}
								</c:if>&nbsp;
							</td>
						</c:when>

						<c:when test='${columnInfoList[statusCol.index].itemId=="poSlipId"}'>
							<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">&nbsp;
								<c:if test='${!outputExcel && isInputPOrderValid}'>
									<a
										<bean:define id="concatUrl" value="${'/porder/inputPOrder/load/?poSlipId='}${value.poSlipId}" />
										href="javascript:doHref('${f:url(concatUrl)}');"
									 	tabindex="${statusRow.index+1000}">
										${f:h(value.poSlipId)}
									</a>
								</c:if>
								<c:if test='${outputExcel||!isInputPOrderValid}'>
									${f:h(value.poSlipId)}
								</c:if>&nbsp;
							</td>
						</c:when>

						<c:when test='${columnInfoList[statusCol.index].itemId=="poLineIdNo"}'>
							<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">&nbsp;
								<c:if test='${!outputExcel && isInputPOrderValid}'>
									<a
										<bean:define id="concatUrl" value="${'/porder/inputPOrder/load/?poSlipId='}${value.poSlipId}" />
										href="javascript:doHref('${f:url(concatUrl)}');"
									 	tabindex="${statusRow.index+1000}">
										${f:h(value.poSlipId)} - ${f:h(value.poLineNo)}
									</a>
								</c:if>
								<c:if test='${outputExcel||!isInputPOrderValid}'>
									${f:h(value.poSlipId)} - ${f:h(value.poLineNo)}
								</c:if>&nbsp;
							</td>
						</c:when>

						<c:otherwise>
							<c:choose>
								<c:when test="${columnInfoList[statusCol.index].formatType == 1}">
									<td class="xl65" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										&nbsp;<fmt:formatNumber value="${value}" pattern="###,##0" />&nbsp;
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 2}">
									<td class="xl66" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										&nbsp;<fmt:formatNumber value="${value}" pattern="${mineDto.unitPriceDecAlignFormat}" />&nbsp;
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 3}">
									<td class="xl67" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										&nbsp;<fmt:formatNumber value="${value}" pattern="${mineDto.numDecAlignFormat}" />&nbsp;
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 10}">
									<td class="xl68" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										&nbsp;<fmt:formatDate value="${value}" pattern="yyyy/MM/dd" />&nbsp;
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 11}">
									<td  class="xl69"style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										&nbsp;<fmt:formatDate value="${value}" pattern="yyyy/MM/dd HH:mm:ss.S" />&nbsp;
									</td>
								</c:when>
								<c:otherwise>
									<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										&nbsp;${f:h(value)}&nbsp;
									</td>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</tr>
		</c:forEach>
	</tbody>
</table>
