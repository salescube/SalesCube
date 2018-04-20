<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="jp.co.arkinfosys.common.Categories"%>
<%@page import="jp.co.arkinfosys.common.Constants"%>
<%@page import="jp.co.arkinfosys.common.CategoryTrns" %>
<%@page import="jp.co.arkinfosys.form.AbstractSlipEditForm"%>
<html lang="ja">
<head>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<title><bean:message key='titles.system'/>　売上入力</title>

	<script type="text/javascript" src="${f:url('/scripts/dialogs.js')}"></script>
	<script type="text/javascript" src="${f:url('/scripts/salescommon.js')}"></script>
	<script type="text/javascript">

	<!--
	var MAX_LINE_ROW_COUNT = <%= AbstractSlipEditForm.MAX_LINE_SIZE %>;
	var MAIN_FORM_NAME = "sales_inputSalesActionForm";
	var maxIndex = 0;
	// 明細行の確保領域
	var trCloneBase = null;
	var trCloneBaseIndex = 0;
	// 税転嫁の確保領域
	var catArray = {};
	var reportType = "pdf";
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

	var changeFlag = false;

	 var SalesCatList;

	//ページ読込時の動作
	function init() {

    	// 取引区分区分　リストを取得
    	SalesCatList = {
    	<c:forEach var="sList" varStatus="s" items="${salesCmCategoryList}">
    		"${sList.value}":"${sList.label}"
    		<c:if test="${!s.last}">
       			,
    		</c:if>
    	</c:forEach>
    	};

		// 明細行のIndex管理
		var tBodyLine = $("#tbodyLine");
		maxIndex = tBodyLine.children().size() - 2;

		// 税転嫁の確保領域
		catArray["#taxShiftCategory"] = $("#taxShiftCategory").clone(true);
		// 支払区分の確保領域
		catArray["#cutoffGroupCategory"] = $("#cutoffGroupCategory").clone(true);
		// 取引区分の確保領域
		catArray["#salesCmCategory"] = $("#salesCmCategory").clone(true);
		// 敬称の確保領域
		catArray["#deliveryPcPreCategory"] = $("#deliveryPcPreCategory").clone(true);

		$("input[type='text']").add("input[type='checkbox']:not('.dummy')").add("select").add("textarea").bind("change", changeOn );

		// 初期状態は空欄にする
		catrgoryListCtrl("#taxShiftCategory","${taxShiftCategory}");
		catrgoryListCtrl("#cutoffGroupCategory","${cutoffGroupCategory}");
		catrgoryListCtrl("#salesCmCategory","${salesCmCategory}");
		catrgoryListCtrl("#deliveryPcPreCategory","${deliveryPcPreCategory}");

		// 取引区分リスト整理
		SalescatrgoryListAdd("#salesCmCategory","${salesCmCategory}");

		// 明細行の項目にイベントをバインド
		for(var i=0; i<=maxIndex; i++) {
			$("#salesLineList\\[" + i + "\\]\\.productCode").bind("focus", {index: i}, function(e){ this.curVal=this.value; });
			$("#salesLineList\\[" + i + "\\]\\.productCode").bind("blur", {index: i}, function(e){ if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); changeProductCode(e); } });

			$("#salesLineList\\[" + i + "\\]\\.quantity").bind("focus", {index: i}, function(e){ this.curVal=this.value; });
			$("#salesLineList\\[" + i + "\\]\\.quantity").bind("blur", {index: i}, function(e){ if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); changeQuantity(e); } });
			$("#salesLineList\\[" + i + "\\]\\.unitRetailPrice").bind("focus", {index: i}, function(e){ this.curVal=this.value; });
			$("#salesLineList\\[" + i + "\\]\\.unitRetailPrice").bind("blur", {index: i}, function(e){ if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); changeUnitRetailPrice(e); } });
			$("#salesLineList\\[" + i + "\\]\\.unitCost").bind("focus", {index: i}, function(e){ this.curVal=this.value; });
			$("#salesLineList\\[" + i + "\\]\\.unitCost").bind("blur", {index: i}, function(e){ if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); changeUnitCost(e); } });
			$("#salesLineList\\[" + i + "\\]\\.cost").bind("focus", {index: i}, function(e){ this.curVal=this.value; });
			$("#salesLineList\\[" + i + "\\]\\.cost").bind("blur", {index: i}, function(e){ if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); changeCost(e); } });
			$("#salesLineList\\[" + i + "\\]\\.retailPrice").bind("focus", {index: i}, function(e){ this.curVal=this.value; });
			$("#salesLineList\\[" + i + "\\]\\.retailPrice").bind("blur", {index: i}, function(e){ if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); changeRetailPrice(e); } });

			$("#salesLineList\\[" + i + "\\]\\.deliveryProcessCategory").bind("change", {index: i}, changeCategory);
			$("#productCodeImg" + i).bind("click", {index: i}, openProductSearchDialog);
			$("#stockBtn" + i).bind("click", {index: i}, showStockInfo);
			$("#deleteBtn" + i).bind("click", {index: i}, deleteRow);
			$("#copyBtn" + i).bind("click", {index: i}, copyRow);
		}

		// 端数処理初期設定
		applyNumeralStyles();


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
				function() {
					this.selectedIndex = 0;
				}
		);

		// コンボの調整
		if( $("#customerName").val() != "" ){
			catrgoryListCtrl("#taxShiftCategory",$("#taxShiftCategory").val());
			catrgoryListCtrl("#salesCmCategory",$("#salesCmCategory").val());
			catrgoryListCtrl("#cutoffGroupCategory",$("#cutoffGroupCategory").val());
			catrgoryListCtrl("#deliveryPcPreCategory",$("#deliveryPcPreCategory").val());
			// 取引区分リスト整理
			SalescatrgoryListAdd("#salesCmCategory",$("#salesCmCategory").val());
			$("#custsalesCmCategory").val($("#salesCmCategory").val());
		}

		if( $("#roSlipId").val() != "" ){
			for(var i=0; i<=maxIndex; i++) {
				// 明細行にて、伝票呼出された行は変更不可
				if( $("#salesLineList\\["+i+"\\]\\.roLineId").val() != ""){
					$("#copyBtn"+i).attr("disabled", true);
					$("#salesLineList\\["+i+"\\]\\.productCode").attr("readOnly", "true");
					$("#salesLineList\\["+i+"\\]\\.productCode").addClass("c_disable");
					$("#productCodeImg"+i).attr("disabled", true);

					if($("#salesSlipId").val() == "" ){
						// 明細行にて、伝票呼出された行は金額を再計算

						calcQuantity( i );

						// calcUnitRetailPrice( i );
						// calcUnitCost(i);
						// calcGm( i );
						// calcCTax( i );
					}
				}
			}
			if( maxIndex == 0 ){
				$("#deleteBtn0").attr("disabled", true);
			}

			$("#customerCode").attr("readOnly", "true");
			$("#customerCode").addClass("c_disable");
			$("#customerCodeImg").attr("disabled", true);
			$("#addLine").attr("disabled", false);

			// 伝票呼出された場合は、再計算
			if($("#salesSlipId").val() == "" ) {
				calcTotal();
			}
		}

		// 伝票番号が無い場合には削除は無効
		if( $("#salesSlipId").val() == "" ){
			$("#btnF2").attr("disabled", true);
		}
		else{
			// 編集の場合は、取引区分は変更不可
			$("#salesCmCategory").attr("readOnly", "true");
			$("#salesCmCategory").addClass("c_disable");
		}
		// 初期フォーカス
		if( $("#salesSlipId").val() != "" ){
			$("#salesSlipId").attr("readOnly", "true");
			$("#salesSlipId").addClass("c_disable");
			$("#salesSlipId").css("background-color", "#CCCCCC");
			$("#salesSlipId").css("border-top", "2px solid #AEAEAE");
			// 受注番号も変更不可とする。
			$("#roSlipId").attr("readOnly", "true");
			$("#roSlipId").addClass("c_disable");
			$("#roSlipId").css("background-color", "#CCCCCC");
			$("#roSlipId").css("border-top", "2px solid #AEAEAE");
			$("#salesDate").focus();
		}else{
			$("#salesSlipId").focus();
		}

		// 消費税の初期値を設定する
		$("#taxRate").val($("#ctaxRate").val());


		$("#customerCode").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁

	}


	function changeOn(){
		changeFlag = true;
	}

	function applyNumeralStyles(){
		// 明細行のIndex管理
		var maxIndex = $("#tbodyLine").get(0).children.length-1;

		for(var i=0; i<=maxIndex; i++) {
			// 丸めの少数桁と端数処理を設定する
			applyNumeralStylesToObj(quantityCategory,quantityAlignment,$("#salesLineList\\[" + i + "\\]\\.quantity"));
			applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#salesLineList\\[" + i + "\\]\\.unitCost"));
			applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#salesLineList\\[" + i + "\\]\\.unitRetailPrice"));
			applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#salesLineList\\[" + i + "\\]\\.cost"));
			applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#salesLineList\\[" + i + "\\]\\.retailPrice"));
		}

		// 粗利益 gmTotal
		applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#gmTotal"));
		// 粗利益率 gmTotalPer
		applyNumeralStylesToObj(rateCategory,rateAlignment,$("#gmTotalPer"));
		// 金額合計 priceTotal
		applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#priceTotal"));
		// 消費税 ctaxPriceTotal
		applyNumeralStylesToObj($("#taxFractCategory").val(),priceAlignment,$("#ctaxPriceTotal"));
		// 伝票合計 total
		applyNumeralStylesToObj($("#priceFractCategory").val(),priceAlignment,$("#total"));

		// 下部の合計欄を計算する
		reCalc();

		// カンマをつける
		_after_load($(".numeral_commas"));

	}

	// 初期化
	function onF1(){
		if(confirm('<bean:message key="confirm.init" />')){
			showNowSearchingDiv();
			window.location.doHref('${f:url("/sales/inputSales")}');
			$("#salesSlipId").focus();
		}
	}

	//売上番号直接入力
	function findSlip(){
		if( $("#salesSlipId").val() != "" ){
			showNowSearchingDiv();
			$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/sales/inputSales/load")}');
			$("form[name='" + MAIN_FORM_NAME + "']").submit();
		}
	}

	// 伝票複写（受注番号入力）
	function findCopy(){
		if( ($("#roSlipId").val() != "" )&&(!$("#roSlipId").attr("readOnly"))){
			if(confirm("<bean:message key='confirm.copyslip'/>")){

				$("#salesSlipId").val("");
				$("#salesSlipId").attr("readOnly", "true");
				$("#salesSlipId").addClass("c_disable");


				$("#copySlipName").val( "RORDER" );
				$("#copySlipId").val( $("#roSlipId").val() );
				$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/sales/inputSales/copy")}');
				$("form[name='" + MAIN_FORM_NAME + "']").submit();
			}
			else{
				$("#roSlipId").val("");
			}
		}
	}

	// 削除
	function onF2(){
		if(confirm('<bean:message key="confirm.delete" />')){
			_before_submit($(".numeral_commas"));
			$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/sales/inputSales/delete")}');
			showNowSearchingDiv();
			$("form[name='" + MAIN_FORM_NAME + "']").submit();
		}
	}

	// 登録
	function onF3(){
		if(confirm('<bean:message key="confirm.insert" />')){
			_before_submit($(".numeral_commas"));
			$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/sales/inputSales/upsert")}');
			showNowSearchingDiv();
			$("form[name='" + MAIN_FORM_NAME + "']").submit();
		}
	}
	//Excel
	function onF4() {
		if(changeFlag){
			alert("<bean:message key='infos.valid.print' arg0='Excel'/>");
		}else{
			reportType = "excel";
			outputDialog();
		}
	}
	//PDF
	function onF5() {
		if(changeFlag){
			alert("<bean:message key='infos.valid.print' arg0='PDF'/>");
		}else{
			reportType = "pdf";
			outputDialog();
		}
	}
	//伝票複写
	function onF6() {
		if(confirm("<bean:message key='confirm.copyslip'/>")){
			openCopySlipDialog('0400', 'copySlip3', copySlipCallback);
		}
	}

	// ================================================================================================
	// 行削除
	function deleteRow(event){
		var i, lineNo, lineSize;
		var trId, id;
		var index;

		index = event.data.index;

		if(confirm('<bean:message key="confirm.line.delete" />')){
			// 行を削除する
			lineNo = parseInt($("#salesLineList\\["+index+"\\]\\.lineNo").get(0).value)-1;
	        if ($("#salesLineList\\[" + index + "\\]\\.salesLineId").val() != "") {
				var ids = $("#deleteLineIds").val();
				if(ids.length > 0){
					ids += ",";
				}
				$("#deleteLineIds").val(ids + $("#salesLineList\\[" + index + "\\]\\.salesLineId").val());
	        }

			$("#tbodyLine").get(0).deleteRow(lineNo);
			// 行番号を調整する
			lineSize = $("#tbodyLine").get(0).children.length-1;
			tabIdx = 1000 + (lineNo) * ${f:h(lineElementCount)} - 1;


			for(i=lineNo; i<lineSize; i++) {
				trId = $("#tbodyLine").get(0).children[i].id;
				id = trId.replace("trLine", "");
				// 行番号を振りなおす
				$("#tdNo"+id).get(0).innerHTML = i+1;
				$("#salesLineList\\["+id+"\\]\\.lineNo").get(0).value = i+1;
				// タブを振り直し
				$("#salesLineList\\["+id+"\\]\\.productCode").attr("tabindex", (++tabIdx));
				$("#productCodeImg"+id).attr("tabindex", (++tabIdx));
				$("#salesLineList\\["+id+"\\]\\.productRemarks").attr("tabindex", (++tabIdx));
				$("#salesLineList\\["+id+"\\]\\.quantity").attr("tabindex", (++tabIdx));
				$("#stockBtn"+id).attr("tabindex", (++tabIdx));
				$("#salesLineList\\["+id+"\\]\\.deliveryProcessCategory").attr("tabindex", (++tabIdx));
				$("#salesLineList\\["+id+"\\]\\.unitCost").attr("tabindex", (++tabIdx));
				$("#salesLineList\\["+id+"\\]\\.cost").attr("tabindex", (++tabIdx));
				$("#salesLineList\\["+id+"\\]\\.unitRetailPrice").attr("tabindex", (++tabIdx));
				$("#salesLineList\\["+id+"\\]\\.retailPrice").attr("tabindex", (++tabIdx));
				$("#salesLineList\\["+id+"\\]\\.remarks").attr("tabindex", (++tabIdx));
				$("#salesLineList\\["+id+"\\]\\.eadRemarks").attr("tabindex", (++tabIdx));
				$("#salesLineList\\["+id+"\\]\\.rackCodeSrc").attr("tabindex", (++tabIdx));
				$("#deleteBtn"+id).attr("tabindex", (++tabIdx));
				$("#copyBtn"+id).attr("tabindex", (++tabIdx));

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
		// 合計の再計算
		calcTotal();
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
		calcTotal();
	}

	function setElem( elmPa, name, idx, val, tabIdx ){
		var elemWork;
		elemWork = elmPa.children("#salesLineList\\[" + trCloneBaseIndex + "\\]\\." + name );
		elemWork.attr("id", "salesLineList[" + idx + "]." + name );
		elemWork.attr("name", "salesLineList[" + idx + "]." + name );
		if( val != null ){
			elemWork.val(val);
		}
		elemWork.attr("tabindex", tabIdx);
		return elemWork;
	}

	// 行追加
	function addRow(){
		var elemTr, elemTd, elemWork;
//		var elemTR, elemTD;
		var lineSize, tabIdx;
		var html;
		var cellIndex = 0;

		// 最大行数の確認
		lineSize = $("#tbodyLine").get(0).children.length-1;
		if(lineSize >= MAX_LINE_ROW_COUNT) {
			alert('<bean:message key="errors.line.maxrows" />');
			return;
		}

		// 伝票呼出された伝票の場合は、警告メッセージを表示する。
		if( $("#roSlipId").val() != "" ){
			alert('<bean:message key="warns.addrow.copy.not" />');
		}

		// ベースオブジェクトからクローンを生成
		elemTr = trCloneBase.clone(true);
		elemTr.find("select").each(
			function() {
				this.selectedIndex = 0;
			}
		);

		// 明細行のIndex管理
		var endLine = $("#tbodyLine").children("[id^='trLine']:last");
		var match = endLine.attr("id").match(/^trLine([0-9]+)$/);
		if( match ) {
			maxIndex = match[1];
		}
		maxIndex++;

		// タブインデックスの計算
		tabIdx = 1000 + (lineSize+1) * ${f:h(lineElementCount)} - 1;

		elemTr.attr("id", "trLine" + maxIndex);
		// No列の設定
		elemTd = elemTr.children(":first");
		elemTd.attr("id", "tdNo" + maxIndex);
		elemTd.html(lineSize + 1);

		// Hidden列の設定
		elemTd = elemTd.next();
		setElem( elemTd, "salesLineId", maxIndex, null );
		setElem( elemTd, "status", maxIndex, null );
		setElem( elemTd, "salesSlipId", maxIndex, null );
		setElem( elemTd, "lineNo", maxIndex, lineSize + 1 );
		setElem( elemTd, "roLineId", maxIndex, null );
		setElem( elemTd, "salesDetailCategory", maxIndex, null );
		setElem( elemTd, "customerPcode", maxIndex, null );
		setElem( elemTd, "productAbstract", maxIndex, null );
		setElem( elemTd, "unitCategory", maxIndex, null );
		setElem( elemTd, "unitName", maxIndex, null );
		setElem( elemTd, "packQuantity", maxIndex, null );
		setElem( elemTd, "taxCategory", maxIndex, null );
		setElem( elemTd, "ctaxRate", maxIndex, null );
		setElem( elemTd, "ctaxPrice", maxIndex, null );
		setElem( elemTd, "gm", maxIndex, null );
		setElem( elemTd, "setTypeCategory", maxIndex, null );
		setElem( elemTd, "roQuantity", maxIndex, null );
		setElem( elemTd, "bkQuantity", maxIndex, null );
		setElem( elemTd, "roMaxNum", maxIndex, null );
		setElem( elemTd, "possibleDrawQuantity", maxIndex, null );
		setElem( elemTd, "stockCtlCategory", maxIndex, null );

		elemTd = elemTd.next();
		elemWork = setElem( elemTd.children(), "productCode", maxIndex, null, (++tabIdx) );
		elemWork.unbind("focus");
		elemWork.bind("focus", {index: maxIndex}, function(e){ this.curVal=this.value; });
		elemWork.unbind("blur");
		elemWork.bind("blur", {index: maxIndex}, function(e){ if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); changeProductCode(e); } });

		elemWork = elemTd.children().children("#productCodeImg0");
		elemWork.attr("id", "productCodeImg" + maxIndex);
		elemWork.attr("tabindex", (++tabIdx));
		elemWork.bind("click", {index: maxIndex}, openProductSearchDialog);

		elemTd = elemTd.next();
		var elemWork;
		elemWork = elemTd.children(":first");
		elemWork.attr("id", "productAbstract" + maxIndex );
		elemWork.text("");
		//setElem( elemTd, "productAbstract", maxIndex, "", null );
		setElem( elemTd.children(), "productRemarks", maxIndex, "", (++tabIdx) );

		elemTd = elemTd.next();
		elemWork = setElem( elemTd.children(), "quantity", maxIndex, null , (++tabIdx));
//		elemWork.bind("change", {index: maxIndex}, changeQuantity);
		elemWork.unbind("focus");
		elemWork.bind("focus", {index: maxIndex}, function(e){ this.curVal=this.value; });
		elemWork.unbind("blur");
		elemWork.bind("blur", {index: maxIndex}, function(e){ if(this.curVal!=this.value){ changeQuantity(e); } });

		elemWork = elemTd.children().children("#stockBtn0");
		elemWork.attr("id", "stockBtn" + maxIndex);
		elemWork.attr("tabindex", (++tabIdx));
		elemWork.bind("click", {index: maxIndex}, showStockInfo);

		elemTd = elemTd.next();
		setElem( elemTd.children(), "deliveryProcessCategory", maxIndex, null, (++tabIdx) );
		elemWork.bind("click", {index: maxIndex}, changeCategory);

		elemTd = elemTd.next();
		elemWork = setElem( elemTd.children(), "unitCost", maxIndex, null, (++tabIdx) );
//		elemWork.bind("change", {index: maxIndex}, changeUnitCost);
		elemWork.unbind("focus");
		elemWork.bind("focus", {index: maxIndex}, function(e){ this.curVal=this.value; });
		elemWork.unbind("blur");
		elemWork.bind("blur", {index: maxIndex}, function(e){ if(this.curVal!=this.value){ changeUnitCost(e); } });

		elemWork = setElem( elemTd.children(), "cost", maxIndex, null, (++tabIdx) );
//		elemWork.bind("change", {index: maxIndex}, changeCost);
		elemWork.unbind("focus");
		elemWork.bind("focus", {index: maxIndex}, function(e){ this.curVal=this.value; });
		elemWork.unbind("blur");
		elemWork.bind("blur", {index: maxIndex}, function(e){ if(this.curVal!=this.value){ changeCost(e); } });

		elemTd = elemTd.next();
		elemWork = setElem( elemTd.children(), "unitRetailPrice", maxIndex, null, (++tabIdx) );
//		elemWork.bind("change", {index: maxIndex}, changeUnitRetailPrice);
		elemWork.unbind("focus");
		elemWork.bind("focus", {index: maxIndex}, function(e){ this.curVal=this.value; });
		elemWork.unbind("blur");
		elemWork.bind("blur", {index: maxIndex}, function(e){ if(this.curVal!=this.value){ changeUnitRetailPrice(e); } });

		elemWork = setElem( elemTd.children(), "retailPrice", maxIndex, null, (++tabIdx) );
//		elemWork.bind("change", {index: maxIndex}, changeRetailPrice);
		elemWork.unbind("focus");
		elemWork.bind("focus", {index: maxIndex}, function(e){ this.curVal=this.value; });
		elemWork.unbind("blur");
		elemWork.bind("blur", {index: maxIndex}, function(e){ if(this.curVal!=this.value){ changeRetailPrice(e); } });

		elemTd = elemTd.next();
		setElem( elemTd.children(), "remarks", maxIndex, null, (++tabIdx) );
		setElem( elemTd.children(), "eadRemarks", maxIndex, null, (++tabIdx) );

		elemTd = elemTd.next();
		setElem( elemTd.children(), "rackCodeSrc", maxIndex, null, (++tabIdx) );

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
			elemWork.attr("disabled", true);
		} else {
			elemWork.attr("disabled", false);
		}

		// 行を追加
		$("#trAddLine").before(elemTr);

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
		elem = $("#salesLineList\\["+lineNo+"\\]\\.productCode").get(0);
		if(elem.value != "") {
			retVal = false;
		}
		// 数量
		elem = $("#salesLineList\\["+lineNo+"\\]\\.quantity").get(0);
		if(elem.value != "") {
			retVal = false;
		}
		// 仕入単価
		elem = $("#salesLineList\\["+lineNo+"\\]\\.unitCost").get(0);
		if(elem.value != "") {
			retVal = false;
		}
		// 仕入金額
		elem = $("#salesLineList\\["+lineNo+"\\]\\.cost").get(0);
		if(elem.value != "") {
			retVal = false;
		}
		// 売上単価
		elem = $("#salesLineList\\["+lineNo+"\\]\\.unitRetailPrice").get(0);
		if(elem.value != "") {
			retVal = false;
		}
		// 売上金額
		elem = $("#salesLineList\\["+lineNo+"\\]\\.retailPrice").get(0);
		if(elem.value != "") {
			retVal = false;
		}
		// 備考
		elem = $("#salesLineList\\["+lineNo+"\\]\\.remarks").get(0);
		if(elem.value != "") {
			retVal = false;
		}

		return retVal;
	}

	// 明細行のコピー
	function copyLine(destIndex, srcIndex){

		for(var i in ProductInfosIDList){
			var v = $("#salesLineList\\["+srcIndex+"\\]\\."+ ProductInfosIDList[i]).attr("value");
			$("#salesLineList\\["+destIndex+"\\]\\."+ ProductInfosIDList[i]).attr("value",v);
		}
		for(var i in ProductInfosIDListDisp){
			var v = $("#salesLineList\\["+srcIndex+"\\]\\."+ ProductInfosIDListDisp[i]).attr("value");
			$("#salesLineList\\["+destIndex+"\\]\\."+ ProductInfosIDListDisp[i]).attr("value",v);
		}

		// 商品名（表示用）
		$("#productAbstract"+destIndex).html($("#productAbstract"+srcIndex).html());

		// 合計の再計算
		calcTotal();
	}

	// 指定行から前行のIndexを返す
	// （見つからない場合は-1を返す）
	function getPrevIndex(index){
		var retVal = -1;
		var i, lineNo, trId;

		// 指定行の行番号を取得
		lineNo = parseInt($("#salesLineList\\["+index+"\\]\\.lineNo").get(0).value);
		// 前行を探す（削除行は非表示になっている為、直前の削除されていない行を探す）
		if(lineNo>1) {
			trId = $("#tbodyLine").get(0).children[lineNo-2].id;
			retVal = trId.replace("trLine", "");
		}

		return retVal;
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
	//取引区分リストへの追加（現金）
	function SalescatrgoryListAdd(targetName,value){

		//新規時のみ追加
		if( $("#salesSlipId").val() == "" ){

			// 現金以外の場合、現金を追加
			if((value != "1")&&(value != "")){
				$(targetName).append($('<option>').attr("value","1").text(SalesCatList[1]));
			}
		}
	}


	// ================================================================================================
	// 顧客系Ajax
	// ================================================================================================
	//顧客検索ダイアログ呼び出し
	function customerSearch(){
		openSearchCustomerDialog('customer', customerCallBack);
		$("#"+'customer'+"_customerCode").val($("#customerCode").val());
	}

	//顧客先情報フォームIDリスト
	var CustomerInfosIDList = new Array(
			"customerIsExist"
			,"customerName"
			,"taxShiftCategory"
			,"salesCmCategory"
			,"billCutoffGroup"
			,"paybackCycleCategory"
			,"customerPcName"
			,"customerZipCode"
			,"customerAddress1"
			,"customerAddress2"
			,"customerTel"
			,"taxFractCategory"
			,"priceFractCategory"
			,"tempDeliverySlipFlag"
			,"billPrintUnit"
			,"billDatePrint"
			,"customerRemarks"
			,"customerCommentData"
			,"customerRemarks"
			,"customerCommentData"
			,"adlabel"
	);
	var CustomerInfosDBList = new Array(
			"customerIsExist"
			,"customerName"
			,"taxShiftCategory"
			,"salesCmCategory"
			,"cutoffGroup"
			,"paybackCycleCategory"
			,"customerPcName"
			,"customerZipCode"
			,"customerAddress1"
			,"customerAddress2"
			,"customerTel"
			,"taxFractCategory"
			,"priceFractCategory"
			,"tempDeliverySlipFlag"
			,"billPrintUnit"
			,"billDatePrint"
			,"remarks"
			,"commentData"
			,"customerRemarks"
			,"customerCommentData"
			,"customerName"
	);
	//顧客情報の初期化
	function InitCustomerInfos(){
		for(var i in CustomerInfosIDList){
			$("#"+ CustomerInfosIDList[i]).attr("value","");
		}
		catrgoryListCtrl("#taxShiftCategory","");
		catrgoryListCtrl("#salesCmCategory","");
		// 取引区分リスト整理
		SalescatrgoryListAdd("#salesCmCategory","");
		catrgoryListCtrl("#cutoffGroupCategory","");
		$("#custsalesCmCategory").val($("#salesCmCategory").val());

		// 端数処理を変数に設定
		$("#taxFractCategory").val(${mineDto.taxFractCategory});
		$("#priceFractCategory").val(${mineDto.priceFractCategory});
		applyNumeralStyles();
	}

	function customerCallBack(id, map) {
		$("#customerCode").attr("value",map["customerCode"]);
		catrgoryListCtrl("#taxShiftCategory",map["taxShiftCategory"]);
		catrgoryListCtrl("#salesCmCategory",map["salesCmCategory"]);
		// 取引区分リスト整理
		SalescatrgoryListAdd("#salesCmCategory",map["salesCmCategory"]);
		$("#custsalesCmCategory").val($("#salesCmCategory").val());
		catrgoryListCtrl("#cutoffGroupCategory",map["cutoffGroup"]+map["paybackCycleCategory"]);
		for(var i in CustomerInfosIDList){
			$("#"+ CustomerInfosIDList[i]).attr("value",map[CustomerInfosDBList[i]]);
		}
		// 宛名には、敬称も追加する。
		if($("#adlabel").val() != ""){
			$("#adlabel").attr("value",$("#adlabel").val() + "　" + map["customerPcPreCategoryName"]);
		}
		checkCustomerRoCategory(map["customerRoCategory"]);
		InitDeliveryInfos();
		GetDeliveryList();
		GetBillingInfos();
		changeOn();
	}
	// 顧客情報の設定
	function setCustomerInfos( value ){
		catrgoryListCtrl("#taxShiftCategory",value.taxShiftCategory);
		catrgoryListCtrl("#salesCmCategory",value.salesCmCategory);
		// 取引区分リスト整理
		SalescatrgoryListAdd("#salesCmCategory",value.salesCmCategory);
		$("#custsalesCmCategory").val($("#salesCmCategory").val());
		catrgoryListCtrl("#cutoffGroupCategory",value.cutoffGroup+value.paybackCycleCategory);
		for(var i in CustomerInfosIDList){
			if(value[CustomerInfosDBList[i]]) {
				$("#"+ CustomerInfosIDList[i]).attr("value",value[CustomerInfosDBList[i]]);
			}
		}

		$("#billPrintUnit").val(value.billPrintUnit);

		// 宛名には、敬称も追加する。
		if($("#adlabel").val() != ""){
			$("#adlabel").attr("value",$("#adlabel").val() + "　" + value["customerPcPreCategoryName"]);
		}
		checkCustomerRoCategory(value["customerRoCategory"]);
		applyNumeralStyles();
	}
	function checkCustomerRoCategory(cat){
		if( cat == "<%=CategoryTrns.RECIEVE_ORDER_DEPOSIT_LATE%>" ){
			alert('<bean:message key="alert.orderDepositLate" />');
		}else if( cat == "<%=CategoryTrns.RECIEVE_ORDER_STOP%>" ){
			alert('<bean:message key="alert.orderStop" />');
		}
	}


	//顧客情報の取得
	function GetCustomerInfos(){
		if(jQuery.trim($("#customerCode").attr("value"))==""){
			InitCustomerInfos();
			InitBillingInfos();
			InitDeliveryInfos();
			initDeliveryList();
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
						InitBillingInfos();
						InitDeliveryInfos();
						initDeliveryList();
						alert('<bean:message key="errors.customer.not.exist.code" />');
					}else{
						$("#customerIsExist").attr("value","1");
						var value = eval("(" + data + ")");
						setCustomerInfos( value );
						GetBillingInfos();
						InitDeliveryInfos();
						GetDeliveryList();
					}
				});
	}

	//顧客コード変更
	function ChangeCustomerCode(){
		//仕入先情報取得
		GetCustomerInfos();
	}

	// ================================================================================================
	// 請求先系
	//請求先情報フォームIDリスト
	var BillingInfosIDList = new Array(
			"baCode",
			"baName",
			"baKana",
			"baOfficeName",
			"baOfficeKana",
			"baDeptName",
			"baZipCode",
			"baAddress1",
			"baAddress2",
			"baPcName",
			"baPcKana",
			"baPcPreCategory",
			"baPcPre",
			"baTel",
			"baFax",
			"baEmail",
			"baUrl"
	);
	var BillingInfosDBList = new Array(
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
	//請求先情報の初期化
	function InitBillingInfos(){
		for(var i in BillingInfosIDList){
			$("#"+ BillingInfosIDList[i]).attr("value","");
		}
	}
	// 請求先情報の設定
	function setBillingInfos( value ){
		for(var i in BillingInfosIDList){
			$("#"+ BillingInfosIDList[i]).attr("value",value[BillingInfosDBList[i]]);
		}
	}
	//請求先情報の取得
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
						setBillingInfos( value );
					}
				});

	}

	// ================================================================================================
	// 納入先系Ajax
	// ================================================================================================
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
//		$("#deliveryCode").append($('<option>').attr("value","").text(""));
		for( i=0 ; i < length/2 ; i++ ){
			var val = value["value"+i];
			var name = value["name"+i];
			$("#deliveryCode").append($('<option>').attr("value",val).text(name));
		}

		// 先頭は空白状態なので、２番目を選択する
		$('#deliveryCode').get(0).selectedIndex = 1;

		changedelivery();
	}
	//納入先リストのクリア
	function initDeliveryList(){
		// クリア
		$("#deliveryCode" + " option").each(
			function(i){
				$(this).remove();
		});
		$("#deliveryCode").append($('<option>').attr("value","").text(""));
	}

	//納入先リストの取得
	function GetDeliveryList(){
		var data = new Object();
		data["customerCode"] = $("#customerCode").attr("value");
		asyncRequest(
				contextRoot + "/ajax/commonDelivery/getDeliveryListByCustomerCodeSortedByCreDate",
				data,
				function(data) {
					if(data==""){
						InitDeliveryInfos();
					}else{
						var value = eval("(" + data + ")");
						setDeliveryList( value );
					}
				});
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
	//納入先情報の初期化
	function InitDeliveryInfos(){
		for(var i in DeliveryInfosIDList){
			$("#"+ DeliveryInfosIDList[i]).attr("value","");
		}
	}
	// 納入先情報の設定
	function setDeliveryInfos( value ){
		for(var i in DeliveryInfosIDList){
			$("#"+ DeliveryInfosIDList[i]).attr("value",value[DeliveryInfosIDList[i]]);
		}
		catrgoryListCtrl("#deliveryPcPreCategory",value.deliveryPcPreCategory);
	}

	function changedelivery(){
		if( $("#deliveryCode").attr("value") == "" ){
			InitDeliveryInfos();
			return;
		}
		asyncRequest(
				contextRoot + "/ajax/commonDelivery/getDeliveryInfosByDeliveryCode/" + $("#deliveryCode").attr("value"),
				null,
				function(data) {
					if(data==""){
						InitDeliveryInfos();
						initDeliveryList();
					}else{
						var value = eval("(" + data + ")");
						setDeliveryInfos( value );
					}
				});
	}

	// ================================================================================================
	// DBから設定しない画面上の項目
	var ProductInfosIDListDisp = new Array(
			"status",
			"quantity",
			"retailPrice",
			"cost",
			"retailPrice",
			"ctaxPrice",
			"gm"
	);
	//商品情報フォームIDリスト
	var ProductInfosIDList = new Array(
			"productCode",
			"customerPcode",
			"productAbstract",
			"unitCategory",
			"unitName",
			"packQuantity",
			"unitRetailPrice",
			"unitCost",
			"taxCategory",
			"ctaxRate",
			"rackCodeSrc",
			"productRemarks",
			"eadRemarks",
			"setTypeCategory",
			"roMaxNum",
			"possibleDrawQuantity"
			,"deliveryProcessCategory"
			,"stockCtlCategory"
	);
	// DB側の名称 ProductInfosIDListと対応させる
	var ProductInfosDBList = new Array(
			"productCode",
			"onlinePcode",
			"productName",
			"unitCategory",
			"unitCategoryName",
			"packQuantity",
			"retailPrice",
			"supplierPriceYen",
			"taxCategory",
			"",
			"rackCode",
			"remarks",
			"eadRemarks",
			"setTypeCategory",
			"roMaxNum",
			"possibleDrawQuantity",
			"deliveryProcessCategory",
			"stockCtlCategory"
	);

	//商品情報の初期化
	function InitProductInfos( index ){
		for(var i in ProductInfosIDList){
			// 商品コードは初期化しない
			if( ProductInfosIDList[i] == "productCode" )	continue;
			$("#salesLineList\\["+index+"\\]\\."+ProductInfosIDList[i]).attr("value","");
		}
		// 商品名（表示用）
		$("#productAbstract"+index).html("");
	}
	// 商品情報の設定
	function setProductInfos( index, value ){
		for(var i in ProductInfosIDList){
			$("#salesLineList\\["+index+"\\]\\."+ ProductInfosIDList[i]).attr("value",value[ProductInfosIDList[i]]);
		}
	}
	// DBからの商品情報の設定
	function setProductInfosByDB( index, value ){

		var test;
		for(var i in ProductInfosIDList){
			$("#salesLineList\\["+index+"\\]\\."+ ProductInfosIDList[i]).attr("value",value[ProductInfosDBList[i]]);
			test = test + "\n" + ProductInfosIDList[i] + ":" + value[ProductInfosDBList[i]];
		}
		// 特殊商品コードの数量を１にする
		sc_setLooseExceptianalProductCodeQuantiry($("#salesLineList\\["+index+"\\]\\.productCode").val(),
				$("#salesLineList\\["+index+"\\]\\.quantity"))

		// 商品名（表示用）
		$("#productAbstract"+index).text(value["productName"]);
		_after_load($("#salesLineList\\["+index+"\\]\\.unitRetailPrice"));
		_after_load($("#salesLineList\\["+index+"\\]\\.unitCost"));
		// 在庫管理区分を取得
		$("#salesLineList\\["+index+"\\]\\.stockCtlCategory").val(value["stockCtlCategory"]);


		setCTax( index );
	}

	function setCTax( index ){
		// 課税区分を見て消費税領域を初期化
		var cat = $("#salesLineList\\["+index+"\\]\\.taxCategory").val();
		if(( cat == "${TAX_CATEGORY_IMPOSITION}" )
				||( cat == "${TAX_CATEGORY_IMPOSITION_OLD}" )){
			$("#salesLineList\\["+index+"\\]\\.ctaxRate").val($("#taxRate").val());
		}else
			if( cat == "${TAX_CATEGORY_INCLUDED}" ){
				$("#salesLineList\\["+index+"\\]\\.ctaxRate").val($("#taxRate").val());
		}else{
			$("#salesLineList\\["+index+"\\]\\.ctaxRate").val("0.0");
		}
	}


	// 商品検索
	function searchProductCode(index) {

		var map = new Object();
		if(jQuery.trim($("#salesLineList\\["+index+"\\]\\.productCode").val()) == "") {
			InitProductInfos(index);
			return;
		}

		var data = new Object();
		data["productCode"] = $("#salesLineList\\["+index+"\\]\\.productCode").val();
		asyncRequest(
				contextRoot + "/ajax/commonProduct/getProductInfos",
				data,
				function(data) {
					if(data==""){
						InitProductInfos(index);
						alert('<bean:message key="errors.notExist" arg0="商品コード" />');
					} else {
						var value = eval("(" + data + ")");
						setProductInfosByDB( index, value );
						// 在庫情報を検索してmapに設定する
						searchProductStock(value);
						// 数量が入っていたらまとめ買い値引きを実施
						if( $("#salesLineList\\["+index+"\\]\\.quantity").val() != "" ){
							// 割引単価取得　合計再計算はこの中で実施
							searchBulkPrice(index);
						}else{
							calcUnitRetailPrice(index);
							calcUnitCost(index);
							// 下部の合計欄を計算する
							reCalc();
						}
						changeOn();

						// 廃止されている商品は警告ダイアログを表示し、処理を続行する。
				    	if(value["discarded"] == "1"){
				    		alert('<bean:message key="warns.product.discarded" />');
				    	}
					}
				});
	}

	// 商品検索
	function openProductSearchDialog(event) {
		// 商品検索ダイアログを開く
		openSearchProductDialog(event.data.index, setProductInfoFromDialog );
		// 商品コードを設定する
		$("#"+event.data.index+"_productCode").val($("#salesLineList\\["+event.data.index+"\\]\\.productCode").val());
	}

	//商品検索後の反映の上書き確認
	function checkProductWrite(index){
		if($("#salesLineList\\["+index+"\\]\\.productAbstract").val()
				|| $("#salesLineList\\["+index+"\\]\\.unitCost").val()
				|| $("#salesLineList\\["+index+"\\]\\.unitRetailPrice").val()){

			// 上書き確認
			return confirm('<bean:message key="confirm.product.copy" />');

		}

		return true;
	}

	// 商品検索後の設定処理
	function setProductInfoFromDialog(index, map) {
		if( checkProductWrite(index) == false ){
			return;
		}
		setProductInfosByDB(index, map);
//		setProductInfo(index, map);
		searchProductCode(index);
		// 在庫情報を検索してmapに設定する
		searchProductStock(map);

		calcUnitRetailPrice(index);
		calcUnitCost(index);
		// 下部の合計欄を計算する
		reCalc();
		changeOn();
	}

	// 商品検索後の設定処理
	function setProductInfo(index, map) {
		// 商品コード
		$("#salesLineList\\["+index+"\\]\\.productCode").val(map["productCode"]);
		// 商品名（表示用）
		$("#productAbstract"+index).html(map["productName"]);
		// 商品名
		$("#salesLineList\\["+index+"\\]\\.productAbstract").val(map["productName"]);
		// 現在庫数
		$("#salesLineList\\["+index+"\\]\\.stockCount").val(map["stockCount"]);
		// 棚番
		$("#salesLineList\\["+index+"\\]\\.rackCode").val(map["rackCode"]);
	}

	// 商品コード変更
	function changeProductCode(event) {
		searchProductCode(event.data.index);
	}


	// ================================================================================================
	// 在庫検索
	function showStockInfo(event) {
		var index;

		index = event.data.index;
		openStockInfoDialog('stockInfo', $("#salesLineList\\["+index+"\\]\\.productCode").val());
	}

	//在庫情報を取得する
	function searchProductStock(map){
		var data = new Object();
		data["productCode"] = map["productCode"];
		asyncRequest(
			contextRoot + "/ajax/dialog/showStockInfoDialog/calcStock",
			null,
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


	// ================================================================================================
	// 金額計算系

	function reCalc(){
		for(var i=0; i<=maxIndex; i++) {
//			calcUnitRetailPrice(i);
//			calcUnitCost(i);
			calcCTax( i );
			calcGm(i);
		}
		calcTotal();
	}

	//まとめ買い値引き単価を取得する
	function searchBulkPrice(index){
		// 受注伝票から参照した明細行の場合には、まとめ買い値引きを適用しない
		if( $("#salesLineList\\["+index+"\\]\\.productCode").attr("readOnly") ){
			calcQuantity( index );
			return;
		}

		var elem = $("#salesLineList\\["+index+"\\]\\.quantity");
		if(elem.val() == "") {
			calcQuantity( index );
			return;
		}
		var data = new Object();
		data["bulkProductCode"] = $("#salesLineList\\["+index+"\\]\\.productCode").val();
		data["bulkQuantity"] = $("#salesLineList\\["+index+"\\]\\.quantity").val();

		asyncRequest(
			contextRoot + "/ajax/commonBulkRetailPrice/getPrice",
			data,
			function(data) {
				if(data!=""){
					var value = eval("(" + data + ")");
					// 売上単価
					$("#salesLineList\\["+index+"\\]\\.unitRetailPrice").val(value);
					calcQuantity( index );
				}
			}
		);
		return false;
	}

	// 数量変更
	function changeQuantity( event ){

		// asyncで処理しない。
		async_request_off = true;

		var index = event.data.index;
		// まとめ買い値引き単価を取得する
		searchBulkPrice(index);
	}

	// 数量変更
	function calcQuantity( index ){
		var elem = $("#salesLineList\\["+index+"\\]\\.quantity");
		if(elem.val() == "") {
			$("#salesLineList\\["+index+"\\]\\.cost"		).val("");
			$("#salesLineList\\["+index+"\\]\\.retailPrice"	).val("");
			return;
		}

		var quantityObj = oBDCS(elem.val());
		if( !quantityObj.isValid() ) {
			return;
		}
        var quantity = Number(quantityObj.setSettingsFromObj($("#salesLineList\\["+index+"\\]\\.quantity")).value());

		// 受注限度数を超えている場合
		if(IsCheckOverQuantity(index)){
			if(_isNum($("#salesLineList\\["+index+"\\]\\.roMaxNum").val())) {
				var roMaxNum = parseInt($("#salesLineList\\["+index+"\\]\\.roMaxNum").val());

				if(roMaxNum && quantity > roMaxNum){
					alert('<bean:message key="warns.quantity.over.roMaxNum" />');
				}
			}
		}

		// 引当可能数チェックするかどうか、チェックする。
		if(IsCheckpossibleDrawQuantity(index, quantity)){
			// 引当可能数を超えている場合
			var elemp = $("#salesLineList\\["+index+"\\]\\.possibleDrawQuantity");
			var possibleDrawQuantityObj = oBDCS(elemp.val());
	        var possibleDrawQuantity = Number(possibleDrawQuantityObj.setSettingsFromObj($("#salesLineList\\["+index+"\\]\\.possibleDrawQuantity")).value());
			if(quantity > possibleDrawQuantity && quantity > 0){
				alert('<bean:message key="warns.quantity.over.possibleDrawQuantity" />');
			}
		}

		var restQuantity;
		var roQuantity = Number( $("#salesLineList\\["+index+"\\]\\.roQuantity").val() );
		var bkQuantity = Number( $("#salesLineList\\["+index+"\\]\\.bkQuantity").val() );
		if($("#salesSlipId").val() == "" )
			restQuantity = roQuantity - quantity;
		else
			// 更新の場合は、元の数量を足す
			restQuantity = roQuantity + bkQuantity - quantity;
//		if( $("#roSlipId").val() == "" ){
		if( $("#salesLineList\\["+index+"\\]\\.roLineId").val() == ""){
			// 伝票複写対象でなければ数量が入ると完了
			$("#salesLineList\\["+index+"\\]\\.deliveryProcessCategory").val(${DELIVERY_PROCESS_CATEGORY_FULL});
		}
		else{
			if( restQuantity == 0 )
				// TODO 保持している受注伝票の数量＝数量 完納に
				$("#salesLineList\\["+index+"\\]\\.deliveryProcessCategory").val(${DELIVERY_PROCESS_CATEGORY_FULL});
			else if( restQuantity > 0)
				// 保持している受注伝票の数量＞数量 分納に
				$("#salesLineList\\["+index+"\\]\\.deliveryProcessCategory").val(${DELIVERY_PROCESS_CATEGORY_PARTIAL});
			else if( restQuantity < 0 ){
				// 保持している受注伝票の数量＜数量 警告ダイアログ表示
				var lineNo = $("#salesLineList\\["+index+"\\]\\.lineNo").val();
				alert("<bean:message key='errors.quantity.over' arg0='" + lineNo + "' />");
				// 一応、完納にする
				$("#salesLineList\\["+index+"\\]\\.deliveryProcessCategory").val(${DELIVERY_PROCESS_CATEGORY_FULL});
			}
		}
		calcUnitRetailPrice( index );
		calcUnitCost(index);
		calcGm( index );
		calcCTax( index );
		calcTotal();
	}

	// 引当可能数チェックをするか？
	function IsCheckpossibleDrawQuantity( index,quantity ){
		// 在庫管理する商品のみ
		if($("#salesLineList\\["+index+"\\]\\.stockCtlCategory").val() == "<%=CategoryTrns.PRODUCT_STOCK_CTL_NO%>" )
			return false;
		// 特殊コード以外
		var excpProductCode = sc_isLooseExceptianalProductCode($("#salesLineList\\["+index+"\\]\\.productCode").val() );
		if(excpProductCode)
			return false;

		// 数量マイナス以外
		if(quantity < 0)
			return false;

		return true;

	}

	//大口受注チェックをするか？
	function IsCheckOverQuantity( index ){
		// 特殊コード以外
		var excpProductCode = sc_isLooseExceptianalProductCode($("#salesLineList\\["+index+"\\]\\.productCode").val() );
		if(excpProductCode)
			return false;

		// 数量マイナス以外
		if($("#salesLineList\\["+index+"\\]\\.quantity").val() < 0)
			return false;

		return true;

	}

	// 売上単価変更
	function changeUnitRetailPrice( event ){
		calcUnitRetailPrice( event.data.index );
		calcGm( event.data.index );
		calcCTax( event.data.index );
		calcTotal();
	}
	// 仕入単価変更
	function changeUnitCost( event ){
		calcUnitCost(event.data.index);
		calcGm( event.data.index );
		calcCTax( event.data.index );
		calcTotal();
	}

	// 売上金額変更
	function changeRetailPrice( event ){
		calcGm( event.data.index );
		calcCTax( event.data.index );
		calcTotal();
	}
	// 仕入金額変更
	function changeCost( event ){
		calcGm( event.data.index );
		calcCTax( event.data.index );
		calcTotal();
	}

	// 消費税計算(明細行単位）実際には存在しない
	function calcCTax( index ){
		var cat = $("#salesLineList\\["+index+"\\]\\.taxCategory").val();
		// 外税の時のみ計算
		if(( cat == "${TAX_CATEGORY_IMPOSITION}" )
				||( cat == "${TAX_CATEGORY_IMPOSITION_OLD}" )){
			var nRetailPrice = _Number( $("#salesLineList\\["+index+"\\]\\.retailPrice").val() );
			var nCTax = Number( $("#salesLineList\\["+index+"\\]\\.ctaxRate").val() );
			// 税率は％表記なので100.0で割る
			var nTax = nRetailPrice * nCTax / 100.0;
			var l_nCtaxPriceTotal = oBDCS( nTax.toString() ).setScale($("#taxFractCategory").val(),priceAlignment).setComma(false).toBDCString();
			$("#salesLineList\\["+index+"\\]\\.ctaxPrice").val( l_nCtaxPriceTotal );
		}else{
			$("#salesLineList\\["+index+"\\]\\.ctaxPrice").val( "0" );
		}
	}

	// 粗利益計算
	function calcGm( index ){

		var nRetailPrice = _Number( $("#salesLineList\\["+index+"\\]\\.retailPrice").val() );
		var nCost = _Number( $("#salesLineList\\["+index+"\\]\\.cost").val() );
		$("#salesLineList\\["+index+"\\]\\.gm").val( nRetailPrice - nCost );
	}

	// 売上単価計算
	function calcUnitRetailPrice( index ){
		var quantityObj = oBDCS( $("#salesLineList\\["+index+"\\]\\.quantity").val() );
		var retailPriceObj = oBDCS( $("#salesLineList\\["+index+"\\]\\.unitRetailPrice").val() );
		if( !quantityObj.isValid() || !retailPriceObj.isValid() ) {
			return;
		}
		var inputQuantity = quantityObj.setSettingsFromObj($("#salesLineList\\["+index+"\\]\\.quantity")).BDValue().toString();
		var nUnitRetailPrice = retailPriceObj.setSettingsFromObj($("#salesLineList\\["+index+"\\]\\.unitRetailPrice")).BDValue().toString();
		$("#salesLineList\\["+index+"\\]\\.retailPrice").val( inputQuantity * nUnitRetailPrice );
		_after_load($("#salesLineList\\["+index+"\\]\\.retailPrice"));
	}

	// 仕入単価計算
	function calcUnitCost( index ){
		var quantityObj = oBDCS( $("#salesLineList\\["+index+"\\]\\.quantity").val() );
		var costObj = oBDCS( $("#salesLineList\\["+index+"\\]\\.unitCost").val() );
		if( !quantityObj.isValid() || !costObj.isValid() ) {
			return;
		}
		var inputQuantity = quantityObj.setSettingsFromObj($("#salesLineList\\["+index+"\\]\\.quantity")).BDValue().toString();
		var nUnitCost = costObj.setSettingsFromObj($("#salesLineList\\["+index+"\\]\\.unitCost")).BDValue().toString();
		$("#salesLineList\\["+index+"\\]\\.cost").val( inputQuantity * nUnitCost );
		_after_load($("#salesLineList\\["+index+"\\]\\.cost"));
	}

	// 合計欄表示更新
	function calcTotal(){
		// 集計
		var nCostTotal = Number(0.0);
		var nPriceTotal = Number(0.0);
		var nCtaxPriceTotal = Number(0.0);
		var arryPriceTotal = {};

		lineSize = $("#tbodyLine").get(0).children.length-1;
		for(i=0; i<lineSize; i++) {
			trId = $("#tbodyLine").get(0).children[i].id;
			id = trId.replace("trLine", "");
			var a = $("#salesLineList\\["+id+"\\]\\.cost").val();
			var b = $("#salesLineList\\["+id+"\\]\\.retailPrice").val();
			if( ($("#salesLineList\\["+id+"\\]\\.cost").val() == "" )
					||( $("#salesLineList\\["+id+"\\]\\.retailPrice").val() == "" )){
				continue;
			}
			var nCost = _Number($("#salesLineList\\["+id+"\\]\\.cost").val());
			var nPrice = _Number($("#salesLineList\\["+id+"\\]\\.retailPrice").val());
			if(( isNaN(nCost)==true )||( isNaN(nPrice)==true)){	continue;	}
			// 合計を計算
			nCostTotal += nCost;
			nPriceTotal += nPrice;
			// 外税の時のみ消費税計算
			var cat = $("#salesLineList\\["+id+"\\]\\.taxCategory").val();
			if(( cat == "${TAX_CATEGORY_IMPOSITION}" )
					||( cat == "${TAX_CATEGORY_IMPOSITION_OLD}" )){
				// 消費税の計算対象を税率ごとに加算する
				//var pos = $("#salesLineList\\["+id+"\\]\\.ctaxRate").val();
				var pos = $("#ctaxRate").val();

				var total = Number(arryPriceTotal[pos]);
				if( isNaN(total) == true ){
					arryPriceTotal[pos] = nPrice;
				}else{
					arryPriceTotal[pos] += nPrice;
				}
			}
		}
		// 消費税の計算は税率ごとに行う
		for (var key in arryPriceTotal) {
			var total = arryPriceTotal[key];
			var rate = Number( key );

			// 税率は％表記なので100.0で割る
			nCtaxPriceTotal += ( rate / 100.0 * total );
		}

		// 表示
		// 粗利益
		var gmTotal = nPriceTotal - nCostTotal;
		$("#gmTotal").html( gmTotal.toString() );
		SetBigDecimalScale_Obj($("#gmTotal"));

		// 粗利益率
		if( (isNaN(nPriceTotal)==true)||( nPriceTotal == "0.0" )){
			$("#gmTotalPer").html( "0" );
		}else{
			// 2010.04.22 add kaki 粗利益率は％表示とする。まず100倍する。
			$("#gmTotalPer").html( (( nPriceTotal - nCostTotal ) / nPriceTotal) * 100 );
		}

		// 金額合計
		$("#priceTotal").html( nPriceTotal );
		SetBigDecimalScale_Obj($("#priceTotal"));

		// 税転嫁が内税の時には消費税の表示は行わない
		if( $("#taxShiftCategory") == ${TAX_SHIFT_CATEGORY_INCLUDE_CTAX}){

			// 消費税
			$("#ctaxPriceTotal").html( "0" );


			// 伝票合計
			$("#total").html( nPriceTotal );

		} else {

			// 消費税
			$("#ctaxPriceTotal").html(nCtaxPriceTotal);

			// 伝票合計
			var l_nCtaxPriceTotal = oBDCS(nCtaxPriceTotal.toString()).setScale($("#taxFractCategory").val(),priceAlignment).setComma(false).toBDCString();
			var l_nPriceTotal = oBDCS(nPriceTotal.toString()).setScale($("#priceFractCategory").val(),priceAlignment).setComma(false).toBDCString();
			var ttl = Number(l_nCtaxPriceTotal) + Number(l_nPriceTotal);
			$("#total").html( ttl );

		}

		SetBigDecimalScale_Obj($("#ctaxPriceTotal"));
		SetBigDecimalScale_Obj($("#total"));

		// カンマをつける
		_after_load($(".numeral_commas"));

		// 2010.04.22 add kaki 粗利益率は％表示とする。
		var sgmTotalPer = $("#gmTotalPer").html();
		$("#gmTotalPer").html(sgmTotalPer + "%");
		SetBigDecimalScale_Obj($("#gmTotalPer"));

	}

	// -------------
	// 伝票複写
	// -------------
	function copySlipCallback(dialogId, slipName, slipId ) {
		$("#copySlipName").val( slipName );
		$("#copySlipId").val( slipId );
		$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/sales/inputSales/copy")}');
		$("form[name='" + MAIN_FORM_NAME + "']").submit();
	}

function copyDummy(){
//	copySlipCallback("", "RORDER", "1" );
	copySlipCallback("", "SALES", "58" );
}
function copyDummy2(){
	copySlipCallback("", "RORDER", "64" );
//	copySlipCallback("", "SALES", "58" );
}

function checkPrint(){
	if( $("#output_dialog").find("input:checkbox[checked='true']").length > 0 ){
		$("#doPrintButton").attr("disabled", "");
	}else{
		$("#doPrintButton").attr("disabled", "disabled");
	}
}

//印刷用ダイアログ
function outputDialog(evt) {
	var custCode = $("#customerCode").val();
	var billPrintUnit = $("#billPrintUnit").val();
	var billDatePrint = $("#billDatePrint").val();
	var tmpCat = $("#tempDeliverySlipFlag").val();
	var salesCmCat = $("#salesCmCategory").val();
	$("#output_dialog").find("input:checkbox[checked='true']").attr("checked", false);

	$("#spanA").hide();		//見積書
	$("#spanG").hide();		//請求書G
	$("#spanH").hide();		//請求書H
	$("#spanD").hide();		//納品書D
	$("#spanC").hide();		//納品書C
	$("#spanE").hide();		//仮納品書
	$("#spanJ").hide();		//ピッキングリスト
	$("#spanF").hide();		//納品書兼領収書

	// ピッキングリストは常に発行可能
	$("#spanJ").show();

	// 納品書は、請求書を発行する顧客の場合発行可能
	if( salesCmCat == "<%=CategoryTrns.SALES_CM_CREDIT%>" && billPrintUnit != "<%=CategoryTrns.BILL_PRINT_UNIT_NO_BILL%>" ) {
		$("#spanD").show();
	}

	// 仮納品書のチェックボックス表示/非表示の切り替え
	if( tmpCat == "1" ){
		$("#spanE").show();
		if($("#reportEFlag").val() == "false" ){
			$("#typeE").attr("disabled","disabled" );
		}
	}
	// (月単位の請求書含め)請求書を発行しない顧客の場合は、納品書兼領収書を発行可能
	if( salesCmCat != "<%=CategoryTrns.SALES_CM_CREDIT%>" || billPrintUnit == "<%=CategoryTrns.BILL_PRINT_UNIT_NO_BILL%>" ) {
		$("#spanF").show();
	}
	// 売上伝票単位の請求書を発行する顧客の場合は、請求書発行可能
	if( billPrintUnit == "<%=CategoryTrns.BILL_PRINT_UNIT_SALES_SLIP%>" ) {
		$("#spanG").show();
	}

	$("#doPrintButton").attr("disabled", true);

	$("#output_dialog").dialog({
		bgiframe: true,
		autoOpen: false,
		width: 290,
		height: 300,
		modal: false,
		buttons: {
		}
	});

	$("#output_dialog").dialog('open');
	$("#output_dialog").dialog('option', 'height', 300);

	if(jQuery.browser.msie) {
		window.event.keyCode = null;
		window.event.returnValue = false;
	}
	return false;
}

// 印刷実行
function doPrint( type ){
	$("#printId").val( $("#salesSlipId").val() );
	$("#printSalesSlipCategory").val( $("#salesSlipCategory").val() );

	var url = '${f:url("/sales/outputSalesReportSingle/")}' + reportType;
	$("form[name='printForm']").attr("action", url );
	$("form[name='printForm']").submit();

	$('#output_dialog').dialog('close');
}

function setRate( rate ){
	// 消費税保持
	$("#taxRate").val(rate);
	changeRate();
}


function changeRate(){
	// 行のレートを変更
	lineSize = $("#tbodyLine").get(0).children.length-1;
	for(i=0; i<lineSize; i++) {
		trId = $("#tbodyLine").get(0).children[i].id;
		id = trId.replace("trLine", "");
		// レート設定
		setCTax( id );
		// 消費税再計算
		calcCTax( i );
	}
	// 合計の再計算
	calcTotal();

}

	// 完納区分変更
	function changeCategory( event ){
		var index = event.data.index;

		var elem = $("#salesLineList\\["+index+"\\]\\.quantity");
		if(elem.val() == "") {
			return;
		}

		var quantityObj = oBDCS(elem.val());
		if( !quantityObj.isValid() ) {
			return;
		}
        var quantity = Number(quantityObj.setSettingsFromObj($("#salesLineList\\["+index+"\\]\\.quantity")).value());

		var restQuantity;
		var roQuantity = Number( $("#salesLineList\\["+index+"\\]\\.roQuantity").val() );
		var bkQuantity = Number( $("#salesLineList\\["+index+"\\]\\.bkQuantity").val() );
		if($("#salesSlipId").val() == "" )
			restQuantity = roQuantity - quantity;
		else
			// 更新の場合は、元の数量を足す
			restQuantity = roQuantity + bkQuantity - quantity;

		if( $("#salesLineList\\["+index+"\\]\\.roLineId").val() == ""){
			// 伝票複写対象でなければ数量が入ると完了
			$("#salesLineList\\["+index+"\\]\\.deliveryProcessCategory").val(${DELIVERY_PROCESS_CATEGORY_FULL});
		}
		else{
			// 完納区分が「完納」に設定され、残数が＞０の場合、警告を表示
			if(($("#salesLineList\\["+index+"\\]\\.deliveryProcessCategory").val() == ${DELIVERY_PROCESS_CATEGORY_FULL})&&( restQuantity > 0 )){
				alert('<bean:message key="errors.line.updatePaid" />');
			}
		}

	}

	-->
	</script>

</head>
<body onload="init()" onhelp="return false;">


<!-- ヘッダ -->
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>
<!-- メニュー -->
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0004"/>
		<jsp:param name="MENU_ID" value="0400"/>
	</jsp:include>

<!-- メイン機能 -->
	<div id="main_function">
		<!-- タイトル -->
		<span class="title">売上入力</span>

		<div class="function_buttons">
			<!-- TODO EL式でファンクション制御用DTOを参照して表示制御する  -->
			<button type="button" tabindex="2000" id="btnF1" onclick="onF1();">F1<br>初期化</button>
			<c:if test="${closed || !menuUpdate}"><button type="button" tabindex="2001" id="btnF2" onclick="onF2();" disabled>F2<br>削除</button></c:if>
			<c:if test="${!closed && menuUpdate}"><button type="button" tabindex="2001" id="btnF2" onclick="onF2();">F2<br>削除</button></c:if>
			<c:if test="${newData}" >
				<c:if test="${closed || !menuUpdate}"><button type="button" tabindex="2002" id="btnF3" onclick="onF3();" disabled>F3<br>登録</button></c:if>
				<c:if test="${!closed && menuUpdate}"><button type="button" tabindex="2002" id="btnF3" onclick="onF3();">F3<br>登録</button></c:if>
			</c:if>
			<c:if test="${!newData}" >
				<c:if test="${closed || !menuUpdate}"><button type="button" tabindex="" id="btnF3" onclick="onF3();" disabled>F3<br>更新</button></c:if>
				<c:if test="${!closed && menuUpdate}"><button type="button" tabindex="" id="btnF3" onclick="onF3();">F3<br>更新</button></c:if>
			</c:if>
			<button type="button" tabindex="2003" id="btnF4" disabled>F4<br>&nbsp;</button>
			<c:if test="${newData}" ><button type="button" tabindex="2004" id="btnF5" onclick="onF5();" disabled>F5<br>PDF</button></c:if>
			<c:if test="${!newData}" ><button type="button" tabindex="2004" id="btnF5" onclick="onF5();">F5<br>PDF</button></c:if>
			<c:if test="${newData}" >
			<button type="button" tabindex="2005" id="btnF6" onclick="onF6();">F6<br>伝票呼出</button>
			</c:if>
			<c:if test="${!newData}" >
			<button type="button" tabindex="2005" id="btnF6" onclick="onF6();" disabled>F6<br>伝票呼出</button>
			</c:if>
			<button type="button" tabindex="" id="btnF7" disabled="disabled" >F7<br>&nbsp;</button>
			<button type="button" tabindex="" id="btnF8" disabled="disabled" >F8<br>&nbsp;</button>
			<button type="button" tabindex="" id="btnF9" disabled="disabled" >F9<br>&nbsp;</button>
			<button type="button" tabindex="" id="btnF10" disabled="disabled" >F10<br>&nbsp;</button>
			<button type="button" tabindex="" id="btnF11" disabled="disabled" >F11<br>&nbsp;</button>
			<button type="button" tabindex="" id="btnF12" disabled="disabled" >F12<br>&nbsp;</button>
		</div>
		<br><br><br>

		<s:form onsubmit="return false;">
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
							<span >売上伝票情報</span>
				            <button class="btn_toggle" />
						</div><!-- /.section_title -->

						<div id="order_section" class="section_body">
							<table id="order_info1" class="forms" summary="売上伝票情報">
								<tr>
									<th><div class="col_title_right">売上番号</div></th>
									<td>
										<html:text tabindex="100" property="salesSlipId" styleId="salesSlipId" style="width: 135px; ime-mode:disabled;"  maxlength="10"  onfocus="this.curVal=this.value;" onblur="if((this.curVal == '') || ((this.curVal != '')&&(this.curVal!=this.value))){findSlip();}" />
									</td>
									<th><div class="col_title_right">受注番号</div></th>
									<td>
										<html:text tabindex="101" property="roSlipId" styleId="roSlipId" style="width: 135px; ime-mode:disabled;" maxlength="10"  onchange="findCopy();"/>
									</td>
									<th><div class="col_title_right_req">売上日<bean:message key='labels.must'/></div></th>
									<td>
										<html:text tabindex="102" property="salesDate" styleId="salesDate" styleClass="date_input"  style="width: 135px; ime-mode:disabled; text-align:center;" maxlength="10" />
									</td>
									<th><div class="col_title_right">納期指定日</div></th>
									<td>
										<html:text tabindex="103" property="deliveryDate" styleId="deliveryDate" styleClass="date_input"  style="width: 135px; ime-mode:disabled;" maxlength="10" />
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">受付番号</div></th>
									<td>
										<html:text tabindex="104" property="receptNo" styleId="receptNo" style="width: 135px; ime-mode:disabled;" maxlength="30" />
									</td>
									<th><div class="col_title_right">客先伝票番号</div></th>
									<td>
										<html:text tabindex="105" property="customerSlipNo" styleId="customerSlipNo" style="width: 135px; ime-mode:disabled;" maxlength="30" />
									</td>
									<th><div class="col_title_right">入力担当者</div></th>
									<td colspan="3">
										<html:text tabindex="106" property="userName" styleId="userName" readonly="true" styleClass="c_disable" style="width: 135px" />
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">摘要</div></th>
									<td colspan="3">
										<html:text tabindex="107" property="remarks" styleId="remarks" style="width: 420px; ime-mode:active;" maxlength="50" />
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
									<th><div class="col_title_right">ピッキング備考</div></th>
									<td colspan="7">
										<html:text tabindex="110" property="pickingRemarks" styleId="pickingRemarks" style="width: 800px; ime-mode:active;" maxlength="120" />
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">宛名</div></th>
									<td colspan="3">
										<html:text tabindex="111" property="adlabel" styleId="adlabel" style="width: 420px; ime-mode:active;" maxlength="100" />
									</td>
									<th><div class="col_title_right">但書</div></th>
									<td colspan="3" >
										<html:text tabindex="112" property="disclaimer" styleId="disclaimer" style="width: 135px; ime-mode:active;" maxlength="60" />
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">消費税率</div></th>
									<td colspan="3">
										<html:select property="ctaxRate" styleId="ctaxRate" tabindex="113" style="width: 135px;" onchange="setRate(this.value)">
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
							<span >顧客情報</span>
				            <button class="btn_toggle" />
						</div><!-- /.section_title -->

						<div id="order_section" class="section_body">
							<table id="customer_info" class="forms" summary="顧客情報">
								<tr>
									<th><div class="col_title_right_req">顧客コード<bean:message key='labels.must'/></div></th>
									<td>
										<html:text tabindex="200" property="customerCode" styleId="customerCode" style="width: 130px; ime-mode:disabled;"
											onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ ChangeCustomerCode(); }"/>
										<html:image tabindex="201" styleId="customerCodeImg" src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" onclick="customerSearch()" />
									</td>
									<th><div class="col_title_right">顧客名</div></th>
									<td colspan="3">
										<html:text tabindex="202" property="customerName" styleId="customerName" style="width: 500px" readonly="true" styleClass="c_disable" maxlength="60" />
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">税転嫁</div></th>
									<td>
										<html:select tabindex="203" property="taxShiftCategory"  styleId="taxShiftCategory" styleClass="c_disable"  >
											<c:forEach var="tscl" items="${taxShiftCategoryList}">
												<html:option value="${tscl.value}">${tscl.label}</html:option>
											</c:forEach>
										</html:select>
									</td>
									<th><div class="col_title_right">支払条件</div></th>
									<td>
										<html:select tabindex="203" property="cutoffGroupCategory"  styleId="cutoffGroupCategory" styleClass="c_disable" >
											<c:forEach var="pdcl" items="${cutoffGroupCategoryList}">
												<html:option value="${pdcl.value}">${pdcl.label}</html:option>
											</c:forEach>
										</html:select>
									</td>
									<th width="120px"><div class="col_title_right">取引区分</div></th>
									<td>
										<html:select tabindex="204" property="salesCmCategory"  styleId="salesCmCategory" >
											<c:forEach var="scmcl" items="${salesCmCategoryList}">
												<html:option value="${scmcl.value}">${scmcl.label}</html:option>
											</c:forEach>
										</html:select>
									</td>
									<td style="display: none;">
										<html:hidden property="userId" styleId="userId" />
										<html:hidden property="status" styleId="status" />
										<html:hidden property="billId" styleId="billId" />
										<html:hidden property="salesBillId" styleId="salesBillId" />
										<html:hidden property="billDate" styleId="billDate" />
										<html:hidden property="billCutoffGroup" styleId="billCutoffGroup" />
										<html:hidden property="paybackCycleCategory" styleId="paybackCycleCategory" />
										<html:hidden property="billCutoffDate" styleId="billCutoffDate" />
										<html:hidden property="billCutoffPdate" styleId="billCutoffPdate" />
										<html:hidden property="salesCutoffDate" styleId="salesCutoffDate" />
										<html:hidden property="salesCutoffPdate" styleId="salesCutoffPdate" />
										<html:hidden property="baCode" styleId="baCode" />
										<html:hidden property="baName" styleId="baName" />
										<html:hidden property="baKana" styleId="baKana" />
										<html:hidden property="baOfficeName" styleId="baOfficeName" />
										<html:hidden property="baOfficeKana" styleId="baOfficeKana" />
										<html:hidden property="baDeptName" styleId="baDeptName" />
										<html:hidden property="baZipCode" styleId="baZipCode" />
										<html:hidden property="baAddress1" styleId="baAddress1" />
										<html:hidden property="baAddress2" styleId="baAddress2" />
										<html:hidden property="baPcName" styleId="baPcName" />
										<html:hidden property="baPcKana" styleId="baPcKana" />
										<html:hidden property="baPcPreCategory" styleId="baPcPreCategory" />
										<html:hidden property="baPcPre" styleId="baPcPre" />
										<html:hidden property="baTel" styleId="baTel" />
										<html:hidden property="baFax" styleId="baFax" />
										<html:hidden property="baEmail" styleId="baEmail" />
										<html:hidden property="baUrl" styleId="baUrl" />
										<html:hidden property="taxFractCategory" styleId="taxFractCategory" />
										<html:hidden property="priceFractCategory" styleId="priceFractCategory" />
										<html:hidden property="billPrintCount" styleId="billPrintCount" />
										<html:hidden property="deliveryPrintCount" styleId="deliveryPrintCount" />
										<html:hidden property="tempDeliveryPrintCount" styleId="tempDeliveryPrintCount" />
										<html:hidden property="shippingPrintCount" styleId="shippingPrintCount" />
										<html:hidden property="siPrintCount" styleId="siPrintCount" />
										<html:hidden property="updDatetm" styleId="updDatetm" />
										<html:hidden styleId="copySlipName" property="copySlipName"/>
										<html:hidden styleId="copySlipId" property="copySlipId"/>
										<html:hidden styleId="roUpdDatetm" property="roUpdDatetm"/>
										<html:hidden styleId="tempDeliverySlipFlag" property="tempDeliverySlipFlag"/>
										<html:hidden styleId="taxRate" property="taxRate"/>
										<html:hidden styleId="codSc" property="codSc"/>
										<html:hidden property="custsalesCmCategory" styleId="custsalesCmCategory" />
										<html:hidden styleId="reportEFlag" property="reportEFlag"/>
										<html:hidden styleId="artId" property="artId"/>
										<html:hidden styleId="billPrintUnit" property="billPrintUnit"/>
										<html:hidden styleId="billDatePrint" property="billDatePrint"/>
										<html:hidden property="newData" />
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">備考</div></th>
									<td colspan="5">
										<html:text property="customerRemarks" styleId="customerRemarks" styleClass="c_disable" style="width: 800px;" readonly="true" tabindex="205" />
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">コメント</div></th>
									<td colspan="5">
										<html:text property="customerCommentData" styleId="customerCommentData" styleClass="c_disable" style="width: 800px;" readonly="true" tabindex="206" />
									</td>
								</tr>
							</table>
						</div><!-- /.section_body -->
					</div><!-- /.form_section -->
				</div><!-- /.form_section_wrap -->

			    <div class="form_section_wrap">
				    <div class="form_section">
				    	<div class="section_title">
							<span >納入先情報</span>
				            <button class="btn_toggle" />
						</div><!-- /.section_title -->

						<div id="order_section" class="section_body">
							<table id="delivery_info" class="forms" summary="納入先情報">
								<tr>
									<c:if test="${!onlineOrderData}">
										<th><div class="col_title_right_req">顧客納入先<bean:message key='labels.must'/></div></th>
									</c:if>
									<c:if test="${onlineOrderData}">
										<th><div class="col_title_right">顧客納入先</div></th>
									</c:if>
									<td colspan="7">
										<c:if test="${!onlineOrderData}">
										<html:select tabindex="300" property="deliveryCode"  styleId="deliveryCode" onchange="changedelivery()" style="width: 970px">
											<c:forEach var="dell" items="${deliveryList}">
												<html:option value="${dell.value}">${dell.label}</html:option>
											</c:forEach>
										</html:select>
										<html:hidden styleId="deliveryName" property="deliveryName"/>
										</c:if>
										<c:if test="${onlineOrderData}">
										<html:hidden styleId="deliveryCode" property="deliveryCode"/>
										<html:text tabindex="300" property="deliveryName" styleId="deliveryName" style="width: 970px" readonly="true" styleClass="c_disable"  />
										</c:if>
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">事業所名</div></th>
									<td>
										<html:hidden styleId="deliveryKana" property="deliveryKana"/>
										<html:text tabindex="301" property="deliveryOfficeName" styleId="deliveryOfficeName" style="width: 200px" readonly="true" styleClass="c_disable"  />
										<html:hidden styleId="deliveryOfficeKana" property="deliveryOfficeKana"/>
										<html:hidden styleId="pickingListId" property="pickingListId"/>
										<html:hidden styleId="roDate" property="roDate"/>
										<html:hidden styleId="customerPcName" property="customerPcName"/>
										<html:hidden styleId="customerZipCode" property="customerZipCode"/>
										<html:hidden styleId="customerAddress1" property="customerAddress1"/>
										<html:hidden styleId="customerAddress2" property="customerAddress2"/>
										<html:hidden styleId="customerTel" property="customerTel"/>
										<html:hidden styleId="printDate" property="printDate"/>
									</td>
									<th><div class="col_title_right">部署名</div></th>
									<td>
										<html:text tabindex="302" property="deliveryDeptName" styleId="deliveryDeptName" style="width: 200px" readonly="true" styleClass="c_disable"  />
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">郵便番号</div></th>
									<td>
										<html:text tabindex="303" property="deliveryZipCode" styleId="deliveryZipCode" style="width: 70px" readonly="true" styleClass="c_disable"  />
									</td>
									<th><div class="col_title_right">住所１</div></th>
									<td>
										<html:text tabindex="304" property="deliveryAddress1" styleId="deliveryAddress1" style="width: 200px" readonly="true" styleClass="c_disable"  />
									</td>
									<th ><div class="col_title_right">住所２</div></th>
									<td>
										<html:text tabindex="305" property="deliveryAddress2" styleId="deliveryAddress2" style="width: 200px" readonly="true" styleClass="c_disable"  />
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">担当者</div></th>
									<td>
										<html:text tabindex="306" property="deliveryPcName" styleId="deliveryPcName" style="width: 200px" readonly="true" styleClass="c_disable"  />
									</td>
									<th><div class="col_title_right">担当者カナ</div></th>
									<td>
										<html:text tabindex="307" property="deliveryPcKana" styleId="deliveryPcKana" style="width: 200px" readonly="true" styleClass="c_disable"  />
									</td>
									<th><div class="col_title_right">敬称</div></th>
									<td>
										<html:select tabindex="308" property="deliveryPcPreCategory" styleId="deliveryPcPreCategory" styleClass="c_disable" style="width: 220px">
											<c:forEach var="ptcl" items="${preTypeCategoryList}">
												<html:option value="${ptcl.value}">${ptcl.label}</html:option>
											</c:forEach>
										</html:select>
										<html:hidden styleId="deliveryPcPre" property="deliveryPcPre"/>
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right">TEL</div></th>
									<td>
										<html:text tabindex="309" property="deliveryTel" styleId="deliveryTel" style="width: 200px" readonly="true" styleClass="c_disable"  />
									</td>
									<th><div class="col_title_right">FAX</div></th>
									<td>
										<html:text tabindex="310" property="deliveryFax" styleId="deliveryFax" style="width: 200px" readonly="true" styleClass="c_disable"  />
									</td>
									<th><div class="col_title_right">E-MAIL</div></th>
									<td>
										<html:text tabindex="311" property="deliveryEmail" styleId="deliveryEmail" style="width: 200px" readonly="true" styleClass="c_disable"  />
										<html:hidden styleId="deliveryUrl" property="deliveryUrl"/>
									</td>
								</tr>
							</table>
						</div><!-- /.section_body -->
					</div><!-- /.form_section -->
				</div><!-- /.form_section_wrap -->

				<div id="order_detail_info_wrap">
				<table summary="受注商品明細リスト" class="forms detail_info" style="margin-top: 20px;">
					<tr>
						<th rowspan="2" class="rd_top_left" style="height: 60px; width: 30px;">No</th>
						<th rowspan="2" style="height: 60px;">商品コード※</th>
						<th style="height: 30px;">商品名・摘要</th>
						<th rowspan="2" style="height: 60px;">数量※</th>
						<th rowspan="2" style="height: 60px;">完納区分※</th>
						<th style="height: 30px;">仕入単価※</th>
						<th style="height: 30px;">売上単価※</th>
						<th style="height: 30px;">備考</th>
						<th rowspan="2" style="height: 60px;">棚番</th>
						<th rowspan="2" class="rd_top_right" style="height: 60px;">&nbsp;</th>
					</tr>
					<tr>
						<th style="height: 30px;">商品備考</th>
						<th style="height: 30px;">仕入金額※</th>
						<th style="height: 30px;">売価金額※</th>
						<th style="height: 30px;">ピッキング備考</th>
					</tr>
					<tbody id="tbodyLine">
						<c:forEach var="salesLineList" items="${salesLineList}" varStatus="s" >
							<c:if test='${salesLineList.lineNo != null}'>
							<tr id="trLine${s.index}">

								<!-- No -->
								<td id="tdNo${s.index}">
									<div class="box_1of1">
										<c:out value="${f:h(salesLineList.lineNo)}" />
									</div>
								</td>
								<td style="display: none;">
									<html:hidden name="salesLineList" property="salesLineId" indexed="true" styleId="salesLineList[${s.index}].salesLineId" />
									<html:hidden name="salesLineList" property="status" indexed="true" styleId="salesLineList[${s.index}].status" />
									<html:hidden name="salesLineList" property="salesSlipId" indexed="true" styleId="salesLineList[${s.index}].salesSlipId" />
									<html:hidden name="salesLineList" property="lineNo" indexed="true" styleId="salesLineList[${s.index}].lineNo" />
									<html:hidden name="salesLineList" property="roLineId" indexed="true" styleId="salesLineList[${s.index}].roLineId" />
									<html:hidden name="salesLineList" property="salesDetailCategory" indexed="true" styleId="salesLineList[${s.index}].salesDetailCategory" />
									<html:hidden name="salesLineList" property="customerPcode" indexed="true" styleId="salesLineList[${s.index}].customerPcode" />
									<html:hidden name="salesLineList" property="productAbstract" indexed="true" styleId="salesLineList[${s.index}].productAbstract" />
									<html:hidden name="salesLineList" property="unitCategory" indexed="true" styleId="salesLineList[${s.index}].unitCategory" />
									<html:hidden name="salesLineList" property="unitName" indexed="true" styleId="salesLineList[${s.index}].unitName" />
									<html:hidden name="salesLineList" property="packQuantity" indexed="true" styleId="salesLineList[${s.index}].packQuantity" />
									<html:hidden name="salesLineList" property="taxCategory" indexed="true" styleId="salesLineList[${s.index}].taxCategory" />
									<html:hidden name="salesLineList" property="ctaxRate" indexed="true" styleId="salesLineList[${s.index}].ctaxRate" />
									<html:hidden name="salesLineList" property="ctaxPrice" indexed="true" styleId="salesLineList[${s.index}].ctaxPrice" />
									<html:hidden name="salesLineList" property="gm" indexed="true" styleId="salesLineList[${s.index}].gm" />
									<html:hidden name="salesLineList" property="setTypeCategory" indexed="true" styleId="salesLineList[${s.index}].setTypeCategory" />
									<html:hidden name="salesLineList" property="roQuantity" indexed="true" styleId="salesLineList[${s.index}].roQuantity" />
									<html:hidden name="salesLineList" property="bkQuantity" indexed="true" styleId="salesLineList[${s.index}].bkQuantity" />
									<html:hidden name="salesLineList" property="roMaxNum" indexed="true" styleId="salesLineList[${s.index}].roMaxNum" />
									<html:hidden name="salesLineList" property="possibleDrawQuantity" indexed="true" styleId="salesLineList[${s.index}].possibleDrawQuantity" />
									<html:hidden name="salesLineList" property="stockCtlCategory" indexed="true" styleId="salesLineList[${s.index}].stockCtlCategory" />
									// 関連する受注伝票の情報
									// 受注伝票行ID
									// 数量
								</td>

								<!-- 商品コード -->
								<td style="text-align: right; background-color: #fae4eb;">
									<div class="box_1of1" style="margin: 5px;">
										<html:text tabindex="${1000+s.index*16}" name="salesLineList" property="productCode"
											style="width: 120px; ime-mode:disabled;" indexed="true" styleId="salesLineList[${s.index}].productCode" maxlength="20" />
										<html:image styleId="productCodeImg${s.index}" tabindex="${1001+s.index*16}" src="${f:url('/images//customize/btn_search.png')}"
											style="width: auto; vertical-align: middle; cursor: pointer;" />
									</div>
								</td>

								<!-- 商品名・摘要、商品備考 -->
								<td  style="white-space: normal">
									<div class="box_1of2" id="productAbstract${s.index}" style="position: static; white-space: normal;">
										<c:out value="${salesLineList.productAbstract}" />
									</div>
									<div class="box_2of2">
										<html:textarea name="salesLineList"  styleId="salesLineList[${s.index}].productRemarks" property="productRemarks"
											tabindex="${1002+s.index*16}" readonly="true" styleClass="c_disable" indexed="true" style="margin: 2px 0 7px 0; width: 110px;"/>
									</div>
								</td>

								<!-- 数量※ -->
								<td style="background-color: #fae4eb;">
									<div class="box_1of2" style="border-bottom: 0;">
										<html:text tabindex="${1003+s.index*16}" name="salesLineList" property="quantity"
											style="width: 40px; ime-mode:disabled;" indexed="true" styleId="salesLineList[${s.index}].quantity"  styleClass="numeral_commas" maxlength="6" />
									</div>
									<div class="box_2of2">
										<button id="stockBtn${s.index}" tabindex="${1005+s.index*16}" class="btn_list_action">在庫</button>
									</div>
								</td>

								<!-- 完納区分※ -->
								<td style="background-color: #fae4eb;">
									<div class="box_1of1" style="width: 80px; margin: 5px;">
										<html:select tabindex="${1006+s.index*16}" name="salesLineList" property="deliveryProcessCategory"
											styleId="salesLineList[${s.index}].deliveryProcessCategory"  indexed="true" >
											<c:forEach var="dpcl" items="${delivertProcessCategoryList}">
												<html:option value="${dpcl.value}">${dpcl.label}</html:option>
											</c:forEach>
										</html:select>
									</div>
								</td>

								<!-- 仕入単価※、仕入金額※ -->
								<td style="background-color: #fae4eb;">
									<div class="box_1of2">
										<html:text tabindex="${1007+s.index*16}" name="salesLineList" property="unitCost"
											style="width: 70px; ime-mode:disabled;" indexed="true" styleId="salesLineList[${s.index}].unitCost"
											styleClass="numeral_commas c_disable" readonly="true" maxlength="9" />
									</div>
									<div class="box_2of2">
										<html:text tabindex="${1008+s.index*16}" name="salesLineList" property="cost"
											style="width: 70px; ime-mode:disabled;" indexed="true" styleId="salesLineList[${s.index}].cost"
											styleClass="numeral_commas c_disable" readonly="true" maxlength="9" />
									</div>
								</td>

								<!-- 売上単価※、売価金額※ -->
								<td style="background-color: #fae4eb;">
									<div class="box_1of2">
										<html:text tabindex="${1009+s.index*16}" name="salesLineList" property="unitRetailPrice"
											style="width: 70px; ime-mode:disabled;" indexed="true" styleId="salesLineList[${s.index}].unitRetailPrice" styleClass="numeral_commas" maxlength="9" />
									</div>
									<div class="box_2of2">
										<html:text tabindex="${1010+s.index*16}" name="salesLineList" property="retailPrice"
											style="width: 70px; ime-mode:disabled;" indexed="true" styleId="salesLineList[${s.index}].retailPrice" styleClass="numeral_commas" maxlength="9" />
									</div>
								</td>

								<!-- 備考、ピッキング備考 -->
								<td>
									<div class="box_1of2">
										<html:textarea tabindex="${1011+s.index*16}" name="salesLineList" property="remarks" style="margin: 2px 0 7px 0; width: 150px; ime-mode:active;"
										indexed="true" styleId="salesLineList[${s.index}].remarks" />
									</div>
									<div class="box_2of2">
										<html:textarea tabindex="${1012+s.index*16}" name="salesLineList" property="eadRemarks" style="margin: 2px 0 7px 0; width: 150px; ime-mode:active;"
										indexed="true" styleId="salesLineList[${s.index}].eadRemarks" readonly="true" styleClass="c_disable"/>
									</div>
								</td>

								<!-- 棚番 -->
								<td style="text-align: right;">
									<div class="box_1of1" style="margin: 5px;">
										<html:text tabindex="${1013+s.index*16}" name="salesLineList" property="rackCodeSrc" readonly="true" styleClass="c_disable"
											style="width: 55px; ime-mode:inactive;" indexed="true" styleId="salesLineList[${s.index}].rackCodeSrc" maxlength="7" />
									</div>
								</td>

								<!--  -->
								<td>
									<div class="box_1of2">
									<c:if test="${closed || !menuUpdate}">
										<button name="salesLineList" id="deleteBtn${s.index}" style="width:70px;" tabindex="${1014+s.index*16}" class="btn_list_action" disabled="disabled" >削除</button>
									</c:if>
									<c:if test="${!closed && menuUpdate}">
										<button name="salesLineList" id="deleteBtn${s.index}" style="width:70px;" tabindex="${1014+s.index*16}" class="btn_list_action" >削除</button>
									</c:if>
									</div>
									<div class="box_2of2">
									<c:if test="${s.first}" >
										<button name="copyLine" id="copyBtn${s.index}" style="width:70px;" tabindex="${1015+s.index*16}" class="btn_list_action" disabled="disabled" >前行複写</button>
									</c:if>
									<c:if test="${!s.first}" >
										<c:if test="${closed || !menuUpdate}">
											<button name="copyLine" id="copyBtn${s.index}" style="width:70px;" tabindex="${1015+s.index*16}" class="btn_list_action" disabled="disabled" >前行複写</button>
										</c:if>
										<c:if test="${!closed && menuUpdate}">
											<button name="copyLine" id="copyBtn${s.index}" style="width:70px;" tabindex="${1015+s.index*16}" class="btn_list_action">前行複写</button>
										</c:if>
									</c:if>
									</div>
								</td>
							</tr>
							</c:if>
						</c:forEach>

						<!-- 追加ボタン -->
						<tr id="trAddLine">
							<td style="height: 60px; text-align: center" colspan="10" class="rd_bottom_left rd_bottom_right">
							<c:if test="${closed || !menuUpdate}">
								<!--<html:button tabindex="1999" styleId ="addLine" property="addLine" disabled="disabled" styleClass="btn_small">追加</html:button>-->

								<button type="button" tabindex="1999" id="addRowBtn" style="width:80px;" disabled="disabled" >
									<img alt="行追加" border="none" src="${f:url('/images/customize/btn_line_add.png')}"  width="31" height="33">
								</button>
							</c:if>
							<c:if test="${!closed && menuUpdate}">
								<!--<html:button tabindex="1999" styleId ="addLine" property="addLine" onclick="addRow();" style="cursor: pointer;" styleClass="btn_small">追加</html:button>-->

								<button type="button" tabindex="1999" id="addRowBtn" style="width:80px; cursor: pointer;" onclick="addRow();" onkeypress="addRow();">
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
							<th style="height: 60px;" class="rd_top_left">粗利益</th>
							<th>粗利益率</th>
							<th>金額合計</th>
							<th>消費税</th>
							<th class="rd_top_right">伝票合計</th>
						</tr>
						<tr>
							<td id="gmTotal" style="text-align: center; height: 100px;" class="BDCyen yen_value">
								&nbsp;<c:out value="${f:h(gmTotal)}" />
							</td>
							<td id="gmTotalPer" style="text-align: center" class="numeral_commas">
								&nbsp;<c:out value="${f:h(gmTotalPer)}" />
							</td>
							<td id="priceTotal" style="text-align: center" class="BDCyen yen_value">
								&nbsp;<c:out value="${f:h(priceTotal)}" />
							</td>
							<td id="ctaxPriceTotal" style="text-align: center" class="BDCyen yen_value">
								&nbsp;<c:out value="${f:h(ctaxPriceTotal)}" />
							</td>
							<td id="total" style="text-align: center" class="BDCyen yen_value">
								&nbsp;<c:out value="${f:h(total)}" />
							</td>
						</tr>
					</table>
				</div>
				</div>

				<div style="width: 1160px; text-align: center; margin-top: 10px;">
					<c:if test="${newData}" >
						<c:if test="${closed || !menuUpdate}">
							<button type="button" tabindex="1999" id="btnF3btm" style="width:260px; height:51px;" class="btn_medium" disabled>
								<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.register'/></span><%// 登録 %>
							</button>
						</c:if>
						<c:if test="${!closed && menuUpdate}">
							<button type="button" tabindex="1999" id="btnF3btm" style="width:260px; height:51px;" class="btn_medium" onclick="onF3();">
								<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.register'/></span><%// 登録 %>
							</button>
						</c:if>
					</c:if>
					<c:if test="${!newData}" >
						<c:if test="${closed || !menuUpdate}">
							<button type="button" tabindex="1999" id="btnF3btm" style="width:260px; height:51px;" class="btn_medium" disabled>
								<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.renew'/></span><%// 更新 %>
							</button>
						</c:if>
						<c:if test="${!closed && menuUpdate}">
							<button type="button" tabindex="1999" id="btnF3btm" style="width:260px; height:51px;" class="btn_medium" onclick="onF3();">
								<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.renew'/></span><%// 更新 %>
							</button>
						</c:if>
					</c:if>
				</div>
			</div>
		</s:form>
	</div>

	<div id="output_dialog" title="出力選択" style="display: none;">
		<div style="padding: 20px 20px 0 20px;">
			<form name="printForm" method="post" style="margin: 0px;">
				<table class="forms" style="width: 260px;" summary="出力選択">
					<tr style="display: none;">
						<td>
							<input type="hidden" id="printId" name="printId" />
							<input type="hidden" id="printBillDatePrint" name="printBillDatePrint" />
						</td>
					</tr>
					<tr id="spanJ">
						<td><input type="checkbox" id="typeJ" name="typeJ" class="dummy" tabindex="3006" onclick="checkPrint();" />ピッキングリスト</td>
					</tr>
					<tr id="spanA">
						<td><input type="checkbox" id="typeA" name="typeA" class="dummy" tabindex="3000" onclick="checkPrint();" />見積書</td>
					</tr>
					<tr id="spanH">
						<td><input type="checkbox" id="typeH" name="typeH" class="dummy" tabindex="3002" onclick="checkPrint();" />請求書</td>
					</tr>
					<tr id="spanD">
						<td><input type="checkbox" id="typeD" name="typeD" class="dummy" tabindex="3003" onclick="checkPrint();" />納品書</td>
					</tr>
					<tr id="spanC">
						<td><input type="checkbox" id="typeC" name="typeC" class="dummy" tabindex="3004" onclick="checkPrint();" />納品書</td>
					</tr>
					<tr id="spanF">
						<td><input type="checkbox" id="typeF" name="typeF" class="dummy" tabindex="3007" onclick="checkPrint();" />納品書兼領収書</td>
					</tr>
					<tr id="spanE">
						<td><input type="checkbox" id="typeE" name="typeE" class="dummy" tabindex="3005" onclick="checkPrint();" />仮納品書</td>
					</tr>
					<tr id="spanG">
						<td><input type="checkbox" id="typeG" name="typeG" class="dummy" tabindex="3001" onclick="checkPrint();" />請求書</td>
					</tr>
				</table>
			</form>
			<div style="width: 96%; text-align: right; margin-top: 15px;">
				<button id="doPrintButton" tabindex="3008" onclick="doPrint()" >出力</button>
				<button tabindex="3009" onclick="$('#output_dialog').dialog('close');">閉じる</button>
			</div>
		</div>
	</div>
</body>
</html>