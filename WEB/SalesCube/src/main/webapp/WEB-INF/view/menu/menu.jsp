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
	
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value=""/>
		<jsp:param name="MENU_ID" value="0101"/>
	</jsp:include>

	
	<div id="main_function">

		
		<table class="forms" style="height: 70px">
			<colgroup>
				<col span="1" style="width: 15%">
				<col span="1" style="width: 85%">
			</colgroup>
			<tr>
				<th>おしらせ<br>
					<c:if test="${empty newsDto.updDatetm}">
						${newsDto.creDatetm}
					</c:if>
					<c:if test="${!empty newsDto.updDatetm}">
						${newsDto.updDatetm}
					</c:if>
				</th>
				<td>
					<textarea class="news" style="height: 70px; width: 790px;" readonly>${f:h(newsDto.description)}</textarea>
				</td>
			</tr>
		</table>

		
		<div class="titleLeftTop" style="width: 910px;">
		<div class="titleRightTop">
		<div class="titleRightBottom">
		<div class="titleLeftBottom">
			<br>
			<table class="sitemap" style="width: 850px;">
				<colgroup>
					<col spna="1" width="25%">
					<col spna="1" width="25%">
					<col spna="1" width="25%">
					<col spna="1" width="25%">
				</colgroup>
				<tr>
					<td>
					<c:forEach var="menuDto" items="${userDto.menuDtoList}" varStatus="status">

					
					<c:if test="${status.index != 0}">
					<c:if test="${status.index % 4 == 0 || menuDto.menuId == '0013'}">
					</td>
					<td>
					</c:if>
					</c:if>

						<span>${f:h(menuDto.caption)}</span>
						<ul>
							<c:forEach var="subMenuDto" items="${menuDto.subMenuDtoList}">
							<li><a href="javascript:window.location.doHref('${f:url(subMenuDto.url)}');">${f:h(subMenuDto.caption)}</a></li>
							</c:forEach>
						</ul>
					</c:forEach>

					<c:if test="${empty userDto.menuDtoList}">
						<p>利用可能なメニューがありません</p>
					</c:if>

					</td>
				</tr>
			</table>
		</div>
		</div>
		</div>
		</div>
	</div>

</body>
</html>
