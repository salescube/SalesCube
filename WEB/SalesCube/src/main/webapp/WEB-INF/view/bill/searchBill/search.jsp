<%@page import="jp.co.arkinfosys.common.Constants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="jp.co.arkinfosys.common.CategoryTrns"%>
<%@page import="jp.co.arkinfosys.common.Constants"%>
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><bean:message key='titles.system'/> <bean:message key='titles.searchBill'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

<script type="text/javascript">
<!--

var paramData = null;
var paramDataTmp = null;
var data = null;

//ページ読込時の動作
function init() {
	$("#billCrtCategory").val("<%=CategoryTrns.BILL_CRT_BILL%>");
	changeBillCrt();

	// 初期フォーカス
	$('#billId').focus();

	$("#customerCode").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限
}


// 初期化
function onF1(){
	// 入力内容を初期化してよろしいですか？
	if(confirm('<bean:message key="confirm.init" />')){
		location.doHref('${f:url("/bill/searchBill")}');
	}
}

// 検索ボタンによる検索処理
function onF2() {

	paramData = createParamData();
	paramData["pageNo"] = 1;

	execSearch(paramData);
	return;
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
	openDetailDispSettingDialog('detailDisp', '0500', 1);
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


	if($("#billId").val()) {
		paramData["billId"] = $("#billId").val();
	}
	if($("#billCrtCategory").val()) {
		paramData["billCrtCategory"] = $("#billCrtCategory").val();
	}
	if($("#lastPrintDateFrom").val()) {
		paramData["lastPrintDateFrom"] = $("#lastPrintDateFrom").val();
	}
	if($("#lastPrintDateTo").val()) {
		paramData["lastPrintDateTo"] = $("#lastPrintDateTo").val();
	}
	if($("#billCutoffDateFrom").val()) {
		paramData["billCutoffDateFrom"] = $("#billCutoffDateFrom").val();
	}
	if($("#billCutoffDateTo").val()) {
		paramData["billCutoffDateTo"] = $("#billCutoffDateTo").val();
	}
	if($("#lastSalesDateFrom").val()) {
		paramData["lastSalesDateFrom"] = $("#lastSalesDateFrom").val();
	}
	if($("#lastSalesDateTo").val()) {
		paramData["lastSalesDateTo"] = $("#lastSalesDateTo").val();
	}
	if($("#cutoffGroupCategory").val()) {
		paramData["cutoffGroupCategory"] = $("#cutoffGroupCategory").val();
	}
	if($("#customerCode").val()) {
		paramData["customerCode"] = $("#customerCode").val();
	}
	if($("#customerName").val()) {
		paramData["customerName"] = $("#customerName").val();
	}
	if($("#covPriceZero").attr('disabled') == false && $("#covPriceZero").attr('checked') == true) {
		paramData["covPriceZero"] = $("#covPriceZero").val();
	}
	if($("#covPriceMinus").attr('disabled') == false && $("#covPriceMinus").attr('checked') == true) {
		paramData["covPriceMinus"] = $("#covPriceMinus").val();
	}
	if($("#covPricePlus").attr('disabled') == false && $("#covPricePlus").attr('checked') == true) {
		paramData["covPricePlus"] = $("#covPricePlus").val();
	}
	if($("#thisBillPricePlus").attr('disabled') == false && $("#thisBillPricePlus").attr('checked') == true) {
		paramData["thisBillPricePlus"] = $("#thisBillPricePlus").val();
	}
	if($("#thisBillPriceZero").attr('disabled') == false && $("#thisBillPriceZero").attr('checked') == true) {
		paramData["thisBillPriceZero"] = $("#thisBillPriceZero").val();
	}
	if($("#thisBillPriceMinus").attr('disabled') == false && $("#thisBillPriceMinus").attr('checked') == true) {
		paramData["thisBillPriceMinus"] = $("#thisBillPriceMinus").val();
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
				contextRoot + "/ajax/bill/searchBillResultAjax/search",
				paramData,
				function(data) {
					$("#errors").empty();
					// 検索結果テーブルを更新する
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
				});
}

function changeBillCrt(){
	if( $("#billCrtCategory").val() == '<%=CategoryTrns.BILL_CRT_SALES%>' ){
		$("#billCutoffDateFrom").attr("disabled",true );
		$("#billCutoffDateTo").attr("disabled",true );

		$("#covPriceZero").attr("disabled",true );
		$("#covPriceMinus").attr("disabled",true );
		$("#covPricePlus").attr("disabled",true );
		$("#thisBillPricePlus").attr("disabled",true );
		$("#thisBillPriceZero").attr("disabled",true );
		$("#thisBillPriceMinus").attr("disabled",true );

		$("#billCutoffDateFrom").datepicker("disable");
		$("#billCutoffDateTo").datepicker("disable");
	}else{
		$("#billCutoffDateFrom").attr("disabled",false );
		$("#billCutoffDateTo").attr("disabled",false );

		$("#covPriceZero").attr("disabled",false );
		$("#covPriceMinus").attr("disabled",false );
		$("#covPricePlus").attr("disabled",false );
		$("#thisBillPricePlus").attr("disabled",false );
		$("#thisBillPriceZero").attr("disabled",false );
		$("#thisBillPriceMinus").attr("disabled",false );

		$("#billCutoffDateFrom").datepicker("enable");
		$("#billCutoffDateTo").datepicker("enable");
	}
}
-->
</script>
</head>
<body onload="init()" onhelp="return false;" >
	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0005"/>
		<jsp:param name="MENU_ID" value="0500"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">

		<!-- タイトル -->
		<span class="title"><bean:message key='titles.searchBill'/></span>

		<div class="function_buttons">
			<button id="btnF1" type="button" onclick="onF1();" tabindex="2000">F1<br><bean:message key='words.action.initialize'/></button><!-- 初期化-->
			<button id="btnF2" type="button" onclick="onF2();" tabindex="2001">F2<br><bean:message key='words.action.search'/></button><!-- 検索-->
			<button id="btnF3" type="button" onclick="onF3();" disabled="disabled" tabindex="2002">F3<br><bean:message key='words.name.excel'/></button><!-- EXCEL -->
			<button id="btnF4" type="button" onclick="onF4();" tabindex="2003">F4<br><bean:message key='words.action.setting'/></button><!-- 設定-->
			<button type="button" disabled="disabled">F5<br>&nbsp;</button><!-- -->
			<button type="button" disabled="disabled">F6<br>&nbsp;</button><!-- -->
			<button type="button" disabled="disabled">F7<br>&nbsp;</button><!-- -->
			<button type="button" disabled="disabled">F8<br>&nbsp;</button><!-- -->
			<button type="button" disabled="disabled">F9<br>&nbsp;</button><!-- -->
			<button type="button" disabled="disabled">F10<br>&nbsp;</button><!-- -->
			<button type="button" disabled="disabled">F11<br>&nbsp;</button><!-- -->
			<button type="button" disabled="disabled">F12<br>&nbsp;</button>
		</div>
		<br><br><br>

		<s:form onsubmit="return false;">

		<div class="function_forms">

			<!-- エラー情報 -->
			<div id="errors" style="color: red">
				<html:errors />
			</div>
			<div id="ajax_errors" style="color: red"></div>
			<div style="padding-left: 20px;color: blue;">
				<html:messages id="msg" message="true">
				<bean:write name="msg" ignore="true"/><br>
				</html:messages>
			</div>


			<div class="form_section_wrap">
				<div class="form_section">
					<div class="section_title">
						<span><bean:message key='labels.searchCondition'/></span><br>
						<button class="btn_toggle" />
					</div><!-- /.section_title -->

					<div id="search_info" class="section_body">
						<table id="order_info1" class="forms" summary="請求検索情報">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.billId'/></div></th> <!-- 請求書番号 -->
								<td><html:text property="billId" styleId="billId" tabindex="100" style="width: 100px; ime-mode:disabled;" /></td>
								<th ><div class="col_title_right"><bean:message key='labels.billCrtCategory'/></div></th> <!-- 請求書分類 -->
								<td>
									<html:select tabindex="101" property="billCrtCategory"  styleId="billCrtCategory" onchange="changeBillCrt()">
										<c:forEach var="dcl" items="${billCrtCategoryList}">
											<html:option value="${dcl.value}">${dcl.label}</html:option>
										</c:forEach>
									</html:select>
								</td>
								<td></td>
								<th><div class="col_title_right"><bean:message key='labels.cutoffGroupCategory'/></div></th> <!-- 支払条件 -->
								<td>
									<html:select tabindex="109" property="cutoffGroupCategory"  styleId="cutoffGroupCategory" >
										<c:forEach var="dcl" items="${cutoffGroupCategoryList}">
											<html:option value="${dcl.value}">${dcl.label}</html:option>
										</c:forEach>
									</html:select>
								</td>
							</tr>
						</table>
						<table id="order_info2" class="forms" summary="請求検索情報" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.billPrintDate'/></div></th> <!-- 請求書発行日 -->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="lastPrintDateFrom" styleId="lastPrintDateFrom"  styleClass="date_input" style="width: 175px; ime-mode:disabled;" tabindex="102" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td colspan="1" >
									<div class="pos_r">
										<html:text property="lastPrintDateTo" styleId="lastPrintDateTo" styleClass="date_input"  style="width: 175px; ime-mode:disabled;" tabindex="103" />
									</div>
								</td>
								<th><div class="col_title_right"><bean:message key='labels.billCutOffDate'/></div></th> <!-- 請求締日 -->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="billCutoffDateFrom" styleId="billCutoffDateFrom"  styleClass="date_input" style="width: 175px; ime-mode:disabled;" tabindex="104" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td colspan="1" >
									<div class="pos_r">
										<html:text property="billCutoffDateTo" styleId="billCutoffDateTo" styleClass="date_input"  style="width: 175px; ime-mode:disabled;" tabindex="105" />
									</div>
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.laseSalesDate'/></div></th> <!-- 最終売上日 -->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="lastSalesDateFrom" styleId="lastSalesDateFrom"  styleClass="date_input" style="width: 175px; ime-mode:disabled;" tabindex="104" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td colspan="1" >
									<div class="pos_r">
										<html:text property="lastSalesDateTo" styleId="lastSalesDateTo" styleClass="date_input"  style="width: 175px; ime-mode:disabled;" tabindex="105" />
									</div>
								</td>
							</tr>
						</table>
						<table id="order_info3" class="forms" summary="請求検索情報">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.customerCode'/></div></th> <!-- 顧客コード -->
								<td ><html:text property="customerCode" styleId="customerCode" style="width: 100px;ime-mode:disabled;" tabindex="200" />
									<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(1)" tabindex="201"/></td>
									<th><div class="col_title_right"><bean:message key='labels.customerName'/></div></th> <!-- 顧客名 -->
								<td colspan="4">
									<html:text property="customerName" styleId="customerName" style="width: 500px;" tabindex="202" />
									<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(2)" tabindex="203"/></td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.covPrice'/></div></th> <!-- 繰越金額-->
								<td>
									<html:checkbox property="covPriceZero" styleId="covPriceZero" value="<%=Constants.SEARCH_BILL.CARRY_OVER_ZERO %>"  tabindex="204" /><bean:message key='labels.notexist'/>&nbsp;&nbsp;
									<html:checkbox property="covPriceMinus" styleId="covPriceMinus" value="<%=Constants.SEARCH_BILL.CARRY_OVER_MINUS %>"  tabindex="205" /><bean:message key='labels.over'/>&nbsp;&nbsp;
									<html:checkbox property="covPricePlus" styleId="covPricePlus" value="<%=Constants.SEARCH_BILL.CARRY_OVER_PLUS %>"  tabindex="206" /><bean:message key='labels.less'/>&nbsp;&nbsp;
								</td>
								<th><div class="col_title_right"><bean:message key='labels.thisBillPrice'/></div></th> <!-- 今回請求金額 -->
								<td colspan="4">
									<html:checkbox property="thisBillPricePlus" styleId="thisBillPricePlus" value="<%=Constants.SEARCH_BILL.BILL_PRICE_PLUS %>"  tabindex="207" /><bean:message key='labels.exist'/>&nbsp;&nbsp;
									<html:checkbox property="thisBillPriceZero" styleId="thisBillPriceZero" value="<%=Constants.SEARCH_BILL.BILL_PRICE_ZERO %>"  tabindex="208" /><bean:message key='labels.notexist'/>&nbsp;&nbsp;
									<html:checkbox property="thisBillPriceMinus" styleId="thisBillPriceMinus" value="<%=Constants.SEARCH_BILL.BILL_PRICE_MINUS %>"  tabindex="209" /><bean:message key='labels.over'/>&nbsp;&nbsp;
								</td>
							</tr>
						</table>
					</div>
				</div><!-- /.form_section -->
	    	</div><!-- /.form_section_wrap -->
		</div>

		<html:hidden property="sortColumn" styleId="sortColumn" />
		<html:hidden property="sortOrderAsc" styleId="sortOrderAsc" />
		</s:form>

		<form name="OutputForm" action="${f:url('/bill/searchBillResultOutput/excel')}" target="_blank" style="display: none;" method="POST">
		</form>

		<div style="width: 1160px; text-align: right;">
			<button type="button" tabindex="250" onclick="onF1();" class="btn_medium"><bean:message key='words.action.initialize'/></button> <!-- 初期化 -->
			<button type="button" tabindex="251" onclick="onF2();"class="btn_medium"><bean:message key='words.action.search'/></button> <!-- 検索 -->
		</div>
		<span id="ListContainer">
			<%@ include file="/WEB-INF/view/ajax/bill/searchBillResultAjax/result.jsp" %>
		</span>

	</div>
</body>
</html>
