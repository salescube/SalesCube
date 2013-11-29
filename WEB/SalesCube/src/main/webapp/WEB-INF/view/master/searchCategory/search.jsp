<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　区分マスタ一覧</title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--

	function edit(categoryId) {
		$("#categoryId").val(categoryId);
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
	<jsp:param name="MENU_ID" value="1309"/>
</jsp:include>

<!-- メイン機能 -->
<div id="main_function">
<s:form styleId="categoryForm" onsubmit="return false;">

	<span class="title">区分</span>

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
 	<br><br></br>
 
	<div class="function_forms">
		<div style="padding-left: 20px"><html:errors/></div>
		<div style="padding-left: 20px;color: blue;">
			<html:messages id="msg" message="true">
				<bean:write name="msg" ignore="true"/><br>
			</html:messages>
		</div>

		<div id="forms_in_search_group">
			<span style ="color:blue">区分名一覧</span><br>
			<div id="detail_info_wrap">
				<table class="detail_info" style="width: 40%;" summary="分類名一覧">
	
					<colgroup>
	
						<col span="1" style="">
	
						<col span="1" style="width:80px;">
					</colgroup>
	
					<tr>
						<th style="width: 60%; height: 30px;" class="rd_top_left" >区分名</th>
						<th class="rd_top_right"></th>
					</tr>
	
		        <c:forEach var="group" varStatus="g" items="${groupList}">
		            <tr><td colspan="2" style="font-weight: bold; text-align:center;">－${group.name}－</td></tr>
		            <c:forEach var="category" varStatus="s" items="${group.categoryList}">
		                <tr>
		                	<td>${category.name}</td>
		                	<td style="text-align:center;">
		                		<button class="btn_medium"  onclick="edit('${category.categoryId}');">${isUpdate ? '編集' : '参照'}</button>
		                	</td>
		                </tr>
		            </c:forEach>
		        </c:forEach>
				</table>
			</div>
		</div>
	</div>
</s:form>

<s:form styleId="editForm" action="/master/editCategory/edit" >
	<input type="hidden" id="categoryId" name="categoryId">
</s:form>
</div>
</body>
</html>
