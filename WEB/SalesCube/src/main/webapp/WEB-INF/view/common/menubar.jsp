
<div id="menu">
	<table border="0" width="100%" cellspacing="0">
		<tr class="menu1">
			<td>
			<table cellpadding="0" cellspacing="0">
				<tr class="menu1">
					<td style="display: none;">&nbsp;</td>
					<td style="display: none;" class="menu1" id="dummy_L"></td>
					<td style="display: none;" class="menu2" id="dummy_M"></td>
					<td style="display: none;" class="menu1" id="dummy_R"></td>
					<td>&nbsp;</td>

				<c:forEach var="menuDto" items="${userDto.menuDtoList}">
					<bean:define id="leftImagePath" value="/images/menu/${menuDto.bgColor}_L.png"/>
					<bean:define id="middleImagePath" value="/images/menu/${menuDto.bgColor}_M.png"/>
					<bean:define id="rightImagePath" value="/images/menu/${menuDto.bgColor}_R.png"/>
					<c:if test="${menuDto.menuId == param.PARENT_MENU_ID}"><bean:define id="menu1Color" value="#${menuDto.bgColor}"/></c:if>

					<td class="menu1" id="${menuDto.menuId}_L"
						style="<c:if test="${menuDto.menuId == param.PARENT_MENU_ID}">background-image: url(${f:url(leftImagePath)})</c:if>"></td>
					<td class="menu2" id="${menuDto.menuId}_M"
						style="<c:if test="${menuDto.menuId == param.PARENT_MENU_ID}"><c:if test="${!empty menuDto.fontColor}">color: #${menuDto.fontColor};</c:if> background-image: url(${f:url(middleImagePath)})</c:if>"
						onclick="changeMenu1('${menuDto.menuId}', '${menuDto.bgColor}', '${menuDto.fontColor}');">${menuDto.caption}</td>
					<td class="menu1" id="${menuDto.menuId}_R"
						style="<c:if test="${menuDto.menuId == param.PARENT_MENU_ID}">background-image: url(${f:url(rightImagePath)})</c:if>"></td>
					<td>&nbsp;</td>
				</c:forEach>

				</tr>
			</table>
			</td>
		</tr>
		<tr id="menubar1" style="height: 5px; <c:if test="${!empty menu1Color}">background-color: ${menu1Color};</c:if>">
			<td></td>
		</tr>
		<tr class="menu2">
			<td>
			<table cellpadding="0" cellspacing="0">

			<c:forEach var="menuDto" items="${userDto.menuDtoList}">
				<tr class="menu2" id="${menuDto.menuId}_row"
					style="<c:if test="${menuDto.menuId != param.PARENT_MENU_ID}">display: none;</c:if>">
					<td>&nbsp;</td>

				<c:forEach var="subMenuDto" items="${menuDto.subMenuDtoList}">
					<bean:define id="leftImagePath" value="/images/menu/${subMenuDto.bgColor}_L.png"/>
					<bean:define id="middleImagePath" value="/images/menu/${subMenuDto.bgColor}_M.png"/>
					<bean:define id="rightImagePath" value="/images/menu/${subMenuDto.bgColor}_R.png"/>
					<c:if test="${subMenuDto.menuId == param.MENU_ID}"><bean:define id="menu2Color" value="#${subMenuDto.bgColor}"/></c:if>

					<td class="menu1" id="${subMenuDto.menuId}_L"
						style="<c:if test="${subMenuDto.menuId == param.MENU_ID}">background-image: url(${f:url(leftImagePath)})</c:if>"
						onclick="window.location.doHref('${f:url(subMenuDto.url)}');"></td>
					<td class="menu2" id="${subMenuDto.menuId}_M"
						style="<c:if test="${subMenuDto.menuId == param.MENU_ID}"><c:if test="${!empty subMenuDto.fontColor}">color: #${subMenuDto.fontColor};</c:if> background-image: url(${f:url(middleImagePath)})</c:if>"
						onclick="window.location.doHref('${f:url(subMenuDto.url)}');">${subMenuDto.caption}</td>
					<td class="menu1" id="${subMenuDto.menuId}_R"
						style="<c:if test="${subMenuDto.menuId == param.MENU_ID}">background-image: url(${f:url(rightImagePath)})</c:if>"
						onclick="window.location.doHref('${f:url(subMenuDto.url)}');"></td>
					<td>&nbsp;
						<input type="hidden" id="menujs_${subMenuDto.menuId}_bgColor" name="menujs_${subMenuDto.menuId}_bgColor" value="#${subMenuDto.bgColor}">
						<input type="hidden" id="menujs_${subMenuDto.menuId}_fontColor" name="menujs_${subMenuDto.menuId}_fontColor" value="#${subMenuDto.fontColor}">
					</td>
				</c:forEach>
				</tr>
			</c:forEach>

			</table>
			</td>
		</tr>
		<tr id="menubar2" style="height: 5px; <c:if test="${!empty menu2Color}">background-color: ${menu2Color};</c:if>">
			<td></td>
		</tr>
	</table>

	<input type="hidden" id="menujs_PARENT_MENU_ID" name="menujs_PARENT_MENU_ID" value="${param.PARENT_MENU_ID}">
	<input type="hidden" id="menujs_MENU_ID" name="menujs_MENU_ID" value="${param.MENU_ID}">
</div>
