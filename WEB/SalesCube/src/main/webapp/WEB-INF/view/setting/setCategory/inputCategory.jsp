<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　区分情報</title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>
	<link rel="stylesheet" href="${f:url('/css/menu.css')}" type="text/css">
<script type="text/javascript">
<!--
var dataTmp = new Object();

function init(){
	// 処理なし
}
//初期化
function onF1(){
	if(!confirm('<bean:message key="confirm.init" />')){
		return;
	}
	document.setting_setCategoryActionForm.action = "${f:url('/setting/setCategory/reset')}";
	document.setting_setCategoryActionForm.submit();
}


//更新
function onF3(){
	if(!confirm('<bean:message key="confirm.update" />')){
		return;
	}
	document.setting_setCategoryActionForm.action = "${f:url('/setting/setCategory/update')}";
	document.setting_setCategoryActionForm.submit();
}
-->
</script>
</head>
<body onload="init()">
	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0012"/>
		<jsp:param name="MENU_ID" value="1209"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">
		<!-- タイトル -->
		<span class="title">区分情報</span>

		<div class="function_buttons">
			<button type="button" tabindex="2000" onclick="onF1();">F1<br>初期化</button>
			<button type="button" disabled="disabled">F2<br>&nbsp;</button>
			<button type="button" tabindex="2002" onclick="onF3();">F3<br>更新</button>
			<button type="button" disabled="disabled">F4<br>&nbsp;</button>
			<button type="button" disabled="disabled">F5<br>&nbsp;</button>
			<button type="button" disabled="disabled">F6<br>&nbsp;</button>
			<button type="button" disabled="disabled">F7<br>&nbsp;</button>
			<button type="button" disabled="disabled">F8<br>&nbsp;</button>
			<button type="button" disabled="disabled">F9<br>&nbsp;</button>
			<button type="button" disabled="disabled">F10<br>&nbsp;</button>
			<button type="button" disabled="disabled">F11<br>&nbsp;</button>
			<button type="button" disabled="disabled">F12<br>&nbsp;</button>
		</div>
		<br><br><br>

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
			</div>
			<div class="form_section_wrap">
				<div class="form_section">
					<div class="section_title">
						<span>区分情報</span>
						<button class="btn_toggle" />
					</div><!-- /.section_title -->
					<div class="section_body">
					<div>
						<table class="forms" style="width: 800px" summary="区分情報">
							<colgroup>
								<col span="1" style="width: 25%">
								<col span="1" style="width: 25%">
								<col span="1" style="width: 20%">
								<col span="1" style="width: 25%">
							</colgroup>

							<c:forEach var="bean" items="${categoryDtoList}" varStatus="status" >
								<c:if test='${(status.index % 2) == 0 || (status.first)}'>
									<tr>
								</c:if>
									<th><div class="col_title_right">${bean.name}</div></th>
									<td>
										<html:checkbox
											styleId="categoryDtoList[${status.index}].isInsert"
											property="categoryDtoList[${status.index}].isInsert"
											tabindex="${status.index + 200}" />
											<label for="categoryDtoList[${status.index}].isInsert">追加</label>
										<html:checkbox
											styleId="categoryDtoList[${status.index}].isUpdate"
											property="categoryDtoList[${status.index}].isUpdate"
											tabindex="${status.index + 200}" />
											<label for="categoryDtoList[${status.index}].isUpdate">更新</label>
										<html:checkbox
											styleId="categoryDtoList[${status.index}].isDelete"
											property="categoryDtoList[${status.index}].isDelete"
											tabindex="${status.index + 200}" />
											<label for="categoryDtoList[${status.index}].isDelete">削除</label>
										<html:hidden
											styleId="categoryDtoList[${status.index}].categoryId"
											property="categoryDtoList[${status.index}].categoryId" />
										<html:hidden
											styleId="categoryDtoList[${status.index}].updDatetm"
											property="categoryDtoList[${status.index}].updDatetm" />
									</td>
								<c:if test='${(status.index % 2) == 1 || (status.last)}'>
									</tr>
								</c:if>
							</c:forEach>
						</table>
					</div>
					</div><!-- /.section_body -->
    			</div><!-- /.form_section -->
    		</div><!-- /.form_section_wrap -->
   			<div style="text-align: right; width: 1160px">
				<button class="btn_medium"  type="button" tabindex="400" onclick="onF1()">初期化</button>
				<button class="btn_medium"  type="button" tabindex="401" onclick="onF3()" >更新</button>
			</div>
		</s:form>
	</div>
</body>
</html>