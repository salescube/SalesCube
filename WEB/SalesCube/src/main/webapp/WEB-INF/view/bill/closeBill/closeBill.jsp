<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="jp.co.arkinfosys.common.Constants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ja">
<head>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<title><bean:message key='titles.system'/> 請求締処理</title>

	<script type="text/javascript" src="${f:url('/scripts/dialogs.js')}"></script>
	<meta http-equiv="Content-Style-Type" content="text/css">
	<script type="text/javascript">

	<!--
	var MAIN_FORM_NAME = "bill_closeBillActionForm";
	var maxIndex = 0;


	//ページ読込時の動作
	function init() {
		// 初期フォーカス 支払条件
		$("#cutoffGroupCategory").focus();
		$("#customerCode").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁
		notYetRequestedCheckChanged();	//請求漏れチェックの場合は、「掛売以外の全顧客」の部分を表示しないようにする
	}

	// 初期化
	function onF1(){
		if(confirm('<bean:message key="confirm.init" />')){
				showNowSearchingDiv();
				window.location.doHref('${f:url("/bill/closeBill")}');
		}
	}

	// 検索
	function onF2(){

		$("#message").hide();	// 締処理完了のメッセージを非表示にする
		var data = createParamData();
		return execSearch(data);

	}

	// 締実行
	function onF3(){
		doClose();
	}

	// 締解除
	function onF4(){
		if(confirm('<bean:message key="confirm.exec" arg0="締解除" />')){
			document.bill_closeBillActionForm.action = '${f:url("/bill/closeBill/reopen")}';
			showNowSearchingDiv();
			document.bill_closeBillActionForm.submit();
		}
	}

	//伝票複写機能のダミー
	function onF6() {
		copy_flip();
	}

	// ================================================================================================

	// ================================================================================================
	// 区分系共通
	// ================================================================================================
	//リストの絞込み
	function catrgoryListCtrl(targetName,value){
		$(targetName + " option").each(
			function(i){
				$(this).remove();
		});

		var elmSel = catArray[targetName];
		var elmOpt = elmSel.children(":first");
		for( i=0 ; i < elmSel.get(0).length ; i++ ){
			if( elmOpt.get(0).value == value ){
				$(targetName).append($('<option>').attr("value",value).text(elmOpt.get(0).text));
				return;
			}
			elmOpt = elmOpt.next();
		}
	}
	//リストへの追加
	function catrgoryListInit(targetName){
		$(targetName + " option").each(
			function(i){
				$(this).remove();
		});

		var elmSel = catArray[targetName];
		var elmOpt = elmSel.children(":first");
		for( i=0 ; i < elmSel.get(0).length ; i++ ){
			$(targetName).append($('<option>').attr("value",elmOpt.get(0).value).text(elmOpt.get(0).text));
			elmOpt = elmOpt.next();
		}
	}

	// ================================================================================================
	// 顧客系Ajax
	// ================================================================================================
	//顧客検索ダイアログ呼び出し
	function customerSearch(codeFlg){
		openSearchCustomerDialog('customer', customerCallBack);
		if( codeFlg ){
			$("#"+'customer'+"_customerCode").val($("#customerCode").val());
		}else{
			$("#"+'customer'+"_customerName").val($("#customerName").val());
		}
	}

	//顧客先情報フォームIDリスト
	var CustomerInfosIDList = new Array(
			"customerCode"
			,"customerName"
	);
	var CustomerInfosDBList = new Array(
			"customerCode"
			,"customerName"
	);
	//顧客情報の初期化
	function InitCustomerInfos(){
		for(var i in CustomerInfosIDList){
			$("#"+ CustomerInfosIDList[i]).attr("value","");
		}
	}

	function customerCallBack(id, map) {
		for(var i in CustomerInfosIDList){
			$("#"+ CustomerInfosIDList[i]).attr("value",map[CustomerInfosDBList[i]]);
		}
	}


	// ================================================================================================
	// 検索系Ajax
	// ================================================================================================
	//検索条件IDリスト
	var SearchIDList = new Array(
			"cutoffGroupCategory"
			,"customerCode"
			,"customerName"
	);

	function createParamData(){
		// リクエストデータ作成
		var data = new Object();

		for(var i in SearchIDList) {
			if($("#"+ SearchIDList[i]).val()) {
				data[SearchIDList[i]] = $("#"+ SearchIDList[i]).val();
			}
		}

		data["notYetRequestedCheck"] = $("#notYetRequestedCheck").attr("checked");

		return data;
	}

	//検索実行
	function execSearch(data){

		// Ajaxリクエストによって検索結果をロードする
		asyncRequest(
				contextRoot + "/ajax/bill/searchCloseBillResultAjax/search",
				data,
				function(data) {
					$("#errors").empty();
					// 検索結果テーブルを更新する
					$("#ListContainer").empty();
					$("#ListContainer").append(data);

					afterSearch();

				});
		return false;

	}

	// 検索後の処理
	function afterSearch(){
		notYetRequestedCheckChanged();	//請求漏れチェックの場合は、「掛売以外の全顧客」の部分を表示しないようにする
	}
	// ================================================================================================
	// 画面操作系
	// ================================================================================================
	function checkAll(flg){
		$("#tbodyLine").find("input[type='checkbox']").attr('checked', flg);
	}

	// 締日指定画面
	function doClose(evt) {
		$("#do_close").dialog({
			bgiframe: true,
			autoOpen: false,
			width: 400,
			height: 200,
			modal: false,
			buttons: {
			}
		});

		$("#cutOffDateDlg").val($("#cutOffDate").val());
		$("#do_close").dialog('open');

		if(jQuery.browser.msie) {
			$("#do_close").dialog('option', 'height', 150);
			$("#do_close").css("height", 150);
			window.event.keyCode = null;
			window.event.returnValue = false;
		}
		return false;
	}

	// 締実行
	function dcCloseBill(){
		$("#cutOffDate").val($("#cutOffDateDlg").val());
		document.bill_closeBillActionForm.action = '${f:url("/bill/closeBill/close")}';
		showNowSearchingDiv();
		document.bill_closeBillActionForm.submit();
	}

	//請求漏れチェックの場合は、「掛売以外の全顧客」の部分を表示しないようにする
	function notYetRequestedCheckChanged(){
		if($("#notYetRequestedCheck").attr("checked") == true) {
			$("#billing_close_2").hide();
		} else {
			$("#billing_close_2").show();
		}
	}

	-->
	</script>

</head>
<body onload="init()" onhelp="return false;">


<!-- ヘッダ -->
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>
<!-- メニュー -->
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0005"/>
		<jsp:param name="MENU_ID" value="0501"/>
	</jsp:include>

<!-- メイン機能 -->
	<div id="main_function">

		<!-- タイトル -->
		<span class="title">請求締処理</span>

		<div class="function_buttons">
			<button type="button" tabindex="2000" id="btnF1" onclick="onF1();">F1<br>初期化</button>
			<button type="button" tabindex="2001" id="btnF2" onclick="onF2();">F2<br>検索</button>
			<button type="button" tabindex="2002" id="btnF3" onclick="onF3();">F3<br>締実行</button>
			<button type="button" tabindex="2003" id="btnF4" onclick="onF4();">F4<br>締解除</button>
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
			<!-- エラー情報 -->
			<div id="errors" style="color: red">
				<html:errors />
			</div>
				<span id="ajax_errors"></span>
				<div id = "message" style="padding-left: 20px;color: blue;">
				<html:messages id="msg" message="true">
					<bean:write name="msg" ignore="true"/><br>
				</html:messages>
				</div>

		<div class="form_section_wrap">
			<div class="form_section">
				<div class="section_title">
					<span >検索条件</span>
				</div><!-- /.section_title -->
				<div class="section_body">
						<table id="search_info" class="forms" style="width: 600px" summary="請求締処理">
							<colgroup>
								<col span="1" style="width: 20%">
								<col span="1" style="width: 30%">
								<col span="1" style="width: 20%">
								<col span="1" style="width: 30%">
							</colgroup>
							<tr>
								<th><div class="col_title_right">支払条件</div></th>
								<td>
									<html:select tabindex="100" property="cutoffGroupCategory"  styleId="cutoffGroupCategory" >
										<c:forEach var="cgcl" items="${cutoffGroupCategoryList}">
											<html:option value="${cgcl.value}">${cgcl.label}</html:option>
										</c:forEach>
									</html:select>
								</td>
								<th><div class="col_title_right">請求漏れチェック</div></th>
								<td>
									<html:checkbox property="notYetRequestedCheck" styleId="notYetRequestedCheck"  onclick="notYetRequestedCheckChanged()" />請求漏れのみ
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right">顧客コード</div></th>
								<td>
									<html:text tabindex="100" property="customerCode" styleId="customerCode" style="width: 100px; ime-mode:disabled;"  />
									<html:image tabindex="101" src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" onclick="customerSearch(true)" />
									<html:hidden property="cutOffDate" styleId="cutOffDate" />
								</td>
								<th><div class="col_title_right">顧客名</div></th>
								<td>
									<html:text tabindex="102" property="customerName" styleId="customerName" style="width: 240px; ime-mode:active;" />
									<html:image tabindex="103" src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" onclick="customerSearch(false)" />
								</td>
							</tr>
						</table>
					</div><!-- /.section_body -->
    			</div><!-- /.form_section -->
   			</div><!-- /.form_section_wrap -->

			<div style="text-align: right; width: 1160px">
				<button name="initSearch" type="button" onclick="onF1()" tabindex="103" class="btn_medium">初期化</button><!-- 初期化 -->
				<button name="search" type="button" onclick="onF2()" tabindex="104" class="btn_medium">検索</button><!-- 検索 -->
			</div>

			<span id="ListContainer">
			<%@ include file="/WEB-INF/view/ajax/bill/searchCloseBillResultAjax/result.jsp" %>
			</span>
		</div>
		
		<!-- 情報ダイアログ -->
		<div id="do_close" title="締実行" style="display: none;">
			<div style="padding: 20px 20px 0 20px;">
				<s:form style="margin: 0px;">
					<table class="forms" style="width: 380px;" summary="締実行">
						<tr>
							<th>締実行日</th>
							<td><input tabindex="-1" type="text" id="cutOffDateDlg" class="date_input" style="ime-mode:disabled;" /></td>
						</tr>
					</table>
				</s:form>
				<div style="width: 96%; text-align: right; margin-top: 15px;">
					<button id="closeExecButton" tabindex="3000" onclick="dcCloseBill()">ＯＫ</button>
					<button tabindex="3001" onclick="$('#do_close').dialog('close');">キャンセル</button>
				</div>
			</div>
		</div>
	</s:form>
	</div>
</body>
</html>