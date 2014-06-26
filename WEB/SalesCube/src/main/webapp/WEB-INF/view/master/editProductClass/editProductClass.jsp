<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><bean:message key='titles.system'/>　商品分類マスタ管理（登録・編集）</title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
    function init() {
		if( ${!editMode} ){
			changeTargetClass();
		}
    }

    // ファンクションキーとのマッピング
    function onF1() { initForm(); }
    function onF2() { backToSearch(); }
    function onF3() { registerProductClass(); }
    function onF4() { deleteProductClass(); }

    function initForm() {
    	if(confirm("<bean:message key='confirm.init'/>")){
            location.doHref(contextRoot + "/master/editProductClass/");
        }
    }

    function backToSearch() {
    	if(confirm("<bean:message key='confirm.master.productclass.back'/>")){
            location.doHref(contextRoot + "/master/searchProductClass/");
        }
    }

    function registerProductClass() {
        <c:if test="${!editMode}">
    	if(confirm("<bean:message key='confirm.insert'/>")){
        	$("#editProductClassForm").attr("action", "${f:url("/master/editProductClass/insert")}");
        </c:if>
        <c:if test="${editMode}">
    	if(confirm("<bean:message key='confirm.update'/>")){
        	$("#editProductClassForm").attr("action", "${f:url("/master/editProductClass/update")}");
        </c:if>
    	$("#editProductClassForm").trigger("submit");
        }
    }

    function deleteProductClass() {
    	if(confirm("<bean:message key='confirm.delete'/>")){
        	$("#editProductClassForm").attr("action", "${f:url("/master/editProductClass/delete")}");
        	$("#editProductClassForm").trigger("submit");
        }
    }

    function changeTargetClass() {

        // 大を選択
        if ($("#targetClass").val() == 1) {
            // プルダウン大・中をカラにしてdisabled。
            $("#classCode1").empty();
            $("#classCode2").empty();
            $("#classCode1").attr("disabled", true);
            $("#classCode2").attr("disabled", true);
            $("#classCode1").addClass("c_disable");
            $("#classCode2").addClass("c_disable");

            
            // プルダウン大を取る
    		var data = {
                "classCode1" : "",
                "classCode2" : "",
                "classCode3" : ""
    		}

            // 終了
            getNextValue(data);
            return;
        }

        // 中を選択
        if ($("#targetClass").val() == 2) {

            // プルダウン中をカラにして無効
            $("#classCode2").empty();
            $("#classCode2").attr("disabled", true);

            // プルダウン大は有効
            $("#classCode1").empty();
            $("#classCode1").removeAttr("disabled");
            $("#classCode1").removeClass("c_disable");

            // プルダウン大を取る
    		syncRequest(
    	    	"${f:url("/ajax/productClassAjax/searchClass1")}",
    	    	data,
   				function(data) {
    					// 成功用関数
    				var options = eval(data);

    				// （大）を空にする
    				$("#classCode1").empty();

    				// （大）に要素をセットする
    				for ( var i = 0; i < options.length; i++) {
    					if (options[i].classCode1 == "") {
    						continue;
    					}
    					opt = $(document.createElement("option"));
    					opt.attr("value", options[i].classCode1);
    					$(opt).append(document.createTextNode(options[i].className));
    					$("#classCode1").append(opt);
    				}
    			});

    		var data = {
                "classCode1" : $("#classCode1").val(),
                "classCode2" : "",
                "classCode3" : ""
    		}

            getNextValue(data);
        }

        // 小を選択
        if ($("#targetClass").val() == 3) {
            // プルダウン大・中は有効
            $("#classCode1").empty();
            $("#classCode1").removeAttr("disabled");
            $("#classCode1").removeClass("c_disable");
            $("#classCode2").empty();
            $("#classCode2").removeAttr("disabled");
            $("#classCode2").removeClass("c_disable");

            // プルダウン大を取る
    		var data = {
                "classCode1" : "",
                "classCode2" : "",
                "classCode3" : ""
    		}

    		syncRequest(
    	    		"${f:url("/ajax/productClassAjax/searchClass1")}",
    	    		data,
    				function(data) {
    					// 成功用関数
    				var options = eval(data);

    				// （大）を空にする
    				$("#classCode1").empty();

    				// （大）に要素をセットする
    				for ( var i = 0; i < options.length; i++) {
    					if (options[i].classCode1 == "") {
    						continue;
    					}

    					opt = $(document.createElement("option"));
    					opt.attr("value", options[i].classCode1);
    					$(opt)
    							.append(
    									document
    											.createTextNode(options[i].className));
    					$("#classCode1").append(opt);
    				}
    				$("#classCode1").change();	//大の選択を中に反映させるべきである（空要素ならまだしも）
    			});
        }
    }

    function changeProductClass1() {

        // 中を選択した場合はここで値を取りに行く
    	var data = {
    		"classCode1" : $("#classCode1").val()
    	}
        if ($("#targetClass").val() == 3) {
    		syncRequest(
    				"${f:url("/ajax/productClassAjax/search")}",
    				data,
    				function(data) {
    					// 成功用関数
    				var options = eval(data);

    				// （中）を空にする
    				$("#classCode2").empty();

    				// （中）に要素をセットする
    				for ( var i = 0; i < options.length; i++) {
    					if (options[i].classCode2 == "") {
    						continue;
    					}
                        if (options[i].classCode3 != "") {
                            continue;
                        }

    					opt = $(document.createElement("option"));
    					opt.attr("value", options[i].classCode2);
    					$(opt)
    							.append(
    									document
    											.createTextNode(options[i].className));
    					$("#classCode2").append(opt);
    				}
    			});
			$("#classCode2").change();	//中の選択を小に反映させるべきである（空要素ならまだしも）
        } else {
            getNextValue(data);
        }
    }

    function changeProductClass2() {
		var data = {
			"classCode1" : $("#classCode1").val(),
            "classCode2" : $("#classCode2").val(),
            "classCode3" : ""
		}
        getNextValue(data);
    }

    function getNextValue(data) {
        asyncRequest("${f:url("/ajax/productClassAjax/searchNextValue")}", data,
            function(data) {
                var value = eval(data);
                $("#classCode").val(value[0].nextVal);
            });
    }
	-->
	</script>
	<script type="text/javascript" src="./scripts/common.js"></script>
</head>
<body onhelp="return false;" onload="init()">

<%-- ページヘッダ領域 --%>
<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

<%-- メニュー領域 --%>
<jsp:include page="/WEB-INF/view/common/menubar.jsp">
	<jsp:param name="PARENT_MENU_ID" value="0013"/>
	<jsp:param name="MENU_ID" value="1311"/>
</jsp:include>

<!-- メイン機能 -->
<s:form styleId="editProductClassForm" onsubmit="return false;">
<div id="main_function">

	<span class="title">分類</span>

	<div class="function_buttons">
		<button tabindex="2000" onclick="initForm()"> F1<br>初期化</button>
		<button tabindex="2001" onclick="backToSearch()">F2<br>戻る</button>
<c:if test="${!isUpdate}">
		<button tabindex="2002" disabled="disabled">F3<br>更新</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="2002" onclick="registerProductClass()">F3<br>更新</button>
    </c:if>
	<c:if test="${!editMode}">
		<button tabindex="2002" onclick="registerProductClass()">F3<br>登録</button>
    </c:if>
</c:if>
<c:if test="${!isUpdate}">
		<button tabindex="2003" disabled="disabled">F4<br>削除</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button tabindex="2003" onclick="deleteProductClass()">F4<br>削除</button>
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
					<span>分類情報</span><br>
				</div><!-- /.section_title -->
				<div class="section_body">

					<table id="productClass_info" class="forms" summary="分類情報１">
						<colgroup>
							<col span="1" style="width: 8%">
							<col span="1" style="width: 7%">
							<col span="1" style="width: 85%">
						</colgroup>
						<tr>
							<th colspan="2"><div class="col_title_right">登録・編集分類</div></th>
							<td>
			<c:if test="${!editMode}">
								<html:select styleId="targetClass" property="targetClass" onchange="changeTargetClass()" tabindex="100">
									<html:option value="1">分類（大）</html:option>
									<html:option value="2">分類（中）</html:option>
									<html:option value="3">分類（小）</html:option>
								</html:select>
			</c:if>
			<c:if test="${editMode}">
								<html:select styleId="targetClass" property="targetClass" styleClass="c_disable" tabindex="100">
			        <c:if test="${targetClass == 1}">
									<html:option value="1">分類（大）</html:option>
			        </c:if>
			        <c:if test="${targetClass == 2}">
									<html:option value="2">分類（中）</html:option>
			        </c:if>
			        <c:if test="${targetClass == 3}">
									<html:option value="3">分類（小）</html:option>
			        </c:if>
								</html:select>
			</c:if>
							</td>
						</tr>
						<tr>
							<th rowspan="2"><div class="col_title_right">親分類</div></th>
							<th><div class="col_title_right">（大）</div></th>
							<td colspan="3">
			<c:if test="${!editMode}">
			    				<html:select styleId="classCode1" property="classCode1" style="width:500px" onchange="changeProductClass1()" tabindex="101">
			    					<html:options collection="classCode1List" property="value" labelProperty="label"/>
			    				</html:select>
			</c:if>
			<c:if test="${editMode}">
			    				<html:select styleId="classCode1" property="classCode1" style="width:500px" styleClass="c_disable" tabindex="101">
			    					<html:options collection="classCode1List" property="value" labelProperty="label"/>
			    				</html:select>
			</c:if>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right">（中）</div></th>
							<td colspan="3">
			<c:if test="${!editMode}">
			    				<html:select styleId="classCode2" property="classCode2" style="width:500px" onchange="changeProductClass2()" tabindex="102">
			    					<html:options collection="classCode2List" property="value" labelProperty="label"/>
			    				</html:select>
			</c:if>
			<c:if test="${editMode}">
			    				<html:select styleId="classCode2" property="classCode2" style="width:500px" styleClass="c_disable" tabindex="102">
			    					<html:options collection="classCode2List" property="value" labelProperty="label"/>
			    				</html:select>
			</c:if>
							</td>
						</tr>
					</table>


		<table class="forms" style="width: 910px" summary="分類情報２">
			<colgroup>
				<col span="1" style="width: 15%">
				<col span="1" style="width: 25%">
				<col span="1" style="width: 15%">
				<col span="1" style="width: 45%">
			</colgroup>
			<tr>
				<th><div class="col_title_right">分類コード</div></th>
				<td><html:text maxlength="4" styleId="classCode" property="classCode" style="width: 100px" tabindex="103" readonly="true" styleClass="c_disable"/>
				</td>
				<th><div class="col_title_right_req">分類名<bean:message key='labels.must'/></div></th>
				<td><html:text maxlength="250" styleId="className" property="className" style="width: 250px" tabindex="104"/></td>
			</tr>
		</table>
		</div><!-- /.section_body -->
    		</div><!-- /.form_section -->
   		</div><!-- /.form_section_wrap -->
		<div style="text-align: right; width: 1160px">
			<span>登録日：${creDatetmShow}　更新日:${updDatetmShow}　</span>
			<button class="btn_medium" tabindex="150" onclick="initForm()">初期化</button>
<c:if test="${!isUpdate}">
            <button class="btn_medium" tabindex="151" disabled="disabled">更新</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
            <button class="btn_medium" tabindex="151" onclick="registerProductClass()">更新</button>
    </c:if>
	<c:if test="${!editMode}">
            <button class="btn_medium" tabindex="151" onclick="registerProductClass()">登録</button>
    </c:if>
</c:if>
<c:if test="${!isUpdate}">
		<button class="btn_medium" tabindex="152" disabled="disabled">削除</button>
</c:if>
<c:if test="${isUpdate}">
	<c:if test="${editMode}">
		<button class="btn_medium" tabindex="152" onclick="deleteProductClass()">削除</button>
    </c:if>
	<c:if test="${!editMode}">
		<button class="btn_medium" tabindex="152" disabled="disabled">削除</button>
    </c:if>
</c:if>
		</div>

	</div>
</div>

<html:hidden property="creDatetm"/>
<html:hidden property="creDatetmShow"/>
<html:hidden property="updDatetm"/>
<html:hidden property="updDatetmShow"/>
<html:hidden property="editMode"/>
</s:form>
</body>

</html>
