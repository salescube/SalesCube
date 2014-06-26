<table summary="商品情報" class="forms detail_info" style="width: 900px; table-layout: fixed; word-break:break-all; margin-top: 10px;">
	<colgroup>
		<col span="1" style="width: 12%">
		<col span="1" style="width: 25%">
		<col span="1" style="width: 16%">
		<col span="1" style="width: 25%">
		<col span="1" style="width: 12%">
		<col span="1" style="width: 10%">
	</colgroup>
	<tr>
		<th class="xl64 rd_top_left" style="height: 30px; border-bottom: 1px solid #555555;">商品コード</th>
		<td>${f:h(stockInfoDto.productCode)}</td>
		<th class="xl64" style="height: 30px; border-bottom: 1px solid #555555;">仕入先商品コード</th>
		<td>${f:h(stockInfoDto.supplierPcode)}</td>
		<th class="xl64" style="height: 30px; border-bottom: 1px solid #555555;">セット分類</th>
		<td class="rd_top_right">${f:h(stockInfoDto.setTypeCategoryName)}</td>
	</tr>
	<tr>
		<th class="xl64" style="height: 30px; border-bottom: 1px solid #555555;">商品名</th>
		<td style="white-space: normal">${f:h(stockInfoDto.productName)}</td>
		<th class="xl64" style="height: 30px; border-bottom: 1px solid #555555;">倉庫名</th>
		<td style="white-space: normal">${f:h(stockInfoDto.warehouseName)}</td>
		<th class="xl64" style="height: 30px; border-bottom: 1px solid #555555;">棚番</th>
		<td style="white-space: normal">${f:h(stockInfoDto.rackCode)}</td>
	</tr>
	<tr>
		<th class="xl64 rd_bottom_left" style="height: 30px;">分類-状況</th>
		<td>${f:h(stockInfoDto.productStatusCategoryName)}</td>
		<th class="xl64" style="height: 30px;">分類-保管</th>
		<td>${f:h(stockInfoDto.productStockCategoryName)}</td>
		<th class="xl64" style="height: 30px;">月平均出荷数</th>
		<td class="xl64 rd_bottom_right" style="text-align: right">${f:h(stockInfoDto.formattedAvgShipCount)}</td>
	</tr>
</table>

<table summary="在庫情報" style="width: 900px; margin-top: 20px;" class="forms detail_info">
	<colgroup>
		<col span="5" style="width: 20%">
	</colgroup>
	<tr>
		<th class="xl64 rd_top_left" style="height: 30px;">現在庫総数</th>
		<th class="xl64" style="height: 30px;">引当可能数</th>
		<th class="xl64" style="height: 30px;">受注残数</th>
		<th class="xl64" style="height: 30px;">発注残数</th>
		<th class="xl64 rd_top_right" style="height: 30px;">委託残数</th>
	</tr>
	<tr>
		<td style="text-align: right">${stockInfoDto.formattedCurrentTotalQuantity}</td>
		<td style="text-align: right">${stockInfoDto.formattedPossibleDrawQuantity}</td>
		<td style="text-align: right">${stockInfoDto.formattedRorderRestQuantity}</td>
		<td style="text-align: right">${stockInfoDto.formattedPorderRestQuantity}</td>
		<td style="text-align: right">${stockInfoDto.formattedEntrustRestQuantity}</td>
	</tr>
</table>

<table summary="受発注明細" style="width: 700px; border: none; table-layout: fixed; text-align: left; margin-top: 20px;">
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
				<table summary="受注残明細" class="forms detail_info" style="width: 260px;">
					<colgroup>
						<col span="1" style="width: 100px">
						<col span="1" style="width: 80px">
						<col span="1" style="width: 80px">
					</colgroup>
					<tr>
						<th class="xl64 rd_top_left" style="height: 30px;">受注番号 - 行</th>
						<th class="xl64" style="height: 30px;">出荷日</th>
						<th class="xl64 rd_top_right" style="height: 30px;">受注残数</th>
					</tr>
<c:forEach var="rowData" items="${rorderRestDetailList}" varStatus="status">
					<tr>
						<bean:define id="concatUrl" value="/rorder/inputROrder/load/?roSlipId=${rowData.roSlipId}" />
						<td style="text-align: center">
						<a href="javascript:location.doHref('${f:url(concatUrl)}')">${f:h(rowData.roSlipId)}&nbsp-&nbsp${f:h(rowData.lineNo)}</a>
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
				<table summary="発注残明細" class="forms detail_info" style="width: 260px;">
					<colgroup>
						<col span="1" style="width: 100px">
						<col span="1" style="width: 80px">
						<col span="1" style="width: 80px">
					</colgroup>
					<tr>
						<th class="xl64 rd_top_left" style="height: 30px;">発注番号 - 行</th>
						<th class="xl64" style="height: 30px;">納期</th>
						<th class="xl64 rd_top_right" style="height: 30px;">発注残数</th>
					</tr>
<c:forEach var="rowData" items="${porderRestDetailList}" varStatus="status">
					<tr>
						<bean:define id="concatUrl" value="/porder/inputPOrder/load/?poSlipId=${rowData.poSlipId}" />
						<td style="text-align: center">
						<a href="javascript:location.doHref('${f:url(concatUrl)}')">${f:h(rowData.poSlipId)}&nbsp-&nbsp${f:h(rowData.lineNo)}</a>
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
		<th style="text-align: left; padding-top:20px;">委託発注残明細</th>
		<th style="text-align: left; padding-top:20px;">委託在庫明細</th>
	</tr>
	<tr>
		<td>
			<div style="position: static; padding: 0px; border: none; width: 360px; height: 122px; overflow-y:scroll;">
				<table summary="委託発注残明細" class="forms detail_info" style="width: 340px;">
					<colgroup>
						<col span="1" style="width: 100px">
						<col span="1" style="width: 80px">
						<col span="1" style="width: 80px">
						<col span="1" style="width: 80px">
					</colgroup>
					<tr>
						<th class="xl64 rd_top_left" style="height: 30px;">発注番号 - 行</th>
						<th class="xl64" style="height: 30px;">発注日</th>
						<th class="xl64" style="height: 30px;">納期</th>
						<th class="xl64 rd_top_right" style="height: 30px;">発注残数</th>
					</tr>
<c:forEach var="rowData" items="${entrustPorderRestDetailList}" varStatus="status">
					<tr>
						<bean:define id="concatUrl" value="/porder/inputPOrder/load/?poSlipId=${rowData.poSlipId}" />
						<td style="text-align: center">
						<a href="javascript:location.doHref('${f:url(concatUrl)}')">${f:h(rowData.poSlipId)}&nbsp-&nbsp${f:h(rowData.lineNo)}</a>
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
</c:forEach>
				</table>
			</div>
		</td>
		<td>
			<div style="position: static; padding: 0px; border: none; width: 280px; height: 122px; overflow-y:scroll;">
				<table summary="委託在庫明細" class="forms detail_info" style="width: 260px;">
					<colgroup>
						<col span="1" style="width: 100px">
						<col span="1" style="width: 80px">
						<col span="1" style="width: 80px">
					</colgroup>
					<tr>
						<th class="xl64 rd_top_left" style="height: 30px;">発注番号 - 行</th>
						<th class="xl64" style="height: 30px;">委託入庫日</th>
						<th class="xl64 rd_top_right" style="height: 30px;">委託在庫数</th>
					</tr>
<c:forEach var="rowData" items="${entrustStockDetailList}" varStatus="status">
					<tr>
						<bean:define id="concatUrl" value="/porder/inputPOrder/load/?poSlipId=${rowData.poSlipId}" />
						<td style="text-align: center">
						<a href="javascript:location.doHref('${f:url(concatUrl)}')">${f:h(rowData.poSlipId)}&nbsp-&nbsp${f:h(rowData.lineNo)}</a>
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
