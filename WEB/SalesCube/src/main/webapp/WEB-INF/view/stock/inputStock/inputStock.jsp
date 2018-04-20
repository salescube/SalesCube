<%@page import="jp.co.arkinfosys.common.CategoryTrns" %>
<%@page import="jp.co.arkinfosys.common.Constants"%>
<%@page import="jp.co.arkinfosys.form.AbstractSlipEditForm"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/> <bean:message key='titles.inputStock'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>
	<c:set var="code_size_rack" value="<%=Constants.CODE_SIZE.RACK%>" />
	<script type="text/javascript">
	<!--
		var MAX_LINE_ROW_COUNT = <%= AbstractSlipEditForm.MAX_LINE_SIZE %>;
		var MAIN_FORM_NAME = "stock_inputStockActionForm";
		var maxIndex = 0;
		var trCloneBase = null;
		var baseIndex = 0;

		// ページ読込時の動作
		$(document).ready(function(){

			// 明細行のIndex管理
			var maxLineNo = $("#tbodyLine").children().length-2;
			var maxLineId = $("#tbodyLine").children().eq(maxLineNo).attr("id");
			maxIndex = maxLineId.replace("trLine", "");
			// 明細行のクローンを生成
			trCloneBase = $("#tbodyLine").children(":first").clone(true);
			baseIndex = trCloneBase.attr("id").replace("trLine", "");
			// 明細行の項目にイベントをバインド
			for(var i=0; i<=maxIndex; i++) {
				$("#eadLineTrnDtoList\\[" + i + "\\]\\.productCode").bind("focus", {index: i}, function(e){ this.curVal=this.value; });
				$("#eadLineTrnDtoList\\[" + i + "\\]\\.productCode").bind("blur", {index: i}, function(e){ if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); changeProductCode(e); } });
				$("#productCodeImg" + i).bind("click", {index: i}, openProductSearchDialog);
				$("#eadLineTrnDtoList\\[" + i + "\\]\\.rackCode").bind("focus", {index: i}, function(e){ this.curVal=this.value; });
				$("#eadLineTrnDtoList\\[" + i + "\\]\\.rackCode").bind("blur", {index: i}, function(e){ if(this.curVal!=this.value){ changeRackCode(e); } });
				$("#eadLineTrnDtoList\\[" + i + "\\]\\.quantity").bind("focus", {index: i}, function(e){ this.curVal=this.value; });
				$("#eadLineTrnDtoList\\[" + i + "\\]\\.quantity").bind("blur", {index: i}, function(e){ if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); checkQuantity(this.value, e); } });
				$("#rackCodeImg" + i).bind("click", {index: i}, openRackSearchDialog);
				$("#deleteBtn" + i).bind("click", {index: i}, deleteRow);
				$("#copyBtn" + i).bind("click", {index: i}, copyRow);
				applyNumeralStylesToObj(${mineDto.productFractCategory}, ${mineDto.numDecAlignment}, $("#eadLineTrnDtoList\\[" + i + "\\]\\.quantity"));
				applyNumeralStylesToObj(${mineDto.productFractCategory}, ${mineDto.numDecAlignment}, $("#eadLineTrnDtoList\\[" + i + "\\]\\.stockCount"));
				applyNumeralStylesToObj(${mineDto.productFractCategory}, ${mineDto.numDecAlignment}, $("#eadLineTrnDtoList\\[" + i + "\\]\\.updateQuantity"));
			}
			// 初期フォーカス設定
//			$("#eadDate").focus();
			if( $("#eadSlipId").val() != "" ){
				$("#eadSlipId").attr("readOnly", "true");
				$("#eadSlipId").addClass("c_disable");
				$("#eadDate").focus();
			}else{
				$("#eadSlipId").focus();
			}
		});

		function findSlip(){
			if( $("#eadSlipId").val() == "" ){
			}else{
				showNowSearchingDiv();
				$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/stock/inputStock/load")}');
				$("form[name='" + MAIN_FORM_NAME + "']").submit();
			}
		}

		// 初期化
		function onF1(){
			// 入力内容を初期化してよろしいですか？
			if(confirm('<bean:message key="confirm.init" />')){
				showNowSearchingDiv();

				// F1ボタンを押下すると、常に新規とする。
				<bean:define id="concatUrl" value="${'/stock/inputStock'}" />
				location.doHref('${f:url(concatUrl)}');

			}
		}

		// 削除
		function onF2(){
			// このデータを削除しますか？
			if(confirm('<bean:message key="confirm.delete" />')){
				showNowSearchingDiv();
				$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/stock/inputStock/delete")}');
				$("form[name='" + MAIN_FORM_NAME + "']").submit();
			}
		}

		// 登録
		function onF3(){
			// 入力内容を登録します。よろしいですか？
			if(confirm('<bean:message key="confirm.insert" />')){
				showNowSearchingDiv();
				_before_submit($(".numeral_commas"));
				$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/stock/inputStock/upsert")}');
				$("form[name='" + MAIN_FORM_NAME + "']").submit();
			}
		}

		// 行削除
		function deleteRow(event){
			var i, lineNo, lineSize, index;
			var trId, id;

			index = event.data.index;

			if(confirm('<bean:message key="confirm.line.delete" />')){
				// 行を削除する
				lineNo = parseInt($("#eadLineTrnDtoList\\["+index+"\\]\\.lineNo").val())-1;
				$("#tbodyLine").get(0).deleteRow(lineNo);
				// 行番号を調整する
				lineSize = $("#tbodyLine").get(0).children.length-1;
				for(i=lineNo; i<lineSize; i++) {
					trId = $("#tbodyLine").get(0).children[i].id;
					id = trId.replace("trLine", "");
					// 行番号を振りなおす
					$("#tdNo"+id).html(i+1);
					$("#eadLineTrnDtoList\\["+id+"\\]\\.lineNo").val(i+1);
					// 先頭行の場合、前行複写ボタンの不活性化
					if(i == 0) {
						$("#copyBtn"+id).get(0).disabled = true;
					}
				}
				// 残り１行の場合、削除ボタンの不活性化
				if(lineSize == 1) {
					trId = $("#tbodyLine").get(0).children[0].id;
					id = trId.replace("trLine", "");
					$("#deleteBtn"+id).get(0).disabled = true;
				}
			}
		}

		// 前行複写
		function copyRow(event){
			var prevIndex;
			var i, index;

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
		}

		// 行追加
		function addRow(){
			var elemTr, elemTd, elemWork;
			var lineSize, tabIdx;
			var trId, id;

			// 最大行数の確認
			lineSize = $("#tbodyLine").get(0).children.length-1;
			if(lineSize >= MAX_LINE_ROW_COUNT) {
				alert('<bean:message key="errors.line.maxrows" />');
				return;
			}

			// 明細行のIndex管理
			maxIndex++;
			// タブインデックスの計算
			tabIdx = 1000 + maxIndex * ${f:h(lineElementCount)} - 1;

			// ベースオブジェクトからクローンを生成
			elemTr = trCloneBase.clone(true);
			elemTr.attr("id", "trLine" + maxIndex);
			// No列の設定
			elemTd = elemTr.children(":first");
			elemTd.attr("id", "tdNo" + maxIndex);
			elemTd.html(lineSize + 1);
			// Hidden列の設定
			elemTd = elemTd.next();
			elemWork = elemTd.children("#eadLineTrnDtoList\\[" + baseIndex + "\\]\\.lineNo");
			elemWork.attr("id", "eadLineTrnDtoList[" + maxIndex + "].lineNo");
			elemWork.attr("name", "eadLineTrnDtoList[" + maxIndex + "].lineNo");
			elemWork.val(lineSize + 1);
			elemWork = elemTd.children("#eadLineTrnDtoList\\[" + baseIndex + "\\]\\.eadLineId");
			elemWork.attr("id", "eadLineTrnDtoList[" + maxIndex + "].eadLineId");
			elemWork.attr("name", "eadLineTrnDtoList[" + maxIndex + "].eadLineId");
			elemWork.val("");
			elemWork = elemTd.children("#eadLineTrnDtoList\\[" + baseIndex + "\\]\\.productAbstract");
			elemWork.attr("id", "eadLineTrnDtoList[" + maxIndex + "].productAbstract");
			elemWork.attr("name", "eadLineTrnDtoList[" + maxIndex + "].productAbstract");
			elemWork.val("");
			elemWork = elemTd.children("#eadLineTrnDtoList\\[" + baseIndex + "\\]\\.rackName");
			elemWork.attr("id", "eadLineTrnDtoList[" + maxIndex + "].rackName");
			elemWork.attr("name", "eadLineTrnDtoList[" + maxIndex + "].rackName");
			elemWork.val("");
			// 商品コード列の設定
			elemTd = elemTd.next();
			elemWork = elemTd.children().children("#eadLineTrnDtoList\\[" + baseIndex + "\\]\\.productCode");
			elemWork.attr("id", "eadLineTrnDtoList[" + maxIndex + "].productCode");
			elemWork.attr("name", "eadLineTrnDtoList[" + maxIndex + "].productCode");
			elemWork.attr("tabindex", (++tabIdx));
			elemWork.bind("focus", {index: maxIndex}, function(e){ this.curVal=this.value; });
			elemWork.bind("blur", {index: maxIndex}, function(e){ if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); changeProductCode(e); } });
			elemWork.val("");
			elemWork = elemTd.children().children("#productCodeImg" + baseIndex);
			elemWork.attr("id", "productCodeImg" + maxIndex);
			elemWork.attr("tabindex", (++tabIdx));
			elemWork.bind("click", {index: maxIndex}, openProductSearchDialog);
			// 商品名列の設定
			elemTd = elemTd.next();
			elemWork = elemTd.children().children("#productAbstract" + baseIndex);
			elemWork.attr("id", "productAbstract" + maxIndex);
			elemWork.text("");
			// 数量列の設定
			elemTd = elemTd.next();
			elemWork = elemTd.children().children("#eadLineTrnDtoList\\[" + baseIndex + "\\]\\.quantity");
			elemWork.attr("id", "eadLineTrnDtoList[" + maxIndex + "].quantity");
			elemWork.attr("name", "eadLineTrnDtoList[" + maxIndex + "].quantity");
			elemWork.attr("tabindex", (++tabIdx));
			elemWork.bind("focus", {index: maxIndex}, function(e){ this.curVal=this.value; });
			elemWork.bind("blur", {index: maxIndex}, function(e){ if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); checkQuantity(this.value, e); } });
			applyNumeralStylesToObj(${mineDto.productFractCategory}, ${mineDto.numDecAlignment}, elemWork);
			elemWork.val("");
			elemWork = elemTd.children().children("#eadLineTrnDtoList\\[" + baseIndex + "\\]\\.stockCount");
			elemWork.attr("id", "eadLineTrnDtoList[" + maxIndex + "].stockCount");
			elemWork.attr("name", "eadLineTrnDtoList[" + maxIndex + "].stockCount");
			applyNumeralStylesToObj(${mineDto.productFractCategory}, ${mineDto.numDecAlignment}, elemWork);
			elemWork.val("");
			elemWork = elemTd.children().children("#eadLineTrnDtoList\\[" + baseIndex + "\\]\\.updateQuantity");
			elemWork.attr("id", "eadLineTrnDtoList[" + maxIndex + "].updateQuantity");
			elemWork.attr("name", "eadLineTrnDtoList[" + maxIndex + "].updateQuantity");
			applyNumeralStylesToObj(${mineDto.productFractCategory}, ${mineDto.numDecAlignment}, elemWork);
			elemWork.val("");
			// 「商品備考」「備考」列の設定
			elemTd = elemTd.next();
			elemWork = elemTd.children().children("#eadLineTrnDtoList\\[" + baseIndex + "\\]\\.productRemarks");
			elemWork.attr("id", "eadLineTrnDtoList[" + maxIndex + "].productRemarks");
			elemWork.attr("name", "eadLineTrnDtoList[" + maxIndex + "].productRemarks");
			elemWork.attr("tabindex", (++tabIdx));
			elemWork.val("");
			// 備考欄の設定(列は商品備考と同じ)
			elemWork = elemTd.children().children("#eadLineTrnDtoList\\[" + baseIndex + "\\]\\.remarks");
			elemWork.attr("id", "eadLineTrnDtoList[" + maxIndex + "].remarks");
			elemWork.attr("name", "eadLineTrnDtoList[" + maxIndex + "].remarks");
			elemWork.attr("tabindex", (++tabIdx));
			elemWork.val("");
			// 棚番列の設定
			elemTd = elemTd.next();
			elemWork = elemTd.children().children("#eadLineTrnDtoList\\[" + baseIndex + "\\]\\.rackCode");
			elemWork.attr("id", "eadLineTrnDtoList[" + maxIndex + "].rackCode");
			elemWork.attr("name", "eadLineTrnDtoList[" + maxIndex + "].rackCode");
			elemWork.attr("tabindex", (++tabIdx));
			elemWork.bind("focus", {index: maxIndex}, function(e){ this.curVal=this.value; });
			elemWork.bind("blur", {index: maxIndex}, function(e){ if(this.curVal!=this.value){ changeRackCode(e); } });
			elemWork.val("");
			elemWork = elemTd.children().children("#rackCodeImg" + baseIndex);
			elemWork.attr("id", "rackCodeImg" + maxIndex);
			elemWork.attr("tabindex", (++tabIdx));
			elemWork.bind("click", {index: maxIndex}, openRackSearchDialog);
			// ボタン列の設定
			elemTd = elemTd.next();
			elemWork = elemTd.children().children("#deleteBtn" + baseIndex);
			elemWork.attr("id", "deleteBtn" + maxIndex);
			elemWork.attr("tabindex", (++tabIdx));
			elemWork.bind("click", {index: maxIndex}, deleteRow);
			elemWork = elemTd.children().children("#copyBtn" + baseIndex);
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

			// １行目の削除ボタンの活性化
			trId = $("#tbodyLine").get(0).children[0].id;
			id = trId.replace("trLine", "");
			$("#deleteBtn"+id).get(0).disabled = false;
		}

		// 明細行の空行判定
		function isEmptyLine(lineNo){
			var retVal = true;

			// 商品コード
			if($("#eadLineTrnDtoList\\["+lineNo+"\\]\\.productCode").val() != "") {
				retVal = false;
			}
			// 数量
			if($("#eadLineTrnDtoList\\["+lineNo+"\\]\\.quantity").val() != "") {
				retVal = false;
			}
			// 備考
			if($("#eadLineTrnDtoList\\["+lineNo+"\\]\\.remarks").val() != "") {
				retVal = false;
			}
			// 棚番コード
			if($("#eadLineTrnDtoList\\["+lineNo+"\\]\\.rackCode").val() != "") {
				retVal = false;
			}

			return retVal;
		}

		// 明細行のコピー
		function copyLine(destIndex, srcIndex){
			// 商品コード
			$("#eadLineTrnDtoList\\["+destIndex+"\\]\\.productCode").val($("#eadLineTrnDtoList\\["+srcIndex+"\\]\\.productCode").val());
			// 商品名（表示用）
			$("#productAbstract"+destIndex).text($("#productAbstract"+srcIndex).text());
			// 商品名
			$("#eadLineTrnDtoList\\["+destIndex+"\\]\\.productAbstract").val($("#eadLineTrnDtoList\\["+srcIndex+"\\]\\.productAbstract").val());
			// 数量
			$("#eadLineTrnDtoList\\["+destIndex+"\\]\\.quantity").val($("#eadLineTrnDtoList\\["+srcIndex+"\\]\\.quantity").val());
			// 在庫
			$("#eadLineTrnDtoList\\["+destIndex+"\\]\\.stockCount").val($("#eadLineTrnDtoList\\["+srcIndex+"\\]\\.stockCount").val());
			// 変更在庫
			$("#eadLineTrnDtoList\\["+destIndex+"\\]\\.updateQuantity").val($("#eadLineTrnDtoList\\["+srcIndex+"\\]\\.updateQuantity").val());
			// 商品備考
			$("#eadLineTrnDtoList\\["+destIndex+"\\]\\.productRemarks").val($("#eadLineTrnDtoList\\["+srcIndex+"\\]\\.productRemarks").val());
			// 備考
			$("#eadLineTrnDtoList\\["+destIndex+"\\]\\.remarks").val($("#eadLineTrnDtoList\\["+srcIndex+"\\]\\.remarks").val());
			// 棚番コード
			$("#eadLineTrnDtoList\\["+destIndex+"\\]\\.rackCode").val($("#eadLineTrnDtoList\\["+srcIndex+"\\]\\.rackCode").val());
			// 棚番名
			$("#eadLineTrnDtoList\\["+destIndex+"\\]\\.rackName").val($("#eadLineTrnDtoList\\["+srcIndex+"\\]\\.rackName").val());
		}

		// 指定行から前行のIndexを返す
		// （見つからない場合は-1を返す）
		function getPrevIndex(index){
			var retVal = -1;
			var i, lineNo, trId;

			// 指定行の行番号を取得
			lineNo = parseInt($("#eadLineTrnDtoList\\["+index+"\\]\\.lineNo").val());
			// 前行を探す（削除行は非表示になっている為、直前の削除されていない行を探す）
			if(lineNo>1) {
				trId = $("#tbodyLine").get(0).children[lineNo-2].id;
				retVal = trId.replace("trLine", "");
			}

			return retVal;
		}

		// 商品コード変更
		function changeProductCode(event) {
			searchProductCode(event.data.index);
		}

		// 商品検索
		function searchProductCode(index) {
			var map = new Object();
			var lineNo = parseInt($("#eadLineTrnDtoList\\["+index+"\\]\\.lineNo").val());
			var label = '<bean:message key="labels.productCode" />';
			var code = $("#eadLineTrnDtoList\\["+index+"\\]\\.productCode").val();

			if($("#eadLineTrnDtoList\\["+index+"\\]\\.productCode").val() == "") {
				map["productCode"] = "";
				map["productName"] = "";
				map["stockCount"] = "";
				map["rackCode"] = "";
				// 変更後在庫数
				$("#eadLineTrnDtoList\\["+index+"\\]\\.updateQuantity").val("");
				setProductInfo(index, map);
				return;
			}
			var data = new Object();
			data["productCode"] = $("#eadLineTrnDtoList\\["+index+"\\]\\.productCode").val();
			asyncRequest(
				contextRoot + "/ajax/commonProduct/getProductInfos",
				data,
				function(data) {
					if(data==""){
						alert('<bean:message key="errors.notExist" arg0="'+label+'" />');
						//map["productCode"] = "";
						map["productName"] = "";
						map["stockCount"] = "";
						map["rackCode"] = "";
						// 変更後在庫数
						$("#eadLineTrnDtoList\\["+index+"\\]\\.updateQuantity").val("");
						setProductInfo(index, map);
					} else {
						var value = eval("(" + data + ")");
						// 商品コード
						$("#eadLineTrnDtoList\\["+index+"\\]\\.productCode").val(value.productCode);
						// 商品名（表示用）
						$("#productAbstract"+index).text(value.productName);
						// 商品名
						$("#eadLineTrnDtoList\\["+index+"\\]\\.productAbstract").val(value.productName);
						// 現在庫数
						var stockCount = "";
						if(value.currentTotalQuantity) {
							stockCount = value.currentTotalQuantity;
						}
						$("#eadLineTrnDtoList\\["+index+"\\]\\.stockCount").val(stockCount);
						SetBigDecimalScale_Obj($("#eadLineTrnDtoList\\["+index+"\\]\\.stockCount"));
						// 棚番
						$("#eadLineTrnDtoList\\["+index+"\\]\\.rackCode").val(value.rackCode);

						// 商品備考
						$("#eadLineTrnDtoList\\["+index+"\\]\\.productRemarks").val(value.remarks);

						// 変更後在庫数
						changeUpdateQuantity(index);
					}
				}
			);
		}

		// 商品検索
		function openProductSearchDialog(event) {
			// 商品検索ダイアログを開く
			openSearchProductDialog(event.data.index, setProductInfoFromDialog );
			// 商品コードを設定する
			$("#"+event.data.index+"_productCode").val($("#eadLineTrnDtoList\\["+event.data.index+"\\]\\.productCode").val());
			// セット分類を設定する
			$("#"+event.data.index+"_setTypeCategory").val("${productSetSingle}");
		}

		// 商品検索後の設定処理
		function setProductInfoFromDialog(index, map) {
			setProductInfo(index, map);
			searchProductCode(index);
		}

		// 商品検索後の設定処理
		function setProductInfo(index, map) {
			// 商品コード
			$("#eadLineTrnDtoList\\["+index+"\\]\\.productCode").val(map["productCode"]);
			// 商品名（表示用）
			$("#productAbstract"+index).text(map["productName"]);
			// 商品名
			$("#eadLineTrnDtoList\\["+index+"\\]\\.productAbstract").val(map["productName"]);
			// 現在庫数
			$("#eadLineTrnDtoList\\["+index+"\\]\\.stockCount").val(map["stockCount"]);
			// 棚番
			$("#eadLineTrnDtoList\\["+index+"\\]\\.rackCode").val(map["rackCode"]);
		}

		// 棚番コード変更
		function changeRackCode(event) {
			searchRackCode(event.data.index);
		}

		// 棚検索
		function searchRackCode(index) {
			var map = new Object();
			var lineNo = parseInt($("#eadLineTrnDtoList\\["+index+"\\]\\.lineNo").val());
			var label = '<bean:message key="labels.rackCode" />';
			var code = $("#eadLineTrnDtoList\\["+index+"\\]\\.rackCode").val();

			if($("#eadLineTrnDtoList\\["+index+"\\]\\.rackCode").val() == "") {
				map["rackCode"] = "";
				map["rackName"] = "";
				setRackInfo(index, map);
				return;
			}
			var data = new Object();
			data["rackCode"] = $("#eadLineTrnDtoList\\["+index+"\\]\\.rackCode").val();
			asyncRequest(
				contextRoot + "/ajax/commonRack/getRackInfos",
				data,
				function(data) {
					if(data==""){
						alert('<bean:message key="errors.notExist" arg0="'+label+'" />');
						//map["rackCode"] = "";
						map["rackName"] = "";
					} else {
						var value = eval("(" + data + ")");
						map["rackCode"] = value.rackCode;
						map["rackName"] = value.rackName;
					}
					setRackInfo(index, map);
				}
			);
		}

		// 棚検索
		function openRackSearchDialog(event) {
			// 棚検索ダイアログを開く
			openSearchRackDialog(event.data.index, setRackInfo );
			// 棚番コードを設定する
			$("#"+event.data.index+"_rackCode").val($("#eadLineTrnDtoList\\["+event.data.index+"\\]\\.rackCode").val());
		}

		// 棚検索後の設定処理
		function setRackInfo(index, map) {
			// 棚番
			$("#eadLineTrnDtoList\\["+index+"\\]\\.rackCode").val(map["rackCode"]);
			// 棚番名
			$("#eadLineTrnDtoList\\["+index+"\\]\\.rackName").val(map["rackName"]);
		}

		/**
		 * 入力した数量と現在個数の比較チェックを行う
		 */
		function checkQuantity( quantity, event ) {

			var index = event.data.index;

			var stockQuantity = $("#eadLineTrnDtoList\\["+index+"\\]\\.stockCount").val();

			if(isNaN(dec_numeral_commas(quantity))) {
				return;
			}

			if(isNaN(dec_numeral_commas(stockQuantity))) {
				return;
			}

			// 変更後在庫数を表示
			var updQuantity;
			if($("#eadCategory").val() == "<%=CategoryTrns.EAD_CATEGORY_ENTER%>")  // 入庫
				updQuantity = Number(dec_numeral_commas(stockQuantity))+Number(dec_numeral_commas(quantity));
			else
				updQuantity = Number(dec_numeral_commas(stockQuantity))-Number(dec_numeral_commas(quantity));

			$("#eadLineTrnDtoList\\["+index+"\\]\\.updateQuantity").val(updQuantity);

			// カンマをつける
			_after_load($(".numeral_commas"));

			if($("#eadCategory").val() != "<%=CategoryTrns.EAD_CATEGORY_DISPATCH%>") {
				return;
			}

			if(new Number(dec_numeral_commas(quantity)) > new Number(dec_numeral_commas(stockQuantity))) {
				var message = "<bean:message key="errors.line.dispatchQuantity.over" arg0="##"/>";
				message = message.replace("##", index + 1);
				alert(message);
			}
		}
		// 入出庫区分変更
		function changeCategory(){

			for(var i=0; i<=maxIndex; i++) {
				if( $("#eadLineTrnDtoList\\[" + i + "\\]\\.productCode").val() != ""){

					var stockCount = $("#eadLineTrnDtoList\\[" + i + "\\]\\.stockCount").val();
					var quantity = $("#eadLineTrnDtoList\\[" + i + "\\]\\.quantity").val();

					if(isNaN(dec_numeral_commas(stockCount)))
						continue;

					if(isNaN(dec_numeral_commas(quantity)))
						continue;

					// 変更後在庫数を再表示
					var updQuantity;
					if($("#eadCategory").val() == "<%=CategoryTrns.EAD_CATEGORY_ENTER%>")  // 入庫
						updQuantity = Number(dec_numeral_commas(stockCount)) + Number(dec_numeral_commas(quantity));
					else
						updQuantity = Number(dec_numeral_commas(stockCount)) - Number(dec_numeral_commas(quantity));

					$("#eadLineTrnDtoList\\["+i+"\\]\\.updateQuantity").val(updQuantity);

				}
			}
			// カンマをつける
			_after_load($(".numeral_commas"));
		}

		function changeUpdateQuantity(index){

			if( $("#eadLineTrnDtoList\\[" + index + "\\]\\.productCode").val() != ""){

				var stockCount = $("#eadLineTrnDtoList\\[" + index + "\\]\\.stockCount").val();
				var quantity = $("#eadLineTrnDtoList\\[" + index + "\\]\\.quantity").val();

				if(isNaN(dec_numeral_commas(stockCount)))
					return;

				if(isNaN(dec_numeral_commas(quantity)))
					return;

				if(quantity == ""){
					return;
				}

				// 変更後在庫数を再表示
				var updQuantity;
				if($("#eadCategory").val() == "<%=CategoryTrns.EAD_CATEGORY_ENTER%>")  // 入庫
					updQuantity = Number(dec_numeral_commas(stockCount)) + Number(dec_numeral_commas(quantity));
				else
					updQuantity = Number(dec_numeral_commas(stockCount)) - Number(dec_numeral_commas(quantity));

				$("#eadLineTrnDtoList\\["+index+"\\]\\.updateQuantity").val(updQuantity);

			}
			// カンマをつける
			_after_load($(".numeral_commas"));
		}

	-->
	</script>
</head>
<body>
	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0010"/>
		<jsp:param name="MENU_ID" value="1000"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">

		<span class="title"><bean:message key='titles.inputStock'/></span>

		<div class="function_buttons">
			<button type="button" id="btnF1" tabindex="2000" onclick="onF1();">F1<br><bean:message key='words.action.initialize'/><%// 初期化 %></button>
			<button type="button" id="btnF2" tabindex="2001" onclick="onF2();" ${newData||cuttOff||!menuUpdate?"disabled":""}>F2<br><bean:message key='words.action.delete'/><%// 削除 %></button>
			<button type="button" id="btnF3" tabindex="2002" onclick="onF3();" ${newData&&menuUpdate?"":"disabled"}>F3<br><bean:message key='words.action.register'/><%// 登録 %></button>
			<button type="button" id="btnF4" tabindex="2003" disabled>F4<br>&nbsp;</button>
			<button type="button" id="btnF5" tabindex="2004" disabled>F5<br>&nbsp;</button>
			<button type="button" id="btnF6" tabindex="2005" disabled>F6<br>&nbsp;</button>
			<button type="button" id="btnF7" tabindex="2006" disabled>F7<br>&nbsp;</button>
			<button type="button" id="btnF8" tabindex="2007" disabled>F8<br>&nbsp;</button>
			<button type="button" id="btnF9" tabindex="2008" disabled>F9<br>&nbsp;</button>
			<button type="button" id="btnF10" tabindex="2009" disabled>F10<br>&nbsp;</button>
			<button type="button" id="btnF11" tabindex="2010" disabled>F11<br>&nbsp;</button>
			<button type="button" id="btnF12" tabindex="2011" disabled>F12<br>&nbsp;</button>

		</div><br><br><br>

		<s:form onsubmit="return false;">

			<div class="function_forms">

				<div style="padding-left: 20px">
					<html:errors />
				</div>
				<div id="messages" style="color: blue;padding-left: 20px">
				    <c:if test="${cuttOff}">
						<bean:message key='errors.delete.stockclosed'/><br>
				    </c:if>
					<html:messages id="msg" message="true">
						<bean:write name="msg" ignore="true"/><br>
					</html:messages>
				</div>


			    <div class="form_section_wrap">
   				<div class="form_section">
       			<div class="section_title">
					<span><bean:message key='labels.stockSlipInfos'/></span><br>
        			<button class="btn_toggle" />
      				 </div>
       			<div id="order_section" class="section_body">

				<table id="order_info" class="forms" summary="入出庫伝票情報">
					<colgroup>
						<col span="1" style="width: 10%">
						<col span="1" style="width: 15%">
						<col span="1" style="width: 10%">
						<col span="1" style="width: 15%">
						<col span="1" style="width: 10%">
						<col span="1" style="width: 15%">
						<col span="1" style="width: 10%">
						<col span="1" style="width: 15%">
					</colgroup>
					<tr>
						<th><div class="col_title_right"><bean:message key='labels.eadSlipId'/></div></th><%// 入出庫番号 %>
						<td><html:text property="eadSlipId" styleId="eadSlipId"  style="width: 140px; ime-mode: disabled;" styleClass="" tabindex="100" readonly="false"  maxlength="10" onfocus="this.curVal=this.value;" onblur="if((this.curVal == '') || ((this.curVal != '')&&(this.curVal!=this.value))) {findSlip();}"/></td>
						<th><div class="col_title_right_req"><bean:message key='labels.eadDate'/><bean:message key='labels.must'/></div></th><%// 入出庫日 %>
						<td><html:text property="eadDate" styleId="eadDate" style="width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="101" maxlength="10" /></td>
						<th><div class="col_title_right"><bean:message key='labels.eadSlipCategory.short'/></div></th><%// 入出庫伝票区分 %>
						<td>
							<html:select property="eadSlipCategory" tabindex="102">
								<html:options collection="slipCategoryList" property="value" labelProperty="label"/>
							</html:select>
						</td>
						<th><div class="col_title_right"><bean:message key='labels.eadCategory'/></div></th><%// 入出庫区分 %>
						<td>
							<html:select styleId="eadCategory" property="eadCategory" tabindex="103" onchange="changeCategory();">
								<html:options collection="categoryList" property="value" labelProperty="label"/>
							</html:select>
						</td>
					</tr>
					<tr>
						<th><div class="col_title_right"><bean:message key='labels.reason'/></div></th><%// 理由 %>
						<td colspan="5"><html:text property="remarks" styleClass="c_referable" style="width: 550px;" tabindex="104" maxlength="120" /></td>
						<th><div class="col_title_right"><bean:message key='labels.userName'/></div></th><%// 入力担当者 %>
						<td>
							<html:text property="userName" styleClass="c_disable" tabindex="105" readonly="true" />
						</td>
					</tr>
				</table>
				<html:hidden property="userId"/>
				<html:hidden property="stockPdate" />
				<html:hidden property="updDatetm" />

				</div>
				</div>
				</div>

   <div id="order_detail_info_wrap">
				<table id="order_detail_info" summary="入出庫明細リスト" class="forms" style="margin-top: 20px;">
					<thead>
						<tr>
							<th rowspan="3" class="rd_top_left" style="height: 60px; width: 30px;"><bean:message key='labels.lineNo'/></th><%// No %>
							<th rowspan="3" style="height: 60px; width: 210px;"><bean:message key='labels.productCode'/><bean:message key='labels.must'/></th><%// 商品コード %>
							<th rowspan="3" style="height: 60px;"><bean:message key='labels.productName'/></th><%// 商品名 %>
							<th style="height: 20px;"><bean:message key='labels.quantity'/><bean:message key='labels.must'/></th><%// 数量 %>
							<th rowspan="2" style="height: 30px; width: 300px;"><bean:message key='labels.productRemarks'/></th><%// 商品備考 %>
							<th rowspan="3" style="height: 60px; width: 130px;"><bean:message key='labels.rackCode'/><bean:message key='labels.must'/></th><%// 棚番 %>
							<th rowspan="3" class="rd_top_right" style="height: 60px; width: 100px;">&nbsp;</th>
						</tr>
						<tr>
							<th style="height: 20px;"><bean:message key='labels.stockQuantity'/></th><%// 現在庫数 %>
						</tr>
						<tr>
							<th style="height: 20px;"><bean:message key='labels.updateQuantity'/></th><%// 変更後在庫数 %>
							<th style="height: 30px;"><bean:message key='labels.remarks'/></th><%// 備考 %>
						</tr>
					</thead>
					<tbody id="tbodyLine">
						<% int lineTab = 400; %>
						<c:forEach var="eadLineTrnDtoList" items="${eadLineTrnDtoList}" varStatus="status">
							<c:if test='${eadLineTrnDtoList.lineNo != null}'>
								<tr id="trLine${status.index}">

									<!-- No -->
									<td id="tdNo${status.index}" style="text-align: center;">
									<div class="box_1of1">
										<c:out value="${eadLineTrnDtoList.lineNo}" />
									</div>
									</td>

									<td style="display: none;">
										<html:hidden name="eadLineTrnDtoList" property="lineNo" indexed="true" styleId="eadLineTrnDtoList[${status.index}].lineNo" />
										<html:hidden name="eadLineTrnDtoList" property="eadLineId" indexed="true" styleId="eadLineTrnDtoList[${status.index}].eadLineId" />
										<html:hidden name="eadLineTrnDtoList" property="productAbstract" indexed="true" styleId="eadLineTrnDtoList[${status.index}].productAbstract" />
										<html:hidden name="eadLineTrnDtoList" property="rackName" indexed="true" styleId="eadLineTrnDtoList[${status.index}].rackName" />
									</td>

									<!-- 商品コード -->
									<td style="background-color: #fae4eb;">
									<div class="box_1of1">
										<html:text name="eadLineTrnDtoList" property="productCode" indexed="true" styleId="eadLineTrnDtoList[${status.index}].productCode" styleClass="c_referable" style="width: 165px; ime-mode: disabled;" tabindex="<%=String.valueOf(lineTab++) %>" maxlength="20" />
										<html:image styleId="productCodeImg${status.index}" src='${f:url("/images/customize/btn_search.png")}' style="width: auto; vertical-align: middle; cursor: pointer;" tabindex="<%=String.valueOf(lineTab++) %>"/>
									</div>
									</td>

									<!-- 商品名 -->
									<td>
										<div class="box_1of1">
										<span id="productAbstract${status.index}" style="display: block; width:200px; height:3em; white-space: normal; overflow: auto; padding: 1px;">
											<c:out value="${eadLineTrnDtoList.productAbstract}" />
										</span>
										</div>
									</td>

									<!-- 数量・現在庫数・変更後在庫数 -->
									<td>
									<div class="box_1of3" style="background-color: #fae4eb;">
										<html:text name="eadLineTrnDtoList" property="quantity" indexed="true" styleId="eadLineTrnDtoList[${status.index}].quantity" styleClass="numeral_commas" style="width: 55px; height: 24px; margin: 3px; ime-mode: disabled;" tabindex="<%=String.valueOf(lineTab++) %>" maxlength="6"/>
									</div>
									<div class="box_2of3">
										<html:text name="eadLineTrnDtoList" property="stockCount" indexed="true" styleId="eadLineTrnDtoList[${status.index}].stockCount" styleClass="c_disable numeral_commas" style="width: 55px; height: 24px; margin: 3px; ime-mode: disabled;" tabindex="<%=String.valueOf(lineTab++) %>" readonly="true" />
									</div>
									<div class="box_3of3">
										<html:text name="eadLineTrnDtoList" property="updateQuantity" indexed="true" styleId="eadLineTrnDtoList[${status.index}].updateQuantity" styleClass="numeral_commas" style="border: 0px; width: 55px; margin: 3px; height: 24px;" readonly="true" />
									</div>
									</td>

									<!-- 商品備考・備考 -->
									<td>
									<div class="box_1of2">
										<html:textarea name="eadLineTrnDtoList" property="productRemarks" indexed="true" styleId="eadLineTrnDtoList[${status.index}].productRemarks" style="width: 96%; height: 3em; margin: 3px;"  tabindex="<%=String.valueOf(lineTab++) %>" readonly="true" styleClass="c_disable"/><br>
									</div>
									<div class="box_2of2">
										<html:textarea name="eadLineTrnDtoList" property="remarks" indexed="true" styleId="eadLineTrnDtoList[${status.index}].remarks" style="width: 96%; height: 3em; margin: 3px;" tabindex="<%=String.valueOf(lineTab++) %>" />
									</div>
									</td>
									<td style="background-color: #fae4eb;">

									<!-- 棚番 -->
									<div class="box_1of1">
										<html:text name="eadLineTrnDtoList" property="rackCode" indexed="true" styleId="eadLineTrnDtoList[${status.index}].rackCode" styleClass="c_referable" style="width: 85px; ime-mode: disabled;" tabindex="<%=String.valueOf(lineTab++) %>" maxlength="${code_size_rack}" />
										<html:image styleId="rackCodeImg${status.index}" src='${f:url("/images/customize/btn_search.png")}' style="width: auto; vertical-align: middle; cursor: pointer;" tabindex="<%=String.valueOf(lineTab++) %>"/>
									</div>
									</td>

									<!-- ボタン -->
									<td style="text-align:right;">
										<div class="box_1of2">
										<button id="deleteBtn${status.index}" type="button" style="width:80px;" class="btn_list_action"  tabindex="<%=String.valueOf(lineTab++) %>"
											<c:if test="${!newData}">
												disabled
											</c:if>
										><bean:message key='words.action.delete'/><%// 削除 %></button>
										</div>
										<div class="box_2of2">
										<button id="copyBtn${status.index}" type="button" style="width:80px;"  class="btn_list_action" tabindex="<%=String.valueOf(lineTab++) %>"
											<c:if test="${status.first || !newData}">
												disabled
											</c:if>
										><bean:message key='words.action.copyFromPreviousLine'/><%// 前行複写 %></button>
										</div>
									</td>
								</tr>
							</c:if>
						</c:forEach>

						<!-- 追加 -->
						<tr id="trAddLine">
							<td style="height: 60px; text-align: center" colspan="7" class="rd_bottom_left rd_bottom_right">
								<button type="button" style="width:80px;" onclick="addRow();" tabindex="1999"
									<c:if test="${!newData}">
										disabled
									</c:if>
								><%// 行追加 %>
									<img alt="<bean:message key='words.action.addLine'/>" border="none" src="${f:url('/images/customize/btn_line_add.png')}"  width="31" height="33">
								</button>

							</td>
						</tr>
					</tbody>
				</table>
			</div>
			</div>
				<div style="text-align: center; width: 1160px; margin-top: 10px;">
					<button type="button" id="btnF3btm" class="btn_medium" style="width:260px; height:51px;" tabindex="1999" onclick="onF3();" ${newData&&menuUpdate?"":"disabled"}>
						<span style="font-weight:bold; font-size:16px;"><bean:message key='words.action.register'/></span><%// 登録 %>
					</button>
				</div>
		</s:form>
	</div>

</body>

</html>
