<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　顧客ランクマスタ管理（検索）</title>

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
        searchCustomerRank();
    }
    function onF3() {
        addCustomerRank();
    }

    function onF4() {
        outputExcel();
    }

	/**
	 * 初期化ボタン押下
	 */
	function initForm() {
		// 入力内容を初期化してよろしいですか？
		if(confirm('<bean:message key="confirm.init" />')){
			window.location.doHref('${f:url("/master/searchCustomerRank")}');
		}
	}

    function searchCustomerRank() {

		return execSearch(createData());
    }

	function deleteCustomerRank(rankCode,updDatetm) {
		if(!confirm('<bean:message key="confirm.delete" />')){
			return;
		}

		// 削除実行
		asyncRequest(
			"${f:url("/ajax/master/deleteCustomerRankAjax/delete")}",
			{
              "rankCode": rankCode,
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
    function addCustomerRank() {
		window.location.doHref("${f:url("/master/editCustomerRank")}");
    }

	// 編集
	function editCustomerRank(rankCode){
		window.location.doHref("${f:url("/master/editCustomerRank/edit/")}" + rankCode);
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

		// ランクコード
		var id = "#" + prev + "rankCode";
		if($(id).val()) {
			paramData["rankCode"] = $(id).val();
		}
		// ランク名
		id = "#" + prev + "rankName";
		if($(id).val()) {
			paramData["rankName"] = $(id).val();
		}
		// 値引率１
		id = "#" + prev + "rankRate1";
		if($(id).val()) {
			paramData["rankRate1"] = $(id).val();
		}
		// 値引率２
		id = "#" + prev + "rankRate2";
		if($(id).val()) {
			paramData["rankRate2"] = $(id).val();
		}
        // 送料区分
        id = "#" + prev + "postageType";
        if ($(id).val()) {
            paramData["postageType"] = $(id).val();
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
			"${f:url("/ajax/master/searchCustomerRankAjax/search")}",
			paramData,
			function(data) {
				var jData = $(data);

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

    function outputExcel() {
    	// 検索結果をEXCELファイルでダウンロードしますか？
    	if(!confirm('<bean:message key="confirm.excel.result" />')){
    		return;
    	}
		return execExcel(createData());
    }

	function execExcel(data) {
        // ターゲットとアクションを一時的に変更
        var target = $("#SearchCustomerRankForm").attr("target");
        var action = $("#SearchCustomerRankForm").attr("action");
        $("#SearchCustomerRankForm").attr("target", "blank");
        $("#SearchCustomerRankForm").attr("action", "${f:url("/master/searchCustomerRankResultOutput/excel")}");

        // 実行
       	$("#SearchCustomerRankForm").trigger("submit");

        // ターゲットを戻しておく（他の処理がAjaxだからあまり意味無いかも）
        $("#SearchCustomerRankForm").attr("target", target);
        $("#SearchCustomerRankForm").attr("action", action);
    }

	-->
	</script>
</head>
<body onhelp="return false;" onload="init()">

<%-- ページヘッダ領域 --%>
<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

<%-- メニュー領域 --%>
<jsp:include page="/WEB-INF/view/common/menubar.jsp">
	<jsp:param name="PARENT_MENU_ID" value="0013"/>
	<jsp:param name="MENU_ID" value="1314"/>
</jsp:include>

<!-- メイン機能 -->
<div id="main_function">
<s:form styleId="SearchCustomerRankForm" onsubmit="return false;">
	<span class="title">顧客ランク</span>
	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm()">F1<br>初期化</button>
        <button tabindex="2001" onclick="searchCustomerRank()">F2<br>検索</button>
<c:if test="${!isUpdate}">
        <button disabled="disabled">F3<br>追加</button>
</c:if>
<c:if test="${isUpdate}">
        <button tabindex="2002" onclick="addCustomerRank()">F3<br>追加</button>
</c:if>
<c:if test="${isUpdate}">
        <button tabindex="2003" onclick="outputExcel()">F4<br><bean:message key='words.name.excel'/></button>
</c:if>
<c:if test="${!isUpdate}">
        <button tabindex="2003" disabled="disabled">F4<br><bean:message key='words.name.excel'/></button>
</c:if>
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

	<div class="function_forms">
		<div style="padding-left: 20px">
			<html:errors/>
			<span id="ajax_errors"></span>
		</div>

	    <div class="form_section_wrap">
		    <div class="form_section">
		    	<div class="section_title">
					<span>顧客ランク条件</span>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="search_info" class="section_body">
					<table id="user_info" class="forms" summary="顧客ランク情報" style="width: 600px">
						<tr>
							<th><div class="col_title_right">顧客ランクコード</div></th>
							<td><html:text styleId="rankCode" property="rankCode" style="width: 100px; ime-mode: disabled;"  tabindex="100"/></td>
						</tr>
						<tr>
							<th><div class="col_title_right">顧客ランク名</div></th>
							<td><html:text styleId="rankName" property="rankName" style="width: 300px;" tabindex="101"/></td>
						</tr>
						<tr>
							<th><div class="col_title_right">値引率</div></th>
							<td><html:text styleId="rankRate1" property="rankRate1" style="width: 100px; text-align:right; ime-mode: disabled;" tabindex="102"/> ％ ～ <html:text styleId="rankRate2" property="rankRate2" style="width: 100px; text-align:right; ime-mode: disabled;" tabindex="103"/> ％</td>
			                <th><div class="col_title_right">送料区分</div></th>
			                <td>
							<html:select styleId="postageType" property="postageType" tabindex="104" style="width: 120px;">
								<html:options collection="postageTypeList" property="value" labelProperty="label"/>
							</html:select>
			                </td>
						</tr>
					</table>
				</div>
	    	</div><!-- /.form_section -->
	    </div><!-- /.form_section_wrap -->

		<div style="text-align: right; width: 1160px">
			<button tabindex="150" onclick="initForm()" class="btn_medium">初期化</button>
			<button tabindex="151" onclick="searchCustomerRank()" class="btn_medium">検索</button>
		</div>

		<div id="ListContainer">
			<div style="width: 1010px; height: 25px;">
					<div style="position:absolute; left: 0px;">検索結果件数： 0件</div>
                    <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
			</div>

			<table id="search_result" summary="searchResult" class="forms detail_info" style="table-layout: auto; margin-top: 20px;">
				<colgroup>
					<col span="1" style="width: 12%">
					<col span="1" style="width: 12%">
					<col span="1" style="width: 11%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 5%">
				</colgroup>
				<tr>
					<th class="rd_top_left" style="cursor: pointer; height: 30px;" rowspan="2">顧客ランク<br>コード</th>
					<th class="xl64" style="cursor: pointer; height: 30px;" rowspan="2">顧客ランク名</th>
					<th class="xl64" style="cursor: pointer; height: 30px;" rowspan="2">値引率</th>
					<th class="xl64" style="cursor: pointer; height: 15px;" colspan="4">基準</th>
					<th class="rd_top_right" style="cursor: pointer" rowspan="2">&nbsp;</th>
				</tr>
				<tr>
					<th class="xl64" style="cursor: pointer; height: 15px;">受注回数</th>
					<th class="xl64" style="cursor: pointer; height: 15px;">在籍期間</th>
					<th class="xl64" style="cursor: pointer; height: 15px;">離脱期間</th>
					<th class="xl64" style="cursor: pointer; height: 15px;">月平均受注額</th>
				</tr>
			</table>
		</div>
	<html:hidden styleId="sortColumn" property="sortColumn" />
	<html:hidden styleId="sortOrderAsc" property="sortOrderAsc" />
</s:form>
</div>
</body>

</html>
