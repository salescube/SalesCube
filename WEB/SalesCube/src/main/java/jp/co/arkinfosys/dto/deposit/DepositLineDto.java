/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.deposit;

import jp.co.arkinfosys.dto.AbstractLineDto;
import jp.co.arkinfosys.entity.DepositLine;
/**
 * 入金伝票明細行のDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DepositLineDto extends AbstractLineDto {
	public String depositLineId;		// 入金伝票行ID
	public String status;				// 状態フラグ
	public String depositSlipId;		// 入金伝票番号
	public String depositCategory;		// 入金区分コード
	public String price;				// 金額
	public String instDate;				// 手形期日
	public String instNo;				// 手形番号
	public String bankId;				// 銀行マスタID
	public String bankInfo;				// 銀行情報
	public String remarks;				// 備考
	public String salesLineId;			// 売上伝票行ID
	public String creFunc;				// 作成機能
	public String creDatetm;			// 作成日時
	public String creUser;				// 作成者
	public String updFunc;				// 更新機能
	public String updDatetm;			// 更新日時
	public String updUser;				// 更新者

	/**
	 * 金額がnull又は空白かどうか調べます.
	 * @return　true：null又は空 false:nullでも空白でもない
	 */
	@Override
	public boolean isBlank() {
		return price == null || price.length() == 0;
	}

	/**
	 * 明細行をクリアします(伝票複写用）.
	 */
	public void initForCopy() {
		depositLineId = "";					// 入金伝票行ID
		status = DepositLine.STATUS_INIT;	// 状態フラグ
		depositSlipId = "";					// 入金伝票番号
	}

	/**
	 * 初期化します.
	 */
	public void initialize(){
		depositLineId = "";					// 入金伝票行ID
		status = DepositLine.STATUS_INIT;	// 状態フラグ
		depositSlipId = "";					// 入金伝票番号
		lineNo = "";					// 入金伝票行番
		price = "";							// 金額
		bankId = "";						// 銀行マスタID
		bankInfo = "";						// 銀行情報
		remarks = "";						// 備考
	}
}
