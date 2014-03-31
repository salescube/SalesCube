<table id="search_result" summary="検索結果" class="forms detail_info" style="table-layout: auto; margin-top: 10px;">
	<colgroup>
		<c:forEach var="colInfo" items="${columnInfoList}" varStatus="status">
			<col span="1" style="min-width: ${f:h(colInfo.colWidth)}px">
		</c:forEach>
	</colgroup>
	<thead>
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
					<c:if test='${outputExcel}'>
						${f:h(value)}
					</c:if>
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
	</thead>
	<tbody>
		<c:forEach var="rowData" items="${searchResultList}" varStatus="statusRow">
			<tr>
				<c:forEach var="value" items="${rowData}" varStatus="statusCol">
					<c:choose>
						<c:when test='${columnInfoList[statusCol.index].itemId=="slipId"}'>
							<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">&nbsp;
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
								</c:if>&nbsp;
							</td>
						</c:when>
						<c:when test='${columnInfoList[statusCol.index].itemId=="srcFunc"}'>
							<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
								&nbsp;${f:h(value.srcFuncName)}&nbsp;
							</td>
						</c:when>
						<c:when test='${columnInfoList[statusCol.index].itemId=="eadSlipCategory"}'>
							<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
								&nbsp;${f:h(value.eadSlipCategoryName)}&nbsp;
							</td>
						</c:when>
						<c:when test='${columnInfoList[statusCol.index].itemId=="eadCategory"}'>
							<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">
								&nbsp;${f:h(value.eadCategoryName)}&nbsp;
							</td>
						</c:when>
						<c:when test='${columnInfoList[statusCol.index].itemId=="rackCode"}'>
							<td class="xl70" style="text-align: ${columnInfoList[statusCol.index].textAlign}">&nbsp;
								<c:if test='${value.srcFunc!=CONST_STOCK_TRANSFER}'>
									${f:h(value.rackCode)}
								</c:if>
								<c:if test='${value.srcFunc==CONST_STOCK_TRANSFER}'>
									<bean:message key='labels.src'/>${f:h(value.rackCode)}<br>
									<bean:message key='labels.dest'/>${f:h(value.rackCodeMove)}
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
