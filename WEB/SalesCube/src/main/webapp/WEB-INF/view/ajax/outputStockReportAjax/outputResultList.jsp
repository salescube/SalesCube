<table id="search_result" summary="検索結果" class="forms" style="table-layout: fixed;">
	<colgroup>
		<col span="1" style="width: 183px">
		<col span="1" style="width: 336px">
		<col span="1" style="width: 91px">
		<col span="1" style="width: 91px">
		<col span="1" style="width: 91px">
		<col span="1" style="width: 91px">
	</colgroup>
	<tbody>
		<tr>
			<td class="xl70"><bean:message key='labels.stockReport'/></td><%// 在庫残高表 %>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td class="xl70" style="text-align: right;"><bean:message key='labels.outputDate'/>${f:h(currentDate)}</td><%// 出力日 %>
		</tr>
		<tr>
			<td class="xl70" style="text-align: right;"><bean:message key='labels.targetYm.excel'/></td><%// 対象年月度 %>
			<td class="xl70" style="text-align: left;">${f:h(targetYm)}</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td class="xl70" style="text-align: right;"><bean:message key='labels.sumStockPrice'/></td><%// 在庫残高合計金額 %>
			<td class="xl71" style="text-align: left;">${f:h(sumStockPrice)}</td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<th class=xl64><bean:message key='labels.productCode'/></th><%// 商品コード %>
			<th class=xl64><bean:message key='labels.productName'/></th><%// 商品名 %>
			<th class=xl64><bean:message key='labels.rackCode'/></th><%// 棚番 %>
			<th class=xl64><bean:message key='labels.allStockQuantity'/></th><%// 在庫数 %>
			<th class=xl64><bean:message key='labels.stockPrice'/></th><%// 在庫高 %>
			<th class=xl64 style="border-right:.5pt solid windowtext;"><bean:message key='labels.supplierPrice'/></th><%// 仕入単価 %>
		</tr>
		<c:forEach var="rowData" items="${searchResultList}" varStatus="status">
			<tr>
				<td class="xl72" style="text-align: left;">
					${f:h(rowData.productCode)}
				</td>
				<td class="xl72" style="text-align: left;">
					${f:h(rowData.productName)}
				</td>
				<td class="xl72" style="text-align: left;">
					${f:h(rowData.rackCode)}
				</td>
				<td class="xl73" style="text-align: right;">
					${f:h(rowData.allStockNum)}
				</td>
				<td class="xl74" style="text-align: right;">
					${f:h(rowData.stockPrice)}
				</td>
				<td class="xl74" style="text-align: right;">
					${f:h(rowData.supplierPriceYen)}
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
