/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.stock;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractReportWriterAction;
import jp.co.arkinfosys.dto.stock.EntrustEadLineTrnDto;
import jp.co.arkinfosys.dto.stock.EntrustEadSlipTrnDto;
import jp.co.arkinfosys.form.stock.InputEntrustStockForm;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.stock.InputEntrustStockService;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 委託入出庫入力画面のPDF出力アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class OutputEntrustStockOrderAction extends AbstractReportWriterAction {
	@ActionForm
	@Resource
	public InputEntrustStockForm inputEntrustStockForm;

	@Resource
	protected InputEntrustStockService inputEntrustStockService;

	/**
	 * 帳票テンプレートID
	 */
	public static final String REPORT_ID = "0000X";

	/**
	 * ダウンロードファイル名定義
	 */
	public static final String FILE_PREFFIX = "EntrustDelivery";

	/**
	 * PDFファイルを出力します.
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String pdf() throws Exception {
		entrustEadSlipTrnDto = this.inputEntrustStockService
				.createEntrustEadSlipTrnDto(inputEntrustStockForm.entrustEadSlipId);

		// 出庫指示書印刷回数をインクリメントする
		inputEntrustStockService
				.incrementDispatchOrderPrintCount(entrustEadSlipTrnDto.entrustEadSlipId);

		return super.pdf();
	}

	private EntrustEadSlipTrnDto entrustEadSlipTrnDto;

	/**
	 * レポートテンプレートIDを返します.
	 * @param index 出力データのインデックス
	 * @return レポートテンプレートID
	 */
	@Override
	protected String getReportId(int index) {
		if (index != 0) {
			return null;
		}
		return REPORT_ID;
	}

	/**
	 * 拡張子を除くファイル名を返します.
	 * @return 拡張子を除くファイル名
	 */
	@Override
	protected String getFilePreffix() {
		return FILE_PREFFIX;
	}

	/**
	 * 伝票データを返します.
	 * @param index 出力データのインデックス
	 * @return 伝票データ
	 * @throws ServiceException
	 */
	@Override
	protected BeanMap getSlip(int index) throws ServiceException {
		if (index != 0) {
			return null;
		}

		BeanMap beanMap = new BeanMap();
		Beans.copy(entrustEadSlipTrnDto, beanMap).execute();

		return beanMap;
	}

	/**
	 * 明細データを返します.
	 * @param index 出力データのインデックス
	 * @return 明細行データ
	 * @throws ServiceException
	 */
	@Override
	protected List<BeanMap> getDetailList(int index) throws ServiceException {
		if (index != 0) {
			return null;
		}

		List<BeanMap> lbm = new ArrayList<BeanMap>();

		for (EntrustEadLineTrnDto entrustEadLineTrnDto : entrustEadSlipTrnDto.getLineDtoList()) {
			BeanMap beanMap = new BeanMap();
			Beans.copy(entrustEadLineTrnDto, beanMap).execute();
			lbm.add(beanMap);
		}

		return lbm;
	}
}
