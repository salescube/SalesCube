<%@page import="jp.co.arkinfosys.common.Constants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><bean:message key='titles.system'/> <bean:message key='titles.makeOutBill'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--

	var paramData = null;
	var paramDataTmp = null;
	var checkData = null;
	var doaData = null;

	//ページ読込時の動作
	function init() {
		// 初期フォーカス
		$('#billId').focus();
		$("#customerCode").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);
	}


	// 初期化
	function onF1(){
		// 入力内容を初期化してよろしいですか？
		if(confirm('<bean:message key="confirm.init" />')){
			location.doHref('${f:url("/bill/makeOutBill")}');
		}
	}

	// 検索ボタンによる検索処理
	function onF2() {

		paramData = createParamData();
		paramData["pageNo"] = 1;

		return execSearch(paramData, false);
	}

	// PDF
	function onF3(){
		saveDisplayCheckState();

		outCnt = createPrintForm();

		if( outCnt > 0 ){
			var url = '${f:url("/bill/makeOutBillReportOutput/pdf")}';
			$("form[name='printForm']").attr("action", url );
			$("form[name='printForm']").submit();
		}
		viewOldBill("viewPdf");
	}

	/**
	* 保存請求書を別ブラウザに出力する
	*/
	function viewOldBill(funcTarget) {
		var temp = new Array();
		for(var billId in checkData) {
			if( doaData[ billId ] == '2' ){
				$("form[name='printForm']").attr("target", billId );
				var url = '${f:url("/bill/makeOutBillReportOutput/")}' + funcTarget + "/" + billId;
				$("form[name='printForm']").attr("action", url );
				$("form[name='printForm']").submit();
			}
		}
		$("form[name='printForm']").attr("target", "_self" );
	}

	/**
	 * 帳票出力用のフォームを作成する
	 */
	function createPrintForm() {
		var temp = new Array();
		for(var billId in checkData) {
			if( doaData[ billId ] == '1' ){	// #$#
				temp.push( { "billId": billId, "customerCode": checkData[ billId ] } );
			}
		}

		// チェックされた請求書を顧客コードの昇順、請求書番号の降順にソートする
		temp.sort(
			function(a, b) {
				if(a.customerCode == b.customerCode) {
					return new Number(b.billId) - new Number(a.billId);
				}

				if(a.customerCode > b.customerCode) {
					return 1;
				}
				else{
					return -1;
				}
			}
		);

		$("#printForm").empty();
		for(var i = 0; i < temp.length; i++) {
			var hidden = $(document.createElement("input"));
			hidden.attr("type", "hidden");
			hidden.attr("name", "rowDataPrint[" + i + "].billId");
			hidden.val(temp[i].billId);
			$("#printForm").append(hidden);
		}

		return temp.length;
	}

	// チェックボックス選択時
	function checkPrint(){
		// 各ページのチェック状態を記憶する
		saveDisplayCheckState();

		for(var billId in checkData) {
			$("#btnF3").attr("disabled", false);
			return;
		}

		$("#btnF3").attr("disabled", true);
	}

	//顧客検索
	function openCustomerSearchDialog(type){
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

	//顧客情報設定
	function setCustomerInfo(type, map) {
		if(type == 1) {
			// 顧客コードを設定する
			$("#customerCode").val(map["customerCode"]);
		} else if(type == 2) {
			// 顧客名を設定する
			$("#customerName").val(map["customerName"]);
		}
	}

	// ソート
	function sort(sortColumn) {
		// 各ページのチェック状態を記憶する
		saveDisplayCheckState();

		// 前回のソートカラムとソート順を取得
		var beforeSortColumn = $("#sortColumn").val();
		var beforeSortOrderAsc = $("#sortOrderAsc").val();

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

		// 1回以上検索しており、前回の結果が1件以上ある場合のみ再検索
		if(paramDataTmp != null && $("#searchResultCount").val() != "0") {
			// 前回の検索条件からソート条件のみを変更
			paramData = paramDataTmp;
			paramData["sortColumn"] = $("#sortColumn").val();
			paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
			paramData["pageNo"] = "1";
			// 検索
			execSearch(paramData, true);
		}
	}


	//ページ繰り、ソートによる検索処理
	function goPage(no){
		// 各ページのチェック状態を記憶する
		saveDisplayCheckState();

		// 検索条件を設定する
		paramData = paramDataTmp;
		paramData["pageNo"] = no;
		// 検索を実行する
		execSearch(paramData, true);

	}

	function createParamData(){
		// リクエストデータ作成
		paramData = new Object();


		if($("#billId").val()) {
			paramData["billId"] = $("#billId").val();
		}
		if($("#billCutoffDateFrom").val()) {
			paramData["billCutoffDateFrom"] = $("#billCutoffDateFrom").val();
		}
		if($("#billCutoffDateTo").val()) {
			paramData["billCutoffDateTo"] = $("#billCutoffDateTo").val();
		}
		if($("#cutoffGroupCategory").val()) {
			paramData["cutoffGroupCategory"] = $("#cutoffGroupCategory").val();
		}
		if($("#customerCode").val()) {
			paramData["customerCode"] = $("#customerCode").val();
		}
		if($("#customerName").val()) {
			paramData["customerName"] = $("#customerName").val();
		}
		if($("#covPriceZero").attr('disabled') == false && $("#covPriceZero").attr('checked') == true) {
			paramData["covPriceZero"] = $("#covPriceZero").val();
		}
		if($("#covPriceMinus").attr('disabled') == false && $("#covPriceMinus").attr('checked') == true) {
			paramData["covPriceMinus"] = $("#covPriceMinus").val();
		}
		if($("#covPricePlus").attr('disabled') == false && $("#covPricePlus").attr('checked') == true) {
			paramData["covPricePlus"] = $("#covPricePlus").val();
		}
		if($("#thisBillPricePlus").attr('disabled') == false && $("#thisBillPricePlus").attr('checked') == true) {
			paramData["thisBillPricePlus"] = $("#thisBillPricePlus").val();
		}
		if($("#thisBillPriceZero").attr('disabled') == false && $("#thisBillPriceZero").attr('checked') == true) {
			paramData["thisBillPriceZero"] = $("#thisBillPriceZero").val();
		}
		if($("#thisBillPriceMinus").attr('disabled') == false && $("#thisBillPriceMinus").attr('checked') == true) {
			paramData["thisBillPriceMinus"] = $("#thisBillPriceMinus").val();
		}
		if($("#excludePrint").attr("checked")) {
			paramData["excludePrint"] = $("#excludePrint").val();
		}

		// ページあたりの表示件数
		if($("#rowCount").val()) {
			paramData["rowCount"] = $("#rowCount").val();
		}

		// ソート列
		if($("#sortColumn").val()) {
			paramData["sortColumn"] = $("#sortColumn").val();
		}
		// ソート昇順フラグ
		if($("#sortOrderAsc").val()) {
			paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
		}

		return paramData;
	}

	//検索実行
	function execSearch(paramData, fromSaveState){
		var url = contextRoot + "/ajax/bill/makeOutBillAjax/search";

		$("#searchResultMsg").val("");
		$("#errors").empty();
		$("#ajax_errors").empty();

		// Ajaxリクエストによって検索結果をロードする
		asyncRequest(
				url,
				paramData,
				function(result) {
					$("#searchResultMsg").val("");
					$("#errors").empty();

					// 検索結果テーブルを更新する
					$("#ListContainer").empty();
					$("#ListContainer").html(result);

					// カンマをつける
					_after_load($(".numeral_commas"));

					paramDataTmp = paramData;

					if(fromSaveState) {
						setDisplayCheckState();
						adjustCheckData();
					}
					else {
						// 全ページのチェック状態をリセットする
						checkData = new Object();
						doaData = new Object();
					}

					checkPrint();

					// メッセージ
					if( $("#searchResultMsg").val() != "" ){
						$("#errors").append($("#searchResultMsg").val());
					}
				});
	}

	/**
	* 検索結果とチェック状態の突合
	*/
	function adjustCheckData() {
		var checkExists = false;
		for(var salesSlipId in checkData) {
			checkExists = true;
			break;
		}

		if(!checkExists) {
			return;
		}

		var doc = window.document;
		var slipIds = doc.getElementsByName("slipId");
		if(slipIds == null) {
			return;
		}

		var len = slipIds.length;
		if(len == null && slipIds.value != null) {
			var tempArray = new Object();
			tempArray.push(slipIds);
			slipIds = tempArray;
			len = 1;
		}

		var billIdHash = new Object();
		for(var i = 0; i < slipIds.length; i++) {
			billIdHash[ slipIds[ i ].value.split(",")[0] ] = true;
		}

		for(var billId in checkData) {
			if(!billIdHash[billId]) {
				delete checkData[ billId ];
				delete doaData[ billId ];
			}
		}
	}

	// ================================================================================================
	// 画面操作系
	// ================================================================================================
	function checkAll(flg){

		$("#ListContainer").find("input[type='checkbox']").attr('checked', flg);

		checkData = new Object();
		doaData = new Object();
		if(flg) {
			readAllBillId();
		}

		checkPrint();
	}

	/**
	* 検索結果全体の請求書番号と顧客コードを読み込む
	*/
	function readAllBillId() {
		var doc = window.document;
		var billIdAndCustomerCodes = doc.getElementsByName("slipId");
		if(billIdAndCustomerCodes != null) {
			var temp = null;
			var len = billIdAndCustomerCodes.length;
			if(len) {
				for(var i = 0; i < len; i++) {
					temp = billIdAndCustomerCodes[ i ].value.split(",");
					checkData[ temp[ 0 ] ] = temp[ 1 ];
					doaData[ temp[ 0 ] ] = temp[ 2 ];
				}
			}
			else {
				if( billIdAndCustomerCodes.value != null ) {
					temp = billIdAndCustomerCodes[ i ].value.split(",");
					checkData[ temp[ 0 ] ] = temp[ 1 ];
					doaData[ temp[ 0 ] ] = temp[ 2 ];
				}
			}
		}
	}

	/**
	 * 表示中ページのチェック状態を記憶する
	 */
	function saveDisplayCheckState() {
		$("#ListContainer").find("input:checkbox").each(
			function(){
				if( $(this).attr("checked") == true ){
					checkData[ this.value ] = $(this).next("input[name$='customerCode']").val();
					doaData[ this.value ] = $(this).nextAll("input[name$='doa']").val();
				}
				else {
					// チェックされていないものは除外
					delete checkData[ this.value ];
					delete doaData[ this.value ];
				}
			}
		);
	}

	/**
	 * 画面内のチェックボックスのチェックに保存した状態を反映する
	 */
	function setDisplayCheckState() {
		$("#ListContainer").find("input:checkbox").each(
			function(){
				$(this).attr("checked", false);
				if( checkData[ this.value ] ){
					$(this).attr("checked", true);
				}
			}
		);
	}
	-->
	</script>
</head>
<body onload="init()" onhelp="return false;" >
	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0005"/>
		<jsp:param name="MENU_ID" value="0502"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">

		<!-- タイトル -->
		<span class="title"><bean:message key='titles.makeOutBill'/></span>

		<div class="function_buttons">
				<button id="btnF1" type="button" onclick="onF1();" tabindex="2000">F1<br><bean:message key='words.action.initialize'/></button>
				<button id="btnF2" type="button" onclick="onF2();" tabindex="2001">F2<br><bean:message key='words.action.search'/></button>
				<button id="btnF3" type="button" onclick="onF3();" disabled="disabled" tabindex="2002">F3<br><bean:message key='words.name.pdf'/></button>
				<button type="button" disabled="disabled">F4<br>&nbsp;</button>
				<button type="button" disabled="disabled">F5<br>&nbsp;</button>
				<button type="button" disabled="disabled">F6<br>&nbsp;</button>
				<button type="button" disabled="disabled">F7<br>&nbsp;</button>
				<button type="button" disabled="disabled">F8<br>&nbsp;</button>
				<button type="button" disabled="disabled">F9<br>&nbsp;</button>
				<button type="button" disabled="disabled">F10<br>&nbsp;</button>
				<button type="button" disabled="disabled">F11<br>&nbsp;</button>
				<button type="button" disabled="disabled">F12<br>&nbsp;</button>
		</div>
		<br><br><br>

		<s:form onsubmit="return false;">

			<div class="function_forms">

				<!-- エラー情報 -->
				<div id="errors" style="color: red"><html:errors /></div>
				<span id="ajax_errors" ></span>
				<div style="padding-left: 20px;color: blue;">
					<html:messages id="msg" message="true"><bean:write name="msg" ignore="true"/><br></html:messages>
				</div>


				<div class="form_section_wrap">
					<div class="form_section">
						<div class="section_title">
							<span><bean:message key='labels.searchCondition'/></span><br>
							<button class="btn_toggle" />
						</div><!-- /.section_title -->

						<div id="search_info" class="section_body">
						<table id="order_info" class="forms" summary="請求検索情報" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.billId'/></div></th> <!-- 請求書番号 -->
								<td><html:text property="billId" styleId="billId" tabindex="100" style="width: 100px; ime-mode:disabled;" /></td>
								<th><div class="col_title_right"><bean:message key='labels.cutoffGroupCategory'/></div></th> <!-- 支払条件 -->
								<td>
									<html:select tabindex="102" property="cutoffGroupCategory"  styleId="cutoffGroupCategory" >
										<c:forEach var="dcl" items="${cutoffGroupCategoryList}">
											<html:option value="${dcl.value}">${dcl.label}</html:option>
										</c:forEach>
									</html:select>
								</td>
								<th><div class="col_title_right"><bean:message key='labels.billCutOffDate'/></div></th> <!-- 請求締日 -->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="billCutoffDateFrom" styleId="billCutoffDateFrom"  styleClass="date_input" style="width: 135px; ime-mode:disabled;" tabindex="103" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<div class="pos_r">
										<html:text property="billCutoffDateTo" styleId="billCutoffDateTo" styleClass="date_input"  style="width: 135px; ime-mode:disabled;" tabindex="104" />
									</div>
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.customerCode'/></div></th> <!-- 顧客コード -->
								<td ><html:text property="customerCode" styleId="customerCode" style="width: 100px;ime-mode:disabled;" tabindex="105" />
									<img src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(1)" tabindex="106">
								</td>
								<th><div class="col_title_right"><bean:message key='labels.customerName'/></div></th> <!-- 顧客名 -->
								<td  colspan="3" style="padding-right: 0;">
									<html:text property="customerName" styleId="customerName" style="width: 430px;" tabindex="107" />
									<img src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(2)" tabindex="108">
								</td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.covPrice'/></div></th> <!-- 繰越金額-->
								<td>
									<html:checkbox property="covPriceZero" styleId="covPriceZero" value="<%=Constants.SEARCH_BILL.CARRY_OVER_ZERO %>"  tabindex="109"  /><label for="covPriceZero"><bean:message key='labels.notexist'/></label>&nbsp;&nbsp;
									<html:checkbox property="covPriceMinus" styleId="covPriceMinus" value="<%=Constants.SEARCH_BILL.CARRY_OVER_MINUS %>"  tabindex="110"  /><label for="covPriceMinus"><bean:message key='labels.over'/></label>&nbsp;&nbsp;
									<html:checkbox property="covPricePlus" styleId="covPricePlus" value="<%=Constants.SEARCH_BILL.CARRY_OVER_PLUS %>"  tabindex="111"  /><label for="covPricePlus"><bean:message key='labels.less'/></label>&nbsp;&nbsp;
								</td>
								<th><div class="col_title_right"><bean:message key='labels.thisBillPrice'/></div></th> <!-- 今回請求金額 -->
								<td>
									<html:checkbox property="thisBillPricePlus" styleId="thisBillPricePlus" value="<%=Constants.SEARCH_BILL.BILL_PRICE_PLUS %>"  tabindex="112" /><label for="thisBillPricePlus"><bean:message key='labels.exist'/></label>&nbsp;&nbsp;
									<html:checkbox property="thisBillPriceZero" styleId="thisBillPriceZero" value="<%=Constants.SEARCH_BILL.BILL_PRICE_ZERO %>"  tabindex="113" /><label for="thisBillPriceZero"><bean:message key='labels.notexist'/></label>&nbsp;&nbsp;
									<html:checkbox property="thisBillPriceMinus" styleId="thisBillPriceMinus" value="<%=Constants.SEARCH_BILL.BILL_PRICE_MINUS %>"  tabindex="114" /><label for="thisBillPriceMinus"><bean:message key='labels.over'/></label>
								</td>
								<th><div class="col_title_right"><bean:message key='labels.exceptAlreadyOutput'/></div></th> <!-- 発行済を除く -->
								<td style="text-align: left"><html:checkbox property="excludePrint" styleId="excludePrint" tabindex="115" style="ime-mode:disabled; width: 120px; " />&nbsp;&nbsp;</td>
								<td></td>
								<td></td>
							</tr>
						</table>
						</div>
					</div><!-- /.form_section -->
		    	</div><!-- /.form_section_wrap -->

				<html:hidden property="sortColumn" styleId="sortColumn" />
				<html:hidden property="sortOrderAsc" styleId="sortOrderAsc" />

				<div style="width: 1160px; text-align: right">
					<button type="button" onclick="onF1();" tabindex="250" class="btn_medium"><bean:message key='words.action.initialize'/></button> <!-- 初期化 -->
					<button type="button" onclick="onF2();" tabindex="251" class="btn_medium"><bean:message key='words.action.search'/></button> <!-- 検索 -->
				</div>
			</div>

			<button id="allCheck" name="allCheck" type="button" tabindex="300" onclick="checkAll(true)" class="btn_list_action">全て選択</button>
			<button id="allUnCheck" name="allUnCheck" type="button" tabindex="301" onclick="checkAll(false)" class="btn_list_action">全て解除</button>

			<span id="ListContainer">
				<%@ include file="/WEB-INF/view/ajax/bill/makeOutBillAjax/result.jsp" %>
			</span>
		</s:form>
	</div>

	<div id="output_dialog" title="出力選択" style="display: none; margin: 20px 0px 0px 10px;">
		<form name="printForm" method="post" id="printForm" >
		</form>
	</div>

</body>
</html>
