<div id="${dialogId}" title="郵便番号検索" style="display: none;">
	<span id="${dialogId}_errors" style="color: red">
	</span>
	<form>
	<div style="padding: 20px 20px 0 20px;">
		<table class="forms" summary="郵便番号検索条件" style="width: 600px">
			<tr>
				<th>郵便番号</th>
				<td><input id="${dialogId}_zipCode" name="${dialogId}_zipCode" type="text" value="" style="width: 130px; ime-mode: disabled;" tabindex="8000"></td>
			</tr>
			<tr>
				<th>住所１</th>
				<td><input id="${dialogId}_zipAddress1" name="${dialogId}_zipAddress1" type="text" value="" style="width: 350px; ime-mode: active;" tabindex="8001"></td>
			</tr>
		</table>

		<div style="width: 600px; text-align: right;">
			<button type="reset" style="width: 70px;" tabindex="8050" onclick="$('#${dialogId}_errors').empty();">初期化</button>
			<button type="button" style="width: 70px" tabindex="8051" onclick="$('#${dialogId}_selectButton').attr('disabled', true); _searchZipCode( '${dialogId}');">検索</button>
		</div>
	</div>
	</form>

	<div id="${dialogId}ListContainer"  style="padding: 0 20px;">
		<div style=" color: #FFFFFF;">検索結果件数： 0件</div>
		<div style="border: none; width: 100%; height: 260px; overflow: hidden;">
			<table class="dialog_resultList" id="${dialogId}List" summary="住所検索結果" style="width: 100%;">
				<colgroup>
					<col span="1" style="width: 20%">
					<col span="1" style="width: 40%">
					<col span="1" style="width: 40%">
				</colgroup>
				<tr>
					<th style="width: 20%">郵便番号</th>
					<th style="width: 40%">住所１</th>
					<th style="width: 40%">住所２</th>
				</tr>
			</table>
		</div>
	</div>

	<div style="width: 96%; text-align: right; margin-top: 15px;">
		<button type="button" style="width: 70px" tabindex="8151"
			onclick="$('#${dialogId}').dialog('close');">閉じる</button>
	</div>
</div>