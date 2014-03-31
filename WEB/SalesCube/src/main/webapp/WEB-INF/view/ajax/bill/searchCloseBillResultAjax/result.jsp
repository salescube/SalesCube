<button name="allCheck" type="button" tabindex="300" onclick="checkAll(true)" class="btn_list_action">全て選択</button>
<button name="allUnCheck" type="button" tabindex="301" onclick="checkAll(false)" class="btn_list_action">全て解除</button>

	<div id="detail_info_wrap">
	<table class="detail_info"  id="billing_close" summary="締処理リスト" class="forms" style="width: 800px;">
		<colgroup>
			<col span="1" style="width: 10%">
			<col span="1" style="width: 15%">
			<col span="1" style="width: 50%">
			<col span="1" style="width: 25%">
		</colgroup>
		<tr>
			<th class="rd_top_left" style="height: 30px;" style="cursor: pointer">選択</th>
			<th style="cursor: pointer">請求先コード</th>
			<th style="cursor: pointer">請求先名</th>
			<th class="rd_top_right" style="cursor: pointer">前回締日</th>
		</tr>
		<tbody id="tbodyLine">
			<c:forEach var="searchResultList" items="${searchResultList}" varStatus="s" >
				<tr id="trLine${s.index}">
					<td style="text-align: center">
						<html:checkbox tabindex="${302+s.index*lineElementCount}" name="searchResultList" indexed="true" property="closeCheck"
											styleId="searchResultList[${s.index}].closeCheck" />
					</td>
					<td style="text-align: center">
						&nbsp;<c:out value="${f:h(searchResultList.customerCode)}" />&nbsp;
					</td>
					<td>
						&nbsp;<c:out value="${f:h(searchResultList.customerName)}" />&nbsp;
					</td>
					<td>
						&nbsp;<c:out value="${f:h(searchResultList.billCutoffDate)}" />&nbsp;
					</td>
					<td style="display: none;">
						<html:hidden name="searchResultList" property="customerCode" indexed="true" styleId="closeBillList[${s.index}].customerCode" />
						<html:hidden name="searchResultList" property="customerName" indexed="true" styleId="closeBillList[${s.index}].customerName" />
						<html:hidden name="searchResultList" property="billCutoffDate" indexed="true" styleId="closeBillList[${s.index}].billCutoffDate" />
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
