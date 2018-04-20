<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><bean:message key='titles.system'/> 単価照会</title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>
	<script type="text/javascript" src="${f:url('/scripts/dialogs.js')}"></script>
<script type="text/javascript">
<!--
var MAIN_FORM_NAME = "estimate_dispProductPriceListActionForm";

var paramData = null;
var paramDataTmp = null;
var data = null;
// 数量の丸め（桁=自社マスタ.数量少数桁、丸め=自社マスタ.商品端数処理）
var quantityAlignment = ${mineDto.numDecAlignment};
var quantityCategory = ${mineDto.productFractCategory};
var rateAlignment = ${mineDto.statsDecAlignment};
var rateCategory = scale_half_up;
var priceAlignment = scale_0;
var priceCategory = ${mineDto.priceFractCategory};;

//ページ読込時の動作
function init() {
	applyNumeralStylesToObj(quantityCategory,quantityAlignment,$(".dataFrom"));
	applyNumeralStylesToObj(quantityCategory,quantityAlignment,$(".dataTo"));
	applyNumeralStylesToObj(rateCategory,rateAlignment,$(".discountRate"));
	applyNumeralStylesToObj(priceAlignment,priceAlignment,$(".Price"));
	// 初期フォーカス
	$('#productCode').focus();

	// カンマをつける
	_after_load($(".dataFrom"));
	_after_load($(".dataTo"));
	_after_load($(".discountRate"));
	_after_load($(".Price"));


}


// 初期化
function onF1(){
	// 入力内容を初期化してよろしいですか？
	if(confirm('<bean:message key="confirm.init" />')){
		showNowSearchingDiv();
		location.doHref('${f:url("/estimate/dispProductPriceList")}');
	}
}

// 検索ボタンによる検索処理
function onF2() {

	showNowSearchingDiv();
	$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/estimate/dispProductPriceList/show")}');
	$("form[name='" + MAIN_FORM_NAME + "']").submit();
	return;

}

/**
* 商品検索ダイアログを開く
*/
function productSearch(jqObject) {
	var dialogId = jqObject.attr("id") + "Dialog";
	openSearchProductDialog(
		dialogId,
		function(id, map) {
			jqObject.val( map[ "productCode" ] );
		}
	);

	// ダイアログのフィールドに値をセットしてフォーカス
	$("#" + dialogId + "_productCode").val( jqObject.val() );
	$("#" + dialogId + "_productCode").focus();

}

//在庫ボタンクリック
function openStockInfo() {
	// 在庫ダイアログを開く
	var productCode = $("#productCode").val();
	if(!productCode){
		return;
	}

	openStockInfoDialog('stockInfo', productCode);
}
-->
</script>
</head>
<body onload="init()">
	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0002"/>
		<jsp:param name="MENU_ID" value="0203"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">

		<!-- タイトル -->
		<span class="title">単価参照</span>

		<div class="function_buttons">
			<button id="btnF1" type="button" onclick="onF1();" tabindex="2000">F1<br><bean:message key='words.action.initialize'/></button>
			<button id="btnF2" type="button" onclick="onF2();" tabindex="2001">F2<br><bean:message key='words.action.search'/></button>
			<button type="button" disabled="disabled">F3<br>&nbsp;</button>
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

		<s:form onsubmit="return false;">
			<div class="function_forms">
			<div id="errors" style="color: red">
				<html:errors />
			</div>

		    <div class="form_section_wrap">
		    <div class="form_section">
		    	<div class="section_title">
					<span><bean:message key='labels.searchCondition'/></span>
	            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="search_info" class="section_body">
					<table id="order_info" class="forms" summary="商品情報">
						<colgroup>
							<col span="1" style="width: 15%">
							<col span="1" style="width: 85%">
						</colgroup>
						<tr>
							<th><div class="col_title_right">商品コード</div></th>
							<td><html:text property="productCode" styleId="productCode" tabindex="100" style="width: 165px; ime-mode:disabled;"
									onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){ this.value=this.value.toUpperCase(); }" />
							<html:image styleId="productCodeImg" tabindex="101" src="${f:url('/images//customize/btn_search.png')}" style="vertical-align: middle; cursor: pointer;" onclick="productSearch($('#productCode'));" />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<!-- 無理矢理空白をあけた -->
							<button type="button" onclick="openStockInfo();" tabindex="102" class="btn_list_action">在庫</button>
							</td>
						</tr>
					</table>
				</div>
	    	</div><!-- /.form_section -->
	    	</div><!-- /.form_section_wrap -->
		</div>
		</s:form>

		<div style="width: 1160px; text-align: right">
			<button type="button" onclick="onF1();" tabindex="103" class="btn_medium"><bean:message key='words.action.initialize'/></button> <!-- 初期化 -->
			<button type="button" onclick="onF2();" tabindex="104" class="btn_medium"><bean:message key='words.action.search'/></button> <!-- 検索 -->
		</div>
		<span id="ListContainer">

		    <div class="form_section_wrap">
		    <div class="form_section">
		    	<div class="section_title">
					<span>商品情報</span>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="search_info" class="section_body">
				<table id="discount_info" class="forms detail_info" summary="商品情報" >
					<colgroup>
						<col span="1" style="width: 15%">
						<col span="1" style="width: 60%">
						<col span="1" style="width: 10%">
						<col span="1" style="width: 15%">
					</colgroup>
					<tr>
						<th class="rd_top_left" style="height: 30px; border-bottom: 1px solid #555555;">商品名</th>
						<td>
						<c:out value="${f:h(productName)}" />
						</td>
						<th style="height: 30px;">売単価</th>
						<td style="text-align: right;" class="rd_top_right">
							<span class="numeral_commas scale_round_down scale_0" >
								<c:out value="${f:h(retailPrice)}" />
							</span>
						</td>
					</tr>
					<tr>
						<th class="rd_bottom_left" style="height: 30px;">備考</th>
						<td colspan="3" class="rd_bottom_right"><c:out value="${f:h(productRemarks)}" /></td>
					</tr>
				</table>
				</div>
	    	</div><!-- /.form_section -->
	    	</div><!-- /.form_section_wrap -->


		    <div class="form_section_wrap">
		    <div class="form_section">
		    	<div class="section_title">
					<span>割引情報</span>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="search_info" class="section_body">
				<table id="discount_info" class="forms detail_info" summary="割引パターン情報" >
					<colgroup>
						<col span="1" style="width: 15%">
						<col span="1" style="width: 25%">
						<col span="1" style="width: 15%">
						<col span="1" style="width: 45%">
					</colgroup>
					<tr>
						<th class="rd_top_left" style="height: 30px; border-bottom: 1px solid #555555;">割引コード</th>
						<td>
						<c:out value="${f:h(discountId)}" />
						</td>
						<th style="height: 30px;">割引名</th>
						<td style="text-align: right;" class="rd_top_right">
						<c:out value="${f:h(discountName)}" />
						</td>
					</tr>
					<tr>
						<th class="rd_bottom_left" style="height: 30px;">備考</th>
						<td colspan="3" class="rd_bottom_right">
						<c:out value="${f:h(remarks)}" />
						</td>
					</tr>
				</table>
				</div>
	    	</div><!-- /.form_section -->
	    	</div><!-- /.form_section_wrap -->


		    <div class="form_section_wrap">
		    <div class="form_section">
		    	<div class="section_title">
					<span>数量スライド設定</span>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="search_info" class="section_body">
				<table id="discountTrnList" class="forms detail_info" summary="数量スライド設定" style="width: 600px;">
					<colgroup>
						<col span="1" style="width: 10%">
						<col span="1" style="width: 10%">
						<col span="1" style="width: 10%">
						<col span="1" style="width: 10%">
						<col span="1" style="width: 10%">
						<col span="1" style="width: 10%">
					</colgroup>
					<tr>
						<th class="rd_top_left" style="height: 30px;">no</th>
						<th colspan="3" class="xl64  style="height: 30px;">数量範囲</th>
						<th style="height: 30px;">割引率</th>
						<th class="rd_top_right" style="height: 30px;">単価</th>
					</tr>
		            <c:forEach var="discountTrnList" varStatus="s" items="${discountTrnList}">
					<tr id="discountTrnList_${s.index}">
						<td id="discountTrnList_${s.index}.no" style="text-align: center">${s.index+1}
		                </td>
						<td style="text-align: right; border-width: 1px 0px;" >
							<span class="dataFrom" >
								<c:out value="${f:h(discountTrnList.dataFrom)}" />
							</span>
		                </td>
						<td style="text-align: center; border-width: 1px 0px;" >
							～
		                </td>
						<td style="text-align: right; border-width: 1px 0px; padding-right: 30px; ">
							<span class="dataTo" >
								<c:out value="${f:h(discountTrnList.dataTo)}" />
							</span>
		                </td>
						<td style="text-align: right" >
							<span class="discountRate" >
								<c:out value="${f:h(discountTrnList.discountRate)}" />
							</span>％
		                </td>
						<td style="text-align: right">
							<span class="Price" >
								<c:if test="${retailPrice != ''}" >
									<c:out value="${f:h(retailPrice - ( discountTrnList.discountRate / 100 * retailPrice ) )}" />
								</c:if>
							</span>
		                </td>
					</tr>
		            <script type="text/javascript">
		            </script>
		            </c:forEach>
				</table>
				</div>
	    	</div><!-- /.form_section -->
	    	</div><!-- /.form_section_wrap -->
 		</span>
	</div>
</body>
</html>
