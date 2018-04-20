<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><bean:message key='titles.system'/> <bean:message key='titles.importBankDeposit'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>
<script type="text/javascript">
<!--
var MAIN_FORM_NAME = "deposit_importBankDepositActionForm";

var paramData = null;
var paramDataTmp = null;
var data = null;

//ページ読込時の動作
function init(){
	// 初期フォーカス設定
	$("#csvFile").focus();
	$("#bankId").val("1");

	// EXCELボタンの状態変更
	if($("#dispResultCount").val() != "0") {
		$("#btnF3").attr("disabled","");
	} else {
		$("#btnF3").attr("disabled","disabled");
	}}

function onF1() {
	if(!confirm('<bean:message key="confirm.init" />')){
		return;
	}
	showNowSearchingDiv();
	document.deposit_importBankDepositActionForm.action = "${f:url('/deposit/importBankDeposit/reset')}";
	document.deposit_importBankDepositActionForm.submit();
}

function ActionSubmit(ActionName){
	showNowSearchingDiv();
	$("form[name='" + MAIN_FORM_NAME + "']").attr("action",ActionName);
	$("form[name='" + MAIN_FORM_NAME + "']").submit();
}

function onF2() {
	if(!confirm('<bean:message key="confirm.uptake" />')){
		return;
	}
	ActionSubmit("${f:url('/deposit/importBankDeposit/upload')}");
}

//EXCEL
function onF3(){
	// 検索結果をEXCELファイルでダウンロードしますか？
	if(!confirm('<bean:message key="confirm.excel.result" />')){
		return;
	}

	if($("#selectCount").val() == 0){
		window.open(contextRoot + "/ajax/deposit/importBankDepositAjax/excelAll","<bean:message key='words.name.excel'/>");
	}
	else if ($("#selectCount").val() == 1){
		window.open(contextRoot + "/ajax/deposit/importBankDepositAjax/excelOK","<bean:message key='words.name.excel'/>");
	}
	else if ($("#selectCount").val() == 2){
		window.open(contextRoot + "/ajax/deposit/importBankDepositAjax/excelErr","<bean:message key='words.name.excel'/>");
	}
	else{
		window.open(contextRoot + "/ajax/deposit/importBankDepositAjax/excelAll","<bean:message key='words.name.excel'/>");
	}

}

function resetListAll(){
	ActionSubmit("${f:url('/deposit/importBankDeposit/resetListAll')}");
	$("#selectCount").val(0);
}

function resetListOK(){
	ActionSubmit("${f:url('/deposit/importBankDeposit/resetListOK')}");
	$("#selectCount").val(1);
}

function resetListNG(){
	ActionSubmit("${f:url('/deposit/importBankDeposit/resetListNG')}");
	$("#selectCount").val(2);
}


//ソート
function sort(sortColumn) {
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
	}
	// 前回と異なる場合は昇順に設定
	else {
		$("#sortOrderAsc").val("true");
	}

	// 今回のソートカラムにソートラベルを追加
	if($("#sortOrderAsc").val() == "true") {
		$("#sortStatus_"+sortColumn).html("<bean:message key='labels.asc'/>");
	} else {
		$("#sortStatus_"+sortColumn).html("<bean:message key='labels.desc'/>");
	}

	// 前回の結果が1件以上ある場合のみ再検索
	if($("#searchResultCount").val() != "0") {
		// 前回の検索条件からソート条件のみを変更
		paramData = new Object();
		paramData["sortColumn"] = $("#sortColumn").val();
		paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
		paramData["newDepositSlipIdStr"] = $("#newDepositSlipIdStr").val();
		// 検索
		execSearch(paramData);
	}
}

//検索実行
function execSearch(paramData){

	// Ajaxリクエストによって検索結果をロードする
	asyncRequest(
			contextRoot + "/ajax/deposit/importBankDepositAjax/search",
			paramData,
			function(data) {
				// 検索結果テーブルを更新する
				$("#listContainer").empty();
				$("#listContainer").append(data);
			}
		);

}

-->
</script>
</head>
<body onload="init()" onhelp="return false;">
	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0006"/>
		<jsp:param name="MENU_ID" value="0604"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">

		<!-- タイトル -->
		<span class="title"><bean:message key='titles.importBankDeposit'/></span>

		<div class="function_buttons">
			<button id="btnF1" type="button" onclick="onF1();" tabindex="2000">F1<br><bean:message key='words.action.initialize'/></button>
			<button id="btnF2" type="button" onclick="onF2();" tabindex="2001">F2<br><bean:message key='words.action.uptake'/></button>
			<button id="btnF3" tabindex="2002" onclick="onF3()" disabled="disabled">F3<br><bean:message key='words.name.excel'/></button>
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

		<s:form enctype="multipart/form-data" onsubmit="return false;">
		<div id="errors" style="color: red">
			<html:errors />
		</div>
		<div style="padding-left: 20px;color: blue;">
			<html:messages id="msg" message="true">
				<bean:write name="msg" ignore="true"/><br>
			</html:messages>
		</div>

		<div class="function_forms">

			<div class="form_section_wrap">
			<div class="form_section">
				<div class="section_title">
					<span>取込ファイル</span>
					<button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="search_info" class="section_body">
				<table id="search_info1" class="forms" style="width: 600px" summary="取込ファイル">
					<tr>
						<th><div class="col_title_right"><bean:message key='labels.bank.deposit.csv'/></div></th> <!-- 入金CSVファイル-->
						<td style="padding-left: 5px;"><html:file property="csvFile" styleId="csvFile" style="width: 300px;" tabindex="100" onchange="$('#importBtn').focus();"/></td>
					</tr>
				</table>
				</div>
			</div><!-- /.form_section -->
	    	</div><!-- /.form_section_wrap -->

			<div style="width: 1160px; text-align :right;">
				<button type="button" id="initBtn" onclick="onF1();" tabindex="150" class="btn_medium"><bean:message key='words.action.initialize'/></button> <!-- 初期化 -->
				<button type="button" id="importBtn" onclick="onF2();" tabindex="151" class="btn_medium"><bean:message key='words.action.uptake'/></button> <!-- 取込 -->
				<input type="submit" name="upload" tabindex="-1" value="取込" style="display:none;">
			</div>
		</div>

		<html:hidden property="sortColumn" styleId="sortColumn" />
		<html:hidden property="sortOrderAsc" styleId="sortOrderAsc" />
		<html:hidden property="newDepositSlipIdStr" styleId="newDepositSlipIdStr" />
		<html:hidden property="selectCount" styleId="selectCount" />
		<html:hidden property="bankId" styleId="bankId" />
		</s:form>

		<span id="listContainer">
			<%-- 検索結果領域 --%>
			<%@ include file="/WEB-INF/view/ajax/deposit/importBankDepositAjax/searchResultList.jsp" %>
		</span>
	</div>
</body>

</html>
