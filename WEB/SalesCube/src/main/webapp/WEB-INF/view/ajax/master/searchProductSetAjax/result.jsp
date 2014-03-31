<div style="width: 1010px; height: 25px;">
		<input type="hidden" id="searchResultCount" value="${f:h(searchResultCount)}">
		<div style="position:absolute; left: 0px;">検索結果件数： ${searchResultCount}件</div>
		<div style="position:absolute; width: 1160px; text-align: center;">
			${sw:pageLink(searchResultCount,rowCount,pageNo)}
		</div>
        <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
</div>

<table id="detail_info" summary="searchResult" class="forms detail_info" style="table-layout: auto; margin-top: 20px;">
	<colgroup>
		<col span="1" style="width: 20%">
		<col span="1" style="width: 70%">
		<col span="1" style="width: 10%">
	</colgroup>
	<tr>
		<th class="xl64 rd_top_left" style="cursor: pointer; height: 30px;" onclick="sort('productCode');">セット商品コード
			<c:if test="${sortColumn == 'productCode'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('productName');">セット商品名
			<c:if test="${sortColumn == 'productName'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="xl64 rd_top_right">&nbsp;</th>
	</tr>

	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
	<tr>
		<td>&nbsp;${f:h(bean.setProductCode)}&nbsp;</td>
		<td style="white-space: normal">&nbsp;${f:h(bean.setProductName)}&nbsp;</td>
		<td style="text-align: center">
			<c:if test="${isUpdate}">
			<button class="btn_list_action" onclick="editProductSet('${f:h(bean.setProductCode)}');">編集</button>
			</c:if>
			<c:if test="${!isUpdate}">
			<button class="btn_list_action" onclick="editProductSet('${f:h(bean.setProductCode)}');">参照</button>
			</c:if>
		</td>
	</tr>
	</c:forEach>
</table>

<div style="position:absolute; width: 1160px; text-align: center; margin-top: 20px;">
	${sw:pageLink(searchResultCount,rowCount,pageNo)}
</div>

<%-- 今回の検索条件を保持 --%>
<input type="hidden" id="prev_setProductCode" name="prev_setProductCode" value="${setProductCode}">
<input type="hidden" id="prev_setProductName" name="prev_setProductName" value="${f:h(setProductName)}">
<input type="hidden" id="prev_productCode" name="prev_productCode" value="${productCode}">
<input type="hidden" id="prev_productName" name="prev_productName" value="${f:h(productName)}">

<input type="hidden" id="prev_sortColumn" name="prev_sortColumn" value="${sortColumn}">
<input type="hidden" id="prev_sortOrderAsc" name="prev_sortOrderAsc" value="${sortOrderAsc}">
<input type="hidden" id="prev_pageNo" name="prev_pageNo" value="${pageNo}">
<input type="hidden" id="prev_rowCount" name="prev_rowCount" value="${rowCount}">


<c:if test="${searchResultCount == 1}">
<%-- 検索結果が1件の場合にはそのセット商品コードをhiddenで配置する --%>
<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<input type="hidden" id="singleProductSetCode" name="singleProductSetCode" value="${sw:u(bean.setProductCode)}">
</c:forEach>
</c:if>