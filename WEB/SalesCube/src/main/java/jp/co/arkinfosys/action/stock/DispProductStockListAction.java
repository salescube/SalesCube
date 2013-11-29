/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.stock;

import java.util.List;

import javax.annotation.Resource;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.dto.StockInfoDto;
import jp.co.arkinfosys.entity.join.EntrustPorderRestDetail;
import jp.co.arkinfosys.entity.join.EntrustStockDetail;
import jp.co.arkinfosys.entity.join.PorderRestDetail;
import jp.co.arkinfosys.entity.join.RorderRestDetail;
import jp.co.arkinfosys.form.stock.DispProductStockForm;

/**
 * 在庫照会画面のアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DispProductStockListAction extends CommonResources  {
	@ActionForm
	@Resource
	private DispProductStockForm dispProductStockForm;

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
	 * 画面遷移用のマッピングクラスです.
	 */
	public static class Mapping {
		public static final String INPUT = "dispProductStockList.jsp";
	}

	/**
	 * 初期表示処理を行います.
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		this.dispProductStockForm.productCode = "";
		return Mapping.INPUT;
	}
}
