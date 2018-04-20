<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html lang="ja">
<head>
	<title><bean:message key='titles.system'/> <bean:message key='titles.makeOutPOrder'/></title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

<script type="text/javascript">
<!--

//ロード時の関数読込み
window.onload = init;
var checkData = new Object();
var paramDataTmp = null;

//ページ読込時の動作
function init() {
	$("#poSlipIdFrom").focus();
}

// 初期化
function onF1(){
	// 入力内容を初期化してよろしいですか？
	if(confirm('<bean:message key="confirm.init" />')){
		window.location.doHref('${f:url("/porder/makeOutPOrder")}');
	}
}
// 検索
function onF2(){

	// 検索条件を設定し検索を実行する
	var paramData = createParamData();
	paramData[ "pageNo" ] = 1;
	execSearch(paramData, false);
}

//PDF
function onF3(){
	pdf();
}

// 伝票IDリストを取得する
function createSlipIdListParam(){
	saveDisplayCheckState();

	var idList = new Array();
	for(var poSlipId in checkData) {
		idList.push( poSlipId );
	}

	data = new Object();
	data["slipIdList"] = idList;

	return data;
}

// PDF
function pdf(){
	if(!confirm('<bean:message key="confirm.makePOSlipPdf" />')){
		return;
	}

	var form = $(window.document.forms["PDFOutputForm"]);
	writeHiddenParam(form);
	form.submit();
}

/**
 * 帳票の選択状態をhidden項目としてレポート出力用のフォームに設定する
 */
function writeHiddenParam(form) {
	form.empty();

	var outputData = createSlipIdListParam();
	var slipIdList = outputData["slipIdList"];
	for(var i = 0; i < slipIdList.length; i++) {
		var hidden = $(document.createElement("input"));
		hidden.attr("type", "hidden");
		hidden.attr("name", "slipIdList");
		hidden.val(slipIdList[i]);
		form.append(hidden);
	}
}

// 検索パラメータの作成
function createParamData() {
	data = new Object();
	data["poSlipIdFrom"] = $("#poSlipIdFrom").val();
	data["poSlipIdTo"] = $("#poSlipIdTo").val();
	data["poDateFrom"] = $("#poDateFrom").val();
	data["poDateTo"] = $("#poDateTo").val();
	data["supplierCode"] = $("#supplierCode").val();
	data["supplierName"] = $("#supplierName").val();
	data["userName"] = $("#userName").val();
	data["rowCount"] = $("#rowCount").val();
	data["sortColumn"] = $("#sortColumn").val();
	data["sortColumnAsc"] = $("#sortOrderAsc").val();

	data["exceptAlreadyOutput"] = $("#exceptAlreadyOutput").attr("checked");
	return data;
}

// 検索実行
function execSearch(data, fromSaveState) {
	var url = contextRoot + "/ajax/porder/makeOutPOrderAjax/search";

	// Ajaxリクエストによって検索結果をロードする
	$("#errors").empty();
	asyncRequest(
			url,
			data,
			function(result) {
				// 検索結果テーブルを更新する
				$("#listContainer").empty();
				$("#listContainer").html(result);

				paramDataTmp = data;

				if(fromSaveState) {
					// 保存したチェック状態を復元する
					setDisplayCheckState();
					adjustCheckData();
				}
				else {
					// 全ページのチェック状態をリセットする
					checkData = new Object();
				}

				// ボタンの状態変更
				checkCount();
			}
	);
}

/**
 * 検索結果とチェック状態の突合
 */
function adjustCheckData() {
	var checkExists = false;
	for(var salesSlipId in checkData) {
		checkExists = true;
		break;
	}

	if(!checkExists) {
		return;
	}

	var doc = window.document;
	var slipIds = doc.getElementsByName("slipId");
	if(slipIds == null) {
		return;
	}

	var len = slipIds.length;
	if(len == null && slipIds.value != null) {
		var tempArray = new Object();
		tempArray.push(slipIds);
		slipIds = tempArray;
		len = 1;
	}

	var poSlipIdHash = new Object();
	for(var i = 0; i < slipIds.length; i++) {
		poSlipIdHash[ slipIds[ i ].value ] = true;
	}

	for(var poSlipId in checkData) {
		if(!poSlipIdHash[poSlipId]) {
			delete checkData[ poSlipId ];
		}
	}
}


// チェックの個数が0個の場合、Excelボタンを押下不可にする
function checkCount(){
	// 各ページのチェック状態を記憶する
	saveDisplayCheckState();

	for(var salesSlipId in checkData) {
		$("#btnF3").attr("disabled", false);
		return;
	}

	$("#btnF3").attr("disabled", true);
}

//全て選択
function checkAll(){
	$("input[type='checkbox'][id^='row_']").each(function(){
		$(this).attr("checked",true);
	});
	readAllPoSlipId();
	checkCount();
}

//全て解除
function checkNone(){
	$("input[type='checkbox'][id^='row_']").each(function(){
		$(this).attr("checked",false);
	});
	checkData = new Object();
	checkCount();
}

// 担当者検索
function openUserSearchDialog(){
	var id = "UserSearch";

	// 担当者検索ダイアログを開く
	openSearchUserDialog(id, setUserInfo );
	// 担当者コードを設定する
	$("#"+id+"_nameKnj").val($("#userName").val());
}

// 担当者情報設定
function setUserInfo(id, map) {
	$("#userName").val(map["nameKnj"]);
}

// 仕入先検索
function openSupplierSearchDialog(type){
	var id = "SupplierSearch";

	// 仕入先検索ダイアログを開く
	openSearchSupplierDialog(type, setSupplierInfo );
	if(type == 1) {
		// 仕入先コードを設定する
		$("#"+type+"_supplierCode").val($("#supplierCode").val());
	} else if(type == 2) {
		// 仕入先名を設定する
		$("#"+type+"_supplierName").val($("#supplierName").val());
	}
}

// 仕入先情報設定
function setSupplierInfo(type, map) {
	if(type == 1) {
		// 仕入先コードを設定する
		$("#supplierCode").val(map["supplierCode"]);
	} else if(type == 2) {
		// 仕入先名を設定する
		$("#supplierName").val(map["supplierName"]);
	}
}

// ソート
function sort(sortColumn) {
	// 各ページのチェック状態を記憶する
	saveDisplayCheckState();

	// 検索条件を設定する
	var paramData = paramDataTmp;
	if(paramData == null) {
		return;
	}

	// 前回のソートカラムとソート順を取得
	var beforeSortColumn = paramData["sortColumn"];
	var beforeSortOrderAsc = paramData["sortOrderAsc"];

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

	paramData["sortColumn"] = $("#sortColumn").val();
	paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
	paramData["pageNo"] = 1;

	execSearch(paramData, true);
}

//ページ繰り、ソートによる検索処理
function goPage(no){
	// 各ページのチェック状態を記憶する
	saveDisplayCheckState();

	// 検索条件を設定する
	paramData = paramDataTmp;
	paramData["pageNo"] = no;

	// 検索を実行する
	execSearch(paramData, true);

}

/**
 * 検索結果全体の発注伝票番号を読み込む(チェック状態で)
 */
function readAllPoSlipId() {
	var doc = window.document;
	var checkIds = doc.getElementsByName("slipId");
	if(checkIds != null) {
		var len = checkIds.length;
		if(len) {
			for(var i = 0; i < len; i++) {
				checkData[ checkIds[ i ].value ] = true;
			}
		}
		else {
			if( checkIds.value != null ) {
				checkData[ checkIds.value ] = true;
			}
		}
	}
}

/**
* 表示中ページのチェック状態を記憶する
*/
function saveDisplayCheckState() {
	$("input[type='checkbox'][id^='row_']").each(
		function(){
			if( $(this).attr("checked") == true ){
				checkData[ this.value ] = true;
			}
			else {
				// チェックされていないものは除外
				delete checkData[ this.value ];
			}
		}
	);
}

/**
* 画面内のチェックボックスのチェックに保存した状態を反映する
*/
function setDisplayCheckState() {
	$("input[type='checkbox'][id^='row_']").each(
		function(){
			$(this).attr("checked", false);

			if( checkData[ this.value ] ){
				$(this).attr("checked", true);
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
		<jsp:param name="PARENT_MENU_ID" value="0007"/>
		<jsp:param name="MENU_ID" value="0702"/>
	</jsp:include>

<!-- メイン機能  -->
<div id="main_function">

	<!-- タイトル -->
	<span class="title"><bean:message key='titles.makeOutPOrder'/></span>

	<div class="function_buttons">
		<button            onclick="onF1()"                     tabindex="2000">F1<br><bean:message key='words.action.initialize'/></button><!--初期化-->
		<button            onclick="onF2()"                     tabindex="2001">F2<br><bean:message key='words.action.search'/></button><!--検索-->
		<button id="btnF3" onclick="onF3()" disabled="disabled" tabindex="2002">F3<br><bean:message key='words.name.pdf'/></button><!--PDF-->
		<button                             disabled="disabled" tabindex="2003">F4<br><bean:message key='words.action.none'/></button><!---->
		<button                             disabled="disabled" tabindex="2004">F5<br><bean:message key='words.action.none'/></button><!---->
		<button                             disabled="disabled" tabindex="2005">F6<br><bean:message key='words.action.none'/></button><!---->
		<button                             disabled="disabled" tabindex="2006">F7<br><bean:message key='words.action.none'/></button><!---->
		<button                             disabled="disabled" tabindex="2007">F8<br><bean:message key='words.action.none'/></button><!---->
		<button                             disabled="disabled" tabindex="2008">F9<br><bean:message key='words.action.none'/></button><!---->
		<button                             disabled="disabled" tabindex="2009">F10<br><bean:message key='words.action.none'/></button><!---->
		<button                             disabled="disabled" tabindex="2010">F11<br><bean:message key='words.action.none'/></button><!---->
		<button                             disabled="disabled" tabindex="2011">F12<br><bean:message key='words.action.none'/></button>
	</div>
	<br><br><br>

	<s:form onsubmit="return false;">

	<div class="function_forms">

	<div id="errors" style="color: red">
		<html:errors/>
	</div>
	<div id="ajax_errors" style="color: red"></div>
	<div id="messages" style="color: blue;">
		<html:messages id="msg" message="true">
			<bean:write name="msg" ignore="true"/><br>
		</html:messages>
	</div>

	<div class="form_section_wrap">
	<div class="form_section">
		<div class="section_title">
			<span><bean:message key='labels.makePOSlipSearchCondition'/></span><br><!-- 検索条件 -->
			<button class="btn_toggle" />
		</div><!-- /.section_title -->

		<div id="search_info" class="section_body">
			<table id="search_info1" class="forms" style="width: auto;" summary="search_info1">
				<tr>
					<th><div class="col_title_right"><bean:message key='labels.poSlipId'/></div></th><!-- 発注番号 -->
					<td colspan="3">
						<html:text property="poSlipIdFrom" styleId="poSlipIdFrom" styleClass="c_ime_off" style="width:100px;" tabindex="100" /> <bean:message key='labels.betweenSign'/>
						<html:text property="poSlipIdTo" styleId="poSlipIdTo" styleClass="c_ime_off" style="width:100px;" tabindex="101" />
					</td>
					<th><div class="col_title_right"><bean:message key='labels.poDate'/></div></th><!-- 発注日 -->
					<td style="padding-right: 0;">
						<div class="pos_r">
							<html:text property="poDateFrom" styleId="poDateFrom" styleClass="date_input c_ime_off" style="text-align:center; width:135px;" tabindex="102" />
						</div>
					</td>
					<td style="text-align: center; width:30px; padding-right: 0;">
						<bean:message key='labels.betweenSign'/><!-- ～ -->
					</td>
					<td>
						<div class="pos_r">
							<html:text property="poDateTo" styleId="poDateTo" styleClass="date_input c_ime_off" style="text-align:center; width:135px;" tabindex="103" />
						</div>
					</td>
				</tr>
			</table>
			<table id="search_info2" class="forms" style="table-layout: fixed;" summary="search_info2">
				<tr>
					<th><div class="col_title_right"><bean:message key='labels.supplierCode'/></div></th><!-- 仕入先コード -->
					<td colspan="2">
						<html:text property="supplierCode" styleId="supplierCode" styleClass="c_ime_off" style="width: 100px;" tabindex="104" />
						<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSupplierSearchDialog(1)" tabindex="105" />
					</td>
					<th><div class="col_title_right"><bean:message key='labels.supplierName'/></div></th><!-- 仕入先名 -->
					<td colspan="4">
						<html:text property="supplierName" styleId="supplierName" style="width: 250px;" tabindex="106" />
						<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSupplierSearchDialog(2)" tabindex="107" />
					</td>

				</tr>

				<tr>
					<th><div class="col_title_right"><bean:message key='labels.userName'/></div></th><!-- 入力担当者 -->
					<td colspan="7">
						<html:text property="userName" styleId="userName" style="width:100px;" tabindex="108" />
						<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openUserSearchDialog()" tabindex="109" />
					</td>
				</tr>

				<tr>
					<th><div class="col_title_right"><bean:message key='labels.alreadyOutput'/></div></th><!-- 発行済み -->
					<td colspan="7">
						<html:checkbox property="exceptAlreadyOutput" styleId="exceptAlreadyOutput" tabindex="110" /><bean:message key='labels.exceptAlreadyOutput' /><!-- 発行済みを除く -->
					</td>
				</tr>

			</table>
		</div>
	</div><!-- /.form_section -->
   	</div><!-- /.form_section_wrap -->

	<div style="width: 1160px; text-align: right">
		<button onclick="onF1()" tabindex="150" style="" class="btn_medium"><bean:message key='words.action.initialize'/></button><!-- 初期化 -->
		<button onclick="onF2()" tabindex="151" style="" class="btn_medium"><bean:message key='words.action.search'/></button><!-- 検索 -->
	</div>
	</div>

	<html:hidden property="sortColumn" styleId="sortColumn" />
	<html:hidden property="sortOrderAsc" styleId="sortOrderAsc" />
</s:form>

		   <button onclick="checkAll()" tabindex="1000" class="btn_list_action">
				<bean:message key='words.action.selectAll'/>
		   </button><!--
		--><button onclick="checkNone()" tabindex="1001" class="btn_list_action">
				<bean:message key='words.action.selectNone'/>
		   </button>
		<span id="listContainer">
			<%-- 検索結果領域 --%>
			<%@ include file="/WEB-INF/view/ajax/porder/makeOutPOrderAjax/result.jsp" %>
		</span>

		<form name="ExcelOutputForm" action="${f:url('/porder/makeOutPOrderResultOutput/excel')}" style="display: none;" method="POST">
		</form>

		<form name="PDFOutputForm" action="${f:url('/porder/makeOutPOrderResultOutput/pdf')}" style="display: none;" method="POST">
		</form>

</div>

</body>

</html>
