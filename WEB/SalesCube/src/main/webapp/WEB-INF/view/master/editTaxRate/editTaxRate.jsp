<%@page import="jp.co.arkinfosys.common.Constants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title><bean:message key='titles.system'/>　税マスタ管理（登録・編集）</title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
    var maxRowCount = 0;
    var trCloneBase = null;
    function init() {
        var categoryDataType = Number($("#categoryDataType").val());

        // 行追加用オブジェクト
        trCloneBase = $("#taxRateList_dummy").clone(true);

        // イベントの貼り付け
        for (var i = 0; i < maxRowCount; i++) {
            // 削除ボタン
            $("#taxRateList_" + i + "\\.deleteBtn").bind("click", {"index": i}, deleteRow);
        }
    }
    // ファンクションキーとのマッピング
    function onF1() { initForm(); }
    function onF2() { backToSearch(); }
    function onF3() { registerTaxRate(); }

    function initForm() {
    	if(confirm("<bean:message key='confirm.reset'/>")){
        	$("#editTaxRateForm").attr("action", "${f:url("/master/editTaxRate/edit")}");
        	$("#editTaxRateForm").submit();
        }
    }

    function backToSearch() {
    	if(confirm("<bean:message key='confirm.master.taxrate.back'/>")){
            location.doHref(contextRoot + "/master/searchTaxRate/");
        }
    }

    function registerTaxRate() {
    	if(confirm("<bean:message key='confirm.update'/>")){
        	$("#editTaxRateForm").attr("action", "${f:url("/master/editTaxRate/update")}");
        	$("#editTaxRateForm").submit();
        }
    }

    function deleteRow(event) {
		if(!confirm('<bean:message key="confirm.delete" />')){
	        return;
		}
        var index = event.data.index;

        // 確認メッセージは不要？
        $("#taxRateList_" + index).remove();

        // 行数を減らす
        maxRowCount--;

        // 行番号の振替
        resetNo(index);
    }

    function resetNo(index) {
        for (var i = index; i < maxRowCount; i++) {
            var nextId = i+1;

            // TRのID付け替え
            var elemTR = $("#taxRateList_" + nextId);
            elemTR.attr("id", "taxRateList_" + i);

            // 開始年月日
            var elemTd = elemTR.children(":first");
            var elemWork = elemTd.children("#taxRateList_" + nextId  + "\\.startDate");
            elemWork.attr("id", "taxRateList_" + i  + ".startDate");
            elemWork.attr("name", "taxRateList[" + i + "].startDate");
            elemWork.attr("tabindex", i*4+201);

            // 税率
            elemTd = elemTd.next();
            elemWork = elemTd.children("#taxRateList_" + nextId + "\\.taxRate");
            elemWork.attr("name", "taxRateList[" + i + "].taxRate");
            elemWork.attr("id", "taxRateList_" + i  + ".taxRate");
            elemWork.attr("tabindex", i*4+202);

            // 削除ボタンのイベント
            elemTd = elemTd.next();
            elemWork = elemTd.children("#taxRateList_" + nextId + "\\.deleteBtn");
            elemWork.attr("id", "taxRateList_" + i  + ".deleteBtn");
            elemWork.bind("click", {"index":i}, deleteRow);
            elemWork.attr("tabindex", i*4+203);
        }
    }

    // 行追加
    function addRow() {
    	var elemTr, elemTd;
    	// ベースオブジェクトからクローンを生成
    	elemTr = trCloneBase.clone(true);
    	elemTr.attr("id", "taxRateList_" + maxRowCount);
        elemTr.show();

    	// 開始日列の設定
    	elemTd = elemTr.children(":first");
    	elemWork = elemTd.children("#taxTypeCategory");
        elemWork.remove();
        elemWork = $(document.createElement('input'));
        elemWork.attr("type", "hidden");
        elemWork.attr("id", "taxRateList_" + maxRowCount + ".taxTypeCategory");
        elemWork.attr("name", "taxRateList[" + maxRowCount + "].taxTypeCategory");
        elemWork.val($("#taxTypeCategory").val());
        elemTd.append(elemWork);

    	elemWork = elemTd.children("#startDate");
        elemWork.remove();
        elemWork = $(document.createElement('input'));
        elemWork.attr("type", "text");
        elemWork.css("text-align", "center");
        elemWork.css("ime-mode", "disabled");
        elemWork.css("width", "100px");
        elemWork.attr("maxlength", "10");
        elemWork.attr("id", "taxRateList_" + maxRowCount + ".startDate");
        elemWork.attr("name", "taxRateList[" + maxRowCount + "].startDate");
        elemWork.attr("tabindex", maxRowCount*4+201);
        elemTd.append(elemWork);
		elemWork.datepicker(datePickcerSetting);

        // 税率列の設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children("#taxRate");
        elemWork.attr("id", "taxRateList_" + maxRowCount + ".taxRate");
        elemWork.attr("name", "taxRateList[" + maxRowCount + "].taxRate");
        elemWork.attr("tabindex", maxRowCount*4+202);

        // 削除ボタンの設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children("#deleteBtn");
        elemWork.attr("id", "taxRateList_" + maxRowCount + ".deleteBtn");
        elemWork.bind("click", {"index":maxRowCount}, deleteRow);
        elemWork.attr("tabindex", maxRowCount*4+203);

        // 行番号を増やす
        maxRowCount++;

        // エンターキーによるフォーカス移動
		var target = elemTr.find(":input[type!='hidden'][type!='file'][type!='reset'][type!='submit'][type!='button'][type!='image'][type!='textarea']");
		if (target.size() > 0) {
			target.bind('keypress', 'return', move_focus_to_next_tabindex );
		}

    	// 行を追加
    	$("#trAddLine").before(elemTr);

		$(".date_input").datepicker("destroy");
		$(".date_input").datepicker(datePickcerSetting);

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
	<jsp:param name="MENU_ID" value="1310"/>
</jsp:include>

<!-- メイン機能 -->


<div id="main_function">
	<span class="title">税</span>

	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm()"> F1<br>リセット</button>
		<button tabindex="2001" onclick="backToSearch()">F2<br>税一覧</button>
<c:if test="${!isUpdate}">
		<button tabindex="2002" disabled="true">F3<br>更新</button>
</c:if>
<c:if test="${isUpdate}">
    	<button tabindex="2002" onclick="registerTaxRate()">F3<br>更新</button>
</c:if>
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
	<br><br><br>

	<div class="function_forms">
<s:form styleId="editTaxRateForm" onsubmit="return false;">
<html:hidden styleId="taxTypeCategory" property="taxTypeCategory"/>
<html:hidden property="category.categoryCodeName"/>
<html:hidden property="category.categoryCode"/>

    	<div style="padding-left: 20px"><html:errors/></div>
    	<div style="padding-left: 20px;color: blue;">
        	<html:messages id="msg" message="true">
        		<bean:write name="msg" ignore="true"/><br>
        	</html:messages>
    	</div>

		<div class="form_section_wrap">
			<div class="form_section">
				<div class="section_title">
					<span>税区分情報</span>
					<button class="btn_toggle" />
				</div><!-- /.section_title -->
				<div class="section_body">
					<table class="forms" style="width:210px;" summary="税区分名">
						<colgroup>
							<col span="1" style="width: 60px">
							<col span="1" style="width:150px">
						</colgroup>

					<tr id="row_0">
						<th><div class="col_title_right">税区分</div></th>
						<td>
						<span >${f:h(category.categoryCodeName)}</span>
			            </td>
					</tr>
					</table>

				</div><!-- /.section_body -->
    		</div><!-- /.form_section -->
   		</div><!-- /.form_section_wrap -->

		<br>
		<div id="detail_info_wrap">
			<table class="detail_info"  class="forms" style="width:500px;" summary="追加更新">
				<colgroup>
					<col span="1" style="width: 200px">
					<col span="1" style="width: 200px">
					<col span="1" style="width: 100px">
				</colgroup>
				<thead>
				<tr>
					<th class="rd_top_left" style="height: 30px;">開始年月日<bean:message key='labels.must'/></th>
					<th>税率<bean:message key='labels.must'/></th>
					<th class="rd_top_right"></th>
				</tr>
				</thead>
				<tbody>
	            <tr style="display:none;" id="taxRateList_dummy">
	                <td><input type="hidden" id="taxTypeCategory">
	                	<input id="startDate" maxlength="10" type="text" style="width:100px;text-align:center;ime-mode:disabled;"></td>
	                <td><input id="taxRate" maxlength="3" type="text" style="width:90px; text-align:right; ime-mode: disabled;" class="numeral_commas scale_0 scale_half_up" >％</td>
					<td style="text-align: center">
	<c:if test="${!isUpdate}">
	                    <button class="btn_list_action"  style="width:80px;" disabled="disabled">削除</button>
	</c:if>
	<c:if test="${isUpdate}">
	                    <button class="btn_list_action"  id="deleteBtn" style="width:80px;">削除</button>
	</c:if>
	                </td>
	            </tr>
	<c:forEach var="taxRateList" varStatus="s" items="${taxRateList}">
	            <tr id="taxRateList_${s.index}">
	                <td style="background-color: #fae4eb;">
	                	<input type="hidden" name="taxRateList[${s.index}].taxTypeCategory" value="${category.categoryCode}">
	                    <html:text name="taxRateList" maxlength="10" styleId="taxRateList_${s.index}.startDate" tabindex="${s.index*4+201}" property="startDate" style="width:100px;text-align:center;ime-mode:disabled;" styleClass="date_input" indexed="true"/>
	                </td>
	                <td style="background-color: #fae4eb;">
	                    <html:text name="taxRateList" maxlength="3" styleId="taxRateList_${s.index}.taxRate" tabindex="${s.index*4+202}" styleClass="numeral_commas scale_0 scale_half_up" property="taxRate" style="width:90px; text-align:right; ime-mode: disabled;" indexed="true"/>％
	                </td>
					<td style="text-align: center;">
	<c:if test="${!isUpdate}">
	                    <button class="btn_list_action" disabled="disabled" style="width:80px;">削除</button>
	</c:if>
	<c:if test="${isUpdate}">
	                    <button class="btn_list_action" id="taxRateList_${s.index}.deleteBtn" tabindex="${s.index*4+203}" style="width:80px;">削除</button>
	</c:if>
	                </td>
	            </tr>
	<script>
	<!--
	maxRowCount = ${s.index+1};
	-->
	</script>
	</c:forEach>
				<tr id="trAddLine" >
					<td colspan="5" style="text-align:right;" class="rd_bottom_left rd_bottom_right">
	<c:if test="${!isUpdate}">
	                    <button class="btn_list_action" tabindex="1997" style="width:80px;" disabled>行追加</button>
	</c:if>
	<c:if test="${isUpdate}">
	                    <button class="btn_list_action" tabindex="1997" onclick="addRow();" style="width:80px;">行追加</button>
	</c:if>
	                </td>
				</tr>
	            </tbody>
	        </table>
	    </div>
		<div style="text-align: right; width: 500px">
			<button class="btn_medium" tabindex="1998" onclick="initForm()">リセット</button>
<c:if test="${!isUpdate}">
            <button class="btn_medium" tabindex="1999" disabled="true">更新</button>
</c:if>
<c:if test="${isUpdate}">
            <button class="btn_medium" tabindex="1999" onclick="registerTaxRate()">更新</button>
</c:if>
		</div>

<html:hidden property="updDatetm"/>
</s:form>
	</div>
</div>
</body>
</html>