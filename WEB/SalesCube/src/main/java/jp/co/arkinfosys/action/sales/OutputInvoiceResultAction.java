/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.sales;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchResultAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.sales.OutputInvoiceSearchResultDto;
import jp.co.arkinfosys.entity.join.SalesSlipTrnJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.sales.OutputInvoiceForm;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.SalesService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.sales.SearchOutputInvoiceService;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 送り状発行画面の検索結果をEXCEL出力するアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class OutputInvoiceResultAction
		extends
		AbstractSearchResultAction<OutputInvoiceSearchResultDto, SalesSlipTrnJoin> {

	@ActionForm
	@Resource
	private OutputInvoiceForm outputInvoiceForm;

	@Resource
	private SearchOutputInvoiceService searchOutputInvoiceService;

	@Resource
	private SalesService salesService;

	/**
	 * 送り状データ検索結果をExcel形式で出力します.<br>
	 * @return 画面遷移先のURI文字列
	 */
	@Execute(validator = true, validate = "validate", input = Mapping.EXCEL)
	public String excel() throws Exception {
		return super.doSearch();
	}

	/**
	 * 検索結果件数を返します.<br>
	 * 使用しないため、必ず0を返します.
	 * @param params パラメータを設定したマップ
	 * @return 0固定
	 * @throws ServiceException
	 */
	@Override
	protected int doCount(BeanMap params) throws ServiceException {
		return 0;
	}

	/**
	 * 検索条件に応じて売上伝票を検索します.<br>
	 * @param params 検索パラメータを設定したマップ
	 * @param sortColumn ソート対象カラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param rowCount 取得する検索件数
	 * @param offset 取得開始位置
	 * @return 検索結果のリスト
	 * @throws ServiceException
	 */
	@Override
	protected List<SalesSlipTrnJoin> execSearch(BeanMap params,
			String sortColumn, boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		return this.searchOutputInvoiceService
				.getOutputInvoiceList(this.outputInvoiceForm.salesIdList);
	}

	/**
	 * SalesSlipTrnJoin形式のエンティティから、OutputInvoiceSearchResultDto形式のDTOに変換します.<br>
	 * 金額項目書式を変換します.<BR>
	 * @param entityList SalesSlipTrnJoin形式のENTITYリスト
	 * @return OutputInvoiceSearchResultDto形式のリスト
	 * @throws Exception
	 */
	@Override
	protected List<OutputInvoiceSearchResultDto> exchange(
			List<SalesSlipTrnJoin> entityList) throws Exception {
		List<OutputInvoiceSearchResultDto> resultList = new ArrayList<OutputInvoiceSearchResultDto>();
		for (SalesSlipTrnJoin entity : entityList) {
			OutputInvoiceSearchResultDto dto = Beans.createAndCopy(
					OutputInvoiceSearchResultDto.class, entity)
					.numberConverter("#", "PRICE_TOTAL", "CTAX_PRICE_TOTAL")
					.execute();

			// リストに追加
			resultList.add(dto);
		}

		this.createOutputData(resultList);

		return resultList;
	}

	/**
	 * {@link AbstractSearchResultAction#doSearch()}検索後に必要な処理を行います.<br>
	 * 送り状データを発行した売上伝票の出力済フラグを更新します.<br>
	 * @throws Exception
	 */
	@Override
	protected void doAfterSearch() throws Exception {
		this.updatePrintCount();
	}

	/**
	 * 出力用にデータを加工します.<BR>
	 */
	private void createOutputData(List<OutputInvoiceSearchResultDto> list) {
		for (OutputInvoiceSearchResultDto dto : list) {
			dto.createOutputData(super.mineDto);
		}
	}

	/**
	 * 送り状データを発行した売上伝票を更新します.
	 *
	 * @throws ServiceException
	 */
	private void updatePrintCount() throws ServiceException {
		for (String salesId : outputInvoiceForm.salesIdList) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(Constants.REPORT_TEMPLATE.SI);

			salesService.updatePrintCount(salesId, list);
		}
	}

	/**
	 * 送り状データ出力用アクションフォームを返します.
	 * @return OutputInvoiceForm形式のアクションフォーム
	 */
	@Override
	protected AbstractSearchForm<OutputInvoiceSearchResultDto> getActionForm() {
		return this.outputInvoiceForm;
	}

	/**
	 * OutputInvoiceSearchResultDtoクラスを返します.
	 * @return OutputInvoiceSearchResultDtoクラス
	 */
	@Override
	protected Class<OutputInvoiceSearchResultDto> getDtoClass() {
		return OutputInvoiceSearchResultDto.class;
	}

	/**
	 * 送り状データ出力正常終了時の画面遷移先URIを返します.
	 * @return 画面遷移先のURI文字列
	 */
	@Override
	protected String getResultURIString() {
		return Mapping.EXCEL;
	}

	/**
	 * 送り状データ出力画面のメニューIDを返します.
	 * @return 送り状データ出力画面メニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.OUTPUT_SALES_INVOICE;
	}

	/**
	 * 検索サービスを返します.<BR>
	 * 使用されていないので常にnullを返します.<BR>
	 * @return null固定
	 */
	@Override
	protected MasterSearch<SalesSlipTrnJoin> getService() {
		return null;
	}

}
