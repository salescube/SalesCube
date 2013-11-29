/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.porder;

import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.porder.POrderSlipLineJoinDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.IntegerType;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Msg;

/**
 *　発注書発行画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class MakeOutPOrderForm extends AbstractSearchForm<POrderSlipLineJoinDto> {

	/**
	 * 伝票番号（開始）
	 */
	@IntegerType
	public String poSlipIdFrom;

	/**
	 * 伝票番号（終了）
	 */
	@IntegerType
	public String poSlipIdTo;

	/**
	 * 発注日（開始）
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String poDateFrom;

	/**
	 * 発注日（終了）
	 */
	@DateType(datePatternStrict = "yyyy/MM/dd")
	public String poDateTo;

	/**
	 * 仕入先コード
	 */
	@Mask(mask = Constants.CODE_MASK.SUPPLIER_MASK, msg = @Msg(key = "errors.invalid"), args = @Arg(key = "labels.supplierCode", resource = true, position = 0))
	public String supplierCode;

	/**
	 * 仕入先名
	 */
	public String supplierName;

	/**
	 * 入力担当者名
	 */
	public String userName;

	/**
	 * 発行済みを除く
	 */
	public boolean exceptAlreadyOutput;

	/**
	 * 選択された伝票番号リスト
	 */
	public List<String> poSlipIdList;

	/**
	 * 検索結果リスト(全件)
	 */
	public List<POrderSlipLineJoinDto> allSearchResultList;
}
