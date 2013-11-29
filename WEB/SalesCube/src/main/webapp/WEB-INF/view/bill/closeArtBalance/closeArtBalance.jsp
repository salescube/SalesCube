<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="jp.co.arkinfosys.common.Constants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ja">
<head>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<title><bean:message key='titles.system'/> 売掛締処理</title>

	<script type="text/javascript" src="${f:url('/scripts/dialogs.js')}"></script>
	<meta http-equiv="Content-Style-Type" content="text/css">
	<script type="text/javascript">

	<!--
	var MAIN_FORM_NAME = "bill_closeArtBalanceActionForm";
	var maxIndex = 0;


	//ページ読込時の動作
	function init() {
		// 初期フォーカス 支払条件
		$("#cutoffGroupCategory").focus();
		$("#customerCode").attr("maxlength", <%=Constants.CODE_SIZE.CUSTOMER%>);	//顧客コードの文字数制限10桁
	}

	// 初期化
	function onF1(){
		if(confirm('<bean:message key="confirm.init" />')){
				showNowSearchingDiv();
				window.location.doHref('${f:url("/bill/closeArtBalance")}');
		}
	}

	// 締実行
	function onF3(){
//		if(confirm('<bean:message key="confirm.insert" />')){
			doClose();
//			document.bill_closeBillActionForm.action = '${f:url("/bill/closeBill/close")}';
//			document.bill_closeBillActionForm.submit();
//		}
	}
	// 締解除
	function onF4(){
		if(confirm('<bean:message key="confirm.exec" arg0="締解除" />')){
			document.bill_closeArtBalanceActionForm.action = '${f:url("/bill/closeArtBalance/reopen")}';
			showNowSearchingDiv();
			document.bill_closeArtBalanceActionForm.submit();
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
	function dcCloseArt(){
		$("#cutOffDate").val($("#cutOffDateDlg").val());
		document.bill_closeArtBalanceActionForm.action = '${f:url("/bill/closeArtBalance/close")}';
		showNowSearchingDiv();
		document.bill_closeArtBalanceActionForm.submit();
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
		<jsp:param name="MENU_ID" value="0503"/>
	</jsp:include>

<!-- メイン機能 -->
	<div id="main_function">

		<!-- タイトル -->
		<span class="title">売掛締処理</span>

		<div class="function_buttons">
			<button type="button" tabindex="2000" id="btnF1" onclick="onF1();">F1<br>初期化</button>
			<button disabled="disabled" type="button" tabindex="2001" id="btnF2" onclick="onF2();">F2<br>&nbsp;</button>
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

			<span id="ListContainer">

			<table id="billing_close_2" summary="締処理リスト2" class="forms detail_info" style="width: 150px;">
				<colgroup>
					<col span="1" style="width: 100%">
				</colgroup>
				<tr>
					<th class="rd_top_left rd_top_right" style="cursor: pointer; height: 30px;">前回締日</th>
				</tr>
				<tr>
					<html:hidden name="closeArtBalanceForm" property="lastCutOffDate"></html:hidden>
					<c:if test="${closeArtBalanceForm.lastCutOffDate == ''}" >
						<td align="center" style="height: 30px;"><bean:message key='labels.artBalanceNoData'/></td>
					</c:if>
					<c:if test="${closeArtBalanceForm.lastCutOffDate != ''}" >
						<td align="center" style="height: 30px;"><c:out value="${f:h(lastCutOffDate)}" /></td>
					</c:if>
				</tr>
			</table>

			</span>
			<html:hidden property="cutOffDate" styleId="cutOffDate" />
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
			<button id="closeExecButton" tabindex="3000" onclick="dcCloseArt()">ＯＫ</button>
			<button tabindex="3001" onclick="$('#do_close').dialog('close');">キャンセル</button>
		</div>
	</div>
</div>
	</s:form>
	</div>
</body>
</html>