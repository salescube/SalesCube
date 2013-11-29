<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title><bean:message key='titles.system'/> <bean:message key='titles.dispProductStockList'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>
	<script type="text/javascript" src="${f:url('/scripts/dialogs.js')}"></script>

	<script type="text/javascript">
	<!--
		// ページ読込時の動作
		function init() {
			// 初期フォーカス設定
			$("#productCode").focus();

			applyNumeralStylesToObj(${mineDto.productFractCategory},${mineDto.numDecAlignment},$(".num"));
			_after_load($(".num"));
		}

		// 商品検索
		function openProductSearchDialog(){
			// 商品検索ダイアログを開く
			openSearchProductDialog("0",setProductInfo);
			$("#0_productCode").val($("#productCode").val());
		}

		// 商品情報設定
		function setProductInfo(type, map) {
			$("#productCode").val(map["productCode"]);
		}

		// 初期化
		function onF1(){
			// 入力内容を初期化してよろしいですか？
			if(confirm('<bean:message key="confirm.init" />')){
				window.location.doHref('${f:url("/stock/dispProductStockList")}');
			}
		}

		// 検索
		function onF2(){
			// この条件で検索しますか？
			if ($("#productCode").val().length==0) {
				return;
			}

			// 検索条件を設定する
			var data = createParamData();

			// 検索を実行する
			execSearch(data);
		}

		// 検索パラメータの作成
		function createParamData() {
			data = new Object();
			data["productCode"] = $("#productCode").val();
			return data;
		}

		// 検索実行
		function execSearch(data) {
			// Ajaxリクエストによって検索結果をロードする
			asyncRequest(
				contextRoot + "/ajax/dispProductStockListAjax/search",
				data,
				function(data) {
					// 検索結果テーブルを更新する
					$("#errors").empty();
					$("#listContainer").empty();
					$("#listContainer").append(data);
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
	-->
	</script>
</head>
<body onload="init()" onhelp="return false;">
	<!-- ヘッダ -->
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<!-- メニュー -->
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0010"/>
		<jsp:param name="MENU_ID" value="1006"/>
	</jsp:include>

	<!-- メイン機能 -->
	<div id="main_function">
	
		<!-- タイトル -->
		<span class="title"><bean:message key='titles.dispProductStockList'/></span>

		<!-- ファンクションボタン -->
		<div class="function_buttons">
			<button id="btnF1" tabindex="2000" onclick="onF1()">F1<br><bean:message key='words.action.initialize'/></button>
			<button id="btnF2" tabindex="2001" onclick="onF2()">F2<br><bean:message key='words.action.search'/></button>
			<button disabled="disabled">F3<br>&nbsp;</button>
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

		<s:form onsubmit="return false;">

			<!-- 検索条件 -->
			<div class="function_forms">
				<div id="errors" style="color: red">
					<html:errors/>
				</div>


				<div class="form_section_wrap">
					<div class="form_section">
						<div class="section_title">
							<bean:message key='labels.searchCondition'/><br>
						</div><!-- /.section_title -->
						
						<div id="search_info" class="section_body">
							<table id="product_info" class="forms" summary="productInfo">
								<colgroup>
									<col span="1" style="width: 13%">
									<col span="1" style="width: 87%">
								</colgroup>
								<tr>
									<th><div class="col_title_right"><bean:message key='labels.productCode'/></div></th><!-- 商品コード -->
									<td>
										<html:text property="productCode" styleId="productCode" style="width: 165px; ime-mode: disabled;" tabindex="100"
											onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }"/>
										<html:image src='${f:url("/images//customize/btn_search.png")}' style="vertical-align: middle; cursor: pointer;" onclick="openProductSearchDialog()" tabindex="101" />
									</td>
								</tr>
							</table>
						</div>
					</div><!-- /.form_section -->
		    	</div><!-- /.form_section_wrap -->
			</div>
		</s:form>

		<div style="width: 1160px; text-align: right">
			<button type="button" tabindex="450" onclick="onF1();" class="btn_medium"><bean:message key='words.action.initialize'/></button> <!-- 初期化 -->
			<button type="button" tabindex="451" onclick="onF2();" class="btn_medium"><bean:message key='words.action.search'/></button> <!-- 検索 -->
		</div>
		<br>
		<span id="listContainer">
			<%@ include file="/WEB-INF/view/stock/dispProductStockList/dispStockInfo.jsp" %>
		</span>
	</div>
</body>
</html>
