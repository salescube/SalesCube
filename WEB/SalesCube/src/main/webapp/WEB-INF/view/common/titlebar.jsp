<div id="header">
		
		<c:if test="${!empty userDto.userId}">
		<s:link href="javascript:window.location.doHref('${f:url('/menu')}');">
			<c:if test="${!empty mineDto.logoImgPath}">
			<img src="${f:url(mineDto.logoImgPath)}" alt="<bean:message key='titles.system'/>のメニュー画面に遷移します。" width="200" height="30">
			</c:if>
			<c:if test="${empty mineDto.logoImgPath}">
			<img src="${f:url('/images/logo.png')}" alt="<bean:message key='titles.system'/>のメニュー画面に遷移します。" width="200" height="30">
			</c:if>
		</s:link>
		</c:if>

		<c:if test="${empty userDto.userId}">
		<c:if test="${!empty mineDto.logoImgPath}">
		<img src="${f:url(mineDto.logoImgPath)}" alt="<bean:message key='titles.system'/>のメニュー画面に遷移します。" width="200" height="30">
		</c:if>
		<c:if test="${empty mineDto.logoImgPath}">
		<img src="${f:url('/images/logo.png')}" alt="<bean:message key='titles.system'/>のメニュー画面に遷移します。" width="200" height="30">
		</c:if>
		</c:if>

		<c:if test="${!empty userDto.userId}">
		
		<div class="login_user">[ログインユーザー] ${f:h(userDto.nameKnj)}</div>

		
		<div class="header_buttons">
			<s:form action="/logout">
				
				<c:if test="${!empty userDto.fileOpenLevel}">
				<button type="button" style="width: 100px" tabindex="3000"
					onclick="openReferFilesDialog('REFERENCE_FILES');">ファイル参照</button>
				<s:submit style="width: 80px;" value="ログアウト" tabindex="3001" property="logout"/>
				</c:if>
				<c:if test="${empty userDto.fileOpenLevel}">
				<s:submit style="margin-left: 100px; width: 80px;"
					value="ログアウト" tabindex="3001" property="logout"/>
				</c:if>
			</s:form>
		</div>
		</c:if>
	</div>