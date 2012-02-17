<div id="${dialogId}" title="数量割引検索" style="display: none;">
	<span id="${dialogId}_errors" style="color: red">
	</span>
	<form>
		<table class="forms" summary="数量割引検索条件" style="width: 780px">
			<colgroup>
				<col span="1" style="width: 15%">
				<col span="1" style="width: 25%">
				<col span="1" style="width: 20%">
				<col span="1" style="width: 40%">
			</colgroup>
			<tr>
				<th>割引コード</th>
				<td><input id="${dialogId}_discountId" name="${dialogId}_discountId" type="text" value="" style="width: 120px; ime-mode: disabled;"  tabindex="16000"></td>
				<th>割引有効</th>
				<td><select id="${dialogId}_useFlag" name="${dialogId}_useFlag" tabindex="16001">
						<c:forEach var="bean" items="${useFlagList}">
							<option value="${bean.value}">${f:h(bean.label)}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<th>割引名</th>
				<td colspan="3"><input id="${dialogId}_discountName" name="${dialogId}_discountName" type="text" value="" style="width: 250px"  tabindex="16002"></td>
			</tr>
			<tr>
				<th>備考</th>
				<td colspan="3"><input id="${dialogId}_remarks" name="${dialogId}_remarks" type="text" value="" style="width: 430px"  tabindex="16003"></td>
			</tr>
		</table>

		<div style="width: 780px; text-align: right">
			<button type="reset" style="width: 70px" tabindex="16050" onclick="$('#${dialogId}_errors').empty();">初期化</button>
			<button type="button" style="width: 70px" tabindex="16051"
				onclick="$('#${dialogId}_selectButton').attr('disabled', true);
					_searchDiscount( '${dialogId}');
					_clearDiscountData( '${dialogId}');">検索</button>
		</div>
	</form>

	<table>
		<tr>
			<td id="${dialogId}ListContainer">
				検索結果件数： 0件
				<div style="padding: 0px; border: none; width: 500px; height: 220px; overflow: hidden;">
					<table id="${dialogId}List" summary="数量割引検索結果" style="width: 490px;">
						<colgroup>
							<col span="1" style="width: 10%">
							<col span="1" style="width: 20%">
							<col span="1" style="width: 60%">
							<col span="1" style="width: 10%">
						</colgroup>
						<tr>
							<th>&nbsp;</th>
							<th>割引コード</th>
							<th>割引名</th>
							<th>割引有効</th>
						</tr>
					</table>
				</div>
			</td>
			<td id="${dialogId}DataListContainer" style="padding-top:35px">
				<div style="padding: 0px; border: none; width: 300px; height: 240px; overflow: hidden">
					<table id="${dialogId}DataList" summary="割引データ一覧" style="width: 290px;">
						<colgroup>
							<col span="1" style="width: 20%">
							<col span="1" style="width: 60%">
							<col span="1" style="width: 20%">
						</colgroup>
						<tr>
							<th>No</th>
							<th>数量範囲</th>
							<th>掛率</th>
						</tr>
					</table>
				</div>
			</td>
		</tr>
	</table>

	<br>

	<div style="width: 780px; text-align: right">
		<button id="${dialogId}_selectButton" type="button" style="width: 70px" tabindex="16150"
			onclick="_selectSearchResult( '${dialogId}', 'selectedDiscount' );
					$('#${dialogId}').dialog('close');" disabled>選択</button>
		<button id="" type="button" style="width: 70px" tabindex="16151"
			onclick="$('#${dialogId}').dialog('close');">閉じる</button>
	</div>

	
	<div id="templateDiv" style="padding: 0px; border: none; width: 300px; height: 240px; overflow: hidden; display: none">
		<table id="template${dialogId}DataList" summary="割引データ一覧" style="width: 290px;">
			<colgroup>
				<col span="1" style="width: 20%">
				<col span="1" style="width: 60%">
				<col span="1" style="width: 20%">
			</colgroup>
			<tr>
				<th>No</th>
				<th>数量範囲</th>
				<th>掛率</th>
			</tr>
		</table>
	</div>
</div>