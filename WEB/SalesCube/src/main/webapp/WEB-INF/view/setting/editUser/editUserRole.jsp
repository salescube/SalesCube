<th><div class="col_title_right">${bean.caption}</div></th>
<td>
	<%-- ファイル参照以外 --%>
	<c:if test="${bean.menuId != '1407'}">
	<input type="radio"
		id="menuDtoList[${status.index}].validFlag1"
		name="menuDtoList[${status.index}].validFlag" value="0"
		tabindex="${status.index + 200}" <c:if test="${bean.validFlag=='0'}">checked</c:if> >
		<label for="menuDtoList[${status.index}].validFlag1">無効</label>
	<input type="radio"
		id="menuDtoList[${status.index}].validFlag2"
		name="menuDtoList[${status.index}].validFlag" value="2"
		tabindex="${status.index + 200}" <c:if test="${bean.validFlag=='2'}">checked</c:if> >
		<label for="menuDtoList[${status.index}].validFlag2">有効</label>

	<c:if test="${bean.validType=='1'}">
	<%-- メニューによっては参照が存在しない --%>
	<input type="radio"
		id="menuDtoList[${status.index}].validFlag3"
		name="menuDtoList[${status.index}].validFlag" value="1"
		tabindex="${status.index + 200}" <c:if test="${bean.validFlag=='1'}">checked</c:if> >
		<label for="menuDtoList[${status.index}].validFlag3">参照</label>
	</c:if>
	</c:if>

	<%-- ファイル参照 --%>
	<c:if test="${bean.menuId == '1407'}">
	<input type="radio"
		id="menuDtoList[${status.index}].validFlag"
		name="menuDtoList[${status.index}].validFlag" value="0"
		tabindex="${status.index + 200}" <c:if test="${bean.validFlag=='0'}">checked</c:if> >
		<label for="menuDtoList[${status.index}].validFlag1">不可</label>
	<input type="radio"
		id="menuDtoList[${status.index}].validFlag2"
		name="menuDtoList[${status.index}].validFlag" value="2"
		tabindex="${status.index + 200}" <c:if test="${bean.validFlag=='2'}">checked</c:if> >
		<label for="menuDtoList[${status.index}].validFlag2">全て可</label>
	<input type="radio"
		id="menuDtoList[${status.index}].validFlag3"
		name="menuDtoList[${status.index}].validFlag" value="1"
		tabindex="${status.index + 200}" <c:if test="${bean.validFlag=='1'}">checked</c:if> >
		<label for="menuDtoList[${status.index}].validFlag3">全社員向けのみ可</label>
	</c:if>

	<input type="hidden" name="menuDtoList[${status.index}].menuId" value="${bean.menuId}">
	<input type="hidden" name="menuDtoList[${status.index}].caption" value="${bean.caption}">
	<input type="hidden" name="menuDtoList[${status.index}].validType" value="${bean.validType}">

	<input type="hidden" name="originalMenuDtoList[${status.index}].caption" value="${bean.caption}">
	<input type="hidden" name="originalMenuDtoList[${status.index}].menuId" value="${bean.menuId}">
	<input type="hidden" name="originalMenuDtoList[${status.index}].validFlag" value="${bean.validFlag}">
	<input type="hidden" name="originalMenuDtoList[${status.index}].validType" value="${bean.validType}">
</td>
