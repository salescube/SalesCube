<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/> <bean:message key='titles.searchEntrustStock'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>
	<script type="text/javascript">
	<!--
		var paramData = null;
		var paramDataTmp = null;

		// ページ読込時の動作
		$(document).ready(function(){
			// 初期フォーカス設定
			$("#searchTarget").focus();
		});

		// 初期化
		function onF1(){
			if(confirm('<bean:message key="confirm.init" />')){
				<bean:define id="concatUrl" value="${'/stock/searchEntrustStock'}" />
				location.doHref('${f:url(concatUrl)}');
			}
		}

		// 検索
		function onF2(){

			// 検索条件を設定する
			paramData = createParamData();
			paramData["pageNo"] = 1;
			// 検索を実行する
			execSearch(paramData);
		}

		// EXCEL
		function onF3(){
			// 検索結果をEXCELファイルでダウンロードしますか？
			if(!confirm('<bean:message key="confirm.excel.result" />')){
				return;
			}

			var form = $(window.document.forms["OutputForm"]);
			form.empty();

			for(var key in paramDataTmp) {
				if(!paramDataTmp[key] ||
						(typeof paramDataTmp[key].length != undefined && paramDataTmp[key].length == 0)) {
					continue;
				}

				var hidden = $(document.createElement("input"));
				hidden.attr("type", "hidden");
				hidden.attr("name", key);
				hidden.val(paramDataTmp[key]);
				form.append(hidden);
			}

			form.submit();
		}

		// 設定
		function onF4(){
			openDetailDispSettingDialog('detailDisp', '1008', $('#searchTarget').val());
		}

		// 検索パラメータの作成
		function createParamData() {
			paramData = new Object();
			// 検索対象
			paramData["searchTarget"] = $("#searchTarget").val();

			// 伝票分類
			paramData["entrustEadCategoryEnter"] = $("#entrustEadCategoryEnter").attr("checked");
			paramData["entrustEadCategoryDispatch"] = $("#entrustEadCategoryDispatch").attr("checked");
			paramData["entrustEadCategoryDispatchNoPrint"] = $("#entrustEadCategoryDispatchNoPrint").attr("checked");

			// 検索条件
			paramData["entrustEadSlipId"] = $("#entrustEadSlipId").val();
			paramData["poSlipId"] = $("#poSlipId").val();
			paramData["userName"] = $("#userName").val();
			paramData["entrustEadDateFrom"] = $("#entrustEadDateFrom").val();
			paramData["entrustEadDateTo"] = $("#entrustEadDateTo").val();
			paramData["remarks"] = $("#remarks").val();
			paramData["productCode"] = $("#productCode").val();
			paramData["productAbstract"] = $("#productAbstract").val();
			paramData["supplierCode"] = $("#supplierCode").val();
			paramData["supplierName"] = $("#supplierName").val();
			paramData["product1"] = $("#product1").val();
			paramData["product2"] = $("#product2").val();
			paramData["product3"] = $("#product3").val();

			// 結果表示情報
			paramData["rowCount"] = $("#rowCount").val();
			paramData["sortColumn"] = $("#sortColumn").val();
			paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
			return paramData;
		}

		// 検索実行
		function execSearch(paramData) {
			// Ajaxリクエストによって検索結果をロードする
			asyncRequest(
				contextRoot + "/ajax/stock/searchEntrustStockResultAjax/search",
				paramData,
				function(data) {
					// 検索結果テーブルを更新する
					$("#listContainer").empty();
					$("#listContainer").html(data);

					// 1件以上ヒットした場合
					if($("#searchResultCount").val() != "0") {
						// 検索条件を保持
						paramDataTmp = paramData;
						// EXCELボタンの状態変更
						$("#btnF3").attr("disabled", false);
					} else {
						// EXCELボタンの状態変更
						$("#btnF3").attr("disabled", true);
					}
				}
			);
		}

		/**
		 * 担当者検索ダイアログを開く
		 */
		function openUserSearchDialog(){
			var id = "UserSearch";

			// 担当者検索ダイアログを開く
			openSearchUserDialog(
				id,
				function(id, map) {
					$("#userId").val(map["userId"]);
					$("#userName").val(map["nameKnj"]);
				}
			);

			// 担当者コードを設定する
			$("#"+id+"_nameKnj").val($("#userName").val());
		}

		/**
		 * 商品検索ダイアログを開く
		 * @param type 1:商品コード, 2:商品名
		 */
		function openProductSearchDialog(type){
			// 商品検索ダイアログを開く
			openSearchProductDialog(
				type,
				function(id, map) {
					if(id == 1) {
						$("#productCode").val(map["productCode"]);
					} else if(id == 2) {
						$("#productAbstract").val(map["productName"]);
					}
				}
			);

			if(type == 1) {
				$("#"+type+"_productCode").val($("#productCode").val());
			} else if(type == 2) {
				$("#"+type+"_productName").val($("#productAbstract").val());
			}
		}

		/**
		 * 仕入先検索ダイアログを開く
		 * @param type 1:仕入先コード、2:仕入先名
		 */
		function openSupplierSearchDialog(type){
			var id = "SupplierSearch";

			// 仕入先検索ダイアログを開く
			openSearchSupplierDialog(
				type,
				function(id, map) {
					if(id == 1) {
						$("#supplierCode").val(map["supplierCode"]);
					} else if(id == 2) {
						$("#supplierName").val(map["supplierName"]);
					}
				}
			);

			if(type == 1) {
				$("#"+type+"_supplierCode").val($("#supplierCode").val());
			} else if(type == 2) {
				$("#"+type+"_supplierName").val($("#supplierName").val());
			}
		}

		// ページ遷移
		function goPage(no) {
			// 検索条件を設定する
			paramData = paramDataTmp;
			paramData["pageNo"] = no;
			// 検索を実行する
			execSearch(paramData);
		}

		// ソート
		function sort(sortColumn) {
			// 前回のソートカラムとソート順を取得
			var beforeSortColumn = $("#sortColumn").val();
			var beforeSortOrderAsc = $("#sortOrderAsc").val();
			// 前回のソートカラムからソートラベルを削除
			if($("#sortStatus_"+beforeSortColumn).get(0)) {
				$("#sortStatus_"+beforeSortColumn).empty();
			}
			// 今回のソートカラムを設定
			$("#sortColumn").val(sortColumn);
			// 前回と同じカラムをクリックした場合はソート順を入れ替える
			if(beforeSortColumn == sortColumn) {
				if(beforeSortOrderAsc == "true") {
					$("#sortOrderAsc").val("false");
				} else {
					$("#sortOrderAsc").val("true");
				}
			} else {
				// 前回と異なる場合は昇順に設定
				$("#sortOrderAsc").val("true");
			}
			// 今回のソートカラムにソートラベルを追加
			if($("#sortOrderAsc").val() == "true") {
				$("#sortStatus_"+sortColumn).html("<bean:message key='labels.asc'/>");
			} else {
				$("#sortStatus_"+sortColumn).html("<bean:message key='labels.desc'/>");
			}
			// 1回以上検索しており、前回の結果が1件以上ある場合のみ再検索
			if(paramDataTmp != null && $("#searchResultCount").val() != "0") {
				// 前回の検索条件からソート条件のみを変更
				paramData = paramDataTmp;
				paramData["sortColumn"] = $("#sortColumn").val();
				paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
				paramData["pageNo"] = 1;
				// 検索
				execSearch(paramData);
			}
		}

		// 画面遷移
		function doHref(url) {
			// 実行中表示をする
			showNowSearchingDiv();
			// 画面遷移する
			location.doHref(url);
		}
	-->
	</script>
</head>
<body>
	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0010"/>
		<jsp:param name="MENU_ID" value="1008"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">

		<span class="title"><bean:message key='titles.searchEntrustStock'/></span>

		<div class="function_buttons">
			<button type="button" id="btnF1" tabindex="2000" onclick="onF1();">F1<br><bean:message key='words.action.initialize'/><%// 初期化 %></button>
			<button type="button" id="btnF2" tabindex="2001" onclick="onF2();">F2<br><bean:message key='words.action.search'/><%// 検索 %></button>
			<button type="button" id="btnF3" tabindex="2002" onclick="onF3();" disabled>F3<br><bean:message key='words.name.excel'/><%// EXCEL %></button>
			<button type="button" id="btnF4" tabindex="2003" onclick="onF4();">F4<br><bean:message key='words.action.setting'/><%// 設定 %></button>
			<button type="button" id="btnF5" tabindex="2004" disabled>F5<br>&nbsp;</button>
			<button type="button" id="btnF6" tabindex="2005" disabled>F6<br>&nbsp;</button>
			<button type="button" id="btnF7" tabindex="2006" disabled>F7<br>&nbsp;</button>
			<button type="button" id="btnF8" tabindex="2007" disabled>F8<br>&nbsp;</button>
			<button type="button" id="btnF9" tabindex="2008" disabled>F9<br>&nbsp;</button>
			<button type="button" id="btnF10" tabindex="2009" disabled>F10<br>&nbsp;</button>
			<button type="button" id="btnF11" tabindex="2010" disabled>F11<br>&nbsp;</button>
			<button type="button" id="btnF12" tabindex="2011" disabled>F12<br>&nbsp;</button>
		</div>
		<br><br><br>

		<s:form onsubmit="return false;">

			<div class="function_forms">

				<div id="ajax_errors" style="color: red">
					<html:errors />
				</div>

				<div class="form_section_wrap">
				<div class="form_section">
					<div class="section_title">
						<span><bean:message key='labels.searchCondition'/></span>
						<br>
						<button class="btn_toggle" />
					</div>

					<div class="section_body">
						<table id="search_info1" class="forms" summary="search_info1" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.searchTarget'/></div></th><%// 検索対象 %>
								<td>
									<html:select property="searchTarget" styleId="searchTarget" tabindex="100" onchange="$('#sortColumn').val('');">
										<html:options collection="searchTargetList" property="value" labelProperty="label"/>
									</html:select>
									&nbsp;
									<html:checkbox property="entrustEadCategoryEnter" styleId="entrustEadCategoryEnter" tabindex="101" /><label for="entrustEadCategoryEnter"><bean:message key='labels.entrustEadCategoryEnter'/></label>
									<html:checkbox property="entrustEadCategoryDispatch" styleId="entrustEadCategoryDispatch" tabindex="102" /><label for="entrustEadCategoryDispatch"><bean:message key='labels.entrustEadCategoryDispatch'/></label>
									<html:checkbox property="entrustEadCategoryDispatchNoPrint" styleId="entrustEadCategoryDispatchNoPrint" tabindex="103" /><label for="entrustEadCategoryDispatchNoPrint"><bean:message key='labels.entrustEadCategoryDispatchNoPrint'/></label>
								</td>
							</tr>
						</table>

						<table id="search_info2" class="forms" summary="search_info2">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.entrustEadSlipId'/></div></th><%// 委託入出庫伝票番号 %>
								<td>
									<html:text property="entrustEadSlipId" styleId="entrustEadSlipId" style="ime-mode: disabled;" tabindex="200" />
								</td>
								<th><div class="col_title_right"><bean:message key='labels.poSlipId'/></div></th><%// 発注伝票番号 %>
								<td>
									<html:text property="poSlipId" styleId="poSlipId" style="ime-mode: disabled;" tabindex="201" />
								</td>
								<th><div class="col_title_right"><bean:message key='labels.userName'/></div></th><%// 入力担当者 %>
								<td>
									<html:hidden property="userId" styleId="userId" />
									<html:text property="userName" styleId="userName" tabindex="202" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openUserSearchDialog()" tabindex="203" />
								</td>
							</tr>
						</table>

						<table id="search_info3" class="forms" summary="search_info3" style="width: auto;">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.entrustEadDate'/></div></th><%// 入出庫日 %>
								<td style="padding-right: 0;">
									<div class="pos_r">
										<html:text property="entrustEadDateFrom" styleId="entrustEadDateFrom" styleClass="date_input" style="text-align:center; width: 135px; ime-mode: disabled;" tabindex="204" />
									</div>
								</td>
								<td style="text-align: center; width:30px; padding-right: 0;">
									<bean:message key='labels.betweenSign'/><!-- ～ -->
								</td>
								<td>
									<div class="pos_r">
										<html:text property="entrustEadDateTo" styleId="entrustEadDateTo" styleClass="date_input" style="text-align:center; width: 135px; ime-mode: disabled;" tabindex="205" />
									</div>
								</td>
							</tr>
						</table>

						<table id="search_info4" class="forms" summary="search_info4">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.remarks'/></div></th><%// 理由 %>
								<td>
									<html:text property="remarks" styleId="remarks" style="width: 500px" tabindex="206" />
								</td>
							</tr>
						</table>

						<table id="search_info5" class="forms" summary="search_info5">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.supplierCode'/></div></th><%// 仕入先コード %>
								<td>
									<html:text property="supplierCode" styleId="supplierCode" styleClass="c_referable" style="width: 165px; ime-mode: disabled;" tabindex="300" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSupplierSearchDialog(1)" tabindex="301" />
								</td>
								<th><div class="col_title_right"><bean:message key='labels.supplierName'/></div></th><%// 仕入先名 %>
								<td>
									<html:text property="supplierName" styleId="supplierName" styleClass="c_referable" style="width: 250px;" tabindex="302" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSupplierSearchDialog(2)" tabindex="303" />
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.productCode'/></div></th><%// 商品コード %>
								<td>
									<html:text property="productCode" styleId="productCode" styleClass="c_referable" style="width: 165px; ime-mode: disabled;" tabindex="306"
										onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(1)" tabindex="307" />
								</td>
								<th><div class="col_title_right"><bean:message key='labels.productName'/></div></th><%// 商品名 %>
								<td>
									<html:text property="productAbstract" styleId="productAbstract" styleClass="c_referable" style="width: 250px;" tabindex="308" />
									<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(2)" tabindex="309" />
								</td>
							</tr>
						</table>

						<table id="search_info6" class="forms" summary="search_info6">
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.product1'/></div></th><%// 分類(大) %>
								<td>
									<html:select property="product1" styleId="product1" styleClass="ProductClass1_TopEmpty" style="width: 500px;" tabindex="312">
										<html:options collection="product1List" property="value" labelProperty="label"/>
									</html:select>
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.product2'/></div></th><%// 分類(中) %>
								<td>
									<html:select property="product2" styleId="product2" styleClass="ProductClass2_TopEmpty" style="width: 500px;" tabindex="313">
										<html:options collection="product2List" property="value" labelProperty="label"/>
									</html:select>
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right"><bean:message key='labels.product3'/></div></th><%// 分類(小) %>
								<td>
									<html:select property="product3" styleId="product3" styleClass="ProductClass3_TopEmpty" style="width: 500px;" tabindex="314">
										<html:options collection="product3List" property="value" labelProperty="label"/>
									</html:select>
								</td>
							</tr>
						</table>
					</div>
				</div>
				</div>
			</div>
			<html:hidden property="sortColumn" styleId="sortColumn" />
			<html:hidden property="sortOrderAsc" styleId="sortOrderAsc" />
		</s:form>

		<form name="OutputForm" action="${f:url('/stock/searchEntrustStockResultOutput/excel')}" target="_blank" style="display: none;" method="POST">
		</form>

		<div style="width: 1160px; text-align: right">
			<button type="button" onclick="onF1();" tabindex="350" class="btn_medium"><bean:message key='words.action.initialize'/><%// 初期化 %></button>
			<button type="button" onclick="onF2();" tabindex="351" class="btn_medium"><bean:message key='words.action.search'/><%// 検索 %></button>
		</div>

		<span id="listContainer">
			<%-- 検索結果領域 --%>
			<%@ include file="/WEB-INF/view/ajax/stock/searchEntrustStockResultAjax/result.jsp" %>
		</span>

	</div>
</body>
</html>
