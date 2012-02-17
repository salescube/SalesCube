<div class="search_paging" style="margin: 10px 0px 5px 0px;">
	<input type="hidden" id="searchResultCount" value="${f:h(searchResultCount)}">
	<div id="count">検索結果:${f:h(searchResultCount)}件</div>
</div>
<br>
<span id="searchResultList">
	<%@ include file="/WEB-INF/view/ajax/outputStockReportAjax/outputResultList.jsp" %>
</span>
<br>
