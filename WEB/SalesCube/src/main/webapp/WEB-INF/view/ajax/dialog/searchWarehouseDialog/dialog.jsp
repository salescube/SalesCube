<div id="${dialogId}" title="倉庫検索" style="display: none;">
	<span id="${dialogId}_errors" style="color: red">
	</span>
	<form>
		<div style="padding: 20px 20px 0 20px;">
		<table class="forms" summary="倉庫検索条件" style="width: 550px;">
			<tr>
				<th>倉庫コード</th>
				<td><input id="${dialogId}_warehouseCode" name="${dialogId}_warehouseCode" type="text" value="" style="width:80px; ime-mode: disabled;" tabindex="10000"></td>
				<th>倉庫名</th>
				<td><input id="${dialogId}_warehouseName" name="${dialogId}_warehouseName" type="text" value="" style="width:150px;" tabindex="10001"></td>
			</tr>
			<tr>
				<th>倉庫状態</th>
				<td colspan="3">
				<select id="${dialogId}_warehouseState" name="${dialogId}_warehouseState" tabindex="10003">
					<option value=""></option>
					<option value="運用中">運用中</option>
					<option value="棚卸中">棚卸中</option>
					<option value="廃止">廃止</option>
				</select>
				</td>
			</tr>
		</table>

		<div style="width: 550px; text-align: right">
			<button type="reset" style="width: 70px" tabindex="10050" onclick="$('#${dialogId}_errors').empty();">初期化</button>
			<button type="button" style="width: 70px" tabindex="10051"
				onclick="$('#${dialogId}_selectButton').attr('disabled', true);
					_searchWarehouse( '${dialogId}');">検索</button>
		</div>
	</div>
	</form>

	<div id="${dialogId}ListContainer"  style="padding: 0 20px;">
		<div style=" color: #FFFFFF;">検索結果件数： 0件</div>
		<div style="border: none; width: 100%; height: 220px; overflow: hidden;">
			<table class="dialog_resultList" id="${dialogId}List" summary="倉庫検索結果" style="width: 100%;">
				<colgroup>
					<col span="1" style="width: 30%">
					<col span="1" style="width: 40%">
					<col span="1" style="width: 30%">
				</colgroup>
				<tr>
					<th>倉庫コード</th>
					<th>倉庫名</th>
					<th>倉庫状態</th>
				</tr>
			</table>
		</div>
	</div>

	<div style="width: 96%; text-align: right; margin-top: 15px;">
		<button id="" type="button" style="width: 70px" tabindex="10151"
			onclick="$('#${dialogId}').dialog('close');">閉じる</button>
	</div>
</div>