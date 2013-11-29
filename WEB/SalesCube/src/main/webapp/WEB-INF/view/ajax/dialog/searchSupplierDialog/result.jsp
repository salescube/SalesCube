<table style="width: 620px;">
	<tr>
		<td style="text-align: left; color: #FFFFFF;">検索結果件数: ${searchResultCount}件</td>
		<td style="text-align: right;">
			<span style="color: red">
			 	<html:messages id="resultThreshold" message="true">
			 		<bean:write name="resultThreshold" filter="false"/>
			 	</html:messages>
			</span>
		</td>
	</tr>
</table>

<div id="${dialogId}ListContainer"
	style="border: none; width: 620px; height: 220px; overflow: hidden;">
<table class="dialog_resultList"  id="${dialogId}List" summary="仕入先検索結果" style="width: 100%;">
	<colgroup>
		<col span="1" style="width: 20%">
		<col span="1" style="width: 30%">
		<col span="1" style="width: 20%">
		<col span="1" style="width: 15%">
		<col span="1" style="width: 15%">
	</colgroup>
	<tr>
		<th>仕入先コード</th>
		<th>仕入先名</th>
		<th>担当者</th>
		<th>取引区分</th>
		<th>備考</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td>
				<a href="javascript:void(0)"  tabindex="6100" style="color: #1D9CCC" onclick="_selectLinkSearchResult( '${dialogId}', '${f:h(bean.supplierCode)}');
				$('#${dialogId}').dialog('close');" >${f:h(bean.supplierCode)}</a>
			</td>
			<td>${f:h(bean.supplierName)}</td>
			<td>${f:h(bean.supplierPcName)}</td>
			<td>${f:h(bean.supplierCmCategoryName)}</td>
			<td>${f:h(bean.remarks)}</td>
		</tr>
	</c:forEach>
</table>
</div>

<c:forEach var="bean" items="${searchResultList}" varStatus="status">
	<%-- 顧客マスタの属性値をhiddenに保持する --%>
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierCode" name="${dialogId}_${f:h(bean.supplierCode)}_supplierCode" value="${f:h(bean.supplierCode)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierName" name="${dialogId}_${f:h(bean.supplierCode)}_supplierName" value="${f:h(bean.supplierName)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierKana" name="${dialogId}_${f:h(bean.supplierCode)}_supplierKana" value="${f:h(bean.supplierKana)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierAbbr" name="${dialogId}_${f:h(bean.supplierCode)}_supplierAbbr" value="${f:h(bean.supplierAbbr)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierZipCode" name="${dialogId}_${f:h(bean.supplierCode)}_supplierZipCode" value="${f:h(bean.supplierZipCode)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierAddress1" name="${dialogId}_${f:h(bean.supplierCode)}_supplierAddress1" value="${f:h(bean.supplierAddress1)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierAddress2" name="${dialogId}_${f:h(bean.supplierCode)}_supplierAddress2" value="${f:h(bean.supplierAddress2)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierDeptName" name="${dialogId}_${f:h(bean.supplierCode)}_supplierDeptName" value="${f:h(bean.supplierDeptName)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierPcName" name="${dialogId}_${f:h(bean.supplierCode)}_supplierPcName" value="${f:h(bean.supplierPcName)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierPcKana" name="${dialogId}_${f:h(bean.supplierCode)}_supplierPcKana" value="${f:h(bean.supplierPcKana)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierPcPreCategory" name="${dialogId}_${f:h(bean.supplierCode)}_supplierPcPreCategory" value="${f:h(bean.supplierPcPreCategory)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierPcPost" name="${dialogId}_${f:h(bean.supplierCode)}_supplierPcPost" value="${f:h(bean.supplierPcPost)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierTel" name="${dialogId}_${f:h(bean.supplierCode)}_supplierTel" value="${f:h(bean.supplierTel)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierFax" name="${dialogId}_${f:h(bean.supplierCode)}_supplierFax" value="${f:h(bean.supplierFax)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierEmail" name="${dialogId}_${f:h(bean.supplierCode)}_supplierEmail" value="${f:h(bean.supplierEmail)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierUrl" name="${dialogId}_${f:h(bean.supplierCode)}_supplierUrl" value="${f:h(bean.supplierUrl)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierCmCategory" name="${dialogId}_${f:h(bean.supplierCode)}_supplierCmCategory" value="${f:h(bean.supplierCmCategory)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_supplierCmCategoryName" name="${dialogId}_${f:h(bean.supplierCode)}_supplierCmCategoryName" value="${f:h(bean.supplierCmCategoryName)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_taxShiftCategory" name="${dialogId}_${f:h(bean.supplierCode)}_taxShiftCategory" value="${f:h(bean.taxShiftCategory)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_paymentTypeCategory" name="${dialogId}_${f:h(bean.supplierCode)}_paymentTypeCategory" value="${f:h(bean.paymentTypeCategory)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_paymentCycleCategory" name="${dialogId}_${f:h(bean.supplierCode)}_paymentCycleCategory" value="${f:h(bean.paymentCycleCategory)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_lastCutoffDate" name="${dialogId}_${f:h(bean.supplierCode)}_lastCutoffDate" value="${f:h(bean.lastCutoffDate)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_paymentDate" name="${dialogId}_${f:h(bean.supplierCode)}_paymentDate" value="${f:h(bean.paymentDate)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_taxFractCategory" name="${dialogId}_${f:h(bean.supplierCode)}_taxFractCategory" value="${f:h(bean.taxFractCategory)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_priceFractCategory" name="${dialogId}_${f:h(bean.supplierCode)}_priceFractCategory" value="${f:h(bean.priceFractCategory)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_poSlipComeoutCategory" name="${dialogId}_${f:h(bean.supplierCode)}_poSlipComeoutCategory" value="${f:h(bean.poSlipComeoutCategory)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_serviceChargeCategory" name="${dialogId}_${f:h(bean.supplierCode)}_serviceChargeCategory" value="${f:h(bean.serviceChargeCategory)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_transferTypeCategory" name="${dialogId}_${f:h(bean.supplierCode)}_transferTypeCategory" value="${f:h(bean.transferTypeCategory)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_nationalCategory" name="${dialogId}_${f:h(bean.supplierCode)}_nationalCategory" value="${f:h(bean.nationalCategory)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_rateId" name="${dialogId}_${f:h(bean.supplierCode)}_rateId" value="${f:h(bean.rateId)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_remarks" name="${dialogId}_${f:h(bean.supplierCode)}_remarks" value="${f:h(bean.remarks)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.supplierCode)}_commentData" name="${dialogId}_${f:h(bean.supplierCode)}_commentData" value="${f:h(bean.commentData)}">
</c:forEach>