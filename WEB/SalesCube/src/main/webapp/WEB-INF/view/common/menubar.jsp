<div id="menu">
	<table border="0" width="100%" cellspacing="0">
		<tr class="menu1">
			<td>
				<table cellpadding="0" cellspacing="0">
					<tr class="menu1">
					<c:forEach var="menuDto" items="${userDto.menuDtoList}">
						<bean:define id="ImagePath" value="/images/menu/btn_gnavi_${menuDto.menuId}.png"/>
						<bean:define id="onImagePath" value="/images/menu/btn_gnavi_${menuDto.menuId}_on.png"/>
						<c:if test="${menuDto.menuId == param.PARENT_MENU_ID}">
							<bean:define id="menu1Color" value="#${menuDto.bgColor}"/>
							<td class="menu2" id="${menuDto.menuId}_M"
								onclick="changeMenu1('${menuDto.menuId}', '${menuDto.bgColor}', '${menuDto.fontColor}');">
								<!-- <img id="img_gnavi_${menuDto.menuId}" alt="${menuDto.caption}" src="${f:url(onImagePath)}" width="100" height="80" class="active"> -->
								<img id="img_gnavi_${menuDto.menuId}" alt="${menuDto.caption}" src="${f:url(onImagePath)}" width="100" height="80" class="btn">
							</td>
						</c:if>
						<c:if test="${menuDto.menuId != param.PARENT_MENU_ID}">
							<td class="menu2" id="${menuDto.menuId}_M"
								onclick="changeMenu1('${menuDto.menuId}', '${menuDto.bgColor}', '${menuDto.fontColor}');">
								<img id="img_gnavi_${menuDto.menuId}" alt="${menuDto.caption}" src="${f:url(ImagePath)}" width="100" height="80" class="btn">
							</td>
						</c:if>
					</c:forEach>
					</tr>
				</table>
			</td>
		</tr>
		<tr class="menu2">
			<td class="submenubar" style="<c:if test="${!empty menu1Color}">background-color: ${menu1Color};</c:if>" >
			<table cellpadding="0" cellspacing="0">

			<c:forEach var="menuDto" items="${userDto.menuDtoList}">
				<tr class="menu2" id="${menuDto.menuId}_row"
					style="<c:if test="${menuDto.menuId != param.PARENT_MENU_ID}">display: none;</c:if>">
					<td class="submenubar" style=" width:20px; <c:if test="${!empty menu1Color}">background-color: ${menu1Color};</c:if>">&nbsp;</td>
				<c:forEach var="subMenuDto" items="${menuDto.subMenuDtoList}">
					<c:if test="${subMenuDto.menuId == param.MENU_ID}"><bean:define id="menu2Color" value="#${subMenuDto.bgColor}"/></c:if>

					<td class="menu2 submenubar" id="${subMenuDto.menuId}_M"
						style="<c:if test="${!empty menu1Color}">background-color: ${menu1Color};</c:if>"
<%-- 						style="<c:if test="${subMenuDto.menuId == param.MENU_ID}"> --%>
<%-- 							<c:if test="${!empty subMenuDto.fontColor}"> color: #${subMenuDto.fontColor}; </c:if> --%>
<%-- 						 	<c:if test="${!empty menu2Color}"> background-color: #${menu2Color}; </c:if> --%>
<%-- 						 </c:if>" --%>
						>
<%-- 						onclick="window.location.doHref('${f:url(subMenuDto.url)}');"> --%>
						<button id="${subMenuDto.menuId}_button"
						style="<c:if test="${subMenuDto.menuId == param.MENU_ID}">background-color: #${subMenuDto.bgColor};</c:if>"
						 onclick="window.location.doHref('${f:url(subMenuDto.url)}');">${subMenuDto.caption}</button></td>

					<td class="submenubar" style="width:15px;  <c:if test="${!empty menu1Color}">background-color: ${menu1Color};</c:if>">&nbsp;
						<input type="hidden" id="menujs_${subMenuDto.menuId}_bgColor" name="menujs_${subMenuDto.menuId}_bgColor" value="#${subMenuDto.bgColor}">
						<input type="hidden" id="menujs_${subMenuDto.menuId}_fontColor" name="menujs_${subMenuDto.menuId}_fontColor" value="#${subMenuDto.fontColor}">
					</td>
				</c:forEach>
				</tr>
			</c:forEach>

			</table>
			</td>
		</tr>
	</table>

	<input type="hidden" id="menujs_PARENT_MENU_ID" name="menujs_PARENT_MENU_ID" value="${param.PARENT_MENU_ID}">
	<input type="hidden" id="menujs_MENU_ID" name="menujs_MENU_ID" value="${param.MENU_ID}">
</div>
