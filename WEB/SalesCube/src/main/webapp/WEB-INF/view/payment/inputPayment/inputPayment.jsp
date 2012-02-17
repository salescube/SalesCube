<%@page import="jp.co.arkinfosys.common.Constants" %>
<%@page import="jp.co.arkinfosys.common.CategoryTrns" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title><bean:message key='titles.system'/> <bean:message key='titles.inputPayment'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
		var MAIN_FORM_NAME = "payment_inputPaymentActionForm";

		var taxShiftCategorySlipTotal = "<%=CategoryTrns.TAX_SHIFT_CATEGORY_SLIP_TOTAL%>";
		var taxShiftCategoryCloseTheBooks = "<%=CategoryTrns.TAX_SHIFT_CATEGORY_CLOSE_THE_BOOKS%>";

		//ページ読込時初期化処理
		$(function() {


			// フォーカス設定
			// $("#paymentDate").focus();

			//買掛系の金額自動計算（単体懸案 #106）
			manager = new SlipPriceManager();
			manager.init();

			// 初期フォーカス
			if( $("#paymentSlipId").val() != "" ){
				$("#paymentSlipId").attr("readOnly", "true");
				$("#paymentSlipId").addClass("c_disable");
				$("#paymentDate").focus();
				// 発注番号も入力不可とする。
				$("#poSlipId").attr("readOnly", "true");
				$("#poSlipId").addClass("c_disable");
			}else{
				// 発注番号が空欄ではない（伝票複写した）
				if( $("#poSlipId").val() != "" ){
					$("#paymentSlipId").attr("readOnly", "true");
					$("#paymentSlipId").addClass("c_disable");
					$("#paymentDate").focus();
				}
				else{
					$("#paymentSlipId").focus();
				}
			}
		});

		function findSlip(){
			if( $("#paymentSlipId").val() == "" ){
			}else{
				showNowSearchingDiv();
				$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/payment/inputPayment/load")}');
				$("form[name='" + MAIN_FORM_NAME + "']").submit();
			}
		}
		// 伝票複写（発注番号入力）
		function findCopy(){
			if(( $("#poSlipId").val() != "" )&&(!$("#poSlipId").attr("readOnly"))){
				if(confirm("<bean:message key='confirm.copyslip'/>")){

					$("#paymentSlipId").val("");
					$("#paymentSlipId").attr("readOnly", "true");
					$("#paymentSlipId").addClass("c_disable");

					$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/payment/inputPayment/copy")}');
					$("form[name='" + MAIN_FORM_NAME + "']").submit();
				}
				else{
					$("#poSlipId").val("");
				}
			}
		}
		// 削除ボタン
		function onF2(){
			if(confirm('<bean:message key="confirm.delete" />')){
				$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/payment/inputPayment/delete")}');
				$("form[name='" + MAIN_FORM_NAME + "']").submit();
			}
		}

		// 初期化ボタン
		function onF1(){
			if(confirm('<bean:message key="confirm.init" />')){
				window.location.doHref('${f:url("/payment/inputPayment")}');

			}
		}

		// 登録ボタン
		function onF3(){
			<c:if test="${new}">
			if(confirm('<bean:message key="confirm.insert" />')){
			</c:if>
			<c:if test="${!new}">
			if(confirm('<bean:message key="confirm.update" />')){
			</c:if>
				manager.prepareSubmit();
				$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/payment/inputPayment/upsert")}');
				$("form[name='" + MAIN_FORM_NAME + "']").submit();
			}
		}

		// 伝票複写ボタン
		function onF6(){
			// 伝票複写画面を表示
			openCopySlipDialog( '0900', 'copySlip5', callbackSlipCopy );

		}

		/**
		 * 伝票複写画面から呼び出されるコールバック関数
		 */
		function callbackSlipCopy(dialogId, slipName, poSlipId ){
			// 初期状態でない場合はメッセージ表示して確認
			if(isDefault()==false){
				if(confirm("入力された内容を破棄して複写しますか？") == false){
					return;
				}
			}

			$("#poSlipId").val(poSlipId);
			$("form[name='" + MAIN_FORM_NAME + "']").attr("action", '${f:url("/payment/inputPayment/copy")}');
			$("form[name='" + MAIN_FORM_NAME + "']").submit();
		}

		/**
		 * 画面の表示状態がデフォルトのままであるかチェック
		 * @return true デフォルト状態(編集なし)
		 *         false 編集されている
		 */
		function isDefault(){
			// 支払番号が空欄でない
			if($("#paymentSlipId").val() != ""){
				return false;
			}

			// 発注番号が空欄でない
			if($("#poSlipId").val() != ""){
				return false;
			}

			// 明細行が存在する
			var lineCount = $("input[name='lineExist']").size();
			if(lineCount > 0){
				return false;
			}

			// 支払日が初期表示時の現在日でない
			if($("#paymentDate").val() != $("#currentDateInit").val()){
				return false;
			}

			// 入力担当者がログインユーザでない
			if($("#userNameInit").val() != $("#userName").val()){
				return false;
			}

			// 仕入先コードが空欄でない
			if($("#supplierCode").val() != ""){
				return false;
			}

			// 仕入先名称が空欄でない
			if($("#supplierName").val() != ""){
				return false;
			}

			// レートタイプが空欄でない
			if($("#rateId").val() != ""){
				return false;
			}

			// 備考が空欄でない
			if($("#remarks").val() != ""){
				return false;
			}

			return true;
		}

		/**
		 * 仕入先検索画面呼び出し関数
		 */
		function searchSupplierInfo(){
			openSearchSupplierDialog('supplier', setSupplierInfo);
		}

		/**
		 * 仕入先検索画面から呼び出されるコールバック関数
		 */
		function setSupplierInfo(id, map){
			// レート情報を設定する
			setSuppierInfoByAjax(map['supplierCode']);
		}

		/**
		 * 仕入先コード変更時、仕入先情報(レート情報含む)を取得するAjax
		 */
		function setSuppierInfoByAjax(supplierCode){
			// 仕入先コードが無い場合は検索しない
			if(supplierCode == ""){
				return ;
			}

			var label = '<bean:message key="labels.supplierCode" />';
			var data = new Object();
			data["supplierCode"] = supplierCode;
			asyncRequest(
					contextRoot + "/ajax/commonSupplier/getSupplierInfosBySupplierCode",
					data,
					function(data) {
						if(data==""){
							alert('<bean:message key="errors.notExist" arg0="'+label+'" />');
							$("#supplierName").val("");
							$("#rateId").val("");
							$("#taxShiftCategory").val("");
							$("#taxFractCategory").val("");
							$("#priceFractCategory").val("");
							$("#aptBalance").val("");
							$("#rateName").val("");
							return ;
						}else{
							var supplier = eval("("+data+")");
							$("#supplierCode").val(supplier.supplierCode);
							$("#supplierName").val(supplier.supplierName);
							$("#rateId").val(supplier.rateId);
							$("#taxShiftCategory").val(supplier.taxShiftCategory);
							$("#taxFractCategory").val(supplier.taxFractCategory);
							$("#priceFractCategory").val(supplier.priceFractCategory);
							$("#aptBalance").val(supplier.aptBalance);
							$("#rateName").val(supplier.supplierRateName);
						}

						// 画面項目の制御
						setPriceField(supplier.supplierRateName);
						manager.init();
					}
			);
			return false;
		}

		/**
		 * // レートタイプ(rateName)の値によって画面項目を制御する
		 */
		function setPriceField(rateName){
			if(rateName == ""){
				// レートタイプが空の場合(国内)
				// 円単価  →有効
				// 外貨単価→無効
				$("input[name^='lineDtoList['][name$='].dolUnitPrice']").each(function() {
					$(this).attr("readonly","true");
					$(this).addClass("c_disable");
				});

				$("input[name^='lineDtoList['][name$='].unitPrice']").each(function() {
					$(this).removeAttr("readonly");
					$(this).removeClass("c_disable");
				});
			}else{
				// レートタイプが空でない場合(国外)
				// 円単価  →無効
				// 外貨単価→有効
				$("input[name^='lineDtoList['][name$='].unitPrice']").each(function() {
					$(this).attr("readonly","true");
					$(this).addClass("c_disable");
				});

				$("input[name^='lineDtoList['][name$='].dolUnitPrice']").each(function() {
					$(this).removeAttr("readonly");
					$(this).removeClass("c_disable");
				});
			}
		}

		// 全て選択・全て解除
		 function checkAll(flg){
		 	$("#input_table").find("input[type='checkbox']").attr('checked', flg);
		 	changePrice();
		 }

		// 外貨記号リストを作成して仕入先の外貨記号をセットする(記号の付与・除去に必要)
		function applyCUnitSign(){
			// レートマスタの全ての外貨記号リストを取得する
			var data = new Object();
			asyncRequest(
				contextRoot + "/ajax/commonRate/getAllRateSign",
				data,
				function(data) {
					if(data==""){
						alert('<bean:message key="errors.notExist" arg0="レート情報"/>');
						return;
					}
					var values = eval("(" + data + ")");

					// レート記号を設定
					for(var key in values){
						CurrencyUnitClassNameHashList[key] = values[key];
					}
				}
			);
		}

		// 外貨記号の初期値を設定する(仕入先に設定されたレートから取得)
		CurrencyUnitClassNameHashList["dollar_value"] = "${cUnitSign}";
	-->
	</script>

</head>
<!-- ヘッダ -->
<body onhelp="return false;" >
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

<!-- メニュー -->
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0009"/>
		<jsp:param name="MENU_ID" value="0900"/>
	</jsp:include>

<!-- メイン機能 -->
	<div id="main_function">

		<span class="title"><bean:message key='titles.inputPayment'/></span>

		<div class="function_buttons">
			<button id="btnF1" type="button" tabindex="2000" onclick="onF1()">F1<br><bean:message key='words.action.initialize'/>
			</button><button id="btnF2" type="button" tabindex="2001" ${notDeletable? "disabled='disabled'" : "onclick='onF2()'"}>F2<br><bean:message key='words.action.delete'/>
			<c:if test="${f:h(new)}">
				</button><button type="button" id="F3" tabindex="2002" ${notRegisterable? "disabled='disabled'":"onclick='onF3()'"}>F3<br><bean:message key='words.action.register'/>
			</c:if>
			<c:if test="${!f:h(new)}">
				</button><button type="button" id="F3" tabindex="2002" ${notUpdatable? "disabled='disabled'":"onclick='onF3()'"}>F3<br><bean:message key='words.action.renew'/>
			</c:if>
			</button><button id="btnF4" type="button" tabindex="2003" disabled="disabled"  >F4<br>&nbsp;
			</button><button id="btnF5" type="button" tabindex="2004" disabled="disabled"  >F5<br>&nbsp;
			</button><button id="btnF6" type="button" tabindex="2005" ${notCopiable? "disabled='disabled'":"onclick='onF6()'"}>F6<br><bean:message key='words.action.copySlip'/>
			</button><button id="btnF7" type="button" tabindex="2006" disabled="disabled"  >F7<br>&nbsp;
			</button><button id="btnF8" type="button" tabindex="2007" disabled="disabled"  >F8<br>&nbsp;
			</button><button id="btnF9" type="button" tabindex="2008" disabled="disabled"  >F9<br>&nbsp;
			</button><button id="btnF10" type="button" tabindex="2009" disabled="disabled"  >F10<br>&nbsp;
			</button><button id="btnF11" type="button" tabindex="2010" disabled="disabled"  >F11<br>&nbsp;
			</button><button id="btnF12" type="button" tabindex="2011" disabled="disabled"  >F12<br>&nbsp;</button>
		</div>

		<s:form onsubmit="return false;">

		<!-- 2.支払伝票情報 -->
		<DIV class="function_forms">
			<!-- エラー情報 -->
			<div style="padding-left: 20px">
				<html:errors />&nbsp;
			</div>
			<div style="color: blue">
				<html:messages id="msg" message="true">
					<bean:write name="msg" ignore="true"/>
				</html:messages>
			</div>

			<span><bean:message key='labels.paymentSlipInfos'/></span><br>
			<table id="payment_info" class="forms" summary="支払伝票情報">
				<colgroup>
					<col span="1" style="width: 10%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 10%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 10%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 10%">
					<col span="1" style="width: 15%">
				</colgroup>
				<tr>
					<th><bean:message key='labels.paymentSlipId'/></th>
					<td><html:text tabindex="100" property="paymentSlipId" styleClass="" styleId="paymentSlipId" readonly="false" style="width:100px; ime-mode:disabled;"  maxlength="10"  onfocus="this.curVal=this.value;" onblur="if((this.curVal == '') || ((this.curVal != '')&&(this.curVal!=this.value))){ findSlip()}"/></td>
					<th><bean:message key='labels.poSlipId'/></th>
					<td><html:text tabindex="101" property="poSlipId" styleClass="${poSlipId==''?'':'c_disable'}" styleId="poSlipId" readonly="${poSlipId==''?'false':'true'}" style="width:100px; ime-mode:disabled;"  maxlength="10"  onblur="findCopy()"/></td>
					<th><bean:message key='labels.paymentDate'/><bean:message key='labels.must'/></th>
					<td><html:text tabindex="102" property="paymentDate" styleId="paymentDate" styleClass="date_input" style="width:100px" /></td>
					<th><bean:message key='labels.userName'/></th>
					<td>
						<html:text tabindex="103" property="userName" styleId="userName" styleClass="c_disable" readonly="true" />
					</td>
				</tr>
				<tr>
					<th><bean:message key='labels.memorandum'/></th>
					<td colspan="7">
						<html:text tabindex="104" property="remarks" styleId="remarks" style="width: 800px"  />
					</td>
				</tr>
			</table>

			<span><bean:message key='labels.supplierInfos'/></span><br>
			<table id="supplier_info" class="forms" summary="仕入先情報">
				<colgroup>
					<col span="1" style="width: 10%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 10%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 10%">
					<col span="1" style="width: 15%">
					<col span="1" style="width: 10%">
					<col span="1" style="width: 15%">
				</colgroup>
				<tr>
					<th><bean:message key='labels.supplierCode'/></th>
					<td>
						<html:text tabindex="200" property="supplierCode" styleId="supplierCode" styleClass="c_disable" style="width: 130px;" readonly="true" />
						<html:hidden property="taxShiftCategory" styleId="taxShiftCategory" />
						<html:hidden property="taxFractCategory" styleId="taxFractCategory" />
						<html:hidden property="priceFractCategory" styleId="priceFractCategory" />
						<html:hidden property="supplierTaxRate" styleId="supplierTaxRate" />
					</td>
					<th><bean:message key='labels.supplierName'/></th>
					<td colspan="3">
						<html:text tabindex="201" property="supplierName" style="width: 235px;" styleId="supplierName" styleClass="c_disable" readonly="true" />
					</td>
					<th><bean:message key='labels.rateType'/></th>
					<td>
						<html:hidden property="rateId" styleId="rateId" />
						<html:text tabindex="202" property="rateName" styleId="rateName" styleClass="c_disable" readonly="true" />
					</td>
				</tr>
			</table>
			<html:hidden name="inputPaymentForm" property="cUnitSign" />
			<html:hidden name="inputPaymentForm" property="newData" />
			<BR>
			<input type="button" id="allCheck" name="allCheck" tabindex="300" value="全て選択" onclick="checkAll(true);" >
			<input type="button" id="allUnCheck" name="allUnCheck" tabindex="301" value="全て解除" onclick="checkAll(false);" >

			<!-- 3.支払伝票明細リスト -->
			<table id="input_table" summary="支払明細リスト" class="forms" style="margin-top: 0px;">

				<colgroup>
					<col span="1" style="width:  20px">
					<col span="1" style="width:  90px">
					<col span="1" style="width: 150px">
					<col span="1" style="width:  50px">

					<col span="1" style="width: 140px">
					<col span="1" style="width:  70px">
					<col span="1" style="width:  70px">

					<col span="1" style="width:  290px">


				</colgroup>
				<thead>
				<tr>
					<th rowspan="3"><bean:message key='labels.select'/></th>
					<th><bean:message key='labels.supplierSlipId'/> - <bean:message key='labels.line'/></th>
					<th><bean:message key='labels.productCode'/></th><th></th>

					<th><bean:message key='labels.paymentLineCategory'/></th>
					<th><bean:message key='labels.quantity'/></th><th><bean:message key='labels.lastRate'/></th>
					<th rowspan="3"><bean:message key='labels.remarks'/></th>
				</tr>
				<tr>

					<th><!-- <bean:message key='labels.supplierLineCategory'/> --></th>
					<th colspan="2" rowspan="2"><bean:message key='labels.productName'/></th>

					<th><bean:message key='labels.unitPrice'/></th>
					<th colspan="2"><bean:message key='labels.price'/></th>

				</tr>
				<tr>

					<th><bean:message key='labels.supplierDate'/></th>


					<th><bean:message key='labels.dolUnitPrice'/></th>
					<th colspan="2"><bean:message key='labels.dolPrice'/></th>

				</tr>
				</thead>
				<tbody>
				<!-- 繰り返し部分開始(明細行) -->
				<c:forEach var="lineDtoList" varStatus="s" items="${lineDtoList}">
					<input type="hidden" name="lineExist">
					<html:hidden name="lineDtoList" property="paymentLineId" indexed="true" />
					<html:hidden name="lineDtoList" property="poLineId" indexed="true" />
					<html:hidden name="lineDtoList" property="supplierLineId" indexed="true" />
					<html:hidden name="lineDtoList" property="supUpdDatetm" indexed="true" />
					<html:hidden name="lineDtoList" property="productAbstract" indexed="true" />
					<html:hidden name="lineDtoList" property="paymentLineNo" indexed="true" />
					<tr>
						<td style="text-align: right;">
<c:if test="${new}">
							<html:checkbox name="lineDtoList" property="checkPayLine"  indexed="true" styleId="checkPayLine${s.index}" styleClass="AutoCalcCheckPayLine"  onclick="changePrice();" />
</c:if>
						</td>
						<td style="vertical-align: middle">
							<c:if test="${lineDtoList.supplierSlipId != null}">
								<c:if test="${isInputPurchaseValid}">
									<bean:define id="concatUrl" value="/purchase/inputPurchase/load/?supplierSlipId=${f:h(lineDtoList.supplierSlipId)}"/>
									<a href="javascript:location.doHref('${f:url(concatUrl)}')">${f:h(lineDtoList.supplierSlipId)} - ${f:h(lineDtoList.supplierLineNo)}</a>
								</c:if>
								<c:if test="${!isInputPurchaseValid}">${f:h(lineDtoList.supplierSlipId)} - ${f:h(lineDtoList.supplierLineNo)}</c:if><br>
							</c:if>
							<html:hidden name="lineDtoList" property="supplierSlipId" indexed="true" />
							<html:hidden name="lineDtoList" property="supplierLineNo" indexed="true" />
							<!-- <html:text name="lineDtoList" property="supplierDetailCategoryName" style="width: 85px;" styleClass="c_disable" readonly="true"  indexed="true" /><br> -->
							<br>
							<html:text tabindex="${1000+s.index*10}" name="lineDtoList" property="supplierDate" style="width: 85px;" styleClass="c_disable"  readonly="true"  indexed="true" />
						</td>
						<td colspan="2">
							<html:text tabindex="${1001+s.index*10}" name="lineDtoList" property="productCode" style="width: 150px;" styleClass="goods_code c_disable" readonly="true"  indexed="true" />
							<br>
							<span style="display: block;  width:200px; height:3em; white-space: normal; overflow: auto; padding: 1px; border-top: 1px gray solid;">
							${f:h(lineDtoList.productAbstract)}
							</span>
						</td>
						<td style="vertical-align: top;">
							<html:select tabindex="${1002+s.index*10}" name="lineDtoList" property="paymentCategory" indexed="true" style="width: 105px;" >
								<html:options collection="paymentDetailList" property="value" labelProperty="label"/>
							</html:select><br>
							<html:text tabindex="${1003+s.index*10}" name="lineDtoList" styleId="unitPrice${s.index}" property="unitPrice" styleClass="c_disable AutoCalcUnitPrice numeral_commas yen_value" style="width: 140px; text-align:right;" indexed="true" readonly="true" maxlength="9" /><br>
							<html:text tabindex="${1004+s.index*10}" name="lineDtoList" styleId="dolUnitPrice${s.index}" property="dolUnitPrice" styleClass="c_disable AutoCalcDolUnitPrice numeral_commas dollar_value" style="width: 140px; text-align:right;" readonly="true"  indexed="true" maxlength="9" /><br>
						</td>
						<td style="vertical-align: top;" colspan="2">
							<html:text tabindex="${1005+s.index*10}" name="lineDtoList" styleId="quantity${s.index}" property="quantity" styleClass="c_disable AutoCalcQuantity numeral_commas" style="width: 80px; text-align:right;" readonly="true" indexed="true" />
							<html:text tabindex="${1006+s.index*10}" name="lineDtoList" styleId="rate${s.index}" property="rate" styleClass="c_disable AutoCalcRate" style="width: 55px; text-align:right;" readonly="true" indexed="true" /><br>
							<html:text tabindex="${1007+s.index*10}" name="lineDtoList" styleId="price${s.index}" property="price" styleClass="c_disable AutoCalcPrice numeral_commas yen_value" style="width: 140px; text-align:right;ime-mode:disabled" readonly="true" indexed="true"  /><br>
							<html:text tabindex="${1008+s.index*10}" name="lineDtoList" styleId="dolPrice${s.index}" property="dolPrice" styleClass="c_disable AutoCalcDolPrice numeral_commas dollar_value" style="width: 140px; text-align:right;ime-mode:disabled" readonly="true" indexed="true" />
						</td>
						<td style="vertical-align: top;">
						<html:textarea tabindex="${1009+s.index*10}" name="lineDtoList" property="remarks" style="width: 290px; height: 65px;" indexed="true"  />
						</td>
					</tr>
				</c:forEach>
				<!-- 繰り返し部分終了(明細行) -->
				</tbody>
			</table>



			<!-- 4.伝票合計 -->
			<div id="information" class="information" style="margin-top: 20px;">
				<table class="forms" summary="伝票情報" style="width: 450px; position: absolute; top: 0px; left: 460px;">
					<colgroup>
						<col span="4" style="width: 25%">
					</colgroup>
					<tr>
						<th><bean:message key='labels.priceTotal'/></th>
						<th><bean:message key='labels.fePriceTotal'/></th>
						<th><bean:message key='labels.paymentBalanceYen'/></th>
						<th><bean:message key='labels.aptBalanceYen'/></th>
					</tr>
					<tr>
						<td style="text-align: center">
							<html:text property="priceTotal" styleId="priceTotal" style="width: 100px; text-align:right; background-color: #FFFFFF; border-style: none;" readonly="true"/>
						</td>
						<td style="text-align: center">
							<html:text property="fePriceTotal" styleId="fePriceTotal" style="width: 100px; text-align:right; background-color: #FFFFFF; border-style: none;" readonly="true"/>
						</td>
						<td style="text-align: center">
							<input type="text" id="paymentBalance" value=""  style="width: 100px; text-align:right; background-color: #FFFFFF; border-style: none;" readonly>
						</td>
						<td style="text-align: center">
							<html:text styleId="aptBalance" property="aptBalance" style="width: 100px; text-align:right; background-color: #FFFFFF; border-style: none;" readonly="true" />
						</td>
					</tr>
				</table>
			</div>
				<br><br><br>
				<div style="text-align: right; width: 910px">
					<c:if test="${f:h(new)}">
						<button type="button" id="F3btm" tabindex="1999" ${notRegisterable? "disabled='disabled'":"onclick='onF3()'"}><bean:message key='words.action.register'/></button><!--登録-->
					</c:if>
					<c:if test="${!f:h(new)}">
						<button type="button" id="F3btm" tabindex="1999" ${notRegisterable? "disabled='disabled'":"onclick='onF3()'"}><bean:message key='words.action.renew'/></button><!--更新-->
					</c:if>
				</div>
		</DIV>



		<html:hidden property="updDatetm" styleId="updDatetm" />
		<html:hidden property="poDate" styleId="poDate" />
		<html:hidden property="initCalc" styleId="initCalc"/>


		<input type="hidden" id="unitPriceDecAlignment" value="${mineDto.unitPriceDecAlignment}">
		<input type="hidden" id="numDecAlignment" value="${mineDto.numDecAlignment}">

		<input type="hidden" id="currentDateInit" value="${f:h(paymentDate)}">
		<input type="hidden" id="userNameInit" value="${f:h(userName)}">

		</s:form>

	</div>

</body>

</html>
