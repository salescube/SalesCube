<div style="width: 910px; height: 25px;">
	<div style="position:absolute; left: 0px;">検索結果件数： ${searchResultCount}件</div>
	<input type="hidden" id="searchResultCount" value="${searchResultCount}">

	<div style="position:absolute; width: 910px; text-align: center;">
		${sw:pageLink(searchResultCount,rowCount,pageNo)}
	</div>

	<jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
</div>

<table id="search_result" summary="searchResult" class="forms" style="width: 910px;">
	<colgroup>
		<col span="1" style="width: 8%">
		<col span="1" style="width: 8%">
		<col span="1" style="width:12%">
		<col span="1" style="width:12%">
		<col span="1" style="width:12%">
		<col span="1" style="width:48%">
	</colgroup>
	<tr>
		<th>
			<bean:message key='labels.select'/>
		</th><!-- 選択 -->
		<th style="cursor: pointer"  onclick="sort('siPrintCount');">
			<bean:message key='labels.output.yet'/>
			<c:if test="${sortColumn == 'siPrintCount'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th><!-- 出力済 -->
		<th style="cursor: pointer"  onclick="sort('roSlipId');">
			<bean:message key='labels.roSlipId'/>
			<c:if test="${sortColumn == 'roSlipId'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th><!-- 受注番号 -->
		<th style="cursor: pointer"  onclick="sort('salesSlipId');">
			<bean:message key='labels.salesSlipId'/>
			<c:if test="${sortColumn == 'salesSlipId'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th><!-- 売上番号 -->
		<th style="cursor: pointer"  onclick="sort('salesDate');">
			<bean:message key='labels.salesDate'/>
			<c:if test="${sortColumn == 'salesDate'}">
				<c:if test="${sortOrderAsc}">▲</c:if>
				<c:if test="${!sortOrderAsc}">▼</c:if>
			</c:if>
		</th><!-- 売上日 -->
		<th><bean:message key='labels.memorandum'/></th><!-- 備考 -->
	</tr>
	<tbody id="tbodyLine">
	<c:forEach var="searchResultList" items="${searchResultList}" varStatus="s">
		<tr id="resultTr${f:h(s.index)}">
			<td style="text-align :center;"><input id="row_${f:h(s.index)}" type="checkbox" checked="checked" onclick="checkCount()" tabindex="1002" value="${f:h(searchResultList.salesSlipId)}"><!-- 選択 --></td>
			<td style="text-align :center;">
				<c:if test="${searchResultList.isSiPrinted eq true}">
					<span style="color:blue" ><bean:message key='words.adverb.already'/></span>
				</c:if>
				<c:if test="${searchResultList.isSiPrinted eq false}">
					<span style="color:red"><B><bean:message key='words.adverb.yet'/></B></span>
				</c:if>
			</td>
			<td>${f:h(searchResultList.roSlipId)}</td><!--受注番号 -->
			<td id="salesSlipId_row_${f:h(s.index)}">${f:h(searchResultList.salesSlipId)}</td><!-- 売上番号 -->
			<td>${f:h(searchResultList.salesDate)}</td><!--売上日 -->
			<td>${f:h(searchResultList.remarks)}</td><!-- 備考 -->
		</tr>
	</c:forEach>
	</tbody>
</table>

<div style="position:absolute; width: 910px; text-align: center;">
	${sw:pageLink(searchResultCount,rowCount,pageNo)}
</div>

<div id="AllSalesSlipIdList">
	<c:forEach var="searchResultList" items="${allSearchResultList}" varStatus="s"><input type="hidden" name="slipId" value="${f:h(searchResultList.salesSlipId)}"></c:forEach>
</div>