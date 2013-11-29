/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.MessageResourcesUtil;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.DiscountRelHist;
import jp.co.arkinfosys.entity.ProductHist;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;

/**
 * 商品情報履歴サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class ProductHistoryService extends AbstractService<BeanMap> {
	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String PRODUCT_CODE = "productCode";
	}

	/**
	 * パラメータを配列化
	 */
	private static final String[] paramArray = {Param.PRODUCT_CODE,};

	/**
	 * 変更履歴のリストを返します.
	 * @param params 検索条件のマップ
	 * @return 変更履歴のリスト
	 * @throws ServiceException
	 */
	public List<BeanMap> getHistroyList(BeanMap params) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(params, param);
			/**
			 * 商品履歴
			 */
			// SQLの結果リストから、それぞれの差分を取得する
			List<ProductHist> result = this.selectBySqlFile(ProductHist.class, "product/FindProductHistByCode.sql", param).getResultList();

			List<BeanMap> compList = new ArrayList<BeanMap>();

			// 1レコードづつ比較する
			ProductHist oldHist = null;
			boolean flgFirst = true;
			for(ProductHist newHist : result) {
				// INSERT
				if(AbstractService.ActionType.INSERT.equals(newHist.actionType)) {
					flgFirst = false;
					oldHist = newHist;
					addCompList(compList, 0, newHist.updDatetm, "追加", "", "");
					continue;
				}
				// 先頭
				if(flgFirst) {
					flgFirst = false;
					oldHist = newHist;
					continue;
				}

				try {
					Field[] fs = oldHist.getClass().getFields();
					for(int i = 4; i < fs.length - 1; i++) {
						Field f2 = newHist.getClass().getField(fs[i].getName());
						String columnName = fs[i].getName();

						// 比較処理
						Object o1 = fs[i].get(oldHist);
						Object o2 = f2.get(newHist);

						if(o1 == null || o2 == null) {
							if(o1 == null && o2 == null) {
								// nullで一致
							} else {
								// 不一致
								if(columnName.indexOf("Cdx") != -1) {
									i++;
									columnName = fs[i].getName();
									o1 = fs[i].get(oldHist);
									f2 = newHist.getClass().getField(fs[i].getName());
									o2 = f2.get(newHist);
								}
								addCompList(compList, i, newHist.updDatetm, MessageResourcesUtil.getMessage("labels.product." + columnName), o1, o2);
							}
							continue;
						}

						if(o1.equals(o2)) {
							// 一致
						} else {
							// 不一致
							// コードで不一致なら、名称を出力する
							if(columnName.indexOf("Cdx") != -1) {
								i++;
								columnName = fs[i].getName();
								o1 = fs[i].get(oldHist);
								f2 = newHist.getClass().getField(fs[i].getName());
								o2 = f2.get(newHist);
							}
							addCompList(compList, i, newHist.updDatetm, MessageResourcesUtil.getMessage("labels.product." + columnName), o1, o2);
						}
						// コードなら、名称を飛ばす
						if(columnName.indexOf("Cdx") != -1) {
							i++;
						}
					}
				} catch(SecurityException e) {
					e.printStackTrace();
				} catch(IllegalArgumentException e) {
					e.printStackTrace();
				} catch(NoSuchFieldException e) {
					e.printStackTrace();
				} catch(IllegalAccessException e) {
					e.printStackTrace();
				}

				oldHist = newHist;
			}

			/**
			 * 数量割引
			 */
			// SQLの結果リストから、それぞれの差分を取得する
			List<DiscountRelHist> resultDiscount = this.selectBySqlFile(DiscountRelHist.class, "product/FindDiscountRelHistByCode.sql", param).getResultList();

			// 1レコードづつ比較する
			DiscountRelHist oldRelHist = null;
			flgFirst = true;
			for(DiscountRelHist newRelHist : resultDiscount) {
				// INSERT
				if(AbstractService.ActionType.INSERT.equals(newRelHist.actionType) || AbstractService.ActionType.DELETE.equals(newRelHist.actionType)) {
					Field[] fs = newRelHist.getClass().getFields();
					String columnName = fs[5].getName();
					flgFirst = false;
					oldRelHist = newRelHist;
					Object o1 = fs[5].get(newRelHist);

					if(AbstractService.ActionType.INSERT.equals(newRelHist.actionType)) {
						// 追加
						addCompList(compList, 1000, newRelHist.updDatetm, MessageResourcesUtil.getMessage("labels.product." + columnName), "", o1);
					} else {
						// 削除
						addCompList(compList, 1000, newRelHist.updDatetm, MessageResourcesUtil.getMessage("labels.product." + columnName), o1, "");
					}
					continue;
				}
				// 先頭
				if(flgFirst) {
					flgFirst = false;
					oldRelHist = newRelHist;
					continue;
				}
				try {
					Field[] fs = oldRelHist.getClass().getFields();
					for(int i = 5; i < fs.length - 1; i++) {
						Field f2 = newRelHist.getClass().getField(fs[i].getName());
						String columnName = fs[i].getName();

						// 比較処理
						Object o1 = fs[i].get(oldRelHist);
						Object o2 = f2.get(newRelHist);

						if(o1 == null || o2 == null) {
							if(o1 == null && o2 == null) {
								// nullで一致
							} else {
								// 不一致
								if(columnName.indexOf("Cdx") != -1) {
									i++;
									columnName = fs[i].getName();
									o1 = fs[i].get(oldRelHist);
									f2 = newRelHist.getClass().getField(fs[i].getName());
									o2 = f2.get(newRelHist);
								}
								addCompList(compList, 1000 + i, newRelHist.updDatetm, MessageResourcesUtil.getMessage("labels.product." + columnName), o1, o2);
							}
							continue;
						}

						if(o1.equals(o2)) {
							// 一致
						} else {
							// 不一致
							// コードで不一致なら、名称を出力する
							if(columnName.indexOf("Cdx") != -1) {
								i++;
								columnName = fs[i].getName();
								o1 = fs[i].get(oldRelHist);
								f2 = newRelHist.getClass().getField(fs[i].getName());
								o2 = f2.get(newRelHist);
							}
							addCompList(compList, 1000 + i, newRelHist.updDatetm, MessageResourcesUtil.getMessage("labels.product." + columnName), o1, o2);
						}
						// コードなら、名称を飛ばす
						if(columnName.indexOf("Cdx") != -1) {
							i++;
						}
					}
				} catch(SecurityException e) {
					e.printStackTrace();
				} catch(IllegalArgumentException e) {
					e.printStackTrace();
				} catch(NoSuchFieldException e) {
					e.printStackTrace();
				} catch(IllegalAccessException e) {
					e.printStackTrace();
				}
				oldRelHist = newRelHist;
			}

			// 更新日順にソート
			BeanMap[] array = (BeanMap[])compList.toArray(new BeanMap[0]);
			java.util.Arrays.sort(array, new MyComparator());

			compList = Arrays.asList(array);
			return compList;

		} catch(Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 検索条件を指定して、結果リストを返します.
	 * @param sql SQLファイル名
	 * @param conditions 検索条件のマップ
	 * @return 検索結果のリスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findRecordByCondition(String sql, Map<String, Object> conditions) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(BeanMap.class, sql, param).getResultList();

		} catch(Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 空の検索条件マップを作成します.
	 * @param param 検索条件のマップ
	 * @return 検索条件キーのみ設定した検索条件マップ
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		for(int i = 0; i < ProductHistoryService.paramArray.length; i++) {
			param.put(ProductHistoryService.paramArray[i], null);
		}
		return param;
	}

	/**
	 * 検索条件を設定します.
	 * @param conditions 検索条件値のマップ
	 * @param param 検索条件のマップ
	 * @return
	 */
	private Map<String, Object> setConditionParam(Map<String, Object> conditions, Map<String, Object> param) {
		for(int i = 0; i < ProductHistoryService.paramArray.length; i++) {
			String key = ProductHistoryService.paramArray[i];
			if(conditions.containsKey(key)) {
				if(StringUtil.hasLength((String)conditions.get(key))) {
					param.put(key, (String)conditions.get(key));
				}
			}
		}

		return param;
	}

	/**
	 * 変更内容を出力リストに追加します.
	 * @param compList 出力リスト
	 * @param keyValue キー値
	 * @param updDatetm 更新日時
	 * @param colName カラム名
	 * @param before 変更前オブジェクト
	 * @param after 変更後オブジェクト
	 */
	private void addCompList(List<BeanMap> compList, int keyValue, Timestamp updDatetm, String colName, Object before, Object after) {
		String beforeStr = "";
		String afterStr = "";

		if(before != null) {
			beforeStr = before.toString();
		}

		if(after != null) {
			afterStr = after.toString();
		}

		BeanMap compData = new BeanMap();
		compData.put("sortKey", Integer.valueOf(keyValue));
		compData.put("updDatetm", updDatetm);
		compData.put("colName", colName);
		compData.put("before", beforeStr);
		compData.put("after", afterStr);

		compList.add(compData);
	}

}
