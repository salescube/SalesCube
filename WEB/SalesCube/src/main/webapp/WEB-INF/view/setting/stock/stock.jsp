<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　在庫管理</title>
<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--

	//リセット
	function onF1(){
		if(confirm('<bean:message key="confirm.reset" />')){
			document.setting_stockActionForm.action = '${f:url("/setting/stock/reset")}';
			document.setting_stockActionForm.submit();
		}
	}

	//更新
	function onF3(){
		if(confirm('<bean:message key="confirm.insert" />')){
			document.setting_stockActionForm.action = '${f:url("/setting/stock/update")}';
			document.setting_stockActionForm.submit();
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
		<jsp:param name="MENU_ID" value="1201"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">

		<span class="title">在庫管理</span>

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

		<s:form action="update" onsubmit="return false;">

			<div class="function_forms">

				<div style="padding-left: 20px">
					<html:errors/>
				</div>
				<div style="padding-left: 20px; color: blue;">
					<html:messages id="msg" message="true">
						<bean:write name="msg" ignore="true"/>
					</html:messages>
				</div>

				<html:hidden property="updDatetm"/>

				<div class="form_section_wrap">
					<div class="form_section">
						<div class="section_title">
							<span>計算設定</span>
							<button class="btn_toggle" />
			      		</div><!-- /.section_title -->

						<div class="section_body">
							<table id="scompany_info1" class="forms" style="width: 700px" summary="在庫管理情報">
								<colgroup>
									<col span="1" style="width: 35%">
									<col span="1" style="width: 65%">
								</colgroup>
								<tr>
									<th><div class="col_title_right"><bean:message key="labels.stockHoldTermCalcCategory"/></div></th>
									<td>過去
										<html:select property="stockHoldTermCalcCategory"  tabindex="100" style="width: 150px;">
										<html:options collection="stockHoldTermCalcCategoryList" property="value" labelProperty="label"/>&nbsp;
										</html:select>
									間</td>

								</tr>
								<tr>
									<th><div class="col_title_right_req"><bean:message key="labels.deficiencyRate"/><bean:message key='labels.must'/></div></th>
									<td><html:text style="width: 50px; ime-mode: disabled; text-align: right;" tabindex="101" property="deficiencyRate" maxlength="4"/></td>
								</tr>
								<tr>
									<th><div class="col_title_right_req"><bean:message key="labels.maxEntrustPoTimelag"/><bean:message key='labels.must'/></div></th>
									<td><html:text style="width: 50px; ime-mode: disabled; text-align: right;" tabindex="102" property="maxEntrustPoTimelag" maxlength="3"/> 日</td>
								</tr>
							</table>
						</div><!-- /.section_body -->
			    	</div><!-- /.form_section -->
			   	</div><!-- /.form_section_wrap -->


				<div class="form_section_wrap">
					<div class="form_section">
						<div class="section_title">
							<span>制限設定</span>
							<button class="btn_toggle" />
			      		</div><!-- /.section_title -->

						<div class="section_body">
							<table id="scompany_info1" class="forms" style="width: 700px" summary="在庫管理情報">
								<colgroup>
									<col span="1" style="width: 35%">
									<col span="1" style="width: 40%">
									<col span="1" style="width: 15%">
									<col span="1" style="width: 10%">
								</colgroup>
								<tr>
									<th><div class="col_title_right_req"><bean:message key="labels.stockHoldDays"/><bean:message key='labels.must'/></div></th>
									<td colspan="3">月平均出荷数の <html:text style="width: 50px; ime-mode: disabled; text-align: right;" tabindex="201" property="stockHoldDays" maxlength="3"/> ヶ月分</td>
								</tr>
								<tr>
									<th><div class="col_title_right_req"><bean:message key="labels.minPoLotCalcDays"/><bean:message key='labels.must'/></div></th>
									<td>月平均出荷数の <html:text style="width: 50px; ime-mode: disabled; text-align: right;" tabindex="202" property="minPoLotCalcDays" maxlength="3"/> ヶ月分</td>
									<th><div class="col_title_right_req"><bean:message key="labels.minPoLotNum"/><bean:message key='labels.must'/></div></th>
									<td><html:text style="width: 50px; ime-mode: disabled; text-align: right;" tabindex="203" property="minPoLotNum" maxlength="3"/></td>
								</tr>
								<tr>
									<th><div class="col_title_right_req"><bean:message key="labels.maxPoNumCalcDays"/><bean:message key='labels.must'/></div></th>
									<td colspan="3">月平均出荷数の <html:text style="width: 50px; ime-mode: disabled; text-align: right;" tabindex="204" property="maxPoNumCalcDays" maxlength="3"/> ヶ月分</td>
								</tr>
								<tr>
									<th><div class="col_title_right_req"><bean:message key="labels.minPoNum"/><bean:message key='labels.must'/></div></th>
									<td colspan="3"> <html:text style="width: 50px; ime-mode: disabled; text-align: right;" tabindex="205" property="minPoNum" maxlength="3"/></td>
								</tr>
							</table>
						</div><!-- /.section_body -->
			    	</div><!-- /.form_section -->
			   	</div><!-- /.form_section_wrap -->

				<div style="text-align: right; width: 1160px;">
					<button tabindex="250" onclick="onF1()" style="" class="btn_medium">リセット</button>
					<c:if test="${isUpdate == true}">
					<button tabindex="251" onclick="onF3()" style="" class="btn_medium">更新</button>
					</c:if>
					<c:if test="${isUpdate == false}">
					<button tabindex="251" onclick="onF3()" style="" disabled="disabled" class="btn_medium">更新</button>
					</c:if>
				</div>
			</div>
		</s:form>
	</div>
</body>

</html>
