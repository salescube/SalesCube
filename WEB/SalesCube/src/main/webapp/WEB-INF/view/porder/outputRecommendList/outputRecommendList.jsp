<%@page pageEncoding="UTF-8"%>
<%@page import="jp.co.arkinfosys.common.Constants" %>
<%@page import="jp.co.arkinfosys.common.CategoryTrns" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html lang="ja">
<head>
	<title><bean:message key='titles.system'/> <bean:message key='titles.outputRecommendList'/></title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

<script type="text/javascript">
<!--

//ロード時の関数読込み
window.onload = init;

var tempData = null;

//メインフォームの名前
var MainFormName = "porder_outputRecommendListActionForm";
//メインフォームのデフォルトのアクション保持用
var MainDefaultAction;
// 数量の丸め（桁=自社マスタ.数量少数桁、丸め=自社マスタ.商品端数処理）
var quantityAlignment = ${mineDto.numDecAlignment};
var quantityCategory = ${mineDto.productFractCategory};
// 粗利益率の丸め（桁=自社マスタ.統計少数桁、丸め=四捨五入固定）
var rateAlignment = ${mineDto.statsDecAlignment};
var rateCategory = scale_half_up;

//フォームのアクションをいじってからサブミットする。
function ActionSubmit(FormName,DefaultActionName,ActionName){
	showNowSearchingDiv();
	$("form[name='" + FormName + "']").attr("action",DefaultActionName + ActionName);
	$("form[name='" + FormName + "']").submit();
	$("form[name='" + FormName + "']").attr("action",DefaultActionName);
}

//フォームのアクションをいじってからサブミットする。(EXCEL出力の別画面へのサブミット専用バージョン)
function ActionSubmitExcel(){
	var excelForm = $(document.createElement("form"));
	$("body").append(excelForm);
	excelForm.attr("action", contextRoot + "/ajax/outputRecommendListAjax/excel");
	excelForm.attr("target","<bean:message key='words.name.excel'/>");
	excelForm.attr("method","POST");
	excelForm.attr("id","ExcelForm");

	for (var key in tempData) {
		var hidden = $(document.createElement("input"));
		hidden.attr("type", "hidden");
		hidden.attr("name", key);
		hidden.val(tempData[key]);
		excelForm.append(hidden);
	}

	if((tempData["poCategory"] == "<%=CategoryTrns.IMMEDIATELY_PORDER%>" &&
			tempData["immediatelyPOCategory"] == "<%=CategoryTrns.NORMAL_PORDER%>") ||
			tempData["poCategory"] == "<%=CategoryTrns.ENTRUST_PORDER%>") {
		$("input[id^='searchResultList'][id$='productCode']").each(
			function() {
				var hidden = $(document.createElement("input"));
				hidden.attr("type", "hidden");
				hidden.attr("name", "searchResultInputProductCodeArray");
				hidden.val(this.value);
				excelForm.append(hidden);
			}
		);
		$("input[id^='searchResultList'][id$='validRow']").each(
			function() {
				var hidden = $(document.createElement("input"));
				hidden.attr("type", "hidden");
				hidden.attr("name", "searchResultInputOrderCheckStatusArray");
				hidden.val(this.value);
				excelForm.append(hidden);
			}
		);
		$("input[id^='searchResultList'][id$='pOrderQuantity']").each(
			function() {
				var hidden = $(document.createElement("input"));
				hidden.attr("type", "hidden");
				hidden.attr("name", "searchResultInputOrderQuantityArray");
				hidden.val(this.value);
				excelForm.append(hidden);
			}
		);
	}

	$("#ExcelForm").submit();
	$("#ExcelForm").remove();
}

//ページ読込時の動作
function init() {

	//デフォルトアクションの取得
	MainDefaultAction = $("form[name='" + MainFormName + "']").attr("action");

	// 発注エラー時の画面項目状態復元
	poCategoryChange();

	if( parseInt($("#searchResultCount").val()) > 0 ) {
		// ボタン状態復元
		$("#btnF4").attr("disabled", false);
		if(($("#searchPOCategory").val() == "<%=CategoryTrns.IMMEDIATELY_PORDER%>"
				&& $("#searchImmediatelyPOCategory").val() == "<%=CategoryTrns.NORMAL_PORDER%>")
				|| $("#searchPOCategory").val() == "<%=CategoryTrns.ENTRUST_PORDER%>") {
			$("#btnF3").attr("disabled", false);
			$("#btnPOrder").attr("disabled", false);
			$("#allCheck").attr("disabled", false);
			$("#allUnCheck").attr("disabled", false);
		}

		// 前回検索条件の復元
		tempData = new Object();
		tempData["supplierCode"] = $("#searchSupplierCode").val();
		tempData["poCategory"] = $("#searchPOCategory").val();
		tempData["immediatelyPOCategory"] = $("#searchImmediatelyPOCategory").val();
		tempData["excludeHoldingStockZero"] = $("#searchExcludeHoldingStockZero").val();
		tempData["excludeAvgShipCountZero"] = $("#searchExcludeAvgShipCountZero").val();
		tempData["excludeAvgLessThanHoldingStock"] = $("#searchExcludeAvgLessThanHoldingStock").val();
		tempData["sortColumn"] = $("#searchSortColumn").val();
		tempData["sortOrderAsc"] = $("#searchSortOrderAsc").val();
	}

	//初期フォーカス
	$("#supplierCode").focus();

}

//検索ボタン
function onSearch(){
	if(confirm('<bean:message key="confirm.reSearchRecommendList" />')){
		searchMain(false);
	}
}

//検索処理メイン
function searchMain(isSort, optData){
	var data = null;
	var clearSortColumn = false;
	if(optData) {
		data = optData;

		// ソート条件
		data["sortColumn"] = $("#sortColumn").val();
		data["sortOrderAsc"] = $("#sortOrderAsc").val();
	}
	else {
		data = new Object();

		// 検索条件
		data["supplierCode"] = $("#supplierCode").val();
		data["poCategory"] = $("#poCategory").val();
		data["immediatelyPOCategory"] = $("#immediatelyPOCategory").val();

		// 除外条件
		data["excludeHoldingStockZero"] = $("#excludeHoldingStockZero").attr("checked");
		data["excludeAvgShipCountZero"] = $("#excludeAvgShipCountZero").attr("checked");
		data["excludeAvgLessThanHoldingStock"] = $("#excludeAvgLessThanHoldingStock").attr("checked");

		// ソート条件
		data["sortColumn"] = "";
		data["sortOrderAsc"] = true;
	}

	//ソート前の入力値を保持する
	var searchResultInputProductCodeArray = new Array();
	var searchResultInputOrderCheckStatusArray = new Array();
	var searchResultInputOrderQuantityArray = new Array();

	$("input[id^='searchResultList'][id$='productCode']").each(
		function() {
			searchResultInputProductCodeArray.push(this.value);
		}
	);
	var validRow = $("input[id^='searchResultList'][id$='validRow']").each(
		function() {
			searchResultInputOrderCheckStatusArray.push($(this).attr("checked"));
		}
	);
	var pOrderQuantity = $("input[id^='searchResultList'][id$='pOrderQuantity']").each(
		function() {
			searchResultInputOrderQuantityArray.push(this.value);
		}
	);

	//現在の入力値をFormに追加する
	data["searchResultInputOrderCheckStatusArray"] = searchResultInputOrderCheckStatusArray;
	data["searchResultInputProductCodeArray"] = searchResultInputProductCodeArray;
	data["searchResultInputOrderQuantityArray"] = searchResultInputOrderQuantityArray;

	// 検索条件を保持する
	tempData = data;

	$("#supplierName").val($("#supplierCode option:selected").html());

	//入力された値を、ソート前後で保持するために、ソートか通常の検索かで別メソッドを実行する
	var destRequestURL = "/ajax/outputRecommendListAjax/";
	if(isSort &&
			(data["poCategory"] == "<%=CategoryTrns.IMMEDIATELY_PORDER%>"
				&& data["immediatelyPOCategory"] == "<%=CategoryTrns.NORMAL_PORDER%>")
				|| data["poCategory"] == "<%=CategoryTrns.ENTRUST_PORDER%>") {
		destRequestURL += "sortSearch";
	}
	else {
		destRequestURL += "search";
	}

	// Ajaxリクエストによって検索結果をロードする
	asyncRequest(
			contextRoot + destRequestURL,
			data,
			function(data) {
				// 検索結果テーブルを更新する
				$("#listContainer").empty();
				$("#listContainer").html(data);

				// 発注ボタン・Excelボタンの制御
				$("#btnF3").attr("disabled", true);
				$("#btnF4").attr("disabled", true);
				$("#btnPOrder").attr("disabled", true);

				// 全て選択・全て解除ボタンの制御
				$("#allCheck").attr("disabled", true);
				$("#allUnCheck").attr("disabled", true);

				if( parseInt($("#searchResultCount").val()) > 0 ) {
					$("#btnF4").attr("disabled", false);
					if( ($("#searchPOCategory").val() == "<%=CategoryTrns.IMMEDIATELY_PORDER%>" &&
						 $("#searchImmediatelyPOCategory").val() == "<%=CategoryTrns.NORMAL_PORDER%>" )
						 || $("#searchPOCategory").val() == "<%=CategoryTrns.ENTRUST_PORDER%>") {
						$("#btnF3").attr("disabled", false);
						$("#btnPOrder").attr("disabled", false);
						$("#allCheck").attr("disabled", false);
						$("#allUnCheck").attr("disabled", false);
					}
				}

				$(".BDCexecTarget").attBDC();
				applyNumeralStylesToObj(quantityCategory,quantityAlignment,$(".num"));
				applyNumeralStylesToObj(rateCategory,rateAlignment,$(".per"));
				_after_load($(".num"));
				_after_load($(".per"));

				rebindNC();
				var target = $("input:visible.EnableTabindex[tabindex>0]");
				if (target.size() > 0) {
					target.bind('keypress', 'return', move_focus_to_next_tabindex );
				}
			},
			null
	);
}

// 初期化
function onF1(){
	// 入力内容を初期化してよろしいですか？
	if(confirm('<bean:message key="confirm.init" />')){
		window.location.doHref('${f:url("/porder/outputRecommendList")}');
	}
}

//検索
function onF2(){
	onSearch();
}

// 発注
function onF3(){
	if(!confirm('<bean:message key="confirm.porder" />')){return;}
	$(".BDCexecTarget").rmvBDC();
	_before_submit($(".numeral_commas"));
	ActionSubmit(MainFormName,MainDefaultAction,"order");
}

// EXCEL
function onF4(){
	// 検索結果をEXCELファイルでダウンロードしますか？
	if(!confirm('<bean:message key="confirm.excel.result" />')){
		return;
	}

	_before_submit($(".numeral_commas"));
	ActionSubmitExcel();
}

// 全て選択・全て解除
function checkAll(flg){

	$("#listContainer").find("input[type='checkbox']").attr('checked', flg);
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
	// 1回以上検索しており、前回の結果が1件以上ある場合のみ再検索
	if( tempData != null && parseInt($("#searchResultCount").val()) > 0) {
		// 検索
		searchMain(true, tempData);
	}
}

/**
 * 発注区分が変更された場合の処理
 */
function poCategoryChange() {
	if("<%=CategoryTrns.IMMEDIATELY_PORDER%>" == $("#poCategory").val()) {
		$("#immediatelyPOCategory").attr("disabled", false);
		$("#immediatelyPOCategory").removeClass("c_disable");
		immediatelyPOCategoryChange();
	}
	else if("<%=CategoryTrns.ENTRUST_PORDER%>" == $("#poCategory").val()){
		$("#deliveryDate").datepicker("enable");
		$("#deliveryDate").removeClass("c_disable");

		$("#immediatelyPOCategory").attr("disabled", true);
		$("#transportCategory").attr("disabled", true);

		$("#immediatelyPOCategory").addClass("c_disable");
		$("#transportCategory").addClass("c_disable");
	}
}

/**
* 都度発注区分が変更された場合の処理
*/
function immediatelyPOCategoryChange() {
	if("<%=CategoryTrns.NORMAL_PORDER%>" == $("#immediatelyPOCategory").val()) {
		$("#immediatelyPOCategory").attr("disabled", false);
		$("#deliveryDate").datepicker("enable");
		$("#transportCategory").attr("disabled", false);

		$("#immediatelyPOCategory").removeClass("c_disable");
		$("#deliveryDate").removeClass("c_disable");
		$("#transportCategory").removeClass("c_disable");
	}
	else if("<%=CategoryTrns.MOVE_ENTRUST_STOCK%>" == $("#immediatelyPOCategory").val()){
		$("#deliveryDate").datepicker("disable");
		$("#transportCategory").attr("disabled", true);

		$("#deliveryDate").addClass("c_disable");
		$("#transportCategory").addClass("c_disable");
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
		<jsp:param name="PARENT_MENU_ID" value="0007"/>
		<jsp:param name="MENU_ID" value="0704"/>
	</jsp:include>

<!-- メイン機能  -->
<div id="main_function">

	<!-- タイトル -->
	<span class="title"><bean:message key='titles.outputRecommendList'/></span>

	<div class="function_buttons">
		<button id="btnF1" onclick="onF1()"                     tabindex="2000">F1<br><bean:message key='words.action.initialize'/></button><!--初期化-->
		<button id="btnF2" onclick="onF2()"                     tabindex="2001">F2<br><bean:message key='words.action.search'/></button><!---->
		<button id="btnF3" onclick="onF3()"                     tabindex="2002" disabled="disabled">F3<br><bean:message key='words.action.porder'/></button><!--発注-->
		<button id="btnF4" onclick="onF4()"                     tabindex="2003" disabled="disabled">F4<br><bean:message key='words.name.excel'/></button><!---->
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
	<div id="messages" style="color: white;">
		<html:messages id="msg" message="true">
			<bean:write name="msg" ignore="true"/><br>
		</html:messages>
	</div>

	<div class="form_section_wrap">
	<div class="form_section">
		<div class="section_title">
			<bean:message key='labels.searchCondition'/><!-- 検索条件 -->
			<button class="btn_toggle" />
		</div><!-- /.section_title -->

		<div id="search_info" class="section_body">
			<table class="forms" style="width: 800px">
				<colgroup>
					<col span="1" style="width: 15%">
					<col span="1" style="width: 35%">
					<col span="1" style="width: 10%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 10%">
					<col span="1" style="width: 15%">
				</colgroup>
				<tbody>
				<tr>
					<th><div class="col_title_right"><bean:message key='labels.supplier'/></div></th><!-- 仕入先名 -->
					<td>
						<html:select styleId="supplierCode" property="supplierCode" tabindex="100">
							<c:forEach var="bean" items="${supplierList}">
								<html:option value="${bean.value}">${f:h(bean.label)}</html:option>
							</c:forEach>
						</html:select>
						<input type="hidden" id="lastSelectedSupplierCode"/>
						<html:hidden styleId="supplierName" property="supplierName"/>
					</td>
					<th><div class="col_title_right"><bean:message key='labels.poCategory'/></div></th><!-- 発注区分 -->
					<td>
						<html:select styleId="poCategory" property="poCategory" tabindex="100" onchange="poCategoryChange();">
							<c:forEach var="bean" items="${poCategoryList}">
								<html:option value="${bean.value}">${f:h(bean.label)}</html:option>
							</c:forEach>
						</html:select>
					</td>
					<th><div class="col_title_right"><bean:message key='labels.immediatelyPOCategory'/></div></th><!-- 都度発注区分 -->
					<td>
						<html:select styleId="immediatelyPOCategory" property="immediatelyPOCategory" tabindex="100" onchange="immediatelyPOCategoryChange();">
							<c:forEach var="bean" items="${immediatelyPOCategoryList}">
								<html:option value="${bean.value}">${f:h(bean.label)}</html:option>
							</c:forEach>
						</html:select>
					</td>
				</tr>
				<tr>
					<th><div class="col_title_right"><bean:message key='labels.excludeProductsSearchCondition'/></div></th>
					<td colspan="5">
						<html:checkbox property="excludeHoldingStockZero" styleId="excludeHoldingStockZero" tabindex="200"/><label for="excludeHoldingStockZero"><bean:message key='labels.exclude.holdingStockZero'/></label><br><%// 保有数0の商品は除く %>
						<html:checkbox property="excludeAvgShipCountZero" styleId="excludeAvgShipCountZero" tabindex="201"/><label for="excludeAvgShipCountZero"><bean:message key='labels.exclude.avgShipCountZero'/></label><br><%// 平均出荷数0の商品は除く %>
						<html:checkbox property="excludeAvgLessThanHoldingStock" styleId="excludeAvgLessThanHoldingStock" tabindex="202"/><label for="excludeAvgLessThanHoldingStock"><bean:message key='labels.exclude.avgLessThanHoldingStock'/></label><br><%// 保有数 > 平均出荷数 となる商品は除く %>
					</td>
				</tr>
				</tbody>
			</table>
		</div>
	</div><!-- /.form_section -->
   	</div><!-- /.form_section_wrap -->


	<div>
	<div style="width: 1160px; text-align :right">
		<button onclick="onSearch()" class="btn_medium"><bean:message key='words.action.search'/></button>
	</div>


	<div class="form_section_wrap">
	<div class="form_section">
		<div class="section_title">
			<bean:message key='labels.poSlipInfos'/><!-- 発注伝票情報 -->
			<button class="btn_toggle" />
		</div><!-- /.section_title -->

		<div id="search_info" class="section_body">
			<table class="forms" style="width: 500px">
				<colgroup>
					<col span="1" style="width: 20%">
					<col span="1" style="width: 30%">
					<col span="1" style="width: 20%">
					<col span="1" style="width: 30%">
				</colgroup>
				<tbody>
				<tr>
				<th><div class="col_title_right_req"><bean:message key='labels.deliveryDate'/><bean:message key='labels.must'/></div></th><!-- 納期 -->
				<td><html:text styleId="deliveryDate" property="deliveryDate" maxlength="${f:h(ML_DATE)}" styleClass="date_input" style="width:135px;" tabindex="101"/></td>
				<th><div class="col_title_right"><bean:message key='labels.transportCategory'/></div></th><!-- 運送便区分 -->
				<td>
					<html:select styleId="transportCategory" property="transportCategory" tabindex="102">
						<c:forEach var="bean" items="${transportCategoryList}">
							<html:option value="${bean.value}">${f:h(bean.label)}</html:option>
						</c:forEach>
					</html:select>
				</td>
				</tr>
				</tbody>
			</table>
		</div>
	</div><!-- /.form_section -->
   	</div><!-- /.form_section_wrap -->

	<div>
		<table>
			<tr>
				<td><bean:message key='labels.holdQuantity'/></td><!-- 保有数 -->
				<td><bean:message key='labels.formula.holdQuantity'/></td><!-- ：現在庫数 ＋ 委託在庫数 ＋ 発注残数 ＋ 委託残数 － 受注残数 -->
			</tr>
			<tr>
				<td><bean:message key='labels.holdTerm'/></td><!-- 保有月数 -->
				<td><bean:message key='labels.formula.holdTerm'/></td><!-- ：保有数 ÷ 平均出庫数 -->
			</tr>
		</table>
	</div>
</div>

<button id="allCheck" name="allCheck" tabindex="300" onclick="checkAll(true)" class="btn_list_action" disabled="disabled">全て選択</button>
<button id="allUnCheck" name="allUnCheck" tabindex="301" onclick="checkAll(false)" class="btn_list_action" disabled="disabled">全て解除</button>

<html:hidden property="sortColumn" styleId="sortColumn" />
<html:hidden property="sortOrderAsc" styleId="sortOrderAsc" />

<span id="listContainer">
	<%-- 検索結果領域 --%>
	<%@ include file="/WEB-INF/view/ajax/outputRecommendListAjax/searchResultList.jsp" %>
</span>

<div style="width: 1160px; text-align :right; margin-top: 10px;">
	<button id="btnPOrder" onclick="onF3()" tabindex="1999" disabled="disabled" class="btn_medium"><bean:message key='words.action.porder'/></button><!-- 発注 -->
</div>

</s:form>

</div>

</body>

</html>
