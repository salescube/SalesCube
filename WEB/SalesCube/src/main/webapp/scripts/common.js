
var datePickcerSetting = {
		dateFormat : 'yy/mm/dd',
		showOn: 'button',
		buttonImage: contextRoot + '/images/customize/calendar.png',
		buttonImageOnly: true,
		buttonText: 'カレンダー',
		speed: 'fast'
};

var async_request_off = false;

/******************
 * 検索中表示用
 */
function createNowSearchingDiv(){
	$("body").append("<div id=\"NowSearchingDiv\"></div>");
}

function showNowSearchingDiv(){
	if($("#NowSearchingDiv").size()==0){
		createNowSearchingDiv();
	}
	if($("#NowSearchingDiv").size()>0){
		$("#NowSearchingDiv").bgiframe();
		NowSearchingDivOnScroll();
		NowSearchingDivOnResize();
		$("#NowSearchingDiv").css("display", "inline-block");
	}
}

$(window).scroll(NowSearchingDivOnScroll);
function NowSearchingDivOnScroll(){
	if($("#NowSearchingDiv").size()>0){
		var y1 = document.documentElement.scrollTop;
		var y2 = document.body.scrollTop;
		(y1 > y2) ? scrollY = y1 : scrollY = y2;
		$("#NowSearchingDiv").css("top", scrollY + "px");
		var x1 = document.documentElement.scrollLeft;
		var x2 = document.body.scrollLeft;
		(x1 > x2) ? scrollX = x1 : scrollX = x2;
		$("#NowSearchingDiv").css("left", scrollX + "px");
	}
}

$(window).resize(NowSearchingDivOnResize);
function NowSearchingDivOnResize(){
	if($("#NowSearchingDiv").size()>0){
		$("#NowSearchingDiv").width($(window).width());
		$("#NowSearchingDiv").height($(window).height());
	}
}

function hideNowSearchingDiv(){
	if($("#NowSearchingDiv").size()>0){
		$("#NowSearchingSpan").css("cursor", "auto");
		$("#NowSearchingDiv").css("display", "none");
		$("#NowSearchingDiv").remove();
	}
}

/**
* エンターキーでの入力欄移動
* 拒否属性：disabled,readonly,display=none
**/
function move_focus_to_next_tabindex(evt){
	if($(evt.target).filter("[readonly!='true'][disabled!='true'][class!='c_disable']").size() == 0) {
		// 無効な項目をフォーカス中の場合
		evt.preventDefault();
		return false;
	}

	var elements = $(":input:visible[tabindex>0]" +
			"[type!='hidden'][type!='file'][type!='reset'][type!='submit'][type!='button'][type!='image']" +
			"[readonly!='true'][disabled!='true'][class!='c_disable']");

	// tabindex > 0 のもののみ残す それ以外は削除
	for(var i = elements.length - 1; 0 <= i; i--) {
		var tabindex = elements[i].getAttribute("tabindex");
		if(tabindex > 0) {
			continue;
		}
		// i を削除
		elements.splice(i, 1);
	}

	elements.sort(
		function(a, b) {
			return parseInt(a.getAttribute("tabindex")) - parseInt(b.getAttribute("tabindex"));
		}
	);

	var index = elements.index(evt.target);
	if(index != -1 && elements.get( index + 1 ) ) {
		try{
		elements.get( index + 1 ).focus();
		}catch(e){}
	}
	else {
		evt.target.blur();
	}

	//ここでよばないと最後のオブジェクトでエンターキーが有効になってしまう。
	evt.preventDefault();
	return false;
}

/**
 * Ajax処理用の基本エラー処理を行う
 *
 * @param xmlHttpRequest
 * @param textStatus
 * @param errorThrown
 * @return
 */
function _defaultAjaxErrorHandler(xmlHttpRequest, textStatus, errorThrown){
	if (xmlHttpRequest.status == 450) {
		// Ajaxエラー
		if($("#ajax_errors").size() > 0) {
			$("#ajax_errors").append(xmlHttpRequest.responseText);
		}
	} else if (xmlHttpRequest.status == 401) {
		// 未ログインエラー
		alert(xmlHttpRequest.responseText);
		window.location.href = contextRoot + "/login/" + domainId;
	} else {
		// その他のエラー
		alert(xmlHttpRequest.responseText);
	}
}

/**
* Ajaxによる非同期POSTリクエストを行う
*
* @param url 呼び出しURL
* @param data POSTパラメータ
* @param callback リクエスト成功時のコールバック関数
* @param callbackerr エラー時のコールバック関数
* @return
*/
function asyncRequest(url, data, callback, callbackerr) {
	var errFunc = callbackerr;
	var beforeFunc = function(){showNowSearchingDiv();};
	var completeFunc = function(){hideNowSearchingDiv();};
	if(!errFunc) {
		errFunc = _defaultAjaxErrorHandler;

		beforeFunc = function() {
			showNowSearchingDiv();
			if($("#ajax_errors").size() > 0) {
				$("#ajax_errors").empty();
			}
		}
	}

	// asyncするかフラグを参照
	var async = true;
	if(async_request_off)		async = false;
	async_request_off = false;

	$.ajax( {
		"type" : "POST",
//		"async" : true,
		"async" : async,
		"url" : url,
		"data" : data,
		"dataType" : "text",
		"beforeSend" : beforeFunc,
		"complete" : completeFunc,
		"error" : errFunc,
		"success" : callback
	});
}
/**
 * 同期Ver
 * @param url
 * @param data
 * @param callback
 * @param callbackerr
 * @return
 */
function syncRequest(url, data, callback, callbackerr) {
	var errFunc = callbackerr;
	var beforeFunc = function(){showNowSearchingDiv();};
	var completeFunc = function(){hideNowSearchingDiv();};
	if(!errFunc) {
		errFunc = _defaultAjaxErrorHandler;

		beforeFunc = function() {
			showNowSearchingDiv();
			if($("#ajax_errors").size() > 0) {
				$("#ajax_errors").empty();
			}
		}
	}

	$.ajax( {
		"type" : "POST",
		"async" : false,
		"url" : url,
		"data" : data,
		"dataType" : "text",
		"beforeSend" : beforeFunc,
		"complete" : completeFunc,
		"error" : errFunc,
		"success" : callback
	});
}

/**
 * 強制除去
 */
$(function(){ $("body").ajaxStop(function(){ hideNowSearchingDiv(); }); });

/**
 * 擬似同期
 */
var pseudoSyncAjaxFunctionList = new Array();
var pseudoSyncAjaxFunctionRunning = false;
function pseudoSyncRequest(){
	if( (pseudoSyncAjaxFunctionList!=null) && (pseudoSyncAjaxFunctionList.length!=0) ){
		showNowSearchingDiv();
		var tempfunc = pseudoSyncAjaxFunctionList.shift();
		if(typeof(tempfunc) == 'function'){
			tempfunc();
			if(!pseudoSyncAjaxFunctionRunning){
				pseudoSyncRequest();
			}
		}else{pseudoSyncRequest();}
	}else{
		hideNowSearchingDiv();
	}
}

function pseudoSyncRequestElement(url, data, callback, callbackerr) {
	pseudoSyncAjaxFunctionRunning = true;
	var errFunc = callbackerr;
	var beforeFunc = null;
	var completeFunc = function(){
		pseudoSyncAjaxFunctionRunning = false;
		pseudoSyncRequest();
	};
	if(!errFunc) {
		errFunc = _defaultAjaxErrorHandler;
		beforeFunc = function() {
			showNowSearchingDiv();
			if($("#ajax_errors").size() > 0) {
				$("#ajax_errors").empty();
			}
		}
	}
	$.ajax( {
		"type" : "POST",
		"async" : true,
		"url" : url,
		"data" : data,
		"dataType" : "text",
		"beforeSend" : beforeFunc,
		"complete" : completeFunc,
		"error" : errFunc,
		"success" : callback
	});
}

/**
 * 端数処理区分からDecimalFormatの丸めモードを返す
 * @return
 */
function getRoundingMode(fractCategory) {
 	if (fractCategory == "0") {
 		// 切り捨て
 		return BigDecimal.prototype.ROUND_DOWN;
 	} else if (fractCategory == "1") {
 		// 四捨五入
 		return BigDecimal.prototype.ROUND_HALF_UP;
 	} else if (fractCategory == "2") {
 		// 切り上げ
 		return BigDecimal.prototype.ROUND_UP;
 	}

 	return BigDecimal.prototype.ROUND_HALF_UP;

 }

/**
 * ひらがなを全角カタカナに変換する
 * @param source
 * @return
 */
function convertHKanaToKKana(source) {
	if(source == null || source.length == 0) {
		return source;
	}
	// 元の文字列をカタカナの文字コード配列に変換する
	var result = [];

	var c = null;
	for(var i = 0; i < source.length; i++) {
        c = source.charCodeAt(i);
        result[i] = (0x3041 <= c && c <= 0x3096) ? c + 0x0060 : c;
    };
    // 文字コード配列から文字列に変換
    return String.fromCharCode.apply(null, result);
 };


 /*******************************************************************************
  * 入力欄(valueプロパティ)や、innerHemlを持つタグで、端数処理、カンマ・通貨単位を付与する
  *
  * ●文字列に対して使う場合
  * １．oBDCS()			で、オブジェクトを作る。oBDCS(文字列)で初期化可能。
  * ２．oBDCS().value(処理したい文字列)		で処理対象をセットする。
  * ３．oBDCS().value()		で、カンマ・通貨単位の除去された文字列を得る。
  * ４．oBDCS().toBDCString()	で、付与後の文字列を得る。デフォルトではカンマのみ付与
  * ５．oBDCS().setComma(b_attComma).setScale(type,scale).setCunit(name).toBDCString()
  *				で、任意のものを付与した状態の文字列を得る。
  * ６．oBDCS().Number()		で、計算に使えるように標準のNumber関数を通す。
  *
  * ●オブジェクトに対して使う場合
  * １．ゲット・ロストフォーカスに関連付けるクラス名を適当に用意する。
  *						numeral_commasとか
  * ２．処理が必要なクラスをオブジェクトに付ける
  *						以下クラス名リストを随時編集
  ******************************************************************************/

 /*******************************************************************************
  * ■定数もどきなど
  ******************************************************************************/

 /*******************************************************************************
  * クラス名リスト
  * 通貨単位について	yen_value,dollar_value
  * 桁について、		scale_0,scale_1,scale_2,scale_3
  * 処理方法について、	scale_round_down,scale_half_up,scale_round_up
  *
  * 例)クラス名リストへの追加
  * CurrencyUnitClassNameHashList['新単位クラス名']="新単位";
  ******************************************************************************/
 //通貨単位(通貨単位文字列指定)
 var CurrencyUnitClassNameHashList = {
 	'yen_value'	: '￥',
 	'dollar_value'	: '＄'
 };
 //端数処理小数点以下桁(桁数数値指定)
 var ScaleClassNameHashList = {
 	'scale_0'	: 0,
 	'scale_1'	: 1,
 	'scale_2'	: 2,
 	'scale_3'	: 3
 };
 //端数処理方式(BigDecimal.js内数値指定)
 var TypeClassNameHashList = {
 	'scale_half_up'		: BigDecimal.prototype.ROUND_HALF_UP,
 	'scale_round_down'	: BigDecimal.prototype.ROUND_DOWN,
 	'scale_round_up'	: BigDecimal.prototype.ROUND_UP
 };

 /*******************************************************************************
  * 【逆引き用】DB値からクラス名を引く		配列インデックス⇒クラス名
  ******************************************************************************/
 var ScaleClassNameList = new Array(
 	"scale_0",
 	"scale_1",
 	"scale_2",
 	"scale_3"
 );
 var TypeClassNameList = new Array(
 	"scale_round_down",
 	"scale_half_up",
 	"scale_round_up"
 );
 // クラス名適用関数引数用
 var scale_0 = 0;
 var scale_1 = 1;
 var scale_2 = 2;
 var scale_3 = 3;
 var scale_round_down = 0;
 var scale_half_up = 1;
 var scale_round_up = 2;

 /*******************************************************************************
  * ■文字列処理
  ******************************************************************************/

 /*******************************************************************************
  * BigDecimalとCommaとその他諸々の文字列処理用クラス定義(コンストラクタ定義)
  * 関数
  * BDCStr(targetString)		コンストラクタ(処理文字列)
  * value()			処理文字列getter
  * value(targetString)		処理文字列setter(処理文字列)
  * Number()			処理文字列を標準Number関数に渡す
  * BDValue()			BigDecimal型の値を返す
  * isValid()			処理文字列の処理対象としての正当性
  * isNum()			処理文字列の数値としての正当性
  * setComma(b_attComma)		カンマ付与の有無設定(bool)
  * setScale(type,scale)		端数処理方式設定(方式，桁数)
  *					引数はクラス名文字列あるいは数値
  * setCunit(name)		通貨単位設定(通貨単位名)
  *					引数はクラス名文字列
  * toBDCString()		処理後文字列を返す
  ******************************************************************************/
 function BDCStr(){

 	//■プライベート

 	var self = this;

 	//処理に値する文字列かどうか
 	var m_isValid = false;

 	//処理対象文字列(カンマなど除去済み)
 	var m_value = "";

 	//カンマを付与するかどうか
 	var m_attComma = true;

 	//端数処理パラメータ
 	var m_scale = -1;
 	var m_type = -1;

 	//通貨単位
 	var m_cunit = "";

 	//通貨単位除去＋カンマ除去
 	function mf_getNumStr(l_target_string){
 		return mf_rmvComma(mf_rmvCunit(l_target_string));
 	}

 	//正当性チェック
 	function mf_isValid(l_target_string){
 		m_isValid = mf_isTargetAttComma(l_target_string);
 		return m_isValid;
 	}

 	//ヌルを空文字列に
 	function mf_nullToEmpty(l_target_string){
 		return ((l_target_string==null)?"":l_target_string);
 	}

 	//値セット
 	function mf_setValue(l_target_string){
 		var l_value = mf_getNumStr(mf_nullToEmpty(l_target_string));
 		mf_isValid(l_value);
 		m_value = l_value;
 		return m_value;
 	}

 	//カンマ付与対象チェック
 	function mf_isTargetAttComma(l_target_string){
 		//ヌルおよび空文字列は偽とする
 		if( (l_target_string == null) || (l_target_string == "") ){
 			return false;
 		}else{
 			//カンマ付与対象かどうか判定する。
 			var re4match = new RegExp("^([-]?(([1-9]\\d*)|(0))(\\.\\d+)?)$");
 			return (l_target_string.match(re4match)!=null);
 		}
 	}

 	//カンマ付与
 	function mf_attComma(l_target_string){
 		var l_output_string = l_target_string;
 		//付与対象チェック
 		if(mf_isTargetAttComma(l_output_string)){
 			//整数部小数部分解
 			var l_output_array = new Array();
 			l_output_array = l_output_string.match(/[^\.]+/g);
 			if ( !((l_output_array == null) || (l_output_array.length > 2)) ) {
 				//整数部にしか付与しないよ
 				l_output_string = l_output_array[0];
 				var re4replace = new RegExp("^([-]?\\d+)(\\d{3})");//チェック済みなので単純に
 				while(l_output_string != (l_output_string = l_output_string.replace(re4replace, "$1,$2")));
 				//再度小数部をくっつける
 				l_output_string = l_output_string + ((l_output_array.length==2)?("." + l_output_array[1]):"");
 			}
 		}
 		return l_output_string;
 	}

 	//カンマ除去
 	function mf_rmvComma(l_target_string){
 		var l_output_string = l_target_string;
 		//文字列の正当性確認
 		var re4match = new RegExp("^(\\s*[-\+]?\\s*(([1-9][0-9,]*)|(0))(\\.\\d+)?\\s*)$");
 		if (l_output_string.match(re4match) != null){
 			//空白の除去
 			l_output_string = l_output_string.replace(/\s/g, "");
 			//+の除去
 			l_output_string = l_output_string.replace(/\+/, "");
 			//カンマ除去
 			l_output_string = l_output_string.replace(/,/g, "");
 		}
 		return l_output_string;
 	}

 	//通貨単位除去
 	function mf_rmvCunit(l_target_string){
 		var l_output_string = l_target_string;
 		//リストにある通貨単位を除去
 		for (var key in CurrencyUnitClassNameHashList) {
 			//先頭の空白を除去
 			var l_temp_string = l_output_string.replace(/^\s*/, "");
 			if(l_temp_string.indexOf(CurrencyUnitClassNameHashList[key])==0){
 				//先頭の通貨単位とそのあとの空白を除去
 				l_output_string = l_temp_string.replace(CurrencyUnitClassNameHashList[key],"").replace(/^\s*/, "");
 			}
 		}
 		return l_output_string;
 	}

 	//■プリビレッジドメソッド

 	//値のセットおよび読出し
 	this.value = function(){
 		//セットだってさ
 		if (arguments.length == 1) {
 			mf_setValue(arguments[0]);
 			return self;
 		}
 		//読出しだそうですよ
 		else if(arguments.length == 0){
 			return m_value;
 		}
 		return;
 	}

 	//Number関数
 	this.Number = function(){
 		return Number(m_value);//NaNエラー値も返す点に注意
 	}

 	//BD型で値を返す
 	this.BDValue = function(){
 		if(m_isValid){
 			var l_BDValue = new BigDecimal(m_value);
 			if( !( (m_type < 0) || (m_scale < 0) ) ){
 				return l_BDValue.setScale(m_scale,m_type);
 			}else{
 				return l_BDValue;
 			}
 		}else{return;}
 	}


 	//処理に値する文字列かどうか
 	this.isValid = function(){return m_isValid;}//値のセット時に既に更新している

 	//数値かどうか
 	this.isNum = function(){return m_isValid;}//現時点(mf_isTargetAttCommaの仕様が変わらない限り)では !isNaN(m_value) とする必要はない

 	//カンマ付与
 	this.setComma = function(l_attComma){
 		m_attComma = ( (typeof l_attComma == "boolean")?l_attComma:true );
 		return self;
 	}

 	//処理タイプを設定する
 	this.setScale = function(l_type,l_scale){
 		m_scale = -1;
 		m_type = -1;
 		if(m_isValid){
 			//範囲内の整数じゃない？
 			if( isNaN( parseInt( l_type ) ) || isNaN( l_type ) ||
 			     ( 0 > l_type ) || ( TypeClassNameList.length <= l_type ) ||
 			    isNaN( parseInt( l_scale ) ) || isNaN( l_scale ) ||
 			     ( 0 > l_scale ) || ( ScaleClassNameList.length <= l_scale ) ){
 				//タイプクラス名の文字列指定かも
 				if( (ScaleClassNameHashList[l_scale] != undefined) &&
 				     (TypeClassNameHashList[l_type] != undefined) ){
 					m_scale = ScaleClassNameHashList[l_scale];
 					m_type = TypeClassNameHashList[l_type];
 				}
 			}else{
 				//タイプ値の数値指定だね
 				m_scale = ScaleClassNameHashList[ScaleClassNameList[l_scale]];
 				m_type = TypeClassNameHashList[TypeClassNameList[l_type]];
 			}
 		}
 		return self;
 	}

 	//通貨単位を設定する
 	this.setCunit = function(l_cunit){
 		m_cunit = "";
 		if(m_isValid){
 			//単位クラス名の文字列指定以外あるの？
 			m_cunit = ( (CurrencyUnitClassNameHashList[l_cunit] != undefined)?
 					CurrencyUnitClassNameHashList[l_cunit]:"" );
 		}
 		return self;
 	}

 	//処理後の文字列
 	this.toBDCString = function(){
 		var l_target_string = m_value;
 		//処理に値する文字列かどうか
 		if (m_isValid){
 			//端数処理
 			if( !( (m_type < 0) || (m_scale < 0) ) ){
 				var targetBDValue = new BigDecimal(l_target_string);
 				l_target_string = targetBDValue.setScale(m_scale,m_type).toString();
 			}
 			//カンマ付与
 			if(m_attComma){l_target_string = mf_attComma(l_target_string);}
 			//通貨単価付与
 			if(m_cunit != ""){l_target_string = m_cunit + " " + l_target_string;}
 		}
 		return l_target_string;
 	};

 	//■コンストラクタでの処理
 	m_value = mf_setValue( ((arguments.length == 1)?arguments[0]:"") );

 }

 /*******************************************************************************
  * BDCSTR型のオブジェクトを返す
  ******************************************************************************/
 function oBDCS(targetString){
 	return new BDCStr(targetString);
 }

 /*******************************************************************************
  * ■オブジェクトのパラメータでの処理(jQuery拡張)
  ******************************************************************************/

 /*******************************************************************************
  * BDCStrの拡張(クラス名から処理方法取得)
  ******************************************************************************/
 BDCStr.prototype.setSettingsFromObj = function(l_objJQuery) {
 	if($(l_objJQuery).size() == 1){
 		//通貨単位のクラス名あり？
 		for (var keyCunit in CurrencyUnitClassNameHashList) {
 			if($(l_objJQuery).hasClass(keyCunit)){
 				this.setCunit(keyCunit);
 				break;
 			}
 		}
 		//端数処理方式のクラス名あり？
 		for (var keyType in TypeClassNameHashList) {
 			if($(l_objJQuery).hasClass(keyType)){
 				//端数処理桁数のクラス名あり？
 				for (var keyScale in ScaleClassNameHashList) {
 					if($(l_objJQuery).hasClass(keyScale)){
 						//両方持ってた
 						this.setScale(keyType,keyScale);
 						break;
 					}
 				}
 			break;//セットで見つからないなら無視
 			}
 		}
 	}
 	return this;
 };

 /*******************************************************************************
  * jQuery拡張
  ******************************************************************************/
 jQuery.fn.extend({
 	tagValue	: jQtagValue,	 //無加工で値の設定・取得「value あるいは innerHtml」
 	valueBDC	: jQvalueBDC,	 //加工して値の設定・取得
 	setBDCStyle	: jQsetBDCStyle, //端数処理方法および桁の設定 引数：(l_type,l_scale)
 	attBDC		: jQattBDC,	 //既存値を加工して再代入(付与側)
 	rmvBDC		: jQrmvBDC	 //既存値を加工して再代入(除去側)
 });

//プロパティの取得・設定
 function jQtagValue(){
 	//オブジェクトや引数の数がおかしいです。
 	if( ($(this).length <= 0) ||
 	    ( (arguments.length != 1) && (arguments.length != 0) )
 		){return;}

 	//複数の戻値があったとき
 	var getterResultList = new Array();

 	//すべてのオブジェクトについて
 	for(var i=0; i<$(this).length; i++){
 		var l_object = $(this).get(i);
 		var targetAttribute = "";
 		switch($(l_object).attr("tagName")){
 		//明示的にvalue属性を対象とするもの
 			//HTML 4.01 で value属性を持つとされているもの
 			case "INPUT":
 			case "BUTTON":
 			case "OPTION":
 			case "PARAM":
 			//同、非推奨のもの
 			case "LI":
 			//追加的に必要なもの//未設定
 				//value属性の指定
 				targetAttribute = "value";
 				break;
 		//明示的にinnerHTMLを対象とするもの//未設定
 		//他に参照すべき属性のあるもの//未設定
 		//該当しない場合
 			default:
 				//innerHTML か innerText 正しくは ""未指定とすべき
 				targetAttribute = "innerHTML";
 		}
 		//属性が指定されていたら
 		if(targetAttribute!=""){
 			//setterですよ
 			if(arguments.length==1){
 				$(l_object).attr(targetAttribute,arguments[0]);
 			}
 			//getterですね
 			else{
 				getterResultList.push($(l_object).attr(targetAttribute));
 			}
 		}
 	}

 	//setterでした
 	if(arguments.length==1){
 		return $(this);
 	}
 	//getterですね
 	else{
 		//要素個数で
 		return ( ($(this).length == 1)?getterResultList[0]:getterResultList );
 	}
 }

 //BDC処理を通したプロパティの設定・取得
 function jQvalueBDC(){

 	//オブジェクトや引数の数がおかしいです。
 	if( ($(this).length <= 0) ||
 	    ( (arguments.length != 1) && (arguments.length != 0) )
 		){return;}

 	//複数の戻値があったとき
 	var getterResultList = new Array();

 	//すべてのオブジェクトについて
 	for(var i=0; i<$(this).length; i++){
 		var l_object = $(this).get(i);
 		//setterですよ
 		if(arguments.length==1){
 			$(l_object).tagValue( oBDCS(arguments[0])
 						.setSettingsFromObj($(l_object))
 						.toBDCString() );
 		}
 		//getterですね
 		else{
 			getterResultList.push( oBDCS( $(l_object).tagValue() ).value() );
 		}
 	}
 	//setterでした
 	if(arguments.length==1){
 		return $(this);
 	}
 	//getterですね
 	else{
 		//要素個数で
 		return ( ($(this).length == 1)?getterResultList[0]:getterResultList );
 	}
 }

 //端数処理方法および桁の設定
 function jQsetBDCStyle(l_type,l_scale){

 	//クラス名除去関数
 	function safeRemoveClass(l_obj,l_class){
 		if(($(l_obj).size()>0) && (l_class != undefined)){
 			if($(l_obj).hasClass(l_class)){$(l_obj).removeClass(l_class);}
 		}
 	}
 	function removeAllStyle(l_obj){
 		for(var keyScale in ScaleClassNameHashList){
 			safeRemoveClass(l_obj,keyScale);
 		}
 		for(var keyType in TypeClassNameHashList){
 			safeRemoveClass(l_obj,keyType);
 		}
 	}

 	//範囲内の整数指定？
 	if( !(	isNaN( parseInt( l_type ) ) || isNaN( l_type ) ||
 		( 0 > l_type ) || ( TypeClassNameList.length <= l_type ) ||
 		isNaN( parseInt( l_scale ) ) || isNaN( l_scale ) ||
 		( 0 > l_scale ) || ( ScaleClassNameList.length <= l_scale )		) ){

 		//すべてのオブジェクトについて
 		for(var i=0; i<$(this).length; i++){
 			//取っ払います
 			removeAllStyle($($(this).get(i)));
 			//つけます
 			$($(this).get(i)).addClass(ScaleClassNameList[l_scale]);
 			$($(this).get(i)).addClass(TypeClassNameList[l_type]);
 		}
 	}
 	//クラス名文字列指定？
 	else if( (ScaleClassNameHashList[l_scale] != undefined) &&
 			(TypeClassNameHashList[l_type] != undefined) ){

 		//すべてのオブジェクトについて
 		for(var i=0; i<$(this).length; i++){
 			//取っ払います
 			removeAllStyle($($(this).get(i)));
 			//つけます
 			$($(this).get(i)).addClass(l_scale);
 			$($(this).get(i)).addClass(l_type);
 		}
 	}

 	return this;
 }

 //既存値を加工して再代入(付与側)
 function jQattBDC(){
 	return this.each(function(){
 		$(this).valueBDC($(this).tagValue());
 	});

 }

 //既存値を加工して再代入(除去側)
 function jQrmvBDC(){
 	return this.each(function(){
 		$(this).tagValue($(this).valueBDC());
 	});
 }

 /*******************************************************************************
  * ■互換性
  ******************************************************************************/

 /*******************************************************************************
  * カンマ付与対象チェック
  ******************************************************************************/
 function is_target_numeral_commas(targetString){
 	return oBDCS(targetString).isValid();
 }

 /*******************************************************************************
  * カンマ付与
  * 【変更点】可能な限りカンマのみを付与した文字列を返す
  ******************************************************************************/
 function enc_numeral_commas(targetString){
 	return oBDCS(targetString).toBDCString();
 }

 /*******************************************************************************
  * カンマ除去
  * 【変更点】単位も除去
  ******************************************************************************/
 function dec_numeral_commas(targetString){
 	return oBDCS(targetString).value();
 }

 /*******************************************************************************
  * ￥＄除去
  * 【変更点】カンマも除去
  ******************************************************************************/
 function _rmv_cunit(targetString){
 	return oBDCS(targetString).value();
 }

 /*******************************************************************************
  * ￥＄付与
  * 【変更点】カンマも付与
  ******************************************************************************/
 function _att_yen_unit(targetString){
 	return oBDCS(targetString).setCunit("yen_value").toBDCString();
 }
 function _att_dollar_unit(targetString){
 	return oBDCS(targetString).setCunit("dollar_value").toBDCString();
 }

 /*******************************************************************************
  * _getNumStr
  ******************************************************************************/
 function _getNumStr(targetString){
 	return oBDCS(targetString).value();
 }

 /*******************************************************************************
  * _Number	（Number関数代替）
  ******************************************************************************/
 function _Number(targetString){
 	return oBDCS(targetString).Number();
 }

 /*******************************************************************************
  * _isNum	（isNaN関数代替）
  ******************************************************************************/
 function _isNum(targetString){
 	return oBDCS(targetString).isNum();
 }

 /*******************************************************************************
  * 強制カンマ付与
  ******************************************************************************/
 function _set_commas(jQobject){
 	return $(jQobject).attBDC();
 }

 /*******************************************************************************
  * カンマ除去＋フォーカス
  ******************************************************************************/
 function _rmv_commas(jQobject){
 	$(jQobject).rmvBDC();
 	$(jQobject).select();
 	return $(jQobject);
 }

 /*******************************************************************************
  * カンマ付与
  ******************************************************************************/
 function _after_load(jQobjects){
 	return $(jQobjects).attBDC();
 }

 /*******************************************************************************
  * 範囲カンマ除去
  ******************************************************************************/
 function _before_submit(jQobjects){
 	return $(jQobjects).rmvBDC();
 }

 /*******************************************************************************
  * INPUT対象端数処理
  * 【変更】INPUT以外も対象になった
  ******************************************************************************/
 function SetBigDecimalScale_Obj(jQobject){
 	return $(jQobject).attBDC();
 }

 /*******************************************************************************
  * 端数処理のクラス名を付与する
  * @param l_type	端数処理方法（0：切捨て、1:四捨五入、2:切り上げ）
  * @param l_scale	端数処理位置（加工後の小数点以下桁数：0,1,2,3）
  * @param l_objs
  ******************************************************************************/
 function applyNumeralStylesToObj(type,scale,jQobjects){
 	return $(jQobjects).setBDCStyle(type,scale);
 }


 /*******************************************************************************
  * オブジェクトから端数処理方式の定数を返す
  * @param jQobject
  * @return BigDecimal.prototype. の定数を返す。
  ******************************************************************************/
 function getScaletype(jQobject){
 	if($(jQobjects).size() == 1){
 		for (var keyType in TypeClassNameHashList){
 			if($(jQobjects).hasClass(keyType)){
 				return TypeClassNameHashList(keyType);
 			}
 		}
 	}
 	return -1;
 }

 /*******************************************************************************
  * オブジェクトから端数処理桁数を返す
  * @param jQobject
  * @return 端数処理の桁数を返す。
  ******************************************************************************/
 function getScale(jQobject){
 	if($(jQobjects).size() == 1){
 		for (var keyScale in ScaleClassNameHashList){
 			if($(jQobjects).hasClass(keyScale)){
 				return ScaleClassNameHashList(keyScale);
 			}
 		}
 	}
 	return -1;
 }

 /*******************************************************************************
  * ■別名の互換性(ここから)
  ******************************************************************************/
 //alias⇒クラス名
 var AliasClassNameHashList = {
 	'style_price'		: ['scale_2','scale_half_up'],
 	'style_quantity'	: ['scale_0','scale_half_up']
 };
 //alias番号⇒新クラス名
 var STYLE_PRICE = 1;
 var STYLE_QUANTITY = 2;
 var AliasClassNameHashListByNum = {
 	'1'			: ['scale_2','scale_half_up'],
 	'2'			: ['scale_0','scale_half_up']
 };

 //既存値を加工して再代入(付与側)	【オーバーライド】
 function jQattBDC(){
 	return this.each(function(){
 		for(var keyClass in AliasClassNameHashList){
 			//aliasをもっていたら
 			if($(this).hasClass(keyClass)){
 				//クラス名をつけます
 				$(this).setBDCStyle( (AliasClassNameHashList[keyClass])[1],
 							(AliasClassNameHashList[keyClass])[0] );
 				break;
 			}
 		}
 		//後は通常通りの処理
 		$(this).valueBDC($(this).tagValue());
 	});

 }

 //文字列対象
 function SetBigDecimalScale(style,value){
 	for(var keyClass in AliasClassNameHashListByNum){
 		//aliasをもっていたら
 		if(style == keyClass){
 			//マッピングのクラス名でスケールし返す
 			return oBDCS(value)
 				.setScale( (AliasClassNameHashListByNum[keyClass])[1],
 						(AliasClassNameHashListByNum[keyClass])[0] )
 				.setComma(false)
 				.toBDCString();
 		}
 	}
 	return value;
 }

 //文字列対象 内部関数：戻りBD型(エラーチェックなし)
 function _SetBigDecimalScale(style,bdvalue){
 	for(var keyClass in AliasClassNameHashListByNum){
 		//aliasをもっていたら
 		if(style == keyClass){
 			//マッピングの有効性を確認し
 			if( (ScaleClassNameHashList[(AliasClassNameHashListByNum[keyClass])[0]] != undefined) &&
 				     (TypeClassNameHashList[(AliasClassNameHashListByNum[keyClass])[1]] != undefined) ){
 				//スケール処理をして返す
 				return bdvalue.setScale(ScaleClassNameHashList[(AliasClassNameHashListByNum[keyClass])[0]],
 							TypeClassNameHashList[(AliasClassNameHashListByNum[keyClass])[1]]);
 			}
 		}
 	}
 	return bdvalue;
 }

 // 端数処理⇒カンマ付与
 function SetBigDecimalScaleWithNumeralCommas(style,value){
 	for(var keyClass in AliasClassNameHashListByNum){
 		//aliasをもっていたら
 		if(style == keyClass){
 			//マッピングのクラス名でスケールし返す
 			return oBDCS(value)
 				.setScale( (AliasClassNameHashListByNum[keyClass])[1],
 						(AliasClassNameHashListByNum[keyClass])[0] )
 				.toBDCString();
 		}
 	}
 	return value;
 }
 /*******************************************************************************
  * ■別名の互換性(ここまで)
  ******************************************************************************/
