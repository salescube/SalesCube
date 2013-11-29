<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title><bean:message key='titles.system'/>　税マスタ一覧</title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
        //編集ボタン
        function edit(taxTypeCategory){
    		$("#taxTypeCategory").val(taxTypeCategory);
    		$("#editForm").submit();
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
	<jsp:param name="MENU_ID" value="1310"/>
</jsp:include>

<!-- メイン機能 -->
<div id="main_function">

	<span class="title">税</span>

	<div class="function_buttons">
		<button disabled="disabled">F1<br>&nbsp;</button>
		<button disabled="disabled">F2<br>&nbsp;</button>
		<button disabled="disabled">F3<br>&nbsp;</button>
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
   <br></br>
	<div class="function_forms">

	<div id="forms_in_search_group">
	<s:form styleId="taxRateForm" onsubmit="return false;">

		<span style ="color:blue">税区分一覧</span><br>
		<div id="detail_info_wrap">
		<table class="detail_info" style="width: 40%; summary="poLines" class="forms" style="margin-top: 20px; margin-bottom: 0px; border-bottom: 0px;" summary="税区分一覧">
<!--
			<colgroup>

				<col span="1" style="">

				<col span="1" style="width:80px;">
			</colgroup>
 -->
			<tr>
				<th style="width: 60%; height: 30px;" class="rd_top_left"  >税区分</th>
				<th class="rd_top_right"></th>
			</tr>

        <c:forEach var="categoryList" varStatus="s" items="${categoryList}">
            <tr>
            	<td class="rd_bottom_left">${categoryList.label}</td>
            	<td class="rd_bottom_right" style="text-align:center;">
                	<button class="btn_medium" onclick="edit('${categoryList.value}');">${isUpdate ? '編集' : '参照'}</button>

            	</td>
            </tr>
        </c:forEach>
		</table>
		</div>
	</s:form>
	</div>

	<s:form styleId="editForm" action="/master/editTaxRate/edit" >
		<input type="hidden" id="taxTypeCategory" name="taxTypeCategory">
	</s:form>
	</div>
</div>
</body>
</html>
