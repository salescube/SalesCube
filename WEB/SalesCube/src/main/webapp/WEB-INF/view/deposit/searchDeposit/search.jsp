<%@page import="jp.co.arkinfosys.common.Constants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title><bean:message key='titles.system'/> <bean:message key='titles.searchDeposit'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>
	<script type="text/javascript" src="${f:url('/scripts/dialogs.js')}"></script>

	<script type="text/javascript">
	<!--
		var paramData = null;
		var paramDataTmp = null;

		// ページ読込時の動作
		function init() {
			// 初期フォーカス設定
			$("#depositSlipId").focus();
			$("#customerCode").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁
		}

		// 入力担当者検索
		function openUserSearchDialog(type){
			var id = "UserSearch";

			// 担当者ダイアログを開く
			openSearchUserDialog(type, setUserInfo );
			if(type == 1) {
				// 担当者コードを設定する
				$("#"+type+"_userId").val($("#userId").val());
			} else if(type == 2) {
				// 担当者名を設定する
				$("#"+type+"_nameKnj").val($("#userName").val());
			}
		}

		// 入力担当者情報設定
		function setUserInfo(type, map) {
			if(type == 1) {
				// 担当者コードを設定する
				$("#userId").val(map["userId"]);
			} else if(type == 2) {
				// 担当者名を設定する
				$("#userName").val(map["nameKnj"]);
			}
		}

		// 顧客検索
		function openCustomerSearchDialog(type){
			var id = "CustomerSearch";

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

		// 顧客情報設定
		function setCustomerInfo(type, map) {
			if(type == 1) {
				// 顧客コードを設定する
				$("#customerCode").val(map["customerCode"]);
			} else if(type == 2) {
				// 顧客名を設定する
				$("#customerName").val(map["customerName"]);
			}
		}

		// 初期化
		function onF1(){
			// 入力内容を初期化してよろしいですか？
			if(confirm('<bean:message key="confirm.init" />')){
				window.location.doHref('${f:url("/deposit/searchDeposit")}');
			}
		}

		// 検索
		function onF2(){

			paramData = createParamData();
			paramData["pageNo"] = 1;

			// 検索を実行する
			execSearch(paramData);
		}

		// EXCEL
		function onF3(){
			// 検索結果をEXCELファイルでダウンロードしますか？
			if(!confirm('<bean:message key="confirm.excel.result" />')){
				return;
			}

			var form = $(window.document.forms["OutputForm"]);
			form.empty();

			for(var key in paramDataTmp) {
				if(!paramDataTmp[key] ||
						(typeof paramDataTmp[key].length != undefined && paramDataTmp[key].length == 0)) {
					continue;
				}

				var hidden = $(document.createElement("input"));
				hidden.attr("type", "hidden");
				hidden.attr("name", key);
				hidden.val(paramDataTmp[key]);
				form.append(hidden);
			}

			form.submit();
		}

		// 設定
		function onF4(){
			openDetailDispSettingDialog('detailDisp', '0601', '1');
		}

		// ページ遷移
		function goPage(no) {
			// 検索条件を設定する
			paramData = paramDataTmp;
			paramData["pageNo"] = no;
			// 検索を実行する
			execSearch(paramData);
		}

		// 全て選択/全て解除
		function onSelectAll(flag) {
			$("input[type='checkbox'][id^='depositCategory_']").each(function() {
				$(this).attr("checked",(flag==true?"checked":""));
			});
		}

		// 検索パラメータの作成
		function createParamData() {
			paramData = new Object();
			paramData["depositSlipId"] = $("#depositSlipId").val();
			paramData["userId"] = $("#userId").val();
			paramData["userName"] = $("#userName").val();
			paramData["depositDateFrom"] = $("#depositDateFrom").val();
			paramData["depositDateTo"] = $("#depositDateTo").val();
			paramData["inputPdateFrom"] = $("#inputPdateFrom").val();
			paramData["inputPdateTo"] = $("#inputPdateTo").val();
			paramData["depositTotalFrom"] = $("#depositTotalFrom").val();
			paramData["depositTotalTo"] = $("#depositTotalTo").val();
			paramData["depositAbstract"] = $("#depositAbstract").val();
			paramData["customerCode"] = $("#customerCode").val();
			paramData["customerName"] = $("#customerName").val();
			paramData["paymentName"] = $("#paymentName").val();
			paramData["depositMethodTypeCategory"] = $("#depositMethodTypeCategory").val();
			paramData["depositCategory"] = createDepositCategoryArray();
			paramData["rowCount"] = $("#rowCount").val();
			paramData["sortColumn"] = $("#sortColumn").val();
			paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
			return paramData;
		}

		// 入金区分配列生成
		function createDepositCategoryArray() {
			var arry = new Array();
			$("input[type='checkbox'][id^='depositCategory_']").each(function() {
				if ($(this).attr("checked") == true) {
					arry.push($(this).val());
				}
			});
			return arry;
		}

		// 検索実行
		function execSearch(paramData) {
			// Ajaxリクエストによって検索結果をロードする
			asyncRequest(
				contextRoot + "/ajax/deposit/searchDepositResultAjax/search",
				paramData,
				function(data) {
					// 検索結果テーブルを更新する
					$("#errors").empty();
					$("#listContainer").empty();
					$("#listContainer").append(data);

					// EXCELボタンの状態変更
					if($("#searchResultCount").val() != "0") {
						// 検索条件を保持
						paramDataTmp = paramData;

						$("#btnF3").attr("disabled","");
					} else {
						$("#btnF3").attr("disabled","disabled");
					}
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

		// ソート
		function sort(sortColumn) {
			// 前回のソートカラムとソート順を取得
			var beforeSortColumn = $("#sortColumn").val();
			var beforeSortOrderAsc = $("#sortOrderAsc").val();

			// 前回のソートカラムからソートラベルを削除
			if($("#sortStatus_"+beforeSortColumn).get(0)) {
				$("#sortStatus_"+beforeSortColumn).empty();
			}

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

			// 今回のソートカラムにソートラベルを追加
			if($("#sortOrderAsc").val() == "true") {
				$("#sortStatus_"+sortColumn).html("<bean:message key='labels.asc'/>");
			} else {
				$("#sortStatus_"+sortColumn).html("<bean:message key='labels.desc'/>");
			}

			// 1回以上検索しており、前回の結果が1件以上ある場合のみ再検索
			if(paramDataTmp != null && $("#searchResultCount").val() != "0") {
				// 前回の検索条件からソート条件のみを変更
				paramData = paramDataTmp;
				paramData["pageNo"] = 1;
				paramData["sortColumn"] = $("#sortColumn").val();
				paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
				// 検索
				execSearch(paramData);
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
		<jsp:param name="PARENT_MENU_ID" value="0006"/>
		<jsp:param name="MENU_ID" value="0601"/>
	</jsp:include>

	<!-- メイン機能 -->
	<div id="main_function">

		<!-- タイトル -->
		<span class="title"><bean:message key='titles.searchDeposit'/></span>

		<!-- ファンクションボタン -->
		<div class="function_buttons">
			<button id="btnF1" tabindex="2000" onclick="onF1()">F1<br><bean:message key='words.action.initialize'/></button>
			<button id="btnF2" tabindex="2001" onclick="onF2()">F2<br><bean:message key='words.action.search'/></button>
			<button id="btnF3" tabindex="2002" onclick="onF3()" disabled="disabled">F3<br><bean:message key='words.name.excel'/></button>
			<button id="btnF4" tabindex="2003" onclick="onF4()">F4<br><bean:message key='words.action.setting'/></button>
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

			<!-- 検索条件 -->
			<div class="function_forms">
				<div id="errors" style="color: red">
					<html:errors/>
				</div>

				<div class="form_section_wrap">
				<div class="form_section">
					<div class="section_title">
						<bean:message key='labels.searchCondition'/><br>
						<button class="btn_toggle" />
					</div><!-- /.section_title -->

					<div id="search_info" class="section_body">
						<table id="search_target" class="forms" summary="searchTarget">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.depositSlipId'/></div></th> <!-- 入金番号 -->
								<td><html:text property="depositSlipId" styleId="depositSlipId" style="width: 100px; ime-mode: disabled;" tabindex="100" /></td>
								<th><div class="col_title_right"><bean:message key='labels.tantou.userId'/></div></th> <!-- 入力担当者コード -->
								<td><html:text property="userId" styleId="userId" style="width: 150px; ime-mode: disabled;" tabindex="101" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openUserSearchDialog(1)" tabindex="102" />
								</td>
								<th><div class="col_title_right"><bean:message key='labels.tantou.userName'/></div></th> <!-- 入力担当者名 -->
								<td><html:text property="userName" styleId="userName" style="width: 150px;" tabindex="103" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openUserSearchDialog(2)" tabindex="104" />
								</td>
							</tr>
						</table>
						<table id="search_target1" class="forms" summary="searchTarget1" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.depositDate'/></div></th> <!-- 入金日 -->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="depositDateFrom" styleId="depositDateFrom" style="width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="105" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/> <!-- ～ -->
								</td>
								<td>
									<div class="pos_r">
										<html:text property="depositDateTo" styleId="depositDateTo" style="width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="106" />
									</div>
								</td>
								<th><div class="col_title_right"><bean:message key='labels.inputPdate'/></div></th> <!-- 入力日 -->
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="inputPdateFrom" styleId="inputPdateFrom" style="width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="107" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/> <!-- ～ -->
								</td>
								<td>
									<div class="pos_r">
										<html:text property="inputPdateTo" styleId="inputPdateTo" style="width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="108" />
									</div>
								</td>
							</tr>
						</table>
						<table id="search_target2" class="forms" summary="searchTarget2">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.depositTotal'/></div></th> <!-- 回収金額 -->
								<td>
									<html:text property="depositTotalFrom" styleId="depositTotalFrom" style="width: 75px; ime-mode: disabled;" tabindex="109" />
									<bean:message key='labels.betweenSign'/> <!-- ～ -->
									<html:text property="depositTotalTo" styleId="depositTotalTo" style="width: 75px; ime-mode: disabled;" tabindex="110" />
								</td>
								<th><div class="col_title_right"><bean:message key='labels.memorandum'/></div></th> <!-- 摘要 -->
								<td colspan="5"><html:text property="depositAbstract" styleId="depositAbstract" style="width: 300px;" tabindex="111" /></td>
							</tr>
						</table>

						<table id="customer_info" class="forms" summary="customerInfo">
							<colgroup>
								<col span="1" style="width: 13%">
								<col span="1" style="width: 20%">
								<col span="1" style="width: 13%">
								<col span="1" style="width: 20%">
								<col span="1" style="width: 13%">
								<col span="1" style="width: 21%">
							</colgroup>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.customerCode'/></div></th> <!-- 顧客コード -->
								<td>
									<html:text property="customerCode" styleId="customerCode" style="width: 150px; ime-mode: disabled;" tabindex="200" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(1)" tabindex="201" />
								</td>
								<th><div class="col_title_right"><bean:message key='labels.customerName'/></div></th> <!-- 顧客名 -->
								<td>
									<html:text property="customerName" styleId="customerName" style="width: 150px; ime-mode: auto;" tabindex="202" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openCustomerSearchDialog(2)" tabindex="203" />
								</td>
								<th><div class="col_title_right"><bean:message key='labels.paymentName'/></div></th> <!-- 振込名義 -->
								<td>
									<html:text property="paymentName" styleId="paymentName" style="width: 150px; ime-mode: auto;" tabindex="204" />
								</td>
							</tr>
						</table>

						<table id="deposit_info" class="forms" summary="dedpositInfo">
							<colgroup>
								<col span="1" style="width: 13%">
								<col span="1" style="width: 87%">
							</colgroup>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.depositMethodType'/></div></th><!-- 入金取込 -->
								<td>
									<html:select property="depositMethodTypeCategory" styleId="depositMethodTypeCategory" style="width: 500px;" tabindex="300">
										<html:options collection="depositMethodTypeCategoryList" property="value" labelProperty="label"/>
									</html:select>
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.depositCategory'/></div></th> <!-- 入金区分 -->
								<td>
									<c:forEach var="item" items="${depositCategoryList}" varStatus="status">
										<input type="checkbox" id="depositCategory_${f:h(status.index)}" value="${item.value}" tabindex="301"/>${f:h(item.label)}&nbsp
									</c:forEach>
									<br>
									<button id="btnSelectAll" tabindex="302" onclick="onSelectAll(true)" class="btn_list_action"><bean:message key='words.action.selectAll'/></button> <!-- 全て選択-->
									<button id="btnSelectNone" tabindex="303" onclick="onSelectAll(false)" class="btn_list_action"><bean:message key='words.action.selectNone'/></button> <!-- 全て解除-->
								</td>
							</tr>
						</table>
					</div>
					<html:hidden property="sortColumn" styleId="sortColumn" />
					<html:hidden property="sortOrderAsc" styleId="sortOrderAsc" />
				</div><!-- /.form_section -->
		    	</div><!-- /.form_section_wrap -->
			</div>
		</s:form>

		<form name="OutputForm" action="${f:url('/deposit/searchDepositResultOutput/excel')}" target="_blank" style="display: none;" method="POST">
		</form>

		<div style="width: 1160px; text-align: right">
			<button type="button" tabindex="350" onclick="onF1();" class="btn_medium"><bean:message key='words.action.initialize'/></button> <!-- 初期化 -->
			<button type="button" tabindex="351" onclick="onF2();"class="btn_medium"><bean:message key='words.action.search'/></button> <!-- 検索 -->
		</div>
		<span id="listContainer">
			<%@ include file="/WEB-INF/view/ajax/deposit/searchDepositResultAjax/result.jsp" %>
		</span>
	</div>
</body>
</html>
