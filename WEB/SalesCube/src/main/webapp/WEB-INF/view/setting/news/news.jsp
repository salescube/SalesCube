<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　おしらせ編集</title>
<%@ include file="/WEB-INF/view/common/header.jsp" %>
<script type="text/javascript">
<!--
//初期化
function onF1(){
	if(confirm('<bean:message key="confirm.init" />')){
		document.setting_newsActionForm.action = '${f:url("/setting/news/reset")}';
		document.setting_newsActionForm.submit();
	}
}

//更新
function onF3(){
	if(confirm('<bean:message key="confirm.insert" />')){
		document.setting_newsActionForm.action = '${f:url("/setting/news/update")}';
		document.setting_newsActionForm.submit();
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
		<jsp:param name="MENU_ID" value="1204"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">

		<span class="title">おしらせ編集</span>

		<div class="function_buttons">
			<button tabindex="2000" onclick="onF1()">F1<br>リセット</button>
			<button tabindex="2001" disabled="disabled">F2<br>&nbsp;</button>

			<c:if test="${isUpdate == true}">
			<button tabindex="2002" onclick="onF3()">F3<br>更新</button>
			</c:if>
			<c:if test="${isUpdate == false}">
			<button tabindex="2002" disabled="disabled">F3<br>更新</button>
			</c:if>
			<button tabindex="2003" disabled="disabled">F4<br>&nbsp;</button>
			<button tabindex="2004" disabled="disabled">F5<br>&nbsp;</button>
			<button tabindex="2005" disabled="disabled">F6<br>&nbsp;</button>
			<button tabindex="2006" disabled="disabled">F7<br>&nbsp;</button>
			<button tabindex="2007" disabled="disabled">F8<br>&nbsp;</button>
			<button tabindex="2008" disabled="disabled">F9<br>&nbsp;</button>
			<button tabindex="2009" disabled="disabled">F10<br>&nbsp;</button>
			<button tabindex="2010" disabled="disabled">F11<br>&nbsp;</button>
			<button tabindex="2011" disabled="disabled">F12<br>&nbsp;</button>
		</div>
		<br><br><br>
		
		<s:form action="update">

			<div class="function_forms">

				<div style="padding-left: 20px">
					<html:errors/>
				</div>
				<div style="padding-left: 20px; color: blue;">
					<html:messages id="msg" message="true">
						<bean:write name="msg" ignore="true"/>
					</html:messages>
				</div>
				<div class="form_section_wrap">
					<div class="form_section">
						<div class="section_title">
						<span>おしらせ編集</span>
						</div><!-- /.section_title -->
							<div id="target_month_info" class="section_body">
								<html:hidden property="updDatetm"/>
								<table id="snews_info1" class="forms" style="width: 910px" summary="おしらせ編集">
									<colgroup>
										<col span="1" style="width: 50%">
										<col span="1" style="width: 50%">
									</colgroup>
									<tr>
										<th><div class="col_title_right">おしらせ</div></th>
										<td><html:textarea style="width: 1000px; height: 200px; overflow-y:scroll;" property="description" tabindex="100" /></td>
									</tr>
								</table>
						</div>
					</div>
								<div style="text-align: right; width: 1160px">
									<button type="button" tabindex="150" onclick="onF1();" class="btn_medium">リセット</button>
									<c:if test="${isUpdate == true}">
									<button type="button" tabindex="151" onclick="onF3();" class="btn_medium">更新</button>
									</c:if>
									<c:if test="${isUpdate == false}">
									<button type="button" tabindex="151" onclick="onF3();" disabled="disabled" class="btn_medium">更新</button>
									</c:if>
								</div>
							</div>


				</div>
		</s:form>
	</div>
</body>

</html>
