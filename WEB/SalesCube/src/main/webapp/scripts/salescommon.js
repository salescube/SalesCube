//売掛関連　共通関数群


// 特殊商品コードのルーズ判定
// 先頭３文字で判断
function sc_isLooseExceptianalProductCode(productCode){
	if( productCode == "" ){
		return false;
	}
	if( productCode.indexOf("XXX",0) == 0 ){
		return true;
	}
	if( productCode.indexOf("ZZZ",0) == 0 ){
		return true;
	}
	return false;
}

//特殊商品コードの数量を１にする
// productCode 商品コード
// ELM 数量エレメント
function sc_setLooseExceptianalProductCodeQuantiry(productCode, elm){
	if( sc_isLooseExceptianalProductCode(productCode) ){
		elm.val("1");
	}
}
