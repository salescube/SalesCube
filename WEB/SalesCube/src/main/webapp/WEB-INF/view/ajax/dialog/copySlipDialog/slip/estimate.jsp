<div id="${dialogId}_ESTIMATE_copy" style="display: none;">
	<form style="margin: 0px;">
		<span>見積伝票検索</span>

		<table class="forms" style="width: 600px; margin-top: 10px;" summary="見積伝票検索条件">
			<colgroup>
				<col span="1" style="width: 15%">
				<col span="1" style="width: 30%">
				<col span="1" style="width: 15%">
				<col span="1" style="width: 40%">
			</colgroup>
			<tr>
				<th>見積番号</th>
				<td colspan="3">
					<input type="text"
						id="${dialogId}_estimateCondition.estimateSheetId" name="${dialogId}_estimateCondition.estimateSheetId"
						value="" tabindex="14001" size="15" style="ime-mode: disabled;">
				</td>
			</tr>
			<tr>
				<th>見積日</th>
				<td colspan="3">
					<input type="text"
						id="${dialogId}_estimateCondition.estimateDateFrom" name="${dialogId}_estimateCondition.estimateDateFrom"
						class="date_input" value="${dateFrom}" tabindex="14001" size="15">
					<span style="color:#FFFFFF; font-weight:bold;">～</span>
					<input type="text"
						id="${dialogId}_estimateCondition.estimateDateTo" name="${dialogId}_estimateCondition.estimateDateTo"
						class="date_input" value="" tabindex="14002" size="15">
				</td>
			</tr>
			<tr>
				<th>提出先名</th>
				<td><input type="text"
					id="${dialogId}_estimateCondition.submitName" name="${dialogId}_estimateCondition.submitName"
					value="" tabindex="14003" size="15"></td>
				<th>件名</th>
				<td><input type="text"
					id="${dialogId}_ESTIMATE_title" name="${dialogId}_estimateCondition.title"
					value="" tabindex="14004" size="15"></td>
			</tr>

		</table>

		<div style="width: 96%; text-align: right">
			<button type="reset" style="width: 70px" tabindex="14050" onclick="$('#${dialogId}_errors').empty();">初期化</button>
			<button type="button" style="width: 70px" tabindex="14051"
				onclick="$('#${dialogId}_copyButton').attr('disabled', true);
					_searchCopySlip( '${dialogId}', 'ESTIMATE' );">検索</button>
		</div>
	</form>

	<div id="${dialogId}_ESTIMATE_ListContainer">
		<div style=" color: #FFFFFF;">検索結果件数： 0件</div>
		<div style="border: none; width: 600px; height: 230px; overflow: hidden;">
			<table class="dialog_resultList" id="${dialogId}_ESTIMATE_List" summary="伝票検索結果" style="width: 96%;">
				<colgroup>
					<col span="1" style="width: 15%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 25%">
					<col span="1" style="width: 45%">
				</colgroup>
				<tr>
					<th>見積番号</th>
					<th>見積日</th>
					<th>提出先名</th>
					<th>件名</th>
				</tr>
			</table>
		</div>
	</div>

</div>