<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　ファイル登録</title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
	var dataTmp = new Object();


	//初期化
	function onF1(){
		initForm();
	}

	//検索
	function onF2(){
		searchFileUpload();
	}

	//登録・更新
	function onF3(){
		addFileUpload();
	}

	/**
	 * 初期化ボタン押下
	 */
	function initForm() {
		if(!confirm('<bean:message key="confirm.init" />')){
			return;
		}
		window.location.doHref("${f:url('/setting/searchFileUpload')}");
	}

	/**
	 * 検索処理実行
	 */
	function searchFileUpload(){

		return execSearch(createData(), true);
	}

	function deleteFileUpload(fileId, updDatetm) {
		if(!confirm('<bean:message key="confirm.delete" />')){
			return;
		}

		// 検索実行
		asyncRequest(
			"${f:url('/ajax/setting/deleteFileAjax/delete')}",
			{ "fileId": fileId, "updDatetm": updDatetm },
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
		return execSearch(createData());
	}

	function execSearch(data, autoEdit) {
		if(!data["pageNo"]) {
			// ページの設定がなければ1ページ
			data["pageNo"] = 1;
		}

		// 検索実行
		asyncRequest(
			"${f:url('/ajax/setting/searchFileResultAjax/search')}",
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

		//タイトル　ファイル名　公開設定

		// タイトル
		var id = "#" + prev + "title";
		if($(id).attr("value")) {
			data["title"] = $(id).attr("value");
		}
		// ファイル名
		id = "#" + prev + "fileName";
		if($(id).attr("value")) {
			data["fileName"] = $(id).attr("value");
		}

		//公開設定
		id = "#" + prev + "openLevel";
		if($(id).attr("value")) {
			data["openLevel"] = $(id).attr("value");
		}

		// 表示件数
		id = "#" + prev + "rowCount";
		data["rowCount"] = $(id).val();

		// ソート列
		id = "#" + prev + "sortColumn";
		data["sortColumn"] = $(id).attr("value");

		// ソート昇順フラグ
		id = "#" + prev + "sortOrderAsc";
		data["sortOrderAsc"] = $(id).attr("value");

		return data;
	}

	//追加
	function addFileUpload(){
		window.location.doHref("${f:url("/setting/editFileUpload")}");
	}

	// 編集
	function editFileUpload(fileId){

		window.location.doHref("${f:url("/setting/editFileUpload/edit/")}" + fileId);
	}
	-->
	</script>
</head>

<body>
	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0012"/>
		<jsp:param name="MENU_ID" value="1205"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">
		<!-- タイトル -->
		<span class="title">ファイル登録</span>

		<div class="function_buttons">
			<button tabindex="2000" onclick="initForm();">F1<br>初期化</button>
			<button tabindex="2001" onclick="searchFileUpload();">F2<br>検索</button>
			<button tabindex="2002" onclick="addFileUpload();" <c:if test="${!isUpdate}">disabled</c:if> >F3<br>追加</button>
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
						<span>ファイル登録情報</span>
						<button class="btn_toggle">
	           				<img alt="表示／非表示" src="${f:url('/images/customize/btn_toggle.png')}" width="28" height="29" class="tbtn">
	      				</button>
						<br>
					</div><!-- /.section_title -->
				<div class="section_body">
					<div id="search_info">
						<table id="order_info" class="forms" style="width: 600px" summary="ファイル登録情報">
							<tr>
								<th><div class="col_title_right">タイトル</div></th>
								<td colspan="3"><html:text styleId="title" property="title"  tabindex="101" style="width: 150px"/></td>
							</tr>
							<tr>
								<th><div class="col_title_right">ファイル名</div></th>
								<td colspan="3"><html:text styleId="fileName" property="fileName"  tabindex="102" style="width: 150px"/></td>
							</tr>
							<tr>
								<th><div class="col_title_right">公開設定</div></th>
								<td>
									<html:select  tabindex="101"  styleId="openLevel" property="openLevel" style="width: 300px">
										<html:options collection="openLevelList" property="value" labelProperty="label" />
									</html:select>
								</td>
							</tr>
						</table>
					</div>
				</div><!-- /.section_body -->
    		</div><!-- /.form_section -->
   		</div><!-- /.form_section_wrap -->

		<div style="text-align: right; width: 1160px">
			<button class="btn_medium"  type="button" tabindex="350" style=""  onclick="initForm();">初期化</button>
			<button class="btn_medium"  type="button" tabindex="351" style="" onclick="searchFileUpload();">検索</button>
		</div>


   		<div id="ListContainer">
			<div style="width: 1010px; height: 25px;">
					<div style="position:absolute; left: 0px;">検索結果件数： 0件</div>
					<jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
			</div>
			<table id="search_result" summary="searchResult" class="forms detail_info" summary="登録ファイル検索結果" style="table-layout: auto; margin-top: 20px;">
				<colgroup>
					<col span="1" style="width: 5%">
					<col span="1" style="width: 30%">
					<col span="1" style="width: 24%">
					<col span="1" style="width: 16%">
					<col span="1" style="width: 10%">
					<col span="1" style="width: 10%">
					<col span="1" style="width: 5%">
				</colgroup>
				<tr>
					<th class="rd_top_left" style="height: 30px; cursor: pointer">No</th>
					<th style="cursor: pointer">タイトル</th>
					<th style="cursor: pointer">ファイル名</th>
					<th style="cursor: pointer">公開設定</th>
					<th style="cursor: pointer">登録日</th>
					<th style="cursor: pointer">登録者</th>
					<th class="rd_top_right" >&nbsp;</th>
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
