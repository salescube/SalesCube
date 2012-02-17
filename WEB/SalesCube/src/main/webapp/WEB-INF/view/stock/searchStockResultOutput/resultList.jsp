<table id="search_result" summary="検索結果" class="forms" style="table-layout: auto;">
	<colgroup>
		<c:forEach var="colInfo" items="${columnInfoList}" varStatus="status">
			<col span="1" style="min-width: ${f:h(colInfo.colWidth)}px">
		</c:forEach>
	</colgroup>
	<thead>
		<tr>
			<c:forEach var="colInfo" items="${columnInfoList}" varStatus="status">
				<th class="xl64" id='result_${f:h(colInfo.itemId)}'
					<c:if test='${colInfo.sortFlag=="1"}'>
						style='cursor: pointer' onclick="sort('${f:h(colInfo.itemId)}')"
					</c:if>
				>
					${f:h(colInfo.itemName)}
					<c:if test='${!outputExcel}'>
						<span id="sortStatus_${f:h(colInfo.itemId)}" style="color: blue">
							<c:if test='${sortColumn==colInfo.itemId}'>
								<c:if test='${sortOrderAsc}'>
									<bean:message key='labels.asc'/>
								</c:if>
								<c:if test='${!sortOrderAsc}'>
									<bean:message key='labels.desc'/>
								</c:if>
							</c:if>
						</span>
					</c:if>
				</th>
			</c:forEach>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="rowData" items="${searchResultList}" varStatus="statusRow">
			<tr>
				<c:forEach var="value" items="${rowData}" varStatus="statusCol">
					<c:choose>
						<c:when test='${columnInfoList[statusCol.index].itemId=="slipId"}'>
							<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
								<c:if test='${!outputExcel&&value.menuValid}'>
									<a
										<c:choose>
											<c:when test="${value.srcFunc==CONST_SALES}">
												<bean:define id="concatUrl" value="${'/sales/inputSales/load/?salesSlipId='}${value.salesSlipId}" />
												href="javascript:doHref('${f:url(concatUrl)}');"
											</c:when>
											<c:when test="${value.srcFunc==CONST_PURCHASE}">
												<bean:define id="concatUrl" value="${'/purchase/inputPurchase/load/?supplierSlipId='}${value.supplierSlipId}" />
												href="javascript:doHref('${f:url(concatUrl)}');"
											</c:when>
											<c:when test="${value.srcFunc==CONST_STOCK}">
												<bean:define id="concatUrl" value="${'/stock/inputStock/load/?eadSlipId='}${value.eadSlipId}" />
												href="javascript:doHref('${f:url(concatUrl)}');"
											</c:when>
											<c:when test="${value.srcFunc==CONST_STOCK_TRANSFER}">
												<bean:define id="concatUrl" value="${'/stock/inputStockTransfer/load/?eadSlipId='}${value.eadSlipId}" />
												href="javascript:doHref('${f:url(concatUrl)}');"
											</c:when>
											<c:otherwise>
												href=""
											</c:otherwise>
										</c:choose>
									 tabindex="${statusRow.index+1000}">
										${f:h(value.slipId)}
									</a>
								</c:if>
								<c:if test='${outputExcel||!value.menuValid}'>
									${f:h(value.slipId)}
								</c:if>
							</td>
						</c:when>
						<c:when test='${columnInfoList[statusCol.index].itemId=="srcFunc"}'>
							<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
								${f:h(value.srcFuncName)}
							</td>
						</c:when>
						<c:when test='${columnInfoList[statusCol.index].itemId=="eadSlipCategory"}'>
							<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
								${f:h(value.eadSlipCategoryName)}
							</td>
						</c:when>
						<c:when test='${columnInfoList[statusCol.index].itemId=="eadCategory"}'>
							<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
								${f:h(value.eadCategoryName)}
							</td>
						</c:when>
						<c:when test='${columnInfoList[statusCol.index].itemId=="rackCode"}'>
							<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
								<c:if test='${value.srcFunc!=CONST_STOCK_TRANSFER}'>
									${f:h(value.rackCode)}
								</c:if>
								<c:if test='${value.srcFunc==CONST_STOCK_TRANSFER}'>
									<bean:message key='labels.src'/>${f:h(value.rackCode)}<br>
									<bean:message key='labels.dest'/>${f:h(value.rackCodeMove)}
								</c:if>
							</td>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${columnInfoList[statusCol.index].formatType == 1}">
									<td class="xl65" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										<fmt:formatNumber value="${value}" pattern="###,##0" />
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 2}">
									<td class="xl66" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										<fmt:formatNumber value="${value}" pattern="${mineDto.unitPriceDecAlignFormat}" />
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 3}">
									<td class="xl67" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										<fmt:formatNumber value="${value}" pattern="${mineDto.numDecAlignFormat}" />
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 10}">
									<td class="xl68" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
										<fmt:formatDate value="${value}" pattern="yyyy/MM/dd" />
									</td>
								</c:when>
								<c:when test="${columnInfoList[statusCol.index].formatType == 11}">
									<td  class="xl69"style="text-align: ${columnInfoList[statusCol.index].textAlign}">
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
	</tbody>
</table>
