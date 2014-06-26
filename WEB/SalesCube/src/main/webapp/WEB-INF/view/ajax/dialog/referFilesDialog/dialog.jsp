<div id="${dialogId}" title="ファイル参照" style="display: none;">
	<span id="${dialogId}_errors" style="color: red">
	</span>


	<div id="${dialogId}Div"  style="padding: 20px 20px 0 20px;">
		<div style=" color: #FFFFFF;">ファイル件数: ${fileInfoCount}件</div>
		<div style="border: none; width: 610px; height: 200px; overflow: hidden;">
		<table class="dialog_resultList" id="${dialogId}List" summary="ファイル一覧" style="width: 96%;">
			<colgroup>
				<col span="1" style="width: 5%">
				<col span="1" style="width: 50%">
				<col span="1" style="width: 15%">
				<col span="1" style="width: 15%">
				<col span="1" style="width: 15%">
			</colgroup>
			<tr>
				<th>No</th>
				<th onclick="searchReferFiles('${dialogId}', 'title')">タイトル
					<span id="sortStatus_title" style="color: white">
						<c:if test="${sortColumn == 'title'}">
							<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
							<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
						</c:if>
					</span>
				</th>
				<th onclick="searchReferFiles('${dialogId}', 'fileSize')">サイズ
					<span id="sortStatus_fileSize" style="color: white">
						<c:if test="${sortColumn == 'fileSize'}">
							<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
							<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
						</c:if>
					</span>
				</th>
				<th onclick="searchReferFiles('${dialogId}', 'creDatetm')">登録日時
					<span id="sortStatus_creDatetm" style="color: white">
						<c:if test="${sortColumn == 'creDatetm'}">
							<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
							<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
						</c:if>
					</span>
				</th>
				<th onclick="searchReferFiles('${dialogId}', 'creUser')">登録者
					<span id="sortStatus_creUser" style="color: white">
						<c:if test="${sortColumn == 'creUser'}">
							<c:if test="${sortOrderAsc}"><bean:message key='labels.asc'/></c:if>
							<c:if test="${!sortOrderAsc}"><bean:message key='labels.desc'/></c:if>
						</c:if>
					</span>
				</th>
			</tr>
			<c:forEach var="bean" items="${fileInfoDtoList}" varStatus="status">
			<tr>
				<td style="text-align: center">${status.count}</td>
				<td style="white-space: normal">
					<bean:define id="fileUrl" value="${'/setting/fileDownload/download/'}${bean.fileId}" />
					<a href="javascript:window.location.doHref('${f:url(fileUrl)}');" tabindex="12000">${f:h(bean.title)}</a>
				</td>
				<td>${bean.fileSize}</td>
				<td>${bean.creDatetm}</td>
				<td>${f:h(bean.creUserName)}</td>
			</tr>
			</c:forEach>
		</table>
		</div>
	</div>

	<div style="width: 610px; text-align: right; margin-top:15px;">
		<button type="button" style="width: 70px" tabindex="12050"
			onclick="$('#${dialogId}').dialog('close');">閉じる</button>
	</div>
</div>
