<div style="width: 910px; height: 25px;">
	<div style="position:absolute; left: 0px;">検索結果件数： ${searchResultCount}件</div>
	<input type="hidden" id="searchResultCount" value="${searchResultCount}">

	<div style="position:absolute; width: 910px; text-align: center;">
		${sw:pageLink(searchResultCount,rowCount,pageNo)}
	</div>

	<jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
</div>

<table id="depositTotal" summary="searchResult" class="forms" style="table-layout:fixed; width:350px">
	<tr>
		<th nowrap="nowrap" style="table-layout:fixed; width:200px"><bean:message key='labels.depositResultTotal'/></th>
		<td style="width:150px" style="text-align:right"><fmt:formatNumber value="${f:h(depositTotal)}" pattern="###,##0" /></td>
	</tr>
</table>

<span id="searchResultList">
	<%@ include file="/WEB-INF/view/deposit/searchDepositResultOutput/resultList.jsp" %>
</span>

<div style="position:absolute; width: 910px; text-align: center;">
	${sw:pageLink(searchResultCount,rowCount,pageNo)}
</div>
