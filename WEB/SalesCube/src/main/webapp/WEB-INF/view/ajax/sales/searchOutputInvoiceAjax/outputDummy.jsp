<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%@ include file="/WEB-INF/view/common/header.jsp" %>
	<script type="text/javascript" src="${f:url('/scripts/dialogs.js')}"></script>

	<script type="text/javascript">
	<!--
	var MAIN_FORM_NAME = "ajax_searchOutputInvoiceAjaxActionForm";

	function submitParam(){
		// 帳票出力条件を設定
		var data = window.opener.getReportInfo();
		setCondition(data);

		// 出力
		$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/ajax/searchOutputInvoiceAjax/excel")}');
		$("form[name='" + MAIN_FORM_NAME + "']").submit();
	}

	/**
	 * 帳票の出力条件を設定する
	 */
	function setCondition(data){
		var list = data["salesSlipIdList"];
		for(i=0;i<list.length;i++){
			// 売上伝票番号
			$("#conditions").append("<input type='hidden' name='salesIdList[" + i + "]' value='" + list[ i ] + "'>");
		}
	}
	-->
	</script>
</head>
<body onload="submitParam()">
<s:form>
	<!-- 帳票出力条件を設定 -->
	<div id="conditions"></div>
</s:form>
</body>
</html>