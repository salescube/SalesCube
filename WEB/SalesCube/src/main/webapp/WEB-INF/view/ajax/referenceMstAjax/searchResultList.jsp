<!-- 伝票部 -->
<table id="slip_section" summary="slipSection" class="forms" style="table-layout: fixed;">
	<tr>
		<c:forEach var="colInfo" items="${masterColList}" varStatus="status">
			<th class=xl64><bean:message key='${colInfo.propKey}'/></th>
		</c:forEach>
	</tr>
	<c:forEach var="rowData" items="${masterList}" varStatus="statusRow">
		<tr>
			<c:forEach var="colInfo" items="${masterColList}" varStatus="statusCol">
				<c:choose>
				<c:when test="${rowData[colInfo.name] == null}">
					<td class=xl70 style="text-align: left">
						&nbsp
					</td>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${colInfo.format == 1}">
							<td class=xl65 style="text-align: right">
								<fmt:formatNumber value="${rowData[colInfo.name]}" pattern="0" />
							</td>
						</c:when>
						<c:when test="${colInfo.format == 2}">
							<td class=xl66 style="text-align: right">
								<fmt:formatNumber value="${rowData[colInfo.name]}" pattern="0.000" />
							</td>
						</c:when>
						<c:when test="${colInfo.format == 10}">
							<td class=xl68 style="text-align: center">
									<fmt:formatDate value="${rowData[colInfo.name]}" pattern="yyyy/MM/dd" />
							</td>
						</c:when>
						<c:when test="${colInfo.format == 11}">
							<td class=xl69 style="text-align: center">
								<fmt:formatDate value="${rowData[colInfo.name]}" pattern="yyyy/MM/dd HH:mm:ss.S" />
							</td>
						</c:when>
						<c:otherwise>
							<td class=xl70 style="text-align: left">
								${f:h(rowData[colInfo.name])}
							</td>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
				</c:choose>
			</c:forEach>
		</tr>
	</c:forEach>
</table>
<!-- 明細部 -->
<c:if test="${detailList != null}">
<BR>
<table id="detail_section" summary="detailSection" class="forms" style="table-layout: fixed;">
	<tr>
		<c:forEach var="colInfo" items="${detailColList}" varStatus="status">
			<th class=xl64><bean:message key='${colInfo.propKey}'/></th>
		</c:forEach>
	</tr>
	<c:forEach var="rowData" items="${detailList}" varStatus="statusRow">
		<tr>
			<c:forEach var="colInfo" items="${detailColList}" varStatus="statusCol">
				<c:choose>
				<c:when test="${rowData[colInfo.name] == null}">
					<td class=xl70 style="text-align: left">
						&nbsp
					</td>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${colInfo.format == 1}">
							<td class=xl65 style="text-align: right">
								<fmt:formatNumber value="${rowData[colInfo.name]}" pattern="0" />
							</td>
						</c:when>
						<c:when test="${colInfo.format == 2}">
							<td class=xl66 style="text-align: right">
								<fmt:formatNumber value="${rowData[colInfo.name]}" pattern="0.000" />
							</td>
						</c:when>
						<c:when test="${colInfo.format == 10}">
							<td class=xl68 style="text-align: center">
									<fmt:formatDate value="${rowData[colInfo.name]}" pattern="yyyy/MM/dd" />
							</td>
						</c:when>
						<c:when test="${colInfo.format == 11}">
							<td class=xl69 style="text-align: center">
								<fmt:formatDate value="${rowData[colInfo.name]}" pattern="yyyy/MM/dd HH:mm:ss.S" />
							</td>
						</c:when>
						<c:otherwise>
							<td class=xl70 style="text-align: left">
								${f:h(rowData[colInfo.name])}
							</td>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
				</c:choose>
			</c:forEach>
		</tr>
	</c:forEach>
</table>
</c:if>
