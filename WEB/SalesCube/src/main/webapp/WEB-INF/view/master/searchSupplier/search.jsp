<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><bean:message key='titles.system'/>　仕入先マスタ管理（検索）</title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
    <!--
    var paramDataTmp = null;

	$(
		function() {
			$("#supplierCode").focus();
		}
	);

    function init() {
        // 初期表示時に全件検索
        execSearch(createData());
    }
    function onF1() {
        initForm();
    }
    function onF2() {
        searchSupplier();
    }
    function onF3() {
        addSupplier();
    }

	/**
	 * 初期化ボタン押下
	 */
	function initForm() {
		// 入力内容を初期化してよろしいですか？
		if(confirm('<bean:message key="confirm.init" />')){
			window.location.doHref('${f:url("/master/searchSupplier")}');
		}
    }

	/**
	 * 検索処理実行
	 */
	function searchSupplier(){

		return execSearch(createData());
	}

	function deleteSupplier(supplierCode, updDatetm) {
		if(!confirm('<bean:message key="confirm.delete" />')){
			return;
		}

		// 検索実行
		asyncRequest(
			"${f:url("/ajax/master/deleteSupplierAjax/delete")}",
			{ "supplierCode": supplierCode, "updDatetm": updDatetm },
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
			var paramData = paramDataTmp;
			paramData["pageNo"] = 1;
			paramData["sortColumn"] = $("#sortColumn").val();
			paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
			// 検索
			return execSearch(paramData);
		}
	}

	function execSearch(paramData) {
		if(!paramData["pageNo"]) {
			// ページの設定がなければ1ページ
			paramData["pageNo"] = 1;
		}

		// 検索実行
		asyncRequest(
			"${f:url("/ajax/master/searchSupplierAjax/search")}",
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

	/**
	 * リクエストパラメータ作成
	 */
	function createData(prev){
		// リクエストデータ作成
		var data = new Object();
		var prev = "";
		if(prev) {
			prev = "prev_";
		}

		// 仕入先コード
		var id = "#" + prev + "supplierCode";
		if($(id).val()) {
			data["supplierCode"] = $(id).val();
		}
		// 仕入先名
		id = "#" + prev + "supplierName";
		if($(id).val()) {
			data["supplierName"] = $(id).val();
		}
		// 仕入先名カナ
		id = "#" + prev + "supplierKana";
		if($(id).val()) {
			data["supplierKana"] = $(id).val();
		}
		// 備考
		id = "#" + prev + "remarks";
		if($(id).val()) {
			data["remarks"] = $(id).val();
		}
		// 表示件数
		id = "#" + prev + "rowCount";
		if(prev) {
			data["rowCount"] = $(id).attr("value");
		}
		else {
			var rowCount = $(id).get(0);
			data["rowCount"] = rowCount.options[ rowCount.selectedIndex ].value;
		}

		// ソート列
		id = "#" + prev + "sortColumn";
		data["sortColumn"] = $(id).val();

		// ソート昇順フラグ
		id = "#" + prev + "sortOrderAsc";
		data["sortOrderAsc"] = $(id).val();

		return data;
    }

	//追加
	function addSupplier(){
		window.location.doHref("${f:url("/master/editSupplier")}");
	}

	// 編集
	function editSupplier(supplierCode){
		window.location.doHref("${f:url("/master/editSupplier/edit/")}" + supplierCode);
	}

	/**
	 * 仕入検索ダイアログを開く
	 */
	function supplierSearch(jqObject) {
		var dialogId = jqObject.attr("id") + "Dialog";
		openSearchSupplierDialog(
			dialogId,
			function(id, map) {
				if(jqObject.attr("id").search(/Code$/) > 0) {
					jqObject.val( map[ "supplierCode" ] );
				}
				else if(jqObject.attr("id").search(/Name$/) > 0) {
					jqObject.val( map[ "supplierName" ] );
				}
			}
		);

		// ダイアログのフィールドに値をセットしてフォーカス
		if(jqObject.attr("id").search(/Code$/) > 0) {
			$("#" + dialogId + "_supplierCode").val( jqObject.val() );
			$("#" + dialogId + "_supplierCode").focus();
		}
		else if(jqObject.attr("id").search(/Name$/) > 0) {
			$("#" + dialogId + "_supplierName").val( jqObject.val() );
			$("#" + dialogId + "_supplierName").focus();
		}
	}
    -->
	</script>
	<script type="text/javascript" src="./scripts/common.js"></script>
</head>
<body onhelp="return false;" onload="init()">

<%-- ページヘッダ領域 --%>
<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

<%-- メニュー領域 --%>
<jsp:include page="/WEB-INF/view/common/menubar.jsp">
	<jsp:param name="PARENT_MENU_ID" value="0013"/>
	<jsp:param name="MENU_ID" value="1303"/>
</jsp:include>

<!-- メイン機能 -->
<div id="main_function">
<s:form onsubmit="return false;">

	<span class="title">仕入先</span>

	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm();">F1<br>初期化</button>
        <button tabindex="2001" onclick="searchSupplier()">F2<br>検索</button>
		<c:if test="${!isUpdate}">
			<button disabled="disabled">F3<br>追加</button>
		</c:if>
		<c:if test="${isUpdate}">
			<button tabindex="2002" onclick="addSupplier();">F3<br>追加</button>
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
	    <div class="form_section_wrap">
		    <div class="form_section">
		    	<div class="section_title">
					<span>仕入先情報</span><br>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="search_info" class="section_body">
					<table id="supplier_info" class="forms" summary="仕入先情報">
						<tr>
							<th><div class="col_title_right">仕入先コード</div></th>
							<td>
								<html:text styleId="supplierCode" property="supplierCode" style="width: 120px;ime-mode:disabled;"  tabindex="100" maxlength="9"/>
								<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;"
									onclick="supplierSearch($('#supplierCode'));" tabindex="101" />
							</td>
							<th><div class="col_title_right">仕入先名</div></th>
							<td>
								<html:text styleId="supplierName" property="supplierName" style="width: 200px" tabindex="102" maxlength="60"/>
								<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;"
									onclick="supplierSearch($('#supplierName'));" tabindex="103"/>
							</td>
							<th><div class="col_title_right">仕入先名カナ</div></th>
							<td><html:text styleId="supplierKana" property="supplierKana" style="width: 200px" tabindex="104" maxlength="60"/></td>
						</tr>
						<tr>
							<th><div class="col_title_right">備考</div></th>
							<td colspan="5"><html:text styleId="remarks" property="remarks" tabindex="105" style="width: 600px" maxlength="120"/></td>
						</tr>
					</table>
				</div>
	    	</div><!-- /.form_section -->
	    </div><!-- /.form_section_wrap -->

		<div style="width: 1160px; text-align :right;">
			<button type="button" tabindex="150" onclick="initForm()" class="btn_medium">初期化</button>
			<button type="button" tabindex="151" onclick="searchSupplier()" class="btn_medium">検索</button>
		</div>

		<div id="ListContainer">
			<div style="width: 1010px; height: 25px;">
					<div style="position:absolute; left: 0px;">検索結果件数： 0件</div>
                    <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
			</div>
			<table id="search_result" summary="仕入先検索結果"  class="forms detail_info" style="table-layout: auto; margin-top: 20px;">
				<colgroup>
					<col span="1" style="width: 15%">
					<col span="1" style="width: 20%">
					<col span="1" style="width: 20%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 20%">
					<col span="1" style="width: 10%">
				</colgroup>
				<tr>
					<th class="rd_top_left" style="cursor: pointer; height: 30px;">仕入先コード</th>
					<th class="xl64" style="cursor: pointer; height: 30px;">仕入先名</th>
					<th class="xl64" style="cursor: pointer; height: 30px;">担当者</th>
					<th class="xl64" style="cursor: pointer; height: 30px;">取引区分</th>
					<th class="xl64" style="cursor: pointer; height: 30px;">備考</th>
					<th class="xl64 rd_top_right" style="cursor: pointer; height: 30px;">&nbsp;</th>
				</tr>
			</table>
		</div>
	</div>

	<html:hidden styleId="sortColumn" property="sortColumn" />
	<html:hidden styleId="sortOrderAsc" property="sortOrderAsc" />
</s:form>
</div>
</body>

</html>
