<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/> <bean:message key='titles.importOnlineOrder' /></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>
	<script type="text/javascript">
	<!--
		var MAIN_FORM_NAME = "rorder_importOnlineOrderActionForm";

		// ページ読込時の動作
		$(document).ready(function(){
			// 初期フォーカス設定
			$("#uploadFile").focus();
			// 初期検索
			sort($("#sortColumn").val());
		});

		// 初期化
		function onF1(){
			// 入力内容を初期化してよろしいですか？
			if(confirm('<bean:message key="confirm.init" />')){
				showNowSearchingDiv();
				<bean:define id="concatUrl" value="${'/rorder/importOnlineOrder/init'}" />
				location.doHref('${f:url(concatUrl)}');
			}
		}

		// 取込
		function onF3(){
			// 通販サイトデータファイルを取り込みますか？
			if(confirm('<bean:message key="confirm.onlineorder.file.import" />')){
				showNowSearchingDiv();
				$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/rorder/importOnlineOrder/importFile")}');
				$("form[name='" + MAIN_FORM_NAME + "']").submit();
			}
		}

		// 再表示（取込済は除くチェックボックス選択時）
		function onShowExist(){
			showNowSearchingDiv();
			flg = $("#showExist").attr('checked');
			<bean:define id="concatUrl" value="${'/rorder/importOnlineOrder/redraw/'}" />
			location.doHref('${f:url(concatUrl)}' + flg);
		}

		// ソート
		function sort(sortColumn) {
			var paramData = null;
			// 前回のソートカラムとソート順を取得
			var beforeSortColumn = $("#sortColumn").val();
			var beforeSortOrderAsc = $("#sortOrderAsc").val();
			// 前回のソートカラムからソートラベルを削除
			if($("#sortStatus_"+beforeSortColumn).get(0)) {
				$("#sortStatus_"+beforeSortColumn).empty();
			}
			// 今回のソートカラムを設定
			$("#sortColumn").val(sortColumn);
			// 前回と同じカラムをクリックした場合はソート順を入れ替える
			if(beforeSortColumn == sortColumn) {
				if(beforeSortOrderAsc == "true") {
					$("#sortOrderAsc").val("false");
				} else {
					$("#sortOrderAsc").val("true");
				}
			} else {
				// 前回と異なる場合は昇順に設定
				$("#sortOrderAsc").val("true");
			}
			// 今回のソートカラムにソートラベルを追加
			if($("#sortOrderAsc").val() == "true") {
				$("#sortStatus_"+sortColumn).html("<bean:message key='labels.asc'/>");
			} else {
				$("#sortStatus_"+sortColumn).html("<bean:message key='labels.desc'/>");
			}
			// 前回の結果が1件以上ある場合のみ再検索
			//if($("#searchResultCount").val() != "0") {
				// 前回の検索条件からソート条件のみを変更
				paramData = new Object();
				if($("#showExist:checked").size() > 0) {
					paramData["showExist"] = true;
				}
				else {
					paramData["showExist"] = false;
				}
				paramData["sortColumn"] = $("#sortColumn").val();
				paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
				paramData["isUpdate"] = $("#isUpdate").val();
				paramData["isInputValid"] = $("#isInputValid").val();
				// 検索
				execSearch(paramData);
			//}
		}

		// 検索実行
		function execSearch(paramData) {
			// Ajaxリクエストによって検索結果をロードする
			asyncRequest(
				contextRoot + "/ajax/rorder/importOnlineOrderResultAjax/search",
				paramData,
				function(data) {
					// 検索結果テーブルを更新する
					$("#listContainer").empty();
					$("#listContainer").append(data);
				}
			);
		}

		function deleteLine( onlineOrderId ){
			if(confirm('<bean:message key="confirm.delete"/>')) {
				$("#roId").val( onlineOrderId );
				$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/rorder/importOnlineOrder/delete")}');
				$("form[name='" + MAIN_FORM_NAME + "']").submit();
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
		<jsp:param name="PARENT_MENU_ID" value="0003"/>
		<jsp:param name="MENU_ID" value="0303"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">

		<span class="title"><bean:message key='titles.importOnlineOrder' /></span>

		<div class="function_buttons">
			<button type="button" id="btnF1" tabindex="2000" onclick="onF1();">F1<br><bean:message key='words.action.initialize'/><%// 初期化 %></button>
			<button type="button" id="btnF2" tabindex="2001" disabled>F2<br>&nbsp;</button>
			<button type="button" id="btnF3" tabindex="2002" onclick="onF3();">F3<br><bean:message key='words.action.uptake'/><%// 取込 %></button>
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

		<s:form enctype="multipart/form-data" onsubmit="return false;">
			<div class="function_forms">

				<html:errors />
				<div id="ajax_errors" style="padding-left: 20px">
				</div>
				<div id="messages" style="padding-left: 20px; color: blue;">
					<html:messages id="msg" message="true">
						<bean:write name="msg" ignore="true"/><br>
					</html:messages>
				</div>

			    <div class="form_section_wrap">
			    <div class="form_section">
			    	<div class="section_title">
						<span>受注データ</span>
			            <button class="btn_toggle" />
					</div><!-- /.section_title -->

					<div id="search_info" class="section_body">
						<table summary="注文データ" class="forms">
							<colgroup>
								<col span="1" style="width: 15%">
								<col span="1" style="width: 85%">
							</colgroup>
							<tr>
								<th><div class="col_title_right_req"><bean:message key='labels.onlineorder.file' /><bean:message key='labels.must'/><%// 注文データ %></div></th>
								<td><html:file property="uploadFile" styleId="uploadFile" style="width: 600px" tabindex="100" onchange="$('#importBtn').focus();" /></td>
							</tr>
							<tr>
								<th><div class="col_title_right">取込済は除く</div></th>
								<td><html:checkbox property="showExist" styleId="showExist" onclick="onShowExist();"></html:checkbox></td>
							</tr>
						</table>
					</div>
		    	</div><!-- /.form_section -->
		    	</div><!-- /.form_section_wrap -->

				<div style="width: 1160px; text-align: right">
					<button type="button" id="initBtn" onclick="onF1();" tabindex="150" class="btn_medium"><bean:message key='words.action.initialize'/><%// 初期化 %></button>
					<button type="button" id="importBtn" onclick="onF3();" tabindex="151" class="btn_medium"><bean:message key='words.action.uptake'/><%// 取込 %></button>
				</div>

				<html:hidden property="sortColumn" styleId="sortColumn" />
				<html:hidden property="sortOrderAsc" styleId="sortOrderAsc" />
				<html:hidden property="isUpdate" styleId="isUpdate" />
				<html:hidden property="isInputValid" styleId="isInputValid" />
				<html:hidden property="roId" styleId="roId" />

			</div>
		</s:form>

		<span id="listContainer">
			<%-- 検索結果領域 --%>
			<%@ include file="/WEB-INF/view/ajax/rorder/importOnlineOrderResultAjax/result.jsp" %>
		</span>
	</div>
</body>

</html>
