<div id="${dialogId}" title="仕入先検索" style="display: none;">
	<span id="${dialogId}_errors" style="color: red">
	</span>
	<form>
		<table class="forms" summary="仕入先検索条件" style="width: 600px">
			<colgroup>
				<col span="1" style="width: 20%">
				<col span="1" style="width: 30%">
				<col span="1" style="width: 20%">
				<col span="1" style="width: 30%">
			</colgroup>
			<tr>
				<th>仕入先コード</th>
				<td colspan="3">
					<input id="${dialogId}_supplierCode" name="${dialogId}_supplierCode" type="text" value="" style="width: 150px; ime-mode: disabled;" tabindex="7000">
				</td>
			</tr>
			<tr>
				<th>仕入先名</th>
				<td>
					<input id="${dialogId}_supplierName" name="${dialogId}_supplierName" type="text" value="" style="width: 150px;" tabindex="7001">
				</td>
				<th>仕入先名カナ</th>
				<td>
					<input id="${dialogId}_supplierKana" name="${dialogId}_supplierKana" type="text" value="" style="width: 150px;" tabindex="7002">
				</td>
			</tr>
		</table>

		<div style="width: 600px; text-align: right">
			<button type="reset" style="width: 70px" tabindex="7050" onclick="$('#${dialogId}_errors').empty();">初期化</button>
			<button type="button" style="width: 70px" tabindex="7051"
				onclick="
					$('#${dialogId}_selectButton').attr('disabled', true);
					_searchSupplier( '${dialogId}');">検索</button>
		</div>
	</form>

	<span id="${dialogId}ListContainer">
		検索結果件数: 0件
		<div style="padding: 0px; border: none; width: 620px; height: 240px; overflow: hidden;">
			<table id="${dialogId}List" summary="仕入先検索結果" style="width: 600px;">
				<colgroup>
					<col span="1" style="width: 10%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 25%">
					<col span="1" style="width: 20%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 15%">
				</colgroup>
				<tr>
					<th>&nbsp;</th>
					<th>仕入先コード</th>
					<th>仕入先名</th>
					<th>担当者</th>
					<th>取引区分</th>
					<th>備考</th>
				</tr>
			</table>
		</div>
	</span>

	<div style="width: 600px; text-align: right">
		<button id="${dialogId}_selectButton" type="button" style="width: 70px" tabindex="7150"
			onclick="_selectSearchResult( '${dialogId}', 'selectedSupplier' );
					$('#${dialogId}').dialog('close');" disabled>選択</button>
		<button type="button" style="width: 70px" tabindex="7151"
			onclick="$('#${dialogId}').dialog('close');">閉じる</button>
	</div>
</div>