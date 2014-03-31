/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.setting;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.NumberUtil;
import jp.co.arkinfosys.entity.Mine;
import jp.co.arkinfosys.form.setting.StockForm;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.MineService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * 在庫管理画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class StockAction extends CommonResources {

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Mapping {
		public static final String INPUT = "stock.jsp";
	}

	@ActionForm
	@Resource
	public StockForm stockForm;

	@Resource
	private MineService mineService;

	@Resource
    private CategoryService categoryService;

	//月平均出荷数の計算期間プルダルン
    public List<LabelValueBean> stockHoldTermCalcCategoryList = new ArrayList<LabelValueBean>();

    public boolean isUpdate;

    /**
     * 初期表示を行います.<br>
     * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
     * @return 画面遷移先のURI文字列
     * @throws Exception
     */
	@Execute(validator = false)
	public String index() throws Exception {
		init();
		return StockAction.Mapping.INPUT;
	}

	/**
	 * 更新処理を行います.<br>
	 * 更新完了時および何かしらの問題があった場合に、画面にメッセージを表示します.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validate = "validate", validator = true, input = "init2")
	public String update() throws Exception {
		// 自社情報を更新する
		// 排他制御
		Mine mine = this.mineService.getMine();
		if (mine != null) {
			String nowUpdatetm = "";
			if( mine.updDatetm != null)	{
				nowUpdatetm = String.valueOf(mine.updDatetm);
			}
			if( stockForm.updDatetm.equals(nowUpdatetm) == false){
				super.messages.add(ActionMessages.GLOBAL_MESSAGE,	new ActionMessage("errors.exclusive.control.updated"));
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return StockAction.Mapping.INPUT;
			}
		}

		this.mineService.updateMineByStock(stockForm.stockHoldTermCalcCategory,
										Integer.parseInt(stockForm.stockHoldDays)*30,
										Integer.parseInt(stockForm.minPoLotCalcDays)*30,
										Integer.parseInt(stockForm.minPoLotNum),
										Integer.parseInt(stockForm.maxPoNumCalcDays)*30,
										Integer.parseInt(stockForm.minPoNum),
										Double.parseDouble(stockForm.deficiencyRate),
										Integer.parseInt(stockForm.maxEntrustPoTimelag));
		super.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("infos.stock.changed"));
		ActionMessagesUtil.addMessages(super.httpRequest, super.messages);

		init();
		return StockAction.Mapping.INPUT;

	}

	/**
	 * リセット処理を行います.<br>
	 * 処理実行後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String reset() throws Exception {
		init();
		return StockAction.Mapping.INPUT;
	}

	/**
	 * バリデートでエラーになった際の初期化処理を行います.<br>
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String init2() throws Exception{
		isUpdate = this.userDto.isMenuUpdate( Constants.MENU_ID.SETTING_STOCK );
		try{
			// 月平均出荷数の計算期間プルダルン設定
			stockHoldTermCalcCategoryList = categoryService.findCategoryLabelValueBeanListById(Categories.STOCK_HOLD_TERM_CALC);
		} catch (ServiceException e) {
			super.errorLog(e);
			super.httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return StockAction.Mapping.INPUT;
	}

	/**
	 * アクションフォームを初期化します.
	 * @throws Exception
	 */
	private void init() throws Exception {
		isUpdate = this.userDto.isMenuUpdate( Constants.MENU_ID.SETTING_STOCK );

		try{
			// 月平均出荷数の計算期間プルダルン設定
			stockHoldTermCalcCategoryList = categoryService.findCategoryLabelValueBeanListById(Categories.STOCK_HOLD_TERM_CALC);
		} catch (ServiceException e) {
			super.errorLog(e);
			super.httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		// 自社マスタの情報を取得する
		Mine mine = this.mineService.getMine();
		if (mine != null) {
			stockForm.stockHoldTermCalcCategory="";
			stockForm.stockHoldDays="";
			stockForm.minPoLotCalcDays="";
			stockForm.minPoLotNum="";
			stockForm.maxPoNumCalcDays="";
			stockForm.minPoNum="";
			stockForm.deficiencyRate = "";
			stockForm.maxEntrustPoTimelag = "";
			stockForm.updDatetm = "";

			if( mine.stockHoldTermCalcCategory !=null) {
				stockForm.stockHoldTermCalcCategory = mine.stockHoldTermCalcCategory;
			}
			if( mine.stockHoldDays != null ) {
				stockForm.stockHoldDays = String.valueOf(mine.stockHoldDays/30);
			}
			if( mine.minPoLotCalcDays != null ) {
				stockForm.minPoLotCalcDays = String.valueOf(mine.minPoLotCalcDays/30);
			}
			if( mine.minPoLotNum != null ){
				stockForm.minPoLotNum = String.valueOf(mine.minPoLotNum);
			}
			if( mine.maxPoNumCalcDays != null ){
				stockForm.maxPoNumCalcDays = String.valueOf(mine.maxPoNumCalcDays/30);
			}
			if( mine.minPoNum != null ){
				stockForm.minPoNum = String.valueOf(mine.minPoNum);
			}
			if( mine.updDatetm != null){
				stockForm.updDatetm = String.valueOf(mine.updDatetm);
			}
			if( mine.deficiencyRate != null ) {
				DecimalFormat df = NumberUtil.createDecimalFormat(mine.statsDecAlignment, false);
				stockForm.deficiencyRate = df.format(mine.deficiencyRate);
			}
			if( mine.maxEntrustPoTimelag != null ) {
				stockForm.maxEntrustPoTimelag = String.valueOf(mine.maxEntrustPoTimelag);
			}
		}
	}
}

