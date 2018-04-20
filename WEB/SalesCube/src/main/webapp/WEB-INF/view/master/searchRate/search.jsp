<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　レートマスタ管理（検索）</title>
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
        searchRate();
    }
    function onF3() {
        addRate();
    }

	/**
	 * 初期化ボタン押下
	 */
	function initForm() {
		// 入力内容を初期化してよろしいですか？
		if(confirm('<bean:message key="confirm.init" />')){
			window.location.doHref('${f:url("/master/searchRate")}');
		}
	}

    function searchRate() {

		return execSearch(createData());
    }

	function deleteRate(rateId,updDatetm) {
		if(!confirm('<bean:message key="confirm.delete" />')){
			return;
		}

		// 削除実行
		asyncRequest(
			"${f:url("/ajax/master/deleteRateAjax/delete")}",
			{
              "rateId": rateId,
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
    function addRate() {
		window.location.doHref("${f:url("/master/editRate")}");
    }

	// 編集
	function editRate(rateId){
		window.location.doHref("${f:url("/master/editRate/edit/")}" + rateId);
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

		// レートタイプ名称コード
		var id = "#" + prev + "name";
		if($(id).val()) {
			paramData["name"] = $(id).val();
		}
		// レートタイプ備考
		id = "#" + prev + "remarks";
		if($(id).val()) {
			paramData["remarks"] = $(id).val();
		}
		// 期間（開始）
		id = "#" + prev + "startDate1";
		if($(id).val()) {
			paramData["startDate1"] = $(id).val();
		}
		// 期間（開始）
		id = "#" + prev + "startDate2";
		if($(id).val()) {
			paramData["startDate2"] = $(id).val();
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
			"${f:url("/ajax/master/searchRateAjax/search")}",
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

	-->
	</script>
</head>
<body onhelp="return false;" onload="init()">

<%-- ページヘッダ領域 --%>
<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

<%-- メニュー領域 --%>
<jsp:include page="/WEB-INF/view/common/menubar.jsp">
	<jsp:param name="PARENT_MENU_ID" value="0013"/>
	<jsp:param name="MENU_ID" value="1313"/>
</jsp:include>

<!-- メイン機能 -->
<div id="main_function">
<s:form onsubmit="return false;">
	<span class="title">レート</span>
	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm()">F1<br>初期化</button>
        <button tabindex="2001" onclick="searchRate()">F2<br>検索</button>
<c:if test="${!isUpdate}">
        <button disabled="disabled">F3<br>追加</button>
</c:if>
<c:if test="${isUpdate}">
        <button tabindex="2002" onclick="addRate()">F3<br>タイプ追加</button>
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
	<br><br><br>

	<div class="function_forms">
		<div style="padding-left: 20px">
			<html:errors/>
			<span id="ajax_errors"></span>
		</div>

	    <div class="form_section_wrap">
		    <div class="form_section">
		    	<div class="section_title">
        			<span>レートタイプ検索条件</span>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="search_info" class="section_body">
					<table id="rate_code_search_condition" class="forms" summary="レートタイプ検索条件">
						<tr>
							<th><div class="col_title_right">レートタイプ名称</div></th>
							<td>
								<html:text styleId="name" property="name" style="width: 235px;" tabindex="100" maxlength="60"/>
							</td>
							<th><div class="col_title_right">レートタイプ備考</div></th>
							<td>
								<html:text styleId="remarks" property="remarks" style="width: 235px;" tabindex="101" maxlength="120"/>

							</td>
						</tr>
					</table>
					<table id="rate_code_search_condition1" class="forms" summary="レートタイプ検索条件" style="width: auto;">
						<tr>
							<th><div class="col_title_right">&nbsp;&nbsp;最新レート適用開始日&nbsp;&nbsp;</div></th>
							<td style="padding-right: 0;">
								<div class="pos_r">
									<html:text styleId="startDate1" property="startDate1" styleClass="date_input" style="width: 135px;" tabindex="102" maxlength="10"/>
								</div>
							</td>
							<td style="text-align: center; width:30px; padding-right: 0;">
								～
							</td>
							<td>
								<div class="pos_r">
									<html:text styleId="startDate2" property="startDate2" styleClass="date_input" style="width: 135px;" tabindex="103" maxlength="10"/>
								</div>
							</td>
						</tr>
					</table>
				</div>
	    	</div><!-- /.form_section -->
	    </div><!-- /.form_section_wrap -->

		<div style="text-align: right; width: 1160px">
			<button tabindex="150" onclick="initForm()" class="btn_medium">初期化</button>
			<button tabindex="151" onclick="searchRate()" class="btn_medium">検索</button>
		</div>

		<div id="ListContainer">
			<div style="width: 1010px; height: 25px;">
					<div style="position:absolute; left: 0px;">検索結果件数： 0件</div>
                    <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
			</div>

    		<table id="search_result" summary="searchResult" class="forms detail_info" style="table-layout: auto; margin-top: 20px;">
			<colgroup>
				<col span="1" style="width: 80px">
				<col span="1" style="width:200px">
				<col span="1" style="width:120px">
				<col span="1" style="width:120px">
				<col span="1" style="">
				<col span="1" style="width:120px">
			</colgroup>
			<thead>
			<tr>
				<th class="rd_top_left" style="height: 60px;">レート<br>タイプID</th>
				<th class="xl64" style="height: 60px;">レートタイプ名称</th>
				<th class="xl64" style="height: 60px;">本日付の<br>最新レート</th>
				<th class="xl64" style="height: 60px;">最新レートの<br>適用開始日</th>
				<th class="xl64" style="height: 60px;">レートタイプ備考</th>
				<th class="rd_top_right" style="height: 60px;">&nbsp;</th>
			</tr>
			</thead>
            </table>
    	</div>
    </div>
<html:hidden styleId="sortColumn" property="sortColumn" />
<html:hidden styleId="sortOrderAsc" property="sortOrderAsc" />
</s:form>
</div>
</body>

</html>

