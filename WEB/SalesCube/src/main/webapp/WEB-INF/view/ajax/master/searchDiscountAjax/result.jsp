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
		<col span="1" style="width: 15%">
		<col span="1" style="width: 30%">
		<col span="1" style="width: 35%">
		<col span="1" style="width: 10%">
		<col span="1" style="width: 10%">
	</colgroup>
	<tr>
		<th class="rd_top_left" style="cursor: pointer; height: 30px;" onclick="sort('discountId');">割引コード
			<c:if test="${sortColumn == 'discountId'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('discountName');">割引名
			<c:if test="${sortColumn == 'discountName'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('remarks');">備考
			<c:if test="${sortColumn == 'remarks'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('useFlagName');">有効無効
			<c:if test="${sortColumn == 'useFlagName'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="rd_top_right" style="height: 30px;">&nbsp;</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
	<tr>
		<td>&nbsp;${f:h(bean.discountId)}&nbsp;</td>
		<td style="white-space: normal">&nbsp;${f:h(bean.discountName)}&nbsp;</td>
		<td style="white-space: normal">&nbsp;${f:h(bean.remarks)}&nbsp;</td>
		<td style="white-space: normal">&nbsp;${f:h(bean.useFlagName)}&nbsp;</td>
		<td style="text-align: center">
			<c:if test="${isUpdate}">
			<button class="btn_list_action" onclick="editDiscount('${sw:u(bean.discountId)}');">編集</button>
			<button class="btn_list_action" onclick="deleteDiscount('${bean.discountId}', '${bean.updDatetm}');">削除</button>
			</c:if>
			<c:if test="${!isUpdate}">
			<button class="btn_list_action" onclick="editDiscount('${sw:u(bean.discountId)}');">参照</button>
			</c:if>
		</td>
	</tr>
	</c:forEach>

</table>

<div style="position:absolute; width: 1160px; text-align: center; margin-top: 20px;">
	${sw:pageLink(searchResultCount,rowCount,pageNo)}
</div>

<%-- 今回の検索条件を保持 --%>
<input type="hidden" id="prev_discountId" name="prev_discountId" value="${discountId}">
<input type="hidden" id="prev_discountName" name="discountName" value="${f:h(discountName)}">
<input type="hidden" id="prev_remarks" name="prev_remarks" value="${f:h(remarks)}">
<input type="hidden" id="prev_useFlag" name="prev_useFlag" value="${f:h(useFlag)}">
<input type="hidden" id="prev_sortColumn" name="prev_sortColumn" value="${sortColumn}">
<input type="hidden" id="prev_sortOrderAsc" name="prev_sortOrderAsc" value="${sortOrderAsc}">
<input type="hidden" id="prev_pageNo" name="prev_pageNo" value="${pageNo}">
<input type="hidden" id="prev_rowCount" name="prev_rowCount" value="${rowCount}">


<c:if test="${searchResultCount == 1}">
<%-- 検索結果が1件の場合にはその顧客コードをhiddenで配置する --%>
<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<input type="hidden" id="singleDiscountId" name="singleDiscountId" value="${sw:u(bean.discountId)}">
</c:forEach>
</c:if>
