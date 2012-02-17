<div style="width: 910px; height: 25px;">
		<input type="hidden" id="searchResultCount" value="${f:h(searchResultCount)}">
		<input type="hidden" id="dispResultCount" value="${f:h(dispResultCount)}">
		<div style="position:absolute; left: 0px;"><a href="javascript:reset()" tabindex="200"><bean:message key='labels.inputResult'/> ${searchResultCount}<bean:message key='labels.searchResultCountUnit'/></a>／<a href="javascript:resetOK()" tabindex="200"><bean:message key='labels.deliverydeposit.insCount'/>${importOKCount}<bean:message key='labels.searchResultCountUnit'/></a>／<a href="javascript:resetListErr()" tabindex="202"><bean:message key='labels.deliverydeposit.errCount'/>${importNGCount}<bean:message key='labels.searchResultCountUnit'/></a>／<a href="javascript:resetListEtc()" tabindex="203"><bean:message key='labels.deliverydeposit.etcCount'/>${importEtcCount}<bean:message key='labels.searchResultCountUnit'/></a></div>
		<div style="position:absolute; width: 910px; text-align: center;">
			&nbsp;
		</div>
		<div style="position:absolute; right: 0px;">
			&nbsp;
		</div>
</div>
<span id="searchResultList">
<table summary="入金取込結果" class="forms" style="width: 910px;">
	<colgroup>
		<col span="1" style="width: 10%">
		<col span="1" style="width: 12%">
		<col span="1" style="width: 12%">
		<col span="1" style="width: 12%">
		<col span="1" style="width: 25%">
		<col span="1" style="width: 9%">
		<col span="1" style="width: 10%">
		<col span="1" style="width: 10%">
	</colgroup>
	<thead>
	<tr>
		<th style="cursor: pointer" onclick="sort('status');"><bean:message key='labels.slipStatus.status'/><!-- 状態 -->
			<span id="sortStatus_status" style="color: blue">
				<c:if test="${sortColumn == 'status'}">
					<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
					<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
				</c:if>
			</span>
		</th>
		<th style="cursor: pointer" onclick="sort('salesSlipId');"><bean:message key='labels.salesSlipId'/><!-- 売上番号 -->
			<span id="sortStatus_salesSlipId" style="color: blue">
				<c:if test="${sortColumn == 'salesSlipId'}">
					<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
					<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
				</c:if>
			</span>
		</th>
		<th style="cursor: pointer" onclick="sort('depositSlipId');"><bean:message key='labels.depositSlipId'/><!-- 入金番号 -->
			<span id="sortStatus_depositSlipId" style="color: blue">
				<c:if test="${sortColumn == 'depositSlipId'}">
					<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
					<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
				</c:if>
			</span>
		</th>
		<th style="cursor: pointer" onclick="sort('deliverySlipId');"><bean:message key='labels.delivery.slipid'/><!-- 伝票番号  -->
			<span id="sortStatus_deliverySlipId" style="color: blue">
				<c:if test="${sortColumn == 'deliverySlipId'}">
					<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
					<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
				</c:if>
			</span>
		</th>
		<th style="cursor: pointer" onclick="sort('customer');"><bean:message key='labels.customer'/><!-- 顧客 -->
			<span id="sortStatus_customer" style="color: blue">
				<c:if test="${sortColumn == 'customer'}">
					<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
					<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
				</c:if>
			</span>
		</th>
		<th style="cursor: pointer" onclick="sort('deliveryDate');"><bean:message key='labels.delivery.deliveryDate'/><!-- 発送日 -->
			<span id="sortStatus_deliveryDate" style="color: blue">
				<c:if test="${sortColumn == 'deliveryDate'}">
					<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
					<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
				</c:if>
			</span>
		</th>
		<th style="cursor: pointer" onclick="sort('productPrice');"><bean:message key='labels.delivery.productPrice'/><!-- 品代金 -->
			<span id="sortStatus_productPric" style="color: blue">
				<c:if test="${sortColumn == 'productPrice'}">
					<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
					<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
				</c:if>
			</span>
		</th>
		<th style="cursor: pointer" onclick="sort('salesMoney');"><bean:message key='labels.salesMoney'/><!-- 売上金額 -->
			<span id="sortStatus_salesMoney" style="color: blue">
				<c:if test="${sortColumn == 'salesMoney'}">
					<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
					<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
				</c:if>
			</span>
		</th>
	</tr>
	</thead>
		<tbody>
			<c:forEach var="rowData" items="${searchResultList}" varStatus="statusRow">
				<tr>
					<td style="text-align: left">
						${f:h(rowData.status)}
					</td>
					<td style="text-align: right">
					<c:if test="${linkInputSales == true}">
						<bean:define id="concatUrl" value="/sales/inputSales/load/?salesSlipId=${f:h(rowData.salesSlipId)}"/>
						<a href="javascript:location.doHref('${f:url(concatUrl)}')" tabindex="1000">${f:h(rowData.salesSlipId)}</a>
					</c:if>
					<c:if test="${linkInputSales == false}">
						${f:h(rowData.salesSlipId)}
					</c:if>
					</td>
					<td style="text-align: right">
					<c:if test="${linkInputDeposit == true}">
						<bean:define id="concatUrl" value="/deposit/inputDeposit/load/?depositSlipId=${f:h(rowData.depositSlipId)}"/>
						<a href="javascript:location.doHref('${f:url(concatUrl)}')" tabindex="1001">${f:h(rowData.depositSlipId)}</a>
					</c:if>
					<c:if test="${linkInputDeposit == false}">
						${f:h(rowData.depositSlipId)}
					</c:if>
					</td>
					<td style="text-align: left">
						${f:h(rowData.deliverySlipId)}
					</td>
					<td style="text-align: left">
						${f:h(rowData.customer)}
					</td>
					<td style="text-align: left">
						${f:h(rowData.deliveryDate)}
					</td>
					<td style="text-align: right">
						<fmt:formatNumber value="${rowData.productPrice}" pattern="###,##0" />
					</td>
					<td style="text-align: right">
						<fmt:formatNumber value="${rowData.salesMoney}" pattern="###,##0" />
					</td>
				</tr>
			</c:forEach>
		</tbody>
</table>
</span>
<div style="position:absolute; width: 910px; text-align: center;">
	&nbsp;
</div>

