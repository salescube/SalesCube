<%@page import="jp.co.arkinfosys.common.Constants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title><bean:message key='titles.system'/> <bean:message key='titles.referenceMst'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>
	<script type="text/javascript" src="${f:url('/scripts/dialogs.js')}"></script>

	<script type="text/javascript">
	<!--
		// ページ読込時の動作
		function init() {
			// 初期フォーカス設定
			$("#outputTarget").focus();
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
				window.location.doHref('${f:url("/report/referenceMst")}');
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
			var data = new Object();
			data["outputTarget"] = target;
			data["recDateFrom"] = $("#recDateFrom").val();
			data["recDateTo"] = $("#recDateTo").val();

			switch (target) {
			case "1" :
				data["customerCodeFrom9"] = $("#customerCodeFrom9").val();
				data["customerCodeTo9"] = $("#customerCodeTo9").val();
				data["creDateFrom9"] = $("#creDateFrom9").val();
				data["creDateTo9"] = $("#creDateTo9").val();
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
				contextRoot + "/ajax/referenceMstAjax/prepare",
				data,
				function(data) {
					$("#errors").empty();
					window.open(contextRoot + "/ajax/referenceMstAjax/excel","<bean:message key='words.name.excel'/>");
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
		<jsp:param name="MENU_ID" value="1102"/>
	</jsp:include>

	<!-- メイン機能 -->
	<div id="main_function">
		<!-- タイトル -->
		<span class="title"><bean:message key='titles.referenceMst'/></span>

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
						<span>マスタリスト</span>
						<button class="btn_toggle" />
					</div>

					<div class="section_body">
						<table id="output_condition" class="forms" summary="outputCondition" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.selectMst'/></div></th> <!-- マスタリスト選択 -->
								<td>
									<html:select property="outputTarget" styleId="outputTarget" onchange="onChangeTarget()" tabindex="100" style="width: 300px;">
										<html:options collection="outputTargetList" property="value" labelProperty="label"/>
									</html:select>
								</td>
							</tr>
						</table>

						<!-- 顧客マスタ -->
						<table id="detail_condition_1" class="forms" summary="detailCondition1" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.customerCodeRange'/></div></th> <!-- 顧客コード範囲-->
								<td style="padding-right: 0;">
									<html:text property="customerCodeFrom9" styleId="customerCodeFrom9" style="width: 150px; ime-mode: disabled;"
										maxlength="<%=String.valueOf(Constants.CODE_SIZE.CUSTOMER)%>" tabindex="162" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(901)" tabindex="163" />
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<html:text property="customerCodeTo9" styleId="customerCodeTo9" style="width: 150px; ime-mode: disabled;"
										maxlength="<%=String.valueOf(Constants.CODE_SIZE.CUSTOMER)%>" tabindex="164" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(902)" tabindex="165" />
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.creDateRange'/></div></th> <!-- 登録年月日範囲 -->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="creDateFrom9" styleId="creDateFrom9" style="width: 150px; ime-mode: disabled;" styleClass="date_input" tabindex="166" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<div class="pos_r">
										<html:text property="creDateTo9" styleId="creDateTo9" style="width: 150px; ime-mode: disabled;" styleClass="date_input" tabindex="167" />
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
