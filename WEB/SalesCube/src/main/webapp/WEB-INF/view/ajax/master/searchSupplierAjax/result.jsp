<div style="width: 1010px; height: 25px;">
		<input type="hidden" id="searchResultCount" value="${f:h(searchResultCount)}">
		<div style="position:absolute; left: 0px;">検索結果件数： ${searchResultCount}件</div>
		<div style="position:absolute; width: 1160px; text-align: center;">
			${sw:pageLink(searchResultCount,rowCount,pageNo)}
		</div>
        <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
</div>

<!-- <table summary="検索結果"  class="forms"> -->
<table id="order_detail_info" summary="検索結果" class="forms detail_info" style="table-layout: auto; margin-top: 20px;">
	<colgroup>
		<col span="1" style="width: 10%">
		<col span="1" style="width: 15%">
		<col span="1" style="width: 15%">
		<col span="1" style="width: 15%">
		<col span="1" style="width: 35%">
		<col span="1" style="width: 10%">
	</colgroup>
	<tr>
		<th class="xl64 rd_top_left" style="cursor: pointer; height: 30px;" onclick="sort('supplierCode');">仕入先コード
			<c:if test="${sortColumn == 'supplierCode'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('supplierName');">仕入先名
			<c:if test="${sortColumn == 'supplierName'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('supplierPcName');">担当者
			<c:if test="${sortColumn == 'supplierPcName'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('supplierCmCategoryName');">取引区分
			<c:if test="${sortColumn == 'supplierCmCategoryName'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="xl64" >備考</th>
		<th class="xl64 rd_top_right">&nbsp;</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
	<tr>
		<td>&nbsp;${f:h(bean.supplierCode)}&nbsp;</td>
		<td style="white-space: normal; text-align: left;">&nbsp;${f:h(bean.supplierName)}&nbsp;</td>
		<td style="white-space: normal; text-align: left;">&nbsp;${f:h(bean.supplierPcName)}&nbsp;</td>
		<td style="white-space: normal; text-align: left;">&nbsp;${f:h(bean.supplierCmCategoryName)}&nbsp;</td>
		<td style="white-space: normal; text-align: left;">&nbsp;${f:h(bean.remarks)}&nbsp;</td>
		<td style="text-align: center">
			<c:if test="${isUpdate}">
				<button type="button" class="btn_product_action" onclick="editSupplier('${sw:u(bean.supplierCode)}');">編集</button>
				<button type="button" class="btn_product_action" onclick="deleteSupplier('${bean.supplierCode}', '${bean.updDatetm}');">削除</button>
			</c:if>
			<c:if test="${!isUpdate}">
				<button type="button" class="btn_product_action" onclick="editSupplier('${sw:u(bean.supplierCode)}');">参照</button>
			</c:if>
		</td>
	</tr>
	</c:forEach>
</table>

<div style="position:absolute; width: 1160px; text-align: center; margin-top: 20px;">
	${sw:pageLink(searchResultCount,rowCount,pageNo)}
</div>

<%-- 今回の検索条件を保持 --%>
<input type="hidden" id="prev_supplierCode" name="prev_supplierCode" value="${supplierCode}">
<input type="hidden" id="prev_supplierName" name="prev_supplierName" value="${f:h(supplierName)}">
<input type="hidden" id="prev_supplierKana" name="prev_supplierKana" value="${f:h(supplierKana)}">
<input type="hidden" id="prev_remarks" name="prev_remarks" value="${f:h(remarks)}">
<input type="hidden" id="prev_sortColumn" name="prev_sortColumn" value="${sortColumn}">
<input type="hidden" id="prev_sortOrderAsc" name="prev_sortOrderAsc" value="${sortOrderAsc}">
<input type="hidden" id="prev_pageNo" name="prev_pageNo" value="${pageNo}">
<input type="hidden" id="prev_rowCount" name="prev_rowCount" value="${rowCount}">


<c:if test="${searchResultCount == 1}">
<%-- 検索結果が1件の場合にはその顧客コードをhiddenで配置する --%>
<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<input type="hidden" id="singleSupplierCode" name="singleSupplierCode" value="${sw:u(bean.supplierCode)}">
</c:forEach>
</c:if>
