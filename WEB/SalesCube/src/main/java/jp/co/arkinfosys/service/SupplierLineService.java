/*
 *  Copyright 2009-2010 Ark Information Systems.
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
			
			insertSupplierLineList(dto);
		} else {
			
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
		
		List<PurchaseLineDto> l = dto.getLineDtoList();
		for (PurchaseLineDto lineDto : l) {
			
			lineDto.deliveryDate = dto.deliveryDate;
			lineDto.supplierSlipId = dto.supplierSlipId;
			lineDto.rate = dto.rate;
			if (dto.rateId != null && dto.rateId.length() > 0) {
				lineDto.ctaxRate = null;
			} else {
				lineDto.ctaxRate = dto.supplierTaxRate;
			}

			SupplierLineTrn line = Beans.createAndCopy(SupplierLineTrn.class,
					lineDto).execute();
			if (!StringUtil.hasLength(line.productCode)) {
				continue;
			}

			
			try {
				line.supplierLineId = Integer.parseInt((Long
						.toString(seqMakerService
								.nextval(Table.SUPPLIER_LINE_TRN))));
				lineDto.supplierLineId = line.supplierLineId.toString();
			} catch (Exception e) {
				throw new ServiceException(e);
			}

			line.status = Constants.STATUS_SUPPLIER_SLIP.UNPAID;
			line.supplierSlipId = Integer.parseInt(dto.supplierSlipId);
			
			
			
			if("".equals(dto.rateId)){
				if(CategoryTrns.TAX_SHIFT_CATEGORY_SLIP_TOTAL.equals(dto.taxShiftCategory)  				
						|| CategoryTrns.TAX_SHIFT_CATEGORY_CLOSE_THE_BOOKS.equals(dto.taxShiftCategory)){	
					line.ctaxPrice = line.price.multiply((line.ctaxRate.divide(new BigDecimal(100.0)))); 	
				}
			}
			
			insertRecord(line);

			
			this.updateRelatedSlipLines(line, line.deliveryProcessCategory);

		}

	}

	/**
	 * 仕入伝票明細行を更新します.
	 * @param inDto 仕入伝票DTO
	 * @throws ServiceException
	 */
	private void updateSupplierLineList(PurchaseSlipDto inDto) throws ServiceException{

		
		
		deletedLineStoreDto = Beans.createAndCopy(PurchaseSlipDto.class, inDto).execute();
		deletedLineStoreDto.setLineDtoList(new ArrayList<PurchaseLineDto>());

		try {

			
			List<SupplierLineTrn> slList = findLineBySupplierSlipId(Integer
					.valueOf(inDto.supplierSlipId));

			
			List<PurchaseLineDto> l = inDto.getLineDtoList();
			for (PurchaseLineDto lineDto : l) {
				
				lineDto.deliveryDate = inDto.deliveryDate;
				lineDto.supplierSlipId = inDto.supplierSlipId;
				lineDto.rate = inDto.rate;
				if (inDto.rateId != null && inDto.rateId.length() > 0) {
					lineDto.ctaxRate = null;
				} else {
					lineDto.ctaxRate = inDto.supplierTaxRate;
				}

				SupplierLineTrn line = Beans.createAndCopy(
						SupplierLineTrn.class, lineDto).execute();

				
				if (!StringUtil.hasLength(line.productCode)) {
					continue;
				}

				
				
				
				if("".equals(inDto.rateId)){
					if(CategoryTrns.TAX_SHIFT_CATEGORY_SLIP_TOTAL.equals(inDto.taxShiftCategory)  				
							|| CategoryTrns.TAX_SHIFT_CATEGORY_CLOSE_THE_BOOKS.equals(inDto.taxShiftCategory)){	
						line.ctaxPrice = line.price.multiply((line.ctaxRate.divide(new BigDecimal(100.0)))); 	
					}
				}

				boolean bExist = false;
				
				for (SupplierLineTrn tmpSl : slList) {
					if (tmpSl.supplierLineId.equals(line.supplierLineId)) {
						bExist = true;
						break;
					}
				}
				if (bExist == true) {
					
					if (this.updateRecord(line) == 0) {
						throw new ServiceException("errors.system");
					}
				} else {
					
					
					try {
						line.supplierLineId = Integer.parseInt((Long
								.toString(seqMakerService
										.nextval(Table.SUPPLIER_LINE_TRN))));
					} catch (Exception e) {
						throw new ServiceException(e);
					}

					line.supplierSlipId = Integer
							.parseInt(inDto.supplierSlipId);

					
					insertRecord(line);
				}
				
				this.updateRelatedSlipLines(line, line.deliveryProcessCategory);

			}
			
			for (SupplierLineTrn tmpSl : slList) {

				boolean bExist = false;

				
				for (PurchaseLineDto lineDto : inDto.getLineDtoList()) {
					SupplierLineTrn line = Beans.createAndCopy(
							SupplierLineTrn.class, lineDto).execute();

					
					if (!StringUtil.hasLength(line.productCode)) {
						continue;
					}

					if (tmpSl.supplierLineId.equals(line.supplierLineId)) {
						bExist = true;
						break;
					}
				}
				if (bExist == false) {
					
					if (this.deleteSupplierLine(tmpSl) == 0) {
						throw new ServiceException("errors.system");
					}

					
					PurchaseLineDto tmpSlDto = Beans.createAndCopy(PurchaseLineDto.class, tmpSl).execute();
					deletedLineStoreDto.getLineDtoList().add(tmpSlDto);

					
					updateRelatedSlipLines(tmpSl, CategoryTrns.DELIVERY_PROCESS_CATEGORY_PARTIAL);

				}
			}

		} catch (Exception e) {
			throw new ServiceException(e);
		}

		
	}

	/**
	 * 仕入伝票DTOを指定して、関連する仕入伝票明細行のステータスを更新します.
	 * @param target 仕入伝票DTO
	 * @throws ServiceException
	 */
	public void updateRelatedSlipLinesBySlip(PurchaseSlipDto target) throws ServiceException {

		for (PurchaseLineDto purchaseLineDto : target.getLineDtoList()) {

			
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
		
		List<SupplierLineTrn> lines = this.findRelatedSlipLine(target);

		
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
		
		Map<String, Object> param = setLineEntityToParam(line);

		
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
		
		return  this.updateBySqlFile(
				"purchase/UpdateSupplierLineTrn.sql", setLineEntityToParam(entity))
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
		
		Map<String, Object> param = setLineEntityToParam(entity);

		
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
				line.supplierLineId); 
		param.put(SupplierSlipService.Param.STATUS, line.status); 
		param.put(SupplierSlipService.Param.SUPPLIER_SLIP_ID,
				line.supplierSlipId); 
		param.put(SupplierSlipService.Param.LINE_NO,
				line.lineNo); 
		param.put(SupplierSlipService.Param.PRODUCT_CODE, line.productCode); 
		param.put(SupplierSlipService.Param.SUPPLIER_PCODE, line.supplierPcode); 
		param.put(SupplierSlipService.Param.PRODUCT_ABSTRACT,
				line.productAbstract); 
		param.put(SupplierSlipService.Param.PRODUCT_REMARKS,
				line.productRemarks); 
		param.put(SupplierSlipService.Param.SUPPLIER_DETAIL_CATEGORY,
				line.supplierDetailCategory); 
		param.put(SupplierSlipService.Param.DELIVERY_PROCESS_CATEGORY,
				line.deliveryProcessCategory);
		param.put(SupplierSlipService.Param.TEMP_UNIT_PRICE_CATEGORY,
				line.tempUnitPriceCategory); 
		param.put(SupplierSlipService.Param.TAX_CATEGORY, line.taxCategory); 
		param.put(SupplierSlipService.Param.DELIVERY_DATE, line.deliveryDate); 
		param.put(SupplierSlipService.Param.QUANTITY, line.quantity); 
		param.put(SupplierSlipService.Param.UNIT_PRICE, line.unitPrice); 
		param.put(SupplierSlipService.Param.PRICE, line.price); 
		param.put(SupplierSlipService.Param.CTAX_RATE, line.ctaxRate); 
		param.put(SupplierSlipService.Param.CTAX_PRICE, line.ctaxPrice); 
		param.put(SupplierSlipService.Param.DOL_UNIT_PRICE, line.dolUnitPrice); 
		param.put(SupplierSlipService.Param.DOL_PRICE, line.dolPrice); 
		param.put(SupplierSlipService.Param.RATE, line.rate); 
		param.put(SupplierSlipService.Param.REMARKS, line.remarks); 
		param.put(SupplierSlipService.Param.RACK_CODE, line.rackCode); 
		param.put(SupplierSlipService.Param.RACK_NAME, line.rackName); 
		param.put(SupplierSlipService.Param.WAREHOUSE_NAME, line.warehouseName); 
		param.put(SupplierSlipService.Param.PO_LINE_ID, line.poLineId); 
		param
				.put(SupplierSlipService.Param.PAYMENT_LINE_ID,
						line.paymentLineId); 
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

			
			Map<String, Object> param = super.createSqlParam();

			
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

		
		Converter numConv = new NumberConverter(
				super.mineDto.productFractCategory,
				super.mineDto.numDecAlignment, true);
		
		Converter yenConv = new NumberConverter(dto.priceFractCategory, 0, true);
		
		Converter dolConv = new NumberConverter(dto.priceFractCategory, super.mineDto.unitPriceDecAlignment, true);
		
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

			
			lineDto.oldQuantity = supplierLineTrn.quantity.toString();
			PoLineTrn poLineTrn = this.poSlipService
					.getPOLineTrnByPoLineId(lineDto.poLineId);
			lineDto.totalQuantity = lineDto.oldQuantity;
			lineDto.restQuantity = BigDecimal.ZERO.toString();
			if (poLineTrn != null) {
				
				if(poLineTrn.quantity != null) {
					lineDto.totalQuantity = poLineTrn.quantity.toString();
				}
				
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
