/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.BankDto;
import jp.co.arkinfosys.entity.Bank;
import jp.co.arkinfosys.entity.join.BankDwb;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 *
 * 銀行マスタサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class BankService extends AbstractMasterEditService<BankDto, BankDwb>
		implements MasterSearch<BankDwb> {

	@Resource
	private SeqMakerService seqMakerService;

	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String BANK_ID = "bankId";
		public static final String BANK_CODE = "bankCode";
		public static final String BANK_NAME = "bankName";
		public static final String STORE_CODE = "storeCode";
		public static final String STORE_NAME = "storeName";
		public static final String DWB_TYPE = "dwbType";
		public static final String DWB_NAME = "dwbName";
		public static final String ACCOUNT_NUM = "accountNum";
		public static final String ACCOUNT_OWNER_NAME = "accountOwnerName";
		public static final String ACCOUNT_OWNER_KANA = "accountOwnerKana";
		public static final String VALID = "valid";
		public static final String SORT_COLUMN_BANK = "sortColumnBank";
		public static final String SORT_ORDER = "sortOrder";
		public static final String ROW_COUNT = "rowCount";
		public static final String OFFSET = "offsetRow";

		public static final String CATEGORY_ID = "categoryId";

	}

	private static final String COLUMN_BANK_CODE = "BANK_CODE";
	private static final String COLUMN_BANK_NAME = "BANK_NAME";
	private static final String COLUMN_STORE_CODE = "STORE_CODE";
	private static final String COLUMN_STORE_NAME = "STORE_NAME";
	private static final String COLUMN_DWB_NAME = "DWB_NAME";
	private static final String COLUMN_ACCOUNT_NUM = "ACCOUNT_NUM";
	private static final String COLUMN_ACCOUNT_OWNER_NAME = "ACCOUNT_OWNER_NAME";
	private static final String COLUMN_ACCOUNT_OWNER_KANA = "ACCOUNT_OWNER_KANA";
	private static final String COLUMN_VALID = "VALID";

	/**
	 * 銀行マスタの一覧を返します.
	 *
	 * @return 銀行マスタリスト
	 */
	public List<Bank> selectBankList() {
		// MAPの生成
		Map<String, Object> param = new HashMap<String, Object>();

		// Bankの情報を食わせる
		Bank bank = new Bank();
		BeanMap Eparam = Beans.createAndCopy(BeanMap.class, bank).execute();
		param.putAll(Eparam);

		// 更新日時とかPUT
		Map<String, Object> CommonParam = super.createSqlParam();
		param.putAll(CommonParam);

		return this.selectBySqlFile(Bank.class, "bank/SelectBankList.sql",
				param).getResultList();

	}

	/**
	 * 銀行マスタと区分データを結合したリストを返します.
	 *
	 * @return 銀行マスタと区分データの結合リスト
	 */
	public List<BankDwb> selectBankDwbList() {
		// MAPの生成
		Map<String, Object> param = new HashMap<String, Object>();

		// Bankの情報を食わせる
		BankDwb bank = new BankDwb();
		BeanMap Eparam = Beans.createAndCopy(BeanMap.class, bank).execute();
		param.putAll(Eparam);

		// 更新日時とかPUT
		Map<String, Object> CommonParam = super.createSqlParam();
		param.putAll(CommonParam);

		// 預金種類の区分IDを指定
		param.put(BankDwb.keyName, Categories.DWB_TYPE);

		return this.selectBySqlFile(BankDwb.class,
				"bank/SelectBankDwbList.sql", param).getResultList();

	}

	/**
	 * 指定した銀行情報から銀行IDを返します.
	 *
	 * @param bank 銀行情報
	 * @return 銀行ID
	 */
	public String getValue(Bank bank) {
		return bank.bankId.toString();
	}

	/**
	 * 指定した銀行情報から銀行名を返します.
	 *
	 * @param bank 銀行情報
	 * @return 銀行名
	 */
	public String getName(BankDwb bank) {
		return bank.bankName + " " + bank.storeName + " " + bank.dwbName + " "
				+ bank.accountNum;
	}

	/**
	 * 検索条件を受け取り、初期化して返します.
	 *
	 * @param param 検索条件オブジェクト
	 * @return 空の検索条件オブジェクト
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(Param.BANK_ID, null);
		param.put(Param.BANK_CODE, null);
		param.put(Param.BANK_NAME, null);
		param.put(Param.STORE_CODE, null);
		param.put(Param.STORE_NAME, null);
		param.put(Param.DWB_TYPE, null);
		param.put(Param.ACCOUNT_NUM, null);
		param.put(Param.ACCOUNT_OWNER_NAME, null);
		param.put(Param.ACCOUNT_OWNER_KANA, null);
		param.put(Param.VALID, null);

		param.put(Param.SORT_COLUMN_BANK, null);
		param.put(Param.SORT_ORDER, null);
		return param;
	}

	/**
	 * 検索条件を設定します.
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @param param 検索条件のマップ
	 */
	private void setCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc, Map<String, Object> param) {
		// 銀行ID
		if (conditions.containsKey(Param.BANK_ID)) {
			param.put(Param.BANK_ID, super
					.createPrefixSearchCondition((String) conditions
							.get(Param.BANK_ID)));
		}

		// 銀行コード
		if (conditions.containsKey(Param.BANK_CODE)) {
			param.put(Param.BANK_CODE, super
					.createPrefixSearchCondition((String) conditions
							.get(Param.BANK_CODE)));
		}

		// 銀行名
		if (conditions.containsKey(Param.BANK_NAME)) {
			param.put(Param.BANK_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(Param.BANK_NAME)));
		}

		// 店名
		if (conditions.containsKey(Param.STORE_NAME)) {
			param.put(Param.STORE_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(Param.STORE_NAME)));
		}

		// 店番
		if (conditions.containsKey(Param.STORE_CODE)) {
			param.put(Param.STORE_CODE, super
					.createPrefixSearchCondition((String) conditions
							.get(Param.STORE_CODE)));
		}

		// 科目
		if (conditions.containsKey(Param.DWB_TYPE)) {
			param.put(Param.DWB_TYPE, conditions.get(Param.DWB_TYPE));
		}
		// 口座番号
		if (conditions.containsKey(Param.ACCOUNT_NUM)) {
			param.put(Param.ACCOUNT_NUM, conditions.get(Param.ACCOUNT_NUM));
		}
		// 口座名義
		if (conditions.containsKey(Param.ACCOUNT_OWNER_NAME)) {
			param.put(Param.ACCOUNT_OWNER_NAME, super
					.createPartialSearchCondition((String) conditions
							.get(Param.ACCOUNT_OWNER_NAME)));
		}
		// 口座名義カナ
		if (conditions.containsKey(Param.ACCOUNT_OWNER_KANA)) {
			param.put(Param.ACCOUNT_OWNER_KANA, super
					.createPartialSearchCondition((String) conditions
							.get(Param.ACCOUNT_OWNER_KANA)));
		}
		// 有効
		if (conditions.containsKey(Param.VALID)) {
			param.put(Param.VALID, conditions.get(Param.VALID));
		}

		// 区分IDは「預金種類」固定
		param.put(Param.CATEGORY_ID, Categories.DWB_TYPE);

		// ソートカラムを設定する
		if (Param.BANK_CODE.equals(sortColumn)) {
			// 銀行コード
			param.put(Param.SORT_COLUMN_BANK, COLUMN_BANK_CODE);
		} else if (Param.BANK_NAME.equals(sortColumn)) {
			// 銀行名
			param.put(Param.SORT_COLUMN_BANK, COLUMN_BANK_NAME);
		} else if (Param.STORE_CODE.equals(sortColumn)) {
			// 店番
			param.put(Param.SORT_COLUMN_BANK, COLUMN_STORE_CODE);
		} else if (Param.STORE_NAME.equals(sortColumn)) {
			// 店名
			param.put(Param.SORT_COLUMN_BANK, COLUMN_STORE_NAME);
		} else if (Param.DWB_NAME.equals(sortColumn)) {
			// 科目
			param.put(Param.SORT_COLUMN_BANK, COLUMN_DWB_NAME);
		} else if (Param.ACCOUNT_NUM.equals(sortColumn)) {
			// 口座番号
			param.put(Param.SORT_COLUMN_BANK, COLUMN_ACCOUNT_NUM);
		}else if (Param.ACCOUNT_OWNER_NAME.equals(sortColumn)) {
			// 口座名義
			param.put(Param.SORT_COLUMN_BANK, COLUMN_ACCOUNT_OWNER_NAME);
		}else if (Param.ACCOUNT_OWNER_KANA.equals(sortColumn)) {
			// 口座名義カナ
			param.put(Param.SORT_COLUMN_BANK, COLUMN_ACCOUNT_OWNER_KANA);
		}

		else if (Param.VALID.equals(sortColumn)) {
			// 有効
			param.put(Param.SORT_COLUMN_BANK, COLUMN_VALID);
		}

		// ソートオーダーを設定する
		if (sortOrderAsc) {
			param.put(Param.SORT_ORDER, Constants.SQL.ASC);
		} else {
			param.put(Param.SORT_ORDER, Constants.SQL.DESC);
		}
	}

	/**
	 * 銀行IDを指定して、銀行マスタと区分データを結合したデータを返します.
	 * @param bankId 銀行ID
	 * @return 銀行マスタと区分データを結合したデータ
	 * @throws ServiceException
	 */
	@Override
	public BankDwb findById(String bankId) throws ServiceException {
		if (bankId == null) {
			return null;
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			param.put(Param.BANK_ID, bankId);
			return this.selectBySqlFile(BankDwb.class,
					"bank/FindBankByBankId.sql", param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して、結果件数を返します.
	 * @param conditions 検索条件
	 * @return 結果件数
	 * @throws ServiceException
	 */
	@Override
	public int countByCondition(Map<String, Object> conditions)
			throws ServiceException {
		if (conditions == null) {
			return 0;
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			this.setCondition(conditions, null, false, param);

			return this.selectBySqlFile(Integer.class,
					"bank/CountBankByCondition.sql", param).getSingleResult()
					.intValue();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して、銀行マスタと区分データを結合したデータのリストを返します.
	 *
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @param rowCount 取得件数(LIMIT)
	 * @param offset 取得開始位置(OFFSET)
	 * @return 銀行マスタと区分データを結合したデータのリスト
	 * @throws ServiceException
	 */
	@Override
	public List<BankDwb> findByConditionLimit(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		if (conditions == null) {
			return new ArrayList<BankDwb>();
		}
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			this.setCondition(conditions, sortColumn, sortOrderAsc, param);

			// LIMITを設定する
			if (rowCount > 0) {
				param.put(Param.ROW_COUNT, rowCount);
				param.put(Param.OFFSET, offset);
			}

			return this.selectBySqlFile(BankDwb.class,
					"bank/FindBankByConditionLimit.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して、銀行マスタと区分データを結合したデータのリストを返します.<br>
	 * 未実装です.
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @return 銀行マスタと区分データを結合したデータのリスト
	 * @throws ServiceException
	 */
	@Override
	public List<BankDwb> findByCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc) throws ServiceException {
		return new ArrayList<BankDwb>();
		// 未使用メソッド
	}

	/**
	 * 銀行情報を削除します.
	 * @param dto 銀行情報DTO
	 * @throws Exception
	 */
	@Override
	public void deleteRecord(BankDto dto) throws Exception {
		// 排他制御
		Map<String, Object> param = super.createSqlParam();
		param.put(Param.BANK_ID, dto.bankId);
		this.lockRecordBySqlFile("bank/LockBank.sql", param, dto.updDatetm);

		// 削除
		param = super.createSqlParam();
		param.put(Param.BANK_ID, dto.bankId);
		this.updateBySqlFile("bank/DeleteBank.sql", param).execute();
	}

	/**
	 * マスタコードを発番します.
	 * @return マスタコード
	 * @throws Exception
	 */
	// BankIDの発番
	public String makeBankId() throws Exception {
		long newBankId = seqMakerService.nextval(Bank.TABLE_NAME);
		return String.valueOf(newBankId);
	}

	/**
	 * 銀行情報を登録します.
	 * @param dto 銀行情報DTO
	 * @throws Exception
	 */
	@Override
	public void insertRecord(BankDto dto) throws Exception {
		if (dto == null) {
			return;
		}
		try {
			// 登録
			Map<String, Object> param = super.createSqlParam();

			BeanMap bankInfo = Beans.createAndCopy(BeanMap.class, dto)
					.timestampConverter(Constants.FORMAT.TIMESTAMP)
					.dateConverter(Constants.FORMAT.DATE).execute();

			param.putAll(bankInfo);
			this.updateBySqlFile("bank/InsertBank.sql", param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 銀行情報を更新します.
	 * @param dto 銀行情報DTO
	 * @throws Exception
	 */
	@Override
	public void updateRecord(BankDto dto) throws Exception {
		if (dto == null) {
			return;
		}

		// 排他制御
		Map<String, Object> lockParam = createSqlParam();
		lockParam.put(Param.BANK_ID, dto.bankId);

		// 排他制御エラー時は例外が発生する
		lockRecordBySqlFile("bank/LockBank.sql", lockParam, dto.updDatetm);

		// 顧客情報の更新
		Map<String, Object> param = super.createSqlParam();
		BeanMap bankInfo = Beans.createAndCopy(BeanMap.class, dto)
				.timestampConverter(Constants.FORMAT.TIMESTAMP).dateConverter(
						Constants.FORMAT.DATE).execute();

		param.putAll(bankInfo);
		this.updateBySqlFile("bank/UpdateBank.sql", param).execute();
	}

	/**
	 * キーカラム名を返します.
	 * @return 銀行マスタのキーカラム名
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "BANK_ID" };
	}

	/**
	 * テーブル名を返します.
	 * @return 銀行マスタ名
	 */
	@Override
	protected String getTableName() {
		return BankDwb.TABLE_NAME;
	}
}
