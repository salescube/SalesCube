<table summary="商品情報" class="forms" style="width: 700px; table-layout: fixed; word-break:break-all;">
	<colgroup>
		<col span="1" style="width: 12%">
		<col span="1" style="width: 25%">
		<col span="1" style="width: 16%">
		<col span="1" style="width: 25%">
		<col span="1" style="width: 12%">
		<col span="1" style="width: 10%">
	</colgroup>
	<tr>
		<th>商品コード</th>
		<td>${f:h(stockInfoDto.productCode)}</td>
		<th>仕入先商品コード</th>
		<td>${f:h(stockInfoDto.supplierPcode)}</td>
		<th>セット分類</th>
		<td>${f:h(stockInfoDto.setTypeCategoryName)}</td>
	</tr>
	<tr>
		<th>商品名</th>
		<td style="white-space: normal">${f:h(stockInfoDto.productName)}</td>
		<th>倉庫名</th>
		<td style="white-space: normal">${f:h(stockInfoDto.warehouseName)}</td>
		<th>棚番</th>
		<td style="white-space: normal">${f:h(stockInfoDto.rackCode)}</td>
	</tr>
	<tr>
		<th>分類-状況</th>
		<td>${f:h(stockInfoDto.productStatusCategoryName)}</td>
		<th>分類-保管</th>
		<td>${f:h(stockInfoDto.productStockCategoryName)}</td>
		<th>月平均出荷数</th>
		<td style="text-align: right">${f:h(stockInfoDto.formattedAvgShipCount)}</td>
	</tr>
</table>

<table summary="在庫情報" style="width: 460px;" class="forms">
	<colgroup>
		<col span="5" style="width: 20%">
	</colgroup>
	<tr>
		<th>現在庫総数</th>
		<th>引当可能数</th>
		<th>受注残数</th>
		<th>発注残数</th>
		<th>委託残数</th>
	</tr>
	<tr>
		<td style="text-align: right">${stockInfoDto.formattedCurrentTotalQuantity}</td>
		<td style="text-align: right">${stockInfoDto.formattedPossibleDrawQuantity}</td>
		<td style="text-align: right">${stockInfoDto.formattedRorderRestQuantity}</td>
		<td style="text-align: right">${stockInfoDto.formattedPorderRestQuantity}</td>
		<td style="text-align: right">${stockInfoDto.formattedEntrustRestQuantity}</td>
	</tr>
</table>

<table summary="受発注明細" style="width: 700px; border: none; table-layout: fixed; text-align: left;">
	<colgroup>
		<col span="1" style="width: 400px">
		<col span="1" style="width: 300px">
	</colgroup>
	<tr>
		<th style="text-align: left">受注残明細</th>
		<th style="text-align: left">発注残明細</th>
	</tr>
	<tr>
		<td>
			<div style="position: static; padding: 0px; border: none; width: 280px; height: 122px; overflow-y:scroll;">
				<table summary="受注残明細" class="forms" style="width: 260px;">
					<colgroup>
						<col span="1" style="width: 100px">
						<col span="1" style="width: 80px">
						<col span="1" style="width: 80px">
					</colgroup>
					<tr>
						<th>受注番号 - 行</th>
						<th>出荷日</th>
						<th>受注残数</th>
					</tr>
<c:forEach var="rowData" items="${rorderRestDetailList}" varStatus="status">
					<tr>
						<td style="text-align: center">${f:h(rowData.roSlipId)}&nbsp-&nbsp${f:h(rowData.lineNo)}</td>
						<td style="text-align: center">
	<c:if test="${rowData.shipDate != null}">
						<fmt:formatDate value="${rowData.shipDate}" pattern="yyyy/MM/dd" />
	</c:if>
	<c:if test="${rowData.shipDate == null}">
						&nbsp
	</c:if>
						</td>
						<td style="text-align: right"><span class="num" >${f:h(rowData.restQuantity)}</span></td>
					</tr>
</c:forEach>
				</table>
			</div>
		</td>
		<td>
			<div style="position: static; padding: 0px; border: none; width: 280px; height: 122px; overflow-y:scroll;">
				<table summary="発注残明細" class="forms" style="width: 260px;">
					<colgroup>
						<col span="1" style="width: 100px">
						<col span="1" style="width: 80px">
						<col span="1" style="width: 80px">
					</colgroup>
					<tr>
						<th>発注番号 - 行</th>
						<th>納期</th>
						<th>発注残数</th>
					</tr>
<c:forEach var="rowData" items="${porderRestDetailList}" varStatus="status">
					<tr>
						<td style="text-align: center">${f:h(rowData.poSlipId)}&nbsp-&nbsp${f:h(rowData.lineNo)}</td>
						<td style="text-align: center">
	<c:if test="${rowData.deliveryDate != null}">
						<fmt:formatDate value="${rowData.deliveryDate}" pattern="yyyy/MM/dd" />
	</c:if>
	<c:if test="${rowData.deliveryDate == null}">
						&nbsp
	</c:if>
						</td>
						<td style="text-align: right"><span class="num" >${f:h(rowData.restQuantity)}</span></td>
					</tr>
</c:forEach>
				</table>
			</div>
		</td>
	</tr>
	<tr>
		<th style="text-align: left">委託発注残明細</th>
		<th style="text-align: left">委託在庫明細</th>
	</tr>
	<tr>
		<td>
			<div style="position: static; padding: 0px; border: none; width: 360px; height: 122px; overflow-y:scroll;">
				<table summary="委託発注残明細" class="forms" style="width: 340px;">
					<colgroup>
						<col span="1" style="width: 100px">
						<col span="1" style="width: 80px">
						<col span="1" style="width: 80px">
						<col span="1" style="width: 80px">
					</colgroup>
					<tr>
						<th>発注番号 - 行</th>
						<th>発注日</th>
						<th>納期</th>
						<th>発注残数</th>
					</tr>
<c:forEach var="rowData" items="${entrustPorderRestDetailList}" varStatus="status">
					<tr>
						<td style="text-align: center">${f:h(rowData.poSlipId)}&nbsp-&nbsp${f:h(rowData.lineNo)}</td>
						<td style="text-align: center">
	<c:if test="${rowData.poDate != null}">
						<fmt:formatDate value="${rowData.poDate}" pattern="yyyy/MM/dd" />
	</c:if>
	<c:if test="${rowData.poDate == null}">
						&nbsp
	</c:if>
						</td>
						<td style="text-align: center">
	<c:if test="${rowData.deliveryDate != null}">
						<fmt:formatDate value="${rowData.deliveryDate}" pattern="yyyy/MM/dd" />
	</c:if>
	<c:if test="${rowData.deliveryDate == null}">
						&nbsp
	</c:if>
						</td>
						<td style="text-align: right"><span class="num" >${f:h(rowData.restQuantity)}</span></td>
					</tr>
					</tr>
</c:forEach>
				</table>
			</div>
		</td>
		<td>
			<div style="position: static; padding: 0px; border: none; width: 280px; height: 122px; overflow-y:scroll;">
				<table summary="委託在庫明細" class="forms" style="width: 260px;">
					<colgroup>
						<col span="1" style="width: 100px">
						<col span="1" style="width: 80px">
						<col span="1" style="width: 80px">
					</colgroup>
					<tr>
						<th>発注番号 - 行</th>
						<th>委託入庫日</th>
						<th>委託在庫数</th>
					</tr>
<c:forEach var="rowData" items="${entrustStockDetailList}" varStatus="status">
					<tr>
						<td style="text-align: center">${f:h(rowData.poSlipId)}&nbsp-&nbsp${f:h(rowData.lineNo)}</td>
						<td style="text-align: center">
	<c:if test="${rowData.entrustEadDate != null}">
						<fmt:formatDate value="${rowData.entrustEadDate}" pattern="yyyy/MM/dd" />
	</c:if>
	<c:if test="${rowData.entrustEadDate == null}">
						&nbsp
	</c:if>
						</td>
						<td style="text-align: right"><span class="num" >${f:h(rowData.quantity)}</span></td>
					</tr>
					</tr>
</c:forEach>
				</table>
			</div>
		</td>
	</tr>
</table>
	<script type="text/javascript">
	<!--
	applyNumeralStylesToObj(${mineDto.productFractCategory},${mineDto.numDecAlignment},$(".num"));
	_after_load($(".num"));
	-->
	</script>
