<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　数量割引マスタ管理（登録・編集）</title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
    var maxRowCount = 0;
    var trCloneBase = null;
    function init() {

        // 行追加用オブジェクト
        trCloneBase = $("#discountTrnList_dummy").clone(true);

        // 削除ボタンイベントの貼り付け
        for (var i = 0; i < maxRowCount; i++) {
            // イベントの貼り付け
            $("#discountTrnList_" + i + "\\.deleteBtn").bind("click", {"index": i}, deleteRow);
        }
    }
    // ファンクションキーとのマッピング
    function onF1() { initForm(); }
    function onF2() { backToSearch(); }
    function onF3() { registerDiscount(); }
    function onF4() { deleteDiscount(); }

    function initForm() {
    	if(confirm("<bean:message key='confirm.init'/>")){
    		location.doHref(contextRoot + "/master/editDiscount/");
        }
    }

    function backToSearch() {
    	if(confirm("<bean:message key='confirm.master.discount.back'/>")){
            location.doHref(contextRoot + "/master/searchDiscount/");
        }
    }

    function registerDiscount() {
        <c:if test="${!editMode}">
    	if(confirm("<bean:message key='confirm.insert'/>")){
        	$("#editDiscountForm").attr("action", "${f:url("/master/editDiscount/insert")}");
        </c:if>
        <c:if test="${editMode}">
    	if(confirm("<bean:message key='confirm.update'/>")){
        	$("#editDiscountForm").attr("action", "${f:url("/master/editDiscount/update")}");
        </c:if>

	        _before_submit( $(".numeral_commas") );

	    	$("#editDiscountForm").trigger("submit");
        }
    }

    function deleteRow(event) {
        var index = event.data.index;

        // 確認メッセージは不要？

        if ($("#discountTrnList_" + index + "\\.discountDataId").val() != "") {
            // 削除された行の割引データＩＤを覚える
            $("#deletedDataId").val($("#deletedDataId").val() + "," + $("#discountTrnList_" + index + "\\.discountDataId").val());
        }
        $("#discountTrnList_" + index).remove();

        // 行数を減らす
        maxRowCount--;

        // 行番号の振替
        resetNo(index);
    }

    function resetNo(index) {
        for (var i = index; i < maxRowCount; i++) {
            var nextId = i+1;

            // TRのID付け替え
            var elemTR = $("#discountTrnList_" + nextId);
            elemTR.attr("id", "discountTrnList_" + i);

            // noの書き換え
            var elemTd = elemTR.children(":first");
            elemTd.attr("id", "discountTrnList_" + i + ".no");
            elemTd.html(nextId);

            // DISCOUNT_DATA_IDのhidden
            elemTd = elemTd.next();
            var elemWork = elemTd.children("#discountTrnList_" + nextId  + "\\.discountDataId");
            elemWork.attr("id", "discountTrnList_" + i  + ".discountDataId");
            elemWork.attr("name", "discountTrnList[" + i + "].discountDataId");

            // LINE_NOのhidden
            elemWork = elemTd.children("#discountTrnList_" + nextId  + "\\.lineNo");
            elemWork.attr("id", "discountTrnList_" + i  + ".lineNo");
            elemWork.attr("name", "discountTrnList[" + i + "].lineNo");
            elemWork.val(i+1);

            // 範囲FROM
            elemWork = elemTd.children("#discountTrnList_" + nextId + "\\.dataFrom");
            elemWork.attr("name", "discountTrnList[" + i + "].dataFrom");
            elemWork.attr("id", "discountTrnList_" + i  + ".dataFrom");
            // 範囲TO
            elemWork = elemTd.children("#discountTrnList_" + nextId + "\\.dataTo");
            elemWork.attr("name", "discountTrnList[" + i + "].dataTo");
            elemWork.attr("id", "discountTrnList_" + i  + ".dataTo");

            // 掛率
            elemTd = elemTd.next();
            elemWork = elemTd.children("#discountTrnList_" + nextId + "\\.discountRate");
            elemWork.attr("name", "discountTrnList[" + i + "].discountRate");
            elemWork.attr("id", "discountTrnList_" + i  + ".discountRate");

            // 削除ボタンのイベント
            elemTd = elemTd.next();
            elemWork = elemTd.children("#discountTrnList_" + nextId + "\\.deleteBtn");
            elemWork.attr("id", "discountTrnList_" + i  + ".deleteBtn");
            elemWork.bind("click", {"index": i}, deleteRow);
        }
    }

    function deleteDiscount() {
    	if(confirm("<bean:message key='confirm.delete'/>")){
        	$("#editDiscountForm").attr("action", "${f:url("/master/editDiscount/delete")}");
        	$("#editDiscountForm").trigger("submit");
        }
    }

    // 行追加
    function addRow() {
    	var elemTr, elemTd;
    	// ベースオブジェクトからクローンを生成
    	elemTr = trCloneBase.clone(true);
    	elemTr.attr("id", "discountTrnList_" + maxRowCount);
        elemTr.show();

    	// No列の設定
    	elemTd = elemTr.children(":first");
        elemTd.attr("id", "discountTrnList_" + maxRowCount + ".no");
    	elemTd.html(maxRowCount+1);

        // 数量範囲列の設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children("#dataId");
        elemWork.attr("id", "discountTrnList_" + maxRowCount + ".discountDataId");
        elemWork.attr("name", "discountTrnList[" + maxRowCount + "].discountDataId");
    	elemWork = elemTd.children("#lineNo");
        elemWork.attr("id", "discountTrnList_" + maxRowCount + ".lineNo");
        elemWork.attr("name", "discountTrnList[" + maxRowCount + "].lineNo");
        elemWork.val(maxRowCount+1);
    	elemWork = elemTd.children("#from");
        elemWork.attr("id", "discountTrnList_" + maxRowCount + ".dataFrom");
        elemWork.attr("name", "discountTrnList[" + maxRowCount + "].dataFrom");
        elemWork.bind('keypress', 'return', move_focus_to_next_tabindex );
        elemWork.attr("tabindex", maxRowCount * 4 + 200);
    	elemWork = elemTd.children("#to");
        elemWork.attr("id", "discountTrnList_" + maxRowCount + ".dataTo");
        elemWork.attr("name", "discountTrnList[" + maxRowCount + "].dataTo");
        elemWork.bind('keypress', 'return', move_focus_to_next_tabindex );
        elemWork.attr("tabindex", maxRowCount * 4 + 201);

        // 掛率列の設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children("#rate");
        elemWork.attr("id", "discountTrnList_" + maxRowCount + ".discountRate");
        elemWork.attr("name", "discountTrnList[" + maxRowCount + "].discountRate");
        elemWork.bind('keypress', 'return', move_focus_to_next_tabindex );
        elemWork.attr("tabindex", maxRowCount * 4 + 202);

        // 削除ボタンの設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children("#deleteBtn");
        elemWork.attr("id", "discountTrnList_" + maxRowCount + ".deleteBtn");
        elemWork.bind("click", {"index":maxRowCount}, deleteRow);

    	// 行を追加
    	$("#trAddLine").before(elemTr);

        // 行番号を増やす
        maxRowCount++;
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
	<jsp:param name="MENU_ID" value="1305"/>
</jsp:include>

<!-- メイン機能 -->
<s:form styleId="editDiscountForm" onsubmit="return false;">
<html:hidden styleId="deletedDataId" property="deletedDataId"/>
<input type="hidden" id="updatable" name="updatable" value="${isUpdate}">
<div id="main_function">

	<span class="title">数量割引</span>

	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm()"> F1<br>初期化</button>
		<button tabindex="2001" onclick="backToSearch()">F2<br>戻る</button>
<c:if test="${!isUpdate}">
		<button tabindex="2002" disabled="true">F3<br>更新</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="2002" onclick="registerDiscount()">F3<br>更新</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="2002" onclick="registerDiscount()">F3<br>登録</button>
    </c:if>
</c:if>
<c:if test="${!isUpdate}">
		<button tabindex="2003" disabled="true">F4<br>削除</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="2003" onclick="deleteDiscount()">F4<br>削除</button>
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
					<span>割引情報</span>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="order_section" class="section_body">
					<table id="discount_info" class="forms" summary="割引パターン情報" style="width: 600px">
						<tr>
							<th><div class="col_title_right_req">割引コード<bean:message key='labels.must'/></div></th>
							<td>
			                <c:if test="${editMode}">
			                    <html:text maxlength="20" styleId="discountId" property="discountId" style="width: 100px; ime-mode: disabled;"  tabindex="100" readonly="true" styleClass="c_disable"/>
			                </c:if>
			                <c:if test="${!editMode}">
			                    <html:text maxlength="20" styleId="discountId" property="discountId" style="width: 100px; ime-mode: disabled;"  tabindex="100"/>
			                </c:if>
			                </td>
							<th><div class="col_title_right">割引有効</div></th>
							<td><html:checkbox tabindex="101" styleId="useFlag" property="useFlag" value="1"/></td>
						</tr>
						<tr>
							<th><div class="col_title_right_req">割引名<bean:message key='labels.must'/></div></th>
							<td colspan="3"><html:text maxlength="60" styleId="discountName" property="discountName" style="width: 230px"  tabindex="102"/></td>
						</tr>
						<tr>
							<th><div class="col_title_right">備考</div></th>
							<td colspan="3"><html:text maxlength="120" styleId="remarks" property="remarks" style="width: 450px" tabindex="103"/></td>
						</tr>
					</table>
				</div><!-- /.section_body -->
			</div><!-- /.form_section -->
		</div><!-- /.form_section_wrap -->

	    <div class="form_section_wrap">
		    <div class="form_section">
		    	<div class="section_title">
					<span><bean:message key="labels.discountTrn"/></span>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="order_section" class="section_body">
					<table id="discountTrnList" class="forms detail_info" summary="<bean:message key="labels.discountTrn"/>" style="width: 600px;">
						<colgroup>
							<col span="1" style="width: 10%">
							<col span="1" style="width: 60%">
							<col span="1" style="width: 20%">
							<col span="1" style="width: 10%">
						</colgroup>
						<tr>
							<th class="rd_top_left" style="height: 30px;">No</th>
							<th class="xl64" style="height: 30px;"><bean:message key="labels.discountTrn.dataRange"/><bean:message key='labels.must'/></th>
							<th class="xl64" style="height: 30px;"><bean:message key="labels.discountTrn.discountRate"/><bean:message key='labels.must'/></th>
							<th class="rd_top_right" style="height: 30px;">&nbsp;</th>
						</tr>
						<tr id="discountTrnList_dummy" style="display:none;">
							<td id="discountTrnList_dummy" style="text-align: center">0
			                </td>
							<td style="text-align: center; background-color: #fae4eb;">
			                    <input type="hidden" id="dataId">
			                    <input type="hidden" id="lineNo">
								<input type="text" maxlength="13" id="from" style="width:120px;ime-mode:disabled;" class="numeral_commas style_quantity" indexed="true" />
								～
								<input type="text" maxlength="13" id="to" style="width:120px;ime-mode:disabled;" class="numeral_commas style_quantity" indexed="true" />
							<td style="text-align: center; background-color: #fae4eb;">
								<input type="text" maxlength="7" id="rate" style="width:80px;ime-mode:disabled;text-align:right;" indexed="true" /> ％
			                </td>
							<td style="text-align: center">
			                    <button id="deleteBtn" class="btn_list_action">削除</button>
			                </td>
						</tr>
			            <c:forEach var="discountTrnList" varStatus="s" items="${discountTrnList}">
						<tr id="discountTrnList_${s.index}">
							<td id="discountTrnList_${s.index}.no" style="text-align: center">${s.index+1}
			                </td>
							<td style="text-align: center; background-color: #fae4eb;">
			                    <html:hidden styleId="discountTrnList_${s.index}.discountDataId" name="discountTrnList" property="discountDataId" indexed="true" />
			                    <html:hidden styleId="discountTrnList_${s.index}.lineNo" name="discountTrnList" property="lineNo" indexed="true"/>
								<html:text maxlength="13" styleId="discountTrnList_${s.index}.dataFrom" name="discountTrnList" property="dataFrom" style="width: 120px;ime-mode:disabled;" tabindex="${s.index*4+200}" indexed="true" styleClass="numeral_commas style_quantity" />
								～
								<html:text maxlength="13" styleId="discountTrnList_${s.index}.dataTo" name="discountTrnList" property="dataTo" style="width: 120px;ime-mode:disabled;" tabindex="${s.index*4+201}" indexed="true" styleClass="numeral_commas style_quantity" />
							<td style="text-align: center; background-color: #fae4eb;">
								<html:text maxlength="7" styleId="discountTrnList_${s.index}.discountRate" name="discountTrnList" property="discountRate" style="width: 80px;ime-mode:disabled;text-align:right;" tabindex="${s.index*4+202}" indexed="true"/> ％
			                </td>
							<td style="text-align: center">
			<c:if test="${!isUpdate}">
			                    <button disabled="disabled" class="btn_list_action">削除</button>
			</c:if>
			<c:if test="${isUpdate}">
			                    <button id="discountTrnList_${s.index}.deleteBtn" tabindex="${s.index*4+203}" class="btn_list_action">削除</button>
			</c:if>
			                </td>
						</tr>
			            <script type="text/javascript">
			            <!--
			            maxRowCount++;
			            -->
			            </script>
			            </c:forEach>
						<tr id="trAddLine">
							<td style="text-align: right" colspan="4"><button onclick="addRow()" class="btn_list_action">行追加</button></td>
						</tr>
					</table>
				</div><!-- /.section_body -->
			</div><!-- /.form_section -->
		</div><!-- /.form_section_wrap -->

		<div style="text-align: right; width: 1160px">
			<span>登録日：${creDatetmShow}　更新日:${updDatetmShow}　</span>
<html:hidden property="creDatetm"/>
<html:hidden property="creDatetmShow"/>
<html:hidden property="updDatetm"/>
<html:hidden property="updDatetmShow"/>
			<button tabindex="301" onclick="initForm()" class="btn_medium">初期化</button>
<c:if test="${!isUpdate}">
            <button tabindex="302" disabled="true" class="btn_medium">更新</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
            <button tabindex="302" onclick="registerDiscount()" class="btn_medium">更新</button>
    </c:if>
	<c:if test="${!editMode}">
            <button tabindex="302" onclick="registerDiscount()" class="btn_medium">登録</button>
    </c:if>
</c:if>
<c:if test="${!isUpdate}">
		<button tabindex="303" disabled="true" class="btn_medium">削除</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="303" onclick="deleteDiscount()" class="btn_medium">削除</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="303" disabled="true" class="btn_medium">削除</button>
    </c:if>
</c:if>
		</div>

	</div>
</div>
</s:form>
</body>

</html>
