<!-- 配送業者入金結果 -->
<table id="slip_section" summary="slipSection" class="forms" style="table-layout: fixed;">
	<tr>
		<c:forEach var="colInfo" items="${columnInfoList}" varStatus="status">
			<c:choose>
				<c:when test="${status.index < 5}">
					<c:set var="cellColor" value="xlbg1" />
				</c:when>
				<c:when test="${status.index < 20}">
					<c:set var="cellColor" value="xlbg2" />
				</c:when>
				<c:otherwise>
					<c:set var="cellColor" value="xlbg3" />
				</c:otherwise>
			</c:choose>
			<th class="${cellColor}"><bean:message key='${colInfo.propKey}'/></th>
		</c:forEach>
	</tr>
	<c:forEach var="rowData" items="${importResultList}" varStatus="statusRow">
		<tr>
			<c:forEach var="colInfo" items="${columnInfoList}" varStatus="statusCol">
				<c:choose>
				<c:when test="${rowData[colInfo.name] == null}">
					<td class=xl70 style="text-align: left">
						&nbsp
					</td>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${colInfo.format == 1}">
							<td class="xl65" style="text-align: right">
								<fmt:formatNumber value="${rowData[colInfo.name]}" pattern="0" />
							</td>
						</c:when>
						<c:when test="${colInfo.format == 2}">
							<td class="xl66" style="text-align: right">
								<fmt:formatNumber value="${rowData[colInfo.name]}" pattern="0.000" />
							</td>
						</c:when>
						<c:when test="${colInfo.format == 10}">
							<td class="xl68" style="text-align: center">
									<fmt:formatDate value="${rowData[colInfo.name]}" pattern="yyyy/MM/dd" />
							</td>
						</c:when>
						<c:when test="${colInfo.format == 11}">
							<td class="xl69" style="text-align: center">
								<fmt:formatDate value="${rowData[colInfo.name]}" pattern="yyyy/MM/dd HH:mm:ss.S" />
							</td>
						</c:when>
						<c:otherwise>
							<td class="xl70" style="text-align: left">
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
