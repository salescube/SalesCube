<div class="search_paging" style="margin: 10px 0px 5px 0px;">
	<input type="hidden" id="searchResultCount" value="${f:h(searchResultCount)}">
	<div id="count"><bean:message key='labels.importCount'/>${f:h(searchResultCount)}<bean:message key='labels.searchResultCountUnit'/></div>
	<div id="paging_top" style="position:absolute; font-size: 12pt; top: 0px; left: 350px;">
		&nbsp;
	</div>
	<div id="pageing_denominator" style="position:absolute; top: 0px; left: 720px;">
		&nbsp;
	</div>
</div>
<span id="searchResultList">
	<table id="search_result" summary="検索結果" class="forms detail_info" style="table-layout: auto; margin-top: 20px;width:1160px">
		<colgroup>
			<col span="1" style="min-width: 10%">
			<col span="1" style="min-width: 15%">
			<col span="1" style="min-width: 20%">
			<col span="1" style="min-width: 15%">
			<col span="1" style="min-width: 30%">
			<col span="1" style="min-width: 10%">
		</colgroup>
		<thead>
			<tr>
				<th rowspan="2" id='result_status' class="rd_top_left"  style='cursor: pointer; height:60px;' onclick="sort('status')">
					<bean:message key='labels.onlineorder.status' /><%// 状態 %>
					<span id="sortStatus_status" style="color: white">
						<c:if test='${sortColumn=="status"}'>
							<c:if test='${sortOrderAsc}'>
								<bean:message key='labels.asc'/>
							</c:if>
							<c:if test='${!sortOrderAsc}'>
								<bean:message key='labels.desc'/>
							</c:if>
						</c:if>
					</span>
				</th>
				<th rowspan="2" id='result_roSlipId' style='cursor: pointer; height:60px;' onclick="sort('roSlipId')">
					<bean:message key='labels.roSlipId' /><%// 受注番号 %>
					<span id="sortStatus_roSlipId" style="color: white">
						<c:if test='${sortColumn=="roSlipId"}'>
							<c:if test='${sortOrderAsc}'>
								<bean:message key='labels.asc'/>
							</c:if>
							<c:if test='${!sortOrderAsc}'>
								<bean:message key='labels.desc'/>
							</c:if>
						</c:if>
					</span>
				</th>
				<th colspan="3" style='height:30px; border-bottom: 1px solid #555555;'>
					<bean:message key='labels.onlineorder.info' /><%// 通販サイト情報 %>
				</th>
				<th rowspan="2" id='result_loadDate' style='cursor: pointer; height:30px;' onclick="sort('loadDate')">
					<bean:message key='labels.loadDate' /><%// 取込日 %>
					<span id="sortStatus_status" style="color: white">
						<c:if test='${sortColumn=="loadDate"}'>
							<c:if test='${sortOrderAsc}'>
								<bean:message key='labels.asc'/>
							</c:if>
							<c:if test='${!sortOrderAsc}'>
								<bean:message key='labels.desc'/>
							</c:if>
						</c:if>
					</span>
				</th>
				<th rowspan="2" class="rd_top_right" style='height:60px;'>&nbsp;</th><%// 削除ボタン %>
			</tr>
			<tr>
				<th id='result_onlineOrderId' style='cursor: pointer; height:30px;' onclick="sort('onlineOrderId')">
					<bean:message key='labels.onlineorder.orderId' /><%// 注文番号 %>
					<span id="sortStatus_onlineOrderId" style="color: white">
						<c:if test='${sortColumn=="onlineOrderId"}'>
							<c:if test='${sortOrderAsc}'>
								<bean:message key='labels.asc'/>
							</c:if>
							<c:if test='${!sortOrderAsc}'>
								<bean:message key='labels.desc'/>
							</c:if>
						</c:if>
					</span>
				</th>
				<th id='result_supplierDate' style='cursor: pointer; height:30px;' onclick="sort('supplierDate')">
					<bean:message key='labels.onlineorder.roDate' /><%// 受注日 %>
					<span id="sortStatus_supplierDate" style="color: white">
						<c:if test='${sortColumn=="supplierDate"}'>
							<c:if test='${sortOrderAsc}'>
								<bean:message key='labels.asc'/>
							</c:if>
							<c:if test='${!sortOrderAsc}'>
								<bean:message key='labels.desc'/>
							</c:if>
						</c:if>
					</span>
				</th>
				<th id='result_customerName' style='cursor: pointer; height:30px;' onclick="sort('customerName')">
					<bean:message key='labels.onlineorder.orderCustomerName' /><%// 注文者氏名 %>
					<span id="sortStatus_customerName" style="color: white">
						<c:if test='${sortColumn=="customerName"}'>
							<c:if test='${sortOrderAsc}'>
								<bean:message key='labels.asc'/>
							</c:if>
							<c:if test='${!sortOrderAsc}'>
								<bean:message key='labels.desc'/>
							</c:if>
						</c:if>
					</span>
				</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="rowData" items="${searchResultList}" varStatus="statusRow">
<c:if test='${rowData.roSlipId==null or showExist == false }'>
				<tr>
					<td style="text-align: center">
						<c:if test='${rowData.roSlipId==null}'>
							<span style="color: red; font-weight: bold">
								<bean:message key='labels.onlineorder.status.notInput' /><%// 未 %>
							</span>
						</c:if>
						<c:if test='${rowData.roSlipId!=null}'>
							<span style="color: blue">
								<bean:message key='labels.onlineorder.status.input' /><%// 済 %>
							</span>
						</c:if>
					</td>
					<td style="text-align: center">&nbsp;
						<c:if test='${rowData.roSlipId==null}'>
							<c:if test="${isInputValid}">
								<bean:define id="concatUrl" value="${'/rorder/inputROrder/online/'}?roSlipId=${rowData.onlineOrderId}" />
								<button type="button" class="btn_list_action" onclick="javascript:location.doHref('${f:url(concatUrl)}');" tabindex="${statusRow.index*2+1000}"><bean:message key='labels.onlineorder.inputOrder' /><%// 受注入力 %></button>
							</c:if>
						</c:if>
						<c:if test='${rowData.roSlipId!=null}'>
							<c:if test="${isInputValid}">
								<bean:define id="concatUrl" value="${'/rorder/inputROrder/load/'}?roSlipId=${rowData.roSlipId}" />
								<a href="javascript:location.doHref('${f:url(concatUrl)}');" tabindex="${statusRow.index*2+1000}">${f:h(rowData.roSlipId)}</a>
							</c:if>
							<c:if test="${!isInputValid}">
								${f:h(rowData.roSlipId)}
							</c:if>
						</c:if>&nbsp;
					</td>
					<td style="text-align: left">
						&nbsp;${f:h(rowData.onlineOrderId)}&nbsp;
					</td>
					<td style="text-align: center">
						&nbsp;${f:h(rowData.supplierDate)}&nbsp;
					</td>
					<td style="text-align: left">
						&nbsp;${f:h(rowData.customerName)}&nbsp;
					</td>
					<td style="text-align: center">
						&nbsp;${f:h(rowData.loadDate)}
					</td>
					<td style="text-align: center">
							<button type="button" class="btn_list_action" onclick="deleteLine('${rowData.onlineOrderId}')"
									tabindex="${statusRow.index*2+1001}"><bean:message key="words.action.delLine"/><%// 削除 %></button>
					</td>
				</tr>
</c:if>
			</c:forEach>
		</tbody>
	</table>
</span>
<br>
<div class="search_paging" style="margin: 0px 0px 0px 350px; font-size: 12pt;">
	&nbsp;
</div>
