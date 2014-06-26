/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.purchase.PurchaseLineDto;
import jp.co.arkinfosys.dto.purchase.PurchaseSlipDto;
import jp.co.arkinfosys.entity.PoLineTrn;
import jp.co.arkinfosys.entity.SupplierLineTrn;
import jp.co.arkinfosys.s2extend.NumberConverter;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.Converter;
import org.seasar.framework.beans.util.Beans;

/**
 * 仕入伝票明細行サービスクラスです.
 *
 * @author Ark Information Systems
 *
 */

public class SupplierLineService extends AbstractLineService<SupplierLineTrn,PurchaseLineDto,PurchaseSlipDto> {

	@Resource
	private SeqMakerService seqMakerService;
	@Resource
	private PoSlipService poSlipService;

	private PurchaseSlipDto deletedLineStoreDto;

	/**
	 *
	 * テーブル名定義クラスです.
	 *
	 */
	public static class Table {

		/** テーブル名：仕入伝票明細行 */
		private static final String SUPPLIER_LINE_TRN = "SUPPLIER_LINE_TRN";

	}

	/**
	 * 仕入伝票明細行の新規登録・更新処理を行います.
	 * @param dto 仕入伝票DTO
	 * @param lineList 仕入伝票明細行DTO
	 * @param deletedLineIds 削除対象仕入伝票明細行ID文字列
	 * @param abstractServices 保存で使用するサービス
	 * @throws ServiceException
	 */
	@Override
	public void save(PurchaseSlipDto dto, List<PurchaseLineDto> lineList,
			String deletedLineIds
			, AbstractService<?>... abstractServices) throws ServiceException {
		if (dto.newData == null || dto.newData ) {
			// 明細の新規登録
			insertSupplierLineList(dto);
		} else {
			// 更新する
			updateSupplierLineList(dto);
		}
	}


	/**
	 * 仕入伝票明細行を登録します.
	 * @param dto 仕入伝票DTO
	 * @throws ServiceException
	 */
	private void insertSupplierLineList(PurchaseSlipDto dto)
			throws ServiceException {
		// Entityオブジェクトに変換
		List<PurchaseLineDto> l = dto.getLineDtoList();
		for (PurchaseLineDto lineDto : l) {
			// 伝票の納期をコピー
			lineDto.deliveryDate = dto.deliveryDate;
			lineDto.supplierSlipId = dto.supplierSlipId;
			lineDto.rate = dto.rate;
			if (dto.rateId != null && dto.rateId.length() > 0) {
				lineDto.ctaxRate = null;
			} else {
				lineDto.ctaxRate = dto.ctaxRate;
			}

			SupplierLineTrn line = Beans.createAndCopy(SupplierLineTrn.class,
					lineDto).execute();
			if (!StringUtil.hasLength(line.productCode)) {
				continue;
			}

			// 仕入伝票明細行IDを発番
			try {
				line.supplierLineId = Integer.parseInt((Long
						.toString(seqMakerService
								.nextval(Table.SUPPLIER_LINE_TRN))));
				lineDto.supplierLineId = line.supplierLineId.toString();
			} catch (Exception e) {
				throw new ServiceException(e);
			}

			line.status = Constants.STATUS_SUPPLIER_SLIP.UNPAID;// 未払い
			line.supplierSlipId = Integer.parseInt(dto.supplierSlipId);
			// 以下の２つの条件を満たす場合のみ、消費税を設定する
			// 1.【仕入先マスタ】レートタイプが空欄でない
			// 2.【仕入先マスタ】税転嫁が「外税伝票計」or「外税締単位」である
			if("".equals(dto.rateId)){
				if(CategoryTrns.TAX_SHIFT_CATEGORY_SLIP_TOTAL.equals(dto.taxShiftCategory)  				// 外税伝票計
						|| CategoryTrns.TAX_SHIFT_CATEGORY_CLOSE_THE_BOOKS.equals(dto.taxShiftCategory)	// 外税締単位
						){
					if(line.ctaxRate != null){
						line.ctaxPrice = line.price.multiply((line.ctaxRate.divide(new BigDecimal(100.0)))); 	// 金額 * (レート / 100.0)
					}
				}
			}
			// 仕入伝票明細行を登録
			insertRecord(line);

			// 関連する仕入明細を更新する
			this.updateRelatedSlipLines(line, line.deliveryProcessCategory);

		}

	}

	/**
	 * 仕入伝票明細行を更新します.
	 * @param inDto 仕入伝票DTO
	 * @throws ServiceException
	 */
	private void updateSupplierLineList(PurchaseSlipDto inDto) throws ServiceException{

		// 仕入伝票更新の場合は、明細が削除された状態で更新される場合があるので、その場合はそれらの明細をDBから削除する必要がある。
		// このような明細を処理するために、このオブジェクトに格納する
		deletedLineStoreDto = Beans.createAndCopy(PurchaseSlipDto.class, inDto).execute();
		deletedLineStoreDto.setLineDtoList(new ArrayList<PurchaseLineDto>());

		try {

			// 明細エンティティの取得
			List<SupplierLineTrn> slList = findLineBySupplierSlipId(Integer
					.valueOf(inDto.supplierSlipId));

			// 明細行の更新
			List<PurchaseLineDto> l = inDto.getLineDtoList();
			for (PurchaseLineDto lineDto : l) {
				// 伝票番号、納期,レートのコピー
				lineDto.deliveryDate = inDto.deliveryDate;
				lineDto.supplierSlipId = inDto.supplierSlipId;
				lineDto.rate = inDto.rate;
				if (inDto.rateId != null && inDto.rateId.length() > 0) {
					lineDto.ctaxRate = null;
				} else {
					lineDto.ctaxRate = inDto.ctaxRate;
				}

				SupplierLineTrn line = Beans.createAndCopy(
						SupplierLineTrn.class, lineDto).execute();

				// 入力内容が存在する行だけを登録対象とする
				if (!StringUtil.hasLength(line.productCode)) {
					continue;
				}

				// 以下の２つの条件を満たす場合のみ、消費税を設定する
				// 1.【仕入先マスタ】レートタイプが空欄でない
				// 2.【仕入先マスタ】税転嫁が「外税伝票計」or「外税締単位」である
				if("".equals(inDto.rateId)){
					if(CategoryTrns.TAX_SHIFT_CATEGORY_SLIP_TOTAL.equals(inDto.taxShiftCategory)  				// 外税伝票計
							|| CategoryTrns.TAX_SHIFT_CATEGORY_CLOSE_THE_BOOKS.equals(inDto.taxShiftCategory)){	// 外税締単位
						line.ctaxPrice = line.price.multiply((line.ctaxRate.divide(new BigDecimal(100.0)))); 	// 金額 * (レート / 100.0)
					}
				}

				boolean bExist = false;
				// 明細行が存在する場合
				for (SupplierLineTrn tmpSl : slList) {
					if (tmpSl.supplierLineId.equals(line.supplierLineId)) {
						bExist = true;
						break;
					}
				}
				if (bExist == true) {
					// 存在したら常に更新
					if (this.updateRecord(line) == 0) {
						throw new ServiceException("errors.system");
					}
				} else {
					// 存在しない時には追加
					// 仕入伝票明細行IDを発番
					try {
						line.supplierLineId = Integer.parseInt((Long
								.toString(seqMakerService
										.nextval(Table.SUPPLIER_LINE_TRN))));
					} catch (Exception e) {
						throw new ServiceException(e);
					}

					line.supplierSlipId = Integer
							.parseInt(inDto.supplierSlipId);

					// 仕入伝票明細行を登録
					insertRecord(line);
				}
				// 関連する仕入明細を更新する
				this.updateRelatedSlipLines(line, line.deliveryProcessCategory);

			}
			// 明細行の削除
			for (SupplierLineTrn tmpSl : slList) {

				boolean bExist = false;

				// 明細行が存在する場合
				for (PurchaseLineDto lineDto : inDto.getLineDtoList()) {
					SupplierLineTrn line = Beans.createAndCopy(
							SupplierLineTrn.class, lineDto).execute();

					// 入力内容が存在する行だけを登録対象とする
					if (!StringUtil.hasLength(line.productCode)) {
						continue;
					}

					if (tmpSl.supplierLineId.equals(line.supplierLineId)) {
						bExist = true;
						break;
					}
				}
				if (bExist == false) {
					// 明細行が存在しない場合、画面から削除されているのでレコードも削除
					if (this.deleteSupplierLine(tmpSl) == 0) {
						throw new ServiceException("errors.system");
					}

					// 画面に存在しなかった明細行は、DB上だけにある明細行であるので、更新対象格納オブジェクトに明細を追加する
					PurchaseLineDto tmpSlDto = Beans.createAndCopy(PurchaseLineDto.class, tmpSl).execute();
					deletedLineStoreDto.getLineDtoList().add(tmpSlDto);

					// 関連する仕入明細を更新する
					updateRelatedSlipLines(tmpSl, CategoryTrns.DELIVERY_PROCESS_CATEGORY_PARTIAL);

				}
			}

		} catch (Exception e) {
			throw new ServiceException(e);
		}

		//		updateRecord(entity);
	}

	/**
	 * 仕入伝票DTOを指定して、関連する仕入伝票明細行のステータスを更新します.
	 * @param target 仕入伝票DTO
	 * @throws ServiceException
	 */
	public void updateRelatedSlipLinesBySlip(PurchaseSlipDto target) throws ServiceException {

		for (PurchaseLineDto purchaseLineDto : target.getLineDtoList()) {

			// 関連する仕入明細を更新する
			SupplierLineTrn tmpSl = new SupplierLineTrn();
			tmpSl.supplierLineId = Integer.parseInt(purchaseLineDto.supplierLineId);
			tmpSl.poLineId = Integer.parseInt(purchaseLineDto.poLineId);
			this.updateRelatedSlipLines(tmpSl, CategoryTrns.DELIVERY_PROCESS_CATEGORY_PARTIAL);
		}
	}


	/**
	 * 仕入伝票明細行を指定して、関連する仕入伝票明細行リストを取得します.
	 * @param target 仕入伝票明細行エンティティ
	 * @return 仕入伝票明細行リスト
	 * @throws ServiceException
	 */
	protected List<SupplierLineTrn> findRelatedSlipLine(SupplierLineTrn target) throws ServiceException {
		Map<String, Object> param = this.createSqlParam();
		param.put(SupplierSlipService.Param.PO_LINE_ID, target.poLineId);
		param.put(SupplierSlipService.Param.SUPPLIER_LINE_ID, target.supplierLineId);
		return this.selectBySqlFile(SupplierLineTrn.class, "purchase/FindRelatedSlipLine.sql", param).getResultList();
	}

	/**
	 * 関連する明細行を更新します.<br>
	 * 完納区分を指定した値に設定します.
	 * @param target 仕入伝票明細行エンティティ
	 * @param deliveryProcessCategory 完納区分
	 * @throws ServiceException
	 */
	protected void updateRelatedSlipLines(SupplierLineTrn target, String deliveryProcessCategory) throws ServiceException {
		// 関連する明細行を取得
		List<SupplierLineTrn> lines = this.findRelatedSlipLine(target);

		// １行ごとに更新
		Iterator<SupplierLineTrn> it = lines.iterator();
		while (it.hasNext()) {
			SupplierLineTrn line = it.next();
			if (deliveryProcessCategory!=null && !deliveryProcessCategory.equals(line.deliveryProcessCategory)) {
				line.deliveryProcessCategory = deliveryProcessCategory;
				this.updateRecord(line);
			}
		}
	}


	/**
	 * 仕入伝票明細行エンティティを指定して、仕入伝票明細行を削除します.
	 *
	 * @param line 仕入伝票明細行エンティティ
	 * @return 更新件数
	 */
	public int deleteSupplierLine(SupplierLineTrn line) {
		int SuccessedLineCount = 0;
		// パラメータを設定
		Map<String, Object> param = setLineEntityToParam(line);

		// SQLクエリを投げる
		super.updateAudit(new String[] { line.supplierSlipId.toString(),
				line.supplierLineId.toString() });

		SuccessedLineCount = this.updateBySqlFile(
				"purchase/DeleteLineBySupplierLineId.sql", param).execute();
		return SuccessedLineCount;
	}

	/**
	 * 仕入伝票番号を指定して、仕入伝票明細行リストを取得します.
	 * @param SupplierLineTrn 仕入伝票番号
	 * @return 仕入伝票明細行エンティティリスト
	 * @throws ServiceException
	 */
	public List<SupplierLineTrn> findLineBySupplierSlipId(
			Integer SupplierLineTrn) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(SupplierSlipService.Param.SUPPLIER_SLIP_ID,
					SupplierLineTrn);

			return this.selectBySqlFile(SupplierLineTrn.class,
					"purchase/FindLineBySupplierSlipId.sql", param)
					.getResultList();
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 仕入伝票明細行を更新します.
	 * @param entity 仕入伝票明細行エンティティ
	 * @return 更新件数
	 * @throws ServiceException
	 */
	@Override
	protected int updateRecord(SupplierLineTrn entity) throws ServiceException {
		// SQLクエリを投げる
		return  this.updateBySqlFile(
				"purchase/UpdateSupplierLineTrn.sql", setLineEntityToParam(entity))
				.execute();
	}

	/**
	 * 発注明細行番号を指定して、仕入伝票明細行の完納区分を更新します.
	 * @param poLineId 対応する発注明細行番号
	 * @param deliveryProcessCategory　完納区分
	 * @return　更新件数
	 * @throws ServiceException
	 */
	public int updateDeliveryProcessCategory(Integer poLineId,String deliveryProcessCategory) throws ServiceException {

		Map<String, Object> param = super.createSqlParam();
		param.put(SupplierSlipService.Param.DELIVERY_PROCESS_CATEGORY, deliveryProcessCategory); // 完納区分
		param.put(SupplierSlipService.Param.PO_LINE_ID, poLineId); // 

		// SQLクエリを投げる
		return  this.updateBySqlFile(
				"purchase/UpdateSupplierLineDeliveryProcessCategory.sql", param)
				.execute();
	}
	/**
	 * 仕入伝票明細行を登録します.
	 * @param entity 仕入伝票明細行エンティティ
	 * @return 更新件数
	 * @throws ServiceException
	 */
	@Override
	protected int insertRecord(SupplierLineTrn entity) throws ServiceException {
		// パラメータを設定
		Map<String, Object> param = setLineEntityToParam(entity);

		// 仕入伝票明細行登録
		return this.updateBySqlFile("purchase/InsertSupplierLineTrn.sql", param)
				.execute();

	}

	/**
	 * 検索条件パラメータを設定して返します.
	 * @param line 仕入伝票明細行エンティティ
	 * @return 検索条件パラメータ
	 */
	private Map<String, Object> setLineEntityToParam(SupplierLineTrn line) {
		Map<String, Object> param = super.createSqlParam();
		param.put(SupplierSlipService.Param.SUPPLIER_LINE_ID,
				line.supplierLineId); // 仕入伝票行ID
		param.put(SupplierSlipService.Param.STATUS, line.status); // 状態フラグ
		param.put(SupplierSlipService.Param.SUPPLIER_SLIP_ID,
				line.supplierSlipId); // 仕入伝票番号
		param.put(SupplierSlipService.Param.LINE_NO,
				line.lineNo); // 仕入伝票行番
		param.put(SupplierSlipService.Param.PRODUCT_CODE, line.productCode); // 商品コード
		param.put(SupplierSlipService.Param.SUPPLIER_PCODE, line.supplierPcode); // 仕入先品番
		param.put(SupplierSlipService.Param.PRODUCT_ABSTRACT,
				line.productAbstract); // 商品名
		param.put(SupplierSlipService.Param.PRODUCT_REMARKS,
				line.productRemarks); // 商品備考
		param.put(SupplierSlipService.Param.SUPPLIER_DETAIL_CATEGORY,
				line.supplierDetailCategory); // 仕入明細区分
		param.put(SupplierSlipService.Param.DELIVERY_PROCESS_CATEGORY,
				line.deliveryProcessCategory);// 完納区分
		param.put(SupplierSlipService.Param.TEMP_UNIT_PRICE_CATEGORY,
				line.tempUnitPriceCategory); // 仮単価区分
		param.put(SupplierSlipService.Param.TAX_CATEGORY, line.taxCategory); // 課税区分
		param.put(SupplierSlipService.Param.DELIVERY_DATE, line.deliveryDate); // 納期
		param.put(SupplierSlipService.Param.QUANTITY, line.quantity); // 数量
		param.put(SupplierSlipService.Param.UNIT_PRICE, line.unitPrice); // 単価
		param.put(SupplierSlipService.Param.PRICE, line.price); // 金額
		param.put(SupplierSlipService.Param.CTAX_RATE, line.ctaxRate); // 消費税率
		param.put(SupplierSlipService.Param.CTAX_PRICE, line.ctaxPrice); // 消費税
		param.put(SupplierSlipService.Param.DOL_UNIT_PRICE, line.dolUnitPrice); // ドル単価
		param.put(SupplierSlipService.Param.DOL_PRICE, line.dolPrice); // ドル金額
		param.put(SupplierSlipService.Param.RATE, line.rate); // レート
		param.put(SupplierSlipService.Param.REMARKS, line.remarks); // 備考
		param.put(SupplierSlipService.Param.RACK_CODE, line.rackCode); // 棚番コード
		param.put(SupplierSlipService.Param.RACK_NAME, line.rackName); // 棚番名称
		param.put(SupplierSlipService.Param.WAREHOUSE_NAME, line.warehouseName); // 倉庫名称
		param.put(SupplierSlipService.Param.PO_LINE_ID, line.poLineId); // 発注伝票行ID
		param
				.put(SupplierSlipService.Param.PAYMENT_LINE_ID,
						line.paymentLineId); // 支払伝票行ID
		return param;
	}

	/**
	 * 仕入伝票番号を指定して、仕入伝票明細行情報を削除します.
	 * @param id 仕入伝票番号
	 * @return 削除件数
	 * @throws ServiceException
	 */
	@Override
	public int deleteRecords(String id) throws ServiceException {
		try {

			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();

			// 削除のキーを明細番号から伝票番号に変更した
			param.put(SupplierSlipService.Param.SUPPLIER_SLIP_ID,
					id);

			return this.updateBySqlFile(
					"purchase/DeleteLineBySupplierSlipId.sql", param)
					.execute();

		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 仕入伝票明細行IDを複数指定して明細行を削除します.<br>
	 * 未使用です.
	 * @param ids 削除対象の仕入伝票明細行ID
	 * @return 削除件数
	 * @throws ServiceException
	 */
	@Override
	protected int deleteRecordsByLineId(String[] ids) throws ServiceException {
		// この伝票では使えない
		return 0;
	}

	/**
	 * 仕入伝票DTOを指定して、仕入伝票明細行リストを取得します.
	 * @param dto 仕入伝票DTO
	 * @return 仕入伝票明細行DTOリスト
	 * @throws ServiceException
	 */
	@Override
	public List<PurchaseLineDto> loadBySlip(PurchaseSlipDto dto)
			throws ServiceException {
		List<SupplierLineTrn> supplierLineTrnList = findLineBySupplierSlipId(Integer
				.valueOf(dto.supplierSlipId));

		// 数量端数処理
		Converter numConv = new NumberConverter(
				super.mineDto.productFractCategory,
				super.mineDto.numDecAlignment, true);
		// 円単価端数処理
		Converter yenConv = new NumberConverter(dto.priceFractCategory, 0, true);
		// 外貨単価端数処理
		Converter dolConv = new NumberConverter(dto.priceFractCategory, super.mineDto.unitPriceDecAlignment, true);
		// レート
		Converter rateConv = new NumberConverter(
				CategoryTrns.FLACT_CATEGORY_DOWN, 2, false);

		List<PurchaseLineDto> supplierLineTrnDtoList = new ArrayList<PurchaseLineDto>();
		for (SupplierLineTrn supplierLineTrn : supplierLineTrnList) {
			PurchaseLineDto lineDto = Beans.createAndCopy(
					PurchaseLineDto.class, supplierLineTrn).converter(
					rateConv, SupplierSlipService.Param.RATE).converter(
					numConv, SupplierSlipService.Param.QUANTITY).converter(
					yenConv, SupplierSlipService.Param.UNIT_PRICE,
					SupplierSlipService.Param.PRICE).converter(dolConv,
					SupplierSlipService.Param.DOL_UNIT_PRICE,
					SupplierSlipService.Param.DOL_PRICE).execute();

			// 更新前数量
			lineDto.oldQuantity = supplierLineTrn.quantity.toString();
			PoLineTrn poLineTrn = this.poSlipService
					.getPOLineTrnByPoLineId(lineDto.poLineId);
			lineDto.totalQuantity = lineDto.oldQuantity;
			lineDto.restQuantity = BigDecimal.ZERO.toString();
			if (poLineTrn != null) {
				// 発注伝票の発注数をトータル数量にセットする
				if(poLineTrn.quantity != null) {
					lineDto.totalQuantity = poLineTrn.quantity.toString();
				}
				// 発注残数をセットする
				if(poLineTrn.restQuantity != null) {
					lineDto.restQuantity = poLineTrn.restQuantity.toString();
				}
			}
			supplierLineTrnDtoList.add(lineDto);
		}
		return supplierLineTrnDtoList;
	}

	/**
	 * 削除された仕入伝票明細行を持つ仕入伝票DTOを返します.
	 * @return 仕入伝票DTO
	 */
	public PurchaseSlipDto getDeletedLineStoreDto() {
		return deletedLineStoreDto;
	}

	/**
	 * キーカラム名を返します.
	 * @return 仕入伝票明細行テーブルのキーカラム名
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "SUPPLIER_SLIP_ID", "SUPPLIER_LINE_ID" };
	}

	/**
	 * テーブル名を返します.
	 * @return 仕入伝票明細行テーブル名
	 */
	@Override
	protected String getTableName() {
		return "SUPPLIER_LINE_TRN";
	}
}
