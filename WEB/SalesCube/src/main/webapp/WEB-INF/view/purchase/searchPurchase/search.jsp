<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title><bean:message key='titles.system'/>　<bean:message key='titles.searchPurchase'/></title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
		var targetTmp = null;
		var paramData = null;
		var paramDataTmp = null;

		// ページ読込時の動作
		function init() {
			// 初期フォーカス設定
			$("#searchTarget").focus();
		}

		// 入力担当者検索
		function openUserSearchDialog(type){
			var id = "UserSearch";

			// 担当者ダイアログを開く
			openSearchUserDialog(type, setUserInfo );
			$("#"+type+"_nameKnj").val($("#userName").val());
		}

		// 入力担当者情報設定
		function setUserInfo(type, map) {
			// 担当者名を設定する
			$("#userName").val(map["nameKnj"]);
		}

		// 仕入先検索
		function openSupplierSearchDialog(type){
			var id = "SupplierSearch";

			// 仕入先検索ダイアログを開く
			openSearchSupplierDialog(type, setSupplierInfo );
			if(type == 1) {
				// 仕入先コードを設定する
				$("#"+type+"_supplierCode").val($("#supplierCode").val());
			} else if(type == 2) {
				// 仕入先名を設定する
				$("#"+type+"_supplierName").val($("#supplierName").val());
			}
		}

		// 仕入先情報設定
		function setSupplierInfo(type, map) {
			if(type == 1) {
				// 仕入先コードを設定する
				$("#supplierCode").val(map["supplierCode"]);
			} else if(type == 2) {
				// 仕入先名を設定する
				$("#supplierName").val(map["supplierName"]);
			}
		}

		// 商品検索
		function openProductSearchDialog(type){
			// 商品検索ダイアログを開く
			openSearchProductDialog(type, setProductInfo );
			if(type == 1) {
				// 商品コードを設定する
				$("#"+type+"_productCode").val($("#productCode").val());
			} else if(type == 2) {
				// 商品名を設定する
				$("#"+type+"_productName").val($("#productAbstract").val());
			}
		}

		// 商品情報設定
		function setProductInfo(type, map) {
			if(type == 1) {
				// 商品コードを設定する
				$("#productCode").val(map["productCode"]);
			} else if(type == 2) {
				// 商品名を設定する
				$("#productAbstract").val(map["productName"]);
			}
		}

		// 初期化
		function onF1(){
			// 入力内容を初期化してよろしいですか？
			if(confirm('<bean:message key="confirm.init" />')){
				window.location.doHref('${f:url("/purchase/searchPurchase")}');
			}
		}

		// 検索
		function onF2(){

			// 検索条件を設定する
			if (targetTmp!=null) {
				if (targetTmp!=$("#searchTarget").val()) {
					$("#sortColumn").val("");
				}
			}
			targetTmp=$("#searchTarget").val();

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

				// 完納区分(deliveryProcessCategory)はActionFormの配列オブジェクトに渡せるように変換する
				if(key == "deliveryProcessCategory"){
					var catArray = paramDataTmp[key];
					for(var i=0;i<catArray.length;i++){
						var h = $(document.createElement("input"));
						h.attr("type", "hidden");
						h.attr("name",key + "[" + i +"]");
						h.val(catArray[i]);
						form.append(h);
					}
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
			openDetailDispSettingDialog('detailDisp', '0801', $('#searchTarget').val());
		}

		// ページ遷移
		function goPage(no) {
			// 検索条件を設定する
			paramData = paramDataTmp;
			paramData["pageNo"] = no;
			// 検索を実行する
			execSearch(paramData);
		}

		// 検索パラメータの作成
		function createParamData() {
			paramData = new Object();
			paramData["searchTarget"] = $("#searchTarget").val();
			paramData["supplierSlipId"] = $("#supplierSlipId").val();
			paramData["poSlipId"] = $("#poSlipId").val();
			paramData["userName"] = $("#userName").val();
			paramData["supplierDateFrom"] = $("#supplierDateFrom").val();
			paramData["supplierDateTo"] = $("#supplierDateTo").val();
			if ($("#searchTarget").val()!=1) {
				paramData["deliveryProcessCategory"] = createDeliveryProcessCategoryArray();
			}
			paramData["supplierCode"] = $("#supplierCode").val();
			paramData["supplierName"] = $("#supplierName").val();
			paramData["remarks"] = $("#remarks").val();
			paramData["productCode"] = $("#productCode").val();
			paramData["productAbstract"] = $("#productAbstract").val();
			paramData["product1"] = $("#product1").val();
			paramData["product2"] = $("#product2").val();
			paramData["product3"] = $("#product3").val();
			paramData["rowCount"] = $("#rowCount").val();
			paramData["sortColumn"] = $("#sortColumn").val();
			paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
			return paramData;
		}

		// 納入区分配列生成
		function createDeliveryProcessCategoryArray() {
			var arry = new Array();
			$("input[type='checkbox'][id^='deliveryProcessCategory_']").each(function() {
				if ($(this).attr("checked") == true) {
					arry.push($(this).val());
				}
			});
			return arry;
		}

		// 検索実行
		function execSearch(paramData) {
			// Ajaxリクエストによって検索結果をロードする
			asyncRequest(
				contextRoot + "/ajax/purchase/searchPurchaseResultAjax/search",
				paramData,
				function(data) {
					// 検索結果テーブルを更新する
					$("#errors").empty();
					$("#listContainer").empty();
					$("#listContainer").append(data);

					// EXCELボタンの状態変更
					if($("#searchResultCount").val() != "0") {
						paramDataTmp = paramData;

						$("#btnF3").attr("disabled","");
					} else {
						$("#btnF3").attr("disabled","disabled");
					}
				},
				function(xmlHttpRequest, textStatus, errorThrown) {
					if (xmlHttpRequest.status == 450) {
						// 検索条件エラー
						$("#errors").empty();
						$("#errors").append(xmlHttpRequest.responseText);
					} else if (xmlHttpRequest.status == 401) {
						// 未ログイン
						alert(xmlHttpRequest.responseText);
						window.location.doHref(contextRoot + "/login");
					} else {
						// その他のエラー
						alert(xmlHttpRequest.responseText);
					}
				}
			);
		}

		// 検索対象のチェンジ処理
		function onChangeTarget() {
			// 検索対象「伝票」
			if ($("#searchTarget").val()==1) {
				$("input[type='checkbox'][id^='deliveryProcessCategory_']").each(function() {
					$(this).attr("disabled","disabled");
				});
			}
			// 検索対象「明細」
			else {
				$("input[type='checkbox'][id^='deliveryProcessCategory_']").each(function() {
					$(this).attr("disabled","");
				});
			}
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
			}
			// 前回と異なる場合は昇順に設定
			else {
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
				paramData["pageNo"] = 1;
				paramData["sortColumn"] = $("#sortColumn").val();
				paramData["sortOrderAsc"] = $("#sortOrderAsc").val();
				// 検索
				execSearch(paramData);
			}
		}
	-->
	</script>
</head>

<body onload="init()" onhelp="return false;">
	<!-- ヘッダ -->
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<!-- メニュー -->
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0008"/>
		<jsp:param name="MENU_ID" value="0801"/>
	</jsp:include>

	<!-- メイン機能 -->
	<div id="main_function">
		<!-- タイトル -->
		<span class="title"><bean:message key='titles.searchPurchase'/></span>

		<!-- ファンクションボタン -->
		<div class="function_buttons">
			<button id="btnF1" tabindex="2000" onclick="onF1()">F1<br><bean:message key='words.action.initialize'/></button>
			<button id="btnF2" tabindex="2001" onclick="onF2()">F2<br><bean:message key='words.action.search'/></button>
			<button id="btnF3" tabindex="2002" onclick="onF3()" disabled="disabled">F3<br><bean:message key='words.name.excel'/></button>
			<button id="btnF4" tabindex="2003" onclick="onF4()">F4<br><bean:message key='words.action.setting'/></button>
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

		<s:form onsubmit="return false;">

			<!-- 検索条件 -->
			<div class="function_forms">
				<div id="errors" style="color: red">
					<html:errors/>
				</div>

				<div class="form_section_wrap">
					<div class="form_section">
						<div class="section_title">
							<span><bean:message key='labels.searchCondition'/></span>
						          <button class="btn_toggle" />
						</div><!-- /.section_title -->

						<div id="search_info" class="section_body">
							<table id="search_target" class="forms" summary="searchTarget">
								<tr>
									<th><div class="col_title_right"><bean:message key='labels.searchTarget'/></div></th> <!-- 検索対象 -->
									<td>
										<html:select property="searchTarget" styleId="searchTarget" tabindex="100" onchange="onChangeTarget()">
											<html:options collection="searchTargetList" property="value" labelProperty="label"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right"><bean:message key='labels.supplierSlipId'/></div></th> <!-- 仕入番号 -->
									<td><html:text property="supplierSlipId" styleId="supplierSlipId" style="width: 150px; ime-mode: disabled;" tabindex="200" /></td>
									<th><div class="col_title_right"><bean:message key='labels.poSlipId'/></div></th> <!-- 発注番号 -->
									<td><html:text property="poSlipId" styleId="poSlipId" style="width: 150px; ime-mode: disabled;" tabindex="201" /></td>
									<th><div class="col_title_right"><bean:message key='labels.userName'/></div></th> <!-- 入力担当者 -->
									<td><html:text property="userName" styleId="userName" style="width: 150px;" tabindex="202" />
										<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openUserSearchDialog()" tabindex="203" />
									</td>
								</tr>
							</table>
							<table id="payment_slip_info1" class="forms" summary="paymentSlipInfo1" style="width: auto;">
								<tr>
									<th><div class="col_title_right"><bean:message key='labels.supplierDate'/></div></th> <!-- 仕入日 -->
										<td style="padding-right: 0;">
											<div class="pos_r">
												<html:text property="supplierDateFrom" styleId="supplierDateFrom" style="text-align:center;ime-mode:disabled; width: 135px;" styleClass="date_input" tabindex="204" />
											</div>
										</td>
										<td style="text-align: center; width:30px; padding-right: 0;">
											<bean:message key='labels.betweenSign'/><!-- ～ -->
										</td>
										<td>
											<div class="pos_r">
												<html:text property="supplierDateTo" styleId="supplierDateTo" style="text-align:center; width: 135px; ime-mode: disabled;" styleClass="date_input" tabindex="205" />
											</div>
										</td>
									<th><div class="col_title_right"><bean:message key='labels.deliveryProcessCategory'/></div></th> <!-- 完納区分 -->
									<td colspan="3">
										<c:forEach var="item" items="${deliveryProcessCategoryList}" varStatus="status">
											<input type="checkbox" id="deliveryProcessCategory_${f:h(status.index)}" value="${item.value}" tabindex="206"/>${f:h(item.label)}&nbsp
										</c:forEach>
									</td>
								</tr>
							</table>
							<table id="payment_slip_info2" class="forms" summary="paymentSlipInfo2">
								<tr>
									<th><div class="col_title_right"><bean:message key='labels.supplierCode'/></div></th> <!-- 仕入先コード -->
									<td>
										<html:text property="supplierCode" styleId="supplierCode" style="width: 150px; ime-mode: disabled;" tabindex="211" />
										<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSupplierSearchDialog(1)" tabindex="212" />
									</td>
									<th><div class="col_title_right"><bean:message key='labels.supplierName'/></div></th> <!-- 仕入先名 -->
									<td colspan="3">
										<html:text property="supplierName" styleId="supplierName" style="width: 450px;" tabindex="213" />
										<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openSupplierSearchDialog(2)" tabindex="214" />
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right"><bean:message key='labels.memorandum'/></div></th> <!-- 摘要 -->
									<td colspan="5">
										<html:text property="remarks" styleId="remarks" style="width: 550px;" tabindex="215" />
									</td>
								</tr>
							</table>
							<table id="product_info" class="forms" summary="productInfo">
								<colgroup>
									<col span="1" style="width: 13%">
									<col span="1" style="width: 20%">
									<col span="1" style="width: 13%">
									<col span="1" style="width: 54%">
								</colgroup>
								<tr>
									<th><div class="col_title_right"><bean:message key='labels.productCode'/></div></th><!-- 商品コード -->
									<td>
										<html:text property="productCode" styleId="productCode" style="width: 165px; ime-mode: disabled;" tabindex="300"
											onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
										<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(1)" tabindex="301" />
									</td>
									<th><div class="col_title_right"><bean:message key='labels.productName'/></div></th> <!-- 商品名 -->
									<td>
										<html:text property="productAbstract" styleId="productAbstract" style="width: 450px;" tabindex="302" />
										<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog(2)" tabindex="303" />
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right"><bean:message key='labels.product1'/></div></th><!-- 分類(大) -->
									<td colspan="3">
										<html:select property="product1" styleId="product1" styleClass="ProductClass1_TopEmpty" style="width: 500px;" tabindex="304">
											<html:options collection="product1List" property="value" labelProperty="label"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right"><bean:message key='labels.product2'/></div></th> <!-- 分類(中) -->
									<td colspan="3">
										<html:select property="product2" styleId="product2" styleClass="ProductClass2_TopEmpty" style="width: 500px;" tabindex="305">
											<html:options collection="product2List" property="value" labelProperty="label"/>
										</html:select>
									</td>
								</tr>
								<tr>
									<th><div class="col_title_right"><bean:message key='labels.product3'/></div></th> <!-- 分類(小) -->
									<td colspan="3">
										<html:select property="product3" styleId="product3" styleClass="ProductClass3_TopEmpty" style="width: 500px;" tabindex="306">
											<html:options collection="product3List" property="value" labelProperty="label"/>
										</html:select>
									</td>
								</tr>
							</table>
						</div>
						<html:hidden property="sortColumn" styleId="sortColumn" />
						<html:hidden property="sortOrderAsc" styleId="sortOrderAsc" />

					</div><!-- /.form_section -->
		    	</div><!-- /.form_section_wrap -->
			</div>
		</s:form>

		<form name="OutputForm" action="${f:url('/purchase/searchPurchaseResultOutput/excel')}" target="_blank" style="display: none;" method="POST">
		</form>

		<div style="width: 1160px; text-align: right">
			<button type="button" tabindex="350" onclick="onF1();" class="btn_medium"><bean:message key='words.action.initialize'/></button> <!-- 初期化 -->
			<button type="button" tabindex="351" onclick="onF2();"class="btn_medium"><bean:message key='words.action.search'/></button> <!-- 検索 -->
		</div>
		<span id="listContainer">
			<%@ include file="/WEB-INF/view/ajax/purchase/searchPurchaseResultAjax/result.jsp" %>
		</span>
	</div>
</body>
</html>
