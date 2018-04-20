<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　自社情報</title>
<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--

	$(function() {
		applyPriceAlignment();
	});

	function init() {
	    	//桁適用
    	_after_load($(".numeral_commas"));
    	applyPriceAlignment();
    }

	function applyPriceAlignment() {
		if($("#priceFractCategory").val()) {
			// 円単価
			$(".BDCyen").setBDCStyle( $("#priceFractCategory").val() ,0 ).attBDC();
		}
	}

	//郵便番号ダイアログ
	function zipSearch(jqObject) {
		openSearchZipDialog( 'zipCode1',
			function(id, map) {
				$("#companyZipCode").val(map["zipCode"]);
				$("#companyAddress1").val(map["zipAddress1"]);
			}
		);
		$("#zipCode1_zipCode").val( jqObject.val() );
		$("#zipCode1_zipCode").focus();
	}

    function searchZipCodeDirect() {
		// 入力チェック
		var val = $("#companyZipCode").val();
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
					var message = "<bean:message key="warns.zipcode.notidentical" arg0="会社"/>";
					$("#errors").append(message);
				}
				else {
					$("#companyZipCode").val(results[0].zipCode);
					$("#companyAddress1").val(results[0].zipAddress1);
					$("#companyAddress2").val(results[0].zipAddress2);
				}
			}
		);
	}

	//リセット
	function onF1(){
		if(confirm('<bean:message key="confirm.reset" />')){
			document.setting_companyActionForm.action = '${f:url("reset")}';
			document.setting_companyActionForm.submit();
		}
	}

	//更新
	function onF3(){
		if(confirm('<bean:message key="confirm.update" />')){
			_before_submit($(".numeral_commas"));
			document.setting_companyActionForm.action = '${f:url("update")}';
			document.setting_companyActionForm.submit();
		}
	}

	//ロゴ初期化と参照の連動
	function logoInitAct(checkobj){
		var fileobj = checkobj.form.elements["logoImgPath"];
		fileobj.disabled = checkobj.checked;
	}
//-->
</script>
</head>
<body onload="init()">
	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0012"/>
		<jsp:param name="MENU_ID" value="1200"/>
	</jsp:include>

	<%-- メイン機能領域 --%>
	<div id="main_function">

		<span class="title">自社情報</span>

		<div class="function_buttons">
			<button tabindex="2000" onclick="onF1()">F1<br>リセット</button>
			<button tabindex="2001" disabled="disabled">F2<br>&nbsp;</button>
			<c:if test="${isUpdate == true}">
			<button tabindex="2002" onclick="onF3()">F3<br>更新</button>
			</c:if>
			<c:if test="${isUpdate == false}">
			<button tabindex="2002" disabled="disabled">F3<br>更新</button>
			</c:if>
			<button tabindex="2003" disabled="disabled">F4<br>&nbsp;</button>
			<button tabindex="2004" disabled="disabled">F5<br>&nbsp;</button>
			<button tabindex="2005" disabled="disabled">F6<br>&nbsp;</button>
			<button tabindex="2006" disabled="disabled">F7<br>&nbsp;</button>
			<button tabindex="2007" disabled="disabled">F8<br>&nbsp;</button>
			<button tabindex="2008" disabled="disabled">F9<br>&nbsp;</button>
			<button tabindex="2009" disabled="disabled">F10<br>&nbsp;</button>
			<button tabindex="2010" disabled="disabled">F11<br>&nbsp;</button>
			<button tabindex="2011" disabled="disabled">F12<br>&nbsp;</button>
		</div>
		<br><br><br>

		<s:form action="update" onsubmit="return false;" enctype="multipart/form-data">

			<div class="function_forms">
				<div id="errors" style="color: red" style="padding-left: 20px"><html:errors/></div>
				<div style="padding-left: 20px;color: blue;">
		        	<html:messages id="msg" message="true">
		        		<bean:write name="msg" ignore="true"/><br>
		        	</html:messages>
		    	</div>

				<html:hidden property="updDatetm"/>
				<div class="form_section_wrap">
					<div class="form_section">
						<div class="section_title">
							<span>会社情報</span>
						</div><!-- /.section_title -->
						<div class="section_body">
						<table class="forms" style="width: 800px" summary="会社情報">
							<colgroup>
								<col span="1" style="width: 15%">
								<col span="1" style="width: 35%">
								<col span="1" style="width: 15%">
								<col span="1" style="width: 35%">
							</colgroup>
							<tr>
								<th><div class="col_title_right_req">会社名<bean:message key='labels.must'/></div></th>
								<td><html:text style="width: 350px;" tabindex="100" property="companyName" /></td>
								<th><div class="col_title_right_req">会社略名<bean:message key='labels.must'/></div></th>
								<td><html:text style="width: 350px;" tabindex="101" property="companyAbbr" /></td>
							</tr>
							<tr>
								<th><div class="col_title_right_req">会社名カナ<bean:message key='labels.must'/></div></th>
								<td><html:text style="width: 350px;" tabindex="102" property="companyKana" /></td>
								<th><div class="col_title_right_req">代表取締役<bean:message key='labels.must'/></div></th>
								<td><html:text style="width: 350px;" tabindex="103" property="companyCeoName" /></td>
							</tr>
							<tr>
								<th><div class="col_title_right_req">代表者肩書<bean:message key='labels.must'/></div></th>
								<td colspan="3"><html:text style="width: 350px;" tabindex="104" property="companyCeoTitle" /></td>
							</tr>
							<tr>
								<th><div class="col_title_right">会社ロゴ</div></th>
								<td colspan="3">
									<html:file property="logoImgPath" accept='image/gif,image/jpeg,image/png' tabindex="105" style="width: 450px;" onchange="$('#openLevel0').focus();"  />
									<html:checkbox property="logoInit" onclick="logoInitAct(this)"/>初期化
								</td>
							</tr>
						</table>

						<table class="forms" style="width: 800px" summary="自社情報2">
							<colgroup>
								<col span="1" style="width: 15%">
								<col span="1" style="width: 35%">
								<col span="1" style="width: 15%">
								<col span="1" style="width: 35%">
							</colgroup>
							<tr>
								<th><div class="col_title_right_req">郵便番号<bean:message key='labels.must'/></div></th>
								<td colspan="3"><html:text style="width: 100px; ime-mode: disabled;" tabindex="200" property="companyZipCode" styleId="companyZipCode" onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){searchZipCodeDirect();}"/>
								<html:image src="${f:url('/images//customize/btn_search.png')}"  tabindex="201" style="vertical-align: middle; cursor: pointer;" onclick="zipSearch($('#companyZipCode'))"/>
								</td>
							</tr>
							<tr>
								<th><div class="col_title_right_req">住所１<bean:message key='labels.must'/></div></th>
								<td colspan="3"><html:text style="width: 500px;" tabindex="202" property="companyAddress1" styleId="companyAddress1"/></td>
							</tr>
							<tr>
								<th><div class="col_title_right">住所２</div></th>
								<td colspan="3"><html:text style="width: 500px;" tabindex="203" property="companyAddress2" /></td>
							</tr>
							<tr>
								<th><div class="col_title_right_req">TEL<bean:message key='labels.must'/></div></th>
								<td><html:text style="width: 200px; ime-mode: disabled;" tabindex="204" property="companyTel" /></td>
								<th><div class="col_title_right_req">FAX<bean:message key='labels.must'/></div></th>
								<td><html:text style="width: 200px; ime-mode: disabled;" tabindex="205" property="companyFax" /></td>
							</tr>
							<tr>
								<th><div class="col_title_right_req">E-MAIL<bean:message key='labels.must'/></div></th>
								<td colspan="3"><html:text style="width: 500px; ime-mode: disabled;" tabindex="206" property="companyEmail" /></td>
							</tr>
							<tr>
								<th><div class="col_title_right">Webサイト</div></th>
								<td colspan="3"><html:text style="width: 500px;" tabindex="207" property="companyWebSite" /></td>
							</tr>
						</table>

						<table class="forms" style="width: 800px" summary="自社情報3">
							<colgroup>
								<col span="1" style="width: 15%">
								<col span="1" style="width: 35%">
								<col span="1" style="width: 15%">
								<col span="1" style="width: 35%">
							</colgroup>
							<tr>
								<th><div class="col_title_right">締日</div></th>
								<td>
								<html:select property="cutoffGroup"  tabindex="301" style="width: 200px;">
								<html:option value="01">01</html:option>
								<html:option value="02">02</html:option>
								<html:option value="03">03</html:option>
								<html:option value="04">04</html:option>
								<html:option value="05">05</html:option>
								<html:option value="06">06</html:option>
								<html:option value="07">07</html:option>
								<html:option value="08">08</html:option>
								<html:option value="09">09</html:option>
								<html:option value="10">10</html:option>
								<html:option value="11">11</html:option>
								<html:option value="12">12</html:option>
								<html:option value="13">13</html:option>
								<html:option value="14">14</html:option>
								<html:option value="15">15</html:option>
								<html:option value="16">16</html:option>
								<html:option value="17">17</html:option>
								<html:option value="18">18</html:option>
								<html:option value="19">19</html:option>
								<html:option value="20">20</html:option>
								<html:option value="21">21</html:option>
								<html:option value="22">22</html:option>
								<html:option value="23">23</html:option>
								<html:option value="24">24</html:option>
								<html:option value="25">25</html:option>
								<html:option value="26">26</html:option>
								<html:option value="27">27</html:option>
								<html:option value="28">28</html:option>
								<html:option value="29">29</html:option>
								<html:option value="30">30</html:option>
								<html:option value="31">31</html:option>
								</html:select>
								日</td>
								<th><div class="col_title_right">決算月</div></th>
								<td>
								<html:select property="closeMonth"  tabindex="302" style="width: 200px;">
								<html:option value="01">01</html:option>
								<html:option value="02">02</html:option>
								<html:option value="03">03</html:option>
								<html:option value="04">04</html:option>
								<html:option value="05">05</html:option>
								<html:option value="06">06</html:option>
								<html:option value="07">07</html:option>
								<html:option value="08">08</html:option>
								<html:option value="09">09</html:option>
								<html:option value="10">10</html:option>
								<html:option value="11">11</html:option>
								<html:option value="12">12</html:option>
								</html:select>
								月</td>
							</tr>
						</table>


					</div><!-- /.section_body -->
	    		</div><!-- /.form_section -->
	   		</div><!-- /.form_section_wrap -->

	    <div class="form_section_wrap">
		    <div class="form_section">
		    	<div class="section_title">
					<span>送料設定情報</span>
		            <button class="btn_toggle" />
				</div><!-- /.section_title -->

				<div id="order_section" class="section_body">
					<table id="rate_info" class="forms" summary="送料設定情報">
						<tr>
							<th colspan="2"><div class="col_title_right_req">送料区分<bean:message key='labels.must'/></div></th>
							<td>
			    				<html:select styleId="postageType" property="iniPostageType"  style="width: 300px;" tabindex="310">
			    					<html:options collection="postageTypeList" property="value" labelProperty="label"/>
			    				</html:select>
			                </td>
						</tr>
						<tr>
							<th><div class="col_title_right">送料対象金額</div></th>
							<td colspan="5">
								<html:text maxlength="120" styleId="targetPostageCharges" property="targetPostageCharges" styleClass="numeral_commas yen_value BDCyen"
							onchange="applyPriceAlignment();" style="width: 200px;" tabindex="311"/>
							</td>
						</tr>
						<tr>
							<th><div class="col_title_right">送料</div></th>
							<td colspan="5">
								<html:text maxlength="120" styleId="postage" property="postage" styleClass="numeral_commas yen_value BDCyen"
							onchange="applyPriceAlignment();" style="width: 200px;" tabindex="312"/>
							</td>
						</tr>
					</table>
					<html:hidden property="priceFractCategory" styleId="priceFractCategory" />
				</div><!-- /.section_body -->
			</div><!-- /.form_section -->
		</div><!-- /.form_section_wrap -->

				<div style="text-align: right; width: 1160px">
					<button class="btn_medium"  tabindex="350" onclick="onF1()" style="" >リセット</button>
					<c:if test="${isUpdate == true}">
					<button class="btn_medium"  tabindex="351" onclick="onF3()" style="" >更新</button>
					</c:if>
					<c:if test="${isUpdate == false}">
					<button class="btn_medium"  tabindex="351" onclick="onF3()" style="" disabled="disabled">更新</button>
					</c:if>
				</div>
			</div>
			<html:hidden property="isUpdate"/>
		</s:form>
	</div>
</body>

</html>
