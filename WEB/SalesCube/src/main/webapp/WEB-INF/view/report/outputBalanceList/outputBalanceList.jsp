<%@page pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title><bean:message key='titles.system'/> <bean:message key='titles.outputBalanceList'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
		// ページ読込時の動作
		function init() {
			// 初期フォーカス設定
			$("#outputTarget").focus();
			// 表示切替
			onChangeTarget();
		}

		// 出力対象のチェンジ処理
		function onChangeTarget() {
			$("#porder_balance").css("display",($("#outputTarget").val()==1?"block":"none"));
			$("#rorder_balance").css("display",($("#outputTarget").val()==2?"block":"none"));
		}

		// 仕入先検索
		function openSupplierSearchDialog(type){
			var id = "SupplierSearch";

			// 仕入先検索ダイアログを開く
			openSearchSupplierDialog(type, setSupplierInfo );
			if(type == 1) {
				// 仕入先コードを設定する
				$("#"+type+"_supplierCode").val($("#supplierCode").val());
			}
		}

		// 仕入先情報設定
		function setSupplierInfo(type, map) {
			if(type == 1) {
				// 仕入先コードを設定する
				$("#supplierCode").val(map["supplierCode"]);
			}
		}

		// 顧客検索
		function openCustomerSearchDialog(type){
			var id = "CustomerSearch";

			// 顧客検索ダイアログを開く
			openSearchCustomerDialog(type, setCustomerInfo );
			if(type == 1) {
				// 顧客コードFromを設定する
				$("#"+type+"_customerCode").val($("#customerCodeFrom").val());
			} else if(type == 2) {
				// 顧客コードToを設定する
				$("#"+type+"_customerCode").val($("#customerCodeTo").val());
			}
		}

		// 顧客情報設定
		function setCustomerInfo(type, map) {
			if(type == 1) {
				// 顧客コードFromを設定する
				$("#customerCodeFrom").val(map["customerCode"]);
			} else if(type == 2) {
				// 顧客コードToを設定する
				$("#customerCodeTo").val(map["customerCode"]);
			}
		}

		// 初期化
		function onF1(){
			// 入力内容を初期化してよろしいですか？
			if(confirm('<bean:message key="confirm.init" />')){
				window.location.doHref('${f:url("/report/outputBalanceList")}');
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
			var data = new Object();
			data["outputTarget"] = $("#outputTarget").val();
			data["targetDate"] = $("#targetDate").val();
			data["supplierCode"] = $("#supplierCode").val();
			data["customerCodeFrom"] = $("#customerCodeFrom").val();
			data["customerCodeTo"] = $("#customerCodeTo").val();
			return data;
		}

		// 出力実行
		function execOutput(data) {
			// Ajaxリクエストによって検索結果をロードする
			asyncRequest(
				contextRoot + "/ajax/outputBalanceListAjax/prepare",
				data,
				function(data) {
					$("#errors").empty();
					window.open(contextRoot + "/ajax/outputBalanceListAjax/excel","<bean:message key='words.name.excel'/>");
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
		<jsp:param name="MENU_ID" value="1100"/>
	</jsp:include>

	<!-- メイン機能 -->
	<div id="main_function">
		<!-- タイトル -->
		<span class="title"><bean:message key='titles.outputBalanceList'/></span>

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
		</div><br><br><br>

		<s:form onsubmit="return false;">

			<!-- 出力条件 -->
			<div class="function_forms">
				<div id="errors" style="color: red">
					<html:errors/>
				</div>

				<div class="form_section_wrap">
				<div class="form_section">
				<div class="section_title">
					<span><bean:message key='labels.searchCondition'/></span>
					<br>
					<button class="btn_toggle" />
				</div>
				<div class="section_body">


				<div id="target_info">
					<table id="output_condition" class="forms" summary="outputCondition">
						<colgroup>
							<col span="1" style="width: 13%">
							<col span="1" style="width: 20%">
							<col span="1" style="width: 13%">
							<col span="1" style="width: 54%">
						</colgroup>
						<tr>
							<th><div class="col_title_right"><bean:message key='labels.outputTarget'/></div></th> <!-- 出力対象 -->
							<td>
								<html:select property="outputTarget" styleId="outputTarget" onchange="onChangeTarget()" tabindex="100">
									<html:options collection="outputTargetList" property="value" labelProperty="label"/>
								</html:select>
							</td>
							<th><div class="col_title_right_req"><bean:message key='labels.targetDate'/><bean:message key='labels.must'/></div></th> <!-- 対象年月 -->
							<td>
								<html:text property="targetDate" styleId="targetDate" style="width: 150px; ime-mode: disabled;" tabindex="101" />
								<bean:message key='labels.targetDateSuffix'/>
							</td>
						</tr>
					</table>
				</div>
				<div id="porder_balance">
					<table id="porder_condition" class="forms" summary="porderCondition">
						<colgroup>
							<col span="1" style="width: 13%">
							<col span="1" style="width: 87%">
						</colgroup>
						<tr>
							<th><div class="col_title_right_req"><bean:message key='labels.supplierCode'/><bean:message key='labels.must'/></div></th> <!-- 仕入先コード -->
							<td>
								<html:text property="supplierCode" styleId="supplierCode" style="width: 150px; ime-mode: disabled;" maxlength="9" tabindex="102" />
								<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" tabindex="103" onclick="openSupplierSearchDialog(1);"/>
							</td>

						</tr>
					</table>
				</div>
				<div id="rorder_balance">
					<table id="rorder_condition" class="forms" summary="rorderCondition">
						<colgroup>
							<col span="1" style="width: 13%">
							<col span="1" style="width: 87%">
						</colgroup>
						<tr>
							<th><div class="col_title_right"><bean:message key='labels.customerCodeRange'/></div></th> <!-- 顧客コード範囲 -->
							<td>
								<html:text property="customerCodeFrom" styleId="customerCodeFrom" style="width: 150px; ime-mode: disabled;" maxlength="15" tabindex="106" />
								<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(1)" tabindex="107" />
								<bean:message key='labels.betweenSign'/> <!-- ～ -->
								<html:text property="customerCodeTo" styleId="customerCodeTo" style="width: 150px; ime-mode: disabled;" maxlength="15" tabindex="108" />
								<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(2)" tabindex="109" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			</div>
			</div>
			</div>
		</s:form>
	</div>
</body>
</html>
