<div style="width: 1010px; height: 25px;">
		<input type="hidden" id="searchResultCount" value="${f:h(searchResultCount)}">
		<div style="position:absolute; left: 0px;">検索結果件数： ${searchResultCount}件</div>
		<div style="position:absolute; width: 1160px; text-align: center;">
			${sw:pageLink(searchResultCount,rowCount,pageNo)}
		</div>
        <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
</div>

		<table id="detail_info" summary="searchResult" class="forms detail_info" style="table-layout: auto; margin-top: 20px;">
			<colgroup>
				<col span="1" style="width: 6%">
				<col span="1" style="width: 10%">
				<col span="1" style="width: 20%">
				<col span="1" style="width: 7%">
				<col span="1" style="width: 13%">
				<col span="1" style="width: 6%">
				<col span="1" style="width: 10%">
				<col span="1" style="width: 10%">
				<col span="1" style="width: 10%">
				<col span="1" style="width: 8%">
			</colgroup>
				<th class="rd_top_left" style="cursor: pointer; height: 30px;" onclick="sort('valid');">有効
    			<c:if test="${sortColumn == 'valid'}">
    				<c:if test="${sortOrderAsc}">▲</c:if>
    				<c:if test="${!sortOrderAsc}">▼</c:if>
    			</c:if>
                </th>
				<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('bankCode');">銀行コード
    			<c:if test="${sortColumn == 'bankCode'}">
    				<c:if test="${sortOrderAsc}">▲</c:if>
    				<c:if test="${!sortOrderAsc}">▼</c:if>
    			</c:if>
                </th>
				<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('bankName');">銀行名
    			<c:if test="${sortColumn == 'bankName'}">
    				<c:if test="${sortOrderAsc}">▲</c:if>
    				<c:if test="${!sortOrderAsc}">▼</c:if>
    			</c:if>
                </th>
				<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('storeCode');">店番
    			<c:if test="${sortColumn == 'storeCode'}">
    				<c:if test="${sortOrderAsc}">▲</c:if>
    				<c:if test="${!sortOrderAsc}">▼</c:if>
    			</c:if>
                </th>
				<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('storeName');">店名
    			<c:if test="${sortColumn == 'storeName'}">
    				<c:if test="${sortOrderAsc}">▲</c:if>
    				<c:if test="${!sortOrderAsc}">▼</c:if>
    			</c:if>
                </th>
				<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('dwbName');">科目
    			<c:if test="${sortColumn == 'dwbName'}">
    				<c:if test="${sortOrderAsc}">▲</c:if>
    				<c:if test="${!sortOrderAsc}">▼</c:if>
    			</c:if>
                </th>
				<th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('accountNum');">口座番号
    			<c:if test="${sortColumn == 'accountNum'}">
    				<c:if test="${sortOrderAsc}">▲</c:if>
    				<c:if test="${!sortOrderAsc}">▼</c:if>
    			</c:if>
                </th>
                <th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('accountOwnerName');">口座名義
    			<c:if test="${sortColumn == 'accountNum'}">
    				<c:if test="${sortOrderAsc}">▲</c:if>
    				<c:if test="${!sortOrderAsc}">▼</c:if>
    			</c:if>
                </th>

                <th class="xl64" style="cursor: pointer; height: 30px;" onclick="sort('accountOwnerKana');">口座名義カナ
    			<c:if test="${sortColumn == 'accountNum'}">
    				<c:if test="${sortOrderAsc}">▲</c:if>
    				<c:if test="${!sortOrderAsc}">▼</c:if>
    			</c:if>
                </th>

				<th class="rd_top_right">&nbsp;</th>
			</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">


		<c:if test="${f:h(bean.valid) == '1'}">
			<tr>
		</c:if>

		<c:if test="${f:h(bean.valid) != '1'}">
			<tr style="background-color: #C0C0C0">
		</c:if>

				<td>
        			<c:if test="${f:h(bean.valid) == '1'}">
						&nbsp;〇&nbsp;
        			</c:if>

				</td>
				<td>&nbsp;${f:h(bean.bankCode)}&nbsp;</td>
				<td style="white-space: normal">&nbsp;${f:h(bean.bankName)}&nbsp;</td>
				<td style="white-space: normal">&nbsp;${f:h(bean.storeCode)}&nbsp;</td>
				<td>&nbsp;${f:h(bean.storeName)}&nbsp;</td>
				<td>&nbsp;${f:h(bean.dwbName)}&nbsp;</td>
				<td>&nbsp;${f:h(bean.accountNum)}&nbsp;</td>
				<td>&nbsp;${f:h(bean.accountOwnerName)}&nbsp;</td>
				<td>&nbsp;${f:h(bean.accountOwnerKana)}&nbsp;</td>
        		<td style="text-align: center">
        			<c:if test="${isUpdate}">
        			<button class="btn_list_action" onclick="editBank('${sw:u(bean.bankId)}');">編集</button>
        			<button class="btn_list_action" onclick="deleteBank('${bean.bankId}', '${bean.updDatetm}');">削除</button>
        			</c:if>
        			<c:if test="${!isUpdate}">
        			<button class="btn_list_action" onclick="editBank('${sw:u(bean.bankId)}');">参照</button>
        			</c:if>
        		</td>
			</tr>
	</c:forEach>
        </table>
        <div style="position:absolute; width: 1160px; text-align: center; margin-top: 20px;">
        	${sw:pageLink(searchResultCount,rowCount,pageNo)}
        </div>

<%-- 今回の検索条件を保持 --%>
<input type="hidden" id="prev_bankCode" name="prev_bankCode" value="${f:h(bankCode)}">
<input type="hidden" id="prev_bankName" name="prev_bankName" value="${f:h(bankName)}">
<input type="hidden" id="prev_storeName" name="prev_storeName" value="${f:h(storeName)}">
<input type="hidden" id="prev_storeCode" name="prev_storeCode" value="${f:h(storeCode)}">
<input type="hidden" id="prev_dwbType" name="prev_dwbType" value="${f:h(dwbType)}">
<input type="hidden" id="prev_accountNum" name="prev_accountNum" value="${f:h(accountNum)}">
<input type="hidden" id="prev_accountOwnerName" name="prev_accountOwnerName" value="${f:h(accountOwnerName)}">
<input type="hidden" id="prev_accountOwnerKana" name="prev_accountOwnerKana" value="${f:h(accountOwnerKana)}">

<c:if test="${searchResultCount == 1}">
<%-- 検索結果が1件の場合にはその棚番コードをhiddenで配置する --%>
<c:forEach var="bean" items="${searchResultList}" varStatus="status">
<input type="hidden" id="singleBankId" name="singleBankId" value="${sw:u(bean.bankId)}">
</c:forEach>
</c:if>
