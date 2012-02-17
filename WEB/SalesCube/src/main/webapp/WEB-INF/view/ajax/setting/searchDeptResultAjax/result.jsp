<div style="width: 910px; height: 25px;">
		<div style="position:absolute; left: 0px;">検索結果件数： ${searchResultCount}件</div>
		<div style="position:absolute; width: 910px; text-align: center;">
			${sw:pageLink(searchResultCount,rowCount,pageNo)}
		</div>
        <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
</div>

<table summary="部門検索結果" class="forms" style="width: 910px;">
	<colgroup>
		<col span="1" style="width: 15%">
		<col span="1" style="width: 35%">
		<col span="1" style="width: 35%">
		<col span="1" style="width: 15%">
	</colgroup>
	<tr>
		<th style="cursor: pointer" onclick="sort('deptId');">部門コード
			<c:if test="${sortColumn == 'deptId'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th style="cursor: pointer" onclick="sort('name');">部門名
			<c:if test="${sortColumn == 'name'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th style="cursor: pointer" onclick="sort('parentId');">親部門
			<c:if test="${sortColumn == 'parentId'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th>&nbsp;</th>
	</tr>

	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
	<tr>
		<td>${f:h(bean.deptId)}</td>
		<td style="white-space: normal">${f:h(bean.name)}</td>
		<td style="white-space: normal">${f:h(bean.parentName)}</td>
		<td style="text-align: center">
			<c:if test="${isUpdate}">
			<button onclick="editDept('${sw:u(bean.deptId)}');">編集</button>
			<button onclick="deleteDept('${bean.deptId}', '${bean.updDatetm}');">削除</button>
			</c:if>
			<c:if test="${!isUpdate}">
			<button onclick="editDept('${sw:u(bean.deptId)}');">参照</button>
			</c:if>
		</td>
	</tr>
	</c:forEach>
</table>

<div style="position:absolute; width: 910px; text-align: center;">
	${sw:pageLink(searchResultCount,rowCount,pageNo)}
</div>


<input type="hidden" id="prev_deptId" name="prev_deptId" value="${deptId}">
<input type="hidden" id="prev_parentId" name="prev_parentId" value="${f:h(parentId)}">
<input type="hidden" id="prev_name" name="prev_name" value="${f:h(name)}">
<input type="hidden" id="prev_sortColumn" name="prev_sortColumn" value="${sortColumn}">
<input type="hidden" id="prev_sortOrderAsc" name="prev_sortOrderAsc" value="${sortOrderAsc}">
<input type="hidden" id="prev_pageNo" name="prev_pageNo" value="${pageNo}">
<input type="hidden" id="prev_rowCount" name="prev_rowCount" value="${rowCount}">


<c:if test="${searchResultCount == 1}">

<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<input type="hidden" id="singleDeptId" name="singleDeptId" value="${sw:u(bean.deptId)}">
</c:forEach>
</c:if>