<div id="${dialogId}" title="<bean:message key='titles.productInfos'/>" style="display: none;">


		<table id="product_info" class="forms" summary="product_info" style="table-layout:fixed;">
			<colgroup>
				<col span="1" style="word-break:break-all;width:118px;">
				<col span="1" style="word-break:break-all;width:182px;">
				<col span="1" style="word-break:break-all;width: 91px;">
				<col span="1" style="word-break:break-all;width:209px;">
				<col span="1" style="word-break:break-all;width: 91px;">
				<col span="1" style="word-break:break-all;width:209px;">
			</colgroup>
			<tr>
				<th><bean:message key='labels.productCode'/></th><!-- 商品コード -->
				<td>${f:h(productInfosWithNameDto.productCode)}</td>
				<th><bean:message key='labels.productName'/></th><!-- 商品名 -->
				<td>${f:h(productInfosWithNameDto.productName)}</td>
				<th><bean:message key='labels.productKana'/></th><!-- 商品名カナ -->
				<td>${f:h(productInfosWithNameDto.productKana)}</td>
			</tr>
			<tr>
				<th><bean:message key='labels.onlineorder.pcode'/></th><!-- 通販サイト品番 -->
				<td>${f:h(productInfosWithNameDto.onlinePcode)}</td>
				<th><bean:message key='labels.janPcode'/></th><!-- JANコード -->
				<td>${f:h(productInfosWithNameDto.janPcode)}</td>
				<th><bean:message key='labels.discardDate'/></th><!-- 廃番予定日 -->
				<td>${f:h(productInfosWithNameDto.discardDate)}</td>
			</tr>
		</table>

		<table id="supplier_info" class="forms" summary="supplier_info" style="table-layout:fixed;">
			<colgroup>
				<col span="1" style="word-break:break-all;width:118px;">
				<col span="1" style="word-break:break-all;width:182px;">
				<col span="1" style="word-break:break-all;width:118px;">
				<col span="1" style="word-break:break-all;width:182px;">
				<col span="1" style="word-break:break-all;width:118px;">
				<col span="1" style="word-break:break-all;width:182px;">
			</colgroup>
			<tr>
				<th><bean:message key='labels.supplierCode'/></th><!-- 仕入先コード -->
				<td>${f:h(productInfosWithNameDto.supplierCode)}</td>
				<th><bean:message key='labels.supplierName'/></th><!-- 仕入先名 -->
				<td>${f:h(productInfosWithNameDto.supplierName)}</td>
				<th><bean:message key='labels.purchasePcode'/></th><!-- 仕入先商品コード -->
				<td>${f:h(productInfosWithNameDto.supplierPcode)}</td>
			</tr>
			<tr>
				<th><bean:message key='labels.supplierPriceYen'/></th><!-- 仕入単価（円） -->
				<td>${f:h(productInfosWithNameDto.supplierPriceYen)}</td>
				<th><bean:message key='labels.supplierPriceDol'/></th><!-- 仕入単価（外貨） -->
				<td>${f:h(productInfosWithNameDto.supplierPriceDol)}</td>
				<th><bean:message key='labels.unitRetailPrice'/></th><!-- 売単価 -->
				<td>${f:h(productInfosWithNameDto.retailPrice)}</td>
			</tr>
		</table>

		<table id="product_info_2" class="forms" summary="product_info_2" style="table-layout:fixed;">
			<colgroup>
				<col span="1" style="word-break:break-all;width:118px;">
				<col span="1" style="word-break:break-all;width:182px;">
				<col span="1" style="word-break:break-all;width:118px;">
				<col span="1" style="word-break:break-all;width:182px;">
				<col span="1" style="word-break:break-all;width:118px;">
				<col span="1" style="word-break:break-all;width:182px;">
			</colgroup>
			<tr>
				<th><bean:message key='labels.stockCtlCategory'/></th><!-- 在庫管理 -->
				<td>${f:h(productInfosWithNameDto.stockCtlCategoryName)}</td>
				<th><bean:message key='labels.packQuantity'/></th><!-- 入数 -->
				<td colspan="3">${f:h(productInfosWithNameDto.packQuantity)}</td>
			</tr>
			<tr>
				<th><bean:message key='labels.avgShipCount'/></th><!-- 月平均出荷数 -->
				<td>${f:h(productInfosWithNameDto.avgShipCount)}</td>
				<th><bean:message key='labels.rackCode'/></th><!-- 棚番 -->
				<td colspan="3">${f:h(productInfosWithNameDto.rackCode)}</td>
			</tr>
			<tr>
				<th><bean:message key='labels.leadTime'/></th><!-- リードタイム -->
				<td>${f:h(productInfosWithNameDto.leadTime)} <bean:message key='words.unit.day'/></td><!-- 日 -->
				<th><bean:message key='labels.poNum'/></th><!-- 発注点 -->
				<td colspan="3">${f:h(productInfosWithNameDto.poNum)}</td>
			</tr>
			<tr>
				<th><bean:message key='labels.poLot'/></th><!-- 発注ロット -->
				<td>${f:h(productInfosWithNameDto.poLot)}</td>
				<th><bean:message key='labels.maxStockNum'/></th><!-- 最大在庫保有数 -->
				<td>${f:h(productInfosWithNameDto.maxStockNum)}</td>
				<th><bean:message key='labels.maxPoNum'/></th><!-- 単位発注限度数 -->
				<td>${f:h(productInfosWithNameDto.maxPoNum)}</td>
			</tr>
		</table>

		<table id="rank_info" class="forms" summary="rank_info" style="table-layout:fixed;">
			<colgroup>
				<col span="1" style="word-break:break-all;width:118px;">
				<col span="1" style="word-break:break-all;width:182px;">
				<col span="1" style="word-break:break-all;width:118px;">
				<col span="1" style="word-break:break-all;width:482px;">
			</colgroup>
			<tr>
				<th><bean:message key='labels.roMaxNum'/></th><!-- 受注限度数 -->
				<td>${f:h(productInfosWithNameDto.roMaxNum)}</td>
				<th><bean:message key='labels.discountName'/></th><!-- 数量割引 -->
				<td>${f:h(productInfosWithNameDto.discountName)}</td>
			</tr>
		</table>

		<table id="product_category_info" class="forms" summary="product_category_info" style="table-layout:fixed;">
			<colgroup>
				<col span="1" style="word-break:break-all;width: 36px;">
				<col span="1" style="word-break:break-all;width: 82px;">
				<col span="1" style="word-break:break-all;width:191px;">
				<col span="1" style="word-break:break-all;width: 91px;">
				<col span="1" style="word-break:break-all;width:204px;">
				<col span="1" style="word-break:break-all;width: 91px;">
				<col span="1" style="word-break:break-all;width:204px;">
			</colgroup>

			<tr>
				<th rowspan="5"><bean:message key='labels.Category'/></th><!-- 分類 -->
				<th><bean:message key='labels.productStatusCategory'/></th><!-- 状況 -->
				<td>${f:h(productInfosWithNameDto.productStatusCategoryName)}</td>
				<th><bean:message key='labels.productStockCategory'/></th><!-- 保管 -->
				<td>${f:h(productInfosWithNameDto.productStockCategoryName)}</td>
				<th><bean:message key='labels.productPurvayCategory'/></th><!-- 調達 -->
				<td>${f:h(productInfosWithNameDto.productPurvayCategoryName)}</td>
			</tr>
			<tr>
				<th><bean:message key='labels.productStandardCategory'/></th><!-- 標準化 -->
				<td>${f:h(productInfosWithNameDto.productStandardCategoryName)}</td>
				<th><bean:message key='labels.soRate'/></th><!-- 特注計算掛率 -->
				<td>${f:h(productInfosWithNameDto.soRate)}</td>
				<th><bean:message key='labels.setTypeCategory'/></th><!-- セット -->
				<td>${f:h(productInfosWithNameDto.setTypeCategoryName)}</td>
			</tr>

			<tr>
				<th><bean:message key='labels.product1'/></th><!-- カテゴリ(大) -->
				<td colspan="5">${f:h(productInfosWithNameDto.product1Name)}</td>
			</tr>
			<tr>
				<th><bean:message key='labels.product2'/></th><!-- カテゴリ(中) -->
				<td colspan="5">${f:h(productInfosWithNameDto.product2Name)}</td>
			</tr>
			<tr>
				<th><bean:message key='labels.product3'/></th><!-- カテゴリ(小) -->
				<td colspan="5">${f:h(productInfosWithNameDto.product3Name)}</td>
			</tr>
		</table>

		<table id="unit_size_info" class="forms" summary="unit_size_info" style="table-layout:fixed;">
			<colgroup>
				<col span="1" style="word-break:break-all;width: 36px;">
				<col span="1" style="word-break:break-all;width: 82px;">
				<col span="1" style="word-break:break-all;width:191px;">
				<col span="1" style="word-break:break-all;width: 91px;">
				<col span="1" style="word-break:break-all;width:204px;">
				<col span="1" style="word-break:break-all;width: 91px;">
				<col span="1" style="word-break:break-all;width:204px;">
			</colgroup>
			<tr>
				<th rowspan="3"><bean:message key='labels.propertysort'/></th>
				<th><bean:message key='labels.unitCategory'/></th><!-- 単位 -->
				<td>${f:h(productInfosWithNameDto.unitCategoryName)}</td>
				<th><bean:message key='labels.weight'/></th><!-- 重量 -->
				<td>${f:h(productInfosWithNameDto.weight)}&nbsp;
				${f:h(productInfosWithNameDto.weightUnitSizeCategoryName)}</td>
				<th><bean:message key='labels.length'/></th><!-- 長さ -->
				<td>${f:h(productInfosWithNameDto.length)}&nbsp;
				${f:h(productInfosWithNameDto.lengthUnitSizeCategoryName)}</td>
			</tr>
			<tr>
				<th><bean:message key='labels.width'/></th><!-- サイズ(幅) -->
				<td>${f:h(productInfosWithNameDto.width)}&nbsp;
				${f:h(productInfosWithNameDto.widthUnitSizeCategoryName)}</td>
				<th><bean:message key='labels.depth'/></th><!-- サイズ(奥) -->
				<td>${f:h(productInfosWithNameDto.depth)}&nbsp;
				${f:h(productInfosWithNameDto.depthUnitSizeCategoryName)}</td>
				<th><bean:message key='labels.height'/></th><!-- サイズ(高) -->
				<td>${f:h(productInfosWithNameDto.height)}&nbsp;
				${f:h(productInfosWithNameDto.heightUnitSizeCategoryName)}</td>
			</tr>
			<tr>
				<th><bean:message key='labels.coreNum'/></th><!-- 芯数 -->
				<td>${f:h(productInfosWithNameDto.coreNum)}</td>
				<td colspan="4"></td>
			</tr>
		</table>

		<table id="remarks_info" class="forms" summary="remarks_info" style="table-layout:fixed;">
			<colgroup>
				<col span="1" style="word-break:break-all;width:118px;">
				<col span="1" style="word-break:break-all;width:782px;">
			</colgroup>
			<tr>
				<th><bean:message key='labels.remarks'/></th><!-- 備考 -->
				<td>${f:h(productInfosWithNameDto.remarks)}</td>
			</tr>
			<tr>
				<th><bean:message key='labels.eadRemarks'/></th><!-- ピッキング備考 -->
				<td>${f:h(productInfosWithNameDto.eadRemarks)}</td>
			</tr>
			<tr>
				<th><bean:message key='labels.commentData'/></th><!-- コメント -->
				<td>${f:h(productInfosWithNameDto.commentData)}</td>
			</tr>

		</table>

	<div style="width: 900px; text-align: right">
		<button type="button" style="width: 70px" tabindex="10000"
			onclick="$('#${dialogId}').dialog('close');"><bean:message key='words.action.close'/></button>
	</div>

</div>