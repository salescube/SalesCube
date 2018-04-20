<%@page import="jp.co.arkinfosys.common.Constants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　顧客マスタ管理（検索）</title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
    var paramDataTmp = null;

    function onF1() {
        initForm();
    }
    function onF2() {
        searchCustomer();
    }
    function onF3() {
        addCustomer();
    }

	$(
	function() {
		$("#customerCode").focus();
		$("#customerCode").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁
	}
	);

	/**
	 * 初期化ボタン押下
	 */
	function initForm() {
		// 入力内容を初期化してよろしいですか？
		if(confirm('<bean:message key="confirm.init" />')){
			window.location.doHref('${f:url("/master/searchCustomer")}');
		}
	}

	/**
	 * 検索処理実行
	 */
	function searchCustomer(){

		return execSearch(createData());
	}

	function deleteCustomer(customerCode, updDatetm) {
		if(!confirm('<bean:message key="confirm.delete" />')){
			return;
		}

		// 削除実行
		asyncRequest(
			"${f:url("/ajax/master/deleteCustomerAjax/delete")}",
			{ "customerCode": customerCode, "updDatetm": updDatetm },
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
			"${f:url("/ajax/master/searchCustomerAjax/search")}",
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

		// 顧客コード
		var id = "#" + prev + "customerCode";
		if($(id).val()) {
			data["customerCode"] = $(id).val();
		}
		// 顧客名
		id = "#" + prev + "customerName";
		if($(id).val()) {
			data["customerName"] = $(id).val();
		}
		// 顧客名カナ
		id = "#" + prev + "customerKana";
		if($(id).val()) {
			data["customerKana"] = $(id).val();
		}
		// 事業所名
		id = "#" + prev + "customerOfficeName";
		if($(id).val()) {
			data["customerOfficeName"] = $(id).val();
		}
		// 事業所名カナ
		id = "#" + prev + "customerOfficeKana";
		if($(id).val()) {
			data["customerOfficeKana"] = $(id).val();
		}
		// 担当者
		id = "#" + prev + "customerPcName";
		if($(id).val()) {
			data["customerPcName"] = $(id).val();
		}
		// 担当者カナ
		id = "#" + prev + "customerPcKana";
		if($(id).val()) {
			data["customerPcKana"] = $(id).val();
		}
		// TEL
		id = "#" + prev + "customerTel";
		if($(id).val()) {
			data["customerTel"] = $(id).val();
		}
		// FAX
		id = "#" + prev + "customerFax";
		if($(id).val()) {
			data["customerFax"] = $(id).val();
		}
		// 顧客ランク
		id = "#" + prev + "customerRankCategory";
		if(prev) {
			if($(id).val()) {
				data["customerRankCategory"] = $(id).val();
			}
		}
		else {
			var customerRankCategory = $(id).get(0);
			if(customerRankCategory.selectedIndex > 0) {
				data["customerRankCategory"] = customerRankCategory.options[ customerRankCategory.selectedIndex ].value;
			}
		}

		// 支払条件
		id = "#" + prev + "cutoffGroup";
		if(prev) {
			if($(id).val()) {
				data["cutoffGroup"] = $(id).val();
			}
		}
		else {
			var cutoffGroup = $(id).get(0);
			if(cutoffGroup.selectedIndex > 0) {
				data["cutoffGroup"] = cutoffGroup.options[ cutoffGroup.selectedIndex ].value;
			}
		}

        // 振込名義
		id = "#" + prev + "paymentName";
		if($(id).val()) {
			data["paymentName"] = $(id).val();
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
	function addCustomer(){
		window.location.doHref("${f:url("/master/editCustomer")}");
	}

	// 編集
	function editCustomer(customerCode){
		$("#editForm").find("#customerCode").val(customerCode);
		$("#editForm").submit();
	}

	/**
	 * 顧客検索ダイアログを開く
	 */
	function customerSearch(jqObject) {
		var dialogId = jqObject.attr("id") + "Dialog";
		openSearchCustomerDialog(
			dialogId,
			function(id, map) {
				if(jqObject.attr("id").search(/Code$/) > 0) {
					jqObject.val( map[ "customerCode" ] );
				}
				else if(jqObject.attr("id").search(/Name$/) > 0) {
					jqObject.val( map[ "customerName" ] );
				}
			}
		);

		// ダイアログのフィールドに値をセットしてフォーカス
		if(jqObject.attr("id").search(/Code$/) > 0) {
			$("#" + dialogId + "_customerCode").val( jqObject.val() );
			$("#" + dialogId + "_customerCode").focus();
		}
		else if(jqObject.attr("id").search(/Name$/) > 0) {
			$("#" + dialogId + "_customerName").val( jqObject.val() );
			$("#" + dialogId + "_customerName").focus();
		}
	}
	-->
	</script>
</head>
<body onhelp="return false;">

<%-- ページヘッダ領域 --%>
<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

<%-- メニュー領域 --%>
<jsp:include page="/WEB-INF/view/common/menubar.jsp">
	<jsp:param name="PARENT_MENU_ID" value="0013"/>
	<jsp:param name="MENU_ID" value="1302"/>
</jsp:include>

<!-- メイン機能 -->

<div id="main_function">
<s:form onsubmit="return false;">

	<span class="title">顧客検索</span>

	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm()"> F1<br>初期化</button>
		<button tabindex="2001" onclick="searchCustomer()">F2<br>検索</button>
		<c:if test="${!isUpdate}">
			<button tabindex="2002" disabled="disabled">F3<br>追加</button>
		</c:if>
		<c:if test="${isUpdate}">
			<button tabindex="2002" onclick="addCustomer()">F3<br>追加</button>
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
					<span>顧客情報</span><br>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="search_info" class="section_body">
					<table id="user_info" class="forms" summary="顧客情報">

						<!--
						<colgroup>
							<col span="1" style="width: 13%">
							<col span="1" style="width: 20%">
							<col span="1" style="width: 13%">
							<col span="1" style="width: 20%">
							<col span="1" style="width: 13%">
							<col span="1" style="width: 20%">
						</colgroup>
						 -->
						<tr>
							<th><div class="col_title_right">顧客コード</div></th>
							<td>
								<html:text property="customerCode" styleId="customerCode" style="width: 180px;ime-mode:disabled;" tabindex="100"/>
								<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;"
										onclick="customerSearch($('#customerCode'));" tabindex="101"/>
							</td>
							<th><div class="col_title_right">顧客名</div></th>
							<td>
								<html:text property="customerName" styleId="customerName" style="width: 180px" tabindex="102" maxlength="60"/>
								<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;"
										onclick="customerSearch($('#customerName'));" tabindex="103"/>
							</td>
							<th><div class="col_title_right">顧客名カナ</div></th>
							<td><html:text property="customerKana"  styleId="customerKana" style="width: 180px" tabindex="104" maxlength="60"/></td>
						</tr>
						<tr>
							<th><div class="col_title_right">事業所名</div></th>
							<td><html:text property="customerOfficeName" styleId="customerOfficeName" style="width: 180px;" tabindex="105"/></td>
							<th><div class="col_title_right">事業所名カナ</div></th>
							<td><html:text property="customerOfficeKana" styleId="customerOfficeKana" style="width: 180px;" tabindex="106"/></td>
							<th><div class="col_title_right">TEL</div></th>
							<td><html:text property="customerTel" styleId="customerTel" style="width: 180px;ime-mode:disabled;" tabindex="107"/></td>
						</tr>
						<tr>
							<th><div class="col_title_right">担当者</div></th>
							<td><html:text property="customerPcName" styleId="customerPcName" style="width: 180px;" tabindex="108"/></td>
							<th><div class="col_title_right">担当者カナ</div></th>
							<td><html:text property="customerPcKana" styleId="customerPcKana" style="width: 180px;" tabindex="109"/></td>
							<th><div class="col_title_right">FAX</div></th>
							<td><html:text property="customerFax" styleId="customerFax" style="width: 180px;ime-mode:disabled;" tabindex="110"/></td>
						</tr>
						<tr>
							<th><div class="col_title_right">顧客ランク</div></th>
							<td>
								<html:select styleId="customerRankCategory" property="customerRankCategory" tabindex="111">
									<html:options collection="customerRankList" property="value" labelProperty="label"/>
								</html:select>
							</td>
							<th><div class="col_title_right">支払条件</div></th>
							<td colspan="3">
								<html:select styleId="cutoffGroup" property="cutoffGroup" tabindex="112">
									<html:options collection="cutoffGroupList" property="value" labelProperty="label"/>
								</html:select>
							</td>
						</tr>
					</table>

					<table class="forms" style="width: 910px" summary="自社情報3">
						<!--
						<colgroup>
							<col span="1" style="width: 10%">
							<col span="1" style="width: 40%">
							<col span="1" style="width: 10%">
							<col span="1" style="width: 40%">
						</colgroup>
						 -->
						<tr>
							<th><div class="col_title_right">振込名義</div></th>
							<td><html:text styleId="paymentName" property="paymentName" tabindex="200" style="width: 250px" maxlength="60"/></td>
							<th><div class="col_title_right">備考</div></th>
							<td><html:text styleId="remarks" property="remarks" tabindex="201" style="width: 250px" maxlength="120"/></td>
						</tr>
					</table>
				</div>
	    	</div><!-- /.form_section -->
	    </div><!-- /.form_section_wrap -->

		<div style="text-align: right; width: 1160px">
			<button type="button" tabindex="202" onclick="initForm();" class="btn_medium">初期化</button>
			<button type="button" tabindex="203" onclick="searchCustomer();" class="btn_medium">検索</button>
		</div>

		<div id="ListContainer">
			<div style="width: 1010px; height: 25px;">
				<div style="position:absolute; left: 0px;">検索結果件数： 0件</div>
				<jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
			</div>

			<span id="searchResultList">
				<table id="search_result" summary="searchResult" class="forms detail_info" style="table-layout: auto; margin-top: 20px;">
					<colgroup>
						<col span="1" style="width: 9%">
						<col span="1" style="width: 20%">
						<col span="1" style="width: 10%">
						<col span="1" style="width: 10%">
						<col span="1" style="width: 8%">
						<col span="1" style="width: 13%">
						<col span="1" style="width: 10%">
						<col span="1" style="width: 10%">
						<col span="1" style="width: 10%">
					</colgroup>
					<tr>
						<th class="rd_top_left" style="cursor: pointer; height: 30px;">顧客コード</th>
						<th class="xl64" style="cursor: pointer; height: 30px;">顧客名</th>
						<th class="xl64" style="cursor: pointer; height: 30px;">TEL</th>
						<th class="xl64" style="cursor: pointer; height: 30px;">担当者</th>
						<th class="xl64" style="cursor: pointer; height: 30px;">取引区分</th>
						<th class="xl64" style="cursor: pointer; height: 30px;">支払条件</th>
						<th class="xl64" style="cursor: pointer; height: 30px;">事業所名</th>
						<th class="xl64" style="cursor: pointer; height: 30px;">部署名</th>
						<th class="xl64 rd_top_right" style="cursor: pointer; height: 30px;">&nbsp;</th>
					</tr>
				</table>
			</span>
		</div>
	</div>
<html:hidden styleId="sortColumn" property="sortColumn" />
<html:hidden styleId="sortOrderAsc" property="sortOrderAsc" />
</s:form>

<s:form styleId="editForm" action="/master/editCustomer/edit" >
	<input type="hidden" id="customerCode" name="customerCode">
</s:form>

</div>
</body>
</html>
