<%@page import="jp.co.arkinfosys.common.Constants" %>
<%@page import="jp.co.arkinfosys.common.CategoryTrns" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title><bean:message key='titles.system'/>　仕入入力</title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
		var MAX_LINE_ROW_COUNT = ${f:h(maxLineRowCount)};
		var MAIN_FORM_NAME = "purchase_inputPurchaseActionForm";
		var maxIndex = 0;

		var initFormElementSize = 0;
		var initValuesDigest = "";

		var taxShiftCategorySlipTotal = "<%=CategoryTrns.TAX_SHIFT_CATEGORY_SLIP_TOTAL%>";
		var taxShiftCategoryCloseTheBooks = "<%=CategoryTrns.TAX_SHIFT_CATEGORY_CLOSE_THE_BOOKS%>";
		// 数量の丸め（桁=自社マスタ.数量少数桁、丸め=自社マスタ.商品端数処理）
		var quantityAlignment = ${mineDto.numDecAlignment};
		var quantityCategory = ${mineDto.productFractCategory};

		//単価・金額の必須表示
		function changeMustStatus(){
			var l_rateId = oBDCS($("#rateId").val());
			if(!l_rateId.isNum()){
				$("span.dolMust").css("display","none");
				$("span.yenMust").css("display","");
			}else{
				$("span.dolMust").css("display","");
				$("span.yenMust").css("display","none");
			}
		}

		// ページ読込時の動作
		function init() {
			// 明細行のIndex管理
			maxIndex = $("#tbodyLine").get(0).children.length-1;

			// 明細行のクローンを生成
			trCloneBase = $("#trLine0").clone(true);

			// 明細行の項目にイベントをバインド
			for(var i=0; i<=maxIndex; i++) {
				$("#rackCodeImg" + i).bind("click", {index: i}, searchRack);
				$("#productCodeImg" + i).bind("click", {index: i}, changeProductCode);
				$("#deleteBtn" + i).bind("click", {index: i}, deleteRow);
				$("#stockBtn" + i).bind("click", {index: i}, openStockInfo);
			}
			if( ${menuUpdate} ){
				$("#deleteBtn0").attr("disabled",(maxIndex <= 0));
			}
			// 画面の初期状態を記憶する
			var initForms = $(document.purchase_inputPurchaseActionForm).find("input, select, textarea");
			initFormElementSize = initForms.size();

			initForms.each(
				function() {
					initValuesDigest += this.value;
				}
			);
			//買掛系の金額自動計算（単体懸案 #106）
			manager = new SlipPriceManager();
			manager.init();

			// 初期フォーカス
//			$("#remarks").focus();
			if( $("#supplierSlipId").val() != "" ){
				$("#supplierSlipId").attr("readOnly", "true");
				$("#supplierSlipId").addClass("c_disable");

				// 発注番号も入力不可とする。
				$("#poSlipId").attr("readOnly", "true");
				$("#poSlipId").addClass("c_disable");
				$("#remarks").focus();
			}else{
				// 売上番号は、空欄
				// 発注番号が空欄ではない（伝票複写した）
				if( $("#poSlipId").val() != "" ){
					$("#supplierSlipId").attr("readOnly", "true");
					$("#supplierSlipId").addClass("c_disable");
					$("#remarks").focus();
				}
				else{
					$("#supplierSlipId").focus();
				}
			}

			applyNumeralStylesToObj(${mineDto.priceFractCategory},${mineDto.unitPriceDecAlignment},$(".price"));
			_after_load($(".price"));

			//必須表示の変更
			changeMustStatus();
		}

		function findSlip(){
			if( $("#supplierSlipId").val() == "" ){
			}else{
				showNowSearchingDiv();
//				window.location.doHref('${f:url("edit/")}'+ $("#supplierSlipId").val());
				$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/purchase/inputPurchase/load")}');
				$("form[name='" + MAIN_FORM_NAME + "']").submit();
			}
		}
		// 伝票複写（発注番号入力）
		function findCopy(){
			if(( $("#poSlipId").val() != "" )&&(!$("#poSlipId").attr("readOnly"))){
				if(confirm("<bean:message key='confirm.copyslip'/>")){
					$("#supplierSlipId").attr("readOnly", "true");
					$("#supplierSlipId").addClass("c_disable");

					// 発注伝票
					purCopySlip("PORDER", $("#poSlipId").val());
				}
				else{
					$("#poSlipId").val("");
				}
			}
		}

		// 初期化
		function onF1(){
			// 入力内容を初期化してよろしいですか？
			if(confirm('<bean:message key="confirm.init" />')){

				window.location.doHref('${f:url("/purchase/inputPurchase")}');
				$("#supplierSlipId").focus();
			}
		}

		// 削除
		function onF2(){
			if(${f:h(newData)||f:h(cuttOff)?"true":"false"}) {
				return;
			}
			// 削除してよろしいですか？
			if(confirm('<bean:message key="confirm.delete" />')){
				manager.prepareSubmit();
				$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/purchase/inputPurchase/delete")}');
				$("form[name='" + MAIN_FORM_NAME + "']").submit();
			}
		}

		// 登録
		function onF3(){
			// 入力内容を登録します。よろしいですか？
			<c:if test="${f:h(newData)}">
			if(confirm('<bean:message key="confirm.insert" />')){
				manager.prepareSubmit();
				$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/purchase/inputPurchase/upsert")}');
				$("form[name='" + MAIN_FORM_NAME + "']").submit();
			}
			</c:if>
			<c:if test="${!f:h(newData)}">
			if(confirm('<bean:message key="confirm.update" />')){
				manager.prepareSubmit();
				$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/purchase/inputPurchase/upsert")}');
				$("form[name='" + MAIN_FORM_NAME + "']").submit();
			}
			</c:if>
		}

		function purCopySlip(slipName, copySlipId) {

			if( slipName == "PORDER" || slipName == "ENTRUST_PORDER" ){
				// 発注伝票
				$("#copySlipId").val($("#poSlipId").val());
				document.purchase_inputPurchaseActionForm.action = '${f:url("copySlipFromPorderLoad")}';
				manager.prepareSubmit();
				document.purchase_inputPurchaseActionForm.submit();
			}
		}


		// 伝票複写ボタン
		function onF6(){
			openCopySlipDialog('0800', 'copySlipFrom0800',
					function (dialogId, slipName, copySlipId) {
				if(isChangedForm() && !confirm("<bean:message key='confirm.copyslip'/>")){
					// フォームが変更されている場合
					return;
				}

				if( slipName == "PORDER" || slipName == "ENTRUST_PORDER" ){
					// 発注伝票
					document.purchase_inputPurchaseActionForm.action = '${f:url("copySlipFromPorder/' + copySlipId + '")}';
					manager.prepareSubmit();
					document.purchase_inputPurchaseActionForm.submit();
				}
			}
			);

			// 伝票複写ダイアログのhidden項目に、検索対象の発注明細ステータスをセットする(委託発注用)
			$("#copySlipFrom0800_entrustPorderCondition\\.targetPoLineStatus").val("<%=Constants.STATUS_PORDER_LINE.ENTRUST_STOCK_DELIVERED%>");
		}

		/**
		 * フォームの内容が変更されているか否かを確認する
		 * 変更されている:true
		 */
		function isChangedForm() {
			var currentForms = $(document.purchase_inputPurchaseActionForm).find("input, select, textarea");
			if(initFormElementSize != currentForms.size()) {
				return true;
			}

			var currentValuesDigest = "";
			currentForms.each(
				function() {
					currentValuesDigest += this.value;
				}
			);
			if(initValuesDigest != currentValuesDigest) {
				return true;
			}

			return false;
		}

		// 行削除
		function deleteRow(event){
			var i, lineNo, lineSize, index;
			var trId, id;

			index = event.data.index;

			if(confirm('<bean:message key="confirm.line.delete" />')){
				// 行を削除する
				lineNo = parseInt($("#lineDtoList\\["+index+"\\]\\.lineNo").val())-1;
				// 削除行IDを保存
				var deleteLineId = $("#lineDtoList\\["+index+"\\]\\.supplierLineId").val(i);;
				if(deleteLineId != null && deleteLineId.length > 0){

					var ids = $("#deleteLineIds").val();
					if(ids.length > 0){
						ids += ",";
					}
					$("#deleteLineIds").val(ids + deleteLineId);
				}
				$("#tbodyLine").get(0).deleteRow(lineNo);
				// 行番号を調整する
				lineSize = $("#tbodyLine").get(0).children.length;
				for(i=lineNo; i<lineSize; i++) {
					trId = $("#tbodyLine").get(0).children[i].id;
					id = trId.replace("trLine", "");
					// 行番号を振りなおす
					$("#tdNo"+id).html(i+1);
					$("#lineDtoList\\["+id+"\\]\\.lineNo").val(i+1);
				}

				// 残り１行の場合、削除ボタンの不活性化
				if(lineSize == 1) {
					trId = $("#tbodyLine").get(0).children[0].id;
					id = trId.replace("trLine", "");
					$("#deleteBtn"+id).get(0).disabled = true;
				}

				// 再計算
				manager = new SlipPriceManager();
				manager.init();
			}
		}

		function setName( element, name ) {
			element.attr("id", name);
			element.attr("name", name);
		}

		// 指定行から前行のIndexを返す
		// （見つからない場合は-1を返す）
		function getPrevIndex(index){
			var retVal = -1;
			var i, lineNo, trId;

			// 指定行の行番号を取得
			lineNo = parseInt($("#lineDtoList\\["+index+"\\]\\.lineNo").val());
			// 前行を探す（削除行は非表示になっている為、直前の削除されていない行を探す）
			if(lineNo>1) {
				trId = $("#tbodyLine").get(0).children[lineNo-2].id;
				retVal = trId.replace("trLine", "");
			}

			return retVal;
		}

		// 商品コード変更
		function changeProductCode(event) {
//			searchProductCode(event.data.index);
			ProductCodeSearch(event.data.index);
		}

		// 商品検索
		function searchProductCode(index) {
			var map = new Object();
			var label = '<bean:message key="labels.productCode" />';
			if($("#lineDtoList\\["+index+"\\]\\.productCode").val() == "") {
				map["productCode"] = "";
				map["supplierPcode"] = "";
				map["productName"] = "";
				map["warehouseName"] = $("#lineDtoList\\["+index+"\\]\\.warehouseName").val();
				map["rackCode"] = $("#lineDtoList\\["+index+"\\]\\.rackCode").val();
				map["rackName"] = $("#lineDtoList\\["+index+"\\]\\.rackName").val();
				setProductInfo(index, map);
			}
			var data = new Object();
			data["productCode"] = $("#lineDtoList\\["+index+"\\]\\.productCode").val();
			asyncRequest(
				contextRoot + "/ajax/commonProduct/getProductInfos",
				data,
				function(data) {
					if(data==""){
						alert('<bean:message key="errors.notExist" arg0="'+label+'" />');
						map["supplierPcode"] = "";
						map["productName"] = "";
						map["rackCode"] = $("#lineDtoList\\["+index+"\\]\\.rackCode").val();
						map["rackName"] = $("#lineDtoList\\["+index+"\\]\\.rackName").val();
						map["warehouseName"] = $("#lineDtoList\\["+index+"\\]\\.warehouseName").val();
						setProductInfo(index, map);
					} else {
						var value = eval("(" + data + ")");
						// 商品コード
						$("#lineDtoList\\["+index+"\\]\\.productCode").val(value.productCode);
						// 相手先品番
						$("#lineDtoList\\["+index+"\\]\\.supplierPcode").val(value.supplierPcode);
						// 商品名（表示用）
						$("#productAbstract"+index).html(value.productName);
						// 商品名
						$("#lineDtoList\\["+index+"\\]\\.productAbstract").val(value.productName);
						// 倉庫名
						$("#lineDtoList\\["+index+"\\]\\.warehouseName").val(value.warehouseName);
						// 棚番
						$("#lineDtoList\\["+index+"\\]\\.rackCode").val(value.rackCode);
						// 棚番名
						$("#lineDtoList\\["+index+"\\]\\.rackName").val(value.rackName);
					}
				}
			 );
		}

		// 商品検索ダイアログ
		function ProductCodeSearch(id){

			openSearchProductDialog( id ,setProductInfo);
			// ダイアログのフィールドに値をセットしてフォーカス
			$("#" + id + "_productCode").val($("#lineDtoList\\["+id+"\\]\\.productCode").val());
			$("#" + id + "_productCode").focus();
		}

		// 商品検索後の設定処理
		function setProductInfo(index, map) {
			// 商品コード
			$("#lineDtoList\\["+index+"\\]\\.productCode").val(map["productCode"]);
			// 相手先品番
			$("#lineDtoList\\["+index+"\\]\\.supplierPcode").val(map["supplierPcode"]);
			// 商品名（表示用）
			$("#productAbstract"+index).html(map["productName"]);
			// 商品名
			$("#lineDtoList\\["+index+"\\]\\.productAbstract").val(map["productName"]);
			// 課税区分
			$("#lineDtoList\\["+index+"\\]\\.taxCategory").val(map["taxCategory"]);
			// 棚番
			$("#lineDtoList\\["+index+"\\]\\.rackCode").val(map["rackCode"]);
			// 棚番名
			$("#lineDtoList\\["+index+"\\]\\.rackName").val(map["rackName"]);
			// 倉庫名
			$("#lineDtoList\\["+index+"\\]\\.warehouseName").val(map["warehouseName"]);
		}

		// 棚検索
		function searchRack(event) {
			var index = event.data.index;
			// 棚検索ダイアログを開く
			openSearchRackDialog(index, setRackInfo );

			$("#"+ index +"_warehouseName").val($("#lineDtoList\\["+index+"\\]\\.warehouseName").val());
			$("#"+ index +"_rackCode").val($("#lineDtoList\\["+index+"\\]\\.rackCode").val());
			$("#"+ index +"_rackCode").focus();
		}

		// 棚検索後の設定処理
		function setRackInfo(index, map) {
			// 棚番
			$("#lineDtoList\\["+index+"\\]\\.rackCode").val(map["rackCode"]);
			// 棚番名
			$("#lineDtoList\\["+index+"\\]\\.rackName").val(map["rackName"]);
			// 倉庫名
			$("#lineDtoList\\["+index+"\\]\\.warehouseName").val(map["warehouseName"]);
		}

		//在庫ボタンクリック
		function openStockInfo(event) {
			index = event.data.index;
			// 在庫ダイアログを開く
			var productCode = $("#lineDtoList\\["+index+"\\]\\.productCode").val();
			if(!productCode){
				return;
			}
			openStockInfoDialog('stockInfo', productCode);
		}

		//レートの取得
		function GetSupplierRate(){

			// 仕入先コードか仕入日に入力がない場合はレートをクリアする
			if ( ($("#supplierCode").val() == "") ||
					($("#supplierDate").val() == "") ) {
				$("#rate").val("");

			// 仕入先コードと仕入日に入力がある場合
			} else {
				var data = new Object();
				data["tempSupplierCode"] = $("#supplierCode").val();
				data["targetDate"] = $("#supplierDate").val();

				$.ajax({
					"type" : "POST",
					"async" : false,
					"url" : contextRoot + "/ajax/commonPurchase/getSupplierRate/",
					"data" : data,
					"success" : function(result) {
						if (_isNum(result)) {
							var rate = new BigDecimal(result);
							rate = rate.setScale(2, BigDecimal.prototype.ROUND_DOWN);
							$("#rate").val(rate.toString());
						} else {
							$("#rate").val(result);
						}
						changeRate($("#rate").val());
					}
				});
			}
			return false;
		}

		//税率の取得
		function GetSupplierTaxRate(){
			if( ( $("#supplierCode").attr("value") == "" )
			//		|| ( $("#supplierDate").attr("value") == "" )
					){
				$("#cTaxRate").attr("value","");
			}else{
				//	var data = new Object();
				//data["tempSupplierCode"] = $("#supplierCode").attr("value");
				//data["targetDate"] = $("#supplierDate").attr("value");

				//$.ajax({
				//	"type" : "POST",
				//	"async" : false,
				//	"url" : contextRoot + "/ajax/commonPurchase/getSupplierTaxRate/",
				//	"data" : data,
				//	"success" : function(result) {
				//		$("#supplierTaxRate").attr("value",result);
						/////////////ChangeSupplierTaxRate();
				//	}
				//});
			}
		}

		//仕入日変更
		function ChangeSupplierDate(){

			//レートを取得
			GetSupplierRate();

			//税率取得
			// 仕入日の変更で税率を変更しないようにする。発注時に設定した税率を維持。
			//GetSupplierTaxRate();

			// レート設定されていない場合は、レートによる明細の金額計算を行わない
			if ($("#rate").val() == "") {
				return;
			}

			// 取得したレートで明細行の金額を再計算する
			fullCalc();
		}

		//全計算
		 function fullCalc(){
			 var sumPrice = 0;

			// レートを取得
			var rate = oBDCS($("#rate").val()).BDValue();

			// 明細行の再計算
			for (var row = 0; row < $("#tbodyLine").get(0).children.length ; row++) {

				// 明細の商品コードに入力がない場合
				if ($("#lineDtoList\\["+row+"\\]\\.productCode").val() == "") {
					continue;
				}

				// 外貨単価を取得
				var dolUnitPrice = oBDCS($("#lineDtoList\\["+row+"\\]\\.dolUnitPrice").val()).BDValue();

				// 数量を取得
				var quantity = oBDCS($("#lineDtoList\\["+row+"\\]\\.quantity").val()).BDValue();

				// 円単価を設定（円単価 = レート×外貨単価）
				var unitPrice = rate * dolUnitPrice;
				$("#lineDtoList\\["+row+"\\]\\.unitPrice").valueBDC(unitPrice.toString());

				// 金額(円)を設定（金額(円) = 円単価×数量）
				var price = unitPrice * quantity;
				$("#lineDtoList\\["+row+"\\]\\.price").valueBDC(price.toString());
				sumPrice = sumPrice + price;
			}

			// 伝票合計(円)設定
			$("#nonTaxPriceTotal").valueBDC(sumPrice.toString());

			// 本体金額合計(円)設定
			$("#priceTotal").valueBDC(sumPrice.toString());

			return;
		}

		// 数量の変更で完納区分をセット
		function setDeliveryProcessCategory(quantityElement){
			if(!_isNum(quantityElement.value)) {
				return;
			}
			var trObj = $(quantityElement).parent().parent().parent().parent();
			if(!trObj) {
				return;
			}
			var zero = new BigDecimal("0");

			// 数量
			var quantity = new BigDecimal(_getNumStr(trObj.find("[name$='quantity']").val()));
			// 更新前数量
			var oldQuantity = trObj.find("[name$='oldQuantity']").val();
			if(!oldQuantity) {
				oldQuantity = "0";
			}
			oldQuantity = new BigDecimal(oldQuantity);
			// 発注残数量
			var restQuantity = trObj.find("[name$='restQuantity']").val();
			if(!restQuantity) {
				restQuantity = "0";
			}
			restQuantity = new BigDecimal(restQuantity);
			// トータル発注数量
			var totalQuantity = trObj.find("[name$='totalQuantity']").val();
			if(!totalQuantity) {
				totalQuantity = "0";
			}
			totalQuantity = new BigDecimal(totalQuantity);
			// 他の仕入伝票で処理された数量を計算する
			var otherQuantity = totalQuantity.subtract( restQuantity.add( oldQuantity ) );
			// 新しい残数を計算する
			var newRestQuantity = totalQuantity.subtract( quantity.add( otherQuantity ) );
			if( newRestQuantity.compareTo( zero ) == 0 ){
				// 完納
				trObj.find("[name$='deliveryProcessCategory']").val('<%=CategoryTrns.DELIVERY_PROCESS_CATEGORY_FULL%>');
			}
			else {
				if( newRestQuantity.compareTo( totalQuantity ) == 0 ) {
					// 未納
					trObj.find("[name$='deliveryProcessCategory']").val('<%=CategoryTrns.DELIVERY_PROCESS_CATEGORY_NONE%>');
				}
				else {
					// 分納
					trObj.find("[name$='deliveryProcessCategory']").val('<%=CategoryTrns.DELIVERY_PROCESS_CATEGORY_PARTIAL%>');

					if( (totalQuantity.compareTo( zero ) == -1 && newRestQuantity.compareTo( zero ) == 1)
							|| (totalQuantity.compareTo( zero ) == 1 && newRestQuantity.compareTo( zero ) == -1)) {
						// 完納(数量オーバー)
						var match = trObj.attr("id").match(/trLine([0-9]+)/);
						var lineNo = parseInt(match[1]) + 1;
						// エラー
						trObj.find("[name$='deliveryProcessCategory']").val('<%=CategoryTrns.DELIVERY_PROCESS_CATEGORY_FULL%>');
						alert('<bean:message key="errors.line.overRestQuantity" />');
					}
				}
			}

		}

		// 未納数　計算処理
		function setUnPaidQuantity(quantityElement){
			if(!_isNum(quantityElement.value)) {
				return;
			}
			var trObj = $(quantityElement).parent().parent().parent().parent();
			if(!trObj) {
				return;
			}
			var zero = new BigDecimal("0");

			// 数量
			var quantity = new BigDecimal(_getNumStr(trObj.find("[name$='quantity']").val()));
			// 更新前数量
			var oldQuantity = trObj.find("[name$='oldQuantity']").val();
			if(!oldQuantity) {
				oldQuantity = "0";
			}
			oldQuantity = new BigDecimal(oldQuantity);
			// 発注残数量
			var restQuantity = trObj.find("[name$='restQuantity']").val();
			if(!restQuantity) {
				restQuantity = "0";
			}
			restQuantity = new BigDecimal(restQuantity);
			// トータル発注数量
			var totalQuantity = trObj.find("[name$='totalQuantity']").val();
			if(!totalQuantity) {
				totalQuantity = "0";
			}
			totalQuantity = new BigDecimal(totalQuantity);
			// 他の仕入伝票で処理された数量を計算する
			var otherQuantity = totalQuantity.subtract( restQuantity.add( oldQuantity ) );
			// 新しい残数を計算する
			var newRestQuantity = totalQuantity.subtract( quantity.add( otherQuantity ) );

			// 未納数
			//var id = quantityElement.id;
			//id = id.replace("[", "\\[").replace("]", "\\]").replace(".", "\\.");
			newRestQuantity = newRestQuantity.setScale(quantityAlignment,quantityCategory);
			//$("#" + id + " ~ input[name='unPaidQuantity']").val(newRestQuantity.toString());


			trObj.find("[name='unPaidQuantity']").val(newRestQuantity.toString());


			// カンマをつける
			_set_commas(trObj.find("[name='unPaidQuantity']"));
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

		function changeCategory(categoryElement){
			var trObj = $(categoryElement).parent().parent().parent();
			if(!trObj) {
				return;
			}
			var match = trObj.attr("id").match(/trLine([0-9]+)/);
			var lineNo = parseInt(match[1]) + 1;

			var delCategory = trObj.find("[name$='deliveryProcessCategory']").val();
			var sUnPaidQuantity = trObj.find("[name$='unPaidQuantity']").val();
			var lUnPaidQuantity = Number(dec_numeral_commas(sUnPaidQuantity));

			// 完納区分を「完納」とし、未納数が０より大きい場合は、警告を表示
			if(( delCategory == '<%=CategoryTrns.DELIVERY_PROCESS_CATEGORY_FULL%>' )&& (lUnPaidQuantity > 0)){
				alert('<bean:message key="errors.line.updatePaid" />');
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
	CurrencyUnitClassNameHashList["dollar_value"] = "${sign}";
	-->
	</script>
</head>
<body onhelp="return false;" onload="init()">
	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0008"/>
		<jsp:param name="MENU_ID" value="0800"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">

		<span class="title">仕入入力</span>

		<div class="function_buttons">
			<button type="button" id="btnF1" tabindex="2000" onclick="onF1();">F1<br>初期化</button>
			<button	type="button" id="btnF2" tabindex="2001" onclick="onF2();" ${notDeletable?"disabled":""}>F2<br>削除</button>
			<c:if test="${f:h(newData)}">
				<button	type="button" id="btnF3" tabindex="2002" ${!copySlip? "disabled='disabled'":"onclick='onF3()'"}>F3<br>登録</button>
			</c:if>
			<c:if test="${!f:h(newData)}">
				<button type="button" id="btnF3" tabindex="2002" ${notRegisterable? "disabled='disabled'":"onclick='onF3()'"}>F3<br>更新</button>
			</c:if>
				<button type="button" id="btnF4" tabindex="2003" disabled>F4<br>&nbsp;</button>
				<button type="button" id="btnF5" tabindex="2004" disabled>F5<br>&nbsp;</button>
				<button type="button" id="btnF6" tabindex="2005" ${notCopiable? "disabled='disabled'":"onclick='onF6()'"}>F6<br>伝票呼出</button>
				<button type="button" id="btnF7" tabindex="2006" disabled>F7<br>&nbsp;</button>
				<button type="button" id="btnF8" tabindex="2007" disabled>F8<br>&nbsp;</button>
				<button type="button" id="btnF9" tabindex="2008" disabled>F9<br>&nbsp;</button>
				<button type="button" id="btnF10" tabindex="2009" disabled>F10<br>&nbsp;</button>
				<button type="button" id="btnF11" tabindex="2010" disabled>F11<br>&nbsp;</button>
				<button type="button" id="btnF12" tabindex="2011" disabled>F12<br>&nbsp;</button>
		</div><br><br><br>

		<s:form onsubmit="return false;">
			<div class="function_forms">

				<div id="errors" style="padding-left: 20px; color: red;">
					<html:errors />
				</div>
				<div id="messages" style="color: blue;padding-left: 20px">
					<html:messages id="msg" message="true">
						<bean:write name="msg" ignore="true"/><br>
					</html:messages>
				</div>


				<div class="form_section_wrap">
				<div class="form_section">
				<div class="section_title">
					<span >仕入伝票情報</span><br>
					<br>
					<button class="btn_toggle" />
				</div>

				<div class="section_body">


				<table id="supplier_info" class="forms" summary="仕入伝票情報">
					<tr>
						<th><div class="col_title_right">仕入番号</div></th>
						<td><html:text property="supplierSlipId"  styleClass="" styleId="supplierSlipId" style="ime-mode:disabled;"  tabindex="100" readonly="false"  maxlength="10"  onfocus="this.curVal=this.value;" onblur="if((this.curVal == '') || ((this.curVal != '')&&(this.curVal!=this.value))){findSlip()}"/></td>
						<th><div class="col_title_right">発注番号</div></th>
						<td><html:text property="poSlipId" styleClass="${poSlipId==''?'':'c_disable'}" styleId="poSlipId" style="ime-mode:disabled;" tabindex="101" readonly="${poSlipId==''?'false':'true'}" maxlength="10"  onfocus="this.curVal=this.value;" onblur="if((this.curVal == '') || ((this.curVal != '')&&(this.curVal!=this.value))){findCopy()}"/></td>
						<th><div class="col_title_right_req">仕入日<bean:message key='labels.must'/></div></th>
						<td><html:text property="supplierDate"  styleClass="date_input" styleId="supplierDate" tabindex="102" onchange="ChangeSupplierDate();" maxlength="10" style="width:135px;"/></td>
						<th><div class="col_title_right">納期</div></th>
						<td><html:text property="deliveryDate"  styleClass="date_input" styleId="deliveryDate" tabindex="103" maxlength="10" style="width:135px;"/></td>
					</tr>
					<tr>
						<th><div class="col_title_right">入力担当者</div></th>
						<td>
							<html:text property="userName" styleId="userName" styleClass="c_disable" tabindex="104" readonly="true"/>
						</td>
						<th><div class="col_title_right">レートタイプ</div></th>
						<td>
							<html:hidden property="rateId" styleId="rateId" />
							<html:text property="rateName" styleId="rateName" styleClass="c_disable" tabindex="106" readonly="true" />
						</td>
						<th><div class="col_title_right">レート</div></th>
						<td><html:text property="rate" styleId="rate" styleClass="c_disable price" tabindex="107"  readonly="true" /></td>
						<td colspan="2"></td>
					</tr>
					<tr>
						<th><div class="col_title_right">摘要</div></th>
						<td colspan="5"><html:text styleId="remarks" property="remarks" style="width: 740px;" tabindex="108" /></td>
						<td colspan="2"></td>
					</tr>
					<tr>
						<th><div class="col_title_right">消費税率</div></th>
						<td colspan="5">
							<html:select property="ctaxRate" styleId="ctaxRate" tabindex="109" style="width: 135px;" onchange="changeTaxRate(this.value)">
							    <html:options collection="ctaxRateList" property="value" labelProperty="label"/>
							</html:select>&nbsp;％
						</td>
					</tr>
				</table>
				</div>
				</div>
				</div>
				<div class="form_section_wrap">
				<div class="form_section">
				<div class="section_title">
					<span>仕入先情報</span><br>
					<br>
					<button class="btn_toggle" />
				</div>
				<div class="section_body">



				<table id="order_info" class="forms" summary="仕入先情報">
					<colgroup>
						<col span="1" style="width: 10%">
						<col span="1" style="width: 15%">

						<col span="1" style="width: 10%">
						<col span="1" style="width: 65%">
					</colgroup>
					<tr>
						<th><div class="col_title_right">仕入先コード</div></th>
						<td>
							<html:text property="supplierCode" styleId="supplierCode" styleClass="c_disable" style="width: 130px"  tabindex="200" readonly="true" />
						</td>
						<th><div class="col_title_right">仕入先名</div></th>
						<td><html:text property="supplierName" styleId="supplierName" style="width: 235px;" styleClass="c_disable" tabindex="202" readonly="true" /></td>
					</tr>
				</table>
				</div>
				</div>
				</div>
				<html:hidden property="sign" styleId="sign"/>
				<html:hidden property="userId" styleId="userId"/>
				<html:hidden property="status" styleId="status"/>
				<html:hidden property="taxShiftCategory" styleId="taxShiftCategory" />
				<html:hidden property="taxFractCategory" styleId="taxFractCategory" />
				<html:hidden property="priceFractCategory" styleId="priceFractCategory" />
				<html:hidden property="supplierCmCategory" styleId="supplierCmCategory"/>
				<html:hidden property="updDatetm" styleId="updDatetm" />
				<html:hidden property="tempPoLineId" styleId="tempPoLineId" />
				<html:hidden property="copySlipId" styleId="copySlipId" />
				<html:hidden property="initCalc" styleId="initCalc"/>
				<html:hidden property="isEntrustPorder" styleId="isEntrustPorder"/>
				<html:hidden property="deleteLineIds"  styleId="deleteLineIds"/>


				<div id="order_detail_info_wrap">
				<table id="order_detail_info" summary="仕入明細リスト" class="forms" style="margin-top: 20px;">
					<colgroup>
						<col span="1" style="width: 25px;">
						<col span="1" style="width: 90px;">
						<col span="1" style="width: 170px;">
						<col span="1" style="width: 150px;">
						<col span="1" style="width: 110px;">
						<col span="1" style="width: 110px;">
						<col span="1" style="width: 270px;">
						<col span="1" style="width: 95px;">
					</colgroup>
					<thead>
						<tr>
							<th class="rd_top_left" style="height:60px;" rowspan="3">No</th>
							<th style="height:20px;">&nbsp;</th>
							<th style="height:30px; border-bottom:1px solid #555555;">商品コード※</th>
							<th style="height:30px; border-bottom:1px solid #555555;">相手品番</th>
							<th style="height:20px;">数量※</th>
							<th style="height:20px;">未納数</th>
							<th style="height:20px;"style="height:30px;">備考</th>
							<th class="rd_top_right" style="height:60px;" rowspan="3">&nbsp;</th>
						</tr>
						<tr>
							<th style="height:20px;">完納区分</th>
							<th style="height:20px;" colspan="2" rowspan="2">商品名・摘要</th>
							<th style="height:20px;">円単価<span class="yenMust"><bean:message key='labels.must'/></span></th>
							<th style="height:20px;">金額(円)<span class="yenMust"><bean:message key='labels.must'/></span></th>
							<th style="height:20px;">倉庫名</th>
						</tr>
						<tr>
							<th style="height:20px;">納期</th>
							<th style="height:20px;">外貨単価<span class="dolMust"><bean:message key='labels.must'/></span></th>
							<th style="height:20px;">外貨金額<span class="dolMust"><bean:message key='labels.must'/></span></th>
							<th style="height:20px;">棚番※</th>
						</tr>
					</thead>
					<tbody id="tbodyLine">
						<% int lineTab = 1000; %>
						<c:forEach var="lineDtoList" items="${lineDtoList}" varStatus="status">
						  <c:if test='${lineDtoList.lineNo != null}'>
							<tr id="trLine${status.index}">

								<!-- No -->
								<td id="tdNo${status.index}" style="text-align: right;">
									<div class="box_1of1">
										<c:out value="${lineDtoList.lineNo}" />
									</div>
								</td>

								<td style="display: none;">
									<html:hidden name="lineDtoList" property="lineNo" indexed="true" styleId="lineDtoList[${status.index}].lineNo" />
									<html:hidden name="lineDtoList" property="status" indexed="true" styleId="lineDtoList[${status.index}].status" />
									<html:hidden name="lineDtoList" property="supplierLineId" indexed="true" styleId="lineDtoList[${status.index}].supplierLineId" />
									<html:hidden name="lineDtoList" property="productAbstract" indexed="true" styleId="lineDtoList[${status.index}].productAbstract" />
									<html:hidden name="lineDtoList" property="taxCategory" indexed="true" styleId="lineDtoList[${status.index}].taxCategory" />
									<html:hidden name="lineDtoList" property="rackName" indexed="true" styleId="lineDtoList[${status.index}].rackName" />
									<html:hidden name="lineDtoList" property="rate"  indexed="true" styleId="lineDtoList[${status.index}].rate"  styleClass="AutoCalcRate" />
									<html:hidden name="lineDtoList" property="ctaxRate"  indexed="true" styleId="lineDtoList[${status.index}].ctaxRate" styleClass="AutoCalcCtaxRate"/>
									<html:hidden name="lineDtoList" property="ctaxPrice"  indexed="true" styleId="lineDtoList[${status.index}].ctaxPrice" styleClass="AutoCalcCtaxPrice"/>
									<html:hidden name="lineDtoList" property="poLineId" indexed="true" styleId="lineDtoList[${status.index}].poLineId" />
									<html:hidden name="lineDtoList" property="oldQuantity" indexed="true" styleId="lineDtoList[${status.index}].oldQuantity" />
									<html:hidden name="lineDtoList" property="restQuantity" indexed="true" styleId="lineDtoList[${status.index}].restQuantity" />
									<html:hidden name="lineDtoList" property="totalQuantity" indexed="true" styleId="lineDtoList[${status.index}].totalQuantity" />
									<html:hidden name="lineDtoList" property="supplierDetailCategory" indexed="true" styleId="lineDtoList[${status.index}].supplierDetailCategory" value="01"/>
									<br>
								</td>

								<!-- 完納区分/納期 -->
								<td>
									<div>
										<html:select name="lineDtoList" property="deliveryProcessCategory" indexed="true" styleId="lineDtoList[${status.index}].deliveryProcessCategory" styleClass="AutoCalcCategory c_referable" style="width: 85px; ime-mode:disabled;" tabindex="<%=String.valueOf(lineTab++) %>" >
											<html:options collection="deliveryProcessCategoryList" property="value" labelProperty="label"/>
										</html:select>
									</div>
									<div style="margin-top:10px;">
										<html:text name="lineDtoList" property="deliveryDate" indexed="true" styleId="lineDtoList[${status.index}].deliveryDate" styleClass="c_disable" readonly="true" style="width: 85px;" tabindex="<%=String.valueOf(lineTab++) %>" />
									</div>
								</td>

								<!-- 商品コード※/相手品番/商品名・摘要 -->
								<td colspan="2">
									<div class="box_1of3" style="height: 50px;">
										<div style="float: left; background-color: #fae4eb;">
											&nbsp;
											<html:text name="lineDtoList" property="productCode" indexed="true" styleId="lineDtoList[${status.index}].productCode"
												styleClass="${ copySlip ? 'c_disable' : ''} goods_code c_referable" style="width: 140px; height: 30px; ime-mode:disabled; margin:10px 0;"  tabindex="<%=String.valueOf(lineTab++) %>" readonly="${copySlip}"/>

											<c:if test="${!copySlip}">
												<html:image src="${f:url('/images/customize/btn_search.png')}" styleId="productCodeImg${status.index}"
													style="width: 25px; height: 25px; vertical-align: middle; cursor: pointer;"  tabindex="<%=String.valueOf(lineTab++) %>" />
											</c:if>
											&nbsp;
										</div>
										<div style="float:right">
											<html:text name="lineDtoList" property="supplierPcode" indexed="true" styleId="lineDtoList[${status.index}].supplierPcode" styleClass="c_disable"  readonly="true" style="width: 155px; height: 30px; margin: 10px 0;"  tabindex="<%=String.valueOf(lineTab++) %>" />&nbsp;
										</div>
									</div>
									<div class="box_2of3" id="productAbstract${status.index}" style="position:static; white-space: normal; height: 50px; vertical-align: middle; text-align: center;" >
										<c:out value="${lineDtoList.productAbstract}" />
									</div>

									<div class="box_3of3" style="height: 50px; vertical-align: middle; text-align: center;">
									<html:textarea name="lineDtoList" indexed="true" styleId="lineDtoList[${status.index}].productRemarks" property="productRemarks"
										style="width: 95%; height: 40px; margin: 5px;"  tabindex="<%=String.valueOf(lineTab++) %>" readonly="true" styleClass="c_disable"/>
									</div>
								</td>

								<!-- 数量※/円単価※/外貨単価 -->
								<td colspan="2" >
									<div class="box_1of3" style="height: 50px;">
										<div style="float:left; background-color: #fae4eb;">
											&nbsp;
											<c:if test="${isEntrustPorder}">
												<html:text name="lineDtoList" property="quantity" indexed="true" styleId="lineDtoList[${status.index}].quantity" styleClass="AutoCalcQuantity c_disable numeral_commas"
													style="width: 75px; height: 30px; margin: 10px 0; vertical-align: middle;" tabindex="<%=String.valueOf(lineTab++) %>"  maxlength="6" readonly="true"  />
											</c:if>
											<c:if test="${!isEntrustPorder}">
												<html:text name="lineDtoList" property="quantity" indexed="true" styleId="lineDtoList[${status.index}].quantity" styleClass="AutoCalcQuantity numeral_commas"
													style="width: 75px; height: 30px; margin: 10px 0; vertical-align: middle;" tabindex="<%=String.valueOf(lineTab++) %>"  maxlength="6"  />
											</c:if>
											<button type="button" id="stockBtn${status.index}" tabindex="<%=String.valueOf(lineTab++) %>" class="btn_list_action" style="width:30px; height: 25px; margin:0; vertical-align: middle;">在庫</button>
											&nbsp;
										</div>
										<div style="float:right;">
											<input type="text" name="unPaidQuantity" value="" style="text-align: right; border-style: none; border-color: #FFFFFF; width: 110px; height: 30px; margin: 10px 0;" styleClass="numeral_commas"  tabindex="-1" readonly="true"><%-- 未納数 --%>&nbsp;
										</div>
									</div>
									<div class="box_2of3" style="height: 50px; background-color: #fae4eb;">
										<html:text name="lineDtoList" property="unitPrice" indexed="true" styleId="lineDtoList[${status.index}].unitPrice" styleClass="AutoCalcUnitPrice numeral_commas yen_value" style="width:45%; height: 30px; margin:10px 0 0 10px;" tabindex="<%=String.valueOf(lineTab++) %>" maxlength="9" />
										<html:text name="lineDtoList" property="price" indexed="true" styleId="lineDtoList[${status.index}].price" styleClass="AutoCalcPrice numeral_commas yen_value" style="width:43%; height: 30px; margin:10px 10px 0;" tabindex="<%=String.valueOf(lineTab++) %>"  maxlength="9" />
									</div>
									<div class="box_3of3" style="height: 50px;">
										<html:text name="lineDtoList" property="dolUnitPrice" indexed="true" styleId="lineDtoList[${status.index}].dolUnitPrice" styleClass="AutoCalcDolUnitPrice numeral_commas dollar_value" style="width:45%; height: 30px; margin:10px 0 0 10px; height:30px;" tabindex="<%=String.valueOf(lineTab++) %>"  maxlength="9" />
										<html:text name="lineDtoList" property="dolPrice" indexed="true" styleId="lineDtoList[${status.index}].dolPrice" styleClass="AutoCalcDolPrice numeral_commas dollar_value" style="width:43%; margin:10px 10px 0; height:30px;" tabindex="<%=String.valueOf(lineTab++) %>"  maxlength="9" />
									</div>
								</td>

								<!-- 備考/倉庫名/棚番※ -->
								<td>
									<div class="box_1of3" style="height: 50px; vertical-align: middle; text-align: center;">
										<html:textarea name="lineDtoList" property="remarks" indexed="true" styleId="lineDtoList[${status.index}].remarks" style="width: 95%; height: 40px; margin: 5px; ime-mode:active;" tabindex="<%=String.valueOf(lineTab++) %>" />
									</div>
									<div class="box_2of3" style="height: 50px; vertical-align: middle; text-align: center;">
										<html:text name="lineDtoList" property="warehouseName" indexed="true" styleId="lineDtoList[${status.index}].warehouseName" style="width: 95%; height: 40px; margin: 5px; ime-mode: disabled;"  readonly="true" styleClass="c_disable" tabindex="<%=String.valueOf(lineTab++) %>"  />
									</div>
									<div class="box_3of3" style="height: 50px; background-color: #fae4eb;">
										<html:text name="lineDtoList" property="rackCode" indexed="true" styleId="lineDtoList[${status.index}].rackCode" style="width: 80%; height: 30px; ime-mode: disabled; vertical-align: middle; margin:10px 0;"  tabindex="<%=String.valueOf(lineTab++) %>"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ changeRackCode(this); }" />
										<html:image src="${f:url('/images/customize/btn_search.png')}" styleId="rackCodeImg${status.index}" style="margin: 0; padding: 0; width: 26px; height: 26px; vertical-align: middle; cursor: pointer;" tabindex="<%=String.valueOf(lineTab++) %>" />
									</div>
								</td>

								<!-- 削除ボタン -->
								<td>
<c:if test="${menuUpdate}">
									<button id="deleteBtn${status.index}" class="btn_list_action" type="button" alt="この行を削除" style="width:80px;" tabindex="<%=String.valueOf(lineTab++) %>" >削除</button>
</c:if>
<c:if test="${!menuUpdate}">
									<button disabled="disabled" class="btn_list_action" id="deleteBtn${status.index}" type="button" alt="この行を削除" style="width:80px;" tabindex="<%=String.valueOf(lineTab++) %>" >削除</button>
</c:if>
								</td>
							</tr>
						  </c:if>
						</c:forEach>
					</tbody>
				</table>
				</div>

			<div id="poSlipPriseInfos" class="information" style="margin-top:10px;">
        	<div id="information" class="information" style="">

				<table id="voucher_info" class="forms" summary="伝票情報" style="">
					<colgroup>
						<col span="4" style="width: 25%">
					</colgroup>
					<tr>
						<th class="rd_top_left" style="height:60px;">本体金額合計(円)</th>
						<th>消費税合計</th>
						<th>伝票合計(円)</th>
						<th class="rd_top_right" style="height:60px;">外貨伝票合計</th>
					</tr>
   					<tr>
						<td class="rd_bottom_left" style="text-align: center;height: 100px;">
							<html:text styleId="nonTaxPriceTotal" property="nonTaxPriceTotal" styleClass="Anumeral_commas yen_value"  style="width: 100%; text-align:center; background-color: #FFFFFF; border-style: none;font-weight: bold;color: #555555;font-size: 24px;" readonly="true" />
						</td>
						<td style="text-align: center">
							<html:text styleId="ctaxTotal" property="ctaxTotal" styleClass="Anumeral_commas yen_value" style="width: 100%; text-align:center; background-color: #FFFFFF; border-style: none;font-weight: bold;color: #555555;font-size: 24px;" readonly="true" />
						</td>
						<td style="text-align: center">
							<html:text property="priceTotal" styleId="priceTotal" styleClass="Anumeral_commas yen_value" style="width: 100%; text-align:center; background-color: #FFFFFF; border-style: none;font-weight: bold;color: #555555;font-size: 24px;" readonly="true"/>
						</td>
						<td class="rd_bottom_right" style="text-align: center">
							<html:text property="fePriceTotal" styleId="fePriceTotal" styleClass="Anumeral_commas dollar_value" style="width: 100%; text-align:center; background-color: #FFFFFF; border-style: none;font-weight: bold;color: #555555;font-size: 24px;" readonly="true"/>
						</td>
					</tr>
				</table>
				</div>
				</div>

			</div>
				<div style="text-align: center; width: 1170px">
					<c:if test="${f:h(newData)}">
						<button type="button" id="btnF3btm" style="width:260px;height:51px;" class="btn_medium" tabindex="1999" ${!copySlip? "disabled='disabled'":"onclick='onF3()'"}>
							<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.register'/></span><%// 登録 %>
						</button>
					</c:if>
					<c:if test="${!f:h(newData)}">
						<button type="button" id="btnF3btm" style="width:260px;height:51px;" class="btn_medium" tabindex="1999" ${notRegisterable? "disabled='disabled'":"onclick='onF3()'"}>
							<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.renew'/></span><%// 更新 %>
						</button>
					</c:if>
				</div>
			</div>


			<%-- 単価小数桁数と数量小数桁数 --%>
			<input type="hidden" id="unitPriceDecAlignment" value="${mineDto.unitPriceDecAlignment}">
			<input type="hidden" id="numDecAlignment" value="${mineDto.numDecAlignment}">
			<html:hidden property="newData" />
		</s:form>
	</div>

</body>

</html>
