<%@page import="jp.co.arkinfosys.common.CategoryTrns" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　セット商品マスタ管理(登録・編集)</title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--

	//// Fボタン
	// F1
	function onF1(){
		init_screen();		// リセット
	}

	// F2
	function onF2(){
		go_to_search();		// 検索画面へ
	}

	// F3
	function onF3(){
		update_product();	// 更新
	}

	////
	// リセット
	function init_screen(){
		if(confirm('<bean:message key="confirm.reset" />')){
			document.master_editProductSetActionForm.action = '${f:url("edit")}';
			document.master_editProductSetActionForm.submit();
		}
	}

	// 検索画面へ
	function go_to_search(){
		if(confirm('<bean:message key="confirm.master.productset.back" />')){
			window.location.doHref('${f:url("/master/searchProductSet")}');
		}
	}

	// 更新
	function update_product(){
		if(!confirm('<bean:message key="confirm.insert" />')){
			return;
		}

		// テンプレート行を削除
		$("#TEMPLATE").remove();

		// submit用にname属性を修正する childProductList[i].xxxxの形式に修正
		var i = 0;
		$("#childProductList").children("tr").each(
			function() {
				var tr = $(this);
				tr.find("input").each(
					function() {
						var inputElement = $(this);

						inputElement.attr("name", "childProductList[" + i + "]." + inputElement.attr("name") );
					}
				);
				i++;
			}
		);

		document.master_editProductSetActionForm.action = '${f:url("update")}';
		_before_submit($(".numeral_commas"));
		document.master_editProductSetActionForm.submit();
	}

	/**
	 * 行追加処理
	 */
	function addRow(){
		var templateRow = $("#TEMPLATE").clone(true);
		templateRow.attr("id", (new Date()).getTime());
		templateRow.css("display", "");

		// カンマ処理付与
		var target = templateRow.find(".numeral_commas");
		if (target.size() > 0) {
			target.focus( function() { _rmv_commas(this); });
			target.blur( function() { _set_commas(this); });
		}

		// エンターキーフォーカス移動設定
		target = templateRow.find("[tabindex]");
		if (target.size() > 0) {
			target.bind('keypress', 'return', move_focus_to_next_tabindex );
		}


		// 数量入力フィールドにonchange時のイベントを付与
		templateRow.find(".BDCqua").change(
			function() {
				applyQuantityAlignment($(this));
			}
		);

		$("#childProductList").append(templateRow);

		var rowCount = resetIndex();
		if(rowCount == 2) {
			$("#childProductList").find("[name='deleteButton']").removeAttr("disabled");
		}
	}

	/**
	 * 数量小数桁処理と端数処理を適用する
	 */
	function applyQuantityAlignment(jQueryObject) {
		if(jQueryObject != null) {
			jQueryObject.setBDCStyle( $("#productFractCategory").val() ,$("#numDecAlignment").val() ).attBDC();
		}
	}

	/**
	 * 行削除処理
	 */
	function deleteRow(obj){
		if(!confirm('<bean:message key="confirm.line.delete" />')){
			return;
		}
		var rowObj = $(obj).parent().parent();

		var updDatetm = rowObj.find("[name='updDatetm']").val();
		if(updDatetm.length > 0) {
			// 更新日が存在しないものは画面上で作成、削除されただけ
			rowObj.css("display", "none");
			rowObj.find("[name='deleted']").val("true");
		}
		else {
			rowObj.remove();
		}

		var rowCount = resetIndex();
		if(rowCount == 1) {
			$("#childProductList").find("[name='deleteButton']").attr("disabled", "disabled");
		}
	}

	/**
	 * 行番号とtabindex属性を再設定し、設定した行数を返す
	 */
	function resetIndex() {
		// 行インデックス
		var i = 0;
		$("#childProductList").children("tr").each(
			function() {
				if($(this).find("[name='deleted']").val() == "true") {
					return;
				}

				var span = $(this).find("[class='classRowIndex']");
				span.empty();
				span.append(document.createTextNode(++i));
			}
		);

		var rowCount = i;

		// tabindex
		i = 200;
		$("#childProductList").find("[tabindex]").each(
			function() {
				$(this).attr("tabindex", i++);
			}
		);

		return rowCount;
	}

	/**
	 * 商品検索ダイアログを開く
	 */
	function productSearch(obj) {
		var rowObj = $(obj).parent().parent();
		var dialogId = rowObj.attr("id") + "Dialog";

		openSearchProductDialog(
			dialogId,
			function(id, map) {
				rowObj.find("[name='productCode']").val( map["productCode"] );
				rowObj.find("[name='productName']").val( map["productName"] );
			}
		);

		// ダイアログのフィールドに値をセットしてフォーカス
		$("#" + dialogId + "_productCode").val( rowObj.find("[name='productCode']").val() );
		$("#" + dialogId + "_productCode").focus();

		// 適切なセット分類を選択
		var i = 0;
		var setTypeCategory = $("#" + dialogId + "_setTypeCategory");
		setTypeCategory.children("option").each(
			function() {
				if(this.value == "<%=CategoryTrns.PRODUCT_SET_TYPE_SINGLE%>") {
					setTypeCategory.get(0).selectedIndex = i;
				}
				i++;
			}
		);
	}

	$(function() {
		$(".BDCqua").change(
			function() {
				applyQuantityAlignment($(this));
			}
		);
	});

	/**
	 * 商品コード変更処理
	 */
	function changeProductCode(obj) {
		if(obj.value.length == 0){
			// クリアする
			setRowInfo(obj, "");
			return;
		}

		var data = new Object();
		data["productCode"] = obj.value;
		asyncRequest(
				contextRoot + "/ajax/commonProduct/getProductInfos",
				data,
				function(data) {
					if(data==""){
						alert("<bean:message key="errors.notExist" arg0="商品コード"/>");
						setRowInfo(obj, "");
						return;
					}

					var values = eval("(" + data + ")");
					setRowInfo(obj, values[ "productName" ]);
				}
		);
	}

	function setRowInfo(obj, productName) {
		var rowObj = $(obj).parent().parent();
		rowObj.find("[name='productName']").val(productName);
	}

	-->
	</script>

</head>
<body>

	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0013"/>
		<jsp:param name="MENU_ID" value="1301"/>
	</jsp:include>

	<%-- メイン機能 --%>
	<div id="main_function">
		<span class="title">セット商品</span>

		<%-- Fキー群 --%>
		<div class="function_buttons">
			<button tabindex="2000" onclick="onF1()">F1<br>リセット</button>
			<button type="button" tabindex="2001" onclick="onF2()">F2<br>戻る</button>
			<button type="button" tabindex="2002" onclick="onF3();"
				<c:if test="${!isUpdate}">disabled</c:if> >F3<br>更新</button>
			<button type="button" disabled="disabled">F4<br>&nbsp;</button>
			<button type="button" disabled="disabled">F5<br>&nbsp;</button>
			<button type="button" disabled="disabled">F6<br>&nbsp;</button>
			<button type="button" disabled="disabled">F7<br>&nbsp;</button>
			<button type="button" disabled="disabled">F8<br>&nbsp;</button>
			<button type="button" disabled="disabled">F9<br>&nbsp;</button>
			<button type="button" disabled="disabled">F10<br>&nbsp;</button>
			<button type="button" disabled="disabled">F11<br>&nbsp;</button>
			<button type="button" disabled="disabled">F12<br>&nbsp;</button>
		</div>
		<br><br><br>

		<div class="function_forms">

			<%-- エラー表示部分 --%>
			<div style="color:red; padding-left: 20px">
				<html:errors/>
				<span id="ajax_errors"></span>
			</div>
			<div style="color:blue; padding-left: 20px">
				<html:messages id="msg" message="true">
					<bean:write name="msg" ignore="true"/>
				</html:messages>
			</div>

			<s:form onsubmit="return false;">
				<html:hidden styleId="isUpdate" property="isUpdate"/>

			    <div class="form_section_wrap">
				    <div class="form_section">
				    	<div class="section_title">
							<span>セット商品情報</span>
				            <button class="btn_toggle" />
						</div><!-- /.section_title -->

						<div id="order_section" class="section_body">
							<table class="forms" summary="セット商品情報">
								<tr>
									<th><div class="col_title_right">セット商品コード</div></th>
									<td><html:text styleId="setProductCode" property="setProductCode"
											style="width: 170px" styleClass="c_disable" tabindex="100" readonly="true" /></td>
									<th><div class="col_title_right">セット商品名</div></th>
									<td><html:text styleId="setProductName" property="setProductName"
											style="width: 400px" styleClass="c_disable" tabindex="101" readonly="true" /></td>
								</tr>
							</table>
						</div><!-- /.section_body -->
					</div><!-- /.form_section -->
				</div><!-- /.form_section_wrap -->

			    <div class="form_section_wrap">
				    <div class="form_section">
				    	<div class="section_title">
							<span>セット内容</span>
				            <button class="btn_toggle" />
						</div><!-- /.section_title -->

						<div id="order_section" class="section_body">
							<table class="forms detail_info" summary="セット商品内訳" style="width: 910px;">
								<colgroup>
									<col span="1" style="width: 5%">
									<col span="1" style="width: 20%">
									<col span="1" style="width: 41%">
									<col span="1" style="width: 10%">
									<col span="1" style="width: 7%">
								</colgroup>
								<thead>
								<tr>
									<th class="rd_top_left" style="height: 30px;">No</th>
									<th class="xl64" style="height: 30px;">商品コード※</th>
									<th class="xl64" style="height: 30px;">商品名</th>
									<th class="xl64" style="height: 30px;">数量※</th>
									<th class="rd_top_right" style="height: 30px;">&nbsp;</th>
								</tr>
								</thead>

								<tbody id="childProductList">

								<bean:define id="lineNo" value="1"/>

								<c:forEach var="product" items="${childProductList}" varStatus="status" >
								<%-- 行の順序が自在に入れ替わるためindexed属性は使わない --%>

								<tr id="childNo_${status.index}" <c:if test="${product.deleted}">style="display: none;"</c:if> >
									<td style="text-align: center">
										<span class="classRowIndex">${product.deleted ? "" : lineNo }</span>

										<%-- セット商品コード・セット商品名 --%>
										<html:hidden name="product" property="setProductCode" />
										<html:hidden name="product" property="setProductName"/>

										<%-- 更新日時 --%>
										<html:hidden name="product" property="updDatetm"/>

										<%-- オリジナルの商品コード --%>
										<html:hidden name="product" property="originalProductCode"/>
									</td>
									<td style="text-align: center; background-color: #fae4eb;">
										<html:text name="product" property="productCode" style="width: 170px; ime-mode: disabled;" tabindex="${200 + (5 * status.index)}" maxlength="20"
											onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); changeProductCode(this); }"/>
										<input type="image" name="searchProductButton" src="${f:url('/images//customize/btn_search.png')}"
											style="vertical-align: middle; cursor: pointer; width: auto;"
											onclick="productSearch(this);" tabindex="${201 + (5 * status.index)}"/>
									</td>
									<td>
										<html:text name="product" property="productName" style="width: 400px;"
											styleClass="c_disable" readonly="true" tabindex="${202 + (5 * status.index)}"/>
									</td>
									<td style="text-align: center; background-color: #fae4eb;">
										<html:text name="product" property="quantity" style="width: 80px; ime-mode: disabled;"
											styleClass="numeral_commas BDCqua" tabindex="${203 + (5 * status.index)}" maxlength="6"/>
									</td>
									<td>
										<html:hidden name="product" property="deleted"/>
										<button type="button" name="deleteButton" class="btn_list_action" tabindex="${204 + (5 * status.index)}"
												onclick="deleteRow(this);"
												<c:if test="${!isUpdate || childProductCount == 1}">disabled="disabled"</c:if>>削除</button>
									</td>
								</tr>

								<bean:define id="lineNo" value="${product.deleted ? lineNo : lineNo + 1}"/>

								</c:forEach>
								</tbody>

								<tfoot>
								<tr>
									<c:if test="${isUpdate}">
									<td style="text-align: right" colspan="6">
										<button type="button" onclick="addRow();" tabindex="400" class="btn_list_action">行追加</button>
									</td>
									</c:if>
									<c:if test="${!isUpdate}">
									<td style="text-align: right" colspan="6">
										<button type="button" disabled="disabled" tabindex="400" class="btn_list_action">行追加</button>
									</td>
									</c:if>
								</tr>

								<%-- 行追加用の行テンプレート --%>
								<tr id="TEMPLATE" style="display: none;">
									<td style="text-align: center">
										<span class="classRowIndex"></span>

										<%-- セット商品コード・セット商品名 --%>
										<html:hidden property="setProductCode"/>
										<html:hidden property="setProductName"/>

										<%-- 更新日時 --%>
										<input type="hidden" name="updDatetm" value="">

										<%-- オリジナルの商品コード --%>
										<input type="hidden" name="originalProductCode" value="">
									</td>
									<td style="text-align: center; background-color: #fae4eb;">
										<input type="text" name="productCode" style="width: 170px; ime-mode: disabled;" tabindex="-1"  maxlength="20"
											onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); changeProductCode(this); }"/>
										<input type="image"  name="searchProductButton" src="${f:url('/images//customize/btn_search.png')}"
											style="vertical-align: middle; cursor: pointer; width: auto;"
											onclick="productSearch(this);" tabindex="-1">
									</td>
									<td>
										<input type="text" name="productName" style="width: 400px;" class="c_disable" readonly="readonly" tabindex="-1">
									</td>
									<td style="text-align: center; background-color: #fae4eb;">
										<input type="text" name="quantity" style="width: 80px; ime-mode: disabled;" class="numeral_commas BDCqua" tabindex="-1" maxlength="6">
									</td>
									<td>
										<input type="hidden" name="deleted" value="false">
										<button type="button" name="deleteButton" class="btn_list_action" tabindex="-1"
												onclick="deleteRow(this);">削除</button>
									</td>
								</tr>
								</tfoot>
							</table>
						</div><!-- /.section_body -->
					</div><!-- /.form_section -->
				</div><!-- /.form_section_wrap -->

				<div style="text-align: right; width: 1160px;">
					<span>登録日：${creDatetmShow} 更新日:${updDatetmShow}</span>

					<html:hidden styleId="creDatetmShow" property="creDatetmShow"/>
					<html:hidden styleId="updDatetmShow" property="updDatetmShow"/>
					<html:hidden styleId="productCode" property="productCode"/>
					<html:hidden styleId="updDatetm" property="updDatetm"/>
					<html:hidden styleId="productFractCategory" property="productFractCategory" />
					<html:hidden styleId="numDecAlignment" property="numDecAlignment"/>

					<button type="button" tabindex="450" onclick="onF1()" class="btn_medium">リセット</button>
					<button type="button" tabindex="451" onclick="onF3();" class="btn_medium"
						<c:if test="${!isUpdate}">disabled</c:if> >更新</button>
				</div>
			</s:form>
		</div>
	</div>
</body>

</html>