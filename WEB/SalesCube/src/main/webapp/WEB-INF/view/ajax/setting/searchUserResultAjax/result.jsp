<div style="width: 1010px; height: 25px;">
		<div style="position:absolute; left: 0px;">検索結果件数： ${searchResultCount}件</div>
		<div style="position:absolute; width: 1160px; text-align: center;">
			${sw:pageLink(searchResultCount,rowCount,pageNo)}
		</div>
        <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
</div>

<table id="search_result" summary="社員検索結果" class="forms detail_info" style="table-layout: auto; margin-top: 20px;">
	<colgroup>
		<col span="1" style="width: 10%">
		<col span="1" style="width: 5%">
		<col span="1" style="width: 15%">
		<col span="1" style="width: 15%">
		<col span="1" style="width: 20%">
		<col span="1" style="width: 35%">
		<col span="1" style="width: 10%">
	</colgroup>
	<tr>
		<th class="rd_top_left" style="height: 30px; style="cursor: pointer" onclick="sort('userId');">社員コード
			<c:if test="${sortColumn == 'userId'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th style="cursor: pointer" onclick="sort('lockflg');">ロック
			<c:if test="${sortColumn == 'lockflg'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th style="cursor: pointer" onclick="sort('nameKnj');">社員名
			<c:if test="${sortColumn == 'nameKnj'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th style="cursor: pointer" onclick="sort('nameKana');">社員名カナ
			<c:if test="${sortColumn == 'nameKana'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th style="cursor: pointer" onclick="sort('deptName');">部門
			<c:if test="${sortColumn == 'deptName'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th style="cursor: pointer" onclick="sort('email');">E-MAIL
			<c:if test="${sortColumn == 'email'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th>
		<th class="rd_top_right">&nbsp;</th>
	</tr>

	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
	<tr>
		<td>${f:h(bean.userId)}</td>
		<td>
      		<c:if test="${f:h(bean.lockflg) == '1'}">
				&nbsp;○&nbsp;<br>
				&nbsp;${f:h(bean.lockdatetm)}&nbsp;
      		</c:if>

		</td>
		<td style="white-space: normal">&nbsp;${f:h(bean.nameKnj)}&nbsp;</td>
		<td style="white-space: normal">&nbsp;${f:h(bean.nameKana)}&nbsp;</td>
		<td style="white-space: normal">&nbsp;${f:h(bean.deptName)}&nbsp;</td>
		<td style="white-space: normal">&nbsp;${f:h(bean.email)}&nbsp;</td>
		<td style="text-align: center">
			<c:if test="${isUpdate}">
			<button class="btn_list_action"  onclick="editUser('${sw:u(bean.userId)}');">編集</button>
			<button class="btn_list_action"  onclick="deleteUser('${sw:u(bean.userId)}', '${bean.updDatetm}');">削除</button>
			</c:if>
			<c:if test="${!isUpdate}">
			<button class="btn_list_action"  onclick="editUser('${sw:u(bean.userId)}');">参照</button>
			</c:if>
		</td>
	</tr>
	</c:forEach>
</table>

<div style="position:absolute; width: 1160px; text-align: center; margin-top: 20px;">
	${sw:pageLink(searchResultCount,rowCount,pageNo)}
</div>

<%-- 今回の検索条件を保持 --%>
<input type="hidden" id="prev_userId" name="prev_userId" value="${userId}">
<input type="hidden" id="prev_lockflg" name="prev_lockflg" value="${lockflg}">
<input type="hidden" id="prev_nameKnj" name="prev_nameKnj" value="${f:h(nameKnj)}">
<input type="hidden" id="prev_nameKana" name="prev_nameKana" value="${f:h(nameKana)}">
<input type="hidden" id="prev_deptId" name="prev_deptId" value="${deptId}">
<input type="hidden" id="prev_email" name="prev_email" value="${f:h(email)}">
<input type="hidden" id="prev_sortColumn" name="prev_sortColumn" value="${sortColumn}">
<input type="hidden" id="prev_sortOrderAsc" name="prev_sortOrderAsc" value="${sortOrderAsc}">
<input type="hidden" id="prev_pageNo" name="prev_pageNo" value="${pageNo}">
<input type="hidden" id="prev_rowCount" name="prev_rowCount" value="${rowCount}">


<c:if test="${searchResultCount == 1}">
<%-- 検索結果が1件の場合にはそのユーザーIDをhiddenで配置する --%>
<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<input type="hidden" id="singleUserId" name="singleUserId" value="${sw:u(bean.userId)}">
</c:forEach>
</c:if>