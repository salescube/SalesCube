<table style="width: 550px;">
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
	style="padding: 0px; border: none; width: 650px; height: 220px; overflow: hidden;">
<table id="${dialogId}List" summary="棚番検索結果" style="width: 640px;">
	<colgroup>
		<col span="1" style="width: 10%">
		<col span="1" style="width: 15%">
		<col span="1" style="width: 30%">
		<col span="1" style="width: 10%">
		<col span="1" style="width: 15%">
		<col span="1" style="width: 30%">
	</colgroup>
	<tr>
		<th>&nbsp;</th>
		<th>倉庫コード</th>
		<th>倉庫名</th>
		<th>棚番</th>
		<th>棚番名</th>
		<th>商品コード</th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td style="text-align: center;"><input type="radio"
				name="${dialogId}_selectedRack" value="${f:h(bean.rackCode)}"
				tabindex="10100"
				onclick="$('#${dialogId}_selectButton').attr('disabled', false);">
			</td>
			<td>${f:h(bean.warehouseCode)}</td>
			<td>${f:h(bean.warehouseName)}</td>
			<td>${f:h(bean.rackCode)}</td>
			<td>${f:h(bean.rackName)}</td>
			<td style="white-space: normal;">
				<c:forEach var="productCode" items="${bean.productCodeList}" varStatus="status">
				<c:if test="${status.index != 0}">,</c:if>${f:h(productCode)}
				</c:forEach>
			</td>
		</tr>
	</c:forEach>
</table>
</div>

<c:forEach var="bean" items="${searchResultList}" varStatus="status">

<input type="hidden" id="${dialogId}_${f:h(bean.rackCode)}_warehouseCode" name="${dialogId}_${f:h(bean.rackCode)}_warehouseCode" value="${f:h(bean.warehouseCode)}"><input type="hidden" id="${dialogId}_${f:h(bean.rackCode)}_warehouseName" name="${dialogId}_${f:h(bean.rackCode)}_warehouseName" value="${f:h(bean.warehouseName)}">
<input type="hidden" id="${dialogId}_${f:h(bean.rackCode)}_rackCode" name="${dialogId}_${f:h(bean.rackCode)}_rackCode" value="${f:h(bean.rackCode)}"><input type="hidden" id="${dialogId}_${f:h(bean.rackCode)}_rackName" name="${dialogId}_${f:h(bean.rackCode)}_rackName" value="${f:h(bean.rackName)}"><input type="hidden" id="${dialogId}_${f:h(bean.rackCode)}_rackCategory" name="${dialogId}_${f:h(bean.rackCode)}_rackCategory" value="${f:h(bean.rackCategory)}"><input type="hidden" id="${dialogId}_${f:h(bean.rackCode)}_rackCategoryName" name="${dialogId}_${f:h(bean.rackCode)}_rackCategoryName" value="${f:h(bean.rackCategoryName)}"><input type="hidden" id="${dialogId}_${f:h(bean.rackCode)}_zipCode" name="${dialogId}_${f:h(bean.rackCode)}_zipCode" value="${f:h(bean.zipCode)}"><input type="hidden" id="${dialogId}_${f:h(bean.rackCode)}_address1" name="${dialogId}_${f:h(bean.rackCode)}_address1" value="${f:h(bean.address1)}"><input type="hidden" id="${dialogId}_${f:h(bean.rackCode)}_address2" name="${dialogId}_${f:h(bean.rackCode)}_address2" value="${f:h(bean.address2)}"><input type="hidden" id="${dialogId}_${f:h(bean.rackCode)}_rackPcName" name="${dialogId}_${f:h(bean.rackCode)}_rackPcName" value="${f:h(bean.rackPcName)}"><input type="hidden" id="${dialogId}_${f:h(bean.rackCode)}_rackTel" name="${dialogId}_${f:h(bean.rackCode)}_rackTel" value="${f:h(bean.rackTel)}"><input type="hidden" id="${dialogId}_${f:h(bean.rackCode)}_rackFax" name="${dialogId}_${f:h(bean.rackCode)}_rackFax" value="${f:h(bean.rackFax)}"><input type="hidden" id="${dialogId}_${f:h(bean.rackCode)}_rackEmail" name="${dialogId}_${f:h(bean.rackCode)}_rackEmail" value="${f:h(bean.rackEmail)}">
<input type="hidden" id="${dialogId}_${f:h(bean.rackCode)}_multiFlag" name="${dialogId}_${f:h(bean.rackCode)}_multiFlag" value="${f:h(bean.multiFlag)}"><input type="hidden" id="${dialogId}_${f:h(bean.rackCode)}_updDatetm" name="${dialogId}_${f:h(bean.rackCode)}_updDatetm" value="${f:h(bean.updDatetm)}">
</c:forEach>