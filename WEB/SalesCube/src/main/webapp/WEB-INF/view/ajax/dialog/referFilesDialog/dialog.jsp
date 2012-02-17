<div id="${dialogId}" title="ファイル参照" style="display: none;">
	<span id="${dialogId}_errors" style="color: red">
	</span>

	ファイル件数: ${fileInfoCount}件
	<div style="padding: 0px; border: none; width: 570px; height: 240px; overflow: hidden">
		<table id="${dialogId}List" summary="ファイル一覧" style="width: 560px;">
			<colgroup>
				<col span="1" style="width: 5%">
				<col span="1" style="width: 50%">
				<col span="1" style="width: 15%">
				<col span="1" style="width: 15%">
				<col span="1" style="width: 15%">
			</colgroup>
			<tr>
				<th>No</th>
				<th>タイトル</th>
				<th>サイズ</th>
				<th>登録日時</th>
				<th>登録者</th>
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

	<div style="width: 570px; text-align: right">
		<button type="button" style="width: 70px" tabindex="12050"
			onclick="$('#${dialogId}').dialog('close');">閉じる</button>
	</div>
</div>
