<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/> <bean:message key='titles.outputInvoice'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
		var paramDataTmp = null;
		var checkData = new Object();

		// 初期化
		function onF1(){
			// 入力内容を初期化してよろしいですか？
			if(confirm('<bean:message key="confirm.init" />')){
				window.location.doHref('${f:url("/sales/outputInvoice")}');
			}
		}

		// 検索
		function onF2(){

			// 検索条件を設定する
			data = createParamData();
			data["pageNo"] = 1;
			// 検索を実行する
			execSearch(data, false);
		}

		// EXCEL
		function onF3(){
			//「帳票をEXCELファイルでダウンロードしますか？」
			if(!confirm('<bean:message key="confirm.excel.report" />')){
				return;
			}

			var form = $(window.document.forms["ExcelOutputForm"]);
			writeHiddenParam(form);
			form.submit();
		}

		/**
		 * 帳票の選択状態をhidden項目としてレポート出力用のフォームに設定する
		 */
		function writeHiddenParam(form) {
			form.empty();

			var outputData = getReportInfo();
			var salesSlipIdList = outputData["salesSlipIdList"];
			for(var i = 0; i < salesSlipIdList.length; i++) {
				var hidden = $(document.createElement("input"));
				hidden.attr("type", "hidden");
				hidden.attr("name", "salesIdList");
				hidden.val(salesSlipIdList[i]);
				form.append(hidden);
			}
		}

		// 検索パラメータの作成
		function createParamData() {
			data = new Object();
			data["salesDate"] = $("#salesDate").val();
			data["salesCmCategory"] = $("#salesCmCategory").val();
			data["dcCategory"] = $("#dcCategory").val();
			data["excludingOutput"] = $("#excludingOutput").attr("checked");
			data["rowCount"] = $("#rowCount").val();
			data["sortColumn"] = $("#sortColumn").val();
			data["sortColumnAsc"] = $("#sortOrderAsc").val();
			return data;
		}

		// 検索実行
		function execSearch(data, fromSaveState) {
			var url = contextRoot + "/ajax/sales/searchOutputInvoiceAjax/search";

			// Ajaxリクエストによって検索結果をロードする
			asyncRequest(
				url, data,
				function(result) {
					// 検索結果テーブルを更新する
					$("#errors").empty();
					$("#listContainer").empty();
					$("#listContainer").html(result);

					// メッセージ表示
					if( $("#searchResultMsg").val() != "" ){
						$("#errors").append($("#searchResultMsg").val());
					}

					paramDataTmp = data;

					if(fromSaveState) {
						setDisplayCheckState();
						adjustCheckData();
					}
					else {
						// 全ページのチェック状態をリセットする
						checkData = new Object();
						readAllSalesSlipId();
					}

					// EXCELボタンの状態変更
					checkCount();
				}
			);
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

			var salesSlipIdHash = new Object();
			for(var i = 0; i < slipIds.length; i++) {
				salesSlipIdHash[ slipIds[ i ].value ] = true;
			}

			for(var salesSlipId in checkData) {
				if(!salesSlipIdHash[salesSlipId]) {
					delete checkData[ salesSlipId ];
				}
			}
		}

		// 帳票出力に必要な情報を取得する
		function getReportInfo(){
			saveDisplayCheckState();

			var idList = new Array();
			for(var salesSlipId in checkData) {
				idList.push( salesSlipId );
			}

			data = new Object();
			data["salesSlipIdList"] = idList;

			return data;
		}

		// 検索結果リスト - 選択・解除ボタン押下
		// param [選択]:true
		//       [解除]:false
		function setCheckBox(check){
			// 見えているページのチェックボックス設定
			$("input[type='checkbox'][id^='row_']").attr("checked",check);

			checkData = new Object();
			if(check) {
				readAllSalesSlipId();
			}

			// Excelボタン制御
			checkCount();
		}

		// チェックの個数が0個の場合、Excelボタンを押下不可にする
		function checkCount(){
			// 各ページのチェック状態を記憶する
			saveDisplayCheckState();

			for(var salesSlipId in checkData) {
				$("#btnF3").attr("disabled","");
				return;
			}

			$("#btnF3").attr("disabled","disabled");
		}

		// ページ遷移
		function goPage(no) {
			// 各ページのチェック状態を記憶する
			saveDisplayCheckState();

			// 検索条件を設定する
			var paramData = paramDataTmp;
			paramData["pageNo"] = no;
			// 検索を実行する
			execSearch(paramData, true);
		}

		// ソート
		function sort(sortColumn) {
			// 各ページのチェック状態を記憶する
			saveDisplayCheckState();

			// 検索条件を設定する
			var paramData = paramDataTmp;
			if(paramData == null) {
				return;
			}

			// 前回のソートカラムとソート順を取得
			var beforeSortColumn = paramData["sortColumn"];
			var beforeSortOrderAsc = paramData["sortOrderAsc"];

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

			paramData["sortColumn"] = $("#sortColumn").val();
			paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
			paramData["pageNo"] = 1;

			execSearch(paramData, true);
		}

		/**
		 * 検索結果全体の売上伝票番号を読み込む(チェック状態で)
		 */
		function readAllSalesSlipId() {
			var doc = window.document;
			var checkIds = doc.getElementsByName("slipId");
			if(checkIds != null) {
				var len = checkIds.length;
				if(len) {
					for(var i = 0; i < len; i++) {
						checkData[ checkIds[ i ].value ] = true;
					}
				}
				else {
					if( checkIds.value != null ) {
						checkData[ checkIds.value ] = true;
					}
				}
			}
		}

		/**
		 * 表示中ページのチェック状態を記憶する
		 */
		function saveDisplayCheckState() {
			$("input[type='checkbox'][id^='row_']").each(
				function(){
					if($(this).attr("checked") == true){
						checkData[ this.value ] = true;
					}
					else {
						// チェックされていないものは除外
						delete checkData[ this.value ];
					}
				}
			);
		}

		/**
		 * 画面内のチェックボックスのチェックに保存した状態を反映する
		 */
		function setDisplayCheckState() {
			$("input[type='checkbox'][id^='row_']").each(
				function(){
					$(this).attr("checked", false);
					if(checkData[ this.value ]){
						$(this).attr("checked", true);
					}
				}
			);
		}
	-->
	</script>
</head>
<body>
	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0004"/>
		<jsp:param name="MENU_ID" value="0403"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">

		<!-- タイトル -->
		<span class="title"><bean:message key='titles.outputInvoice'/></span>


		<div class="function_buttons">
			<button type="button" id="btnF1" tabindex="2000" onclick="onF1()">F1<br><bean:message key='words.action.initialize'/></button>
			<button type="button" id="btnF2" tabindex="2001" onclick="onF2()">F2<br><bean:message key='words.action.search'/></button>
			<button type="button" id="btnF3" tabindex="2002" onclick="onF3()" ${searchResultExist? "onclick='onF3()'" : "disabled='disabled'"}>F3<br><bean:message key='words.name.excel'/></button>
			<button type="button" id="btnF4" tabindex="2003" disabled="disabled">F4<br>&nbsp;</button>
			<button type="button" id="btnF5" tabindex="2004" disabled="disabled">F5<br>&nbsp;</button>
			<button type="button" id="btnF6" tabindex="2005" disabled="disabled">F6<br>&nbsp;</button>
			<button type="button" id="btnF7" tabindex="2006" disabled="disabled">F7<br>&nbsp;</button>
			<button type="button" id="btnF8" tabindex="2007" disabled="disabled">F8<br>&nbsp;</button>
			<button type="button" id="btnF9" tabindex="2008" disabled="disabled">F9<br>&nbsp;</button>
			<button type="button" id="btnF10" tabindex="2009" disabled="disabled">F10<br>&nbsp;</button>
			<button type="button" id="btnF11" tabindex="2010" disabled="disabled">F11<br>&nbsp;</button>
			<button type="button" id="btnF12" tabindex="2011" disabled="disabled">F12<br>&nbsp;</button>
		</div>
		<br><br><br>

		<s:form onsubmit="return false;">

		<html:hidden property="sortColumn" styleId="sortColumn" />
		<html:hidden property="sortOrderAsc" styleId="sortOrderAsc" />

		<div class="function_forms">
			<div id="errors" style="color: red">
				<html:errors/>
			</div>

		    <div class="form_section_wrap">
		    <div class="form_section">
		    	<div class="section_title">
					<span><bean:message key='labels.outputCondition'/></span>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="search_info" class="section_body">
					<table id="search_info1" class="forms" style="width: 400px" summary="出力条件">
						<tr>
							<th><div class="col_title_right"><bean:message key='labels.salesDate'/></div></th><!-- 売上日 -->
							<td><html:text styleId="salesDate" property="salesDate" styleClass="date_input" tabindex="100" /></td>
						</tr>
						<tr>
							<th><div class="col_title_right"><bean:message key='labels.salesCmCategory'/></div></th><!-- 取引区分 -->
							<td>
								<html:select property="salesCmCategory" styleId="salesCmCategory">
									<html:options collection="salesCategoryList" property="value" labelProperty="label"/>
								</html:select><br>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right"><bean:message key='labels.dcCategory'/></div></th><!-- 配送業者 -->
							<td>
								<html:select property="dcCategory" styleId="dcCategory">
									<html:options collection="dcCategoryList" property="value" labelProperty="label"/>
								</html:select>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right"><bean:message key='labels.excludingOutput'/></div></th><!-- 出力済を除く -->
							<td><html:checkbox styleId="excludingOutput" property="excludingOutput" tabindex="103"/></td>
						</tr>
					</table>
				</div>
	    	</div><!-- /.form_section -->
	    	</div><!-- /.form_section_wrap -->

			<div style="width: 1160px; text-align :right;">
				<button type="button" onclick="onF1()" tabindex="150" class="btn_medium"><bean:message key='words.action.initialize'/></button><!-- 初期化 -->
				<button type="button" onclick="onF2()" tabindex="151" class="btn_medium"><bean:message key='words.action.search'/></button><!-- 検索 -->
			</div>

			<div style="width: 1160px; text-align :left">
				<button type="button" tabindex="1000" onclick="setCheckBox(true)" class="btn_list_action"><bean:message key='words.action.selectAll'/></button>
				<button type="button" tabindex="1001" onclick="setCheckBox(false)" class="btn_list_action"><bean:message key='words.action.selectNone'/></button>
			</div>
		</div>
		<!-- 検索結果表示エリア -->
		<span id="listContainer">
			<%@ include file="/WEB-INF/view/ajax/sales/searchOutputInvoiceAjax/result.jsp" %>
		</span>

		</s:form>

		<form name="ExcelOutputForm" action="${f:url('/sales/outputInvoiceResult/excel')}" style="display: none;" method="POST">
		</form>
	</div>
</body>
</html>
