<span>登録ファイル件数： ${searchResultCount}件</span>
<table class="forms" style="width: 910px" summary="ファイル一覧">
	<colgroup>
		<col span="1" style="width: 5%">
		<col span="1" style="width: 30%">
		<col span="1" style="width: 24%">
		<col span="1" style="width: 16%">
		<col span="1" style="width: 10%">
		<col span="1" style="width: 10%">
		<col span="1" style="width: 5%">
	</colgroup>
	<tr>
		<th>No</th>
		<th style="cursor: pointer" onclick="sort('title');">タイトル <c:if
			test="${sortColumn == 'title'}">
			<c:if test="${sortOrderAsc}">▲</c:if>
			<c:if test="${!sortOrderAsc}">▼</c:if>
		</c:if></th>
		<th style="cursor: pointer" onclick="sort('fileName');">ファイル名 <c:if
			test="${sortColumn == 'fileName'}">
			<c:if test="${sortOrderAsc}">▲</c:if>
			<c:if test="${!sortOrderAsc}">▼</c:if>
		</c:if></th>
		<th style="cursor: pointer" onclick="sort('openLevel');">公開設定 <c:if
			test="${sortColumn == 'openLevel'}">
			<c:if test="${sortOrderAsc}">▲</c:if>
			<c:if test="${!sortOrderAsc}">▼</c:if>
		</c:if></th>
		<th style="cursor: pointer" onclick="sort('creDatetm');">登録日 <c:if
			test="${sortColumn == 'creDatetm'}">
			<c:if test="${sortOrderAsc}">▲</c:if>
			<c:if test="${!sortOrderAsc}">▼</c:if>
		</c:if></th>
		<th style="cursor: pointer" onclick="sort('creUser');">登録者 <c:if
			test="${sortColumn == 'creUser'}">
			<c:if test="${sortOrderAsc}">▲</c:if>
			<c:if test="${!sortOrderAsc}">▼</c:if>
		</c:if></th>
		<th>&nbsp;</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td style="text-align: center">${status.count}</td>
			<td style="white-space: normal"><bean:define id="fileUrl"
				value="${'/setting/fileDownload/download/'}${bean.fileId}" /> <a
				href="javascript:window.location.doHref('${f:url(fileUrl)}');"
				tabindex="${200 + (2 * status.index)}">${f:h(bean.title)}</a></td>
			<td style="white-space: normal">${f:h(bean.fileName)}</td>
			<td style="white-space: normal">${f:h(bean.openLevelName)}</td>
			<td>${bean.creDate}</td>
			<td>${f:h(bean.creUserName)}</td>
			<td>
			<button type="button"
				onclick="deleteFile('${bean.fileId}', '${bean.updDatetm}');"
				tabindex="${200 + (2 * status.index) + 1}">削除</button>
			</td>
		</tr>
	</c:forEach>

</table>