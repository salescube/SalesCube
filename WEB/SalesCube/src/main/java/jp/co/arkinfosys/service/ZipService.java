/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.Zip;
import jp.co.arkinfosys.service.exception.ServiceException;

/**
 * 郵便番号マスタサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class ZipService extends AbstractService<Zip> implements MasterSearch<Zip> {

	/**
	 * パラメータ定義クラスです.
	 */
	public static class Param {
		/** 郵便番号 **/
		public static final String ZIP_CODE = "zipCode";

		public static final String ZIP_ID = "zipId";
		public static final String ZIP_ADDRESS_1 = "zipAddress1";
		public static final String ZIP_ADDRESS_2 = "zipAddress2";
		/** ソートカラム **/
		private static final String SORT_COLUMN = "sortColumn";
		/** ソートオーダー **/
		private static final String SORT_ORDER = "sortOrder";
	}

	/**
	 * 郵便番号を指定して住所情報を取得します.
	 * @param zipCode 郵便番号
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @return 郵便番号エンティティのリスト
	 * @throws ServiceException
	 */
	public List<Zip> findAddressByZipCode(String zipCode, String sortColumn, boolean sortOrderAsc) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			Map<String, Object> conditions = new HashMap<String, Object>();
			conditions.put(Param.ZIP_CODE, zipCode.replaceAll("-", ""));
			this.setCondition(param, conditions, sortColumn, sortOrderAsc);

			return this.selectBySqlFile(Zip.class,
					"zip/FindAddressByZipCode.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して住所情報を取得します.
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @return 郵便番号エンティティのリスト
	 * @throws ServiceException
	 */
	@Override
	public List<Zip> findByCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);
			this.setCondition(param, conditions, sortColumn, sortOrderAsc);

			return this.selectBySqlFile(Zip.class,
					"zip/FindAddressByCondition.sql", param).getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して結果件数を取得します.<br>
	 * 未使用です.
	 * @param conditions 検索条件
	 * @return 0(固定値)
	 * @throws ServiceException
	 */
	@Override
	public int countByCondition(Map<String, Object> conditions)
			throws ServiceException {
		// 未使用メソッド
		return 0;
	}

	/**
	 * 検索条件を指定して住所情報を取得します.<br>
	 * 未使用です.
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @param rowCount 表示数
	 * @param offset 表示ページ数
	 * @return 郵便番号エンティティのリスト
	 * @throws ServiceException
	 */
	@Override
	public List<Zip> findByConditionLimit(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		// 未使用メソッド
		return null;
	}

	/**
	 * 郵便番号IDを指定して、郵便番号マスタ情報を取得します.<br>
	 * 未使用です.
	 * @param id 郵便番号ID
	 * @return 郵便番号エンティティ
	 * @throws ServiceException
	 */
	@Override
	public Zip findById(String id) throws ServiceException {
		// 未使用メソッド
		return null;
	}

	/**
	 * 指定された条件の住所が存在するかチェックします.
	 * @param zipCode 郵便番号
	 * @param address 住所
	 * @return 存在するか否か
	 * @throws ServiceException
	 */
	public boolean checkZipCodeAndAddress(String zipCode, String address) throws ServiceException {
		try {
			if(zipCode == null || address == null) {
				return true;
			}

			Map<String, Object> param = super.createSqlParam();
			this.setEmptyCondition(param);

			Map<String, Object> conditions = new HashMap<String, Object>();
			conditions.put(Param.ZIP_CODE, zipCode.replaceAll("-", ""));
			this.setCondition(param, conditions, Param.ZIP_ID, true);

			List<Zip> zipList = this.selectBySqlFile(Zip.class,
					"zip/FindAddressByZipCode.sql", param).getResultList();

			String padAddress = StringUtil.trimBlank(address);
			for(Zip zip : zipList) {
				String temp = zip.zipAddress1;
				if(temp == null) {
					continue;
				}
				if(padAddress.startsWith(temp)) {
					return true;
				}
				// 「の」や「が」を除く
				temp = temp.replaceAll("ノ|の|ﾉ|が|ケ|ヶ|ｹ", "");
				if(padAddress.startsWith(temp)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 空の検索条件オブジェクトを作成します.
	 *
	 * @param param 検索条件オブジェクト
	 * @return 空の検索条件オブジェクト
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(ZipService.Param.ZIP_CODE, null);
		param.put(ZipService.Param.ZIP_ADDRESS_1, null);
		param.put(ZipService.Param.ZIP_ADDRESS_2, null);
		param.put(ZipService.Param.SORT_COLUMN, null);
		param.put(ZipService.Param.SORT_ORDER, null);
		return param;
	}

	/**
	 * 検索条件パラメータを設定して返します.
	 *
	 * @param param 検索条件パラメータ
	 * @param conditions 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @return 検索条件が設定された検索条件パラメータ
	 */
	private Map<String, Object> setCondition(Map<String, Object> param,
			Map<String, Object> conditions, String sortColumn,
			boolean sortOrderAsc) {
		// 郵便番号
		if (conditions.containsKey(ZipService.Param.ZIP_CODE)) {
			param.put(ZipService.Param.ZIP_CODE, super
					.createPrefixSearchCondition((String) conditions
							.get(ZipService.Param.ZIP_CODE)));
		}

		// 住所1
		if (conditions.containsKey(ZipService.Param.ZIP_ADDRESS_1)) {
			param.put(ZipService.Param.ZIP_ADDRESS_1, super
					.createPartialSearchCondition((String) conditions
							.get(ZipService.Param.ZIP_ADDRESS_1)));
		}

		// 住所2
		if (conditions.containsKey(ZipService.Param.ZIP_ADDRESS_2)) {
			param.put(ZipService.Param.ZIP_ADDRESS_2, super
					.createPartialSearchCondition((String) conditions
							.get(ZipService.Param.ZIP_ADDRESS_2)));
		}

		// ソートカラムを設定する
		if (conditions.containsKey(Param.SORT_COLUMN)) {
			if (StringUtil.hasLength((String)conditions.get(Param.SORT_COLUMN))) {
				param.put(Param.SORT_COLUMN,
						StringUtil.convertColumnName((String)conditions.get(Param.SORT_COLUMN)));
			}
		}

		// ソートオーダーを設定する
		if (sortOrderAsc) {
			param.put(ZipService.Param.SORT_ORDER, Constants.SQL.ASC);
		} else {
			param.put(ZipService.Param.SORT_ORDER, Constants.SQL.DESC);
		}

		return param;
	}

	/**
	 * 郵便番号マスタの全データを削除します.
	 * @throws ServiceException
	 */
	public void deleteAllRecords() throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			this.updateBySqlFile("zip/deleteAll.sql", param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * 郵便番号マスタにデータを登録します.
	 * @param zipId 郵便番号ID
	 * @param zipCode 郵便番号
	 * @param address 住所
	 * @throws ServiceException
	 */
	public void insertRecord(int zipId, String zipCode, String address) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(Param.ZIP_ID, Integer.valueOf(zipId));
			param.put(Param.ZIP_CODE, zipCode);
			param.put(Param.ZIP_ADDRESS_1, address);
			param.put(Param.ZIP_ADDRESS_2, null);

			this.updateBySqlFile("zip/InsertZipCode.sql", param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
