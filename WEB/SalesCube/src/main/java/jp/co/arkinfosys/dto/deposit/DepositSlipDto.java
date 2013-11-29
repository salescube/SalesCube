/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.deposit;

import jp.co.arkinfosys.dto.AbstractLineDto;
import jp.co.arkinfosys.dto.AbstractSlipDto;

/**
 * 入金伝票行のDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DepositSlipDto extends AbstractSlipDto<DepositLineDto> {

	// 入金伝票情報エンティティの内容
	public String depositSlipId;	// 入金伝票番号
	public String status;			// 状態フラグ
	public String depositDate;		// 入金日
	public String inputPdate;			// 入力処理日
	public String depositAnnual;		// 入金年度
	public String depositMonthly;	// 入金月度
	public String depositYm;		// 入金年月度
	public String userId;			// 担当者コード
	public String userName;			// 担当者名
	public String depositAbstract;	// 摘要
	public String remarks;			// 備考
	public String customerCode;		// 得意先コード
	public String customerName;		// 得意先会社名
	public String cutoffGroup;		// 締日グループ
	public String paybackCycleCategory;		// 回収間隔
	public String customerRemarks;	// 備考（顧客）
	public String customerCommentData; // コメント（顧客）
	public String baCode;			// 請求先コード
	public String baName;			// 請求先会社名
	public String baKana;			// 請求先カナ
	public String baOfficeName;		// 請求先事業所名
	public String baOfficeKana;		// 請求先事業所カナ
	public String baDeptName;		// 請求先部署名
	public String baZipCode;		// 請求先郵便番号
	public String baAddress1;		// 請求先住所１
	public String baAddress2;		// 請求先住所２
	public String baPcName;			// 請求先担当者
	public String baPcKana;			// 請求先担当者カナ
	public String baPcPreCatrgory;	// 請求先敬称コード
	public String baPcPre;			// 請求先敬称
	public String baTel;			// 請求先電話番号
	public String baFax;			// 請求先ＦＡＸ番号
	public String baEmail;			// 請求先Email
	public String baUrl;			// 請求先URL
	public String salesCmCategory;	// 売上取引区分
	public String salesCmCategoryName;	// 売上取引区分名
	public String depositCategory;	// 入金区分コード
	public String depositTotal;	// 伝票合計金額
	public String billId;			// 請求書番号
	public String billCutoffDate;		// 請求締め日付
	public String billCutoffPdate;	// 請求締め処理日
	public String artId;			// 売掛残高番号
	public String salesSlipId;		// 売上伝票番号
	public String depositMethodTypeCategory;	// 入金取り込み方法
	public String creFunc;			// 作成機能
	public String creDatetm;		// 作成日時
	public String creUser;			// 作成者
	public String updFunc;			// 更新機能
	public String updDatetm;		// 更新日時
	public String updUser;			// 更新者
	public String salesCutoffDate;	// 売掛締め日
	public String salesCutoffPdate;	// 売掛締め処理日
	public String taxFractCategory;		// 税端数処理　顧客マスタ
	public String priceFractCategory;	// 単価端数処理　顧客マスタ


	// 集計情報
	public String lastBillingPrice;			// 前回請求額
	public String nowPaybackPrice;			// 今回回収額
	public String nowSalesPrice;			// 今回売上高
	public String billingBalancePrice;		// 請求残高


	// 伝票複写用情報
	public String copySlipName;		// 複写対象　伝票種類
	public String copySlipId;		// 複写対象　伝票番号

	public String inputBillId;			// 入力の元になっている請求書番号

	// 顧客コード選択状態フラグ　"1" 選択
	public String customerIsExist;

	/**
	 * 入金伝票明細行を１行作成します.
	 * @return 入金伝票明細行
	 */
	@Override
	public AbstractLineDto createLineDto() {
		DepositLineDto dto = new DepositLineDto();
		dto.initialize();
		return dto;
	}

	/**
	 * 入金伝票番号を取得します.
	 * @return 入金伝票番号
	 */
	@Override
	public String getKeyValue() {
		return this.depositSlipId;
	}
}
