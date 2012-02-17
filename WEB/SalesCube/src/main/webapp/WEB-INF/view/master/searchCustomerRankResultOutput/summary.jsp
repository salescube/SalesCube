		<table border="1" id="search_result" summary="検索結果" style="width: 910px">
			<colgroup>
				<col span="1" style="width: 100px">
				<col span="1" style="width: 100px">
				<col span="1" style="width: 100px">
				<col span="1" style="width: 100px">
				<col span="1" style="width: 100px">
				<col span="1" style="width: 100px">
				<col span="1" style="width: 100px">
				<col span="1" style="width: 100px">
				<col span="1" style="width: 100px">
				<col span="1" style="width: 100px">
				<col span="1" style="width: 110px">
				<col span="1" style="width: 100px">
				<col span="1" style="width: 100px">
				<col span="1" style="width: 100px">
				<col span="1" style="width: 100px">
			</colgroup>
            <tr>
				<th class=xl64 rowspan="2">顧客No.</th>
				<th class=xl64 rowspan="2">会社名</th>
				<th class=xl64 rowspan="2">登録者名</th>
                <th class=xl64 rowspan="2">ランク</th>
                <th class=xl64 colspan="7">売上金額</th>
                <th class=xl64 rowspan="2">初回売上日</th>
                <th class=xl64 rowspan="2">最終売上日</th>
                <th class=xl64 rowspan="2">在籍期間日数</th>
                <th class=xl64 rowspan="2">離脱期間日数</th>
            </tr>
			<tr>
                <th class=xl64>${labelSales6}</th>
                <th class=xl64>${labelSales5}</th>
                <th class=xl64>${labelSales4}</th>
                <th class=xl64>${labelSales3}</th>
                <th class=xl64>${labelSales2}</th>
                <th class=xl64>${labelSales1}</th>
                <th class=xl64>月平均売上金額</th>
			</tr>
	<c:forEach var="bean" items="${summaryList}" varStatus="status">
			<tr>
				<td class="xl65">${bean.customerCode}</td>
				<td class="xl65">${f:h(bean.customerName)}</td>
				<td class="xl65">${f:h(bean.nameKnj)}</td>
				<td class="xl65">${f:h(bean.rankName)}</td>
				<td class="xl65">${f:h(bean.salesPrice6)}</td>
				<td class="xl65">${f:h(bean.salesPrice5)}</td>
				<td class="xl65">${f:h(bean.salesPrice4)}</td>
				<td class="xl65">${f:h(bean.salesPrice3)}</td>
				<td class="xl65">${f:h(bean.salesPrice2)}</td>
				<td class="xl65">${f:h(bean.salesPrice1)}</td>
				<td class="xl65">${f:h(bean.salesPriceLsm)}</td>
				<td class="xl68" style="text-align: center;">${f:h(bean.firstSalesDate)}</td>
				<td class="xl68" style="text-align: center;">${f:h(bean.lastSalesDate)}</td>
				<td class="xl65">${f:h(bean.enrollTermSpan)}</td>
				<td class="xl65">${f:h(bean.defectTermSpan)}</td>
			</tr>
	</c:forEach>
</table>
