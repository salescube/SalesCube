<table border="1" id="search_result" summary="検索結果" style="width: 910px">
	<colgroup>
		<col span="1" style="width: 100px">
		<col span="1" style="width: 100px">
		<col span="1" style="width: 100px">
	</colgroup>
	<tr>
		<th class=xl64><bean:message key='labels.master.warehouseCode'/></th>
		<th class=xl64><bean:message key='labels.master.warehouseName'/></th>
		<th class=xl64><bean:message key='labels.master.rackCode'/></th>
		<th class=xl64><bean:message key='labels.master.rackName'/></th>
		<th class=xl64><bean:message key='labels.product.csv.productCode'/></th>
	</tr>
	<c:forEach var="bean" items="${searchResultList}" varStatus="status">
		<tr>
			<td class="xl70" style="text-align: left;">${f:h(bean.warehouseCode)}</td>
			<td class="xl70" style="text-align: left;">${f:h(bean.warehouseName)}</td>
			<td class="xl70" style="text-align: left;">${f:h(bean.rackCode)}</td>
			<td class="xl70" style="text-align: left;">${f:h(bean.rackName)}</td>
			<td class="xl70" style="text-align: left;">
				<c:forEach var="productCode" items="${bean.productCodeList}" varStatus="status">
					<c:if test="${status.index != 0}">,</c:if>${f:h(productCode)}
				</c:forEach>
			</td>
		</tr>
	</c:forEach>
</table>
