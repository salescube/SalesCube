/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.dto.stock.EntrustEadLineTrnDto;
import jp.co.arkinfosys.dto.stock.EntrustEadSlipTrnDto;
import jp.co.arkinfosys.entity.EntrustEadLineTrn;
import jp.co.arkinfosys.entity.EntrustEadSlipTrn;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.AbstractSlipService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.EntrustEadService;
import jp.co.arkinfosys.service.PoSlipService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.SeqMakerService;
import jp.co.arkinfosys.service.YmService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.Converter;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 委託入出庫入力サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class InputEntrustStockService extends AbstractSlipService<EntrustEadSlipTrn, EntrustEadSlipTrnDto> {
	/**
	 * パラメータ定義クラスです.
	 */
	public static class Param {
		public static final String ENTRUST_EAD_SLIP_ID = "entrustEadSlipId";
		public static final String ENTRUST_EAD_DATE = "entrustEadDate";
		public static final String ENTRUST_EAD_DATE_FROM = "entrustEadDateFrom";
		public static final String ENTRUST_EAD_DATE_TO = "entrustEadDateTo";
		public static final String ENTRUST_EAD_ANNUAL = "entrustEadAnnual";
		public static final String ENTRUST_EAD_MONTHLY = "entrustEadMonthly";
		public static final String ENTRUST_EAD_YM = "entrustEadYm";
		public static final String USER_ID = "userId";
		public static final String USER_NAME = "userName";
		public static final String ENTRUST_EAD_CATEGORY = "entrustEadCategory";
		public static final String ENTRUST_EAD_CATEGORY_ENTER = "entrustEadCategoryEnter";
		public static final String ENTRUST_EAD_CATEGORY_DISPATCH = "entrustEadCategoryDispatch";
		public static final String ENTRUST_EAD_CATEGORY_DISPATCH_NO_PRINT = "entrustEadCategoryDispatchNoPrint";
		public static final String REMARKS = "remarks";
		public static final String PO_SLIP_ID = "poSlipId";
		public static final String DISPATCH_ORDER_PRINT_COUNT = "dispatchOrderPrintCount";
		public static final String ENTRUST_EAD_LINE_ID = "entrustEadLineId";
		public static final String LINE_NO = "lineNo";
		public static final String PRODUCT_CODE = "productCode";
		public static final String PRODUCT_ABSTRACT = "productAbstract";
		public static final String QUANTITY = "quantity";
		public static final String PO_LINE_ID = "poLineId";
		public static final String REL_ENTRUST_EAD_LINE_ID = "relEntrustEadLineId";
		public static final String REL_ENTRUST_EAD_SLIP_ID = "relEntrustEadSlipId";

		public static final String ENTRUST_EAD_LINE_ID_NO = "entrustEadLineIdNo";
		public static final String PO_LINE_ID_NO = "poLineIdNo";

		public static final String SUPPLIER_CODE = "supplierCode";
		public static final String SUPPLIER_NAME = "supplierName";
		public static final String PRODUCT1 = "product1";
		public static final String PRODUCT2 = "product2";
		public static final String PRODUCT3 = "product3";

		public static final String ROW_COUNT = "rowCount";
		public static final String OFFSET_ROW = "offsetRow";

		public static final String SORT_ORDER_ASC = "sortOrderAsc";
		public static final String SORT_ORDER = "sortOrder";
		public static final String SORT_COLUMN = "sortColumn";
	}

    /**
	 * テーブル定義クラスです.
	 */
	public static class Table {
		/** テーブル名：入出庫伝票 */
		public static final String ENTRUST_EAD_SLIP_TRN = "ENTRUST_EAD_SLIP_TRN";
		/** テーブル名：入出庫伝票明細 */
		public static final String ENTRUST_EAD_LINE_TRN = "ENTRUST_EAD_LINE_TRN";
	}

    @Resource
    protected CategoryService categoryService;

	@Resource
	private SeqMakerService seqMakerService;

    @Resource
    protected PoSlipService poSlipService;

    @Resource EntrustEadService entrustEadService;

	@Resource
	protected YmService ymService;

	@Resource
	protected ProductService productService;

	/**
	 * 入力された入出庫日を元に、年月度系のカラムを一括計算して返します.
	 * @param entrustEadSlipTrnDto 委託入出庫伝票DTO
	 * @param entrustEadDate 入出庫日
	 * @return 委託入出庫伝票DTO
	 * @throws ServiceException
	 */
	private EntrustEadSlipTrnDto calcYmComuns(EntrustEadSlipTrnDto entrustEadSlipTrnDto, String entrustEadDate) throws ServiceException {
		YmDto ymDto = ymService.getYm(entrustEadDate);
		if(ymDto == null) {
			ServiceException se = new ServiceException(
					MessageResourcesUtil.getMessage("errors.system"));
			se.setStopOnError(true);
			throw se;
		}
		entrustEadSlipTrnDto.entrustEadAnnual = ymDto.annual.toString();
		entrustEadSlipTrnDto.entrustEadMonthly = ymDto.monthly.toString();
		entrustEadSlipTrnDto.entrustEadYm = ymDto.ym.toString();

		return entrustEadSlipTrnDto;
	}

	/**
	 * 委託出庫指示書印刷回数をインクリメントします.
	 *
	 * @param entrustEadSlipId 委託入出庫伝票番号
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	public void incrementDispatchOrderPrintCount( String entrustEadSlipId )
			throws ServiceException, UnabledLockException {
		try {
			// 伝票の更新
			entrustEadService.incrementDispatchOrderPrintCount(entrustEadSlipId);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 委託入出庫伝票を削除します.<br>
	 * 委託入出庫伝票の削除と発注伝票の更新を行います.
	 *
	 * @param entrustEadSlipId 委託入出庫伝票番号
	 * @return 削除件数
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	@Override
	public int deleteById(String entrustEadSlipId, String updDatetm) throws ServiceException,
			UnabledLockException {
		try {
			// 排他制御
			int lockResult = this.lockRecord(EntrustEadService.Param.ENTRUST_EAD_SLIP_ID, entrustEadSlipId, updDatetm, "entrustead/LockSlipByEntrustEadSlipId.sql");

			// 最新の状態を取得する
			EntrustEadSlipTrnDto entrustEadSlipTrnDto = createEntrustEadSlipTrnDto(entrustEadSlipId);
			if(entrustEadSlipTrnDto == null) {
				ServiceException se = new ServiceException(
						MessageResourcesUtil.getMessage("errors.system"));
				se.setStopOnError(true);
				throw se;
			}

			// 伝票の削除
			deleteSlipByEntrustEadSlipId(Integer.parseInt(entrustEadSlipId));

			// 発注伝票の状態を変更する(更新済みの明細状態を再集計してメソッド内で自動的に伝票状態を判別し、設定される)
			poSlipService.updatePOrderTrnStatusByPoSlipId(entrustEadSlipTrnDto.poSlipId);

			return lockResult;

		} catch (ServiceException e) {
			throw e;
		} catch (UnabledLockException e) {
			throw e;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 委託入出庫入力伝票を登録します.<br>
	 * 戻り値は固定値「0」を返します.
	 *
	 * @param entrustEadSlipTrnDto 委託入出庫伝票DTO
	 * @return 0
	 * @throws ServiceException
	 */
	@Override
	protected int insertRecord(EntrustEadSlipTrnDto entrustEadSlipTrnDto)
			throws ServiceException {
		try {

			// 入出庫伝票の処理
			// 入出庫伝票番号を採番
			entrustEadSlipTrnDto.entrustEadSlipId = Long.toString(seqMakerService
					.nextval(EntrustEadService.Table.ENTRUST_EAD_SLIP_TRN));

			// 入出庫年度、月度、年月度を計算
			entrustEadSlipTrnDto = calcYmComuns(entrustEadSlipTrnDto, entrustEadSlipTrnDto.entrustEadDate);

			// 未印刷状態で初期化する
			entrustEadSlipTrnDto.dispatchOrderPrintCount = "0";

			// Insert
			insertSlip(entrustEadSlipTrnDto);

			return 0;

		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 委託入出庫伝票DTOを返します.
	 * @param id 委託入出庫伝票番号
	 * @return 委託入出庫伝票DTO
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	@Override
	public EntrustEadSlipTrnDto loadBySlipId(String id)
			throws ServiceException, UnabledLockException {
		return createEntrustEadSlipTrnDto(id);
	}

	/**
	 * 委託入出庫伝票の新規登録・更新処理を行います.
	 *
	 * @param dto 委託入出庫伝票DTO
	 * @param abstractServices サービスリスト
	 * @return ロック結果
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	@Override
	public int save(EntrustEadSlipTrnDto dto,
			AbstractService<?>... abstractServices) throws ServiceException,
			UnabledLockException {

		if (dto.entrustEadSlipId == null || dto.entrustEadSlipId.length() == 0) {
			return insertRecord(dto);
		}
		return updateRecord(dto);
	}

	/**
	 * 委託入出庫伝票を更新します.
	 * @param entrustEadSlipTrnDto 委託入出庫伝票DTO
	 * @return ロック結果
	 * @throws UnabledLockException
	 * @throws ServiceException
	 */
	@Override
	protected int updateRecord(EntrustEadSlipTrnDto entrustEadSlipTrnDto)
			throws UnabledLockException, ServiceException {
		try {
			// 入出庫年度、月度、年月度を計算
			entrustEadSlipTrnDto = calcYmComuns(entrustEadSlipTrnDto, entrustEadSlipTrnDto.entrustEadDate);

			EntrustEadSlipTrn entrustEadSlipTrn = Beans.createAndCopy(EntrustEadSlipTrn.class,
					entrustEadSlipTrnDto).execute();

			// 排他制御
			int lockResult =this.lockRecord(EntrustEadService.Param.ENTRUST_EAD_SLIP_ID, entrustEadSlipTrnDto.entrustEadSlipId,
												entrustEadSlipTrnDto.updDatetm, "entrustead/LockSlipByEntrustEadSlipId.sql");
			// 伝票の更新
			updateSlipByEntrustEadSlipId(entrustEadSlipTrn);

			return lockResult;

		} catch (ServiceException e) {
			throw e;
		} catch (UnabledLockException e) {
			throw e;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 委託入出庫伝票を登録します.<br>
	 * 発注伝票の更新も行います.
	 * @param entrustEadSlipTrnDto 委託入出庫伝票DTO
	 * @throws ServiceException
	 */
	public void insertSlip(EntrustEadSlipTrnDto entrustEadSlipTrnDto)
			throws ServiceException {
		try {
			// 入出庫伝票の処理
			EntrustEadSlipTrn entrustEadSlipTrn = Beans.createAndCopy(EntrustEadSlipTrn.class,
					entrustEadSlipTrnDto).execute();

			// Insert
			insertSlip(entrustEadSlipTrn);

			// 発注伝票の状態を変更する(更新済みの明細状態を再集計してメソッド内で自動的に伝票状態を判別し、設定される)
			poSlipService.updatePOrderTrnStatusByPoSlipId(entrustEadSlipTrnDto.poSlipId);

		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 委託入出庫伝票を登録します.
	 * @param entrustEadSlipTrn 委託入出庫伝票エンティティ
	 * @return 登録件数
	 * @throws ServiceException
	 */
	public int insertSlip(EntrustEadSlipTrn entrustEadSlipTrn) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = createSlipSqlParam(entrustEadSlipTrn);
			return this.updateBySqlFile("entrustead/InsertSlip.sql", param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 伝票作成時のSQLパラメータを作成します.
	 * @param entrustEadSlipTrn 委託入出庫伝票エンティティ
	 * @return 伝票作成時のSQLパラメータ
	 */
	protected Map<String, Object> createSlipSqlParam(EntrustEadSlipTrn entrustEadSlipTrn) {
		Map<String, Object> param = super.createSqlParam();
		param.put(EntrustEadService.Param.ENTRUST_EAD_SLIP_ID, entrustEadSlipTrn.entrustEadSlipId);
		param.put(EntrustEadService.Param.ENTRUST_EAD_DATE, entrustEadSlipTrn.entrustEadDate);
		param.put(EntrustEadService.Param.ENTRUST_EAD_ANNUAL, entrustEadSlipTrn.entrustEadAnnual);
		param.put(EntrustEadService.Param.ENTRUST_EAD_MONTHLY, entrustEadSlipTrn.entrustEadMonthly);
		param.put(EntrustEadService.Param.ENTRUST_EAD_YM, entrustEadSlipTrn.entrustEadYm);
		param.put(EntrustEadService.Param.USER_ID, entrustEadSlipTrn.userId);
		param.put(EntrustEadService.Param.USER_NAME, entrustEadSlipTrn.userName);
		param.put(EntrustEadService.Param.ENTRUST_EAD_CATEGORY, entrustEadSlipTrn.entrustEadCategory);
		param.put(EntrustEadService.Param.REMARKS, entrustEadSlipTrn.remarks);
		param.put(EntrustEadService.Param.PO_SLIP_ID, entrustEadSlipTrn.poSlipId);
		param.put(EntrustEadService.Param.DISPATCH_ORDER_PRINT_COUNT, entrustEadSlipTrn.dispatchOrderPrintCount);
		return param;
	}

	/**
	 * 委託入出庫伝票を更新します.
	 * @param entrustEadSlipTrn 委託入出庫伝票エンティティ
	 * @return 更新件数
	 * @throws ServiceException
	 */
	public int updateSlipByEntrustEadSlipId(
			EntrustEadSlipTrn entrustEadSlipTrn ) throws ServiceException {

		try {
			// SQLパラメータを構築する
			Map<String, Object> param = createSlipSqlParam(entrustEadSlipTrn);
			return this.updateBySqlFile("entrustead/UpdateSlip.sql", param).execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 委託入出庫伝票を削除します.
	 * @param entrustEadSlipId 委託入出庫伝票番号
	 * @return 削除件数
	 * @throws ServiceException
	 */
	public int deleteSlipByEntrustEadSlipId(Integer entrustEadSlipId) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(EntrustEadService.Param.ENTRUST_EAD_SLIP_ID, entrustEadSlipId);

			return this.updateBySqlFile("entrustead/DeleteSlipByEntrustEadSlipId.sql", param)
					.execute();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}
	/**
	 * 伝票番号から委託入出庫伝票DTOを作成します.
	 * @param entrustEadSlipId 委託入出庫伝票番号
	 * @return 委託入出庫伝票DTO
	 * @throws ServiceException
	 */
	public EntrustEadSlipTrnDto createEntrustEadSlipTrnDto(String entrustEadSlipId)
			throws ServiceException {
		try {
			// 入力された入出庫番号が数値であるかチェック
			try {
				Integer.valueOf(entrustEadSlipId);
			} catch (NumberFormatException e) {
				return null;
			}

			// TrnオブジェクトをDBから取得する
			EntrustEadSlipTrn entrustEadSlipTrn = entrustEadService.findSlipByEntrustEadSlipId(Integer.valueOf(entrustEadSlipId));
			List<EntrustEadLineTrn> eadLineTrnList = entrustEadService.findLineByEntrustEadSlipId(Integer.valueOf(entrustEadSlipId));
			if (entrustEadSlipTrn == null
					|| (eadLineTrnList == null || eadLineTrnList.size() == 0)) {
				// 伝票または明細を取得できなかった場合はnullを返す
				return null;
			}

			// TrnDtoオブジェクトをTrnオブジェクトから生成する
			EntrustEadSlipTrnDto entrustEadSlipTrnDto = Beans.createAndCopy(
					EntrustEadSlipTrnDto.class, entrustEadSlipTrn).dateConverter(
									Constants.FORMAT.TIMESTAMP, "updDatetm").execute();

			List<EntrustEadLineTrnDto> entrustEadLineTrnDtoList = new ArrayList<EntrustEadLineTrnDto>();
			Converter conv = createProductNumConverter();
			for (EntrustEadLineTrn eadLineTrn : eadLineTrnList) {
				// 画面表示用のDTOへ変換する
				EntrustEadLineTrnDto entrustEadLineTrnDto = Beans.createAndCopy(
						EntrustEadLineTrnDto.class, eadLineTrn).converter(conv, EntrustEadService.Param.QUANTITY).execute();

				// 商品備考の取得
				ProductJoin pj = productService.findById(eadLineTrn.productCode);
				if( pj != null ) {
					entrustEadLineTrnDto.productRemarks = pj.remarks;
				}

				entrustEadLineTrnDtoList.add(entrustEadLineTrnDto);
			}
			entrustEadSlipTrnDto.setLineDtoList(entrustEadLineTrnDtoList) ;

			return entrustEadSlipTrnDto;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 商品コードから商品情報を取得します.
	 *
	 * @param productCode 商品コード
	 * @return 商品情報エンティティ
	 * @throws ServiceException
	 */
	public ProductJoin findProductByCode(String productCode)
			throws ServiceException {
		return productService.findById(productCode);
	}

	/**
	 * 入出庫区分リストを返します.
	 * @return 入出庫区分リスト
	 * @throws ServiceException
	 */
	public List<LabelValueBean> getCategoryList() throws ServiceException {
		return categoryService.findCategoryLabelValueBeanListById(Categories.ENTRUST_EAD_CATEGORY);
	}

	/**
	 * キーカラム名を返します.
	 * @return 委託入出庫伝票テーブルのキーカラム名
	 */
	@Override
	protected String getKeyColumnName() {
		return "ENTRUST_EAD_SLIP_ID";
	}

	/**
	 * テーブル名を返します.
	 * @return 委託入出庫伝票テーブル名
	 */
	@Override
	protected String getTableName() {
		return "ENTRUST_EAD_SLIP_TRN";
	}
}
