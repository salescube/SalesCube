<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/> <bean:message key='titles.outputStockReport'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>
	<script type="text/javascript">
	<!--

		// ページ読込時の動作
		$(document).ready(function(){
			// 初期フォーカス設定
			$("#targetYm").focus();
		});

		// Excel
		function onF3(){
			// この内容でEXCEL出力しますか？
			if(!confirm('<bean:message key="confirm.excel" />')){
				return;
			}

			// 検索条件を設定する
			var data = createParamData();
			// 検索を実行する
			execSearch(data);
		}

		// 検索パラメータの作成
		function createParamData() {
			var data = new Object();
			data["targetYm"] = $("#targetYm").val();
			return data;
		}

		// 検索実行
		function execSearch(data) {
			// Ajaxリクエストによって検索結果をロードする
			asyncRequest(
				contextRoot + "/ajax/outputStockReportAjax/search",
				data,
				function(data) {
					// 検索結果テーブルを更新する
					$("#listContainer").empty();
					$("#listContainer").append(data);
					// 1件以上ヒットした場合
					if($("#searchResultCount").val() != "0") {
						window.open(contextRoot + "/ajax/outputStockReportAjax/excel","<bean:message key='words.name.excel'/>");
					}
				}
			);
		}

	-->
	</script>
</head>
<body>
	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0010"/>
		<jsp:param name="MENU_ID" value="1003"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">

		<span class="title"><bean:message key='titles.outputStockReport'/></span>

		<div class="function_buttons">
			<button type="button" id="btnF1" tabindex="2000" disabled>F1<br>&nbsp;</button>
			<button type="button" id="btnF2" tabindex="2001" disabled>F2<br>&nbsp;</button>
			<button type="button" id="btnF3" tabindex="2002" onclick="onF3();">F3<br><bean:message key='words.name.excel'/><%// EXCEL %></button>
			<button type="button" id="btnF4" tabindex="2003" disabled>F4<br>&nbsp;</button>
			<button type="button" id="btnF5" tabindex="2004" disabled>F5<br>&nbsp;</button>
			<button type="button" id="btnF6" tabindex="2005" disabled>F6<br>&nbsp;</button>
			<button type="button" id="btnF7" tabindex="2006" disabled>F7<br>&nbsp;</button>
			<button type="button" id="btnF8" tabindex="2007" disabled>F8<br>&nbsp;</button>
			<button type="button" id="btnF9" tabindex="2008" disabled>F9<br>&nbsp;</button>
			<button type="button" id="btnF10" tabindex="2009" disabled>F10<br>&nbsp;</button>
			<button type="button" id="btnF11" tabindex="2010" disabled>F11<br>&nbsp;</button>
			<button type="button" id="btnF12" tabindex="2011" disabled>F12<br>&nbsp;</button>
		</div>
		<br><br><br>
		<s:form onsubmit="return false;">

			<div class="function_forms">

				<div id="ajax_errors" style="padding-left: 20px">
					<html:errors />
				</div>

			<div class="form_section_wrap">
				<div class="form_section">
					<div class="section_title">
						<span>在庫残高表</span>
					</div><!-- /.section_title -->
					<div id="target_month_info" class="section_body">
						<table class="forms" summary="在庫表出力条件">
							<colgroup>
								<col span="1" style="width: 20%">
								<col span="1" style="width: 80%">
							</colgroup>
							<tr>
								<th><div class="col_title_right_req"><bean:message key='labels.targetYm'/><bean:message key='labels.must'/></th><%// 出力対象年月 %></div>
								<td>
									<html:text property="targetYm" styleId="targetYm" styleClass="c_referable" style="width: 100px; ime-mode: disabled;" tabindex="100" maxlength="7" />
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			</div>
		</s:form>
		<span id="listContainer" style="display: none;">
		</span>
	</div>
</body>

</html>
