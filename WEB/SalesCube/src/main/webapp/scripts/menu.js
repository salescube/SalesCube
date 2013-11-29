
var menujs_CURRENT_MENU_ID = null;

function changeMenu1(menuId, bgColor, fontColor) {
	if(!menujs_CURRENT_MENU_ID) {
		menujs_CURRENT_MENU_ID = $("#menujs_PARENT_MENU_ID").val();
	}
    if (menujs_CURRENT_MENU_ID == menuId) {
        return;
    }

    // 今回選択メニューを表示
    if(bgColor) {
    	$(".submenubar").css("background-color", "#" + bgColor);
//    	$("#" + menuId + "_L").css("background-image", "url('" + contextRoot + "/images/menu/" + bgColor + "_L.png')");
//    	$("#" + menuId + "_M").css("background-image", "url('" + contextRoot + "/images/menu/" + bgColor + "_M.png')");
//    	$("#" + menuId + "_R").css("background-image", "url('" + contextRoot + "/images/menu/" + bgColor + "_R.png')");
    }
//    if(fontColor) {
//    	$("#" + menuId + "_M").css("color", "#" + fontColor);
//    }

    // 前回選択メニューを非表示
//    $("#" + menujs_CURRENT_MENU_ID + "_L").css("background-image", "");
//    $("#" + menujs_CURRENT_MENU_ID + "_M").css("background-image", "");
//    $("#" + menujs_CURRENT_MENU_ID + "_R").css("background-image", "");
//    $("#" + menujs_CURRENT_MENU_ID + "_M").css("color", "");


    $("#" + menujs_CURRENT_MENU_ID + "_row").hide();
    $("#" + menuId + "_row").show();
    
	// クリックされたメニューのアイコンをアクティブのアイコンに変える
    $("#img_gnavi_" + menuId).attr("src", contextRoot + "/images/menu/btn_gnavi_" + menuId + "_on.png");
	$("#img_gnavi_" + menuId).attr("oSrc", contextRoot + "/images/menu/btn_gnavi_" + menuId + "_on.png");
	
	// 前回クリックされたアイコンを非アクティブのアイコンに変える
	if (menujs_CURRENT_MENU_ID != null) {
		$("#img_gnavi_" + menujs_CURRENT_MENU_ID).attr("src", contextRoot + "/images/menu/btn_gnavi_" + menujs_CURRENT_MENU_ID + ".png");
		$("#img_gnavi_" + menujs_CURRENT_MENU_ID).attr("hSrc", contextRoot + "/images/menu/btn_gnavi_" + menujs_CURRENT_MENU_ID + "_on.png");
		$("#img_gnavi_" + menujs_CURRENT_MENU_ID).attr("oSrc", contextRoot + "/images/menu/btn_gnavi_" + menujs_CURRENT_MENU_ID + ".png");
	}

    menujs_CURRENT_MENU_ID = menuId;
}

