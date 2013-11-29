<div id="header">
		<%-- タイトル画像 --%>
		<div class="logo">
		<c:if test="${!empty userDto.userId}">
		<s:link href="javascript:window.location.doHref('${f:url('/menu')}');">
			<c:if test="${!empty mineDto.logoImgPath}">
			<img src="${f:url(mineDto.logoImgPath)}" alt="<bean:message key='titles.system'/>のメニュー画面に遷移します。">
			</c:if>
			<c:if test="${empty mineDto.logoImgPath}">
			<img src="${f:url('/images/logo.png')}" alt="<bean:message key='titles.system'/>のメニュー画面に遷移します。">
			</c:if>
		</s:link>
		</c:if>


		<c:if test="${empty userDto.userId}">
		<c:if test="${!empty mineDto.logoImgPath}">
		<img src="${f:url(mineDto.logoImgPath)}" alt="<bean:message key='titles.system'/>のメニュー画面に遷移します。">
		</c:if>
		<c:if test="${empty mineDto.logoImgPath}">
		<img src="${f:url('/images/logo.png')}" alt="<bean:message key='titles.system'/>のメニュー画面に遷移します。">
		</c:if>
		</c:if>
		</div>

		<c:if test="${!empty userDto.userId}">
		<%-- ログインユーザー情報 --%>
		<div class="login_user">
			<img src="${f:url('/images/customize/login.png')}" width="15" height="16" style="margin-right:10px;">${f:h(userDto.nameKnj)}
		</div>

		<%-- ファイル参照ボタン、ログアウトボタン --%>
		<div class="header_buttons">
			<s:form action="/logout">
				<%-- ファイル参照権があればボタンを表示 --%>
				<c:if test="${!empty userDto.fileOpenLevel}">
				<button type="button" style="width: 100px" tabindex="3000"	onclick="openReferFilesDialog('REFERENCE_FILES');">
				<img border="0" src="${f:url('/images/customize/btn_file.png')}" width="100" height="33">
				</button>

				<button type="submit"  tabindex="3001" name="logout">
					<img border="0" src="${f:url('/images/customize/btn_logout.png')}" width="100" height="33">
				</button>

				</c:if>
				<c:if test="${empty userDto.fileOpenLevel}">
				<button type="submit"  tabindex="3001" name="logout">
					<img border="0" src="${f:url('/images/customize/btn_logout.png')}" width="100" height="33">
				</button>
				</c:if>
			</s:form>
		</div>
		</c:if>
	</div>