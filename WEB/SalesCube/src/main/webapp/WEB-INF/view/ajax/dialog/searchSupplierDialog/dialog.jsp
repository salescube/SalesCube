<div id="${dialogId}" title="仕入先検索" style="display: none;">
	<span id="${dialogId}_errors" style="color: red">
	</span>
	<form>
		<div style="padding: 20px 20px 0 20px;">
			<table class="forms" summary="仕入先検索条件" style="width: 600px">
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
		</div>
	</form>

	<div id="${dialogId}ListContainer"  style="padding: 0 20px;">
		<div style=" color: #FFFFFF;">検索結果件数： 0件</div>
		<div style="border: none; width: 100%; height: 220px; overflow: hidden;">
			<table class="dialog_resultList" id="${dialogId}List" summary="仕入先検索結果" style="width: 100%;">
				<colgroup>
					<col span="1" style="width: 20%">
					<col span="1" style="width: 30%">
					<col span="1" style="width: 20%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 15%">
				</colgroup>
				<tr>
					<th>仕入先コード</th>
					<th>仕入先名</th>
					<th>担当者</th>
					<th>取引区分</th>
					<th>備考</th>
				</tr>
			</table>
		</div>
	</div>

	<div style="width: 96%; text-align: right; margin-top: 15px;">
		<button type="button" style="width: 70px" tabindex="7151"
			onclick="$('#${dialogId}').dialog('close');">閉じる</button>
	</div>
</div>