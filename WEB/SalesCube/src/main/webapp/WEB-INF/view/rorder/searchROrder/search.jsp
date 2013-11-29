<%@page import="jp.co.arkinfosys.common.Constants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><bean:message key='titles.system'/> <bean:message key='titles.searchROrder'/></title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>
	<script type="text/javascript" src="${f:url('/scripts/dialogs.js')}"></script>
	<script type="text/javascript">
	<!--
	var targetTmp = null;
	var paramData = null;
	var paramDataTmp = null;
	var data = null;



function init(){
	// 初期フォーカス
	$('#roSlipId').focus();

	$("#customerCode").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁

}

// 顧客検索
function openCustomerSearchDialog(type){
	var id = "CustomerSearch";

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

// 顧客情報設定
function setCustomerInfo(type, map) {
	if(type == 1) {
		// 顧客コードを設定する
		$("#customerCode").val(map["customerCode"]);
	} else if(type == 2) {
		// 顧客名を設定する
		$("#customerName").val(map["customerName"]);
	}
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



// 初期化
function onF1(){
	// 入力内容を初期化してよろしいですか？
	if(confirm('<bean:message key="confirm.init" />')){
		showNowSearchingDiv();
		location.doHref('${f:url("/rorder/searchROrder")}');
	}

}
// 検索
function onF2(){

	// 検索条件を設定する
	if (targetTmp!=null) {
		if (targetTmp!=$("#searchTarget").val()) {
			$("#sortColumn").val("");
		}
	}
	targetTmp=$("#searchTarget").val();

	paramData = createParamData();
	paramData["pageNo"] = 1;
	// 検索を実行する
	execSearch(paramData);

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
	openDetailDispSettingDialog('detailDisp', '0301', $('#searchTarget').val());
}

// ページ遷移
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

	paramData["roSlipId"] = $("#roSlipId").val();
	paramData["receptNo"] = $("#receptNo").val();
	if($("#restOnly:checked").size() > 0){
		paramData["restOnly"] = $("#restOnly").val();
	}
	if($("#razyOnly:checked").size() > 0){
		paramData["razyOnly"] = $("#razyOnly").val();
	}
	paramData["roDateFrom"] = $("#roDateFrom").val();
	paramData["roDateTo"] = $("#roDateTo").val();
	paramData["shipDateFrom"] = $("#shipDateFrom").val();
	paramData["shipDateTo"] = $("#shipDateTo").val();
	paramData["deliveryDateFrom"] = $("#deliveryDateFrom").val();
	paramData["deliveryDateTo"] = $("#deliveryDateTo").val();
	paramData["customerCode"] = $("#customerCode").val();
	paramData["customerName"] = $("#customerName").val();
	paramData["deliveryPcName"] = $("#deliveryPcName").val();

	paramData["salesCmCategoryList"] = new Array();
	$("input[name='salesCmCategory']").each(
		function() {
			if(this.checked) {
				paramData["salesCmCategoryList"].push( this.value );
			}
		}
	);

	paramData["productCode"] = $("#productCode").val();
	paramData["productAbstract"] = $("#productAbstract").val();
	paramData["product1"] = $("#product1").val();
	paramData["product2"] = $("#product2").val();
	paramData["product3"] = $("#product3").val();
	paramData["supplierCode"] = $("#supplierCode").val();
	paramData["supplierName"] = $("#supplierName").val();

	paramData["rowCount"] = $("#rowCount").val();
	paramData["sortColumn"] = $("#sortColumn").val();
	paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
	return paramData;
}
// 検索実行
function execSearch(paramData) {
	// Ajaxリクエストによって検索結果をロードする
	asyncRequest(
		contextRoot + "/ajax/rorder/searchROrderResultAjax/search",
		paramData,
		function(data) {
			// 検索結果テーブルを更新する
			$("#errors").empty();
			$("#listContainer").empty();
			$("#listContainer").append(data);

			// 1件以上ヒットした場合
			if($("#searchResultCount").val() != "0") {
				// 検索条件を保持
				paramDataTmp = paramData;

				// EXCELボタンの状態変更
				$("#btnF3").attr("disabled","");
			} else {
				// EXCELボタンの状態変更
				$("#btnF3").attr("disabled","disabled");
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


	-->
	</script>
</head>
<body onload="init()" onhelp="return false;">

	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0003"/>
		<jsp:param name="MENU_ID" value="0301"/>
	</jsp:include>

<!-- メイン機能 -->
<div id="main_function">

	<!-- タイトル -->
		<span class="title"><bean:message key='titles.searchROrder'/></span>

		<div class="function_buttons">
			<button id="btnF1" type="button" tabindex="2000" onclick="onF1();">F1<br><bean:message key='words.action.initialize'/></button>
			<button id="btnF2" type="button" tabindex="2001" onclick="onF2();">F2<br><bean:message key='words.action.search'/></button>
			<button id="btnF3" type="button" tabindex="2002" onclick="onF3();" disabled="disabled">F3<br><bean:message key='words.name.excel'/></button>
			<button id="btnF4" type="button" tabindex="2003" onclick="onF4();">F4<br><bean:message key='words.action.setting'/></button>
			<button type="button" tabindex="2004" disabled>F5<br>&nbsp;</button>
			<button type="button" tabindex="2005" disabled>F6<br>&nbsp;</button>
			<button type="button" tabindex="2006" disabled>F7<br>&nbsp;</button>
			<button type="button" tabindex="2007" disabled>F8<br>&nbsp;</button>
			<button type="button" tabindex="2008" disabled>F9<br>&nbsp;</button>
			<button type="button" tabindex="2009" disabled>F10<br>&nbsp;</button>
			<button type="button" tabindex="2010" disabled>F11<br>&nbsp;</button>
			<button type="button" tabindex="2011" disabled>F12<br>&nbsp;</button>
		</div>
		<br><br><br>

		<s:form onsubmit="return false;">
			<div class="function_forms">
				<div id="errors" style="color: red">
					<html:errors />
				</div>

				<html:hidden property="sortColumn" styleId="sortColumn" />
				<html:hidden property="sortOrderAsc" styleId="sortOrderAsc" />


			    <div class="form_section_wrap">
			    <div class="form_section">
			    	<div class="section_title">
						<span><bean:message key='labels.searchCondition'/></span>
			            <button class="btn_toggle">
			                <img alt="表示／非表示" src='${f:url("/images/customize/btn_toggle.png")}' width="28" height="29" class="tbtn">
			            </button>
					</div><!-- /.section_title -->
			
					
					<div id="search_info" class="section_body">
						<table id="search_info1" class="forms" summary="検索条件1">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.searchTarget'/></div></th> <!-- 検索対象 -->
								<td>
									<html:select property="searchTarget" styleId="searchTarget" tabindex="100">
										<html:options collection="searchTargetList" property="value" labelProperty="label"/>
									</html:select>
								</td>
								<th><div class="col_title_right"><bean:message key='labels.roSlipId'/></div></th> <!-- 受注番号 -->
								<td><html:text property="roSlipId" styleId="roSlipId" style="ime-mode:disabled;" tabindex="101"/></td>
								<th><div class="col_title_right"><bean:message key='labels.receptNo'/></div></th> <!-- 受付番号 -->
								<td><html:text property="receptNo" styleId="receptNo" style="ime-mode:disabled;" tabindex="102"/></td>
							</tr>
						</table>
	
						<table id="search_info2" class="forms" summary="検索条件2" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.roDate'/></div></th> <!-- 受注日 -->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="roDateFrom" styleId="roDateFrom" styleClass="date_input" style="width: 135px;ime-mode:disabled;" tabindex="103" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/> <!-- ～ -->
								</td>
								<td>
									<div class="pos_r">
										<html:text property="roDateTo" styleId="roDateTo" styleClass="date_input" style="width: 135px;ime-mode:disabled;" tabindex="104" />
									</div>
								</td>
								<th><div class="col_title_right"><bean:message key='labels.shipDate'/></div></th> <!-- 出荷日 -->
								<td style="padding-right: 0;">
									<div class="pos_r" style="padding-right: 0; margin-right: 0;">
										<html:text property="shipDateFrom" styleId="shipDateFrom" styleClass="date_input" style="width: 135px;ime-mode:disabled;" tabindex="105" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/> <!-- ～ -->
								</td>
								<td>
									<div class="pos_r">
										<html:text property="shipDateTo" styleId="shipDateTo" styleClass="date_input" style="width: 135px;ime-mode:disabled;" tabindex="106" />
									</div>
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.deliveryDate2'/></div></th> <!-- 納期指定日 -->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="deliveryDateFrom" styleId="deliveryDateFrom" styleClass="date_input" style="width: 135px;ime-mode:disabled;" tabindex="107" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/> <!-- ～ -->
								</td>
								<td>
									<div class="pos_r">
										<html:text property="deliveryDateTo" styleId="deliveryDateTo" styleClass="date_input" style="width: 135px;ime-mode:disabled;" tabindex="108" />
									</div>
								</td>
							</tr>
						</table>
	
						<table id="search_info3" class="forms" summary="検索条件3">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.restOnly'/></div></th>
								<td><html:checkbox property="restOnly" styleId="restOnly" value="1" tabindex="109" /></td>
								<th><div class="col_title_right"><bean:message key='labels.razyOnly'/></div></th>
								<td colspan="3"><html:checkbox property="razyOnly" styleId="razyOnly" value="1" tabindex="110" /></td>
							</tr>						
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.customerCode'/></div></th> <!-- 顧客コード -->
								<td>
									<html:text property="customerCode" styleId="customerCode" style="width: 150px;ime-mode:disabled;"  tabindex="200"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer" onclick="openCustomerSearchDialog(1)" tabindex="201" />
								</td>
								<th><div class="col_title_right"><bean:message key='labels.customerName'/></div></th> <!-- 顧客名 -->
								<td>
									<html:text property="customerName" styleId="customerName" style="width: 250px"  tabindex="202"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(2)" tabindex="203" />
								</td>
								<th><div class="col_title_right"><bean:message key='labels.customerPcName2'/></div></th> <!-- 顧客担当者名 -->
								<td>
									<html:text property="deliveryPcName" styleId="deliveryPcName" style="width: 100px; ime-mode: auto;"  tabindex="204"/>
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.salesCmCategory'/></div></th> <!-- 顧客コード -->
								<td colspan="5">
									<c:forEach var="salesCmCategory" varStatus="s" items="${salesCmCategoryList}">
										<input id="salesCmCategory${s.index}" type="checkbox" name="salesCmCategory" value="${salesCmCategory.value}" tabindex="205" >
										<label for="salesCmCategory${s.index}">${salesCmCategory.label}</label>
									</c:forEach>
								</td>
							</tr>
						</table>
	
						<table id="search_info4" class="forms" summary="検索条件4">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.productCode'/></div></th><!-- 商品コード -->
								<td>
									<html:text property="productCode" styleId="productCode" style="width: 165px;ime-mode:disabled;" tabindex="300"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer" onclick="openProductSearchDialog(1)" tabindex="301" />
								</td>
								<th><div class="col_title_right"><bean:message key='labels.productName'/></div></th><!-- 商品名 -->
								<td>
									<html:text property="productAbstract" styleId="productAbstract" style="width: 250px" tabindex="302" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(2)" tabindex="303" />
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.product1'/></div></th><!-- 分類(大) -->
								<td>
										<html:select property="product1" styleId="product1" styleClass="ProductClass1_TopEmpty" style="width: 500px;" tabindex="304">
											<html:options collection="product1List" property="value" labelProperty="label"/>
										</html:select>
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.product2'/></div></th> <!-- 分類(中) -->
								<td>
									<html:select property="product2" styleId="product2" styleClass="ProductClass2_TopEmpty" style="width: 500px;" tabindex="305">
										<html:options collection="product2List" property="value" labelProperty="label"/>
									</html:select>
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.product3'/></div></th> <!-- 分類(小) -->
								<td>
									<html:select property="product3" styleId="product3" styleClass="ProductClass3_TopEmpty" style="width: 500px;" tabindex="306">
										<html:options collection="product3List" property="value" labelProperty="label"/>
									</html:select>
								</td>
							</tr>
						</table>
	
						<table id="search_info5" class="forms" summary="検索条件5">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.supplierCode'/></div></th> <!-- 仕入先コード -->
								<td>
									<html:text property="supplierCode" styleId="supplierCode" style="width: 100px;ime-mode:disabled;" tabindex="400" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSupplierSearchDialog(1)" tabindex="401" />
								</td>
								<th><div class="col_title_right"><bean:message key='labels.supplierName'/></div></th> <!-- 仕入先名 -->
								<td>
									<html:text property="supplierName" styleId="supplierName" style="width: 250px" tabindex="402" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSupplierSearchDialog(2)" tabindex="403" />
								</td>
							</tr>
						</table>
					</div>
		    	</div><!-- /.form_section -->
		    	</div><!-- /.form_section_wrap -->
			</div>
		</s:form>

		<form name="OutputForm" action="${f:url('/rorder/searchROrderResultOutput/excel')}" target="_blank" style="display: none;" method="POST">
		</form>

		<div style="width: 1160px; text-align: right">
			<button type="button" onclick="onF1();" tabindex="450" class="btn_medium"><bean:message key='words.action.initialize'/></button> <!-- 初期化 -->
			<button type="button" onclick="onF2();" tabindex="451" class="btn_medium"><bean:message key='words.action.search'/></button> <!-- 検索 -->
		</div>
		<span id="listContainer">
			<%-- ページヘッダ領域 --%>
			<%@ include file="/WEB-INF/view/ajax/rorder/searchROrderResultAjax/result.jsp" %>
		</span>
	</div>
</body>
</html>
