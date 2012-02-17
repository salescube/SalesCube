<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　社員情報</title>
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
		searchUser();
	}

	//登録・更新
	function onF3(){
		addUser();
	}

	/**
	 * 初期化ボタン押下
	 */
	function initForm() {
		if(!confirm('<bean:message key="confirm.init" />')){
			return;
		}
		window.location.doHref("${f:url('/setting/searchUser')}");
	}

	/**
	 * 検索処理実行
	 */
	function searchUser(){
		if(!confirm('<bean:message key="confirm.search" />')){
			return;
		}
		return execSearch(createData(), true);
	}

	function deleteUser(userId, updDatetm) {
		if(!confirm('<bean:message key="confirm.delete" />')){
			return;
		}

		// 検索実行
		asyncRequest(
			"${f:url('/ajax/setting/deleteUserAjax/delete')}",
			{ "userId": userId, "updDatetm": updDatetm },
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
			"${f:url('/ajax/setting/searchUserResultAjax/search')}",
			data,
			function(data) {
				// 検索結果件数が1件であれば編集画面に遷移する
				var jData = $(data);
				if(autoEdit && jData.is("#singleUserId")) {
					var userId = jData.filter("#singleUserId");
					window.location.doHref("${f:url('/setting/editUser/edit/')}" + userId.attr("value"));
					return;
				}

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

		// 社員コード
		var id = "#" + prev + "userId";
		if($(id).attr("value")) {
			data["userId"] = $(id).attr("value");
		}
		// 社員名
		id = "#" + prev + "nameKnj";
		if($(id).attr("value")) {
			data["nameKnj"] = $(id).attr("value");
		}
		// 社員名カナ
		id = "#" + prev + "nameKana";
		if($(id).attr("value")) {
			data["nameKana"] = $(id).attr("value");
		}

		// 部門
		id = "#" + prev + "deptId";
		if(prev) {
			if($(id).attr("value")) {
				data["deptId"] = $(id).attr("value");
			}
		}
		else {
			var deptId = $(id).get(0);
			if(deptId.selectedIndex > 0) {
				data["deptId"] = deptId.options[ deptId.selectedIndex ].value;
			}
		}

		// E-MAIL
		id = "#" + prev + "email";
		if($(id).attr("value")) {
			data["email"] = $(id).attr("value");
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
		data["sortColumn"] = $(id).attr("value");

		// ソート昇順フラグ
		id = "#" + prev + "sortOrderAsc";
		data["sortOrderAsc"] = $(id).attr("value");

		return data;
	}

	//追加
	function addUser(){
		window.location.doHref("${f:url("/setting/editUser")}");
	}

	// 編集
	function editUser(userId){
		window.location.doHref("${f:url("/setting/editUser/edit/")}" + userId);
	}
	-->
	</script>
</head>

<body>
	
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0012"/>
		<jsp:param name="MENU_ID" value="1203"/>
	</jsp:include>

	
	<div id="main_function">
		<!-- タイトル -->
		<span class="title">社員情報</span>

		<div class="function_buttons">
			<button tabindex="2000" onclick="initForm();">F1<br>初期化</button><button
				tabindex="2001" onclick="searchUser();">F2<br>検索</button><button
				tabindex="2002" onclick="addUser();" <c:if test="${!isUpdate}">disabled</c:if> >F3<br>追加</button><button
				disabled="disabled">F4<br>&nbsp;</button><button
				disabled="disabled">F5<br>&nbsp;</button><button
				disabled="disabled">F6<br>&nbsp;</button><button
				disabled="disabled">F7<br>&nbsp;</button><button
				disabled="disabled">F8<br>&nbsp;</button><button
				disabled="disabled">F9<br>&nbsp;</button><button
				disabled="disabled">F10<br>&nbsp;</button><button
				disabled="disabled">F11<br>&nbsp;</button><button
				disabled="disabled">F12<br>&nbsp;</button>
		</div>

		<s:form onsubmit="return false;">
			<html:hidden styleId="sortColumn" property="sortColumn" />
			<html:hidden styleId="sortOrderAsc"property="sortOrderAsc" />

			<div class="function_forms">

				<div style="padding-left: 20px">
					<html:errors/>
					<span id="ajax_errors"></span>
				</div>

				<span>社員情報</span><br>
				<div id="search_info">
					<table id="order_info" class="forms" style="width: 600px" summary="社員情報">
						<colgroup>
							<col span="1" style="width: 20%">
							<col span="1" style="width: 30%">
							<col span="1" style="width: 20%">
							<col span="1" style="width: 30%">
						</colgroup>
						<tr>
							<th>社員コード</th>
							<td colspan="3"><html:text styleId="userId" property="userId" tabindex="100" style="width: 130px; ime-mode: disabled;"  /></td>
						</tr>
						<tr>
							<th>社員名</th>
							<td colspan="3"><html:text styleId="nameKnj" property="nameKnj"  tabindex="101" style="width: 150px"/></td>
						</tr>
						<tr>
							<th>社員名カナ</th>
							<td colspan="3"><html:text styleId="nameKana" property="nameKana"  tabindex="102" style="width: 150px"/></td>
						</tr>
						<tr>
							<th>部門</th>
							<td colspan="3">
								<html:select styleId="deptId" property="deptId" tabindex="103" >
									<html:options collection="deptList" property="value" labelProperty="label" />
								</html:select>
							</td>
						</tr>
						<tr>
							<th>E-MAIL</th>
							<td colspan="3"><html:text styleId="email" property="email"  tabindex="106" style="width: 400px; ime-mode: disabled;"/></td>
						</tr>
					</table>
				</div>

				<div style="text-align: right; width: 600px">
					<button type="button" tabindex="350" style="width: 80px;"  onclick="initForm();">初期化</button>
					<button type="button" tabindex="351" style="width: 80px;" onclick="searchUser();">検索</button>
				</div>
			</div>
		</s:form>

		<div id="ListContainer">
			<div style="width: 910px; height: 25px;">
					<div style="position:absolute; left: 0px;">検索結果件数： 0件</div>
					<jsp:include page="/WEB-INF/view/common/rowcount.jsp"/>
			</div>

			<table id="List" summary="社員検索結果"  class="forms" style="width: 910px">
				<colgroup>
					<col span="1" style="width: 10%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 20%">
					<col span="1" style="width: 30%">
					<col span="1" style="width: 10%">
				</colgroup>
				<tr>
					<th style="cursor: pointer">社員コード</th>
					<th style="cursor: pointer">社員名</th>
					<th style="cursor: pointer">社員名カナ</th>
					<th style="cursor: pointer">部門</th>
					<th style="cursor: pointer">E-MAIL</th>
					<th>&nbsp;</th>
				</tr>
			</table>
		</div>

	</div>
</body>
</html>
