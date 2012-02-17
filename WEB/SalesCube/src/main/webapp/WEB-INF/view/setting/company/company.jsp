<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/>　自社情報</title>
<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--

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
<body>
	
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0012"/>
		<jsp:param name="MENU_ID" value="1200"/>
	</jsp:include>

	
	<div id="main_function">

		<span class="title">自社情報</span>

		<div class="function_buttons">
			<button tabindex="2000" onclick="onF1()">F1<br>リセット
			</button><button tabindex="2001" disabled="disabled">F2<br>&nbsp;
			<c:if test="${isUpdate == true}">
			</button><button tabindex="2002" onclick="onF3()">F3<br>更新
			</c:if>
			<c:if test="${isUpdate == false}">
			</button><button tabindex="2002" disabled="disabled">F3<br>更新
			</c:if>
			</button><button tabindex="2003" disabled="disabled">F4<br>&nbsp;
			</button><button tabindex="2004" disabled="disabled">F5<br>&nbsp;
			</button><button tabindex="2005" disabled="disabled">F6<br>&nbsp;
			</button><button tabindex="2006" disabled="disabled">F7<br>&nbsp;
			</button><button tabindex="2007" disabled="disabled">F8<br>&nbsp;
			</button><button tabindex="2008" disabled="disabled">F9<br>&nbsp;
			</button><button tabindex="2009" disabled="disabled">F10<br>&nbsp;
			</button><button tabindex="2010" disabled="disabled">F11<br>&nbsp;
			</button><button tabindex="2011" disabled="disabled">F12<br>&nbsp;</button>
		</div>

		<s:form action="update" onsubmit="return false;" enctype="multipart/form-data">

			<div class="function_forms">
				<div id="errors" style="color: red" style="padding-left: 20px"><html:errors/></div>
				<div style="padding-left: 20px;color: blue;">
		        	<html:messages id="msg" message="true">
		        		<bean:write name="msg" ignore="true"/><br>
		        	</html:messages>
		    	</div>

				<html:hidden property="updDatetm"/>
				<span>自社情報</span>
				<table class="forms" style="width: 800px" summary="自社情報1">
					<colgroup>
						<col span="1" style="width: 15%">
						<col span="1" style="width: 35%">
						<col span="1" style="width: 15%">
						<col span="1" style="width: 35%">
					</colgroup>
					<tr>
						<th>会社名※</th>
						<td><html:text style="width: 250px;" tabindex="100" property="companyName" /></td>
						<th>会社略名※</th>
						<td><html:text style="width: 250px;" tabindex="101" property="companyAbbr" /></td>
					</tr>
					<tr>
						<th>会社名カナ※</th>
						<td><html:text style="width: 250px;" tabindex="102" property="companyKana" /></td>
						<th>代表取締役※</th>
						<td><html:text style="width: 250px;" tabindex="103" property="companyCeoName" /></td>
					</tr>
					<tr>
						<th>会社ロゴ</th>
						<td colspan="3">
							<html:file property="logoImgPath" accept='image/gif,image/jpeg,image/png' tabindex="104" style="width: 450px;" onchange="$('#openLevel0').focus();"  />
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
						<th>郵便番号※</th>
						<td colspan="3"><html:text style="width: 70px; ime-mode: disabled;" tabindex="200" property="companyZipCode" styleId="companyZipCode" onfocus="this.curVal=this.value;" onblur="if(this.curVal!=this.value){searchZipCodeDirect();}"/>
						<html:image src="${f:url('/images/icon_04_02.gif')}"  tabindex="201" style="vertical-align: middle; cursor: pointer;" onclick="zipSearch($('#companyZipCode'))"/>
						</td>
					</tr>
					<tr>
						<th>住所１※</th>
						<td colspan="3"><html:text style="width: 500px;" tabindex="202" property="companyAddress1" styleId="companyAddress1"/></td>
					</tr>
					<tr>
						<th>住所２</th>
						<td colspan="3"><html:text style="width: 500px;" tabindex="203" property="companyAddress2" /></td>
					</tr>
					<tr>
						<th>TEL※</th>
						<td><html:text style="width: 200px; ime-mode: disabled;" tabindex="204" property="companyTel" /></td>
						<th>FAX※</th>
						<td><html:text style="width: 200px; ime-mode: disabled;" tabindex="205" property="companyFax" /></td>
					</tr>
					<tr>
						<th>E-MAIL※</th>
						<td colspan="3"><html:text style="width: 500px; ime-mode: disabled;" tabindex="206" property="companyEmail" /></td>
					</tr>
					<tr>
						<th>Webサイト</th>
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
						<th>締日</th>
						<td>
						<html:select property="cutoffGroup"  tabindex="301" >
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
						<th>決算月</th>
						<td>
						<html:select property="closeMonth"  tabindex="302" >
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

				<div style="text-align: right; width: 800px">
					<button tabindex="350" onclick="onF1()" style="width: 80px;" >リセット</button>
					<c:if test="${isUpdate == true}">
					<button tabindex="351" onclick="onF3()" style="width: 80px;" >更新</button>
					</c:if>
					<c:if test="${isUpdate == false}">
					<button tabindex="351" onclick="onF3()" style="width: 80px;" disabled="disabled">更新</button>
					</c:if>
				</div>
			</div>
			<html:hidden property="isUpdate"/>
		</s:form>
	</div>
</body>

</html>
