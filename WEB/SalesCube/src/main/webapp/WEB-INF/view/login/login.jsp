<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="ja">
<head>
	<title><bean:message key='titles.system'/> <bean:message key='words.action.login'/></title>
	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
	$(function() {
		$("[name='userId']").focus();
		$("#password").keypress(function(evt){
			if(evt.keyCode==13){
				$("#login").click();
				return false;
			}
			return true;
		});
	});

	function initForm() {
    	if(!confirm("<bean:message key='confirm.init'/>")){
        	return;
    	}
    	$("[name='userId']").val("");
    	$("[name='password']").val("");
	}
	-->
	</script>
</head>
<body>
	
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	
	<div id="menu">
		<div class="buisiness_menu1" style="height: 24px;">
			<img src="${f:url('/images/bmenu1_empty.png')}">
		</div>

		<div id="empty" class="buisiness_menu2" style="display: block; height: 24px;">
			<img src="${f:url('/images/bmenu2_empty.png')}">
		</div>
	</div>

	
	<div id="main_function">
		<div class="function_forms">

			<div style="padding-left: 20px">
				<html:errors/>
			</div>

			<s:form>
				<table class="forms" style="width: 500px" summary="login_form">
					<colgroup>
						<col span="1" style="width: 50%">
						<col span="1" style="width: 50%">
					</colgroup>
					<tr>
						<th><bean:message key='labels.userId'/><bean:message key='labels.must'/></th><!-- ユーザーID -->
						<td><html:text style="width: 150px; ime-mode: disabled;" tabindex="100" property="userId"/></td>
					</tr>
					<tr>
						<th><bean:message key='labels.password'/><bean:message key='labels.must'/></th><!-- パスワード -->
						<td><html:password style="width: 150px; ime-mode: disabled;" tabindex="101" property="password" redisplay="false" styleId="password"/></td>
					</tr>
				</table>

				<div style="text-align: right; width: 500px">
					<html:button property="resetButton" style="width: 80px;" tabindex="150"
						onclick="initForm();">
						<bean:message key='words.action.initialize'/>
					</html:button>
					<s:submit property="login" styleId="login" style="width: 80px;" tabindex="151">
						<bean:message key='words.action.login'/>
					</s:submit>
				</div>
			</s:form>
<!--
			<fieldset style="width: 750px; border-style: ridge; border-width: 5px; border-color: #6495ed; font-weight: bold;">
				<legend>デモサイトについて</legend>
				<ul>
					<li style="margin-bottom: 10px;">デモサイトは『<a href="http://www.ark-info-sys.co.jp/jp/product/salescube/demo.html">SalesCube デモサイト利用規約</a>』に同意頂いた上でご利用下さい。<br></li>

					<li style="margin-bottom: 10px;">デモサイトには以下のユーザーIDとパスワードでログインして下さい。<br>
						<table>
							<tr><td>ユーザーID</td><td>：</td><td>DEMO</td></tr>
							<tr><td>パスワード</td><td>：</td><td>DEMO</td></tr>
						</table>
					</li>

					<li style="margin-bottom: 10px;">デモサイトはユーザーの権限変更など、一部機能の利用に制限を設けております。</li>

					<li style="margin-bottom: 10px;">デモサイトのセッション持続時間は<span style="color: red">10分間</span>に設定しております。<br>
						10分間以上操作が行われない場合、自動的にログアウト状態となることにご注意下さい。
					</li>

					<li style="margin-bottom: 10px;">デモサイトはバージョン7以降のInternet Explorerで正常に動作致します。</li>

					<li>デモサイトの一部機能は、デモサイトを信頼済みサイトに登録して頂く事で正常に動作致します。<br>
						→<a href="${f:url('/setTrustSite.html')}">信頼済みサイトの設定方法</a>
					</li>
				</ul>
			</fieldset>
-->
		</div>
	</div>

</body>
</html>
