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
		<col span="1" style="width: 50px">
		<col span="1" style="width:175px">

		<col span="1" style="width: 50px">
		<col span="1" style="width:175px">

		<col span="1" style="width: 50px">
		<col span="1" style="width:175px">

		<col span="1" style="width:125px">
	</colgroup>
	<tr>
		<th class="rd_top_left" style="height: 30px; border-bottom: 1px solid #555555;" colspan="2">大分類</th>
		<th colspan="2" style="border-bottom: 1px solid #555555;">中分類</th>
		<th colspan="2" style="border-bottom: 1px solid #555555;">小分類</th>
		<th class="rd_top_right" rowspan="2" style="border-bottom: 1px solid #555555;"></th>
	</tr>

	<tr>
		<th style="cursor: pointer; height: 30px; white-space: nowrap;"onclick="sort('classCode1');">コード
   			<c:if test="${sortColumn == 'classCode1'}">
   				<c:if test="${sortOrderAsc}">▲</c:if>
   				<c:if test="${!sortOrderAsc}">▼</c:if>
   			</c:if>
		</th>
		<th style="cursor: pointer; white-space: nowrap;" onclick="sort('className1');">名称
   			<c:if test="${sortColumn == 'className1'}">
   				<c:if test="${sortOrderAsc}">▲</c:if>
   				<c:if test="${!sortOrderAsc}">▼</c:if>
   			</c:if>
		</th>
		<th style="cursor: pointer; white-space: nowrap;" onclick="sort('classCode2');">コード
   			<c:if test="${sortColumn == 'classCode2'}">
   				<c:if test="${sortOrderAsc}">▲</c:if>
   				<c:if test="${!sortOrderAsc}">▼</c:if>
   			</c:if>
		</th>
		<th style="cursor: pointer; white-space: nowrap;" onclick="sort('className2');">名称
   			<c:if test="${sortColumn == 'className2'}">
   				<c:if test="${sortOrderAsc}">▲</c:if>
   				<c:if test="${!sortOrderAsc}">▼</c:if>
   			</c:if>
		</th>
		<th style="cursor: pointer; white-space: nowrap;" onclick="sort('classCode3');">コード
   			<c:if test="${sortColumn == 'classCode3'}">
   				<c:if test="${sortOrderAsc}">▲</c:if>
   				<c:if test="${!sortOrderAsc}">▼</c:if>
   			</c:if>
		</th>
		<th style="cursor: pointer; white-space: nowrap;"  onclick="sort('className3');">名称
   			<c:if test="${sortColumn == 'className3'}">
   				<c:if test="${sortOrderAsc}">▲</c:if>
   				<c:if test="${!sortOrderAsc}">▼</c:if>
   			</c:if>
		</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
	<tr>
		<td>&nbsp;${bean.classCode1}&nbsp;</td>
		<td style="white-space: normal">&nbsp;${f:h(bean.className1)}&nbsp;</td>
		<td>&nbsp;${bean.classCode2}&nbsp;</td>
		<td style="white-space: normal">&nbsp;${f:h(bean.className2)}&nbsp;</td>
		<td>&nbsp;${bean.classCode3}</td>
		<td style="white-space: normal">&nbsp;${f:h(bean.className3)}&nbsp;</td>
		<td style="text-align: center">
			<c:if test="${isUpdate}">
			<button class="btn_list_action"  onclick="editProductClass('${bean.classCode1}','${bean.classCode2}','${bean.classCode3}');">編集</button>
			<button class="btn_list_action"  onclick="deleteProductClass('${bean.classCode1}','${bean.classCode2}','${bean.classCode3}','${bean.updDatetm}');">削除</button>
			</c:if>
			<c:if test="${!isUpdate}">
			<button class="btn_list_action"  onclick="editProductClass('${bean.classCode1}','${bean.classCode2}','${bean.classCode3}');">参照</button>
			</c:if>
		</td>
	</tr>
	</c:forEach>

</table>

<div style="position:absolute; width: 1160px; text-align: center; margin-top: 20px;">
	${sw:pageLink(searchResultCount,rowCount,pageNo)}
</div>

<%-- 今回の検索条件を保持 --%>
<input type="hidden" id="prev_classCode1" name="prev_classCode1" value="${classCode1}">
<input type="hidden" id="prev_classCode2" name="prev_classCode2" value="${f:h(classCode2)}">
<input type="hidden" id="prev_classCode3" name="prev_classCode3" value="${f:h(classCode3)}">
<input type="hidden" id="prev_classCode" name="prev_classCode" value="${classCode}">
<input type="hidden" id="prev_className" name="prev_className" value="${f:h(className)}">
<input type="hidden" id="prev_pageNo" name="prev_pageNo" value="${pageNo}">
<input type="hidden" id="prev_rowCount" name="prev_rowCount" value="${rowCount}">


<c:if test="${searchResultCount == 1}">
<%-- 検索結果が1件の場合にはその顧客コードをhiddenで配置する --%>
<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<input type="hidden" id="singleProductClassCode" name="singleProductClassCode" value="${bean.classCode1}-${bean.classCode2}-${bean.classCode3}">
</c:forEach>
</c:if>
