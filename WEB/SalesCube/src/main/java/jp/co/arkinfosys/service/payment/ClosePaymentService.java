/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.payment;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.entity.AptBalanceTrn;
import jp.co.arkinfosys.entity.PaymentSlipTrn;
import jp.co.arkinfosys.entity.PoLineTrn;
import jp.co.arkinfosys.entity.Supplier;
import jp.co.arkinfosys.entity.SupplierSlipTrn;
import jp.co.arkinfosys.entity.join.SupplierJoin;
import jp.co.arkinfosys.entity.join.SupplierSlipLineJoin;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.AptBalanceService;
import jp.co.arkinfosys.service.SeqMakerService;
import jp.co.arkinfosys.service.SupplierService;
import jp.co.arkinfosys.service.SupplierSlipService;
import jp.co.arkinfosys.service.YmService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

/**
 * 支払実績締処理サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class ClosePaymentService extends AbstractService<AptBalanceTrn> {
	@Resource
	private SeqMakerService seqMakerService;

	@Resource
	private AptBalanceService aptBalanceService;

	@Resource
	private SupplierService supplierService;

	@Resource
	private YmService ymService;

	@Resource
	private SupplierSlipService supplierSlipService;

	/**
	 * パラメータ定義クラスです.
	 */
    public static class Param {
    	// 共通
    	public static final String SUPPLIER_CODE = "supplierCode";

    	// 買掛残高
    	public static final String APT_CUTOFF_DATE = "aptCutoffDate";
    	public static final String APT_BALANCE_ID = "aptBalanceId";
    	public static final String APT_ANNUAL = "aptAnnual";
    	public static final String APT_MONTHLY = "aptMonthly";
    	public static final String APT_YM = "aptYm";
    	public static final String USER_ID = "userId";
    	public static final String USER_NAME = "userName";
    	public static final String SUPPLIER_NAME = "supplierName";
    	public static final String PRODUCT_CODE = "productCode";
    	public static final String PRODUCT_NAME = "productName";
    	public static final String SUPPLIER_PCODE = "supplierPcode";
    	public static final String QUANTITY = "quantity";
    	public static final String UNIT_PRICE = "unitPrice";
    	public static final String PRICE = "price";
    	public static final String DOL_UNIT_PRICE = "dolUnitPrice";
    	public static final String DOL_PRICE = "dolPrice";

    	// 支払伝票
    	public static final String PAYMENT_SLIP_ID = "paymentSlipId";
    	public static final String PAYMENT_STATUS = "status";
    	public static final String PAYMENT_CUTOFF_DATE = "paymentCutoffDate";
    	public static final String PAYMENT_PDATE = "paymentPdate";
    	// 仕入伝票
    	public static final String SUPPLIER_SLIP_ID = "supplierSlipId";
    	public static final String SUPPLIER_LINE_ID = "supplierLineId";
    	public static final String SUPPLIER_SLIP_STATUS_UNPAID = "unpaid";
    	public static final String SUPPLIER_SLIP_STATUS_PAID = "paid";
    	public static final String SUPPLIER_LINE_STATUS_UNPAID = "unpaid";
    	public static final String SUPPLIER_LINE_STATUS_PAID = "paid";
    	public static final String SUPPLIER_DATE = "supplierDate";
    	public static final String SUPPLIER_CUTOFF_DATE = "supplierCutoffDate";
    	// 発注伝票
    	public static final String PO_SLIP_ID = "poSlipId";
    	public static final String PO_LINE_ID = "poLineId";
    	public static final String PORDER_SLIP_STATUS_PURCHASED = "porderSlipStatusPurchased";
    }

    /**
     * テーブル名定義クラスです.
     *
     */
	public static class Table {
		/** テーブル名：買掛残高 */
		private static final String APT_BALANCE_TRN = "APT_BALANCE_TRN";
	}

	/**
	 * 支払実績締処理を取り消します.
	 * <p>
	 * 以下のテーブルを更新します.<br>
	 * ・買掛残高<br>
	 * ・支払伝票<br>
	 * ・仕入伝票<br>
	 * ・仕入先マスタ
	 * </p>
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	public void reopenPayment() throws ServiceException,UnabledLockException{
		try{
			// 最終締日を取得
			Date latestAptCutoffDate = aptBalanceService.findLatestAptBalance();

			//***** 買掛残高更新 *****
			deleteAptBalance(latestAptCutoffDate);

			//***** 支払伝票更新 *****
			updatePaymentSlipReopen(latestAptCutoffDate);

			//***** 仕入伝票更新 *****
			updateSupplierSlipReopen(latestAptCutoffDate);

			//***** 仕入先マスタの最終締処理日を元に戻す *****
			List<Supplier> supplierList = supplierService.findAllSupplier();
			for(Supplier supplier : supplierList){
				// 仕入先ごとに仕入伝票から最新の締処理日を取得する
				SupplierSlipTrn supplierSlipTrn = supplierSlipService.findLatestCutoffDateBySupplierCode(supplier.supplierCode);

				Date lastCutoffDate = null;
				if(supplierSlipTrn != null){
					lastCutoffDate = supplierSlipTrn.paymentCutoffDate;
				}

				// 仕入先マスタの最終締処理日を更新する
				supplierService.updateLastCutoffDate(supplier.supplierCode,convertUtilDateToSqlDate(lastCutoffDate));
			}
		}catch(Exception e){
			throw new ServiceException(e);
		}
	}

	/**
	 * 対象日の買掛残高データを削除します.
	 * @param date 取り消し対象日
	 */
	public void deleteAptBalance(Date date) {
		super.updateAudit(AptBalanceTrn.TABLE_NAME, new String[] { super
				.convertVariableNameToColumnName(Param.APT_CUTOFF_DATE) },
				new Object[] { date });

		Map<String, Object> param = super.createSqlParam();
		param.put(ClosePaymentService.Param.APT_CUTOFF_DATE, date);
		this.updateBySqlFile("payment/DeleteCutOffDateOfAptBalance.sql", param)
				.execute();
	}

	/**
	 * 締解除で更新対象となる支払伝票を取得します.
	 *
	 * @param date 取り消し対象日
	 * @return {@link PaymentSlipTrn}のリスト
	 */
	public List<PaymentSlipTrn> selectPaymentSlipReopen(Date date){
    	Map<String, Object> param = super.createSqlParam();
    	param.put(ClosePaymentService.Param.PAYMENT_CUTOFF_DATE, date);

    	return this.selectBySqlFile(PaymentSlipTrn.class, "payment/FindPaymentSlipIdBeforeReopen.sql", param).getResultList();
	}

	/**
	 * 締解除で更新対象となる仕入伝票を取得します.
	 *
	 * @param date 取り消し対象日
	 * @return {@link SupplierSlipTrn}のリスト
	 */
	public List<SupplierSlipTrn> selectSupplierSlipReopen(Date date){
    	Map<String, Object> param = super.createSqlParam();
    	param.put(ClosePaymentService.Param.PAYMENT_CUTOFF_DATE, date);

    	return this.selectBySqlFile(SupplierSlipTrn.class, "payment/FindSupplierSlipIdBeforeReopen.sql", param).getResultList();
	}

	/**
	 * 支払伝票に対する締解除処理を行います.
	 *
	 * @param date 取り消し対象日
	 */
	public void updatePaymentSlipReopen(Date date){
    	Map<String, Object> param = super.createSqlParam();
    	param.put(ClosePaymentService.Param.PAYMENT_CUTOFF_DATE, date);
    	param.put(ClosePaymentService.Param.PAYMENT_STATUS, Constants.STATUS_PAYMENT_SLIP.PAID);

    	this.updateBySqlFile("payment/UpdatePaymentSlipReopen.sql", param).execute();
	}

	/**
	 * 仕入伝票に対する締解除処理を行います.
	 *
	 * @param date 取り消し対象日
	 */
	public void updateSupplierSlipReopen(Date date){
    	Map<String, Object> param = super.createSqlParam();
    	param.put(ClosePaymentService.Param.PAYMENT_CUTOFF_DATE, date);

    	this.updateBySqlFile("payment/UpdateSupplierSlipReopen.sql", param).execute();
	}

	/**
	 * 支払締処理を実行します.
	 * <p>
	 * 全仕入先について以下のように締処理を行います.<br>
	 * 1.支払伝票更新<br>
	 * 2.仕入伝票更新<br>
	 * 3.買掛残高設定<br>
	 * 4.仕入先マスタ更新
	 * </p>
	 * @param cutoffDate 締年月日
	 * @throws ServiceException
	 */
	public void closePayment(Date cutoffDate) throws ServiceException{
		// 仕入先マスタから全仕入先を取得
		List<Supplier> supplierList = supplierService.findAllSupplier();

		java.sql.Date cutoffDateSql = super.convertUtilDateToSqlDate(cutoffDate);

		// 全仕入先について処理を実施
		for(Supplier supplier : supplierList){
			//***** 支払伝票更新 *****
			updatePaymentSlipClose(supplier,cutoffDateSql);

			//***** 仕入伝票更新 *****
			updateSupplierSlipClose(supplier,cutoffDateSql);

			// 仕入伝票明細行から買掛残高作成用データ取得
			List<SupplierSlipLineJoin> supplierLineList = selectSupplierLineTrnClose(supplier,cutoffDateSql);

			// 買掛残高設定
			insertAptBalanceClose(supplierLineList,cutoffDateSql);

			// 仕入先マスタの最終締処理日を更新
			supplierService.updateLastCutoffDate(supplier.supplierCode, cutoffDateSql);
		}
	}

	/**
	 * 支払伝票に対する締処理を行います.
	 *
	 * @param supplier 仕入先情報
	 * @param cutoffDate 締年月日
	 */
	private void updatePaymentSlipClose(Supplier supplier,Date cutoffDate){
    	Map<String, Object> param = super.createSqlParam();
    	param.put(ClosePaymentService.Param.PAYMENT_STATUS,Constants.STATUS_PAYMENT_SLIP.CUTOFF);// 支払締め
    	param.put(ClosePaymentService.Param.SUPPLIER_CODE,supplier.supplierCode);
    	param.put(ClosePaymentService.Param.PAYMENT_CUTOFF_DATE,cutoffDate);

		this.updateBySqlFile("payment/UpdatePaymentSlipClose.sql", param).execute();
	}

	/**
	 * 仕入伝票に対する締処理を行います.
	 *
	 * @param supplier 仕入先情報
	 * @param cutoffDate 締年月日
	 */
	private void updateSupplierSlipClose(Supplier supplier,Date cutoffDate){
    	Map<String, Object> param = super.createSqlParam();
    	param.put(ClosePaymentService.Param.SUPPLIER_CUTOFF_DATE,cutoffDate);
    	param.put(ClosePaymentService.Param.SUPPLIER_CODE,supplier.supplierCode);

    	this.updateBySqlFile("payment/UpdateSupplierSlipClose.sql", param).execute();
	}

	/**
	 * 締処理で対象となる仕入伝票明細行情報を取得します.
	 * <p>
	 * 仕入先コードで絞り込まれた仕入伝票の中から、未払いの仕入伝票と仕入伝票明細行の情報を取得します.
	 * </p>
	 * @param supplier 仕入先情報
	 * @param cutoffDate 締年月日
	 * @return {@link SupplierSlipLineJoin}のリスト
	 */
	private List<SupplierSlipLineJoin> selectSupplierLineTrnClose(Supplier supplier,Date cutoffDate){
    	Map<String, Object> param = super.createSqlParam();
    	param.put(ClosePaymentService.Param.SUPPLIER_CUTOFF_DATE,cutoffDate);
    	param.put(ClosePaymentService.Param.SUPPLIER_CODE,supplier.supplierCode);
    	param.put(ClosePaymentService.Param.SUPPLIER_LINE_STATUS_UNPAID, Constants.STATUS_SUPPLIER_LINE.UNPAID); // 未払い
    	param.put(ClosePaymentService.Param.SUPPLIER_LINE_STATUS_PAID, Constants.STATUS_SUPPLIER_LINE.PAID); // 支払済

    	// 仕入伝票明細行テーブルから情報を取得する
    	return this.selectBySqlFile(SupplierSlipLineJoin.class, "payment/FindSupplierLineTrnForClose.sql", param).getResultList();
	}

	/**
	 * 買掛残高情報を登録します.
	 * <p>
	 * 仕入伝票情報に紐づく発注伝票情報を取得して、買掛残高情報を登録します.
	 * </p>
	 * @param supplierLineList 仕入伝票情報リスト
	 * @param cutoffDate 締年月日
	 */
	private void insertAptBalanceClose(List<SupplierSlipLineJoin> supplierLineList,Date cutoffDate) throws ServiceException{
		// 発注伝票明細行から仕入伝票情報を取得し、買掛残高に設定
		for(SupplierSlipLineJoin supplierSlipLineJoin  : supplierLineList){
	    	Map<String, Object> param = super.createSqlParam();
			param.put(ClosePaymentService.Param.PO_LINE_ID,supplierSlipLineJoin.poLineId);

			// 仕入伝票明細行に紐づく発注伝票明細行情報を取得
			PoLineTrn poLineTrn = this.selectBySqlFile(PoLineTrn.class,
					"payment/FindPoLineTrnByIdForClose.sql", param).getSingleResult();

			// 発注伝票明細行情報が存在する場合のみ処理対象とする
			if(poLineTrn != null){
				// 買掛残高にデータを設定(履歴も設定)
				insertAptBalance(poLineTrn,supplierSlipLineJoin,cutoffDate);
			}
		}
	}

	/**
	 * 買掛残高情報を登録します.
	 * @param poLineTrn 発注伝票明細行情報
	 * @param supplierSlipLineJoin 仕入伝票明細行情報
	 * @param cutoffDate 締年月日
	 * @throws ServiceException
	 */
	private void insertAptBalance(PoLineTrn poLineTrn, SupplierSlipLineJoin supplierSlipLineJoin,Date cutoffDate) throws ServiceException{
    	Map<String, Object> param = super.createSqlParam();

    	// パラメータ設定
	    try{
	    	// 買掛残高ID
	    	long aptBalanceId = seqMakerService.nextval(Table.APT_BALANCE_TRN);
	    	param.put(ClosePaymentService.Param.APT_BALANCE_ID,aptBalanceId);
	    	// 年度、月度、年月度
	    	YmDto ymDto = ymService.getYm(cutoffDate);
	    	if(ymDto != null){
		    	param.put(ClosePaymentService.Param.APT_ANNUAL,ymDto.annual);
		    	param.put(ClosePaymentService.Param.APT_MONTHLY,ymDto.monthly);
		    	param.put(ClosePaymentService.Param.APT_YM,ymDto.ym);
	    	}
	    	// 買掛締日
	    	param.put(ClosePaymentService.Param.APT_CUTOFF_DATE,cutoffDate);
	    	// 担当者
	    	param.put(ClosePaymentService.Param.USER_ID,userDto.userId);
	    	param.put(ClosePaymentService.Param.USER_NAME,userDto.nameKnj);
	    	// 仕入先
	    	param.put(ClosePaymentService.Param.SUPPLIER_CODE,supplierSlipLineJoin.supplierCode);
	    	SupplierJoin supplierJoin = supplierService.findById(supplierSlipLineJoin.supplierCode);
	    	param.put(ClosePaymentService.Param.SUPPLIER_NAME,supplierJoin.supplierName);
	    	// 発注伝票明細行
	    	param.put(ClosePaymentService.Param.PRODUCT_CODE,poLineTrn.productCode);
	    	param.put(ClosePaymentService.Param.PRODUCT_NAME,poLineTrn.productAbstract);
	    	param.put(ClosePaymentService.Param.SUPPLIER_PCODE,poLineTrn.supplierPcode);
	    	param.put(ClosePaymentService.Param.PO_SLIP_ID,poLineTrn.poSlipId);
	    	param.put(ClosePaymentService.Param.PO_LINE_ID,poLineTrn.poLineId);
	    	// 仕入伝票明細行
	    	param.put(ClosePaymentService.Param.QUANTITY,supplierSlipLineJoin.quantity);
	    	param.put(ClosePaymentService.Param.UNIT_PRICE,supplierSlipLineJoin.unitPrice);
	    	param.put(ClosePaymentService.Param.PRICE,supplierSlipLineJoin.price);
	    	param.put(ClosePaymentService.Param.DOL_UNIT_PRICE,supplierSlipLineJoin.dolUnitPrice);
	    	param.put(ClosePaymentService.Param.DOL_PRICE,supplierSlipLineJoin.dolPrice);
	    	param.put(ClosePaymentService.Param.SUPPLIER_SLIP_ID,supplierSlipLineJoin.supplierSlipId);
	    	param.put(ClosePaymentService.Param.SUPPLIER_LINE_ID,supplierSlipLineJoin.supplierLineId);
	    	param.put(ClosePaymentService.Param.SUPPLIER_DATE,supplierSlipLineJoin.supplierDate);
	    	param.put(ClosePaymentService.Param.PRICE,supplierSlipLineJoin.price);

	    	// 買掛残高テーブルにデータを設定
	    	this.updateBySqlFile("payment/InsertAptBalanceForClose.sql", param).execute();
		}catch(Exception e){
			throw new ServiceException(e);
		}

	}
}
