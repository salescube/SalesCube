<%@page import="jp.co.arkinfosys.common.Constants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　棚番（登録・編集）</title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>
	<c:set var="code_size_rack" value="<%=Constants.CODE_SIZE.RACK%>" />
	<script type="text/javascript">
	<!--
    function init() {

    }
    // ファンクションキーとのマッピング
    function onF1() { initForm(); }
    function onF2() { backToSearch(); }
    function onF3() { registerRack(); }
    function onF4() { deleteRack(); }

    function initForm() {
    	if(confirm("<bean:message key='confirm.init'/>")){
            location.doHref(contextRoot + "/master/editRack/");
        }
    }

    function backToSearch() {
    	if(confirm("<bean:message key='confirm.master.rack.back'/>")){
            location.doHref(contextRoot + "/master/searchRack/");
        }
    }

    function registerRack() {
        <c:if test="${!editMode}">
    	if(confirm("<bean:message key='confirm.insert'/>")){
        	$("#editRackForm").attr("action", "${f:url("/master/editRack/insert")}");
        </c:if>
        <c:if test="${editMode}">
    	if(confirm("<bean:message key='confirm.update'/>")){
        	$("#editRackForm").attr("action", "${f:url("/master/editRack/update")}");
        </c:if>
    	$("#editRackForm").trigger("submit");
        }
    }

    function deleteRack() {
    	if(confirm("<bean:message key='confirm.delete'/>")){
        	$("#editRackForm").attr("action", "${f:url("/master/editRack/delete")}");
        	$("#editRackForm").trigger("submit");
        }
    }

	/**
	 * 倉庫検索ダイアログを開く
	 */
	function warehouseSearch(jqObject) {
		openSearchWarehouseDialog(
			"warehouseDialog",
			function(id, map) {
					$("#warehouseCode").val( map[ "warehouseCode" ] );
					$("#warehouseName").val( map[ "warehouseName" ] );
			}
		);
		$("#warehouseDialog_warehouseCode").val( jqObject.val() );
		$("#warehouseDialog_warehouseCode").focus();
	}

	/**
	 * 倉庫情報を消す
	 */
	function warehouseClear() {
		$("#warehouseCode").val("");
		$("#warehouseName").val("");
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
	<jsp:param name="MENU_ID" value="1306"/>
</jsp:include>

<!-- メイン機能 -->
<s:form styleId="editRackForm" onsubmit="return false;">
<input type="hidden" id="updatable" name="updatable" value="${isUpdate}">
<div id="main_function">

	<span class="title">棚番</span>

	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm()"> F1<br>初期化</button>
		<button tabindex="2001" onclick="backToSearch()">F2<br>戻る</button>
<c:if test="${!isUpdate}">
		<button tabindex="2002" disabled="true">F3<br>更新</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="2002" onclick="registerRack()">F3<br>更新</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="2002" onclick="registerRack()">F3<br>登録</button>
    </c:if>
</c:if>
<c:if test="${!isUpdate}">
		<button tabindex="2003" disabled="true">F4<br>削除</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="2003" onclick="deleteRack()">F4<br>削除</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="2003" disabled="true">F4<br>削除</button>
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
    	<div style="padding-left: 20px"><html:errors/></div>
    	<div style="padding-left: 20px;color: blue;">
        	<html:messages id="msg" message="true">
        		<bean:write name="msg" ignore="true"/><br>
        	</html:messages>
    	</div>

	    <div class="form_section_wrap">
		    <div class="form_section">

		    	<div class="section_title">
					<span>棚番情報</span>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="order_section" class="section_body">
			        <table id="user_info" class="forms" summary="倉庫情報1">
			            <colgroup>
			                <col span="1" style="width: 10%">
			                <col span="1" style="width: 10%">
			                <col span="1" style="width: 10%">
			                <col span="1" style="width: 25%">
			                <col span="1" style="width: 25%">
			            </colgroup>
			            <tr>
			                <th><div class="col_title_right">倉庫コード</div></th>
			                <td>
			                 	<html:text maxlength="10" styleId="warehouseCode" property="warehouseCode" style="width: 100px; ime-mode: disabled;" tabindex="100"/>
			                	<html:image src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;"
										onclick="warehouseSearch($('#warehouseCode'));" tabindex="101"/>
			                </td>
			                <th><div class="col_title_right">倉庫名</div></th>
			                <td>
			                	<html:text maxlength="60" styleId="warehouseName" property="warehouseName" style="width: 200px" readonly="true" styleClass="c_disable" tabindex="-1"/>
			                </td>
			                <td>
			                	<input type=button class="btn_list_action" value="クリア" onclick="warehouseClear();"/>
			                </td>
			            </tr>
			        </table>

					<table id="user_info" class="forms" summary="棚番情報1">
						<colgroup>
			                <col span="1" style="width: 10%">
			                <col span="1" style="width: 10%">
			                <col span="1" style="width: 10%">
			                <col span="1" style="width: 25%">
			                <col span="1" style="width: 15%">
			                <col span="1" style="width: 10%">
						</colgroup>
						<tr>
							<th><div class="col_title_right_req">棚番コード<bean:message key='labels.must'/></div></th>
							<td>
			                <c:if test="${editMode}">
			                    <html:text maxlength="${code_size_rack}" styleId="rackCode" property="rackCode" style="width: 100px; ime-mode: disabled;"  tabindex="200" readonly="true" styleClass="c_disable"/>
			                </c:if>
			                <c:if test="${!editMode}">
			                    <html:text maxlength="${code_size_rack}" styleId="rackCode" property="rackCode" style="width: 100px; ime-mode: disabled;"  tabindex="200"/>
			                </c:if>
			                </td>
							<th><div class="col_title_right_req">棚番名<bean:message key='labels.must'/></div></th>
							<td><html:text maxlength="60" styleId="rackName" property="rackName" style="width: 200px" tabindex="201"/></td>
							<th><div class="col_title_right">重複登録可能</div></th>
							<td><html:checkbox styleId="multiFlag" property="multiFlag" value="1" tabindex="203"></html:checkbox></td>
						</tr>
					</table>

					<table class="forms" summary="商品情報2" style="display: none;">
						<colgroup>
							<col span="1" style="width: 10%">
							<col span="1" style="width: 12%">
							<col span="1" style="width: 8%">
							<col span="1" style="width: 17%">
							<col span="1" style="width: 8%">
							<col span="1" style="width: 17%">
							<col span="1" style="width: 8%">
							<col span="1" style="width: 17%">
						</colgroup>
						<tr>
							<th><div class="col_title_right">郵便番号</div></th>
							<td>
							<html:text maxlength="8" styleId="zipCode" property="zipCode" style="width:70px;ime-mode:disabled;" tabindex="200"/>
			                <html:image tabindex="301" src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSearchZipDialog('zipcode', setZipCode);$('#zipcode_zipCode').val($('#zipCode').val());" />
			                </td>
							<th><div class="col_title_right">住所１</div></th>
							<td><html:text maxlength="50" styleId="address1" property="address1" tabindex="302" style="width: 150px"/></td>
							<th><div class="col_title_right">住所２</div></th>
							<td colspan="3"><html:text maxlength="50" styleId="address2" property="address2" tabindex="303" style="width: 300px"/></td>
						</tr>
						<tr>
							<th><div class="col_title_right">担当者</div></th>
							<td><html:text maxlength="60" styleId="rackPcName" property="rackPcName" style="width: 100px" tabindex="304"/></td>
							<th><div class="col_title_right">TEL</div></th>
							<td><html:text maxlength="15" styleId="rackTel" property="rackTel" style="width:150px;ime-mode:disabled;" tabindex="305"/></td>
							<th><div class="col_title_right">FAX</div></th>
							<td><html:text maxlength="15" styleId="rackFax" property="rackFax" style="width:150px;ime-mode:disabled;" tabindex="306"/></td>
							<th><div class="col_title_right">E-MAIL</div></th>
							<td><html:text maxlength="255" styleId="rackEmail" property="rackEmail" style="width:180px;ime-mode:disabled;" tabindex="307"/></td>
						</tr>
					</table>
				</div><!-- /.section_body -->
			</div><!-- /.form_section -->
		</div><!-- /.form_section_wrap -->

		<div style="text-align: right; width: 1160px">
			<span>登録日：${creDatetmShow}　更新日:${updDatetmShow}　</span>
			<button tabindex="800" onclick="initForm()" class="btn_medium">初期化</button>
<c:if test="${!isUpdate}">
            <button tabindex="801" disabled="true" class="btn_medium">更新</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
            <button tabindex="801" onclick="registerRack()" class="btn_medium">更新</button>
    </c:if>
	<c:if test="${!editMode}">
            <button tabindex="801" onclick="registerRack()" class="btn_medium">登録</button>
    </c:if>
</c:if>
<c:if test="${!isUpdate}">
		<button tabindex="802" disabled="true" class="btn_medium">削除</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="802" onclick="deleteRack()" class="btn_medium">削除</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="802" disabled="true" class="btn_medium">削除</button>
    </c:if>
</c:if>
		</div>
	</div>
</div>

<html:hidden property="creDatetm"/>
<html:hidden property="creDatetmShow"/>
<html:hidden property="updDatetm"/>
<html:hidden property="updDatetmShow"/>
</s:form>

</body>

</html>
