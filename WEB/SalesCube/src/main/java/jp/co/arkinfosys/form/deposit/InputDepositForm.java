/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.deposit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.common.Constants.MENU_ID;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.deposit.DepositLineDto;
import jp.co.arkinfosys.dto.deposit.DepositSlipDto;
import jp.co.arkinfosys.entity.Bill;
import jp.co.arkinfosys.entity.DepositSlip;
import jp.co.arkinfosys.entity.join.CustomerJoin;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;

/**
 * 入金入力画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class InputDepositForm extends AbstractSlipEditForm<DepositLineDto> {
	// 入金伝票情報エンティティの内容
	public String depositSlipId; // 入金伝票番号
	public String status; // 状態フラグ
	@Required
	@DateType
	public String depositDate; // 入金日
	@DateType
	public String inputPdate; // 入力処理日
	public String depositAnnual; // 入金年度
	public String depositMonthly; // 入金月度
	public String depositYm; // 入金年月度
	public String userId; // 担当者コード
	public String userName; // 担当者名
	@Maxlength(maxlength = 50, arg0 = @Arg(key = "labels.tekiyou"))
	public String depositAbstract; // 摘要
	@Maxlength(maxlength = 120, arg0 = @Arg(key = "labels.remarks"))
	public String remarks; // 備考
	@Required
	@Mask(mask = Constants.CODE_MASK.CUSTOMER_MASK, msg = @Msg(key = "errors.invalid"), args = @Arg(key = "labels.customerCode", resource = true, position = 0))
	public String customerCode; // 得意先コード
	public String customerName; // 得意先会社名
	public String cutoffGroup; // 締日グループ
	public String paybackCycleCategory; // 回収間隔
	public String customerRemarks; // 備考（顧客）
	public String customerCommentData; // コメント（顧客）
	public String baCode; // 請求先コード
	public String baName; // 請求先会社名
	public String baKana; // 請求先カナ
	public String baOfficeName; // 請求先事業所名
	public String baOfficeKana; // 請求先事業所カナ
	public String baDeptName; // 請求先部署名
	public String baZipCode; // 請求先郵便番号
	public String baAddress1; // 請求先住所１
	public String baAddress2; // 請求先住所２
	public String baPcName; // 請求先担当者
	public String baPcKana; // 請求先担当者カナ
	public String baPcPreCatrgory; // 請求先敬称コード
	public String baPcPre; // 請求先敬称
	public String baTel; // 請求先電話番号
	public String baFax; // 請求先ＦＡＸ番号
	public String baEmail; // 請求先Email
	public String baUrl; // 請求先URL
	public String salesCmCategory; // 売上取引区分
	public String salesCmCategoryName; // 売上取引区分名
	@Required
	public String depositCategory; // 入金区分コード
	public String depositTotal; // 伝票合計金額
	public String billId; // 請求書番号
	@DateType
	public String billCutoffDate; // 請求締め日付
	@DateType
	public String billCutoffPdate; // 請求締め処理日
	public String artId; // 売掛残高番号
	public String salesSlipId; // 売上伝票番号
	public String depositMethodTypeCategory; // 入金取り込み方法
	public String creFunc; // 作成機能
	@DateType
	public String creDatetm; // 作成日時
	public String creUser; // 作成者
	public String updFunc; // 更新機能
	public String salesCutoffDate; // 売掛締め日
	public String salesCutoffPdate; // 売掛締め処理日

	// 集計情報
	public String lastBillingPrice; // 前回請求額
	public String nowPaybackPrice; // 今回回収額
	public String nowSalesPrice; // 今回売上高
	public String billingBalancePrice; // 請求残高

	// 伝票複写用情報
	public String copySlipName; // 複写対象　伝票種類
	public String copySlipId; // 複写対象　伝票番号

	// 伝票明細行エンティティの内容
	public List<DepositLineDto> depLineList = new ArrayList<DepositLineDto>();

	public String inputBillId; // 入力の元になっている請求書番号

	// 処理タイプ
	public int procType = 2;

	// 明細行のタブ移動可能項目数
	public int lineElementCount = 3;

	// 備考文字数
	public static final int remarksSize = 120;

	// 入金区分初期値
	private String initDepositCategory = CategoryTrns.DEPOSIT_CATEGORY_CASH;

	// 顧客コード選択状態フラグ　"1" 選択
	public String customerIsExist;

	/**
	 * 請求書から入金伝票を作成する場合の初期化を行います.
	 * @param bill　請求書
	 */
	public void initialize(Bill bill) {

		// 伝票情報の初期化
		initializeSlip();

		// 顧客は未選択
		customerIsExist = "";

		DepositSlipDto dto = (DepositSlipDto) this.copyToDto();
		dto.setLineDtoList(this.depLineList);
		dto.fillList();

		// １行目に請求金額をセットする
		DepositLineDto lineDto = depLineList.get(0);
		lineDto.price = bill.thisBillPrice.toString();

	}

	/**
	 * 請求書から入金伝票を初期化する場合の顧客情報を設定します.
	 * @param cj 顧客情報
	 * @param dap 請求先情報
	 */
	public void initialize(CustomerJoin cj, DeliveryAndPre dap) {

		customerCode = cj.customerCode; // 得意先コード
		customerName = cj.customerName; // 得意先会社名
		cutoffGroup = cj.cutoffGroup; // 締日グループ
		paybackCycleCategory = cj.paybackCycleCategory; // 回収間隔
		customerRemarks = cj.remarks; // 備考（顧客）
		customerCommentData = cj.commentData; // コメント（顧客）
		baCode = dap.deliveryCode; // 請求先コード
		baName = dap.deliveryName; // 請求先会社名
		baKana = dap.deliveryKana; // 請求先カナ
		baOfficeName = dap.deliveryOfficeName; // 請求先事業所名
		baOfficeKana = dap.deliveryOfficeKana; // 請求先事業所カナ
		baDeptName = dap.deliveryDeptName; // 請求先部署名
		baZipCode = dap.deliveryZipCode; // 請求先郵便番号
		baAddress1 = dap.deliveryAddress1; // 請求先住所１
		baAddress2 = dap.deliveryAddress2; // 請求先住所２
		baPcName = dap.deliveryPcName; // 請求先担当者
		baPcKana = dap.deliveryPcKana; // 請求先担当者カナ
		baPcPreCatrgory = dap.deliveryPcPreCategory; // 請求先敬称コード
		baPcPre = dap.customerPcPreCategoryName; // 請求先敬称
		baTel = dap.deliveryTel; // 請求先電話番号
		baFax = dap.deliveryFax; // 請求先ＦＡＸ番号
		baEmail = dap.deliveryEmail; // 請求先Email
		baUrl = dap.deliveryUrl; // 請求先URL
		salesCmCategory = cj.salesCmCategory; // 売上取引区分
		salesCmCategoryName = cj.categoryCodeName3; // 売上取引区分名
		taxFractCategory = cj.taxFractCategory;
		priceFractCategory = cj.priceFractCategory;
	}

	/**
	 * 初期化処理を行います.
	 */
	public void initializeSlip() {
		// 新規作成時のIDは空欄
		depositSlipId = ""; // 入金伝票番号
		status = DepositSlip.STATUS_INIT; // 状態フラグ
		// 入金日は当日日付
		depositDate = ""; //new Date(GregorianCalendar.getInstance().getTimeInMillis()).toString();
		// 入力処理日も当日
		inputPdate = ""; //new Date(GregorianCalendar.getInstance().getTimeInMillis()).toString();
		userId = this.userDto.userId; // 担当者コード
		userName = this.userDto.nameKnj; // 担当者名
		depositAbstract = ""; // 摘要
		customerCode = ""; // 得意先コード
		customerName = ""; // 得意先会社名
		cutoffGroup = ""; // 締日グループ
		paybackCycleCategory = ""; // 回収間隔
		customerRemarks = ""; // 備考（顧客）
		customerCommentData = ""; // コメント（顧客）
		baName = ""; // 請求先会社名
		baOfficeName = ""; // 請求先事業所名
		baDeptName = ""; // 請求先部署名
		baZipCode = ""; // 請求先郵便番号
		baAddress1 = ""; // 請求先住所１
		baAddress2 = ""; // 請求先住所２
		baPcName = ""; // 請求先担当者
		baPcKana = ""; // 請求先担当者カナ
		baPcPreCatrgory = ""; // 請求先敬称コード
		baPcPre = ""; // 請求先敬称
		baTel = ""; // 請求先電話番号
		baFax = ""; // 請求先ＦＡＸ番号
		baEmail = ""; // 請求先Email
		// 入金区分コード
		depositCategory = this.initDepositCategory;

		// 取り込み区分 入力 画面入力
		depositMethodTypeCategory = CategoryTrns.DEPOSIT_METHOD_INPUT;

		lastBillingPrice = ""; // 前回請求額
		nowPaybackPrice = ""; // 今回回収額
		nowSalesPrice = ""; // 今回売上高
		billingBalancePrice = ""; // 請求残高

	}

	/**
	 * 支払条件コードを返します.
	 * @return　支払条件コード
	 */
	public String getCutoffGroupCategory() {
		return this.cutoffGroup + this.paybackCycleCategory;
	}

	/**
	 * @return {@link MENU_ID#INPUT_DEPOSIT}で定義されたID
	 */
	@Override
	protected String getMenuID() {
		return Constants.MENU_ID.INPUT_DEPOSIT;
	}

	/**
	 * 明細行をクリアし、入力担当者を設定します.
	 */
	@Override
	public void initializeScreenInfo() {
		for (DepositLineDto lineDto : this.depLineList) {
			// 明細行クリア
			lineDto.initialize();
		}

		// 入力担当者
		this.userId = this.userDto.userId;
		this.userName = this.userDto.nameKnj;
	}

	/**
	 * @param dto {@link DepositSlipDto}
	 */
	@Override
	public void setDefaultSelected(AbstractSlipDto<DepositLineDto> dto) {
		depositMethodTypeCategory = CategoryTrns.DEPOSIT_METHOD_INPUT;
	}

	/**
	 * @return {@link DepositSlipDto}
	 */
	@Override
	public AbstractSlipDto<DepositLineDto> copyToDto() {
		return Beans.createAndCopy(DepositSlipDto.class, this).execute();
	}

	/**
	 * 入金伝票番号を設定します.
	 * @param keyValue 入金伝票番号
	 */
	@Override
	public void setKeyValue(String keyValue) {
		this.depositSlipId = keyValue;
	}

	/**
	 * @return {@link DepositLineDto}のリスト
	 */
	@Override
	public List<DepositLineDto> getLineList() {
		return this.depLineList;
	}

	/**
	 * @param lineList {@link DepositLineDto}のリスト
	 */
	@Override
	public void setLineList(List<DepositLineDto> lineList) {
		this.depLineList = lineList;
	}

	@Override
	public void initCopy() throws ServiceException {
		depositSlipId = ""; // 入金伝票番号
		status = DepositSlip.STATUS_INIT; // 状態フラグ
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT.DATE);

		// 入金日は当日日付
		depositDate = sdf.format(new Date(GregorianCalendar.getInstance()
				.getTimeInMillis()));
		// 入力処理日も当日
		inputPdate = sdf.format(new Date(GregorianCalendar.getInstance()
				.getTimeInMillis()));
		userId = this.userDto.userId; // 担当者コード
		userName = this.userDto.nameKnj; // 担当者名

		// 取り込み区分 入力 画面入力
		depositMethodTypeCategory = CategoryTrns.DEPOSIT_METHOD_INPUT;

		for (DepositLineDto lineDto : this.depLineList) {
			// 明細行クリア
			lineDto.initForCopy();
		}
	}

	/**
	 * 伝票の状態を判定します.
	 * @return　入金締終了状態か否か
	 */
	public boolean isClosed() {
		return DepositSlip.STATUS_CLOSE.equals(status)
				|| StringUtil.hasLength(salesCutoffDate);
	}
}
