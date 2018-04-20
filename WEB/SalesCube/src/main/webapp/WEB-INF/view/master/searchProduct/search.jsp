<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　商品マスタ管理（検索）</title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
    var paramDataTmp = null;

	$(
		function() {
			$("#productCode").focus();
		}
	);

	// F1
	function onF1(){
		initForm();
	}

	// F2
	function onF2(){
		searchProduct();
	}

	// F3
	function onF3(){
		addProduct();
	}

	// F4
	function onF4(){
		outputExcel();
	}

	// F5
	function onF5(){
		showImportExcelDialog();
	}

	// 初期化
	function initForm(){
		// 入力内容を初期化してよろしいですか？
		if(confirm('<bean:message key="confirm.init" />')){
			window.location.doHref('${f:url("/master/searchProduct")}');
		}
	}

	/**
	 * 検索処理実行
	 */
	function searchProduct(){

		return execSearch(createData(), true);
	}

	/**
	 * 商品削除
	 */
	function deleteProduct(productCode, updDatetm, discountUpdDatetm) {
		if(!confirm('<bean:message key="confirm.delete" />')){
			return;
		}

		// 検索実行
		asyncRequest(
			"${f:url("/ajax/master/deleteProductAjax/delete")}",
			{ "productCode": productCode, "updDatetm": updDatetm, "discountUpdDatetm": discountUpdDatetm },
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

	function execSearch(paramData, autoEdit) {
		if(!paramData["pageNo"]) {
			// ページの設定がなければ1ページ
			paramData["pageNo"] = 1;
		}

		// 検索実行
		asyncRequest(
			"${f:url("/ajax/master/searchProductAjax/search")}",
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

		// 商品コード
		var id = "#" + prev + "productCode";
		if($(id).val()) {
			data["productCode"] = $(id).val();
		}
		// 商品名
		id = "#" + prev + "productName";
		if($(id).val()) {
			data["productName"] = $(id).val();
		}
		// 商品名カナ
		id = "#" + prev + "productKana";
		if($(id).val()) {
			data["productKana"] = $(id).val();
		}
		// 仕入先コード
		id = "#" + prev + "supplierCode";
		if($(id).val()) {
			data["supplierCode"] = $(id).val();
		}
		// 仕入先名
		id = "#" + prev + "supplierName";
		if($(id).val()) {
			data["supplierName"] = $(id).val();
		}

		// 標準化分類
		id = "#" + prev + "productStandardCategory";
		if($(id).val()) {
			data["productStandardCategory"] = $(id).val();
		}
		// 分類状況
		id = "#" + prev + "productStatusCategory";
		if($(id).val()) {
			data["productStatusCategory"] = $(id).val();
		}
		// 保管分類
		id = "#" + prev + "productStockCategory";
		if($(id).val()) {
			data["productStockCategory"] = $(id).val();
		}
		// セット分類
		id = "#" + prev + "setTypeCategory";
		if($(id).val()) {
			data["setTypeCategory"] = $(id).val();
		}

		// 備考
		id = "#" + prev + "remarks";
		if($(id).val()) {
			data["remarks"] = $(id).val();
		}

		// 分類（大）
		id = "#" + prev + "product1";
		if($(id).val()) {
			data["product1"] = $(id).val();
		}
		// 分類（中）
		id = "#" + prev + "product2";
		if($(id).val()) {
			data["product2"] = $(id).val();
		}
		// 分類（小）
		id = "#" + prev + "product3";
		if($(id).val()) {
			data["product3"] = $(id).val();
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


	/**
	 * 追加
	 */
	function addProduct(){
		window.location.doHref("${f:url("/master/editProduct")}");
	}

	/**
	 * 編集
	 */
	function editProduct(productCode){
		$("#editForm").find("#productCode").val(productCode);
		$("#editForm").submit();
	}

	/**
	 * 仕入先検索画面を開く
	 */
	function showSearchSupplierDialog(element) {
		// ダイアログを開く
		openSearchSupplierDialog(
				element.attr("id") + "Dialog",
				function(id, map) {
					element.val( map[ element.attr("id") ] );
				}
		 );

		// ダイアログに初期値を設定する
		$("#" + element.attr("id") + "Dialog_" + element.attr("id")).val(element.val());
		$("#" + element.attr("id") + "Dialog_" + element.attr("id")).focus();
	}

	/**
	 * Excelファイルを出力する
	 */
	function outputExcel() {
		if(!confirm('<bean:message key="confirm.csv.condition" />')){
			return;
		}

		document.master_searchProductActionForm.action = "${f:url("/master/downloadProductExcel/download")}";
		document.master_searchProductActionForm.submit();
	}

	function ActionSubmit(FormName,ActionName){
		showNowSearchingDiv();
		$("form[name='" + FormName + "']").attr("action",ActionName);
		$("form[name='" + FormName + "']").submit();
	}

	/**
	 * Excelファイルを取込む
	 */
	function uploadExcel() {
		if(!confirm('<bean:message key="confirm.uptake" />')){
			return;
		}
		ActionSubmit("ImportProductExcelForm","${f:url('/master/importProductExcel/upload')}");
	}

	/**
	 * Excelファイル取込みダイアログを開く
	 */
	function showImportExcelDialog() {

		var dialogParam = {
			"draggable":	true,
			"resizable":	false,
			"bgiframe": true,
			"autoOpen": false,
			"modal": false,
			"buttons": {},
			"width": 600,
			"height": 120
		};

		var dialog = $("#ImportProductExcelDialog");
		dialog.dialog(dialogParam);
		dialog.dialog("open");
		if (jQuery.browser.msie) {
			dialog.dialog("option", "height", dialogParam.height);
			dialog.css("height", dialogParam.height);
			dialog.dialog("option", "position", "center");
			window.event.keyCode = null;
			window.event.returnValue = false;
		}
	}
	/**
	 * 商品検索ダイアログを開く
	 */
	function productSearch(jqObject) {
		var dialogId = jqObject.attr("id") + "Dialog";
		openSearchProductDialog(
			dialogId,
			function(id, map) {
				if(jqObject.attr("id").search(/Code$/) > 0) {
					jqObject.val( map[ "productCode" ] );
				}
				else if(jqObject.attr("id").search(/Name$/) > 0) {
					jqObject.val( map[ "productName" ] );
				}
			}
		);

		// ダイアログのフィールドに値をセットしてフォーカス
		if(jqObject.attr("id").search(/Code$/) > 0) {
			$("#" + dialogId + "_productCode").val( jqObject.val() );
			$("#" + dialogId + "_productCode").focus();
		}
		else if(jqObject.attr("id").search(/Name$/) > 0) {
			$("#" + dialogId + "_productName").val( jqObject.val() );
			$("#" + dialogId + "_productName").focus();
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
		<jsp:param name="PARENT_MENU_ID" value="0013"/>
		<jsp:param name="MENU_ID" value="1300"/>
	</jsp:include>

	<%-- メイン機能 --%>
	<div id="main_function">
		<span class="title">商品</span>

		<s:form onsubmit="return false;">

			<div class="function_buttons">
				<button type="button" tabindex="2000" onclick="initForm();">F1<br>初期化</button>
					<button type="button" tabindex="2001" onclick="searchProduct();">F2<br>検索</button>
					<button type="button" tabindex="2002"
						onclick="addProduct();" <c:if test="${!isUpdate}">disabled="disabled"</c:if> >F3<br>追加</button>
					<button type="button" tabindex="2003"
						onclick="outputExcel();" <c:if test="${!userDto.validUpDwnProducts}">disabled="disabled"</c:if> >F4<br><bean:message key='words.name.excel'/>出力</button>
					<button type="button" tabindex="2004"
						onclick="showImportExcelDialog();" <c:if test="${!userDto.validUpDwnProducts}">disabled="disabled"</c:if> >F5<br><bean:message key='words.name.excel'/>取込</button>
					<button type="button" disabled="disabled">F6<br>&nbsp;</button>
					<button type="button" disabled="disabled">F7<br>&nbsp;</button>
					<button type="button" disabled="disabled">F8<br>&nbsp;</button>
					<button type="button" disabled="disabled">F9<br>&nbsp;</button>
					<button type="button" disabled="disabled">F10<br>&nbsp;</button>
					<button type="button" disabled="disabled">F11<br>&nbsp;</button>
					<button type="button" disabled="disabled">F12<br>&nbsp;</button>
			</div>
			<br><br><br>

			<%-- 入力メイン --%>
			<div class="function_forms">

				<%-- エラー表示部分 --%>
				<div style="color:red; padding-left: 20px">
					<span id="ajax_errors">
						<html:errors/>
						<span  style="color:blue;">
							<html:messages id="msg" message="true">
								<bean:write name="msg" ignore="true"/>
							</html:messages>
						</span>
					</span>
				</div>


			    <div class="form_section_wrap">
			    <div class="form_section">
			    	<div class="section_title">
						<span>商品情報</span><br>
			            <button class="btn_toggle" />
					</div><!-- /.section_title -->

					<%-- 検索条件1 --%>
					<div id="search_info" class="section_body">
						<table id="product_searach_info_1" class="forms" summary="検索情報１">
							<tr>
								<th><div class="col_title_right">商品コード</div></th>
								<td><html:text styleId="productCode" property="productCode" style="width: 200px; ime-mode: disabled;" tabindex="100"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
									<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;"
										onclick="productSearch($('#productCode'));" tabindex="101"/>
								</td>
								<th><div class="col_title_right">商品名</div></th>
								<td><html:text styleId="productName" property="productName" style="width: 200px;" tabindex="102"/>
									<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;
										" onclick="productSearch($('#productName'));" tabindex="103"/>
								</td>
								<th><div class="col_title_right">商品名カナ</div></th>
								<td><html:text styleId="productKana" property="productKana" style="width: 200px;" tabindex="104"/></td>
							</tr>
							<tr>
								<th><div class="col_title_right">仕入先コード</div></th>
								<td><html:text styleId="supplierCode" property="supplierCode" style="width: 100px; ime-mode: disabled;" tabindex="105"/>
									<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" tabindex="106" onclick="showSearchSupplierDialog($('#supplierCode'));" /></td>
								<th><div class="col_title_right">仕入先名</div></th>
								<td><html:text styleId="supplierName" property="supplierName" style="width: 200px;" tabindex="107"/>
									<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" tabindex="108"  onclick="showSearchSupplierDialog($('#supplierName'));" /></td>
								<th><div class="col_title_right">標準化分類</div></th>
								<td>
								<html:select styleId="productStandardCategory" property="productStandardCategory" tabindex="109">
									<html:options collection="standardCategoryList" property="value" labelProperty="label" />
								</html:select>
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right">状況</div></th>
								<td>
								<html:select styleId="productStatusCategory" property="productStatusCategory" tabindex="110">
									<html:options collection="statusCategoryList" property="value" labelProperty="label" />
								</html:select>
								</td>
								<th><div class="col_title_right">保管</div></th>
								<td>
								<html:select styleId="productStockCategory" property="productStockCategory" tabindex="111">
									<html:options collection="stockCategorylist" property="value" labelProperty="label" />
								</html:select>
								</td>
								<th><div class="col_title_right">セット分類</div></th>
								<td>
								<html:select styleId="setTypeCategory" property="setTypeCategory" tabindex="112">
									<html:options collection="setCategoryList" property="value" labelProperty="label" />
								</html:select>
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right">備考</div></th>
								<td colspan="5"><html:text styleId="remarks" property="remarks" style="width: 500px;" tabindex="113"/></td>
							</tr>
						</table>

						<table class="forms" summary="検索条件2">
							<colgroup>
								<col span="1" style="width: 10%">
								<col span="1" style="width: 90%">
							</colgroup>
							<tr>
								<th><div class="col_title_right">分類（大）</div></th>
								<td>
									<select id="product1" name="product1" class="ProductClass1_TopEmpty" style="width: 500px;" tabindex="200">
										<c:forEach var="bean" items="${product1List}">
											<option value="${bean.value}">${f:h(bean.label)}</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right">分類（中）</div></th>
								<td>
									<select id="product2" name="product2" class="ProductClass2_TopEmpty" style="width: 500px;" tabindex="201">
										<option value=""></option>
									</select>
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right">分類（小）</div></th>
								<td>
									<select id="product3" name="product3" class="ProductClass3_TopEmpty" style="width: 500px;" tabindex="202">
										<option value=""></option>
									</select>
								</td>
							</tr>
						</table>
					</div>
		    	</div><!-- /.form_section -->
		    	</div><!-- /.form_section_wrap -->

				<div style="width: 1160px; text-align: right">
					<button type="button" tabindex="250" onclick="initForm();" class="btn_medium">初期化</button>
					<button type="button" tabindex="251" onclick="searchProduct();" class="btn_medium">検索</button>
				</div>

				<div id="ListContainer">
					<div style="width: 1010px; height: 25px;">
							<div style="position:absolute; left: 0px;">検索結果件数： 0件</div>
		                    <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
					</div>
					<span id="searchResultList">
						<table id="search_result" summary="searchResult" class="forms detail_info" style="table-layout: auto; margin-top: 20px;">
							<colgroup>
								<col span="1" style="width: 15%">
								<col span="1" style="width: 20%">
								<col span="1" style="width: 20%">
								<col span="1" style="width: 15%">
								<col span="1" style="width: 20%">
								<col span="1" style="width: 10%">
							</colgroup>
							<tr>
								<th class="rd_top_left" style="cursor: pointer; height: 30px;">商品コード</th>
								<th class="xl64" style="cursor: pointer; height: 30px;">商品名</th>
								<th class="xl64" style="cursor: pointer; height: 30px;">仕入先</th>
								<th class="xl64" style="cursor: pointer; height: 30px;">分類（大）</th>
								<th class="xl64" style="cursor: pointer; height: 30px;">備考</th>
								<th class="xl64 rd_top_right" style="cursor: pointer; height: 30px;">&nbsp;</th>
							</tr>
						</table>
					</span>
				</div>
			</div>

			<html:hidden styleId="sortColumn" property="sortColumn" />
			<html:hidden styleId="sortOrderAsc" property="sortOrderAsc" />

		</s:form>
	</div>

	<%-- Excel取込みダイアログ --%>
	<div id="ImportProductExcelDialog" title="商品マスタExcel取込" style="display: none;">
		<form name="ImportProductExcelForm" action="${f:url('/master/importProductExcel')}" method="post" enctype="multipart/form-data">
			<div style="padding: 20px 20px 0 20px;">
				<table style="width: 100%;">
					<tr>
						<td><input type="file" name="productExcelFile" style="width: 350px; color: #FFFFFF;"></td>
						<td>
							<button type="button" tabindex="500"
								onclick="uploadExcel();">取込</button>
							<input id="uploadSubmit" type="submit" name="upload" value="取込" style="display: none;">
							<button type="button" tabindex="501"
								onclick="$('#ImportProductExcelDialog').dialog('close');">閉じる</button>
						</td>
					</tr>
				</table>
			</div>
		</form>
	</div>

	<s:form styleId="editForm" action="/master/editProduct/edit" >
		<input type="hidden" id="productCode" name="productCode">
	</s:form>

</body>

</html>
