<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　ファイル登録</title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
	var MAIN_FORM_NAME = "setting_editFileUploadActionForm";

	//初期化
	function onF1() {
		if(!confirm('<bean:message key="confirm.init" />')){
			return;
		}
		document.setting_editFileUploadActionForm.fileId.value = "";
		document.setting_editFileUploadActionForm.action = "${f:url("/setting/editFileUpload/reset")}";
		document.setting_editFileUploadActionForm.submit();
		//window.location.doHref('${f:url("/setting/fileUpload")}');

	}

	//戻る
	function onF2(){
		if(!confirm('<bean:message key="confirm.back" />')){
			return;
		}
		  window.location.doHref("${f:url("/setting/searchFileUpload")}");

	}

	function ActionSubmit(ActionName){
		showNowSearchingDiv();
		$("form[name='" + MAIN_FORM_NAME + "']").attr("action",ActionName);
		$("form[name='" + MAIN_FORM_NAME + "']").submit();
	}

	//登録・更新
	function onF3(){
		<c:if test="${!editMode}">
		if(!confirm('<bean:message key="confirm.file.insert" />')){
			return;
		}
		document.setting_editFileUploadActionForm.action = "${f:url("/setting/editFileUpload/insert")}";
		//document.setting_editFileUploadActionForm.action = "${f:url("/setting/editFileUpload/insert")}";
		</c:if>

		<c:if test="${editMode}">
		if(!confirm('<bean:message key="confirm.file.upload" />')){
			return;
		}
		document.setting_editFileUploadActionForm.action = "${f:url("/setting/editFileUpload/update")}";

		</c:if>

		document.setting_editFileUploadActionForm.submit();
	}

	function onF4() {
		if(!confirm('<bean:message key="confirm.delete" />')){
			return;
		}
		document.setting_editFileUploadActionForm.action = "${f:url("/setting/editFileUpload/delete")}";
		document.setting_editFileUploadActionForm.submit();
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
		<jsp:param name="MENU_ID" value="1205"/>
	</jsp:include>


	<%-- メイン機能領域 --%>
	<div id="main_function">
		<%-- タイトル --%>
		<span class="title">ファイル登録</span>

			<div class="function_buttons">
				<button type="button" tabindex="2000" onclick="onF1();">F1<br>初期化</button>
				<button type="button" tabindex="2001" onclick="onF2();">F2<br>戻る</button>
				<button type="button" tabindex="2002" onclick="onF3();"
					<c:if test="${!isUpdate}">disabled</c:if> >F3<br>
					<c:if test="${!editMode}">登録</c:if>
					<c:if test="${editMode}">更新</c:if></button>
				<button type="button" tabindex="2003" onclick="onF4();"
					<c:if test="${!editMode || !isUpdate}">disabled</c:if> >F4<br>削除</button>
				<button type="button" disabled="disabled">F5<br>&nbsp;</button>
				<button type="button" disabled="disabled">F6<br>&nbsp;</button>
				<button type="button" disabled="disabled">F7<br>&nbsp;</button>
				<button type="button" disabled="disabled">F8<br>&nbsp;</button>
				<button type="button" disabled="disabled">F9<br>&nbsp;</button>
				<button type="button" disabled="disabled">F10<br>&nbsp;</button>
				<button type="button" disabled="disabled">F11<br>&nbsp;</button>
				<button type="button" disabled="disabled">F12<br>&nbsp;</button>
			</div>

			<s:form enctype="multipart/form-data" onsubmit="return false;">
			<br><br><br>
			<div class="function_forms">
				<div id="info" style="padding-left: 20px">
					<html:errors/>
				</div>
				<span id="ajax_errors"></span>

				<div style="padding-left: 20px; color: blue;">
					<span id="ajax_infos">
					<html:messages id="msg" message="true">
						<bean:write name="msg" ignore="true"/>
					</html:messages>
					</span>
				</div>

			<div class="form_section_wrap">
				<div class="form_section">
					<div class="section_title">
						<span>ファイル情報</span>
					</div><!-- /.section_title -->
					<div class="section_body">
						<table class="forms" style="width: 600px" summary="ファイル情報">
							<colgroup>
								<col span="1" style="width: 20%">
								<col span="1" style="width: 80%">
							</colgroup>
							<tr>
								<th><div class="col_title_right_req">タイトル<bean:message key='labels.must'/></div></th>
								<td><html:text property="title" style="width: 450px;" tabindex="100" maxlength="60"/></td>
							</tr>
							<tr>
								<th><div class="col_title_right_req">ファイル名<bean:message key='labels.must'/></div></th>
							<c:if test="${!editMode}">
								<td><html:file property="formFile" style="width: 450px;" onchange="$('#openLevel0').focus();" tabindex="101"/></td>
							</c:if>
							<c:if test="${editMode}">
								<td><html:text property="fileName" style="width: 450px;"  tabindex="101" readonly="true" styleClass="c_disable"/></td>
							</c:if>
							</tr>
							<tr>
								<th><div class="col_title_right">公開設定</div></th>
								<td>
									<html:select  tabindex="101"  styleId="openLevel" property="openLevel" style="width: 300px">
										<html:options collection="openLevelList" property="value" labelProperty="label" />
									</html:select>
								</td>
							</tr>
						</table>
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
				<c:forEach var="dept" items="${openLevelList}" varStatus="status">
					<input type="hidden" name="openLevelList[${status.index}].value" value="${dept.value}">
					<input type="hidden" name="openLevelList[${status.index}].label" value="${dept.label}">
				</c:forEach>
				<html:hidden property="creDatetmShow" />
				<html:hidden property="updDatetmShow" />
				<html:hidden property="updDatetm" />
				<html:hidden property="editMode" />
				<html:hidden property="isUpdate" />
				<html:hidden property="fileId" />
			</div>
		</s:form>
	</div>
</body>

</html>
