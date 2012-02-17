<div style="width: 910px; height: 25px;">
		<input type="hidden" id="searchResultCount" value="${f:h(searchResultCount)}">
		<div style="position:absolute; left: 0px;">検索結果件数： ${searchResultCount}件</div>
		<div style="position:absolute; width: 910px; text-align: center;">
			${sw:pageLink(searchResultCount,rowCount,pageNo)}
		</div>
        <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
</div>

<table summary="検索結果"  class="forms">

	<colgroup>
		<col span="1" style="width: 9%">
		<col span="1" style="width: 20%">
		<col span="1" style="width: 10%">
		<col span="1" style="width: 10%">
		<col span="1" style="width: 8%">
		<col span="1" style="width: 13%">
		<col span="1" style="width: 10%">
		<col span="1" style="width: 10%">
		<col span="1" style="width: 10%">
	</colgroup>
	<tr>
		<th style="cursor: pointer" onclick="sort('customerCode');">顧客コード
			<c:if test="${sortColumn == 'customerCode'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th style="cursor: pointer" onclick="sort('customerName');">顧客名
			<c:if test="${sortColumn == 'customerName'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th style="cursor: pointer" onclick="sort('customerTel');">TEL
			<c:if test="${sortColumn == 'customerTel'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th style="cursor: pointer" onclick="sort('customerPcName');">担当者
			<c:if test="${sortColumn == 'customerPcName'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th style="cursor: pointer" onclick="sort('salesCmCategory');">取引区分
			<c:if test="${sortColumn == 'salesCmCategory'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th style="cursor: pointer" onclick="sort('cutoffGroup');">支払条件
			<c:if test="${sortColumn == 'cutoffGroup'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th style="cursor: pointer" onclick="sort('customerOfficeName');">事業所名
			<c:if test="${sortColumn == 'customerOfficeName'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th style="cursor: pointer" onclick="sort('customerDeptName');">部署名
			<c:if test="${sortColumn == 'customerDeptName'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th>&nbsp;</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
	<tr>
		<td>${f:h(bean.customerCode)}</td>
		<td style="white-space: normal">${f:h(bean.customerName)}</td>
		<td style="white-space: normal">${f:h(bean.customerTel)}</td>
		<td style="white-space: normal">${f:h(bean.customerPcName)}</td>
		<td style="white-space: normal">${f:h(bean.categoryCodeName3)}</td>
		<td style="white-space: normal">${f:h(bean.categoryCodeName)}</td>
		<td style="white-space: normal">${f:h(bean.customerOfficeName)}</td>
		<td style="white-space: normal">${f:h(bean.customerDeptName)}</td>

		<td style="text-align: center">
			<c:if test="${isUpdate}">
			<button onclick="editCustomer('${f:h(bean.customerCode)}');">編集</button>
			<button onclick="deleteCustomer('${f:h(bean.customerCode)}', '${bean.updDatetm}');">削除</button>
			</c:if>
			<c:if test="${!isUpdate}">
			<button onclick="editCustomer('${f:h(bean.customerCode)}');">参照</button>
			</c:if>
		</td>
	</tr>
	</c:forEach>

</table>

<div style="position:absolute; width: 910px; text-align: center;">
	${sw:pageLink(searchResultCount,rowCount,pageNo)}
</div>


<input type="hidden" id="prev_customerCode" name="prev_customerCode" value="${f:h(customerCode)}">
<input type="hidden" id="prev_customerName" name="prev_customerName" value="${f:h(customerName)}">
<input type="hidden" id="prev_customerKana" name="prev_customerKana" value="${f:h(customerKana)}">
<input type="hidden" id="prev_customerOfficeName" name="prev_customerOfficeName" value="${f:h(customerOfficeName)}">
<input type="hidden" id="prev_customerOfficeKana" name="prev_customerOfficeKana" value="${f:h(customerOfficeKana)}">
<input type="hidden" id="prev_customerPcName" name="prev_customerPcName" value="${f:h(customerPcName)}">
<input type="hidden" id="prev_customerPcKana" name="prev_customerPcKana" value="${f:h(customerPcKana)}">
<input type="hidden" id="prev_customerTel" name="prev_customerTel" value="${f:h(customerTel)}">
<input type="hidden" id="prev_customerFax" name="prev_customerFax" value="${f:h(customerFax)}">
<input type="hidden" id="prev_customerRankCategory" name="prev_customerRankCategory" value="${customerRankCategory}">
<input type="hidden" id="prev_cutoffGroup" name="prev_cutoffGroup" value="${f:h(cutoffGroup)}">
<input type="hidden" id="prev_paymentName" name="prev_paymentName" value="${f:h(paymentName)}">
<input type="hidden" id="prev_remarks" name="prev_remarks" value="${f:h(remarks)}">
<input type="hidden" id="prev_sortColumn" name="prev_sortColumn" value="${sortColumn}">
<input type="hidden" id="prev_sortOrderAsc" name="prev_sortOrderAsc" value="${sortOrderAsc}">
<input type="hidden" id="prev_pageNo" name="prev_pageNo" value="${pageNo}">
<input type="hidden" id="prev_rowCount" name="prev_rowCount" value="${rowCount}">


<c:if test="${searchResultCount == 1}">

<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<input type="hidden" id="singleCustomerCode" name="singleCustomerCode" value="${f:h(bean.customerCode)}">
</c:forEach>
</c:if>
