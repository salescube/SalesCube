<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　セキュリティ設定</title>
<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--

	//リセット
	function onF1(){
		if(confirm('<bean:message key="confirm.reset" />')){
			document.setting_setSecurityActionForm.action = '${f:url("reset")}';
			document.setting_setSecurityActionForm.submit();
		}
	}

	//更新
	function onF3(){
		if(confirm('<bean:message key="confirm.update" />')){
			document.setting_setSecurityActionForm.action = '${f:url("update")}';
			document.setting_setSecurityActionForm.submit();
		}
	}


//-->
</script>
</head>
<body>
	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0012"/>
		<jsp:param name="MENU_ID" value="1207"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">

		<span class="title">セキュリティ設定</span>

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
				<div id="errors" style="color: red" style="padding-left: 20px"><html:errors/></div>
				<div style="padding-left: 20px;color: blue;">
		        	<html:messages id="msg" message="true">
		        		<bean:write name="msg" ignore="true"/><br>
		        	</html:messages>
		    	</div>

				<html:hidden property="updDatetm"/>
				<div class="form_section_wrap">
					<div class="form_section">
						<div class="section_title">
							<span>パスワード設定</span>
						</div><!-- /.section_title -->
						<div class="section_body">

						<table class="forms" style="width: 800px" summary="パスワード設定">
							<colgroup>
								<col span="1" style="width: 15%">
								<col span="1" style="width: 35%">
								<col span="1" style="width: 15%">
								<col span="1" style="width: 35%">
							</colgroup>
							<tr>
								<th><div class="col_title_right">パスワード桁数</div></th>
								<td><html:text style="width: 50px;" tabindex="1" property="passwordLength" />&nbsp;桁</td>
								<th><div class="col_title_right">パスワード文字種</div></th>
								<td>
									<html:radio tabindex="2" property="passwordCharType" value="1" />指定なし
									<html:radio tabindex="2" property="passwordCharType" value="2" />英数
									<html:radio tabindex="2" property="passwordCharType" value="3" />英数記号

								</td>
							</tr>
							<tr>
								<th><div class="col_title_right">パスワード有効日数</div></th>
								<td><html:text style="width: 50px;" tabindex="3" property="passwordValidDays" />&nbsp;日</td>
								<th><div class="col_title_right">&nbsp;パスワードリトライ回数&nbsp;</div></th>
								<td><html:text style="width: 50px;" tabindex="4" property="totalFailCount" />&nbsp;回</td>
								<th><div class="col_title_right">&nbsp;使用可能過去パスワード&nbsp;</div></th>
								<td><html:text style="width: 50px;" tabindex="5" property="passwordHistCount" />&nbsp;個</td>
							</tr>
						</table>

					</div><!-- /.section_body -->
	    		</div><!-- /.form_section -->
	   		</div><!-- /.form_section_wrap -->

				<div style="text-align: right; width: 1160px">
					<button class="btn_medium"  tabindex="350" onclick="onF1()" style="" >リセット</button>
					<c:if test="${isUpdate == true}">
					<button class="btn_medium"  tabindex="351" onclick="onF3()" style="" >更新</button>
					</c:if>
					<c:if test="${isUpdate == false}">
					<button class="btn_medium"  tabindex="351" onclick="onF3()" style="" disabled="disabled">更新</button>
					</c:if>
				</div>
			</div>
			<html:hidden property="isUpdate"/>
		</s:form>
	</div>
</body>

</html>
