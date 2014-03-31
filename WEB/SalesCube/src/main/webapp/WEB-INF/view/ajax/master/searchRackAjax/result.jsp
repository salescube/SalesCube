<div style="width: 1010px; height: 25px;">
		<input type="hidden" id="searchResultCount" value="${f:h(searchResultCount)}">
		<div style="position:absolute; left: 0px;">検索結果件数： ${searchResultCount}件</div>
		<div style="position:absolute; width: 1160px; text-align: center;">
			${sw:pageLink(searchResultCount,rowCount,pageNo)}
		</div>
        <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
</div>

<table id="detail_info" summary="searchResult" class="forms detail_info" style="table-layout: auto; margin-top: 10px;">

		<colgroup>
		    <col span="1" style="width: 10%">
		    <col span="1" style="width: 10%">
		    <col span="1" style="width: 10%">
		    <col span="1" style="width: 20%">
		    <col span="1" style="width: 5%">
		    <col span="1" style="width: 20%">
		    <col span="1" style="width: 20%">
		    <col span="1" style="width: 15%">
		</colgroup>
		<tr>
		<th class="xl64 rd_top_left" style="cursor: pointer; height: 30px;" onclick="sort('warehouseCode');">倉庫コード
			<c:if test="${sortColumn == 'warehouseCode'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('warehouseName');">倉庫名
			<c:if test="${sortColumn == 'warehouseName'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('rackCode');">棚番コード
			<c:if test="${sortColumn == 'rackCode'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('rackName');">棚番名
			<c:if test="${sortColumn == 'rackName'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="xl64">重複登録可能</th>
		<th class="xl64">商品コード</th>
		<th class="xl64">商品名</th>
        <th class="xl64 rd_top_right">&nbsp;</th>
     </tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
	<tr>
		<td>&nbsp;${f:h(bean.warehouseCode)}&nbsp;</td>
		<td>&nbsp;${f:h(bean.warehouseName)}&nbsp;</td>
		<td>&nbsp;${f:h(bean.rackCode)}&nbsp;</td>
		<td style="white-space: normal">&nbsp;${f:h(bean.rackName)}&nbsp;</td>
		<td style="white-space: normal">
			&nbsp;
			<c:forEach var="duplicate" items="${bean.duplicateList}" varStatus="status">
				${f:h(duplicate)}
			</c:forEach>
			&nbsp;
		</td>
		<td style="white-space: normal">
			&nbsp;
			<c:forEach var="productCode" items="${bean.productCodeList}" varStatus="status">
				<c:if test="${status.index != 0}">,</c:if>${f:h(productCode)}
			</c:forEach>
			&nbsp;
		</td>
		<td style="white-space: normal">
			&nbsp;
			<c:forEach var="productName" items="${bean.productNameList}" varStatus="status">
				<c:if test="${status.index != 0}">,</c:if>${f:h(productName)}
			</c:forEach>
			&nbsp;
		</td>
		<td style="text-align: center">
			<c:if test="${isUpdate}">
			<button class="btn_list_action" onclick="editRack('${sw:u(bean.rackCode)}');">編集</button>
			<button class="btn_list_action" onclick="deleteRack('${bean.rackCode}', '${bean.updDatetm}');">削除</button>
			</c:if>
			<c:if test="${!isUpdate}">
			<button class="btn_list_action" onclick="editRack('${sw:u(bean.rackCode)}');">参照</button>
			</c:if>
		</td>
	</tr>
	</c:forEach>
</table>

<div style="position:absolute; width: 1160px; text-align: center; margin-top: 20px;">
	${sw:pageLink(searchResultCount,rowCount,pageNo)}
</div>

<%-- 今回の検索条件を保持 --%>
<input type="hidden" id="prev_rackCode" name="prev_rackCode" value="${rackCode}">
<input type="hidden" id="prev_rackName" name="prev_rackName" value="${f:h(rackName)}">
<input type="hidden" id="prev_emptyRack" name="prev_emptyRack" value="${emptyRack}">
<input type="hidden" id="prev_sortColumn" name="prev_sortColumn" value="${sortColumn}">
<input type="hidden" id="prev_sortOrderAsc" name="prev_sortOrderAsc" value="${sortOrderAsc}">
<input type="hidden" id="prev_pageNo" name="prev_pageNo" value="${pageNo}">
<input type="hidden" id="prev_rowCount" name="prev_rowCount" value="${rowCount}">


<c:if test="${searchResultCount == 1}">
<%-- 検索結果が1件の場合にはその棚番コードをhiddenで配置する --%>
<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<input type="hidden" id="singleRackCode" name="singleRackCode" value="${sw:u(bean.rackCode)}">
</c:forEach>
</c:if>


