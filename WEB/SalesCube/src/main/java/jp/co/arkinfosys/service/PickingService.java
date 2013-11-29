/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.sales.SalesLineDto;
import jp.co.arkinfosys.dto.sales.SalesSlipDto;
import jp.co.arkinfosys.entity.PickingLine;
import jp.co.arkinfosys.entity.PickingList;
import jp.co.arkinfosys.s2extend.NumberConverter;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 出荷指示書サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class PickingService extends AbstractService<PickingList> {

	//発番用サービス
	@Resource
	private SeqMakerService seqMakerService;

	// 出荷指示書明細行用サービス
	@Resource
	private PickingLineService pickingLineService;

	/**
	 * SQLファイルのパラメータ名定義
	 */
	public static class Param {
		// ソート方向
		private static final String SORT_ORDER = "sortOrder";
		// 取得件数
		private static final String ROW_COUNT = "rowCount";
		// 取得件数
		private static final String OFFSET_ROW = "offsetRow";

		public static final String PICKING_LIST_ID = "pickingListId";
		public static final String RO_SLIP_ID = "roSlipId";
		public static final String SALES_SLIP_ID = "salesSlipId";
		public static final String CUSTOMER_CODE = "customerCode";
		// 請求締日のソート条件
		private static final String SORT_COLUMN_CUSTOMER_CODE = "sortColumnCustomerCode";

	}

	public String[] paramNames = { Param.SORT_ORDER, Param.ROW_COUNT,
			Param.OFFSET_ROW, Param.PICKING_LIST_ID, Param.RO_SLIP_ID,
			Param.SALES_SLIP_ID, Param.SORT_COLUMN_CUSTOMER_CODE };

	public static final String SORT_COLUMN_CUSTOMER_CODE = "CUSTOMER_CODE";

	/**
	 * エンティティ情報から登録用Mapオブジェクトを生成します.
	 * @param pl　{@link PickingList}
	 * @return　登録用マップ
	 */
	private Map<String, Object> createParamMap(PickingList pl) {

		//MAPの生成
		Map<String, Object> param = new HashMap<String, Object>();

		//アクションフォームの情報をPUT
		BeanMap AFparam = Beans.createAndCopy(BeanMap.class, pl).execute();
		param.putAll(AFparam);

		//更新日時とかPUT
		Map<String, Object> CommonParam = super.createSqlParam();
		param.putAll(CommonParam);

		return param;
	}

	/**
	 * 出荷指示書番号を発番します.
	 * @return　出荷指示書番号
	 * @throws Exception
	 */
	public Long getNextVal() throws Exception {

		Long newSlipId = -1L;
		//伝票番号の発番
		try {
			newSlipId = seqMakerService.nextval(PickingList.TABLE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return newSlipId;
	}

	/**
	 * 処理対象伝票をロックします.<BR>
	 * ロック不可の場合には例外を投げます.
	 * @param dto　対象伝票({@link SalesSlipDto})
	 * @throws ServiceException　
	 * @throws UnabledLockException
	 */
	protected void isLocked(SalesSlipDto dto) throws ServiceException,
			UnabledLockException {
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.SALES_SLIP_ID, dto.salesSlipId);
		param
				.put(AbstractService.Param.LOCK_RECORD,
						AbstractService.FOR_UPDATE);
		lockRecordBySqlFile("picking/FindPickingList.sql", param, dto.updDatetm);
	}

	/**
	 * DTOからエンティティへ変換します.
	 * @param unitFract　単価端数処理区分
	 * @param taxFract　税端数処理区分
	 * @param dto　{@link SalesSlipDto}
	 * @return　{@link PickingList}
	 */
	public PickingList createAndCopy(String unitFract, String taxFract,
			SalesSlipDto dto) {
		NumberConverter convUP = createUnitPriceConverter(unitFract);
		NumberConverter convTax = createUnitPriceConverter(taxFract);
		PickingList pl = Beans.createAndCopy(PickingList.class, dto).converter(
				convUP, "priceTotal").converter(convTax, "ctaxPriceTotal")
				.dateConverter(Constants.FORMAT.DATE, "roDate").dateConverter(
						Constants.FORMAT.TIMESTAMP, "printDate", "creDatetm",
						"updDatetm").execute();
		return pl;
	}

	/**
	 * エンティティからDTOへの変換します.
	 * @param unitFract　単価端数処理区分
	 * @param taxFract　税端数処理区分
	 * @param ss　{@link PickingList}
	 * @return　{@link SalesSlipDto}
	 */
	public SalesSlipDto createAndCopy(String unitFract, String taxFract,
			PickingList ss) {
		NumberConverter convUP = createUnitPriceConverter(unitFract);
		NumberConverter convTax = createUnitPriceConverter(taxFract);
		return Beans.createAndCopy(SalesSlipDto.class, ss).converter(convUP,
				"priceTotal").converter(convTax, "ctaxPriceTotal")
				.dateConverter(Constants.FORMAT.DATE, "roDate").dateConverter(
						Constants.FORMAT.TIMESTAMP, "printDate", "creDatetm",
						"updDatetm").execute();
	}

	/**
	 * DTOをエンティティへコピーします.
	 * @param unitFract　単価端数処理区分
	 * @param taxFract　税端数処理区分
	 * @param dto {@link SalesSlipDto}
	 * @param ss {@link PickingList}
	 */
	public void copy(String unitFract, String taxFract, SalesSlipDto dto,
			PickingList ss) {
		NumberConverter convUP = createUnitPriceConverter(unitFract);
		NumberConverter convTax = createUnitPriceConverter(taxFract);
		Beans.copy(dto, ss).converter(convUP, "priceTotal").converter(convTax,
				"ctaxPriceTotal")
				.dateConverter(Constants.FORMAT.DATE, "roDate").dateConverter(
						Constants.FORMAT.TIMESTAMP, "printDate", "creDatetm",
						"updDatetm").execute();
	}

	/**
	 * エンティティをDTOへコピーします.
	 * @param unitFract　単価端数処理区分
	 * @param taxFract　税端数処理区分
	 * @param ss {@link PickingList}
	 * @param dto {@link SalesSlipDto}
	 */
	public void copy(String unitFract, String taxFract, PickingList ss,
			SalesSlipDto dto) {
		NumberConverter convUP = createUnitPriceConverter(unitFract);
		NumberConverter convTax = createUnitPriceConverter(taxFract);
		Beans.copy(ss, dto).converter(convUP, "priceTotal").converter(convTax,
				"ctaxPriceTotal")
				.dateConverter(Constants.FORMAT.DATE, "roDate").dateConverter(
						Constants.FORMAT.TIMESTAMP, "printDate", "creDatetm",
						"updDatetm").execute();
	}

	/**
	 * 売上明細情報を元に、ピッキング明細をDBに登録します.<BR>
	 * エンティティの内容は、ほぼ空なので注意して下さい.<BR>
	 * SQL内で売上明細情報を取得して空欄を埋めています.
	 * @param pl {@link PickingList}
	 * @return　実行行数
	 */
	public int insert(PickingList pl) {
		//SQLクエリを投げる
		return this.updateBySqlFile("picking/InsertPickingListBySales.sql",
				createParamMap(pl)).execute();
	}

	/**
	 * エンティティ情報で出荷指示書テーブルを更新します.
	 * @param pl {@link PickingList}
	 * @return　実行行数
	 *
	 */
	public int update(PickingList pl) {
		//SQLクエリを投げる
		return this.updateBySqlFile("picking/UpdatePickingListBySales.sql",
				createParamMap(pl)).execute();
	}

	/**
	 * エンティティ情報で出荷指示書テーブルからレコードを削除します.
	 * @param pl {@link PickingList}
	 * @return　実行行数
	 *
	 */
	public int delete(PickingList pl) {
		super.updateAudit(PickingList.TABLE_NAME,
				new String[] { Param.SALES_SLIP_ID },
				new Object[] { pl.salesSlipId });
		return this.updateBySqlFile("picking/DeletePickingListBySales.sql",
				createParamMap(pl)).execute();
	}

	/**
	 *　売上伝票番号を指定して出荷指示書を取得します.
	 * @param salesSlipId 売上伝票番号
	 * @return {@link PickingList}のリスト
	 * @throws ServiceException
	 */
	public List<PickingList> findPickingListBySalesSlipId(String salesSlipId)
			throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.SALES_SLIP_ID, salesSlipId);

		return findByCondition(conditions, paramNames,
				"picking/FindPickingList.sql");

	}

	/**
	 *　売上伝票番号を指定して出荷指示書を取得します.
	 * @param salesSlipId 売上伝票番号
	 * @return 出荷指示書をBeanMapに入れたもの
	 * @throws ServiceException
	 */
	public BeanMap findPickingListBySalesSlipIdSimple(String salesSlipId)
			throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.SALES_SLIP_ID, salesSlipId);

		List<PickingList> pickList = findByCondition(conditions, paramNames,
				"picking/FindPickingList.sql");
		if (pickList.size() == 1) {
			return Beans.createAndCopy(BeanMap.class, pickList.get(0))
					.execute();
		}
		return null;
	}

	/**
	 * 指定された伝票の内容をDBに登録します.<BR>
	 * 明細行も追加します.
	 * @param dto　{@link SalesSlipDto}
	 * @throws Exception
	 */
	public void insert(SalesSlipDto dto) throws Exception {
		Long newSlipId = -1L;
		try {
			// 伝票番号の発番
			newSlipId = getNextVal();

			PickingList pl = new PickingList();
			pl.pickingListId = Integer.parseInt(newSlipId.toString());
			pl.salesSlipId = Integer.parseInt(dto.salesSlipId);

			// 明細行の追加
			List<SalesLineDto> salesLineList = dto.getLineDtoList();
			for (SalesLineDto lineDto : salesLineList) {

				// 入力内容が存在する行だけを登録対象とする
				if (lineDto.isBlank() == false) {
					// 伝票番号の発番
					Long newLineId = pickingLineService.getNextVal();

					PickingLine pll = new PickingLine();
					pll.pickingLineId = Integer.parseInt(newLineId.toString());
					pll.pickingListId = Integer.parseInt(newSlipId.toString());
					pll.salesLineId = Integer.parseInt(lineDto.salesLineId);

					// ピッキングリストは売上伝票をもとに作成するのでDTOは使用しない
					if (pickingLineService.insert(pll) == 0) {
						throw new ServiceException("errors.system");
					}
				}
			}

			// 伝票の追加
			if (insert(pl) == 0) {
				throw new ServiceException("errors.system");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 指定された伝票の内容をDBに更新します.<BR>
	 * 明細行も更新します.
	 *
	 * @param dto　{@link SalesSlipDto}
	 * @throws Exception
	 */
	public void update(SalesSlipDto dto) throws Exception {
		// 排他制御
		isLocked(dto);

		// 明細エンティティの取得
		List<PickingLine> plList = pickingLineService
				.findPickingLineByPickingListId(dto.pickingListId);

		// 明細行の更新
		List<SalesLineDto> salesLineList = dto.getLineDtoList();
		for (SalesLineDto lineDto : salesLineList) {

			// 入力内容が存在する行だけを登録対象とする
			if (lineDto.isBlank()) {
				continue;
			}

			// 伝票の情報を明細に複写
			lineDto.salesSlipId = dto.salesSlipId;

			PickingLine pl = pickingLineService.createAndCopy(
					dto.priceFractCategory, dto.taxFractCategory, lineDto);

			boolean bExist = false;
			// 明細行が存在する場合
			for (PickingLine tmpSl : plList) {
				if (tmpSl.salesLineId.equals(pl.salesLineId)) {
					bExist = true;
					break;
				}
			}
			if (bExist == true) {
				// 存在したら常に更新
				if (pickingLineService.update(pl) == 0) {
					throw new ServiceException("errors.system");
				}
			}
		}
		// 明細行の削除
		for (PickingLine tmpPl : plList) {

			boolean bExist = false;

			// 明細行が存在する場合
			for (SalesLineDto lineDto : salesLineList) {

				// 入力内容が存在する行だけを登録対象とする
				if (lineDto.isBlank()) {
					continue;
				}
				PickingLine pl = pickingLineService.createAndCopy(
						dto.priceFractCategory, dto.taxFractCategory, lineDto);

				if (tmpPl.salesLineId.equals(pl.salesLineId)) {
					bExist = true;
					break;
				}
			}
			if (bExist == false) {
				// 明細行が存在しない場合、画面から削除されているのでレコードも削除
				if (pickingLineService.delete(tmpPl) == 0) {
					throw new ServiceException("errors.system");
				}
			}
		}

		// ActionFormをエンティティに変換
		PickingList ps = createAndCopy(dto.priceFractCategory,
				dto.taxFractCategory, dto);

		// 伝票の更新
		if (update(ps) == 0) {
			throw new ServiceException("errors.system");
		}

	}

	/**
	 * 指定された伝票の内容をDBから削除します.<BR>
	 * 明細行も削除します.
	 *
	 * @param dto　{@link SalesSlipDto}
	 * @throws Exception
	 */
	public void delete(SalesSlipDto dto) throws Exception {
		try {
			// 排他制御
			isLocked(dto);

			// 明細行の更新
			List<SalesLineDto> salesLineList = dto.getLineDtoList();
			for (SalesLineDto lineDto : salesLineList) {
				PickingLine pl = pickingLineService.createAndCopy(
						dto.priceFractCategory, dto.taxFractCategory, lineDto);
				// 明細行の削除
				if (pickingLineService.delete(pl) == 0) {
					throw new ServiceException("errors.system");
				}
			}
			// ActionFormをエンティティに変換
			PickingList ps = createAndCopy(dto.priceFractCategory,
					dto.taxFractCategory, dto);

			// 伝票の削除
			if (delete(ps) == 0) {
				throw new ServiceException("errors.system");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * ピッキングリスト登録用エンティティを作成します.
	 * @param dto {@link SalesSlipDto}
	 * @return　登録に使用する{@link PickingList}
	 * @throws Exception
	 */
	public PickingList createPickingList(SalesSlipDto dto) throws Exception {
		Long newSlipId = getNextVal();

		PickingList pl = new PickingList();
		pl.pickingListId = Integer.parseInt(newSlipId.toString());
		pl.salesSlipId = Integer.parseInt(dto.salesSlipId);
		return pl;
	}
}
