/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.CategoryDto;
import jp.co.arkinfosys.dto.master.TaxRateDto;
import jp.co.arkinfosys.entity.TaxRate;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;

/**
 *
 * 税率情報サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class TaxRateService extends
		AbstractMasterEditService<TaxRateDto, TaxRate> {

	/**
	 *
	 * パラメータ定義クラスです.
	 *
	 */
	public static class Param {
		public static final String TAX_TYPE_CATEGORY = "taxTypeCategory";
		public static final String START_DATE = "startDate";
		public static final String TAX_RATE = "taxRate";
		public static final String SORT_COLUMN = "sortColumn";
	}

	private static final String COLUMN_START_DATE = "START_DATE";

	@Resource
	public CategoryService categoryService;

	/**
	 * 税区分と開始日をキーに、税率情報を取得します.
	 * @param taxTypeCategory 税区分
	 * @param startDate 開始日
	 * @return 税率情報エンティティ
	 * @throws ServiceException
	 */
	public TaxRate findTaxRateById(String taxTypeCategory, String startDate)
			throws ServiceException {
		Map<String, Object> param = super.createSqlParam();
		param.put(TaxRateService.Param.TAX_TYPE_CATEGORY, taxTypeCategory);
		param.put(TaxRateService.Param.START_DATE, startDate);

		return this.selectBySqlFile(TaxRate.class,
				"taxrate/FindTaxRateById.sql", param).getSingleResult();
	}

	/**
	 * 税区分をキーに、税率情報の一覧を開始年月日でソートして取得します.
	 * @param taxTypeCategory 税区分
	 * @return 税率情報エンティティのリスト
	 * @throws ServiceException
	 */
	public List<TaxRate> findTaxRateByTaxTypeCagory(String taxTypeCategory)
			throws ServiceException {
		Map<String, Object> param = super.createSqlParam();
		param.put(TaxRateService.Param.TAX_TYPE_CATEGORY, taxTypeCategory);
		param.put(TaxRateService.Param.SORT_COLUMN,
				TaxRateService.COLUMN_START_DATE);

		return this.selectBySqlFile(TaxRate.class,
				"taxrate/FindTaxRateByTaxTypeCategory.sql", param)
				.getResultList();
	}

	/**
	 * 税率情報を更新します.
	 * @param taxRateList 税率情報DTOリスト
	 * @param categoryCode 区分データコード
	 * @param updDatetm 更新日時
	 * @throws Exception
	 */
	public void updateRecords(List<TaxRateDto> taxRateList,
			String categoryCode, String updDatetm) throws Exception {

		if (taxRateList == null || taxRateList.size() == 0) {
			return; // 何もしない
		}

		// 親の区分データを更新する

		// CategoryDtoを生成する
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.categoryId = String.valueOf(Categories.TAX_TYPE_CATEGORY);
		categoryDto.categoryCode = categoryCode;
		categoryDto.updDatetm = updDatetm;

		// ロックと更新日設定
		categoryService.updateRecord(categoryDto);

		// 現在の税率マスタのリストを取得
		List<TaxRate> list = findTaxRateByTaxTypeCagory(categoryCode);

		// レコード削除
		for (TaxRate taxRate : list) {
			boolean isExist = false;
			for (TaxRateDto taxRateDto : taxRateList) {
				if (taxRateDto.equalsKey(taxRate)) {
					isExist = true;
					break;
				}
			}
			if (isExist) {
				continue;
			}

			// 画面に存在しない物は削除
			super.updateAudit(Beans.createAndCopy(TaxRateDto.class, taxRate)
					.execute().getKeys());
			this.deleteRecord(Beans.createAndCopy(TaxRateDto.class, taxRate)
					.execute());
		}

		// 新規レコード追加
		for (TaxRateDto dto : taxRateList) {
			// 画面にのみ存在する物のみ追加
			boolean isInsert = true;
			boolean isUpdate = false;
			for (TaxRate taxRate : list) {
				if (dto.equalsKey(taxRate)) {
					// DBにあるので追加しない
					isInsert = false;
					if (!dto.equalsValue(taxRate)) {
						// DBから変更されているので更新
						isUpdate = true;
					}
					break;
				}
			}
			// 登録
			if (isInsert) {
				this.insertRecord(dto);
			} else if (isUpdate) {
				this.updateRecord(dto);
			}
		}
	}

	/**
	 * 税率情報DTOを指定して税率情報を削除します.
	 * @param dto 税率情報DTO
	 * @throws Exception
	 */
	@Override
	public void deleteRecord(TaxRateDto dto) throws Exception {
		// 履歴レコード作成
		Map<String, Object> param = super.createSqlParam();
		TaxRate taxRate = Beans.createAndCopy(TaxRate.class, dto)
				.timestampConverter(Constants.FORMAT.TIMESTAMP).dateConverter(
						Constants.FORMAT.DATE).execute();
		param.put(Param.TAX_TYPE_CATEGORY, taxRate.taxTypeCategory);
		param.put(Param.START_DATE, taxRate.startDate);

		// データ削除
		this.updateBySqlFile("taxrate/DeleteTaxRate.sql", param).execute();
	}

	/**
	 * 税率情報を登録します.
	 * @param dto 税率情報DTO
	 * @throws Exception
	 */
	@Override
	public void insertRecord(TaxRateDto dto) throws Exception {
		Map<String, Object> param = super.createSqlParam();
		TaxRate taxRate = Beans.createAndCopy(TaxRate.class, dto)
				.timestampConverter(Constants.FORMAT.TIMESTAMP).dateConverter(
						Constants.FORMAT.DATE).execute();
		param.put(Param.TAX_TYPE_CATEGORY, taxRate.taxTypeCategory);
		param.put(Param.START_DATE, taxRate.startDate);
		param.put(Param.TAX_RATE, taxRate.taxRate);

		// データ登録
		this.updateBySqlFile("taxrate/InsertTaxRate.sql", param).execute();
	}

	/**
	 * 税率情報を更新します.
	 * @param dto 税率情報DTO
	 * @throws Exception
	 */
	@Override
	public void updateRecord(TaxRateDto dto) throws Exception {
		Map<String, Object> param = super.createSqlParam();
		TaxRate taxRate = Beans.createAndCopy(TaxRate.class, dto)
				.timestampConverter(Constants.FORMAT.TIMESTAMP).dateConverter(
						Constants.FORMAT.DATE).execute();
		param.put(Param.TAX_TYPE_CATEGORY, taxRate.taxTypeCategory);
		param.put(Param.START_DATE, taxRate.startDate);
		param.put(Param.TAX_RATE, taxRate.taxRate);

		// データ登録
		this.updateBySqlFile("taxrate/UpdateTaxRate.sql", param).execute();
	}

	/**
	 * キーカラム名を返します.
	 * @return 税率情報テーブルのキーカラム名
	 */
	@Override
	protected String[] getKeyColumnNames() {
		return new String[] { "TAX_TYPE_CATEGORY", "START_DATE" };
	}

	/**
	 * テーブル名を返します.
	 * @return 税率情報テーブル名
	 */
	@Override
	protected String getTableName() {
		return TaxRate.TABLE_NAME;
	}
}
