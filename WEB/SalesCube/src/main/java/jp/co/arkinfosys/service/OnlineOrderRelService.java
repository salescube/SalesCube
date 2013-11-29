/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.dto.rorder.ROrderLineDto;
import jp.co.arkinfosys.entity.OnlineOrderRel;
import jp.co.arkinfosys.entity.OnlineOrderWork;

/**
 * オンライン受注関連データサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class OnlineOrderRelService extends AbstractService<OnlineOrderRel> {

	@Resource
	public SeqMakerService seqMakerService;

	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String RO_SLIP_ID = "roSlipId";
		public static final String RO_LINE_ID = "roLineId";
		public static final String ONLINE_ORDER_ID = "onlineOrderId";
		public static final String ONLINE_ITEM_ID = "onlineItemId";
	}

	/**
	 * 受注伝票番号を指定して、オンライン受注関連データが存在するか否かを取得します.
	 * @param roSlipId 受注伝票番号
	 * @return オンライン受注関連データが存在するか否か
	 * @throws Exception
	 */
	public boolean hasRecordByROrderSlip(String roSlipId) throws Exception {
		List<OnlineOrderRel> list = this
				.findOnlineOrderRelByROrderSlip(roSlipId);
		if (list == null) {
			return false;
		}
		if (list.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 受注伝票番号を指定して、オンライン受注関連データを取得します.
	 * @param roSlipId 受注伝票番号
	 * @return オンライン受注関連データエンティティリスト
	 * @throws Exception
	 */
	public List<OnlineOrderRel> findOnlineOrderRelByROrderSlip(String roSlipId)
			throws Exception {
		Map<String, Object> param = super.createSqlParam();

		// 条件を追加
		param.put(Param.RO_SLIP_ID, roSlipId);

		return this.selectBySqlFile(OnlineOrderRel.class,
				"onlineorder/FindOnlineOrderRelByCondition.sql", param)
				.getResultList();
	}

	/**
	 * 受注伝票番号と受注伝票行IDを指定して、オンライン受注関連データを取得します.
	 * @param roSlipId 受注伝票番号
	 * @param roLineId 受注伝票行ID
	 * @return オンライン受注関連データエンティティリスト
	 * @throws Exception
	 */
	public List<OnlineOrderRel> findOnlineOrderRelByROrderLine(String roSlipId,
			String roLineId) throws Exception {
		Map<String, Object> param = super.createSqlParam();

		// 条件を追加
		param.put(Param.RO_SLIP_ID, roSlipId);
		param.put(Param.RO_LINE_ID, roLineId);

		return this.selectBySqlFile(OnlineOrderRel.class,
				"onlineorder/FindOnlineOrderRelByCondition.sql", param)
				.getResultList();
	}

	/**
	 * オンライン受注関連データを登録します.
	 * @param rorderLineDto 受注伝票明細行DTO
	 * @param work オンライン受注データDTO
	 * @throws Exception
	 */
	public void insertOnlineOrderRel(ROrderLineDto rorderLineDto,
			OnlineOrderWork work) throws Exception {
		Map<String, Object> param = super.createSqlParam();

		// 条件を追加
		param.put(Param.RO_SLIP_ID, rorderLineDto.roSlipId);
		param.put(Param.RO_LINE_ID, rorderLineDto.roLineId);
		param.put(Param.ONLINE_ORDER_ID, work.onlineOrderId);
		param.put(Param.ONLINE_ITEM_ID, work.onlineItemId);

		// 登録
		this.updateBySqlFile("onlineorder/InsertOnlineOrderRel.sql", param)
				.execute();
	}

	/**
	 * オンライン受注関連データを削除します.
	 * @param rorderLineDto 受注伝票明細行DTO
	 * @throws Exception
	 */
	public void deleteOnlineOrderRel(ROrderLineDto rorderLineDto)
			throws Exception {
		Map<String, Object> param = super.createSqlParam();

		// 条件を追加
		param.put(Param.RO_LINE_ID, rorderLineDto.roLineId);

		super.updateAudit(OnlineOrderRel.TABLE_NAME,
				new String[] { Param.RO_LINE_ID },
				new Object[] { rorderLineDto.roLineId });

		// 削除
		this.updateBySqlFile("onlineorder/DeleteOnlineOrderRel.sql", param)
				.execute();
	}
}
