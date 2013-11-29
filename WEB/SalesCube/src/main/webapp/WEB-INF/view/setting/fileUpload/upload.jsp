<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　ファイル登録</title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
	var MAIN_FORM_NAME = "setting_fileUploadActionForm";

	function onF1() {
		if(!confirm('<bean:message key="confirm.init" />')){
			return;
		}
		//document.setting_fileUploadActionForm.action = "${f:url("/setting/fileUpload")}";
		//document.setting_fileUploadActionForm.submit();
		window.location.doHref('${f:url("/setting/fileUpload")}');
	}

	function ActionSubmit(ActionName){
		showNowSearchingDiv();
		$("form[name='" + MAIN_FORM_NAME + "']").attr("action",ActionName);
		$("form[name='" + MAIN_FORM_NAME + "']").submit();
	}

	function onF3() {
		if(!confirm('<bean:message key="confirm.file.upload" />')){
			return;
		}
		ActionSubmit("${f:url('/setting/fileUpload/upload')}");
	}

	function deleteFile(fileId, updDatetm) {
		if(!confirm('<bean:message key="confirm.delete" />')){
			return;
		}

		$("#info").empty();

		// 削除実行
		$("#ajax_infos").empty();
		asyncRequest(
			"${f:url('/ajax/setting/deleteFileAjax/delete')}",
			{ "fileId": fileId, "updDatetm": updDatetm },
			function(data) {
				$("#ajax_infos").append(data);
				return search();
			}
		);
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

		$("#info").empty();

		return search();
	}

	/**
	 * ファイル検索実行
	 */
	function search() {
		var data = new Object();
		data["sortColumn"] = $("#sortColumn").val();
		data["sortOrderAsc"] = $("#sortOrderAsc").val();

		// 検索実行
		asyncRequest(
			"${f:url('/ajax/setting/searchFileResultAjax/search')}",
			data,
			function(data) {
				// 検索結果テーブルを更新する
				$("#ListContainer").empty();
				$("#ListContainer").append(data);
			}
		);
	}

	/**
	 * 初期検索
	 */
	$(
		function() { search(); }
	);
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
		<%-- タイトル --%>
		<span class="title">ファイル登録</span>

		<s:form enctype="multipart/form-data">
			<div class="function_buttons">
				<button type="button" tabindex="2000" onclick="onF1();">F1<br>初期化</button>
				<button type="button" disabled="disabled">F2<br>&nbsp;</button>
				<button type="button" tabindex="2002" onclick="onF3();">F3<br>登録</button>
				<button type="button" disabled="disabled">F4<br>&nbsp;</button>
				<button type="button" disabled="disabled">F5<br>&nbsp;</button>
				<button type="button" disabled="disabled">F6<br>&nbsp;</button>
				<button type="button" disabled="disabled">F7<br>&nbsp;</button>
				<button type="button" disabled="disabled">F8<br>&nbsp;</button>
				<button type="button" disabled="disabled">F9<br>&nbsp;</button>
				<button type="button" disabled="disabled">F10<br>&nbsp;</button>
				<button type="button" disabled="disabled">F11<br>&nbsp;</button>
				<button type="button" disabled="disabled">F12<br>&nbsp;</button>
			</div>
<br><br><br>
			<div class="function_forms">
				<div id="info" style="padding-left: 20px">
					<html:errors/>
				</div>
				<span id="ajax_errors"></span>

				<div style="padding-left: 20px; color: blue;">
					<span id="ajax_infos">
					<html:messages id="msg" message="true">
						<bean:write name="msg" ignore="true"/>
					</html:messages>
					</span>
				</div>

			<div class="form_section_wrap">
				<div class="form_section">
					<div class="section_title">
						<span>ファイル情報</span>
					</div><!-- /.section_title -->
					<div class="section_body">
						<table class="forms" style="width: 600px" summary="ファイル情報">
							<colgroup>
								<col span="1" style="width: 20%">
								<col span="1" style="width: 80%">
							</colgroup>
							<tr>
								<th><div class="col_title_right">タイトル※</div></th>
								<td><html:text property="title" style="width: 450px;" tabindex="100" maxlength="60"/></td>
							</tr>
							<tr>
								<th><div class="col_title_right">ファイル名※</div></th>
								<td><html:file property="formFile" style="width: 450px;" onchange="$('#openLevel0').focus();" tabindex="101"/></td>
							</tr>
							<tr>
								<th><div class="col_title_right">公開設定</div></th>
								<td>
									<c:forEach var="bean" items="${openLevelList}" varStatus="status">
									<c:if test="${status.index != 0}"><br></c:if>
									<input type="radio" id="openLevel${status.index}" name="openLevel" value="${bean.value}" tabindex="102" <c:if test="${bean.value == openLevel}">checked</c:if> >
									<label for="openLevel${status.index}">${f:h(bean.label)}</label>

									<input type="hidden" name="openLevelList[${status.index}].label" value="${f:h(bean.label)}">
									<input type="hidden" name="openLevelList[${status.index}].value" value="${bean.value}">
									</c:forEach>
								</td>
							</tr>
						</table>
					</div><!-- /.section_body -->
    			</div><!-- /.form_section -->
   			</div><!-- /.form_section_wrap -->


				<div style="text-align: right; width: 1160px">
					<button type="button"  class="btn_medium" tabindex="150" onclick="onF1();">初期化</button>
					<button type="button"  class="btn_medium" tabindex="151" onclick="onF3();">登録</button>

					<input type="submit"  name="upload" tabindex="-1" value="登録" style="display:none;">
				</div>

				<br>

				<div id="ListContainer">
				</div>
			</div>

			<input type="hidden" id="sortColumn" name="sortColumn" value="title" />
			<input type="hidden" id="sortOrderAsc" name="sortOrderAsc" value="true" />
		</s:form>
	</div>
</body>

</html>
