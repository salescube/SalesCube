<div id="${dialogId}" title="郵便番号検索" style="display: none;">
	<span id="${dialogId}_errors" style="color: red">
	</span>

	<form>
		<table class="forms" summary="郵便番号検索条件" style="width: 600px">
			<colgroup>
				<col span="1" style="width: 30%">
				<col span="1" style="width: 70%">
			</colgroup>
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
	</form>

	<div id="${dialogId}ListContainer">
		検索結果件数: 0件
		<div style="padding: 0px; border: none; width: 600px; height: 280px; overflow: hidden;">
			<table id="${dialogId}List" summary="住所検索結果" style="width: 590px">
				<colgroup>
					<col span="1" style="width: 10%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 40%">
					<col span="1" style="width: 35%">
				</colgroup>
				<tr>
					<th>&nbsp;</th>
					<th>郵便番号</th>
					<th>住所１</th>
					<th>住所２</th>
				</tr>
			</table>
		</div>
	</div>

	<div style="width: 600px; text-align: right; margin-top: 10px;">
		<button id="${dialogId}_selectButton" type="button" style="width: 70px" tabindex="8150"
			onclick="_selectSearchResult( '${dialogId}', 'selectedZipId' );
					$('#${dialogId}').dialog('close');" disabled>選択</button>
		<button type="button" style="width: 70px" tabindex="8151"
			onclick="$('#${dialogId}').dialog('close');">閉じる</button>
	</div>
</div>