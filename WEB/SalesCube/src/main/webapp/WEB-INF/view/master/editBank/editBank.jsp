<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　銀行マスタ管理(登録・編集)</title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
    function init() {
    }

    // ファンクションキーとのマッピング
    function onF1() { initForm(); }
    function onF2() { backToSearch(); }
    function onF3() { registerBank(); }
    function onF4() { deleteBank(); }

    function initForm() {
    	if(confirm("<bean:message key='confirm.init'/>")){
            location.doHref(contextRoot + "/master/editBank/");
        }
    }

    function backToSearch() {
    	if(confirm("<bean:message key='confirm.master.bank.back'/>")){
            location.doHref(contextRoot + "/master/searchBank/");
        }
    }

    function registerBank() {
        <c:if test="${!editMode}">
    	if(confirm("<bean:message key='confirm.insert'/>")){
        	$("#editBankForm").attr("action", "${f:url('/master/editBank/insert')}");
        </c:if>
        <c:if test="${editMode}">
    	if(confirm("<bean:message key='confirm.update'/>")){
        	$("#editBankForm").attr("action", "${f:url('/master/editBank/update')}");
        </c:if>
       		_before_submit($(".numeral_commas"));
        	$("#editBankForm").trigger("submit");
        }
    }

    function deleteBank() {
    	if(confirm("<bean:message key='confirm.delete'/>")){
        	$("#editBankForm").attr("action", "${f:url('/master/editBank/delete')}");
    		_before_submit($(".numeral_commas"));
        	$("#editBankForm").trigger("submit");
        }
    }

    -->
	</script>
</head>
<body onhelp="return false;" onload="init()">

<%-- ページヘッダ領域 --%>
<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

<%-- メニュー領域 --%>
<jsp:include page="/WEB-INF/view/common/menubar.jsp">
	<jsp:param name="PARENT_MENU_ID" value="0013"/>
	<jsp:param name="MENU_ID" value="1315"/>
</jsp:include>


<!-- メイン機能 -->
<div id="main_function">

	<span class="title">銀行</span>

	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm()"> F1<br>初期化</button>
		<button tabindex="2001" onclick="backToSearch()">F2<br>戻る</button>
<c:if test="${!isUpdate}">
		<button tabindex="2002" disabled="disabled">F3<br>更新</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="2002" onclick="registerBank()">F3<br>更新</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="2002" onclick="registerBank()">F3<br>登録</button>
    </c:if>
</c:if>
<c:if test="${!isUpdate}">
		<button tabindex="2003" disabled="disabled">F4<br>削除</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="2003" onclick="deleteBank()">F4<br>削除</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="2003" disabled="disabled">F4<br>削除</button>
    </c:if>
</c:if>
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

	<div class="function_forms">
    <s:form styleId="editBankForm" onsubmit="return false;">
    <html:hidden styleId="bankId" property="bankId"/>
    	<div style="padding-left: 20px"><html:errors/></div>
    	<div style="padding-left: 20px;color: blue;">
        	<html:messages id="msg" message="true">
        		<bean:write name="msg" ignore="true"/><br>
        	</html:messages>
    	</div>

	    <div class="form_section_wrap">
		    <div class="form_section">

		    	<div class="section_title">
					<span>銀行情報</span>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="order_section" class="section_body">
					<table id="user_info" class="forms" summary="銀行情報" style="width: 500px">
						<tr>
							<th><div class="col_title_right_req">銀行コード<bean:message key='labels.must'/></div></th>
							<td colspan="3">
								<html:text tabindex="100" maxlength="4" styleId="bankCode" property="bankCode" style="width: 100px;ime-mode:disabled;"/>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right_req">銀行名<bean:message key='labels.must'/></div></th>
							<td colspan="3">
								<html:text tabindex="101" maxlength="20" styleId="bankName" property="bankName" style="width: 200px"/>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right_req">店名<bean:message key='labels.must'/></div></th>
							<td><html:text tabindex="102" maxlength="20" styleId="storeName" property="storeName" style="width: 200px"/></td>
							<th><div class="col_title_right_req">店番<bean:message key='labels.must'/></div></th>
							<td><html:text tabindex="103" maxlength="3" styleId="storeCode" property="storeCode" style="width: 50px;ime-mode:disabled;"/></td>
						</tr>
						<tr>
							<th><div class="col_title_right_req">科目<bean:message key='labels.must'/></div></th>
							<td colspan="3">
			    				<html:select styleId="dwbType" property="dwbType" tabindex="104">
			    					<html:options collection="dwbTypeList" property="value" labelProperty="label"/>
			    				</html:select>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right_req">口座番号<bean:message key='labels.must'/></div></th>
							<td colspan="3">
								<html:text tabindex="105" maxlength="7" styleId="accountNum" property="accountNum" style="width: 100px;ime-mode:disabled;"/>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right_req">口座名義<bean:message key='labels.must'/></div></th>
							<td colspan="3">
								<html:text tabindex="106" maxlength="20" styleId="accountOwnerName" property="accountOwnerName" style="width: 200px;ime-mode:disabled;"/>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right_req">口座名義カナ<bean:message key='labels.must'/></div></th>
							<td colspan="3">
								<html:text tabindex="107" maxlength="20" styleId="accountOwnerKana" property="accountOwnerKana" style="width: 200px;ime-mode:disabled;"/>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right">有効</div></th>
							<td colspan="3">
								<html:checkbox tabindex="108" styleId="valid" property="valid" value="1" />
							</td>
						</tr>
					</table>
				</div><!-- /.section_body -->
			</div><!-- /.form_section -->
		</div><!-- /.form_section_wrap -->

		<div style="text-align: right; width: 1160px">
			<span>登録日：${creDatetmShow}<html:hidden property="creDatetmShow"/>　更新日:${updDatetmShow}<html:hidden property="updDatetmShow"/>　</span>
			<button tabindex="150" onclick="initForm()" class="btn_medium">初期化</button>
<c:if test="${!isUpdate}">
			<button tabindex="151" disabled="disabled" class="btn_medium">更新</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
            <button tabindex="151" onclick="registerBank()" class="btn_medium">更新</button>
    </c:if>
	<c:if test="${!editMode}">
            <button tabindex="151" onclick="registerBank()" class="btn_medium">登録</button>
    </c:if>
</c:if>
<c:if test="${!isUpdate}">
		<button tabindex="152" disabled="disabled" class="btn_medium">削除</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="152" onclick="deleteBank()" class="btn_medium">削除</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="152" disabled="disabled" class="btn_medium">削除</button>
    </c:if>
</c:if>
		</div>
<html:hidden property="updDatetm"/>
    </s:form>
	</div>
</div>
</body>

</html>
