<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><bean:message key='titles.system'/>　部門情報</title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
    <!--
	var dataTmp = new Object();

    function onF1() {
        initForm();
    }
    function onF2() {
        searchDept();
    }
    function onF3() {
        addDept();
    }

	/**
	 * 初期化ボタン押下
	 */
	function initForm() {
		if(!confirm('<bean:message key="confirm.init" />')){
			return;
		}
		window.location.doHref("${f:url('/setting/searchDept')}");
    }

	/**
	 * 検索処理実行
	 */
	function searchDept(){

		return execSearch(createData(), true);
	}

	function deleteDept(deptId, updDatetm) {
		if(!confirm('<bean:message key="confirm.delete" />')){
			return;
		}

		// 削除実行
		asyncRequest(
			"${f:url('/ajax/setting/deleteDeptAjax/delete')}",
			{ "deptId": deptId, "updDatetm": updDatetm },
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
		if($("#sortColumn").val() == itemId) {
			// 同じ項目の場合は順序を反転する
			if($("#sortOrderAsc").val() == "true") {
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
		return execSearch(createData());
	}

	function execSearch(data, autoEdit) {
		if(!data["pageNo"]) {
			// ページの設定がなければ1ページ
			data["pageNo"] = 1;
		}

		// 検索実行
		asyncRequest(
			"${f:url('/ajax/setting/searchDeptResultAjax/search')}",
			data,
			function(data) {
				var jData = $(data);

				// 検索結果テーブルを更新する
				$("#ListContainer").empty();
				$("#ListContainer").append(data);
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

		// 部門ID
		var id = "#" + prev + "deptId";
		data["deptId"] = $(id).val();

		// 親部門ID
		id = "#" + prev + "parentId";
		data["parentId"] = $(id).val();

		// 部門名
		id = "#" + prev + "name";
		data["name"] = $(id).val();

		// 表示件数
		id = "#" + prev + "rowCount";
		data["rowCount"] = $(id).val();

		// ソート列
		id = "#" + prev + "sortColumn";
		data["sortColumn"] = $(id).val();

		// ソート昇順フラグ
		id = "#" + prev + "sortOrderAsc";
		data["sortOrderAsc"] = $(id).val();

		return data;
    }

	//追加
	function addDept(){
		window.location.doHref("${f:url('/setting/editDept')}");
	}

	// 編集
	function editDept(deptId){
		window.location.doHref("${f:url('/setting/editDept/edit/')}" + deptId);
	}

    -->
	</script>
	<script type="text/javascript" src="./scripts/common.js"></script>
</head>
<body onhelp="return false;">

<%-- ページヘッダ領域 --%>
<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

<%-- メニュー領域 --%>
<jsp:include page="/WEB-INF/view/common/menubar.jsp">
	<jsp:param name="PARENT_MENU_ID" value="0012"/>
	<jsp:param name="MENU_ID" value="1202"/>
</jsp:include>

<!-- メイン機能 -->
<div id="main_function">
	<span class="title">部門情報</span>

	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm();">F1<br>初期化</button>
		<button tabindex="2001" onclick="searchDept()">F2<br>検索</button>

		<c:if test="${!isUpdate}">
			<button disabled="disabled">F3<br>追加</button>
		</c:if>
		<c:if test="${isUpdate}">
			<button tabindex="2002" onclick="addDept();">F3<br>追加</button>
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

	<s:form onsubmit="return false;">
	<div class="function_forms">

		<div style="padding-left: 20px">
			<html:errors/>
			<span id="ajax_errors"></span>
		</div>

		<div class="form_section_wrap">
			<div class="form_section">
				<div class="section_title">
					<span>部門情報</span>
					<button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div class="section_body">
					<table id="dept_info" class="forms" summary="部門情報" style="width: 500px">
						<tr>
							<th><div class="col_title_right">部門コード</div></th>
							<td><html:text styleId="deptId" property="deptId" style="width: 120px;ime-mode:disabled;"  tabindex="100"/></td>
						</tr>
						<tr>
							<th><div class="col_title_right">親部門</div></th>
							<td>
								<html:select  tabindex="101"  styleId="parentId" property="parentId" style="width: 300px">
									<html:options collection="parentList" property="value" labelProperty="label" />
								</html:select>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right">部門名</div></th>
							<td><html:text styleId="name" property="name" style="width: 300px" tabindex="102"/></td>
						</tr>
					</table>
				</div><!-- /.section_body -->
	    	</div><!-- /.form_section -->
	   	</div><!-- /.form_section_wrap -->

		<div style="width: 1160px; text-align :right;">
			<button class="btn_medium"  tabindex="104" onclick="initForm()">初期化</button>
			<button class="btn_medium"  tabindex="105" onclick="searchDept()">検索</button>
		</div>


		<div id="ListContainer">
			<div style="width: 1010px; height: 25px;">
					<div style="position:absolute; left: 0px;">検索結果件数： 0件</div>
					<jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
			</div>
			<table id="search_result" summary="searchResult" class="forms detail_info" summary="部門検索結果" style="table-layout: auto; margin-top: 20px;">
				<colgroup>
					<col span="1" style="width: 15%">
					<col span="1" style="width: 35%">
					<col span="1" style="width: 35%">
					<col span="1" style="width: 15%">
				</colgroup>
				<tr>
					<th class="rd_top_left" style="height: 30px; cursor: pointer">部門コード</th>
					<th style="cursor: pointer">部門名</th>
					<th style="cursor: pointer">親部門</th>
					<th class="rd_top_right" style="cursor: pointer">&nbsp;</th>
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
