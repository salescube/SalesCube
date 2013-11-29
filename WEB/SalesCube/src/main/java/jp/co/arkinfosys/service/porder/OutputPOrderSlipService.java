/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.porder;

import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.entity.PoSlipTrn;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.framework.beans.util.BeanMap;
/**
 * 発注書発行サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class OutputPOrderSlipService extends AbstractService<PoSlipTrn> {

	private static class SQLParam {
		public static final String PO_SLIP_ID = "poSlipId";
	}

	/**
	 * 発注伝票を読込んで結果マップを返します.
	 * @param slipId 発注伝票番号
	 * @return 結果マップ
	 * @throws ServiceException
	 */
	public BeanMap getBeanMapPOrderSlipBySlipId(String slipId)
			throws ServiceException {
		try {
			// ドメイン名の取得
			Map<String, Object> param = super.createSqlParam();
			// 発注番号の設定
			param.put(SQLParam.PO_SLIP_ID, slipId);
			// クエリ
			BeanMap temp = this.selectBySqlFile(BeanMap.class,
					"porder/FindPOrderSlipByPOSlipIdWithCUnit.sql", param)
					.getSingleResult();
			return temp;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 発注伝票明細行を読込んで結果リストを返します.
	 * @param slipId 発注伝票番号
	 * @return 結果リスト
	 * @throws ServiceException
	 */
	public List<BeanMap> getBeanMapListPOrderLinesBySlipId(String slipId)
			throws ServiceException {
		try {
			// ドメイン名の取得
			Map<String, Object> param = super.createSqlParam();
			// 発注番号の設定
			param.put(SQLParam.PO_SLIP_ID, slipId);
			// クエリ
			return this.selectBySqlFile(BeanMap.class,
					"porder/FindPOrderLineByPOSlipIdWithCUnit.sql", param)
					.getResultList();
		} catch (SNonUniqueResultException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 伝票発行数を1増やします.
	 * @param slipId 発注伝票番号
	 * @throws Exception
	 */
	public void incrementSlipPrintCount(String slipId) throws Exception{
		Map<String, Object> param = super.createSqlParam();

		param.put(SQLParam.PO_SLIP_ID, slipId);
		if(this.updateBySqlFile(
				"porder/IncrementSlipPrintCount.sql", param).execute() != 1){
			throw new Exception();
		}
	}
}
