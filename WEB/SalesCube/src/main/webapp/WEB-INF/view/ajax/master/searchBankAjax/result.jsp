<div style="width: 910px; height: 25px;">
		<input type="hidden" id="searchResultCount" value="${f:h(searchResultCount)}">
		<div style="position:absolute; left: 0px;">検索結果件数： ${searchResultCount}件</div>
		<div style="position:absolute; width: 910px; text-align: center;">
			${sw:pageLink(searchResultCount,rowCount,pageNo)}
		</div>
        <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
</div>

		<table id="search_result" summary="検索結果" class="forms" style="width: 910px">
			<colgroup>
				<col span="1" style="width: 8%">
				<col span="1" style="width: 25%">
				<col span="1" style="width: 8%">
				<col span="1" style="width: 25%">
				<col span="1" style="width: 10%">
				<col span="1" style="width: 19%">
				<col span="1" style="width: 5%">
			</colgroup>
				<th style="cursor: pointer" onclick="sort('bankCode');">銀行コード
    			<c:if test="${sortColumn == 'bankCode'}">
    				<c:if test="${sortOrderAsc}">▲</c:if>
    				<c:if test="${!sortOrderAsc}">▼</c:if>
    			</c:if>
                </th>
				<th style="cursor: pointer" onclick="sort('bankName');">銀行名
    			<c:if test="${sortColumn == 'bankName'}">
    				<c:if test="${sortOrderAsc}">▲</c:if>
    				<c:if test="${!sortOrderAsc}">▼</c:if>
    			</c:if>
                </th>
				<th style="cursor: pointer" onclick="sort('storeCode');">店番
    			<c:if test="${sortColumn == 'storeCode'}">
    				<c:if test="${sortOrderAsc}">▲</c:if>
    				<c:if test="${!sortOrderAsc}">▼</c:if>
    			</c:if>
                </th>
				<th style="cursor: pointer" onclick="sort('storeName');">店名
    			<c:if test="${sortColumn == 'storeName'}">
    				<c:if test="${sortOrderAsc}">▲</c:if>
    				<c:if test="${!sortOrderAsc}">▼</c:if>
    			</c:if>
                </th>
				<th style="cursor: pointer" onclick="sort('dwbName');">科目
    			<c:if test="${sortColumn == 'dwbName'}">
    				<c:if test="${sortOrderAsc}">▲</c:if>
    				<c:if test="${!sortOrderAsc}">▼</c:if>
    			</c:if>
                </th>
				<th style="cursor: pointer" onclick="sort('accountNum');">口座番号
    			<c:if test="${sortColumn == 'accountNum'}">
    				<c:if test="${sortOrderAsc}">▲</c:if>
    				<c:if test="${!sortOrderAsc}">▼</c:if>
    			</c:if>
                </th>
				<th>&nbsp;</th>
			</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
			<tr>
				<td>${f:h(bean.bankCode)}</td>
				<td style="white-space: normal">${f:h(bean.bankName)}</td>
				<td style="white-space: normal">${f:h(bean.storeCode)}</td>
				<td>${f:h(bean.storeName)}</td>
				<td>${f:h(bean.dwbName)}</td>
				<td>${f:h(bean.accountNum)}</td>
        		<td style="text-align: center">
        			<c:if test="${isUpdate}">
        			<button onclick="editBank('${sw:u(bean.bankId)}');">編集</button>
        			<button onclick="deleteBank('${bean.bankId}', '${bean.updDatetm}');">削除</button>
        			</c:if>
        			<c:if test="${!isUpdate}">
        			<button onclick="editBank('${sw:u(bean.bankId)}');">参照</button>
        			</c:if>
        		</td>
			</tr>
	</c:forEach>
        </table>
        <div style="position:absolute; width: 910px; text-align: center;">
        	${sw:pageLink(searchResultCount,rowCount,pageNo)}
        </div>


<input type="hidden" id="prev_bankCode" name="prev_bankCode" value="${f:h(bankCode)}">
<input type="hidden" id="prev_bankName" name="prev_bankName" value="${f:h(bankName)}">
<input type="hidden" id="prev_storeName" name="prev_storeName" value="${f:h(storeName)}">
<input type="hidden" id="prev_storeCode" name="prev_storeCode" value="${f:h(storeCode)}">
<input type="hidden" id="prev_dwbType" name="prev_dwbType" value="${f:h(dwbType)}">
<input type="hidden" id="prev_accountNum" name="prev_accountNum" value="${f:h(accountNum)}">

<c:if test="${searchResultCount == 1}">

<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<input type="hidden" id="singleBankId" name="singleBankId" value="${sw:u(bean.bankId)}">
</c:forEach>
</c:if>
