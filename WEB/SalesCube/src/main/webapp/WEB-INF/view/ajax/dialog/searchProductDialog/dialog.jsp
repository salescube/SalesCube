<div id="${dialogId}" title="商品検索" style="display: none;">
	<span id="${dialogId}_errors" style="color: red">
	</span>
	<form onsubmit="return false;">
	<div style="padding: 20px 20px 0 20px;">
		<table class="forms" summary="商品検索条件" style="width: 730px;">
			<tr>
				<th>商品コード</th>
				<td><input id="${dialogId}_productCode" name="${dialogId}_productCode" type="text" value="" style="width: 140px; ime-mode: disabled;" tabindex="5000"
						onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"></td>
				<th>仕入先商品コード</th>
				<td><input id="${dialogId}_supplierPcode" name="${dialogId}_supplierPcode" type="text" value="" style="width: 140px; ime-mode: disabled;" tabindex="5001"></td>
				<th>JANコード</th>
				<td><input id="${dialogId}_janPcode" name="${dialogId}_janPcode" type="text" value="" style="width: 140px; ime-mode: disabled;" tabindex="5002"></td>
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
			<tr>
				<th>仕入先コード</th>
				<td><input id="${dialogId}_supplierCode" name="${dialogId}_supplierCode" type="text" value="" style="width: 120px; ime-mode: disabled;" tabindex="5008">
					<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" tabindex="5009"
						onclick="
							openSearchSupplierDialog('${dialogId}_supplierCodeSearchDialog', _searchSupplierCallBack);
							$('#${dialogId}_supplierCodeSearchDialog_supplierCode').val( $('#${dialogId}_supplierCode').val() );
						"/>
				</td>
				<th>仕入先名</th>
				<td><input id="${dialogId}_supplierName" name="${dialogId}_supplierName" type="text" value="" style="width: 250px" tabindex="5010">
					<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" tabindex="5011"
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
	</div>
	</form>

	<div id="${dialogId}ListContainer"  style="padding: 0 20px;">
		<div style=" color: #FFFFFF;">検索結果件数： 0件</div>
		<div style="border: none; width: 100%; height: 220px; overflow: hidden;">
			<table class="dialog_resultList" id="${dialogId}List" summary="商品検索結果" style="width: 96%;">
				<col span="1" style="width: 30%">
				<col span="1" style="width: 50%">
				<col span="1" style="width: 20%">
				<tr>
					<th>商品コード</th>
					<th>商品名</th>
					<th>仕入先名</th>
				</tr>
			</table>
		</div>
	</div>

	<div style="width: 90%; text-align: right; margin-top: 15px;">
		<button id="" type="button" style="width: 70px" tabindex="5151"
			onclick="$('#${dialogId}').dialog('close');">閉じる</button>
	</div>
</div>