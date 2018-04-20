<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　レートマスタ管理（登録・編集）</title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
    var maxRowCount = 0;
    var trCloneBase = null;
    function init() {
<c:if test="${editMode}">
        // 行追加用オブジェクト
        trCloneBase = $("#rateTrnList_dummy").clone(true);

        // 行数
        maxRowCount = ${rateTrnListSize};

        // 削除ボタンイベントの貼り付け
        for (var i = 0; i < maxRowCount; i++) {
            // イベントの貼り付け
            $("#rateTrnList_" + i + "\\.deleteBtn").bind("click", {"index": i}, deleteRow);
        }
</c:if>
    }

    // ファンクションキーとのマッピング
    function onF1() { initForm(); }
    function onF2() { backToSearch(); }
    function onF3() { registerRate(); }
    function onF4() { deleteRate(); }

    function initForm() {
    	if(confirm("<bean:message key='confirm.init'/>")){
            location.doHref(contextRoot + "/master/editRate/");
        }
    }

    function backToSearch() {
    	if(confirm("<bean:message key='confirm.master.rate.back'/>")){
            location.doHref(contextRoot + "/master/searchRate/");
        }
    }

    function registerRate() {
        <c:if test="${!editMode}">
    	if(confirm("<bean:message key='confirm.insert'/>")){
        	$("#editRateForm").attr("action", "${f:url("/master/editRate/insert")}");
        </c:if>

        $("#rateTrnListSize").val($("#rateTrnListSizeLabel").html());

        <c:if test="${editMode}">
    	if(confirm("<bean:message key='confirm.update'/>")){
        	$("#editRateForm").attr("action", "${f:url("/master/editRate/update")}");
        </c:if>
    	$("#editRateForm").trigger("submit");
        }
    }

    function deleteRow(event) {
        var index = event.data.index;

        // 確認メッセージは不要？
        if ($("#rateTrnList_" + index + "_rateId").val() != "") {
            // 削除された行の割引データＩＤを覚える
            $("#deletedRateId").val($("#deletedRateId").val() + "," + $("#rateTrnList_" + index + "_rateId").val() + "-" + $("#rateTrnList_" + index + "_startDate").val());
        }
        $("#rateTrnList_" + index).remove();

        // 行数を減らす
        maxRowCount--;

        // 行番号の振替
        resetNoForDel(index);

        $("#rateTrnListSizeLabel").html(maxRowCount);
    }

    function resetNoForDel(index) {

        // 削除された行番号の次から、１ずつ減らす
        for (var i = index; i < maxRowCount; i++) {
            resetNo(i+1, i);
        }
    }

    function resetNoForAdd() {
        // 先頭行に追加されるので、全体的に１つずらす
        // 逆回しじゃないとダメ
        for (var i = maxRowCount-1; i >=0; i--) {
            resetNo(i, i+1);
        }
    }

    function resetNo(from, to) {
		var tabindex = 300 + (4 * to);

        // TRのID付け替え
        var elemTR = $("#rateTrnList_" + from);
        elemTR.attr("id", "rateTrnList_" + to);

        // RATE_ID
        var elemTd = elemTR.children(":first");
        elemWork = elemTd.children("#rateTrnList_" + from + "_rateId");
        elemWork.attr("id", "rateTrnList_" + to + "_rateId");
        elemWork.attr("name", "rateTrnList[" + to + "].rateId");

        // 適用開始日
        elemWork = elemTd.children("#rateTrnList_" + from + "_startDate");
        elemWork.attr("id", "rateTrnList_" + to + "_startDate");
        elemWork.attr("name", "rateTrnList[" + to + "].startDate");
        elemWork.attr("tabindex", tabindex++);

        // レート
        elemTd = elemTd.next();
        elemWork = elemTd.children("#rateTrnList_" + from  + "_rate");
        elemWork.attr("id", "rateTrnList_" + to  + "_rate");
        elemWork.attr("name", "rateTrnList[" + to + "].rate");
        elemWork.attr("tabindex", tabindex++);

        // レートデータ備考
        elemTd = elemTd.next();
        elemWork = elemTd.children("#rateTrnList_" + from + "_remarks");
        elemWork.attr("id", "rateTrnList_" + to  + "_remarks");
        elemWork.attr("name", "rateTrnList[" + to + "].remarks");
        elemWork.attr("tabindex", tabindex++);

        // 削除ボタンのイベント
        elemTd = elemTd.next();
        elemWork = elemTd.children("#rateTrnList_" + from + "_deleteBtn");
        elemWork.attr("id", "rateTrnList_" + to  + "_deleteBtn");
        elemWork.bind("click", {"index": to}, deleteRow);
        elemWork.attr("tabindex", tabindex++);
    }
    function deleteRate() {
    	if(confirm("<bean:message key='confirm.delete'/>")){
        	$("#editRateForm").attr("action", "${f:url("/master/editRate/delete")}");
        	$("#editRateForm").trigger("submit");
        }
    }

    // 行追加
    function addRow() {
    	var elemTr, elemTd, elemWork;

        // 先に番号をずらす
        resetNoForAdd();

        // ０番で追加
    	// ベースオブジェクトからクローンを生成
    	elemTr = trCloneBase.clone(true);
    	elemTr.attr("id", "rateTrnList_0");
        elemTr.show();

    	// 適用開始日列の設定
    	elemTd = elemTr.children(":first");
        elemWork = elemTd.children("#rateId_dummy");
        elemWork.attr("id", "rateTrnList_0_rateId");
        elemWork.attr("name", "rateTrnList[0].rateId");

        elemWork = elemTd.children("#startDate");
        elemWork.remove();
        elemWork = $(document.createElement('input'));
        elemWork.attr("type", "text");
        elemWork.css("text-align", "center");
        elemWork.css("ime-mode", "disabled");
        elemWork.css("width", "90px");
        elemWork.attr("maxlength", "10");
        elemWork.attr("id", "rateTrnList_0_startDate");
        elemWork.attr("name", "rateTrnList[0].startDate");
        elemWork.val($("#add_startDate").val());
        $("#add_startDate").val("");
        elemWork.addClass("date_input");
        elemWork.attr("tabindex", 300);
        elemTd.append(elemWork);

        // レート列の設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children("#rate");
        elemWork.attr("id", "rateTrnList_0_rate");
        elemWork.attr("name", "rateTrnList[0].rate");
        elemWork.val($("#add_rate").val());
        elemWork.attr("tabindex", 301);
        $("#add_rate").val("");

        // レートデータ備考列の設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children("#remarks");
        elemWork.attr("id", "rateTrnList_0_remarks");
        elemWork.attr("name", "rateTrnList[0].remarks");
        elemWork.val($("#add_remarks").val());
        elemWork.attr("tabindex", 302);
        $("#add_remarks").val("");

        // 削除ボタンの設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children("#deleteBtn");
        elemWork.attr("id", "rateTrnList_0_deleteBtn");
        elemWork.bind("click", {"index": 0}, deleteRow);
        elemWork.attr("tabindex", 303);

        // エンターキーによるフォーカス移動
		var target = elemTr.find(":input[type!='hidden'][type!='file'][type!='reset'][type!='submit'][type!='button'][type!='image'][type!='textarea']");
		if (target.size() > 0) {
			target.bind('keypress', 'return', move_focus_to_next_tabindex );
		}

    	// 行を追加
    	$("#rateTrnList_dummy").after(elemTr);

		$(".date_input").datepicker("destroy");
		$(".date_input").datepicker(datePickcerSetting);

        // 行番号を増やす
        maxRowCount++;

        $("#rateTrnListSizeLabel").html(maxRowCount);
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
	<jsp:param name="MENU_ID" value="1313"/>
</jsp:include>

<!-- メイン機能 -->
<s:form styleId="editRateForm" onsubmit="return false;">
<html:hidden styleId="deletedRateId" property="deletedRateId"/>
<input type="hidden" id="updatable" name="updatable" value="${isUpdate}">
<div id="main_function">

	<span class="title">レート</span>

	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm()"> F1<br>初期化</button>
		<button tabindex="2001" onclick="backToSearch()">F2<br>戻る</button>
<c:if test="${!isUpdate}">
		<button tabindex="2002" disabled="disabled">F3<br>更新</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="2002" onclick="registerRate()">F3<br>更新</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="2002" onclick="registerRate()">F3<br>登録</button>
    </c:if>
</c:if>
<c:if test="${!isUpdate}">
		<button tabindex="2003" disabled="disabled">F4<br>&nbsp;</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="2003" onclick="deleteRate()">F4<br>削除</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="2003" disabled="disabled">F4<br>&nbsp;</button>
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
					<span>レートタイプ情報</span>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="order_section" class="section_body">
					<table id="rate_info" class="forms" summary="レートタイプ情報">
						<tr>
							<th><div class="col_title_right">レートタイプID</div></th>
							<td>&nbsp;${rateId}<html:hidden styleId="rateId" property="rateId"/></td>
							<th><div class="col_title_right_req">レートタイプ名称<bean:message key='labels.must'/></div></th>
							<td>
								<html:text maxlength="60" styleId="name" property="name" style="width: 235px;" tabindex="100"/>
							</td>
			                <th><div class="col_title_right">レートタイプ記号</div></th>
			                <td>
			                    <html:text maxlength="1" styleId="sign" property="sign" style="width:50px;" tabindex="101"/>
			                </td>
						</tr>

						<tr>
							<th><div class="col_title_right">レートタイプ備考</div></th>
							<td colspan="5">
								<html:text maxlength="120" styleId="remarks" property="remarks" style="width: 400px;" tabindex="102"/>
							</td>
						</tr>
					</table>
				</div><!-- /.section_body -->
			</div><!-- /.form_section -->
		</div><!-- /.form_section_wrap -->


	    <div class="form_section_wrap">
		    <div class="form_section">
		    	<div class="section_title">
					<c:if test="${isUpdate && editMode}">
						<span style="margin: 10px 0px 5px 0px;">レートデータ追加</span><br>
					</c:if>
					<c:if test="${!isUpdate || !editMode}">
						<span style="margin: 10px 0px 5px 0px;">レートデータ</span><br>
					</c:if>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="order_section" class="section_body">
					<table id="add_row" class="forms detail_info" summary="行追加" style="margin-top: 0px; width: 910px;">
						<colgroup>

							<col span="1" style="width:120px">
							<col span="1" style="width:120px">
							<col span="1" style="">
							<col span="1" style="width:100px">
						</colgroup>
						<thead>
						<tr>
							<th class="rd_top_left" style="height: 30px;">適用開始日<bean:message key='labels.must'/></th>
							<th class="xl64" style="height: 30px;">レート<bean:message key='labels.must'/></th>
							<th class="xl64" style="height: 30px;">レートデータ備考</th>
							<th class="rd_top_right" style="height: 30px;"></th>
						</tr>
						</thead>
						<tbody>
						<tr class="rate_list_data">
							<c:if test="${isUpdate && editMode}">
								<td style="text-align:center; background-color: #fae4eb;"><input maxlength="10" id="add_startDate" class="date_input" type="text" style="width: 90px; text-align: center; margin: 3px;" value="" tabindex="200"></td>
								<td style="text-align:center; background-color: #fae4eb;"><input maxlength="9" id="add_rate" type="text" style="width: 100px; text-align: right;ime-mode:disabled; margin: 3px;" value="" tabindex="201"></td>
								<td style="text-align:center;"><input maxlength="120" id="add_remarks" type="text" style="width: 600px; margin: 3px;" value="" tabindex="202"></td>
								<td style="text-align:center;"><button style="width:80px;" onclick="addRow()" tabindex="203" class="btn_list_action">レート追加</button></td>
							</c:if>
							<c:if test="${isUpdate && !editMode}">
								<td style="text-align:center; background-color: #fae4eb;"><html:text maxlength="10" styleId="rateTrnList_0_startDate" styleClass="date_input" property="rateTrnList[0].startDate"  style="width: 90px; text-align: center; margin: 3px;"  tabindex="200"/></td>
								<td style="text-align:center; background-color: #fae4eb;"><html:text maxlength="9" styleId="rateTrnList_0_rate" property="rateTrnList[0].rate" style="width: 100px; text-align: right;ime-mode:disabled; margin: 3px;" tabindex="201"/></td>
								<td style="text-align:center;"><html:text maxlength="120" styleId="rateTrnList_0_remarks" property="rateTrnList[0].remarks" style="width: 600px; margin: 3px;" tabindex="202"/></td>
								<td></td>
							</c:if>
							<c:if test="${!isUpdate}">
								<td style="text-align:center; background-color: #fae4eb;"><html:text maxlength="10" styleId="rateTrnList_0_startDate" property="rateTrnList[0].startDate"  style="width: 90px; text-align: center; margin: 3px;" tabindex="200"/></td>
								<td style="text-align:center; background-color: #fae4eb;"><html:text maxlength="9" styleId="rateTrnList_0_rate" property="rateTrnList[0].rate" style="width: 100px; text-align: right;ime-mode:disabled; margin: 3px;" tabindex="201"/></td>
								<td style="text-align:center;"><html:text maxlength="120" styleId="rateTrnList_0_remarks" property="rateTrnList[0].remarks" style="width: 600px; margin: 3px;" tabindex="202"/></td>
								<td></td>
							</c:if>
						</tr>
						</tbody>
					</table>

					<c:if test="${isUpdate && editMode}">
					<div id="ListContainer" class="search_paging" style="margin: 20px 0px 5px 0px;">
						<div id="count">レートデータ:<html:hidden property="rateTrnListSize" styleId="rateTrnListSize" /><span id="rateTrnListSizeLabel">${rateTrnListSize}</span>件</div>
			            <table id="List" class="forms detail_info" summary="レートデータ一覧" style="width:910px;">
			    			<colgroup>
			    				<col span="1" style="width:120px">
			    				<col span="1" style="width:120px">
			    				<col span="1" style="">
			    				<col span="1" style="width:100px">
			    			</colgroup>
			    			<thead>
				    			<tr>
				    				<th class="rd_top_left" style="height: 30px;">適用開始日</th>
				    				<th class="xl64" style="height: 30px;">レート</th>
				    				<th class="xl64" style="height: 30px;">レートデータ備考</th>
				    				<th class="rd_top_right" style="height: 30px;"></th>
				    			</tr>
			    			</thead>
			    			<tbody>
				                <!--　行追加用のダミー行 -->
				                <tr class="rate_list_date" id="rateTrnList_dummy" style="display:none;">
				                    <td style="text-align:center;">
				                    	<input type="hidden" id="rateId_dummy" value="">
				                    	<input type="text" maxlength="10" id="startDate" style="width: 90px; text-align: center;" tabindex="-1">
				                    </td>
				    				<td style="text-align:center;"><input type="text" maxlength="9" id="rate" style="width: 100px; text-align: right;ime-mode:disabled; margin: 3px;" tabindex="-1"></td>
				    				<td style="text-align:center;"><input type="text" maxlength="120" id="remarks" style="width: 600px; margin: 3px;" tabindex="-1"></td>
				    				<td style="text-align: center;"><button id="deleteBtn" style="width:80px; margin: 3px;" tabindex="-1" class="btn_list_action">削除</button></td>
				    			</tr>

								<c:forEach var="rateTrnList" varStatus="s" items="${rateTrnList}">
					    			<tr class="rate_list_data" id="rateTrnList_${s.index}">
					    				<td style="text-align:center;">
						    				<html:hidden name="rateTrnList" styleId="rateTrnList_${s.index}_rateId" property="rateId" indexed="true"/>
											<c:if test="${rateTrnList.rateId == ''}">
						                    	<html:text name="rateTrnList" maxlength="10" styleId="rateTrnList_${s.index}_startDate" property="startDate" styleClass="date_input" style="width: 90px; text-align: center; margin: 3px;" indexed="true" tabindex="${300+(s.index*4)}"/>
											</c:if>
											<c:if test="${rateTrnList.rateId != ''}">
						                    	<html:text name="rateTrnList" maxlength="10" styleId="rateTrnList_${s.index}_startDate" property="startDate" styleClass="c_disable" readonly="true" style="width: 90px; text-align: center; margin: 3px;" indexed="true" tabindex="${300+(s.index*4)}"/>
											</c:if>
					                    </td>
					    				<td style="text-align:center;"><html:text name="rateTrnList" maxlength="9" styleId="rateTrnList_${s.index}_rate" property="rate" style="width: 100px; text-align: right;ime-mode:disabled; margin: 3px;" indexed="true" tabindex="${301+(s.index*4)}"/></td>
					    				<td style="text-align:center;"><html:text name="rateTrnList" maxlength="120" styleId="rateTrnList_${s.index}_remarks" property="remarks" style="width: 600px; margin: 3px;" indexed="true" tabindex="${302+(s.index*4)}"/></td>
					    				<td style="text-align: center;"><button id="rateTrnList_${s.index}_deleteBtn" style="width:80px;" tabindex="${303+(s.index*4)}" class="btn_list_action">削除</button></td>
					    			</tr>
									<script type="text/javascript">
									<!--
									// イベントの張り付け
									$("#rateTrnList_${s.index}_deleteBtn").bind("click", {"index": ${s.index}}, deleteRow);
									-->
									</script>
								</c:forEach>
			                </tbody>
			            </table>
					</div>
					</c:if>
				</div><!-- /.section_body -->
			</div><!-- /.form_section -->
		</div><!-- /.form_section_wrap -->

		<div style="text-align: right; width: 1160px">
			<c:if test="${editMode}">
				<span>登録日：${creDatetmShow}　更新日:${updDatetmShow}　</span>
			</c:if>

			<c:if test="${!editMode}">
            	<span>（新規レートタイプ）</span>
			</c:if>

			<html:hidden property="creDatetm"/>
			<html:hidden property="creDatetmShow"/>
			<html:hidden property="updDatetm"/>
			<html:hidden property="updDatetmShow"/>
			<button tabindex="350" onclick="initForm();" class="btn_medium">初期化</button>

			<c:if test="${!isUpdate}">
				<button tabindex="351" disabled="disabled" class="btn_medium">更新</button>
			</c:if>
			<c:if test="${isUpdate}">
				<c:if test="${editMode}">
					<button tabindex="351" onclick="registerRate()" class="btn_medium">更新</button>
			    </c:if>
				<c:if test="${!editMode}">
					<button tabindex="351" onclick="registerRate()" class="btn_medium">登録</button>
			    </c:if>
			</c:if>
			<c:if test="${isUpdate}">
				<c:if test="${editMode}">
					<button tabindex="352" onclick="deleteRate()" class="btn_medium">削除</button>
			    </c:if>
			</c:if>
		</div>
	</div>
</div>
</s:form>
</body>

</html>

