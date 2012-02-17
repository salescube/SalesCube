<div style="width: 910px; height: 25px;">
		<input type="hidden" id="searchResultCount" value="${f:h(searchResultCount)}">
		<div style="position:absolute; left: 0px;">検索結果件数： ${searchResultCount}件</div>
		<div style="position:absolute; width: 910px; text-align: center;">
			${sw:pageLink(searchResultCount,rowCount,pageNo)}
		</div>
        <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
</div>

<table summary="検索結果"  class="forms" style="table-layout: fixed;">

	<colgroup>
		<col span="1" style="width: 60px">
		<col span="1" style="width:180px">

		<col span="1" style="width: 60px">
		<col span="1" style="width:180px">

		<col span="1" style="width: 60px">
		<col span="1" style="width:180px">

		<col span="1" style="width:80px">
	</colgroup>
	<tr>
		<th colspan="2">大分類</th>
		<th colspan="2">中分類</th>
		<th colspan="2">小分類</th>
		<th rowspan="2"></th>
	</tr>

	<tr>
		<th style="cursor: pointer; white-space: nowrap;" onclick="sort('classCode1');">コード
   			<c:if test="${sortColumn == 'classCode1'}">
   				<c:if test="${sortOrderAsc}">▲</c:if>
   				<c:if test="${!sortOrderAsc}">▼</c:if>
   			</c:if>
		</th>
		<th style="cursor: pointer; white-space: nowrap;" onclick="sort('className1');">名称
   			<c:if test="${sortColumn == 'className1'}">
   				<c:if test="${sortOrderAsc}">▲</c:if>
   				<c:if test="${!sortOrderAsc}">▼</c:if>
   			</c:if>
		</th>
		<th style="cursor: pointer; white-space: nowrap;" onclick="sort('classCode2');">コード
   			<c:if test="${sortColumn == 'classCode2'}">
   				<c:if test="${sortOrderAsc}">▲</c:if>
   				<c:if test="${!sortOrderAsc}">▼</c:if>
   			</c:if>
		</th>
		<th style="cursor: pointer; white-space: nowrap;" onclick="sort('className2');">名称
   			<c:if test="${sortColumn == 'className2'}">
   				<c:if test="${sortOrderAsc}">▲</c:if>
   				<c:if test="${!sortOrderAsc}">▼</c:if>
   			</c:if>
		</th>
		<th style="cursor: pointer; white-space: nowrap;" onclick="sort('classCode3');">コード
   			<c:if test="${sortColumn == 'classCode3'}">
   				<c:if test="${sortOrderAsc}">▲</c:if>
   				<c:if test="${!sortOrderAsc}">▼</c:if>
   			</c:if>
		</th>
		<th style="cursor: pointer; white-space: nowrap;"  onclick="sort('className3');">名称
   			<c:if test="${sortColumn == 'className3'}">
   				<c:if test="${sortOrderAsc}">▲</c:if>
   				<c:if test="${!sortOrderAsc}">▼</c:if>
   			</c:if>
		</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
	<tr>
		<td>${bean.classCode1}</td>
		<td style="white-space: normal">${f:h(bean.className1)}</td>
		<td>${bean.classCode2}</td>
		<td style="white-space: normal">${f:h(bean.className2)}</td>
		<td>${bean.classCode3}</td>
		<td style="white-space: normal">${f:h(bean.className3)}</td>
		<td style="text-align: center">
			<c:if test="${isUpdate}">
			<button onclick="editProductClass('${bean.classCode1}','${bean.classCode2}','${bean.classCode3}');">編集</button>
			<button onclick="deleteProductClass('${bean.classCode1}','${bean.classCode2}','${bean.classCode3}','${bean.updDatetm}');">削除</button>
			</c:if>
			<c:if test="${!isUpdate}">
			<button onclick="editProductClass('${bean.classCode1}','${bean.classCode2}','${bean.classCode3}');">参照</button>
			</c:if>
		</td>
	</tr>
	</c:forEach>

</table>

<div style="position:absolute; width: 910px; text-align: center;">
	${sw:pageLink(searchResultCount,rowCount,pageNo)}
</div>


<input type="hidden" id="prev_classCode1" name="prev_classCode1" value="${classCode1}">
<input type="hidden" id="prev_classCode2" name="prev_classCode2" value="${f:h(classCode2)}">
<input type="hidden" id="prev_classCode3" name="prev_classCode3" value="${f:h(classCode3)}">
<input type="hidden" id="prev_classCode" name="prev_classCode" value="${classCode}">
<input type="hidden" id="prev_className" name="prev_className" value="${f:h(className)}">
<input type="hidden" id="prev_pageNo" name="prev_pageNo" value="${pageNo}">
<input type="hidden" id="prev_rowCount" name="prev_rowCount" value="${rowCount}">


<c:if test="${searchResultCount == 1}">

<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<input type="hidden" id="singleProductClassCode" name="singleProductClassCode" value="${bean.classCode1}-${bean.classCode2}-${bean.classCode3}">
</c:forEach>
</c:if>
