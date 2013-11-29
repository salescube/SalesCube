/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.entity.CustomerRel;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 顧客・納入先関連情報のサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class CustomerRelService extends AbstractService<CustomerRel> {

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String CUSTOMER_CODE = "customerCode";

		public static final String REL_CODE = "relCode";

		public static final String CUST_REL_CATEGORY = "custRelCategory";
	}

	/**
	 * 顧客コードを指定して、顧客・納入先関連情報のリストを返します.
	 * @param customerCode 顧客コード
	 * @return 顧客・納入先関連情報のリスト
	 * @throws ServiceException
	 */
	public List<CustomerRel> findCustomerRelByCustomerCode(String customerCode) throws ServiceException {

		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(CustomerRelService.Param.CUSTOMER_CODE, customerCode);
			List<CustomerRel> result = this.selectBySqlFile(
					CustomerRel.class, "customerrel/FindCustomerRelByCustomerCode.sql",
					param).getResultList();
			return result;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 顧客・納入先関連情報を削除します.
	 * @param customerCode 顧客コード
	 * @param relCode 関連コード
	 * @param custRelCategory 関連カテゴリ
	 * @throws ServiceException
	 */
	public void deleteCustomerRel(String customerCode, String relCode,
			String custRelCategory) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(CustomerRelService.Param.CUSTOMER_CODE, customerCode);
			param.put(CustomerRelService.Param.REL_CODE, relCode);
			param.put(CustomerRelService.Param.CUST_REL_CATEGORY,
					custRelCategory);

			super.updateAudit(CustomerRel.TABLE_NAME, new String[] {
					Param.CUSTOMER_CODE, Param.REL_CODE,
					Param.CUST_REL_CATEGORY }, new Object[] { customerCode,
					relCode, custRelCategory });

			// 削除
			this.updateBySqlFile("customerrel/DeleteCustomerRel.sql", param)
					.execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 顧客・納入先関連情報を登録します.
	 * @param customerRel 顧客・納入先関連情報
	 * @throws ServiceException
	 */
	public void insertCustomerRel(CustomerRel customerRel) throws ServiceException {
		try {
			Map<String, Object> paramS = super.createSqlParam();

			// 納入先ＩＤの発番
			BeanMap param = Beans.createAndCopy(BeanMap.class, customerRel).execute();
			param.putAll(paramS);

			this.updateBySqlFile("customerrel/InsertCustomerRel.sql", param).execute();

		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}
}
