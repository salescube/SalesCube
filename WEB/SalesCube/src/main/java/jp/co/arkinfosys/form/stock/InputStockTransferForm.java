/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.stock;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.stock.EadLineTrnDto;
import jp.co.arkinfosys.dto.stock.EadSlipTrnDto;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

/**
 * 在庫移動入力画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class InputStockTransferForm extends AbstractSlipEditForm<EadLineTrnDto>{

    /**
     * 在庫移動番号（出庫側の入出庫伝票番号）
     */
    public String eadSlipId;

    /**
     * 在庫移動日（入出庫日）
     */
    @Required(arg0 = @Arg(key = "labels.eadDate.transfer"))
    @DateType(datePatternStrict = Constants.FORMAT.DATE, arg0 = @Arg(key = "labels.eadDate.transfer"))
    public String eadDate;

    /**
     * 担当者コード
     */
    public String userId;

    /**
     * 担当者名
     */
    public String userName;

    /**
     * 備考
     */
    @Maxlength(maxlength = 120, arg0 = @Arg(key = "labels.reason"))
    public String remarks;

    /**
     * 在庫締処理日
     */
	public String stockPdate;

	/**
	 * 移動入出庫伝票番号
	 */
	public String moveDepositSlipId;

    /**
     * 明細行リスト
     */
    public List<EadLineTrnDto> eadLineTrnDtoList;

	// 明細行のタブ移動可能項目数
	public int lineElementCount = 12;

	 /**
     * 新規伝票かどうかを判定します.
     * @return　新期伝票か否か
     */
	public boolean isNew() {
		return !StringUtil.hasLength(eadSlipId);
	}

    /**
     * 伝票か〆てあるかどうかを判定します.
     * @return　〆状態か否か
     */
	public boolean isCuttOff() {
		return StringUtil.hasLength(stockPdate);
	}

	/**
	 * フォームをクリアします.
	 */
	public void reset() {
		//menuUpdate = false;
		eadSlipId = null;
		eadDate = null;
		remarks = null;
		userId = null;
		userName = null;
		stockPdate = null;
		moveDepositSlipId = null;
		updDatetm = null;
		eadLineTrnDtoList = null;
	}

	/**
	 * 在庫移動番号（出庫側の入出庫伝票番号）を設定します.
	 * @param keyValue 在庫移動番号
	 */
	@Override
	public void setKeyValue(String keyValue) {
		this.eadSlipId = keyValue;
	}

	/**
	 * @return {@link EadLineTrnDto}のリスト
	 */
	@Override
	public List<EadLineTrnDto> getLineList() {
		return this.eadLineTrnDtoList;
	}

	/**
	 * @param lineList {@link EadLineTrnDto}のリスト
	 */
	@Override
	public void setLineList(List<EadLineTrnDto> lineList) {
		this.eadLineTrnDtoList = lineList;
	}

	/**
	 * フォームの初期化共通処理２を行います.
	 *
	 */
	public void initializeScreenInfo() {
		// Formの初期化
    	reset();
		userId = this.userDto.userId;
		userName = this.userDto.nameKnj;
		eadLineTrnDtoList = new ArrayList<EadLineTrnDto>();
	}

	/**
	 * @return {@link EadSlipTrnDto}
	 */
	@Override
	public AbstractSlipDto<EadLineTrnDto> copyToDto() {
		return Beans.createAndCopy(EadSlipTrnDto.class, this).execute();
	}

	/**
	 * @return {@link MENU_ID#INPUT_STOCK_TRANSFER}で定義されたID
	 */
	@Override
	protected String getMenuID() {
		return Constants.MENU_ID.INPUT_STOCK_TRANSFER;
	}

	/**
	 * @param dto {@link EadSlipTrnDto}
	 */
	@Override
	public void setDefaultSelected(AbstractSlipDto<EadLineTrnDto> dto) {
		// 明細行を再設定する
		if (this.eadLineTrnDtoList == null) {
			this.eadLineTrnDtoList = new ArrayList<EadLineTrnDto>();
		}
	}

	/**
	 * 伝票複写時の初期値を設定します.<BR>
	 * 未使用です.
	 */
	public void initCopy() throws ServiceException {
		// 伝票複写なし
		return ;
	}
}
