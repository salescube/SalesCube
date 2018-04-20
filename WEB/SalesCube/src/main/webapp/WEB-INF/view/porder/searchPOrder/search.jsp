<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html lang="ja">
<head>
	<title><bean:message key='titles.system'/> <bean:message key='titles.poSearch'/></title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

<script type="text/javascript">
<!--

//ロード時の関数読込み
window.onload = init;

var paramData = null;
var paramDataTmp = null;

//ページ読込時の動作
function init() {
	$("#searchTarget").focus();

}

// 初期化
function onF1(){
	// 入力内容を初期化してよろしいですか？
	if(confirm('<bean:message key="confirm.init" />')){
		window.location.doHref('${f:url("/porder/searchPOrder")}');
	}
}
// 検索
function onF2(){

	paramData = createParamData();
	paramData["pageNo"] = 1;

	// 検索を実行する
	execSearch(paramData);
}

// EXCEL
function onF3(){
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
	openDetailDispSettingDialog('detailDisp', '0701', $('#searchTarget').val());
}

//ページ遷移
function goPage(no) {
	// 検索条件を設定する
	paramData = paramDataTmp;
	paramData["pageNo"] = no;
	// 検索を実行する
	execSearch(paramData);
}

// 検索パラメータの作成
function createParamData() {
	paramData = new Object();
	paramData["searchTarget"] = $("#searchTarget").val();
	paramData["poSlipId"] = $("#poSlipId").val();
	paramData["userName"] = $("#userName").val();
	paramData["poDateFrom"] = $("#poDateFrom").val();
	paramData["poDateTo"] = $("#poDateTo").val();
	paramData["deliveryDateFrom"] = $("#deliveryDateFrom").val();
	paramData["deliveryDateTo"] = $("#deliveryDateTo").val();
	paramData["remarks"] = $("#remarks").val();
	paramData["transportCategory"] = $("#transportCategory").val();
	paramData["onlyRestQuantityExist"] = $("#onlyRestQuantityExist").attr("checked");
	paramData["onlyUnpaid"] = $("#onlyUnpaid").attr("checked");

	paramData["supplierCode"] = $("#supplierCode").val();
	paramData["supplierName"] = $("#supplierName").val();
	paramData["supplierPcName"] = $("#supplierPcName").val();

	paramData["productCode"] = $("#productCode").val();
	paramData["productAbstract"] = $("#productAbstract").val();
	paramData["product1"] = $("#product1").val();
	paramData["product2"] = $("#product2").val();
	paramData["product3"] = $("#product3").val();

	paramData["rowCount"] = $("#rowCount").val();
	paramData["sortColumn"] = $("#sortColumn").val();
	paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
	return paramData;
}

// 検索実行
function execSearch(paramData) {
	// Ajaxリクエストによって検索結果をロードする
	asyncRequest(
			contextRoot + "/ajax/porder/searchPOrderResultAjax/search",
			paramData,
			function(data) {
				// 検索結果テーブルを更新する
				$("#listContainer").empty();
				$("#listContainer").append(data);

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
			null
	);
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

// 商品検索
function openProductSearchDialog(type){
	// 商品検索ダイアログを開く
	openSearchProductDialog(type, setProductInfo );
	if(type == 1) {
		// 商品コードを設定する
		$("#"+type+"_productCode").val($("#productCode").val());
	} else if(type == 2) {
		// 商品名を設定する
		$("#"+type+"_productName").val($("#productAbstract").val());
	}
}

// 商品情報設定
function setProductInfo(type, map) {
	if(type == 1) {
		// 商品コードを設定する
		$("#productCode").val(map["productCode"]);
	} else if(type == 2) {
		// 商品名を設定する
		$("#productAbstract").val(map["productName"]);
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
		<jsp:param name="MENU_ID" value="0701"/>
	</jsp:include>

<!-- メイン機能  -->
<div id="main_function">

	<!-- タイトル -->
	<span class="title"><bean:message key='titles.poSearch'/></span>

	<div class="function_buttons">
		<button            onclick="onF1()"                     tabindex="2000">F1<br><bean:message key='words.action.initialize'/></button><!--初期化-->
		<button            onclick="onF2()"                     tabindex="2001">F2<br><bean:message key='words.action.search'/></button><!--検索-->
		<button id="btnF3" onclick="onF3()" disabled="disabled" tabindex="2002">F3<br><bean:message key='words.name.excel'/></button><!--EXCEL-->
		<button            onclick="onF4()"                     tabindex="2003">F4<br><bean:message key='words.action.setting'/></button><!--設定-->
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
				<span><bean:message key='labels.searchCondition'/></span><br><!-- 検索条件 -->
				<button class="btn_toggle" />
			</div><!-- /.section_title -->

			<div id="search_info" class="section_body">
				<table id="search_info1" class="forms" summary="search_info1">
					<tr>
						<th><div class="col_title_right"><bean:message key='labels.searchTarget'/></div></th><!-- 検索対象 -->
						<td>
							<html:select property="searchTarget" styleId="searchTarget" tabindex="100">
								<html:options collection="searchTargetList" property="value" labelProperty="label"/>
							</html:select>
						</td>
						<th><div class="col_title_right"><bean:message key='labels.poSlipId'/></div></th><!-- 発注番号 -->
						<td>
							<html:text property="poSlipId" styleId="poSlipId" styleClass="c_ime_off" style="width:100px;" tabindex="101" />
						</td>
						<th><div class="col_title_right"><bean:message key='labels.userName'/></div></th><!-- 入力担当者 -->
						<td >
							<html:text property="userName" styleId="userName" style="width:100px;" tabindex="104" />
							<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" tabindex="105" onclick="openUserSearchDialog()"/>
						</td>
					</tr>
				</table>
				<table id="search_info2" class="forms" summary="search_info2" style="width: auto;">
					<tr>
						<th><div class="col_title_right"><bean:message key='labels.poDate'/></div></th><!-- 発注日 -->
						<td style="padding-right: 0;">
							<div class="pos_r">
								<html:text property="poDateFrom" styleId="poDateFrom" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="106" />
							</div>
						</td>
						<td style="text-align: center; width:30px; padding-right: 0;">
							<bean:message key='labels.betweenSign'/><!-- ～ -->
						</td>
						<td>
							<div class="pos_r">
								<html:text property="poDateTo" styleId="poDateTo" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="107" />
							</div>
						</td>
						<th><div class="col_title_right"><bean:message key='labels.deliveryDate'/></div></th><!-- 納期 -->
						<td style="padding-right: 0;">
							<div class="pos_r">
								<html:text property="deliveryDateFrom" styleId="deliveryDateFrom" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="108" />
							</div>
						</td>
						<td style="text-align: center; width:30px; padding-right: 0;">
							<bean:message key='labels.betweenSign'/><!-- ～ -->
						</td>
						<td>
							<div class="pos_r">
								<html:text property="deliveryDateTo" styleId="deliveryDateTo" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="109" />
							</div>
						</td>
					</tr>
				</table>
				<table id="search_info3" class="forms" summary="search_info3">
					<tr>
						<th><div class="col_title_right"><bean:message key='labels.memorandum'/></div></th><!-- 摘要 -->
						<td colspan="4">
							<html:text styleId="remarks" property="remarks" style="width: 350px" tabindex="110"/>
						</td>
						<th><div class="col_title_right"><bean:message key='labels.transportCategory'/></div></th><!-- 運送便 -->
						<td>
							<html:select property="transportCategory" styleId="transportCategory" tabindex="111">
								<html:options collection="transportCategoryList" property="value" labelProperty="label"/>
							</html:select>
						</td>
						<th><div class="col_title_right"><bean:message key='labels.slipStatus.status'/></div></th><!-- 状態 -->
						<td>
							<html:checkbox property="onlyRestQuantityExist" styleId="onlyRestQuantityExist" tabindex="112"/>
							<bean:message key='labels.slipStatus.onlyRestQuantityExist'/><br>
							<html:checkbox property="onlyUnpaid" styleId="onlyUnpaid" tabindex="113"/><bean:message key='labels.slipStatus.onlyUnpaid'/>
						</td>
					</tr>
				</table>
				<table id="search_info4" class="forms" summary="search_info4">
					<tr>
						<th><div class="col_title_right"><bean:message key='labels.supplierCode'/></div></th><!-- 仕入先コード -->
						<td>
							<html:text property="supplierCode" styleId="supplierCode" styleClass="c_ime_off" style="width: 100px;" tabindex="200" />
							<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" tabindex="201" onclick="openSupplierSearchDialog(1)" />
						</td>
						<th><div class="col_title_right"><bean:message key='labels.supplierName'/></div></th><!-- 仕入先名 -->
						<td>
							<html:text property="supplierName" styleId="supplierName" style="width: 250px;" tabindex="202" />
							<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" tabindex="203" onclick="openSupplierSearchDialog(2)" />
						</td>
						<th><div class="col_title_right"><bean:message key='labels.pcName'/></div></th><!-- 担当者名 -->
						<td>
							<html:text property="supplierPcName" styleId="supplierPcName" style="width: 100px;" tabindex="204" />
						</td>
					</tr>
				</table>
				<table id="search_info5" class="forms" summary="search_info5">
					<tr>
						<th><div class="col_title_right"><bean:message key='labels.productCode'/></div></th><!-- 商品コード -->
						<td>
							<html:text property="productCode" styleId="productCode" styleClass="c_ime_off" style="width: 165px;" tabindex="300"
								onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
							<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" tabindex="301" onclick="openProductSearchDialog(1)" />
						</td>
						<th><div class="col_title_right"><bean:message key='labels.productName'/></div></th><!-- 商品名 -->
						<td>
							<html:text property="productAbstract" styleId="productAbstract" styleClass="c_referable" style="width: 250px;" tabindex="302" />
							<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" tabindex="303" onclick="openProductSearchDialog(2)" />
						</td>
					</tr>
					<tr>
						<th><div class="col_title_right"><bean:message key='labels.product1'/></div></th><!-- 分類(大) -->
						<td colspan="3">
							<html:select property="product1" styleId="product1" styleClass="ProductClass1_TopEmpty" style="width: 500px;" tabindex="304">
								<html:options collection="product1List" property="value" labelProperty="label"/>
							</html:select>
						</td>
					</tr>
					<tr>
						<th><div class="col_title_right"><bean:message key='labels.product2'/></div></th><!-- 分類(中) -->
						<td colspan="3">
							<html:select property="product2" styleId="product2" styleClass="ProductClass2_TopEmpty" style="width: 500px;" tabindex="305">
								<html:options collection="product2List" property="value" labelProperty="label"/>
							</html:select>
						</td>
					</tr>
					<tr>
						<th><div class="col_title_right"><bean:message key='labels.product3'/></div></th><!-- 分類(小) -->
						<td colspan="3">
							<html:select property="product3" styleId="product3" styleClass="ProductClass3_TopEmpty" style="width: 500px;" tabindex="306">
								<html:options collection="product3List" property="value" labelProperty="label"/>
							</html:select>
						</td>
					</tr>
				</table>
			</div>

			<html:hidden property="sortColumn" styleId="sortColumn" />
			<html:hidden property="sortOrderAsc" styleId="sortOrderAsc" />

			</div><!-- /.form_section -->
	    	</div><!-- /.form_section_wrap -->
	    </div>
	</s:form>

	<form name="OutputForm" action="${f:url('/porder/searchPOrderResultOutput/excel')}" target="_blank" style="display: none;" method="POST">
	</form>

	<div style="width: 1160px; text-align: right">
		<button type="button" tabindex="350" onclick="onF1();" class="btn_medium"><bean:message key='words.action.initialize'/></button> <!-- 初期化 -->
		<button type="button" tabindex="351" onclick="onF2();"class="btn_medium"><bean:message key='words.action.search'/></button> <!-- 検索 -->
	</div>

	<span id="listContainer">
		<%-- 検索結果領域 --%>
		<%@ include file="/WEB-INF/view/ajax/porder/searchPOrderResultAjax/result.jsp" %>
	</span>
</div>
</body>
</html>
