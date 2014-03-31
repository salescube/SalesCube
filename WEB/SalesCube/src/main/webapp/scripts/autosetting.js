/*******************************************************************************
 * フォームのサブミットおよびリダイレクトのパラメータに対してドメインIDを付与する
 ******************************************************************************/
$( function() {
	$("form").each(
			function() {
				var hidden = $(document.createElement("input"));
				hidden.attr("type", "hidden");
				hidden.attr("name", "domainId");
				hidden.val(domainId);
				$(this).append(hidden);
			}
	);

	//サブミットのときにドメインIDをくっつける（懸案273（#667））
	$("form").submit(function() {
			var action = $(this).attr("action");
			if(action) {
				if(action.indexOf("?") == -1) {
					action = action + "?domainId=" + domainId;
				}
				else {
					action = action + "&domainId=" + domainId;
				}
				$(this).attr("action",action);
			}
	});

	$("form").bind('keypress', 'return',
		function(evt) {
			var tagName = evt.target.tagName.toUpperCase();
			if(tagName!="TEXTAREA" && tagName!="INPUT" && tagName!="BUTTON") {
				evt.preventDefault();
				return false;
			}
		}
	);
});

/**
 * location.hrefの前段階として呼び出すとパラメータとしてドメインIDを付与する
 */
window.location.doHref = function(url) {
	if(url) {
		if(url.indexOf("?") == -1) {
			url = url + "?domainId=" + domainId;
		}
		else {
			url = url + "&domainId=" + domainId;
		}

		window.location.href = url;
	}
}

/*******************************************************************************
 * カレンダー入力設定 INPUTタグに設定するクラス名：date_input
 ******************************************************************************/
$( function() {
	var target = $('input.date_input');
	if (target.size() > 0) {
		target.datepicker(datePickcerSetting);
	}
});

/*******************************************************************************
 * 数値入力フィールドのカンマ付与設定 INPUTタグに設定するクラス名；numeral_commas
 ******************************************************************************/
$( function() {
	var target = $(".numeral_commas");
	if (target.size() > 0) {
		target.focus( function() { _rmv_commas(this); });

		target.blur( function() { _set_commas(this); });

		_after_load(target);
	}
});

//後から追加した要素にバインドするとき
function rebindNC(){
	var target = $(".numeral_commas");
	if (target.size() > 0) {
		target.focus( function() { _rmv_commas(this); });
		target.blur( function() { _set_commas(this); });
	}
}

/*******************************************************************************
 * エンターキーでの入力欄移動設定
 ******************************************************************************/

$( function() {
	var target = $(":input:visible[tabindex>0][type!='hidden'][type!='file'][type!='reset'][type!='submit'][type!='button'][type!='image'][type!='textarea']");
	if (target.size() > 0) {
		target.bind('keypress', 'return', move_focus_to_next_tabindex );
	}
});

/*******************************************************************************
 * 商品分類のプルダウン連携処理
 *
 * SELECTタグに設定するクラス名 商品分類（大）： ProductClass1 商品分類（中）： ProductClass2 商品分類（小）：
 * ProductClass3
 ******************************************************************************/
var _productClass1 = null;
var _productClass2 = null;
var _productClass3 = null;

/**
 * 後処理
 * @return
 */
function afterChangeProductClass(){
}

$( function() {
	_productClass1 = $("select[class^='ProductClass1']");
	_productClass2 = $("select[class^='ProductClass2']");
	_productClass3 = $("select[class^='ProductClass3']");
	if (!_productClass1 || !_productClass2 || !_productClass3) {
		return;
	}

	_productClass1
			.change(
			function(e) {
				var data = {
					"classCode1" : _productClass1.val(),
					"classCode3" : "" // 分類（小）のコードには空文字を指定する
				}

				asyncRequest(contextRoot
					+ "/ajax/productClassAjax/search", data,
						function(data) {
							// 成功用関数
						var options = eval(data);

						// （中）と（小）を空にする
						_productClass2.empty();

						// （中）に要素をセットする
						if (_productClass2.attr("class").match(/.+_TopEmpty$/)) {
							opt = $(document.createElement("option"));
							opt.attr("value", "");
							$(opt).append(document.createTextNode(""));
							_productClass2.append(opt);
						}

						for ( var i = 0; i < options.length; i++) {
							if (!options[i].classCode2 || options[i].classCode2.length == 0) {
								continue;
							}

							opt = $(document.createElement("option"));
							opt.attr("value", options[i].classCode2);
							$(opt)
									.append(
											document
													.createTextNode(options[i].className));
							_productClass2.append(opt);
						}

						_productClass2.change();
					}
				);
			}
	);

	_productClass2
			.change(
			function(e) {
				var data = {
					"classCode1" : _productClass1.val(),
					"classCode2" : _productClass2.val()
				}

				asyncRequest(contextRoot
						+ "/ajax/productClassAjax/search", data,
						function(data) {
							// 成功用関数
							var options = eval(data);

							// （小）を空にする
							_productClass3.empty();

							// （小）に要素をセットする
							if (_productClass3.attr("class").match(/.+_TopEmpty$/)) {
								opt = $(document.createElement("option"));
								opt.attr("value", "");
								$(opt).append(document.createTextNode(""));
								_productClass3.append(opt);
							}

							// （小）に要素をセットする
							for ( var i = 0; i < options.length; i++) {
								if (!options[i].classCode3 || options[i].classCode3.length == 0) {
									continue;
								}

								opt = $(document.createElement("option"));
								opt.attr("value", options[i].classCode3);
								$(opt)
										.append(
												document
														.createTextNode(options[i].className));
								_productClass3.append(opt);
							}

							if(afterChangeProductClass) {
								// 後処理関数が必要なら実装する
								afterChangeProductClass();
							}
						}
				);
			}
	);
});

/*******************************************************************************
 * 買掛系の金額自動計算
 *
 * SELECTタグに設定するクラス名
 * 数量 ： 	 AutoCalcQuantity
 * レート：  AutoCalcRate
 * 円単価 ： AutoCalcUnitPrice
 * 外貨単価：AutoCalcDolUnitPrice
 * 金額(円)：AutoCalcPrice
 * 外貨金額：AutoCalcDolPrice
 * 消費税率: AutoCalcRate
 * 明細選択チェックボックス: AutoCalcCheckPayLine  (明細選択用のチェックボックスがある場合、チェックボックスにこのクラス名を設定してください。)

	必要な画面に以下のように転記する
	$( function() {
		manager = new SlipPriceManager();
		manager.init();
	});

 * 端数計算 taxShiftCategory taxFractCategory priceFractCategory
 ******************************************************************************/
var LinePriceInfo = function() {
}

var taxShiftCategorySlipTotal = "";
var taxShiftCategoryCloseTheBooks = "";
var setDeliveryProcessCategory = null;
var setUnPaidQuantity = null;
var changeCategory = null;
var curValunitPrice = null;
var curValdolUnitPrice = null;
var curValQuantity = null;

/**
 * 消費税計算を利用する各画面で以下を追記してください
 * var taxShiftCategorySlipTotal = "<%=CategoryTrns.TAX_SHIFT_CATEGORY_SLIP_TOTAL%>";
 * var taxShiftCategoryCloseTheBooks = "<%=CategoryTrns.TAX_SHIFT_CATEGORY_CLOSE_THE_BOOKS%>";
 */

var SlipPriceManager = function() {
	// 税転嫁区分
	this.taxShiftCategory = null;
	// 税端数処理区分
	this.taxFractCategory = null;
	// 単価端数処理区分
	this.priceFractCategory = null;
	// 単価小数桁数
	this.unitPriceDecAlignment = null;
	// 数量小数桁数
	this.numDecAlignment = null;
	// 買掛残高
	this.aptBalance = null;
	// レート
	this.rate = null;
	// レートID（国内外判定用）
	this.rateId = null;
	// 支払伝票番号
	this.paymentSlipId = null;
	this.initCalc = false;

	this.quantityDict = new Object();
	this.unitPriceDict = new Object();
	this.dolUnitPriceDict = new Object();

	// 金額
	this.PriceInfo = {
		"priceTotal" : "",
		"fePriceTotal" : "",
		"paymentBalance" : "",
		"aptBalance" : "",
		"ctaxTotal" : "",
		"nonTaxPriceTotal" : ""
	};

	this.loadSlip = function() {
		this.taxShiftCategory = $("#taxShiftCategory").val();
		this.taxFractCategory = $("#taxFractCategory").val();
		this.priceFractCategory = $("#priceFractCategory").val();
		this.unitPriceDecAlignment = $("#unitPriceDecAlignment").val();
		this.numDecAlignment = $("#numDecAlignment").val();
		this.aptBalance = $("#aptBalance").val();
		this.rate = $("#rate").val();
		this.rateId = $("#rateId").val();
		this.paymentSlipId = $("#paymentSlipId").val();
		// 伝票の税率（仕入伝票にある。支払伝票にはない）
		this.ctaxRateSlip = $("#ctaxRate").val();
		this.ctaxTotal = $("#ctaxTotal").val();

		// 初期計算の制御
		this.initCalc = $("#initCalc").val() == "true";
		$("#initCalc").val("false");
	}

	/**
	 * 初期設定を行う
	 */
	this.init = function() {
		// 基本情報の読み取り
		this.loadSlip();

		// 行の読み取り
		this.loadLine(window.document);

		// 買掛残高
		if (this.aptBalance) {
			var b = new BigDecimal(_getNumStr(this.aptBalance));
			b = this.setYenScale(b);
			this.PriceInfo.aptBalance = b.toString();
		} else {
			this.PriceInfo.aptBalance = "0";
		}

		// 金額計算
		this.calcPrice();

		// 金額表示
		updatePrice();
	};

	this.loadLine = function( obj ) {
		// 各明細行の入力フィールドを取得
		var temp = $(obj).find("input,select");
		var quantity = temp.filter(".AutoCalcQuantity");
		var rate = temp.filter(".AutoCalcRate");
		var productFractCategory = temp.filter(".AutoCalcProductFractCategory");
		var unitPrice = temp.filter(".AutoCalcUnitPrice");
		var dolUnitPrice = temp.filter(".AutoCalcDolUnitPrice");
		var price = temp.filter(".AutoCalcPrice");
		var dolPrice = temp.filter(".AutoCalcDolPrice");
		var ctaxRate = temp.filter(".AutoCalcCtaxRate");
		var ctaxPrice = temp.filter(".AutoCalcCtaxPrice");
		var deliveryProcessCategory = temp.filter(".AutoCalcCategory");	// 完納区分
		var checkPayLine = temp.filter(".AutoCalcCheckPayLine");	// 支払画面の明細選択用のチェックボックス
		if (quantity.size() == 0 || price.size() == 0 || dolPrice.size() == 0
				|| unitPrice.size() == 0 || dolUnitPrice.size() == 0) {
			return;
		}

		// 再表示された際に、複数回バインドされてしまうことを防ぐ
		deliveryProcessCategory.unbind('change');
		quantity.unbind('change');
//		unitPrice.unbind('change');

		unitPrice.unbind('focus');
		unitPrice.unbind('blur');

//		dolUnitPrice.unbind('change');
		dolUnitPrice.unbind('focus');
		dolUnitPrice.unbind('blur');

		price.unbind('change');
		dolPrice.unbind('change');
		for ( var i = 0; i < quantity.size(); i++) {
			var info = new LinePriceInfo();
			info.quantity = quantity[i];
			info.rate = rate[i];
			if (productFractCategory[i]) {
				info.productFractCategory = productFractCategory[i].value;
			}
			info.unitPrice = unitPrice[i];
			info.dolUnitPrice = dolUnitPrice[i];
			info.price = price[i];
			info.dolPrice = dolPrice[i];
			info.deliveryProcessCategory = deliveryProcessCategory[i];
			info.checkPayLine = checkPayLine[i];
			if(ctaxRate.size() > 0) {
				info.ctaxRate = ctaxRate[i];
				info.ctaxPrice = ctaxPrice[i];
			}

			$(info.quantity).focus(focusQuantity);
//			$(info.quantity).change(
			$(info.quantity).blur(
					function(){
						changeQuantity(this);
						if( setDeliveryProcessCategory ){
							// 完納区分のセット
							setDeliveryProcessCategory(this);
						}
						if( setUnPaidQuantity ){
							// 未納数の計算
							setUnPaidQuantity(this);
						}
					}
			);

			$(info.deliveryProcessCategory).change(
					function(){
						if( changeCategory ){
							// 完納区分変更
							changeCategory(this);
						}
					}
			);

//			$(info.unitPrice).change(changeUnitPrice);
			$(info.unitPrice).focus(focusUnitPrice);
			$(info.unitPrice).blur(changeUnitPrice);

//			$(info.dolUnitPrice).change(changeDolUnitPrice);
			$(info.dolUnitPrice).focus(focusdolUnitPrice);
			$(info.dolUnitPrice).blur(changeDolUnitPrice);

			$(info.price).change(changePrice);
			$(info.dolPrice).change(changeDolPrice);

			this.quantityDict[info.quantity.id] = info;
			this.unitPriceDict[info.unitPrice.id] = info;
			this.dolUnitPriceDict[info.dolUnitPrice.id] = info;

			if(this.initCalc) {
				// 金額合計、外貨金額合計を再計算
				changeQuantity(quantity[i]);
			}

//			if( setDeliveryProcessCategory ){
//				// 完納区分のセット
//				setDeliveryProcessCategory(info.quantity, "0");
//			}

			if( setUnPaidQuantity ){
				// 未納数の計算
				setUnPaidQuantity(info.quantity);
			}
		}
	}

	/**
	 * 金額合計を計算する
	 */
	this.calcPrice = function() {
		
		// 本体金額合計と外貨伝票合計金額を計算する
		var priceTotal = new BigDecimal("0");
		var ctaxTotal = new BigDecimal("0");
		var nonTaxPriceTotal =  new BigDecimal("0");
		var fePriceTotal = new BigDecimal("0");
		for ( var key in this.unitPriceDict) {
			// 支払伝票入力から呼び出されている場合は、チェックされた明細しか伝票合計金額に計上しない
			if( this.unitPriceDict[key].checkPayLine != null && !this.unitPriceDict[key].checkPayLine.checked ) {
				continue;
			}

			if (_isNum(this.unitPriceDict[key].price.value)) {
				var p = new BigDecimal(
					_getNumStr(this.unitPriceDict[key].price.value));

				// 消費税計算
				// 有効なレートを持たず、外税伝票計か外税締単位の場合のみ本体合計金額に明細の税額をプラスする
				if((this.rateId == null  || this.rateId == "") &&
						taxShiftCategorySlipTotal != "" && taxShiftCategoryCloseTheBooks != "" &&
						((this.taxShiftCategory == taxShiftCategorySlipTotal) ||
						(this.taxShiftCategory == taxShiftCategoryCloseTheBooks) )){

					// この明細の消費税額
					var cTaxPrice = new BigDecimal(_getNumStr(this.unitPriceDict[key].ctaxPrice.value));
					nonTaxPriceTotal =  nonTaxPriceTotal.add(p);
					priceTotal = priceTotal.add(p).add(cTaxPrice);
//					priceTotal = priceTotal.add(cTaxPrice);
					ctaxTotal = ctaxTotal.add(cTaxPrice);
				} else {
					priceTotal = priceTotal.add(p);	// 消費税適用対象外の場合、そのままプラスする
					nonTaxPriceTotal = priceTotal;
				}
			}
			if (_isNum(this.unitPriceDict[key].dolPrice.value)) {
				p = new BigDecimal(
					_getNumStr(this.unitPriceDict[key].dolPrice.value));
				fePriceTotal = fePriceTotal.add(p);
			}
		}


		// 本体金額合計
		nonTaxPriceTotal = this.setYenScale(nonTaxPriceTotal);
		this.PriceInfo.nonTaxPriceTotal = nonTaxPriceTotal.toString();

		// 消費税合計
		// 有効なレートを持たず、外税伝票計か外税締単位の場合のみ計算を行う
		if((this.rateId == null  || this.rateId == "") &&
			taxShiftCategorySlipTotal != "" && taxShiftCategoryCloseTheBooks != ""
			&& ((this.taxShiftCategory == taxShiftCategorySlipTotal)
					||(this.taxShiftCategory == taxShiftCategoryCloseTheBooks) )){

				if(MAIN_FORM_NAME == "purchase_inputPurchaseActionForm"){
					// 仕入の場合、税抜合計に新しい消費税率を掛ける
					var tax = new BigDecimal(_getNumStr(this.ctaxRateSlip));
					ctaxTotal = nonTaxPriceTotal.multiply(tax).divide(new BigDecimal("100.0"));
					priceTotal = nonTaxPriceTotal.add(ctaxTotal);
				} else {
					// 支払の場合、各行の税額の和（行ごとに税率が異なる可能性がある）
					this.PriceInfo.ctaxTotal = ctaxTotal.toString();
				}
				ctaxTotal = this.setYenScale(ctaxTotal);
				this.PriceInfo.ctaxTotal = ctaxTotal.toString();
		}else{
			this.PriceInfo.ctaxTotal = ctaxTotal.toString();
		}

		// 伝票合計
		priceTotal = this.setYenScale(priceTotal);
		this.PriceInfo.priceTotal = priceTotal.toString();

		// 外貨伝票合計
		if(this.rateId == null  || this.rateId == "") {
			this.PriceInfo.fePriceTotal = fePriceTotal.toString();
		}else{
			fePriceTotal = this.setDolScale(fePriceTotal);
			this.PriceInfo.fePriceTotal = fePriceTotal.toString();
		}
		
		// 本体金額合計と買掛残高から支払残高を計算する
		if (this.aptBalance) {
			var b = new BigDecimal(_getNumStr(this.aptBalance));
			// 支払伝票登録前のみ買掛残高から伝票合計を引く
			if(this.paymentSlipId==""){
				b = b.subtract(priceTotal);
			}
			b = this.setYenScale(b);
			this.PriceInfo.paymentBalance = b.toString();
		} else {
			this.PriceInfo.paymentBalance = "0";
		}
	};

	/**
	 * DecimalFormatクラスに外貨単価端数処理設定を行う
	 */
	this.setDolScale = function(dec) {
		if (dec) {
			dec = dec.setScale(new Number(this.unitPriceDecAlignment),
					getRoundingMode(this.priceFractCategory));
		}
		return dec;
	};

	/**
	 * DecimalFormatクラスに円単価端数処理設定を行う
	 */
	this.setYenScale = function(dec) {
		if (dec) {
			dec = dec.setScale(0, getRoundingMode(this.priceFractCategory));
		}
		return dec;
	};

	/**
	 * サーバ送信のためにカンマと記号を除去する
	 */
	this.prepareSubmit = function() {
		for ( var key in this.unitPriceDict) {
			var info = this.unitPriceDict[key];
			_before_submit(info.quantity);
			_before_submit(info.rate);
			_before_submit(info.unitPrice);
			_before_submit(info.dolUnitPrice);
			_before_submit(info.price);
			_before_submit(info.dolPrice);
		}

		_before_submit($("#ctaxTotal"));
		_before_submit($("#nonTaxPriceTotal"));
		_before_submit($("#priceTotal"));
		_before_submit($("#fePriceTotal"));
		_before_submit($("#paymentBalance"));
		_before_submit($("#aptBalance"));
	}

}
var manager = null;

/**
 * 伝票金額情報の表示更新処理
 */
function updatePrice() {
	// 伝票合計（円）
	var priceTotal = $("#priceTotal");
	if (priceTotal.size() > 0) {
		priceTotal.val(_att_yen_unit(manager.PriceInfo.priceTotal));
	}

	// 伝票合計外貨金額
	var fePriceTotal = $("#fePriceTotal");
	if (fePriceTotal.size() > 0) {
		fePriceTotal.val(_att_dollar_unit(manager.PriceInfo.fePriceTotal));
	}

	// 支払残高
	var paymentBalance = $("#paymentBalance");
	if (paymentBalance.size() > 0) {
		paymentBalance.val(_att_yen_unit(manager.PriceInfo.paymentBalance));
	}

	// 買掛残高
	var aptBalance = $("#aptBalance");
	if (aptBalance.size() > 0) {
		aptBalance.val(_att_yen_unit(manager.PriceInfo.aptBalance));
	}

	// 本体金額合計
	var nonTaxPriceTotal = $("#nonTaxPriceTotal");
	if (nonTaxPriceTotal.size() > 0) {
		nonTaxPriceTotal.val(_att_yen_unit(manager.PriceInfo.nonTaxPriceTotal));
	}

	// 消費税合計
	var ctaxTotal = $("#ctaxTotal");
	if (ctaxTotal.size() > 0) {
		ctaxTotal.val(_att_yen_unit(manager.PriceInfo.ctaxTotal));
	}
}

/**
 * 数量変更イベント
 *
 * @return
 */
function changeQuantity(info) {
	var linePriceInfo = null;
	if (info instanceof LinePriceInfo) {
		linePriceInfo = info;
	} else {
		linePriceInfo = manager.quantityDict[info.id];
	}

	if (!linePriceInfo) {
		return;
	}

	var q = linePriceInfo.quantity.value;
	if (!_isNum(q)) {
		return;
	}

	if(q == curValQuantity){
		return;
	}

	q = new BigDecimal(_getNumStr(q));
	if (linePriceInfo.productFractCategory) {
		// 商品端数処理が設定されていれば設定値で処理する
		q = q.setScale(new Number(manager.numDecAlignment),
				getRoundingMode(linePriceInfo.productFractCategory));
	} else {
		// 無ければ切り捨て
		q = q.setScale(new Number(manager.numDecAlignment),
				BigDecimal.prototype.ROUND_DOWN);
	}
	linePriceInfo.quantity.value = q.toString();

	changeDolUnitPrice(linePriceInfo);
	changeUnitPrice(linePriceInfo);

	_set_commas(linePriceInfo.quantity);
}
 /**
  * 数量変更イベント
  *
  * @return
  */
function focusQuantity(info) {
 	var linePriceInfo = null;
 	if (info instanceof LinePriceInfo) {
 		linePriceInfo = info;
 	} else {
 		linePriceInfo = manager.quantityDict[info.id];
 	}

 	if (!linePriceInfo) {
 		return;
 	}

 	var q = linePriceInfo.quantity.value;
 	if (!_isNum(q)) {
 		return;
 	}

 	curValQuantity = q;
}
 /**
  * 外貨単価　フォーカスイベント
  * @return
  */
  function focusdolUnitPrice(info) {
 	var linePriceInfo = null;
 	if (info instanceof LinePriceInfo) {
 		linePriceInfo = info;
 	} else {
 		linePriceInfo = manager.dolUnitPriceDict[this.id];
 	}

 	if (!linePriceInfo) {
 		return;
 	}

 	var up = dec_numeral_commas(linePriceInfo.dolUnitPrice.value);
 	if (!_isNum(up)) {
 		return;
 	}
 	curValdolUnitPrice = up;
 }

/**
 * 外貨単価変更イベント
 *
 * @return
 */
function changeDolUnitPrice(info) {
	var linePriceInfo = null;
	if (info instanceof LinePriceInfo) {
		linePriceInfo = info;
	} else {
		linePriceInfo = manager.dolUnitPriceDict[this.id];
	}

	if (!linePriceInfo) {
		return;
	}

	var dup = linePriceInfo.dolUnitPrice.value;
	if (!_isNum(dup)) {
		return;
	}

	// 単価端数処理
	dup = new BigDecimal(_getNumStr(dup));
	dup = manager.setDolScale(dup);
	linePriceInfo.dolUnitPrice.value = dup.toString();
	_set_commas(linePriceInfo.dolUnitPrice);

	// フォーカスイン時と内容が変わらない場合は、処理しない
	if(dup == curValdolUnitPrice){
		return;
	}


	// 数量を掛けて外貨金額を算出
	var q = linePriceInfo.quantity.value;
	if (_isNum(q)) {
		q = new BigDecimal(_getNumStr(q));

		var dp = dup.multiply(q);
		dp = manager.setDolScale(dp);
		linePriceInfo.dolPrice.value = dp.toString();
		_set_commas(linePriceInfo.dolPrice);
	}

	if (!(info instanceof LinePriceInfo)) {
		// 外貨単価のテキストフィールドによるイベントから起動され、
		// 前回レートが数値なら円単価を更新
		var r = linePriceInfo.rate.value;
		if (_isNum(r)) {
			var r = new BigDecimal(r);

			// 変更前の単価を保持
			var up_bef = dec_numeral_commas(linePriceInfo.unitPrice.value);
			if (!_isNum(up_bef)) {
				return;
			}
			curValunitPrice = up_bef;

			// ドル単価にレートを掛けて円単価を計算
			var up = dup.multiply(r);
			// 端数処理
			up = manager.setYenScale(up);
			linePriceInfo.unitPrice.value = up.toString();

			changeUnitPrice(linePriceInfo);
			return;
		}
	}

	manager.calcPrice();
	updatePrice();
}

/**
 * 単価変更イベント
 * @return
 */
function changeUnitPrice(info) {
	var linePriceInfo = null;
	if (info instanceof LinePriceInfo) {
		linePriceInfo = info;
	} else {
		linePriceInfo = manager.unitPriceDict[this.id];
	}

	if (!linePriceInfo) {
		return;
	}

	var up = linePriceInfo.unitPrice.value;
	if (!_isNum(up)) {
		return;
	}

	// フォーカスイン時と内容が変わらない場合は、処理しない
	if(up == curValunitPrice){
		return;
	}

	// 円単価を更新
	up = new BigDecimal(_getNumStr(up));
	up = manager.setYenScale(up);
	linePriceInfo.unitPrice.value = up.toString();
	_set_commas(linePriceInfo.unitPrice);

	// 円金額を更新
	var q = linePriceInfo.quantity.value;
	if(_isNum(q)) {
		q = new BigDecimal(_getNumStr(q));

		var p = up.multiply(q);

		// 消費税計算
		if(linePriceInfo.ctaxRate) {
			if((manager.rateId == null || manager.rateId == "") &&
				taxShiftCategorySlipTotal != "" && taxShiftCategoryCloseTheBooks != ""
				&& ((manager.taxShiftCategory == taxShiftCategorySlipTotal)
						||(manager.taxShiftCategory == taxShiftCategoryCloseTheBooks) )){
				var taxRate = linePriceInfo.ctaxRate.value;
				if(_isNum(taxRate)) {
					taxRate = new BigDecimal(taxRate);
					var ctaxp = p.multiply(taxRate).divide(new BigDecimal("100.0"));
					ctaxp = manager.setYenScale(ctaxp);
					linePriceInfo.ctaxPrice.value = ctaxp.toString();
				}
			}
			else {
				linePriceInfo.ctaxPrice.value = "0";
			}
		}

		p = manager.setYenScale(p);
		linePriceInfo.price.value = p.toString();
		_set_commas(linePriceInfo.price);
	}

	manager.calcPrice();
	updatePrice();
}

/**
 * 仕入入力画面の税率変更イベント
 * @return
 */
function changeTaxRate(taxrateValue){
	// 消費税計算
	manager.ctaxRateSlip = taxrateValue;
	
	for ( var key in manager.unitPriceDict) {
		var linePriceInfo = manager.unitPriceDict[key];

		if((manager.rateId == null || manager.rateId == "") &&
			taxShiftCategorySlipTotal != "" && taxShiftCategoryCloseTheBooks != ""
			&& ((manager.taxShiftCategory == taxShiftCategorySlipTotal)
					||(manager.taxShiftCategory == taxShiftCategoryCloseTheBooks) )
			&& _isNum(taxrateValue)){

			var taxRate = new BigDecimal(taxrateValue);
			var p = new BigDecimal(_getNumStr(linePriceInfo.price.value));
			
			var ctaxp = p.multiply(taxRate).divide(new BigDecimal("100.0"));
			ctaxp = manager.setYenScale(ctaxp);

			linePriceInfo.ctaxRate.value = taxrateValue;
			linePriceInfo.ctaxPrice.value = ctaxp.toString();
		} else {
			linePriceInfo.ctaxRate.value = "0";
			linePriceInfo.ctaxPrice.value = "0";
		}
	}

 	manager.calcPrice();
 	updatePrice();

}



 /**
 * 単価フォーカスイベント
 * @return
 */
 function focusUnitPrice(info) {
		var linePriceInfo = null;
		if (info instanceof LinePriceInfo) {
			linePriceInfo = info;
		} else {
			linePriceInfo = manager.unitPriceDict[this.id];
		}

		if (!linePriceInfo) {
			return;
		}

		var up = dec_numeral_commas(linePriceInfo.unitPrice.value);
		if (!_isNum(up)) {
			return;
		}
		curValunitPrice = up;

}

 /**
  * 金額（円）変更イベント
  * @return
  */
 function changePrice() {
	// 円金額を更新
	var price = this.value;

	if(_isNum(price)) {
		price = new BigDecimal(_getNumStr(price));
		price = manager.setYenScale(price);

		this.value = price.toString();
		_set_commas($(this.value));
	}

	manager.calcPrice();
 	updatePrice();
 }
  /**
   * 外貨金額変更イベント
   * @return
   */
  function changeDolPrice() {
	// 外貨金額を更新
	var dolPrice = this.value;
	if(_isNum(dolPrice)) {
		dolPrice = new BigDecimal(_getNumStr(dolPrice));
		dolPrice = manager.setDolScale(dolPrice);

		this.value = dolPrice.toString();
		_set_commas($(this.value));
	}

  	manager.calcPrice();
  	updatePrice();
  }

 function changeRate(rateValue){
	 var temp = $("input");
	 var rate = temp.filter(".AutoCalcRate");
	 for ( var i = 0; i < rate.size(); i++) {
		 rate[i].value = rateValue;
		 // 円単価の再計算
		 this.id="supplierLineTrnDtoList["+i+"].dolUnitPrice";
		 this.changeDolUnitPrice();
	 }
 }
 
 
