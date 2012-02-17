//ヘルプ表示無効
document.onhelp = function() {
	return false;
}

//入力キー制御
function keyCodeCancel(){
	if($(document.activeElement).attr("type") != "file"){
		if (jQuery.browser.msie) {
			window.event.keyCode = 0;
			window.event.returnValue = false;
		}
	}
	return false;
}

$(function() {
	// ファンクションキーボタンにIDを付与する
	var i = 1;
	$(".function_buttons").children("button").each(
		function() {
			$(this).attr("id", "btnF" + i);
			i++;
		}
	);

	//F1キーへの機能割り当て
	$(document).bind('keydown', 'F1', function() {
		OnFKey("F1");//onF1();
		return keyCodeCancel();
	});

	// F2キーへの機能割り当て
	$(document).bind('keydown', 'F2', function() {
		OnFKey("F2");//onF2();
		return keyCodeCancel();
	});

	// F3キーへの機能割り当て
	$(document).bind('keydown', 'F3', function() {
		OnFKey("F3");//onF3();
		return keyCodeCancel();
	});

	// F4キーへの機能割り当て
	$(document).bind('keydown', 'F4', function() {
		OnFKey("F4");//onF4();
		return keyCodeCancel();
	});

	// F5キーへの機能割り当て
	$(document).bind('keydown', 'F5', function() {
		OnFKey("F5");//onF5();
		return keyCodeCancel();
	});

	// F6キーへの機能割り当て
	$(document).bind('keydown', 'F6', function() {
		OnFKey("F6");//onF6();
		return keyCodeCancel();
	});

	// F7キーへの機能割り当て
	$(document).bind('keydown', 'F7', function() {
		OnFKey("F7");//onF7();
		return keyCodeCancel();
	});

	// F8キーへの機能割り当て
	$(document).bind('keydown', 'F8', function() {
		OnFKey("F8");//onF8();
		return keyCodeCancel();
	});

	// F9キーへの機能割り当て
	$(document).bind('keydown', 'F9', function() {
		OnFKey("F9");//onF9();
		return keyCodeCancel();
	});

	// F10キーへの機能割り当て
	$(document).bind('keydown', 'F10', function() {
		OnFKey("F10");//onF10();
		return keyCodeCancel();
	});

	// F11キーへの機能割り当て
	$(document).bind('keydown', 'F11', function() {
		OnFKey("F11");//onF11();
		return keyCodeCancel();
	});

	// F12キーへの機能割り当て
	$(document).bind('keydown', 'F12', function() {
		OnFKey("F12");//onF12();
		return keyCodeCancel();
	});
});

// 無効制御用ラッパ
// idは"btnF??"
function OnFKey(FKeyName){
	var selector = "#btnFX:enabled".replace("FX", FKeyName);
	if( $(selector).size() > 0 ){
		switch (FKeyName){
		  case "F1" : onF1();  break;
		  case "F2" : onF2();  break;
		  case "F3" : onF3();  break;
		  case "F4" : onF4();  break;
		  case "F5" : onF5();  break;
		  case "F6" : onF6();  break;
		  case "F7" : onF7();  break;
		  case "F8" : onF8();  break;
		  case "F9" : onF9();  break;
		  case "F10": onF10(); break;
		  case "F11": onF11(); break;
		  case "F12": onF12(); break;
		}
	}
	return false;
}

// 空の関数
function onF1() {
}
function onF2() {
}
function onF3() {
}
function onF4() {
}
function onF5() {
}
function onF6() {
}
function onF7() {
}
function onF8() {
}
function onF9() {
}
function onF10() {
}
function onF11() {
}
function onF12() {
}
