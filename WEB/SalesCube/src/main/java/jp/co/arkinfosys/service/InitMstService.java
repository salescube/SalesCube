/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.InitMstDto;
import jp.co.arkinfosys.entity.InitMst;
import jp.co.arkinfosys.entity.join.InitMstJoin;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.PropertyNotFoundRuntimeException;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.beans.util.Beans;

/**
 * 初期値マスタサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class InitMstService extends AbstractService<InitMst> {

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	private static class Param {
		public static final String TABLE_NAME = "tableName";
		public static final String COLUMN_NAME = "columnName";
		public static final String STR_DATA = "strData";
		public static final String NUM_DATA = "numData";
		public static final String FLT_DATA = "fltData";
	}

	/**
	 * データタイプ定義クラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class DataType {
		public static final String STRING_VALUE = "1";
		public static final String INTEGER_VALUE = "2";
		public static final String FLOAT_VALUE = "3";
	}

	/**
	 * テーブル名を指定して、初期値マスタ情報のリストを返します.
	 * @param tableName テーブル名
	 * @return 初期値マスタ情報{@link InitMstJoin}のリスト
	 * @throws ServiceException
	 */
	public List<InitMstJoin> findInitDataByTableNameWithCategory(
			String tableName) throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(InitMstService.Param.TABLE_NAME, tableName);

			// 0:未使用は検索にヒットさせないようにする
			return this.selectBySqlFile(InitMstJoin.class,
					"initmst/FindInitDataByTableNameWithCategory.sql", param)
					.getResultList();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * エンティティからDTOへの変換を行います.
	 * @param initMstJoinList 初期値マスタエンティティのリスト
	 * @return 初期値マスタDTOのリスト
	 * @throws ServiceException
	 */
	public List<InitMstDto> convertEntityToDto(List<InitMstJoin> initMstJoinList)
			throws ServiceException {
		List<InitMstDto> initMstDtoList = new ArrayList<InitMstDto>();

		Map<String, InitMstDto> temp = new HashMap<String, InitMstDto>();
		for (InitMstJoin initMstJoin : initMstJoinList) {
			InitMstDto dto = temp.get(initMstJoin.tableName
					+ initMstJoin.columnName);
			if (dto == null) {
				dto = Beans.createAndCopy(InitMstDto.class, initMstJoin)
						.timestampConverter(Constants.FORMAT.TIMESTAMP)
						.dateConverter(Constants.FORMAT.DATE).execute();
				dto.masterList = new ArrayList<LabelValueBean>();
				initMstDtoList.add(dto);
				temp.put(dto.tableName + dto.columnName, dto);
			}

			if (initMstJoin.categoryId != null
					&& initMstJoin.categoryId.intValue() > 0) {
				dto.masterList
						.add(new LabelValueBean(initMstJoin.categoryCodeName,
								initMstJoin.categoryCode));
			}
		}

		return initMstDtoList;
	}

	/**
	 * 初期値マスタを更新します.
	 * @param initMustDtoList 初期値マスタ情報のリスト
	 * @throws ServiceException
	 * @throws UnabledLockException
	 */
	public void updateInitData(List<InitMstDto> initMustDtoList)
			throws ServiceException, UnabledLockException {
		if (initMustDtoList == null) {
			return;
		}

		Map<String, Object> param = super.createSqlParam();
		this.setEmptyCondition(param);

		// 先頭レコードでロックを取得する
		for (InitMstDto dto : initMustDtoList) {
			param.put(InitMstService.Param.TABLE_NAME, dto.tableName);
			param.put(InitMstService.Param.COLUMN_NAME, dto.columnName);
			super.lockRecordBySqlFile(
					"initmst/LockInitDataByTableNameAndColumnName.sql", param,
					dto.updDatetm);
			break;
		}

		for (InitMstDto dto : initMustDtoList) {
			this.setEmptyCondition(param);
			param.put(InitMstService.Param.TABLE_NAME, dto.tableName);
			param.put(InitMstService.Param.COLUMN_NAME, dto.columnName);

			if (DataType.STRING_VALUE.equals(dto.useDataType)) {
				param.put(InitMstService.Param.STR_DATA, dto.strData);
			} else if (DataType.INTEGER_VALUE.equals(dto.useDataType)) {
				param.put(InitMstService.Param.NUM_DATA, dto.numData);
			} else if (DataType.FLOAT_VALUE.equals(dto.useDataType)) {
				param.put(InitMstService.Param.FLT_DATA, dto.fltData);
			}

			super.updateBySqlFile("initmst/UpdateInitData.sql", param)
					.execute();
		}
	}

	/**
	 * テーブル名を指定して、初期値マップを返します.
	 * @param tableName テーブル名
	 * @return 初期値マップ
	 * @throws ServiceException
	 */
	public Map<String, Object> findInitDataByTableName(String tableName)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(InitMstService.Param.TABLE_NAME, tableName);

			// 0:未使用は検索にヒットさせないようにする
			List<InitMst> list = this.selectBySqlFile(InitMst.class,
					"initmst/FindInitDataByTableName.sql", param)
					.getResultList();

			Map<String, Object> result = new HashMap<String, Object>();
			for (InitMst record : list) {
				if (DataType.STRING_VALUE.equals(record.useDataType)) {
					result.put(record.columnName, record.strData);
				} else if (DataType.INTEGER_VALUE.equals(record.useDataType)) {
					result.put(record.columnName, record.numData);
				} else if (DataType.FLOAT_VALUE.equals(record.useDataType)) {
					result.put(record.columnName, record.fltData);
				}
			}

			return result;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 空の検索条件マップを作成します.
	 * @param param 検索条件マップ
	 * @return 検索条件キーのみ設定した検索条件マップ
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		param.put(InitMstService.Param.TABLE_NAME, null);
		param.put(InitMstService.Param.COLUMN_NAME, null);
		param.put(InitMstService.Param.STR_DATA, null);
		param.put(InitMstService.Param.NUM_DATA, null);
		param.put(InitMstService.Param.FLT_DATA, null);
		return param;
	}

	/**
	 * テーブル名とJavaBeanオブジェクトを指定して、初期値マスタによる初期値設定を行います.<br>
	 * JavaBeanオブジェクトのプロパティ名は、対象とするテーブルのエンティティクラスのプロパティ名と同一である必要があります.
	 * @param tableName テーブル名
	 * @param bean JavaBeanオブジェクト
	 * @throws ServiceException
	 */
	public void initBean(String tableName, Object bean) throws ServiceException {
		try {
			Map<String, Object> initValues = this
					.findInitDataByTableName(tableName);
			if (initValues == null || initValues.isEmpty()) {
				return;
			}

			BeanDesc beanDesc = BeanDescFactory.getBeanDesc(bean.getClass());
			if (beanDesc == null) {
				return;
			}

			Iterator<Entry<String, Object>> ite = initValues.entrySet()
					.iterator();
			while (ite.hasNext()) {
				Entry<String, Object> entry = ite.next();
				if(entry.getValue() == null) {
					continue;
				}

				try {
					PropertyDesc prop = beanDesc.getPropertyDesc(entry.getKey()
							.replace("_", ""));

					if (prop.getPropertyType() == String.class) {
						// 文字列型であればtoString後に設定
						prop.getField().set(bean, entry.getValue().toString());
					} else {
						prop.getField().set(bean, entry.getValue());
					}
				} catch (PropertyNotFoundRuntimeException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
