<div id="${dialogId}_SUPPLIER_copy" style="display: none;">
	<form style="margin: 0px;">
		<span>仕入伝票検索</span>
		<table class="forms" style="width: 600px; margin-top: 10px;" summary="仕入伝票検索条件">
			<colgroup>
				<col span="1" style="width: 15%">
				<col span="1" style="width: 30%">
				<col span="1" style="width: 15%">
				<col span="1" style="width: 40%">
			</colgroup>
			<tr>
				<th>仕入番号</th>
				<td>
					<input type="text"
						id="${dialogId}_supplierCondition.supplierSlipId" name="${dialogId}_supplierCondition.supplierSlipId"
						value="" tabindex="14001" size="15" style="ime-mode: disabled;">
				</td>
				<th>未払い分のみ</th>
				<td>
					<input type="checkbox"
						id="${dialogId}_supplierCondition.unPaidFlag" name="${dialogId}_supplierCondition.unpaidFlag"
						value="true" tabindex="14002" size="15">
				</td>
			</tr>
			<tr>
				<th>仕入日</th>
				<td colspan="3">
					<input type="text"
						id="${dialogId}_supplierCondition.supplierDateFrom" name="${dialogId}_supplierCondition.supplierDateFrom"
						class="date_input" value="${dateFrom}" tabindex="14003" size="15">
					<span style="color:#FFFFFF; font-weight:bold;">～</span>
					<input type="text"
						id="${dialogId}_supplierCondition.supplierDateTo" name="${dialogId}_supplierCondition.supplierDateTo"
						class="date_input" value="" tabindex="14004" size="15">
				</td>
			</tr>
			<tr>
				<th>仕入先コード</th>
				<td><input type="text"
					id="${dialogId}_supplierCondition.supplierCode" name="${dialogId}_supplierCondition.supplierCode"
					value="" tabindex="14005" size="15" style="ime-mode: disabled;"></td>
				<th>仕入先名</th>
				<td><input type="text"
					id="${dialogId}_supplierCondition.supplierName" name="${dialogId}_supplierCondition.supplierName"
					value="" tabindex="14006" size="15"></td>
			</tr>

		</table>

		<div style="width: 96%; text-align: right">
			<button type="reset" style="width: 70px" tabindex="14050" onclick="$('#${dialogId}_errors').empty();">初期化</button>
			<button type="button" style="width: 70px" tabindex="14051"
				onclick="$('#${dialogId}_copyButton').attr('disabled', true );
					_searchCopySlip( '${dialogId}', 'SUPPLIER' );">検索</button>
		</div>
	</form>

	<div id="${dialogId}_SUPPLIER_ListContainer">
		<div style=" color: #FFFFFF;">検索結果件数： 0件</div>
		<div style="border: none; width: 600px; height: 230px; overflow: hidden;">
			<table class="dialog_resultList" id="${dialogId}_SUPPLIER_List" summary="伝票検索結果" style="width: 96%;">
				<colgroup>
					<col span="1" style="width: 5%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 50%">
				</colgroup>
				<tr>
					<th>&nbsp;</th>
					<th>仕入番号</th>
					<th>仕入日</th>
					<th>仕入先コード</th>
					<th>仕入先名</th>
				</tr>
			</table>
		</div>
	</div>

</div>