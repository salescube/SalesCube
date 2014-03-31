<div style="width: 1010px; height: 25px;">
		<input type="hidden" id="searchResultCount" value="${f:h(searchResultCount)}">
		<div style="position:absolute; left: 0px;">検索結果件数： ${searchResultCount}件</div>
		<div style="position:absolute; width: 1160px; text-align: center;">
			${sw:pageLink(searchResultCount,rowCount,pageNo)}
		</div>
        <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
</div>

<table id="search_result" summary="searchResult" class="forms detail_info" style="table-layout: auto; margin-top: 20px;">
	<colgroup>
		<col span="1" style="width: 12%">
		<col span="1" style="width: 12%">
		<col span="1" style="width: 11%">
		<col span="1" style="width: 15%">
		<col span="1" style="width: 15%">
		<col span="1" style="width: 15%">
		<col span="1" style="width: 15%">
		<col span="1" style="width: 5%">
	</colgroup>
	<tr>
		<th class="xl64 rd_top_left" style="cursor: pointer; height: 60px;" rowspan="2" onclick="sort('rankCode');">顧客ランク<br>コード
  			<c:if test="${sortColumn == 'rankCode'}">
  				<c:if test="${sortOrderAsc}">▲</c:if>
  				<c:if test="${!sortOrderAsc}">▼</c:if>
  			</c:if>
              </th>
		<th class="xl64" style="cursor: pointer; height: 60px;" rowspan="2"onclick="sort('rankName');">顧客ランク名
  			<c:if test="${sortColumn == 'rankName'}">
  				<c:if test="${sortOrderAsc}">▲</c:if>
  				<c:if test="${!sortOrderAsc}">▼</c:if>
  			</c:if>
              </th>
		<th class="xl64" style="cursor: pointer; height: 60px;" rowspan="2"onclick="sort('rankRate');">値引率
  			<c:if test="${sortColumn == 'rankRate'}">
  				<c:if test="${sortOrderAsc}">▲</c:if>
  				<c:if test="${!sortOrderAsc}">▼</c:if>
  			</c:if>
              </th>
		<th class="xl64" style="height: 30px; border-bottom: 1px solid #555555;" colspan="4">基準</th>
		<th class="xl64 rd_top_right" rowspan="2">&nbsp;</th>
	</tr>
	<tr>
		<th class="xl64" style="height: 30px;">売上回数</th>
		<th class="xl64" style="height: 30px;">在籍期間</th>
		<th class="xl64" style="height: 30px;">離脱期間</th>
		<th class="xl64" style="height: 30px;">月平均売上額</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td>${bean.rankCode}</td>
			<td>${f:h(bean.rankName)}</td>
			<td style="text-align:right;">${f:h(bean.rankRate)}%</td>
			<td>
				&nbsp;
               <c:if test="${bean.roCountFrom != null}">
                   ${f:h(bean.roCountFrom)}回以上
               </c:if>
               <c:if test="${bean.roCountTo != null}">
                   ${f:h(bean.roCountTo)}回未満
               </c:if>
               &nbsp;
               </td>
			<td>
				&nbsp;
               <c:if test="${bean.enrollTermFrom != null}">
                   ${f:h(bean.enrollTermFrom)}日以上
               </c:if>
               <c:if test="${bean.enrollTermTo != null}">
                   ${f:h(bean.enrollTermTo)}日未満
               </c:if>
               &nbsp;
               </td>
			<td>
				&nbsp;
               <c:if test="${bean.defectTermFrom != null}">
                   ${f:h(bean.defectTermFrom)}日以上
               </c:if>
               <c:if test="${bean.defectTermTo != null}">
                   ${f:h(bean.defectTermTo)}日未満
               </c:if>
               &nbsp;
               </td>
			<td>
				&nbsp;
               <c:if test="${bean.roMonthlyAvgFrom != null}">
                   <span class="numeral_commas style_quantity">${f:h(bean.roMonthlyAvgFrom)}</span>円以上
               </c:if>
               <c:if test="${bean.roMonthlyAvgTo != null}">
                   ${f:h(bean.roMonthlyAvgTo)}円未満
               </c:if>
               &nbsp;
               </td>
       		<td style="text-align: center">
       			<c:if test="${isUpdate}">
       			<button onclick="editCustomerRank('${bean.rankCode}');" class="btn_list_action">編集</button>
       			<button onclick="deleteCustomerRank('${bean.rankCode}', '${bean.updDatetm}');" class="btn_list_action">削除</button>
       			</c:if>
       			<c:if test="${!isUpdate}">
       			<button onclick="editCustomerRank('${bean.rankCode}');" class="btn_list_action">参照</button>
       			</c:if>
       		</td>
		</tr>
	</c:forEach>
</table>

<div style="position:absolute; width: 1160px; text-align: center; margin-top: 20px;">
	${sw:pageLink(searchResultCount,rowCount,pageNo)}
</div>

<%-- 今回の検索条件を保持 --%>
<input type="hidden" id="prev_rankCode" name="prev_rankCode" value="${f:h(rankCode)}">
<input type="hidden" id="prev_rankName" name="prev_rankName" value="${f:h(rankName)}">
<input type="hidden" id="prev_rankRate1" name="prev_rankRate1" value="${f:h(rankRate1)}">
<input type="hidden" id="prev_rankRate2" name="prev_rankRate2" value="${f:h(rankRate2)}">

<c:if test="${searchResultCount == 1}">
<%-- 検索結果が1件の場合にはその棚番コードをhiddenで配置する --%>
<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<input type="hidden" id="singleCustomerRankCode" name="singleCustomerRankCode" value="${bean.rankCode}">
</c:forEach>
</c:if>

