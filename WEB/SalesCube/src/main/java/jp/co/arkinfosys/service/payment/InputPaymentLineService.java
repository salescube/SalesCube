/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.payment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.payment.InputPaymentDto;
import jp.co.arkinfosys.dto.payment.InputPaymentLineDto;
import jp.co.arkinfosys.entity.PaymentLineTrn;
import jp.co.arkinfosys.entity.join.PaymentSlipLineJoin;
import jp.co.arkinfosys.s2extend.NumberConverter;
import jp.co.arkinfosys.service.AbstractLineService;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.SeqMakerService;
import jp.co.arkinfosys.service.TaxRateService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.Converter;
import org.seasar.framework.beans.util.Beans;

/**
 * 支払入力明細行サービスクラスです.
 * @author Ark Information Systems
 */
public class InputPaymentLineService extends AbstractLineService<PaymentLineTrn, InputPaymentLineDto, InputPaymentDto> {

	@Resource
	private SeqMakerService seqMakerService;

	@Resource
	private TaxRateService taxRateService;

	/**
	 *
	 * テーブル名定義クラスです.
	 *
	 */
	public static class Table {
		/** テーブル名：支払伝票明細行 */
		private static final String LINE_TABLE_NAME = "PAYMENT_LINE_TRN";
	}

	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String PAYMENT_SLIP_ID = "paymentSlipId";
		public static final String PAYMENT_LINE_ID = "paymentLineId";
		public static final String PAYMENT_LINE_NO = "lineNo";
		public static final String PAYMENT_CATEGORY = "paymentCategory";
		public static final String PRODUCT_CODE = "productCode";
		public static final String PRODUCT_ABSTRACT = "productAbstract";
		public static final String QUANTITY = "quantity";
		public static final String UNIT_PRICE = "unitPrice";
		public static final String PRICE = "price";
		public static final String DOL_UNIT_PRICE = "dolUnitPrice";
		public static final String DOL_PRICE = "dolPrice";
		public static final String RATE = "rate";
		public static final String CTAX_RATE = "ctaxRate";
		public static final String CTAX_PRICE = "ctaxPrice";
		public static final String PO_LINE_ID = "poLineId";
		public static final String SUPPLIER_LINE_ID = "supplierLineId";
		public static final String SUPPLIER_DATE = "supplierDate";
		public static final String STATUS = "status";
		public static final String REMARKS = "remarks";

		public static final String PAYMENT_LINE_IDS = "paymentLineIds";
	}

	/**
	 * 支払伝票明細行を登録します.
	 * @param entity 支払伝票明細行エンティティ
	 * @return 登録件数
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.AbstractLineService#insertRecord(java.lang.Object)
	 */
	@Override
	protected int insertRecord(PaymentLineTrn entity) throws ServiceException {
		try {
			Map<String, Object> param = setEntityToParam(entity);

			return this.updateBySqlFile("payment/InsertPaymentLineTrn.sql", param).execute();

		} catch(Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 支払伝票明細行を更新します.
	 * @param entity 支払伝票明細行エンティティ
	 * @return 更新件数
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.AbstractLineService#updateRecord(java.lang.Object)
	 */
	@Override
	protected int updateRecord(PaymentLineTrn entity) throws ServiceException {
		try {
			Map<String, Object> param = setEntityToParam(entity);

			return this.updateBySqlFile("payment/UpdatePaymentLineTrn.sql", param).execute();

		} catch(Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 支払伝票番号を指定して支払伝票明細行を削除します.
	 * @param id 支払伝票番号
	 * @return 削除件数
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.AbstractLineService#deleteRecords(java.lang.String)
	 */
	@Override
	public int deleteRecords(String id) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(InputPaymentLineService.Param.PAYMENT_SLIP_ID, id);

			return this.updateBySqlFile("payment/DeletePaymentLineBySlipId.sql", param).execute();

		} catch(Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 支払伝票明細行IDを複数指定して明細行を削除します.
	 * @param ids 削除対象の支払伝票明細行IDの配列
	 * @return 削除件数
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.AbstractLineService#deleteRecordsByLineId(java.lang.String[])
	 */
	@Override
	protected int deleteRecordsByLineId(String[] ids) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(InputPaymentLineService.Param.PAYMENT_LINE_IDS, ids);
			return this.updateBySqlFile("DeletePaymentLineByLineIds.sql", param).execute();

		} catch(Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 支払伝票DTOの情報を基に支払伝票明細行を取得します.
	 * @param dto 支払伝票DTO
	 * @return 支払伝票明細行情報リスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.AbstractLineService#loadBySlip(jp.co.arkinfosys.dto.AbstractSlipDto)
	 */
	@Override
	public List<InputPaymentLineDto> loadBySlip(InputPaymentDto dto) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(InputPaymentLineService.Param.PAYMENT_SLIP_ID, dto.paymentSlipId);
			List<PaymentSlipLineJoin> resultList = this.selectBySqlFile(PaymentSlipLineJoin.class, "payment/FindPaymentSlipLineBySlipId.sql", param).getResultList();

			// 数量端数処理
			Converter numConv = new NumberConverter(super.mineDto.productFractCategory, super.mineDto.numDecAlignment, true);

			// 円単価端数処理
			Converter yenConv = new NumberConverter(dto.priceFractCategory, 0, true);

			// 外貨単価端数処理
			Converter dolConv = new NumberConverter(dto.priceFractCategory, super.mineDto.unitPriceDecAlignment, true);

			List<InputPaymentLineDto> dtoList = new ArrayList<InputPaymentLineDto>();

			for(PaymentSlipLineJoin slipLine : resultList) {
				InputPaymentLineDto lineDto = Beans.createAndCopy(InputPaymentLineDto.class, slipLine).timestampConverter(Constants.FORMAT.TIMESTAMP).dateConverter(Constants.FORMAT.DATE).converter(yenConv, "unitPrice", "price").converter(dolConv, "dolUnitPrice", "dolPrice").converter(numConv, "quantity").execute();
				dtoList.add(lineDto);
			}

			return dtoList;

		} catch(Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 支払伝票明細行を保存します.
	 * @param slipDto 支払伝票DTO
	 * @param lineList 支払伝票明細行リスト
	 * @param deletedLineIds 削除対象支払伝票明細行ID文字列
	 * @param abstractServices 保存で使用するサービス
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.AbstractLineService#save(jp.co.arkinfosys.dto.AbstractSlipDto,java.util.List, java.lang.String,jp.co.arkinfosys.service.AbstractService[])
	 */
	@Override
	public void save(InputPaymentDto slipDto, List<InputPaymentLineDto> lineList, String deletedLineIds
			, AbstractService<?>... abstractServices) throws ServiceException {
		try {
			if(lineList != null && lineList.size() > 0) {
				short i = 1;

				for(InputPaymentLineDto dto : lineList) {
					// Entityに変換する
					PaymentLineTrn entity = Beans.createAndCopy(PaymentLineTrn.class, dto).execute();

					entity.status = Constants.STATUS_PAYMENT_LINE.PAID;
					entity.paymentSlipId = Integer.parseInt(slipDto.paymentSlipId);
//					entity.ctaxRate = taxRateService.findTaxRateById(CategoryTrns.TAX_TYPE_CTAX, StringUtil.getCurrentDateString(Constants.FORMAT.DATE)).taxRate;

					// 以下の２つの条件を満たす場合のみ、消費税を設定する
					// 1.【仕入先マスタ】レートタイプが空欄でない
					// 2.【仕入先マスタ】税転嫁が「外税伝票計」or「外税締単位」である
//					if("".equals(slipDto.rateId)) {
//						if(CategoryTrns.TAX_SHIFT_CATEGORY_SLIP_TOTAL.equals(slipDto.taxShiftCategory) || CategoryTrns.TAX_SHIFT_CATEGORY_CLOSE_THE_BOOKS.equals(slipDto.taxShiftCategory)) {
//							 金額 * (レート / 100.0)
//							entity.ctaxPrice = entity.price.multiply((entity.ctaxRate.divide(new BigDecimal(100.0))));
//						}
//					}

					if(dto.paymentLineId == null || dto.paymentLineId.length() == 0) {
						// チェックされていない明細行の登録はしない
						if(!dto.checkPayLine) {
							continue;
						}

						// 明細IDを採番する
						dto.paymentLineId = Long.toString(seqMakerService.nextval(InputPaymentLineService.Table.LINE_TABLE_NAME));
						entity.paymentLineId = Integer.parseInt(dto.paymentLineId);

						// 明細行番を採番する(登録しない行に対しては行番号を発行しないためここで設定)
						entity.lineNo = i++;
						dto.paymentLineNo = Short.toString(entity.lineNo);

						// レコードを追加する
						insertRecord(entity);

					} else {
						entity.lineNo = i++;
						dto.paymentLineNo = Short.toString(entity.lineNo);

						// レコードを更新する
						updateRecord(entity);
					}
				}
			}

			if(deletedLineIds != null && deletedLineIds.length() > 0) {
				String[] ids = deletedLineIds.split(",");
				super.updateAudit(ids);
				deleteRecordsByLineId(ids);
			}

		} catch(NumberFormatException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件パラメータを設定して返します.
	 * @param entity 支払伝票明細行エンティティ
	 * @return 検索条件パラメータ
	 */
	private Map<String, Object> setEntityToParam(PaymentLineTrn entity) {
		Map<String, Object> param = super.createSqlParam();

		// 支払伝票行ID
		param.put(InputPaymentLineService.Param.PAYMENT_LINE_ID, entity.paymentLineId);

		// 状態フラグ
		param.put(InputPaymentLineService.Param.STATUS, entity.status);

		// 支払伝票番号
		param.put(InputPaymentLineService.Param.PAYMENT_SLIP_ID, entity.paymentSlipId);

		// 支払伝票行番
		param.put(InputPaymentLineService.Param.PAYMENT_LINE_NO, entity.lineNo);

		// 支払区分コード
		param.put(InputPaymentLineService.Param.PAYMENT_CATEGORY, entity.paymentCategory);

		// 商品コード
		param.put(InputPaymentLineService.Param.PRODUCT_CODE, entity.productCode);

		// 商品名
		param.put(InputPaymentLineService.Param.PRODUCT_ABSTRACT, entity.productAbstract);

		// 数量
		param.put(InputPaymentLineService.Param.QUANTITY, entity.quantity);

		// 単価
		param.put(InputPaymentLineService.Param.UNIT_PRICE, entity.unitPrice);

		// 金額
		param.put(InputPaymentLineService.Param.PRICE, entity.price);

		// ドル単価
		param.put(InputPaymentLineService.Param.DOL_UNIT_PRICE, entity.dolUnitPrice);

		// ドル金額
		param.put(InputPaymentLineService.Param.DOL_PRICE, entity.dolPrice);

		// レート
		param.put(InputPaymentLineService.Param.RATE, entity.rate);

		// 消費税率
		param.put(InputPaymentLineService.Param.CTAX_RATE, entity.ctaxRate);

		// 消費税
		param.put(InputPaymentLineService.Param.CTAX_PRICE, entity.ctaxPrice);

		// 発注伝票行ID
		param.put(InputPaymentLineService.Param.PO_LINE_ID, entity.poLineId);

		// 仕入伝票行ID
		param.put(InputPaymentLineService.Param.SUPPLIER_LINE_ID, entity.supplierLineId);

		// 備考
		param.put(InputPaymentLineService.Param.REMARKS, entity.remarks);

		// 仕入日
		param.put(InputPaymentLineService.Param.SUPPLIER_DATE, entity.supplierDate);

		return param;
	}

	/**
	 * キーカラム名を返します.
	 * @return 支払伝票明細行のキーカラム名配列
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "PAYMENT_SLIP_ID", "PAYMENT_LINE_ID" };
	}

	/**
	 * テーブル名を返します.
	 * @return 支払伝票明細行テーブル名
	 */
	@Override
	protected String getTableName() {
		return "PAYMENT_LINE_TRN";
	}
}
