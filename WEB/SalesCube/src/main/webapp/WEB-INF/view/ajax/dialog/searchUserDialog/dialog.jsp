<div id="${dialogId}" title="担当者検索" style="display: none;">
	<span id="${dialogId}_errors" style="color: red">
	</span>
	<form>
		<table class="forms" summary="担当者検索条件" style="width: 650px">
			<colgroup>
				<col span="1" style="width: 20%">
				<col span="1" style="width: 30%">
				<col span="1" style="width: 20%">
				<col span="1" style="width: 30%">
			</colgroup>
			<tr>
				<th>担当者コード</th>
				<td colspan="3"><input id="${dialogId}_userId" name="${dialogId}_userId" type="text" value="" style="width: 130px; ime-mode: disabled;" tabindex="9000"></td>
			</tr>
			<tr>
				<th>担当者名</th>
				<td><input id="${dialogId}_nameKnj" name="${dialogId}_nameKnj" type="text" value="" style="width: 150px;" tabindex="9001"></td>
				<th>担当者カナ</th>
				<td><input id="${dialogId}_nameKana" name="${dialogId}_nameKana" type="text" value="" style="width: 150px;" tabindex="9002"></td>
			</tr>
			<tr>
				<th>部門</th>
				<td>
					<select id="${dialogId}_deptId" name="${dialogId}_deptId" tabindex="9003">
						<c:forEach var="bean" items="${deptList}">
							<option value="${bean.value}">${f:h(bean.label)}</option>
						</c:forEach>
					</select>
				</td>
				<th>権限</th>
				<td>
					<select id="${dialogId}_roleId" name="${dialogId}_roleId" tabindex="9004">
						<c:forEach var="bean" items="${roleList}">
							<option value="${bean.value}">${f:h(bean.label)}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>

		<div style="width: 650px; text-align: right">
			<button type="reset" style="width: 70px" tabindex="9050" onclick="$('#${dialogId}_errors').empty();">初期化</button>
			<button type="button" style="width: 70px" tabindex="9051"
				onclick="
					$('#${dialogId}_selectButton').attr('disabled', true);
					_searchUser( '${dialogId}');">検索</button>
		</div>
	</form>

	<span id="${dialogId}ListContainer">
		検索結果件数: 0件
		<div style="padding: 0px; border: none; width: 650px; height: 240px; overflow: hidden">
			<table id="${dialogId}List" summary="担当者検索結果" style="width: 640px;">
				<colgroup>
					<col span="1" style="width: 10%">
					<col span="1" style="width: 20%">
					<col span="1" style="width: 35%">
					<col span="1" style="width: 35%">
				</colgroup>
				<tr>
					<th>&nbsp;</th>
					<th>担当者コード</th>
					<th>担当者名</th>
					<th>部門</th>
				</tr>
			</table>
		</div>
	</span>

	<br>

	<div style="width: 650px; text-align: right">
		<button id="${dialogId}_selectButton" type="button" style="width: 70px" tabindex="9150"
			onclick="_selectSearchResult( '${dialogId}', 'selectedUserId' );
					$('#${dialogId}').dialog('close');" disabled>選択</button>
		<button type="button" style="width: 70px" tabindex="9151"
			onclick="$('#${dialogId}').dialog('close');">閉じる</button>
	</div>

</div>
