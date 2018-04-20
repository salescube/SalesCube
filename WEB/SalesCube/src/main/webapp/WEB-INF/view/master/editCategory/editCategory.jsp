<%@page import="jp.co.arkinfosys.common.Constants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　区分マスタ管理（編集）</title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
    var maxRowCount = 0;
    var trCloneBase = null;
    function init() {
        var categoryDataType = Number($("#categoryDataType").val());

        // 行追加用オブジェクト
        trCloneBase = $("#categoryTrnList_dummy").clone(true);

        // イベントの貼り付け
        for (var i = 0; i < maxRowCount; i++) {
            $("#categoryTrnList_" + i + "\\.categoryCode").bind("change", {"id": "categoryTrnList_" + i}, valueChange);
            $("#categoryTrnList_" + i + "\\.categoryCodeName").bind("change", {"id": "categoryTrnList_" + i}, valueChange);
            if (categoryDataType == 1) {
                // 文字列の設定
                $("#categoryTrnList_" + i + "\\.categoryStr").bind("change", {"id": "categoryTrnList_" + i}, valueChange);
            } else if (categoryDataType == 2) {
                // 整数値の設定
                $("#categoryTrnList_" + i + "\\.categoryNum").bind("change", {"id": "categoryTrnList_" + i}, valueChange);
            } else if (categoryDataType == 3) {
                // 小数値の設定
                $("#categoryTrnList_" + i + "\\.categoryFlt").bind("change", {"id": "categoryTrnList_" + i}, valueChange);
            } else if (categoryDataType == 4) {
                // 論理値の設定
                $("#categoryTrnList_" + i + "\\.categoryBool").bind("change", {"id": "categoryTrnList_" + i}, valueChange);
            }
            // 削除ボタン
            $("#categoryTrnList_" + i + "\\.deleteBtn").bind("click", {"index": i}, deleteRow);

            // 削除ボタンの有効無効切替
            setDeleteButtonDisabled();
        }
    }
    // ファンクションキーとのマッピング
    function onF1() { initForm(); }
    function onF2() { backToSearch(); }
    function onF3() { registerCategory(); }

    function initForm() {
    	if(confirm("<bean:message key='confirm.reset'/>")){
        	$("#editCategoryForm").attr("action", "${f:url("/master/editCategory/edit")}");
        	$("#editCategoryForm").submit();
        }
    }

    function backToSearch() {
    	if(confirm("<bean:message key='confirm.master.category.back'/>")){
            location.doHref(contextRoot + "/master/searchCategory/");
        }
    }

    function registerCategory() {
    	if(confirm("<bean:message key='confirm.update'/>")){
        	$("#editCategoryForm").attr("action", "${f:url("/master/editCategory/update")}");
        	$("#editCategoryForm").submit();
        }
    }

    function deleteRow(event) {
        var index = event.data.index;

        // 確認メッセージは不要？
        var elemTr = $("#deletedRow").clone(true);
        $("#categoryTrnList_" + index).after(elemTr);
        elemTr.show();
        $("#categoryTrnList_" + index).remove();

        // 行数を減らす
        maxRowCount--;

        // 行番号の振替
        resetNo(index);

        // 削除ボタンの有効無効切替
        setDeleteButtonDisabled();
    }

    function setDeleteButtonDisabled() {
        var categoryDel = Number($("#categoryDel").val());
        if (categoryDel != 1) {
            return;
        }
        // 削除可能属性が「１：1行を残して可能」の場合は、残り1行の時は削除ボタンを押せなくする
        if (maxRowCount == 1) {
            $("#categoryTrnList_0\\.deleteBtn").attr("disabled", true);
        } else {
            $("#categoryTrnList_0\\.deleteBtn").removeAttr("disabled");
        }
    }

    function valueChange(event) {
        var index = event.data.id;
        // マークをつける
        $("#"+index + "\\.mark").html = "*";

        // 区分コードの調整
        var id = "#"+index + "\\.categoryCode";
        $(id).val(zeroPadding($(id).val()));
    }

    function zeroPadding(value) {
        var result = value;
        var categoryCodeSize = $("#categoryCodeSize").val();
        if (categoryCodeSize == 0) {
            return value;
        }
        for (var i = 0; i < categoryCodeSize; i++) {
            result = "0" + result;
        }
        result = result.substring(result.length - categoryCodeSize);
        return result;
    }

    function resetNo(index) {
        for (var i = index; i < maxRowCount; i++) {
            var nextId = i+1;

            // TRのID付け替え
            var elemTR = $("#categoryTrnList_" + nextId);
            elemTR.attr("id", "categoryTrnList_" + i);

            // markの書き換え
            var elemTd = elemTR.children(":first");
            elemTd.attr("id", "categoryTrnList_" + i + ".mark");

            elemTd = elemTd.next();

            // 区分コード
            var elemWork = elemTd.children("#categoryTrnList_" + nextId  + "\\.categoryCode");
            elemWork.attr("id", "categoryTrnList_" + i  + ".categoryCode");
            elemWork.attr("name", "categoryTrnList[" + i + "].categoryCode");

            // 区分コード名
            elemTd = elemTd.next();
            elemWork = elemTd.children("#categoryTrnList_" + nextId + "\\.categoryCodeName");
            elemWork.attr("name", "categoryTrnList[" + i + "].categoryCodeName");
            elemWork.attr("id", "categoryTrnList_" + i  + ".categoryCodeName");

            var categoryDataType = Number($("#categoryDataType").val());

            // 文字列の設定
            elemTd = elemTd.next();
            elemWork = elemTd.children("#categoryTrnList_" + nextId + "\\.categoryStr");
            elemWork.attr("name", "categoryTrnList[" + i + "].categoryStr");
            elemWork.attr("id", "categoryTrnList_" + i  + ".categoryStr");

            if (categoryDataType == 2) {
                // 整数値の設定
                elemWork = elemTd.children("#categoryTrnList_" + nextId + "\\.categoryNum");
                elemWork.attr("name", "categoryTrnList[" + i + "].categoryNum");
                elemWork.attr("id", "categoryTrnList_" + i  + ".categoryNum");
            } else if (categoryDataType == 3) {
                // 小数値の設定
                elemWork = elemTd.children("#categoryTrnList_" + nextId + "\\.categoryFlt");
                elemWork.attr("name", "categoryTrnList[" + i + "].categoryFlt");
                elemWork.attr("id", "categoryTrnList_" + i  + ".categoryFlt");
            } else if (categoryDataType == 4) {
                // 論理値の設定
                elemWork = elemTd.children("#categoryTrnList_" + nextId + "\\.categoryBool");
                elemWork.attr("name", "categoryTrnList[" + i + "].categoryBool");
                elemWork.attr("id", "categoryTrnList_" + i  + ".categoryBool");

                if ($("#categoryUpd").val() == 0) {
                    elemWork = elemTd.children("#categoryTrnList_" + nextId + "\\.categoryBool_hidden");
                    if (elemWork && elemWork.get(0)) {
                        elemWork.attr("name", "categoryTrnList[" + i + "].categoryBool");
                        elemWork.attr("id", "categoryTrnList_" + i  + ".categoryBool_hidden");
                    }
                }
            }

            // 削除ボタンのイベント
            elemTd = elemTd.next();
            elemWork = elemTd.children("#categoryTrnList_" + nextId + "\\.deleteBtn");
            elemWork.attr("id", "categoryTrnList_" + i  + ".deleteBtn");
            elemWork.bind("click", {"index": i}, deleteRow);

            // 表示フラグ
            elemWork = elemTd.children("#categoryTrnList_" + nextId + "\\.categoryDsp");
            elemWork.attr("name", "categoryTrnList[" + i + "].categoryDsp");
            elemWork.attr("id", "categoryTrnList_" + i  + ".categoryDsp");
        }
    }

    // 行追加
    function addRow() {
    	var elemTr, elemTd;
    	// ベースオブジェクトからクローンを生成
    	elemTr = trCloneBase.clone(true);
    	elemTr.attr("id", "categoryTrnList_" + maxRowCount);
        elemTr.show();

    	// mark列の設定
    	elemTd = elemTr.children(":first");
        elemTd.attr("id", "categoryTrnList_" + maxRowCount + ".mark");
        elemTd.html("*");

        // 区分コード列の設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children("#categoryCode");
        elemWork.attr("id", "categoryTrnList_" + maxRowCount + ".categoryCode");
        elemWork.attr("name", "categoryTrnList[" + maxRowCount + "].categoryCode");
        elemWork.bind("change", {"id": "categoryTrnList_" + maxRowCount}, valueChange);

        // 区分コード名列の設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children("#categoryCodeName");
        elemWork.attr("id", "categoryTrnList_" + maxRowCount + ".categoryCodeName");
        elemWork.attr("name", "categoryTrnList[" + maxRowCount + "].categoryCodeName");
        elemWork.bind("change", {"id": "categoryTrnList_" + maxRowCount}, valueChange);

    	elemTd = elemTd.next();
        var categoryDataType = Number($("#categoryDataType").val());

        // 文字列の設定
       	elemWork = elemTd.children("#categoryStr");
        elemWork.attr("id", "categoryTrnList_" + maxRowCount + ".categoryStr");
        elemWork.attr("name", "categoryTrnList[" + maxRowCount + "].categoryStr");
        elemWork.bind("change", {"id": "categoryTrnList_" + maxRowCount}, valueChange);

        if (categoryDataType == 2) {
            // 整数値の設定
        	elemWork = elemTd.children("#categoryNum");
            elemWork.attr("id", "categoryTrnList_" + maxRowCount + ".categoryNum");
            elemWork.attr("name", "categoryTrnList[" + maxRowCount + "].categoryNum");
            elemWork.bind("change", {"id": "categoryTrnList_" + maxRowCount}, valueChange);
        } else if (categoryDataType == 3) {
            // 小数値の設定
        	elemWork = elemTd.children("#categoryFlt");
            elemWork.attr("id", "categoryTrnList_" + maxRowCount + ".categoryFlt");
            elemWork.attr("name", "categoryTrnList[" + maxRowCount + "].categoryFlt");
            elemWork.bind("change", {"id": "categoryTrnList_" + maxRowCount}, valueChange);
        } else if (categoryDataType == 4) {
            // 論理値の設定
        	elemWork = elemTd.children("#categoryBool");
            elemWork.attr("id", "categoryTrnList_" + maxRowCount + ".categoryBool");
            elemWork.attr("name", "categoryTrnList[" + maxRowCount + "].categoryBool");
            elemWork.bind("change", {"id": "categoryTrnList_" + maxRowCount}, valueChange);
        }

        // 削除ボタンの設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children("#deleteBtn");
        elemWork.attr("id", "categoryTrnList_" + maxRowCount + ".deleteBtn");
        elemWork.bind("click", {"index":maxRowCount}, deleteRow);

        // 表示フラグの設定
    	elemWork = elemTd.children("#categoryDsp");
        elemWork.attr("id", "categoryTrnList_" + maxRowCount + ".categoryDsp");
        elemWork.attr("name", "categoryTrnList[" + maxRowCount + "].categoryDsp");

        // 行番号を増やす
        maxRowCount++;

    	// 行を追加
    	$("#trAddLine").before(elemTr);

        // 削除ボタンの有効無効切替
        setDeleteButtonDisabled();
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
	<jsp:param name="MENU_ID" value="1309"/>
</jsp:include>

<!-- メイン機能 -->

<s:form styleId="editCategoryForm" onsubmit="return false;">
<html:hidden styleId="deletedDataId" property="deletedDataId"/>
<html:hidden styleId="categoryId" property="categoryId"/>
<html:hidden property="category.categoryName"/>
<html:hidden property="category.categoryId"/>
<html:hidden styleId="categoryDataType" property="category.categoryDataType"/>
<html:hidden styleId="categoryAdd" property="category.categoryAdd"/>
<html:hidden styleId="categoryUpd" property="category.categoryUpd"/>
<html:hidden styleId="categoryDel" property="category.categoryDel"/>
<html:hidden styleId="categoryCodeSize" property="category.categoryCodeSize"/>
<html:hidden styleId="categoryStrSize" property="category.categoryStrSize"/>
<html:hidden styleId="categoryTitle" property="category.categoryTitle"/>

<div id="main_function">

	<span class="title">区分</span>

	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm()"> F1<br>リセット</button>
		<button tabindex="2001" onclick="backToSearch()">F2<br>区分一覧</button>
<c:if test="${!isUpdate}">
		<button tabindex="2002" disabled="true">F3<br>更新</button>
</c:if>
<c:if test="${isUpdate}">
    	<button tabindex="2002" onclick="registerCategory()">F3<br>更新</button>
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
    	<div style="padding-left: 20px"><html:errors/></div>
    	<div style="padding-left: 20px;color: blue;">
        	<html:messages id="msg" message="true">
        		<bean:write name="msg" ignore="true"/><br>
        	</html:messages>
    	</div>

		<div class="form_section_wrap">
			<div class="form_section">
				<div class="section_title">
					<span>区分情報</span>
					<button class="btn_toggle" />
				</div><!-- /.section_title -->
			<div class="section_body">
				<table class="forms" style="width:210px;" summary="対象区分名">
					<colgroup>
						<col span="1" style="width: 60px">
						<col span="1" style="width:150px">
					</colgroup>

				<tr id="row_0">
					<th><div class="col_title_right">区分名</div></th>
					<td>
		                <span >${f:h(category.categoryName)}</span>
		            </td>
				</tr>
				</table>
			</div><!-- /.section_body -->
    		</div><!-- /.form_section -->
   		</div><!-- /.form_section_wrap -->

	     <br>
		<div id="detail_info_wrap">
			<table class="detail_info"  id="edit_table" class="forms" style="width:800px;" summary="追加更新">
				<colgroup>
					<col span="1" style="width: 50px">
					<col span="1" style="width:200px">
					<col span="1" style="width:250px">
					<col span="1" style="width:300px">
					<col span="1" style="">
				</colgroup>
	            <tr style="display:none;" id="deletedRow">
	                <td style="text-align:center;">*</td>
	                <td colspan="4">削除されました</td>
	            </tr>
	            <tr style="display:none;" id="categoryTrnList_dummy">
	                <td style="text-align:center;"></td>
	                <td style="background-color: #fae4eb;"><input id="categoryCode" maxlength="${category.categoryCodeSize}" type="text" style="width:175px;"></td>
	                <td style="background-color: #fae4eb;"><input id="categoryCodeName" type="text" style="width:175px;"></td>
	                <td>
	<c:if test='${category.categoryUpd == 0}'>
	                <input type="text" readonly="true" class="c_disable" style="width:175px;">
	                <input type="hidden" id="categoryStr" value="">
	</c:if>
	<c:if test="${category.categoryDataType == 0}">
	                <input type="text" readonly="true" class="c_disable" style="width:175px;">
	                <input type="hidden" id="categoryStr" value="">
	</c:if>
	<c:if test="${category.categoryDataType == 1}">
	                    <input type="text" maxlength="${category.categoryStrSize}" id="categoryStr" style="width:175px;">
	</c:if>
	<c:if test="${category.categoryDataType == 2}">
	                    <input type="text" id="categoryNum" style="width:175px;">
	                    <input type="hidden" id="categoryStr" value="">
	</c:if>
	<c:if test="${category.categoryDataType == 3}">
	                    <input type="text" id="categoryFlt" style="width:175px;">
	                    <input type="hidden" id="categoryStr" value="">
	</c:if>
	<c:if test="${category.categoryDataType == 4}">
	                    <input type="checkbox" id="categoryBool"/>${f:h(category.categoryTitle)}
	                    <input type="hidden" id="categoryStr" value="">
	</c:if>
	                </td>
					<td style="text-align: center">
	<c:if test="${!isUpdate}">
	                    <button class="btn_list_action" disabled="disabled">削除</button>
	</c:if>
	<c:if test="${isUpdate}">
	                    <button class="btn_list_action"  id="deleteBtn" >削除</button>
	</c:if>
						<input type="hidden" id="categoryDsp" name="categoryDsp" value="1"/>
	            </tr>
				<thead>
				<tr>
					<th class="rd_top_left" style="height: 30px;">変更</th>
					<th>区分コード<bean:message key='labels.must'/></th>
					<th>区分コード名<bean:message key='labels.must'/></th>
	                <th>
	<c:if test="${category.categoryDataType == 0}">
					(値なし)
	</c:if>
	<c:if test="${category.categoryDataType == 1}">
					${f:h(category.categoryTitle)}(文字列)<bean:message key='labels.must'/>
	</c:if>
	<c:if test="${category.categoryDataType == 2}">
					${f:h(category.categoryTitle)}(整数)<bean:message key='labels.must'/>
	</c:if>
	<c:if test="${category.categoryDataType == 3}">
					${f:h(category.categoryTitle)}(小数)<bean:message key='labels.must'/>
	</c:if>
	<c:if test="${category.categoryDataType == 4}">
					${f:h(category.categoryTitle)}(論理)
	</c:if>
	                </th>
					<th class="rd_top_right"></th>
				</tr>
				</thead>
				<tbody>
	<c:forEach var="categoryTrnList" varStatus="s" items="${categoryTrnList}">
	            <tr id="categoryTrnList_${s.index}">
	                <td id="categoryTrnList_${s.index}.mark" style="text-align:center;">&nbsp;</td>
	                <td style="background-color: #fae4eb;">
	    <c:if test='${category.categoryUpd == 0}'>
	                    <html:text name="categoryTrnList" maxlength="${category.categoryCodeSize}" readonly="true" styleClass="c_disable" styleId="categoryTrnList_${s.index}.categoryCode" property="categoryCode" style="width:175px;" indexed="true"/>
	    </c:if>
	    <c:if test='${category.categoryUpd == 1}'>
	                    <html:text name="categoryTrnList" maxlength="${category.categoryCodeSize}" styleId="categoryTrnList_${s.index}.categoryCode" property="categoryCode" style="width:175px;" indexed="true"/>
	    </c:if>
	                </td>
	                <td style="background-color: #fae4eb;">
	    <c:if test='${category.categoryUpd == 0}'>
	                    <html:text name="categoryTrnList" maxlength="60" readonly="true" styleClass="c_disable" styleId="categoryTrnList_${s.index}.categoryCodeName" property="categoryCodeName" style="width:175px;" indexed="true"/>
	    </c:if>
	    <c:if test='${category.categoryUpd == 1}'>
	                    <html:text name="categoryTrnList" maxlength="60" styleId="categoryTrnList_${s.index}.categoryCodeName" property="categoryCodeName" style="width:175px;" indexed="true"/>
	    </c:if>
	                </td>
	                <td>
	<c:if test="${category.categoryDataType == 0}">
	                <input type="text" readonly="true" class="c_disable" style="width:175px;">
	                <html:hidden name="categoryTrnList" styleId="categoryTrnList_${s.index}.categoryStr" property="categoryStr" indexed="true"/>
	</c:if>
	<c:if test="${category.categoryDataType == 1}">
	    <c:if test='${category.categoryUpd == 0}'>
	                    <html:text name="categoryTrnList" maxlength="${category.categoryStrSize}" styleId="categoryTrnList_${s.index}.categoryStr" property="categoryStr" style="width:175px;" readonly="true" styleClass="c_disable" indexed="true"/>
	    </c:if>
	    <c:if test='${category.categoryUpd == 1}'>
	                    <html:text name="categoryTrnList" maxlength="${category.categoryStrSize}" styleId="categoryTrnList_${s.index}.categoryStr" property="categoryStr" style="width:175px;" indexed="true"/>
	    </c:if>
	</c:if>
	<c:if test="${category.categoryDataType == 2}">
	    <c:if test='${category.categoryUpd == 0}'>
	                    <html:text name="categoryTrnList" maxlength="10" styleId="categoryTrnList_${s.index}.categoryNum" property="categoryNum" style="width:175px;" readonly="true" styleClass="c_disable" indexed="true"/>
	    </c:if>
	    <c:if test='${category.categoryUpd == 1}'>
	                    <html:text name="categoryTrnList" maxlength="10" styleId="categoryTrnList_${s.index}.categoryNum" property="categoryNum" style="width:175px;" indexed="true"/>
	    </c:if>
	    <html:hidden name="categoryTrnList" styleId="categoryTrnList_${s.index}.categoryStr" property="categoryStr" indexed="true"/>
	</c:if>
	<c:if test="${category.categoryDataType == 3}">
	    <c:if test='${category.categoryUpd == 0}'>
	                    <html:text name="categoryTrnList" maxlength="16" styleId="categoryTrnList_${s.index}.categoryFlt" property="categoryFlt" style="width:175px;" readonly="true" styleClass="c_disable" indexed="true"/>
	    </c:if>
	    <c:if test='${category.categoryUpd == 1}'>
	                    <html:text name="categoryTrnList" maxlength="16" styleId="categoryTrnList_${s.index}.categoryFlt" property="categoryFlt" style="width:175px;" indexed="true"/>
	    </c:if>
	    <html:hidden name="categoryTrnList" styleId="categoryTrnList_${s.index}.categoryStr" property="categoryStr" indexed="true"/>
	</c:if>
	<c:if test="${category.categoryDataType == 4}">
	    <c:if test='${category.categoryUpd == 0}'>
	                    <html:checkbox name="categoryTrnList" styleId="categoryTrnList_${s.index}.categoryBool" property="categoryBool" disabled="true" value="<%=Constants.FLAG.ON%>" indexed="true"/>
	                    <html:hidden name="categoryTrnList" styleId="categoryTrnList_${s.index}.categoryBool_hidden" property="categoryBool" indexed="true"/>
	    </c:if>
	    <c:if test='${category.categoryUpd == 1}'>
	                    <html:checkbox name="categoryTrnList" styleId="categoryTrnList_${s.index}.categoryBool" property="categoryBool" value="<%=Constants.FLAG.ON%>" indexed="true"/>
	    </c:if>
	    <html:hidden name="categoryTrnList" styleId="categoryTrnList_${s.index}.categoryStr" property="categoryStr" indexed="true"/>
	</c:if>
	                </td>
					<td style="text-align: center">
	<c:if test="${!isUpdate}">
	                    <button class="btn_list_action"  disabled="disabled">削除</button>
	</c:if>
	<c:if test="${isUpdate}">
	<c:if test='${category.categoryDel == 0}'>
	                    <button class="btn_list_action"  disabled="disabled">削除</button>
	</c:if>
	<c:if test="${category.categoryDel == 1}">
	                    <button class="btn_list_action"  id="categoryTrnList_${s.index}.deleteBtn" tabindex="${s.index*4+203}">削除</button>
	</c:if>
	<c:if test="${category.categoryDel == 2}">
	                    <button class="btn_list_action"  id="categoryTrnList_${s.index}.deleteBtn" tabindex="${s.index*4+203}">削除</button>
	</c:if>
	</c:if>
						<html:hidden name="categoryTrnList" styleId="categoryTrnList_${s.index}.categoryDsp" property="categoryDsp" indexed="true"/>
	                </td>
	            </tr>
	<script>
	<!--
	maxRowCount = ${s.index+1};
	-->
	</script>

	</c:forEach>
				<tr id="trAddLine">
					<td colspan="5" style="text-align:right;">
	<c:if test='${category.categoryAdd == 0}'>
	                    <button class="btn_list_action"  disabled="disabled" style="width:80px;">行追加</button>
	</c:if>
	<c:if test='${category.categoryAdd == 1}'>
	                    <button class="btn_list_action"  onclick="addRow();" style="width:80px;">行追加</button>
	</c:if>
	                </td>
				</tr>
	            </tbody>
	        </table>
	     </div>
		<div style="text-align: right; width: 800px">
			<span>登録日：${creDatetmShow}　更新日:${updDatetmShow}　</span>
			<button class="btn_medium" tabindex="206" onclick="initForm()">リセット</button>
<c:if test="${!isUpdate}">
            <button class="btn_medium" tabindex="207" disabled="true">更新</button>
</c:if>
<c:if test="${isUpdate}">
            <button class="btn_medium" tabindex="207" onclick="registerCategory()">更新</button>
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