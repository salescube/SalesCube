/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.stock;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.common.CategoryTrns;
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
 * 入出庫入力画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class InputStockForm extends AbstractSlipEditForm<EadLineTrnDto> {

    /**
     * 入出庫伝票番号
     */
    public String eadSlipId;

    /**
     * 入出庫日
     */
    @Required
    @DateType(datePatternStrict = Constants.FORMAT.DATE)
    public String eadDate;

    /**
     * 入出庫伝票区分
     */
    public String eadSlipCategory;

    /**
     * 入出庫区分
     */
    public String eadCategory;

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
     * 明細行リスト
     */
    public List<EadLineTrnDto> eadLineTrnDtoList;

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
		eadSlipCategory = null;
		eadCategory = null;
		remarks = null;
		userId = null;
		userName = null;
		stockPdate = null;
		updDatetm = null;
		eadLineTrnDtoList = null;
	}

	/**
	 * @return {@link EadSlipTrnDto}
	 */
	@Override
	public AbstractSlipDto<EadLineTrnDto> copyToDto() {
		return Beans.createAndCopy(EadSlipTrnDto.class, this).execute();
	}

	/**
	 * @return {@link EadLineTrnDto}のリスト
	 */
	@Override
	public List<EadLineTrnDto> getLineList() {
		return eadLineTrnDtoList;
	}

	/**
	 * @return {@link MENU_ID#INPUT_STOCK}で定義されたID
	 */
	@Override
	protected String getMenuID() {
		return Constants.MENU_ID.INPUT_STOCK;
	}

	@Override
	public void initCopy() throws ServiceException {
		// Formを初期化
		reset();
		//eadDate = StringUtil.getCurrentDateString(Constants.FORMAT.DATE);
		menuUpdate = userDto.isMenuUpdate(Constants.MENU_ID.INPUT_STOCK);
		eadSlipCategory = CategoryTrns.EAD_SLIP_CATEGORY_NORMAL;
		eadCategory = CategoryTrns.EAD_CATEGORY_ENTER;
		userId = userDto.userId;
		userName = userDto.nameKnj;
		eadLineTrnDtoList = new ArrayList<EadLineTrnDto>();
		for(int i=0;i<INIT_LINE_SIZE;i++) {
			EadLineTrnDto dto = new EadLineTrnDto();
			dto.lineNo = Integer.toString(i+1);
			eadLineTrnDtoList.add(dto);
		}
	}

	/**
	 * フォームを初期化し、初期値を設定します.
	 */
	@Override
	public void initializeScreenInfo() {
		// Formを初期化
		reset();
		//inputStockForm.eadDate = StringUtil.getCurrentDateString(Constants.FORMAT.DATE);
		menuUpdate = userDto.isMenuUpdate(Constants.MENU_ID.INPUT_STOCK);
		eadSlipCategory = CategoryTrns.EAD_SLIP_CATEGORY_NORMAL;
		eadCategory = CategoryTrns.EAD_CATEGORY_ENTER;
		userId = this.userDto.userId;
		userName = this.userDto.nameKnj;
		eadLineTrnDtoList = new ArrayList<EadLineTrnDto>();
	}

	/**
	 * フォームのデフォルト値を設定します.<BR>
	 * 未使用です.
	 */
	@Override
	public void setDefaultSelected(AbstractSlipDto<EadLineTrnDto> dto) {
	}

	/**
	 * 入出庫伝票番号を設定します.
	 * @param keyValue 入出庫伝票番号
	 */
	@Override
	public void setKeyValue(String keyValue) {
		eadSlipId = keyValue;
	}

	/**
	 * @param lineList {@link EadLineTrnDto}のリスト
	 */
	@Override
	public void setLineList(List<EadLineTrnDto> lineList) {
		eadLineTrnDtoList = lineList;
	}
}
