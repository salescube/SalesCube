<div style="width: 1010px; height: 25px;">
		<input type="hidden" id="searchResultCount" value="${f:h(searchResultCount)}">
		<div style="position:absolute; left: 0px;">検索結果件数： ${searchResultCount}件</div>
		<div style="position:absolute; width: 1160px; text-align: center;">
			${sw:pageLink(searchResultCount,rowCount,pageNo)}
		</div>
        <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
</div>

<table id="order_detail_info" summary="searchResult" class="forms detail_info" style="table-layout: auto; margin-top: 20px;">
	<colgroup>
		<col span="1" style="width: 15%">
		<col span="1" style="width: 20%">
		<col span="1" style="width: 20%">
		<col span="1" style="width: 15%">
		<col span="1" style="width: 20%">
		<col span="1" style="width: 10%">
	</colgroup>
	<tr>
		<th class="xl64 rd_top_left" style="cursor: pointer; height: 30px;" onclick="sort('productCode');">商品コード
			<c:if test="${sortColumn == 'productCode'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('productName');">商品名
			<c:if test="${sortColumn == 'productName'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('supplierCode');">仕入先
			<c:if test="${sortColumn == 'supplierCode'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('PRODUCT_1');">分類（大）
			<c:if test="${sortColumn == 'PRODUCT_1'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="xl64" style="cursor: pointer; height: 30px;">備考</th>
		<th class="xl64 rd_top_right" style="cursor: pointer; height: 30px;">&nbsp;</th>
	</tr>

	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
	<tr>
		<td>&nbsp;${f:h(bean.productCode)}&nbsp;</td>
		<td style="white-space: normal; text-align: left;">&nbsp;${f:h(bean.productName)}&nbsp;</td>
		<td style="white-space: normal; text-align: left;">&nbsp;${f:h(bean.supplierName)}&nbsp;</td>
		<td style="white-space: normal; text-align: left;">&nbsp;${f:h(bean.className)}&nbsp;</td>
		<td style="white-space: normal; text-align: left;">&nbsp;${f:h(bean.remarks)}&nbsp;</td>
		<td style="text-align: center">
			<c:if test="${isUpdate}">
				<button type="button" class="btn_product_action" onclick="editProduct('${f:h(bean.productCode)}');">編集</button>
				<button type="button" class="btn_product_action" onclick="deleteProduct('${f:h(bean.productCode)}', '${bean.updDatetm}', '${bean.discountUpdDatetm}');">削除</button>
			</c:if>
			<c:if test="${!isUpdate}">
				<button type="button" class="btn_product_action" onclick="editProduct('${f:h(bean.productCode)}');">参照</button>
			</c:if>
		</td>
	</tr>
	</c:forEach>
</table>

<div style="position:absolute; width: 1160px; text-align: center; margin-top: 20px;">
	${sw:pageLink(searchResultCount,rowCount,pageNo)}
</div>

<%-- 今回の検索条件を保持 --%>
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
<%-- 検索結果が1件の場合にはその部門IDをhiddenで配置する --%>
<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<input type="hidden" id="singleProductCode" name="singleProductCode" value="${f:h(bean.productCode)}">
</c:forEach>
</c:if>