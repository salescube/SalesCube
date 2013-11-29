/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.SlipStatusCategoryTrns;
import jp.co.arkinfosys.entity.AptBalanceTrn;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.payment.ClosePaymentService;

/**
 *
 * 買掛残高サービスクラスです.
 *
 */
public class AptBalanceService extends AbstractService<AptBalanceTrn> {
	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
    public static class Param {
    	// 買掛締日
    	public static final String APT_CUTOFF_DATE = "aptCutoffDate";

    }

    /**
     * 仕入先コードを指定して買掛残高を取得します.
     * @param supplierCode 仕入先コード
     * @return 買掛残高
     * @throws ServiceException
     */
    public BigDecimal calcAptBalanceBySupplierCode(String supplierCode) throws ServiceException {
		try {
			//MAPの生成
			Map<String, Object> param = super.createSqlParam();
			param.put(SupplierService.Param.SUPPLIER_CODE, supplierCode);
			param.put(SupplierSlipService.Param.STATUS, SlipStatusCategoryTrns.SUPPLIER_SLIP_UNPAID);

			return this.selectBySqlFile(BigDecimal.class,
					"aptbalance/CalcAptBalanceBySupplierCode.sql", param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

    /**
	 * 最新の買掛締日を取得します.<br>
	 * 対象データが0件の場合nullを返します.
	 *
     * @return 最新の買掛日
     * @throws ServiceException
     */
    public Date findLatestAptBalance() throws ServiceException{
		Map<String, Object> param = super.createSqlParam();

		AptBalanceTrn aptBalance = this.selectBySqlFile(AptBalanceTrn.class,
				"aptbalance/FindLatestAptBalance.sql", param).getSingleResult();

		if(aptBalance==null){
			return null;
		}
		return aptBalance.aptCutoffDate;
	}

    /**
	 * 引数の締日に締処理された買掛残高の一覧を取得します.
     * @param date 締日
     * @return 買掛残高リスト
     */
	public List<AptBalanceTrn> findAptBalanceListByCutoffDate(Date date){
		Map<String, Object> param = super.createSqlParam();
    	param.put(ClosePaymentService.Param.APT_CUTOFF_DATE, date);

		return this.selectBySqlFile(AptBalanceTrn.class,"aptbalance/FindAptBalanceListByAptCutoffDate.sql",param).getResultList();
	}

}
