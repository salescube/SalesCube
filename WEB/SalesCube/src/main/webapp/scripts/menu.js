
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
    	$("#menubar1").css("background-color", "#" + bgColor);
    	$("#" + menuId + "_L").css("background-image", "url('" + contextRoot + "/images/menu/" + bgColor + "_L.png')");
    	$("#" + menuId + "_M").css("background-image", "url('" + contextRoot + "/images/menu/" + bgColor + "_M.png')");
    	$("#" + menuId + "_R").css("background-image", "url('" + contextRoot + "/images/menu/" + bgColor + "_R.png')");
    }
    if(fontColor) {
    	$("#" + menuId + "_M").css("color", "#" + fontColor);
    }

    // 前回選択メニューを非表示
    $("#" + menujs_CURRENT_MENU_ID + "_L").css("background-image", "");
    $("#" + menujs_CURRENT_MENU_ID + "_M").css("background-image", "");
    $("#" + menujs_CURRENT_MENU_ID + "_R").css("background-image", "");
    $("#" + menujs_CURRENT_MENU_ID + "_M").css("color", "");


    $("#" + menujs_CURRENT_MENU_ID + "_row").hide();
    $("#" + menuId + "_row").show();

    menujs_CURRENT_MENU_ID = menuId;
}

