<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　倉庫マスタ管理（検索）</title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

    <script type="text/javascript">
    <!--
    var paramData = null;
    var paramDataTmp = null;

    $(
   		function() {
   			$("#warehouseCode").focus();
   		}
   	);

    function onF1() {
        initForm();
    }
    function onF2() {
        searchWarehouse();
    }
    function onF3() {
        addWarehouse();
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
			window.location.doHref('${f:url("/master/searchWarehouse")}');
		}
    }

	/**
	 * 検索処理実行
	 */
	function searchWarehouse(){

		return execSearch(createData());
	}

	function deleteWarehouse(warehouseCode, updDatetm) {
		if(confirm('<bean:message key="confirm.delete" />')){
			if(confirm("<bean:message key='confirm.master.warehouse.delete.rack'/>")){
				// 削除実行
				asyncRequest(
					"${f:url("/ajax/master/deleteWarehouseAjax/deleteRack")}",
					{ "warehouseCode": warehouseCode, "updDatetm": updDatetm },
					function() {
						var data = createData(true);
						return execSearch(data);
					}
				);
			} else {
				// 削除実行
				asyncRequest(
					"${f:url("/ajax/master/deleteWarehouseAjax/updateRack")}",
					{ "warehouseCode": warehouseCode, "updDatetm": updDatetm },
					function() {
						var data = createData(true);
						return execSearch(data);
					}
				);
			}
		}
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

	function execSearch(paramData) {
		if(!paramData["pageNo"]) {
			// ページの設定がなければ1ページ
			paramData["pageNo"] = 1;
		}

		// 検索実行
		asyncRequest(
			"${f:url("/ajax/master/searchWarehouseAjax/search")}",
			paramData,
			function(data) {
				var jData = $(data);

				// 検索結果テーブルを更新する
				$("#ListContainer").empty();
				$("#ListContainer").append(data);

				// 1件以上ヒットした場合
				if($("#searchResultCount").val() != "0") {
					// EXCELボタンの状態変更
					$("#btnF4").attr("disabled","");

					// 検索条件を保持
					paramDataTmp = paramData;
				} else {
					// EXCELボタンの状態変更
					$("#btnF4").attr("disabled","disabled");
				}
			}
		);
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

		// 倉庫コード
		var id = "#" + prev + "warehouseCode";
		if($(id).val()) {
			paramData["warehouseCode"] = $(id).val();
		}
		// 倉庫名
		id = "#" + prev + "warehouseName";
		if($(id).val()) {
			paramData["warehouseName"] = $(id).val();
		}

		// 倉庫状態
		id = "#" + prev + "warehouseState";
		if($(id).val()) {
			paramData["warehouseState"] = $(id).val();
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

	//追加
	function addWarehouse(){
		window.location.doHref("${f:url("/master/editWarehouse")}");
	}

	// 編集
	function editWarehouse(warehouseCode){
		window.location.doHref("${f:url("/master/editWarehouse/edit/")}" + warehouseCode);
	}

	/**
	 * 倉庫検索ダイアログを開く
	 */
	function warehouseSearch(jqObject) {
		var dialogId = jqObject.attr("id") + "Dialog";
		openSearchWarehouseDialog(
			dialogId,
			function(id, map) {
				if(jqObject.attr("id").search(/Code$/) > 0) {
					jqObject.val( map[ "warehouseCode" ] );
				}
				else if(jqObject.attr("id").search(/Name$/) > 0) {
					jqObject.val( map[ "warehouseName" ] );
				}
			}
		);

		// ダイアログのフィールドに値をセットしてフォーカス
		if(jqObject.attr("id").search(/Code$/) > 0) {
			$("#" + dialogId + "_warehouseCode").val( jqObject.val() );
			$("#" + dialogId + "_warehouseCode").focus();
		}
		else if(jqObject.attr("id").search(/Name$/) > 0) {
			$("#" + dialogId + "_warehouseName").val( jqObject.val() );
			$("#" + dialogId + "_warehouseName").focus();
		}
	}

	// Excel出力
	function outputExcel(){
		// この内容でエクセル出力しますか？
		// 検索結果をEXCELファイルでダウンロードしますか？
    	if(!confirm('<bean:message key="confirm.excel.result" />')){
    		return;
    	}

		var form = $(window.document.forms["master_searchWarehouseActionForm"]);
		var orgAction = form.attr("action");
		var orgTarget = form.attr("target");
		form.attr("action", "${f:url('/master/searchWarehouseResultOutput/excel')}");
		form.attr("target", "_blank");

		form.submit();

		form.attr("action", orgAction);
		form.attr("target", orgTarget);
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
	<jsp:param name="MENU_ID" value="1307"/>
</jsp:include>


<!-- メイン機能 -->
<div id="main_function">
<s:form onsubmit="return false;">

    <span class="title">倉庫</span>

    <div class="function_buttons">
		<button tabindex="2000" onclick="initForm()"> F1<br>初期化</button>
		<button tabindex="2001" onclick="searchWarehouse()">F2<br>検索</button>
<c:if test="${!isUpdate}">
		<button tabindex="2002" disabled="true">F3<br>追加</button>
</c:if>
<c:if test="${isUpdate}">
		<button tabindex="2002" onclick="addWarehouse()">F3<br>追加</button>
</c:if>
        <button id="btnF4"  tabindex="2003" onclick="outputExcel()" disabled="disabled">F4<br><bean:message key='words.name.excel'/></button>
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
        			<span>倉庫情報</span>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="search_info" class="section_body">
			        <table id="user_info" class="forms" summary="倉庫情報1">
			            <tr>
			                <th><div class="col_title_right">倉庫コード</div></th>
			                <td>
			                	<html:text maxlength="10" styleId="warehouseCode" property="warehouseCode" style="width: 100px; ime-mode: disabled;" tabindex="100"/>
			                	<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;"
										onclick="warehouseSearch($('#warehouseCode'));" tabindex="101"/>
			                </td>
			                <th><div class="col_title_right">倉庫名</div></th>
			                <td>
			                	<html:text maxlength="60" styleId="warehouseName" property="warehouseName" style="width: 200px" tabindex="102"/>
			                	<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;"
										onclick="warehouseSearch($('#warehouseName'));" tabindex="103"/>
			                </td>
			                <th><div class="col_title_right">倉庫状況</div></th>
			                <td>
			                    <html:select styleId="warehouseState" property="warehouseState" style="width: 100px; ime-mode: disabled;"  tabindex="104">
									<html:option value="" />
									<html:option value="運用中" />
									<html:option value="棚卸中"/>
									<html:option value="廃止" />
			                    </html:select>
			                </td>
			            </tr>
			        </table>
				</div>
	    	</div><!-- /.form_section -->
	    </div><!-- /.form_section_wrap -->

        <div style="text-align: right; width: 1160px">
			<button type="button" tabindex="150" onclick="initForm();" class="btn_medium">初期化</button>
			<button type="button" tabindex="151" onclick="searchWarehouse();" class="btn_medium">検索</button>
        </div>

		<div id="ListContainer">
			<div style="width: 1010px; height: 25px;">
					<div style="position:absolute; left: 0px;">検索結果件数： 0件</div>
                    <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
			</div>
            <table id="search_result" summary="searchResult" class="forms detail_info" style="table-layout: auto; margin-top: 20px;">
                <colgroup>
					<col span="1" style="width: 10%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 10%">
					<col span="1" style="width: 45%">
                </colgroup>
                <tr>
					<th class="rd_top_left" style="cursor: pointer; height: 30px;">倉庫コード</th>
					<th class="xl64" style="cursor: pointer; height: 30px;">倉庫名</th>
					<th class="xl64" style="height: 30px;">倉庫状況</th>
			        <th class="xl64 rd_top_right" style="height: 30px;">&nbsp;</th>
			     </tr>
            </table>
        </div>
		<html:hidden styleId="sortColumn" property="sortColumn" />
		<html:hidden styleId="sortOrderAsc" property="sortOrderAsc" />
		<html:hidden styleId="warehouseState" property="warehouseState" />
    </div>
</s:form>
</div>

</body>

</html>
