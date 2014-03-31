<div id="${dialogId}" title="検索結果設定" style="display: none;">
	<span id="${dialogId}_errors" style="color: red">
	</span>
	
	<div style="padding: 10px 20px 0 20px;">検索対象：${targetName}</div>
	
	<form>
		<div style="padding: 20px 20px 0 20px;">
		
		<input type="hidden" id="${dialogId}_menuId" name="${dialogId}_menuId" value="${menuId}">
		<input type="hidden" id="${dialogId}_target" name="${dialogId}_target" value="${target}">

		<table style="width: 460px;">
			<colgroup>
				<col span="1" style="width: 40%">
				<col span="1" style="width: 10%">
				<col span="1" style="width: 40%">
				<col span="1" style="width: 10%">
			</colgroup>
			<tr>
				<td>
					<div style=" color: #FFFFFF;">非表示項目リスト</div>
					<select id="${dialogId}_disabledItemList" size="10" style="width: 200px; height: 200px; background:none; background-color: #fffadf;" tabindex="13000">
						<c:forEach var="bean" items="${disabledItemList}">
							<option value="${bean.value}">${f:h(bean.label)}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<button type="button" style="width: 30px; margin-bottom: 5px;" tabindex="13001"
						onclick="_enableDispItem('${dialogId}');">→</button><br>
					<button type="button" style="width: 30px" tabindex="13002"
						onclick="_disableDispItem('${dialogId}');">←</button>
				</td>
				<td>
					<div style=" color: #FFFFFF;">表示項目リスト</div>
					<select id="${dialogId}_enabledItemList" size="10" style="width: 200px; height: 200px; background:none; background-color: #fffadf;" tabindex="13003">
						<c:forEach var="bean" items="${enabledItemList}">
							<option value="${bean.value}">${f:h(bean.label)}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<button type="button" style="width: 30px; margin-bottom: 5px;" tabindex="13004"
						onclick="_upDispItem('${dialogId}');">↑</button><br>
					<button type="button" style="width: 30px" tabindex="13005"
						onclick="_downDispItem('${dialogId}');">↓</button>
				</td>
			</tr>
		</table>

		<div style="width: 450px; text-align: right; margin-top: 20px;">
			<button id="${dialogId}_initButton" type="button" style="width: 70px" tabindex="13050"
				onclick="
					if(!confirm('<bean:message key="confirm.init"/>')) { return false; }
					_initDispItem('${dialogId}'); $('#${dialogId}_errors').empty();">初期化</button>
			<button id="${dialogId}_selectButton" type="button" style="width: 70px" tabindex="13051"
				onclick="_updateDetailDispSetting('${dialogId}');">更新</button>
			<button type="button" style="width: 70px" tabindex="13052"
				onclick="$('#${dialogId}').dialog('close');">閉じる</button>
		</div>
		</div>
	</form>

	<%-- リストの初期状態を記憶 --%>
	<input type="hidden" id="${dialogId}_originalEnabledItemList" value="${f:h(originalEnabledItemList)}">
	<input type="hidden" id="${dialogId}_originalDisabledItemList" value="${f:h(originalDisabledItemList)}">
	<input type="hidden" id="${dialogId}_requiredItemList" value="${f:h(requiredDispItemIdList)}">
	<input type="hidden" id="${dialogId}_lockItemId" value="${f:h(lockItemId)}">
	<input type="hidden" id="${dialogId}_lockUpdDatetm" value="${f:h(lockUpdDatetm)}">
</div>