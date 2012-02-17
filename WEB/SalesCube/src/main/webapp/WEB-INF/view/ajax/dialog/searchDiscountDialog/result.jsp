<table style="width: 500px;">
	<tr>
		<td style="text-align: left;">検索結果件数: ${searchResultCount}件</td>
		<td style="text-align: right;">
			<span style="color: red">
			 	<html:messages id="resultThreshold" message="true">
			 		<bean:write name="resultThreshold" filter="false"/>
			 	</html:messages>
			</span>
		</td>
	</tr>
</table>

<div
	style="padding: 0px; border: none; width: 500px; height: 220px; overflow: hidden;">
<table id="${dialogId}List" summary="数量割引検索結果" style="width: 490px;">
	<colgroup>
		<col span="1" style="width: 10%">
		<col span="1" style="width: 20%">
		<col span="1" style="width: 60%">
		<col span="1" style="width: 10%">
	</colgroup>
	<tr>
		<th>&nbsp;</th>
		<th>割引コード</th>
		<th>割引名</th>
		<th>割引有効</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td style="text-align: center;"><input type="radio"
				name="${dialogId}_selectedDiscount" value="${f:h(bean.discountId)}"
				tabindex="16100"
				onclick="_updateDiscountData( '${dialogId}', 'selectedDiscount' );
					$('#${dialogId}_selectButton').attr('disabled', false);">
			</td>
			<td>${f:h(bean.discountId)}</td>
			<td>${f:h(bean.discountName)}</td>
			<td>${f:h(bean.useFlagName)}</td>
		</tr>
	</c:forEach>
</table>
</div>

<c:forEach var="bean" items="${searchResultList}" varStatus="status">

<input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_discountId" name="${dialogId}_${f:h(bean.discountId)}_discountId" value="${f:h(bean.discountId)}"><input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_discountName" name="${dialogId}_${f:h(bean.discountId)}_discountName" value="${f:h(bean.discountName)}"><input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_remarks" name="${dialogId}_${f:h(bean.discountId)}_remarks" value="${f:h(bean.remarks)}"><input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_useFlag" name="${dialogId}_${f:h(bean.discountId)}_useFlag" value="${f:h(bean.useFlag)}"><input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_useFlagName" name="${dialogId}_${f:h(bean.discountId)}_useFlagName" value="${f:h(bean.useFlagName)}">

<c:forEach var="trn" items="${bean.discountTrnList}" varStatus="status2">
	<input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_discountDataId" name="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_discountDataId" value="${f:h(trn.discountDataId)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_lineNo" name="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_lineNo" value="${f:h(trn.lineNo)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_dataFrom" name="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_dataFrom" value="${f:h(trn.dataFrom)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_dataTo" name="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_dataTo" value="${f:h(trn.dataTo)}">
	<input type="hidden" id="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_discountRate" name="${dialogId}_${f:h(bean.discountId)}_Data${trn.lineNo}_discountRate" value="${f:h(trn.discountRate)}">
</c:forEach>
</c:forEach>