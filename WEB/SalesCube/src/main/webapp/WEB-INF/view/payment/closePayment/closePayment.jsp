<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title><bean:message key='titles.system'/> <bean:message key='titles.closePayment'/></title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript" src="${f:url('/scripts/dialogs.js')}"></script>
	<script type="text/javascript">
	<!--
		var MAIN_FORM_NAME = "payment_closePaymentActionForm";

		function init(){
			// フォーカス設定
			$("#closeDate").focus();
		}

		// 初期化ボタン
		function onF1(){
			if(confirm('<bean:message key="confirm.init"/>')){
				$("#closeDate").val('${closeDate}');// 締年月日
				$("#closeDate").focus();
			}
		}

		// 締実行ボタン
		function onF3(){
			if(confirm('<bean:message key="confirm.payment.close" />')){
				$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/payment/closePayment/close")}');
				$("form[name='" + MAIN_FORM_NAME + "']").submit();
			}

		}

		// 締解除ボタン
		function onF4(){
			if(confirm('<bean:message key="confirm.payment.reopen" />')){
				$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/payment/closePayment/reopen")}');
				$("form[name='" + MAIN_FORM_NAME + "']").submit();
			}
		}
	-->
	</script>
</head>
<body onload="init()" onhelp="return false;">
<!-- ヘッダ -->
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

<!-- メニュー -->
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0009"/>
		<jsp:param name="MENU_ID" value="0902"/>
	</jsp:include>

<!-- メイン機能 -->
	<div id="main_function">

		<!-- タイトル -->
		<span class="title"><bean:message key='titles.closePayment'/></span>

		<!-- 1.ファンクションボタン -->
		<div class="function_buttons">
			<button type="button" tabindex="2000" onclick="onF1()">F1<br><bean:message key='words.action.initialize'/></button>
			<button type="button" tabindex="2001" disabled="disabled"  >F2<br>&nbsp;</button>
			<button type="button" tabindex="2002" onclick="onF3()">F3<br><bean:message key='words.action.cutoff'/></button>
			<button type="button" tabindex="2003" ${cutoffDataExist? "" : "disabled='disabled'"} onclick="onF4()">F4<br><bean:message key='words.action.cutoffCancel'/></button>
			<button type="button" tabindex="2004" disabled="disabled"  >F5<br>&nbsp;</button>
			<button type="button" tabindex="2005" disabled="disabled"  >F6<br>&nbsp;</button>
			<button type="button" tabindex="2006" disabled="disabled"  >F7<br>&nbsp;</button>
			<button type="button" tabindex="2007" disabled="disabled"  >F8<br>&nbsp;</button>
			<button type="button" tabindex="2008" disabled="disabled"  >F9<br>&nbsp;</button>
			<button type="button" tabindex="2009" disabled="disabled"  >F10<br>&nbsp;</button>
			<button type="button" tabindex="2010" disabled="disabled"  >F11<br>&nbsp;</button>
			<button type="button" tabindex="2011" disabled="disabled"  >F12<br>&nbsp;</button>
		</div>
		<br><br><br>

		<s:form>

		<!-- 2.支払実績締処理情報 -->
		<div class="function_forms">
		
			<!-- エラー情報 -->
			<div id="errors" style="color: red">
				<html:errors />
			</div>
			<div id = "message" style="padding-left: 20px;color: blue;">
				<html:messages id="msg" message="true">
					<bean:write name="msg" ignore="true"/><br>
				</html:messages>
			</div>
			
			<div class="form_section_wrap">
				<div class="form_section">
					<div class="section_title">
						<span>支払実績締処理</span>
					</div><!-- /.section_title -->

					<div id="target_month_info" class="section_body">
						<table class="forms" style="width: 400px;" summary="処理対象">
							<colgroup>
								<col span="1" style="width: 33%">
								<col span="1" style="width: 64%">
							</colgroup>
							<tr>
								<th><div class="col_title_right_req"><bean:message key='labels.closeDate'/><bean:message key='labels.must'/></div></th>
								<td>
									<c:if test="${cutoffDataExist}">
										最終締日は「${f:h(latestAptCutoffDate)}」です。
									</c:if>
									<c:if test="${!cutoffDataExist}">
										支払実績締は行われていません。
									</c:if>
									<html:hidden property="latestAptCutoffDate" />
									<br>
									<div class="pos_r">
									<html:text tabindex="100" property="closeDate" styleId="closeDate" styleClass="date_input" />
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>

		</s:form>
	</div>
</body>

</html>
