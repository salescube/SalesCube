<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/> <bean:message key='titles.outputStockList'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>
	<script type="text/javascript">
	<!--

		// ページ読込時の動作
		$(document).ready(function(){
			// 初期フォーカス設定
			$("#periodMonth").focus();
		});

		// 初期化
		function onF1(){
			// 入力内容を初期化してよろしいですか？
			if(confirm('<bean:message key="confirm.init" />')){
				showNowSearchingDiv();
				<bean:define id="concatUrl" value="${'/stock/outputStockList'}" />
				location.doHref('${f:url(concatUrl)}');
			}
		}

		// Excel
		function onF3(){
			// この内容でEXCEL出力しますか？
			if(!confirm('<bean:message key="confirm.excel" />')){
				return;
			}

			// 検索条件を設定する
			data = createParamData();
			// 検索を実行する
			execSearch(data);
		}

		// 検索パラメータの作成
		function createParamData() {
			var data = new Object();
			data["periodMonth"] = _getNumStr($("#periodMonth").val());
			data["radioCond2"] = $("input[name='radioCond2']:checked").val();
			data["allocatedQuantity"] = _getNumStr($("#allocatedQuantity").val());
			data["allocatedQuantityWithComma"] = $("#allocatedQuantity").val();
			data["excludeRoNotExists"] = $("#excludeRoNotExists").attr("checked");
			data["excludeSalesCancel"] = $("#excludeSalesCancel").attr("checked");
			data["excludeNotManagementStock"] = $("#excludeNotManagementStock").attr("checked");
			data["excludeMultiRack"] = $("#excludeMultiRack").attr("checked");
			return data;
		}

		// 検索実行
		function execSearch(data) {
			// Ajaxリクエストによって検索結果をロードする
			asyncRequest(
				contextRoot + "/ajax/outputStockListAjax/search",
				data,
				function(data) {
					window.open(contextRoot + "/ajax/outputStockListAjax/excel","<bean:message key='words.name.excel'/>");
				}
			);
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
		<jsp:param name="MENU_ID" value="1004"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">

		<span class="title"><bean:message key='titles.outputStockList'/></span>

		<div class="function_buttons">
			<button type="button" id="btnF1" tabindex="2000" onclick="onF1();">F1<br><bean:message key='words.action.initialize'/><%// 初期化 %></button>
			<button type="button" id="btnF2" tabindex="2001" disabled>F2<br>&nbsp;</button>
			<button type="button" id="btnF3" tabindex="2002" onclick="onF3();">F3<br><bean:message key='words.name.excel'/><%// EXCEL %></button>
			<button type="button" id="btnF4" tabindex="2003" disabled>F4<br>&nbsp;</button>
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

				<div id="ajax_errors" style="padding-left: 20px">
					<html:errors />
				</div>

			    <div class="form_section_wrap">
			    <div class="form_section">
			        <div class="section_title">
						<span>在庫一覧表</span>
				        <button class="btn_toggle" />
			        </div>

					<div class="section_body">
					<table id="search_info1" class="forms" summary="出力条件">
						<colgroup>
							<col span="1" style="width: 20%">
							<col span="1" style="width: 80%">
						</colgroup>
						<tr>
							<th rowspan="2"><div class="col_title_right"><bean:message key='labels.includeProductsSearchCondition'/></div></th><%// 商品の抽出条件 %>
							<td>
								<html:text property="periodMonth" styleId="periodMonth" styleClass="c_referable numeral_commas" style="width:50px; ime-mode: disabled;" tabindex="100" maxlength="3" />&nbsp;<bean:message key='labels.include.orderReultProducts'/><br>
							</td>
						</tr>
						<tr>
							<td>
								<html:radio property="radioCond2" styleId="radioCond2_${RADIO_COND2_VALUE_0}" value="${RADIO_COND2_VALUE_0}" tabindex="101" /><label for="radioCond2_${RADIO_COND2_VALUE_0}"><bean:message key='labels.include.all'/></label><br><%// 全件 %>
								<html:radio property="radioCond2" styleId="radioCond2_${RADIO_COND2_VALUE_1}" value="${RADIO_COND2_VALUE_1}" tabindex="102" /><label for="radioCond2_${RADIO_COND2_VALUE_1}"><bean:message key='labels.include.holdingQuantity.eq.less.orderPoint'/></label><br><%// 保有数＜発注点 %>
								<html:radio property="radioCond2" styleId="radioCond2_${RADIO_COND2_VALUE_2}" value="${RADIO_COND2_VALUE_2}" tabindex="103" /><label for="radioCond2_${RADIO_COND2_VALUE_2}"><bean:message key='labels.include.stockQuantity.eq.less.orderPoint'/></label><br><%// 現在庫数＜発注点 %>
								<html:radio property="radioCond2" styleId="radioCond2_${RADIO_COND2_VALUE_3}" value="${RADIO_COND2_VALUE_3}" tabindex="104" /><label for="radioCond2_${RADIO_COND2_VALUE_3}"><bean:message key='labels.include.allocatableQuantity.eq.less.orderPoint'/></label><br><%// 引当可能数＜発注点（発注点がゼロのものは除く） %>
								<html:radio property="radioCond2" styleId="radioCond2_${RADIO_COND2_VALUE_4}" value="${RADIO_COND2_VALUE_4}" tabindex="105" /><label for="radioCond2_${RADIO_COND2_VALUE_4}"><bean:message key='labels.include.allocatableQuantityPlus.eq.less.orderPoint'/></label><br><%// 引当可能数＋発注残数＜発注点（発注点がゼロのものは除く） %>
								<html:radio property="radioCond2" styleId="radioCond2_${RADIO_COND2_VALUE_5}" value="${RADIO_COND2_VALUE_5}" tabindex="106" /><label for="radioCond2_${RADIO_COND2_VALUE_5}"><bean:message key='labels.include.allocatableQuantity'/></label>&nbsp;<html:text property="allocatedQuantity" styleId="allocatedQuantity" styleClass="c_referable numeral_commas" style="width:50px; ime-mode: disabled;" tabindex="106" maxlength="5" />&nbsp;<label for="radioCond2_${RADIO_COND2_VALUE_4}"><bean:message key='labels.include.quantity.eq.less'/></label><br><%// 引当可能数ｎ個以下 %>
								<html:radio property="radioCond2" styleId="radioCond2_${RADIO_COND2_VALUE_6}" value="${RADIO_COND2_VALUE_6}" tabindex="107" /><label for="radioCond2_${RADIO_COND2_VALUE_6}"><bean:message key='labels.include.over.maxHoldingQuantity'/></label><br><%// 最大保有数超過品  %>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right"><bean:message key='labels.excludeProductsSearchCondition'/></div></th><%// 商品の除外条件 %>
							<td>
								<html:checkbox property="excludeRoNotExists" styleId="excludeRoNotExists" tabindex="200" /><label for="excludeRoNotExists"><bean:message key='labels.exclude.roNotExists'/></label><br><%// 受注実績のない商品は除く %>
								<html:checkbox property="excludeSalesCancel" styleId="excludeSalesCancel" tabindex="201" /><label for="excludeSalesCancel"><bean:message key='labels.exclude.salesCancel'/></label><br><%// 販売中止品(発注停止品)は除く %>
								<html:checkbox property="excludeNotManagementStock" styleId="excludeNotManagementStock" tabindex="202" /><label for="excludeNotManagementStock"><bean:message key='labels.exclude.notManagementStock'/></label><br><%// 在庫管理しない商品（都度調達品）は除く %>
								<html:checkbox property="excludeMultiRack" styleId="excludeMultiRack" tabindex="203" /><label for="excludeMultiRack"><bean:message key='labels.exclude.multiRack'/></label><br><%// 重複可能な棚番の商品は除く %>
							</td>
						</tr>
					</table>
			        </div><!-- /.section_body -->
			    </div><!-- /.form_section -->
			    </div><!-- /.form_section_wrap -->
			</div>
		</s:form>
	</div>

</body>

</html>
