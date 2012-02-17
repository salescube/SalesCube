<div style="width: 910px; height: 25px;">
		<input type="hidden" id="searchResultCount" value="${f:h(searchResultCount)}">
		<div style="position:absolute; left: 0px;">検索結果件数： ${searchResultCount}件</div>
		<div style="position:absolute; width: 910px; text-align: center;">
			${sw:pageLink(searchResultCount,rowCount,pageNo)}
		</div>
        <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
</div>

<table summary="商品検索結果" class="forms" style="width: 910px;">
	<colgroup>
		<col span="1" style="width: 15%">
		<col span="1" style="width: 20%">
		<col span="1" style="width: 20%">
		<col span="1" style="width: 15%">
		<col span="1" style="width: 20%">
		<col span="1" style="width: 10%">
	</colgroup>
	<tr>
		<th style="cursor: pointer" onclick="sort('productCode');">商品コード
			<c:if test="${sortColumn == 'productCode'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th style="cursor: pointer" onclick="sort('productName');">商品名
			<c:if test="${sortColumn == 'productName'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th style="cursor: pointer" onclick="sort('supplierCode');">仕入先
			<c:if test="${sortColumn == 'supplierCode'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th style="cursor: pointer" onclick="sort('PRODUCT_1');">分類（大）
			<c:if test="${sortColumn == 'PRODUCT_1'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th>備考</th>
		<th>&nbsp;</th>
	</tr>

	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
	<tr>
		<td>${f:h(bean.productCode)}</td>
		<td style="white-space: normal">${f:h(bean.productName)}</td>
		<td style="white-space: normal">${f:h(bean.supplierName)}</td>
		<td style="white-space: normal">${f:h(bean.className)}</td>
		<td style="white-space: normal">${f:h(bean.remarks)}</td>
		<td style="text-align: center">
			<c:if test="${isUpdate}">
			<button onclick="editProduct('${f:h(bean.productCode)}');">編集</button>
			<button onclick="deleteProduct('${f:h(bean.productCode)}', '${bean.updDatetm}', '${bean.discountUpdDatetm}');">削除</button>
			</c:if>
			<c:if test="${!isUpdate}">
			<button onclick="editProduct('${f:h(bean.productCode)}');">参照</button>
			</c:if>
		</td>
	</tr>
	</c:forEach>
</table>

<div style="position:absolute; width: 910px; text-align: center;">
	${sw:pageLink(searchResultCount,rowCount,pageNo)}
</div>


<input type="hidden" id="prev_productCode" name="prev_productCode" value="${f:h(productCode)}">
<input type="hidden" id="prev_productName" name="prev_productName" value="${f:h(productName)}">
<input type="hidden" id="prev_productKana" name="prev_productKana" value="${f:h(productKana)}">
<input type="hidden" id="prev_supplierCode" name="prev_supplierCode" value="${supplierCode}">
<input type="hidden" id="prev_supplierName" name="prev_supplierName" value="${f:h(supplierName)}">

<input type="hidden" id="prev_productStandardCategory" name="prev_productStandardCategory" value="${productStandardCategory}">
<input type="hidden" id="prev_productStatusCategory" name="prev_productStatusCategory" value="${setStatusCategory}">
<input type="hidden" id="prev_productStockCategory" name="prev_productStockCategory" value="${setStockCategory}">
<input type="hidden" id="prev_setTypeCategory" name="prev_setTypeCategory" value="${setTypeCategory}">
<input type="hidden" id="prev_remarks" name="prev_remarks" value="${f:h(remarks)}">

<input type="hidden" id="prev_product1" name="prev_product1" value="${product1}">
<input type="hidden" id="prev_product2" name="prev_product2" value="${product2}">
<input type="hidden" id="prev_product3" name="prev_product3" value="${product3}">

<input type="hidden" id="prev_sortColumn" name="prev_sortColumn" value="${sortColumn}">
<input type="hidden" id="prev_sortOrderAsc" name="prev_sortOrderAsc" value="${sortOrderAsc}">
<input type="hidden" id="prev_pageNo" name="prev_pageNo" value="${pageNo}">
<input type="hidden" id="prev_rowCount" name="prev_rowCount" value="${rowCount}">


<c:if test="${searchResultCount == 1}">

<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<input type="hidden" id="singleProductCode" name="singleProductCode" value="${f:h(bean.productCode)}">
</c:forEach>
</c:if>