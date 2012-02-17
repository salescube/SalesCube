<%@page import="jp.co.arkinfosys.common.Constants"%>
<div id="${dialogId}" title="顧客検索" style="display: none;">
	<span id="${dialogId}_errors" style="color: red">
	</span>
	<form>
		<table class="forms" summary="顧客検索条件" style="width: 710px" >
			<colgroup>
				<col span="1" style="width: 20%">
				<col span="1" style="width: 30%">
				<col span="1" style="width: 20%">
				<col span="1" style="width: 30%">
			</colgroup>
			<tr>
				<th>顧客コード</th>
				<td colspan="3">
					<input id="${dialogId}_customerCode" name="${dialogId}_customerCode" type="text" value="" style="width: 150px; ime-mode: disabled;" tabindex="6000"  maxlength="<%=Constants.CODE_SIZE.CUSTOMER%>">
				</td>
			</tr>
			<tr>
				<th>顧客名</th>
				<td><input id="${dialogId}_customerName" name="${dialogId}_customerName" type="text" value="" style="width: 180px;" tabindex="6001"></td>
				<th>顧客名カナ</th>
				<td><input id="${dialogId}_customerKana" name="${dialogId}_customerKana" type="text" value="" style="width: 180px;" tabindex="6002"></td>
			</tr>
			<tr>
				<th>顧客ランク</th>
				<td>
					<select id="${dialogId}_customerRankCategory" name="${dialogId}_customerRankCategory" tabindex="6003">
						<c:forEach var="bean" items="${customerRankCategoryList}">
							<option value="${bean.value}">${f:h(bean.label)}</option>
						</c:forEach>
					</select>
				</td>
				<th>支払条件</th>
				<td>
					<select id="${dialogId}_cutoffGroup" name="${dialogId}_cutoffGroup" tabindex="6004">
						<c:forEach var="bean" items="${cutoffGroupList}">
							<option value="${bean.value}">${f:h(bean.label)}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>

		<div style="width: 710px; text-align: right">
			<button type="reset" style="width: 70px" tabindex="6050" onclick="$('#${dialogId}_errors').empty();">初期化</button>
			<button type="button" style="width: 70px" tabindex="6051"
				onclick="
					$('#${dialogId}_selectButton').attr('disabled', true);
					_searchCustomer( '${dialogId}');">検索</button>
		</div>

	</form>

	<span id="${dialogId}ListContainer">
		検索結果件数: 0件
		<div style="padding: 0px; border: none; width: 710px; height: 240px; overflow: hidden;">
			<table id="${dialogId}List" summary="顧客検索結果" style="width: 700px; margin-top: 0px;">
				<colgroup>
					<col span="1" style="width: 5%">
					<col span="1" style="width: 10%">
					<col span="1" style="width: 20%">
					<col span="1" style="width: 10%">
					<col span="1" style="width: 10%">
					<col span="1" style="width: 8%">
					<col span="1" style="width: 13%">
					<col span="1" style="width: 12%">
					<col span="1" style="width: 12%">
				</colgroup>
				<tr>
					<th>&nbsp;</th>
					<th>顧客コード</th>
					<th>顧客名</th>
					<th>TEL</th>
					<th>担当者</th>
					<th>取引区分</th>
					<th>支払条件</th>
					<th>事業所名</th>
					<th>部署名</th>
				</tr>
			</table>
		</div>
	</span>

	<br>

	<div style="width: 710px; text-align: right">
		<button id="${dialogId}_selectButton" type="button" style="width: 70px" tabindex="6150"
			onclick="_selectSearchResultAjax( '${dialogId}', 'selectedCustomer', CustomerParams, 'customerCode' );
					$('#${dialogId}').dialog('close');" disabled>選択</button>
		<button type="button" style="width: 70px" tabindex="6151"
			onclick="$('#${dialogId}').dialog('close');">閉じる</button>
	</div>

</div>
