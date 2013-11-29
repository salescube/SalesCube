/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.estimate;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.entity.Discount;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.form.estimate.DispProductPriceListForm;
import jp.co.arkinfosys.service.DiscountRelService;
import jp.co.arkinfosys.service.DiscountTrnService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * 単価照会画面の表示アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DispProductPriceListAction extends CommonResources {

	/**
	 * 画面遷移用のマッピングクラス
	 */
	public static class Mapping {
		public static final String SEARCH = "dispProductPriceList.jsp";

	}

	@ActionForm
	@Resource
	public DispProductPriceListForm dispProductPriceListForm;


	@Resource
	public DiscountRelService discountRelService;

	@Resource
	public DiscountTrnService discountTrnService;

	@Resource
	public ProductService productService;


	/**
	 * 初期表示処理を行います.
	 *
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		// フォームの初期化
		dispProductPriceListForm.init(userDto, mineDto);
		return DispProductPriceListAction.Mapping.SEARCH;
	}

	/**
	 * 検索表示処理を行います.
	 *
	 * @return 単価照会画面のパス
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String show() throws Exception {
		try {

			// 商品情報を取得
			ProductJoin pj = productService.findById(this.dispProductPriceListForm.productCode);
			if( pj == null ){
				// メッセージに設定
				super.messages
						.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"errors.dispProductPrice.none.productCode"));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			}else{
				this.dispProductPriceListForm.productName = pj.productName;
				if(pj.retailPrice != null ){
					this.dispProductPriceListForm.retailPrice = pj.retailPrice.toString();
				}else{
					this.dispProductPriceListForm.retailPrice = "";
				}
				this.dispProductPriceListForm.productRemarks = pj.remarks;
				// 割引マスタ情報を取得
				Discount discount = discountRelService
						.findDiscountMstByProduct(this.dispProductPriceListForm.productCode);
				if( discount != null ){
					this.dispProductPriceListForm.discountId = discount.discountId;
					this.dispProductPriceListForm.discountName = discount.discountName;
					this.dispProductPriceListForm.remarks = discount.remarks;

					// 割引情報を取得
					this.dispProductPriceListForm.discountTrnList = discountTrnService
								.findDiscountTrnByDiscountId(discount.discountId);
				}else{
					super.messages
						.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
							"errors.dispProductPrice.none.discountRel"));
					ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				}
			}
		} catch (ServiceException e) {
			throw new Exception(e);
		}

		return DispProductPriceListAction.Mapping.SEARCH;
	}


}
