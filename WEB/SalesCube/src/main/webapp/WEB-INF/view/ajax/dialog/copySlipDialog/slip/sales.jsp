<%@page import="jp.co.arkinfosys.common.Constants"%>
<div id="${dialogId}_SALES_copy" style="display: none;">
	<form style="margin: 0px;">
		<span>売上伝票検索</span>
		<table class="forms" style="width: 600px; margin-top: 10px;" summary="売上伝票検索条件">
			<colgroup>
				<col span="1" style="width: 15%">
				<col span="1" style="width: 30%">
				<col span="1" style="width: 15%">
				<col span="1" style="width: 40%">
			</colgroup>
			<tr>
				<th>売上番号</th>
				<td colspan="3">
					<input type="text"
						id="${dialogId}_salesCondition.salesSlipId" name="${dialogId}_salesCondition.salesSlipId"
						value="" tabindex="14001" size="15" style="ime-mode: disabled;">
				</td>
			</tr>
			<tr>
				<th>売上日</th>
				<td colspan="3">
					<input type="text"
						id="${dialogId}_salesCondition.salesDateFrom" name="${dialogId}_salesCondition.salesDateFrom"
						class="date_input" value="${dateFrom}" tabindex="14002" size="15">
					<span style="color:#FFFFFF; font-weight:bold;">～</span>
					<input type="text"
						id="${dialogId}_salesCondition.salesDateTo" name="${dialogId}_salesCondition.salesDateTo"
						class="date_input" value="" tabindex="14003" size="15">
				</td>
			</tr>
			<tr>
				<th>顧客コード</th>
				<td><input type="text"
					id="${dialogId}_salesCondition.customerCode" name="${dialogId}_salesCondition.customerCode"
					value="" tabindex="14004" size="15" style="ime-mode: disabled;" maxlength="<%=Constants.CODE_SIZE.CUSTOMER%>"></td>
				<th>顧客名</th>
				<td><input type="text"
					id="${dialogId}_salesCondition.customerName" name="${dialogId}_salesCondition.customerName"
					value="" tabindex="14005" size="15"></td>
			</tr>

		</table>

		<div style="width: 96%; text-align: right">
			<button type="reset" style="width: 70px" tabindex="14050" onclick="$('#${dialogId}_errors').empty();">初期化</button>
			<button type="button" style="width: 70px" tabindex="14051"
				onclick="$('#${dialogId}_copyButton').attr('disabled', true);
					_searchCopySlip( '${dialogId}', 'SALES' );">検索</button>
		</div>
	</form>
	
	
	<div id="${dialogId}_SALES_ListContainer" >
		<div style=" color: #FFFFFF;">検索結果件数： 0件</div>
		<div style="border: none; width: 600px; height: 230px; overflow: hidden;">
			<table class="dialog_resultList" id="${dialogId}_SALES_List" summary="伝票検索結果" style="width: 96%;">
				<colgroup>
					<col span="1" style="width: 5%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 50%">
				</colgroup>
				<tr>
					<th>&nbsp;</th>
					<th>売上番号</th>
					<th>売上日</th>
					<th>顧客コード</th>
					<th>顧客名</th>
				</tr>
			</table>
		</div>
	</div>

</div>