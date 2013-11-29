<%@page import="jp.co.arkinfosys.common.Constants"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title><bean:message key='titles.system'/>　郵便番号辞書</title>

	<%@ include file="/WEB-INF/view/common/header.jsp" %>

	<script type="text/javascript">
	<!--
	/**
	 * F1キー押下処理
	 */
	function onF1() {
    	if(confirm("<bean:message key='confirm.init'/>")){
            location.doHref(contextRoot + "/master/importZipCodeCSV");
        }
	}

	/**
	 * F3キー押下処理
	 */
	function onF3() {
		submitForm();
	}

	/**
	 * アップロード
	 */
    function submitForm() {
    	if(confirm("<bean:message key='confirm.zipcode.update'/>")){
    		showNowSearchingDiv();
            $("#entryZipCodeForm").submit();
            $('#btnF1').blur();
            $('#btnF3').blur();
            $('#importBtn').blur();
        }
    }
    -->
    </script>
</head>

<body onhelp="return false;">

	<%-- ページヘッダ領域 --%>
	<%@ include file="/WEB-INF/view/common/titlebar.jsp" %>

	<%-- メニュー領域 --%>
	<jsp:include page="/WEB-INF/view/common/menubar.jsp">
		<jsp:param name="PARENT_MENU_ID" value="0013"/>
		<jsp:param name="MENU_ID" value="1316"/>
	</jsp:include>

	<!-- メイン機能 -->
	<div id="main_function">
		<span class="title">郵便番号辞書</span>

		<div class="function_buttons">
	        <button id="btnF1"  onclick="onF1();" tabindex="2000">F1<br>初期化</button>
        	<button disabled="disabled">F2<br>&nbsp;</button>
        	<button id="btnF3" onclick="onF3();" tabindex="2001">F3<br>一括更新</button>
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

		<div class="function_forms">
			<s:form onsubmit="return false;" styleId="entryZipCodeForm" action="upload" enctype="multipart/form-data" method="POST">
				<html:errors/>
				<div style="padding-left: 20px;color: blue;">
					<html:messages id="msg" message="true">
						<bean:write name="msg" ignore="true"/><br>
					</html:messages>
				</div>

				<div class="form_section_wrap">
					<div class="form_section">
						<div class="section_title">
							<span>郵便番号辞書</span>
						</div><!-- /.section_title -->
						<div class="section_body">
						<a href="http://www.post.japanpost.jp/zipcode/dl/kogaki.html" target="japanpost">日本郵便のWebサイト</a>から全国一括の郵便番号データをダウンロードして、CSVファイルをアップロードしてください。<br>
						<html:file property="zipCodeCSV" size="100" style="height: 24px" tabindex="100" onchange="$('#importBtn').focus();" />
						<button id="importBtn" onclick="submitForm()" tabindex="150" class="btn_medium">一括更新</button>
						</div>
					</div>
				</div>
			</s:form>
		</div>
	</div>

</body>
</html>
