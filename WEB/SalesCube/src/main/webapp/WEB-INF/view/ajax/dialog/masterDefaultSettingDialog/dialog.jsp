<div id="${dialogId}" title="マスタ初期値設定" style="display: none;">
	<span id="${dialogId}_errors" style="color: red">
	</span>

	<form>
		<div style="padding: 20px 20px 0 20px;">
		<table class="forms" style="width: 450px;">
			<c:forEach var="dto" items="${initMstDtoList}" varStatus="status">
			<tr>
				<th>${dto.title}</th>
				<td>
					<input type="hidden"
						id="${dialogId}_initMstDtoList[${status.index}].tableName"
						name="${dialogId}_initMstDtoList[${status.index}].tableName"
						value="${tableName}" />
					<input type="hidden"
						id="${dialogId}_initMstDtoList[${status.index}].columnName"
						name="${dialogId}_initMstDtoList[${status.index}].columnName"
						value="${dto.columnName}" />
					<input type="hidden"
						id="${dialogId}_initMstDtoList[${status.index}].categoryId"
						name="${dialogId}_initMstDtoList[${status.index}].categoryId"
						value="${dto.categoryId}" />
					<input type="hidden"
						id="${dialogId}_initMstDtoList[${status.index}].useDataType"
						name="${dialogId}_initMstDtoList[${status.index}].useDataType"
						value="${dto.useDataType}" />
					<input type="hidden"
						id="${dialogId}_initMstDtoList[${status.index}].useStrSize"
						name="${dialogId}_initMstDtoList[${status.index}].useStrSize"
						value="${dto.useStrSize}" />
					<input type="hidden"
						id="${dialogId}_initMstDtoList[${status.index}].title"
						name="${dialogId}_initMstDtoList[${status.index}].title"
						value="${dto.title}" />

					<c:if test="${dto.useDataType == '1' && !empty dto.masterList}">
						<%-- 区分 --%>
						<select id="${dialogId}_initMstDtoList[${status.index}].strData"
							name="${dialogId}_initMstDtoList[${status.index}].strData">
							<c:forEach var="bean" items="${dto.masterList}" varStatus="status2">
								<c:if test="${dto.strData == bean.value}">
								<option value="${bean.value}" selected>${bean.label}</option>
								</c:if>
								<c:if test="${dto.strData != bean.value}">
								<option value="${bean.value}">${bean.label}</option>
								</c:if>
							</c:forEach>
						</select>
					</c:if>

					<c:if test="${dto.useDataType == '1' && empty dto.masterList}">
						<%-- 文字列 --%>
						<input type="text" id="${dialogId}_initMstDtoList[${status.index}].strData"
							name="${dialogId}_initMstDtoList[${status.index}].strData"
							value="${dto.strData}" tabindex="${status.index + 15000}">
					</c:if>

					<c:if test="${dto.useDataType == '2'}">
						<%-- 整数 --%>
						<input type="text" id="${dialogId}_initMstDtoList[${status.index}].numData"
							name="${dialogId}_initMstDtoList[${status.index}].numData"
							value="${dto.numData}" tabindex="${status.index + 15000}"
							class="numeral_commas"
							style="width: 80px;">
					</c:if>

					<c:if test="${dto.useDataType == '3'}">
						<%-- 小数 --%>
						<input type="text" id="${dialogId}_initMstDtoList[${status.index}].fltData"
							name="${dialogId}_initMstDtoList[${status.index}].fltData"
							value="${dto.fltData}" tabindex="${status.index + 15000}"
							class="numeral_commas"
							style="width: 80px;">
					</c:if>

					<input type="hidden"
						id="${dialogId}_initMstDtoList[${status.index}].updDatetm"
						name="${dialogId}_initMstDtoList[${status.index}].updDatetm"
						value="${dto.updDatetm}" />
					<input type="hidden"
						id="${dialogId}_initMstDtoList[${status.index}].updUser"
						name="${dialogId}_initMstDtoList[${status.index}].updUser"
						value="${dto.updUser}" />
				</td>
			</tr>
			</c:forEach>
		</table>
		</div>

		<div style="width: 450px; text-align: right">
			<button id="${dialogId}_initButton" type="reset" style="width: 70px"
				tabindex="15050">初期化</button>
			<button id="${dialogId}_selectButton" type="button" style="width: 70px" tabindex="15051"
				onclick="_updateMasterDefaultSetting('${dialogId}', '${tableName}');">更新</button>
			<button type="button" style="width: 70px" tabindex="15052"
				onclick="$('#${dialogId}').dialog('close');">閉じる</button>
		</div>
	</form>
</div>