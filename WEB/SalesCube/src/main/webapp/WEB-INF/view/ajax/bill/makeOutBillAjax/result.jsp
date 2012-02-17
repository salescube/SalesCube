<div style="width: 910px; height: 25px;">
	<div style="position:absolute; left: 0px;">検索結果件数： ${searchResultCount}件</div>
	<input type="hidden" id="searchResultCount" value="${searchResultCount}">

	<div style="position:absolute; width: 910px; text-align: center;">
		${sw:pageLink(searchResultCount,rowCount,pageNo)}
	</div>

	<jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
</div>

<table id="order_myself_output_list" summary="order_myself_output_list"
	class="forms" style="width: 1100px;">
	<colgroup>
		<col span="1" style="width: 2%;">
		<col span="1" style="width: 4%;">
		<col span="1" style="width: 9%;">
		<col span="1" style="width: 9%;">
		<col span="1" style="width: 4%;">
		<col span="1" style="width: 10%;">
		<col span="1" style="width: 20%;">
		<col span="1" style="width: 10%;">
		<col span="1" style="width: 10%;">
		<col span="1" style="width: 10%;">
		<col span="1" style="width: 10%;">
	</colgroup>
	<thead>
		<tr>
			<th><bean:message key='words.action.select' /></th>
			
			<th style="cursor: pointer" onclick="sort('billPrintCount');"><bean:message
				key='labels.alreadyOutput' /> <c:if
				test="${sortColumn == 'billPrintCount'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if></th>
			
			<th style="cursor: pointer" onclick="sort('billId');"><bean:message
				key='labels.billId' /> <c:if test="${sortColumn == 'billId'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if></th>
			
			<th style="cursor: pointer" onclick="sort('lastPrintDate');"><bean:message
				key='labels.billPrintDate' /> <c:if
				test="${sortColumn == 'lastPrintDate'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if></th>
			
			<th style="cursor: pointer" onclick="sort('singlePrintCount');">
			<bean:message key='labels.singlePrint' /> <c:if
				test="${sortColumn == 'singlePrintCount'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if></th>
			
			<th style="cursor: pointer" onclick="sort('customerCode');"><bean:message
				key='labels.customerCode' /> <c:if
				test="${sortColumn == 'customerCode'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if></th>
			
			<th style="cursor: pointer" onclick="sort('deliveryName');"><bean:message
				key='labels.billName' /> <c:if
				test="${sortColumn == 'deliveryName'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if></th>
			
			<th style="cursor: pointer" onclick="sort('covPrice');"><bean:message
				key='labels.covPrice' /> <c:if test="${sortColumn == 'covPrice'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if></th>
			
			<th style="cursor: pointer" onclick="sort('salesPrice');"><bean:message
				key='labels.thisSalesPrice' /> <c:if
				test="${sortColumn == 'salesPrice'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if></th>
			
			<th style="cursor: pointer" onclick="sort('ctaxPrice');"><bean:message
				key='labels.ctaxPrice' /> <c:if test="${sortColumn == 'ctaxPrice'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if></th>
			
			<th style="cursor: pointer" onclick="sort('thisBillPrice');"><bean:message
				key='labels.billPrice' /> <c:if
				test="${sortColumn == 'thisBillPrice'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if></th>
			
		</tr>
	</thead>
	<tbody>
		<c:forEach var="rowData" items="${searchResultList}" varStatus="s">
			<c:if test="${rowData.doa == '1'}">
				<c:set var="doaCol" value="" />
			</c:if>
			<c:if test="${rowData.doa == '2'}">
				<c:set var="doaCol" value="background-color: #C0C0C0" />
			</c:if>
			<tr>
				<td style="text-align: center; ${doaCol}"><html:checkbox
					name="rowData" property="printCheck"
					styleId="printCheck_${f:h(s.index)}" indexed="true"
					tabindex="${f:h(s.index)*2+1000}" value="${f:h(rowData.billId)}"
					onclick="checkPrint();"></html:checkbox> <html:hidden
					name="rowData" property="customerCode"
					styleId="customerCode_${f:h(s.index)}" indexed="true" /> <html:hidden
					name="rowData" property="doa" styleId="doa_${f:h(s.index)}"
					indexed="true" /></td>
				<c:if test="${rowData.billPrintCount > 0}">
					<td style="text-align: center; ${doaCol}" style="color: blue"><bean:message
						key='words.adverb.already' /></td>
				</c:if>
				<c:if test="${rowData.billPrintCount == 0}">
					<td style="text-align: center; ${doaCol}"
						style="color: red; font-weight: bold;"><bean:message
						key='words.adverb.yet' /></td>
				</c:if>
				<td style="text-align: right; ${doaCol}">${f:h(rowData.billId)}<html:hidden
					name="rowData" property="billId" styleId="billId_${f:h(s.index)}"
					indexed="true" /></td>
				<td style="text-align: center; ${doaCol}">${f:h(rowData.lastPrintDate)}</td>
				<td style="text-align: center; ${doaCol}"><c:if
					test="${rowData.singlePrintCount > 0}">
					<bean:message key='labels.exist' />
				</c:if> <c:if test="${rowData.singlePrintCount == 0}">
					<bean:message key='labels.notexist' />
				</c:if> <c:if test="${rowData.singlePrintCount == null}">
					<bean:message key='labels.notexist' />
				</c:if></td>
				<td style="text-align: left; ${doaCol}">${f:h(rowData.customerCode)}</td>
				<td style="text-align: left; ${doaCol}">${f:h(rowData.deliveryName)}</td>
				<td style="text-align: right; ${doaCol}"><span
					id="covPrice_${f:h(s.index)}"
					class="numeral_commas scale_round_down scale_0"><c:out
					value="${f:h(rowData.covPrice)}" /></span></td>
				<td style="text-align: right; ${doaCol}"><span
					id="salesPrice_${f:h(s.index)}"
					class="numeral_commas scale_round_down scale_0"><c:out
					value="${f:h(rowData.salesPrice)}" /></span></td>
				<td style="text-align: right; ${doaCol}"><span
					id="ctaxPrice_${f:h(s.index)}"
					class="numeral_commas scale_round_down scale_0"><c:out
					value="${f:h(rowData.ctaxPrice)}" /></span></td>
				<td style="text-align: right; ${doaCol}"><span
					id="thisBillPrice_${f:h(s.index)}"
					class="numeral_commas scale_round_down scale_0"><c:out
					value="${f:h(rowData.thisBillPrice)}" /></span></td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<div style="position:absolute; width: 910px; text-align: center;">
	${sw:pageLink(searchResultCount,rowCount,pageNo)}
</div>

<div id="AllBillIdList"><c:forEach var="searchResultList"
	items="${allSearchResultList}" varStatus="s">
	<input type="hidden" name="slipId"
		value="${f:h(searchResultList.billId)},${f:h(searchResultList.customerCode)},${f:h(searchResultList.doa)}">
</c:forEach></div>