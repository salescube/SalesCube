/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.entity.CustomerRel;
import jp.co.arkinfosys.entity.Delivery;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 納入先のサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class DeliveryService extends AbstractService<DeliveryAndPre> {
	@Resource
	private SeqMakerService seqMakerService;

	@Resource
	private CustomerRelService customerRelService;

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		//
		// 得意先関連
		//
		// 顧客コード
		public static final String CUSTOMER_CODE = "customerCode";
		// 顧客名
		public static final String CUSTOMER_NAME = "customerName";
		// 売上取引区分
		public static final String SALES_CM_CATEGORY = "salesCmCategory";
		// 売上取引区分名
		public static final String SALES_CM_CATEGORY_NAME = "salesCmCategoryName";

		// 得意先関連区分
		public static final String CUST_REL_CATEGORY = "custRelCategory";

		//
		// 納入先
		//
		public static final String DELIVERY_CODE = "deliveryCode";

		public static final String DELIVERY_NAME = "deliveryName";

		public static final String DELIVERY_KANA = "deliveryKana";

		public static final String DELIVERY_OFFICE_NAME = "deliveryOfficeName";

		public static final String DELIVERY_OFFICE_KANA = "deliveryOfficeKana";

		public static final String DELIVERY_DEPT_NAME = "deliveryDeptName";

		public static final String DELIVERY_ZIP_CODE = "deliveryZipCode";

		public static final String DELIVERY_ADDRESS_1 = "deliveryAddress1";

		public static final String DELIVERY_ADDRESS_2 = "deliveryAddress2";

		public static final String DELIVERY_PC_NAME = "deliveryPcName";

		public static final String DELIVERY_PC_KANA = "deliveryPcKana";

		public static final String DELIVERY_PC_PRE_CATEGORY = "deliveryPcPreCategory";

		public static final String DELIVERY_TEL = "deliveryTel";

		public static final String DELIVERY_FAX = "deliveryFax";

		public static final String DELIVERY_EMAIL = "deliveryEmail";

		public static final String DELIVERY_URL = "deliveryUrl";

		public static final String REMARKS = "remarks";

		public static final String CRE_DATETM = "creDatetm";

		public static final String UPD_DATETM = "updDatetm";

		// 区分データの区分名
		public static final String CATEGORY_CODE_NAME = "categoryCodeName";

		private static final String SORT_COLUMN_DELIVERY_CODE = "sortColumnDeleveryCode";

		private static final String SORT_COLUMN_DELIVERY_NAME = "sortColumnDeleveryName";

		private static final String SORT_COLUMN_CRE_DATE = "sortColumnCreDate";

		private static final String SORT_COLUMN_UPD_DATE = "sortColumnUpdDate";

		private static final String SORT_ORDER = "sortOrder";

		private static final String CATEGORY_ID = "categoryId";

		private static final String CATEGORY_ID2 = "categoryId2";

		private static final String CUSTOMER_PC_PRE_CATEGORY_NAME = "customerPcPreCategoryName";

		private static final String SALES_SLIP_CATEGORY = "salesSlipCategory";
	}

	public String[] params = { Param.CUSTOMER_CODE, Param.CUSTOMER_NAME,
			Param.SALES_CM_CATEGORY, Param.SALES_CM_CATEGORY_NAME,
			Param.CUST_REL_CATEGORY, Param.DELIVERY_CODE, Param.DELIVERY_NAME,
			Param.DELIVERY_KANA, Param.DELIVERY_OFFICE_NAME,
			Param.DELIVERY_OFFICE_KANA, Param.DELIVERY_DEPT_NAME,
			Param.DELIVERY_ZIP_CODE, Param.DELIVERY_ADDRESS_1,
			Param.DELIVERY_ADDRESS_2, Param.DELIVERY_PC_NAME,
			Param.DELIVERY_PC_KANA, Param.DELIVERY_PC_PRE_CATEGORY,
			Param.DELIVERY_TEL, Param.DELIVERY_FAX, Param.DELIVERY_EMAIL,
			Param.DELIVERY_URL, Param.REMARKS, Param.CRE_DATETM,
			Param.UPD_DATETM, Param.CATEGORY_CODE_NAME,
			Param.SORT_COLUMN_DELIVERY_CODE, Param.SORT_COLUMN_DELIVERY_NAME,
			Param.SORT_COLUMN_CRE_DATE, Param.SORT_COLUMN_UPD_DATE,
			Param.SORT_ORDER, Param.CATEGORY_ID, Param.CATEGORY_ID2,
			Param.CUSTOMER_PC_PRE_CATEGORY_NAME, Param.SALES_SLIP_CATEGORY };
	//
	// ソート条件に指定時に使用するカラム名
	//

	/**
	 * 納入先コードのカラム名
	 */
	private static final String COLUMN_DELIVERY_CODE = "DELIVERY_CODE";
	/**
	 * 納入先名のカラム名
	 */
	private static final String COLUMN_DELIVERY_NAME = "DELIVERY_NAME";
	public static final String SORT_COLUMN_CREATE_DATE = "CRE_DATETM";

	/**
	 * 検索条件およびソート条件を指定して、納入先情報のリストを返します.
	 * @param conditions 検索条件
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順か否か
	 * @return 納入先情報{@link DeliveryAndPre}のリスト
	 * @throws ServiceException
	 */
	public List<DeliveryAndPre> findDeliveryAndPreByCompleteCode(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc) throws ServiceException {

		return findDeliveryCondition(conditions, sortColumn, sortOrderAsc, true);
	}

	/**
	 * 検索条件、ソート条件、取得件数および取得開始位置を指定して、納入先情報のリストを返します.
	 * @param conditions 検索条件
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順か否か
	 * @return 納入先情報{@link DeliveryAndPre}のリスト
	 * @throws ServiceException
	 */
	public List<DeliveryAndPre> findDeliveryAndPreByCondition(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc) throws ServiceException {

		return findDeliveryCondition(conditions, sortColumn, sortOrderAsc,
				false);

	}

	/**
	 * 検索条件、ソート条件および締済みか否かを指定して、納入先情報のリストを返します.
	 * @param conditions 検索条件
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順か否か
	 * @param complete 締済みか否か
	 * @return 納入先情報{@link DeliveryAndPre}のリスト
	 * @throws ServiceException
	 */
	public List<DeliveryAndPre> findDeliveryCondition(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc, boolean complete) throws ServiceException {
		if (conditions == null) {
			return new ArrayList<DeliveryAndPre>();
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			// 顧客コード
			if (complete == true) {
				if (conditions.containsKey(DeliveryService.Param.CUSTOMER_CODE)) {
					param.put(DeliveryService.Param.CUSTOMER_CODE, conditions
							.get(DeliveryService.Param.CUSTOMER_CODE));
				}
			} else {
				if (conditions.containsKey(DeliveryService.Param.CUSTOMER_CODE)) {
					param.put(DeliveryService.Param.CUSTOMER_CODE, super
							.createPrefixSearchCondition((String) conditions
									.get(DeliveryService.Param.CUSTOMER_CODE)));
				}
			}
			// 得意先関連区分
			if (conditions.containsKey(DeliveryService.Param.CUST_REL_CATEGORY)) {
				param.put(DeliveryService.Param.CUST_REL_CATEGORY, conditions
						.get(DeliveryService.Param.CUST_REL_CATEGORY));
			}

			// 納入先コード
			if (complete == true) {
				if (conditions.containsKey(DeliveryService.Param.DELIVERY_CODE)) {
					param.put(DeliveryService.Param.DELIVERY_CODE, conditions
							.get(DeliveryService.Param.DELIVERY_CODE));
				}
			} else {
				if (conditions.containsKey(DeliveryService.Param.DELIVERY_CODE)) {
					param.put(DeliveryService.Param.DELIVERY_CODE, super
							.createPrefixSearchCondition((String) conditions
									.get(DeliveryService.Param.DELIVERY_CODE)));
				}
			}
			// 納入先名
			if (conditions.containsKey(DeliveryService.Param.DELIVERY_NAME)) {
				param.put(DeliveryService.Param.DELIVERY_NAME, super
						.createPartialSearchCondition((String) conditions
								.get(DeliveryService.Param.DELIVERY_NAME)));
			}
			// 納入先名カナ
			if (conditions.containsKey(DeliveryService.Param.DELIVERY_KANA)) {
				param.put(DeliveryService.Param.DELIVERY_KANA, super
						.createPartialSearchCondition((String) conditions
								.get(DeliveryService.Param.DELIVERY_KANA)));
			}
			// 事業所名
			if (conditions
					.containsKey(DeliveryService.Param.DELIVERY_OFFICE_NAME)) {
				param
						.put(
								DeliveryService.Param.DELIVERY_OFFICE_NAME,
								super
										.createPartialSearchCondition((String) conditions
												.get(DeliveryService.Param.DELIVERY_OFFICE_NAME)));
			}
			// 事業所名カナ
			if (conditions
					.containsKey(DeliveryService.Param.DELIVERY_OFFICE_KANA)) {
				param
						.put(
								DeliveryService.Param.DELIVERY_OFFICE_KANA,
								super
										.createPartialSearchCondition((String) conditions
												.get(DeliveryService.Param.DELIVERY_OFFICE_KANA)));
			}
			// 部署名
			if (conditions
					.containsKey(DeliveryService.Param.DELIVERY_DEPT_NAME)) {
				param
						.put(
								DeliveryService.Param.DELIVERY_DEPT_NAME,
								super
										.createPartialSearchCondition((String) conditions
												.get(DeliveryService.Param.DELIVERY_DEPT_NAME)));
			}
			// 郵便番号
			if (conditions.containsKey(DeliveryService.Param.DELIVERY_ZIP_CODE)) {
				param.put(DeliveryService.Param.DELIVERY_ZIP_CODE, super
						.createPrefixSearchCondition((String) conditions
								.get(DeliveryService.Param.DELIVERY_ZIP_CODE)));
			}
			// 住所１
			if (conditions
					.containsKey(DeliveryService.Param.DELIVERY_ADDRESS_1)) {
				param
						.put(
								DeliveryService.Param.DELIVERY_ADDRESS_1,
								super
										.createPartialSearchCondition((String) conditions
												.get(DeliveryService.Param.DELIVERY_ADDRESS_1)));
			}
			// 住所２
			if (conditions
					.containsKey(DeliveryService.Param.DELIVERY_ADDRESS_2)) {
				param
						.put(
								DeliveryService.Param.DELIVERY_ADDRESS_2,
								super
										.createPartialSearchCondition((String) conditions
												.get(DeliveryService.Param.DELIVERY_ADDRESS_2)));
			}
			// 納入先担当者
			if (conditions.containsKey(DeliveryService.Param.DELIVERY_PC_NAME)) {
				param.put(DeliveryService.Param.DELIVERY_PC_NAME, super
						.createPartialSearchCondition((String) conditions
								.get(DeliveryService.Param.DELIVERY_PC_NAME)));
			}
			// 納入先担当者カナ
			if (conditions.containsKey(DeliveryService.Param.DELIVERY_PC_KANA)) {
				param.put(DeliveryService.Param.DELIVERY_PC_KANA, super
						.createPartialSearchCondition((String) conditions
								.get(DeliveryService.Param.DELIVERY_PC_KANA)));
			}

			// 納入先担当者敬称区分
			if (conditions
					.containsKey(DeliveryService.Param.DELIVERY_PC_PRE_CATEGORY)) {
				param
						.put(
								DeliveryService.Param.DELIVERY_PC_PRE_CATEGORY,
								conditions
										.get(DeliveryService.Param.DELIVERY_PC_PRE_CATEGORY));
			}
			// 電話番号
			if (conditions.containsKey(DeliveryService.Param.DELIVERY_TEL)) {
				param.put(DeliveryService.Param.DELIVERY_TEL, super
						.createPrefixSearchCondition((String) conditions
								.get(DeliveryService.Param.DELIVERY_TEL)));
			}
			// FAX番号
			if (conditions.containsKey(DeliveryService.Param.DELIVERY_FAX)) {
				param.put(DeliveryService.Param.DELIVERY_FAX, super
						.createPrefixSearchCondition((String) conditions
								.get(DeliveryService.Param.DELIVERY_FAX)));
			}
			// E-Mail
			if (conditions.containsKey(DeliveryService.Param.DELIVERY_EMAIL)) {
				param.put(DeliveryService.Param.DELIVERY_EMAIL, super
						.createPrefixSearchCondition((String) conditions
								.get(DeliveryService.Param.DELIVERY_EMAIL)));
			}
			// URL
			if (conditions.containsKey(DeliveryService.Param.DELIVERY_URL)) {
				param.put(DeliveryService.Param.DELIVERY_URL, super
						.createPrefixSearchCondition((String) conditions
								.get(DeliveryService.Param.DELIVERY_URL)));
			}
			// 備考
			if (conditions.containsKey(DeliveryService.Param.REMARKS)) {
				param.put(DeliveryService.Param.REMARKS, super
						.createPartialSearchCondition((String) conditions
								.get(DeliveryService.Param.REMARKS)));
			}
			// 敬称
			if (conditions
					.containsKey(DeliveryService.Param.CUSTOMER_PC_PRE_CATEGORY_NAME)) {
				param
						.put(
								DeliveryService.Param.CUSTOMER_PC_PRE_CATEGORY_NAME,
								super
										.createPartialSearchCondition((String) conditions
												.get(DeliveryService.Param.CUSTOMER_PC_PRE_CATEGORY_NAME)));
			}

			// 売上伝票種別
			if (conditions
					.containsKey(DeliveryService.Param.SALES_SLIP_CATEGORY)) {
				param
						.put(
								DeliveryService.Param.SALES_SLIP_CATEGORY,
								super
										.createPartialSearchCondition((String) conditions
												.get(DeliveryService.Param.SALES_SLIP_CATEGORY)));
			}

			// 区分IDは固定で敬称を指定する
			param.put(DeliveryService.Param.CATEGORY_ID, Categories.PRE_TYPE);

			// 売上取引区分IDは固定で指定する
			param.put(DeliveryService.Param.CATEGORY_ID2,
					Categories.SALES_CM_CATEGORY);

			// ソートカラムを設定する

			// 納入先コード
			if (DeliveryService.Param.DELIVERY_CODE.equals(sortColumn)) {
				param.put(DeliveryService.Param.SORT_COLUMN_DELIVERY_CODE,
						DeliveryService.COLUMN_DELIVERY_CODE);
				// 納入先名
			} else if (DeliveryService.Param.DELIVERY_NAME.equals(sortColumn)) {
				param.put(DeliveryService.Param.SORT_COLUMN_DELIVERY_NAME,
						DeliveryService.COLUMN_DELIVERY_NAME);
				// 作成日時
			} else if (DeliveryService.Param.CRE_DATETM.equals(sortColumn)) {
				param.put(DeliveryService.Param.SORT_COLUMN_CRE_DATE, super
						.convertVariableNameToColumnName(Param.CRE_DATETM));
				// 更新日時
			} else if (DeliveryService.Param.UPD_DATETM.equals(sortColumn)) {
				param.put(DeliveryService.Param.SORT_COLUMN_UPD_DATE, super
						.convertVariableNameToColumnName(Param.UPD_DATETM));
			}

			// ソートオーダーを設定する
			if (sortOrderAsc) {
				param.put(DeliveryService.Param.SORT_ORDER, Constants.SQL.ASC);
			} else {
				param.put(DeliveryService.Param.SORT_ORDER, Constants.SQL.DESC);
			}

			return this.selectBySqlFile(DeliveryAndPre.class,
					"delivery/FindDeliveryAndPreByCondition.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を受け取り、初期化して返します.
	 * @param param 検索条件マップ
	 * @return 検索条件キーのみ設定した検索条件マップ
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(DeliveryService.Param.CUSTOMER_CODE, null);
		param.put(DeliveryService.Param.CUST_REL_CATEGORY, null);
		param.put(DeliveryService.Param.DELIVERY_CODE, null);
		param.put(DeliveryService.Param.DELIVERY_NAME, null);
		param.put(DeliveryService.Param.DELIVERY_KANA, null);
		param.put(DeliveryService.Param.DELIVERY_OFFICE_NAME, null);
		param.put(DeliveryService.Param.DELIVERY_OFFICE_KANA, null);
		param.put(DeliveryService.Param.DELIVERY_DEPT_NAME, null);
		param.put(DeliveryService.Param.DELIVERY_ZIP_CODE, null);
		param.put(DeliveryService.Param.DELIVERY_ADDRESS_1, null);
		param.put(DeliveryService.Param.DELIVERY_ADDRESS_2, null);
		param.put(DeliveryService.Param.DELIVERY_PC_NAME, null);
		param.put(DeliveryService.Param.DELIVERY_PC_KANA, null);
		param.put(DeliveryService.Param.DELIVERY_PC_PRE_CATEGORY, null);
		param.put(DeliveryService.Param.DELIVERY_TEL, null);
		param.put(DeliveryService.Param.DELIVERY_FAX, null);
		param.put(DeliveryService.Param.DELIVERY_EMAIL, null);
		param.put(DeliveryService.Param.DELIVERY_URL, null);
		param.put(DeliveryService.Param.REMARKS, null);

		param.put(DeliveryService.Param.SORT_COLUMN_DELIVERY_CODE, null);
		param.put(DeliveryService.Param.SORT_COLUMN_DELIVERY_NAME, null);
		param.put(DeliveryService.Param.SORT_COLUMN_CRE_DATE, null);
		param.put(DeliveryService.Param.SORT_COLUMN_UPD_DATE, null);
		param.put(DeliveryService.Param.SORT_ORDER, null);
		return param;
	}

	/**
	 * 顧客コードを指定して、納入先情報のリストを返します.
	 * @param customerCode 顧客コード
	 * @return 納入先情報{@link DeliveryAndPre}のリスト
	 * @throws ServiceException
	 */
	public List<DeliveryAndPre> searchDeliveryByCompleteCustomerCode(
			String customerCode) throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		conditions.put(DeliveryService.Param.CUSTOMER_CODE, customerCode);
		conditions.put(DeliveryService.Param.CUST_REL_CATEGORY,
				Constants.CUSTOMER_REL.BILLING);

		String sortColumn = DeliveryService.Param.DELIVERY_CODE;
		boolean sortOrderAsc = true;

		// 検索実行
		return findDeliveryAndPreByCompleteCode(conditions, sortColumn,
				sortOrderAsc);
	}

	/**
	 * 納入先コードを指定して、納入先情報を返します.
	 * @param deliveryCode 納入先コード
	 * @return 納入先情報{@link DeliveryAndPre}
	 * @throws ServiceException
	 */
	public DeliveryAndPre searchDeliveryByCompleteDeliveryCode(
			String deliveryCode) throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		conditions.put(DeliveryService.Param.DELIVERY_CODE, deliveryCode);

		String sortColumn = DeliveryService.Param.DELIVERY_CODE;
		boolean sortOrderAsc = true;

		// 検索実行
		List<DeliveryAndPre> list = findDeliveryAndPreByCompleteCode(
				conditions, sortColumn, sortOrderAsc);
		if (list.size() != 1) {
			throw new ServiceException("errors.system");
		}
		return list.get(0);
	}

	/**
	 * 顧客コードを指定して、納入先情報のリストを返します.
	 * @param customerCode 顧客コード
	 * @return 納入先情報{@link DeliveryAndPre}のリスト
	 * @throws ServiceException
	 */
	public List<DeliveryAndPre> searchDeliveryListByCompleteCustomerCode(
			String customerCode) throws ServiceException {
		return searchDeliveryByCompleteCustomerCodeSortedByCreDate(customerCode);
	}

	/**
	 * 顧客コードを指定して、納入先情報のリストを返します.<br>
	 * 取得順序は、作成日の昇順です.
	 * @param customerCode 顧客コード
	 * @return 納入先情報{@link DeliveryAndPre}のリスト
	 * @throws ServiceException
	 */
	public List<DeliveryAndPre> searchDeliveryByCompleteCustomerCodeSortedByCreDate(
			String customerCode) throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		conditions.put(DeliveryService.Param.CUSTOMER_CODE, customerCode);
		conditions.put(DeliveryService.Param.CUST_REL_CATEGORY,
				Constants.CUSTOMER_REL.DELIVERY);

		String sortColumn = DeliveryService.Param.CRE_DATETM;
		boolean sortOrderAsc = true;

		// 検索実行
		return findDeliveryAndPreByCompleteCode(conditions, sortColumn,
				sortOrderAsc);
	}

	/**
	 * 納入先コードを指定して、納入先情報を削除します.
	 * @param deliveryCode 納入先コード
	 * @throws ServiceException
	 */
	public void deleteDelivery(String deliveryCode) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(DeliveryService.Param.DELIVERY_CODE, deliveryCode);

			super.updateAudit(Delivery.TABLE_NAME,
					new String[] { Param.DELIVERY_CODE },
					new Object[] { deliveryCode });

			// 削除
			this.updateBySqlFile("delivery/DeleteDelivery.sql", param)
					.execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 納入先情報を登録します.
	 * @param delivery 納入先DTO
	 * @throws ServiceException
	 */
	public void insertDelivery(DeliveryAndPre delivery) throws ServiceException {
		try {
			Map<String, Object> paramS = super.createSqlParam();

			// 納入先ＩＤの発番
			Long deliveryCode = seqMakerService.nextval(Delivery.TABLE_NAME);
			delivery.deliveryCode = deliveryCode.toString();

			BeanMap param = Beans.createAndCopy(BeanMap.class, delivery)
					.execute();
			param.putAll(paramS);

			this.updateBySqlFile("delivery/InsertDelivery.sql", param)
					.execute();

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 納入先情報を更新します.
	 * @param delivery 納入先DTO
	 * @throws ServiceException
	 */
	public void updateDelivery(DeliveryAndPre delivery) throws ServiceException {
		try {
			Map<String, Object> paramS = super.createSqlParam();

			BeanMap param = Beans.createAndCopy(BeanMap.class, delivery)
					.execute();
			param.putAll(paramS);

			this.updateBySqlFile("delivery/UpdateDelivery.sql", param)
					.execute();

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 顧客コードを指定して、納入先情報を削除します.
	 * @param customerCode 顧客コード
	 * @throws ServiceException
	 */
	public void deleteDeliveryByCustomerCode(String customerCode)
			throws ServiceException {

		// 顧客メンテからのみ操作されるため、排他制御不要
		try {
			// 該当顧客コードに所属する納入先・請求先を取得する
			List<CustomerRel> list = customerRelService
					.findCustomerRelByCustomerCode(customerCode);
			for (CustomerRel customerRel : list) {
				this.deleteDelivery(customerRel.relCode);

				customerRelService.deleteCustomerRel(customerRel.customerCode,
						customerRel.relCode, customerRel.custRelCategory);
			}

		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}
}
