<div style="width: 910px; height: 25px;">
	<div style="position:absolute; left: 0px;">検索結果件数： ${searchResultCount}件</div>
	<input type="hidden" id="searchResultCount" value="${searchResultCount}">

	<div style="position:absolute; width: 910px; text-align: center;">
		${sw:pageLink(searchResultCount,rowCount,pageNo)}
	</div>

	<jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
</div>

<table id="search_result" summary="searchResult" class="forms" style="width: 900px;">
	<colgroup>
		<col span="1" style="width: 8%">
		<col span="1" style="width: 8%">
		<col span="1" style="width: 8%">
		<col span="1" style="width: 10%">
		<col span="1" style="width: 20%">
		<col span="1" style="width: 7%">
		<col span="1" style="width: 7%">
		<col span="1" style="width: 7%">
		<col span="1" style="width: 7%">
		<col span="1" style="width: 7%">
		<col span="1" style="width:11%">
	</colgroup>
	<tr>
		<th rowspan="2" style="cursor: pointer"  onclick="sort('roSlipId');">
			<bean:message key='labels.roSlipId'/>
			<c:if test="${sortColumn == 'roSlipId'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th><!-- 受注番号 -->
		<th rowspan="2" style="cursor: pointer"  onclick="sort('salesSlipId');">
			<bean:message key='labels.salesSlipId'/>
			<c:if test="${sortColumn == 'salesSlipId'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th><!-- 売上番号 -->
		<th rowspan="2" style="cursor: pointer"  onclick="sort('salesDate');">
			<bean:message key='labels.salesDate'/>
			<c:if test="${sortColumn == 'salesDate'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th><!-- 売上日 -->
		<th rowspan="2" style="cursor: pointer"  onclick="sort('customerCode');">
			<bean:message key='labels.customerCode'/>
			<c:if test="${sortColumn == 'customerCode'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th><!-- 顧客コード -->
		<th rowspan="2" style="cursor: pointer"  onclick="sort('customerName');">
			<bean:message key='labels.customerName'/>
			<c:if test="${sortColumn == 'customerName'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th><!-- 顧客名 -->
		<th colspan="6"><bean:message key='labels.output'/></th><!-- 出力 -->
	</tr>
	<tr>
		<th><bean:message key='labels.slip.picking'/></th><!-- ピッキング -->
		<th><bean:message key='labels.slip.estimate'/></th><!-- 見積書 -->
		<th><bean:message key='labels.slip.delivery'/></th><!-- 納品書 -->
		<th><bean:message key='labels.slip.deliveryReceipt'/></th><!-- 納品書兼領収書 -->
		<th><bean:message key='labels.slip.tempDelivery'/></th><!-- 仮納品書 -->
		<th><bean:message key='labels.slip.bill'/></th><!-- 請求書 -->
	</tr>
	<tbody id="tbodyLine">
	<c:forEach var="searchResultList" items="${searchResultList}" varStatus="s">
		<tr id="resultTr${f:h(s.index)}">
			<input type="hidden" id="dispDateFlag${f:h(s.index)}" value="${f:h(searchResultList.dispDateFlag)}">
			<td style="text-align: right">
				${f:h(searchResultList.roSlipId)}
				<input type="hidden" id="reportRoSlipId${f:h(s.index)}" value="${f:h(searchResultList.roSlipId)}">
			</td><!--受注番号 -->
			<td style="text-align: right" id="reportSalesSlipTd${f:h(s.index)}">
				${f:h(searchResultList.salesSlipId)}
				<input type="hidden" id="reportSalesSlipId${f:h(s.index)}" value="${f:h(searchResultList.salesSlipId)}">
			</td><!--売上番号 -->
			<td style="text-align: center">${f:h(searchResultList.salesDate)}</td><!--売上日 -->
			<td>${f:h(searchResultList.customerCode)}</td><!--顧客コード -->
			<td>${f:h(searchResultList.customerName)}</td><!--顧客名 -->

			<td id="reportPickingListTd${f:h(s.index)}" style="text-align :center;"><!-- ピッキングリスト -->
				<c:if test="${searchResultList.isPickingListCheckDisp eq true}">
					<c:if test="${searchResultList.shippingPrintCount > 0}">
						<span style="color:blue"><bean:message key='words.adverb.already'/></span>
						<input id="report_${searchResultList.REPORT_PICKING_LIST}_already" type="checkbox" onclick="checkCount('resultTr${f:h(s.index)}')" tabindex="${1008 + (s.index * resultElementCount)}" value="${f:h(searchResultList.pickingListCheckId)}">
					</c:if>
					<c:if test="${searchResultList.shippingPrintCount <= 0}">
						<span style="color:red"><bean:message key='words.adverb.yet'/></span>
						<input id="report_${searchResultList.REPORT_PICKING_LIST}_yet" type="checkbox" onclick="checkCount('resultTr${f:h(s.index)}')" tabindex="${1008 + (s.index * resultElementCount)}" value="${f:h(searchResultList.pickingListCheckId)}">
					</c:if>
					<input type="hidden" name="printCount" value="${searchResultList.shippingPrintCount}">
				</c:if>
			</td>
			<td id="reportEstimateTd${f:h(s.index)}" style="text-align :center;"><!-- 見積書 -->
				<c:if test="${searchResultList.isEstimateCheckDisp eq true}">
					<c:if test="${searchResultList.estimatePrintCount > 0}">
						<span style="color:blue"><bean:message key='words.adverb.already'/></span>
						<input id="report_${searchResultList.REPORT_ESTIMATE}_already" type="checkbox" onclick="checkCount('resultTr${f:h(s.index)}')" tabindex="${1004 + (s.index * resultElementCount)}" value="${f:h(searchResultList.estimateCheckId)}">
					</c:if>
					<c:if test="${searchResultList.estimatePrintCount <= 0}">
						<span style="color:red"><bean:message key='words.adverb.yet'/></span>
						<input id="report_${searchResultList.REPORT_ESTIMATE}_yet" type="checkbox" onclick="checkCount('resultTr${f:h(s.index)}')" tabindex="${1004 + (s.index * resultElementCount)}" value="${f:h(searchResultList.estimateCheckId)}">
					</c:if>
					<input type="hidden" name="printCount" value="${searchResultList.estimatePrintCount}">
				</c:if>
			</td>
			<td id="reportDeliveryTd${f:h(s.index)}" style="text-align :center;"><!-- 納品書 -->
				<c:if test="${searchResultList.isDeliveryCheckDisp eq true}">
					<c:if test="${searchResultList.deliveryPrintCount > 0}">
						<span style="color:blue"><bean:message key='words.adverb.already'/></span>
						<input id="report_${searchResultList.REPORT_DELIVERY}_already" type="checkbox" onclick="checkCount('resultTr${f:h(s.index)}')" tabindex="${1006 + (s.index * resultElementCount)}" value="${f:h(searchResultList.deliveryCheckId)}">
					</c:if>
					<c:if test="${searchResultList.deliveryPrintCount <= 0}">
						<span style="color:red"><bean:message key='words.adverb.yet'/></span>
						<input id="report_${searchResultList.REPORT_DELIVERY}_yet" type="checkbox" onclick="checkCount('resultTr${f:h(s.index)}')" tabindex="${1006 + (s.index * resultElementCount)}" value="${f:h(searchResultList.deliveryCheckId)}">
					</c:if>
					<input type="hidden" name="printCount" value="${searchResultList.deliveryPrintCount}">
				</c:if>
			</td>
			<td id="reportDeriveryReceiptTd${f:h(s.index)}" style="text-align :center;"><!-- 納品書兼領収書 -->
				<c:if test="${searchResultList.isDeliveryReceiptCheckDisp eq true}">
					<c:if test="${searchResultList.delborPrintCount > 0}">
						<span style="color:blue"><bean:message key='words.adverb.already'/></span>
						<input id="report_${searchResultList.REPORT_DELIVERY_RECEIPT}_already" type="checkbox" onclick="checkCount('resultTr${f:h(s.index)}')" tabindex="${1009 + (s.index * resultElementCount)}" value="${f:h(searchResultList.deliveryReceiptCheckId)}">
					</c:if>
					<c:if test="${searchResultList.delborPrintCount <= 0}">
						<span style="color:red"><bean:message key='words.adverb.yet'/></span>
						<input id="report_${searchResultList.REPORT_DELIVERY_RECEIPT}_yet" type="checkbox" onclick="checkCount('resultTr${f:h(s.index)}')" tabindex="${1009 + (s.index * resultElementCount)}" value="${f:h(searchResultList.deliveryReceiptCheckId)}">
					</c:if>
					<input type="hidden" name="printCount" value="${searchResultList.delborPrintCount}">
				</c:if>
			</td>
			<td id="reportTempDeliveryTd${f:h(s.index)}" style="text-align :center;"><!-- 仮納品書 -->
				<c:if test="${searchResultList.isTempDeliveryCheckDisp eq true}">
					<c:if test="${searchResultList.tempDeliveryPrintCount > 0}">
						<span style="color:blue"><bean:message key='words.adverb.already'/></span>
						<input id="report_${searchResultList.REPORT_TEMP_DELIVERY}_already" type="checkbox" onclick="checkCount('resultTr${f:h(s.index)}')" ${(searchResultList.tempDeliverySlipFlag eq "0") ? "disabled" : ""}  tabindex="${1007 + (s.index * resultElementCount)}" value="${f:h(searchResultList.tempDeliveryCheckId)}">
					</c:if>
					<c:if test="${searchResultList.tempDeliveryPrintCount <= 0}">
						<span style="color:red"><bean:message key='words.adverb.yet'/></span>
						<input id="report_${searchResultList.REPORT_TEMP_DELIVERY}_yet" type="checkbox" onclick="checkCount('resultTr${f:h(s.index)}')" tabindex="${1007 + (s.index * resultElementCount)}" value="${f:h(searchResultList.tempDeliveryCheckId)}">
					</c:if>
					<input type="hidden" name="printCount" value="${searchResultList.tempDeliveryPrintCount}">
				</c:if>
			</td>
			<td id="reportBillTd${f:h(s.index)}" style="text-align :center;"><!-- 請求書 -->
				<c:if test="${searchResultList.isBillCheckDisp eq true}">
					<c:if test="${searchResultList.billPrintCount > 0}">
						<span style="color:blue"><bean:message key='words.adverb.already'/></span>
						<input id="report_${searchResultList.REPORT_BILL}_already" type="checkbox" onclick="checkCount('resultTr${f:h(s.index)}')" tabindex="${1005 + (s.index * resultElementCount)}" value="${f:h(searchResultList.billCheckId)}">
					</c:if>
					<c:if test="${searchResultList.billPrintCount <= 0}">
						<span style="color:red"><bean:message key='words.adverb.yet'/></span>
						<input id="report_${searchResultList.REPORT_BILL}_yet" type="checkbox" onclick="checkCount('resultTr${f:h(s.index)}')" tabindex="${1005 + (s.index * resultElementCount)}" value="${f:h(searchResultList.billCheckId)}">
					</c:if>
					<input type="hidden" name="printCount" value="${searchResultList.billPrintCount}">
				</c:if>
			</td>
		</tr>
	</c:forEach>
	</tbody>
</table>

<div style="position:absolute; width: 910px; text-align: center;">
	${sw:pageLink(searchResultCount,rowCount,pageNo)}
</div>

<div id="AllReportInfoList">
	<c:forEach var="searchResultList" items="${allSearchResultList}" varStatus="s">
	<c:if test="${!empty searchResultList.defaultOutputStatus}">
		<input type="hidden" name="reportInfo" value="${f:h(searchResultList.defaultOutputStatus)}">
	</c:if>
	</c:forEach>
</div>