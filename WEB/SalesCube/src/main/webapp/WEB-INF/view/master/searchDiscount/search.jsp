<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　数量割引マスタ管理（検索）</title>
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
        searchDiscount();
    }
    function onF3() {
        addDiscount();
    }

	/**
	 * 初期化ボタン押下
	 */
	function initForm() {
		// 入力内容を初期化してよろしいですか？
		if(confirm('<bean:message key="confirm.init" />')){
			window.location.doHref('${f:url("/master/searchDiscount")}');
		}
	}

    function searchDiscount() {
		if(!confirm('<bean:message key="confirm.search" />')){
			return;
		}
		return execSearch(createData());
    }

	function deleteDiscount(discountId, updDatetm) {
		if(!confirm('<bean:message key="confirm.delete" />')){
			return;
		}

		// 削除実行
		asyncRequest(
			"${f:url("/ajax/master/deleteDiscountAjax/delete")}",
			{ "discountId": discountId, "updDatetm": updDatetm },
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
    function addDiscount() {
		window.location.doHref("${f:url("/master/editDiscount")}");
    }

	// 編集
	function editDiscount(discountId){
		window.location.doHref("${f:url("/master/editDiscount/edit/")}" + discountId);
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

		// 割引コード
		var id = "#" + prev + "discountId";
		if($(id).val()) {
			paramData["discountId"] = $(id).val();
		}
		// 割引名
		id = "#" + prev + "discountName";
		if($(id).val()) {
			paramData["discountName"] = $(id).val();
		}
		// 割引有効
		id = "#" + prev + "useFlag";
		if(prev) {
			if($(id).val()) {
				paramData["useFlag"] = $(id).val();
			}
		}
		else {
			var useFlag = $(id).get(0);
			if(useFlag.selectedIndex > 0) {
				paramData["useFlag"] = useFlag.options[ useFlag.selectedIndex ].value;
			}
		}

        // 備考
		id = "#" + prev + "remarks";
		if($(id).val()) {
			paramData["remarks"] = $(id).val();
		}

		// 表示件数
		id = "#" + prev + "rowCount";
		if(prev) {
			paramData["rowCount"] = $(id).attr("value");
		}
		else {
			var rowCount = $(id).get(0);
			paramData["rowCount"] = rowCount.options[ rowCount.selectedIndex ].value;
		}

		// ソート列
		id = "#" + prev + "sortColumn";
		paramData["sortColumn"] = $(id).val();

		// ソート昇順フラグ
		id = "#" + prev + "sortOrderAsc";
		paramData["sortOrderAsc"] = $(id).val();

		return paramData;
	}

	function execSearch(paramData) {
		if(!paramData["pageNo"]) {
			// ページの設定がなければ1ページ
			paramData["pageNo"] = 1;
		}

		// 検索実行
		asyncRequest(
			"${f:url("/ajax/master/searchDiscountAjax/search")}",
			paramData,
			function(data) {
				// 検索結果件数が1件であれば編集画面に遷移する
				var jData = $(data);
				if(jData.is("#singleDiscountId")) {
					var discountId = jData.filter("#singleDiscountId");
					window.location.doHref("${f:url("/master/editDiscount/edit/")}" + discountId.val());
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
<body onhelp="return false;" onload="init()">


<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>


<jsp:include page="/WEB-INF/view/common/menubar.jsp">
	<jsp:param name="PARENT_MENU_ID" value="0013"/>
	<jsp:param name="MENU_ID" value="1305"/>
</jsp:include>

<!-- メイン機能 -->
<div id="main_function">
<s:form onsubmit="return false;">
	<span class="title">数量割引</span>

	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm()">F1<br>初期化</button>
        <button tabindex="2001" onclick="searchDiscount()">F2<br>検索</button>
<c:if test="${!isUpdate}">
        <button disabled="disabled">F3<br>追加</button>
</c:if>
<c:if test="${isUpdate}">
        <button tabindex="2002" onclick="addDiscount()">F3<br>追加</button>
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

		<span>割引情報</span><br>
		<table id="user_info" class="forms" summary="割引パターン情報" style="width: 600px">
			<colgroup>
				<col span="1" style="width: 15%">
				<col span="1" style="width: 25%">
				<col span="1" style="width: 20%">
				<col span="1" style="width: 40%">
			</colgroup>
			<tr>
				<th>割引コード</th>
				<td><html:text maxlength="20" styleId="discountId" property="discountId" style="width: 100px; ime-mode: disabled;" tabindex="100"/></td>
				<th>割引有効</th>
				<td>
    				<html:select styleId="useFlag" property="useFlag" tabindex="102">
    					<html:options collection="useFlagList" property="value" labelProperty="label"/>
    				</html:select>
				</td>
			</tr>
			<tr>
				<th>割引名</th>
				<td colspan="3"><html:text maxlength="60" styleId="discountName" property="discountName" style="width: 230px"  tabindex="101"/></td>
			</tr>
			<tr>
				<th>備考</th>
				<td colspan="3"><html:text maxlength="120" styleId="remarks" property="remarks" style="width: 450px" tabindex="103"/></td>
			</tr>
		</table>

		<div style="text-align: right; width: 600px">
			<button tabindex="104" style="width:80px" onclick="initForm()">初期化</button>
			<button tabindex="105" style="width:80px" onclick="searchDiscount()">検索</button>
		</div>

		<div id="ListContainer">
			<div style="width: 910px; height: 25px;">
					<div style="position:absolute; left: 0px;">検索結果件数： 0件</div>
                    <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
			</div>


    		<table id="List" summary="検索結果" class="forms" style="width: 800px">
    			<colgroup>
    				<col span="1" style="width: 15%">
    				<col span="1" style="width: 30%">
    				<col span="1" style="width: 35%">
    				<col span="1" style="width: 10%">
    				<col span="1" style="width: 10%">
    			</colgroup>
    			<tr>
    				<th style="cursor: pointer">割引コード</th>
    				<th style="cursor: pointer">割引名</th>
    				<th style="cursor: pointer">備考</th>
    				<th style="cursor: pointer">割引有効</th>
    				<th style="cursor: pointer">&nbsp;</th>
    			</tr>
    		</table>
    	</div>
    </div>
<html:hidden styleId="sortColumn" property="sortColumn" />
<html:hidden styleId="sortOrderAsc"property="sortOrderAsc" />
</s:form>
</div>
</body>

</html>
