<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="jp.co.arkinfosys.common.Constants"%>
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><bean:message key='titles.system'/> <bean:message key='titles.searchEstimate'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

<script type="text/javascript">
<!--
var paramData = null;
var paramDataTmp = null;
var data = null;

//ページ読込時の動作
function init() {
	// 初期フォーカス
	$('#estimateSheetId').focus();

	$("#customerCode").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁

}


// 初期化
function onF1(){
	// 入力内容を初期化してよろしいですか？
	if(confirm('<bean:message key="confirm.init" />')){
		showNowSearchingDiv();
		location.doHref('${f:url("/estimate/searchEstimate")}');
	}
}

// 検索ボタンによる検索処理
function onF2() {

	paramData = createParamData();
	paramData["pageNo"] = 1;

	return execSearch(paramData);
}

// EXCEL
function onF3(){
	// 検索結果をEXCELファイルでダウンロードしますか？
	if(!confirm('<bean:message key="confirm.excel.result" />')){
		return;
	}

	var form = $(window.document.forms["OutputForm"]);
	form.empty();

	for(var key in paramDataTmp) {
		if(!paramDataTmp[key] ||
				(typeof paramDataTmp[key].length != undefined && paramDataTmp[key].length == 0)) {
			continue;
		}

		var hidden = $(document.createElement("input"));
		hidden.attr("type", "hidden");
		hidden.attr("name", key);
		hidden.val(paramDataTmp[key]);
		form.append(hidden);
	}

	form.submit();
}

// 設定
function onF4(){
	openDetailDispSettingDialog('detailDisp', '0201', 1);
}

// 担当者検索
function openUserSearchDialog(type){

	// 担当者検索ダイアログを開く
	openSearchUserDialog(type, setUserInfo );

	if(type == 1){
		// 担当者コードを設定する
		$("#"+type+"_userId").val($("#userId").val());
	}else if(type == 2){
		// 担当者名を設定する
		$("#"+type+"_nameKnj").val($("#userName").val());
	}
}

// 担当者情報設定
function setUserInfo(type, map) {
	if(type == 1) {
		$("#userId").val(map["userId"]);
	} else if(type == 2) {
		$("#userName").val(map["nameKnj"]);
	}
}


//顧客検索
function openCustomerSearchDialog(type){
	// 顧客検索ダイアログを開く
	openSearchCustomerDialog(type, setCustomerInfo );
	if(type == 1) {
		// 顧客コードを設定する
		$("#"+type+"_customerCode").val($("#customerCode").val());
	} else if(type == 2) {
		// 顧客名を設定する
		$("#"+type+"_customerName").val($("#customerName").val());
	}
}

//顧客情報設定
function setCustomerInfo(type, map) {
	if(type == 1) {
		// 顧客コードを設定する
		$("#customerCode").val(map["customerCode"]);
	} else if(type == 2) {
		// 顧客名を設定する
		$("#customerName").val(map["customerName"]);
	}
}

// ソート
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

	// 1回以上検索しており、前回の結果が1件以上ある場合のみ再検索
	if(paramDataTmp != null && $("#searchResultCount").val() != "0") {
		// 前回の検索条件からソート条件のみを変更
		paramData = paramDataTmp;
		paramData["sortColumn"] = $("#sortColumn").val();
		paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
		paramData["pageNo"] = "1";
		// 検索
		execSearch(paramData);
	}
}


//ページ繰り、ソートによる検索処理
function goPage(no){

	// 検索条件を設定する
	paramData = paramDataTmp;
	paramData["pageNo"] = no;
	// 検索を実行する
	execSearch(paramData);

}

function createParamData(){
	// リクエストデータ作成
	paramData = new Object();

	if($("#estimateSheetId").val()) {
		paramData["estimateSheetId"] = $("#estimateSheetId").val();
	}
	if($("#estimateDateFrom").val()) {
		paramData["estimateDateFrom"] = $("#estimateDateFrom").val();
	}
	if($("#estimateDateTo").val()) {
		paramData["estimateDateTo"] = $("#estimateDateTo").val();
	}
	if($("#validDateFrom").val()) {
		paramData["validDateFrom"] = $("#validDateFrom").val();
	}
	if($("#validDateTo").val()) {
		paramData["validDateTo"] = $("#validDateTo").val();
	}
	if($("#userId").val()) {
		paramData["userId"] = $("#userId").val();
	}
	if($("#userName").val()) {
		paramData["userName"] = $("#userName").val();
	}
	if($("#title").val()) {
		paramData["title"] = $("#title").val();
	}
	if($("#remarks").val()) {
		paramData["remarks"] = $("#remarks").val();
	}
	if($("#submitName").val()) {
		paramData["submitName"] = $("#submitName").val();
	}
	if($("#customerCode").val()) {
		paramData["customerCode"] = $("#customerCode").val();
	}
	if($("#customerName").val()) {
		paramData["customerName"] = $("#customerName").val();
	}

	// ページあたりの表示件数
	if($("#rowCount").val()) {
		paramData["rowCount"] = $("#rowCount").val();
	}

	// ソート列
	if($("#sortColumn").val()) {
		paramData["sortColumn"] = $("#sortColumn").val();
	}
	// ソート昇順フラグ
	if($("#sortOrderAsc").val()) {
		paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
	}

	return paramData;
}

//検索実行
function execSearch(paramData){

	// Ajaxリクエストによって検索結果をロードする
	asyncRequest(
		contextRoot + "/ajax/estimate/searchEstimateResultAjax/search",
		paramData,
		function(data) {
			// 検索結果テーブルを更新する
			$("#errors").empty();
			$("#ListContainer").empty();
			$("#ListContainer").append(data);

			// 1件以上ヒットした場合
			if($("#searchResultCount").val() != "0") {
				// 検索条件を保持
				paramDataTmp = paramData;

				// EXCELボタンの状態変更
				$("#btnF3").attr("disabled", false);
			} else {
				// EXCELボタンの状態変更
				$("#btnF3").attr("disabled", true);
			}
		},
		function(xmlHttpRequest, textStatus, errorThrown) {
			$("#errors").empty();
			if (xmlHttpRequest.status == 450) {
				// 検索条件エラー
				$("#errors").append(xmlHttpRequest.responseText);
			} else if (xmlHttpRequest.status == 401) {
				// 未ログイン
				alert(xmlHttpRequest.responseText);
				window.location.doHref(contextRoot + "/login");
			} else {
				// その他のエラー
				alert(xmlHttpRequest.responseText);
			}
		}
	);

}


-->
</script>
</head>
<body onload="init()">
	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0002"/>
		<jsp:param name="MENU_ID" value="0201"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">

		<!-- タイトル -->
		<span class="title"><bean:message key='titles.searchEstimate'/></span>

		<div class="function_buttons">
			<button id="btnF1" type="button" onclick="onF1();" tabindex="2000">F1<br><bean:message key='words.action.initialize'/></button>
			<button id="btnF2" type="button" onclick="onF2();" tabindex="2001">F2<br><bean:message key='words.action.search'/></button>
			<button id="btnF3" type="button" onclick="onF3();" disabled="disabled" tabindex="2002">F3<br><bean:message key='words.name.excel'/></button>
			<button id="btnF4" type="button" onclick="onF4();" tabindex="2003">F4<br><bean:message key='words.action.setting'/></button>
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

		<s:form style="margin:0px; padding:0px;" onsubmit="return false;">

		<div class="function_forms">
		<div id="errors" style="color: red">
			<html:errors />
		</div>

	    <div class="form_section_wrap">
	    <div class="form_section">
	    	<div class="section_title">
				<span><bean:message key='labels.searchCondition'/></span>
	            <button class="btn_toggle" />
			</div><!-- /.section_title -->

			<div id="search_info" class="section_body">
				<table id="order_info1" class="forms" summary="見積伝票情報" style="width: auto;">
					<tr>
						<th><div class="col_title_right"><bean:message key='labels.estimateSheetId'/></div></th> <!-- 見積番号 -->
						<td><html:text property="estimateSheetId" styleId="estimateSheetId" tabindex="100" style="ime-mode:disabled;" /></td>
						<th><div class="col_title_right"><bean:message key='labels.estimateDate'/></div></th> <!-- 見積日 -->
						<td style="padding-right: 0;">
							<div class="pos_r" style="padding-right: 0; margin-right: 0;">
								<html:text property="estimateDateFrom" styleId="estimateDateFrom"  styleClass="date_input" style="width: 135px; ime-mode:disabled;" tabindex="101" />
							</div>
						</td>
						<td style="text-align: center; width:30px; padding-right: 0;">
							<bean:message key='labels.betweenSign'/> <!-- ～ -->
						</td>
						<td colspan="1" >
							<div class="pos_r">
								<html:text property="estimateDateTo" styleId="estimateDateTo" styleClass="date_input"  style="width: 135px; ime-mode:disabled;" tabindex="102" />
							</div>
						</td>
					</tr>
				</table>
				<table id="order_info2" class="forms" summary="見積伝票情報" style="width: auto;">
					<tr>
						<th><div class="col_title_right"><bean:message key='labels.validDate'/></div></th> <!-- 有効期限 -->
						<td style="padding-right: 0;">
							<div class="pos_r" style="padding-right: 0; margin-right: 0;">
								<html:text property="validDateFrom" styleId="validDateFrom" styleClass="date_input" style="width: 135px; ime-mode:disabled;" tabindex="104" />
							</div>
						</td>
						<td style="text-align: center; width:30px; padding-right: 0;">
							<bean:message key='labels.betweenSign'/> <!-- ～ -->
						</td>
						<td colspan="1" >
							<div class="pos_r">
								<html:text property="validDateTo" styleId="validDateTo" styleClass="date_input" style="width: 135px; ime-mode:disabled;" tabindex="105" />
							</div>
						</td>
					</tr>
				</table>
				<table id="order_info3" class="forms" summary="見積伝票情報">
					<tr>
						<th><div class="col_title_right"><bean:message key='labels.tantou.userId'/></div></th> <!-- 入力担当者コード -->
						<td><html:text property="userId" styleId="userId" style="ime-mode:disabled;" tabindex="106" />
						<html:image src='${f:url("/images//customize/btn_search.png")}'   onclick="openUserSearchDialog(1)" style="vertical-align: middle; cursor: pointer;ime-mode:disabled;" tabindex="107" /></td>
						<th><div class="col_title_right"><bean:message key='labels.tantou.userName'/></div></th> <!-- 入力担当者名 -->
						<td><html:text property="userName" styleId="userName"  tabindex="108" />
						<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;"  onclick="openUserSearchDialog(2)" tabindex="109" /></td>
					</tr>
					<tr>
						<th><div class="col_title_right"><bean:message key='labels.title'/></div></th> <!-- 件名 -->
						<td><html:text property="title" styleId="title" style="width: 320px" tabindex="110" /></td>
						<th><div class="col_title_right"><bean:message key='labels.tekiyou'/></div></th> <!-- 摘要 -->
						<td><html:text property="remarks" styleId="remarks" style="width: 320px" tabindex="111" /></td>
					</tr>
					<tr>
						<th><div class="col_title_right"><bean:message key='labels.submitName'/></div></th> <!-- 提出先名 -->
						<td colspan="3"><html:text property="submitName" styleId="submitName"  style="width: 700px"  tabindex="112"/></td>
					</tr>
					<tr>
							<th><div class="col_title_right"><bean:message key='labels.customerCode'/></div></th> <!-- 顧客コード -->
						<td><html:text property="customerCode" styleId="customerCode" style="width: 130px;ime-mode:disabled;" tabindex="113" />
							<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(1)" tabindex="114" /></td>
							<th><div class="col_title_right"><bean:message key='labels.customerName'/></div></th> <!-- 顧客名 -->
						<td>
							<html:text property="customerName" styleId="customerName" style="width: 320px;" tabindex="115" />
							<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(2)" tabindex="116" /></td>
					</tr>
				</table>
			</div>
    	</div><!-- /.form_section -->
    	</div><!-- /.form_section_wrap -->
		</div>
		<html:hidden property="sortColumn" styleId="sortColumn" />
		<html:hidden property="sortOrderAsc" styleId="sortOrderAsc" />
		</s:form>

		<form name="OutputForm" action="${f:url('/estimate/searchEstimateResultOutput/excel')}" target="_blank" style="display: none;" method="POST">
		</form>

		<div style="width: 1160px; text-align: right">
			<button type="button" onclick="onF1();" tabindex="450" class="btn_medium"><bean:message key='words.action.initialize'/></button> <!-- 初期化 -->
			<button type="button" onclick="onF2();" tabindex="451" class="btn_medium"><bean:message key='words.action.search'/></button> <!-- 検索 -->
		</div>
		<span id="ListContainer">
			<%@ include file="/WEB-INF/view/ajax/estimate/searchEstimateResultAjax/result.jsp" %>
		</span>
	</div>
</body>
</html>
