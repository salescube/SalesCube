<%@page import="jp.co.arkinfosys.common.Constants"%>
<div id="${dialogId}_RORDER_copy" style="display: none;">
	<form style="margin: 0px;">
		<span>受注伝票検索</span>

		<table class="forms" style="width: 600px; margin-top: 10px;" summary="受注伝票検索条件">
			<tr>
				<th>受注番号</th>
				<td>
					<input type="text"
						id="${dialogId}_rorderCondition.roSlipId" name="${dialogId}_rorderCondition.roSlipId"
						value="" tabindex="14001" size="15" style="width: 150px; ime-mode: disabled;">
				</td>
				<th>受付番号</th>
				<td>
					<input type="text"
						id="${dialogId}_rorderCondition.receptNo" name="${dialogId}_rorderCondition.receptNo"
						value=""  style="width: 150px; ime-mode: disabled;" tabindex="14002" size="15">
				</td>
			</tr>
			<tr>
				<th>受注日</th>
				<td colspan="3">
					<input type="text"
						id="${dialogId}_rorderCondition.roDateFrom" name="${dialogId}_rorderCondition.roDateFrom"
						class="date_input" value="${dateFrom}" tabindex="14003" size="15">
					<span style="color:#FFFFFF; font-weight:bold;">～</span>
					<input type="text"
						id="${dialogId}_rorderCondition.roDateTo" name="${dialogId}_rorderCondition.roDateTo"
						class="date_input" value="" tabindex="14004" size="15">
				</td>
			</tr>
			<tr>
				<th>出荷日</th>
				<td colspan="3">
					<input type="text"
						id="${dialogId}_rorderCondition.shipDateFrom" name="${dialogId}_rorderCondition.shipDateFrom"
						class="date_input" value="" tabindex="14005" size="15">
					<span style="color:#FFFFFF; font-weight:bold;">～</span>
					<input type="text"
						id="${dialogId}_rorderCondition.shipDateTo" name="${dialogId}_rorderCondition.shipDateTo"
						class="date_input" value="" tabindex="14006" size="15">
				</td>
			</tr>
			<tr>
				<th>顧客コード</th>
				<td><input type="text"
					id="${dialogId}_rorderCondition.customerCode" name="${dialogId}_rorderCondition.customerCode"
					value="" style="width: 150px; ime-mode: disabled;" tabindex="14007" size="15" maxlength="<%=Constants.CODE_SIZE.CUSTOMER%>"></td>
				<th>顧客名</th>
				<td><input type="text"
					id="${dialogId}_rorderCondition.customerName" name="${dialogId}_rorderCondition.customerName"
					value="" style="width: 200px;" tabindex="14008" size="15"></td>
			</tr>
		</table>

		<div style="width: 96%; text-align: right">
			<button type="reset" style="width: 70px" tabindex="14050" onclick="$('#${dialogId}_errors').empty();">初期化</button>
			<button type="button" style="width: 70px" tabindex="14051"
				onclick="$('#${dialogId}_copyButton').attr('disabled', true);
					_searchCopySlip( '${dialogId}', 'RORDER' );">検索</button>
		</div>

		<input type="hidden" id="${dialogId}_rorderCondition.restOnly" name="${dialogId}_rorderCondition.restOnly" value="1">
	</form>

	<div id="${dialogId}_RORDER_ListContainer">
		<div style=" color: #FFFFFF;">検索結果件数： 0件</div>
		<div style="border: none; width: 600px; height: 230px; overflow: hidden;">
			<table class="dialog_resultList" id="${dialogId}_RORDER_List" summary="伝票検索結果" style="width: 96%;">
				<colgroup>
					<col span="1" style="width: 15%">
					<col span="1" style="width: 18%">
					<col span="1" style="width: 17%">
					<col span="1" style="width: 50%">
				</colgroup>
				<tr>
					<th>受注番号</th>
					<th>受注日</th>
					<th>顧客コード</th>
					<th style="width: 50%">顧客名</th>
				</tr>
			</table>
		</div>
	</div>
</div>