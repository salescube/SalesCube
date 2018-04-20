<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　部門情報</title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>
	<link rel="stylesheet" href="${f:url('/css/menu.css')}" type="text/css">
<script type="text/javascript">
<!--
var dataTmp = new Object();

//初期化
function onF1(){
	if(!confirm('<bean:message key="confirm.init" />')){
		return;
	}
	document.setting_editDeptActionForm.deptId.value = "";
	document.setting_editDeptActionForm.action = "${f:url("/setting/editDept/reset")}";
	document.setting_editDeptActionForm.submit();
}

//戻る
function onF2(){
	if(!confirm('<bean:message key="confirm.back" />')){
		return;
	}
	window.location.doHref("${f:url("/setting/searchDept")}");
}

//登録・更新
function onF3(){
	<c:if test="${!editMode}">
	if(!confirm('<bean:message key="confirm.insert" />')){
		return;
	}
	document.setting_editDeptActionForm.action = "${f:url("/setting/editDept/insert")}";
	</c:if>

	<c:if test="${editMode}">
	if(!confirm('<bean:message key="confirm.update" />')){
		return;
	}
	document.setting_editDeptActionForm.action = "${f:url("/setting/editDept/update")}";
	</c:if>

	document.setting_editDeptActionForm.submit();
}

function onF4() {
	if(!confirm('<bean:message key="confirm.delete" />')){
		return;
	}
	document.setting_editDeptActionForm.action = "${f:url("/setting/editDept/delete")}";
	document.setting_editDeptActionForm.submit();
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
		<jsp:param name="MENU_ID" value="1202"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">
		<!-- タイトル -->
		<span class="title">部門情報</span>

		<div class="function_buttons">
			<button type="button" tabindex="2000" onclick="onF1();">F1<br>初期化</button>
			<button type="button" tabindex="2001" onclick="onF2();">F2<br>戻る</button>
			<button type="button" tabindex="2002" onclick="onF3();" 
				<c:if test="${!isUpdate}">disabled</c:if> >F3<br>
				<c:if test="${!editMode}">登録</c:if>
				<c:if test="${editMode}">更新</c:if></button>
			<button type="button" tabindex="2003" onclick="onF4();" <c:if test="${!editMode || !isUpdate}">disabled</c:if> >F4<br>削除</button>
			<button type="button" disabled="disabled">F5<br>&nbsp;</button>
			<button type="button" disabled="disabled">F6<br>&nbsp;</button>
			<button type="button" disabled="disabled">F7<br>&nbsp;</button>
			<button type="button" disabled="disabled">F8<br>&nbsp;</button>
			<button type="button" disabled="disabled">F9<br>&nbsp;</button>
			<button type="button" disabled="disabled">F10<br>&nbsp;</button>
			<button type="button" disabled="disabled">F11<br>&nbsp;</button>
			<button type="button" disabled="disabled">F12<br>&nbsp;</button>
		</div>


		<s:form onsubmit="return false;">
		<br><br><br>
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
					<span>部門情報</span><br>
				</div><!-- /.section_title -->
				<div class="section_body">
					<div>
						<table class="forms" summary="部門情報" style="width: 500px">
							<tr>
								<th><div class="col_title_right_req">部門コード<bean:message key='labels.must'/></div></th>
								<td>
									<c:if test="${!editMode}">
									<html:text styleId="deptId" property="deptId" style="width: 120px;ime-mode:disabled;"  tabindex="100"/>
									</c:if>
									<c:if test="${editMode}">
									<html:text styleId="deptId" property="deptId" style="width: 120px;ime-mode:disabled;"  tabindex="100" readonly="true" styleClass="c_disable"/>
									</c:if>
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right">親部門</div></th>
								<td>
									<html:select  tabindex="101"  styleId="parentId" property="parentId" style="width: 300px">
										<html:options collection="parentList" property="value" labelProperty="label" />
									</html:select>
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right_req">部門名<bean:message key='labels.must'/></div></th>
								<td><html:text styleId="name" property="name" style="width: 300px" tabindex="102"/></td>
							</tr>
						</table>
					</div>
				</div><!-- /.section_body -->
	    	</div><!-- /.form_section -->
	   	</div><!-- /.form_section_wrap -->

				<div style="text-align: right; width: 1160px">
					<c:if test="${editMode}">
					<span>登録日：${creDatetmShow}&nbsp;&nbsp;更新日:${updDatetmShow}</span>
					</c:if>
					&nbsp;&nbsp;
					<button class="btn_medium" type="button" tabindex="300" onclick="onF1()">初期化</button>
					<button class="btn_medium" type="button" tabindex="301" onclick="onF3()" <c:if test="${!isUpdate}">disabled</c:if> >
						<c:if test="${!editMode}">登録</c:if>
						<c:if test="${editMode}">更新</c:if>
					</button>
				</div>

				<c:forEach var="dept" items="${parentList}" varStatus="status">
					<input type="hidden" name="parentList[${status.index}].value" value="${dept.value}">
					<input type="hidden" name="parentList[${status.index}].label" value="${dept.label}">
				</c:forEach>

				<html:hidden property="creDatetmShow" />
				<html:hidden property="updDatetmShow" />
				<html:hidden property="updDatetm" />
				<html:hidden property="editMode" />
				<html:hidden property="isUpdate" />
			</div>
		</s:form>
	</div>
</body>
</html>