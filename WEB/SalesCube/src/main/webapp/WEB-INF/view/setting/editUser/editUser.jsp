<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　社員情報</title>
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
	document.setting_editUserActionForm.userId.value = "";
	document.setting_editUserActionForm.action = "${f:url("/setting/editUser/reset")}";
	document.setting_editUserActionForm.submit();
}

//戻る
function onF2(){
	if(!confirm('<bean:message key="confirm.back" />')){
		return;
	}
	window.location.doHref("${f:url("/setting/searchUser")}");
}

//登録・更新
function onF3(){
	<c:if test="${!editMode}">
	if(!confirm('<bean:message key="confirm.insert" />')){
		return;
	}
	document.setting_editUserActionForm.action = "${f:url("/setting/editUser/insert")}";
	</c:if>

	<c:if test="${editMode}">
	if(!confirm('<bean:message key="confirm.update" />')){
		return;
	}
	document.setting_editUserActionForm.action = "${f:url("/setting/editUser/update")}";
	</c:if>

	document.setting_editUserActionForm.submit();
}

function onF4() {
	if(!confirm('<bean:message key="confirm.delete" />')){
		return;
	}
	document.setting_editUserActionForm.action = "${f:url("/setting/editUser/delete")}";
	document.setting_editUserActionForm.submit();
}

-->
</script>
</head>
<body>
	
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0012"/>
		<jsp:param name="MENU_ID" value="1203"/>
	</jsp:include>

	
	<div id="main_function">
		<!-- タイトル -->
		<span class="title">社員情報</span>

		<div class="function_buttons">
			<button type="button" tabindex="2000" onclick="onF1();">F1<br>初期化</button><button
				type="button" tabindex="2001" onclick="onF2();">F2<br>戻る</button><button
				type="button" tabindex="2002" onclick="onF3();" <c:if test="${!isUpdate}">disabled</c:if> >F3<br>
					<c:if test="${!editMode}">登録</c:if>
					<c:if test="${editMode}">更新</c:if></button><button
				type="button" tabindex="2003" onclick="onF4();" <c:if test="${!editMode || !isUpdate}">disabled</c:if> >F4<br>削除</button><button
				type="button" disabled="disabled">F5<br>&nbsp;</button><button
				type="button" disabled="disabled">F6<br>&nbsp;</button><button
				type="button" disabled="disabled">F7<br>&nbsp;</button><button
				type="button" disabled="disabled">F8<br>&nbsp;</button><button
				type="button" disabled="disabled">F9<br>&nbsp;</button><button
				type="button" disabled="disabled">F10<br>&nbsp;</button><button
				type="button" disabled="disabled">F11<br>&nbsp;</button><button
				type="button" disabled="disabled">F12<br>&nbsp;</button>
		</div>

		<s:form onsubmit="return false;">

			<div class="function_forms">
				<div style="padding-left: 20px">
					<html:errors/>
				</div>
				<div style="padding-left: 20px; color: blue;">
					<html:messages id="msg" message="true">
						<bean:write name="msg" ignore="true"/>
					</html:messages>
				</div>

				<div>
					<table class="forms" style="width: 800px" summary="社員情報">
						<colgroup>
							<col span="1" style="width: 5%">
							<col span="1" style="width: 25%">
							<col span="1" style="width: 25%">
							<col span="1" style="width: 20%">
							<col span="1" style="width: 25%">
						</colgroup>
						<tr>
							<th colspan="2">社員コード※</th>
							<td colspan="3">
								<c:if test="${!editMode}">
								<html:text property="userId" tabindex="100" style="width: 130px; ime-mode: disabled;" />
								</c:if>
								<c:if test="${editMode}">
								<html:text property="userId" tabindex="100" style="width: 130px; ime-mode: disabled;"  readonly="true" styleClass="c_disable"/>
								</c:if>
							</td>
						</tr>
						<tr>
							<th colspan="2">社員名※</th>
							<td colspan="3">
								<html:text property="nameKnj"  tabindex="101" style="width: 150px"/>
							</td>
						</tr>
						<tr>
							<th colspan="2">社員名カナ※</th>
							<td colspan="3">
								<html:text property="nameKana"  tabindex="102" style="width: 150px"/>
							</td>
						</tr>
						<tr>
							<th colspan="2">パスワード<c:if test="${!editMode}">※</c:if></th>
							<td>
								<html:password style="width: 150px;" tabindex="103" property="password" redisplay="false"/>
							</td>
							<th>パスワード（確認）<c:if test="${!editMode}">※</c:if></th>
							<td>
								<html:password style="width: 150px;" tabindex="104" property="passwordConfirm" redisplay="false"/>
							</td>
						</tr>
						<tr>
							<th colspan="2">部門</th>
							<td colspan="3">
								<html:select tabindex="105"  property="deptId">
									<html:options collection="deptList" property="value" labelProperty="label" />
								</html:select>
							</td>
						</tr>
						<tr>
							<th colspan="2">E-MAIL</th>
							<td colspan="3">
								<html:text property="email"  tabindex="106" style="width: 400px; ime-mode: disabled;"/>
							</td>
						</tr>

						<tr>
							<th rowspan="${menuCount / 2 + 1}">権限</th>
							<c:forEach var="bean" items="${menuDtoList}" varStatus="status" begin="0" end="0">
							<%@ include file="/WEB-INF/view/setting/editUser/editUserRole.jsp" %>
							</c:forEach>
							<c:forEach var="bean" items="${menuDtoList}" varStatus="status" begin="1" end="1">
							<%@ include file="/WEB-INF/view/setting/editUser/editUserRole.jsp" %>
							</c:forEach>
						</tr>
						<c:forEach var="bean" items="${menuDtoList}" varStatus="status" begin="2">
						<c:if test='${(status.index % 2) == 0}'>
						<tr>
							<%@ include file="/WEB-INF/view/setting/editUser/editUserRole.jsp" %>
						</c:if>
						<c:if test='${(status.index % 2) == 1}'>
							<%@ include file="/WEB-INF/view/setting/editUser/editUserRole.jsp" %>
						</tr>
						</c:if>
						</c:forEach>
					</table>
				</div>

				<div style="text-align: right; width: 800px">
					<c:if test="${editMode}">
					<span>登録日：${creDatetmShow}&nbsp;&nbsp;更新日:${updDatetmShow}</span>
					</c:if>
					&nbsp;&nbsp;
					<button type="button" tabindex="300" onclick="onF1()">初期化</button>
					<button type="button" tabindex="301" onclick="onF3()" <c:if test="${!isUpdate}">disabled</c:if> >
						<c:if test="${!editMode}">登録</c:if>
						<c:if test="${editMode}">更新</c:if>
					</button>
				</div>

				<c:forEach var="dept" items="${deptList}" varStatus="status">
					<input type="hidden" name="deptList[${status.index}].value" value="${dept.value}">
					<input type="hidden" name="deptList[${status.index}].label" value="${dept.label}">
				</c:forEach>
				<html:hidden property="creDatetmShow" />
				<html:hidden property="updDatetmShow" />
				<html:hidden property="updDatetm" />
				<html:hidden property="menuCount" />
				<html:hidden property="editMode" />
				<html:hidden property="isUpdate" />
			</div>
		</s:form>
	</div>
</body>
</html>