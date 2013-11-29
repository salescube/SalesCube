<div id="${dialogId}_PORDER_copy" style="display: none;">
	<form style="margin: 0px;">
		<span>発注伝票検索</span>
		<table class="forms" style="width: 600px; margin-top: 10px;" summary="発注伝票検索条件">
			<colgroup>
				<col span="1" style="width: 15%">
				<col span="1" style="width: 30%">
				<col span="1" style="width: 15%">
				<col span="1" style="width: 40%">
			</colgroup>
			<tr>
				<th>発注番号</th>
				<td colspan="3">
					<input type="text"
						id="${dialogId}_porderCondition.poSlipId" name="${dialogId}_porderCondition.poSlipId"
						value="" tabindex="14001" size="15" style="ime-mode: disabled;">
				</td>
			</tr>
			<tr>
				<th>発注日</th>
				<td colspan="3">
					<input type="text"
						id="${dialogId}_porderCondition.poDateFrom" name="${dialogId}_porderCondition.poDateFrom"
						class="date_input" value="${dateFrom}" tabindex="14002" size="15">
					<span style="color:#FFFFFF; font-weight:bold;">～</span>
					<input type="text"
						id="${dialogId}_porderCondition.poDateTo" name="${dialogId}_porderCondition.poDateTo"
						class="date_input" value="" tabindex="14003" size="15">
				</td>
			</tr>
			<tr>
				<th>仕入先コード</th>
				<td><input type="text"
					id="${dialogId}_porderCondition.supplierCode" name="${dialogId}_porderCondition.supplierCode"
					value="" tabindex="14004" size="15" style="ime-mode: disabled;"></td>
				<th>仕入先名</th>
				<td><input type="text"
					id="${dialogId}_porderCondition.supplierName" name="${dialogId}_porderCondition.supplierName"
					value="" tabindex="14005" size="15"></td>
			</tr>

		</table>

		<div style="width: 96%; text-align: right">
			<button type="reset" style="width: 70px" tabindex="14050" onclick="$('#${dialogId}_errors').empty();">初期化</button>
			<button type="button" style="width: 70px" tabindex="14051"
				onclick="$('#${dialogId}_copyButton').attr('disabled', true );
					_searchCopySlip( '${dialogId}', 'PORDER' );">検索</button>
		</div>

		<c:if test="${slipType == 'supplier'}">
		<input type="hidden" id="${dialogId}_porderCondition.onlyRestQuantityExist" name="${dialogId}_porderCondition.onlyRestQuantityExist" value="true">
		</c:if>
		<c:if test="${slipType == 'payment'}">
		<input type="hidden" id="${dialogId}_porderCondition.onlyUnpaid" name="${dialogId}_porderCondition.onlyUnpaid" value="true">
		</c:if>
	</form>


	<div id="${dialogId}_PORDER_ListContainer">
		<div style=" color: #FFFFFF;">検索結果件数： 0件</div>
		<div style="border: none; width: 600px; height: 230px; overflow: hidden;">
			<table class="dialog_resultList" id="${dialogId}_PORDER_List" summary="伝票検索結果" style="width: 96%;">
				<colgroup>
					<col span="1" style="width: 18%">
					<col span="1" style="width: 30%">
					<col span="1" style="width: 17%">
					<col span="1" style="width: 30%">
					<col span="1" style="width: 5%">
				</colgroup>
				<tr>
					<th>発注番号</th>
					<th>発注日</th>
					<th>仕入先コード</th>
					<th>仕入先名</th>
					<th>仕入済</th>
				</tr>
			</table>
		</div>
	</div>

</div>