<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/> <bean:message key='titles.closeStock'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>
	<script type="text/javascript">
	<!--
		var MAIN_FORM_NAME = "stock_closeStockActionForm";

		// ページ読込時の動作
		$(document).ready(function(){
			// 初期フォーカス設定
			$("#cutoffDate").focus();
		});

		// 初期化
		function onF1(){
			// 入力内容を初期化してよろしいですか？
			if(confirm('<bean:message key="confirm.init" />')){
				<bean:define id="concatUrl" value="${'/stock/closeStock'}" />
				location.doHref('${f:url(concatUrl)}');
			}
		}

		// 締実行
		function onF3(){
			// 在庫締処理を行いますか？
			if(confirm('<bean:message key="confirm.stock.close" />')){
				showNowSearchingDiv();
				$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/stock/closeStock/close")}');
				$("form[name='" + MAIN_FORM_NAME + "']").submit();
			}
		}

		// 締解除
		function onF4(){
			if(new Date("${lastCutoffDate} 23:59:59").getTime() < new Date("2010/07/01 00:00:00").getTime()) {
				alert("<bean:message key="warns.stock.cant.reopen"/>");
				return;
			}

			// 在庫締処理を取り消しますか？
			if(confirm('<bean:message key="confirm.stock.reopen" />')){
				showNowSearchingDiv();
				$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/stock/closeStock/reopen")}');
				$("form[name='" + MAIN_FORM_NAME + "']").submit();
			}
		}

	-->
	</script>
</head>
<body>
	
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0010"/>
		<jsp:param name="MENU_ID" value="1005"/>
	</jsp:include>

	
	<div id="main_function">

		<span class="title"><bean:message key='titles.closeStock'/></span>

		<div class="function_buttons">
			<button type="button" id="btnF1" tabindex="2000" onclick="onF1();">F1<br><bean:message key='words.action.initialize'/><%// 初期化 %>
			</button><button type="button" id="btnF2" tabindex="2001" disabled>F2<br>&nbsp;
			</button><button type="button" id="btnF3" tabindex="2002" onclick="onF3();">F3<br><bean:message key='words.action.cutoff'/><%// 締実行 %>
			</button><button type="button" id="btnF4" tabindex="2003" onclick="onF4();" ${cutoff?"":"disabled"}>F4<br><bean:message key='words.action.cutoffCancel'/><%// 締解除 %>
			</button><button type="button" id="btnF5" tabindex="2004" disabled>F5<br>&nbsp;
			</button><button type="button" id="btnF6" tabindex="2005" disabled>F6<br>&nbsp;
			</button><button type="button" id="btnF7" tabindex="2006" disabled>F7<br>&nbsp;
			</button><button type="button" id="btnF8" tabindex="2007" disabled>F8<br>&nbsp;
			</button><button type="button" id="btnF9" tabindex="2008" disabled>F9<br>&nbsp;
			</button><button type="button" id="btnF10" tabindex="2009" disabled>F10<br>&nbsp;
			</button><button type="button" id="btnF11" tabindex="2010" disabled>F11<br>&nbsp;
			</button><button type="button" id="btnF12" tabindex="2011" disabled>F12<br>&nbsp;
			</button>
		</div>

		<s:form onsubmit="return false;">

			<div class="function_forms">

				<div style="padding-left: 20px">
					<html:errors />
				</div>
				<div id="messages" style="color: blue;padding-left: 20px">
					<html:messages id="msg" message="true">
						<bean:write name="msg" ignore="true"/><br>
					</html:messages>
				</div>

				<div id="target_month_info">
					<table class="forms" style="width: 400px;" summary="処理対象">
						<colgroup>
							<col span="1" style="width: 33%">
							<col span="1" style="width: 64%">
						</colgroup>
						<tr>
							<th><bean:message key='labels.cutoffDate'/><bean:message key='labels.must'/><%// 締年月日 %></th>
							<td>
								<html:hidden property="lastCutoffDate" />
								<c:if test="${cutoff}">
									<%// 最終締日は「yyyy/MM/dd」です。 %>
									<bean:message key='labels.lastCutoffDate.exist' arg0="${lastCutoffDate}" />
								</c:if>
								<c:if test="${!cutoff}">
									<%// 在庫締は行われていません。 %>
									<bean:message key='labels.lastCutoffDate.notExist'/>
								</c:if>
								<br>
								<html:text property="cutoffDate" styleId="cutoffDate" style="width: 100px; ime-mode: disabled;" styleClass="date_input" tabindex="100" maxlength="10" />
							</td>
						</tr>
					</table>
				</div>

			</div>
		</s:form>
	</div>
</body>

</html>
