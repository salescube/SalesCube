<%@page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html lang="ja">
<head>
	<title><bean:message key='titles.system'/> <bean:message key='titles.outputRecommendList'/></title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

<script type="text/javascript">
<!--

//ロード時の関数読込み
window.onload = init;

//ページ読込時の動作
function init() {

}

//戻る
function onF2(){
	//何も聞かずにもどる
	window.location.doHref('${f:url("/porder/outputRecommendList")}');
}

//発注入力画面へ遷移
function goInputPOrderPage(id) {
	window.location.doHref(contextRoot + "/porder/inputPOrder/load/?poSlipId=" + id.replace(/\s*(\d+)\s*\-.*/,"$1"));
}

//伝票IDリスト生成
function createSlipIdListParam(slipId){
	data = new Object();
	var IdList = new Array();
	IdList.push("");
	IdList.push(slipId);
	data["slipIdList"] = IdList;
	return data;
}

//IDリストをAJAXで送る
function sendSlipIdList(slipId){
	//IDリストを取得
	var data = createSlipIdListParam(slipId);
	var result = false;
	//Ajax
	$.ajax( {
		"type" : "POST",
		"async" : false,
		"url" : contextRoot + "/ajax/POrderReportWriterAjax/sendSlipIdList",
		"data" : data,
		"beforeSend" : function() {
			$("#errors").empty();
		},
		"error" : function(xmlHttpRequest, textStatus, errorThrown) {
			if (xmlHttpRequest.status == 450) {

				$("#errors").empty();
				$("#errors").append(xmlHttpRequest.responseText);
			} else if (xmlHttpRequest.status == 401) {
				// 未ログイン
				alert(xmlHttpRequest.responseText);
				window.location.doHref(contextRoot + "/login");
			} else {
				// その他のエラー
				alert(xmlHttpRequest.responseText);
			}
		},
		"success" : function(data) {
			if(data!=""){
				result = false;
				$("#errors").append(data);
			}else{
				result = true;
			}
		}
	});
	return result;
}

//EXCEL
function excel(slipId){
	if(!confirm('<bean:message key="confirm.excel" />')){return;}
	//IDリストを取得
	var data = createSlipIdListParam(slipId);
	//Ajax
	$("#errors").empty();
	asyncRequest(
			contextRoot + "/ajax/POrderReportWriterAjax/sendSlipIdList",
			data,
			function(data) {
				if(data!=""){
					$("#errors").append(data);
				}else{
					window.open(contextRoot + "/ajax/POrderReportWriterAjax/excel","<bean:message key='words.name.excel'/>");
				}
			},
			null
	);
}

// PDF
function pdf(slipId){
	if(!confirm('<bean:message key="confirm.pdf" />')){return;}
	//IDリストを取得
	var form = $(window.document.forms["PDFOutputForm"]);
	var hidden = $(document.createElement("input"));
	hidden.attr("type", "hidden");
	hidden.attr("name", "slipIdList");
	hidden.val(slipId);
	form.append(hidden);
	form.submit();
}

-->
</script>

</head>
<body>

	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0007"/>
		<jsp:param name="MENU_ID" value="0704"/>
	</jsp:include>

<!-- メイン機能  -->
<div id="main_function">

<!-- タイトル -->
<span class="title"><bean:message key='titles.outputRecommendList'/></span>

<div class="function_buttons">
	<button                             disabled="disabled" tabindex="2000">F1<br><bean:message key='words.action.none'/></button><!---->
	<button id="btnF2" onclick="onF2()"                     tabindex="2001">F2<br><bean:message key='words.action.retrun'/></button><!--戻る-->
	<button                             disabled="disabled" tabindex="2002">F3<br><bean:message key='words.action.none'/></button><!---->
	<button                             disabled="disabled" tabindex="2003">F4<br><bean:message key='words.action.none'/></button><!---->
	<button                             disabled="disabled" tabindex="2004">F5<br><bean:message key='words.action.none'/></button><!---->
	<button                             disabled="disabled" tabindex="2005">F6<br><bean:message key='words.action.none'/></button><!---->
	<button                             disabled="disabled" tabindex="2006">F7<br><bean:message key='words.action.none'/></button><!---->
	<button                             disabled="disabled" tabindex="2007">F8<br><bean:message key='words.action.none'/></button><!---->
	<button                             disabled="disabled" tabindex="2008">F9<br><bean:message key='words.action.none'/></button><!---->
	<button                             disabled="disabled" tabindex="2009">F10<br><bean:message key='words.action.none'/></button><!---->
	<button                             disabled="disabled" tabindex="2010">F11<br><bean:message key='words.action.none'/></button><!---->
	<button                             disabled="disabled" tabindex="2011">F12<br><bean:message key='words.action.none'/></button>
</div>
<br><br><br>
	
<div class="function_forms">

	<!-- メッセージ -->
	<div id="errors" style="color: red">
		<html:errors/>
	</div>
	<div id="ajax_errors" style="color: red"></div>
	<div id="messages" style="color: blue;">
		<html:messages id="msg" message="true">
			<bean:write name="msg" ignore="true"/><br>
		</html:messages>
	</div>
	
	<div style="color: blue;">
		${f:h(supplierName)}<bean:message key='infos.polder.madePOrderSlipTo'/>
	</div>
	
	<!-- コンテンツ -->
	<div class="form_section_wrap">
	<div class="form_section">
		<table id="search_result" summary="searchResult" class="forms detail_info" style="table-layout: auto; margin-top: 20px;">
			<colgroup>
				<col span="1" style="width: 5%">
				<col span="1" style="width: 25%">
				<col span="1" style="width: 6%">
				<col span="1" style="width: 32%">
				<col span="1" style="width: 32%">
			</colgroup>
			<tr>
				<th rowspan="2" class="rd_top_left" style="height:30px;"><bean:message key='words.action.select'/></th><!-- 選択 -->
				<th colspan="2"><bean:message key='titles.productInfos'/></th><!-- 商品 -->
				<th rowspan="2"><bean:message key='labels.poSlipId'/></th><!-- 発注番号 -->
				<th rowspan="2" class="rd_top_right"><bean:message key='labels.poSlip'/></th><!-- 発注書 -->
			</tr>
			<tbody id="tbodyLine">
			<c:forEach var="slip" items="${orderResultList}" varStatus="slipStatus">
				<c:forEach var="line" items="${slip.lines}" varStatus="lineStatus">
				<tr>
					<td style="text-align :center"><input type="checkbox" checked="checked" disabled="disabled"></td>
					<td>${f:h(line.productCode)}</td>
					<td style="text-align: center">${f:h(line.pOrderQuantity)}</td>
					<c:if test="${lineStatus.first}">
					<td rowspan="${slip.lineCount}">
					<c:if test="${validInputPOrder}">
						<a href="javascript:goInputPOrderPage('${f:h(slip.poSlipId)}')" tabindex="${f:h(slipStatus.index)*3+100}" >
							${f:h(slip.poSlipId)}
						</a>
					</c:if>
					<c:if test="${!validInputPOrder}">
						${f:h(slip.poSlipId)}
					</c:if>
					</td>
					<td rowspan="${slip.lineCount}">
						<a href="javascript:pdf('${f:h(slip.poSlipId)}')" tabindex="${f:h(slipStatus.index)*3+102}" >${f:h(slip.pdfFileName)}</a><br>
					</td>
					</c:if>
				</tr>
				</c:forEach>
			</c:forEach>
			</tbody>
		</table>
	</div>
	</div>
</div>
</div>
<form name="PDFOutputForm" action="${f:url('/porder/makeOutPOrderResultOutput/pdf')}" style="display: none;" method="POST">
</form>
</body>
</html>
