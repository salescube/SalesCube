<%@page import="jp.co.arkinfosys.common.Constants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　倉庫（登録・編集）</title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>
	<c:set var="code_size_warehouse" value="<%=Constants.CODE_SIZE.RACK%>" />
	<script type="text/javascript">
	<!--
    var maxRowCount = 0;
    var trCloneBase = null;
    function init() {
        // 行追加用オブジェクト
        trCloneBase = $("#editRackList_dummy").clone(true);

        // 削除ボタンイベントの貼り付け
        for (var i = 0; i < maxRowCount; i++) {
            // イベントの貼り付け
            $("#editRackList_" + i + "\\.deleteBtn").bind("click", {"index": i}, deleteRow);
			$("#editRackList_" + i + "\\.rackCodeImg").bind("click", {"index": i}, searchRack);
        }
    }

    // ファンクションキーとのマッピング
    function onF1() { initForm(); }
    function onF2() { backToSearch(); }
    function onF3() { registerWarehouse(); }
    function onF4() { deleteWarehouse(); }

    function initForm() {
    	if(confirm("<bean:message key='confirm.init'/>")){
            location.doHref(contextRoot + "/master/editWarehouse/");
        }
    }

    function backToSearch() {
    	if(confirm("<bean:message key='confirm.master.warehouse.back'/>")){
            location.doHref(contextRoot + "/master/searchWarehouse/");
        }
    }

    function registerWarehouse() {
        <c:if test="${!editMode}">
    	if(confirm("<bean:message key='confirm.insert'/>")){
        	$("#editWarehouseForm").attr("action", "${f:url("/master/editWarehouse/insert")}");
        </c:if>
        <c:if test="${editMode}">
    	if(confirm("<bean:message key='confirm.update'/>")){
        	$("#editWarehouseForm").attr("action", "${f:url("/master/editWarehouse/update")}");
        </c:if>
    	$("#editWarehouseForm").trigger("submit");
        }
    }

    function deleteWarehouse() {
    	if(confirm("<bean:message key='confirm.delete'/>")){
    		if(confirm("<bean:message key='confirm.master.warehouse.delete.rack'/>")){
    			$("#editWarehouseForm").attr("action", "${f:url("/master/editWarehouse/deleteRack")}");
    		} else {
    			$("#editWarehouseForm").attr("action", "${f:url("/master/editWarehouse/updateRack")}");
    		}
        	$("#editWarehouseForm").trigger("submit");
        }
    }

    function setZipCode(id, map) {
        $("#warehouseZipCode").val(map["zipCode"]);
        $("#warehouseAddress1").val(map["zipAddress1"]);
        $("#warejpiseAddress2").val(map["zipAddress2"]);
    }

	//郵便番号ダイアログ
	function zipSearch(jqObject) {
		openSearchZipDialog( 'zipCode1',setZipCode);
		$("#zipCode1_zipCode").val( jqObject.val() );
		$("#zipCode1_zipCode").focus();
	}

    function searchZipCodeDirect() {
		// 入力チェック
		var val = $("#warehouseZipCode").val();
		if (!val) {
			return;
		}

		// 検索実行
		$("#errors").empty();
		var data = {"zipCode" : val};
		asyncRequest(contextRoot + "/ajax/master/searchZipCodeAjax/search", data,
			function(data) {
				var results = eval(data);
				if (results.length!=1) {
					var message = "<bean:message key="warns.zipcode.notidentical" arg0="倉庫"/>";
					$("#errors").append(message);
				}
				else {
					$("#warehouseZipCode").val(results[0].zipCode);
					$("#warehouseAddress1").val(results[0].zipAddress1);
					$("#warehouseAddress2").val(results[0].zipAddress2);
				}
			}
		);
	}

    function resetNo(index) {
        for (var i = index; i < maxRowCount; i++) {
            var nextId = i+1;

            // TRのID付け替え
            var elemTR = $("#editRackList_" + nextId);
            elemTR.attr("id", "editRackList_" + i);

            // noの書き換え
            var elemTd = elemTR.children(":first");
            elemTd.attr("id", "editRackList_" + i + ".no");
            elemTd.html(nextId);

            // 棚番存在
            elemTd = elemTd.next();
            var elemWork = elemTd.children("#editRackList_" + nextId + "\\.exist");
            elemWork.attr("name", "editRackList[" + i + "].exist");
            elemWork.attr("id", "editRackList_" + i  + ".exist");

            // 更新日
            elemWork = elemTd.children("#editRackList_" + nextId + "\\.updDatetm");
            elemWork.attr("name", "editRackList[" + i + "].updDatetm");
            elemWork.attr("id", "editRackList_" + i  + ".updDatetm");

            // 棚番コード
            elemWork = elemTd.children("#editRackList_" + nextId + "\\.rackCode");
            elemWork.attr("name", "editRackList[" + i + "].rackCode");
            elemWork.attr("id", "editRackList_" + i  + ".rackCode");

			// 棚番検索
            elemWork = elemTd.children("#editRackList_" + nextId + "\\.rackCodeImg");
            elemWork.attr("id", "editRackList_" + i  + ".rackCodeImg");
            elemWork.bind("click", {"index": i}, searchRack);

            // 棚番名
            elemTd = elemTd.next();
            elemWork = elemTd.children("#editRackList_" + nextId + "\\.rackName");
            elemWork.attr("name", "editRackList[" + i + "].rackName");
            elemWork.attr("id", "editRackList_" + i  + ".rackName");

            // 重複登録可能
            elemTd = elemTd.next();
            elemWork = elemTd.children("#editRackList_" + nextId + "\\.multiFlag");
            elemWork.attr("name", "editRackList[" + i + "].multiFlag");
            elemWork.attr("id", "editRackList_" + i  + ".multiFlag");

            // 削除ボタンのイベント
            elemTd = elemTd.next();
            elemWork = elemTd.children("#editRackList_" + nextId + "\\.deleteBtn");
            elemWork.attr("id", "editRackList_" + i  + ".deleteBtn");
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
    	elemTr.attr("id", "editRackList_" + maxRowCount);
        elemTr.show();

    	// No列の設定
    	elemTd = elemTr.children(":first");
        elemTd.attr("id", "editRackList_" + maxRowCount + ".no");
    	elemTd.html(maxRowCount+1);

        // 数量範囲列の設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children("#exist");
        elemWork.attr("id", "editRackList_" + maxRowCount  + ".exist");
        elemWork.attr("name", "editRackList[" + maxRowCount + "].exist");
    	elemWork = elemTd.children("#updDatetm");
        elemWork.attr("id", "editRackList_" + maxRowCount  + ".updDatetm");
        elemWork.attr("name", "editRackList[" + maxRowCount + "].updDatetm");
    	elemWork = elemTd.children("#rackCode");
        elemWork.attr("id", "editRackList_" + maxRowCount  + ".rackCode");
        elemWork.attr("name", "editRackList[" + maxRowCount + "].rackCode");
        elemWork.bind('keypress', 'return', move_focus_to_next_tabindex );
        elemWork.attr("tabindex", maxRowCount * 4 + 300);
    	elemWork = elemTd.children("#rackCodeImg");
        elemWork.attr("id", "editRackList_" + maxRowCount  + ".rackCodeImg");
        elemWork.bind("click", {"index": maxRowCount}, searchRack);
    	elemTd = elemTd.next();
    	elemWork = elemTd.children("#rackName");
        elemWork.attr("id", "editRackList_" + maxRowCount + ".rackName");
        elemWork.attr("name", "editRackList[" + maxRowCount + "].rackName");
        elemWork.bind('keypress', 'return', move_focus_to_next_tabindex );
        elemWork.attr("tabindex", maxRowCount * 4 + 302);

        // 掛率列の設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children("#multi");
        elemWork.attr("id", "editRackList_" + maxRowCount  + ".multiFlag");
        elemWork.attr("name", "editRackList[" + maxRowCount + "].multiFlag");
        elemWork.bind('keypress', 'return', move_focus_to_next_tabindex );
        elemWork.attr("tabindex", maxRowCount * 4 + 303);

        // 削除ボタンの設定
    	elemTd = elemTd.next();
    	elemWork = elemTd.children("#deleteBtn");
        elemWork.attr("id", "editRackList_" + maxRowCount  + ".deleteBtn");
        elemWork.bind("click", {"index": maxRowCount}, deleteRow);

    	// 行を追加
    	$("#trAddLine").before(elemTr);

        // 行番号を増やす
        maxRowCount++;
    }

	// 棚検索
	function searchRack(event) {
		var index = event.data.index;
		// 棚検索ダイアログを開く
		openSearchRackDialog(index, setRackInfo );

		$("#"+ index +"_rackCode").val($("#editRackList_"+index+"\\.rackCode").val());
		$("#"+ index +"_rackName").val($("#editRackList_"+index+"\\.rackName").val());
		//空き棚チェックを外す
		$("#"+ index +"_emptyRack").attr("checked",false);
		$("#"+ index +"_rackCode").focus();
	}

	// 棚検索後の設定処理
	function setRackInfo(index, map) {
		// 棚番
		$("#editRackList_"+index+"\\.rackCode").val(map["rackCode"]);
		$("#editRackList_"+index+"\\.rackCode").attr("readonly", true);
		$("#editRackList_"+index+"\\.rackCode").attr("class", "c_disable");
		// 棚番名
		$("#editRackList_"+index+"\\.rackName").val(map["rackName"]);
		// 重複可能
		if(map["multiFlag"] > 0){
			$("#editRackList_"+index+"\\.multiFlag").attr("checked", "checked");
		}else {
			$("#editRackList_"+index+"\\.multiFlag").attr("checked", "");
		}
		// 更新日
		$("#editRackList_"+index+"\\.updDatetm").val(map["updDatetm"]);
		// 存在する
		$("#editRackList_"+index+"\\.exist").val("true");
	}


    function deleteRow(event) {
        var index = event.data.index;

        // 確認メッセージは不要？

        if ($("#editRackList_" + index + "\\.discountDataId").val() != "") {
            // 削除された行の割引データＩＤを覚える
            $("#deletedDataId").val($("#deletedDataId").val() + "," + $("#editRackList_" + index + "\\.discountDataId").val());
        }
        $("#editRackList_" + index).remove();

        // 行数を減らす
        maxRowCount--;

        // 行番号の振替
        resetNo(index);
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
	<jsp:param name="MENU_ID" value="1307"/>
</jsp:include>

<!-- メイン機能 -->
<s:form styleId="editWarehouseForm" onsubmit="return false;">
<input type="hidden" id="updatable" name="updatable" value="${isUpdate}">
<div id="main_function">

	<span class="title">倉庫</span>

	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm()"> F1<br>初期化</button>
		<button tabindex="2001" onclick="backToSearch()">F2<br>戻る</button>
<c:if test="${!isUpdate}">
		<button tabindex="2002" disabled="true">F3<br>更新</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="2002" onclick="registerWarehouse()">F3<br>更新</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="2002" onclick="registerWarehouse()">F3<br>登録</button>
    </c:if>
</c:if>
<c:if test="${!isUpdate}">
		<button tabindex="2003" disabled="true">F4<br>削除</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="2003" onclick="deleteWarehouse()">F4<br>削除</button>
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
		<div id="errors" style="color: red" style="padding-left: 20px"><html:errors/></div>
		<div style="padding-left: 20px;color: blue;">
        	<html:messages id="msg" message="true">
        		<bean:write name="msg" ignore="true"/><br>
        	</html:messages>
    	</div>

	    <div class="form_section_wrap">
		    <div class="form_section">
		    	<div class="section_title">
					<span>倉庫情報</span>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="order_section" class="section_body">
					<table id="user_info" class="forms" style="width: 800px" summary="倉庫情報1">
						<colgroup>
							<col span="1" style="width: 15%">
							<col span="1" style="width: 35%">
							<col span="1" style="width: 15%">
							<col span="1" style="width: 35%">
						</colgroup>
						<tr>
							<th><div class="col_title_right_req">倉庫コード<bean:message key='labels.must'/></div></th>
							<td>
			                <c:if test="${editMode}">
			                    <html:text maxlength="${code_size_warehouse}" styleId="warehouseCode" property="warehouseCode" style="width: 100px; ime-mode: disabled;"  tabindex="100" readonly="true" styleClass="c_disable"/>
			                </c:if>
			                <c:if test="${!editMode}">
			                    <html:text maxlength="${code_size_warehouse}" styleId="warehouseCode" property="warehouseCode" style="width: 100px; ime-mode: disabled;"  tabindex="100"/>
			                </c:if>
			                </td>
							<th><div class="col_title_right_req">倉庫名<bean:message key='labels.must'/></div></th>
							<td><html:text maxlength="60" styleId="warehouseName" property="warehouseName" style="width: 200px" tabindex="101"/></td>
						</tr>
						<tr>
							<th><div class="col_title_right">郵便番号</div></th>
							<td colspan="3"><html:text style="width: 100px; ime-mode: disabled;" tabindex="102" property="warehouseZipCode" styleId="warehouseZipCode"  onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){searchZipCodeDirect();}"/>
							<html:image src="${f:url('/images//customize/btn_search.png')}"  style="vertical-align: middle; cursor: pointer;" onclick="zipSearch($('#warehouseZipCode'));"/>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right">住所１</div></th>
							<td colspan="3"><html:text style="width: 500px;" tabindex="104" property="warehouseAddress1" styleId="warehouseAddress1"/></td>
						</tr>
						<tr>
							<th><div class="col_title_right">住所２</div></th>
							<td colspan="3"><html:text style="width: 500px;" tabindex="105" property="warehouseAddress2" /></td>
						</tr>
						<tr>
							<th><div class="col_title_right">TEL</div></th>
							<td><html:text style="width: 200px; ime-mode: disabled;" tabindex="106" property="warehouseTel" /></td>
							<th><div class="col_title_right">FAX</div></th>
							<td><html:text style="width: 200px; ime-mode: disabled;" tabindex="107" property="warehouseFax" /></td>
						</tr>
					</table>
					<table id="warehouse_info1" class="forms" style="width: 800px" summary="管理者情報1">
						<colgroup>
							<col span="1" style="width: 15%">
							<col span="1" style="width: 35%">
							<col span="1" style="width: 15%">
							<col span="1" style="width: 35%">
						</colgroup>
						<tr>
							<th><div class="col_title_right">管理者名</div></th>
							<td><html:text maxlength="60" styleId="managerName" property="managerName" style="width: 200px" tabindex="200"/></td>
							<th><div class="col_title_right">管理者カナ</div></th>
							<td><html:text maxlength="60" styleId="managerKana" property="managerKana" style="width: 200px" tabindex="201"/></td>
						</tr>
						<tr>
							<th><div class="col_title_right">TEL</div></th>
							<td><html:text style="width: 200px; ime-mode: disabled;" tabindex="202" property="managerTel" /></td>
							<th><div class="col_title_right">FAX</div></th>
							<td><html:text style="width: 200px; ime-mode: disabled;" tabindex="203" property="managerFax" /></td>
						</tr>
						<tr>
							<th><div class="col_title_right">E-MAIL</div></th>
							<td colspan="3"><html:text style="width: 500px; ime-mode: disabled;" tabindex="204" property="managerEmail" /></td>
						</tr>
					</table>
					<table id="warehouse_info2" class="forms" style="width: 800px" summary="倉庫情報2">
						<colgroup>
							<col span="1" style="width: 15%">
							<col span="1" style="width: 85%">
						</colgroup>
						<tr>
							<th><div class="col_title_right_req">倉庫状況<bean:message key='labels.must'/></div></th>
							<td>
			                  <html:select styleId="warehouseState" property="warehouseState" style="width: 100px; ime-mode: disabled;"  tabindex="300">
								<html:option value="運用中" />
								<html:option value="棚卸中"/>
								<html:option value="廃止" />
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
				<span>関連する棚番</span>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="order_section" class="section_body">
					<table id="editRackList" class="forms detail_info" style="width: 600px" summary="棚番情報1">
						<colgroup>
			                <col span="1" style="width: 10%">
			                <col span="1" style="width: 20%">
			                <col span="1" style="width: 60%">
			                <col span="1" style="width: 10%">
						</colgroup>
						<tr>
							<th class="xl64 rd_top_left" style="height: 30px;">No</th>
							<th class="xl64" style="height: 30px;">棚番コード<bean:message key='labels.must'/></th>
							<th class="xl64" style="height: 30px;">棚番名<bean:message key='labels.must'/></th>
							<th class="xl64" style="height: 30px;">&nbsp;重複登録可能&nbsp;</th>
							<th class="xl64 rd_top_right" style="height: 30px;">&nbsp;</th>
						</tr>
						<tr id="editRackList_dummy" style="display:none;">
							<td id="editRackList_dummy" style="text-align: center">0
			                </td>
							<td style="background-color: #fae4eb;">
			                    <input type="hidden" id="exist" value="false" />
			                    <input type="hidden" id="updDatetm" />
								<input type="text" maxlength="10" id="rackCode" style="width:95px;ime-mode:disabled;" indexed="true">
								<input type="image" src="${f:url('/images//customize/btn_search.png')}" id="rackCodeImg" indexed="true" style="vertical-align: middle; cursor: pointer; width: auto;"/>
							</td>
							<td style="background-color: #fae4eb;">
								<input type="text" maxlength="60" id="rackName" style="width:95%;" indexed="true" />
							<td style="text-align: center">
								<input type="checkbox" id="multi" value="1" indexed="true" />
			                </td>
							<td style="text-align: center">
			                    <button id="deleteBtn" class="btn_list_action">削除</button>
			                </td>
						</tr>
						<c:if test="${isUpdate}">
							<c:forEach var="editRackList" items="${editRackList}" varStatus="s">
								<tr id="editRackList_${s.index}">
									<td id="editRackList_${s.index}.no" style="text-align: center">${s.index+1}</td>
									<td style="background-color: #fae4eb;">
										<html:hidden styleId="editRackList_${s.index}.exist" name="editRackList" property="exist" indexed="true" />
										<html:hidden styleId="editRackList_${s.index}.updDatetm" name="editRackList" property="updDatetm" indexed="true" />
										<c:if test="${editRackList.exist}">
											<html:text maxlength="10" styleId="editRackList_${s.index}.rackCode" name="editRackList" property="rackCode" style="width: 100px;ime-mode:disabled; margin: 3px;" tabindex="${s.index*4+300}" indexed="true" readonly="true" styleClass="c_disable" />
										</c:if>
										<c:if test="${!editRackList.exist}">
											<html:text maxlength="10" styleId="editRackList_${s.index}.rackCode" name="editRackList" property="rackCode" style="width: 100px;ime-mode:disabled;" tabindex="${s.index*4+300}" indexed="true" />
											<html:image src="${f:url('/images//customize/btn_search.png')}" styleId="editRackList_${s.index}.rackCodeImg" style="vertical-align: middle; cursor: pointer; width: auto;" tabindex="${s.index*4+301}" indexed="true" />
										</c:if>
									</td>
									<td style="text-align: center;background-color: #fae4eb;">
										<html:text maxlength="60" styleId="editRackList_${s.index}.rackName" name="editRackList" property="rackName" style="width: 98%;ime-mode:disabled; margin: 3px;" tabindex="${s.index*4+302}" indexed="true" />
									</td>
									<td style="text-align: center">
										<html:checkbox styleId="editRackList_${s.index}.multiFlag" name="editRackList" property="multiFlag" value="1" tabindex="${s.index*4+303}" indexed="true" />
									</td>
									<td>
										<c:if test="${!isUpdate}">
										     <button disabled="disabled" class="btn_list_action">削除</button>
										</c:if>
										<c:if test="${isUpdate}">
										     <button id="editRackList_${s.index}.deleteBtn" tabindex="${s.index*4+203}" class="btn_list_action">削除</button>
										</c:if>
									</td>
								</tr>
							<script type="text/javascript">
				            <!--
				            maxRowCount++;
				            -->
				            </script>
				            </c:forEach>
						</c:if>
						<c:if test="${isUpdate}">
						<tr id="trAddLine">
							<td style="text-align: right" colspan="5"><button onclick="addRow()" class="btn_list_action">行追加</button></td>
						</tr>
						</c:if>
					</table>
				</div><!-- /.section_body -->
			</div><!-- /.form_section -->
		</div><!-- /.form_section_wrap -->

		<div style="text-align: right; width: 1160px">
			<span>登録日：${creDatetmShow}　更新日：${updDatetmShow}　</span>
			<button tabindex="800" onclick="initForm()" class="btn_medium">初期化</button>
<c:if test="${!isUpdate}">
            <button tabindex="801" disabled="true" class="btn_medium">更新</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
            <button tabindex="801" onclick="registerWarehouse()" class="btn_medium">更新</button>
    </c:if>
	<c:if test="${!editMode}">
            <button tabindex="801" onclick="registerWarehouse()" class="btn_medium">登録</button>
    </c:if>
</c:if>
<c:if test="${!isUpdate}">
		<button tabindex="802" disabled="true" class="btn_medium">削除</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="802" onclick="deleteWarehouse()" class="btn_medium">削除</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="802" disabled="true" class="btn_medium">削除</button>
    </c:if>
</c:if>
		</div>
	</div>
</div>

<c:forEach var="rackCodesHist" items="${rackCodesHist}" varStatus="s">
	<html:hidden name="rackCodesHist" property="rackCode" indexed="true" />
	<html:hidden name="rackCodesHist" property="updDatetm" indexed="true" />
</c:forEach>
<html:hidden property="creDatetm"/>
<html:hidden property="creDatetmShow"/>
<html:hidden property="updDatetm"/>
<html:hidden property="updDatetmShow"/>
</s:form>

</body>

</html>