<%@page import="jp.co.arkinfosys.common.Constants"%>
<%@page import="jp.co.arkinfosys.common.CategoryTrns"%>
<%@page import="jp.co.arkinfosys.common.SlipStatusCategoryTrns"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><bean:message key='titles.system'/>　受注入力画面</title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript" src="${f:url('/scripts/salescommon.js')}"></script>
	<script type="text/javascript">
	<!--
    var MAX_LINE_SIZE = 35;

	// 税転嫁の確保領域
	var catArray = {};
	// 数量の丸め（桁=自社マスタ.数量少数桁、丸め=自社マスタ.商品端数処理）
	var quantityAlignment = ${mineDto.numDecAlignment};
	var quantityCategory = ${mineDto.productFractCategory};

	var defaultStatusCode = '${defaultStatusCode}';
	var defaultStatusName = '${defaultStatusName}';

	// 単価・金額の丸め（桁=0固定[円の場合]、丸め=得意先マスタ.単価端数処理　[得意先未指定]自社マスタ.単価端数処理）
	var priceAlignment = scale_0;
	// 消費税の丸め（桁=0固定[円の場合]、丸め=得意先マスタ.税端数処理　[得意先未指定]自社マスタ.税端数処理）
	var taxAlignment = scale_0;
	// 粗利益率の丸め（桁=自社マスタ.統計少数桁、丸め=四捨五入固定）
	var rateAlignment = ${mineDto.statsDecAlignment};
	var rateCategory = scale_half_up;

    var maxLineCount;
	// ================================================================================================
	// Ajax共通
	// ================================================================================================
	function ajaxErrorCallback(xmlHttpRequest, textStatus, errorThrown) {
		if (xmlHttpRequest.status == 401) {
			// 認証エラー
			alert(xmlHttpRequest.responseText);
			window.location.doHref(contextRoot + "/login");
		} else {
			// その他のエラー
			alert(xmlHttpRequest.responseText);
		}
	}

    var trCloneBase = null;
    var StatusList;
    function init() {

    	// 完納区分　リストを取得
    	StatusList = {
    	<c:forEach var="sList" varStatus="s" items="${statusCategoryList}">
    		"${sList.value}":"${sList.label}"
    		<c:if test="${!s.last}">
       			,
    		</c:if>
    	</c:forEach>
    	};

    	//デフォルトアクションの取得
    	MainDefaultAction = $("form[name='" + MainFormName + "']").attr("action");

		<c:if test="${f:h(newData)}">
	    	for (var i = 0; i < maxLineCount; i++) {
	            // 各行の税率
	        	$("#productRow_" + (i+1) + "_ctaxRate_hidden").val($("#ctaxRate").val());
	        }
		</c:if>



		// 端数処理初期設定
		applyNumeralStyles(true);

        // 行追加用オブジェクト
        trCloneBase = $("#productRow_1").clone(true);

		// 敬称リストの保持
		catArray["#deliveryPcPreCategory"] = $("#deliveryPcPreCategory").clone(true);
		if( ${!isOnlineOrder} && $("#deliveryCode").val() == "" ){
    		catrgoryListCtrl("#deliveryPcPreCategory","");
		}
		else if( ${!isOnlineOrder} && $("#deliveryPcPreCategory").val() != ""){
			catrgoryListCtrl("#deliveryPcPreCategory", $("#deliveryPcPreCategory").val() );
		}

		// 通販サイトデータか否かで初期状態で無効となっている項目にもENTERキーでのフォーカス移動を設定
		if( ${isOnlineOrder} ) {
			$("#deliveryCode").bind("keypress", move_focus_to_next_tabindex );
		}
		else {
			$("#deliveryName").bind("keypress", 'return',move_focus_to_next_tabindex );
		}

		// 通販サイトデータの場合のみ、初期表示時に数値チェックを行う。
		if( ${isOnlineOrder} ) {
			<c:if test="${f:h(newData)}">
			checkInitImport();
			</c:if>
		}

		// 初期フォーカス設定
//		$("#roDate").focus();
		if( $("#roSlipId").val() != "" ){
			$("#roSlipId").attr("readOnly", "true");
			$("#roSlipId").addClass("c_disable");
			$("#roSlipId").css("background-color", "#CCCCCC");
			$("#roSlipId").css("border-top", "2px solid #AEAEAE");
			$("#roDate").focus();
		}else{
			$("#roSlipId").focus();
		}

		$("#customerCode").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁

	}

	function findSlip(){
		if( $("#roSlipId").val() == "" ){
		}else{
			showNowSearchingDiv();
    		ActionSubmit(MainFormName,MainDefaultAction,"load");
		}
	}

	function applyNumeralStyles(hasChanged){
		for(var i=1; i<=maxLineCount; i++) {
			// 丸めの少数桁と端数処理を設定する
			applyNumeralStylesToObj(quantityCategory,quantityAlignment,$("#productRow_" + i + "_restQuantity"));
			applyNumeralStylesToObj(quantityCategory,quantityAlignment,$("#productRow_" + i + "_quantity"));
			applyNumeralStylesToObj(quantityCategory,quantityAlignment,$("#productRow_" + i + "_possibleDrawQuantity"));
			applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#productRow_" + i + "_unitCost"));
			applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#productRow_" + i + "_unitRetailPrice"));
			applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#productRow_" + i + "_cost"));
			applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#productRow_" + i + "_retailPrice"));
		}

		// 粗利益 gross
		applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#grossDisp"));
		// 粗利益率 grossRatio
		applyNumeralStylesToObj(rateCategory,rateAlignment,$("#grossRatioDisp"));
		// 金額合計 retailPriceTotal
		applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#retailPriceTotalDisp"));
		// 消費税 ctaxPriceTotal
		applyNumeralStylesToObj($("#taxFractCategory").val(),taxAlignment,$("#ctaxPriceTotalDisp"));
		// 伝票合計 priceTotal
		applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#priceTotalDisp"));

		// 下部の合計欄を計算する
		calcSum(hasChanged);

		// カンマをつける
		_after_load($(".numeral_commas"));
	}

    //メインフォームの名前
    var MainFormName = "rorder_inputROrderActionForm";
    //メインフォームのデフォルトのアクション保持用
    var MainDefaultAction;

    //フォームのアクションをいじってからサブミットする。
    function ActionSubmit(FormName,DefaultActionName,ActionName){
    	$("form[name='" + FormName + "']").attr("action",DefaultActionName + ActionName);
    	$("form[name='" + FormName + "']").submit();
    	$("form[name='" + FormName + "']").attr("action",DefaultActionName);
    }

    function openZipCodeDialog() {
		if ($("#customerCode").val() != "<%=Constants.EXCEPTIANAL_CUSTOMER_CODE.ONLINE_ORDER%>" ){
			return;		// 通販サイト顧客以外は未処理
		}
    	openSearchZipDialog('delivery', setZipCode);
    	$('#delivery_zipCode').val($('#deliveryZipCode').val());
    }

    function setZipCode(id, map) {
        $("#deliveryZipCode").val(map["zipCode"]);
        $("#deliveryAddress1").val(map["zipAddress1"]);
        $("#deliveryAddress2").val(map["zipAddress2"]);
    }

    function searchZipCodeDirect() {
		if ($("#customerCode").val() != "<%=Constants.EXCEPTIANAL_CUSTOMER_CODE.ONLINE_ORDER%>" ){
			return;		// 通販サイト顧客以外は未処理
		}

		// 入力チェック
		var val = $("#deliveryZipCode").val();
		if (!val) {
			return;
		}

		// 検索実行
		$("#errors").empty();
		var data = {"zipCode" : val};
		asyncRequest(contextRoot + "/ajax/master/searchZipCodeAjax/search", data,
			function(data) {
				var results = eval(data);
				if (results.length!=1) {
					var message = "<bean:message key="warns.zipcode.notidentical" arg0="納入先"/>";
					$("#errors").append(message);
				}
				else {
					$("#deliveryZipCode").val(results[0].zipCode);
					$("#deliveryAddress1").val(results[0].zipAddress1);
					$("#deliveryAddress2").val(results[0].zipAddress2);
				}
			}
		);
	}

	function onF1() {
    	if(confirm("<bean:message key='confirm.init'/>")){
			showNowSearchingDiv();
            location.doHref(contextRoot + "/rorder/inputROrder/");
            $("#roSlipId").focus();
         }
	}

	function onF2() {
    	if(confirm("<bean:message key='confirm.delete'/>")){
    		ActionSubmit(MainFormName,MainDefaultAction,"delete");
    	}
	}

	function onF3() {
<c:if test="${f:h(newData)}">
    	if(confirm("<bean:message key='confirm.insert'/>")){
</c:if>
<c:if test="${f:h(!newData)}">
    	if(confirm("<bean:message key='confirm.update'/>")){
</c:if>

    		_before_submit($(".numeral_commas"));
    		ActionSubmit(MainFormName,MainDefaultAction,"upsert");
    	}
	}

    // 伝票複写で指定された伝票を読み込む
    function copySlipCallback(dialogId, slipName, slipId){
    	$("form[name='" + MainFormName + "']").attr("action", '${f:url("/rorder/inputROrder/copySlip/'+slipId+'")}');
    	$("form[name='" + MainFormName + "']").submit();
	}


    function setUser(id, map) {
        var formObj = document.forms[MainFormName];
        formObj.userId.value = map["userId"];
        formObj.userName.value = map["nameKnj"];
    }

	//リストの絞込み
	function catrgoryListCtrl(targetName,value){
		$(targetName + " option").each(
			function(i){
				$(this).remove();
		});

		var elmSel = catArray[targetName];
		var elmOpt = elmSel.children(":first");
		for( i=0 ; i < elmSel.get(0).length ; i++ ){
			if( elmOpt.get(0).value == value ){
				$(targetName).append($('<option>').attr("value",value).text(elmOpt.get(0).text));
				return;
			}
			elmOpt = elmOpt.next();
		}
	}
    //リストへの追加
	function catrgoryListInit(targetName){
		$(targetName + " option").each(
			function(i){
				$(this).remove();
		});

		var elmSel = catArray[targetName];
		var elmOpt = elmSel.children(":first");
		for( i=0 ; i < elmSel.get(0).length ; i++ ){
			$(targetName).append($('<option>').attr("value",elmOpt.get(0).value).text(elmOpt.get(0).text));
			elmOpt = elmOpt.next();
		}
	}

    function setOnlineOrderReadOnly(readOnlyFlag){
        if( readOnlyFlag == true ){
            $("#isOnlineOrder").val(false);
    		$("#deliveryCode").attr("disabled", false);
    		$("#deliveryCode").show();
    		$("#deliveryName").hide();
    		$("#deliveryPcPreCategory").attr("class", "c_disable");
        	$("#delivery_info").find("input[type='text']").attr('readOnly', true);
        	$("#delivery_info").find("input[type='text']").attr('class', 'c_disable');
        	InitDeliveryInfos();
        	catrgoryListCtrl("#deliveryPcPreCategory","");
        }else{
        	$("#isOnlineOrder").val(true);
    		$("#deliveryCode").attr("disabled", true );
    		$("#deliveryCode").hide();
    		$("#deliveryName").attr("disabled", false );
    		$("#deliveryName").show();
    		$("#deliveryPcPreCategory").attr("class", "");
        	$("#delivery_info").find("input[type='text']").attr('readOnly', false);
        	$("#delivery_info").find("input[type='text']").attr('class', '');
        	InitDeliveryInfos();
        	catrgoryListInit("#deliveryPcPreCategory");
        }
    }

    function setCustomer(id, map) {
        var formObj = document.forms[MainFormName];
        formObj.customerCode.value = map["customerCode"];
        formObj.customerName.value = map["customerName"];
       	formObj.customerRemarks.value = map["remarks"];
       	formObj.customerCommentData.value = map["commentData"];
        setSingleRowPullDown("cutoffGroupCategory", map, "categoryCodeName", "categoryCode");
        setSingleRowPullDown("taxShiftCategory", map, "categoryCodeName2", "categoryCode2");
        setSingleRowPullDown("salesCmCategory", map, "categoryCodeName3", "categoryCode3");
        $("#taxFractCategory").val(map["taxFractCategory"]);
        $("#priceFractCategory").val(map["priceFractCategory"]);

        var check = map["customerRoCategory"];

        // 受注停止区分が「取引停止」の場合
        if (check == "<%=CategoryTrns.RECIEVE_ORDER_STOP%>") {
            alert("<bean:message key='alert.orderStop'/>");
        }

        // 受注停止区分が「入金遅延」の場合
        if (check == "<%=CategoryTrns.RECIEVE_ORDER_DEPOSIT_LATE%>") {
            alert("<bean:message key='alert.orderDepositLate'/>");
        }
        // 通販サイト顧客の場合、納入先を入力可能にする
        if( map["customerCode"] == "<%=Constants.EXCEPTIANAL_CUSTOMER_CODE.ONLINE_ORDER%>" ){
            setOnlineOrderReadOnly(false);
        }else{
        	setOnlineOrderReadOnly(true);
		       // 納入先リストを取りに行く
			var data = new Object();
			data["customerCode"] = map["customerCode"];
			asyncRequest(
				contextRoot + "/ajax/commonDelivery/getDeliveryListByCustomerCodeSortedByCreDate",
				data,
				function(data) {
					if (data == "") {
						InitDeliveryInfos();
						return;
					}
					var list = eval("("+data+")");
					setDeliveryList(list);

					// 先頭は空白状態なので、２番目を選択する
					$('#deliveryCode').get(0).selectedIndex = 1;

					// 納入先情報を取りに行く
					changeDelivery();
				}
			);
        }
        // 端数処理設定
		applyNumeralStyles(true);
        // 合計金額を計算する
        calcSum(true);
    }

	//納入先情報フォームIDリスト
	var DeliveryInfosIDList = new Array(
			"deliveryCode",
            "deliveryName",
            "deliveryKana",
			"deliveryOfficeName",
            "deliveryOfficeKana",
			"deliveryDeptName",
			"deliveryZipCode",
			"deliveryAddress1",
			"deliveryAddress2",
			"deliveryPcName",
			"deliveryPcKana",
			"deliveryPcPreCategory",
            "deliveryPcPre",
			"deliveryTel",
			"deliveryFax",
			"deliveryEmail",
            "deliveryUrl"
	);

	function changeDelivery(){
        if ($("#deliveryCode").attr("value") == "") {
            InitDeliveryInfos();
        } else {
    		asyncRequest(
    			contextRoot + "/ajax/commonDelivery/getDeliveryInfosByDeliveryCode/" + $("#deliveryCode").attr("value"),
    			{},
    			function(data) {
    				if(data==""){
    					InitDeliveryInfos();
    				}else{
    					var value = eval("(" + data + ")");
    					setDeliveryInfos( value );
    				}
    			}
    		);
        }
	}

	//顧客情報の初期化
	function InitCustomerInfos(){
		$("#customerName").val("");
		$("#customerRemarks").val("");
		$("#customerCommentData").val("");
		$("#taxShiftCategory").get(0).options.length=0;
		$("#salesCmCategory").get(0).options.length=0;
		$("#cutoffGroupCategory").get(0).options.length=0;

		// 端数処理を変数に設定
		$("#taxFractCategory").val(${mineDto.taxFractCategory});
		$("#priceFractCategory").val(${mineDto.priceFractCategory});
		applyNumeralStyles(true);
	}

	//納入先情報の初期化
	function InitDeliveryInfos(){
		for(var i = 0; i < DeliveryInfosIDList.length; i++){
			$("#" + DeliveryInfosIDList[i]).val("");
		}

        // 敬称の調整
        var obj = $("#deliveryPcPreCategory").get(0);
        obj.options.length = 0;
	}

	//顧客情報の取得
	function GetCustomerInfos(){
		if($("#customerCode").val()==""){
			InitCustomerInfos();
    	    InitDeliveryInfos();
            $("#deliveryCode").get(0).options.length=0;
        	setOnlineOrderReadOnly(true);
            return;
		}
		var data = new Object();
		data["customerCode"] = $("#customerCode").val();
		asyncRequest(
			contextRoot + "/ajax/commonCustomer/searchByCustomerCode",
			data,
			function(data) {
				if(data==""){
					alert('<bean:message key="errors.customer.not.exist.code" />');
					InitCustomerInfos();
            	    InitDeliveryInfos();
                    $("#deliveryCode").get(0).options.length=0;
                	setOnlineOrderReadOnly(true);
				}else{
                    var customer = eval("(" + data + ")");
                    var map = new Object();
                    map["customerCode"] = customer.customerCode;
                    map["customerName"] = customer.customerName;
                    map["remarks"] = customer.remarks;
                    map["commentData"] = customer.commentData;
                    map["categoryCode"] = customer.categoryCode;
                    map["categoryCodeName"] = customer.categoryCodeName;
                    map["categoryCode2"] = customer.categoryCode2;
                    map["categoryCodeName2"] = customer.categoryCodeName2;
                    map["categoryCode3"] = customer.categoryCode3;
                    map["categoryCodeName3"] = customer.categoryCodeName3;
                    map["taxFractCategory"] = customer.taxFractCategory;
                    map["priceFractCategory"] = customer.priceFractCategory;
                    map["customerRoCategory"] = customer.customerRoCategory;
                    setCustomer('customer', map);
				}
			}
		);
	}

	// 納入先情報の設定
	function setDeliveryInfos( value ){
        var formObj = document.forms[MainFormName];

		for(var i=0; i < DeliveryInfosIDList.length; i++){
			$("#"+DeliveryInfosIDList[i]).val(value[DeliveryInfosIDList[i]]);
		}
        // 敬称の調整
        setSingleRowPullDown("deliveryPcPreCategory", value, "categoryCodeName", "deliveryPcPreCategory", "deliveryPcPre");
	}

    function setSingleRowPullDown(id, map, name, value, hiddenId) {

        var obj = $("#" + id).get(0);
        obj.options.length = 1;
        obj.options[0].value = map[value];
        obj.options[0].text = map[name];

        if ($("#" + hiddenId).get(0)) {
            $("#" + hiddenId).val(map[name]);
        }
        obj.options.selectedIndex = 0;
    }

	//納入先リストの作成
	function setDeliveryList( value ){
		// クリア
		initDeliveryList();

		// 要素数確認
		var length = 0;
		for( var key in value ){
			length++
		};

		// 追加
		// 先頭要素は空欄
		$("#deliveryCode").append($('<option>').attr("value","").text(""));
		for( i=0 ; i < length/2 ; i++ ){
			var val = value["value"+i];
			var name = value["name"+i];
			$("#deliveryCode").append($('<option>').attr("value",val).text(name));
		}
	}

	//納入先リストのクリア
	function initDeliveryList(){
		// クリア
		$("#deliveryCode" + " option").each(
			function(i){
				$(this).remove();
		});
	}

    function setInit(dlgId, id) {
        $("#" + dlgId + "_productCode").val($("#" + id).val());
    }

    var execLineNo = 0;
    function openSearchProduct(event) {
        var lineNo = event.data.lineNo;
        execLineNo = lineNo;

        if($("#productRow_" + lineNo + "_code").attr("readonly")) {
            return;
        }

        openSearchProductDialog('product', callbackOpenSearchProductDialog);
        setInit("product", "productRow_" + lineNo + "_code");

    }

    // 商品検索ダイアログで商品を選択後に呼ばれる
    function callbackOpenSearchProductDialog(id,map, lineNo){

        // 選択された商品の在庫情報を取得しマップに設定する
        searchProductStock(id,map, lineNo);

    }

	//在庫情報を取得する
	function searchProductStock(id,map, lineNo){
        var data = new Object();
		data["productCode"] = map["productCode"];

		asyncRequest(
			contextRoot + "/ajax/dialog/showStockInfoDialog/calcStock",
			data,
			function(data) {
				if(data!=""){
					var value = eval("(" + data + ")");
					setProductStock(id,map, lineNo,value);
				}
			}
		);

    	return false;
	}

	//商品在庫情報を取得する
	function setProductStock(id,map, lineNo,value){
		// 引当可能数
		map["possibleDrawQuantity"] = value["possibleDrawQuantity"];

        // 商品情報を画面に設定する
    	SetProductInfosToFroms(id,map, lineNo);

	}

    function searchProduct(event) {
        var lineNo = event.data.lineNo;
        var productCode = $("#productRow_" + lineNo + "_code").val();
    	var label = '<bean:message key="labels.productCode" />';
        if (productCode == "") {
			SetProductInfosToFroms(lineNo, null, lineNo);
            return;
    	}else{

    		var data = new Object();
    		data["productCode"] = productCode;
        	asyncRequest(
        		contextRoot + "/ajax/commonProduct/getProductInfos",
        		data,
        		function(data) {
        			if(data==""){
        				alert('<bean:message key="errors.notExist" arg0="'+label+'" />');
        				SetProductInfosToFroms(lineNo, null, lineNo);
        			}else{
        				var value = eval("(" + data + ")");
        				SetProductInfosToFroms(lineNo,value, lineNo);
        			}
        		}
        	);
    	}
    	return false;
    }

    function SetProductInfosToFroms(id,map, lineNo){
        var localId = 'productRow_' + execLineNo;
        if (lineNo) {
            localId = 'productRow_' + lineNo;
        }

        // タイムサービスの場合は、売上取引区分と比較。掛売以外はエラーメッセージを表示する
        if (map == null) {
        	// 得意先商品コード
        	$("#" + localId + "_customerPcode").val("");

        	// 商品名
        	$("#" + localId + "_name").text("");
        	$("#" + localId + "_name_hidden").val("");

            // 数量
            $("#" + localId + "_quantity").val("");

            // 棚番
            $("#" + localId + "_rack").val("");

            // 受注残数
            $("#" + localId + "_restQuantity").text("");
            $("#" + localId + "_restQuantity_hidden").val("");
            $("#" + localId + "_restQuantity_db_hidden").val("");

            // 課税区分
            $("#" + localId + "_taxCategory").val("");

            // 完納区分（＝未納）
            $("#" + localId + "_statusName").text("");
            $("#" + localId + "_status").val(defaultStatusCode);

        	// 仕入単価 SUPPLIER_PRICE_YEN
        	$("#" + localId + "_unitCost").val("");

        	// 仕入金額 SUPPLIER_PRICE_YEN
        	$("#" + localId + "_cost").val("");

        	// 売上単価 RETAIL_PRICE
        	$("#" + localId + "_unitRetailPrice").val("");

        	// 売上金額 RETAIL_PRICE
        	$("#" + localId + "_retailPrice").val("");

        	// 相手先コード（仕入先品番）
        	$("#" + localId + "_supplierPcode").val("");
        	// 受注限度数
        	$("#" + localId + "_roMaxNum").val("");
        	// 在庫情報
        	// 引当可能数
        	$("#" + localId + "_possibleDrawQuantity").val("");

            // 備考
            $("#" + localId + "_remarks").val("");

            // ピッキング備考
            $("#" + localId + "_eadRemarks").val("");

            // 商品備考
            $("#" + localId + "_productRemarks").val("");

            // 在庫管理区分
            $("#" + localId + "_stockCtlCategory_hidden").val("");
        } else {
        	// 廃止されている商品は警告ダイアログを表示し、処理を続行する。
        	if(map["discarded"] == "1"){
        		alert('<bean:message key="warns.product.discarded" />');
        	}

        	// 上書き確認
        	if(!checkProductWrite(localId)){
        		return;
        	}
        	// 商品コード
        	$("#" + localId + "_code").val(map["productCode"]);

        	// 特殊商品コードの数量を１にする
    		sc_setLooseExceptianalProductCodeQuantiry($("#" + localId + "_code").val(),
    				$("#" + localId + "_quantity"))

        	// 得意先商品コード
        	$("#" + localId + "_customerPcode").val(map["onlinePcode"]);

        	// 商品名
        	$("#" + localId + "_name").text(map["productName"]);
        	$("#" + localId + "_name_hidden").val(map["productName"]);

            // 棚番
            $("#" + localId + "_rack").val(map["rackCode"]);

            // 受注残数
            $("#" + localId + "_restQuantity").text($("#" + localId+ "_quantity").val());
            $("#" + localId + "_restQuantity_hidden").val(dec_numeral_commas($("#" + localId + "_quantity").val()));
            $("#" + localId + "_restQuantity_db_hidden").val(dec_numeral_commas($("#" + localId + "_quantity").val()));

            // 課税区分
            $("#" + localId + "_taxCategory_hidden").val(map["taxCategory"]);

            // 消費税率
            //$("#" + localId + "_ctaxRate_hidden").val(${taxRate});

            // 完納区分（＝未納）
            $("#" + localId + "_statusName").text('${defaultStatusName}');
            $("#" + localId + "_status").val(${defaultStatusCode});

        	// 仕入単価 SUPPLIER_PRICE_YEN
        	$("#" + localId + "_unitCost").val(map["supplierPriceYen"]);
            _set_commas($("#" + localId + "_unitCost"));

        	// 仕入金額 SUPPLIER_PRICE_YEN
        	$("#" + localId + "_cost").val("");

        	// 売上単価 RETAIL_PRICE
        	$("#" + localId + "_unitRetailPrice").val(map["retailPrice"]);
            _set_commas($("#" + localId + "_unitRetailPrice"));

        	// 売上金額 RETAIL_PRICE
        	$("#" + localId + "_retailPrice").val("");

        	// 相手先コード（仕入先品番）
        	$("#" + localId + "_supplierPcode").val(map["supplierPcode"]);
        	// 受注限度数
        	$("#" + localId + "_roMaxNum").val(map["roMaxNum"]);
        	// 在庫情報
        	// 引当可能数
        	$("#" + localId + "_possibleDrawQuantity").val(map["possibleDrawQuantity"]);

            // 備考
            $("#" + localId + "_productRemarks").val(map["remarks"]);

            // ピッキング備考
            $("#" + localId + "_eadRemarks").val(map["eadRemarks"]);

            // 在庫管理区分
            $("#" + localId + "_stockCtlCategory_hidden").val(map["stockCtlCategory"]);
        }


		searchBulkPrice(localId);
//        if( $("#" + localId + "_quantity").val() != "" ){
//       	checkQuantity(localId);
//        }

        // 仕入金額と売価金額を計算する
        calcCostAndRetail(localId);

        execLineNo = 0;

        // カンマをつける
		_after_load($(".numeral_commas"));
    }

    //まとめ買い値引き単価を取得する
	function searchBulkPrice(id, curQuantity){

		// オンライン注文の場合は、まとめ買い値引きは摘要しない
		if( $("#customerCode").val() == "<%=Constants.EXCEPTIANAL_CUSTOMER_CODE.ONLINE_ORDER%>" ){
			return;
		}

		// 分納中の場合
		var status = $("#" + id + "_status").val();
		// 数量が変更された際、分納中で、かつ、まとめ買い値引きが摘要されている場合は、警告メッセージを表示する。
		if(status == "<%=SlipStatusCategoryTrns.RO_LINE_PROCESSING%>"){
			var db_quantity = oBDCS($("#" + id + "_quantity_hidden").val()).setSettingsFromObj($("#" + id + "_quantity_hidden"));
			var quantity = oBDCS($("#" + id + "_quantity").val()).setSettingsFromObj($("#" + id + "_quantity"));
			// 数量がDBの値から変更された場合
			if(db_quantity.value() !=quantity.value()){

				// まとめ買い値引きが摘要されているか？
				var data = new Object();
				data["bulkProductCode"] = $("#" + id + "_code").val();
				data["bulkQuantity"] = $("#" + id + "_quantity").val();

				asyncRequest(
					contextRoot + "/ajax/commonBulkRetailPrice/isDiscount",
					data,
					function(data) {
						if(data!=""){
							//　摘要されている
							// 警告メッセージ表示
							if (!(confirm('<bean:message key="confirm.rorder.quantity" />'))){
								//　キャンセルの場合は、元に戻す
								$("#" + id + "_quantity").val(curQuantity);
								_after_load($(".numeral_commas"));
							}
						}
						changeQuantity( id , curQuantity );
					}
				);
			}
		}
		// 分納中以外の場合
		else{

			var elem = $("#" + id + "_quantity");
			if(elem.val() == "") {
				changeQuantity( id );
				return;
			}
			var data = new Object();
			data["bulkProductCode"] = $("#" + id + "_code").val();
			data["bulkQuantity"] = $("#" + id + "_quantity").val();

			asyncRequest(
				contextRoot + "/ajax/commonBulkRetailPrice/getPrice",
				data,
				function(data) {
					if(data!=""){
						var value = eval("(" + data + ")");
						// 売上単価
						$("#" + id + "_unitRetailPrice").val(value);
						changeQuantity( id, curQuantity );
					}
				}
			);
		}
		return false;
	}

	function onChangeQuantity(event,curQuantity) {
		// asyncで処理しない。
		async_request_off = true;

		searchBulkPrice(event.data.id, curQuantity);
    	// オンライン注文の場合は、数量変更処理のみ動作
		if( $("#customerCode").val() == "<%=Constants.EXCEPTIANAL_CUSTOMER_CODE.ONLINE_ORDER%>" ){
			changeQuantity( event.data.id, curQuantity );
		}
	}

	function changeQuantity(myId, curQuantity) {
		checkQuantity(myId, curQuantity);
		calcCostAndRetail(myId);
	}

	function checkQuantityEvent(event) {
		checkQuantity(event.data.id);
	}

	function calcCostEvent(event) {
		calcCost(event.data.id);
	}

	function calcRetailEvent(event) {
		calcRetail(event.data.id);
	}

	function calcCostAndRetailEvent(event) {
		calcCostAndRetail(event.data.id);
	}

    function checkQuantity(id, curQuantity) {
        if( $("#" + id + "_quantity").val() == "" ){
            return;
        }
        // 数量
        var quantity = oBDCS($("#" + id + "_quantity").val()).setSettingsFromObj($("#" + id + "_quantity"));
        // 未納数（受注残数）は、再計算する。
        var db_quantity = oBDCS($("#" + id + "_quantity_hidden").val()).setSettingsFromObj($("#" + id + "_quantity_hidden"));
        var db_restquantity = oBDCS($("#" + id + "_restQuantity_db_hidden").val()).setSettingsFromObj($("#" + id + "_restQuantity_db_hidden"));
        if(!db_quantity.value()){
            // 新規の場合は、そのまま
            $("#" + id + "_restQuantity").text(quantity.BDValue().toString());
            $("#" + id + "_restQuantity_hidden").val(dec_numeral_commas(quantity.BDValue().toString()));
        }
        else{
            // [未納数]＝[数量]－（【受注伝票明細行】数量－【受注伝票明細行】残数量）
            var restquantity = quantity.BDValue() - ( db_quantity.BDValue() - db_restquantity.BDValue());
            if( restquantity < 0){
                // 未納数がマイナスになる場合は、エラーとし、元に戻す。
                alert('<bean:message key="errors.quantity.little" />');
                if(curQuantity) {
                	$("#" + id + "_quantity").val(curQuantity);
                }
                else {
                	$("#" + id + "_quantity").val(db_quantity.value());
                }
        		return;
            }
            if(quantity.BDValue() > 0 && restquantity == 0){
                // 未納数が０になった場合、売上完了とする。
            	 $("#" + id + "_status").val("9");
            	 $("#" + id + "_statusName").text(StatusList[9]);
            }
            else if (restquantity == quantity.BDValue()){
                // 未納数と数量が同じ場合、未納とする。
          		 $("#" + id + "_status").val("0");
          		 $("#" + id + "_statusName").text(StatusList[0]);
            }
            else if (restquantity > 0){
                // 未納数が正になった場合、分納中とする。
           		 $("#" + id + "_status").val("1");
           		 $("#" + id + "_statusName").text(StatusList[1]);
            }
            $("#" + id + "_restQuantity").text(enc_numeral_commas(restquantity.toString()));
            $("#" + id + "_restQuantity_hidden").val(restquantity.toString());
        }

        // 商品コードがある時のみチェック
        if( $("#" + id + "_code").val() != "" ){
            var bExceptianal = sc_isLooseExceptianalProductCode($("#" + id + "_code").val() );
	        var lquantity = Number(quantity.value());

            if(IsCheckOverQuantity(id)){
	            if(_isNum($("#" + id + "_roMaxNum").val())) {
		            // 受注限度数
		        	var roMaxNum = Number($("#" + id + "_roMaxNum").val());
			        if (lquantity > roMaxNum && !bExceptianal ) {
			    		alert('<bean:message key="warns.quantity.over.roMaxNum" />');
			        }
	            }
            }

            if(IsCheckpossibleDrawQuantity(id)){
		        // 引当可能数
		        var possibleDrawQuantity = Number(oBDCS($("#" + id + "_possibleDrawQuantity").val()).setSettingsFromObj($("#" + id + "_possibleDrawQuantity")).value());
		        // チェック２
		        if (lquantity > 0 && lquantity > possibleDrawQuantity && !bExceptianal ) {
		    		alert('<bean:message key="warns.quantity.over.possibleDrawQuantity" />');
		        }
	        }
        }
    }

	// 引当可能数チェックをするか？
	function IsCheckpossibleDrawQuantity( id ){
		// 在庫管理する商品のみ
		if($("#" + id + "_stockCtlCategory_hidden").val() == "<%=CategoryTrns.PRODUCT_STOCK_CTL_NO%>" )
			return false;

		// 特殊コード以外
		var excpProductCode = sc_isLooseExceptianalProductCode($("#" + id + "_code").val() );
		if(excpProductCode)
			return false;

		// 数量マイナス以外
        var quantity = Number(oBDCS($("#" + id + "_quantity").val()).setSettingsFromObj($("#" + id + "_quantity")).value());
		if(quantity < 0)
			return false;

		return true;

	}

	//大口受注チェックをするか？
	function IsCheckOverQuantity( id ){
		// 特殊コード以外
		var excpProductCode = sc_isLooseExceptianalProductCode($("#" + id + "_code").val() );
		if(excpProductCode)
			return false;

		// 数量マイナス以外
        var quantity = Number(oBDCS($("#" + id + "_quantity").val()).setSettingsFromObj($("#" + id + "_quantity")).value());
		if(quantity < 0)
			return false;

		return true;

	}

    //　取込の初期表示にチェックをし、警告メッセージを表示する。
    function checkInitImport() {

    	for(var i=1; i<=maxLineCount; i++) {

        	//数量関連チェック
        	if( $("#productRow_" + i + "_quantity").val() != "" ){
	            // 商品コードがある時のみチェック
		        if( $("#productRow_" + i + "_code").val() != "" ){
		            var quantity = Number(oBDCS($("#productRow_" + i + "_quantity").val()).setSettingsFromObj($("#productRow_" + i + "_quantity")).value());

			        var id = "productRow_" + i;
//			        var bExceptianal = sc_isLooseExceptianalProductCode($("#" + id + "_code").val() );

		            // 受注限度数
		             if(IsCheckOverQuantity(id)){
			        	var roMaxNum = Number($("#productRow_" + i + "_roMaxNum").val());
//				        if (quantity > roMaxNum && !bExceptianal ) {
						if (quantity > roMaxNum) {
				    		alert('<bean:message key="warns.line.quantity.over.roMaxNum" arg0="'+i+'" />');
				        }
		             }

			        // 引当可能数のチェックをするか？
			        if(IsCheckpossibleDrawQuantity(id)){
				        // 引当可能数
				        var possibleDrawQuantity = Number(oBDCS($("#productRow_" + i + "_possibleDrawQuantity").val()).setSettingsFromObj($("#productRow_" + i + "_possibleDrawQuantity")).value());
				        // チェック２
//				        if (quantity > 0 && quantity > possibleDrawQuantity && !bExceptianal) {
						if (quantity > possibleDrawQuantity) {
				    		alert('<bean:message key="warns.line.quantity.over.possibleDrawQuantity" arg0="'+i+'" />');
				        }
			        }
		        }
		     // 商品コード関連チェック
		        CheckLineProduct(i);

            }

		}
		return;
    }

	// 商品コード関連チェック
    function CheckLineProduct(lineno){
		var data = new Object();
        var productCode = $("#productRow_" + lineno + "_code").val();

        data["productCode"] = productCode;
    	asyncRequest(
    		contextRoot + "/ajax/commonProduct/getProductInfos",
    		data,
    		function(data) {
   				var value = eval("(" + data + ")");
   				CheckLineProductDetail(value, lineno);
    		}
    	);
    }

    function CheckLineProductDetail(map, lineNo){

        if (map != null) {
        	// 廃止されている商品は警告ダイアログを表示し、処理を続行する。
        	if(map["discarded"] == "1"){
        		alert('<bean:message key="warns.line.product.discarded" arg0="'+lineNo+'" />');
        	}
        }

    }

    function calcCost(id) {

        // 数量
        var quantity = oBDCS($("#" + id + "_quantity").val()).setSettingsFromObj($("#" + id + "_quantity"));

        if (!$("#" + id + "_quantity").val()) {
            return;
        }

        // 仕入単価
        var unitCost = oBDCS($("#" + id + "_unitCost").val()).setSettingsFromObj($("#" + id + "_unitCost"));

        // 仕入金額
        var cost = unitCost.BDValue() * quantity.BDValue();

        if (isNaN(cost)) {
            $("#" + id + "_cost").val("");
        } else {
            $("#" + id + "_cost").val(cost);
        }

        _set_commas($("#" + id + "_cost").get(0));

        calcSum(true);
    }
    function calcRetail(id) {

        // 数量
        var quantity = oBDCS($("#" + id + "_quantity").val()).setSettingsFromObj($("#" + id + "_quantity"));

        if (!$("#" + id + "_quantity").val()) {
            return;
        }

        // 売上単価
        var unitRetailPrice = oBDCS($("#" + id + "_unitRetailPrice").val()).setSettingsFromObj($("#" + id + "_unitRetailPrice"));

        // 売上金額
        var retailPrice = unitRetailPrice.BDValue() * quantity.BDValue();

        if (isNaN(retailPrice)) {
            $("#" + id + "_retailPrice").val("");
        } else {
            $("#" + id + "_retailPrice").val(retailPrice);
        }

        _set_commas($("#" + id + "_retailPrice").get(0));

        calcSum(true);
    }
    function calcCostAndRetail(id) {

        // 数量
        var quantity = oBDCS($("#" + id + "_quantity").val()).setSettingsFromObj($("#" + id + "_quantity"));

        if (!$("#" + id + "_quantity").val()) {
            return;
        }

        // 仕入単価
        var unitCost = oBDCS($("#" + id + "_unitCost").val()).setSettingsFromObj($("#" + id + "_unitCost"));

        // 売上単価
        var unitRetailPrice = oBDCS($("#" + id + "_unitRetailPrice").val()).setSettingsFromObj($("#" + id + "_unitRetailPrice"));

        // 仕入金額
        var cost = unitCost.BDValue() * quantity.BDValue();

        if (isNaN(cost)) {
            $("#" + id + "_cost").val("");
        } else {
            $("#" + id + "_cost").val(cost);
        }

        _set_commas($("#" + id + "_cost").get(0));
        // 売上金額
        var retailPrice = unitRetailPrice.BDValue() * quantity.BDValue();

        if (isNaN(retailPrice)) {
            $("#" + id + "_retailPrice").val("");
        } else {
            $("#" + id + "_retailPrice").val(retailPrice);
        }

        _set_commas($("#" + id + "_retailPrice").get(0));

        calcSum(true);
    }

    function calcRetailPrice(id) {

        // 数量
        var quantity = oBDCS($("#" + id + "_quantity").val()).setSettingsFromObj($("#" + id + "_quantity"));

        if (!$("#" + id + "_quantity").val()) {
            return;
        }

        // 売上単価
        var unitRetailPrice = oBDCS($("#" + id + "_unitRetailPrice").val()).setSettingsFromObj($("#" + id + "_unitRetailPrice"));

        // 売上金額
        var retailPrice = unitRetailPrice.BDValue() * quantity.BDValue();

        if (isNaN(retailPrice)) {
            $("#" + id + "_retailPrice").val("");
        } else {
            $("#" + id + "_retailPrice").val(retailPrice);
        }

        _set_commas($("#" + id + "_retailPrice").get(0));

    }

    //商品検索後の反映の上書き確認
    function checkProductWrite(id, codeCheck){
    	if($("#" + id + "_name").val()
    		|| $("#" + id + "_quantity").val()
			|| $("#" + id + "_unitCost").val()
    		|| $("#" + id + "_unitRetailPrice").val()
			|| $("#" + id + "_cost").val()
			|| $("#" + id + "_retailPrice").val()
			|| $("#" + id + "_remarks").val()
			|| $("#" + id + "_eadRemarks").val()
			|| $("#" + id + "_productRemarks").val()
			){
			// 上書き確認
			return confirm('<bean:message key="confirm.product.copy" />');
    	}

    	if (codeCheck) {
    		if ($("#" + id + "_code").val()) {
				// 上書き確認
				return confirm('<bean:message key="confirm.product.copy" />');
			}
    	}
    	return true;
    }

    function onStockButton(event) {
        var id = event.data.lineNo;
        var productCode = $("#" + id + "_code").val();
        openStockInfoDialog('stockInfo', productCode);
    }

    function deleteRow(event) {
        var lineId = event.data.lineId;
        var lineNo = event.data.lineNo;

    	if(!confirm('<bean:message key="confirm.line.delete" />')){
            return;
        }
        var trObj = $("#productRow_" + lineNo).get(0);

        var tbodyObj = $("#tbodyLine").get(0);
        tbodyObj.removeChild(trObj);
        if ($("#productRow_" + lineNo + "_roLineId").val() != "") {
            $("#deleteLineIds").val($("#deleteLineIds").val() + "," + lineId);
        }
        resetIds(lineNo);
        resetRowNo();
        maxLineCount--;
        calcSum(true);

        // ボタンの有効・無効制御
        resetButtonDisabled();
    }

    function resetIds(rowNo) {
		tabIdx = 1000 + (rowNo) * ${f:h(lineElementCount)} - 1;
        for (var i = Number(rowNo)+1; i < maxLineCount+1; i++) {

            // TR
            $("#productRow_" + i).attr("id", "productRow_" + (i-1));
            // TD (行番）
            $("#td" + i + "_1").attr("id", "td" + (i-1) + "_1");
            // 商品コード
            $("#productRow_" + i + "_code").attr("name", "lineList[" + (i-2) + "].productCode");
            $("#productRow_" + i + "_code").unbind("focus");
            $("#productRow_" + i + "_code").bind("focus", {lineNo: i-1}, function(e){ this.curVal=this.value; });
            $("#productRow_" + i + "_code").unbind("blur");
            $("#productRow_" + i + "_code").bind("blur", {lineNo: i-1}, function(e){ if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); searchProduct(e); } });
            $("#productRow_" + i + "_code").attr("id", "productRow_" + (i-1) + "_code");
			$("#productRow_" + i + "_code").attr("tabindex", (++tabIdx));

            // アイコン
            $("#productRow_" + i + "_code").unbind("click");
            $("#productRow_" + i + "_icon").bind("click", {lineNo: i-1}, openSearchProduct);
            $("#productRow_" + i + "_icon").attr("id", "productRow_" + (i-1) + "_icon");
			$("#productRow_" + i + "_icon").attr("tabindex", (++tabIdx));

			var thisRoLineId = $("#productRow_" + i + "_roLineId").val();

            // hidden
            $("#productRow_" + i + "_roLineId").attr("name", "lineList[" + (i-2) + "].roLineId");
            $("#productRow_" + i + "_roLineId").attr("id", "productRow_" + (i-1) + "_roLineId");
            $("#productRow_" + i + "_roLineNo").attr("name", "lineList[" + (i-2) + "].lineNo");
            $("#productRow_" + i + "_roLineNo").attr("id", "productRow_" + (i-1) + "_roLineNo");
            $("#productRow_" + (i-1) + "_roLineNo").val(i-1);	// 行番号を変更
            $("#productRow_" + i + "_roItemId").attr("name", "lineList[" + (i-2) + "].roItemId");
            $("#productRow_" + i + "_roItemId").attr("id", "productRow_" + (i-1) + "_roItemId");
            $("#productRow_" + i + "_deletable").attr("name", "lineList[" + (i-2) + "].deletable");
            $("#productRow_" + i + "_deletable").attr("id", "productRow_" + (i-1) + "_deletable");
            $("#productRow_" + i + "_supplierPcode").attr("name", "lineList[" + (i-2) + "].supplierPcode");
            $("#productRow_" + i + "_supplierPcode").attr("id", "productRow_" + (i-1) + "_supplierPcode");
            $("#productRow_" + i + "_roMaxNum").attr("name", "lineList[" + (i-2) + "].roMaxNum");
            $("#productRow_" + i + "_roMaxNum").attr("id", "productRow_" + (i-1) + "_roMaxNum");
            $("#productRow_" + i + "_customerPcode").attr("name", "lineList[" + (i-2) + "].customerPcode");
            $("#productRow_" + i + "_customerPcode").attr("id", "productRow_" + (i-1) + "_customerPcode");

            // 商品名
            $("#productRow_" + i + "_name").attr("id", "productRow_" + (i-1) + "_name");
            // hidden
            $("#productRow_" + i + "_name_hidden").attr("name", "lineList[" + (i-2) + "].productAbstract");
            $("#productRow_" + i + "_name_hidden").attr("id", "productRow_" + (i-1) + "_name_hidden");

            // 棚番
            $("#productRow_" + i + "_rack").attr("name", "lineList[" + (i-2) + "].rackCodeSrc");
            $("#productRow_" + i + "_rack").attr("id", "productRow_" + (i-1) + "_rack");
			$("#productRow_" + i + "_rack").attr("tabindex", (++tabIdx));

            // 数量
            $("#productRow_" + i + "_quantity").attr("name", "lineList[" + (i-2) + "].quantity");
//            $("#productRow_" + i + "_quantity").unbind("change");
//            $("#productRow_" + i + "_quantity").bind("change", {id: 'productRow_' + (i-1)}, onChangeQuantity);

            $("#productRow_" + i + "_quantity").unbind("focus");
            $("#productRow_" + i + "_quantity").bind("focus", {id: 'productRow_' + (i-1)}, function(e){ this.curVal=this.value; });
            $("#productRow_" + i + "_quantity").unbind("blur");
            $("#productRow_" + i + "_quantity").bind("blur", {id: 'productRow_' + (i-1)}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){ onChangeQuantity(e,this.curVal); } });

            $("#productRow_" + i + "_quantity").attr("id", "productRow_" + (i-1) + "_quantity");
			$("#productRow_" + i + "_quantity").attr("tabindex", (++tabIdx));
            // hidden
            $("#productRow_" + i + "_quantity_hidden").attr("name", "lineList[" + (i-2) + "].quantityDB");
            $("#productRow_" + i + "_quantity_hidden").attr("id", "productRow_" + (i-1) + "_quantity_hidden");

            // 在庫ボタン
            $("#productRow_" + i + "_stockBtn").unbind("click");
            $("#productRow_" + i + "_stockBtn").bind("click", {lineNo: 'productRow_'+(i-1)}, onStockButton);
            $("#productRow_" + i + "_stockBtn").attr("id", "productRow_" + (i-1) + "_stockBtn");
			$("#productRow_" + i + "_stockBtn").attr("tabindex", (++tabIdx));

            // 受注残数
            $("#productRow_" + i + "_restQuantity_hidden").attr("name", "lineList[" + (i-2) + "].restQuantity");
            $("#productRow_" + i + "_restQuantity_hidden").attr("id", "productRow_" + (i-1) + "_restQuantity_hidden");
            $("#productRow_" + i + "_restQuantity_db_hidden").attr("name", "lineList[" + (i-2) + "].restQuantityDB");
            $("#productRow_" + i + "_restQuantity_db_hidden").attr("id", "productRow_" + (i-1) + "_restQuantity_db_hidden");
            $("#productRow_" + i + "_restQuantity").attr("id", "productRow_" + (i-1) + "_restQuantity");
            // 課税区分
            $("#productRow_" + i + "_taxCategory_hidden").attr("name", "lineList[" + (i-2) + "].taxCategory");
            $("#productRow_" + i + "_taxCategory_hidden").attr("id", "productRow_" + (i-1) + "_taxCategory_hidden");
            $("#productRow_" + i + "_taxCategory").attr("id", "productRow_" + (i-1) + "_taxCategory");
            // 消費税率
            $("#productRow_" + i + "_ctaxRate_hidden").attr("name", "lineList[" + (i-2) + "].ctaxRate");
            $("#productRow_" + i + "_ctaxRate_hidden").attr("id", "productRow_" + (i-1) + "_ctaxRate_hidden");
            $("#productRow_" + i + "_ctaxRate").attr("id", "productRow_" + (i-1) + "_ctaxRate");
            $("#productRow_" + i + "_ctaxPrice_hidden").attr("name", "lineList[" + (i-2) + "].ctaxPrice");
            $("#productRow_" + i + "_ctaxPrice_hidden").attr("id", "productRow_" + (i-1) + "_ctaxPrice_hidden");
            // 在庫管理区分
            $("#productRow_" + i + "__stockCtlCategory_hidden").attr("name", "lineList[" + (i-2) + "].stockCtlCategory");
            $("#productRow_" + i + "_stockCtlCategory_hidden").attr("id", "productRow_" + (i-1) + "_stockCtlCategory_hidden");

            // 完納区分
            $("#productRow_" + i + "_status").attr("name", "lineList[" + (i-2) + "].status");
            $("#productRow_" + i + "_statusName").attr("id", "productRow_" + (i-1) + "_statusName");
            $("#productRow_" + i + "_status").attr("id", "productRow_" + (i-1) + "_status");

            // 引当可能数
            $("#productRow_" + i + "_possibleDrawQuantity").attr("name", "lineList[" + (i-2) + "].possibleDrawQuantity");
            $("#productRow_" + i + "_possibleDrawQuantity").attr("id", "productRow_" + (i-1) + "_possibleDrawQuantity");

            // 仕入単価
            $("#productRow_" + i + "_unitCost").attr("name", "lineList[" + (i-2) + "].unitCost");
//            $("#productRow_" + i + "_unitCost").unbind("change");
//            $("#productRow_" + i + "_unitCost").bind("change", {id: 'productRow_' + (i-1)}, calcCostEvent);
            $("#productRow_" + i + "_unitCost").unbind("focus");
            $("#productRow_" + i + "_unitCost").bind("focus", {id: 'productRow_' + (i-1)}, function(e){ this.curVal=this.value; });
            $("#productRow_" + i + "_unitCost").unbind("blur");
            $("#productRow_" + i + "_unitCost").bind("blur", {id: 'productRow_' + (i-1)}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){ calcCostEvent(e); } });

            $("#productRow_" + i + "_unitCost").attr("id", "productRow_" + (i-1) + "_unitCost");
			$("#productRow_" + i + "_unitCost").attr("tabindex", (++tabIdx));

            // 仕入金額
            $("#productRow_" + i + "_cost").attr("name", "lineList[" + (i-2) + "].cost");
            $("#productRow_" + i + "_cost").attr("id", "productRow_" + (i-1) + "_cost");
			$("#productRow_" + i + "_cost").attr("tabindex", (++tabIdx));

            // 売上単価
            $("#productRow_" + i + "_unitRetailPrice").attr("name", "lineList[" + (i-2) + "].unitRetailPrice");
//            $("#productRow_" + i + "_unitRetailPrice").unbind("change");
//            $("#productRow_" + i + "_unitRetailPrice").bind("change", {id: 'productRow_' + (i-1)}, calcRetailEvent);
            $("#productRow_" + i + "_unitRetailPrice").unbind("focus");
            $("#productRow_" + i + "_unitRetailPrice").bind("focus", {id: 'productRow_' + (i-1)}, function(e){ this.curVal=this.value; });
            $("#productRow_" + i + "_unitRetailPrice").unbind("blur");
            $("#productRow_" + i + "_unitRetailPrice").bind("blur", {id: 'productRow_' + (i-1)}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){ calcRetailEvent(e); } });

            $("#productRow_" + i + "_unitRetailPrice").attr("id", "productRow_" + (i-1) + "_unitRetailPrice");
			$("#productRow_" + i + "_unitRetailPrice").attr("tabindex", (++tabIdx));

            // 売上金額
            $("#productRow_" + i + "_retailPrice").attr("name", "lineList[" + (i-2) + "].retailPrice");
            $("#productRow_" + i + "_retailPrice").attr("id", "productRow_" + (i-1) + "_retailPrice");
			$("#productRow_" + i + "_retailPrice").attr("tabindex", (++tabIdx));

            // 備考
            $("#productRow_" + i + "_remarks").attr("name", "lineList[" + (i-2) + "].remarks");
            $("#productRow_" + i + "_remarks").attr("id", "productRow_" + (i-1) + "_remarks");
			$("#productRow_" + i + "_remarks").attr("tabindex", (++tabIdx));

            // ピッキング備考
            $("#productRow_" + i + "_eadRemarks").attr("name", "lineList[" + (i-2) + "].eadRemarks");
            $("#productRow_" + i + "_eadRemarks").attr("id", "productRow_" + (i-1) + "_eadRemarks");
			$("#productRow_" + i + "_eadRemarks").attr("tabindex", (++tabIdx));

            // 削除ボタン
            $("#productRow_" + i + "_deleteRowBtn").unbind("click");
//            $("#productRow_" + i + "_deleteRowBtn").bind("click", {lineNo: i-1}, deleteRow);
            $("#productRow_" + i + "_deleteRowBtn").bind("click", {lineId: thisRoLineId, lineNo: i-1}, deleteRow);
            $("#productRow_" + i + "_deleteRowBtn").attr("id", "productRow_" + (i-1) + "_deleteRowBtn");
			$("#productRow_" + i + "_deleteRowBtn").attr("tabindex", (++tabIdx));

            // 前行複写ボタン
            $("#productRow_" + i + "_copyRowBtn").unbind("click");
            $("#productRow_" + i + "_copyRowBtn").bind("click", {lineNo: i-1}, copyRow);
            $("#productRow_" + i + "_copyRowBtn").attr("id", "productRow_" + (i-1) + "_copyRowBtn");
			$("#productRow_" + i + "_copyRowBtn").attr("tabindex", (++tabIdx));
        }
    }

    function resetRowNo() {
        var rowNo = 1;
        var count = 0;
        for (var i = 0; i < maxLineCount; i++) {
            var tdObj = $("#td" + (i+1) + "_1");
            tdObj.html(rowNo);
            rowNo++;
        }
    }

    function changeRate(){
        for (var i = 0; i < maxLineCount; i++) {
            // 各行の税率
        	$("#productRow_" + (i+1) + "_ctaxRate_hidden").val($("#ctaxRate").val());
        	calcSum(true);
        }
    }



    function calcSum(hasChanged) {
        var tbodyObj = $("#tbodyLine").get(0);
        var rowNo = 1;
        var count = 0;
        var sumCost = 0;
        var sumRetailPrice = 0;
        var costIndex = 6;
        var retailPriceIndex = 7;
        var index = 0;

        for (var i = 0; i < maxLineCount; i++) {
            sumCost += _Number($("#productRow_" + (i+1) + "_cost").val());
            var retailPrice = _Number($("#productRow_" + (i+1) + "_retailPrice").val());
            sumRetailPrice += retailPrice;
            if(hasChanged){
                // 税率は％表記なので100.0で割る
    			var myTax = retailPrice  * $("#ctaxRate").val() / 100.0;
    			var l_nCtaxPriceTotal = oBDCS( myTax.toString() ).setScale($("#taxFractCategory").val(),priceAlignment).setComma(false).toBDCString();
                $("#productRow_" + (i+1) + "_ctaxPrice_hidden").val( l_nCtaxPriceTotal );
            }
        }

        // 表示する
        // 粗利益
        var gross = sumRetailPrice - sumCost;
       	$("#gross").val(gross);
       	$("#grossDisp").html(gross);
        SetBigDecimalScale_Obj($("#grossDisp"));

        // 粗利益率
        // 2010.04.22 update kaki 粗利益率は％表示するため、×100する。
        var grossRatio = 0;

        if (sumRetailPrice != 0) {
        	grossRatio= gross / sumRetailPrice * 100;
        }

        $("#grossRatio").val(grossRatio);
        $("#grossRatioDisp").html(grossRatio);
        SetBigDecimalScale_Obj($("#grossRatioDisp"));

        // 金額合計
        var retailPriceTotal = sumRetailPrice;
       	$("#retailPriceTotal").val(retailPriceTotal);
       	$("#retailPriceTotalDisp").html(retailPriceTotal);
        SetBigDecimalScale_Obj($("#retailPriceTotalDisp"));

        // 消費税
        var ctaxPriceTotal = 0.0;

        // 伝票合計
        var priceTotal = retailPriceTotal;

        // 外税の時のみ消費税を計算する
        if ($("#taxShiftCategory").val() != <%=CategoryTrns.TAX_SHIFT_CATEGORY_INCLUDE_CTAX%>) {
            // 税率は％表記なので100.0で割る
           if(hasChanged){
                ctaxPriceTotal = retailPriceTotal * $("#ctaxRate").val() / 100.0;
                ctaxPriceTotal = _Number(oBDCS( ctaxPriceTotal.toString() ).setScale($("#taxFractCategory").val(),priceAlignment).setComma(false).toBDCString());
                $("#ctaxPriceTotal").val(ctaxPriceTotal);

                priceTotal += ctaxPriceTotal;
                $("#priceTotal").val(priceTotal);

            }else{

            	ctaxPriceTotal = $("#ctaxPriceTotal").val();
            	priceTotal = $("#priceTotal").val();

				if (ctaxPriceTotal == "") {
					ctaxPriceTotal = "0";
				}

				if (priceTotal == "") {
					priceTotal = "0";
				}
            }
        }

        $("#ctaxPriceTotalDisp").html(ctaxPriceTotal);
        $("#priceTotalDisp").html(priceTotal);

        SetBigDecimalScale_Obj($("#ctaxPriceTotalDisp"));
        SetBigDecimalScale_Obj($("#priceTotalDisp"));

        $("#costTotal").val(sumCost);

        _after_load($(".numeral_commas"));

		// 2010.04.22 add kaki 粗利益率は％表示とする。
		var sgrossRatio = $("#grossRatioDisp").html()+"%";
		$("#grossRatioDisp").html(sgrossRatio);
		SetBigDecimalScale_Obj($("#grossRatioDisp"));
    }

    // 行追加
    function addRow(){
    	var elemTr, elemTd;
    	var tabIdx;
    	var cellIndex = 0;

    	if(maxLineCount >= MAX_LINE_SIZE) {
    		alert('<bean:message key="errors.line.maxrows" />');
    		return;
    	}

        var lineNo = maxLineCount + 1;
    	tabIdx = 1000+((maxLineCount+1) * 20 )- 1;

    	// ベースオブジェクトからクローンを生成
    	elemTr = trCloneBase.clone(true);
    	elemTr.attr("id", "productRow_" + lineNo);

    	// No列の設定
    	elemTd = elemTr.children(":first");
    	elemTd.attr("id", "td" + lineNo + "_1");
    	elemTd.html(lineNo);

    	// 商品コード列の設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children().children("#productRow_1_code");
    	elemWork.attr("id", "productRow_" + lineNo + "_code");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].productCode");
    	elemWork.attr("tabindex", (++tabIdx));
    	elemWork.attr("readonly", false);
    	elemWork.removeClass("c_disable");

    	elemWork.unbind("focus");
    	elemWork.bind("focus", {"lineNo": lineNo}, function(e){ this.curVal=this.value; });
    	elemWork.unbind("blur");
    	elemWork.bind("blur", {"lineNo": lineNo}, function(e){ if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); searchProduct(e); } });
        elemWork.val("");

    	elemWork = elemTd.children().children("#productRow_1_icon");
        elemWork.attr("id", "productRow_" + lineNo + "_icon");
    	elemWork.attr("tabindex", (++tabIdx));
    	elemWork.bind("click", {"lineNo": lineNo}, openSearchProduct);

    	// Hidden列の設定
    	elemWork = elemTd.children("#productRow_1_roLineId");
    	elemWork.attr("id", "productRow_" + lineNo + "_roLineId");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].roLineId");
        elemWork.val("");

    	elemWork = elemTd.children("#productRow_1_roLineNo");
    	elemWork.attr("id", "productRow_" + lineNo + "_roLineNo");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].lineNo");
        elemWork.val(lineNo);

    	elemWork = elemTd.children("#productRow_1_roItemId");
    	elemWork.attr("id", "productRow_" + lineNo + "_roItemId");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].roItemId");
        elemWork.val("");

    	elemWork = elemTd.children("#productRow_1_deletable");
    	elemWork.attr("id", "productRow_" + lineNo + "_deletable");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].deletable");
        elemWork.val("");

    	elemWork = elemTd.children("#productRow_1_supplierPcode");
    	elemWork.attr("id", "productRow_" + lineNo + "_supplierPcode");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].supplierPcode");
        elemWork.val("");

    	elemWork = elemTd.children("#productRow_1_roMaxNum");
    	elemWork.attr("id", "productRow_" + lineNo + "_roMaxNum");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].roMaxNum");
        elemWork.val("");

    	elemWork = elemTd.children("#productRow_1_customerPcode");
    	elemWork.attr("id", "productRow_" + lineNo + "_customerPcode");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].customerPcode");
        elemWork.val("");

    	// 商品名列の設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children("#productRow_1_name");
    	elemWork.attr("id", "productRow_" + lineNo + "_name");
        elemWork.text("");

    	elemWork = elemTd.children().children("#productRow_1_productRemarks");
    	elemWork.attr("id", "productRow_" + lineNo + "_productRemarks");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].productRemarks");
        elemWork.text("");

    	elemWork = elemTd.children("#productRow_1_name_hidden");
    	elemWork.attr("id", "productRow_" + lineNo + "_name_hidden");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].productAbstract");
        elemWork.val("");

    	// 棚番・数量列の設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children().children("#productRow_1_rack");
    	elemWork.attr("id", "productRow_" + lineNo + "_rack");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].rackCodeSrc");
    	elemWork.attr("tabindex", (++tabIdx));
        elemWork.val("");
    	elemWork = elemTd.children().children("#productRow_1_quantity");
    	elemWork.attr("id", "productRow_" + lineNo + "_quantity");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].quantity");
    	elemWork.attr("tabindex", (++tabIdx));
    	elemWork.attr("readonly", false);
    	elemWork.removeClass("c_disable");
//    	elemWork.bind("change", {"id": 'productRow_' + lineNo}, onChangeQuantity);

    	elemWork.unbind("focus");
    	elemWork.bind("focus", {"id": 'productRow_' + lineNo}, function(e){ this.curVal=this.value; });
    	elemWork.unbind("blur");
    	elemWork.bind("blur", {"id": 'productRow_' + lineNo}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){ onChangeQuantity(e,this.curVal); } });

    	elemWork.val("");
        // hidden
    	elemWork = elemTd.children("#productRow_1_quantity_hidden");
    	elemWork.attr("id", "productRow_" + lineNo + "_quantity_hidden");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].quantityDB");
        elemWork.val("");


    	// 在庫ボタン
    	elemWork = elemTd.children().children("#productRow_1_stockBtn");
    	elemWork.attr("id", "productRow_" + lineNo + "_stockBtn");
    	elemWork.attr("tabindex", (++tabIdx));
    	elemWork.bind("click", {"lineNo": 'productRow_' + lineNo}, onStockButton);

        // 受注残数・完納区分の設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children().children("#productRow_1_restQuantity");
    	elemWork.attr("id", "productRow_" + lineNo + "_restQuantity");
        elemWork.text("");
//    	elemWork.attr("tabindex", (++tabIdx));
    	elemWork = elemTd.children().children("#productRow_1_statusName");
    	elemWork.attr("id", "productRow_" + lineNo + "_statusName");
        elemWork.text("");


        // 引当可能数
        //検証
    	elemWork = elemTd.children().children("#productRow_1_possibleDrawQuantity");
    	elemWork.attr("id", "productRow_" + lineNo + "_possibleDrawQuantity");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].possibleDrawQuantity");
        elemWork.text("");

    	elemWork = elemTd.children("#productRow_1_status");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].status");
    	elemWork.attr("id", "productRow_" + lineNo + "_status");
        elemWork.val("${defaultStatusCode}");
//    	elemWork.attr("tabindex", (++tabIdx));
    	elemWork = elemTd.children("#productRow_1_restQuantity_hidden");
    	elemWork.attr("id", "productRow_" + lineNo + "_restQuantity_hidden");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].restQuantity");
        elemWork.val("");
    	elemWork = elemTd.children("#productRow_1_restQuantity_db_hidden");
    	elemWork.attr("id", "productRow_" + lineNo + "_restQuantity_db_hidden");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].restQuantityDB");
        elemWork.val("");
    	elemWork = elemTd.children("#productRow_1_taxCategory_hidden");
    	elemWork.attr("id", "productRow_" + lineNo + "_taxCategory_hidden");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].taxCategory");
        elemWork.val("");
    	elemWork = elemTd.children("#productRow_1_ctaxRate_hidden");
    	elemWork.attr("id", "productRow_" + lineNo + "_ctaxRate_hidden");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].ctaxRate");
        elemWork.val($("#ctaxRate").val()); // 選択されている消費税率を設定する

    	elemWork = elemTd.children("#productRow_1_ctaxPrice_hidden");
    	elemWork.attr("id", "productRow_" + lineNo + "_ctaxPrice_hidden");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].ctaxPrice");
        elemWork.val("");

    	elemWork = elemTd.children("#productRow_1_stockCtlCategory");
    	elemWork.attr("id", "productRow_" + lineNo + "_stockCtlCategory");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].stockCtlCategory");
        elemWork.val("");


    	// 仕入単価・仕入金額
    	elemTd = elemTd.next();
    	elemWork = elemTd.children().children("#productRow_1_unitCost");
    	elemWork.attr("id", "productRow_" + lineNo + "_unitCost");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].unitCost");
    	elemWork.attr("tabindex", (++tabIdx));
//    	elemWork.attr("readonly", false);
//    	elemWork.removeClass("c_disable");
        elemWork.val("");
//    	elemWork.bind("change", {"id": 'productRow_' + lineNo}, calcCostEvent);
    	elemWork.unbind("focus");
    	elemWork.bind("focus", {"id": 'productRow_' + lineNo}, function(e){ this.curVal=this.value; });
    	elemWork.unbind("blur");
    	elemWork.bind("blur", {"id": 'productRow_' + lineNo}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){ calcCostEvent(e); } });

    	elemWork = elemTd.children().children("#productRow_1_cost");
    	elemWork.attr("id", "productRow_" + lineNo + "_cost");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].cost");
    	elemWork.attr("tabindex", (++tabIdx));
 //   	elemWork.attr("readonly", false);
 //   	elemWork.removeClass("c_disable");
        elemWork.val("");

    	// 売上単価・売価金額
    	elemTd = elemTd.next();
    	elemWork = elemTd.children().children("#productRow_1_unitRetailPrice");
    	elemWork.attr("id", "productRow_" + lineNo + "_unitRetailPrice");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].unitRetailPrice");
    	elemWork.attr("tabindex", (++tabIdx));
    	elemWork.attr("readonly", false);
    	elemWork.removeClass("c_disable");
        elemWork.val("");
//    	elemWork.bind("change", {"id": 'productRow_' + lineNo}, calcRetailEvent);
    	elemWork.unbind("focus");
    	elemWork.bind("focus", {"id": 'productRow_' + lineNo}, function(e){ this.curVal=this.value; });
    	elemWork.unbind("blur");
    	elemWork.bind("blur", {"id": 'productRow_' + lineNo}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){ calcRetailEvent(e); } });

    	elemWork = elemTd.children().children("#productRow_1_retailPrice");
    	elemWork.attr("id", "productRow_" + lineNo + "_retailPrice");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].retailPrice");
    	elemWork.attr("tabindex", (++tabIdx));
    	elemWork.attr("readonly", false);
    	elemWork.removeClass("c_disable");
        elemWork.val("");

    	// 備考列の設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children().children("#productRow_1_remarks");
    	elemWork.attr("id", "productRow_" + lineNo + "_remarks");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].remarks");
    	elemWork.attr("tabindex", (++tabIdx));
    	elemWork.attr("readonly", false);
    	elemWork.removeClass("c_disable");
        elemWork.val("");
    	elemWork = elemTd.children().children("#productRow_1_eadRemarks");
    	elemWork.attr("id", "productRow_" + lineNo + "_eadRemarks");
    	elemWork.attr("name", "lineList[" + (lineNo-1) + "].eadRemarks");
    	elemWork.attr("tabindex", (++tabIdx));
        elemWork.val("");

    	// ボタン列の設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children().children("#productRow_1_deleteRowBtn");
    	elemWork.attr("id", "productRow_" + lineNo + "_deleteRowBtn");
    	elemWork.attr("tabindex", (++tabIdx));
    	elemWork.bind("click", {"lineId": "", "lineNo": lineNo}, deleteRow);

    	elemWork = elemTd.children().children("#productRow_1_copyRowBtn");
    	elemWork.attr("id", "productRow_" + lineNo + "_copyRowBtn");
    	elemWork.attr("tabindex", (++tabIdx));
    	elemWork.bind("click", {"lineNo": lineNo}, copyRow);

        maxLineCount++;

    	// 行を追加
    	$("#trAddLine").before(elemTr);

        // ボタンの状態変化
        resetButtonDisabled();

    }

    function resetButtonDisabled() {
        for (var i = 1; i <= maxLineCount; i++) {
            // 削除ボタンは全体1行の時のみ無効
            if (maxLineCount == 1) {
                $("#productRow_" + i + "_deleteRowBtn").attr("disabled", true);
            } else {
            // それ以外は有効
                $("#productRow_" + i + "_deleteRowBtn").removeAttr("disabled");
            }

            // 前行複写ボタンは1行目のみ無効
            if (i == 1) {
                $("#productRow_" + i + "_copyRowBtn").attr("disabled", true);
            } else {
            // それ以外の行は有効
                $("#productRow_" + i + "_copyRowBtn").removeAttr("disabled");
            }
        }
    }

    function copyRow(event) {
    	var lineNo = event.data.lineNo;
    	if (!checkProductWrite(lineNo)) {
    		return;
    	}
    	if (lineNo == 1) {
    		return;
    	}
    	if(!isEmptyLine(lineNo)) {
			// 上書き確認
			if(!confirm('<bean:message key="confirm.line.copy" />')){
				return;
			}
    	}

    	var source = lineNo-1;

    	// 商品コードのコピー
    	$("#productRow_" + lineNo + "_code").val($("#productRow_" + source + "_code").val());

    	// Hidden列の設定
    	$("#productRow_" + lineNo + "_supplierPcode").val($("#productRow_" + source + "_supplierPcode").val());
    	$("#productRow_" + lineNo + "_roMaxNum").val($("#productRow_" + source + "_roMaxNum").val());
    	$("#productRow_" + lineNo + "_customerPcode").val($("#productRow_" + source + "_customerPcode").val());
    	$("#productRow_" + lineNo + "_stockCtlCategory").val($("#productRow_" + source + "_stockCtlCategory").val());
    	$("#productRow_" + lineNo + "_roMaxNum").val($("#productRow_" + source + "_roMaxNum").val());

    	// 商品名列の設定
    	$("#productRow_" + lineNo + "_name").text($("#productRow_" + source + "_name").text());
    	$("#productRow_" + lineNo + "_productRemarks").text($("#productRow_" + source + "_productRemarks").text());
    	$("#productRow_" + lineNo + "_name_hidden").val($("#productRow_" + source + "_name_hidden").val());

    	// 棚番・数量列の設定
    	$("#productRow_" + lineNo + "_rack").val($("#productRow_" + source + "_rack").val());
    	$("#productRow_" + lineNo + "_quantity").val($("#productRow_" + source + "_quantity").val());
    	$("#productRow_" + lineNo + "_quantity_hidden").val($("#productRow_" + source + "_quantity_hidden").val());

        // 受注残数の設定
//    	$("#productRow_" + lineNo + "_restQuantity").text($("#productRow_" + source + "_restQuantity").text()); --未納数は、数量と同じとする。
        $("#productRow_" + lineNo + "_restQuantity").text($("#productRow_" + source + "_quantity").val());

        //完納区分の設定 データがある場合のみデフォルト値を設定
        if ($("#productRow_" + source + "_statusName").text()) {
    		$("#productRow_" + lineNo + "_statusName").text('${defaultStatusName}');
    	}

        $("#productRow_" + lineNo + "_status").val(${defaultStatusCode});
    	$("#productRow_" + lineNo + "_possibleDrawQuantity").val($("#productRow_" + source + "_possibleDrawQuantity").val());

    	$("#productRow_" + lineNo + "_restQuantity_hidden").val($("#productRow_" + source + "_quantity").val());
    	$("#productRow_" + lineNo + "_taxCategory_hidden").val($("#productRow_" + source + "_taxCategory_hidden").val());
    	$("#productRow_" + lineNo + "_ctaxRate_hidden").val($("#productRow_" + source + "_ctaxRate_hidden").val());

    	// 仕入単価・仕入金額
    	$("#productRow_" + lineNo + "_unitCost").val($("#productRow_" + source + "_unitCost").val());
    	$("#productRow_" + lineNo + "_cost").val($("#productRow_" + source + "_cost").val());

    	// 売上単価・売価金額
    	$("#productRow_" + lineNo + "_unitRetailPrice").val($("#productRow_" + source + "_unitRetailPrice").val());
    	$("#productRow_" + lineNo + "_retailPrice").val($("#productRow_" + source + "_retailPrice").val());

    	// 備考列の設定
    	$("#productRow_" + lineNo + "_remarks").val($("#productRow_" + source + "_remarks").val());
    	$("#productRow_" + lineNo + "_eadRemarks").val($("#productRow_" + source + "_eadRemarks").val());

        calcSum(true);
    }

 	// 明細行の空行判定
    function isEmptyLine(lineNo){
    	var retVal = true;
    	var elem;
    	// 商品コード
    	elem = $("#productRow_" + lineNo + "_code").val();
    	if(elem != "") {
    		retVal = false;
    	}
    	// 商品名・摘要
    	elem = $("#productRow_" + lineNo + "_name").val();
    	if(elem != "") {
    		retVal = false;
    	}
    	// 数量
    	elem = $("#productRow_" + lineNo + "_quantity").val();
    	if(elem != "") {
    		retVal = false;
    	}
    	// 仕入単価（原単価）
    	elem = $("#productRow_" + lineNo + "_unitCost").val();
    	if(elem != "") {
    		retVal = false;
    	}
    	// 仕入金額（原価金額）
    	elem = $("#productRow_" + lineNo + "_cost").val();
    	if(elem != "") {
    		retVal = false;
    	}
    	// 売上単価
    	elem = $("#productRow_" + lineNo + "_unitRetailPrice").val();
    	if(elem != "") {
    		retVal = false;
    	}
    	// 売価金額
    	elem = $("#productRow_" + lineNo + "_retailPrice").val();
    	if(elem != "") {
    		retVal = false;
    	}
    	// 備考
    	elem = $("#productRow_" + lineNo + "_remarks").val();
    	if(elem != "") {
    		retVal = false;
    	}


    	return retVal;
    }

    function checkZipCodeAndAddress() {
		var zipCode = $("#deliveryZipCode");
		var address1 = $("#deliveryAddress1");

		if( zipCode.val().length == 0 || address1.val().length == 0 ) {
			return;
		}

		var message = "<bean:message key="warns.zipcode.address.mismatch" arg0="納入先"/>";
		$("#errors").empty();

		var data = new Object();
		data[ "zipCode" ] = zipCode.val();
		data[ "zipAddress1" ] = address1.val();
		asyncRequest(
				contextRoot + "/ajax/checkZipCodeAndAddressAjax/check",
				data,
				function(result){
					if(result != "true") {
						$("#errors").append(document.createTextNode(message));
					}
				}
			);
    }
-->
	</script>
	<script type="text/javascript" src="./scripts/common.js"></script>
	<script type="text/javascript" src="./scripts/order_customer_input.js"></script>
	<script type="text/javascript" src="./scripts/jquery.bgiframe.min.js"></script>
</head>

<body onload="init()" onhelp="return false;">
<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

<!-- メニュー -->
<jsp:include page="/WEB-INF/view/common/menubar.jsp">
	<jsp:param name="PARENT_MENU_ID" value="0003"/>
	<jsp:param name="MENU_ID" value="<%=Constants.MENU_ID.INPUT_RORDER%>"/>
</jsp:include>


<!-- メイン機能 -->
<div id="main_function">

	<!-- タイトル -->
	<span class="title">受注入力</span>

	<div class="function_buttons">
		<button type="button" tabindex="2000" id="btnF1" onclick="onF1()">F1<br>初期化</button>
	<c:if test="${!newData}">
	    <c:if test="${menuUpdate}">
	        <c:if test="${deletable}">
			<button type="button" tabindex="2001" id="btnF2" onclick="onF2()">F2<br>削除</button>
	        </c:if>
			<c:if test="${!deletable}">
			<button type="button" tabindex="2001" id="btnF2" disabled="disabled">F2<br>削除</button>
	        </c:if>
			<c:if test="${statusUpdate}">
	    		<button type="button" tabindex="2002" id="btnF3" onclick="onF3()">F3<br>更新</button>
	        </c:if>
			<c:if test="${!statusUpdate}">
	    		<button type="button" disabled="disabled" id="btnF3" tabindex="2002">F3<br>更新</button>
	        </c:if>
	    </c:if>
	    <c:if test="${!menuUpdate}">
			<button type="button" disabled="disabled" id="btnF2" tabindex="2001">F2<br>削除</button>
			<button type="button" disabled="disabled" id="btnF3" tabindex="2002">F3<br>更新</button>
	    </c:if>
	</c:if>
	<c:if test="${newData}">
			<button type="button" disabled="disabled" id="btnF2" tabindex="2001">F2<br>削除</button>
	    <c:if test="${menuUpdate}">
			<button type="button" tabindex="2002" id="btnF3" onclick="onF3()">F3<br>登録</button>
	    </c:if>
	    <c:if test="${!menuUpdate}">
			<button type="button" disabled="disabled" id="btnF3" tabindex="2002">F3<br>登録</button>
	    </c:if>
	</c:if>
		<button type="button" id="btnF4" disabled="disabled">F4<br>&nbsp;</button>
		<button type="button" id="btnF5" disabled="disabled">F5<br>&nbsp;</button>
		<button type="button" id="btnF6" disabled="disabled">F6<br>&nbsp;</button>
		<button type="button" id="btnF7" disabled="disabled">F7<br>&nbsp;</button>
		<button type="button" id="btnF8" disabled="disabled">F8<br>&nbsp;</button>
		<button type="button" id="btnF9" disabled="disabled">F9<br>&nbsp;</button>
		<button type="button" id="btnF10" disabled="disabled">F10<br>&nbsp;</button>
		<button type="button" id="btnF11" disabled="disabled">F11<br>&nbsp;</button>
		<button type="button" id="btnF12" disabled="disabled">F12<br>&nbsp;</button>
	</div>
	<br><br><br>

	<s:form onsubmit="return false;">
		<html:hidden property="taxFractCategory" styleId="taxFractCategory" />
		<html:hidden property="priceFractCategory" styleId="priceFractCategory" />
		<html:hidden property="isOnlineOrder" styleId="isOnlineOrder" />
		<html:hidden property="onlineOrderId" styleId="onlineOrderId"/>
		<html:hidden property="isImport" styleId="isImport" />

		<div class="function_forms">
			<div id="errors" style="color: red" style="padding-left: 20px">
				<html:errors/>
			</div>
			<div style="color: blue;">
				<html:messages id="msg" message="true">
					<bean:write name="msg" ignore="true"/><br>
				</html:messages>
			</div>

		    <div class="form_section_wrap">
			    <div class="form_section">
			    	<div class="section_title">
						<span>受注伝票情報</span>
			            <button class="btn_toggle">
			                <img alt="表示／非表示" src='${f:url("/images/customize/btn_toggle.png")}' width="28" height="29" class="tbtn">
			            </button>
					</div><!-- /.section_title -->

			        <html:hidden property="updDatetm"/>

			        <div id="order_section" class="section_body">
					<table id="order_info" class="forms" summary="受注伝票情報">
						<tr>
							<th><div class="col_title_right">受注番号</div></th>
							<td><html:text styleId="roSlipId" property="roSlipId" tabindex="100" readonly="false" style="ime-mode:disabled;" styleClass=""  maxlength="10"  onfocus="this.curVal=this.value;" onblur="if((this.curVal == '') || ((this.curVal != '')&&(this.curVal!=this.value))) {findSlip();}"/></td>
							<th><div class="col_title_right_req">受注日<bean:message key='labels.must'/></div></th>
							<td>
			                    <html:text styleId="roDate" maxlength="10" property="roDate" tabindex="101" style="text-align:center;ime-mode:disabled; width: 135px;" styleClass="date_input"/>
			                </td>
							<th><div class="col_title_right">出荷日</div></th>
							<td><html:text maxlength="10" property="shipDate" tabindex="102" style="text-align:center;ime-mode:disabled; width: 135px;" styleClass="date_input"/></td>
							<th><div class="col_title_right">納期指定日</div></th>
							<td>
			                    <html:text maxlength="10" property="deliveryDate" tabindex="103" style="text-align:center;ime-mode:disabled; width: 135px;" styleClass="date_input"/>
			                </td>
						</tr>
						<tr>
							<th><div class="col_title_right">受付番号</div></th>
							<td>
								<c:if test="${isImport}">
				                    <html:text maxlength="30" property="receptNo" tabindex="104" style="ime-mode:disabled;" styleClass="c_disable" readonly="true"/>
								</c:if>
								<c:if test="${!isImport}">
				                    <html:text maxlength="30" property="receptNo" tabindex="104" style="ime-mode:disabled;"/>
								</c:if>
			                </td>
							<th><div class="col_title_right">客先伝票番号</div></th>
							<td><html:text maxlength="30" property="customerSlipNo" tabindex="105" style="ime-mode:disabled;"/> </td>
							<th><div class="col_title_right">入力担当者</div></th>
							<td colspan="3">
			                    <html:hidden property="userId"/>
			                    <html:text property="userName" tabindex="106" readonly="true" styleClass="c_disable"/>
			                    <html:hidden property="status" styleId="status" />
			                </td>

						</tr>
						<tr>
							<th><div class="col_title_right">摘要</div></th>
							<td colspan="3">
			                    <html:text maxlength="50" property="remarks" style="width: 420px; ime-mode:active;" tabindex="107"/>
			                </td>
							<th><div class="col_title_right">配送業者</div></th>
							<td>
								<html:select tabindex="108" property="dcCategory"  styleId="dcCategory" >
									<c:forEach var="dcl" items="${dcCategoryList}">
										<html:option value="${dcl.value}">${dcl.label}</html:option>
									</c:forEach>
								</html:select>
							</td>
							<th><div class="col_title_right">配送時間帯</div></th>
							<td>
								<html:select tabindex="109" property="dcTimezoneCategory"  styleId="dcTimezoneCategory" >
									<c:forEach var="dctl" items="${dcTimeZoneCategoryList}">
										<html:option value="${dctl.value}">${dctl.label}</html:option>
									</c:forEach>
								</html:select>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right">消費税率</div></th>
							<td colspan="3">
								<html:select property="ctaxRate" styleId="ctaxRate" tabindex="110" style="width: 135px;" onchange="changeRate()">
								    <html:options collection="ctaxRateList" property="value" labelProperty="label"/>
								</html:select>&nbsp;％
							</td>
						</tr>
					</table>
					</div><!-- /.section_body -->
				</div><!-- /.form_section -->
			</div><!-- /.form_section_wrap -->

		    <div class="form_section_wrap">
			    <div class="form_section">
			    	<div class="section_title">
						<span>顧客情報</span>
			            <button class="btn_toggle">
			                <img alt="表示／非表示" src='${f:url("/images/customize/btn_toggle.png")}' width="28" height="29" class="tbtn">
			            </button>
					</div><!-- /.section_title -->

					<div id="order_section" class="section_body">
					<table id="customer_info" class="forms" summary="顧客情報">
						<colgroup>
							<col span="1" style="width: 10%">
							<col span="1" style="width: 15%">
							<col span="1" style="width: 10%">
							<col span="1" style="width: 35%">
							<col span="1" style="width: 10%">
							<col span="1" style="width: 30%">
						</colgroup>
						<tr>
							<th><div class="col_title_right_req">顧客コード<bean:message key='labels.must'/></div></th>
							<td>
								<c:if test="${isImport}">
			                    	<html:text maxlength="13" styleId="customerCode" property="customerCode" style="width: 130px; ime-mode:disabled;" tabindex="200" readonly="true" styleClass="c_disable"/>
								</c:if>
								<c:if test="${!isImport}">
				                    <html:text maxlength="13" styleId="customerCode" property="customerCode" style="width: 130px; ime-mode:disabled;" tabindex="200"
				                    	onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ GetCustomerInfos(); }"/>
				                    <html:image tabindex="201" src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSearchCustomerDialog('customer', setCustomer);$('#customer_customerCode').val($('#customerCode').val());" />
								</c:if>
			                </td>
							<th><div class="col_title_right">顧客名</div></th>
							<td colspan="3">
			                    <html:text styleId="customerName" property="customerName" style="width: 500px;" tabindex="202" readonly="true" styleClass="c_disable"/>
			                </td>
						</tr>
						<tr>
							<th><div class="col_title_right">税転嫁</div></th>
							<td>
								<html:select styleId="taxShiftCategory" property="taxShiftCategory" tabindex="203" styleClass="c_disable">
								<c:if test="${newData}">
									<html:option value=""></html:option>
								</c:if>
									<html:options collection="taxShiftCategoryList" property="value" labelProperty="label"/>
								</html:select>
							</td>
							<th><div class="col_title_right">支払条件</div></th>
							<td>
			                    <html:select styleId="cutoffGroupCategory" property="cutoffGroupCategory" tabindex="204" styleClass="c_disable">
								<c:if test="${newData}">
			                        <html:option value=""></html:option>
								</c:if>
			                        <html:options collection="cutOffList" property="value" labelProperty="label"/>
			                    </html:select>
			                </td>
							<th><div class="col_title_right">取引区分</div></th>
							<td>
			                    <html:select styleId="salesCmCategory" property="salesCmCategory" tabindex="205" styleClass="c_disable">
								<c:if test="${newData}">
			                        <html:option value=""></html:option>
								</c:if>
			                        <html:options collection="salesCmCategoryList" property="value" labelProperty="label"/>
			                    </html:select>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right">備考</div></th>
							<td colspan="5">
								<html:text property="customerRemarks" styleId="customerRemarks" styleClass="c_disable" style="width: 800px;" readonly="true" tabindex="206" />
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right">コメント</div></th>
							<td colspan="5">
								<html:text property="customerCommentData" styleId="customerCommentData" styleClass="c_disable" style="width: 800px;" readonly="true" tabindex="207" />
							</td>
						</tr>
					</table>
					</div><!-- /.section_body -->
				</div><!-- /.form_section -->
			</div><!-- /.form_section_wrap -->

		    <div class="form_section_wrap">
			    <div class="form_section">
			    	<div class="section_title">
						<span>納入先情報</span>
			            <button class="btn_toggle">
			                <img alt="表示／非表示" src='${f:url("/images/customize/btn_toggle.png")}' width="28" height="29" class="tbtn">
			            </button>
					</div><!-- /.section_title -->

					<div id="order_section" class="section_body">
					<table id="delivery_info" class="forms" summary="納入先情報">
						<tr>
							<th><div class="col_title_right">顧客納入先</div></th>
							<td colspan="5">
								<%-- オンライン注文以外 --%>
			                    <html:select styleId="deliveryCode" property="deliveryCode" tabindex="300" onchange="changeDelivery()"
			                    	style="width:250px;${isOnlineOrder?'display: none;':''}" disabled="${isOnlineOrder}">
			                        <option value=""></option>
			                        <html:options collection="deliveryList" property="value" labelProperty="label"/>
			                    </html:select>

			                    <html:text styleId="deliveryName" property="deliveryName" tabindex="300"
			                    	style="width: 500px; ime-mode:active; ${!isOnlineOrder?'display: none;':''}" maxlength="60"/>
			                    <html:hidden styleId="deliveryKana" property="deliveryKana"/>
			                    <html:hidden styleId="deliveryOfficeKana" property="deliveryOfficeKana"/>
			                    <html:hidden styleId="deliveryUrl" property="deliveryUrl"/>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right">事業所名</div></th>
							<td>
								<c:if test="${isOnlineOrder}">
			                    	<html:text styleId="deliveryOfficeName" property="deliveryOfficeName" tabindex="301" style="width: 200px; ime-mode:active;" maxlength="60" />
								</c:if>
								<c:if test="${!isOnlineOrder}">
			                    	<html:text styleId="deliveryOfficeName" property="deliveryOfficeName" tabindex="301" style="width: 200px; ime-mode:active;" readonly="true" styleClass="c_disable" maxlength="60" />
								</c:if>
			                </td>
							<th><div class="col_title_right">部署名</div></th>
							<td>
								<c:if test="${isOnlineOrder}">
			                    	<html:text styleId="deliveryDeptName" property="deliveryDeptName" tabindex="302" style="width: 200px; ime-mode:active;" maxlength="60" />
								</c:if>
								<c:if test="${!isOnlineOrder}">
			                    	<html:text styleId="deliveryDeptName" property="deliveryDeptName" tabindex="302" style="width: 200px; ime-mode:active;" readonly="true" styleClass="c_disable" maxlength="60" />
								</c:if>
			                </td>
						</tr>
						<tr>
							<!-- <th><div class="col_title_right">郵便番号<c:if test="${isOnlineOrder}">※</c:if></div></th> -->
							<c:if test="${isOnlineOrder}">
								<th><div class="col_title_right_req">郵便番号<bean:message key='labels.must'/></div></th>
							</c:if>
							<c:if test="${!isOnlineOrder}">
								<th><div class="col_title_right">郵便番号</div></th>
							</c:if>
							<td>
								<c:if test="${isOnlineOrder}">
			                    	<html:text styleId="deliveryZipCode" property="deliveryZipCode" tabindex="303" style="width: 100px; ime-mode:disabled;" maxlength="8" onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ searchZipCodeDirect();} "/>
								</c:if>
								<c:if test="${!isOnlineOrder}">
			                    	<html:text styleId="deliveryZipCode" property="deliveryZipCode"  tabindex="303" style="width: 100px; ime-mode:disabled;" readonly="true" styleClass="c_disable" maxlength="8" onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){searchZipCodeDirect();}" />
								</c:if>
								<html:image tabindex="304" src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openZipCodeDialog();" />
							</td>
							<!-- <th><div class="col_title_right">住所１<c:if test="${isOnlineOrder}">※</c:if></div></th> -->
							<c:if test="${isOnlineOrder}">
								<th><div class="col_title_right_req">住所１<bean:message key='labels.must'/></div></th>
							</c:if>
							<c:if test="${!isOnlineOrder}">
								<th><div class="col_title_right">住所１</div></th>
							</c:if>
							<td>
								<c:if test="${isOnlineOrder}">
				                    <html:text styleId="deliveryAddress1" property="deliveryAddress1" tabindex="305" style="width: 200px; ime-mode:active;" maxlength="50"
				                    	onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ checkZipCodeAndAddress(); }"/>
								</c:if>
								<c:if test="${!isOnlineOrder}">
				                    <html:text styleId="deliveryAddress1" property="deliveryAddress1" tabindex="305" style="width: 200px; ime-mode:active;" readonly="true" styleClass="c_disable" maxlength="50"
				                    	onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ checkZipCodeAndAddress(); }"/>
								</c:if>
			                </td>
							<th><div class="col_title_right">住所２</div></th>
							<td>
								<c:if test="${isOnlineOrder}">
			                    	<html:text styleId="deliveryAddress2" property="deliveryAddress2" tabindex="306" style="width: 200px; ime-mode:active;" maxlength="50" />
								</c:if>
								<c:if test="${!isOnlineOrder}">
			                    	<html:text styleId="deliveryAddress2" property="deliveryAddress2" tabindex="306" style="width: 200px; ime-mode:active;" readonly="true" styleClass="c_disable" maxlength="50" />
								</c:if>
			                </td>
						</tr>
						<tr>
							<!-- <th><div class="col_title_right">担当者<c:if test="${isOnlineOrder}">※</c:if></div></th> -->
							<c:if test="${isOnlineOrder}">
								<th><div class="col_title_right_req">担当者<bean:message key='labels.must'/></div></th>
							</c:if>
							<c:if test="${!isOnlineOrder}">
								<th><div class="col_title_right">担当者</div></th>
							</c:if>
							<td>
								<c:if test="${isOnlineOrder}">
			                    	<html:text styleId="deliveryPcName" property="deliveryPcName" style="width: 200px; ime-mode:active;" tabindex="307" maxlength="60" />
								</c:if>
								<c:if test="${!isOnlineOrder}">
			                    	<html:text styleId="deliveryPcName" property="deliveryPcName" style="width: 200px; ime-mode:active;" tabindex="307" readonly="true" styleClass="c_disable" maxlength="60" />
								</c:if>
			                </td>
							<th><div class="col_title_right">担当者カナ</div></th>
							<td>
								<c:if test="${isOnlineOrder}">
			                    	<html:text styleId="deliveryPcKana" property="deliveryPcKana" tabindex="308" style="width: 200px; ime-mode:active;"  maxlength="60" />
								</c:if>
								<c:if test="${!isOnlineOrder}">
			                    	<html:text styleId="deliveryPcKana" property="deliveryPcKana" tabindex="308" style="width: 200px; ime-mode:active;" readonly="true" styleClass="c_disable" maxlength="60" />
								</c:if>
			                </td>
							<th><div class="col_title_right">敬称</div></th>
							<td>
								<c:if test="${isOnlineOrder}">
									<html:select styleId="deliveryPcPreCategory" property="deliveryPcPreCategory" tabindex="309" style="width: 200px;">
				                        <html:options collection="preTypeCategoryList" property="value" labelProperty="label"/>
									</html:select>
				                    <html:hidden property="deliveryPcPre" styleId="deliveryPcPre"/>
								</c:if>
								<c:if test="${!isOnlineOrder}">
									<html:select styleId="deliveryPcPreCategory" property="deliveryPcPreCategory" tabindex="309" styleClass="c_disable" style="width: 200px;">
										<c:if test="${newData}">
					                        <html:option value=""></html:option>
										</c:if>
				                        <html:options collection="preTypeCategoryList" property="value" labelProperty="label"/>
									</html:select>
				                    <html:hidden property="deliveryPcPre" styleId="deliveryPcPre"/>
								</c:if>
			                </td>

						</tr>
						<tr>
							<th><div class="col_title_right">TEL<c:if test="${isOnlineOrder}">※</c:if></div></th>
							<td>
								<c:if test="${isOnlineOrder}">
			                    	<html:text styleId="deliveryTel" property="deliveryTel" tabindex="310" style="width: 200px; ime-mode:disabled;" maxlength="15" />
								</c:if>
								<c:if test="${!isOnlineOrder}">
			                    	<html:text styleId="deliveryTel" property="deliveryTel" tabindex="310" style="width: 200px; ime-mode:disabled;" readonly="true" styleClass="c_disable" maxlength="15" />
								</c:if>
			                </td>
							<th><div class="col_title_right">FAX</div></th>
							<td colspan="3">
								<c:if test="${isOnlineOrder}">
			                    	<html:text styleId="deliveryFax" property="deliveryFax" tabindex="311" style="width: 200px; ime-mode:disabled;" maxlength="15" />
								</c:if>
								<c:if test="${!isOnlineOrder}">
			                    	<html:text styleId="deliveryFax" property="deliveryFax" tabindex="311" style="width: 200px; ime-mode:disabled;" readonly="true" styleClass="c_disable" maxlength="15" />
								</c:if>
			                </td>
						</tr>
						<tr>
							<th><div class="col_title_right">E-MAIL</div></th>
							<td colspan="5">
								<c:if test="${isOnlineOrder}">
			                    	<html:text styleId="deliveryEmail" property="deliveryEmail" tabindex="312" style="width: 400px; ime-mode:disabled;" maxlength="255" />
								</c:if>
								<c:if test="${!isOnlineOrder}">
			                    	<html:text styleId="deliveryEmail" property="deliveryEmail" tabindex="312" style="width: 400px; ime-mode:disabled;" readonly="true" styleClass="c_disable" maxlength="255" />
								</c:if>
			                </td>
						</tr>
					</table>
					</div><!-- /.section_body -->
				</div><!-- /.form_section -->
			</div><!-- /.form_section_wrap -->

		<div id="order_detail_info_wrap">
		<table summary="受注商品明細リスト" class="forms detail_info" style="margin-top: 20px;">
			<colgroup>
				<col span="1" style="width: 5%">
				<col span="1" style="width: 13%">
				<col span="1" style="width: 23%">
				<col span="1" style="width: 5%">
				<col span="1" style="width: 5%">
				<col span="1" style="width: 8%">
				<col span="1" style="width: 8%">
				<col span="1" style="width: 23%">
				<col span="1" style="width: 10%">
			</colgroup>
            <thead>
				<tr>
					<th rowspan="3" class="rd_top_left" style="height: 60px; width: 30px;">No</th>
					<th rowspan="3" style="height: 60px;">商品コード<bean:message key='labels.must'/></th>
					<th style="height: 30px;">商品名</th>
					<th style="height: 30px;">棚番</th>
					<th style="height: 20px;">未納数</th>
					<th style="height: 30px;">仕入単価<bean:message key='labels.must'/></th>
					<th style="height: 30px;">売上単価<bean:message key='labels.must'/></th>
					<th style="height: 30px;">備考</th>
					<th rowspan="3" class="rd_top_right" style="height: 60px;">&nbsp;</th>
				</tr>
				<tr>
					<th rowspan="2" style="height: 30px;">商品備考</th>
					<th rowspan="2" style="height: 30px;">数量<bean:message key='labels.must'/></th>
					<th style="height: 20px;">完納区分</th>
					<th rowspan="2" style="height: 30px;">仕入金額<bean:message key='labels.must'/></th>
					<th rowspan="2" style="height: 30px;">売価金額<bean:message key='labels.must'/></th>
					<th rowspan="2" style="height: 30px;">ピッキング備考</th>
				</tr>
				<tr>
					<th style="height: 20px;">&nbsp;引当可能数&nbsp;</th>
				</tr>
            </thead>

            <tbody id="tbodyLine">
			<c:forEach var="lineList" varStatus="s" items="${lineList}">
			<c:if test='${lineList.lineNo != null}'>
				<tr id="productRow_${s.index+1}">

					<!-- No -->
					<td style="text-align: center" id="td${s.index+1}_1">
						<div class="box_1of1">
							${s.index+1}
						</div>
					</td>

					<!-- 商品コード -->
					<td style="background-color: #fae4eb;">
						<div class="box_1of1" style="margin: 5px;">
							<c:if test="${lineList.status == defaultStatusCode}" >
		                    	<html:text maxlength="20" name="lineList" styleId="productRow_${s.index+1}_code" property="productCode" tabindex="${f:h(s.index*14+1000)}" style="width: 140px;ime-mode:disabled" indexed="true"/>
							</c:if>
							<c:if test="${lineList.status != defaultStatusCode}" >
		                    	<html:text maxlength="20" name="lineList" styleId="productRow_${s.index+1}_code" property="productCode" tabindex="${f:h(s.index*14+1000)}" style="width: 140px;ime-mode:disabled" readonly="true" styleClass="c_disable" indexed="true"/>
							</c:if>
		                	<html:image styleId="productRow_${s.index+1}_icon" tabindex="${f:h(s.index*20+1001)}" src='${f:url("/images//customize/btn_search.png")}' style="width: auto; vertical-align: middle; cursor: pointer;"/>
	                	</div>
		                <html:hidden name="lineList" styleId="productRow_${s.index+1}_roLineId" property="roLineId" indexed="true"/>
		                <html:hidden name="lineList" styleId="productRow_${s.index+1}_roLineNo" property="lineNo" indexed="true"/>
		                <html:hidden name="lineList" styleId="productRow_${s.index+1}_roItemId" property="roItemId" indexed="true"/>
		                <html:hidden name="lineList" styleId="productRow_${s.index+1}_supplierPcode" property="supplierPcode" indexed="true"/>
		                <html:hidden name="lineList" styleId="productRow_${s.index+1}_roMaxNum" property="roMaxNum" indexed="true"/>
		                <html:hidden name="lineList" styleId="productRow_${s.index+1}_customerPcode" property="customerPcode" indexed="true"/>
		                <html:hidden name="lineList" styleId="productRow_${s.index+1}_deletable" property="deletable" indexed="true"/>
	                </td>

	                <!-- 商品名・商品備考 -->
					<td>
		                <div class="box_1of2" id="productRow_${s.index+1}_name" style="position: static; white-space: normal;" >
		                	${f:h(lineList.productAbstract)}
		                </div>
		                <div class="box_2of2">
		                	<html:textarea name="lineList"  indexed="true" styleId="productRow_${s.index+1}_productRemarks" property="productRemarks"  style="margin: 4px 0 7px 0; width: 210px;" tabindex="${f:h(s.index*14+1002)}" readonly="true" styleClass="c_disable"/>
		                </div>
		                <html:hidden name="lineList" property="productAbstract" styleId="productRow_${s.index+1}_name_hidden" indexed="true"/>
	                </td>

	                <!-- 棚版・数量 -->
					<td style="padding: 0">
						<div class="box_1of3">
	                    	<html:text name="lineList" styleId="productRow_${s.index+1}_rack" property="rackCodeSrc" style="margin: 3px; height: 24px;" tabindex="${f:h(s.index*14+1003)}" readonly="true" styleClass="c_disable" indexed="true"/><br>
	                    </div>
	                    <div class="box_2of3" style="border-bottom: 0; background-color: #fae4eb;">
							<c:if test="${lineList.status != 9}" >
		                    	<html:text maxlength="6" name="lineList" styleId="productRow_${s.index+1}_quantity" property="quantity" style="text-align:right; ime-mode:disabled; margin: 3px; height: 24px;" tabindex="${f:h(s.index*14+1004)}" styleClass="numeral_commas" indexed="true"/>
							</c:if>
							<c:if test="${lineList.status == 9}" >
		                    	<html:text maxlength="6" name="lineList" styleId="productRow_${s.index+1}_quantity" property="quantity" style="text-align:right; ime-mode:disabled; margin: 3px; height: 24px;" tabindex="${f:h(s.index*14+1004)}" readonly="true" styleClass="c_disable numeral_commas" indexed="true"/>
							</c:if>
						</div>
						<html:hidden name="lineList" property="quantityDB" styleId="productRow_${s.index+1}_quantity_hidden" indexed="true"/>
						<div class="box_3of3" style="background-color: #fae4eb;">
							<button id="productRow_${s.index+1}_stockBtn" tabindex="${f:h(s.index*14+1005)}" class="btn_list_action" style="margin: 3px;">在庫</button>
						</div>
					</td>

					<!-- 未納数・完納区分・引当可能数 -->
					<td style="text-align:center;">
						<div class="box_1of3">
		                	<span class="numeral_commas " id="productRow_${s.index+1}_restQuantity" >${f:h(lineList.restQuantity)}</span>
		                </div>
		                <div class="box_2of3">
		                	<span id="productRow_${s.index+1}_statusName" >${f:h(lineList.statusName)}</span>
		                </div>
		                <html:hidden name="lineList" property="status" styleId="productRow_${s.index+1}_status" indexed="true"/>
		                <div class="box_3of3">
		                	<span id="productRow_${s.index+1}_possibleDrawQuantity" >${f:h(lineList.possibleDrawQuantity)}</span>
		                	<!--<html:text name="lineList" styleId="productRow_${s.index+1}_possibleDrawQuantity" property="possibleDrawQuantity" indexed="true" readonly="true" styleClass="numeral_commas" style="border: 0px;text-align: center;width: 80px; margin: 3px; height: 24px;" />-->
						</div>
		                <html:hidden name="lineList" property="restQuantity" styleId="productRow_${s.index+1}_restQuantity_hidden" indexed="true"/>
		                <html:hidden name="lineList" property="restQuantityDB" styleId="productRow_${s.index+1}_restQuantity_db_hidden" indexed="true"/>
		                <html:hidden name="lineList" property="taxCategory" styleId="productRow_${s.index+1}_taxCategory_hidden" indexed="true"/>
		                <html:hidden name="lineList" property="ctaxRate" styleId="productRow_${s.index+1}_ctaxRate_hidden" indexed="true"/>
		                <html:hidden name="lineList" property="ctaxPrice" styleId="productRow_${s.index+1}_ctaxPrice_hidden" indexed="true"/>
		                <html:hidden name="lineList" property="stockCtlCategory" styleId="productRow_${s.index+1}_stockCtlCategory_hidden" indexed="true"/>
	                </td>

	                <!-- 仕入単価・仕入れ金額 -->
					<td style="background-color: #fae4eb;">
						<c:if test="${lineList.status == defaultStatusCode}" >
							<div class="box_1of2">
		                		<html:text maxlength="9" name="lineList" styleId="productRow_${s.index+1}_unitCost" property="unitCost" style="text-align:right;width: 75px;ime-mode:disabled;" tabindex="${f:h(s.index*14+1006)}" styleClass="c_disable numeral_commas" indexed="true" readonly="true"  /><br>
		                	</div>
		                	<div class="box_2of2">
		                		<html:text maxlength="9" name="lineList" styleId="productRow_${s.index+1}_cost" property="cost" style="text-align:right;width: 75px;ime-mode:disabled;" tabindex="${f:h(s.index*14+1007)}" styleClass="c_disable numeral_commas" indexed="true" readonly="true" />
		                	</div>
						</c:if>
						<c:if test="${lineList.status != defaultStatusCode}" >
							<div class="box_1of2">
		                		<html:text maxlength="9" name="lineList" styleId="productRow_${s.index+1}_unitCost" property="unitCost" style="text-align:right;width: 75px;ime-mode:disabled;" tabindex="${f:h(s.index*20+1007)}" readonly="true" styleClass="c_disable numeral_commas" indexed="true"/><br>
		                	</div>
		                	<div class="box_2of2">
		                		<html:text maxlength="9" name="lineList" styleId="productRow_${s.index+1}_cost" property="cost" style="text-align:right;width: 75px;ime-mode:disabled;" tabindex="${f:h(s.index*20+1008)}" readonly="true" styleClass="c_disable numeral_commas" indexed="true"/>
		                	</div>
						</c:if>
	                </td>

	                <!-- 売上単価・売価金額 -->
					<td style="background-color: #fae4eb;">
						<c:if test="${lineList.status == defaultStatusCode}" >
							<div class="box_1of2">
			                	<html:text maxlength="9" name="lineList" styleId="productRow_${s.index+1}_unitRetailPrice" property="unitRetailPrice" style="text-align:right;width: 75px;" tabindex="${f:h(s.index*14+1008)}" styleClass="numeral_commas" indexed="true"/><br>
			                </div>
			                <div class="box_2of2">
		    	            	<html:text maxlength="9" name="lineList" styleId="productRow_${s.index+1}_retailPrice" property="retailPrice" style="text-align:right;width: 75px;" tabindex="${f:h(s.index*14+1009)}" styleClass="numeral_commas" indexed="true"/>
		    	            </div>
						</c:if>
						<c:if test="${lineList.status != defaultStatusCode}" >
							<div class="box_1of2">
			                	<html:text maxlength="9" name="lineList" styleId="productRow_${s.index+1}_unitRetailPrice" property="unitRetailPrice" style="text-align:right;width: 75px;" tabindex="${f:h(s.index*20+1009)}" readonly="true" styleClass="c_disable numeral_commas" indexed="true"/><br>
			                </div>
			                <div class="box_2of2">
		    	            	<html:text maxlength="9" name="lineList" styleId="productRow_${s.index+1}_retailPrice" property="retailPrice" style="text-align:right;width: 75px;" tabindex="${f:h(s.index*20+1010)}" readonly="true" styleClass="c_disable numeral_commas" indexed="true"/>
		    	            </div>
						</c:if>
	                </td>

	                <!-- 備考・ピッキング備考 -->
					<td>
						<c:if test="${lineList.status != 9}" >
							<div class="box_1of2" style="vertical-align:center;">
	                    		<html:textarea name="lineList" styleId="productRow_${s.index+1}_remarks" property="remarks" style="margin: 2px 0 7px 0; width: 210px;" tabindex="${f:h(s.index*14+1010)}" indexed="true"/><br>
	                    	</div>
						</c:if>
						<c:if test="${lineList.status == 9}" >
							<div class="box_1of2" style="vertical-align:center;">
	                    		<html:textarea name="lineList" styleId="productRow_${s.index+1}_remarks" property="remarks" style="margin: 2px 0 7px 0; width: 210px;" tabindex="${f:h(s.index*14+1010)}" indexed="true" readonly="true" styleClass="c_disable"/><br>
	                    	</div>
						</c:if>
						<c:if test="${lineList.status == defaultStatusCode}" >
							<div class="box_2of2" style="vertical-align:center;">
	                    		<html:textarea name="lineList" styleId="productRow_${s.index+1}_eadRemarks" property="eadRemarks" style="margin: 5px 0 7px 0; width: 210px;" tabindex="${f:h(s.index*14+1011)}" indexed="true" readonly="true" styleClass="c_disable"/>
	                    	</div>
						</c:if>
						<c:if test="${lineList.status != defaultStatusCode}" >
							<div class="box_2of2" style="vertical-align:center;">
	                    		<html:textarea name="lineList" styleId="productRow_${s.index+1}_eadRemarks" property="eadRemarks" style="margin: 5px 0 7px 0; width: 210px;" tabindex="${f:h(s.index*20+1012)}" indexed="true" readonly="true" styleClass="c_disable"/>
	                    	</div>
						</c:if>
	                </td>

	                <!-- ボタン -->
					<td>
						<div class="box_1of2">
							<c:if test="${menuUpdate}">
								<c:if test="${lineList.status == defaultStatusCode}" >
									<button id="productRow_${s.index+1}_deleteRowBtn" tabindex="${f:h(s.index*14+1012)}" style="width: 80px" class="btn_list_action">削除</button>
								</c:if>
								<c:if test="${lineList.status != defaultStatusCode}" >
									<button  disabled="disabled" id="productRow_${s.index+1}_deleteRowBtn" tabindex="${f:h(s.index*14+1012)}" style="width: 80px" class="btn_list_action">削除</button>
								</c:if>
							</c:if>
							<c:if test="${!menuUpdate}">
								<button  disabled="disabled" id="productRow_${s.index+1}_deleteRowBtn" tabindex="${f:h(s.index*14+1012)}" style="width: 80px" class="btn_list_action">削除</button>
							</c:if>
						</div>
						<div class="box_2of2">
							<c:if test="${s.index == 0}">
								<button id="productRow_${s.index+1}_copyRowBtn" tabindex="${f:h(s.index*14+1013)}" style="width: 80px;" disabled="disabled" class="btn_list_action">前行複写</button>
							</c:if>
							<c:if test="${s.index > 0}">
								<c:if test="${menuUpdate}">
									<c:if test="${lineList.status == defaultStatusCode}" >
										<button id="productRow_${s.index+1}_copyRowBtn" tabindex="${f:h(s.index*14+1013)}" style="width: 80px;" class="btn_list_action">前行複写</button>
									</c:if>
									<c:if test="${lineList.status != defaultStatusCode}" >
										<button id="productRow_${s.index+1}_copyRowBtn" tabindex="${f:h(s.index*14+1013)}" style="width: 80px;" disabled="disabled" class="btn_list_action">前行複写</button>
									</c:if>
								</c:if>
								<c:if test="${!menuUpdate}">
									<button id="productRow_${s.index+1}_copyRowBtn" tabindex="${f:h(s.index*14+1013)}" style="width: 80px;" disabled="disabled" class="btn_list_action">前行複写</button>
								</c:if>
							</c:if>
						</div>
					</td>
				</tr>

				<script type="text/javascript">
	                // イベントの貼り付け
	                $("#productRow_${s.index+1}_code").bind("focus", {lineNo: ${s.index+1}}, function(e){ this.curVal=this.value; });
	                $("#productRow_${s.index+1}_code").bind("blur", {lineNo: ${s.index+1}}, function(e){ if(this.curVal!=this.value){ searchProduct(e); } });
	                $("#productRow_${s.index+1}_icon").bind("click", {lineNo: ${s.index+1}}, openSearchProduct);
	                $("#productRow_${s.index+1}_stockBtn").bind("click", {lineNo: 'productRow_${s.index+1}'}, onStockButton);
	                if ($("#productRow_${s.index+1}_deletable").val() == "false") {
	                    $("#productRow_${s.index+1}_deleteRowBtn").attr("disabled", true);
	                    $("#productRow_${s.index+1}_copyRowBtn").attr("disabled", true);
	                }
	                $("#productRow_${s.index+1}_deleteRowBtn").bind("click", {lineId: '${lineList.roLineId}', lineNo: '${s.index+1}'}, deleteRow);
	                $("#productRow_${s.index+1}_copyRowBtn").bind("click", {lineNo: '${s.index+1}'}, copyRow);
	//                    $("#productRow_${s.index+1}_quantity").bind("change", {id: 'productRow_${s.index+1}'}, onChangeQuantity);
	                $("#productRow_${s.index+1}_quantity").bind("focus", {id: 'productRow_${s.index+1}'}, function(e){ this.curVal=this.value; });
	                $("#productRow_${s.index+1}_quantity").bind("blur", {id: 'productRow_${s.index+1}'}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){ onChangeQuantity(e,this.curVal); } });
	//                    $("#productRow_${s.index+1}_unitCost").bind("change", {id: 'productRow_${s.index+1}'}, calcCostEvent);
	//                    $("#productRow_${s.index+1}_unitRetailPrice").bind("change", {id: 'productRow_${s.index+1}'}, calcRetailEvent);
	                $("#productRow_${s.index+1}_unitCost").bind("focus", {id: 'productRow_${s.index+1}'}, function(e){ this.curVal=this.value; });
	                $("#productRow_${s.index+1}_unitCost").bind("blur", {id: 'productRow_${s.index+1}'}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){ calcCostEvent(e); } });
	                $("#productRow_${s.index+1}_unitRetailPrice").bind("focus", {id: 'productRow_${s.index+1}'}, function(e){ this.curVal=this.value; });
	                $("#productRow_${s.index+1}_unitRetailPrice").bind("blur", {id: 'productRow_${s.index+1}'}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){ calcRetailEvent(e); } });
	                maxLineCount = ${s.index+1};
	            </script>
			</c:if>
         	</c:forEach>

         	<!-- 追加ボタン -->
			<tr id="trAddLine">
				<td style="height: 60px; text-align: center" colspan="9" class="rd_bottom_left rd_bottom_right">
    				<c:if test="${menuUpdate}">
                    	<button tabindex="1999" onclick="addRow()">
                    		<img alt="行追加" border="none" src="${f:url('/images/customize/btn_line_add.png')}"  width="31" height="33">
                    	</button>
					</c:if>
    				<c:if test="${!menuUpdate}">
                    	<button tabindex="1999" disabled="disabled">
                    		<img alt="行追加" border="none" src="${f:url('/images/customize/btn_line_add.png')}"  width="31" height="33">
                    	</button>
					</c:if>
                </td>
			</tr>
            </tbody>
		</table>
		</div>

		<html:hidden styleId="deleteLineIds" property="deleteLineIds"/>

		<div id="poSlipPriseInfos" class="information" style="margin-top: 10px;">
        <div id="information" class="information" style="">
			<table id="voucher_info" class="forms" summary="伝票情報" style="">
				<tr>
					<th style="height: 60px;" class="rd_top_left">粗利益</th>
					<th>粗利益率</th>
					<th>金額合計</th>
					<th>消費税</th>
					<th class="rd_top_right">伝票合計</th>
				</tr>
				<tr>
					<td id="grossDisp" style="text-align:center; height: 100px;" class="BDCyen yen_value">&nbsp;${f:h(gross)}</td>
					<td id="grossRatioDisp" style="text-align:center;" class="numeral_commas" >&nbsp;${f:h(grossRatio)}</td>
					<td id="retailPriceTotalDisp" style="text-align:center;" class="BDCyen yen_value" >&nbsp;${f:h(retailPriceTotal)}</td>
					<td id="ctaxPriceTotalDisp" style="text-align:center;" class="BDCtax yen_value" >&nbsp;${f:h(ctaxPriceTotal)}</td>
					<td id="priceTotalDisp" style="text-align:center;" class="BDCyen yen_value" >&nbsp;${f:h(priceTotal)}</td>
				</tr>
			</table>

			<html:hidden property="gross" styleId="gross"/>
			<html:hidden property="grossRatio" styleId="grossRatio"/>
			<html:hidden property="retailPriceTotal" styleId="retailPriceTotal"/>
			<html:hidden property="ctaxPriceTotal" styleId="ctaxPriceTotal"/>
			<html:hidden property="priceTotal" styleId="priceTotal"/>
			<html:hidden property="costTotal" styleId="costTotal"/>
			<html:hidden property="newData" />
		</div>
		</div>

		<div style="width: 1160px; text-align: center; margin-top: 10px;">
			<c:if test="${!newData}">
			    <c:if test="${menuUpdate}">
					<c:if test="${statusUpdate}">
			    		<button type="button" tabindex="1999" id="btnF3btm" style="width:260px; height:51px;" class="btn_medium" onclick="onF3()">
			    			<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.renew'/></span><%// 更新 %>
			    		</button>
			        </c:if>
					<c:if test="${!statusUpdate}">
			    		<button type="button" disabled="disabled" id="btnF3btm" style="width:260px; height:51px;" class="btn_medium" tabindex="1999">
			    			<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.renew'/></span><%// 更新 %>
			    		</button>
			        </c:if>
			    </c:if>
			    <c:if test="${!menuUpdate}">
					<button type="button" disabled="disabled" id="btnF3btm" style="width:260px; height:51px;" class="btn_medium" tabindex="1999">
						<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.renew'/></span><%// 更新 %>
					</button>
			    </c:if>
			</c:if>
			<c:if test="${newData}">
			    <c:if test="${menuUpdate}">
					<button type="button" tabindex="1999" id="btnF3btm" style="width:260px; height:51px;" class="btn_medium" onclick="onF3()">
						<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.register'/></span><%// 登録 %>
					</button>
			    </c:if>
			    <c:if test="${!menuUpdate}">
					<button type="button" disabled="disabled" id="btnF3btm" style="width:260px; height:51px;" class="btn_medium" tabindex="1999">
						<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.register'/></span><%// 登録 %>
					</button>
			    </c:if>
			</c:if>
		</div>
	</div>
	</s:form>
</div>
</body>
</html>
