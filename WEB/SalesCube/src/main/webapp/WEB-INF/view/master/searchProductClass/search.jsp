<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　商品分類マスタ管理（検索）</title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
	var dataParamTmp = new Object();

    function init() {
        // 初期表示時に全件検索
        execSearch(createData());
    }

    function onF1() {
        initForm();
    }
    function onF2() {
        searchProductClass();
    }
    function onF3() {
        addProductClass();
    }

	/**
	 * 初期化ボタン押下
	 */
	function initForm() {
		// 入力内容を初期化してよろしいですか？
		if(confirm('<bean:message key="confirm.init" />')){
			window.location.doHref('${f:url("/master/searchProductClass")}');
		}
	}

    function searchProductClass() {

		return execSearch(createData());
    }

	function deleteProductClass(classCode1,classCode2,classCode3, updDatetm) {
		if(!confirm('<bean:message key="confirm.delete" />')){
			return;
		}

		// 削除実行
		asyncRequest(
			"${f:url("/ajax/master/deleteProductClassAjax/delete")}",
			{
              "classCode1": classCode1,
              "classCode2": classCode2,
              "classCode3": classCode3,
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
		if(dataParamTmp != null && $("#searchResultCount").val() != "0") {
			// 前回の検索条件からソート条件のみを変更
			var data = dataParamTmp;
			data["pageNo"] = 1;
			data["sortColumn"] = $("#sortColumn").val();
			data["sortOrderAsc"] = $("#sortOrderAsc").val();
			// 検索
			return execSearch(data);
		}
	}

    // 追加
    function addProductClass() {
		window.location.doHref("${f:url("/master/editProductClass")}");
    }

	// 編集
	function editProductClass(classCode1, classCode2, classCode3){
		window.location.doHref("${f:url("/master/editProductClass/edit/")}" + classCode1 + "-" + classCode2 + "-" + classCode3);
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

		// 分類（大）コード
		var id = "#" + prev + "classCode1";
		if($(id).val()) {
			data["classCode1"] = $(id).val();
		}
		// 分類（中）
		id = "#" + prev + "classCode2";
		if($(id).val()) {
			data["classCode2"] = $(id).val();
		}
		// 分類（小）
		id = "#" + prev + "classCode3";
		if($(id).val()) {
			data["classCode3"] = $(id).val();
		}
        // 分類コード
		id = "#" + prev + "classCode";
		if($(id).val()) {
			data["classCode"] = $(id).val();
		}
        // 分類名
		id = "#" + prev + "className";
		if($(id).val()) {
			data["className"] = $(id).val();
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

    function changeProductClass1() {
		var data = {
			"classCode1" : $("#classCode1").val(),
			"classCode3" : " " // 分類（小）のコードにはスペースを指定する
		}

		//asyncRequest(contextRoot	//同期にしないと中⇒小 反映中に 大⇒中が上書きされる問題が発生する。
		syncRequest(contextRoot
			+ "/ajax/productClassAjax/search?domainId" + domainId, data,
				function(data) {
					// 成功用関数
				var options = eval(data);

				// （中）を空にする
				$("#classCode2").empty();

				// （中）に要素をセットする
				opt = $(document.createElement("option"));
				opt.attr("value", "");
				$(opt).append(document.createTextNode(""));
				$("#classCode2").append(opt);

				for ( var i = 0; i < options.length; i++) {
					if (options[i].classCode2 == "") {
						continue;
					}

					opt = $(document.createElement("option"));
					opt.val(options[i].classCode2);
					$(opt).append(document.createTextNode(options[i].className));
					$("#classCode2").append(opt);
				}
				$("#classCode2").change();
			});
    }

    function changeProductClass2() {
		var data = {
			"classCode1" : $("#classCode1").val(),
			"classCode2" : $("#classCode2").val()
		}

		syncRequest(contextRoot
				+ "/ajax/productClassAjax/search?domainId" + domainId, data,
				function(data) {
					// 成功用関数
				var options = eval(data);

				// （小）を空にする
				$("#classCode3").empty();

				// （小）に要素をセットする
				opt = $(document.createElement("option"));
				opt.attr("value", "");
				$(opt).append(document.createTextNode(""));
				$("#classCode3").append(opt);

				for ( var i = 0; i < options.length; i++) {
					if (options[i].classCode3 == "") {
						continue;
					}

					opt = $(document.createElement("option"));
					opt.val(options[i].classCode3);
					$(opt).append(document.createTextNode(options[i].className));
					$("#classCode3").append(opt);
				}
			});
    }

	function execSearch(dataParam) {
		if(!dataParam["pageNo"]) {
			// ページの設定がなければ1ページ
			dataParam["pageNo"] = 1;
		}

		// 検索実行
		asyncRequest(
			"${f:url("/ajax/master/searchProductClassAjax/search")}",
			dataParam,
			function(data) {
				var jData = $(data);

				// 検索結果テーブルを更新する
				$("#ListContainer").empty();
				$("#ListContainer").append(data);

				// 1件以上ヒットした場合
				if($("#searchResultCount").val() != "0") {
					dataParamTmp = dataParam;
				}
				else {
					dataParamTmp = null;
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
	<jsp:param name="MENU_ID" value="1311"/>
</jsp:include>

<!-- メイン機能 -->
<div id="main_function">
<s:form onsubmit="return false;">
	<span class="title">分類</span>

	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm()">F1<br>初期化</button>
        <button tabindex="2001" onclick="searchProductClass()">F2<br>検索</button>
<c:if test="${!isUpdate}">
        <button disabled="disabled">F3<br>追加</button>
</c:if>
<c:if test="${isUpdate}">
        <button tabindex="2002" onclick="addProductClass()">F3<br>追加</button>
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
						<span>検索条件</span>
						<button class="btn_toggle" />
					</div><!-- /.section_title -->
				<div class="section_body">

					<table id="user_info" class="forms" summary="検索条件" style="width: 600px">
						<colgroup>
							<col span="1" style="width: 10%">
							<col span="1" style="width: 15%">
							<col span="1" style="width: 10%">
							<col span="1" style="width: 65%">
						</colgroup>
						<tr>
							<th><div class="col_title_right">分類（大）</div></th>
							<td colspan="3">
							<html:select styleId="classCode1" property="classCode1" style="width:500px" onchange="changeProductClass1()" tabindex="100">
								<html:options collection="classCode1List" property="value" labelProperty="label"/>
							</html:select>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right">分類（中）</div></th>
							<td colspan="3">
							<html:select styleId="classCode2" property="classCode2" style="width:500px" onchange="changeProductClass2()" tabindex="101">
								<html:options collection="classCode2List" property="value" labelProperty="label"/>
							</html:select>
			                </td>
						</tr>
						<tr>
							<th><div class="col_title_right">分類（小）</div></th>
							<td colspan="3">
							<html:select styleId="classCode3" property="classCode3" style="width:500px" tabindex="102">
								<html:options collection="classCode3List" property="value" labelProperty="label"/>
							</html:select>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right">分類コード</div></th>
							<td><html:text styleId="classCode" property="classCode" style="width: 100px" tabindex="103"/>
							</td>
							<th><div class="col_title_right">分類名</div></th>
							<td><html:text styleId="className" property="className" style="width: 250px" tabindex="104"/></td>
						</tr>
					</table>

				</div><!-- /.section_body -->
    		</div><!-- /.form_section -->
   		</div><!-- /.form_section_wrap -->

		<div style="text-align: right; width: 1160px">
			<button class="btn_medium" tabindex="150" onclick="initForm()">初期化</button>
			<button class="btn_medium" tabindex="151" onclick="searchProductClass()">検索</button>
		</div>

		<div id="ListContainer">
			<div style="width: 1010px; height: 25px;">
					<div style="position:absolute; left: 0px;">検索結果件数： 0件</div>
                    <jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
			</div>

		<div id="searchResultList">
    		<table id="search_result" summary="検索結果" class="forms detail_info" style="table-layout: auto; margin-top: 20px;">
    			<colgroup>
    				<col span="1" style="width: 50px">
    				<col span="1" style="width:175px">

    				<col span="1" style="width: 50px">
    				<col span="1" style="width:175px">

    				<col span="1" style="width: 50px">
    				<col span="1" style="width:175px">

    				<col span="1" style="width:125px">
    			</colgroup>
    			<tr>
    				<th class="rd_top_left" style="height: 30px;" colspan="2">大分類</th>
    				<th colspan="2">中分類</th>
    				<th colspan="2">小分類</th>
    				<th class="rd_top_right"  rowspan="2"></th>
    			</tr>

    			<tr>
    				<th style="height: 30px;">コード</th><th>名称</th>
    				<th>コード</th><th>名称</th>
    				<th>コード</th><th>名称</th>
    			</tr>
    		</table>
    	</div>
    	</div>
    </div>

   	<html:hidden styleId="sortColumn" property="sortColumn" />
	<html:hidden styleId="sortOrderAsc" property="sortOrderAsc" />
</s:form>
</div>
</body>

</html>
