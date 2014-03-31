<div style="width: 1010px; height: 25px;">
		<div style="position:absolute; left: 0px;">検索結果件数： ${searchResultCount}件</div>
		<div style="position:absolute; width: 1160px; text-align: center;">
			${sw:pageLink(searchResultCount,rowCount,pageNo)}
		</div>
        <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
</div>

<table  id="search_result" summary="searchResult" class="forms detail_info" style="table-layout: auto; margin-top: 10px;" summary="ファイル一覧">
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
		<th class="rd_top_left" style="height: 30px;">No</th>
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
		<th class="rd_top_right">&nbsp;</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td style="text-align: center">&nbsp;${status.count}&nbsp;</td>
			<td style="white-space: normal"><bean:define id="fileUrl"
				value="${'/setting/fileDownload/download/'}${bean.fileId}" /> <a
				href="javascript:window.location.doHref('${f:url(fileUrl)}');"
				tabindex="${200 + (2 * status.index)}">${f:h(bean.title)}</a></td>
			<td style="white-space: normal">&nbsp;${f:h(bean.fileName)}&nbsp;</td>
			<td style="white-space: normal">&nbsp;${f:h(bean.openLevelName)}&nbsp;</td>
			<td>&nbsp;${bean.creDate}&nbsp;</td>
			<td>&nbsp;${f:h(bean.creUserName)}&nbsp;</td>
			<td>
			<c:if test="${isUpdate}">
				<button class="btn_list_action"  onclick="editFileUpload('${sw:u(bean.fileId)}');">編集</button>
				<button class="btn_list_action"  onclick="deleteFileUpload('${bean.fileId}', '${bean.updDatetm}');"
				tabindex="${200 + (2 * status.index) + 1}">削除</button>
				</c:if>
				<c:if test="${!isUpdate}">
				<button class="btn_list_action"  onclick="editFileUpload'${sw:u(bean.fileId)}');">参照</button>
			</c:if>
			</td>
		</tr>
	</c:forEach>
</table>

<div style="position:absolute; width: 1160px; text-align: center; margin-top: 20px;">
	${sw:pageLink(searchResultCount,rowCount,pageNo)}
</div>
</div>