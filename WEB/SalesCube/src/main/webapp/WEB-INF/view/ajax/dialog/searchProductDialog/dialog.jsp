<div id="${dialogId}" title="商品検索" style="display: none;">
	<span id="${dialogId}_errors" style="color: red">
	</span>
	<form onsubmit="return false;">
		<table class="forms" summary="商品検索条件" style="width: 730px;">
			<colgroup>
				<col span="1" style="width: 12%">
				<col span="1" style="width: 20%">
				<col span="1" style="width: 16%">
				<col span="1" style="width: 20%">
				<col span="1" style="width: 12%">
				<col span="1" style="width: 20%">
			</colgroup>
			<tr>
				<th>商品コード</th>
				<td><input id="${dialogId}_productCode" name="${dialogId}_productCode" type="text" value="" style="width: 120px; ime-mode: disabled;" tabindex="5000"
						onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"></td>
				<th>仕入先商品コード</th>
				<td><input id="${dialogId}_supplierPcode" name="${dialogId}_supplierPcode" type="text" value="" style="width: 120px; ime-mode: disabled;" tabindex="5001"></td>
				<th>JANコード</th>
				<td><input id="${dialogId}_janPcode" name="${dialogId}_janPcode" type="text" value="" style="width: 120px; ime-mode: disabled;" tabindex="5002"></td>
			</tr>
			<tr>
				<th>セット分類</th>
				<td>
					<select id="${dialogId}_setTypeCategory" name="${dialogId}_setTypeCategory" tabindex="5003">
						<c:forEach var="bean" items="${setTypeCategoryList}">
							<option value="${bean.value}">${f:h(bean.label)}</option>
						</c:forEach>
					</select>
				</td>
				<th>標準化分類</th>
				<td>
					<select id="${dialogId}_productStandardCategory" name="${dialogId}_productStandardCategory" tabindex="5004">
						<c:forEach var="bean" items="${standardCategoryList}">
							<option value="${bean.value}">${f:h(bean.label)}</option>
						</c:forEach>
					</select>
				</td>
				<th>分類状況</th>
				<td>
					<select id="${dialogId}_productStatusCategory" name="${dialogId}_productStatusCategory" tabindex="5005">
						<c:forEach var="bean" items="${statusCategoryList}">
							<option value="${bean.value}">${f:h(bean.label)}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>

		<table class="forms" summary="商品検索条件" style="width: 730px;">
			<colgroup>
				<col span="1" style="width: 15%">
				<col span="1" style="width: 35%">
				<col span="1" style="width: 15%">
				<col span="1" style="width: 35%">
			</colgroup>
			<tr>
				<th>商品名</th>
				<td><input id="${dialogId}_productName" name="${dialogId}_productName" type="text" value="" style="width: 250px" tabindex="5006"></td>
				<th>商品名カナ</th>
				<td><input id="${dialogId}_productKana" name="${dialogId}_productKana" type="text" value="" style="width: 250px" tabindex="5007"></td>
			</tr>
		</table>

		<table class="forms" summary="商品検索条件" style="width: 730px;">
			<colgroup>
				<col span="1" style="width: 15%">
				<col span="1" style="width: 25%">
				<col span="1" style="width: 15%">
				<col span="1" style="width: 45%">
			</colgroup>
			<tr>
				<th>仕入先コード</th>
				<td><input id="${dialogId}_supplierCode" name="${dialogId}_supplierCode" type="text" value="" style="width: 120px; ime-mode: disabled;" tabindex="5008">
					<html:image src="${f:url('/images/icon_04_02.gif')}" style="vertical-align: middle; cursor: pointer;" tabindex="5009"
						onclick="
							openSearchSupplierDialog('${dialogId}_supplierCodeSearchDialog', _searchSupplierCallBack);
							$('#${dialogId}_supplierCodeSearchDialog_supplierCode').val( $('#${dialogId}_supplierCode').val() );
						"/>
				</td>
				<th>仕入先名</th>
				<td><input id="${dialogId}_supplierName" name="${dialogId}_supplierName" type="text" value="" style="width: 250px" tabindex="5010">
					<html:image src="${f:url('/images/icon_04_02.gif')}" style="vertical-align: middle; cursor: pointer;" tabindex="5011"
						onclick="
							openSearchSupplierDialog('${dialogId}_supplierNameSearchDialog', _searchSupplierCallBack);
							$('#${dialogId}_supplierNameSearchDialog_supplierName').val( $('#${dialogId}_supplierName').val() );
						"/>
				</td>
			</tr>
		</table>

		<div style="width: 730px; text-align: right">
			<button type="reset" style="width: 70px" tabindex="5050" onclick="$('#${dialogId}_errors').empty();">初期化</button>
			<button type="button" style="width: 70px" tabindex="5051"
				onclick="$('#${dialogId}_selectButton').attr('disabled', true);
					_searchProduct( '${dialogId}');">検索</button>
		</div>

	</form>

	<div id="${dialogId}ListContainer">
		検索結果件数： 0件
		<div style="padding: 0px; border: none; width: 750px; height: 220px; overflow: hidden;">
			<table id="${dialogId}List" summary="商品検索結果" style="width: 740px;">
				<colgroup>
					<col span="1" style="width: 10%">
					<col span="1" style="width: 25%">
					<col span="1" style="width: 45%">
					<col span="1" style="width: 20%">
				</colgroup>
				<tr>
					<th>&nbsp;</th>
					<th>商品コード</th>
					<th>商品名</th>
					<th>長さ</th>
				</tr>
			</table>
		</div>
	</div>

	<br>

	<div style="width: 730px; text-align: right">
		<button id="${dialogId}_selectButton" type="button" style="width: 70px" tabindex="5150"
			onclick="_selectSearchResultAjax( '${dialogId}', 'selectedProduct', ProductParams, 'productCode' );
					$('#${dialogId}').dialog('close');" disabled>選択</button>
		<button id="" type="button" style="width: 70px" tabindex="5151"
			onclick="$('#${dialogId}').dialog('close');">閉じる</button>
	</div>

</div>