<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="jp.co.arkinfosys.form.AbstractSlipEditForm"%>
<%@page import="jp.co.arkinfosys.entity.DepositSlip"%>
<%@page import="jp.co.arkinfosys.common.Constants"%>
<%@page import="jp.co.arkinfosys.common.CategoryTrns"%>
<%@page import="jp.co.arkinfosys.common.Constants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<title><bean:message key='titles.system'/> 入金入力</title>
	<script type="text/javascript" src="${f:url('/scripts/dialogs.js')}"></script>


	<meta http-equiv="Content-Style-Type" content="text/css">

	<script type="text/javascript">
	<!--
	var MAX_LINE_ROW_COUNT = <%= AbstractSlipEditForm.MAX_LINE_SIZE %>;
	var MAIN_FORM_NAME = "deposit_inputDepositActionForm";
	var maxIndex = 0;
	// 明細行の確保領域
	var trCloneBase = null;
	// コンボの確保領域
	var catArray = {};
	// 数量の丸め（桁=自社マスタ.数量少数桁、丸め=自社マスタ.商品端数処理）
	var quantityAlignment = ${mineDto.numDecAlignment};
	var quantityCategory = ${mineDto.productFractCategory};

	// 単価・金額の丸め（桁=0固定[円の場合]、丸め=得意先マスタ.単価端数処理　[得意先未指定]自社マスタ.単価端数処理）
	var priceAlignment = scale_0;
	// 伝票の単価端数処理 HIDDEN

	// 消費税の丸め（桁=0固定[円の場合]、丸め=得意先マスタ.税端数処理　[得意先未指定]自社マスタ.税端数処理）
	var taxAlignment = scale_0;
	// 伝票の税端数処理 HIDDEN

	// 粗利益率の丸め（桁=自社マスタ.統計少数桁、丸め=四捨五入固定）
	var rateAlignment = ${mineDto.statsDecAlignment};
	var rateCategory = scale_half_up;

	//ページ読込時の動作
	function init() {
		// 明細行のIndex管理
		var tBodyLine = $("#tbodyLine");
		maxIndex = tBodyLine.children().size() - 2;

		// 敬称の確保領域
		catArray["#baPcPreCatrgory"] = $("#baPcPreCatrgory").clone(true);
		catArray["#salesCmCategoryName"] = $("#salesCmCategoryName").clone(true);
		catArray["#cutoffGroupCategory"] = $("#cutoffGroupCategory").clone(true);

		// 明細行の項目にイベントをバインド
		for(var i=0; i<=maxIndex; i++) {
			$("#salesLineList\\[" + i + "\\]\\.price").bind("change", {index: i}, ChangePrice);
			$("#deleteBtn" + i).bind("click", {index: i}, deleteRow);
			$("#copyBtn" + i).bind("click", {index: i}, copyRow);
		}

		$("#priceFractCategory").val(${mineDto.priceFractCategory});

		// 端数処理初期設定
		applyNumeralStyles();

		// 明細行のクローンを生成
		trCloneBase = tBodyLine.children(":first").clone(true);

		// コンボの調整
		if( $("#customerName").val() != "" ){
			catrgoryListCtrl("#baPcPreCatrgory",$("#baPcPreCatrgory").val());
			catrgoryListCtrl("#salesCmCategoryName",$("#salesCmCategory").val());
			catrgoryListCtrl("#cutoffGroupCategory",$("#cutoffGroupCategory").val());
		}

		if( $("#depositSlipId").val() == "" ){
			$("#btnF2").attr("disabled",true );
		}else{
			if( $("#status").val() == "<%=Constants.STATUS_DEPOSIT_SLIP.CUTOFF%>" ){
				$("#btnF2").attr("disabled",true );
			}
		}
		lineSize = $("#tbodyLine").get(0).children.length-1;
		if( lineSize == 1 ){
			trId = $("#tbodyLine").get(0).children[0].id;
			id = trId.replace("trLine", "");
			$("#deleteBtn"+id).get(0).disabled = true;
		}

		// 入金区分の初期値は、振込
		if( $("#customerCode").val() == "" ){
			$("#depositCategory").val("<%= CategoryTrns.DEPOSIT_CATEGORY_TRANSFER %>");
		}

		// 初期フォーカス
		if(( $("#depositSlipId").val() != "" )
				// 初期状態で顧客コードが入っている場合は、入力初期情報が設定されているので、新規登録固定とする
			||( $("#customerCode").val() != "" )){
			$("#depositSlipId").attr("readOnly", "true");
			$("#depositSlipId").addClass("c_disable");
			$("#depositDate").focus();
		}else{
			$("#depositSlipId").focus();
		}

		$("#customerCode").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁
	}


	// 初期化
	function onF1(){
		if(confirm('<bean:message key="confirm.init" />')){
			showNowSearchingDiv();
			window.location.doHref('${f:url("/deposit/inputDeposit")}');
			$("#depositSlipId").focus();
		}
	}
	function findSlip(){
		if( $("#depositSlipId").val() != "" ){
			showNowSearchingDiv();
			$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/deposit/inputDeposit/load")}');
			$("form[name='" + MAIN_FORM_NAME + "']").submit();
		}
	}

	// 削除
	function onF2(){
		if(confirm('<bean:message key="confirm.delete" />')){
			$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/deposit/inputDeposit/delete")}');
			showNowSearchingDiv();
			$("form[name='" + MAIN_FORM_NAME + "']").submit();

		}
	}

	// 登録
	function onF3(){
		if(confirm('<bean:message key="confirm.insert" />')){
			_before_submit($(".numeral_commas"));
			$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/deposit/inputDeposit/upsert")}');
			showNowSearchingDiv();
			$("form[name='" + MAIN_FORM_NAME + "']").submit();

		}
	}

	// 行削除
	function deleteRow(event){
		var i, lineNo, lineSize;
		var trId, id;
		var index;
		index = event.data.index;

		if(confirm('<bean:message key="confirm.line.delete" />')){
			// 行を削除する
			lineNo = parseInt($("#depLineList\\["+index+"\\]\\.lineNo").get(0).value)-1;
	        if ($("#depLineList\\[" + index + "\\]\\.depositLineId").val() != "") {
	            $("#deleteLineIds").val($("#deleteLineIds").val() + "," + $("#depLineList\\[" + index + "\\]\\.depositLineId").val());
	        }
			$("#tbodyLine").get(0).deleteRow(lineNo);
			// 行番号を調整する
			lineSize = $("#tbodyLine").get(0).children.length-1;
			for(i=lineNo; i<lineSize; i++) {
				trId = $("#tbodyLine").get(0).children[i].id;
				id = trId.replace("trLine", "");
				// 行番号を振りなおす
				$("#tdNo"+id).get(0).innerHTML = i+1;
				$("#depLineList\\["+id+"\\]\\.lineNo").get(0).value = i+1;
				// 先頭行の場合、前行複写ボタンの不活性化
				if(i == 0) {
					$("#copyBtn"+id).get(0).disabled = true;
				}
			}
			// 最終行のみの時には削除不可
			if( lineSize == 1 ){
				trId = $("#tbodyLine").get(0).children[0].id;
				id = trId.replace("trLine", "");
				$("#deleteBtn"+id).get(0).disabled = true;
			}
		}
		ChangePrice();
	}

	// 前行複写
	function copyRow(event){
		var prevIndex;
		var i;
		var index;

		index = event.data.index;

		// 空行判定
		if(!isEmptyLine(index)) {
			// 上書き確認
			if(!confirm('<bean:message key="confirm.line.copy" />')){
				return;
			}
		}
		// 前行を探す（削除するとIndexが飛び番になる為）
		prevIndex = getPrevIndex(index);
		// コピー
		copyLine(index, prevIndex);
		ChangePrice();
	}

	// 行追加
	function addRow(){
		var elemTR, elemTD;
		var lineSize, tabIdx;
		var html;
		var cellIndex = 0;

		// 最大行数の確認
		lineSize = $("#tbodyLine").get(0).children.length-1;
		if(lineSize >= MAX_LINE_ROW_COUNT) {
			alert('<bean:message key="errors.line.maxrows" />');
			return;
		}

		// ベースオブジェクトからクローンを生成
		var trClone = trCloneBase.clone(true);

		// 明細行のIndex管理
		var endLine = $("#tbodyLine").children("[id^='trLine']:last");
		var match = endLine.attr("id").match(/^trLine([0-9]+)$/);
		if( match ) {
			maxIndex = match[1];
		}
		maxIndex++;

		// タブインデックスの計算
		tabIdx = 1000 + (lineSize+1) * ${f:h(lineElementCount)} - 1;

		// 初期化
		$(trClone).attr("id","trLine"+maxIndex);

		var myTd = $(trClone).get(0).children[0];
		$(myTd).attr("id","tdNo"+maxIndex);
		$(myTd).get(0).innerHTML = lineSize+1;

		var tmpTd = $(trClone).get(0).children[1];
		var myLineNo = $(tmpTd).get(0).children[0];
		$(myLineNo).attr("id","depLineList["+maxIndex+"].lineNo");
		$(myLineNo).attr("name","depLineList["+maxIndex+"].lineNo");
		$(myLineNo).get(0).value = lineSize+1;

		var myLineId = $(tmpTd).get(0).children[1];
		$(myLineId).attr("id","depLineList["+maxIndex+"].depositLineId");
		$(myLineId).attr("name","depLineList["+maxIndex+"].depositLineId");
		$(myLineId).get(0).value = "";

		var myStatus = $(tmpTd).get(0).children[2];
		$(myStatus).attr("id","depLineList["+maxIndex+"].status");
		$(myStatus).attr("name","depLineList["+maxIndex+"].status");
		$(myStatus).get(0).value = "0";

		var mySlipId = $(tmpTd).get(0).children[3];
		$(mySlipId).attr("id","depLineList["+maxIndex+"].depositSlipId");
		$(mySlipId).attr("name","depLineList["+maxIndex+"].depositSlipId");
		$(mySlipId).get(0).value = "";

		var myInstDate = $(tmpTd).get(0).children[4];
		$(myInstDate).attr("id","depLineList["+maxIndex+"].instDate");
		$(myInstDate).attr("name","depLineList["+maxIndex+"].instDate");
		$(myInstDate).get(0).value = "";

		var myInstNo = $(tmpTd).get(0).children[5];
		$(myInstNo).attr("id","depLineList["+maxIndex+"].instNo");
		$(myInstNo).attr("name","depLineList["+maxIndex+"].instNo");
		$(myInstNo).get(0).value = "";

		var mySalesLineId = $(tmpTd).get(0).children[6];
		$(mySalesLineId).attr("id","depLineList["+maxIndex+"].salesLineId");
		$(mySalesLineId).attr("name","depLineList["+maxIndex+"].salesLineId");
		$(mySalesLineId).get(0).value = "";

		tmpTd = $(trClone).get(0).children[2];
		var myPrice = $(tmpTd).children().get(0).children[0];
		$(myPrice).attr("id","depLineList["+maxIndex+"].price");
		$(myPrice).attr("name","depLineList["+maxIndex+"].price");
		$(myPrice).attr("value","");
		$(myPrice).attr("tabindex",401+maxIndex*5);
		$(myPrice).bind("change", {index: maxIndex}, ChangePrice);

		tmpTd = $(trClone).get(0).children[3];
		var myBank = $(tmpTd).children().get(0).children[0];
		$(myBank).attr("id","depLineList["+maxIndex+"].bankId");
		$(myBank).attr("name","depLineList["+maxIndex+"].bankId");
		$(myBank).attr("value","");
		$(myBank).attr("tabindex",402+maxIndex*5);

		tmpTd = $(trClone).get(0).children[4];
		var myRemarks = $(tmpTd).children().get(0).children[0];
		$(myRemarks).attr("id","depLineList["+maxIndex+"].remarks");
		$(myRemarks).attr("name","depLineList["+maxIndex+"].remarks");
		$(myRemarks).attr("value","");
		$(myRemarks).attr("tabindex",403+maxIndex*5);

		// 削除関数変更
		tmpTd = $(trClone).get(0).children[5];
		var myDel = $(tmpTd).children().get(0).children[0];
		$(myDel).attr("id","deleteBtn" + maxIndex);
		$(myDel).attr("tabindex",404+maxIndex*5);
		$(myDel).bind("click", {index: maxIndex}, deleteRow);

		var myCopy = $(tmpTd).children().get(1).children[0];
		$(myCopy).attr("id","copyBtn"+maxIndex);
		$(myCopy).attr("tabindex",405+maxIndex*5);
		$(myCopy).bind("click", {index: maxIndex}, copyRow);

		if(lineSize == 0) {
			$(myCopy).attr("disabled", true);
		}else{
			$(myCopy).attr("disabled", false);
		}

		// 挿入位置決定
		var trAddLine = $('#addLineTr');
		$(trAddLine).before(trClone);

		// 先頭行の削除不可を可能に
		if( lineSize != 0 ){
			trId = $("#tbodyLine").get(0).children[0].id;
			id = trId.replace("trLine", "");
			$("#deleteBtn"+id).get(0).disabled = false;
		}
	}

	// 明細行の空行判定
	function isEmptyLine(lineNo){
		var retVal = true;
		var elem;

		// 金額
		elem = $("#depLineList\\["+lineNo+"\\]\\.price").get(0);
		if(elem.value != "") {
			retVal = false;
		}

		// 銀行ID
		elem = $("#depLineList\\["+lineNo+"\\]\\.bankId").get(0);
		if(elem.value != "") {
			retVal = false;
		}

		// 備考
		elem = $("#depLineList\\["+lineNo+"\\]\\.remarks").get(0);
		if(elem.value != "") {
			retVal = false;
		}

		return retVal;
	}

	// セルのコピー
	function copyCell(destIndex, srcIndex, name){
		var destElem, srcElem;
		destElem = $("#depLineList\\["+destIndex+"\\]\\."+name).get(0);
		srcElem = $("#depLineList\\["+srcIndex+"\\]\\."+name).get(0);
		destElem.value = srcElem.value;
	}
	// 明細行のコピー
	function copyLine(destIndex, srcIndex){
		// 金額
		copyCell(destIndex, srcIndex,"price");
		// 銀行
		copyCell(destIndex, srcIndex, "bankId");
		// 備考
		copyCell(destIndex, srcIndex,"remarks");
	}

	// 指定行から前行のIndexを返す
	// （見つからない場合は-1を返す）
	function getPrevIndex(index){
		var retVal = -1;
		var i, lineNo, trId;

		// 指定行の行番号を取得
		lineNo = parseInt($("#depLineList\\["+index+"\\]\\.lineNo").get(0).value);
		// 前行を探す（削除行は非表示になっている為、直前の削除されていない行を探す）
		if(lineNo>1) {
			trId = $("#tbodyLine").get(0).children[lineNo-2].id;
			retVal = trId.replace("trLine", "");
		}

		return retVal;
	}

	//仕入先検索ダイアログ呼び出し
	function customerSearch(){
		openSearchCustomerDialog('customer', customerCallBack);
		// 顧客コードを設定する
		$("#customer_customerCode").val($("#customerCode").val());
		$("#customer_customerCode").focus();
	}

	//顧客先情報フォームIDリスト
	var CustomerInfosIDList = new Array(
			"customerIsExist",
			"customerName",
			"salesCmCategory",
			"salesCmCategoryName",
			"cutoffGroup",
			"paybackCycleCategory",
			"cutoffGroupCategory",
			"customerRemarks",
			"customerCommentData"
	);
	//顧客情報の初期化
	function InitCustomerInfos(){
		for(var i in CustomerInfosIDList){
			$("#"+ CustomerInfosIDList[i]).attr("value","");
		}
		$("#priceFractCategory").val(${mineDto.priceFractCategory});


	}
	// ================================================================================================
	// 区分系共通
	// ================================================================================================
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

	function customerCallBack(id, map) {
		$("#customerIsExist").attr("value","1");
		$("#customerCode").attr("value",map["customerCode"]);
		$("#customerName").attr("value",map["customerName"]);
		$("#salesCmCategory").attr("value",map["salesCmCategory"]);
		$("#taxFractCategory").attr("value",map["taxFractCategory"]);
		$("#priceFractCategory").attr("value",map["priceFractCategory"]);
		$("#cutoffGroup").attr("value",map["cutoffGroup"]);
		$("#paybackCycleCategory").attr("value",map["paybackCycleCategory"]);
		$("#cutoffGroupCategory").attr("value",map["cutoffGroup"] + map["paybackCycleCategory"]);
		$("#customerRemarks").attr("value",map["remarks"]);
		$("#customerCommentData").attr("value",map["commentData"]);

		GetBillingInfos();
	}

	//顧客情報の取得
	function GetCustomerInfos(){
		if(jQuery.trim($("#customerCode").attr("value"))==""){
			InitCustomerInfos();
			InitDeliveryInfos();
			return;
		}
		var data = new Object();
		data["customerCode"] = $("#customerCode").attr("value");
		asyncRequest(
				contextRoot + "/ajax/commonDeposit/getDeliveryInfosByCustomerCode",
				data,
				function(data) {
					if(data==""){
						InitCustomerInfos();
						InitDeliveryInfos();
						alert('<bean:message key="errors.customer.not.exist.code" />');
					}else{
						$("#customerIsExist").attr("value","1");
						var value = eval("(" + data + ")");
						setDeliveryInfos( value );
					}
				});
	}
	//納入先情報フォームIDリスト
	var DeliveryInfosIDList = new Array(
			"baName",
			"baOfficeName",
			"baDeptName",
			"baZipCode",
			"baAddress1",
			"baAddress2",
			"baPcName",
			"baPcKana",
			"baTel",
			"baEmail",
			"baFax"
	);

	//納入先情報の初期化
	function InitDeliveryInfos(){
		for(var i in DeliveryInfosIDList){
			$("#"+ DeliveryInfosIDList[i]).attr("value","");
		}

		$("#nowPaybackPriceInput").html("");
		$("#nowPaybackPriceBack").attr("value","");
		catrgoryListInit("#baPcPreCatrgory");
		catrgoryListInit("#salesCmCategoryName");
		catrgoryListInit("#cutoffGroupCategory");

		$("#lastBillingPrice").html( "" );
		$("#lastBillingPriceBack").attr("value","");

		$("#nowSalesPrice").html( "" );
		$("#nowSalesPrice").attr("value","");

		// 端数処理初期設定
		applyNumeralStyles();

	}
	//納入先情報の取得
	function GetBillingInfos(){
		var data = new Object();
		data["customerCode"] = $("#customerCode").attr("value");
		asyncRequest(
				contextRoot + "/ajax/commonDeposit/getDeliveryInfosByCustomerCode",
				data,
				function(data) {
					if(data==""){
						InitDeliveryInfos();
					}else{
						var value = eval("(" + data + ")");
						setDeliveryInfos( value );
					}
				});
	}
	// 納入先情報の設定
	function setDeliveryInfos( value ){
		$("#baCode"		).attr("value",value.deliveryCode);
		$("#baName"		).attr("value",value.deliveryName);
		$("#baOfficeName").attr("value",value.deliveryOfficeName);
		$("#baDeptName"	).attr("value",value.deliveryDeptName);
		$("#baZipCode"	).attr("value",value.deliveryZipCode);
		$("#baAddress1"	).attr("value",value.deliveryAddress1);
		$("#baAddress2"	).attr("value",value.deliveryAddress2);
		$("#baPcName"	).attr("value",value.deliveryPcName);
		$("#baPcKana"	).attr("value",value.deliveryPcKana);
		$("#baTel"		).attr("value",value.deliveryTel);
		$("#baEmail"	).attr("value",value.deliveryEmail);
		$("#baFax"		).attr("value",value.deliveryFax);
		$("#customerName").attr("value",value.customerName);
		$("#salesCmCategory").attr("value",value.salesCmCategory);
		$("#salesCmCategoryName").attr("value",value.salesCmCategoryName);
		$("#cutoffGroup").attr("value",value.cutoffGroup);
		$("#paybackCycleCategory").attr("value",value.paybackCycleCategory);
		$("#cutoffGroupCategory").attr("value",value.cutoffGroup + value.paybackCycleCategory);
		$("#taxFractCategory").attr("value",value.taxFractCategory);
		$("#priceFractCategory").attr("value",value.priceFractCategory);
		$("#nowPaybackPriceBack").attr("value",value.nowPaybackPrice);
		$("#customerRemarks").attr("value",value.customerRemarks);
		$("#customerCommentData").attr("value",value.customerCommentData);

		catrgoryListCtrl("#baPcPreCatrgory",value.deliveryPcPreCategory);
		catrgoryListCtrl("#salesCmCategoryName",value.salesCmCategory);
		catrgoryListCtrl("#cutoffGroupCategory",value.cutoffGroup + value.paybackCycleCategory);

		$("#lastBillingPrice").html( value.lastBillingPrice );
		$("#lastBillingPriceBack").attr("value",value.lastBillingPrice);

		$("#nowPaybackPriceInput").html( value.nowPaybackPrice );

		$("#nowSalesPrice").html( value.nowSalesPrice );
		$("#nowSalesPriceBack").attr("value",value.nowSalesPrice);

		// 端数処理初期設定
		applyNumeralStyles();

		// 入金区分の設定
		SetDepositCategory();
	}

	//顧客コード変更
	function ChangeCustomerCode(){
		//仕入先情報取得
		GetCustomerInfos();

	}

	//顧客の取引区分により入金区分の初期値を設定
	function SetDepositCategory(){

		//「掛売」＝「振込み」、
		if($("#salesCmCategory").val() == "<%= CategoryTrns.SALES_CM_CREDIT %>")
			$("#depositCategory").val("<%= CategoryTrns.DEPOSIT_CATEGORY_TRANSFER %>");

		//「代引」＝「代引」、
		else if($("#salesCmCategory").val() == "<%= CategoryTrns.SALES_CM_CASH_ON_DELIVERY %>")
			$("#depositCategory").val("<%= CategoryTrns.DEPOSIT_CATEGORY_CASH_ON_DELIVERY %>");

		//「クレジット」＝「クレジット」、
		else if($("#salesCmCategory").val() == "<%= CategoryTrns.SALES_CM_CREDIT_CARD %>")
			$("#depositCategory").val("<%= CategoryTrns.DEPOSIT_CATEGORY_CREDIT_CARD %>");

		//「先入金」＝「先入金」
		else if($("#salesCmCategory").val() == "<%= CategoryTrns.SALES_CM_PAY_FIRST %>")
			$("#depositCategory").val("<%= CategoryTrns.DEPOSIT_CATEGORY_PAY_FIRST %>");

		else
			$("#depositCategory").val("<%= CategoryTrns.DEPOSIT_CATEGORY_TRANSFER %>");

		return;

	}

	// 金額計算
	function ChangePrice(){
		var calcPrice = Number($("#nowPaybackPriceBack").get(0).value);
		lineSize = $("#tbodyLine").get(0).children.length-1;
		for(i=0; i<lineSize; i++) {
			trId = $("#tbodyLine").get(0).children[i].id;
			id = trId.replace("trLine", "");
			calcPrice += _Number($("#depLineList\\["+id+"\\]\\.price").get(0).value);
		}

		// 今回回収額
		$("#nowPaybackPriceInput").html( calcPrice );
		SetBigDecimalScale_Obj($("#nowPaybackPriceInput"));

		// 請求残
		var lastBillingPriceNum = _Number($("#lastBillingPrice").text());
		var nowPaybackPriceInputNum = _Number($("#nowPaybackPriceInput").text());
		var nowSalesPriceNum = _Number($("#nowSalesPrice").text());
		var billingBalancePriceNum = lastBillingPriceNum - nowPaybackPriceInputNum + nowSalesPriceNum;

		$("#billingBalancePrice").html( billingBalancePriceNum );
		SetBigDecimalScale_Obj($("#billingBalancePrice"));

		// カンマをつける
		_after_load($(".numeral_commas"));

	}

	// 税端数処理
	function applyNumeralStyles(){

		var bp = $("#lastBillingPrice").text();
		var sp = $("#nowSalesPrice").text();

		if (bp == "") {
			$("#lastBillingPrice").html( "0" );
		}else{
			$("#lastBillingPrice").html( _Number($("#lastBillingPrice").text()) );
		}

		if (sp == "") {
			$("#nowSalesPrice").html( "0" );
		}else{
			$("#nowSalesPrice").html( _Number($("#nowSalesPrice").text()) );
		}

		SetBigDecimalScale_Obj($("#lastBillingPrice"));
		SetBigDecimalScale_Obj($("#nowSalesPrice"));

		// 明細行のIndex管理
		var maxIndex = $("#tbodyLine").get(0).children.length-1;

		for(var i=0; i<=maxIndex; i++) {
			// 丸めの少数桁と端数処理を設定する
			applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#depLineList\\[" + i + "\\]\\.price"));
		}

		// 前回請求額 lastBillingPrice
		applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#lastBillingPrice"));
		// 今回回収額 nowPaybackPriceInput
		applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#nowPaybackPriceInput"));
		// 請求残高 nowSalesPrice
		applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#nowSalesPrice"));
		// 請求残 billingBalancePrice
		applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#billingBalancePrice"));

		ChangePrice();

		// カンマをつける
		_after_load($(".numeral_commas"));

	}

	// -------------
	// 伝票複写
	// -------------
	function copySlipCallback(dialogId, slipName, slipId ) {
		$("#copySlipName").val( slipName );
		$("#copySlipId").val( slipId );
		$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/deposit/inputDeposit/copySlip/")}');
		$("form[name='" + MAIN_FORM_NAME + "']").submit();
	}

function copyDummy(){
	copySlipCallback("", "DEPOSIT", "76" );
}



	-->
	</script>
</head>
<body onload="init()" onhelp="return false;">


<!-- ヘッダ -->
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>
<!-- メニュー -->
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0006"/>
		<jsp:param name="MENU_ID" value="0600"/>
	</jsp:include>

<!-- メイン機能 -->
	<div id="main_function">

		<!-- タイトル -->
		<span class="title">入金入力</span>

		<div class="function_buttons">
	<!-- TODO EL式でファンクション制御用DTOを参照して表示制御する  -->

			<button id="btnF1" type="button" tabindex="2000" onclick="onF1();">F1<br>初期化</button>
			<c:if test="${!menuUpdate || newData}">
				<button id="btnF2" type="button" tabindex="2001" onclick="onF2(); " disabled>F2<br>削除</button>
			</c:if>
			<c:if test="${menuUpdate && !newData}">
				<c:if test="${menuUpdate && !closed}">
					<button id="btnF2" type="button" tabindex="2001" onclick="onF2(); ">F2<br>削除</button>
				</c:if>
				<c:if test="${!menuUpdate || closed}">
					<button id="btnF2" type="button" tabindex="2001" onclick="onF2(); " disabled>F2<br>削除</button>
				</c:if>
			</c:if>
			<c:if test="${newData}" >
				<c:if test="${menuUpdate && !closed}">
					<button id="btnF3" type="button" tabindex="2002" onclick="onF3(); ">F3<br>登録</button>
				</c:if>
				<c:if test="${!menuUpdate || closed}">
					<button id="btnF3" type="button" tabindex="2002" onclick="onF3(); " disabled>F3<br>登録</button>
				</c:if>
			</c:if>
			<c:if test="${!newData}" >
				<c:if test="${menuUpdate && !closed}">
					<button id="btnF3" type="button" tabindex="2002" onclick="onF3(); ">F3<br>更新</button>
				</c:if>
				<c:if test="${!menuUpdate || closed}">
					<button id="btnF3" type="button" tabindex="2002" onclick="onF3(); " disabled>F3<br>更新</button>
				</c:if>
			</c:if>
			<button id="btnF4" type="button" tabindex="" disabled>F4<br>&nbsp;</button>
			<button id="btnF5" type="button" tabindex="" disabled>F5<br>&nbsp;</button>
			<button id="btnF6" type="button" tabindex="" disabled>F6<br>&nbsp;</button>
			<button id="btnF7" type="button" tabindex="" disabled>F7<br>&nbsp;</button>
			<button id="btnF8" type="button" tabindex="" disabled>F8<br>&nbsp;</button>
			<button id="btnF9" type="button" tabindex="" disabled>F9<br>&nbsp;</button>
			<button id="btnF10" type="button" tabindex="" disabled>F10<br>&nbsp;</button>
			<button id="btnF11" type="button" tabindex="" disabled>F11<br>&nbsp;</button>
			<button id="btnF12" type="button" tabindex="" disabled>F12<br>&nbsp;</button>
		</div>
		<br><br><br>

		<s:form style="margin:0px; padding:0px;" onsubmit="return false;">

			<!--  表示・入力領域 -->
			<div class="function_forms">
				<!-- エラー情報 -->
				<div id="errors" style="color: red">
					<html:errors />
				</div>
					<span id="ajax_errors"></span>
					<div style="padding-left: 20px;color: blue;">
					<html:messages id="msg" message="true">
						<bean:write name="msg" ignore="true"/><br>
					</html:messages>
					</div>

				<div class="form_section_wrap">
				<div class="form_section">
				<div class="section_title">
					<span >入金伝票情報</span>
					<button class="btn_toggle" />
				</div>

				<div class="section_body">


				<table id="receipt_info" class="forms" summary="入金伝票情報">
					<colgroup>
						<col span="1" style="width: 15%">
						<col span="1" style="width: 18%">
						<col span="1" style="width: 15%">
						<col span="1" style="width: 18%">
						<col span="1" style="width: 15%">
						<col span="1" style="width: 18%">
					</colgroup>
					<tr>
						<th><div class="col_title_right">入金番号</div></th>
						<td>
							<c:if test="${newData}">
							<html:text tabindex="100" property="depositSlipId" styleId="depositSlipId" errorStyleClass="err" style="ime-mode:disabled;" maxlength="10"  onfocus="this.curVal=this.value;" onblur="if((this.curVal == '') || ((this.curVal != '')&&(this.curVal!=this.value))){ findSlip();}"/>
							</c:if>
							<c:if test="${!newData}">
							<html:text tabindex="100" property="depositSlipId" styleId="depositSlipId" errorStyleClass="err" readonly="true" styleClass="c_disable" style="ime-mode:disabled;" maxlength="10"/>
							</c:if>
						</td>
						<th><div class="col_title_right_req">入金日<bean:message key='labels.must'/></div></th>
						<td>
							<html:text tabindex="101" title="入金日" property="depositDate" styleId="depositDate" styleClass="date_input" style="ime-mode:disabled; width: 135px;" maxlength="10" />
						</td>
						<th><div class="col_title_right">入力日</div></th>
						<td>
							<html:text tabindex="102" title="入力日" property="inputPdate" styleId="inputPdate" styleClass="date_input" style="ime-mode:disabled; width: 135px;" maxlength="10" />
						</td>
						<th><div class="col_title_right">入力担当者</div></th>
						<td>
							<html:text tabindex="103" property="userName" styleId="userName" readonly="true" styleClass="c_disable" />
							<html:hidden property="userId" />
						</td>
					</tr>
					<tr>

<!-- TODO 入金区分表示用JSPをインクルードする -->
						<th><div class="col_title_right_req">入金区分<bean:message key='labels.must'/></div></th>
						<td>
							<html:select tabindex="105" property="depositCategory"  styleId="depositCategory" >
								<c:forEach var="dcl" items="${depositCategoryList}">
									<html:option value="${dcl.value}">${dcl.label}</html:option>
								</c:forEach>
							</html:select>
						</td>
						<th><div class="col_title_right">摘要</div></th>
						<td colspan="5">
							<html:text tabindex="106" property="depositAbstract" styleId="depositAbstract" style="width: 500px; ime-mode:active;" maxlength="50"  />
						</td>
					</tr>
				</table>
				</div>
				</div>
				</div>

				<div class="form_section_wrap">
				<div class="form_section">
				<div class="section_title">
					<span >請求先情報</span>
					<button class="btn_toggle" />
				</div>

				<div class="section_body">
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
							<html:text tabindex="200" property="customerCode" styleId="customerCode" style="width: 100px; ime-mode:disabled;"
								onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ ChangeCustomerCode(); }" />
							<html:image tabindex="201" styleId="customerCodeImg" src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" onclick="customerSearch()" />
							<html:hidden styleId="customerIsExist" property="customerIsExist"/>
							<html:hidden styleId="taxFractCategory" property="taxFractCategory"/>
							<html:hidden styleId="priceFractCategory" property="priceFractCategory"/>
						</td>
						<th><div class="col_title_right">顧客名</div></th>
						<td>
							<html:text tabindex="202" property="customerName" styleId="customerName" style="width: 300px" readonly="true" styleClass="c_disable" />
						</td>
						<th><div class="col_title_right">支払条件</div></th>
						<td>
							<html:hidden styleId="cutoffGroup" property="cutoffGroup"/>
							<html:hidden styleId="paybackCycleCategory" property="paybackCycleCategory"/>
							<html:select tabindex="203" property="cutoffGroupCategory"  styleId="cutoffGroupCategory" styleClass="c_disable">
								<html:options collection="cutoffGroupCategoryList" property="value" labelProperty="label"/>
							</html:select>
						</td>
					</tr>
					<tr>
						<th><div class="col_title_right">備考</div></th>
						<td colspan="5">
							<html:text property="customerRemarks" styleId="customerRemarks" styleClass="c_disable" style="width: 800px;" readonly="true" tabindex="204" />
						</td>
					</tr>
					<tr>
						<th><div class="col_title_right">コメント</div></th>
						<td colspan="5">
							<html:text property="customerCommentData" styleId="customerCommentData" styleClass="c_disable" style="width: 800px;" readonly="true" tabindex="205" />
						</td>
					</tr>
				</table>
				<table id="delivery_info" class="forms" summary="納入先情報">
					<colgroup>
						<col span="1" style="width: 10%">
						<col span="1" style="width: 12%">
						<col span="1" style="width: 8%">
						<col span="1" style="width: 17%">
						<col span="1" style="width: 8%">
						<col span="1" style="width: 17%">
					</colgroup>
					<tr>
						<th><div class="col_title_right">請求先名</div></th>
						<td>
							<html:text tabindex="300" property="baName" styleId="baName" style="width: 150px" readonly="true" styleClass="c_disable" />
							<html:hidden styleId="status" property="status"/>
							<html:hidden styleId="remarks" property="remarks"/>
							<html:hidden styleId="baUrl" property="baUrl"/>
							<html:hidden styleId="billId" property="billId"/>
							<html:hidden styleId="billCutoffDate" property="billCutoffDate"/>
							<html:hidden styleId="billCutoffPdate" property="billCutoffPdate"/>
							<html:hidden styleId="artId" property="artId"/>
							<html:hidden styleId="salesSlipId" property="salesSlipId"/>
							<html:hidden styleId="depositMethodTypeCategory" property="depositMethodTypeCategory"/>
							<html:hidden styleId="updDatetm" property="updDatetm"/>
							<html:hidden styleId="copySlipName" property="copySlipName"/>
							<html:hidden styleId="copySlipId" property="copySlipId"/>
							<html:hidden styleId="salesCutoffDate" property="salesCutoffDate"/>
							<html:hidden styleId="salesCutoffPdate" property="salesCutoffPdate"/>
						</td>
						<th><div class="col_title_right">事業所名</div></th>
						<td>
							<html:text tabindex="301" property="baOfficeName" styleId="baOfficeName" style="width: 200px" readonly="true" styleClass="c_disable" />
						</td>
						<th><div class="col_title_right">部署名</div></th>
						<td>
							<html:text tabindex="302" property="baDeptName" styleId="baDeptName" style="width: 200px" readonly="true" styleClass="c_disable" />
						</td>
					</tr>
					<tr>
						<th><div class="col_title_right">郵便番号</div></th>
						<td>
							<html:text tabindex="303" property="baZipCode" styleId="baZipCode" style="width: 100px" readonly="true" styleClass="c_disable" />
						</td>
						<th><div class="col_title_right">住所１</div></th>
						<td>
							<html:text tabindex="304" property="baAddress1" styleId="baAddress1" style="width: 200px" readonly="true" styleClass="c_disable" />
						</td>
						<th><div class="col_title_right">住所２</div></th>
						<td>
							<html:text tabindex="305" property="baAddress2" styleId="baAddress2" style="width: 200px" readonly="true" styleClass="c_disable" />
						</td>
					</tr>
					<tr>
						<th><div class="col_title_right">担当者</div></th>
						<td>
							<html:text tabindex="306" property="baPcName" styleId="baPcName" style="width: 150px" readonly="true" styleClass="c_disable" />
						</td>
						<th><div class="col_title_right">担当者カナ</div></th>
						<td>
							<html:text tabindex="307" property="baPcKana" styleId="baPcKana" style="width: 200px" readonly="true" styleClass="c_disable" />
						</td>
						<th><div class="col_title_right">敬称</div></th>
						<td>
							<html:select tabindex="308" property="baPcPreCatrgory" styleId="baPcPreCatrgory" styleClass="c_disable" style="width: 200px">
								<c:forEach var="ptcl" items="${preTypeCategoryList}">
									<html:option value="${ptcl.value}">${ptcl.label}</html:option>
								</c:forEach>
							</html:select>
						</td>
					</tr>
					<tr>
						<th><div class="col_title_right">TEL</div></th>
						<td>
							<html:text tabindex="309" property="baTel" styleId="baTel" style="width: 150px" readonly="true" styleClass="c_disable" />
						</td>
						<th><div class="col_title_right">E-MAIL</div></th>
						<td colspan="3">
							<html:text tabindex="310" property="baEmail" styleId="baEmail" style="width: 400px" readonly="true" styleClass="c_disable" />
						</td>
					</tr>
					<tr>
						<th><div class="col_title_right">FAX</div></th>
						<td>
							<html:text tabindex="311" property="baFax" styleId="baFax" style="width: 150px" readonly="true" styleClass="c_disable" />
						</td>
						<th><div class="col_title_right">取引区分</div></th>
						<td colspan="3">
							<html:select tabindex="312" property="salesCmCategoryName" styleId="salesCmCategoryName" styleClass="c_disable" >
								<c:forEach var="ptcl" items="${salesCmCategoryList}">
									<html:option value="${ptcl.value}">${ptcl.label}</html:option>
								</c:forEach>
							</html:select>
							<html:hidden styleId="salesCmCategory" property="salesCmCategory"/>
						</td>
					</tr>
				</table>
				</div>
				</div>
				</div>

				<div id="order_detail_info_wrap">
				<table id="order_detail_info" summary="入金明細リスト" class="forms" style="margin-top: 20px;">
					<tr>
						<th class="rd_top_left" style="height:30px;">No</th>
						<th>金額※</th>
						<th>銀行</th>
						<th>備考・手形番号/手形期日など</th>
						<th class="rd_top_right">&nbsp;</th>
					</tr>
					<tbody id="tbodyLine">
						<c:forEach var="depLineList" items="${depLineList}" varStatus="s" >
							<c:if test='${depLineList.lineNo != null}'>
							<tr id="trLine${s.index}">

								<!-- No -->
								<td id="tdNo${s.index}" style="text-align: center; width: 50px;">
									<div class="box_1of1">
										<c:out value="${f:h(depLineList.lineNo)}" />
									</div>
								</td>
								<td style="display: none;">
									<html:hidden name="depLineList" property="lineNo" indexed="true" styleId="depLineList[${s.index}].lineNo" />
									<html:hidden name="depLineList" property="depositLineId" indexed="true" styleId="depLineList[${s.index}].depositLineId" />
									<html:hidden name="depLineList" property="status" indexed="true" styleId="depLineList[${s.index}].status" />
									<html:hidden name="depLineList" property="depositSlipId" indexed="true" styleId="depLineList[${s.index}].depositSlipId" />
									<html:hidden name="depLineList" property="instDate" indexed="true" styleId="depLineList[${s.index}].instDate" />
									<html:hidden name="depLineList" property="instNo" indexed="true" styleId="depLineList[${s.index}].instNo" />
									<html:hidden name="depLineList" property="salesLineId" indexed="true" styleId="depLineList[${s.index}].salesLineId" />
								</td>

								<!-- 金額 -->
								<td style="width: 160px; background-color: #fae4eb;">
									<div class="box_1of1" style="margin: 5px;">
										<html:text tabindex="${401+s.index*5}" maxlength="9" name="depLineList" property="price" style="width: 100%; ime-mode:disabled;" styleClass="numeral_commas" indexed="true" styleId="depLineList[${s.index}].price" onchange="ChangePrice()" />
									</div>
								</td>

								<!-- 銀行 -->
								<td style="width: 300px;">
									<div class="box_1of1" style="margin: 5px;">
										<html:select tabindex="${402+s.index*5}" name="depLineList" property="bankId" style="ime-mode:disabled; width: 100%;" indexed="true" styleId="depLineList[${s.index}].bankId" >
											<html:options collection="bankMstList" property="value" labelProperty="label"/>
										</html:select>
									</div>
								</td>

								<!-- 備考・手形番号/手形期日など -->
								<td style="width: 520px;">
									<div class="box_1of1" style="margin: 5px;">
										<html:textarea tabindex="${403+s.index*5}" name="depLineList" property="remarks" style="width: 100%; height: 65px; ime-mode:active;" indexed="true" styleId="depLineList[${s.index}].remarks" />
									</div>
								</td>
								<td>

								<div class="box_1of2">
									<c:if test="${!menuUpdate}">
										<button id="deleteBtn${s.index}" class="btn_list_action" style="width: 80px" tabindex="${404+s.index*5}" disabled="disabled">削除</button><!-- (行)削除 -->
									</c:if>
									<c:if test="${menuUpdate}">
										<c:if test="${menuUpdate && !closed}">
											<button id="deleteBtn${s.index}" class="btn_list_action" style="width: 80px" tabindex="${404+s.index*5}">削除</button><!-- (行)削除 -->
										</c:if>
										<c:if test="${!menuUpdate || closed}">
											<button id="deleteBtn${s.index}" class="btn_list_action" style="width: 80px" tabindex="${404+s.index*5}" disabled="disabled">削除</button><!-- (行)削除 -->
										</c:if>
									</c:if>
								</div>
								<div class="box_2of2">
									<c:if test="${s.first}" >
										<button id="copyBtn${s.index}" class="btn_list_action" style="width: 80px" tabindex="${405+s.index*5}" disabled="disabled">前行複写</button><!-- 前行複写 -->
									</c:if>
									<c:if test="${!s.first}" >
										<c:if test="${menuUpdate && !closed}">
											<button id="copyBtn${s.index}" class="btn_list_action" style="width: 80px" tabindex="${405+s.index*5}">前行複写</button><!-- 前行複写 -->
										</c:if>
										<c:if test="${!menuUpdate || closed}">
											<button id="copyBtn${s.index}" class="btn_list_action" style="width: 80px" tabindex="${405+s.index*5}" disabled="disabled">前行複写</button><!-- 前行複写 -->
										</c:if>
									</c:if>
								</div>
								</td>
							</tr>
							</c:if>
						</c:forEach>

						<tr id="addLineTr" style="text-align: center;height: 60px;">
							<td class="rd_bottom_left rd_bottom_right"  colspan="10">
							<c:if test="${closed || !menuUpdate}">
								<!--<html:button tabindex="1999" styleId ="addLine" property="addLine" disabled="disabled">行追加</html:button>-->
								<button tabindex="1999" id="addLine" disabled="disabled">
								<img alt="行追加" border="none" src="${f:url('/images/customize/btn_line_add.png')}"  width="31" height="33">
								</button>
							</c:if>
							<c:if test="${!closed && menuUpdate}">
								<button tabindex="1999" id="addLine" onclick="addRow();">
								<img alt="行追加" border="none" src="${f:url('/images/customize/btn_line_add.png')}"  width="31" height="33">
								</button>
							</c:if>
							</td>
						</tr>


					</tbody>
				</table>
				</div>

				<!-- 削除された行のCSVデータ -->
		        <html:hidden styleId="deleteLineIds" property="deleteLineIds"/>

				<div id="poSlipPriseInfos" class="information" style="margin-top: 10px;">
		        <div id="information" class="information" style="">
					<table id="voucher_info" class="forms" summary="伝票情報" style="">
						<tr>
							<th style="height: 60px;" class="rd_top_left">前回請求額</th>
							<th>今回回収額</th>
							<th>今回売上高</th>
							<th class="rd_top_right">請求残高</th>
						</tr>
						<tr>
							<td id="lastBillingPrice" style="text-align: center; height: 100px;" class="BDCyen yen_value">
								&nbsp;<c:out value="${f:h(lastBillingPrice)}" />&nbsp;
							</td>
							<td id="nowPaybackPriceInput" style="text-align: center" class="BDCyen yen_value">
								&nbsp;<c:out value="${f:h(nowPaybackPrice)}" />&nbsp;
							</td>
							<td id="nowSalesPrice" style="text-align: center" class="BDCyen yen_value">
								&nbsp;<c:out value="${f:h(nowSalesPrice)}" />&nbsp;
							</td>
							<td id="billingBalancePrice" style="text-align: center" class="BDCyen yen_value">
								&nbsp;<c:out value="${f:h(billingBalancePrice)}" />&nbsp;
							</td>
							<html:hidden styleId="lastBillingPriceBack" property="lastBillingPrice"/>
							<html:hidden styleId="nowPaybackPriceBack" property="nowPaybackPrice"/>
							<html:hidden styleId="nowSalesPriceBack" property="nowSalesPrice"/>
						</tr>
					</table>
				</div>
				</div>

				<div style="width: 1160px; text-align: center; margin-top: 10px;">
					<c:if test="${newData}" >
					<c:if test="${menuUpdate && !closed}">
						<button id="btnF3btm" type="button" tabindex="1999" style="width:260px; height:51px;" class="btn_medium" onclick="onF3();">
							<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.register'/></span><%// 登録 %>
						</button>
					</c:if>
					<c:if test="${!menuUpdate || closed}">
						<button id="btnF3btm" type="button" tabindex="1999" style="width:260px; height:51px;" class="btn_medium" onclick="onF3();" disabled>
							<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.register'/></span><%// 登録 %>
						</button>
					</c:if>
					</c:if>
					<c:if test="${!newData}" >
					<c:if test="${menuUpdate && !closed}">
						<button id="btnF3btm" type="button" tabindex="1999" style="width:260px; height:51px;" class="btn_medium" onclick="onF3();">
							<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.renew'/></span><%// 更新 %>
						</button>
					</c:if>
					<c:if test="${!menuUpdate || closed}">
						<button id="btnF3btm" type="button" tabindex="1999" style="width:260px; height:51px;" class="btn_medium" onclick="onF3();" disabled>
							<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.renew'/></span><%// 更新 %>
						</button>
					</c:if>
					</c:if>
				</div>
			</div>
			<html:hidden property="newData" />
		</s:form>
	</div>

</body>

</html>
