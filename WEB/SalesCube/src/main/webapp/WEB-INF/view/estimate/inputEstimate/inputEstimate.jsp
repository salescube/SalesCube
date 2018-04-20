<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="jp.co.arkinfosys.form.AbstractSlipEditForm"%>
<%@page import="jp.co.arkinfosys.common.Categories"%>
<%@page import="jp.co.arkinfosys.common.Constants"%>
<%@page import="jp.co.arkinfosys.common.CategoryTrns" %>

<html lang="ja">
<head>


	<title><bean:message key='titles.system'/> 見積入力</title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>
<script type="text/javascript" src="${f:url('/scripts/dialogs.js')}"></script>
<script type="text/javascript" src="${f:url('/scripts/salescommon.js')}"></script>
<script type="text/javascript">
<!--

var MAIN_FORM_NAME = "estimate_inputEstimateActionForm";
var MAX_LINE_ROW_COUNT = <%= AbstractSlipEditForm.MAX_LINE_SIZE %>;
var maxIndex = 0;
var trCloneBase = null;
var trCloneBaseIndex = 0;

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

// 帳票出力時の更新チェックフラグ
var changeFlag = false;

//ページ読込時の動作
function init() {
	// 初期フォーカス　見積番号手入力対応

	if(${!newData}){
		$("#estimateSheetId").attr("readOnly", "true");
		$("#estimateSheetId").addClass("c_disable");
		$("#estimateSheetId").css("background-color", "#CCCCCC");
		$("#estimateSheetId").css("border-top", "2px solid #AEAEAE");
		$("#estimateDate").focus();
	}else{
		$("#estimateSheetId").focus();

	}

	if($("#estimateSheetId").val() == "" ){
		$("#btnF3").attr("disabled","true");
	}

	// 明細行のIndex管理
	var tBodyLine = $("#tbodyLine");
	maxIndex = tBodyLine.children().size() - 2;
	// 明細行のクローンを生成
	trCloneBase = tBodyLine.children(":first").clone(true);

	var match = trCloneBase.attr("id").match(/^trLine([0-9]+)$/);
	if( match ) {
		trCloneBaseIndex = parseInt( match[1] );
	}

	trCloneBase.find("input[type='hidden']").val("");
	trCloneBase.find("input[type='text']").val("");
	trCloneBase.find("textarea").text("");
	trCloneBase.find("span").html("");
	trCloneBase.find("select").each(
			function(){
				this.selectedIndex = 0;
			}
	);

	for(var i=0; i<=maxIndex; i++) {
		// 明細行の項目にイベントをバインド
		$("#estimateLineTrnDtoList\\[" + i + "\\]\\.productCode").bind("focus", {index: i}, function(e){ this.curVal=this.value; });
		$("#estimateLineTrnDtoList\\[" + i + "\\]\\.productCode").bind("blur", {index: i}, function(e){ if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); changeProductCode(e); } });
		$("#estimateLineTrnDtoList\\[" + i + "\\]\\.quantity").bind("focus", {index: i}, function(e){ this.curVal=this.value; });
		$("#estimateLineTrnDtoList\\[" + i + "\\]\\.quantity").bind("blur", {index: i}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){ onChangeQuantity(e); } });
		$("#estimateLineTrnDtoList\\[" + i + "\\]\\.unitCost").bind("focus", {index: i}, function(e){ this.curVal=this.value; });
		$("#estimateLineTrnDtoList\\[" + i + "\\]\\.unitCost").bind("blur", {index: i}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){ onChangeUnitCost(e); } });
		$("#estimateLineTrnDtoList\\[" + i + "\\]\\.cost").bind("focus", {index: i}, function(e){ this.curVal=this.value; });
		$("#estimateLineTrnDtoList\\[" + i + "\\]\\.cost").bind("blur", {index: i}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){onChangeCost(e); } });
		$("#estimateLineTrnDtoList\\[" + i + "\\]\\.unitRetailPrice").bind("focus", {index: i}, function(e){ this.curVal=this.value; });
		$("#estimateLineTrnDtoList\\[" + i + "\\]\\.unitRetailPrice").bind("blur", {index: i}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){onChangeUnitRetailPrice(e); } });
		$("#estimateLineTrnDtoList\\[" + i + "\\]\\.retailPrice").bind("focus", {index: i}, function(e){ this.curVal=this.value; });
		$("#estimateLineTrnDtoList\\[" + i + "\\]\\.retailPrice").bind("blur", {index: i}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){onChangeRetailPrice(e); } });

		$("#productCodeImg" + i).bind("click", {index: i}, openProductSearchDialog);
		$("#stockBtn" + i).bind("click", {index: i}, openStockInfo);
		$("#deleteBtn" + i).bind("click", {index: i}, deleteRow);
		$("#copyBtn" + i).bind("click", {index: i}, copyRow);

	}

	applyNumeralStyles(true);

	// 帳票出力時の更新チェックのため
	bindOnChange();

	$("#customerCode").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁

}

function bindOnChange(){

	// 帳票出力時の更新チェックのため
	$("input[type='text']").add("select").add("textarea").bind("change", changeOn );
}

function applyNumeralStyles(hasChanged){

	// 明細行のIndex管理
	var maxIndex = $("#tbodyLine").get(0).children.length-1;

	for(var i=0; i<=maxIndex; i++) {
		// 丸めの少数桁と端数処理を設定する
		applyNumeralStylesForRow(i)
	}

	// 粗利益
	applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#grossMargin"));
	// 粗利益率
	applyNumeralStylesToObj(rateCategory,rateAlignment,$("#grossMarginRate"));
	// 金額合計
	applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#retailPriceTotal"));
	// 消費税
	applyNumeralStylesToObj($("#taxFractCategory").val(),taxAlignment,$("#ctaxPriceTotal"));
	// 伝票合計
	applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#estimateTotal"));

	// 下部の合計欄を計算する
	sum(hasChanged);

	// カンマをつける
	_after_load($(".numeral_commas"));

}

function applyNumeralStylesForRow(i){
	// 丸めの少数桁と端数処理を設定する
	applyNumeralStylesToObj(quantityCategory,quantityAlignment,$("#estimateLineTrnDtoList\\[" + i + "\\]\\.quantity"));
	applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#estimateLineTrnDtoList\\[" + i + "\\]\\.unitCost"));
	applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#estimateLineTrnDtoList\\[" + i + "\\]\\.unitRetailPrice"));
	applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#estimateLineTrnDtoList\\[" + i + "\\]\\.cost"));
	applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#estimateLineTrnDtoList\\[" + i + "\\]\\.retailPrice"));
}

// 初期化
function onF1(){
	if(confirm('<bean:message key="confirm.init" />')){
		showNowSearchingDiv();
		location.doHref('${f:url("/estimate/inputEstimate")}');
		$("#estimateSheetId").focus();
	}
}

function findSlip(){
	if( $("#estimateSheetId").val() != "" ){

		showNowSearchingDiv();

 		//見積番号：存在チェック
		var data = new Object();
		data["estimateSheetId"] = $("#estimateSheetId").val();
		$("#errors").empty();
		$("#messages").empty();

		// 新規見積番号⇒画面データ保持、既存見積番号⇒ダイアログ表示
		syncRequest(
			contextRoot + "/ajax/estimate/checkEstimateSheet/exists",
			data,
			function(data) {
				if(data!=""){
					myRet = confirm('<bean:message key="confirm.estimateSheetId.upd" />')
					if(myRet == true){
						$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/estimate/inputEstimate/load")}');
						$("form[name='" + MAIN_FORM_NAME + "']").submit();
					}else{
						$("#messages").append('<bean:message key="errors.estimateSheetId.upd" />');
					}
				}else{
					$("#messages").append('<bean:message key="infos.mode.new" />');
					//$("#estimateSheetId").attr("readOnly", "true");
					//$("#estimateSheetId").addClass("c_disable");
					//$("#estimateSheetId").css("background-color", "#CCCCCC");
					//$("#estimateSheetId").css("border-top", "2px solid #AEAEAE");
					if( ${menuUpdate} ){
						$("#btnF3").removeAttr("disabled");
					}
				}
			}
		);
	}
}

// 削除
function onF2(){
	<c:if test='${newData || !menuUpdate}'>
		return;
	</c:if>

	if(confirm('<bean:message key="confirm.delete" />')){
		// カンマ除去
		_before_submit($(".numeral_commas"));

		$("#grossMargin").val(_rmv_cunit($("#grossMargin").val()));
		$("#retailPriceTotal").val(_rmv_cunit($("#retailPriceTotal").val()));
		$("#ctaxPriceTotal").val(_rmv_cunit($("#ctaxPriceTotal").val()));
		$("#estimateTotal").val(_rmv_cunit($("#estimateTotal").val()));

		showNowSearchingDiv();
		$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/estimate/inputEstimate/delete")}');
		$("form[name='" + MAIN_FORM_NAME + "']").submit();

	}
}

// 登録
function onF3(){
	<c:if test='${!menuUpdate}'>
		return;
	</c:if>

	// 見積番号のチェック
	$("#errors").empty();
	$("#messages").empty();

	if( $("#estimateSheetId").val() == "" ) {
		$("#estimateSheetId").focus();
		$("#errors").append(document.createTextNode('<bean:message key="errors.required" arg0="見積番号" />'));
		return;
	}

	// 見積番号編集可⇒保存不可
//	if( $("#estimateSheetId").attr("readOnly") == false) {
//		$("#estimateSheetId").focus();
//		$("#errors").append(document.createTextNode('<bean:message key="errors.estimateSheetId.upd" />'));
//		return;
//	}

	if(confirm('<bean:message key="confirm.insert" />')){

		// カンマ除去
		_before_submit($(".numeral_commas"));

		$("#grossMargin").val(_rmv_cunit($("#grossMargin").val()));
		$("#retailPriceTotal").val(_rmv_cunit($("#retailPriceTotal").val()));
		$("#ctaxPriceTotal").val(_rmv_cunit($("#ctaxPriceTotal").val()));
		$("#estimateTotal").val(_rmv_cunit($("#estimateTotal").val()));

		// 登録前のデータ調整
		beforeRegister();

		showNowSearchingDiv();
		$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/estimate/inputEstimate/upsert")}');
		$("form[name='" + MAIN_FORM_NAME + "']").submit();

	}
}

//登録前のデータ調整
function beforeRegister(){

	  $("#submitPreCategory option:selected").each(function() {
			$("#submitPre").attr("value",$(this).text());
		  });
}

function changeOn(){
	changeFlag = true;
}

// PDF
function onF4() {
	if(changeFlag){
		alert("<bean:message key='infos.valid.print' arg0='PDF'/>");
	}
	else {
		var form = $(window.document.forms["PDFOutputForm"]);
		var hidden = $(document.createElement("input"));
		hidden.attr("type", "hidden");
		hidden.attr("name", "estimateSheetId");
		hidden.val("${f:h(estimateSheetId)}");
		form.append(hidden);
		form.submit();
	}

}

// 伝票複写で指定された伝票を読み込む
function copySlipCallback(dialogId, slipName, slipId){
	if(confirm("<bean:message key='confirm.copyslip'/>")){
		showNowSearchingDiv();
		$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/estimate/inputEstimate/copySlip/'+slipId+'")}');
		$("form[name='" + MAIN_FORM_NAME + "']").submit();
	}
}


// 行追加
function addRow(){
	var elemTr, elemTd, elemWork;
	var lineSize, tabIdx;

	// 最大行数の確認
	lineSize = $("#tbodyLine").get(0).children.length-1;

	if(lineSize >= MAX_LINE_ROW_COUNT) {
		alert('<bean:message key="errors.line.maxrows" />');
		return;
	}

	// 明細行のIndex管理
	var endLine = $("#tbodyLine").children("[id^='trLine']:last");
	var match = endLine.attr("id").match(/^trLine([0-9]+)$/);
	if( match ) {
		maxIndex = match[1];
	}
	maxIndex++;

	tabIdx = parseInt($("#addRowBtn").attr("tabindex"));

	// ベースオブジェクトからクローンを生成
	elemTr = trCloneBase.clone(true);
	elemTr.find("select").each(
		function() {
			this.selectedIndex = 0;
		}
	);
	elemTr.attr("id", "trLine" + maxIndex);

	// No列の設定
	elemTd = elemTr.children(":first");
	elemTd.attr("id", "tdNo" + maxIndex);
	elemTd.html(lineSize + 1);

	// Hidden列の設定
	elemTd = elemTd.next();

	elemWork = elemTd.children("#estimateLineTrnDtoList\\[" + trCloneBaseIndex + "\\]\\.lineNo");
	elemWork.attr("id", "estimateLineTrnDtoList[" + maxIndex + "].lineNo");
	elemWork.attr("name", "estimateLineTrnDtoList[" + maxIndex + "].lineNo");
	elemWork.val(lineSize + 1);

	elemWork = elemTd.children("#estimateLineTrnDtoList\\[" + trCloneBaseIndex + "\\]\\.estimateLineId");
	elemWork.attr("id", "estimateLineTrnDtoList[" + maxIndex + "].estimateLineId");
	elemWork.attr("name", "estimateLineTrnDtoList[" + maxIndex + "].estimateLineId");

	elemWork = elemTd.children("#estimateLineTrnDtoList\\[" + trCloneBaseIndex + "\\]\\.customerPcode");
	elemWork.attr("id", "estimateLineTrnDtoList[" + maxIndex + "].customerPcode");
	elemWork.attr("name", "estimateLineTrnDtoList[" + maxIndex + "].customerPcode");

	elemWork = elemTd.children("#estimateLineTrnDtoList\\[" + trCloneBaseIndex + "\\]\\.roMaxNum");
	elemWork.attr("id", "estimateLineTrnDtoList[" + maxIndex + "].roMaxNum");
	elemWork.attr("name", "estimateLineTrnDtoList[" + maxIndex + "].roMaxNum");

	elemWork = elemTd.children("#estimateLineTrnDtoList\\[" + trCloneBaseIndex + "\\]\\.possibleDrawQuantity");
	elemWork.attr("id", "estimateLineTrnDtoList[" + maxIndex + "].possibleDrawQuantity");
	elemWork.attr("name", "estimateLineTrnDtoList[" + maxIndex + "].possibleDrawQuantity");

	elemWork = elemTd.children("#estimateLineTrnDtoList\\[" + trCloneBaseIndex + "\\]\\.stockCtlCategory");
	elemWork.attr("id", "estimateLineTrnDtoList[" + maxIndex + "].stockCtlCategory");
	elemWork.attr("name", "estimateLineTrnDtoList[" + maxIndex + "].stockCtlCategory");

	// 商品コード列の設定
	elemTd = elemTd.next();
	elemWork = setElem( elemTd.children(), "productCode", maxIndex, null , (++tabIdx));
	elemWork.bind("focus", {index: maxIndex}, function(e){ this.curVal=this.value; });
	elemWork.bind("blur", {index: maxIndex}, function(e){ if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); changeProductCode(e); } });

	elemWork = elemTd.children().children("#productCodeImg0");
	elemWork.attr("id", "productCodeImg" + maxIndex);
	elemWork.attr("tabindex", (++tabIdx));
	elemWork.bind("click", {index: maxIndex}, openProductSearchDialog);

	// 商品名列の設定
	elemTd = elemTd.next();
	elemWork = setElem( elemTd.children(), "productAbstract", maxIndex, null , (++tabIdx));

	// 数量列の設定
	elemTd = elemTd.next();
	elemWork = setElem( elemTd.children(), "quantity", maxIndex, null , (++tabIdx));
//	elemWork.bind("change", {index: maxIndex}, onChangeQuantity);
	elemWork.bind("focus", {index: maxIndex}, function(e){ this.curVal=this.value; });
	elemWork.bind("blur", {index: maxIndex}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){ onChangeQuantity(e); } });

	// 在庫ボタン
	elemWork = elemTd.children().children("#stockBtn0");
	elemWork.attr("id", "stockBtn" + maxIndex);
	elemWork.attr("tabindex", (++tabIdx));
	elemWork.bind("click", {index: maxIndex}, openStockInfo);

	// 仕入単価
	elemTd = elemTd.next();
	elemWork = setElem( elemTd.children(), "unitCost", maxIndex, null , (++tabIdx));
//	elemWork.bind("change", {index: maxIndex}, onChangeUnitCost);
	elemWork.bind("focus", {index: maxIndex}, function(e){ this.curVal=this.value; });
	elemWork.bind("blur", {index: maxIndex}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){ onChangeUnitCost(e); } });

	// 仕入金額
	elemWork = elemTd.children("#estimateLineTrnDtoList\\[" + trCloneBaseIndex + "\\]\\.cost");
	elemWork = setElem( elemTd.children(), "cost", maxIndex, null , (++tabIdx));
//	elemWork.bind("change", null, onChangeCost);
	elemWork.bind("focus", {index: maxIndex}, function(e){ this.curVal=this.value; });
	elemWork.bind("blur", {index: maxIndex}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){ onChangeCost(e); } });

	// 売上単価
	elemTd = elemTd.next();
	elemWork = setElem( elemTd.children(), "unitRetailPrice", maxIndex, null , (++tabIdx));
//	elemWork.bind("change", {index: maxIndex}, onChangeUnitRetailPrice);
	elemWork.bind("focus", {index: maxIndex}, function(e){ this.curVal=this.value; });
	elemWork.bind("blur", {index: maxIndex}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){ onChangeUnitRetailPrice(e); } });

	// 売価金額
	elemWork = elemTd.children("#estimateLineTrnDtoList\\[" + trCloneBaseIndex + "\\]\\.retailPrice");
	elemWork = setElem( elemTd.children(), "retailPrice", maxIndex, null , (++tabIdx));
//	elemWork.bind("change", null, onChangeRetailPrice);
	elemWork.bind("focus", {index: maxIndex}, function(e){ this.curVal=this.value; });
	elemWork.bind("blur", {index: maxIndex}, function(e){ if(dec_numeral_commas(this.curVal)!=dec_numeral_commas(this.value)){ onChangeRetailPrice(e); } });

	// 備考列の設定
	elemTd = elemTd.next();
	elemWork = setElem( elemTd.children(), "remarks", maxIndex, null , (++tabIdx));

	// ボタン列の設定
	elemTd = elemTd.next();
	elemWork = elemTd.children().children("#deleteBtn0");
	elemWork.attr("id", "deleteBtn" + maxIndex);
	elemWork.attr("tabindex", (++tabIdx));
	elemWork.bind("click", {index: maxIndex}, deleteRow);

	elemWork = elemTd.children().children("#copyBtn0");
	elemWork.attr("id", "copyBtn" + maxIndex);
	elemWork.attr("tabindex", (++tabIdx));
	elemWork.bind("click", {index: maxIndex}, copyRow);
	if(lineSize == 0) {
		elemWork.get(0).disabled = true;
	}else{
		elemWork.removeAttr("disabled");
	}

	// 行を追加
	$("#trAddLine").before(elemTr);

	applyNumeralStylesForRow(maxIndex);

	// 行追加ボタンのインデックスを設定する
	$("#addRowBtn").attr("tabindex",++tabIdx);

	// １行目の削除ボタンの活性化
	trId = $("#tbodyLine").get(0).children[0].id;
	id = trId.replace("trLine", "");
	$("#deleteBtn"+id).get(0).disabled = false;

	bindOnChange();

}

function setElem( elmPa, name, idx, val, tabIdx ){
	var elemWork;
	elemWork = elmPa.children("#estimateLineTrnDtoList\\[" + trCloneBaseIndex + "\\]\\." + name );
	elemWork.attr("id", "estimateLineTrnDtoList[" + idx + "]." + name );
	elemWork.attr("name", "estimateLineTrnDtoList[" + idx + "]." + name );
	if( val != null ){
		elemWork.val(val);
	}
	elemWork.attr("tabindex", tabIdx);
	return elemWork;
}

// 行削除
function deleteRow(event){
	var index = event.data.index;
	var i, lineNo, lineSize;
	var trId, id;

	if(confirm('<bean:message key="confirm.line.delete" />')){

		// 見積明細がある場合はHIDDENに追加する
		var deleteEstimateLineId = $("#estimateLineTrnDtoList\\["+index+"\\]\\.estimateLineId").get(0).value;

		if(deleteEstimateLineId != null && deleteEstimateLineId.length > 0){

			var ids = $("#deleteLineIds").val();
			if(ids.length > 0){
				ids += ",";
			}
			$("#deleteLineIds").val(ids + deleteEstimateLineId);
		}

		if(!isEmptyLine(index)) {
			// 変更フラグＯＮ
			changeFlag = true;
		}


		// 行を削除する
		lineNo = parseInt($("#estimateLineTrnDtoList\\["+index+"\\]\\.lineNo").get(0).value)-1;
		var tBodyLine = $("#tbodyLine");
		tBodyLine.get(0).deleteRow(lineNo);
		// 行番号を調整する
		lineSize = tBodyLine.children().size() - 1;
		for(i=lineNo; i<lineSize; i++) {
			trId = tBodyLine.children().get(i).id;
			id = trId.replace("trLine", "");
			// 行番号を振りなおす
			$("#tdNo"+id).get(0).innerHTML = i+1;
			$("#estimateLineTrnDtoList\\["+id+"\\]\\.lineNo").get(0).value = i + 1;
			// 先頭行の場合、前行複写ボタンの不活性化
			if(i == 0) {
				$("#copyBtn"+id).get(0).disabled = true;
			}
		}
		// 残り１行の場合、削除ボタンの不活性化
		if(lineSize == 1) {
			trId = tBodyLine.children(":first").attr("id");
			id = trId.replace("trLine", "");
			$("#deleteBtn"+id).get(0).disabled = true;
		}

		// 合計金額計算
		sum(true);
	}
}

// 前行複写
function copyRow(event){
	var index = event.data.index;

	var prevIndex;
	var i;

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

	// 変更フラグＯＮ
	changeFlag = true;

	// 合計金額計算
	sum(true);
}


// 明細行の空行判定
function isEmptyLine(lineNo){
	var retVal = true;
	var elem;
	// 商品コード
	elem = $("#estimateLineTrnDtoList\\["+lineNo+"\\]\\.productCode").get(0);
	if(elem.value != "") {
		retVal = false;
	}
	// 商品名・摘要
	elem = $("#estimateLineTrnDtoList\\["+lineNo+"\\]\\.productAbstract").get(0);
	if(elem.value != "") {
		retVal = false;
	}
	// 数量
	elem = $("#estimateLineTrnDtoList\\["+lineNo+"\\]\\.quantity").get(0);
	if(elem.value != "") {
		retVal = false;
	}
	// 仕入単価（原単価）
	elem = $("#estimateLineTrnDtoList\\["+lineNo+"\\]\\.unitCost").get(0);
	if(elem.value != "") {
		retVal = false;
	}
	// 仕入金額（原価金額）
	elem = $("#estimateLineTrnDtoList\\["+lineNo+"\\]\\.cost").get(0);
	if(elem.value != "") {
		retVal = false;
	}
	// 売上単価
	elem = $("#estimateLineTrnDtoList\\["+lineNo+"\\]\\.unitRetailPrice").get(0);
	if(elem.value != "") {
		retVal = false;
	}
	// 売価金額
	elem = $("#estimateLineTrnDtoList\\["+lineNo+"\\]\\.retailPrice").get(0);
	if(elem.value != "") {
		retVal = false;
	}
	// 備考
	elem = $("#estimateLineTrnDtoList\\["+lineNo+"\\]\\.remarks").get(0);
	if(elem.value != "") {
		retVal = false;
	}

	return retVal;
}

// 明細行のコピー
function copyLine(destIndex, srcIndex){
	var destElem, srcElem;
	// 商品コード
	destElem = $("#estimateLineTrnDtoList\\["+destIndex+"\\]\\.productCode").get(0);
	srcElem = $("#estimateLineTrnDtoList\\["+srcIndex+"\\]\\.productCode").get(0);
	destElem.value = srcElem.value;
	//商品・摘要
	destElem = $("#estimateLineTrnDtoList\\["+destIndex+"\\]\\.productAbstract").get(0);
	srcElem = $("#estimateLineTrnDtoList\\["+srcIndex+"\\]\\.productAbstract").get(0);
	destElem.value = srcElem.value;
	// 数量
	destElem = $("#estimateLineTrnDtoList\\["+destIndex+"\\]\\.quantity").get(0);
	srcElem = $("#estimateLineTrnDtoList\\["+srcIndex+"\\]\\.quantity").get(0);
	destElem.value = srcElem.value;

	// 仕入単価（原単価）
	destElem = $("#estimateLineTrnDtoList\\["+destIndex+"\\]\\.unitCost").get(0);
	srcElem = $("#estimateLineTrnDtoList\\["+srcIndex+"\\]\\.unitCost").get(0);
	destElem.value = srcElem.value;
	// 仕入金額（原価金額）
	destElem = $("#estimateLineTrnDtoList\\["+destIndex+"\\]\\.cost").get(0);
	srcElem = $("#estimateLineTrnDtoList\\["+srcIndex+"\\]\\.cost").get(0);
	destElem.value = srcElem.value;
	// 売上単価
	destElem = $("#estimateLineTrnDtoList\\["+destIndex+"\\]\\.unitRetailPrice").get(0);
	srcElem = $("#estimateLineTrnDtoList\\["+srcIndex+"\\]\\.unitRetailPrice").get(0);
	destElem.value = srcElem.value;
	// 売価金額
	destElem = $("#estimateLineTrnDtoList\\["+destIndex+"\\]\\.retailPrice").get(0);
	srcElem = $("#estimateLineTrnDtoList\\["+srcIndex+"\\]\\.retailPrice").get(0);
	destElem.value = srcElem.value;

	// 備考
	destElem = $("#estimateLineTrnDtoList\\["+destIndex+"\\]\\.remarks").get(0);
	srcElem = $("#estimateLineTrnDtoList\\["+srcIndex+"\\]\\.remarks").get(0);
	destElem.value = srcElem.value;
}

// 指定行から前行のIndexを返す
// （見つからない場合は-1を返す）
function getPrevIndex(index){

	var retVal = -1;
	var i, lineNo, trId;
	// 指定行の行番号を取得
	lineNo = parseInt($("#estimateLineTrnDtoList\\["+index+"\\]\\.lineNo").get(0).value);

	// 前行を探す（削除行は非表示になっている為、直前の削除されていない行を探す）
	if(lineNo>1) {
		trId = $("#tbodyLine").get(0).children[lineNo-2].id;

		retVal = trId.replace("trLine", "");


	}

	return retVal;
}




//商品検索ダイアログ
function openProductSearchDialog(event) {
	var index = event.data.index;

	// 商品検索ダイアログを開く
	openSearchProductDialog(index, setProductInfo );
	// 商品コードを設定する
	$("#"+index+"_productCode").val($("#estimateLineTrnDtoList\\["+index+"\\]\\.productCode").val());
}

//商品検索ダイアログで選択後の設定処理
function setProductInfo(index, map) {

	// 在庫情報を検索してmapに設定する
	searchProductStock(map);

	setProduct(index,map);
	// まとめ買い値引き単価取得
	searchBulkPrice(index);
}

//在庫情報を取得する
function searchProductStock(map){

	var data = new Object();
	data["productCode"] = map["productCode"];
//	asyncRequest(
// 商品情報をセットする処理と同期がとれていないため、同期して動作するように修正
	syncRequest(
		contextRoot + "/ajax/dialog/showStockInfoDialog/calcStock",
		data,
		function(data) {
			if(data!=""){
				var value = eval("(" + data + ")");
				setProductStock(map,value);
			}
		}
	);

	return false;
}


//商品在庫情報を取得する
function setProductStock(map,data){

	// 引当可能数
	map["possibleDrawQuantity"] = data["possibleDrawQuantity"];

}


// 商品コード変更
function changeProductCode(event) {

	ProductCodeChange(event.data.index);
}

//商品コード変更時（フォーカスアウト）
function ProductCodeChange(index){

	var productCode = $("#estimateLineTrnDtoList\\["+index+"\\]\\.productCode").val();
	var label = '<bean:message key="labels.productCode" />';

	if(productCode==""){
		InitProductInfos(index);
	}else{
		var data = new Object();
		data["productCode"] = productCode;
		asyncRequest(
			contextRoot + "/ajax/commonProduct/getProductInfos",
			data,
			function(data) {
				if(data==""){
					InitProductInfos(index);
				}else{
					var value = eval("(" + data + ")");
					SetProductInfosToFroms(index,value);
					// まとめ買い値引き単価取得
					searchBulkPrice(index);
				}
			}
		);
	}
	return false;
}

//商品情報初期化
function InitProductInfos(index){
	// 商品コードはそのまま
	// 商品名
//	$("#estimateLineTrnDtoList\\["+index+"\\]\\.productAbstract").val("");
	// 仕入単価 SUPPLIER_PRICE_YEN
//	$("#estimateLineTrnDtoList\\["+index+"\\]\\.unitCost").val("");
	// 売上単価 RETAIL_PRICE
//	$("#estimateLineTrnDtoList\\["+index+"\\]\\.unitRetailPrice").val("");

	// 相手先コード（仕入先品番）
	$("#estimateLineTrnDtoList\\["+index+"\\]\\.customerPcode").val("");
	// 受注限度数
	$("#estimateLineTrnDtoList\\["+index+"\\]\\.roMaxNum").val("");

	// 在庫情報
	// 引当可能数
	$("#estimateLineTrnDtoList\\["+index+"\\]\\.possibleDrawQuantity").val("");

	// 在庫管理区分
	$("#estimateLineTrnDtoList\\["+index+"\\]\\.stockCtlCategory").val("");

	// 変更フラグＯＮ
	changeFlag = true;

	// 列の合計を計算する
//	sumLine(index)

}

//商品情報設定(確定した情報のセット以外には使わないで（ry)
//引当可能数は、商品コードが決まっているので商品検索時にmapに設定する
function SetProductInfosToFroms(index,map){
	setProduct(index,map);

}

//商品検索後の反映の上書き確認
function checkProductWrite(index){
	if($("#estimateLineTrnDtoList\\["+index+"\\]\\.productAbstract").val()
			|| $("#estimateLineTrnDtoList\\["+index+"\\]\\.unitCost").val()
			|| $("#estimateLineTrnDtoList\\["+index+"\\]\\.unitRetailPrice").val()){

				// 上書き確認
				return confirm('<bean:message key="confirm.product.copy" />');

	}

	return true;
}

//商品検索で取得したデータを画面に設定する
function setProduct(index,map){

	// 廃止されている商品は警告ダイアログを表示し、処理を続行する。
	if(map["discarded"] == "1"){
		alert('<bean:message key="warns.product.discarded" />');
	}

	// 上書き確認
	if(!checkProductWrite(index)){
		return;
	}

	// 商品コード
	$("#estimateLineTrnDtoList\\["+index+"\\]\\.productCode").val(map["productCode"]);
	// 商品名
	$("#estimateLineTrnDtoList\\["+index+"\\]\\.productAbstract").val(map["productName"]);

	// 特殊商品コードの数量を１にする
	sc_setLooseExceptianalProductCodeQuantiry($("#estimateLineTrnDtoList\\["+index+"\\]\\.productCode").val(),
			$("#estimateLineTrnDtoList\\["+index+"\\]\\.quantity"))

	// 仕入単価 SUPPLIER_PRICE_YEN
	$("#estimateLineTrnDtoList\\["+index+"\\]\\.unitCost").val(map["supplierPriceYen"]);
	SetBigDecimalScale_Obj($("#estimateLineTrnDtoList\\["+index+"\\]\\.unitCost"));
	// 売上単価 RETAIL_PRICE
	$("#estimateLineTrnDtoList\\["+index+"\\]\\.unitRetailPrice").val(map["retailPrice"]);
	SetBigDecimalScale_Obj($("#estimateLineTrnDtoList\\["+index+"\\]\\.unitRetailPrice"));

	// 相手先コード（仕入先品番）
	$("#estimateLineTrnDtoList\\["+index+"\\]\\.customerPcode").val(map["supplierPcode"]);
	// 受注限度数
	$("#estimateLineTrnDtoList\\["+index+"\\]\\.roMaxNum").val(map["roMaxNum"]);

	// 在庫情報
	// 引当可能数
	$("#estimateLineTrnDtoList\\["+index+"\\]\\.possibleDrawQuantity").val(map["possibleDrawQuantity"]);
	// 在庫管理区分
	$("#estimateLineTrnDtoList\\["+index+"\\]\\.stockCtlCategory").val(map["stockCtlCategory"]);

	// 変更フラグＯＮ
	changeFlag = true;

	// 列の合計を計算する
	sumLine(index)

}



//在庫ボタンクリック
function openStockInfo(event) {
	var index = event.data.index;
	// 在庫ダイアログを開く
	var productCode = $("#estimateLineTrnDtoList\\["+index+"\\]\\.productCode").val();
	if(!productCode){
		return;
	}

	openStockInfoDialog('stockInfo', productCode);
}

//まとめ買い値引き単価を取得する
function searchBulkPrice(index){

	var elem = $("#estimateLineTrnDtoList\\["+index+"\\]\\.quantity");
	if(elem.val() == "") {
		changeQuantity( index );
		return;
	}
	var data = new Object();
	data["bulkProductCode"] = $("#estimateLineTrnDtoList\\["+index+"\\]\\.productCode").val();
	data["bulkQuantity"] = $("#estimateLineTrnDtoList\\["+index+"\\]\\.quantity").val();

	asyncRequest(
		contextRoot + "/ajax/commonBulkRetailPrice/getPrice",
		data,
		function(data) {
			if(data!=""){
				var value = eval("(" + data + ")");
				// 売上単価
				$("#estimateLineTrnDtoList\\["+index+"\\]\\.unitRetailPrice").val(value);
				changeQuantity( index );
			}
		}
	);
	return false;
}

//数量変更
function onChangeQuantity(event){
	var index = event.data.index;

	// asyncで処理しない。
	async_request_off = true;

	searchBulkPrice(index);
}

// 数量変更
function changeQuantity(index){
	var objQuantity= $("#estimateLineTrnDtoList\\["+index+"\\]\\.quantity");
	var l_quantity = oBDCS(objQuantity.val()).setSettingsFromObj(objQuantity);
    var bExceptianal = sc_isLooseExceptianalProductCode($("#estimateLineTrnDtoList\\["+index+"\\]\\.productCode").val() );

	if(l_quantity.isNum()){
		// 受注限度数を超えている場合
		if(IsCheckOverQuantity(index)){
			var roMaxNum = $("#estimateLineTrnDtoList\\["+index+"\\]\\.roMaxNum").val();

			if(_isNum(roMaxNum) && _Number(roMaxNum) && l_quantity.BDValue() > _Number(roMaxNum) && !bExceptianal ){
				alert('<bean:message key="warns.quantity.over.roMaxNum" />');
			}
		}

		// 引当可能数を超えている場合
		if(IsCheckpossibleDrawQuantity(index)){
			var possibleDrawQuantity = $("#estimateLineTrnDtoList\\["+index+"\\]\\.possibleDrawQuantity").val();
			if(_isNum(possibleDrawQuantity) && l_quantity.BDValue() > _Number(possibleDrawQuantity) && !bExceptianal ){
				alert('<bean:message key="warns.quantity.over.possibleDrawQuantity" />');
				alert('<bean:message key="warns.quantity.over.possibleDrawQuantity.again" />');
			}
		}
	}


	// 列の合計を計算する
	sumLine(index);

	// カンマをつける
	_after_load($(".numeral_commas"));

}
// 引当可能数チェックをするか？
function IsCheckpossibleDrawQuantity( index ){
	// 在庫管理する商品のみ
	if($("#estimateLineTrnDtoList\\["+index+"\\]\\.stockCtlCategory").val() == "<%=CategoryTrns.PRODUCT_STOCK_CTL_NO%>" )
		return false;
	// 特殊コード以外
	var excpProductCode = sc_isLooseExceptianalProductCode($("#estimateLineTrnDtoList\\["+index+"\\]\\.productCode").val() );
	if(excpProductCode)
		return false;

	// 数量マイナス以外
	if($("#estimateLineTrnDtoList\\["+index+"\\]\\.quantity").val() < 0)
		return false;

	return true;

}

//大口受注チェックをするか？
function IsCheckOverQuantity( index ){
	// 特殊コード以外
	var excpProductCode = sc_isLooseExceptianalProductCode($("#estimateLineTrnDtoList\\["+index+"\\]\\.productCode").val() );
	if(excpProductCode)
		return false;

	// 数量マイナス以外
	if($("#estimateLineTrnDtoList\\["+index+"\\]\\.quantity").val() < 0)
		return false;

	return true;

}

function sumLine(index){
	culcCost(index);
	culcRetailPrice(index);
	sum(true);
}

// 仕入単価変更
function onChangeUnitCost(event){
	var index = event.data.index;
	culcCost(index);
	sum(true);
}

// 仕入金額変更
function onChangeCost(){
	sum(true);
}

function culcCost(index){
	var objUnitCost = $("#estimateLineTrnDtoList\\["+index+"\\]\\.unitCost");
	var objQuantity= $("#estimateLineTrnDtoList\\["+index+"\\]\\.quantity");

	var l_unitCost = oBDCS(objUnitCost.val()).setSettingsFromObj(objUnitCost);
	var l_quantity = oBDCS(objQuantity.val()).setSettingsFromObj(objQuantity);

	if ( l_quantity.isNum() && l_unitCost.isNum()){

		// 仕入金額＝仕入単価×数量
		$("#estimateLineTrnDtoList\\["+index+"\\]\\.cost").val(l_unitCost.BDValue()*l_quantity.BDValue());

		SetBigDecimalScale_Obj($("#estimateLineTrnDtoList\\["+index+"\\]\\.cost"));
	}else{

		$("#estimateLineTrnDtoList\\["+index+"\\]\\.cost").val("");
	}

}

function changeTaxRate(){
	sum(true);
}


// 売上単価変更
function onChangeUnitRetailPrice(event){
	var index = event.data.index;
	culcRetailPrice(index);
	sum(true);
}

//売上金額変更
function onChangeRetailPrice(){
	sum(true);
}


function culcRetailPrice(index){
	var unitRetailPrice = $("#estimateLineTrnDtoList\\["+index+"\\]\\.unitRetailPrice").val();
	var quantity = $("#estimateLineTrnDtoList\\["+index+"\\]\\.quantity").val();

	var objUnitRetailPrice = $("#estimateLineTrnDtoList\\["+index+"\\]\\.unitRetailPrice");
	var objQuantity= $("#estimateLineTrnDtoList\\["+index+"\\]\\.quantity");

	var l_unitRetailPrice = oBDCS(objUnitRetailPrice.val()).setSettingsFromObj(objUnitRetailPrice);
	var l_quantity = oBDCS(objQuantity.val()).setSettingsFromObj(objQuantity);


	if(l_quantity.isNum() && l_unitRetailPrice.isNum()){
		// 売価金額＝売上単価×数量
		$("#estimateLineTrnDtoList\\["+index+"\\]\\.retailPrice").val(l_unitRetailPrice.BDValue()*l_quantity.BDValue());
		SetBigDecimalScale_Obj($("#estimateLineTrnDtoList\\["+index+"\\]\\.retailPrice"));
	}else{
		// 売価金額＝売上単価×数量
		$("#estimateLineTrnDtoList\\["+index+"\\]\\.retailPrice").val("");
	}

}

// 合計を計算する
function sum(hasChanged){

	// [仕入金額]の合計
	var sumCost = 0;
	// [売価金額]の合計
	var sumRetailPrice = 0;

	var lineSize = $("#tbodyLine").get(0).children.length-1;
	for(i=0; i<lineSize; i++) {
		trId = $("#tbodyLine").get(0).children[i].id;
		id = trId.replace("trLine", "");
		var cost = _Number($("#estimateLineTrnDtoList\\["+id+"\\]\\.cost").val());


		if(cost){
			sumCost += parseInt(cost);
		}
		var retailPrice = _Number($("#estimateLineTrnDtoList\\["+id+"\\]\\.retailPrice").val());

		if(retailPrice){
			sumRetailPrice += parseInt(retailPrice);
		}
	}

	// [粗利益] 	= [売価金額]の合計―[仕入金額]の合計
	var grossMargin = sumRetailPrice-sumCost;
	$("#grossMargin").val(grossMargin);
	SetBigDecimalScale_Obj($("#grossMargin"));

	// [金額合計]	= [売価金額]の合計
	$("#retailPriceTotal").val(sumRetailPrice);
	SetBigDecimalScale_Obj($("#retailPriceTotal"));

	// [粗利益率]	= [粗利益]／[金額合計]
	if(sumRetailPrice == 0){
		$("#grossMarginRate").val("0.00%");
	}else{

		// 2010.04.22 add kaki 粗利益率は％表示とする。まず100倍する。
		var perObj = oBDCS(String("100")).setScale(rateCategory,rateAlignment).BDValue();
		var grossMarginObj = oBDCS(String(grossMargin)).setScale(rateCategory,rateAlignment).BDValue();
		var sumRetailPriceObj = oBDCS(String(sumRetailPrice)).setScale(rateCategory,rateAlignment).BDValue();
		var grossMarginRate = grossMarginObj.multiply(perObj).divide(sumRetailPriceObj).toString();

		$("#grossMarginRate").val(grossMarginRate);
		SetBigDecimalScale_Obj($("#grossMarginRate"));

		// 2010.04.22 add kaki 粗利益率は％表示とする。
		var sgrossMarginRate = $("#grossMarginRate").val()+"%";
		 $("#grossMarginRate").val(sgrossMarginRate);
	}

	// [消費税]
	// 	　　外税の場合（【自社】課税区分＝外税伝票計または外税締単位）
	// 		= [金額合計]×消費税率
	//      上記以外は、消費税を表示しない
	//      = 0（表示は空）
	var ctaxPriceTotal = 0;

	// 課税区分(税転嫁)
	var taxTypeExclude = true;
	if(${mineDto.taxCategory} == '<%=CategoryTrns.TAX_SHIFT_CATEGORY_SLIP_TOTAL%>'
		|| ${mineDto.taxCategory} == '<%=CategoryTrns.TAX_SHIFT_CATEGORY_CLOSE_THE_BOOKS%>'){
		taxTypeExclude = true;
	}else{
		taxTypeExclude = false;
	}

	// 現在の税率
    // 税率は％表記なので100.0で割る
	if(taxTypeExclude){
		// 外税
		if(hasChanged){
			var taxRate = $("#ctaxRate").val() / 100.0;
			ctaxPriceTotal = sumRetailPrice*taxRate;
		}
	}else{
		// 内税
		ctaxPriceTotal = 0;
	}

	$("#ctaxPriceTotal").val(ctaxPriceTotal);
	SetBigDecimalScale_Obj($("#ctaxPriceTotal"));

	// 2014/6/19
	// 端数処理を行った後の消費税を再設定する
	ctaxPriceTotal = _Number(_rmv_cunit($("#ctaxPriceTotal").val()));

	// [伝票合計]	= [金額合計]＋[消費税]
	var estimateTotal = sumRetailPrice+ctaxPriceTotal;
	$("#estimateTotal").val(estimateTotal);
	SetBigDecimalScale_Obj($("#estimateTotal"));

	// [原価合計]（画面には表示しないがDBに登録する）
	$("#costTotal").val(sumCost);
	SetBigDecimalScale_Obj($("#costTotal"));
}


//顧客検索ダイアログ呼び出し
function customerSearch(){
	openSearchCustomerDialog('customer', customerCallBack);
	$("#"+'customer'+"_customerCode").val($("#customerCode").val());
}

//顧客先情報フォームIDリスト
var CustomerInfosIDList = new Array(
		"customerCode"
		,"customerName"
		,"submitPreCategory"
		,"taxFractCategory"
		,"priceFractCategory"
		,"customerRemarks"
		,"customerCommentData"
		,"salesCmCategory"
);
var CustomerInfosDBList = new Array(
		"customerCode"
		,"customerName"
		,"customerPcPreCategory"
		,"taxFractCategory"
		,"priceFractCategory"
		,"remarks"
		,"commentData"
		,"salesCmCategory"
);

// 顧客情報を画面に設定する
function customerCallBack(id, map) {
	setCustomerInfos( map );
}

//顧客コード変更
function ChangeCustomerCode(){
	//仕入先情報取得
	GetCustomerInfos();
}
//顧客情報の取得
function GetCustomerInfos(){
	if($("#customerCode").attr("value")==""){
		InitCustomerInfos();
		return;
	}
	var label = '<bean:message key="labels.customerCode" />';
	var data = new Object();
	data["customerCode"] = $("#customerCode").attr("value");
	asyncRequest(
		contextRoot + "/ajax/commonCustomer/getCustomerInfoByCustomerCode",
		data,
		function(data) {
			if(data==""){
				alert('<bean:message key="errors.notExist" arg0="'+label+'" />');
				InitCustomerInfosNotCode();
			}else{
				var value = eval("(" + data + ")");
				setCustomerInfos( value );
			}
		}
	);
}


//顧客情報の初期化
function InitCustomerInfos(){
	for(var i in CustomerInfosIDList){
		$("#"+ CustomerInfosIDList[i]).attr("value","");
	}

	InitCustomerInfosCommon();

}
//顧客情報の初期化（顧客コードは残す）
function InitCustomerInfosNotCode(){
	for(var i in CustomerInfosIDList){
		if(CustomerInfosIDList[i]=="customerCode"){
			continue;
		}
		if(CustomerInfosIDList[i]=="customerPcPreCategory"){
			$("#"+ CustomerInfosIDList[i]).attr("value","<%=CategoryTrns.PREFIX_SAMA%>");
			continue;
		}
		$("#"+ CustomerInfosIDList[i]).attr("value","");
	}
//	$("#submitName").attr("value","");
	$("#submitPreCategory").attr("value","<%=CategoryTrns.PREFIX_SAMA%>");

	InitCustomerInfosCommon();
}

function InitCustomerInfosCommon(){
	// 端数処理を変数に設定
	$("#taxFractCategory").val(${mineDto.taxFractCategory});
	$("#priceFractCategory").val(${mineDto.priceFractCategory});
	applyNumeralStyles(true);

	// 変更フラグＯＮ
	changeFlag = true;

}

// 顧客情報の設定
function setCustomerInfos( map ){

	// 取引停止の場合
	if(map["customerRoCategory"] == '<%=CategoryTrns.RECIEVE_ORDER_STOP%>'){
		alert('<bean:message key="alert.orderStop" />');
	}

	// 入金遅延の場合
	if(map["customerRoCategory"] == '<%=CategoryTrns.RECIEVE_ORDER_DEPOSIT_LATE%>'){
		alert('<bean:message key="alert.orderDepositLate" />');
	}


	for(var i in CustomerInfosIDList){

		$("#"+ CustomerInfosIDList[i]).attr("value",map[CustomerInfosDBList[i]]);
	}

	applyNumeralStyles(true);

	// [顧客名]＋[部署名]＋[担当者]＋すでに入力されている提出先名
	var name =  map["customerName"]     + " " +
				map["customerDeptName"] + " " +
				map["customerPcName"]   + " " +
				$("#submitName").attr("value");
	$("#submitName").attr("value",name);

	$("#salesCmCategory").attr("value",map["salesCmCategory"]);
	$("#salesCmCategory").val(map["salesCmCategory"]);

	// 変更フラグＯＮ
	changeFlag = true;

}


// 納期または出荷日ダイアログを開く
function showDeliveryInfo(){
	openCategoryDialog("category",setDeliveryInfo,'<%=Categories.DELIVERY_INFO_CATEGORY%>',"radio", null);
}

//納期または出荷日に選択した項目を設定する
function setDeliveryInfo(result){
	$("#deliveryInfo").attr("value",result);
	// 変更フラグＯＮ
	changeFlag = true;
}

//見積条件ダイアログを開く
function showEstimateCondition(){
	openCategoryDialog("category",setEstimateCondition,'<%=Categories.ESTIMATE_CONDITION_CATEGORY%>',"radio", null);
}

//納期または出荷日に選択した項目を設定する
function setEstimateCondition(result){
	$("#estimateCondition").attr("value",result);
	// 変更フラグＯＮ
	changeFlag = true;
}


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
-->
</script>
</head>
<body onload="init()">

<%-- ページヘッダ領域 --%>
<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

<%-- メニュー領域 --%>
<jsp:include page="/WEB-INF/view/common/menubar.jsp">
	<jsp:param name="PARENT_MENU_ID" value="0002"/>
	<jsp:param name="MENU_ID" value="0200"/>
</jsp:include>

<%-- メイン機能領域 --%>
<div id="main_function">

	<!-- タイトル -->
	<span class="title"><bean:message key='titles.inputEstimate'/></span>

	<div class="function_buttons">
		<button type="button" id="btnF1" tabindex="2000" onclick="onF1();" onkeypress="onF1();">F1<br>初期化</button>
		<button	type="button" id="btnF2" tabindex="2001" onclick="onF2();" onkeypress="onF2();" ${!newData && menuUpdate ?"":"disabled"}>F2<br>削除</button>
		<button	type="button" id="btnF3" tabindex="2002" onclick="onF3();" onkeypress="onF3();" ${menuUpdate?"":"disabled"}>F3<br><c:if test="${newData}">登録</c:if><c:if test="${!newData}">更新</c:if></button>
		<button	type="button" id="btnF4" tabindex="2003" onclick="onF4();" onkeypress="onF4();" ${newData?"disabled":""}>F4<br>PDF</button>
		<button	type="button" tabindex="2004" disabled="disabled">F5<br>&nbsp;</button>
		<button	type="button" tabindex="2005" disabled="disabled">F6<br>&nbsp;</button>
		<button	type="button" tabindex="2006" disabled="disabled">F7<br>&nbsp;</button>
		<button	type="button" tabindex="2007" disabled="disabled">F8<br>&nbsp;</button>
		<button	type="button" tabindex="2008" disabled="disabled">F9<br>&nbsp;</button>
		<button	type="button" tabindex="2009" disabled="disabled">F10<br>&nbsp;</button>
		<button	type="button" tabindex="2010" disabled="disabled">F11<br>&nbsp;</button>
		<button	type="button" tabindex="2011" disabled="disabled">F12<br>&nbsp;</button>
	</div>
	<br><br><br>

	<s:form onsubmit="return false;">
		<html:hidden property="deleteLineIds"  styleId="deleteLineIds"/>
		<html:hidden property="menuUpdate" />
		<html:hidden property="userId" styleId="userId" />
		<html:hidden property="submitPre" styleId="submitPre" />
		<html:hidden property="updDatetm"/>
		<html:hidden property="updUser"/>
		<html:hidden property="taxFractCategory" styleId="taxFractCategory" />
		<html:hidden property="priceFractCategory" styleId="priceFractCategory" />
		<html:hidden property="priceFractCategory" styleId="priceFractCategory"/>
		<html:hidden property="taxFractCategory" styleId="taxFractCategory"/>
		<html:hidden property="salesCmCategory" styleId="salesCmCategory" />

		<div id="errors" style="padding-top: 5px; padding-left: 20px">
			<html:errors/>
		</div>
		<span id="ajax_errors"></span>
		<div id="messages" style="padding-left: 20px;color: blue;">
			<html:messages id="msg" message="true">
				<bean:write name="msg" ignore="true"/><br>
			</html:messages>
		</div>

		<div class="function_forms">
		    <div class="form_section_wrap">
			    <div class="form_section">
			    	<div class="section_title">
						<span>見積伝票情報</span>
			            <button class="btn_toggle" />
					</div><!-- /.section_title -->

					<div id="order_section" class="section_body">
					<table id="order_info" class="forms" summary="見積伝票情報">
						<tr>
							<th><div class="col_title_right_req">見積番号<bean:message key='labels.must'/></div></th>
							<td><html:text property="estimateSheetId"  styleId="estimateSheetId" styleClass="" style="width: 135px; ime-mode: disabled;" readonly="false" tabindex="100" maxlength="10"  onfocus="this.curVal=this.value;" onblur="if((this.curVal == '') || ((this.curVal != '')&&(this.curVal!=this.value))){findSlip();}"/></td>
							<th><div class="col_title_right_req">見積日<bean:message key='labels.must'/></div></th>
							<td><html:text property="estimateDate" styleId="estimateDate" styleClass="date_input" style="width: 135px;ime-mode:disabled;" maxlength="10"  tabindex="101"/></td>
							<th><div class="col_title_right">納期または出荷日</div></th>
							<td>
								<html:text property="deliveryInfo" styleId="deliveryInfo" style="width: 285px;" maxlength="120" tabindex="102" />
								<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" onclick="showDeliveryInfo()" onkeypress="showDeliveryInfo()" tabindex="103" />
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right">有効期限</div></th>
							<td><html:text property="validDate" styleClass="date_input" style="width: 135px;ime-mode:disabled;" maxlength="10" tabindex="104" /></td>
							<th><div class="col_title_right">入力担当者</div></th>
							<td><html:text property="userName" styleClass="c_disable" readonly="true"  tabindex="105" /></td>
							<th><div class="col_title_right">件名</div></th>
							<td><html:text property="title" style="width: 285px" maxlength="100" tabindex="106" /></td>
						</tr>
						<tr>
							<th><div class="col_title_right">納入先</div></th>
							<td colspan="5">
								<html:text property="deliveryName" style="width: 800px" maxlength="60" tabindex="107" />
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right">見積条件</div></th>
							<td colspan="5">
								<html:textarea property="estimateCondition" styleId="estimateCondition" style="width: 800px" tabindex="108" />
								<html:image src="${f:url('/images//customize/btn_search.png')}" style="cursor: pointer;" onclick="showEstimateCondition()" onkeypress="showEstimateCondition()" tabindex="109" />
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right">消費税率</div></th>
							<td colspan="5">
								<html:select property="ctaxRate" styleId="ctaxRate" tabindex="109" style="width: 135px;"  onchange="changeTaxRate()">
								    <html:options collection="ctaxRateList" property="value" labelProperty="label"/>
								</html:select>&nbsp;％
							</td>
						</tr>
						<html:hidden name="inputEstimateForm" property="newData" />
					</table>
					</div><!-- /.section_body -->
				</div><!-- /.form_section -->
			</div><!-- /.form_section_wrap -->

		    <div class="form_section_wrap">
			    <div class="form_section">
			    	<div class="section_title">
						<span>顧客情報</span>
			            <button class="btn_toggle" />
					</div><!-- /.section_title -->

					<div id="order_section" class="section_body">
					<table id="customer_info1" class="forms" summary="顧客情報">
						<tr>
							<th><div class="col_title_right_req">提出先名<bean:message key='labels.must'/></div></th>
							<td><html:text property="submitName" style="width: 430px" styleId="submitName" maxlength="60" tabindex="200"  /></td>

							<th><div class="col_title_right">提出先敬称</div></th>
							<td>
								<html:select property="submitPreCategory" styleId="submitPreCategory" tabindex="201" >
									<html:option value="" ></html:option>
								    <html:options collection="submitPreList" property="value" labelProperty="label"/>
								</html:select>
							</td>
						</tr>
					</table>
					<table id="customer_info2" class="forms" summary="顧客情報">
						<tr>
							<th rowspan="4"><div class="col_title_right">既存顧客</div></th>
							<th><div class="col_title_right">顧客コード</div></th>
							<td colspan="3">
								<html:text property="customerCode" styleId="customerCode" style="width: 130px;ime-mode:disabled;"
									onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ ChangeCustomerCode(); }" tabindex="202" />
								<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" onclick="customerSearch()" onkeypress="customerSearch()" tabindex="203" />
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right">顧客名</div></th>
							<td colspan="3">
								<html:text property="customerName" styleId="customerName" styleClass="c_disable" style="width: 400px;" readonly="true" tabindex="204" />
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right">備考</div></th>
							<td colspan="3">
								<html:text property="customerRemarks" styleId="customerRemarks" styleClass="c_disable" style="width: 700px;" readonly="true" tabindex="205" />
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right">コメント</div></th>
							<td colspan="3">
								<html:text property="customerCommentData" styleId="customerCommentData" styleClass="c_disable" style="width: 700px;" readonly="true" tabindex="206" />
							</td>
						</tr>
					</table>
					</div><!-- /.section_body -->
				</div><!-- /.form_section -->
			</div><!-- /.form_section_wrap -->

		    <div class="form_section_wrap">
			    <div class="form_section">
			    	<div class="section_title">
						<span>摘要</span>
			            <button class="btn_toggle" />
					</div><!-- /.section_title -->

					<div id="order_section" class="section_body">
						<table id="bikou" class="forms" summary="摘要">
							<tr>
								<th><div class="col_title_right">摘要</div></th>
								<td colspan="5">
									<html:textarea property="remarks" style="width: 800px; height: 40px"  tabindex="300" />

								</td>
							</tr>
							<tr>
								<th><div class="col_title_right">メモ</div></th>
								<td colspan="5">
									<html:textarea property="memo" style="width: 800px; height: 40px"  tabindex="301" />
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
					<col span="1" style="width: 30%">
					<col span="1" style="width: 5%">
					<col span="1" style="width: 8%">
					<col span="1" style="width: 8%">
					<col span="1" style="width: 21%">
					<col span="1" style="width: 10%">
				</colgroup>
				<thead>
					<tr>
						<th rowspan="2" class="rd_top_left" style="height: 30px; width: 30px;">No</th>
						<th rowspan="2" style="height: 30px;">商品コード※</th>
						<th rowspan="2" style="height: 30px;">商品名・摘要</th>
						<th rowspan="2" style="height: 30px;">数量※</th>
						<th style="height: 30px;">仕入単価</th>
						<th style="height: 30px;">売上単価※</th>
						<th rowspan="2" style="height: 30px;">備考</th>
						<th rowspan="2" class="rd_top_right" style="height: 30px;">&nbsp;</th>
					</tr>
					<tr>
						<th style="height: 30px;">仕入金額</th>
						<th style="height: 30px;">売価金額※</th>
					</tr>
				</thead>
				<tbody id="tbodyLine">
					<% int lineTab = 400; %>
					<c:forEach var="estimateLineTrnDtoList" items="${estimateLineTrnDtoList}" varStatus="status">

						<c:if test="${estimateLineTrnDtoList.lineNo > 0}">
						<tr id="trLine${status.index}">

							<!-- No -->
							<td id="tdNo${status.index}">
								<div class="box_1of1">
									<c:out value="${estimateLineTrnDtoList.lineNo}" />
								</div>
							</td>
							<td style="display: none;">
								<html:hidden name="estimateLineTrnDtoList" property="lineNo" indexed="true" styleId="estimateLineTrnDtoList[${status.index}].lineNo" />
								<html:hidden name="estimateLineTrnDtoList" property="estimateLineId" indexed="true" styleId="estimateLineTrnDtoList[${status.index}].estimateLineId" />
								<html:hidden name="estimateLineTrnDtoList" property="customerPcode" indexed="true" styleId="estimateLineTrnDtoList[${status.index}].customerPcode" />
								<html:hidden name="estimateLineTrnDtoList" property="roMaxNum" indexed="true" styleId="estimateLineTrnDtoList[${status.index}].roMaxNum" />
								<html:hidden name="estimateLineTrnDtoList" property="possibleDrawQuantity" indexed="true" styleId="estimateLineTrnDtoList[${status.index}].possibleDrawQuantity" />
								<html:hidden name="estimateLineTrnDtoList" property="stockCtlCategory" indexed="true" styleId="estimateLineTrnDtoList[${status.index}].stockCtlCategory" />
							</td>

							<!-- 商品コード※ -->
							<td style="background-color: #fae4eb;">
								<div class="box_1of1" style="margin: 5px;">
									<html:text name="estimateLineTrnDtoList" property="productCode" indexed="true" styleId="estimateLineTrnDtoList[${status.index}].productCode"  styleClass="goods_code c_referable" style="width: 160px; ime-mode:disabled;" maxlength="20" tabindex="<%=String.valueOf(lineTab++) %>" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' styleId="productCodeImg${status.index}" style="width: auto; vertical-align: middle; cursor: pointer;" tabindex="<%=String.valueOf(lineTab++) %>" />
								</div>
							</td>

							<!-- 商品名・摘要 -->
							<td>
								<div class="box_1of1" style="margin:5px;">
									<html:textarea name="estimateLineTrnDtoList" property="productAbstract" indexed="true" styleId="estimateLineTrnDtoList[${status.index}].productAbstract" style="width: 100%; height: 60px;"  tabindex="<%=String.valueOf(lineTab++) %>" />
								</div>
							</td>

							<!-- 数量※ -->
							<td style="background-color: #fae4eb;">
								<div class="box_1of2" style="border-bottom: 0;">
									<html:text name="estimateLineTrnDtoList" property="quantity" indexed="true" styleId="estimateLineTrnDtoList[${status.index}].quantity"  styleClass="numeral_commas" style="width: 62px;ime-mode:disabled;" maxlength="6" tabindex="<%=String.valueOf(lineTab++) %>" />
								</div>
								<div class="box_2of2">
									<button type="button" id="stockBtn${status.index}" tabindex="<%=String.valueOf(lineTab++) %>" class="btn_list_action">在庫</button>
								</div>
							</td>

							<!-- 仕入単価/仕入金額 -->
							<td>
								<div class="box_1of2">
									<html:text name="estimateLineTrnDtoList" property="unitCost" indexed="true" styleId="estimateLineTrnDtoList[${status.index}].unitCost"  styleClass="numeral_commas c_disable" readonly="true" style="width: 75px;ime-mode:disabled;" maxlength="9" tabindex="<%=String.valueOf(lineTab++) %>" />
								</div>
								<div class="box_2of2">
									<html:text name="estimateLineTrnDtoList" property="cost" indexed="true" styleId="estimateLineTrnDtoList[${status.index}].cost" styleClass="numeral_commas c_disable" readonly="true" style="width: 75px;ime-mode:disabled;" maxlength="9" tabindex="<%=String.valueOf(lineTab++) %>" />
								</div>
							</td>

							<!-- 売上単価※/売価金額※ -->
							<td style="background-color: #fae4eb;">
								<div class="box_1of2">
									<html:text name="estimateLineTrnDtoList" property="unitRetailPrice" indexed="true" styleId="estimateLineTrnDtoList[${status.index}].unitRetailPrice"  styleClass="numeral_commas" style="width: 75px;ime-mode:disabled;" maxlength="9" tabindex="<%=String.valueOf(lineTab++) %>" />
								</div>
								<div class="box_2of2">
									<html:text name="estimateLineTrnDtoList" property="retailPrice" indexed="true" styleId="estimateLineTrnDtoList[${status.index}].retailPrice" styleClass="numeral_commas" style="width: 75px;ime-mode:disabled;" maxlength="9" tabindex="<%=String.valueOf(lineTab++) %>" />
								</div>
							</td>

							<!-- 備考 -->
							<td>
								<div class="box_1of1" style="margin:5px;">
									<html:textarea name="estimateLineTrnDtoList" property="remarks" indexed="true" styleId="estimateLineTrnDtoList[${status.index}].remarks" style="width: 100%; height: 60px;"  tabindex="<%=String.valueOf(lineTab++) %>" />
								</div>
							</td>

							<!-- ボタン -->
							<td style="text-align:right;">
								<div class="box_1of2">
									<button id="deleteBtn${status.index}" type="button" alt="この行を削除" style="width:80px;" ${menuUpdate?"":"disabled"}  tabindex="<%=String.valueOf(lineTab++) %>" class="btn_list_action">削除</button><br>
								</div>
								<div class="box_2of2">
									<button id="copyBtn${status.index}" type="button" alt="前行から複写" style="width:80px;" ${!status.first && menuUpdate ?"":"disabled"}  tabindex="<%=String.valueOf(lineTab++) %>" class="btn_list_action">前行複写</button>
								</div>
							</td>
						</tr>
						</c:if>
					</c:forEach>

					<!-- 追加ボタン -->
					<tr id="trAddLine">
						<td style="height: 60px; text-align: center" colspan="9" class="rd_bottom_left rd_bottom_right">
							<button type="button" id="addRowBtn" alt="最後に空行を追加" style="width:80px;" onclick="addRow();" onkeypress="addRow();" ${menuUpdate?"":"disabled"} tabindex="<%=String.valueOf(lineTab++) %>">
								<img alt="行追加" border="none" src="${f:url('/images/customize/btn_line_add.png')}"  width="31" height="33">
							</button>
						</td>
					</tr>
				</tbody>
			</table>
			</div>

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
						<html:hidden property="costTotal" styleId="costTotal" styleClass="numeral_commas" />
						<td class="rd_bottom_left" style="height: 100px;" >
							<html:text property="grossMargin" styleId="grossMargin" size="10" readonly="true"
								style="width: 100%; text-align:center; background-color: #FFFFFF; border-style: none;font-weight: bold;color: #555555; font-size: 24px;"  styleClass="BDCyen yen_value" />
						</td>
						<td>
							<html:text property="grossMarginRate" styleId="grossMarginRate" size="10" readonly="true"
								style="width: 100%; text-align:center; background-color: #FFFFFF; border-style: none;font-weight: bold;color: #555555; font-size: 24px;"  styleClass="numeral_commas" />
						</td>
						<td>
							<html:text property="retailPriceTotal" styleId="retailPriceTotal" size="10" readonly="true"
								style="width: 100%; text-align:center; background-color: #FFFFFF; border-style: none;font-weight: bold;color: #555555; font-size: 24px;"  styleClass="BDCyen yen_value" />
						</td>
						<td>
							<html:text property="ctaxPriceTotal" styleId="ctaxPriceTotal" size="10" readonly="true"
								style="width: 100%; text-align:center; background-color: #FFFFFF; border-style: none;font-weight: bold;color: #555555; font-size: 24px;"  styleClass="BDCtax yen_value" />
						</td>
						<td class="rd_bottom_right">
							<html:text property="estimateTotal" styleId="estimateTotal" size="10"  readonly="true"
								style="width: 100%; text-align:center; background-color: #FFFFFF; border-style: none;font-weight: bold;color: #555555; font-size: 24px;"  styleClass="BDCyen yen_value" />
						</td>
					</tr>
				</table>
			</div>
			</div>
			<div style="width: 1160px; text-align: center; margin-top: 10px;">
				<button type="button" id="btnF3btm" tabindex="1999" style="width:260px;height:51px;" class="btn_medium" onclick="onF3();" onkeypress="onF3();" ${menuUpdate?"":"disabled"}>
					<c:if test="${newData}">
						<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.register'/></span><%// 登録 %>
					</c:if>
					<c:if test="${!newData}">
						<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.renew'/></span><%// 更新 %>
					</c:if>
				</button>
			</div>
		</div>
	</s:form>

	<form name="PDFOutputForm" action="${f:url('/estimate/outputEstimateSheetSingle/pdf')}" style="display: none;" method="POST">
	</form>
	</div>

	<%-- ページフッター領域 --%>
	<%@ include file="/WEB-INF/view/common/footer.jsp" %>
</body>
</html>
