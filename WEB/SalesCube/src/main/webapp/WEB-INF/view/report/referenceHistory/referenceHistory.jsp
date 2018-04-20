<%@page import="jp.co.arkinfosys.common.Constants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title><bean:message key='titles.system'/> <bean:message key='titles.referenceHistory'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>
	<script type="text/javascript" src="${f:url('/scripts/dialogs.js')}"></script>

	<script type="text/javascript">
	<!--
		// ページ読込時の動作
		function init() {
			// 初期フォーカス設定
			$("#outputTarget").focus();

			$("#customerCodeFrom2").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁
			$("#customerCodeTo2").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁
			$("#customerCodeFrom3").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁
			$("#customerCodeTo3").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁
			$("#customerCodeFrom4").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁
			$("#customerCodeTo4").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁
			$("#customerCodeFrom9").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁
			$("#customerCodeTo9").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁

			// 表示切替
			onChangeTarget();
		}

		// ビューの入替え処理
		function onChangeTarget() {
			for (i=1;i<=12;i++) {
				if ($("#outputTarget").val()==i) {
					$("#detail_condition_"+i).show();
				}
				else {
					$("#detail_condition_"+i).hide();
				}
			}
		}

		// 顧客検索
		function openCustomerSearchDialog(type){
			var tgt = type%100;
			var grp = (type-tgt)/100

			// 顧客検索ダイアログを開く
			openSearchCustomerDialog(type, setCustomerInfo );
			if(tgt == 1) {
				$("#"+type+"_customerCode").val($("#customerCodeFrom"+grp).val());
			}
			else {
				$("#"+type+"_customerCode").val($("#customerCodeTo"+grp).val());
			}
		}

		// 顧客情報設定
		function setCustomerInfo(type, map) {
			var tgt = type%100;
			var grp = (type-tgt)/100

			if(tgt == 1) {
				$("#customerCodeFrom"+grp).val(map["customerCode"]);
			}
			else {
				$("#customerCodeTo"+grp).val(map["customerCode"]);
			}
		}

		// 商品検索
		function openProductSearchDialog(type){
			var tgt = type%100;
			var grp = (type-tgt)/100

			// 商品検索ダイアログを開く
			openSearchProductDialog(type, setProductInfo );
			if(tgt == 1) {
				$("#"+type+"_productCode").val($("#productCodeFrom"+grp).val());
			}
			else {
				$("#"+type+"_productCode").val($("#productCodeTo"+grp).val());
			}
		}

		// 商品情報設定
		function setProductInfo(type, map) {
			var tgt = type%100;
			var grp = (type-tgt)/100

			if(tgt == 1) {
				$("#productCodeFrom"+grp).val(map["productCode"]);
			}
			else {
				$("#productCodeTo"+grp).val(map["productCode"]);
			}
		}

		// 仕入先検索
		function openSupplierSearchDialog(type){
			var tgt = type%100;
			var grp = (type-tgt)/100

			// 仕入先検索ダイアログを開く
			openSearchSupplierDialog(type, setSupplierInfo );
			if(tgt == 1) {
				$("#"+type+"_supplierCode").val($("#supplierCodeFrom"+grp).val());
			}
			else {
				$("#"+type+"_supplierCode").val($("#supplierCodeTo"+grp).val());
			}
		}

		// 仕入先情報設定
		function setSupplierInfo(type, map) {
			var tgt = type%100;
			var grp = (type-tgt)/100

			if(tgt == 1) {
				$("#supplierCodeFrom"+grp).val(map["supplierCode"]);
			}
			else {
				$("#supplierCodeTo"+grp).val(map["supplierCode"]);
			}
		}

		// 初期化
		function onF1(){
			// 入力内容を初期化してよろしいですか？
			if(confirm('<bean:message key="confirm.init" />')){
				window.location.doHref('${f:url("/report/referenceHistory")}');
			}
		}

		// EXCEL
		function onF3(){
			// この内容でエクセル出力しますか？
			if(!confirm('<bean:message key="confirm.excel" />')){
				return;
			}

			// 出力条件を設定する
			var data = createParamData();

			// 出力を実行する
			execOutput(data);
		}

		// 出力パラメータの作成
		function createParamData() {
			var target = $("#outputTarget").val();
			var actionType = $("#actionType").val();
			var data = new Object();
			data["outputTarget"] = target;
			data["actionType"] = actionType;
			data["recDateFrom"] = $("#recDateFrom").val();
			data["recDateTo"] = $("#recDateTo").val();

			switch (target) {
			case "1" :
				data["estimateDateFrom1"] = $("#estimateDateFrom1").val();
				data["estimateDateTo1"] = $("#estimateDateTo1").val();
				break;
			case "2" :
				data["customerCodeFrom2"] = $("#customerCodeFrom2").val();
				data["customerCodeTo2"] = $("#customerCodeTo2").val();
				data["productCodeFrom2"] = $("#productCodeFrom2").val();
				data["productCodeTo2"] = $("#productCodeTo2").val();
				data["shipDateFrom2"] = $("#shipDateFrom2").val();
				data["shipDateTo2"] = $("#shipDateTo2").val();
				break;
			case "3" :
				data["customerCodeFrom3"] = $("#customerCodeFrom3").val();
				data["customerCodeTo3"] = $("#customerCodeTo3").val();
				data["productCodeFrom3"] = $("#productCodeFrom3").val();
				data["productCodeTo3"] = $("#productCodeTo3").val();
				break;
			case "4" :
				data["customerCodeFrom4"] = $("#customerCodeFrom4").val();
				data["customerCodeTo4"] = $("#customerCodeTo4").val();
				break;
			case "5" :
				data["supplierCodeFrom5"] = $("#supplierCodeFrom5").val();
				data["supplierCodeTo5"] = $("#supplierCodeTo5").val();
				data["productCodeFrom5"] = $("#productCodeFrom5").val();
				data["productCodeTo5"] = $("#productCodeTo5").val();
				break;
			case "6" :
				data["supplierCodeFrom6"] = $("#supplierCodeFrom6").val();
				data["supplierCodeTo6"] = $("#supplierCodeTo6").val();
				data["productCodeFrom6"] = $("#productCodeFrom6").val();
				data["productCodeTo6"] = $("#productCodeTo6").val();
				data["deliveryDateFrom6"] = $("#deliveryDateFrom6").val();
				data["deliveryDateTo6"] = $("#deliveryDateTo6").val();
				break;
			case "7" :
				data["supplierCodeFrom7"] = $("#supplierCodeFrom7").val();
				data["supplierCodeTo7"] = $("#supplierCodeTo7").val();
				data["productCodeFrom7"] = $("#productCodeFrom7").val();
				data["productCodeTo7"] = $("#productCodeTo7").val();
				data["paymentDateFrom7"] = $("#paymentDateFrom7").val();
				data["paymentDateTo7"] = $("#paymentDateTo7").val();
				break;
			case "8" :
				data["productCodeFrom8"] = $("#productCodeFrom8").val();
				data["productCodeTo8"] = $("#productCodeTo8").val();
				data["eadSlipCategory8"] = createEadSlipCategoryArray();
				break;
			case "9" :
				data["customerCodeFrom9"] = $("#customerCodeFrom9").val();
				data["customerCodeTo9"] = $("#customerCodeTo9").val();
				data["creDateFrom9"] = $("#creDateFrom9").val();
				data["creDateTo9"] = $("#creDateTo9").val();
				break;
			case "10" :
				data["productCodeFrom10"] = $("#productCodeFrom10").val();
				data["productCodeTo10"] = $("#productCodeTo10").val();
				data["creDateFrom10"] = $("#creDateFrom10").val();
				data["creDateTo10"] = $("#creDateTo10").val();
				break;
			case "11" :
				data["supplierCodeFrom11"] = $("#supplierCodeFrom11").val();
				data["supplierCodeTo11"] = $("#supplierCodeTo11").val();
				data["creDateFrom11"] = $("#creDateFrom11").val();
				data["creDateTo11"] = $("#creDateTo11").val();
				break;
			case "12" :
				data["creDateFrom12"] = $("#creDateFrom12").val();
				data["creDateTo12"] = $("#creDateTo12").val();
				break;
			}

			return data;
		}

		// 入出庫伝票区分配列の作成
		function createEadSlipCategoryArray() {
			var arry = new Array();
			$("input[type='checkbox'][id^='eadSlipCategory8_']").each(function() {
				if ($(this).attr("checked") == true) {
					arry.push($(this).val());
				}
			});
			return arry;
		}

		// 出力実行
		function execOutput(data) {
			// Ajaxリクエストによって検索結果をロードする
			asyncRequest(
				contextRoot + "/ajax/referenceHistoryAjax/prepare",
				data,
				function(data) {
					$("#errors").empty();
					window.open(contextRoot + "/ajax/referenceHistoryAjax/excel","<bean:message key='words.name.excel'/>");
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
	-->
	</script>
</head>
<body onload="init()" onhelp="return false;">
	<!-- ヘッダ -->
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<!-- メニュー -->
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0011"/>
		<jsp:param name="MENU_ID" value="1101"/>
	</jsp:include>

	<!-- メイン機能 -->
	<div id="main_function">
		<!-- タイトル -->
		<span class="title"><bean:message key='titles.referenceHistory'/></span>

		<!-- ファンクションボタン -->
		<div class="function_buttons">
			<button id="btnF1" tabindex="2000" onclick="onF1()">F1<br><bean:message key='words.action.initialize'/></button>
			<button disabled="disabled">F2<br>&nbsp;</button>
			<button id="btnF3" tabindex="2001" onclick="onF3()">F3<br><bean:message key='words.name.excel'/></button>
			<button disabled="disabled">F4<br>&nbsp;</button>
			<button disabled="disabled">F5<br>&nbsp;</button>
			<button disabled="disabled">F6<br>&nbsp;</button>
			<button disabled="disabled">F7<br>&nbsp;</button>
			<button disabled="disabled">F8<br>&nbsp;</button>
			<button disabled="disabled">F9<br>&nbsp;</button>
			<button disabled="disabled">F10<br>&nbsp;</button>
			<button disabled="disabled">F11<br>&nbsp;</button>
			<button disabled="disabled">F12<br>&nbsp;</button>
		</div>
		<br><br><br>

		<s:form onsubmit="return false;">

			<!-- 出力条件 -->
			<div class="function_forms">
				<div id="errors" style="color: red">
					<html:errors/>
				</div>

				<div class="form_section_wrap">
				<div class="form_section">
					<div class="section_title">
						<span>履歴参照</span>
						<br>
						<button class="btn_toggle" />
					</div>

					<div class="section_body">
						<table id="output_condition" class="forms" summary="outputCondition" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.selectHistory'/></div></th> <!-- 履歴選択 -->
								<td>
									<html:select property="outputTarget" styleId="outputTarget" onchange="onChangeTarget()" tabindex="100">
										<html:options collection="outputTargetList" property="value" labelProperty="label"/>
									</html:select>
								</td>
								<td colspan="4">&nbsp;</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.selectActionType'/></div></th> <!-- アクションタイプ選択 -->
								<td>
									<html:select property="actionType" styleId="actionType"  tabindex="100">
										<html:options collection="actionTypeList" property="value" labelProperty="label"/>
									</html:select>
								</td>
								<th><div class="col_title_right_req">&nbsp;<bean:message key='labels.recDateRange'/><bean:message key='labels.must'/>&nbsp;</div></th> <!-- 入力／変更日範囲 -->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="recDateFrom" styleId="recDateFrom" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="101" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<div class="pos_r">
										<html:text property="recDateTo" styleId="recDateTo" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="102" />
									</div>
								</td>
							</tr>
						</table>

						<!-- 見積入力 -->
						<table id="detail_condition_1" class="forms" summary="detailCondition1" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.estimateDateRange'/></div></th> <!-- 見積日範囲-->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="estimateDateFrom1" styleId="estimateDateFrom1" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="103" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<div class="pos_r">
										<html:text property="estimateDateTo1" styleId="estimateDateTo1" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="104" />
									</div>
								</td>
							</tr>
						</table>

						<!-- 受注入力 -->
						<table id="detail_condition_2" class="forms" summary="detailCondition2" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.customerCodeRange'/></div></th> <!-- 顧客コード範囲-->
								<td style="padding-right: 0;">
									<html:text property="customerCodeFrom2" styleId="customerCodeFrom2" style="width: 140px; ime-mode: disabled;" tabindex="105" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(201)" tabindex="105" />
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<html:text property="customerCodeTo2" styleId="customerCodeTo2" style="width: 140px; ime-mode: disabled;"  tabindex="107" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(202)" tabindex="108" />
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.productCodeRange'/></div></th> <!-- 商品コード範囲-->
								<td style="padding-right: 0;">
									<html:text property="productCodeFrom2" styleId="productCodeFrom2" style="width: 140px; ime-mode: disabled;" maxlength="20" tabindex="109"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(201)" tabindex="110" />
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<html:text property="productCodeTo2" styleId="productCodeTo2" style="width: 140px; ime-mode: disabled;" maxlength="20" tabindex="111"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(202)" tabindex="112" />
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.shipDateRange'/></div></th> <!-- 出荷日範囲 -->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="shipDateFrom2" styleId="shipDateFrom2" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="113" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<div class="pos_r">
										<html:text property="shipDateTo2" styleId="shipDateTo2" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="114" />
									</div>
								</td>
							</tr>
						</table>

						<!-- 売上入力 -->
						<table id="detail_condition_3" class="forms" summary="detailCondition3" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.customerCodeRange'/></div></th> <!-- 顧客コード範囲-->
								<td style="padding-right: 0;">
									<html:text property="customerCodeFrom3" styleId="customerCodeFrom3" style="width: 200px; ime-mode: disabled;" maxlength="13" tabindex="115" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(301)" tabindex="116" />
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<html:text property="customerCodeTo3" styleId="customerCodeTo3" style="width: 200px; ime-mode: disabled;" maxlength="13" tabindex="117" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(302)" tabindex="118" />
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.productCodeRange'/></div></th> <!-- 商品コード範囲-->
								<td style="padding-right: 0;">
									<html:text property="productCodeFrom3" styleId="productCodeFrom3" style="width: 200px; ime-mode: disabled;" maxlength="20" tabindex="119"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(301)" tabindex="120" />
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<html:text property="productCodeTo3" styleId="productCodeTo3" style="width: 200px; ime-mode: disabled;" maxlength="20" tabindex="121"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(302)" tabindex="122" />
								</td>
							</tr>
						</table>

						<!-- 入金入力 -->
						<table id="detail_condition_4" class="forms" summary="detailCondition4" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.customerCodeRange'/></div></th> <!-- 顧客コード範囲-->
								<td style="padding-right: 0;">
									<html:text property="customerCodeFrom4" styleId="customerCodeFrom4" style="width: 150px; ime-mode: disabled;" maxlength="13" tabindex="123" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(401)" tabindex="124" />
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<html:text property="customerCodeTo4" styleId="customerCodeTo4" style="width: 150px; ime-mode: disabled;" maxlength="13" tabindex="125" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(402)" tabindex="126" />
								</td>
							</tr>
						</table>

						<!-- 発注入力 -->
						<table id="detail_condition_5" class="forms" summary="detailCondition5" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.supplierCodeRange'/></div></th> <!-- 仕入先コード範囲-->
								<td style="padding-right: 0;">
									<html:text property="supplierCodeFrom5" styleId="supplierCodeFrom5" style="width: 200px; ime-mode: disabled;" maxlength="9" tabindex="127" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSupplierSearchDialog(501)" tabindex="128" />
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<html:text property="supplierCodeTo5" styleId="supplierCodeTo5" style="width: 200px; ime-mode: disabled;" maxlength="9" tabindex="129" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSupplierSearchDialog(502)" tabindex="130" />
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.productCodeRange'/></div></th> <!-- 商品コード範囲-->
								<td style="padding-right: 0;">
									<html:text property="productCodeFrom5" styleId="productCodeFrom5" style="width: 200px; ime-mode: disabled;" maxlength="20" tabindex="131"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(501)" tabindex="132" />
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<html:text property="productCodeTo5" styleId="productCodeTo5" style="width: 200px; ime-mode: disabled;" maxlength="20" tabindex="133"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(502)" tabindex="134" />
								</td>
							</tr>
						</table>

						<!-- 仕入入力 -->
						<table id="detail_condition_6" class="forms" summary="detailCondition6" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.supplierCodeRange'/></div></th> <!-- 仕入先コード範囲-->
								<td style="padding-right: 0;">
									<html:text property="supplierCodeFrom6" styleId="supplierCodeFrom6" style="width: 165px; ime-mode: disabled;" maxlength="9" tabindex="135" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSupplierSearchDialog(601)" tabindex="136" />
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<html:text property="supplierCodeTo6" styleId="supplierCodeTo6" style="width: 165px; ime-mode: disabled;" maxlength="9" tabindex="137" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSupplierSearchDialog(602)" tabindex="138" />
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.productCodeRange'/></div></th> <!-- 商品コード範囲-->
								<td style="padding-right: 0;">
									<html:text property="productCodeFrom6" styleId="productCodeFrom6" style="width: 165px; ime-mode: disabled;" maxlength="20" tabindex="139"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(601)" tabindex="140" />
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<html:text property="productCodeTo6" styleId="productCodeTo6" style="width: 165px; ime-mode: disabled;" maxlength="20" tabindex="141"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(602)" tabindex="142" />
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.deliveryDateRange'/></div></th> <!-- 納期範囲 -->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="deliveryDateFrom6" styleId="deliveryDateFrom6" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="145" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<div class="pos_r">
										<html:text property="deliveryDateTo6" styleId="deliveryDateTo6" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="146" />
									</div>
								</td>
							</tr>
						</table>

						<!-- 支払入力 -->
						<table id="detail_condition_7" class="forms" summary="detailCondition7" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.supplierCodeRange'/></div></th> <!-- 仕入先コード範囲-->
								<td style="padding-right: 0;">
									<html:text property="supplierCodeFrom7" styleId="supplierCodeFrom7" style="width: 165px; ime-mode: disabled;" maxlength="9" tabindex="147" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSupplierSearchDialog(701)" tabindex="148" />
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<html:text property="supplierCodeTo7" styleId="supplierCodeTo7" style="width: 165px; ime-mode: disabled;" maxlength="9" tabindex="149" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSupplierSearchDialog(702)" tabindex="150" />
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.productCodeRange'/></div></th> <!-- 商品コード範囲-->
								<td style="padding-right: 0;">
									<html:text property="productCodeFrom7" styleId="productCodeFrom7" style="width: 165px; ime-mode: disabled;" maxlength="20" tabindex="151"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(701)" tabindex="152" />
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<html:text property="productCodeTo7" styleId="productCodeTo7" style="width: 165px; ime-mode: disabled;" maxlength="20" tabindex="153"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(702)" tabindex="154" />
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.paymentDateRange'/></div></th> <!-- 支払日範囲 -->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="paymentDateFrom7" styleId="paymentDateFrom7" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="155" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<div class="pos_r">
										<html:text property="paymentDateTo7" styleId="paymentDateTo7" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="156" />
									</div>
								</td>
							</tr>
						</table>

						<!-- 入出庫入力 -->
						<table id="detail_condition_8" class="forms" summary="detailCondition8" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.productCodeRange'/></div></th> <!-- 商品コード範囲-->
								<td style="padding-right: 0;">
									<html:text property="productCodeFrom8" styleId="productCodeFrom8" style="width: 200px; ime-mode: disabled;" maxlength="20" tabindex="157"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(801)" tabindex="158" />
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<html:text property="productCodeTo8" styleId="productCodeTo8" style="width: 200px; ime-mode: disabled;" maxlength="20" tabindex="159"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(802)" tabindex="160" />
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.targetSlipCategory'/></div></th> <!-- 入出庫伝票区分 -->
								<td>
									<c:forEach var="item" items="${eadSlipCategoryList}" varStatus="status">
										<input type="checkbox" id="eadSlipCategory8_${f:h(status.index)}" value="${item.value}" tabindex="161"/>${f:h(item.label)}&nbsp
									</c:forEach>
								</td>
							</tr>
						</table>

						<!-- 顧客マスタ -->
						<table id="detail_condition_9" class="forms" summary="detailCondition9" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.customerCodeRange'/></div></th> <!-- 顧客コード範囲-->
								<td style="padding-right: 0;">
									<html:text property="customerCodeFrom9" styleId="customerCodeFrom9" style="width: 165px; ime-mode: disabled;" maxlength="13" tabindex="162" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(901)" tabindex="163" />
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<html:text property="customerCodeTo9" styleId="customerCodeTo9" style="width: 165px; ime-mode: disabled;" maxlength="13" tabindex="164" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(902)" tabindex="165" />
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.creDateRange'/></div></th> <!-- 登録年月日範囲 -->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="creDateFrom9" styleId="creDateFrom9" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="166" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<div class="pos_r">
										<html:text property="creDateTo9" styleId="creDateTo9" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="167" />
									</div>
								</td>
							</tr>
						</table>

						<!-- 商品マスタ -->
						<table id="detail_condition_10" class="forms" summary="detailCondition10" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.productCodeRange'/></div></th> <!-- 商品コード範囲-->
								<td style="padding-right: 0;">
									<html:text property="productCodeFrom10" styleId="productCodeFrom10" style="width: 165px; ime-mode: disabled;" maxlength="20" tabindex="168"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(1001)" tabindex="169" />
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<html:text property="productCodeTo10" styleId="productCodeTo10" style="width: 165px; ime-mode: disabled;" maxlength="20" tabindex="170"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(1002)" tabindex="171" />
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.creDateRange'/></div></th> <!-- 登録年月日範囲 -->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="creDateFrom10" styleId="creDateFrom10" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="172" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<div class="pos_r">
										<html:text property="creDateTo10" styleId="creDateTo10" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="173" />
									</div>
								</td>
							</tr>
						</table>

						<!-- 仕入先マスタ -->
						<table id="detail_condition_11" class="forms" summary="detailCondition11" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.supplierCodeRange'/></div></th> <!-- 仕入先コード範囲-->
								<td style="padding-right: 0;">
									<html:text property="supplierCodeFrom11" styleId="supplierCodeFrom11" style="width: 165px; ime-mode: disabled;" maxlength="9" tabindex="174" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSupplierSearchDialog(1101)" tabindex="175" />
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<html:text property="supplierCodeTo11" styleId="supplierCodeTo11" style="width: 165px; ime-mode: disabled;" maxlength="9" tabindex="176" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSupplierSearchDialog(1102)" tabindex="177" />
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.creDateRange'/></div></th> <!-- 登録年月日範囲 -->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="creDateFrom11" styleId="creDateFrom11" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="178" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<div class="pos_r">
										<html:text property="creDateTo11" styleId="creDateTo11" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="179" />
									</div>
								</td>
							</tr>
						</table>
						<!-- 社員情報 -->
						<table id="detail_condition_12" class="forms" summary="detailCondition12" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.creDateRange'/></div></th> <!-- 登録年月日範囲 -->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="creDateFrom12" styleId="creDateFrom12" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="180" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<div class="pos_r">
										<html:text property="creDateTo12" styleId="creDateTo12" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="181" />
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
				</div>
			</div>
		</s:form>
	</div>
</body>
</html>
