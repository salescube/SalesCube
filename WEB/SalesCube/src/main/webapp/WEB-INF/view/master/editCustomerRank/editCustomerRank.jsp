<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　顧客ランクマスタ管理(登録・編集)</title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
    function init() {
		applyStatsAlignment($(".BDCrate"));
    }

	// 統計端数処理を適用する
	function applyStatsAlignment(jQueryObject) {
		if(jQueryObject != null) {
			// 四捨五入
			jQueryObject.setBDCStyle( "1" ,$("#statsDecAlignment").val() ).attBDC();
		}
	}

    // ファンクションキーとのマッピング
    function onF1() { initForm(); }
    function onF2() { backToSearch(); }
    function onF3() { registerCustomerRank(); }
    function onF4() { deleteCustomerRank(); }

    function initForm() {
    	if(confirm("<bean:message key='confirm.init'/>")){
            location.doHref(contextRoot + "/master/editCustomerRank/");
        }
    }

    function backToSearch() {
    	if(confirm("<bean:message key='confirm.master.customerrank.back'/>")){
            location.doHref(contextRoot + "/master/searchCustomerRank/");
        }
    }

    function registerCustomerRank() {
        <c:if test="${!editMode}">
    	if(confirm("<bean:message key='confirm.insert'/>")){
        	$("#editCustomerRankForm").attr("action", "${f:url("/master/editCustomerRank/insert")}");
        </c:if>
        <c:if test="${editMode}">
    	if(confirm("<bean:message key='confirm.update'/>")){
        	$("#editCustomerRankForm").attr("action", "${f:url("/master/editCustomerRank/update")}");
        </c:if>
       		_before_submit($(".numeral_commas"));
        	$("#editCustomerRankForm").trigger("submit");
        }
    }

    function deleteCustomerRank() {
    	if(confirm("<bean:message key='confirm.delete'/>")){
        	$("#editCustomerRankForm").attr("action", "${f:url("/master/editCustomerRank/delete")}");
    		_before_submit($(".numeral_commas"));
        	$("#editCustomerRankForm").trigger("submit");
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
	<jsp:param name="MENU_ID" value="1314"/>
</jsp:include>

<!-- メイン機能 -->
<s:form styleId="editCustomerRankForm" onsubmit="return false;">
<input type="hidden" id="updatable" name="updatable" value="${isUpdate}">
<input type="hidden" id="statsDecAlignment" value="${mineDto.statsDecAlignment}">
<div id="main_function">

	<span class="title">顧客ランク</span>

	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm()"> F1<br>初期化</button>
		<button tabindex="2001" onclick="backToSearch()">F2<br>戻る</button>
<c:if test="${!isUpdate}">
		<button tabindex="2002" disabled="disabled">F3<br>更新</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="2002" onclick="registerCustomerRank()">F3<br>更新</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="2002" onclick="registerCustomerRank()">F3<br>登録</button>
    </c:if>
</c:if>
<c:if test="${!isUpdate}">
		<button tabindex="2003" disabled="disabled">F4<br>削除</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="2003" onclick="deleteCustomerRank()">F4<br>削除</button>
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
    	<div style="padding-left: 20px"><html:errors/></div>
    	<div style="padding-left: 20px;color: blue;">
        	<html:messages id="msg" message="true">
        		<bean:write name="msg" ignore="true"/><br>
        	</html:messages>
    	</div>


	    <div class="form_section_wrap">
		    <div class="form_section">
		    	<div class="section_title">
					<span>顧客ランク情報</span>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="order_section" class="section_body">
					<table id="user_info" class="forms" summary="顧客ランク情報" style="width: 600px">
						<tr>
							<th colspan="2"><div class="col_title_right">顧客ランクコード</div></th>
							<td><html:text styleId="rankCode" property="rankCode" style="width: 100px"  tabindex="100" readonly="true" styleClass="c_disable"/></td>
						</tr>
						<tr>
							<th colspan="2"><div class="col_title_right_req">顧客ランク名<bean:message key='labels.must'/></div></th>
							<td><html:text maxlength="60" styleId="rankName" property="rankName" style="width: 100px" tabindex="101"/></td>
						</tr>
						<tr>
							<th colspan="2"><div class="col_title_right_req">値引率<bean:message key='labels.must'/></div></th>
							<td><html:text maxlength="${(3 + mineDto.statsDecAlignment)}" styleId="rankRate" property="rankRate"
								onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ applyStatsAlignment($(this)); }"
								style="width: 100px;ime-mode:disabled;text-align:right;" styleClass="numeral_commas BDCrate" tabindex="102"/> ％</td>
						</tr>
						<tr>
							<th colspan="2"><div class="col_title_right_req">送料区分<bean:message key='labels.must'/></div></th>
							<td>
			    				<html:select styleId="postageType" property="postageType" tabindex="104">
			    					<html:options collection="postageTypeList" property="value" labelProperty="label"/>
			    				</html:select>
			                </td>
						</tr>
					</table>
				</div><!-- /.section_body -->
			</div><!-- /.form_section -->
		</div><!-- /.form_section_wrap -->

	    <div class="form_section_wrap">
		    <div class="form_section">
		    	<div class="section_title">
					<span>基準</span>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="order_section" class="section_body">
					<table id="user_info" class="forms" summary="顧客ランク情報" style="width: 600px">
						<tr>
							<th><div class="col_title_right">売上回数</div></th>
							<td><html:text maxlength="10" styleId="roCountFrom" property="roCountFrom" style="width: 100px;ime-mode:disabled;text-align:right;" tabindex="105"/> 回以上 <html:text maxlength="10" styleId="roCountTo" property="roCountTo" style="width: 100px;ime-mode:disabled;text-align:right;" tabindex="106"/> 回未満</td>
						</tr>
						<tr>
							<th><div class="col_title_right">在籍期間</div></th>
							<td><html:text maxlength="10" styleId="enrollTermFrom" property="enrollTermFrom" style="width: 100px;ime-mode:disabled;text-align:right;" tabindex="107"/> 日以上 <html:text maxlength="10" styleId="enrollTermTo" property="enrollTermTo" style="width: 100px;ime-mode:disabled;text-align:right;" tabindex="108"/> 日未満</td>
						</tr>
						<tr>
							<th><div class="col_title_right">離脱期間</div></th>
							<td><html:text maxlength="10" styleId="defectTermFrom" property="defectTermFrom" style="width: 100px;ime-mode:disabled;text-align:right;" tabindex="109"/> 日以上 <html:text maxlength="10" styleId="defectTermTo" property="defectTermTo" style="width: 100px;ime-mode:disabled;text-align:right;" tabindex="110"/> 日未満</td>
						</tr>
						<tr>
							<th><div class="col_title_right">月平均売上額</div></th>
							<td><html:text maxlength="10" styleId="roMonthlyAvgFrom" property="roMonthlyAvgFrom" style="width: 100px;ime-mode:disabled;text-align:right;" styleClass="numeral_commas style_quantity" tabindex="111"/> 円以上 <html:text maxlength="10" styleId="roMonthlyAvgTo" property="roMonthlyAvgTo" style="width: 100px;ime-mode:disabled;text-align:right;" styleClass="numeral_commas style_quantity" tabindex="112"/> 円未満</td>
						</tr>
					</table>
				</div><!-- /.section_body -->
			</div><!-- /.form_section -->
		</div><!-- /.form_section_wrap -->

		<div style="text-align: right; width: 1160px">
			<span>登録日：${creDatetmShow}<html:hidden property="creDatetmShow"/>　更新日:${updDatetmShow}<html:hidden property="updDatetmShow"/>　</span>
			<button tabindex="800" onclick="initForm()" class="btn_medium">初期化</button>
<c:if test="${!isUpdate}">
            <button tabindex="801" disabled="disabled">更新</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
            <button tabindex="801" onclick="registerCustomerRank()" class="btn_medium">更新</button>
    </c:if>
	<c:if test="${!editMode}">
            <button tabindex="801" onclick="registerCustomerRank()" class="btn_medium">登録</button>
    </c:if>
</c:if>
<c:if test="${!isUpdate}">
		<button tabindex="802" disabled="disabled" class="btn_medium">削除</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="802" onclick="deleteCustomerRank()" class="btn_medium">削除</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="802" disabled="disabled" class="btn_medium">削除</button>
    </c:if>
</c:if>
		</div>
	</div>
</div>

<html:hidden property="updDatetm"/>
</s:form>

</body>

</html>
