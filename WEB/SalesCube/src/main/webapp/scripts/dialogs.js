
/*************************************************************************
 * 非公開関数
 *************************************************************************/

/**
 * ダイアログIDと戻りの関数オブジェクトを紐付けるハッシュ
 */
var dialogIdToFunc = new Object();

/**
 * 各ダイアログの共通パラメータ
 */
var Params = function() {};
Params.prototype.draggable = true;
Params.prototype.resizable = false;
Params.prototype.bgiframe = true;
Params.prototype.autoOpen = false;
Params.prototype.modal = false;
Params.prototype.buttons = {};
Params.prototype.close = function(event, ui) {
	delete dialogIdToFunc[this.id];
	$("#" + this.id).remove();
};

/**
 * 商品検索画面のパラメータ
 */
var ProductParams = new Params();
ProductParams.url = "/ajax/dialog/searchProductDialog";
ProductParams.width = 800;
ProductParams.height = 630;

/**
 * 顧客検索画面のパラメータ
 */
var CustomerParams = new Params();
CustomerParams.url = "/ajax/dialog/searchCustomerDialog";
CustomerParams.width = 750;
CustomerParams.height = 600;

/**
 * 仕入先検索画面のパラメータ
 */
var SupplierParams = new Params();
SupplierParams.url = "/ajax/dialog/searchSupplierDialog";
SupplierParams.width = 660;
SupplierParams.height = 540;

/**
 * 郵便番号検索画面のパラメータ
 */
var ZipParams = new Params();
ZipParams.url = "/ajax/dialog/searchZipCodeDialog";
ZipParams.width = 650;
ZipParams.height = 590;

/**
 * 担当者検索画面のパラメータ
 */
var UserParams = new Params();
UserParams.url = "/ajax/dialog/searchUserDialog";
UserParams.width = 700;
UserParams.height = 600;

/**
 * 棚番検索画面のパラメータ
 */
var RackParams = new Params();
RackParams.url = "/ajax/dialog/searchRackDialog";
RackParams.width = 700;
RackParams.height = 540;


/**
 * 倉庫検索画面のパラメータ
 */
var WarehouseParams = new Params();
WarehouseParams.url = "/ajax/dialog/searchWarehouseDialog";
WarehouseParams.width = 600;
WarehouseParams.height = 540;

/**
 * 商品在庫情報画面のパラメータ
 */
var StockInfoParams = new Params();
StockInfoParams.url = "/ajax/dialog/showStockInfoDialog";
StockInfoParams.width = 940;
StockInfoParams.height = 650;

/**
 * ファイル参照画面のパラメータ
 */
var ReferFilesParams = new Params();
ReferFilesParams.url = "/ajax/dialog/referFilesDialog";
ReferFilesParams.width = 650;
ReferFilesParams.height = 370;

/**
 * 検索結果設定画面のパラメータ
 */
var DetailDispParams = new Params();
DetailDispParams.url = "/ajax/dialog/detailDispSettingDialog";
DetailDispParams.width = 520;
DetailDispParams.height = 400;

/**
 * 数量割引検索画面のパラメータ
 */
var DiscountParams = new Params();
DiscountParams.url = "/ajax/dialog/searchDiscountDialog";
DiscountParams.width = 850;
DiscountParams.height = 590;

/**
 * 伝票複写画面のパラメータ
 */
var CopySlipParams = new Params();
CopySlipParams.url = "/ajax/dialog/copySlipDialog";
CopySlipParams.width = 640;
CopySlipParams.height = 720;

/**
 * マスタ初期値設定画面のパラメータ
 */
var MasterDefaultSettingParams = new Params();
MasterDefaultSettingParams.url = "/ajax/dialog/masterDefaultSettingDialog";
MasterDefaultSettingParams.width = 500;
MasterDefaultSettingParams.height = 350;

/**
 * 区分データ画面のパラメータ
 */
var CategoryParams = new Params();
CategoryParams.url = "/ajax/dialog/showCategoryDialog";
CategoryParams.width = 600;
CategoryParams.height = 300;


/**
 * ダイアログオープン
 *
 * @param dialogParams
 * @param dialogId
 * @param endFunc
 * @return
 */


function _openDialog(dialogId, endFunc, dialogParams, data) {

	if ($("#" + dialogId).size() > 0) {
		// 既に開いていれば開かない
		return;
	}

	// リクエストパラメータ
	var reqestData = data;
	if(!reqestData) {
		requestData = {};
	}

	// ダイアログHTMLをロードする
	syncRequest(
		contextRoot + dialogParams.url + "/" + dialogId,
		data,
		function(data) {
			$("#main_function").after(data);

			// ダイアログとして表示する
			$("#" + dialogId).dialog(dialogParams);
			$("#" + dialogId).dialog("open");
			if (jQuery.browser.msie) {
				$("#" + dialogId).dialog("option", "height", dialogParams.height);
				$("#" + dialogId).css("height", dialogParams.height);
				$("#" + dialogId).dialog("option", "position", "center");
			}

			// エンターキー移動設定
			var target = $("#" + dialogId).find(":input:visible[tabindex]" +
					"[type!='hidden'][type!='file'][type!='reset'][type!='submit'][type!='button'][type!='textarea']" +
					"[readonly!='true'][disabled!='true'][class!='c_disable']:");
			if (target.size() > 0) {
				target.bind('keypress', 'return', move_focus_to_next_tabindex );
			}

			// 選択時実行関数設定
			if(endFunc) {
				dialogIdToFunc[dialogId] = endFunc;
			}

			// スクロールする検索結果の存在チェック
			if($("#" + dialogId + "List").size() == 0) {
				return false;
			}
			try {
				new superTable(dialogId + "List", {
					cssSkin : "sOrange",
					fixedCols : 0,
					headerRows : 1,
					onStart : function() {
					},
					onFinish : function() {
					}
				});
			} catch (e) {
			}
		}
	);

	if (jQuery.browser.msie && window.event) {
		window.event.keyCode = null;
		window.event.returnValue = false;
	}

	return false;
}

/**
 * 検索処理実行
 * @param dialogId
 * @return
 */
function _search(dialogParams, dialogId, data, successFunc, errFunc ) {

	$("#" + dialogId + "_errors").empty();

	asyncRequest(
		contextRoot + dialogParams.url + "/search/" + dialogId,
		data,
		function(result) {
			if(successFunc) {
				return successFunc(result);
			}

			if(data.slipName) {
				// 伝票複写ダイアログの場合は特殊

				$("#" + dialogId + "_" + data.slipName + "_ListContainer").empty();
				$("#" + dialogId + "_" + data.slipName + "_ListContainer").html(result);
				// 検索結果スクロールの再設定
				try {
					new superTable(dialogId + "_" + data.slipName + "_List", {
						cssSkin : "sOrange",
						fixedCols : 0,
						headerRows : 1,
						onStart : function() {
						},
						onFinish : function() {
						}
					});
				} catch (e) {
				}
				return;
			}

			$("#" + dialogId + "ListContainer").empty();
			$("#" + dialogId + "ListContainer").html(result);
			// 検索結果スクロールの再設定
			try {
				new superTable(dialogId + "List", {
					cssSkin : "sOrange",
					fixedCols : 0,
					headerRows : 1,
					onStart : function() {
					},
					onFinish : function() {
					}
				});
			} catch (e) {
			}
		},
		function(xmlHttpRequest, textStatus, errorThrown) {
			if(errFunc) {
				return errFunc(xmlHttpRequest, textStatus, errorThrown);
			}

			if (xmlHttpRequest.status == 450) {
				// Ajaxダイアログエラー
				$("#" + dialogId + "_errors").empty();
				$("#" + dialogId + "_errors").append(xmlHttpRequest.responseText);
			} else if (xmlHttpRequest.status == 401) {
				// 未ログイン
				alert(xmlHttpRequest.responseText);
				window.location.href = contextRoot + "/login/" + domainId;
			} else {
				// その他のエラー
				alert(xmlHttpRequest.responseText);
			}
		}
	);

	return false;
}


 /**
  * ダイアログ内の入力データをまとめてパラメータオブジェクトを作成する
  *
  * @param prefix ダイアログ内のinput要素のname属性プレフィックス
  * @param selector jQueryの要素セレクタ
  * @return パラメタータオブジェクト
  */
function _createData(prefix, elements) {
	  var data = new Object();

	  var regex = new RegExp();
	  regex.compile(prefix + "_([^_]+)");

	  $(elements).each(
			  function() {
				  match = this.name.match(regex);
				  data[ match[1] ] = $(this).val();
			  }
	  );

	  return data;
}

/**
  * 検索結果の選択処理
  * @return
  */
function _selectSearchResultAjax(dialogId, radioId, dialogParams, keyName ) {
  	// 選択された項目に関する情報をハッシュで作成する
  	var selectValue = $("input[name='" + dialogId + "_" + radioId + "']:checked").val();
  	var func = window.dialogIdToFunc[dialogId];
  	var data = new Object();
  	data[ keyName ] = selectValue;

  	asyncRequest(
  		contextRoot + dialogParams.url + "/select",
  		data,
		function(data) {
		  	// ダイアログの選択処理関数を取得する

		  	if (func != null && func instanceof Function) {
		  		// 呼び出し
		  		func(dialogId, eval("(" + data + ")"));
		  	}
		}
  	);
}
/**
 * 検索結果の選択処理
 * リンク押下で決定しダイアログを閉じる
 * @return
 */
	function _selectLinkSearchResultAjax(dialogId, selectVal, dialogParams, keyName ) {
	  	// 選択された項目に関する情報をハッシュで作成する
	  	var selectValue = selectVal;
	  	var func = window.dialogIdToFunc[dialogId];
	  	var data = new Object();
	  	data[ keyName ] = selectValue;

	  	asyncRequest(
	  		contextRoot + dialogParams.url + "/select",
	  		data,
			function(data) {
			  	// ダイアログの選択処理関数を取得する

			  	if (func != null && func instanceof Function) {
			  		// 呼び出し
			  		func(dialogId, eval("(" + data + ")"));
			  	}
			}
	  	);
	}
/**
 * 検索結果の選択処理
 * @return
 */
function _selectSearchResult(dialogId, radioId) {
	// 選択された項目に関する情報をハッシュで作成する
	var prefix = dialogId + "_" + $("input[name='" + dialogId + "_" + radioId + "']:checked").val();

	var data = _createData(prefix,
			$("#" + dialogId).find("input[type='hidden'][name^='" + prefix + "_']"));

	// ダイアログの選択処理関数を取得する
	var func = dialogIdToFunc[dialogId];
	if (func != null && func instanceof Function) {
		// 呼び出し
		func(dialogId, data);
	}
}

/**
 * 検索結果の選択処理
 *  リンク押下で決定しダイアログを閉じる
 * @return
 */
function _selectLinkSearchResult(dialogId, selectVal) {
	// 選択された項目に関する情報をハッシュで作成する
	var prefix = dialogId + "_" + selectVal;

	var data = _createData(prefix,
			$("#" + dialogId).find("input[type='hidden'][name^='" + prefix + "_']"));

	// ダイアログの選択処理関数を取得する
	var func = dialogIdToFunc[dialogId];
	if (func != null && func instanceof Function) {
		// 呼び出し
		func(dialogId, data);
	}
}


 /**
  * 検索結果の選択処理
  * @return
  */
 function _selectSearchResultSimple(dialogId, result) {

	// ダイアログの選択処理関数を取得する
	var func = dialogIdToFunc[dialogId];
	if (func != null && func instanceof Function) {
		// 呼び出し
		func(result);
	}
 }


/**
 * 商品検索実行
 *
 * @return
 */
function _searchProduct(dialogId) {
	var data = _createData(dialogId, $("#" + dialogId).find("input[type='text'], select"));
	_search(ProductParams, dialogId, data);
}

/**
 * 顧客検索実行
 * @return
 */
function _searchCustomer(dialogId) {
	var data = _createData(dialogId, $("#" + dialogId).find("input[type='text'], select"));
	_search(CustomerParams, dialogId, data);
}

/**
 * 仕入先検索実行
 * @return
 */
function _searchSupplier(dialogId) {
	var data = _createData(dialogId, $("#" + dialogId).find("input[type='text']"));
	_search(SupplierParams, dialogId, data);
}

/**
 * 郵便番号検索実行
 * @return
 */
function _searchZipCode(dialogId) {
	// リクエストデータ作成
	var data = _createData(dialogId, $("#" + dialogId).find("input[type='text']"));
	_search(ZipParams, dialogId, data);
}

/**
 * 担当者検索実行
 * @return
 */
function _searchUser(dialogId) {
	var data = _createData(dialogId, $("#" + dialogId).find("input[type='text'], select"));
	_search(UserParams, dialogId, data);
}

/**
 * 棚番検索実行
 * @return
 */
function _searchRack(dialogId) {
	var data = _createData(dialogId, $("#" + dialogId).find("input[type='text'], input[type='checkbox']:checked, select"));
	_search(RackParams, dialogId, data);
}

/**
 * 倉庫検索実行
 * @return
 */
function _searchWarehouse(dialogId) {
	var data = _createData(dialogId, $("#" + dialogId).find("input[type='text'], input[type='checkbox']:checked, select"));
	_search(WarehouseParams, dialogId, data);
}

 /**
  * 検索結果設定ダイアログの初期化ボタン
  *
  * @param dialogId
  * @return
  */
function _initDispItem(dialogId) {
	var originalDisabledList = $("#" + dialogId + "_originalDisabledItemList").attr("value");
	var originalEnabledList = $("#" + dialogId + "_originalEnabledItemList").attr("value");

	// 非表示項目リストを初期化する
	var originalDisabledList = eval(originalDisabledList);
	if(originalDisabledList) {
		var disabledList = $("#" + dialogId + "_disabledItemList");
		disabledList.empty();
		for(var i = 0; i < originalDisabledList.length; i++) {
			var option = $(document.createElement("option"));
			option.attr("value", originalDisabledList[i].value);
			option.append(document.createTextNode(originalDisabledList[i].label));
			disabledList.append(option);
		}
	}

	// 表示項目リストを初期化する
	var originalEnabledList = eval(originalEnabledList);
	if(originalEnabledList) {
		var enabledList = $("#" + dialogId + "_enabledItemList");
		enabledList.empty();
		for(var i = 0; i < originalEnabledList.length; i++) {
			var option = $(document.createElement("option"));
			option.attr("value", originalEnabledList[i].value);
			option.append(document.createTextNode(originalEnabledList[i].label));
			enabledList.append(option);
		}
	}
}

/**
 * 検索結果設定ダイアログの→ボタン処理
 * @param dialogId
 * @return
 */
function _enableDispItem(dialogId) {
	 var disabledList = $("#" + dialogId + "_disabledItemList").get(0);
	 if(disabledList.selectedIndex < 0) {
		 // 選択なし
		 return;
	 }

	 // 選択項目を表示項目リストに移動
	 $("#" + dialogId + "_enabledItemList").append(
			 disabledList.options[ disabledList.selectedIndex ]);
}

/**
* 検索結果設定ダイアログの←ボタン処理
* @param dialogId
* @return
*/
function _disableDispItem(dialogId) {
	 var enabledList = $("#" + dialogId + "_enabledItemList").get(0);
	 if(enabledList.selectedIndex < 0) {
		 // 選択なし
		 return;
	 }

	 // 選択項目を表示項目リストに移動
	 $("#" + dialogId + "_disabledItemList").append(
			 enabledList.options[ enabledList.selectedIndex ]);
}

/**
* 検索結果設定ダイアログの↑ボタン処理
* @param dialogId
* @return
*/
function _upDispItem(dialogId) {
	 var enabledList = $("#" + dialogId + "_enabledItemList").get(0);
	 if(enabledList.selectedIndex < 0) {
		 // 選択なし
		 return;
	 }
	 var selectedOption = $(enabledList.options[ enabledList.selectedIndex ]);

	 // 前の表示項目を取得
	 var prevOption = selectedOption.prev();
	 if(prevOption.size() == 0) {
		 return;
	 }

	 prevOption.before(selectedOption);
}

/**
* 検索結果設定ダイアログの↓ボタン処理
* @param dialogId
* @return
*/
function _downDispItem(dialogId) {
	 var enabledList = $("#" + dialogId + "_enabledItemList").get(0);
	 if(enabledList.selectedIndex < 0) {
		 // 選択なし
		 return;
	 }
	 var selectedOption = $(enabledList.options[ enabledList.selectedIndex ]);

	 // 後の表示項目を取得
	 var prevOption = selectedOption.next();
	 if(prevOption.size() == 0) {
		 return;
	 }

	 prevOption.after(selectedOption);
}

/**
 * 検索結果設定ダイアログの更新ボタン処理
 * @param dialogId
 * @return
 */
function _updateDetailDispSetting(dialogId) {
	 var enabledList = $("#" + dialogId + "_enabledItemList");
	 var enabledOptions = enabledList.children();
	 if(enabledOptions == null || enabledOptions.length == 0) {
		 alert("表示項目の選択は必須です。");
		 return;
	 }

	 // 必須項目情報リスト
	 var requiredItemList = $("#" + dialogId + "_requiredItemList").attr("value");
	 requiredItemList = eval(requiredItemList);

	 // 必須項目が全て選択されている事をチェック
	 for(var i = 0; i < requiredItemList.length; i++) {
		 var requireOk = false;
		 for(var j = 0; j < enabledOptions.length; j++) {
			 if(requiredItemList[i].value == enabledOptions[j].value) {
				 requireOk = true;
				 break;
			 }
		 }

		 if(!requireOk) {
			 alert( requiredItemList[i].label + "の選択は必須です。");
			 return;
		 }
	 }

	 // リクエストデータを作成する
	 var data = new Object();
	 for(var i = 0; i < enabledOptions.length; i++) {
		 data[ "enabledDispItemList[" + i + "]" ] = enabledOptions[i].value;
	 }
	 data[ "menuId" ] = $("#" + dialogId + "_menuId").val();
	 data[ "target" ] = $("#" + dialogId + "_target").val();

	 // 旧設定の先頭項目を排他制御情報として含める
	 var lockItemId = $("#" + dialogId + "_lockItemId").attr("value");
	 var lockUpdDatetm = $("#" + dialogId + "_lockUpdDatetm").attr("value");
	 if(lockItemId && lockUpdDatetm) {
		 data[ "lockItemId" ] = lockItemId;
		 data[ "lockUpdDatetm" ] = lockUpdDatetm;
	 }

	 asyncRequest(
		 contextRoot + DetailDispParams.url + "/update/" + dialogId,
		 data,
		 function(){
			// ダイアログを閉じる
			$("#" + dialogId).dialog("close");
		 },
		 function(xmlHttpRequest, textStatus, errorThrown) {
			 if (xmlHttpRequest.status == 450) {
				// 想定内エラー
				$("#" + dialogId + "_errors").empty();
				$("#" + dialogId + "_errors").append(xmlHttpRequest.responseText);
			} else if (xmlHttpRequest.status == 401) {
				// 未ログイン
				alert(xmlHttpRequest.responseText);
				window.location.href = contextRoot + "/login/" + domainId;
			} else {
				// その他のエラー
				alert(xmlHttpRequest.responseText);
			}
		}
	);
}

/**
 * 数量割引検索実行
 * @return
 */
function _searchDiscount(dialogId) {
	var data = _createData(dialogId, $("#" + dialogId).find("input[type='text'], select"));
	_search(DiscountParams, dialogId, data);
}

 /**
  * 数量割引ラジオボタンチェック
  * @param dialogId
  * @return
  */
function _updateDiscountData(dialogId, radioId, selectedVal) {
	var selectedValue = null;
	selectedValue = selectedVal;

	// 既存の数量割引データテーブルをクリアする
	$("#" + dialogId + "DataListContainer").empty();
	var clone = $("#templateDiv").clone(true);
	clone.removeAttr("id");
	clone.css("display", "block");
	$("#" + dialogId + "DataListContainer").html(clone);

	// tableタグのid修正
	var table = clone.children("#template" + dialogId + "DataList");
	table.attr("id", dialogId + "DataList");


	// 選択した数量割引に紐づく割引データの行番号を全て取得する
	var prefix = dialogId + "_" + selectedValue + "_Data";
	var lineNoList = $("input[name^='" + prefix + "'][name$='lineNo']");

	if(lineNoList && lineNoList.length > 0) {
		// 行番号を昇順にソートする
		lineNoList.sort(function(a, b) {
				if(parseInt(a.value) > parseInt(b.value)) {
					return 1;
				}
				else {
					return -1;
				}
			}
		);

		// 行番号順にテーブルの行を生成する
		for(var i = 0; i < lineNoList.length; i++) {
			var discountDataId = $("input[name='" + prefix + lineNoList[i].value + "_discountDataId']");
			var dataFrom = $("input[name='" + prefix + lineNoList[i].value + "_dataFrom']");
			var dataTo = $("input[name='" + prefix + lineNoList[i].value + "_dataTo']");
			var discountRate = $("input[name='" + prefix + lineNoList[i].value + "_discountRate']");

			// 行を生成
			var tr = $(document.createElement("tr"));

			// No
			var td = $(document.createElement("td"));
			td.append(document.createTextNode(lineNoList[i].value));
			td.css("text-align", "center");
			tr.append(td);

			// 数量範囲
			td = $(document.createElement("td"));
			tr.append(td);
			td.append(document.createTextNode(dataFrom.attr("value") + "～" + dataTo.attr("value")));

			// 掛率
			td = $(document.createElement("td"));
			tr.append(td);
			td.append(document.createTextNode(discountRate.attr("value")));

			table.append(tr);
		}
		return "<table class='popup_resultList'>" + table.html() + "</table>";
	}

	// 検索結果スクロールの再設定
	try {
		new superTable(dialogId + "DataList", {
			cssSkin : "sOrange",
			fixedCols : 0,
			headerRows : 1,
			onStart : function() {
			},
			onFinish : function() {
			}
		});
	} catch (e) {
	}
}

/**
 * 割引データテーブルをクリアする
 * @param dialogId
 * @return
 */
function _clearDiscountData(dialogId) {
	// 既存の数量割引データテーブルを削除する
	$("#" + dialogId + "DataListContainer").empty();
	var clone = $("#templateDiv").clone(true);
	clone.removeAttr("id");
	clone.css("display", "block");
	$("#" + dialogId + "DataListContainer").html(clone);

	// 新しい数量割引データテーブルを取得する
	var table = clone.children("#template" + dialogId + "DataList");
	table.attr("id", dialogId + "DataList");

	// 検索結果スクロールの再設定
	try {
		new superTable(dialogId + "DataList", {
			cssSkin : "sOrange",
			fixedCols : 0,
			headerRows : 1,
			onStart : function() {
			},
			onFinish : function() {
			}
		});
	} catch (e) {
	}
}

/**
 * 商品検索ダイアログで仕入先検索画面を呼び出した場合のコールバック関数
 * @param dialogId ダイアログID
 * @param map 仕入先情報がセットされているオブジェクト
 * @return
 */
function _searchSupplierCallBack(dialogId, map) {
	// 仕入先ダイアログのIDから、商品ダイアログのIDとフィールド名を取得
	var regex = new RegExp("(.+)_supplier(Name|Code)SearchDialog$");
	var match = dialogId.match(regex);
	if(!match) {
		return;
	}

	// 挿入先の要素を取得
	var target = $("#" + match[1] + "_" + "supplier" + match[2]);
	if(target.length > 0) {
		target.attr("value", map[ "supplier" + match[2] ]);
	}
}

 /**
  * 伝票複写ダイアログで複写元伝票を選択した場合の処理
  * @param dialogId ダイアログID
  * @param slipName 選択された伝票名
  * @return
  */
 function _changeSlipName(dialogId, slipName) {
	  $("#" + dialogId + "_copyButton").attr("disabled", true);

	// 全ての伝票検索フォームを非表示
	var slipSearchDivs = $("div[id^='" + dialogId + "_']:visible").filter("[id$='_copy']");
	if(slipSearchDivs.size() == 0) {
		return;
	}
	slipSearchDivs.hide();

	var prefix = dialogId + "_" + slipName;
	// 選択されている伝票の検索フォームを表示
	var slipSearchDiv = $("#" + prefix + "_copy");
	if(slipSearchDiv.size() == 0) {
		return;
	}
	slipSearchDiv.show();

	// SuperTablesが生成されていなければ生成する(sBaseはSuperTablesが付与するclass属性値)
	if( slipSearchDiv.find("[class='sBase']").size() == 0) {
		try {
			new superTable(prefix + "_List", {
				cssSkin : "sOrange",
				fixedCols : 0,
				headerRows : 1,
				onStart : function() {
				},
				onFinish : function() {
				}
			});
		} catch (e) {
		}
	}
 }

/**
 * 伝票複写ダイアログでの検索実行
 *
 * @param dialogId ダイアログID
 * @param slipName 伝票名
 * @return
 */
function _searchCopySlip(dialogId, slipName) {
	// リクエストデータ作成
	var data = new Object();
	data["slipName"] = slipName;

	var conditions = $("#" + dialogId).find("input[type='text'], input[type='checkbox'], input[type='hidden']");
	var regex = new RegExp(dialogId + "_([^_]+)");
	conditions.each(
		function() {
			if($(this).attr("type") == "checkbox" && !$(this).attr("checked")) {
				// checkboxがチェックされていない場合
				return;
			}
			match = this.name.match(regex);
			if(match != null) {
				data[ match[1] ] = $(this).val();
			}
		}
	);

	_search(CopySlipParams, dialogId, data);
}

 /**
  * 伝票複写ダイアログの複写ボタン処理
  * @param dialogId
  * @param slipName
  * @return
  */
function _copySlip(dialogId, slipName) {
	// ダイアログで選択されたラジオボタンの値を取得する
	var value = $( "input[name='" + dialogId + "_" + slipName + "_selectedSlip']:checked").val();

	// ダイアログの選択処理関数を取得する
	var func = dialogIdToFunc[dialogId];
	if (func != null && func instanceof Function) {
		// 呼び出し
		func(dialogId, slipName, value);
	}
}

/**
 * 伝票複写ダイアログの複写ボタン処理
 * @param dialogId
 * @param slipName
 * @return
 */
function _copyLinkSlip(dialogId, slipName,selectValue) {
	//選択した値を取得する
	var value = selectValue;

	// ダイアログの選択処理関数を取得する
	var func = dialogIdToFunc[dialogId];
	if (func != null && func instanceof Function) {
		// 呼び出し
		func(dialogId, slipName, value);
	}
}
/*
 * マスタ初期値設定画面の更新ボタン処理
 */
function _updateMasterDefaultSetting(dialogId, tableName) {
	// リクエストデータ作成
	var data = new Object();
	data[ "tableName" ] = tableName;

	var conditions = $("#" + dialogId).find("input[type='text'], input[type='hidden'], select");
	var regex = new RegExp(dialogId + "_([^_]+)");
	conditions.each(
		function() {
			match = this.name.match(regex);
			data[ match[1] ] = $(this).val();
		}
	);

	 asyncRequest(
		 contextRoot + MasterDefaultSettingParams.url + "/update/" + dialogId,
		 data,
		 function(){
			// ダイアログを閉じる
			$("#" + dialogId).dialog("close");
		},
		function(xmlHttpRequest, textStatus, errorThrown) {
			if (xmlHttpRequest.status == 450) {
				// 想定内エラー
				$("#" + dialogId + "_errors").empty();
				$("#" + dialogId + "_errors").append(xmlHttpRequest.responseText);
			} else if (xmlHttpRequest.status == 401) {
				// 未ログイン
				alert(xmlHttpRequest.responseText);
				window.location.href = contextRoot + "/login/" + domainId;
			} else {
				// その他のエラー
				alert(xmlHttpRequest.responseText);
			}
		}
	);
}

/*************************************************************************
 * 公開関数
 *************************************************************************/

/**
 * 商品検索ダイアログを開く
 *
 * @param dialogId ダイアログID(画面内で一意であること)
 * @param endFunc コールバック関数オブジェクト
 * @param dialogParams (任意指定）
 * @return
 */
function openSearchProductDialog(dialogId, endFunc, params) {
	var openParams = null;
	if (params) {
		openParams = params;
	} else {
		openParams = ProductParams;
	}
	_openDialog(dialogId, endFunc, openParams);
}

/**
 * 顧客検索ダイアログを開く
 *
 * @param dialogId ダイアログID(画面内で一意であること)
 * @param endFunc コールバック関数オブジェクト
 * @param dialogParams (任意指定）
 * @return
 */
function openSearchCustomerDialog(dialogId, endFunc, params) {
	var openParams = null;
	if (params) {
		openParams = params;
	} else {
		openParams = CustomerParams;
	}
	_openDialog(dialogId, endFunc, openParams);
}

/**
 * 仕入先検索ダイアログを開く
 *
 * @param dialogId ダイアログID(画面内で一意であること)
 * @param endFunc コールバック関数オブジェクト
 * @param dialogParams (任意指定）
 * @return
 */
function openSearchSupplierDialog(dialogId, endFunc, params) {
	var openParams = null;
	if (params) {
		openParams = params;
	} else {
		openParams = SupplierParams;
	}
	_openDialog(dialogId, endFunc, openParams);
}

/**
 * 郵便番号検索ダイアログを開く
 *
 * @param dialogId ダイアログID(画面内で一意であること)
 * @param endFunc コールバック関数オブジェクト
 * @param dialogParams (任意指定）
 * @return
 */
function openSearchZipDialog(dialogId, endFunc, params) {
	var openParams = null;
	if (params) {
		openParams = params;
	} else {
		openParams = ZipParams;
	}
	_openDialog(dialogId, endFunc, openParams);
}

/**
 * 検索結果が複数件であれば郵便番号検索ダイアログを開く
 * 1件であればそのデータをコールバック関数に返す
 *
 * @param dialogId ダイアログID(画面内で一意であること)
 * @param endFunc コールバック関数オブジェクト
 * @param zipCode 検索条件にする郵便番号
 * @param zipAddress1 検索条件にする住所１
 * @param dialogParams (任意指定)
 *
 */
function openSearchZipDialogWhenResultMultiple(dialogId, endFunc, zipCode, zipAddress1, params) {
	if(zipCode == null || zipCode.length == 0) {
		if(zipAddress1 == null || zipAddress1.length == 0) {
			return;
		}
	}

	// リクエストデータを作成して検索
	var data = new Object();
	if(zipCode) {
		data["zipCode"] = zipCode;
	}
	if(zipAddress1) {
		data["zipAddress1"] = zipAddress1;
	}

	_search(ZipParams, dialogId, data,
		function(result){
			var searchResultObj = $(result);
			if(searchResultObj.find("#" + dialogId + "_searchResultCount").size() == 0) {
				return;
			}

			// 検索結果件数を取得する
			var searchResultCount = searchResultObj.find("#" + dialogId + "_searchResultCount");
			var count = parseInt(searchResultCount.val());
			if(count == 0) {
				return;
			}

			if(count == 1) {
				// 1件ならば結果をコールバック関数に渡す
				var zipData = new Object();
				zipData["zipId"] = searchResultObj.filter("input[type='hidden'][id$='zipId']").val();
				zipData["zipCode"] = searchResultObj.filter("input[type='hidden'][id$='zipCode']").val();
				zipData["zipAddress1"] = searchResultObj.filter("input[type='hidden'][id$='zipAddress1']").val();
				zipData["zipAddress2"] = searchResultObj.filter("input[type='hidden'][id$='zipAddress2']").val();
				endFunc(dialogId, zipData);
			}
			else {
				// 結果が1件でなければダイアログを表示して検索結果の一覧を表示
				var openParams = null;
				if (params) {
					openParams = params;
				} else {
					openParams = ZipParams;
				}
				_openDialog(dialogId, endFunc, openParams);

				// 条件の設定
				if(zipCode) {
					$("#" + dialogId + "_zipCode").val(zipCode);
				}
				if(zipAddress1) {
					$("#" + dialogId + "_zipAddress1").val(zipAddress1);
				}

				// 検索結果表示
				$("#" + dialogId + "ListContainer").empty();
				$("#" + dialogId + "ListContainer").html(result);
				// 検索結果スクロールの再設定
				try {
					new superTable(dialogId + "List", {
						cssSkin : "sOrange",
						fixedCols : 0,
						headerRows : 1,
						onStart : function() {
						},
						onFinish : function() {
						}
					});
				} catch (e) {
				}
			}
		}
	);
}

/**
 * 担当者検索ダイアログを開く
 *
 * @param dialogId ダイアログID(画面内で一意であること)
 * @param endFunc コールバック関数オブジェクト
 * @param dialogParams (任意指定）
 * @return
 */
function openSearchUserDialog(dialogId, endFunc, params) {
	var openParams = null;
	if (params) {
		openParams = params;
	} else {
		openParams = UserParams;
	}
	_openDialog(dialogId, endFunc, openParams);
}

/**
 * 棚番検索ダイアログを開く
 *
 * @param dialogId ダイアログID(画面内で一意であること)
 * @param endFunc コールバック関数オブジェクト
 * @param dialogParams (任意指定）
 * @return
 */
function openSearchRackDialog(dialogId, endFunc, params) {
	var openParams = null;
	if (params) {
		openParams = params;
	} else {
		openParams = RackParams;
	}
	_openDialog(dialogId, endFunc, openParams);
}


/**
 * 倉庫検索ダイアログを開く
 *
 * @param dialogId ダイアログID(画面内で一意であること)
 * @param endFunc コールバック関数オブジェクト
 * @param dialogParams (任意指定）
 * @return
 */
function openSearchWarehouseDialog(dialogId, endFunc, params) {
	var openParams = null;
	if (params) {
		openParams = params;
	} else {
		openParams = WarehouseParams;
	}
	_openDialog(dialogId, endFunc, openParams);
}
 /**
  * 商品在庫情報ダイアログを開く
  *
  * @param dialogId ダイアログID(画面内で一意であること)
  * @param productCode 商品コード
  * @param dialogParams (任意指定）
  * @return
  */
 function openStockInfoDialog(dialogId, productCode, params) {
 	var openParams = null;
 	if (params) {
 		openParams = params;
 	} else {
 		openParams = StockInfoParams;
 	}
 	_openDialog(dialogId, null, openParams, { "productCode": productCode });
 }

  /**
   * ファイル参照ダイアログを開く
   *
   * @param dialogId ダイアログID(画面内で一意であること)
   * @param dialogParams (任意指定）
   * @return
   */
function openReferFilesDialog(dialogId, params) {
  var openParams = null;
  if (params) {
  	openParams = params;
  } else {
  	openParams = ReferFilesParams;
  }
  _openDialog(dialogId, null, openParams);
}

/**
 * ファイル参照ダイアログを開く
 *
 * @param dialogId ダイアログID(画面内で一意であること)
 * @param dialogParams (任意指定）
 * @return
 */
var dlgSortColumn;
var dlgSortOrderAsc;

function searchReferFiles(dialogId, sortColumn) {
	var openParams = ReferFilesParams;

	// 前回のソートカラムとソート順を取得
	var beforeSortColumn =dlgSortColumn;
	var beforeSortOrderAsc = dlgSortOrderAsc;

	// 今回のソートカラムを設定
	dlgSortColumn = sortColumn;

	// 前回と同じカラムをクリックした場合はソート順を入れ替える
	if(beforeSortColumn == sortColumn) {
		if(beforeSortOrderAsc == "true") {
			dlgSortOrderAsc = "false";
		} else {
			dlgSortOrderAsc = "true";
		}
	}
	// 前回と異なる場合は昇順に設定
	else {
		dlgSortOrderAsc = "true";
	}


	// 今回のソートカラムにソートラベルを追加
	if(dlgSortOrderAsc == "true") {
		$("#sortStatus_"+sortColumn).html("<bean:message key='labels.asc'/>");
	} else {
		$("#sortStatus_"+sortColumn).html("<bean:message key='labels.desc'/>");
	}
	
	var data = new Object();
	data["sortColumn"] = dlgSortColumn;
	data["sortOrderAsc"] = dlgSortOrderAsc;
	$("#" + dialogId).dialog("close");
  _openDialog(dialogId, null, openParams,data);
  

  


}

/**
 * 検索結果設定ダイアログを開く
 *
 * @param dialogId ダイアログID(画面内で一意であること)
 * @param menuId 画面ID
 * @param searchTarget 伝票(1)か明細(2)かConstants.javaでの定義値
 * @return
 */
function openDetailDispSettingDialog(dialogId, menuId, searchTarget, params) {
	var openParams = null;
	if (params) {
		openParams = params;
	} else {
		openParams = DetailDispParams;
	}
	_openDialog(dialogId, null, openParams, { "menuId": menuId, "target": searchTarget });
}

/**
 * 数量割引検索ダイアログを開く
 *
 * @param dialogId ダイアログID(画面内で一意であること)
 * @param endFunc コールバック関数オブジェクト
 * @param dialogParams (任意指定）
 * @return
 */
function openSearchDiscountDialog(dialogId, endFunc, params) {
	var openParams = null;
	if (params) {
		openParams = params;
	} else {
		openParams = DiscountParams;
	}

	// 割引データテーブルのスクロール設定
	openParams.open = function() {
		try {
			new superTable(this.id + "DataList", {
				cssSkin : "sOrange",
				fixedCols : 0,
				headerRows : 1,
				onStart : function() {
				},
				onFinish : function() {
				}
			});
		} catch (e) {
		}
	}

	_openDialog(dialogId, endFunc, openParams);
}

 /**
  * 伝票複写ダイアログを開く
  *
  * @param menuId 画面ID
  * @param dialogId ダイアログID
  * @param endFunc コールバック関数オブジェクト
  * @param params 任意指定
  * @return
  */
function openCopySlipDialog(menuId, dialogId, endFunc, params) {
	var openParams = null;
	if (params) {
		openParams = params;
	} else {
		openParams = CopySlipParams;
	}

	// 伝票複写検索フォームの表示設定
	openParams.open = function() {
		// 日付項目のカレンダー機能付与
		var dateInputs = $("#" + this.id).find("input.date_input");
		if (dateInputs.size() > 0) {
			dateInputs.datepicker(datePickcerSetting);
		}

		// 選択されている伝票を取得
		var slipName = $("#" + this.id + "_slipName").val();

		// 選択されている伝票の検索画面divを取得
		var slipSearchDiv = $("#" + this.id + "_" + slipName + "_copy");
		if(slipSearchDiv.size() == 0) {
			return;
		}

		// 表示
		slipSearchDiv.show();

		try {
			new superTable(this.id + "_" + slipName + "_List", {
				cssSkin : "sOrange",
				fixedCols : 0,
				headerRows : 1,
				onStart : function() {
				},
				onFinish : function() {
				}
			});
		} catch (e) {
		}
	};

	_openDialog(dialogId, endFunc, openParams, { "menuId": menuId } );
}

/**
 * マスタ初期値設定画面を開く
 * @param dialogId ダイアログID
 * @param tableName マスタテーブル名称
 * @param params オープンパラメータ
 * @return
 */
function openMasterDefaultSettingDialog(dialogId, tableName, params) {
 	var openParams = null;

 	// 初期値設定画面のサイズ設定
 	// 顧客マスタの初期値設定画面
 	if (tableName == 'CUSTOMER_MST') {
 		MasterDefaultSettingParams.width = 500;
 		MasterDefaultSettingParams.height = 550;
 		
 	// 仕入先マスタの初期値設定画面
 	} else if (tableName == 'SUPPLIER_MST') {
 		MasterDefaultSettingParams.width = 500;
 		MasterDefaultSettingParams.height = 400;
 	}
 	
 	if (params) {
 		openParams = params;
 	} else {
 		openParams = MasterDefaultSettingParams;
 	}

	// 数値項目の
	openParams.open = function() {
		// 選択されている伝票を取得
		var numeralInputs = $("#" + this.id).find(":input[class='numeral_commas']");
		numeralInputs.each(
			function() {
				$(this).focus(
						function(evt) {
							_rmv_commas(evt.target);
						}
				);

				$(this).blur(
						function(evt) {
							_set_commas(evt.target);
						}
				);

				_after_load($(this));
			}
		);
	};

 	_openDialog(dialogId, null, openParams, { "tableName": tableName });
}

 /**
  * 区分ダイアログを開く
  *
  * @param dialogId ダイアログID(画面内で一意であること)
  * @param productCode 区分ID
  * @param dialogParams (任意指定）  dialogId, endFunc,categoryId, params
  * @return
  */
 function openCategoryDialog(dialogId,endFunc,categoryId,formType, params) {
 	var openParams = null;
 	if (params) {
 		openParams = params;
 	} else {
 		openParams = CategoryParams;
 	}
 	var data = new Object();
 	data["categoryId"]=categoryId;
 	data["formType"]=formType;

 	_openDialog(dialogId, endFunc, openParams, data);
 }
