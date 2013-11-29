/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.stock;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.AbstractSlipDto;
import jp.co.arkinfosys.dto.stock.EntrustEadLineTrnDto;
import jp.co.arkinfosys.dto.stock.EntrustEadSlipTrnDto;
import jp.co.arkinfosys.form.AbstractSlipEditForm;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;

/**
 * 委託入出庫入力画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class InputEntrustStockForm extends AbstractSlipEditForm<EntrustEadLineTrnDto> {
    /**
     * 委託入出庫伝票番号
     */
    public String entrustEadSlipId;

    /**
     * 発注番号
     */
    public String poSlipId;

    /**
     * 入出庫日
     */
    @Required
    @DateType(datePatternStrict = Constants.FORMAT.DATE)
    public String entrustEadDate;

    /**
     * 委託入出庫区分
     */
    public String entrustEadCategory;

    /**
     * 伝票複写ダイアログで選択された発注伝票が検索された時点の委託入出庫区分
     */
    public String copySlipFixedEntrustEadCategory;

    /**
     * 備考
     */
    @Maxlength(maxlength = 120, arg0 = @Arg(key = "labels.reason"))
    public String remarks;

    /**
     * 担当者コード
     */
    public String userId;

    /**
     * 担当者名
     */
    public String userName;

    /**
     * 在庫締処理日
     */
	public String stockPdate;

	/**
	 * 既に登録済みの伝票の読み込みにより画面表示した場合true、そうでない場合false(チェックボックスの表示非表示の制御等に使用する)
	 */
	public Boolean isExistSlipRead;

	/**
	 * 委託出庫伝票を読み込んでいる場合true、そうでない場合(委託入庫伝票を読み込んでいる時も含む)false
	 * (JSPで委託入出庫区分を判別しやすいようにBooleanにしただけの変数)
	 */
	public Boolean isEntrustDispatch = false;

	public String copySlipId; // 複写伝票番号

	/**
	 * 仕入日
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String supplierDate;

	/**
	 * 納期
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String deliveryDate;

	/**
	 * 仕入先コード
	 */
	public String supplierCode;

	/**
	 * 仕入先取引区分
	 */
	public String supplierCmCategory;

	/**
	 * 出庫指示書発行回数
	 */
	public String dispatchOrderPrintCount;

	/**
	 * 仕入先名
	 */
	public String supplierName;

    /**
     * 明細行リスト
     */
    public List<EntrustEadLineTrnDto> entrustEadLineTrnDtoList;

    /**
     * 新規伝票かどうかを判定します.
     * @return　新期伝票か否か
     */
	public boolean isNew() {
		return !StringUtil.hasLength(entrustEadSlipId);
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
		menuUpdate = false;
		entrustEadSlipId = null;
		poSlipId = null;
		entrustEadDate = null;
		entrustEadCategory = null;
		remarks = null;
		userId = null;
		userName = null;
		stockPdate = null;
		updDatetm = null;
		supplierCode = null;
		supplierName = null;
		supplierDate = null;
		supplierCmCategory = null;
		entrustEadLineTrnDtoList = null;
	}

	/**
	 * 伝票複写時の初期値を設定します.<BR>
	 * 未使用です.
	 */
	@Override
	public void initCopy() throws ServiceException {
	}

	/**
	 * フォームを初期化し、更新可能フラグと入力担当者を設定します.
	 */
	@Override
	public void initializeScreenInfo() {
		// Formを初期化
		reset();

		menuUpdate = userDto.isMenuUpdate(Constants.MENU_ID.INPUT_STOCK);
		entrustEadCategory = "";
		userId = this.userDto.userId;
		userName = this.userDto.nameKnj;
		/***
		entrustEadLineTrnDtoList = new ArrayList<EntrustEadLineTrnDto>();
		for(int i=0;i<INIT_LINE_SIZE;i++) {
			EntrustEadLineTrnDto dto = new EntrustEadLineTrnDto();
			dto.lineNo = Integer.toString(i+1);
			entrustEadLineTrnDtoList.add(dto);
		}
		***/
	}

	/**
	 * @return {@link MENU_ID#INPUT_ENTRUST_STOCK}で定義されたID
	 */
	@Override
	protected String getMenuID() {
		return Constants.MENU_ID.INPUT_ENTRUST_STOCK;
	}

	/**
	 * @param dto {@link EntrustEadSlipTrnDto}
	 */
	@Override
	public void setDefaultSelected(AbstractSlipDto<EntrustEadLineTrnDto> dto) {
		// 明細行を再設定する
		if (this.entrustEadLineTrnDtoList == null) {
			this.entrustEadLineTrnDtoList = new ArrayList<EntrustEadLineTrnDto>();
		}
	}

	/**
	 * @return {@link EntrustEadSlipTrnDto}
	 */
	@Override
	public AbstractSlipDto<EntrustEadLineTrnDto> copyToDto() {
		return Beans.createAndCopy(EntrustEadSlipTrnDto.class, this).execute();
	}

	/**
	 * 委託入出庫伝票番号を設定します.
	 * @param keyValue 委託入出庫伝票番号
	 */
	@Override
	public void setKeyValue(String keyValue) {
		this.entrustEadSlipId = keyValue;
	}

	/**
	 * @return {@link EntrustEadLineTrnDto}のリスト
	 */
	@Override
	public List<EntrustEadLineTrnDto> getLineList() {
		return this.entrustEadLineTrnDtoList;
	}

	/**
	 * @param lineList {@link EntrustEadLineTrnDto}のリスト
	 */
	@Override
	public void setLineList(List<EntrustEadLineTrnDto> lineList) {
		this.entrustEadLineTrnDtoList = lineList;
	}

}
