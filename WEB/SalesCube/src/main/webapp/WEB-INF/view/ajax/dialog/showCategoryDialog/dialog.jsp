<script type="text/javascript">
<!--

function onClose(){

	var checkedCnt = $('input:${formType}:checked').length;

	if(checkedCnt > 0){
		var result = "";
		for(var i = 0;i<checkedCnt;i++){
			result = result + $('input:${formType}:checked').eq(i).val() + "\r\n";
		}

		// 呼び出し元画面のコールバック関数を呼ぶ
		_selectSearchResultSimple( '${dialogId}',result);

	}

	// 画面を閉じる
	$('#${dialogId}').dialog('close');
}

-->
</script>

<div id="${dialogId}" title="区分情報" style="display: none;">
<div style="padding: 20px 20px 0 20px;">
		<c:forEach var="a" items="${categoryDtoList}" varStatus="status">
			<input type="${formType}" name="category" value="${a.categoryCodeName}">
			${a.categoryCodeName}
			<br>
		</c:forEach>

	<div style="width: 550px; text-align: right">
		<button type="button" style="width: 70px" tabindex="11000"
			onclick="onClose()">閉じる</button>
	</div>
</div>
</div>