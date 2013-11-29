/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.dialog;

import java.util.List;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.StockInfoDto;
import jp.co.arkinfosys.entity.join.EntrustPorderRestDetail;
import jp.co.arkinfosys.entity.join.EntrustStockDetail;
import jp.co.arkinfosys.entity.join.PorderRestDetail;
import jp.co.arkinfosys.entity.join.RorderRestDetail;

/**
 * 商品在庫情報ダイアログのアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ShowStockInfoDialogForm {

	public String dialogId;

	public String productCode;

	/**
	 * 在庫数情報
	 */
	public StockInfoDto stockInfoDto;

	/**
	 * 受注残明細
	 */
	public List<RorderRestDetail> rorderRestDetailList;

	/**
	 * 発注残明細
	 */
	public List<PorderRestDetail> porderRestDetailList;

	/**
	 * 委託発注残明細
	 */
	public List<EntrustPorderRestDetail> entrustPorderRestDetailList;

	/**
	 * 委託在庫明細
	 */
	public List<EntrustStockDetail> entrustStockDetailList;

	/**
	 * パラメータの入力チェックを行います.
	 *
	 * @return　表示するメッセージ
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		// 検索条件の有無チェック
		if (!StringUtil.hasLength(productCode)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.condition.insufficiency"));
		}
		return errors;
	}
}
