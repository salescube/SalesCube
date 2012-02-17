<table id="search_result" summary="検索結果" class="forms" style="table-layout: fixed;">
	<colgroup>
		<col span="1" style="width: 138px"><%// 商品コード %>
		<col span="1" style="width: 213px"><%// 商品名 %>
		<col span="1" style="width: 100px"><%// 棚番 %>
		<col span="1" style="width: 100px"><%// 現在庫数 %>
		<col span="1" style="width: 100px"><%// 受注残数 %>
		<col span="1" style="width: 100px"><%// 引当可能数 %>
		<col span="1" style="width: 100px"><%// 船便発注残数 %>
		<col span="1" style="width: 100px"><%// AIR便発注残数 %>
		<col span="1" style="width: 100px"><%// 宅配便発注残数 %>
		<col span="1" style="width: 100px"><%// 発注残数 %>
		<col span="1" style="width: 100px"><%// 委託在庫発注残数 %>
		<col span="1" style="width: 100px"><%// 委託在庫数 %>
		<col span="1" style="width: 100px"><%// 最短入荷日 %>
		<col span="1" style="width: 100px"><%// 保有数 %>
		<col span="1" style="width: 100px"><%// 保有月数 %>
		<col span="1" style="width: 100px"><%// 受注数量 %>
		<col span="1" style="width: 100px"><%// 出荷数量 %>
		<col span="1" style="width: 100px"><%// 平均出荷数量 %>
		<col span="1" style="width: 100px"><%// 出荷数偏差（σ） %>
		<col span="1" style="width: 100px"><%// 過去最大数 %>
		<col span="1" style="width: 100px"><%// 発注点 %>
		<col span="1" style="width: 100px"><%// 発注ロット %>
		<col span="1" style="width: 100px"><%// 売上金額 %>
		<col span="1" style="width: 100px"><%// 売単価 %>
		<col span="1" style="width: 100px"><%// 仕入単価（外貨） %>
		<col span="1" style="width: 100px"><%// 仕入単価（円） %>
		<col span="1" style="width: 100px"><%// 粗利益 %>
		<col span="1" style="width: 100px"><%// 在庫高 %>
		<col span="1" style="width: 100px"><%// 粗利率 %>
		<col span="1" style="width: 100px"><%// 在庫回転率 %>
		<col span="1" style="width: 100px"><%// 交差比率 %>
	</colgroup>
	<tbody>
		<tr>
			<td class="xl70"><bean:message key='titles.outputStockList'/></td><%// 在庫一覧表 %>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td class="xl70" style="text-align: right;"><bean:message key='labels.outputDate'/>${f:h(currentDate)}</td><%// 出力日 %>
		</tr>
		<tr>
			<td class="xl70" style="text-align: right;"><bean:message key='labels.includeCondition'/></td><%// 抽出条件： %>
			<td class="xl70" style="text-align: left;">${f:h(periodMonth)}<bean:message key='labels.include.orderReultProducts'/></td><%// ｎヶ月前～本日 %>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<c:choose>
				<c:when test='${radioCond2==RADIO_COND2_VALUE_0}'>
					<td class="xl70" style="text-align: left;"><bean:message key='labels.include.all'/></td><%// 全件 %>
				</c:when>
				<c:when test='${radioCond2==RADIO_COND2_VALUE_1}'>
					<td class="xl70" style="text-align: left;"><bean:message key='labels.include.holdingQuantity.eq.less.orderPoint'/></td><%// 保有数＜発注点 %>
				</c:when>
				<c:when test='${radioCond2==RADIO_COND2_VALUE_2}'>
					<td class="xl70" style="text-align: left;"><bean:message key='labels.include.stockQuantity.eq.less.orderPoint'/></td><%// 現在庫数＜発注点 %>
				</c:when>
				<c:when test='${radioCond2==RADIO_COND2_VALUE_3}'>
					<td class="xl70" style="text-align: left;"><bean:message key='labels.include.allocatableQuantity.eq.less.orderPoint'/></td><%// 引当可能数＜発注点（発注点がゼロのものは除く） %>
				</c:when>
				<c:when test='${radioCond2==RADIO_COND2_VALUE_4}'>
					<td class="xl70" style="text-align: left;"><bean:message key='labels.include.allocatableQuantityPlus.eq.less.orderPoint'/></td><%// 引当可能数＋発注残数＜発注点（発注点がゼロのものは除く） %>
				</c:when>
				<c:when test='${radioCond2==RADIO_COND2_VALUE_5}'>
					<td class="xl70" style="text-align: left;"><bean:message key='labels.include.allocatableQuantity'/>${f:h(allocatedQuantityWithComma)}<bean:message key='labels.include.quantity.eq.less'/></td><%// 引当可能数ｎ個以下 %>
				</c:when>
				<c:when test='${radioCond2==RADIO_COND2_VALUE_6}'>
					<td class="xl70" style="text-align: left;"><bean:message key='labels.include.over.maxHoldingQuantity'/></td><%// 最大在庫保有数超過 %>
				</c:when>
			</c:choose>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<bean:define id="existExcludeCondition" value="false" />
		<c:if test='${excludeRoNotExists}'>
			<tr>
				<c:if test='${existExcludeCondition}'>
					<td></td>
				</c:if>
				<c:if test='${!existExcludeCondition}'>
					<td class="xl70" style="text-align: right;"><bean:message key='labels.excludeCondition'/></td><%// 除外条件： %>
				</c:if>
				<td class="xl70" style="text-align: left;"><bean:message key='labels.exclude.roNotExists'/></td><%// 受注実績のない商品は除く %>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<bean:define id="existExcludeCondition" value="true" />
		</c:if>
		<c:if test='${excludeSalesCancel}'>
			<tr>
				<c:if test='${existExcludeCondition}'>
					<td></td>
				</c:if>
				<c:if test='${!existExcludeCondition}'>
					<td class="xl70" style="text-align: right;"><bean:message key='labels.excludeCondition'/></td><%// 除外条件： %>
				</c:if>
				<td class="xl70" style="text-align: left;"><bean:message key='labels.exclude.salesCancel'/></td><%// 販売中止品(発注停止品)は除く %>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<bean:define id="existExcludeCondition" value="true" />
		</c:if>
		<c:if test='${excludeNotManagementStock}'>
			<tr>
				<c:if test='${existExcludeCondition}'>
					<td></td>
				</c:if>
				<c:if test='${!existExcludeCondition}'>
					<td class="xl70" style="text-align: right;"><bean:message key='labels.excludeCondition'/></td><%// 除外条件： %>
				</c:if>
				<td class="xl70" style="text-align: left;"><bean:message key='labels.exclude.notManagementStock'/></td><%// 在庫管理しない商品（都度調達品）は除く %>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<bean:define id="existExcludeCondition" value="true" />
		</c:if>
		<c:if test='${excludeMultiRack}'>
			<tr>
				<c:if test='${existExcludeCondition}'>
					<td></td>
				</c:if>
				<c:if test='${!existExcludeCondition}'>
					<td class="xl70" style="text-align: right;"><bean:message key='labels.excludeCondition'/></td><%// 除外条件： %>
				</c:if>
				<td class="xl70" style="text-align: left;"><bean:message key='labels.exclude.multiRack'/></td><%// 重複可能な棚番の商品は除く %>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<bean:define id="existExcludeCondition" value="true" />
		</c:if>
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
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
			<th class=xl64><bean:message key='labels.stockQuantity'/></th><%// 現在庫数 %>
			<th class=xl64><bean:message key='labels.rOrderRestQuantity'/></th><%// 受注残数 %>
			<th class=xl64><bean:message key='labels.allocatableQuantity'/></th><%// 引当可能数 %>
			<th class=xl64><bean:message key='labels.pOrderRestQuantityShip'/></th><%// 船便発注残数 %>
			<th class=xl64><bean:message key='labels.pOrderRestQuantityAir'/></th><%// AIR便発注残数 %>
			<th class=xl64><bean:message key='labels.pOrderRestQuantityDelivery'/></th><%// 宅配便発注残数 %>
			<th class=xl64><bean:message key='labels.pOrderRestQuantity'/></th><%// 発注残数 %>
			<th class=xl64><bean:message key='labels.pOrderQuantityEntrust'/></th><%// 委託在庫発注残数 %>
			<th class=xl64><bean:message key='labels.stockQuantityEntrust'/></th><%// 委託在庫数 %>
			<th class=xl64><bean:message key='labels.minDeliveryDate'/></th><%// 最短入荷日 %>
			<th class=xl64><bean:message key='labels.holdQuantity'/></th><%// 保有数 %>
			<th class=xl64><bean:message key='labels.holdTerm'/></th><%// 保有月数 %>
			<th class=xl64><bean:message key='labels.rOrderQuantity'/></th><%// 受注数量 %>
			<th class=xl64><bean:message key='labels.salesQuantity'/></th><%// 出荷数量 %>
			<th class=xl64><bean:message key='labels.avgSalesQuantity'/></th><%// 平均出荷数量 %>
			<th class=xl64><bean:message key='labels.deviationSalesQuantity'/></th><%// 出荷数偏差（σ） %>
			<th class=xl64><bean:message key='labels.maxMonthlyQuantity'/></th><%// 過去最大数 %>
			<th class=xl64><bean:message key='labels.orderPoint'/></th><%// 発注点 %>
			<th class=xl64><bean:message key='labels.orderLot'/></th><%// 発注ロット %>
			<th class=xl64><bean:message key='labels.salesMoney'/></th><%// 売上金額 %>
			<th class=xl64><bean:message key='labels.salesPrice'/></th><%// 売単価 %>
			<th class=xl64><bean:message key='labels.supplierPriceDol'/></th><%// 仕入単価（外貨） %>
			<th class=xl64><bean:message key='labels.supplierPriceYen'/></th><%// 仕入単価（円） %>
			<th class=xl64><bean:message key='labels.grossMargin'/></th><%// 粗利益 %>
			<th class=xl64><bean:message key='labels.stockPrice'/></th><%// 在庫高 %>
			<th class=xl64><bean:message key='labels.grossMarginRatio'/></th><%// 粗利率 %>
			<th class=xl64><bean:message key='labels.stockRotationRatio'/></th><%// 在庫回転率 %>
			<th class=xl64 style="border-right:.5pt solid windowtext;"><bean:message key='labels.intersectionRatio'/></th><%// 交差比率 %>
		</tr>
		<c:forEach var="rowData" items="${searchResultList}" varStatus="status">
			<tr>
				<td class="xl72" style="text-align: left;">
					${f:h(rowData.productCode)}<%// 商品コード%>
				</td>
				<td class="xl72" style="text-align: left;">
					${f:h(rowData.productName)}<%// 商品名%>
				</td>
				<td class="xl72" style="text-align: left;">
					${f:h(rowData.rackCode)}<%// 棚番コード%>
				</td>
				<td class="xl73" style="text-align: right;">
					${f:h(rowData.currentStockQuantityStr)}<%// 現在庫数%>
				</td>
				<td class="xl73" style="text-align: right;">
					${f:h(rowData.restQuantityRoStr)}<%// 受注残数%>
				</td>
				<td class="xl73" style="text-align: right;">
					${f:h(rowData.availableStockQuantityStr)}<%// 引当可能数%>
				</td>
				<td class="xl73" style="text-align: right;">
					${f:h(rowData.restQuantityPoShipStr)}<%// 船便発注残数%>
				</td>
				<td class="xl73" style="text-align: right;">
					${f:h(rowData.restQuantityPoAirStr)}<%// AIR便発注残数%>
				</td>
				<td class="xl73" style="text-align: right;">
					${f:h(rowData.restQuantityPoDeliveryStr)}<%// 宅配便発注残数%>
				</td>
				<td class="xl73" style="text-align: right;">
					${f:h(rowData.restQuantityPoStr)}<%// 発注残数%>
				</td>
				<td class="xl73" style="text-align: right;">
					${f:h(rowData.restQuantityEntrustStr)}<%// 委託在庫発注残数%>
				</td>
				<td class="xl73" style="text-align: right;">
					${f:h(rowData.stockQuantityEntrustEadStr)}<%// 委託在庫数%>
				</td>
				<td class="xl72" style="text-align: center">
					${f:h(rowData.deliveryDateStr)}<%// 最短入荷日%>
				</td>
				<td class="xl73" style="text-align: right;">
					${f:h(rowData.holdingStockQuantity)}<%// 保有数%>
				</td>
				<td class="xl72" style="text-align: right;">
					${f:h(rowData.holdingStockMonthStr)}<%// 保有月数%>
				</td>
				<td class="xl73" style="text-align: right;">
					${f:h(rowData.roQuantityStr)}<%// 受注数量%>
				</td>
				<td class="xl73" style="text-align: right;">
					${f:h(rowData.salesQuantityStr)}<%// 出荷数量%>
				</td>
				<td class="xl73" style="text-align: right;">
					${f:h(rowData.avgSalesQuantityStr)}<%// 平均出荷数量%>
				</td>
				<td class="xl72" style="text-align: right;">
					${f:h(rowData.salesStandardDeviationStr)}<%// 出荷数偏差%>
				</td>
				<td class="xl73" style="text-align: right;">
					${f:h(rowData.maxSalesQuantityStr)}<%// 過去最大数%>
				</td>
				<td class="xl73" style="text-align: right;">
					${f:h(rowData.poNum)}<%// 発注点%>
				</td>
				<td class="xl73" style="text-align: right;">
					${f:h(rowData.poLotStr)}<%// 発注ロット%>
				</td>
				<td class="xl74" style="text-align: right;">
					${f:h(rowData.retailPriceTotalStr)}<%// 売上金額%>
				</td>
				<td class="xl74" style="text-align: right;">
					${f:h(rowData.retailPriceStr)}<%// 売単価%>
				</td>
				<td class="xl75" style="text-align: right;mso-number-format:'\0022${rowData.cUnitSign}\0022#\,\#\#0\.00_\)\;\\\(\0022${rowData.cUnitSign}\0022#\=,\#\#0\.00\\\)';">
					${f:h(rowData.supplierPriceDolStr)}<%// 仕入単価（外貨）%>
				</td>
				<td class="xl74" style="text-align: right;">
					${f:h(rowData.supplierPriceYenStr)}<%// 仕入単価（円）%>
				</td>
				<td class="xl74" style="text-align: right;">
					${f:h(rowData.grossMarginTotalStr)}<%// 粗利益%>
				</td>
				<td class="xl74" style="text-align: right;">
					${f:h(rowData.currentStockPriceStr)}<%// 在庫高%>
				</td>
				<td class="xl72" style="text-align: right;">
					${f:h(rowData.grossMarginRatioStr)}<%// 粗利率%>
				</td>
				<td class="xl72" style="text-align: right;">
					${f:h(rowData.stockTurnoverRateStr)}<%// 在庫回転率%>
				</td>
				<td class="xl72" style="text-align: right;">
					${f:h(rowData.grossMarginRatioToStockStr)}<%// 交差比率%>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
