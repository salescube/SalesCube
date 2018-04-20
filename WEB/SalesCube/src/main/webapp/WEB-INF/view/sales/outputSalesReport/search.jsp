<%@page import="jp.co.arkinfosys.common.Constants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/> <bean:message key='titles.outputSalesReport'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
		// 帳票種別
		var REPORT_ALL = "1";				// 全ての帳票

		// ステータス種別
		var STATUS_ALL = "1";				// 全て
		var STATUS_UNOUTPUT = "2";			// 未出力
		var STATUS_OUTPUT = "3";			// 出力済

		/**
		 * 帳票種別と帳票IDのマップ
		 */
		var REPORT_ID_MAP = {
			"<%=Constants.REPORT_SELECTION.VALUE_ESTIMATE%>" 				: "<%=Constants.REPORT_TEMPLATE.REPORT_ID_A%>",
			"<%=Constants.REPORT_SELECTION.VALUE_BILL%>" 					: "<%=Constants.REPORT_TEMPLATE.REPORT_ID_G%>",
			"<%=Constants.REPORT_SELECTION.VALUE_DELIVERY%>" 				: "<%=Constants.REPORT_TEMPLATE.REPORT_ID_C%>",
			"<%=Constants.REPORT_SELECTION.VALUE_TEMP_DELIVERY%>"	 		: "<%=Constants.REPORT_TEMPLATE.REPORT_ID_E%>",
			"<%=Constants.REPORT_SELECTION.VALUE_PICKING%>" 				: "<%=Constants.REPORT_TEMPLATE.REPORT_ID_J%>",
			"<%=Constants.REPORT_SELECTION.VALUE_DELIVERY_RECEIPT%>" 		: "<%=Constants.REPORT_TEMPLATE.REPORT_ID_F%>",
			"<%=Constants.REPORT_SELECTION.VALUE_PICKING_CONSTRACTION%>" 	: "<%=Constants.REPORT_TEMPLATE.REPORT_ID_J%>,<%=Constants.REPORT_TEMPLATE.REPORT_ID_K%>",
			"<%=Constants.REPORT_SELECTION.VALUE_DELIVERY_6%>" 				: "<%=Constants.REPORT_TEMPLATE.REPORT_ID_D%>",
			"<%=Constants.REPORT_SELECTION.VALUE_BILL_POST%>" 				: "<%=Constants.REPORT_TEMPLATE.REPORT_ID_H%>"
		};

		var paramDataTmp = null;
		var checkData = new Object();

		// 初期化
		function onF1(){
			// 入力内容を初期化してよろしいですか？
			if(confirm('<bean:message key="confirm.init" />')){
				window.location.doHref('${f:url("/sales/outputSalesReport")}');
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

		// PDF
		function onF3(){
			//「帳票をPDFファイルでダウンロードしますか？」
			if(!confirm('<bean:message key="confirm.pdf.report" />')){
				return;
			}

			var form = $(window.document.forms["PDFOutputForm"]);
			writeHiddenParam(form);
			form.submit();
		}

		/**
		 * 帳票の選択状態をhidden項目としてレポート出力用のフォームに設定する
		 */
		function writeHiddenParam(form) {
			form.empty();

			var outputData = getReportInfo();
			for(var key in outputData) {
				var hidden = $(document.createElement("input"));
				hidden.attr("type", "hidden");
				hidden.attr("name", key);
				hidden.val(outputData[key]);
				form.append(hidden);
			}
		}

		// 検索パラメータの作成
		function createParamData() {
			data = new Object();
			data["salesDateFrom"] = $("#salesDateFrom").val();
			data["salesDateTo"] = $("#salesDateTo").val();
			data["roSlipIdFrom"] = $("#roSlipIdFrom").val();
			data["roSlipIdTo"] = $("#roSlipIdTo").val();
			data["salesSlipIdFrom"] = $("#salesSlipIdFrom").val();
			data["salesSlipIdTo"] = $("#salesSlipIdTo").val();
			data["receptNo"] = $("#receptNo").val();
			data["excludingOutputAll"] = $("#excludingOutputAll").attr("checked");

			// 取引区分
			var salesCategoryList = document.getElementsByName("salesCategoryList");
			data["salesCategoryList"] = new Array();
			for(var i = 0; i < salesCategoryList.length; i++) {
				// checkBoxのID(取引区分データのID)
				data["salesCategoryList"].push( salesCategoryList[i].value );
			}

			data["reportSelection"] = $("#reportSel").val();
			data["statusSelection"] = $("#statusSel").val();
			data["rowCount"] = $("#rowCount").val();
			data["sortColumn"] = $("#sortColumn").val();
			data["sortOrderAsc"] = $("#sortOrderAsc").val();
			return data;
		}

		// 検索実行
		function execSearch(data, fromSaveState) {
			var url = contextRoot + "/ajax/sales/searchOutputSalesReportAjax/search";

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

					//プルダウンデフォルト選択
					$("#reportSelection").val(${reportSelDefault});
					$("#statusSelection").val(${statusSelDefault});

					paramDataTmp = data;

					if(fromSaveState) {
						setDisplayCheckState();
						adjustCheckData();
					}
					else {
						// 全ページのチェック状態をリセットする
						checkData = new Object();
					}

					// PDFボタンの状態変更
					checkCount();
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

		/**
		 * 帳票出力に必要な情報を設定する
		 * ※子画面から呼び出される
		 *
		 * 各行ごとに以下の情報を取得
		 * 1.売上番号
		 * 2.選択された帳票
		 */
		function getReportInfo(){
			// 帳票出力優先順マップ
			var sortOrderMap = {
				"<%=Constants.REPORT_SELECTION.VALUE_PICKING%>" 				: 0,
				"<%=Constants.REPORT_SELECTION.VALUE_PICKING_CONSTRACTION%>" 	: 0,
				"<%=Constants.REPORT_SELECTION.VALUE_ESTIMATE%>" 				: 1,
				"<%=Constants.REPORT_SELECTION.VALUE_DELIVERY%>" 				: 2,
				"<%=Constants.REPORT_SELECTION.VALUE_DELIVERY_6%>" 				: 2,
				"<%=Constants.REPORT_SELECTION.VALUE_DELIVERY_RECEIPT%>" 		: 3,
				"<%=Constants.REPORT_SELECTION.VALUE_TEMP_DELIVERY%>"	 		: 4,
				"<%=Constants.REPORT_SELECTION.VALUE_BILL%>" 					: 5,
				"<%=Constants.REPORT_SELECTION.VALUE_BILL_POST%>" 				: 5
			};

			var reportCount = 0;// 送信する行数
			var outputData = new Object();
			for(var salesSlipId in checkData) {
				// 帳票名を設定
				var reportIds = checkData[salesSlipId]["reportFile"].split(",");
				reportIds.sort(
					function(a, b) {
						if( sortOrderMap[ a ] == sortOrderMap[ b ] ) {
							return 0;
						}
						return sortOrderMap[ a ] > sortOrderMap[ b ] ? 1 : -1;
					}
				);

				var reportFile = "";
				for(var i = 0; i < reportIds.length; i++) {
					if(reportFile.length > 0) {
						reportFile += ",";
					}
					reportFile += REPORT_ID_MAP[ reportIds[ i ] ];
				}
				outputData["resultList["+ reportCount +"].reportFileComma"] = reportFile;

				/***************** 受注番号 *****************/
				outputData["resultList["+ reportCount +"].roSlipId"] = checkData[salesSlipId]["roSlipId"];

				/***************** 売上番号 *****************/
				outputData["resultList["+ reportCount +"].salesSlipId"] = salesSlipId;

				/***************** 日付表示フラグ *****************/
				outputData["resultList["+ reportCount +"].dispDateFlag"] = checkData[salesSlipId]["dispDateFlag"];

				reportCount++;
			}

			// 送信データの行数
			outputData["dataCount"] = reportCount;

			return outputData;
		}

		// 検索結果リスト - 選択・解除ボタン押下
		// param [選択]:true
		//       [解除]:false
		function setCheckBox(check){
			var reportKind = $("#reportSelection").val();// 帳票種別
			var reportStatus = $("#statusSelection").val();// 出力状態

			// 帳票種別の絞り込み条件設定
			var condReportKind = "";
			if(reportKind != REPORT_ALL){
				condReportKind = "[id^='report_" + reportKind + "']";
			}

			// 出力状態の絞り込み条件設定
			var condStatus = "";
			switch(reportStatus){
			case STATUS_ALL://	 全て
				break;
			case STATUS_UNOUTPUT:// 未出力
				condStatus = "[id$='_yet']";
				break;
			case STATUS_OUTPUT:// 出力済
				condStatus = "[id$='_already']";
				break;
			};

			// 帳票種別と出力状態の条件を結合
			condition = condReportKind + condStatus;

			// [全ての帳票][全て]の場合は以下の条件を設定する
			if(condition == ""){
				condition = "[id^='report_']";
			}
			condition += ":enabled";

			// チェックボックス設定
			$("input" + condition).attr("checked",check);

			// チェック状態を更新する
			setCheckStateBackground(check, reportKind, reportStatus);

			// PDFボタン制御
			checkCount();
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
			var reportInfos = doc.getElementsByName("reportInfo");
			if(reportInfos == null) {
				return;
			}

			var len = reportInfos.length;
			if(len == null && reportInfos.value != null) {
				var tempArray = new Object();
				tempArray.push(reportInfos);
				reportInfos = tempArray;
				len = 1;
			}

			var salesSlipIdHash = new Object();
			for(var i = 0; i < reportInfos.length; i++) {
				salesSlipIdHash[ reportInfos[ i ].value.split(":")[1] ] = true;
			}

			for(var salesSlipId in checkData) {
				if(!salesSlipIdHash[salesSlipId]) {
					delete checkData[ salesSlipId ];
				}
			}
		}

		/**
		 * 全体のチェック状態を更新する
		 */
		function setCheckStateBackground(check, reportKind, reportStatus) {
			// チェックする場合は検索結果全体に対して処理する
			var doc = window.document;
			var reportInfos = doc.getElementsByName("reportInfo");
			if(reportInfos == null) {
				return;
			}

			var len = reportInfos.length;
			if(len == null && reportInfos.value != null) {
				// 1件の場合も同様に処理するために配列に変換
				var tempArray = new Object();
				tempArray.push(reportInfos);
				reportInfos = tempArray;
				len = 1;
			}

			for(var i = 0; i < len; i++) {
				if(check) {
					createAndUpdateCheckData(reportInfos[ i ].value, reportKind, reportStatus);
				}
				else {
					removeCheckData(reportInfos[ i ].value, reportKind, reportStatus);
				}
			}
		}

		/**
		 * チェックを外す場合、記憶しているチェック状態を更新する
		 */
		function removeCheckData(reportInfoStr, reportKind, reportStatus) {
			var reportInfo = reportInfoStr.split(":");
			var data = checkData[ reportInfo[ 1 ] ];
			if(!data) {
				return;
			}

			var reportFiles = reportInfo[ 2 ].split(",");
			var printCounts = reportInfo[ 3 ].split(",");
			var filesInfoHash = new Object();
			for(var i = 0; i < reportFiles.length; i++) {
				filesInfoHash[ reportFiles[ i ] ] = printCounts[ i ];
			}

			var currentFiles = data[ "reportFile" ].split(",");
			var currentCounts = data[ "printCount" ].split(",");

			var newCheckedFilesStr = "";
			var newPrintCountsStr = "";
			for(var i = 0; i < currentFiles.length; i++) {
				var reportId = currentFiles[ i ];
				var printCount = filesInfoHash[ reportId ];

				if(reportKind == REPORT_ALL
					|| reportKind == reportId
					|| (reportKind == "<%=Constants.REPORT_SELECTION.VALUE_PICKING%>" && reportId == "<%=Constants.REPORT_SELECTION.VALUE_PICKING_CONSTRACTION%>")
					|| (reportKind == "<%=Constants.REPORT_SELECTION.VALUE_DELIVERY%>" && reportId == "<%=Constants.REPORT_SELECTION.VALUE_DELIVERY_6%>")
					|| (reportKind == "<%=Constants.REPORT_SELECTION.VALUE_BILL%>" && reportId == "<%=Constants.REPORT_SELECTION.VALUE_BILL_POST%>")) {
					// 帳票種類が一致

					if(reportStatus == STATUS_ALL
						|| (reportStatus == STATUS_UNOUTPUT && parseInt(printCount) == 0)
						|| (reportStatus == STATUS_OUTPUT && parseInt(printCount) > 0)) {
						// 出力状態が一致
						continue;
					}
				}

				newCheckedFilesStr += "," + reportId;
				newPrintCountsStr += "," + printCount;
			}

			if(newCheckedFilesStr == "") {
				// 未チェックとなった場合は削除する
				delete checkData[ reportInfo[ 1 ] ];
			}
			else {
				if(newCheckedFilesStr.charAt(0) == ","){
					newCheckedFilesStr = newCheckedFilesStr.substr(1);
					newPrintCountsStr = newPrintCountsStr.substr(1);
				}
				// 更新
				data["reportFile"] = newCheckedFilesStr;
				data["printCount"] = newPrintCountsStr;
			}
		}

		/**
		 * チェックする場合、既にチェック情報があるものは更新、無いものは作成する
		 */
		function createAndUpdateCheckData(reportInfoStr, reportKind, reportStatus) {
			var reportInfo = reportInfoStr.split(":");

			var reportFiles = reportInfo[ 2 ].split(",");
			var printCounts = reportInfo[ 3 ].split(",");
			if(reportFiles == null || reportFiles.length == 0) {
				return;
			}

			// 既存チェック情報があれば取得する
			var data = checkData[ reportInfo[ 1 ] ];
			var filesInfoHash = new Object();
			if(data) {
				var currentFiles = data[ "reportFile" ].split(",");
				var currentCounts = data[ "printCount" ].split(",");
				for(var i = 0; i < currentFiles.length; i++) {
					filesInfoHash[ currentFiles[ i ] ] = currentCounts[ i ];
				}
			}

			for(var i = 0; i < reportFiles.length; i++) {
				var reportId = reportFiles[ i ];
				var printCount = printCounts[ i ];
				if(reportKind == REPORT_ALL
					|| reportKind == reportId
					|| (reportKind == "<%=Constants.REPORT_SELECTION.VALUE_PICKING%>" && reportId == "<%=Constants.REPORT_SELECTION.VALUE_PICKING_CONSTRACTION%>")
					|| (reportKind == "<%=Constants.REPORT_SELECTION.VALUE_DELIVERY%>" && reportId == "<%=Constants.REPORT_SELECTION.VALUE_DELIVERY_6%>")
					|| (reportKind == "<%=Constants.REPORT_SELECTION.VALUE_BILL%>" && reportId == "<%=Constants.REPORT_SELECTION.VALUE_BILL_POST%>")) {
					// 帳票種類が一致

					if(reportId == "<%=Constants.REPORT_SELECTION.VALUE_TEMP_DELIVERY%>" && reportInfo[ 5 ] == "0") {
						// 仮納品書の場合は出力可能フラグをチェック
						continue;
					}

					if(reportStatus == STATUS_ALL
							|| (reportStatus == STATUS_UNOUTPUT && parseInt(printCount) == 0)
							|| (reportStatus == STATUS_OUTPUT && parseInt(printCount) > 0)) {
						// 出力状態が一致

						if(filesInfoHash[ reportId ] == null) {
							// 既存の選択にない場合追加する
							filesInfoHash[ reportId ] = printCount;
						}
					}
				}
			}

			var checkedFilesStr = "";
			var printCountsStr = "";
			for(var id in filesInfoHash) {
				checkedFilesStr += "," + id;
				printCountsStr += "," + filesInfoHash[ id ];
			}
			if(checkedFilesStr.charAt(0) == ","){
				checkedFilesStr = checkedFilesStr.substr(1);
				printCountsStr = printCountsStr.substr(1);
			}

			if(data) {
				data[ "reportFile" ] = checkedFilesStr;
				data[ "printCount" ] = printCountsStr;
			}
			else {
				if(checkedFilesStr.length == 0) {
					return;
				}
				data = new Object();
				data[ "salesSlipId" ] = reportInfo[ 1 ]
				data[ "roSlipId" ] = reportInfo[ 0 ]
				data[ "reportFile" ] = checkedFilesStr;
				data[ "printCount" ] = printCountsStr;
				data[ "dispDateFlag" ] = reportInfo[ 4 ];
				checkData[ reportInfo[ 1 ] ] = data;
			}
		}

		// チェックの個数が0個の場合、PDFボタンを押下不可にする
		function checkCount(trId) {
			if(trId) {
				// 行の状態を記憶する
				saveDisplayCheckState(trId);
			}

			for(var salesSlipId in checkData) {
				$("#btnF3").attr("disabled","");
				return;
			}

			$("#btnF3").attr("disabled","disabled");
		}

		// ページ遷移
		function goPage(no) {
			// 検索条件を設定する
			var paramData = paramDataTmp;
			paramData["pageNo"] = no;
			// 検索を実行する
			execSearch(paramData, true);
		}

		// ソート
		function sort(sortColumn) {
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
		 * 表示中ページのチェック状態を記憶する
		 * 行ID指定の場合はその行のチェック状態のみ記憶する
		 */
		function saveDisplayCheckState(trId) {
			var tr = $("tr[id='" + trId + "']");
			var i = parseInt(trId.replace("resultTr", ""));
			var reportFile = "";
			var printCount = "";

			// ピッキングリスト
			td = tr.find("#reportPickingListTd" + i);
			var checked = td.find("[id^='report_']:checked");
			var count = td.find("input[name='printCount']");
			if(checked.size() > 0) {
				reportFile += checked.get(0).value;
				printCount += count.val();
			}

			// 見積書
			var td = tr.find("#reportEstimateTd" + i);
			var checked = td.find("[id^='report_']:checked");
			var count = td.find("input[name='printCount']");
			if(checked.size() > 0) {
				reportFile += "," + checked.get(0).value;
				printCount += "," + count.val();
			}

			// 納品書
			td = tr.find("#reportDeliveryTd" + i);
			var checked = td.find("[id^='report_']:checked");
			var count = td.find("input[name='printCount']");
			if(checked.size() > 0) {
				reportFile += "," + checked.get(0).value;
				printCount += "," + count.val();
			}

			// 納品書兼領収書
			td = tr.find("#reportDeriveryReceiptTd" + i);
			var checked = td.find("[id^='report_']:checked");
			var count = td.find("input[name='printCount']");
			if(checked.size() > 0) {
				reportFile += "," + checked.get(0).value;
				printCount += "," + count.val();
			}

			// 仮納品書
			td = tr.find("#reportTempDeliveryTd" + i);
			var checked = td.find("[id^='report_']:checked");
			var count = td.find("input[name='printCount']");
			if(checked.size() > 0) {
				reportFile += "," + checked.get(0).value;
				printCount += "," + count.val();
			}

			// 請求書
			td = tr.find("#reportBillTd" + i);
			var checked = td.find("[id^='report_']:checked");
			var count = td.find("input[name='printCount']");
			if(checked.size() > 0) {
				reportFile += "," + checked.get(0).value;
				printCount += "," + count.val();
			}

			var salesSlipId = $("#reportSalesSlipId" + i).val();
			if(reportFile.length == 0) {
				// 未チェックは記憶しない
				delete checkData[ salesSlipId ];
				return;
			}
			// 先頭のCommaを取り除く
			if(reportFile.charAt(0) == ","){
				reportFile = reportFile.substr(1);
				printCount = printCount.substr(1);
			}

			var roSlipId = $("#reportRoSlipId" + i).val();
			var dispDateFlag = $("#dispDateFlag" + i).val();

			// 売上伝票番号をキーとするハッシュに保持する
			checkData[ salesSlipId ] = {
				"roSlipId": roSlipId,
				"salesSlipId": salesSlipId,
				"reportFile": reportFile,
				"printCount" : printCount,
				"dispDateFlag": dispDateFlag
			};
		}

		/**
		 * 画面内のチェックボックスのチェックに保存した状態を反映する
		 */
		function setDisplayCheckState() {
			var resultTrList = $("tr[id^='resultTr']");
			var size = resultTrList.size();
			for(var i = 0; i < size; i++ ) {
				var tr = $(resultTrList.get(i));
				var salesSlipId = $("#reportSalesSlipId" + i).val();

				tr.find("[id^='report_']").attr("checked", false);

				var data = checkData[ salesSlipId ];
				if(data == null) {
					continue;
				}

				var reportFile = data["reportFile"];
				var files = reportFile.split(",");
				if(files == null || files.length == 0) {
					continue;
				}

				for(var j = 0; j < files.length; j++ ) {
					// 見積書
					var td = tr.find("#reportEstimateTd" + i);
					var check = td.find("[id^='report_']");
					if("<%=Constants.REPORT_SELECTION.VALUE_ESTIMATE%>" == files[j]) {
						check.attr("checked", true);
						continue;
					}

					// 請求書
					td = tr.find("#reportBillTd" + i);
					check = td.find("[id^='report_']");
					if("<%=Constants.REPORT_SELECTION.VALUE_BILL%>" == files[j] ||
							"<%=Constants.REPORT_SELECTION.VALUE_BILL_POST%>" == files[j]) {
						check.attr("checked", true);
						continue;
					}

					// 納品書
					td = tr.find("#reportDeliveryTd" + i);
					check = td.find("[id^='report_']");
					if("<%=Constants.REPORT_SELECTION.VALUE_DELIVERY%>" == files[j] ||
							"<%=Constants.REPORT_SELECTION.VALUE_DELIVERY_6%>" == files[j]) {
						check.attr("checked", true);
						continue;
					}

					// 仮納品書
					td = tr.find("#reportTempDeliveryTd" + i);
					check = td.find("[id^='report_']");
					if("<%=Constants.REPORT_SELECTION.VALUE_TEMP_DELIVERY%>" == files[j]) {
						check.attr("checked", true);
						continue;
					}

					// ピッキングリスト
					td = tr.find("#reportPickingListTd" + i);
					check = td.find("[id^='report_']");
					if("<%=Constants.REPORT_SELECTION.VALUE_PICKING%>" == files[j] ||
							"<%=Constants.REPORT_SELECTION.VALUE_PICKING_CONSTRACTION%>" == files[j]) {
						check.attr("checked", true);
						continue;
					}

					// 納品書兼領収書
					td = tr.find("#reportDeriveryReceiptTd" + i);
					check = td.find("[id^='report_']");
					if("<%=Constants.REPORT_SELECTION.VALUE_DELIVERY_RECEIPT%>" == files[j]) {
						check.attr("checked", true);
						continue;
					}
				}
			}
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
		<jsp:param name="MENU_ID" value="0402"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">

		<!-- タイトル -->
		<span class="title"><bean:message key='titles.outputSalesReport'/></span>


		<div class="function_buttons">
			<button type="button" id="btnF1" tabindex="2000" onclick="onF1()">F1<br><bean:message key='words.action.initialize'/></button>
			<button type="button" id="btnF2" tabindex="2001" onclick="onF2()">F2<br><bean:message key='words.action.search'/></button>
			<button type="button" id="btnF3" tabindex="2002" onclick="onF3()" ${searchResultExist ? "onclick='onF3()'" : "disabled='disabled'"}>F3<br><bean:message key='words.name.pdf'/></button>
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
		<html:hidden property="sortOrderAsc" styleId="sortOrderAsc" value="true" />

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
					<table id="search_info1" class="forms" style="width: 750px; width: auto;" summary="出力条件">
						<tr>

							<th width="100px"><div class="col_title_right"><bean:message key='labels.salesDate'/></div></th><!-- 売上日 -->
							<!--
							<td><html:text styleId="salesDateFrom" property="salesDateFrom" styleClass="date_input" tabindex="100" />
							 ～
							<html:text styleId="salesDateTo" property="salesDateTo" styleClass="date_input" tabindex="101"/></td>
							 -->
							<td style="padding-right: 0;">
								<div class="pos_r">
									<html:text styleId="salesDateFrom" property="salesDateFrom" style="width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="100" />
								</div>
							</td>
							<td style="text-align: center; width:30px; padding-right: 0;">
								<bean:message key='labels.betweenSign'/> <!-- ～ -->
							</td>
							<td>
								<div class="pos_r">
									<html:text styleId="salesDateTo" property="salesDateTo" style="width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="101"/>
								</div>
							</td>
						</tr>
						<tr>
							<th width="100px"><div class="col_title_right"><bean:message key='labels.roSlipId'/></div></th><!-- 受注番号 -->
							<td style="padding-right: 0;">
								<div class="pos_r">
									<html:text styleId="roSlipIdFrom" property="roSlipIdFrom" style="width: 175px; ime-mode:disabled;" tabindex="102"/>
								</div>
							</td>
							<td style="text-align: center; width:30px; padding-right: 0;">
								<bean:message key='labels.betweenSign'/> <!-- ～ -->
							</td>
							<td>
								<div class="pos_r">
									<html:text styleId="roSlipIdTo" property="roSlipIdTo" style="width: 175px; ime-mode:disabled;" tabindex="103"/>
								</div>
							</td>
						</tr>
						<tr>
							<th width="100px"><div class="col_title_right"><bean:message key='labels.salesSlipId'/></div></th><!-- 売上番号 -->
							<td style="padding-right: 0;">
								<div class="pos_r">
									<html:text styleId="salesSlipIdFrom" property="salesSlipIdFrom" style="width: 175px; ime-mode:disabled;" tabindex="104"/>
								</div>
							</td>
							<td style="text-align: center; width:30px; padding-right: 0;">
								<bean:message key='labels.betweenSign'/> <!-- ～ -->
							</td>
							<td>
								<div class="pos_r">
									<html:text styleId="salesSlipIdTo" property="salesSlipIdTo" style="width: 175px; ime-mode:disabled;" tabindex="105"/>
								</div>
							</td>
						</tr>
					</table>
					<table id="search_info2" class="forms" style="width: 750px; width: auto;" summary="出力条件">
						<tr>
							<th width="100px"><div class="col_title_right"><bean:message key='labels.receptNo'/></div></th><!-- 受付番号 -->
							<td><html:text styleId="receptNo" property="receptNo" style="width: 175px; ime-mode:disabled;" tabindex="106"/></td>
						</tr>
						<tr>
							<th width="100px"><div class="col_title_right"><bean:message key='labels.salesCmCategory'/></div></th><!-- 取引区分 -->
							<td>
							<c:forEach var="salesCategory" varStatus="s" items="${salesCategoryList}">
								<input type="checkbox" name="salesCategoryList" id="salesCategoryList${s.index}" tabindex="107" value="${salesCategory.value}" checked>
								<label for="salesCategoryList${s.index}">${salesCategory.label}</label>
							</c:forEach>
							</td>
						</tr>
						<tr>
							<th width="100px"><div class="col_title_right"><bean:message key='labels.excludingOutputAll'/></div></th><!-- 全て出力済を除く -->
							<td><html:checkbox styleId="excludingOutputAll" property="excludingOutputAll" tabindex="108"/></td>
						</tr>
					</table>
				</div>
	    	</div><!-- /.form_section -->
	    	</div><!-- /.form_section_wrap -->

			<div style="width: 1160px; text-align :right;">
				<button type="button" onclick="onF1()" tabindex="150" class="btn_medium"><bean:message key='words.action.initialize'/></button><!-- 初期化 -->
				<button type="button" onclick="onF2()" tabindex="151" class="btn_medium"><bean:message key='words.action.search'/></button><!-- 検索 -->
			</div>

			<div style="width: 1160px; text-align :right">
				<html:select styleId="reportSelection" property="reportSelection" tabindex="1000" style="width: 350px">
					<html:options collection="reportSelectionList" property="value" labelProperty="label"/>
				</html:select>
				の
				<html:select styleId="statusSelection" property="statusSelection" tabindex="1001" style="width: 350px">
					<html:options collection="statusSelectionList" property="value" labelProperty="label"/>
				</html:select>
				を
				<button type="button" tabindex="1002" onclick="setCheckBox(true)" class="btn_medium"><bean:message key='words.action.select'/></button>
				<button type="button" tabindex="1003" onclick="setCheckBox(false)" class="btn_medium"><bean:message key='words.action.cancel'/></button>
			</div>
		</div>

		<!-- 検索結果表示エリア -->
		<span id="listContainer">
			<%@ include file="/WEB-INF/view/ajax/sales/searchOutputSalesReportAjax/result.jsp" %>
		</span>

		</s:form>

		<form name="PDFOutputForm" action="${f:url('/sales/outputSalesReportResult/pdf')}" style="display: none;" method="POST">
		</form>
	</div>
</body>
</html>
