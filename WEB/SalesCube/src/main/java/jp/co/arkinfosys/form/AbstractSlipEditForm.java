/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.form;

import java.util.List;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.AbstractLineDto;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.UserDto;
import jp.co.arkinfosys.dto.setting.MineDto;
import jp.co.arkinfosys.entity.TaxRate;
import jp.co.arkinfosys.service.TaxRateService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.action.ActionMessages;

/**
 * 伝票入力画面の基底アクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 * @param <LINEDTOCLASS>
 */
public abstract class AbstractSlipEditForm<LINEDTOCLASS extends AbstractLineDto> {

	/** 明細行の初期サイズ */
	public static final int INIT_LINE_SIZE = 6;

	/** 明細行の最大サイズ */
	public static final int MAX_LINE_SIZE = 35;

	/**
	 * 更新可能フラグ
	 */
	public boolean menuUpdate;

	/**
	 * 削除明細IDリスト
	 */
	public String deleteLineIds;

	/**
	 * 現在の税率
	 */
	public String taxRate;

	/** 税転嫁 */
	public String taxShiftCategory;
	/**
	 * 税端数処理
	 */
	public String taxFractCategory;
	/**
	 * 単価端数処理
	 */
	public String priceFractCategory;

	/**
	 * 更新日（排他制御のため）
	 */
	public String updDatetm;
	/**
	 * 更新者（排他制御のため）
	 */
	public String updUser;

	/** 新規作成状態の管理フラグ */
	public Boolean newData = true;

	/** 税率計算用サービス */
	public TaxRateService taxRateService;

	/** ログインユーザー情報 */
	public UserDto userDto;

	/** 自社情報 */
	public MineDto mineDto;

	/**
	 * 伝票が新規作成かどうか判定します.
	 *
	 * @return 新規作成か否か
	 */
	public boolean isNewData() {
		return newData;
	}

	/**
	 * 全画面共通のフォームの初期化処理を行います.
	 * @throws ServiceException
	 */
	public void initialize() throws ServiceException {

		
		TaxRate tx = taxRateService.findTaxRateById(CategoryTrns.TAX_TYPE_CTAX,
									StringUtil.getCurrentDateString(Constants.FORMAT.DATE));
		this.taxRate = tx.taxRate.toString();

		
		this.menuUpdate = this.userDto.isMenuUpdate(this.getMenuID());

		
		taxFractCategory = mineDto.taxFractCategory;
		taxShiftCategory = mineDto.taxShiftCategory;
		priceFractCategory = mineDto.priceFractCategory;
	}

	/**
	 * メニューＩＤを返します.
	 *
	 * @return メニューＩＤ
	 */
	protected abstract String getMenuID();

	/**
	 * 固有の画面情報を初期化します.
	 */
	public abstract void initializeScreenInfo();

	/**
	 * 初期値を設定します.
	 *
	 * @param dto 設定値を持つDTO
	 */
	public abstract void setDefaultSelected(AbstractSlipDto<LINEDTOCLASS> dto);

	/**
	 * ActionForm をリセットします.<br>
	 * デフォルト実装では何も処理しません.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 */
	public void reset() {
		
	}

	/**
	 * フォームからDTO へコピーします.
	 */
	public abstract AbstractSlipDto<LINEDTOCLASS> copyToDto();

	/**
	 * ＰＫとなる値を設定します.
	 */
	public abstract void setKeyValue(String keyValue);

	/**
	 * 明細行リストを返します.
	 * @return 明細行リスト
	 */
	public abstract List<LINEDTOCLASS> getLineList();

	/**
	 * 明細行リストを設定します.
	 * @param lineList 明細行リスト
	 */
	public abstract void setLineList(List<LINEDTOCLASS> lineList);

	/**
	 * 伝票複写時の初期値を設定します.
	 * @throws ServiceException
	 */
	public abstract void initCopy() throws ServiceException;

	/**
	 * 入力値のバリデートを行います.<br>
	 * デフォルト実装では何も処理しません.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @return　表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();

		
		return errors;
	}

	/**
	 * 既存伝票ロード時の初期値設定を行います.
	 * @throws ServiceException
	 */
	public void initLoad() throws ServiceException {
		newData = false;
	}
}
