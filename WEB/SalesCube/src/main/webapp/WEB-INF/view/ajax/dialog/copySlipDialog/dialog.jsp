<div id="${dialogId}" title="伝票呼出" style="display: none;">
	<s:form onsubmit="return false;" style="margin: 0px;">
	<span id="${dialogId}_errors" style="color: red">
	</span>
	<table class="forms" style="width: 550px;" summary="呼出元伝票">
		<tr>
			<th>呼出元伝票</th>
			<td>
				<select id="${dialogId}_slipName" tabindex="14000"
					onchange="_changeSlipName('${dialogId}', this.value);">
					<c:if test="${menuId == '0200'}">
						<option value="ESTIMATE">見積伝票</option>
					</c:if>
					<c:if test="${menuId == '0300'}">
						<option value="RORDER">受注伝票</option>
					</c:if>
					<c:if test="${menuId == '0400'}">
						<option value="RORDER">受注伝票</option>
					</c:if>
					<c:if test="${menuId == '0600'}">
						<option value="DEPOSIT">入金伝票</option>
					</c:if>
					<c:if test="${menuId == '0700'}">
						<option value="PORDER">発注伝票</option>
					</c:if>
					<c:if test="${menuId == '0800'}">
						<bean:define id="slipType" value="supplier" />
						<option value="PORDER">発注伝票</option>
						<option value="ENTRUST_PORDER">委託発注伝票</option>
					</c:if>
					<c:if test="${menuId == '0900'}">
						<bean:define id="slipType" value="payment" />
						<option value="PORDER">発注伝票</option>
					</c:if>
					<c:if test="${menuId == '1007'}">
						<bean:define id="slipType" value="entrustStock" />
						<option value="ENTRUST_PORDER">委託発注伝票</option>
					</c:if>
				</select>
			</td>
<c:if test="${menuId == '1007'}">
			<th>委託入出庫区分</th>
			<td>
				<html:select property="copySlipEntrustEadCategory" styleId="copySlipEntrustEadCategory" onchange="changeEntrustEadCategoryOnCopyDialog();" >
					<html:options collection="entrustCategoryList" property="value" labelProperty="label"/>
				</html:select>
			</td>
</c:if>
		</tr>
	</table>
	</s:form>

	
	<c:if test="${menuId == '0200'}">
		<%@ include
			file="/WEB-INF/view/ajax/dialog/copySlipDialog/slip/estimate.jsp"%>
	</c:if>
	<c:if test="${menuId == '0300'}">
		<%@ include file="/WEB-INF/view/ajax/dialog/copySlipDialog/slip/rorder.jsp"%>
	</c:if>
	<c:if test="${menuId == '0400'}">
		<%@ include file="/WEB-INF/view/ajax/dialog/copySlipDialog/slip/sales.jsp"%>
		<%@ include file="/WEB-INF/view/ajax/dialog/copySlipDialog/slip/rorder.jsp"%>
	</c:if>
	<c:if test="${menuId == '0600'}">
		<%@ include file="/WEB-INF/view/ajax/dialog/copySlipDialog/slip/deposit.jsp"%>
	</c:if>
	<c:if test="${menuId == '0700'}">
		<%@ include file="/WEB-INF/view/ajax/dialog/copySlipDialog/slip/porder.jsp"%>
	</c:if>
	<c:if test="${menuId == '0800'}">
		<%@ include file="/WEB-INF/view/ajax/dialog/copySlipDialog/slip/porder.jsp"%>
		<%@ include file="/WEB-INF/view/ajax/dialog/copySlipDialog/slip/entrustPorder.jsp"%>
	</c:if>
	<c:if test="${menuId == '0900'}">
		<%@ include file="/WEB-INF/view/ajax/dialog/copySlipDialog/slip/porder.jsp"%>
	</c:if>
	<c:if test="${menuId == '1007'}">
		<%@ include file="/WEB-INF/view/ajax/dialog/copySlipDialog/slip/entrustPorder.jsp"%>
	</c:if>

	<div style="width: 550px; text-align: right">
		<button id="${dialogId}_copyButton"
			type="button" style="width: 70px" tabindex="14150" disabled
			onclick="_copySlip('${dialogId}', $('#${dialogId}_slipName').val());">設定</button>
		<button type="button" style="width: 70px" tabindex="14151"
			onclick="$('#${dialogId}').dialog('close');">閉じる</button>
	</div>

</div>
