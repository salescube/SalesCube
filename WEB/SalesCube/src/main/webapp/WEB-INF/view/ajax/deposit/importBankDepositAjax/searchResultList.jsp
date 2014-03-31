<div style="width: 1010px; height: 25px;">
		<input type="hidden" id="searchResultCount" value="${f:h(searchResultCount)}">
		<input type="hidden" id="dispResultCount" value="${f:h(dispResultCount)}">
		<div style="position:absolute; left: 0px;"><a href="javascript:resetListAll()" tabindex="200"><bean:message key='labels.inputResult'/>${searchResultCount}<bean:message key='labels.searchResultCountUnit'/></a>／<a href="javascript:resetListOK()" tabindex="201"><bean:message key='labels.bank.deposit.OKCount'/>${importOKCount}<bean:message key='labels.searchResultCountUnit'/></a>／<a href="javascript:resetListNG()" tabindex="202"><bean:message key='labels.bank.deposit.NGCount'/>${importNGCount}<bean:message key='labels.searchResultCountUnit'/></a></div>
		<div style="position:absolute; width: 1160px; text-align: center;">
			&nbsp;
		</div>
		<div style="position:absolute; right: 0px;">
			&nbsp;
		</div>
</div>
<span id="searchResultList">
<table summary="銀行入金取込結果" class="forms detail_info" style="table-layout: auto;">
	<colgroup>
		<col span="1" style="width: 10%">
		<col span="1" style="width: 10%">
		<col span="1" style="width: 10%">
		<col span="1" style="width: 10%">
		<col span="1" style="width: 20%">
		<col span="1" style="width: 10%">
		<col span="1" style="width: 10%">
		<col span="1" style="width: 10%">
		<col span="1" style="width: 10%">
		<col span="1" style="width: 10%">
	</colgroup>
	<thead>
	<tr>
		<th class="rd_top_left" style="cursor: pointer; height:30px;" onclick="sort('status');"><bean:message key='labels.slipStatus.status'/><!-- 状態 -->
			<span id="sortStatus_status" style="color: white">
				<c:if test="${sortColumn == 'status'}">
					<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
					<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
				</c:if>
			</span>
		</th>
		<th style="cursor: pointer; height:30px;" onclick="sort('depositSlipId');"><bean:message key='labels.depositSlipId'/><!-- 入金番号 -->
			<span id="sortStatus_depositSlipId" style="color: white">
				<c:if test="${sortColumn == 'depositSlipId'}">
					<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
					<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
				</c:if>
			</span>
		</th>
		<th style="cursor: pointer; height:30px;" onclick="sort('customer');"><bean:message key='labels.customer'/><!--顧客 -->
			<span id="sortStatus_customer" style="color: white">
				<c:if test="${sortColumn == 'customer'}">
					<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
					<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
				</c:if>
			</span>
		</th>

		<th style="cursor: pointer; height:30px;" onclick="sort('paymentDate');"><bean:message key='labels.bank.deposit.PaymentDate'/><!-- 振込日  -->
			<span id="sortStatus_ypaymentDate" style="color: white">
				<c:if test="${sortColumn == 'paymentDate'}">
					<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
					<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
				</c:if>
			</span>
		</th>
		<th style="cursor: pointer; height:30px;" onclick="sort('paymentName');"><bean:message key='labels.bank.deposit.PaymentName'/><!-- 振込人名義 -->
			<span id="sortStatus_paymentName" style="color: white">
				<c:if test="${sortColumn == 'paymentName'}">
					<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
					<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
				</c:if>
			</span>
		</th>
		<th style="cursor: pointer; height:30px;" onclick="sort('lastBillPrice');"><bean:message key='labels.lastBillPrice'/><!-- 前回請求額 -->
			<span id="sortStatus_ylastBillPrice" style="color: white">
				<c:if test="${sortColumn == 'lastBillPrice'}">
					<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
					<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
				</c:if>
			</span>
		</th>
		<th style="cursor: pointer; height:30px;" onclick="sort('paymentPrice');"><bean:message key='labels.bank.deposit.PaymentPrice'/><!-- 振込金額 -->
			<span id="sortStatus_paymentPrice" style="color: white">
				<c:if test="${sortColumn == 'paymentPrice'}">
					<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
					<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
				</c:if>
			</span>
		</th>
		<th style="cursor: pointer; height:30px;" onclick="sort('diffPrice');"><bean:message key='labels.diffPrice'/><!-- 差額 -->
			<span id="sortStatus_sdiffPrice" style="color: white">
				<c:if test="${sortColumn == 'diffPrice'}">
					<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
					<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
				</c:if>
			</span>
		</th>
		<th style="cursor: pointer; height:30px;" onclick="sort('changeName');"><bean:message key='labels.changeName'/><!-- 名義変換 -->
			<span id="sortStatus_changeName" style="color: white">
				<c:if test="${sortColumn == 'changeName'}">
					<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
					<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
				</c:if>
			</span>
		</th>
		<th class="rd_top_right" style="cursor: pointer; height:30px;" onclick="sort('afterChangeName');">
			&nbsp;<bean:message key='labels.changeName'/><bean:message key='labels.afterChangeName'/>&nbsp;<!-- 名義変換 -->
			<span id="sortStatus_afterChangeName" style="color: white">
				<c:if test="${sortColumn == 'afterChangeName'}">
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
						&nbsp;${f:h(rowData.status)}&nbsp;
					</td>
					<td style="text-align: right">&nbsp;
					<c:if test="${linkInputDeposit == true}">
						<bean:define id="concatUrl" value="/deposit/inputDeposit/load/?depositSlipId=${f:h(rowData.depositSlipId)}"/>
						<a href="javascript:location.doHref('${f:url(concatUrl)}')" tabindex="1000">${f:h(rowData.depositSlipId)}</a>
					</c:if>
					<c:if test="${linkInputDeposit == false}">
						${f:h(rowData.depositSlipId)}
					</c:if>&nbsp;
					</td>
					<td style="text-align: left">
						&nbsp;${f:h(rowData.customer)}&nbsp;
					</td>
					<td style="text-align: left">
						&nbsp;${f:h(rowData.paymentDate)}&nbsp;
					</td>
					<td style="text-align: left">
						&nbsp;${f:h(rowData.paymentName)}&nbsp;
					</td>
					<td style="text-align: right">
						&nbsp;<fmt:formatNumber value="${rowData.lastBillPrice}" pattern="###,##0" />&nbsp;
					</td>
					<td style="text-align: right">
						&nbsp;<fmt:formatNumber value="${rowData.paymentPrice}" pattern="###,##0" />&nbsp;
					</td>
					<td style="text-align: right">
						&nbsp;<fmt:formatNumber value="${rowData.diffPrice}" pattern="###,##0" />&nbsp;
					</td>
					<td style="text-align: left">
						&nbsp;${f:h(rowData.changeName)}&nbsp;
					</td>
					<td style="text-align: left">
						&nbsp;${f:h(rowData.afterChangeName)}&nbsp;
					</td>
				</tr>
			</c:forEach>
		</tbody>
</table>
</span>
<div style="position:absolute; width: 1160px; text-align: center;">
	&nbsp;
</div>

