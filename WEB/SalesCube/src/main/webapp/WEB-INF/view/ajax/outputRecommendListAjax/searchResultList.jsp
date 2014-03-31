<%@page import="jp.co.arkinfosys.common.Constants" %>
<%@page import="jp.co.arkinfosys.common.CategoryTrns" %>
<%@page import="jp.co.arkinfosys.common.StringUtil" %>
<bean:define id="IMMEDIATELY_PORDER" value="<%=CategoryTrns.IMMEDIATELY_PORDER%>"/>
<bean:define id="ENTRUST_PORDER" value="<%=CategoryTrns.ENTRUST_PORDER%>"/>
<bean:define id="NORMAL_PORDER" value="<%=CategoryTrns.NORMAL_PORDER%>"/>
<bean:define id="MOVE_ENTRUST_STOCK" value="<%=CategoryTrns.MOVE_ENTRUST_STOCK%>"/>

<c:if test='${isOutputExcel}'>
	<bean:define id="CURRENT_DATE" value="<%=StringUtil.getCurrentDateString(Constants.FORMAT.DATE)%>"/>
	<table style="width: 1230px;">
		<tr>
			<td colspan="7">補充発注推奨リスト</td>
			<td class="xl71">${CURRENT_DATE}</td>
		</tr>
	</table>

	<table style="width: 1230px;">
		<tr>
			<td></td>
			<td><bean:message key='labels.supplier'/>：</td>
			<td>${searchSupplierName}</td>
			<td style="vertical-align: top;" rowspan="3"><bean:message key='labels.excludeProductsSearchCondition'/>：</td>
			<td style="vertical-align: top;" rowspan="3">
				<c:if test="${searchExcludeHoldingStockZero}"><bean:message key='labels.exclude.holdingStockZero'/><br></c:if>
				<c:if test="${searchExcludeAvgShipCountZero}"><bean:message key='labels.exclude.avgShipCountZero'/><br></c:if>
				<c:if test="${searchExcludeAvgLessThanHoldingStock}"><bean:message key='labels.exclude.avgLessThanHoldingStock'/><br></c:if>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><bean:message key='labels.poCategory'/>：</td>
			<td>${searchPOCategoryName}</td>
		</tr>
		<tr>
			<td></td>
			<td><c:if test="${searchPOCategory != ENTRUST_PORDER}"><bean:message key='labels.immediatelyPOCategory'/>：</c:if></td>
			<td><c:if test="${searchPOCategory != ENTRUST_PORDER}">${searchImmediatelyPOCategoryName}</c:if></td>
		</tr>
	</table>
</c:if>

<c:if test='${searchPOCategory == IMMEDIATELY_PORDER && searchImmediatelyPOCategory == MOVE_ENTRUST_STOCK}'>
	<bean:define id="TABLE_WIDTH" value="2000px"/>
	<bean:define id="INFO_COLSPAN" value="18"/>
	<bean:define id="PO_QUANTITY_WIDTH" value="100px"/>
</c:if>
<c:if test='${searchPOCategory != IMMEDIATELY_PORDER || searchImmediatelyPOCategory != MOVE_ENTRUST_STOCK}'>
	<bean:define id="TABLE_WIDTH" value="1840px"/>
	<bean:define id="INFO_COLSPAN" value="17"/>
	<bean:define id="PO_QUANTITY_WIDTH" value="60px"/>
</c:if>

			<table class="forms detail_info" style="width: ${TABLE_WIDTH};">
				<colgroup>
<c:if test='${!isOutputExcel && ((searchPOCategory == IMMEDIATELY_PORDER && searchImmediatelyPOCategory == NORMAL_PORDER) || searchPOCategory == ENTRUST_PORDER)}'>
					<col span="1" style="width: 35px;">
</c:if>

<c:if test='${searchPOCategory == IMMEDIATELY_PORDER && searchImmediatelyPOCategory == MOVE_ENTRUST_STOCK}'>
					<col span="1" style="width: 120px;">
</c:if>

					<col span="1" style="width: 270px; word-wrap: break-word;">
					<col span="1" style="width: ${PO_QUANTITY_WIDTH};">
					<col span="1" style="width: 90px;">
					<col span="1" style="width: 95px;">
					<col span="1" style="width: 95px;">
					<col span="1" style="width: 125px;">
					<col span="1" style="width: 95px;">
					<col span="1" style="width: 125px;">
					<col span="1" style="width: 125px;">
					<col span="1" style="width: 95px;">
					<col span="1" style="width: 125px;">
					<col span="1" style="width: 125px;">
					<col span="1" style="width: 90px;">
					<col span="1" style="width: 90px;">
					<col span="1" style="width: 90px;">
					<col span="1" style="width: 90px;">
				</colgroup>
				<thead>
				<tr>
<c:if test='${!isOutputExcel && ((searchPOCategory == IMMEDIATELY_PORDER && searchImmediatelyPOCategory == NORMAL_PORDER) || searchPOCategory == ENTRUST_PORDER)}'>
					<th class="xl64 rd_top_left" rowspan="2" style="height: 30px;"><bean:message key='words.action.select'/></th><!-- 選択 -->
</c:if>
<c:if test='${searchPOCategory == IMMEDIATELY_PORDER && searchImmediatelyPOCategory == MOVE_ENTRUST_STOCK}'>
					<th class="xl65 rd_top_left" rowspan="2" style='cursor: pointer; height: 30px;"' onclick="sort('poSlipId')">
						発注伝票番号
						<c:if test='${!isOutputExcel}'>
							<span id="sortStatus_poSlipId" style="color: white">
								<c:if test="${searchSortColumn == 'poSlipId' && !isOutputExcel}">
									<c:if test='${searchSortOrderAsc}'>
										<bean:message key='labels.asc'/>
									</c:if>
									<c:if test='${!searchSortOrderAsc}'>
										<bean:message key='labels.desc'/>
									</c:if>
								</c:if>
							</span>
						</c:if>
					</th>
</c:if>
					<th class="xl65 rd_top_left rd_top_right" colspan="${INFO_COLSPAN}" style="height: 30px; border-bottom: 1px solid #555555;">
						<bean:message key='titles.productInfos'/>
					</th><!-- 商品 -->

				</tr>
				<tr>
					<c:forEach var="colInfo" items="${columnInfoList}" varStatus="status">
						<th class="xl64" style="height: 30px;" id='result_${f:h(colInfo.itemId)}'
							<c:if test='${colInfo.sortFlag=="1"}'>
								style='cursor: pointer;' onclick="sort('${f:h(colInfo.itemId)}')"
							</c:if>
						>
							${f:h(colInfo.itemName)}
							<span id="sortStatus_${f:h(colInfo.itemId)}" style="color: white">
								<c:if test='${searchSortColumn==colInfo.itemId && !isOutputExcel}'>
									<c:if test='${searchSortOrderAsc}'>
										<bean:message key='labels.asc'/>
									</c:if>
									<c:if test='${!searchSortOrderAsc}'>
										<bean:message key='labels.desc'/>
									</c:if>
								</c:if>
							</span>
						</th>
					</c:forEach>
				</tr>
				</thead>
				<tbody>
				<c:forEach var="rowData" items="${searchResultList}" varStatus="s">

				<tr>
<c:if test='${!isOutputExcel && ((searchPOCategory == IMMEDIATELY_PORDER && searchImmediatelyPOCategory == NORMAL_PORDER) || searchPOCategory == ENTRUST_PORDER)}'>
					<td class="xl70"  style="text-align :center">
						<input class="EnableTabindex" type="checkbox" name="searchResultList[${f:h(s.index)}].validRow" id="searchResultList[${f:h(s.index)}].validRow" tabindex="${f:h(s.index)*2+1000}"
						<c:if test="${rowData.validRow}">checked="checked"</c:if>/>
					</td>
</c:if>

<c:if test='${searchPOCategory == IMMEDIATELY_PORDER && searchImmediatelyPOCategory == MOVE_ENTRUST_STOCK}'>
					<td class="xl70"  style="text-align:center;">
						&nbsp;${f:h(rowData.poSlipId)}&nbsp;
					</td>
</c:if>

					<td class="xl70"  style="text-align:left;">
						&nbsp;${f:h(rowData.productCode)}&nbsp;
<c:if test='${!isOutputExcel}'>
						<input type="hidden" name="searchResultList[${f:h(s.index)}].productCode" id="searchResultList[${f:h(s.index)}].productCode"
						value="${rowData.productCode}"/>
</c:if>
					</td>
					<td class="xl67"  style="text-align: right">&nbsp;
<c:if test='${searchPOCategory == IMMEDIATELY_PORDER && searchImmediatelyPOCategory == MOVE_ENTRUST_STOCK}'>
					<span class="num" >
						${rowData.entrustQuantity}
					</span>
</c:if>
<c:if test='${(searchPOCategory == IMMEDIATELY_PORDER && searchImmediatelyPOCategory == NORMAL_PORDER) || searchPOCategory == ENTRUST_PORDER}'>
					<span class="num" >
						<c:if test='${isOutputExcel}'>
							${rowData.pOrderQuantity}
						</c:if>
					</span>
						<c:if test='${!isOutputExcel}'>
						<input type="text" name="searchResultList[${f:h(s.index)}].pOrderQuantity" id="searchResultList[${f:h(s.index)}].pOrderQuantity" style="width:53px;"  tabindex="${f:h(s.index)*2+1001}"
						value="${rowData.pOrderQuantity}" maxlength="${f:h(ML_QUANTITY)}" class="EnableTabindex BDCexecTarget numeral_commas num"/>
						</c:if>
</c:if>&nbsp;
					</td>
					<td class="xl67"  style="text-align: right">
						<span class="num" >
						&nbsp;${f:h(rowData.poLot)}&nbsp;
						</span>
<c:if test='${!isOutputExcel}'>
						<input type="hidden" name="searchResultList[${f:h(s.index)}].poLot"
						value="${rowData.poLot}"/>
</c:if>
					</td>

					<td class="xl67"  style="text-align: right">
						<span class="num" >
						&nbsp;${f:h(rowData.leadTime)}&nbsp;
						</span>
<c:if test='${!isOutputExcel}'>
						<input type="hidden" name="searchResultList[${f:h(s.index)}].leadTime"
						value="${rowData.leadTime}"/>
</c:if>
					</td>

					<td class="xl67"  style="text-align: right">
						<span class="num" >
						&nbsp;${f:h(rowData.avgShipCount)}&nbsp;
						</span>
<c:if test='${!isOutputExcel}'>
						<input type="hidden" name="searchResultList[${f:h(s.index)}].avgShipCount"
						value="${rowData.avgShipCount}"/>
</c:if>
					</td>

					<td class="xl66"  style="text-align: right">
						<span class="per" >
						&nbsp;${f:h(rowData.salesStandardDeviation)}&nbsp;
						</span>
<c:if test='${!isOutputExcel}'>
						<input type="hidden" name="searchResultList[${f:h(s.index)}].salesStandardDeviation"
						value="${rowData.salesStandardDeviation}"/>
</c:if>
					</td>

					<td class="xl67"  style="text-align: right">
						<span class="num" >
						&nbsp;${f:h(rowData.stockQuantity)}&nbsp;
						</span>
<c:if test='${!isOutputExcel}'>
						<input type="hidden" name="searchResultList[${f:h(s.index)}].stockQuantity"
						value="${rowData.stockQuantity}"/>
</c:if>
					</td>

					<td class="xl67"  style="text-align: right">
						<span class="num" >
						&nbsp;${f:h(rowData.mineSafetyStock)}&nbsp;
						</span>
<c:if test='${!isOutputExcel}'>
						<input type="hidden" name="searchResultList[${f:h(s.index)}].mineSafetyStock"
						value="${rowData.mineSafetyStock}"/>
</c:if>
					</td>

					<td class="xl67"  style="text-align: right">
						<span class="num" >
						&nbsp;${f:h(rowData.poNum)}&nbsp;
						</span>
<c:if test='${!isOutputExcel}'>
						<input type="hidden" name="searchResultList[${f:h(s.index)}].poNum"
						value="${rowData.poNum}"/>
</c:if>
					</td>

					<td class="xl67"  style="text-align: right">
						<span class="num" >
						&nbsp;${f:h(rowData.entrustQuantity)}&nbsp;
						</span>
<c:if test='${!isOutputExcel}'>
						<input type="hidden" name="searchResultList[${f:h(s.index)}].entrustQuantity"
						value="${rowData.entrustQuantity}"/>
</c:if>
					</td>

					<td class="xl67"  style="text-align: right">
						<span class="num" >
						&nbsp;${f:h(rowData.entrustSafetyStock)}&nbsp;
						</span>
<c:if test='${!isOutputExcel}'>
						<input type="hidden" name="searchResultList[${f:h(s.index)}].entrustSafetyStock"
						value="${rowData.entrustSafetyStock}"/>
</c:if>
					</td>

					<td class="xl67"  style="text-align: right">
						<span class="num" >
						&nbsp;${f:h(rowData.entrustPoNum)}&nbsp;
						</span>
<c:if test='${!isOutputExcel}'>
						<input type="hidden" name="searchResultList[${f:h(s.index)}].entrustPoNum"
						value="${rowData.entrustPoNum}"/>
</c:if>
					</td>

					<td class="xl67"  style="text-align: right">
						<span class="num" >
						&nbsp;${f:h(rowData.holdTerm)}&nbsp;
						</span>
<c:if test='${!isOutputExcel}'>
						<input type="hidden" name="searchResultList[${f:h(s.index)}].holdTerm"
						value="${rowData.holdTerm}"/>
</c:if>
					</td>
					<td class="xl67"  style="text-align: right">
						<span class="num" >
						&nbsp;${f:h(rowData.holdQuantity)}&nbsp;
						</span>
<c:if test='${!isOutputExcel}'>
						<input type="hidden" name="searchResultList[${f:h(s.index)}].holdQuantity"
						value="${rowData.holdQuantity}"/>
</c:if>
					</td>
					<td class="xl67"  style="text-align: right">
						<span class="num" >
						&nbsp;${f:h(rowData.poRestQuantity)}&nbsp;
						</span>
<c:if test='${!isOutputExcel}'>
						<input type="hidden" name="searchResultList[${f:h(s.index)}].poRestQuantity"
						value="${rowData.poRestQuantity}"/>
</c:if>
					</td>
					<td class="xl67"  style="text-align: right">
						<span class="num" >
						&nbsp;${f:h(rowData.entrustRestQuantity)}&nbsp;
						</span>
<c:if test='${!isOutputExcel}'>
						<input type="hidden" name="searchResultList[${f:h(s.index)}].entrustRestQuantity"
						value="${rowData.entrustRestQuantity}"/>
</c:if>
					</td>
					<td class="xl67"  style="text-align: right">
						<span class="num" >
						&nbsp;${f:h(rowData.roRestQuantity)}&nbsp;
						</span>
<c:if test='${!isOutputExcel}'>
						<input type="hidden" name="searchResultList[${f:h(s.index)}].roRestQuantity"
						value="${rowData.roRestQuantity}"/>
</c:if>
					</td>

				</tr>
				</c:forEach>
				</tbody>
			</table>

<c:if test='${!isOutputExcel}'>
			<input id="searchSupplierCode" name="searchSupplierCode" type="hidden"  value="${f:h(searchSupplierCode)}">
			<input id="searchSupplierName" name="searchSupplierName" type="hidden"  value="${f:h(searchSupplierName)}">
			<input id="searchPOCategory" name="searchPOCategory" type="hidden"  value="${f:h(searchPOCategory)}">
			<input id="searchPOCategoryName" name="searchPOCategoryName" type="hidden"  value="${f:h(searchPOCategoryName)}">
			<input id="searchImmediatelyPOCategory" name="searchImmediatelyPOCategory" type="hidden"  value="${f:h(searchImmediatelyPOCategory)}">
			<input id="searchImmediatelyPOCategoryName" name="searchImmediatelyPOCategoryName" type="hidden"  value="${f:h(searchImmediatelyPOCategoryName)}">
			<input id="searchExcludeHoldingStockZero" name="searchExcludeHoldingStockZero" type="hidden"  value="${f:h(searchExcludeHoldingStockZero)}">
			<input id="searchExcludeAvgShipCountZero" name="searchExcludeAvgShipCountZero" type="hidden"  value="${f:h(searchExcludeAvgShipCountZero)}">
			<input id="searchExcludeAvgLessThanHoldingStock" name="searchExcludeAvgLessThanHoldingStock" type="hidden"  value="${f:h(searchExcludeAvgLessThanHoldingStock)}">
			<input id="searchSortColumn" name="searchSortColumn" type="hidden"  value="${f:h(searchSortColumn)}">
			<input id="searchSortOrderAsc" name="searchSortOrderAsc" type="hidden"  value="${f:h(searchSortOrderAsc)}">
			<input id="searchResultCount" name="searchResultCount" type="hidden"  value="${f:h(searchResultCount)}">
</c:if>