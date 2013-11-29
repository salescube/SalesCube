/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.stock;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Categories;
import jp.co.arkinfosys.dto.stock.EadSlipTrnDto;
import jp.co.arkinfosys.entity.EadSlipTrn;
import jp.co.arkinfosys.entity.Rack;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.service.AbstractSlipService;
import jp.co.arkinfosys.service.CategoryService;
import jp.co.arkinfosys.service.EadService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.ProductStockService;
import jp.co.arkinfosys.service.RackService;
import jp.co.arkinfosys.service.SeqMakerService;
import jp.co.arkinfosys.service.YmService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.struts.util.LabelValueBean;

/**
 * 入出庫伝票共通サービスクラスです.
 * @author Ark Information Systems
 *
 */
public abstract class CommonInputStockService extends
		AbstractSlipService<EadSlipTrn, EadSlipTrnDto> {

	@Resource
	protected CategoryService categoryService;

	@Resource
	protected EadService eadService;

	@Resource
	protected ProductService productService;

	@Resource
	protected ProductStockService productStockService;

	@Resource
	protected SeqMakerService seqMakerService;

	@Resource
	protected RackService rackService;

	@Resource
	protected YmService ymService;

	/**
	 * 商品情報を返します.
	 *
	 * @param productCode 商品コード
	 * @return 商品情報
	 * @throws ServiceException
	 */
	public ProductJoin findProductByCode(String productCode)
			throws ServiceException {
		return productService.findById(productCode);
	}

	/**
	 * 棚情報を取得します.
	 * @param rackCode 棚番
	 * @return 棚情報
	 * @throws ServiceException
	 */
	public Rack findRackByCode(String rackCode) throws ServiceException {
		return rackService.findById(rackCode);
	}

	/**
	 * 締処理されていない入庫数をカウントして取得します.<br>
	 * 商品コードと棚番をキーに検索を行います.
	 *
	 * @param productCode
	 *            商品コード
	 * @param rackCode
	 *            棚番
	 * @return 締処理されていない入庫数
	 * @throws ServiceException
	 */
	public int countUnclosedQuantityByProductCode(String productCode,
			String rackCode) throws ServiceException {
		return eadService.countUnclosedQuantityByProductCode(productCode,
				rackCode);
	}

	/**
	 * 前回の在庫締時点での自社倉庫における商品在庫数をカウントして取得します.<br>
	 * 商品コードと棚番をキーに検索を行います.
	 *
	 * @param productCode 商品コード
	 * @param rackCode 棚番
	 * @return 前回の在庫締時点での自社倉庫における商品在庫数
	 * @throws ServiceException
	 */
	public int countClosedQuantityByProductCode(String productCode,
			String rackCode) throws ServiceException {
		return productStockService.countClosedQuantityByProductCode(
				productCode, rackCode);
	}

	/**
	 * 入出庫伝票区分リストを返します.
	 * @return 入出庫伝票区分リスト
	 * @throws ServiceException
	 */
	public List<LabelValueBean> getSlipCategoryList() throws ServiceException {
		return categoryService
				.findCategoryLabelValueBeanListById(Categories.EAD_SLIP_CATEGORY);
	}

	/**
	 * 入出庫区分リストを返します.
	 * @return 入出庫区分リスト
	 * @throws ServiceException
	 */
	public List<LabelValueBean> getCategoryList() throws ServiceException {
		return categoryService
				.findCategoryLabelValueBeanListById(Categories.EAD_CATEGORY);
	}

	/**
	 * 入出庫伝票を削除します.
	 * @param id 入出庫伝票番号
	 * @param updDatetm 削除対象伝票の更新日時
	 * @return ロック結果
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	@Override
	public int deleteById(String id, String updDatetm) throws ServiceException,
			UnabledLockException {
		// 排他制御
		Map<String, Object> param = super.createSqlParam();

		param.put(EadService.Param.EAD_SLIP_ID, id);
		int lockResult = lockRecordBySqlFile("ead/LockSlipByEadSlipId.sql",
				param, updDatetm);

		eadService.deleteSlipByEadSlipId(Integer.valueOf(id));

		return lockResult;
	}

	/**
	 * キーカラム名を返します.
	 * @return 入出庫伝票テーブルのキーカラム名
	 */
	@Override
	protected String getKeyColumnName() {
		return "EAD_SLIP_ID";
	}

	/**
	 * テーブル名を返します.
	 * @return 入出庫伝票テーブル名
	 */
	@Override
	protected String getTableName() {
		return "EAD_SLIP_TRN";
	}

}
