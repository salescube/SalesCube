/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.service.stock;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.CheckUtil;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.dto.sales.SalesLineDto;
import jp.co.arkinfosys.dto.sales.SalesSlipDto;
import jp.co.arkinfosys.dto.stock.EadLineTrnDto;
import jp.co.arkinfosys.dto.stock.EadSlipTrnDto;
import jp.co.arkinfosys.entity.EadLineTrn;
import jp.co.arkinfosys.entity.EadSlipTrn;
import jp.co.arkinfosys.entity.Rack;
import jp.co.arkinfosys.entity.join.EadSlipLineJoin;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.entity.join.ProductSetJoin;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.EadService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.ProductSetService;
import jp.co.arkinfosys.service.RackService;
import jp.co.arkinfosys.service.SeqMakerService;
import jp.co.arkinfosys.service.YmService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.framework.beans.Converter;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 売上伝票入出庫入力サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class InputStockSalesService extends AbstractService<EadSlipTrn> {


	@Resource
	protected EadService eadService;

    @Resource
    protected RackService rackService;

	@Resource
	protected SeqMakerService seqMakerService;

	@Resource
	protected YmService ymService;

	@Resource
	protected ProductSetService productSetService;

	@Resource
	protected ProductService productService;


	/**
	 * 売上伝票入力情報を使用して入出庫入力の伝票・明細行を作成します.
	 * @param dto 売上伝票DTO
	 * @return 採番した伝票番号(未登録の場合はnull)
	 * @throws ServiceException
	 */
	public String insert(SalesSlipDto dto)
			throws ServiceException {
		try {
			EadSlipTrnDto eadSlipTrnDto = new EadSlipTrnDto();
			eadSlipTrnDto.setLineDtoList(new ArrayList<EadLineTrnDto>());


			
			
			eadSlipTrnDto.eadSlipId = Long.toString(seqMakerService
					.nextval(EadService.Table.EAD_SLIP_TRN));

			
			YmDto ymDto = ymService.getYm(dto.salesDate);
			if(ymDto == null) {
				ServiceException se = new ServiceException(
						MessageResourcesUtil.getMessage("errors.system"));
				se.setStopOnError(true);
				throw se;
			}
			eadSlipTrnDto.eadDate = dto.salesDate;
			eadSlipTrnDto.eadAnnual = ymDto.annual.toString();
			eadSlipTrnDto.eadMonthly = ymDto.monthly.toString();
			eadSlipTrnDto.eadYm = ymDto.ym.toString();

			
			eadSlipTrnDto.userId = dto.userId;
			eadSlipTrnDto.userName = dto.userName;

			
			eadSlipTrnDto.eadSlipCategory = CategoryTrns.EAD_SLIP_CATEGORY_NORMAL;

			
			eadSlipTrnDto.eadCategory = CategoryTrns.EAD_CATEGORY_DISPATCH;

			
			eadSlipTrnDto.srcFunc = Constants.SRC_FUNC.SALES;

			
			eadSlipTrnDto.salesSlipId = dto.salesSlipId;

			
			eadSlipTrnDto.supplierSlipId = null;

			
			eadSlipTrnDto.moveDepositSlipId = null;

			
			Integer lineNo = 0;
			List<SalesLineDto> salesLineList = dto.getLineDtoList();
			for (SalesLineDto lineDto : salesLineList) {

				
				if (lineDto.isBlank()) {
					continue;
				}

				
				if(lineDto.quantity == null || new BigDecimal(lineDto.quantity).compareTo(BigDecimal.ZERO) <= 0) {
					continue;
				}

				
				String l_setTypeCategory = productService.findById(lineDto.productCode).setTypeCategory;
				lineDto.setTypeCategory = ((l_setTypeCategory==null)?"":l_setTypeCategory);

				if (CategoryTrns.PRODUCT_SET_TYPE_SET.equals(lineDto.setTypeCategory)) {
					
					
					List<ProductSetJoin> listSet = productSetService.findProductSetByProductCode(lineDto.productCode);
					for( ProductSetJoin psj : listSet ){

						
						ProductJoin pj = productService.findById(psj.productCode);

						
						if( !CheckUtil.isRackCheck(pj) ){
							continue;
						}

						
						EadLineTrnDto eadLineTrnDto = setToDto( eadSlipTrnDto.eadSlipId, dto, lineDto , pj, psj, ++lineNo);

						
						eadSlipTrnDto.getLineDtoList().add(eadLineTrnDto);
					}
				}else{
					
					ProductJoin pj = productService.findById(lineDto.productCode);

					
					if( !CheckUtil.isRackCheck(pj) ){
						continue;
					}
					
					EadLineTrnDto eadLineTrnDto = setToDto( eadSlipTrnDto.eadSlipId, dto, lineDto , ++lineNo);

					
					eadSlipTrnDto.getLineDtoList().add(eadLineTrnDto);
				}
			}

			if( lineNo > 0 ){
				
				eadService.insertSlipAndLine(eadSlipTrnDto);

				return eadSlipTrnDto.eadSlipId;
			}
			
			return null;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 売上伝票入力情報を使用して、対応する在庫締め済みの入出庫伝票が存在するか否かを返します.
	 * @param dto 売上伝票DTO
	 * @return 対応する在庫締め済みの入出庫伝票が存在するか否か
	 * @throws ServiceException
	 */
	public boolean existsClosedEadSlip(SalesSlipDto dto) throws ServiceException {
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put(EadService.Param.SALES_SLIP_ID, dto.salesSlipId);
		conditions.put(EadService.Param.ROW_COUNT, 1);
		conditions.put(EadService.Param.OFFSET_ROW, 0);
		List<EadSlipLineJoin> eadSlips = this.eadService.findEadSlipByCondition(conditions, EadService.Param.SRC_FUNC, true);
		for(EadSlipLineJoin eadSlip : eadSlips) {
			if(eadSlip.stockPdate != null) {
				return true;
			}
			break;
		}
		return false;
	}

	/**
	 * 売上伝票入力情報を使用して、入出庫入力の伝票・明細行を更新します.
	 * @param dto 売上伝票DTO
	 * @throws ServiceException
	 */
	public void update(SalesSlipDto dto)
			throws ServiceException, UnabledLockException {
		try {
			EadSlipTrn eadSlipTrn = this.eadService.findSlipBySalesSlipId(Integer.parseInt(dto.salesSlipId));
			if(eadSlipTrn == null) {
				
				insert(dto);
				return;
			}

			
			Map<String, Object> param = super.createSqlParam();
			param.put(EadService.Param.EAD_SLIP_ID, eadSlipTrn.eadSlipId);
			lockRecordBySqlFile("ead/LockSlipByEadSlipId.sql",
					param, eadSlipTrn.updDatetm);

			List<EadLineTrn> eadLineTrnList = this.eadService.findLineByEadSlipId(eadSlipTrn.eadSlipId);
			Map<Integer, List<EadLineTrn>> salesEadLineMap = new HashMap<Integer, List<EadLineTrn>>();
			for(EadLineTrn eadLineTrn : eadLineTrnList) {
				if(!salesEadLineMap.containsKey(eadLineTrn.salesLineId)) {
					salesEadLineMap.put(eadLineTrn.salesLineId, new ArrayList<EadLineTrn>());
				}
				salesEadLineMap.get(eadLineTrn.salesLineId).add(eadLineTrn);
			}

			short lineNo = 1;
			List<EadLineTrn> newLineTrnList = new ArrayList<EadLineTrn>();
			List<EadLineTrn> updateLineTrnList = new ArrayList<EadLineTrn>();
			List<SalesLineDto> salesLineList = dto.getLineDtoList();
			for (SalesLineDto lineDto : salesLineList) {
				if (!StringUtil.hasLength(lineDto.productCode) || !StringUtil.hasLength(lineDto.salesLineId)) {
					continue;
				}

				
				if(lineDto.quantity == null || new BigDecimal(lineDto.quantity).compareTo(BigDecimal.ZERO) <= 0) {
					continue;
				}

				if(salesEadLineMap.containsKey(Integer.parseInt(lineDto.salesLineId))) {
					

					List<EadLineTrn> list = salesEadLineMap.get(Integer.parseInt(lineDto.salesLineId));
					Iterator<EadLineTrn> ite = list.iterator();

					String l_setTypeCategory = productService.findById(lineDto.productCode).setTypeCategory;
					lineDto.setTypeCategory = ((l_setTypeCategory==null)?"":l_setTypeCategory);

					if( CategoryTrns.PRODUCT_SET_TYPE_SET.equals( lineDto.setTypeCategory )){
						

						List<ProductSetJoin> listSet = productSetService.findProductSetByProductCode( lineDto.productCode );
						for(ProductSetJoin psj : listSet) {
							
							ProductJoin pj = productService.findById(psj.productCode);

							
							if( !CheckUtil.isRackCheck(pj) ){
								continue;
							}

							boolean newLine = false;

							EadLineTrn eadLineTrn = null;
							if(ite.hasNext()) {
								eadLineTrn = ite.next();
								ite.remove();
							}
							else {
								newLine = true;
								eadLineTrn = new EadLineTrn();
							}
							eadLineTrn.eadSlipId = eadSlipTrn.eadSlipId;
							eadLineTrn.lineNo = lineNo++;
							eadLineTrn.productCode = pj.productCode;
							eadLineTrn.productAbstract = pj.productName;
							
							eadLineTrn.quantity = new BigDecimal(lineDto.quantity).multiply(psj.quantity);
							eadLineTrn.remarks = dto.salesSlipId + "-" + lineDto.lineNo
											+ " " + lineDto.productCode + ":" + lineDto.productAbstract;
							eadLineTrn.rackCode = pj.rackCode;
							eadLineTrn.rackName = pj.rackName;
							eadLineTrn.salesLineId = Integer.parseInt(lineDto.salesLineId);
							eadLineTrn.supplierLineId = null;

							if(newLine) {
								newLineTrnList.add(eadLineTrn);
							}
							else {
								updateLineTrnList.add(eadLineTrn);
							}
						}
					}else{
						
						ProductJoin pj = productService.findById(lineDto.productCode);

						
						if( !CheckUtil.isRackCheck(pj) ){
							continue;
						}

						if(ite.hasNext()) {
							EadLineTrn eadLineTrn = ite.next();
							ite.remove();

							eadLineTrn.eadSlipId = eadSlipTrn.eadSlipId;
							eadLineTrn.lineNo = lineNo++;
							eadLineTrn.productCode = pj.productCode;
							eadLineTrn.productAbstract = pj.productName;
							eadLineTrn.quantity = new BigDecimal(lineDto.quantity);
							eadLineTrn.remarks = dto.salesSlipId + "-" + lineDto.lineNo
											+ " " + lineDto.productCode + ":" + lineDto.productAbstract;
							eadLineTrn.rackCode = pj.rackCode;
							eadLineTrn.rackName = pj.rackName;
							eadLineTrn.salesLineId = Integer.parseInt(lineDto.salesLineId);
							eadLineTrn.supplierLineId = null;

							updateLineTrnList.add(eadLineTrn);
						}
					}
				}
				else {
					

					String l_setTypeCategory = productService.findById(lineDto.productCode).setTypeCategory;
					lineDto.setTypeCategory = ((l_setTypeCategory==null)?"":l_setTypeCategory);

					if( CategoryTrns.PRODUCT_SET_TYPE_SET.equals( lineDto.setTypeCategory )){
						

						List<ProductSetJoin> listSet = productSetService.findProductSetByProductCode( lineDto.productCode );
						for( ProductSetJoin psj : listSet ){

							
							ProductJoin pj = productService.findById(psj.productCode);

							
							if( !CheckUtil.isRackCheck(pj) ){
								continue;
							}

							EadLineTrn eadLineTrn = new EadLineTrn();
							eadLineTrn.eadSlipId = eadSlipTrn.eadSlipId;
							eadLineTrn.lineNo = lineNo++;
							eadLineTrn.productCode = pj.productCode;
							eadLineTrn.productAbstract = pj.productName;
							
							eadLineTrn.quantity = new BigDecimal(lineDto.quantity).multiply(psj.quantity);
							eadLineTrn.remarks = dto.salesSlipId + "-" + lineDto.lineNo
											+ " " + lineDto.productCode + ":" + lineDto.productAbstract;
							eadLineTrn.rackCode = pj.rackCode;
							eadLineTrn.rackName = pj.rackName;
							eadLineTrn.salesLineId = Integer.parseInt(lineDto.salesLineId);
							eadLineTrn.supplierLineId = null;

							newLineTrnList.add(eadLineTrn);
						}
					}else{
						
						ProductJoin pj = productService.findById(lineDto.productCode);

						
						if( !CheckUtil.isRackCheck(pj) ){
							continue;
						}

						EadLineTrn eadLineTrn = new EadLineTrn();
						eadLineTrn.eadSlipId = eadSlipTrn.eadSlipId;
						eadLineTrn.lineNo = lineNo++;
						eadLineTrn.productCode = pj.productCode;
						eadLineTrn.productAbstract = pj.productName;
						eadLineTrn.quantity = new BigDecimal(lineDto.quantity);
						eadLineTrn.remarks = dto.salesSlipId + "-" + lineDto.lineNo
										+ " " + lineDto.productCode + ":" + lineDto.productAbstract;
						eadLineTrn.rackCode = pj.rackCode;
						eadLineTrn.rackName = pj.rackName;
						eadLineTrn.salesLineId = Integer.parseInt(lineDto.salesLineId);
						eadLineTrn.supplierLineId = null;

						newLineTrnList.add(eadLineTrn);
					}
				}
			}

			
			for(EadLineTrn eadLineTrn: updateLineTrnList) {
				this.eadService.updateLine(eadLineTrn);
			}

			
			for(EadLineTrn eadLineTrn: newLineTrnList) {
				eadLineTrn.eadLineId = (int)seqMakerService.nextval(EadService.Table.EAD_LINE_TRN);
				this.eadService.insertLine(eadLineTrn);
			}

			
			for(List<EadLineTrn> list : salesEadLineMap.values()) {
				for(EadLineTrn eadLineTrn : list) {
					this.eadService.deleteLineByEadLineId(eadLineTrn.eadLineId);
				}
			}

			
			if(updateLineTrnList.size() == 0 && newLineTrnList.size() == 0) {
				eadService.deleteSlipByEadSlipId(eadSlipTrn.eadSlipId);
			} else {
				
				YmDto ymDto = ymService.getYm(dto.salesDate);
				if(ymDto == null) {
					ServiceException se = new ServiceException(
							MessageResourcesUtil.getMessage("errors.system"));
					se.setStopOnError(true);
					throw se;
				}
				eadSlipTrn.eadDate = super.convertUtilDateToSqlDate(new SimpleDateFormat(Constants.FORMAT.DATE).parse(dto.salesDate));
				eadSlipTrn.eadAnnual = ymDto.annual.shortValue();
				eadSlipTrn.eadMonthly = ymDto.monthly.shortValue();
				eadSlipTrn.eadYm = ymDto.ym;

				this.eadService.updateSlip(eadSlipTrn);
			}

		} catch (UnabledLockException e) {
			throw e;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 売上伝票入力情報を使用して、入出庫入力の伝票・明細行を削除します.
	 * @param dto 売上伝票DTO
	 * @throws ServiceException
	 */
	public void delete(SalesSlipDto dto)
			throws ServiceException {
		try {
			EadSlipTrn eadSlipTrn = this.eadService.findSlipBySalesSlipId(Integer.parseInt(dto.salesSlipId));
			if(eadSlipTrn == null) {
				return;
			}

			
			Map<String, Object> param = super.createSqlParam();
			param.put(EadService.Param.EAD_SLIP_ID, eadSlipTrn.eadSlipId);
			lockRecordBySqlFile("ead/LockSlipByEadSlipId.sql",
					param, eadSlipTrn.updDatetm);

			
			List<EadLineTrn> eadLineTrnList = this.eadService.findLineByEadSlipId(eadSlipTrn.eadSlipId);
			for (EadLineTrn eadLineTrn : eadLineTrnList) {
				this.eadService.deleteLineByEadLineId(eadLineTrn.eadLineId);
			}

			
			this.eadService.deleteSlipByEadSlipId(eadSlipTrn.eadSlipId);
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 売上伝票DTOの内容を入出庫伝票DTO明細行に設定します.
	 * @param eadSlipId 追加対象入出庫伝票番号
	 * @param dto 登録元売上伝票DTO
	 * @param lineDto 登録元売上伝票明細行DTO
	 * @param lineNo 登録行番号
	 * @return 登録対象入出庫入力DTO
	 * @throws ServiceException
	 */
	protected EadLineTrnDto setToDto(
				String eadSlipId,
				SalesSlipDto dto, SalesLineDto lineDto,
				Integer lineNo) throws ServiceException {

		EadLineTrnDto eadLineTrnDto = new EadLineTrnDto();

		
		eadLineTrnDto.eadLineId = Long.toString(seqMakerService
				.nextval(EadService.Table.EAD_LINE_TRN));
		
		eadLineTrnDto.eadSlipId = eadSlipId;

		
		Rack rack = rackService.findById(lineDto.rackCodeSrc);
		if( rack == null ){
			String strLabel = MessageResourcesUtil.getMessage("labels.product.csv.rackCode");
			String strMsg = MessageResourcesUtil.getMessage("errors.line.invalid");
			strMsg = strMsg.replace("{0}", lineDto.lineNo);
			strMsg = strMsg.replace("{1}", strLabel);
			throw new ServiceException(strMsg);
		}
		
		eadLineTrnDto.lineNo = lineNo.toString();

		
		eadLineTrnDto.productCode = lineDto.productCode;

		
		eadLineTrnDto.productAbstract = lineDto.productAbstract;

		
		Converter conv = createProductNumConverter();
		Number num = (Number) conv.getAsObject(lineDto.quantity);
		eadLineTrnDto.quantity = num.toString();

		
		eadLineTrnDto.remarks = dto.salesSlipId + "-" + lineDto.lineNo;

		
		eadLineTrnDto.rackCode = lineDto.rackCodeSrc;

		
		eadLineTrnDto.rackName = rack.rackName;

		
		eadLineTrnDto.salesLineId = lineDto.salesLineId;

		
		eadLineTrnDto.supplierLineId = null;

		return eadLineTrnDto;
	}

	/**
	 * 売上伝票明細行DTOの内容を入出庫伝票明細行DTOに設定
	 * @param eadSlipId 追加対象入出庫伝票番号
	 * @param dto 登録元売上伝票DTO
	 * @param lineDto 登録元売上伝票明細行DTO
	 * @param pj 商品情報エンティティ
	 * @param psj セット商品情報エンティティ
	 * @param lineNo 登録行番号
	 * @return 登録対象入出庫伝票明細行DTO
	 * @throws ServiceException
	 */
	protected EadLineTrnDto setToDto(
				String eadSlipId,
				SalesSlipDto dto, SalesLineDto lineDto,
				ProductJoin pj, ProductSetJoin psj,
				Integer lineNo) throws ServiceException {

		EadLineTrnDto eadLineTrnDto = new EadLineTrnDto();

		
		eadLineTrnDto.eadLineId = Long.toString(seqMakerService
				.nextval(EadService.Table.EAD_LINE_TRN));
		
		eadLineTrnDto.eadSlipId = eadSlipId;

		
		eadLineTrnDto.lineNo = lineNo.toString();

		
		eadLineTrnDto.productCode = pj.productCode;

		
		eadLineTrnDto.productAbstract = pj.productName;

		
		Double quantity = Double.valueOf(lineDto.quantity);
		Double setQuantity = psj.quantity.doubleValue();
		String setNum = Double.toString( quantity * setQuantity );
		Converter conv = createProductNumConverter();
		Number num = (Number) conv.getAsObject( setNum );
		eadLineTrnDto.quantity = num.toString();

		
		eadLineTrnDto.remarks = dto.salesSlipId + "-" + lineDto.lineNo
						+ " " + lineDto.productCode + ":" + lineDto.productAbstract;

		
		eadLineTrnDto.rackCode = pj.rackCode;

		
		eadLineTrnDto.rackName = pj.rackName;

		
		eadLineTrnDto.salesLineId = lineDto.salesLineId;

		
		eadLineTrnDto.supplierLineId = null;

		return eadLineTrnDto;
	}
}
