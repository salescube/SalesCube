<%@page import="jp.co.arkinfosys.common.CategoryTrns" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　商品マスタ管理（登録・編集）</title>

<%@ include file="/WEB-INF/view/common/header.jsp"%>

	<script type="text/javascript">
	<!--

	$(function() {
		applyPriceAlignment();
		applyQuantityAlignment($(".BDCqua"));
		applyStatsAlignment($(".BDCrate"));
		applyCUnitSign();
	});

	//// Fボタン
	// F1
	function onF1(){
		init_screen();		// 初期化
	}

	// F2
	function onF2(){
		go_to_search();		// 検索画面へ
	}

	// F3
	function onF3(){
		<c:if test="${!editMode}">
		insert_product();
		</c:if>

		<c:if test="${editMode}">
		update_product();
		</c:if>
	}

	// F4
	function onF4(){
		delete_product();
	}

	// F5
	function onF5(){
		open_default_dialog();
	}

 	// F8
    function onF8() {
        outputHistory();
    }


	////
	// 初期化
	function init_screen(){
    	if(confirm("<bean:message key='confirm.init'/>")){
    		location.doHref("${f:url("/master/editProduct")}");
        }
	}

	// 登録
	function insert_product(){
		if(confirm('<bean:message key="confirm.insert" />')){
			document.master_editProductActionForm.action = "${f:url("insert")}";
			_before_submit($(".numeral_commas"));
			document.master_editProductActionForm.submit();
		}
	}

	// 更新
	function update_product(){
		if(confirm('<bean:message key="confirm.update" />')){
			document.master_editProductActionForm.action = "${f:url("update")}";
			_before_submit($(".numeral_commas"));
			document.master_editProductActionForm.submit();
		}
	}

	// 削除
	function delete_product(){
		if(confirm('<bean:message key="confirm.delete" />')){
			document.master_editProductActionForm.action = "${f:url("delete")}";
			_before_submit($(".numeral_commas"));
			document.master_editProductActionForm.submit();
		}
	}

	// 検索画面へ
	function go_to_search(){
		if(confirm('<bean:message key="confirm.master.product.back" />')){
			window.location.doHref("${f:url("/master/searchProduct")}");
		}
	}

	// マスタ初期値設定画面を開く
	function open_default_dialog() {
		openMasterDefaultSettingDialog("DefaultSetting", "PRODUCT_MST");
	}

	//仕入先検索ダイアログ
	function supplierSearch() {
		if(!${isUpdate}) {
			return;
		}

		openSearchSupplierDialog("suppDialog",
				function(id, map) {
					$("#supplierCode").val(map["supplierCode"]);
					changeSupplierCode($("#supplierCode").get(0));
				}
		);
		$("#suppDialog_supplierCode").val($("#supplierCode").val());
		$("#suppDialog_supplierCode").focus();
	}

	// 棚番検索ダイアログ
	function rackSearch() {
		if(!${isUpdate}) {
			return;
		}

		openSearchRackDialog("rackDialog",
			function(id, map) {
				$("#rackCode").val(map['rackCode']);
				$("#warehouseName").val( map[ "warehouseName" ] );
			}
		);
		$("#rackDialog_warehouseName").val($("#warehouseName").val());
		$("#rackDialog_rackCode").val($("#rackCode").val());
		$("#rackDialog_rackCode").focus();
	}


	// 数量割検索ダイアログ
	function discountSearch() {
		if(!${isUpdate}) {
			return;
		}

		openSearchDiscountDialog("discountDialog",
			function(id, map) {
				$("#discountId").val(map['discountId']);
				$("#discountName").val(map['discountName']);
			}
		);
		$("#discountDialog_discountId").val($("#discountId").val());
		$("#discountDialog_discountId").focus();
	}

	// 商品分類選択後にリストのhidden項目を更新する
	function afterChangeProductClass() {
		var p2hidden = $("#product2Hidden");
		var p3hidden = $("#product3Hidden");

		var options = $("#product2").children("option");
		p2hidden.empty();
		for(var i = 0; i < options.length; i++) {
			var hidden = $(document.createElement("input"));
			hidden.attr("type", "hidden");
			hidden.attr("name", "product2List[" + i + "].value");
			hidden.val(options[i].value);
			p2hidden.append(hidden);

			hidden = $(document.createElement("input"));
			hidden.attr("type", "hidden");
			hidden.attr("name", "product2List[" + i + "].label");
			hidden.val(options[i].text);
			p2hidden.append(hidden);
		}

		options = $("#product3").children("option");
		p3hidden.empty();
		for(var i = 0; i < options.length; i++) {
			var hidden = $(document.createElement("input"));
			hidden.attr("type", "hidden");
			hidden.attr("name", "product3List[" + i + "].value");
			hidden.val(options[i].value);
			p3hidden.append(hidden);

			hidden = $(document.createElement("input"));
			hidden.attr("type", "hidden");
			hidden.attr("name", "product3List[" + i + "].label");
			hidden.val(options[i].text);
			p3hidden.append(hidden);
		}
	}

	/**
	 * 割引コード変更処理
	 */
	function changeDiscountId(element) {
		if(element.value.length == 0){
			element.value = "";
			$("#discountName").val("");
			return;
		}

		// 割引情報を取得する
		var data = new Object();
		data["discountId"] = element.value;
		asyncRequest(
			contextRoot + "/ajax/commonDiscount/getDiscountInfos",
			data,
			function(data) {
				if(data==""){
					alert('<bean:message key="errors.notExist" arg0="割引コード"/>');
					$("#discountName").val("");
					return;
				}

				var values = eval("(" + data + ")");
				$("#discountName").val(values["discountName"]);
			}
		);
	}

	/**
	 * 仕入先コード変更処理
	 */
	function changeSupplierCode(element) {
		if(element.value.length == 0){
			// クリアする
			clearSupplierInfo();
			return;
		}

		// 仕入先情報を取得する
		var data = new Object();
		data["supplierCode"] = $("#supplierCode").val();
		asyncRequest(
			contextRoot + "/ajax/commonSupplier/getSupplierInfosBySupplierCode",
			data,
			function(data) {
				if(data==""){
					alert('<bean:message key="errors.notExist" arg0="仕入先コード"/>');
					clearSupplierInfo();
					return;
				}

				var values = eval("(" + data + ")");
				$("#supplierName").val(values["supplierName"]);
				$("#priceFractCategory").val(values["priceFractCategory"]);
				$("#unitPriceDecAlignment").val(values["unitPriceDecAlignment"]);
				$("#dolUnitPriceDecAlignment").val(values["dolUnitPriceDecAlignment"]);
				$("#supplierRate").val("");

				// 外貨記号を仕入先の外貨記号で置き換える
				CurrencyUnitClassNameHashList["dollar_value"] = values["cUnitSign"];
				$("#sign").val(values["cUnitSign"]);

				// 本日付の円/外貨レート情報を取得する
				var today = new Date();

				asyncRequest(
					contextRoot + "/ajax/commonPOrder/getSupplierRate/",
					{
						"tempSupplierCode" : $("#supplierCode").val(),
						"targetDate" : (today.getYear() < 2000)?(today.getYear()+1900)+ "/" + (today.getMonth() + 1) + "/" + today.getDate():today.getYear() + "/" + (today.getMonth() + 1) + "/" + today.getDate()
					},
					function(result) {
						$("#supplierRate").val(result);

						// 仕入先のレートで単価を再計算
						exchangePrice();

						// 端数処理
						applyPriceAlignment();
					}
				);
			}
		);
	}

	/**
	 * 仕入先に関する情報をクリアする
	 */
	function clearSupplierInfo() {
		$("#supplierName").val("");
		$("#priceFractCategory").val("");
		$("#unitPriceDecAlignment").val("");
		$("#dolUnitPriceDecAlignment").val("");
		$("#supplierRate").val("");
	}

	// 数量入力フィールドにonchange時のイベントを付与
	$(function() {
		$(".BDCqua").change(
			function() {
				applyQuantityAlignment($(this));
			}
		);
	});

	/**
	 * 数量小数桁処理と端数処理を適用する
	 */
	function applyQuantityAlignment(jQueryObject) {
		if(jQueryObject != null) {
			jQueryObject.setBDCStyle( ${mineDto.productFractCategory} ,${mineDto.numDecAlignment} ).attBDC();
		}
	}

	// 単価小数桁処理と端数処理を適用する
	function applyPriceAlignment() {
		// 円単価
		$(".BDCyen").setBDCStyle( $("#priceFractCategory").val() ,$("#unitPriceDecAlignment").val() ).attBDC();
		// 外貨単価
		$(".BDCdol").setBDCStyle( $("#priceFractCategory").val() ,$("#dolUnitPriceDecAlignment").val() ).attBDC();
	}

	// 統計端数処理を適用する
	function applyStatsAlignment(jQueryObject) {
		if(jQueryObject != null) {
			// 四捨五入
			jQueryObject.setBDCStyle( "1" ,$("#statsDecAlignment").val() ).attBDC();
		}
	}

	/**
	 * 外貨単価変更処理
	 */
	function exchangePrice() {
		// 「外貨」に値が入っているか
		if($("#supplierPriceDol").val().length == 0){
			return;
		}

		// 外貨単価とレートから円単価を計算
		if ( _isNum($("#supplierRate").val()) && _isNum($("#supplierPriceDol").val()) ){
			// 外貨単価
			var dolUnitPrice = _SetBigDecimalScale(STYLE_PRICE,
				new BigDecimal(_getNumStr($("#supplierPriceDol").val())));
			// レート
			var supplierRate = new BigDecimal($("#supplierRate").val());
			$("#supplierPriceYen").val(
					SetBigDecimalScale(STYLE_PRICE, dolUnitPrice.multiply(supplierRate).toString()) );

			_after_load( $("#supplierPriceYen") );
		}

		// セット：単品
		if($("#setTypeCategory").val() == "<%=CategoryTrns.PRODUCT_SET_TYPE_SINGLE%>"){

			// 仕入先コードが入力されていない
			if($("#supplierCode").val().length == 0){
				$("#ajax_errors").empty();
				$("#ajax_errors").append(document.createTextNode("<bean:message key="warns.product.single" arg0="仕入先コード"/>"));
			}
		}

		applyPriceAlignment();
	}

	/**
	 * 棚番コード変更
	 */
	function changeRackCode(element) {
		if(element.value.length == 0){
			return;
		}
		var data = new Object();
		data["rackCode"] = element.value;
		asyncRequest(
			contextRoot + "/ajax/commonRack/getRackInfos",
			data,
			function(data) {
				if(data==""){
					alert('<bean:message key="errors.notExist" arg0="棚番"/>');
				}
			}
		);
	}

	/**
	 * 特注品で在庫管理「する」商品かどうかチェックし、警告メッセージを表示する
	 */
	function checkNMJStockCtlCategory(func) {
		$("#ajax_errors").empty();
		if( checkNMJStandardCategory()
				&& $("#stockCtlCategory").val() == "<%=CategoryTrns.PRODUCT_STOCK_CTL_YES%>" ) {

			$("#ajax_errors").append(document.createTextNode("<bean:message key="warns.nmjproduct.stock.ctl"/>"));
		}
	}

	/**
	 * 標準化分類が「特注品」かどうかを判定する
	 */
	function checkNMJStandardCategory() {
		if($("#productStandardCategory").val() == "<%=CategoryTrns.PRODUCT_STANDARD_ODR%>") {
			return true;
		}
		return false;
	}

	// 履歴出力
	function outputHistory(){
		// この内容でエクセル出力しますか？
		if(!confirm('<bean:message key="confirm.history" />')){
			return;
		}

		// 出力を実行する
		// Ajaxリクエストによって変更履歴をロードする
		var data = new Object();
		data["productCode"] = $("#productCode").val();
		data["productName"] = $("#productName").val();

		asyncRequest(
			contextRoot + "/ajax/outputProductHistAjax/prepare",
			data,
			function(data) {
				$("#errors").empty();
				window.open(contextRoot + "/ajax/outputProductHistAjax/excel",	"<bean:message key='words.name.excel'/>");
			},
			function(xmlHttpRequest, textStatus, errorThrown) {
				if (xmlHttpRequest.status == 450) {
					// 検索条件エラー
					$("#errors").empty();
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

	//JANコードの妥当性チェック
	function chkdigit(oj) {
		var len = oj.value.length;
		if(  len == 0) return;
		if( len != 13  && len != 8 ){
			alert('<bean:message key="warns.product.jancode"/>');
   			oksubmit = false ;
   			return;
		} else {
  			var sumdata = 0;
  			for ( var i=0 ; i < len-1 ; i++ ){
  	  			if( len == 13){
  	    			if( i%2 ) var w = 3;
  	    			else      var w = 1;
  	  			}else{
  	    			if( i%2 ) var w = 1;
  	    			else      var w = 3;
  	  			}

    			sumdata += parseInt(oj.value.charAt(i)) * w;
  			}

  			var digitVal = ( 10 - sumdata % 10 );
  			if( digitVal != parseInt( oj.value.charAt(len-1) ) ){
    			alert('<bean:message key="warns.product.jancode"/>');
    			return;
  			}
		}
	}

	// 外貨記号リストを作成して仕入先の外貨記号をセットする(記号の付与・除去に必要)
	function applyCUnitSign(){
		// レートマスタの全ての外貨記号リストを取得する
		var data = new Object();
		asyncRequest(
			contextRoot + "/ajax/commonRate/getAllRateSign",
			data,
			function(data) {
				if(data==""){
					alert('<bean:message key="errors.notExist" arg0="レート情報"/>');
					return;
				}
				var values = eval("(" + data + ")");

				// レート記号を設定
				for(var key in values){
					CurrencyUnitClassNameHashList[key] = values[key];
				}
			}
		);
	}

	// 外貨記号の初期値を設定する(仕入先に設定されたレートから取得)
	CurrencyUnitClassNameHashList["dollar_value"] = '<bean:write name="editProductForm" property="sign" />';


	/*
	 * 既存商品を呼び出す商品検索ダイアログを表示する
	 */
	function openProductSearchDialog(){
		if(!${isUpdate}) {
			return;
		}
		if(${editMode}) {
			return;
		}

		// 商品検索ダイアログを開く
		openSearchProductDialog( "editProduct", loadProduct );
	}

	/*
	 * 商品検索ダイアログで表示された商品を読み込む
	 */
	function loadProduct( dialogId, map ) {
		$("#LoadForm").find("#productCode").val( map["productCode"] );
		$("#LoadForm").submit();
	}
	-->
	</script>

</head>
<body>

	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp"%>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0013" />
		<jsp:param name="MENU_ID" value="1300" />
	</jsp:include>

	<%-- メイン機能 --%>
	<div id="main_function">
		<span class="title">商品</span>

		<%-- Fキー群 --%>
		<div class="function_buttons">
			<button tabindex="2000" onclick="onF1()">F1<br>初期化</button>
			<button type="button" tabindex="2001" onclick="onF2()">F2<br>戻る</button>
			<button type="button" tabindex="2002" onclick="onF3();"
				<c:if test="${!isUpdate}">disabled</c:if> >F3<br>
				<c:if test="${!editMode}">登録</c:if>
				<c:if test="${editMode}">更新</c:if></button>
			<button type="button" tabindex="2003" onclick="onF4();"
				<c:if test="${!editMode || !isUpdate}">disabled="disabled"</c:if> >F4<br>削除</button>
			<button type="button" tabindex="2004" onclick="onF5();"
				<c:if test="${!isUpdate}">disabled</c:if> >F5<br>初期値</button>
			<button type="button" disabled="disabled">F6<br>&nbsp;</button>
			<button type="button" disabled="disabled">F7<br>&nbsp;</button>
			<button type="button" tabindex="2005" onclick="onF8();"
				<c:if test="${!editMode || !isUpdate}">disabled</c:if> >F8<br>履歴出力</button>
			<button type="button" disabled="disabled">F9<br>&nbsp;</button>
			<button type="button" disabled="disabled">F10<br>&nbsp;</button>
			<button type="button" disabled="disabled">F11<br>&nbsp;</button>
			<button type="button" disabled="disabled">F12<br>&nbsp;</button>
		</div>
		<br><br><br>

		<div class="function_forms">

			<%-- エラー表示部分 --%>
			<div id="errors" style="color: red">
				<html:errors/>
			</div>
			<span id="ajax_errors"></span>
			<div id = "message" style="padding-left: 20px;color: blue;">
				<html:messages id="msg" message="true">
					<bean:write name="msg" ignore="true"/>
				</html:messages>
			</div>

			<s:form onsubmit="return false;">

			    <div class="form_section_wrap">
				    <div class="form_section">
				        <div class="section_title">
							<span>商品情報</span>
				            <button class="btn_toggle" />
						</div><!-- /.section_title -->

						<html:hidden styleId="isUpdate" property="isUpdate"/>
						<html:hidden styleId="editMode" property="editMode"/>

						<html:hidden styleId="fractCategory" property="fractCategory"/>
						<html:hidden styleId="taxCategory" property="taxCategory"/>
						<html:hidden styleId="productFractCategory" property="productFractCategory"/>
						<input type="hidden" id="numDecAlignment" name="numDecAlignment" value="0">

						<html:hidden styleId="priceFractCategory" property="priceFractCategory"/>
						<html:hidden styleId="unitPriceDecAlignment" property="unitPriceDecAlignment"/>
						<html:hidden styleId="dolUnitPriceDecAlignment" property="dolUnitPriceDecAlignment"/>
						<html:hidden styleId="statsDecAlignment" property="statsDecAlignment"/>
						<html:hidden styleId="supplierRate" property="supplierRate"/>

						<div id="order_section" class="section_body">
							<table id="product_info" class="forms" summary="商品情報">
								<tr>
									<th><div class="col_title_right_req">商品コード<bean:message key='labels.must'/></div></th>
									<td>
										<html:text styleId="productCode" property="productCode" style="width: 160px; ime-mode: disabled;" tabindex="100" maxlength="20"
											styleClass="${editMode || !isUpdate ? 'c_disable' : '' }"
											readonly="${editMode || !isUpdate}"
											onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); checkNMJStockCtlCategory(); }" />

										<c:if test="${!editMode}">
										<html:image src="${f:url('/images//customize/btn_search.png')}"
											style="vertical-align: middle; cursor: pointer;"
											tabindex="101" onclick="openProductSearchDialog();" />
										</c:if>
									</td>
									<th><div class="col_title_right_req">商品名<bean:message key='labels.must'/></div></th>
									<td><html:text styleId="productName" property="productName" style="width: 200px;" tabindex="102" maxlength="60"/></td>
									<th><div class="col_title_right">商品名カナ</div></th>
									<td><html:text styleId="productKana" property="productKana" style="width: 200px;" tabindex="103" maxlength="60"/></td>
								</tr>
								<tr>
									<th><div class="col_title_right"><bean:message key='labels.onlineorder.pcode'/></div></th>
									<td><html:text styleId="onlinePcode" property="onlinePcode" style="width: 160px;ime-mode: disabled;" tabindex="104" maxlength="50"/></td>
									<th><div class="col_title_right">JANコード</div></th>
									<td><html:text styleId="janPcode" property="janPcode" style="width: 150px;ime-mode: disabled;" tabindex="105" maxlength="13"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ chkdigit(this); }"/></td>
									<th><div class="col_title_right">廃番予定日</div></th>
									<td><html:text styleClass="date_input" styleId="discardDate" property="discardDate" style="width: 140px; ime-mode: disabled;" tabindex="106" /></td>
								</tr>
							</table>
							<table class="forms" summary="仕入先情報">
								<tr>
									<th><div class="col_title_right">仕入先コード</div></th>
									<td><html:text styleId="supplierCode" property="supplierCode"
										style="width: 100px;ime-mode: disabled;" tabindex="200" maxlength="10"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ changeSupplierCode(this); }"/>
										<html:image src="${f:url('/images//customize/btn_search.png')}"
											style="vertical-align: middle; cursor: pointer;" tabindex="201"
											onclick="supplierSearch()" />
									</td>
									<th><div class="col_title_right">仕入先名</div></th>
									<td><html:text styleId="supplierName" property="supplierName"
											style="width: 150px;" styleClass="c_disable" tabindex="202" readonly="true" /></td>
									<th><div class="col_title_right">仕入先商品コード</div></th>
									<td><html:text styleId="supplierPcode" property="supplierPcode"
											style="width: 150px;ime-mode: disabled;" tabindex="203" maxlength="20"/></td>
								</tr>
								<tr>
									<th><div class="col_title_right_req">仕入単価（円）<bean:message key='labels.must'/></div></th>
									<td><html:text styleClass="numeral_commas yen_value BDCyen" styleId="supplierPriceYen" property="supplierPriceYen"
											style="width: 150px;" tabindex="204" onchange="applyPriceAlignment();" maxlength="9"/></td>
									<th><div class="col_title_right">仕入単価（外貨）</div></th>
									<td colspan="3"><html:text styleClass="numeral_commas dollar_value BDCdol" styleId="supplierPriceDol" property="supplierPriceDol"
											style="width: 150px;" tabindex="205" onchange="exchangePrice();" maxlength="9"/></td>

								</tr>
							</table>
							<html:hidden styleId="sign" property="sign" />
							<table class="forms" summary="商品在庫情報">
								<tr>
									<th><div class="col_title_right">在庫管理</div></th>
									<td>
										<html:select styleId="stockCtlCategory" property="stockCtlCategory" tabindex="300" onchange="checkNMJStockCtlCategory();">
											<html:options collection="stockCtlCategoryList" property="value" labelProperty="label" />
										</html:select>
									</td>
									<th><div class="col_title_right">入数</div></th>
									<td colspan="3">
										<html:text styleClass="numeral_commas BDCqua" styleId="packQuantity" property="packQuantity"  style="width: 100px;" tabindex="301" maxlength="5"/>
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">月平均出荷数</div></th>
									<td>
										<html:text styleId="avgShipCount" property="avgShipCount"
											style="width: 100px;" styleClass="c_disable numeral_commas BDCqua" tabindex="302" readonly="true" /></td>
									<th><div class="col_title_right">倉庫名</div></th>
									<td>
										<html:text styleId="warehouseName" property="warehouseName" style="width: 100px; ime-mode: disabled;" tabindex="303" readonly="true" styleClass="c_disable" />
									<th><div class="col_title_right">棚番</div></th>
									<td>
										<html:text styleId="rackCode" property="rackCode" style="width: 100px; ime-mode: disabled;" tabindex="304" maxlength="10"
											onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ changeRackCode(this); }"/>
										<html:image src="${f:url('/images//customize/btn_search.png')}"
											style="vertical-align: middle; cursor: pointer;"
											tabindex="305" onclick="rackSearch();" /></td>
								</tr>
								<tr>
									<th><div class="col_title_right">リードタイム</div></th>
									<td>
										<html:text styleId="leadTime" property="leadTime" style="width: 100px; text-align: right; ime-mode: disabled;"
											maxlength="5" tabindex="306"/> 日
									</td>
									<th><div class="col_title_right">発注点</div></th>
									<td>
										<html:text styleClass="numeral_commas BDCqua"  styleId="poNum" property="poNum" style="width: 100px;" tabindex="307" maxlength="6"/>
										<html:checkbox styleId="poUpdFlag" property="poUpdFlag" value="1" tabindex="308" />自動更新</td>
									<th><div class="col_title_right">安全在庫数</div></th>
									<td>
										<html:text styleClass="numeral_commas BDCqua"  styleId="mineSafetyStock" property="mineSafetyStock" style="width: 100px;" tabindex="309" maxlength="6"/>
										<html:checkbox styleId="mineSafetyStockUpdFlag" property="mineSafetyStockUpdFlag" value="1" tabindex="310" />自動更新</td>

										<html:hidden property="entrustSafetyStock"/>
										<html:hidden property="salesStandardDeviation"/>
								</tr>
								<tr>
									<th><div class="col_title_right">発注ロット</div></th>
									<td>
										<html:text styleClass="numeral_commas BDCqua"  styleId="poLot" property="poLot" style="width: 100px;" tabindex="311" maxlength="6"/>
										<html:checkbox styleId="lotUpdFlag" property="lotUpdFlag" value="1" tabindex="312" />自動更新
									</td>
									<th><div class="col_title_right">最大保有数</div></th>
									<td>
										<html:text styleClass="numeral_commas BDCqua"  styleId="maxStockNum" property="maxStockNum" style="width: 100px;" tabindex="313" maxlength="6"/>
										<html:checkbox styleId="stockUpdFlag" property="stockUpdFlag" value="1" tabindex="314" />自動更新
									</td>
									<th><div class="col_title_right">単位発注限度数</div></th>
									<td>
										<html:text styleClass="numeral_commas BDCqua"  styleId="maxPoNum" property="maxPoNum" style="width: 100px;" tabindex="315" maxlength="6"/>
										 <html:checkbox styleId="maxPoUpdFlag" property="maxPoUpdFlag" value="1" tabindex="316"/>自動更新
									</td>
								</tr>
							</table>

							<table class="forms" summary="ランク・割引情報">
								<tr>
									<th><div class="col_title_right">受注限度数</div></th>
									<td><html:text styleClass="numeral_commas BDCqua" styleId="roMaxNum" property="roMaxNum" style="width: 100px;" tabindex="400" maxlength="5"/></td>
									<th><div class="col_title_right">売単価</div></th>
									<td><html:text styleClass="numeral_commas yen_value BDCyen" styleId="retailPrice" property="retailPrice"
											style="width: 150px;" tabindex="401" onchange="applyPriceAlignment();" maxlength="9"/></td>
									<th><div class="col_title_right">数量割引</div></th>
									<td>
										<html:text styleId="discountId" property="discountId" style="width: 150px; ime-mode: disabled;" styleClass="discountId" tabindex="402"
											onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ changeDiscountId(this); }" maxlength="20"/>
										<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" tabindex="403" onclick="discountSearch();" />

									</td>
									<td>
										<html:text styleId="discountName" property="discountName" style="width: 150px;" styleClass="c_disable" tabindex="404"  readonly="true" />
										<html:hidden styleId="discountUpdDatetm" property="discountUpdDatetm" />
									</td>
								</tr>
							</table>
						</div><!-- /.section_body -->
					</div><!-- /.form_section -->
				</div><!-- /.form_section_wrap -->

			    <div class="form_section_wrap">
				    <div class="form_section">
				        <div class="section_title">
							<span>分類</span>
				            <button class="btn_toggle" />
						</div><!-- /.section_title -->

						<div id="order_section" class="section_body">
							<table id="product_category_info" class="forms" summary="商品分類">
								<tr>
									<th><div class="col_title_right">状況</div></th>
									<td><html:select tabindex="500" property="productStatusCategory">
										<html:options collection="statusCategoryList" property="value"
											labelProperty="label" />
									</html:select></td>
									<th><div class="col_title_right">保管</div></th>
									<td><html:select tabindex="501" property="productStockCategory">
										<html:options collection="stockCategoryList" property="value"
											labelProperty="label" />
									</html:select></td>
									<th><div class="col_title_right">調達</div></th>
									<td><html:select tabindex="502" property="productPurvayCategory">
										<html:options collection="purvayCategoryList" property="value"
											labelProperty="label" />
									</html:select></td>
								</tr>
								<tr>
									<th><div class="col_title_right">標準化</div></th>
									<td><html:select tabindex="503" styleId="productStandardCategory" onchange="checkNMJStockCtlCategory();"
										property="productStandardCategory">
										<html:options collection="standardCategoryList" property="value"
											labelProperty="label" />
									</html:select></td>
									<th><div class="col_title_right">特注計算掛率</div></th>
									<td><html:text styleClass="numeral_commas BDCrate" styleId="soRate" property="soRate" style="width: 100px;" tabindex="504" onchange="applyStatsAlignment($(this));" maxlength="6"/></td>
									<th><div class="col_title_right">セット</div></th>
									<td>
										<html:select styleId="setTypeCategory" property="setTypeCategory" tabindex="505">
											<html:options collection="setTypeCategoryList" property="value" labelProperty="label" />
										</html:select>
									</td>
								</tr>

								<tr>
									<th><div class="col_title_right">カテゴリ（大）</div></th>
									<td colspan="5">
										<html:select styleClass="ProductClass1_TopEmpty" styleId="product1" property="product1" tabindex="506" style="width: 500px;">
											<html:options collection="product1List" property="value" labelProperty="label" />
										</html:select>
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">カテゴリ（中）</div></th>
									<td colspan="5">
										<html:select styleClass="ProductClass2_TopEmpty" styleId="product2" property="product2" tabindex="507" style="width: 500px;">
											<html:options collection="product2List" property="value" labelProperty="label" />
										</html:select>
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">カテゴリ（小）</div></th>
									<td colspan="5">
										<html:select styleClass="ProductClass3_TopEmpty" styleId="product3" property="product3" tabindex="508" style="width: 500px;">
											<html:options collection="product3List" property="value" labelProperty="label" />
										</html:select>
									</td>
								</tr>
							</table>
						</div><!-- /.section_body -->
					</div><!-- /.form_section -->
				</div><!-- /.form_section_wrap -->

			    <div class="form_section_wrap">
				    <div class="form_section">
				        <div class="section_title">
							<span>特性分類</span>
				            <button class="btn_toggle" />
						</div><!-- /.section_title -->

						<div id="order_section" class="section_body">
							<table class="forms" summary="特性分類">
							<!-- <table border="1"> -->
								<tr>
									<th><div class="col_title_right">単位</div></th>
									<td>
										<html:select styleId="unitCategory" property="unitCategory" tabindex="600" style="width: 150px;">
											<html:options collection="unitList" property="value" labelProperty="label" />
										</html:select>
									</td>
									<th><div class="col_title_right">重量</div></th>
									<td>
										<html:text styleClass="numeral_commas" styleId="weight" property="weight" style="width: 100px; ime-mode:disabled;" tabindex="601" maxlength="6"/>
										<html:select styleId="weightUnitSizeCategory" property="weightUnitSizeCategory" tabindex="602" style="width: 150px;">
											<html:options collection="weightUnitList" property="value" labelProperty="label" />
										</html:select>
									</td>
									<th><div class="col_title_right">長さ</div></th>
									<td>
										<html:text styleClass="numeral_commas"  style="width: 100px; ime-mode:disabled;" tabindex="603" property="length" maxlength="6"/>
										<html:select tabindex="604" property="lengthUnitSizeCategory" style="width: 150px;">
											<html:options collection="lengthUnitList" property="value" labelProperty="label" />
										</html:select>
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">サイズ（幅）</div></th>
									<td>
										<html:text styleClass="numeral_commas"  style="width: 100px; ime-mode:disabled;" tabindex="605" property="width" maxlength="6"/>
										<html:select tabindex="606" property="widthUnitSizeCategory" style="width: 150px;">
											<html:options collection="lengthUnitList" property="value" labelProperty="label" />
										</html:select>
									</td>
									<th><div class="col_title_right">サイズ（奥）</div></th>
									<td>
										<html:text styleClass="numeral_commas"  style="width: 100px; ime-mode:disabled;" tabindex="607" property="depth" maxlength="6" />
										<html:select tabindex="608" property="depthUnitSizeCategory" style="width: 150px;">
											<html:options collection="lengthUnitList" property="value" labelProperty="label" />
										</html:select>
									</td>
									<th><div class="col_title_right">サイズ（高）</div></th>
									<td>
										<html:text styleClass="numeral_commas"  styleId="height" property="height" style="width: 100px; ime-mode:disabled;" tabindex="609" maxlength="6"/>
										<html:select styleId="heightUnitSizeCategory" property="heightUnitSizeCategory" tabindex="610" style="width: 150px;">
											<html:options collection="lengthUnitList" property="value" labelProperty="label" />
										</html:select>
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">芯数</div></th>
									<td><html:text styleId="coreNum" property="coreNum" style="width: 100px; ime-mode: disabled;" tabindex="611" maxlength="5" /></td>
									<th>&nbsp;</th>
									<td>&nbsp;</td>
									<th>&nbsp;</th>
									<td>&nbsp;</td>
								</tr>
							</table>
						</div><!-- /.section_body -->
					</div><!-- /.form_section -->
				</div><!-- /.form_section_wrap -->

			    <div class="form_section_wrap">
				    <div class="form_section">
				        <div class="section_title">
							<span>備考欄</span>
				            <button class="btn_toggle" />
						</div><!-- /.section_title -->

						<div id="order_section" class="section_body">
							<table class="forms" summary="備考欄">
								<colgroup>
									<col span="1" style="width: 13%">
									<col span="1" style="width: 87%">
								</colgroup>
								<tr>
									<th><div class="col_title_right">備考</div></th>
									<td>
										<html:textarea styleId="remarks" property="remarks" style="width:750px; height:45px;" tabindex="700" rows="2"/>
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">ピッキング備考</div></th>
									<td>
										<html:textarea styleId="eadRemarks" property="eadRemarks" style="width: 750px; height:45px;" tabindex="701" rows="2"/>
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">コメント</div></th>
									<td>
										<html:textarea styleId="commentData" property="commentData" style="width: 750px; height:45px;" tabindex="702" rows="2"/>
									</td>
								</tr>
							</table>
						</div><!-- /.section_body -->
					</div><!-- /.form_section -->
				</div><!-- /.form_section_wrap -->

				<div style="text-align: right; width: 1160px;">
					<span>登録日：${creDatetmShow} 更新日:${updDatetmShow}</span>
					<html:hidden styleId="creDatetm" property="creDatetm"/>
					<html:hidden styleId="updDatetm" property="updDatetm"/>
					<html:hidden property="creDatetmShow"/>
					<html:hidden property="updDatetmShow"/>

					<button type="button"  class="btn_medium" tabindex="750" onclick="onF1()">初期化</button>
					<button type="button"  class="btn_medium" tabindex="751" onclick="onF3();"
						<c:if test="${!isUpdate}">disabled</c:if> >
						<c:if test="${!editMode}">登録</c:if>
						<c:if test="${editMode}">更新</c:if>
					</button>
					<button type="button"  class="btn_medium" tabindex="752" onclick="onF4();"
						<c:if test="${!editMode || !isUpdate}">disabled</c:if> >削除</button>
				</div>

				<c:forEach var="bean" items="${stockCtlCategoryList}" varStatus="status">
					<input type="hidden" name="stockCtlCategoryList[${status.index}].value" value="${bean.value}">
					<input type="hidden" name="stockCtlCategoryList[${status.index}].label" value="${f:h(bean.label)}">
				</c:forEach>
				<c:forEach var="bean" items="${standardCategoryList}" varStatus="status">
					<input type="hidden" name="standardCategoryList[${status.index}].value" value="${bean.value}">
					<input type="hidden" name="standardCategoryList[${status.index}].label" value="${f:h(bean.label)}">
				</c:forEach>
				<c:forEach var="bean" items="${statusCategoryList}" varStatus="status">
					<input type="hidden" name="statusCategoryList[${status.index}].value" value="${bean.value}">
					<input type="hidden" name="statusCategoryList[${status.index}].label" value="${f:h(bean.label)}">
				</c:forEach>
				<c:forEach var="bean" items="${stockCategoryList}" varStatus="status">
					<input type="hidden" name="stockCategoryList[${status.index}].value" value="${bean.value}">
					<input type="hidden" name="stockCategoryList[${status.index}].label" value="${f:h(bean.label)}">
				</c:forEach>
				<c:forEach var="bean" items="${purvayCategoryList}" varStatus="status">
					<input type="hidden" name="purvayCategoryList[${status.index}].value" value="${bean.value}">
					<input type="hidden" name="purvayCategoryList[${status.index}].label" value="${f:h(bean.label)}">
				</c:forEach>
				<c:forEach var="bean" items="${setTypeCategoryList}" varStatus="status">
					<input type="hidden" name="setTypeCategoryList[${status.index}].value" value="${bean.value}">
					<input type="hidden" name="setTypeCategoryList[${status.index}].label" value="${f:h(bean.label)}">
				</c:forEach>
				<c:forEach var="bean" items="${product1List}" varStatus="status">
					<input type="hidden" name="product1List[${status.index}].value" value="${bean.value}">
					<input type="hidden" name="product1List[${status.index}].label" value="${f:h(bean.label)}">
				</c:forEach>

				<span id="product2Hidden">
					<c:forEach var="bean" items="${product2List}" varStatus="status">
						<input type="hidden" name="product2List[${status.index}].value" value="${bean.value}">
						<input type="hidden" name="product2List[${status.index}].label" value="${f:h(bean.label)}">
					</c:forEach>
				</span>
				<span id="product3Hidden">
				<c:forEach var="bean" items="${product3List}" varStatus="status">
					<input type="hidden" name="product3List[${status.index}].value" value="${bean.value}">
					<input type="hidden" name="product3List[${status.index}].label" value="${f:h(bean.label)}">
				</c:forEach>
				</span>

				<c:forEach var="bean" items="${unitList}" varStatus="status">
					<input type="hidden" name="unitList[${status.index}].value" value="${bean.value}">
					<input type="hidden" name="unitList[${status.index}].label" value="${f:h(bean.label)}">
				</c:forEach>
				<c:forEach var="bean" items="${weightUnitList}" varStatus="status">
					<input type="hidden" name="weightUnitList[${status.index}].value" value="${bean.value}">
					<input type="hidden" name="weightUnitList[${status.index}].label" value="${f:h(bean.label)}">
				</c:forEach>
				<c:forEach var="bean" items="${lengthUnitList}" varStatus="status">
					<input type="hidden" name="lengthUnitList[${status.index}].value" value="${bean.value}">
					<input type="hidden" name="lengthUnitList[${status.index}].label" value="${f:h(bean.label)}">
				</c:forEach>
			</s:form>

			<form id="LoadForm" action="${f:url('load')}" method="POST">
				<input type="hidden" id="productCode" name="productCode">
			</form>
		</div>
	</div>
</body>

</html>
