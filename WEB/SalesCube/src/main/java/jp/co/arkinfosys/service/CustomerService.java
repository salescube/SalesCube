/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.CustomerDto;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.CustomerRel;
import jp.co.arkinfosys.entity.join.CustomerJoin;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 顧客のサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class CustomerService extends
		AbstractMasterEditService<CustomerDto, CustomerJoin> implements
		MasterSearch<CustomerJoin> {

	@Resource
	private CustomerRelService customerRelService;

	@Resource
	private DeliveryService deliveryService;

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String CUSTOMER_CODE = "customerCode";

		public static final String CUSTOMER_NAME = "customerName";

		public static final String CUSTOMER_KANA = "customerKana";

		public static final String CUSTOMER_OFFICE_NAME = "customerOfficeName";

		public static final String CUSTOMER_OFFICE_KANA = "customerOfficeKana";

		public static final String CUSTOMER_PC_NAME = "customerPcName";

		public static final String CUSTOMER_PC_KANA = "customerPcKana";

		public static final String CUSTOMER_TEL = "customerTel";

		public static final String CUSTOMER_FAX = "customerFax";

		public static final String PAYMENT_NAME = "paymentName";

		private static final String CUSTOMER_RANK_CATEGORY = "customerRankCategory";

		private static final String PAYBACK_CYCLE_CATEGORY = "paybackCycleCategory";

		public static final String CUTOFF_GROUP = "cutoffGroup";

		public static final String REMARKS = "remarks";

		private static final String SORT_COLUMN_CUSTOMER = "sortColumnCustomer";

		private static final String SORT_COLUMN_RANK = "sortColumnRank";

		private static final String SORT_ORDER = "sortOrder";

		private static final String CATEGORY_ID = "categoryId";

		private static final String CATEGORY_ID2 = "categoryId2";

		private static final String CATEGORY_ID3 = "categoryId3";

		private static final String ROW_COUNT = "rowCount";

		private static final String OFFSET = "offsetRow";

		private static final String LAST_CUTOFF_DATE = "lastCutoffDate";

		private static final String SALES_CM_CATEGORY = "salesCmCategory";

		private static final String CUSTOMER_PC_PRE_CATEGORY_NAME = "customerPcPreCategoryName";
		private static final String CATEGORY_ID4 = "categoryId4";

		private static final String SALES_SLIP_CATEGORY = "salesSlipCategory";
		private static final String NOT_YET_REQUESTED_CHECK = "notYetRequestedCheck";

		private static final String CUSTOMER_CODE_FROM = "customerCodeFrom";
		private static final String CUSTOMER_CODE_TO = "customerCodeTo";
		private static final String ART_BALANCE_CHECK_DATE = "artBalanceCheckDate";
		private static final String LAST_SALES_CUTOFF_DATE = "lastSalesCutoffDate";
	}

	public String[] paramNames = { Param.CUSTOMER_CODE, Param.CUSTOMER_NAME,
			Param.CUSTOMER_KANA, Param.CUSTOMER_OFFICE_NAME,
			Param.CUSTOMER_OFFICE_KANA, Param.CUSTOMER_PC_NAME,
			Param.CUSTOMER_PC_KANA, Param.CUSTOMER_TEL, Param.CUSTOMER_FAX,
			Param.PAYMENT_NAME, Param.CUSTOMER_RANK_CATEGORY,
			Param.PAYBACK_CYCLE_CATEGORY, Param.CUTOFF_GROUP, Param.REMARKS,
			Param.SORT_COLUMN_CUSTOMER, Param.SORT_COLUMN_RANK,
			Param.SORT_ORDER, Param.CATEGORY_ID, Param.CATEGORY_ID2,
			Param.CATEGORY_ID3, Param.LAST_CUTOFF_DATE,
			Param.SALES_CM_CATEGORY, Param.CUSTOMER_PC_PRE_CATEGORY_NAME,
			Param.CATEGORY_ID4, Param.SALES_SLIP_CATEGORY,
			Param.NOT_YET_REQUESTED_CHECK };
	/**
	 * 顧客コードのカラム名
	 */
	private static final String COLUMN_CUSTOMER_CODE = "CUSTOMER_CODE";

	/**
	 *
	 * @param conditions 検索条件のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @return {@link CustomerJoin}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findByCondition(java.util.Map, java.lang.String, boolean)
	 */
	@Override
	public List<CustomerJoin> findByCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc) throws ServiceException {
		if (conditions == null) {
			return new ArrayList<CustomerJoin>();
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			this.setCondition(conditions, sortColumn, sortOrderAsc, param);

			List<CustomerJoin> result = this.selectBySqlFile(
					CustomerJoin.class, "customer/FindCustomerByCondition.sql",
					param).getResultList();
			return result;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @param customerCode 顧客コード
	 * @return {@link CustomerJoin}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findById(java.lang.String)
	 */
	@Override
	public CustomerJoin findById(String customerCode) throws ServiceException {
		if (!StringUtil.hasLength(customerCode)) {
			return null;
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);
			param.put(Param.CUSTOMER_CODE, customerCode);
			setCategoriesCondition(param);

			return this.selectBySqlFile(CustomerJoin.class,
					"customer/FindCustomerJoinByCode.sql", param)
					.getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を設定します.
	 * @param conditions 検索条件値のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順か否か
	 * @param param 検索条件のマップ
	 */
	private void setCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc, Map<String, Object> param) {
		// 顧客コード
		if (conditions.containsKey(CustomerService.Param.CUSTOMER_CODE)) {
			param.put(CustomerService.Param.CUSTOMER_CODE, super
					.createPrefixSearchCondition((String) conditions
							.get(CustomerService.Param.CUSTOMER_CODE)));
		}

		// 顧客名
		if (conditions.containsKey(CustomerService.Param.CUSTOMER_NAME)) {
			param.put(CustomerService.Param.CUSTOMER_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(CustomerService.Param.CUSTOMER_NAME)));
		}

		// 顧客名カナ
		if (conditions.containsKey(CustomerService.Param.CUSTOMER_KANA)) {
			param.put(CustomerService.Param.CUSTOMER_KANA, super
					.createPartialSearchCondition((String) conditions
							.get(CustomerService.Param.CUSTOMER_KANA)));
		}

		// 事業所名
		if (conditions.containsKey(CustomerService.Param.CUSTOMER_OFFICE_NAME)) {
			param.put(CustomerService.Param.CUSTOMER_OFFICE_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(CustomerService.Param.CUSTOMER_OFFICE_NAME)));
		}

		// 事業所名カナ
		if (conditions.containsKey(CustomerService.Param.CUSTOMER_OFFICE_KANA)) {
			param.put(CustomerService.Param.CUSTOMER_OFFICE_KANA, super
					.createPartialSearchCondition((String) conditions
							.get(CustomerService.Param.CUSTOMER_OFFICE_KANA)));
		}

		// 担当者
		if (conditions.containsKey(CustomerService.Param.CUSTOMER_PC_NAME)) {
			param.put(CustomerService.Param.CUSTOMER_PC_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(CustomerService.Param.CUSTOMER_PC_NAME)));
		}

		// 担当者カナ
		if (conditions.containsKey(CustomerService.Param.CUSTOMER_PC_KANA)) {
			param.put(CustomerService.Param.CUSTOMER_PC_KANA, super
					.createPartialSearchCondition((String) conditions
							.get(CustomerService.Param.CUSTOMER_PC_KANA)));
		}

		// TEL
		if (conditions.containsKey(CustomerService.Param.CUSTOMER_TEL)) {
			String value = StringUtil.trimBlank((String) conditions
					.get(CustomerService.Param.CUSTOMER_TEL));
			if (StringUtil.hasLength(value)) {
				param.put(CustomerService.Param.CUSTOMER_TEL, value.replaceAll(
						"[()-]", ""));
			}
		}

		// FAX
		if (conditions.containsKey(CustomerService.Param.CUSTOMER_FAX)) {
			String value = StringUtil.trimBlank((String) conditions
					.get(CustomerService.Param.CUSTOMER_FAX));
			if (StringUtil.hasLength(value)) {
				param.put(CustomerService.Param.CUSTOMER_FAX, value.replaceAll(
						"[()-]", ""));
			}
		}

		// 顧客ランク
		if (conditions
				.containsKey(CustomerService.Param.CUSTOMER_RANK_CATEGORY)) {
			param.put(CustomerService.Param.CUSTOMER_RANK_CATEGORY, conditions
					.get(CustomerService.Param.CUSTOMER_RANK_CATEGORY));
		}

		// 支払条件
		if (conditions.containsKey(CustomerService.Param.CUTOFF_GROUP)) {
			// 締日と回収間隔に振り分ける
			String value = (String) conditions
					.get(CustomerService.Param.CUTOFF_GROUP);
			if (value != null) {
				// 締日グループ
				param.put(CustomerService.Param.CUTOFF_GROUP, value.substring(
						0, 2));
				// 回収サイクル
				param.put(CustomerService.Param.PAYBACK_CYCLE_CATEGORY, value
						.substring(2, value.length()));
			}
		}

		// 振込名義
		if (conditions.containsKey(CustomerService.Param.PAYMENT_NAME)) {
			param.put(CustomerService.Param.PAYMENT_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(CustomerService.Param.PAYMENT_NAME)));
		}

		// 備考
		if (conditions.containsKey(CustomerService.Param.REMARKS)) {
			param.put(CustomerService.Param.REMARKS, super
					.createPartialSearchCondition((String) conditions
							.get(CustomerService.Param.REMARKS)));
		}

		// 敬称
		if (conditions
				.containsKey(CustomerService.Param.CUSTOMER_PC_PRE_CATEGORY_NAME)) {
			param
					.put(
							CustomerService.Param.CUSTOMER_PC_PRE_CATEGORY_NAME,
							super
									.createPartialSearchCondition((String) conditions
											.get(CustomerService.Param.CUSTOMER_PC_PRE_CATEGORY_NAME)));
		}
		if (conditions.containsKey(CustomerService.Param.CATEGORY_ID4)) {
			param.put(CustomerService.Param.CATEGORY_ID4, super
					.createPartialSearchCondition((String) conditions
							.get(CustomerService.Param.CATEGORY_ID4)));
		}

		// 売上伝票種別
		if (conditions.containsKey(CustomerService.Param.SALES_SLIP_CATEGORY)) {
			param.put(CustomerService.Param.SALES_SLIP_CATEGORY, super
					.createPartialSearchCondition((String) conditions
							.get(CustomerService.Param.SALES_SLIP_CATEGORY)));
		}

		// 固定の区分ＩＤを設定
		setCategoriesCondition(param);

		// ソートカラムを設定する
		if (sortColumn != null) {
			param.put(CustomerService.Param.SORT_COLUMN_CUSTOMER, StringUtil
					.convertColumnName(sortColumn));

			// ソートオーダーを設定する
			if (sortOrderAsc) {
				param.put(CustomerService.Param.SORT_ORDER, Constants.SQL.ASC);
			} else {
				param.put(CustomerService.Param.SORT_ORDER, Constants.SQL.DESC);
			}
		}
	}

	/**
	 * 検索条件に区分を設定します.
	 * @param param 検索条件マップ
	 */
	private void setCategoriesCondition(Map<String, Object> param) {
		// 区分IDは支払条件、税転嫁、売上取引区分を指定する
		param.put(CustomerService.Param.CATEGORY_ID, Categories.CUTOFF_GROUP);
		param.put(CustomerService.Param.CATEGORY_ID2,
				Categories.ART_TAX_SHIFT_CATEGORY);
		param.put(CustomerService.Param.CATEGORY_ID3,
				Categories.SALES_CM_CATEGORY);
		param.put(CustomerService.Param.CATEGORY_ID4, Categories.PRE_TYPE);
	}

	/**
	 * 空の検索条件マップを作成します.
	 * @param param 検索条件マップ
	 * @return 検索条件キーのみ設定した検索条件マップ
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(CustomerService.Param.CUSTOMER_CODE, null);
		param.put(CustomerService.Param.CUSTOMER_NAME, null);
		param.put(CustomerService.Param.CUSTOMER_KANA, null);
		param.put(CustomerService.Param.CUSTOMER_OFFICE_NAME, null);
		param.put(CustomerService.Param.CUSTOMER_OFFICE_KANA, null);
		param.put(CustomerService.Param.CUSTOMER_PC_NAME, null);
		param.put(CustomerService.Param.CUSTOMER_PC_KANA, null);
		param.put(CustomerService.Param.CUSTOMER_TEL, null);
		param.put(CustomerService.Param.CUSTOMER_FAX, null);
		param.put(CustomerService.Param.PAYMENT_NAME, null);
		param.put(CustomerService.Param.CUSTOMER_RANK_CATEGORY, null);
		param.put(CustomerService.Param.CUTOFF_GROUP, null);
		param.put(CustomerService.Param.REMARKS, null);
		param.put(CustomerService.Param.SORT_COLUMN_CUSTOMER,
				CustomerService.COLUMN_CUSTOMER_CODE);
		param.put(CustomerService.Param.SORT_ORDER, Constants.SQL.ASC);
		return param;
	}

	/**
	 * 顧客コードが存在するか確認します.
	 * @param customerCode 顧客コード
	 * @return 存在するか否か
	 */
	public boolean isExistCustomerCode(String customerCode) {

		try {
			LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

			// 条件設定
			// 顧客コードが一致
			conditions.put(Param.CUSTOMER_CODE, customerCode);

			List<CustomerJoin> listCust = findByCondition(conditions,
					paramNames, "customer/FindCustomerByCondition.sql");

			if (listCust.size() != 1) {
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 顧客コードを指定して、顧客情報を返します.<br>
	 * 該当データが存在しない場合、nullを返します.
	 * @param customerCode 顧客コード
	 * @return 顧客情報{@link Customer}
	 * @throws ServiceException
	 */
	public Customer findCustomerByCode(String customerCode)
			throws ServiceException {

		try {
			// 条件設定
			// 顧客コードが一致
			Map<String, Object> conditions = super.createSqlParam();
			conditions.put(Param.CUSTOMER_CODE, customerCode);
			setCategoriesCondition(conditions);

			CustomerJoin cust = this.selectBySqlFile(CustomerJoin.class,
					"customer/FindCustomerJoinByCode.sql", conditions)
					.getSingleResult();

			if (cust == null) {
				return null;
			}
			return cust;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	/**
	 * 売掛残高が0円以外の顧客コードのリストを返します.
	 * @param artBalanceCheckDate 売掛締日の文字列
	 * @param customerCodeFrom 検索範囲開始顧客コード
	 * @param customerCodeTo 検索範囲終了顧客コード
	 * @return 顧客コードのリスト
	 * @throws ServiceException
	 */
	public List<String> findExistArtBalanceCustomerCodeList(
			String artBalanceCheckDate, String customerCodeFrom,
			String customerCodeTo) throws ServiceException {

		try {
			// 条件設定
			// 顧客コードが一致
			Map<String, Object> conditions = super.createSqlParam();
			conditions.put(Param.CUSTOMER_CODE_FROM, customerCodeFrom);
			conditions.put(Param.CUSTOMER_CODE_TO, customerCodeTo);
			conditions.put(Param.ART_BALANCE_CHECK_DATE, artBalanceCheckDate);

			//			setCategoriesCondition(conditions);

			List<String> customerCodeList = this.selectBySqlFile(String.class,
					"customer/FindExistArtBalanceCustomerCodeList.sql",
					conditions).getResultList();

			if (customerCodeList == null) {
				return null;
			}
			return customerCodeList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	/**
	 * 関連テーブルのレコードが存在するか確認を行う
	 *
	 * @param customerCode
	 * @throws ServiceException
	 */
	/**
	 * 顧客コードを指定して、関連テーブル情報のマップを返します.<br>
	 * 関連テーブル情報が存在しない場合、空のマップを返します.
	 * @param customerCode 顧客コード
	 * @return 関連テーブル情報のマップ
	 * @throws ServiceException
	 */
	public Map<String, Object> countRelations(String customerCode)
			throws ServiceException {
		try {
			// 関連データの存在チェック
			Map<String, Object> param = super.createSqlParam();
			param.put(CustomerService.Param.CUSTOMER_CODE, customerCode);
			BeanMap result = this.selectBySqlFile(BeanMap.class,
					"customer/CountRelations.sql", param).getSingleResult();

			HashMap<String, Object> temp = new HashMap<String, Object>();
			if (result == null) {
				return temp;
			}
			temp.putAll(result);
			return temp;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 顧客情報を削除します.
	 * @param dto {@link CustomerDto}
	 * @throws ServiceException
	 * @throws UnabledLockException
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#deleteRecord(java.lang.Object)
	 */
	@Override
	public void deleteRecord(CustomerDto dto) throws ServiceException,
			UnabledLockException {
		try {
			// 排他制御
			Map<String, Object> param = super.createSqlParam();
			param.put(CustomerService.Param.CUSTOMER_CODE, dto.customerCode);
			this.lockRecordBySqlFile("customer/LockCustomerByCustomerCode.sql",
					param, dto.updDatetm);

			// 納入先を先に処理
			List<CustomerRel> list = customerRelService
					.findCustomerRelByCustomerCode(dto.customerCode);

			for (CustomerRel cr : list) {
				// 納入先を削除
				deliveryService.deleteDelivery(cr.relCode);

				// 関連を削除
				customerRelService.deleteCustomerRel(cr.customerCode,
						cr.relCode, cr.custRelCategory);
			}

			// 顧客の削除
			param = super.createSqlParam();
			param.put(CustomerService.Param.CUSTOMER_CODE, dto.customerCode);
			this.updateBySqlFile("customer/DeleteCustomerByCustomerCode.sql",
					param).execute();
		} catch (UnabledLockException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 顧客を登録します.
	 * @param customerDto {@link CustomerDto}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#insertRecord(java.lang.Object)
	 */
	@Override
	public void insertRecord(CustomerDto customerDto) throws ServiceException {
		if (customerDto == null) {
			return;
		}
		try {
			// 顧客情報の登録
			Map<String, Object> param = super.createSqlParam();

			// 振込人名義の変換
			customerDto.paymentName = StringUtil
					.convertPaymentName(customerDto.paymentName);

			BeanMap customerInfo = Beans.createAndCopy(BeanMap.class,
					customerDto).timestampConverter(Constants.FORMAT.TIMESTAMP)
					.dateConverter(Constants.FORMAT.DATE, "lastCutoffDate",
							"firstSalesDate", "lastSalesDate",
							"lastSalesCutoffDate").excludes(
							AbstractService.Param.CRE_FUNC,
							AbstractService.Param.CRE_DATETM,
							AbstractService.Param.CRE_USER,
							AbstractService.Param.UPD_FUNC,
							AbstractService.Param.UPD_DATETM,
							AbstractService.Param.UPD_USER).execute();

			// 支払条件の調整
			if (customerDto.cutoffGroupCategory != null
					&& customerDto.cutoffGroupCategory.length() > 2) {
				customerInfo.put(CustomerService.Param.CUTOFF_GROUP,
						customerDto.cutoffGroupCategory.substring(0, 2));
				customerInfo.put(CustomerService.Param.PAYBACK_CYCLE_CATEGORY,
						customerDto.cutoffGroupCategory.substring(2));
			}

			param.putAll(customerInfo);
			this.updateBySqlFile("customer/InsertCustomer.sql", param)
					.execute();

		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * 顧客を更新します.
	 * @param customerDto {@link CustomerDto}
	 * @throws ServiceException
	 * @throws UnabledLockException
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#updateRecord(java.lang.Object)
	 */
	@Override
	public void updateRecord(CustomerDto customerDto) throws ServiceException,
			UnabledLockException {
		if (customerDto == null) {
			return;
		}

		// 排他制御
		Map<String, Object> lockParam = createSqlParam();
		lockParam.put(CustomerService.Param.CUSTOMER_CODE,
				customerDto.customerCode);

		// 排他制御エラー時は例外が発生する
		lockRecordBySqlFile("customer/LockCustomerByCustomerCode.sql",
				lockParam, customerDto.updDatetm);

		// 振込人名義の変換
		customerDto.paymentName = StringUtil
				.convertPaymentName(customerDto.paymentName);

		// 顧客情報の更新
		Map<String, Object> param = super.createSqlParam();
		BeanMap customerInfo = Beans.createAndCopy(BeanMap.class, customerDto)
				.timestampConverter(Constants.FORMAT.TIMESTAMP).dateConverter(
						Constants.FORMAT.DATE, "lastCutoffDate",
						"firstSalesDate", "lastSalesDate",
						"lastSalesCutoffDate").excludes(
						AbstractService.Param.CRE_FUNC,
						AbstractService.Param.CRE_DATETM,
						AbstractService.Param.CRE_USER,
						AbstractService.Param.UPD_FUNC,
						AbstractService.Param.UPD_DATETM,
						AbstractService.Param.UPD_USER).execute();

		// 支払条件の調整
		if (customerDto.cutoffGroupCategory != null
				&& customerDto.cutoffGroupCategory.length() > 2) {
			customerInfo.put(CustomerService.Param.CUTOFF_GROUP,
					customerDto.cutoffGroupCategory.substring(0, 2));
			customerInfo.put(CustomerService.Param.PAYBACK_CYCLE_CATEGORY,
					customerDto.cutoffGroupCategory.substring(2));
		}

		param.putAll(customerInfo);
		this.updateBySqlFile("customer/UpdateCustomer.sql", param).execute();
	}

	/**
	 *
	 * @param conditions 検索条件のマップ
	 * @return 検索結果数
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#countByCondition(java.util.Map)
	 */
	public int countByCondition(Map<String, Object> conditions)
			throws ServiceException {
		if (conditions == null) {
			return 0;
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			// 検索条件を設定する
			this.setCondition(conditions, null, false, param);

			return this.selectBySqlFile(Integer.class,
					"customer/CountCustomerByCondition.sql", param)
					.getSingleResult().intValue();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @param conditions 検索条件のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param rowCount 取得件数
	 * @param offset 取得開始位置
	 * @return {@link CustomerJoin}のリスト
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.MasterSearch#findByConditionLimit(java.util.Map, java.lang.String, boolean, int, int)
	 */
	@Override
	public List<CustomerJoin> findByConditionLimit(
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		if (conditions == null) {
			return new ArrayList<CustomerJoin>();
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			this.setCondition(conditions, sortColumn, sortOrderAsc, param);

			// LIMITを設定する
			if (rowCount > 0) {
				param.put(CustomerService.Param.ROW_COUNT, rowCount);
				param.put(CustomerService.Param.OFFSET, offset);
			}

			List<CustomerJoin> result = this.selectBySqlFile(
					CustomerJoin.class,
					"customer/FindCustomerByConditionLimit.sql", param)
					.getResultList();
			return result;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 請求締処理時に最終締処理日を更新します.<br>
	 * この操作は履歴を残しません.
	 * @param customerCode 顧客コード
	 * @param closeDate 締処理日
	 * @return 更新件数
	 * @throws ServiceException
	 */
	public int updateLastCutoffDate(String customerCode, String closeDate)
			throws ServiceException {

		int SuccessedLineCount = 0;

		//MAPの生成
		Map<String, Object> param = new HashMap<String, Object>();

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.LAST_CUTOFF_DATE, closeDate);

		//条件をPUT
		BeanMap AFparam = Beans.createAndCopy(BeanMap.class, conditions)
				.execute();
		param.putAll(AFparam);

		//更新日時とかPUT
		Map<String, Object> CommonParam = super.createSqlParam();
		param.putAll(CommonParam);

		//SQLクエリを投げる
		SuccessedLineCount = this.updateBySqlFile(
				"customer/UpdateLastCutoffDate.sql", param).execute();
		return SuccessedLineCount;
	}

	/**
	 * 売掛締処理時に最終締処理日を更新します.<br>
	 * この操作は履歴を残しません.
	 * @param customerCode 顧客コード
	 * @param closeDate 締処理日
	 * @return 更新件数
	 * @throws ServiceException
	 */
	public int updateLastSalesCutoffDate(String customerCode, String closeDate)
			throws ServiceException {

		int SuccessedLineCount = 0;

		//MAPの生成
		Map<String, Object> param = new HashMap<String, Object>();

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致
		conditions.put(Param.CUSTOMER_CODE, customerCode);
		conditions.put(Param.LAST_SALES_CUTOFF_DATE, closeDate);

		//条件をPUT
		BeanMap AFparam = Beans.createAndCopy(BeanMap.class, conditions)
				.execute();
		param.putAll(AFparam);

		//更新日時とかPUT
		Map<String, Object> CommonParam = super.createSqlParam();
		param.putAll(CommonParam);

		//SQLクエリを投げる
		SuccessedLineCount = this.updateBySqlFile(
				"customer/UpdateLastSalesCutoffDate.sql", param).execute();
		return SuccessedLineCount;
	}

	/**
	 * 締処理対象の顧客情報のリストを返します.
	 * @param art 売掛顧客か否か
	 * @param customerCode 顧客コード
	 * @param customerName 顧客名
	 * @param cutoffGroup 締日グループ
	 * @param paybackCycleCategory 支払サイクル
	 * @param notYetRequestedCheck 請求漏れチェックありか否か
	 * @return 顧客情報{@link CustomerJoin}のリスト
	 * @throws ServiceException
	 */
	public List<CustomerJoin> findCustomerForCloseBill(
			Boolean art, // 売掛
			String customerCode, String customerName, String cutoffGroup,
			String paybackCycleCategory, Boolean notYetRequestedCheck // 請求漏れチェックがチェックされているかどうか
	) throws ServiceException {

		// 請求漏れチェックで無い場合、nullかfalseがくるのでfalseに寄せる
		if (notYetRequestedCheck == null) {
			notYetRequestedCheck = false;
		}

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		// 顧客コードが一致

		// 顧客コード
		if (StringUtil.hasLength(customerCode)) {
			conditions.put(Param.CUSTOMER_CODE, super
					.createPrefixSearchCondition(customerCode));
		}
		// 顧客名
		if (StringUtil.hasLength(customerName)) {
			conditions.put(Param.CUSTOMER_NAME, super
					.createPartialSearchCondition(customerName));
		}
		if (StringUtil.hasLength(cutoffGroup)) {
			conditions.put(Param.CUTOFF_GROUP, cutoffGroup);
		}
		if (StringUtil.hasLength(paybackCycleCategory)) {
			conditions.put(Param.PAYBACK_CYCLE_CATEGORY, paybackCycleCategory);
		}
		conditions.put(Param.SALES_CM_CATEGORY, CategoryTrns.SALES_CM_CREDIT);

		conditions.put(Param.NOT_YET_REQUESTED_CHECK, notYetRequestedCheck);

		if (art) {
			// 現状の取引区分が掛売で、請求書の最も新しい日付を取得。
			return findByCondition(conditions, paramNames,
					"customer/FindCustomerForCloseBillArt.sql");
		}
		// 現状の取引区分が掛売以外で、売掛残高の取引区分が売掛以外かつ最も新しい日付を取得。
		return findByCondition(conditions, paramNames,
				"customer/FindCustomerForCloseBillOther.sql");

	}

	/**
	 * 顧客CSVデータの顧客属性、支払条件および電話番号を指定して、顧客コードを作成します.
	 * @param customerAttr 顧客属性
	 * @param paymentCondition 支払条件
	 * @param telNo 電話番号
	 * @return 作成した顧客コード
	 */
	public String createCustomerCodeFromCSV(String customerAttr,
			String paymentCondition, String telNo) {
		if (!StringUtil.hasLength(telNo)) {
			return telNo;
		}

		String prefix = "0";
		if ("2".equals(customerAttr)) {
			if ("6".equals(paymentCondition)) {
				prefix = "1";
			} else if ("7".equals(paymentCondition)) {
				prefix = "3";
			} else if ("8".equals(paymentCondition)) {
				prefix = "6";
			}
		} else if ("3".equals(customerAttr)) {
			if ("6".equals(paymentCondition)) {
				prefix = "2";
			} else if ("7".equals(paymentCondition)) {
				prefix = "4";
			} else if ("8".equals(paymentCondition)) {
				prefix = "6";
			}
		}

		// 数字以外の文字を除去し、先頭文字を置き換えて顧客コード生成
		String customerCodeTemp = telNo.replaceAll("[^\\d]", "").replaceFirst(
				"[\\d]", prefix);
		if (customerCodeTemp.length() > 10) {
			return customerCodeTemp.substring(0, 10);
		}
		return customerCodeTemp;
	}

	/**
	 * 振込名義を指定して、顧客情報のリストを返します.
	 * @param paymentName 振込名義
	 * @return 顧客情報{@link CustomerJoin}のリスト
	 * @throws ServiceException
	 */
	public List<CustomerJoin> findByPaymentName(String paymentName)
			throws ServiceException {
		try {
			LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

			// 条件設定
			// 振込名義が一致
			conditions.put(Param.PAYMENT_NAME, paymentName);

			return findByCondition(conditions, paramNames,
					"customer/FindCustomerByPaymentName.sql");
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 最新の売掛締日を返します.
	 * @return 売掛締日
	 * @throws ServiceException
	 */
	public Date findLastCloseArtBalanceCutOffDate() throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);

			BeanMap bm = (BeanMap) this.selectBySqlFile(BeanMap.class,
					"customer/FindLastSalesCutoffDate.sql", param)
					.getSingleResult();
			Timestamp ts = (Timestamp) bm.get("lastSalesCutoffDate");
			return ts;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 *
	 * @return {CUSTOMER_CODE}
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#getKeyColumnNames()
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "CUSTOMER_CODE" };
	}

	/**
	 *
	 * @return {@link CustomerJoin#TABLE_NAME}
	 * @see jp.co.arkinfosys.service.AbstractMasterEditService#getTableName()
	 */
	@Override
	protected String getTableName() {
		return CustomerJoin.TABLE_NAME;
	}
}
