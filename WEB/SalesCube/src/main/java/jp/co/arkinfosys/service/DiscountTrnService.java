/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.DiscountTrnDto;
import jp.co.arkinfosys.entity.DiscountTrn;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 割引データサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class DiscountTrnService extends AbstractService<DiscountTrn> {
	// サービス群
	@Resource
	private SeqMakerService seqMakerService;

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String DISCOUNT_DATA_ID = "discountDataId";
		public static final String DISCOUNT_ID = "discountId";
		public static final String LINE_NO = "lineNo";
		public static final String DATA_FROM = "dataFrom";
		public static final String DATA_TO = "dataTo";
		public static final String DISCOUNT_RATE = "discountRate";
	}

	/**
	 * 空の検索条件マップを作成します.
	 * @param param 検索条件マップ
	 * @return 検索条件キーのみ設定した検索条件マップ
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(Param.DISCOUNT_DATA_ID, null);
		param.put(Param.DISCOUNT_ID, null);
		param.put(Param.LINE_NO, null);
		param.put(Param.DATA_FROM, null);
		param.put(Param.DATA_TO, null);
		param.put(Param.DISCOUNT_RATE, null);
		return param;
	}

	/**
	 * 割引IDを指定して、割引情報のリストを返します.
	 * @param discountId 割引ID
	 * @return {@link DiscountTrnDto}のリスト
	 * @throws ServiceException
	 */
	public List<DiscountTrnDto> findDiscountTrnByDiscountId(String discountId)
			throws ServiceException {
		if (discountId == null) {
			return null;
		}

		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			// 割引ID
			param.put(DiscountService.Param.DISCOUNT_ID, discountId);
			return this.selectBySqlFile(DiscountTrnDto.class,
					"discounttrn/FindDiscountTrnByDiscountId.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 割引データを登録します.
	 * @param discountTrnDto 割引データDTO
	 * @return 発番した割引データID
	 * @throws ServiceException
	 */
	public long insertDiscountTrn(DiscountTrnDto discountTrnDto) throws ServiceException {
		if (discountTrnDto == null) {
			return -1;
		}
		try {
			// 登録
			Map<String, Object> param = super.createSqlParam();

			// 発番
			long discountDataId = seqMakerService.nextval(DiscountTrn.TABLE_NAME);
			discountTrnDto.discountDataId = Integer.valueOf((int) discountDataId);
			BeanMap discountTrnInfo = Beans.createAndCopy(BeanMap.class, discountTrnDto)
					.timestampConverter(Constants.FORMAT.TIMESTAMP)
					.dateConverter(Constants.FORMAT.DATE)
					.execute();


			param.putAll(discountTrnInfo);
			this.updateBySqlFile("discounttrn/InsertDiscountTrn.sql", param).execute();

			return discountDataId;
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * 割引データを更新します.
	 * @param discountTrnDto 割引データDTO
	 * @throws Exception
	 */
	public void updateDiscountTrn(DiscountTrnDto discountTrnDto) throws Exception {
		if (discountTrnDto == null) {
			return;
		}

		// 更新
		Map<String, Object> param = super.createSqlParam();
		BeanMap discountTrnInfo = Beans.createAndCopy(BeanMap.class, discountTrnDto)
				.timestampConverter(Constants.FORMAT.TIMESTAMP)
				.dateConverter(Constants.FORMAT.DATE)
				.execute();

		param.putAll(discountTrnInfo);
		this.updateBySqlFile("discounttrn/UpdateDiscountTrn.sql", param).execute();
	}

	/**
	 * 割引データIDを指定して、割引データを削除します.
	 * @param discountDataId 割引データID
	 * @throws Exception
	 */
	public void deleteDiscountTrnByDiscountDataId(String discountDataId)
			throws Exception {

		// 履歴を登録
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.DISCOUNT_DATA_ID, discountDataId);
		super.updateAudit(DiscountTrn.TABLE_NAME,
				new String[] { Param.DISCOUNT_DATA_ID },
				new Object[] { discountDataId });
		this.updateBySqlFile(
				"discounttrn/DeleteDiscountTrnByDiscountDataId.sql", param)
				.execute();
	}
}
