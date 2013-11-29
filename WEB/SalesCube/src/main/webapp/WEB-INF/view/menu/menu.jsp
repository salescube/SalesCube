<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　メニュー</title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
	$(function() {
		$(".sitemap").find("a:first").focus();
	});
	-->
	</script>

</head>
<body>
	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value=""/>
		<jsp:param name="MENU_ID" value="0101"/>
	</jsp:include>

	<%-- メイン機能 --%>
	<div id="main_function">

		<%-- おしらせ --%>
		<table class="forms" style="height: 70px">
			<colgroup>
				<col span="1" style="width: 15%">
				<col span="1" style="width: 85%">
			</colgroup>
			<tr>
				<th><div class="col_title_right" style="height: 90px;">
					おしらせ<br>&nbsp;
					<c:if test="${empty newsDto.updDatetm}">
						${newsDto.creDatetm}
					</c:if>
					<c:if test="${!empty newsDto.updDatetm}">
						${newsDto.updDatetm}
					</c:if>&nbsp;
					</div>
				</th>
				<td>
					<textarea class="news" style="height: 90px; width: 1065px; overflow-y:scroll;" readonly>${f:h(newsDto.description)}</textarea>
				</td>
			</tr>
		</table>

		<%-- サイトマップ --%>
		<table class="sitemap" style="width: 1160px; margin-left: 10px;">
			<colgroup>
				<col span="1" width="25%">
				<col span="1" width="25%">
				<col span="1" width="25%">
				<col span="1" width="25%">
			</colgroup>
			<tr>
				<td>
				<c:forEach var="menuDto" items="${userDto.menuDtoList}" varStatus="status">
					<bean:define id="ImagePath" value="/images/menu/menu_${menuDto.menuId}.png"/>

					<%-- 表示の調整　業務メニューが縦に4つ並ぶか、または「マスタ管理」の場合は次の列へ --%>
					<c:if test="${status.index != 0}">
						<c:if test="${status.index % 4 == 0 || menuDto.menuId == '0013'}">
							</td>
							<td>
						</c:if>
					</c:if>
					
					<div class="menuGroup" style="cursor: default;">
						<table>
							<tr>
								<td>
									<img src="${f:url(ImagePath)}" width="100" height="80" style="cursor: default;"/>
								</td>
								<td>
									<ul style="padding-left:10px;">
										<c:forEach var="subMenuDto" items="${menuDto.subMenuDtoList}">
											<li><a href="javascript:window.location.doHref('${f:url(subMenuDto.url)}');">${f:h(subMenuDto.caption)}</a></li>
										</c:forEach>
									</ul>
								</td>
							</tr>
						</table>
					</div>
				</c:forEach>

				<c:if test="${empty userDto.menuDtoList}">
					<p>利用可能なメニューがありません</p>
				</c:if>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
