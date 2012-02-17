<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　銀行マスタ管理（検索）</title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
    var paramData = null;
    var paramDataTmp = null;

    function init() {
        // 初期表示時に全件検索
        execSearch(createData());
    }
    function onF1() {
        initForm();
    }
    function onF2() {
        searchBank();
    }
    function onF3() {
        addBank();
    }

	/**
	 * 初期化ボタン押下
	 */
	function initForm() {
		// 入力内容を初期化してよろしいですか？
		if(confirm('<bean:message key="confirm.init" />')){
			window.location.doHref('${f:url("/master/searchBank")}');
		}
	}

    function searchBank() {
		if(!confirm('<bean:message key="confirm.search" />')){
			return;
		}
		return execSearch(createData());
    }

	function deleteBank(bankId,updDatetm) {
		if(!confirm('<bean:message key="confirm.delete" />')){
			return;
		}

		// 削除実行
		asyncRequest(
			"${f:url("/ajax/master/deleteBankAjax/delete")}",
			{
              "bankId": bankId,
              "updDatetm": updDatetm
            },
			function() {
				var data = createData(true);
				return execSearch(data);
			}
		);
	}

	//ページ繰り、ソートによる検索処理
	function goPage(page){
		var data = createData();
		data["pageNo"] = page;
		return execSearch(data);
	}

	/**
	 * クリック前のソート列
	 */
	function sort(itemId) {
		if($("#sortColumn").attr("value") == itemId) {
			// 同じ項目の場合は順序を反転する
			if($("#sortOrderAsc").attr("value") == "true") {
				$("#sortOrderAsc").attr("value", false);
			}
			else {
				$("#sortOrderAsc").attr("value", true);
			}
		}
		else {
			$("#sortOrderAsc").attr("value", true);
		}
		// ソート列を設定する
		$("#sortColumn").attr("value", itemId);
		
		// 1回以上検索しており、前回の結果が1件以上ある場合のみ再検索
		if(paramDataTmp != null && $("#searchResultCount").val() != "0") {
			// 前回の検索条件からソート条件のみを変更
			paramData = paramDataTmp;
			paramData["pageNo"] = 1;
			paramData["sortColumn"] = $("#sortColumn").val();
			paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
			// 検索
			return execSearch(paramData);
		}
	}

    // 追加
    function addBank() {
		window.location.doHref("${f:url("/master/editBank")}");
    }

	// 編集
	function editBank(bankId){
		window.location.doHref("${f:url("/master/editBank/edit/")}" + bankId);
	}

	/**
	 * リクエストパラメータ作成
	 */
	function createData(prev){
		// リクエストデータ作成
		paramData = new Object();
		var prev = "";
		if(prev) {
			prev = "prev_";
		}

		// 銀行コード
		var id = "#" + prev + "bankCode";
		if($(id).val()) {
			paramData["bankCode"] = $(id).val();
		}
		// 銀行名
		id = "#" + prev + "bankName";
		if($(id).val()) {
			paramData["bankName"] = $(id).val();
		}
		// 店名
		id = "#" + prev + "storeName";
		if($(id).val()) {
			paramData["storeName"] = $(id).val();
		}
		// 店番
		id = "#" + prev + "storeCode";
		if($(id).val()) {
			paramData["storeCode"] = $(id).val();
		}
        // 科目
        id = "#" + prev + "dwbType";
        if ($(id).val()) {
            paramData["dwbType"] = $(id).val();
        }
        // 口座番号
        id = "#" + prev + "accountNum";
        if ($(id).val()) {
            paramData["accountNum"] = $(id).val();
        }
		// ソート列
		id = "#" + prev + "sortColumn";
		paramData["sortColumn"] = $(id).val();

		// ソート昇順フラグ
		id = "#" + prev + "sortOrderAsc";
		paramData["sortOrderAsc"] = $(id).val();

		// 表示件数
		id = "#" + prev + "rowCount";
		if(prev) {
			paramData["rowCount"] = $(id).attr("value");
		}
		else {
			var rowCount = $(id).get(0);
			paramData["rowCount"] = rowCount.options[ rowCount.selectedIndex ].value;
		}

		return paramData;
	}

	function execSearch(paramData) {
		if(!paramData["pageNo"]) {
			// ページの設定がなければ1ページ
			paramData["pageNo"] = 1;
		}

		// 検索実行
		asyncRequest(
			"${f:url("/ajax/master/searchBankAjax/search")}",
			paramData,
			function(data) {
				// 検索結果件数が1件であれば編集画面に遷移する
				var jData = $(data);
				if(jData.is("#singleBankId")) {
					var bankId = jData.filter("#singleBankId");
					window.location.doHref("${f:url("/master/editBank/edit/")}" + bankId.val());
					return;
				}

				// 検索結果テーブルを更新する
				$("#ListContainer").empty();
				$("#ListContainer").append(data);

				// 1件以上ヒットした場合
				if($("#searchResultCount").val() != "0") {
					// 検索条件を保持
					paramDataTmp = paramData;
				}
			}
		);
    }
	-->
	</script>
</head>
<body onhelp="return false;" onload="init();">


<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>


<jsp:include page="/WEB-INF/view/common/menubar.jsp">
	<jsp:param name="PARENT_MENU_ID" value="0013"/>
	<jsp:param name="MENU_ID" value="1315"/>
</jsp:include>


<!-- メイン機能 -->
<div id="main_function">
<s:form styleId="SearchBankForm" onsubmit="return false;">

	<span class="title">銀行</span>

	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm()">F1<br>初期化</button>
        <button tabindex="2001" onclick="searchBank()">F2<br>検索</button>
<c:if test="${!isUpdate}">
        <button disabled="disabled">F3<br>追加</button>
</c:if>
<c:if test="${isUpdate}">
        <button tabindex="2002" onclick="addBank()">F3<br>追加</button>
</c:if>
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

	<div class="function_forms">
		<div style="padding-left: 20px">
			<html:errors/>
			<span id="ajax_errors"></span>
		</div>

		<span>銀行情報</span><br>
		<table id="user_info" class="forms" summary="銀行情報" style="width: 500px">
			<colgroup>
				<col span="1" style="width: 20%">
				<col span="1" style="width: 40%">
				<col span="1" style="width: 20%">
				<col span="1" style="width: 20%">
			</colgroup>
			<tr>
				<th>銀行コード</th>
				<td colspan="3">
					<html:text tabindex="100" maxlength="4" styleId="bankCode" property="bankCode" style="width:100px; ime-mode: disabled;"/>
				</td>
			</tr>
			<tr>
				<th>銀行名</th>
				<td colspan="3">
					<html:text tabindex="101" maxlength="20" styleId="bankName" property="bankName" style="width: 200px;"/>
				</td>
			</tr>
			<tr>
				<th>店名</th>
				<td><html:text tabindex="102" maxlength="20" styleId="storeName" property="storeName" style="width: 200px;"/></td>
				<th>店番</th>
				<td><html:text tabindex="103" maxlength="3" styleId="storeCode" property="storeCode" style="width: 50px; ime-mode: disabled;"/></td>
			</tr>
			<tr>
				<th>科目</th>
				<td colspan="3">
				<html:select styleId="dwbType" property="dwbType" tabindex="104">
					<html:options collection="dwbTypeList" property="value" labelProperty="label"/>
				</html:select>
				</td>
			</tr>
			<tr>
				<th>口座番号</th>
				<td colspan="3">
					<html:text tabindex="105" maxlength="7" styleId="accountNum" property="accountNum" style="width: 100px; ime-mode: disabled;"/>
				</td>
			</tr>
		</table>

		<div style="text-align: right; width: 500px">
			<button tabindex="150" style="width:80px" onclick="initForm()">初期化</button>
			<button tabindex="151" style="width:80px" onclick="searchBank()">検索</button>
		</div>

		<div id="ListContainer">
			<div style="width: 910px; height: 25px;">
					<div style="position:absolute; left: 0px;">検索結果件数： 0件</div>
                    <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
			</div>
		<table id="search_result" summary="検索結果" class="forms" style="width: 910px">
			<colgroup>
				<col span="1" style="width: 8%">
				<col span="1" style="width: 25%">
				<col span="1" style="width: 8%">
				<col span="1" style="width: 25%">
				<col span="1" style="width: 10%">
				<col span="1" style="width: 19%">
				<col span="1" style="width: 5%">
			</colgroup>
			<tr>
				<th style="cursor: pointer">銀行コード</th>
				<th style="cursor: pointer">銀行名</th>
				<th style="cursor: pointer">店番</th>
				<th style="cursor: pointer">店名</th>
				<th style="cursor: pointer">科目</th>
				<th style="cursor: pointer">口座番号</th>
				<th style="cursor: pointer">&nbsp;</th>
			</tr>
		</table>

	</div>
<html:hidden styleId="sortColumn" property="sortColumn" />
<html:hidden styleId="sortOrderAsc"property="sortOrderAsc" />
</s:form>
</div>
</body>

</html>
