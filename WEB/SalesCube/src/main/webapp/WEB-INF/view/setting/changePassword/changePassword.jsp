<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　パスワード変更</title>
<%@ include file="/WEB-INF/view/common/header.jsp" %>
<script type="text/javascript">
<!--
//初期化
function onF1(){
	if(confirm('<bean:message key="confirm.init" />')){
		document.setting_changePasswordActionForm.action = '${f:url("/setting/changePassword/reset")}';
		document.setting_changePasswordActionForm.submit();
	}
}
//更新
function onF3(){
	if(confirm('<bean:message key="confirm.insert" />')){
		document.setting_changePasswordActionForm.action = '${f:url("/setting/changePassword/update")}';
		document.setting_changePasswordActionForm.submit();
	}
}
-->
</script>
</head>
<body>
	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0012"/>
		<jsp:param name="MENU_ID" value="1206"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">
		<span class="title">パスワード変更</span>

		<div class="function_buttons">
			<button type="button" tabindex="2000" onclick="onF1()">F1<br>初期化</button>
			<button type="button" tabindex="2001" disabled="disabled">F2<br>&nbsp;</button>
			<c:if test="${isUpdate == true}">
				<button type="button" tabindex="2002" onclick="onF3()">F3<br>変更</button>
			</c:if>
			<c:if test="${isUpdate == false}">
				<button type="button" tabindex="2002" disabled>F3<br>変更</button>
			</c:if>
				<button type="button" tabindex="2003" disabled="disabled">F4<br>&nbsp;</button>
				<button type="button" tabindex="2004" disabled="disabled">F5<br>&nbsp;</button>
				<button type="button" tabindex="2005" disabled="disabled">F6<br>&nbsp;</button>
				<button type="button" tabindex="2006" disabled="disabled">F7<br>&nbsp;</button>
				<button type="button" tabindex="2007" disabled="disabled">F8<br>&nbsp;</button>
				<button type="button" tabindex="2008" disabled="disabled">F9<br>&nbsp;</button>
				<button type="button" tabindex="2009" disabled="disabled">F10<br>&nbsp;</button>
				<button type="button" tabindex="2010" disabled="disabled">F11<br>&nbsp;</button>
				<button type="button" tabindex="2011" disabled="disabled">F12<br>&nbsp;</button>
		</div>
		<br><br><br>
		
		<s:form>

			<div class="function_forms">

				<div style="padding-left: 20px">
					<html:errors/>
				</div>
				<div style="padding-left: 20px; color: blue;">
					<html:messages id="msg" message="true">
						<bean:write name="msg" ignore="true"/>
					</html:messages>
				</div>

				<html:hidden property="userId"/>
				
			    <div class="form_section_wrap">
				    <div class="form_section">
				    	<div id="search_info" class="section_body">
							<table id="search_info1" class="forms" style="width: 500px" summary="パスワード変更フォーム">
								<colgroup>
									<col span="1" style="width: 50%">
									<col span="1" style="width: 50%">
								</colgroup>
								<tr>
									<th><div class="col_title_right_req">現在のパスワード<bean:message key='labels.must'/></div></th>
									<td><html:password style="width: 300px;" tabindex="100" property="oldPassword" redisplay="false"/></td>
								</tr>
								<tr>
									<th><div class="col_title_right_req">新しいパスワード<bean:message key='labels.must'/></div></th>
									<td><html:password style="width: 300px;" tabindex="101" property="newPassword" redisplay="false"/></td>
								</tr>
								<tr>
									<th><div class="col_title_right_req">新しいパスワード（確認）<bean:message key='labels.must'/></div></th>
									<td><html:password style="width: 300px;" tabindex="102" property="newPasswordConfirm" redisplay="false"/></td>
								</tr>
							</table>
						</div>
			    	</div><!-- /.form_section -->
		    	</div><!-- /.form_section_wrap -->

				<div style="text-align: right; width: 1160px">
					<button type="button" tabindex="150" onclick="onF1()" style="" class="btn_medium" >初期化</button>
					<c:if test="${isUpdate == true}">
					<button type="button" tabindex="151" onclick="onF3()" style="" class="btn_medium">変更</button>
					</c:if>
					<c:if test="${isUpdate == false}">
					<button type="button" tabindex="151" onclick="onF3()" style="" disabled="disabled" class="btn_medium">変更</button>
					</c:if>
				</div>
			</div>
		</s:form>
	</div>
</body>

</html>
