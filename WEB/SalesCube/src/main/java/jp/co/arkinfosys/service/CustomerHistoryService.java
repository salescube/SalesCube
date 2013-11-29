/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.common.MessageResourcesUtil;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.CustomerHist;
import jp.co.arkinfosys.entity.DeliveryBillHist;
import jp.co.arkinfosys.entity.DeliveryHist;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;

/**
 * 顧客履歴のサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class CustomerHistoryService extends AbstractService<BeanMap> {
	/** Excel出力時種別定義 : 顧客 */
	private static final String KIND_CUSTOMER = "顧客";

	/** Excel出力時種別定義 : 請求先 */
	private static final String KIND_BILL = "請求先";

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String CUSTOMER_CODE = "customerCode";
	}

	/**
	 * パラメータを配列化
	 */
	private static final String[] paramArray = {
		Param.CUSTOMER_CODE,
	};

	/**
	 * 変更履歴のリストを返します.
	 * @param params 検索条件パラメータのマップ
	 * @return 変更履歴のリスト
	 * @throws ServiceException
	 */
	public List<BeanMap> getHistroyList(BeanMap params) throws ServiceException {
		try {

			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(params, param);
			/**
			 * 顧客
			 */
			//SQLの結果リストから、それぞれの差分を取得する
			List<CustomerHist>result = this.selectBySqlFile(
					CustomerHist.class, "customer/FindCustomerHistByCode.sql",
					param).getResultList();

			List<BeanMap>compList = new ArrayList<BeanMap>();

			// 1レコードづつ比較する
			CustomerHist oldHist = null;
			boolean flgFirst = true;
			for (CustomerHist newHist : result) {
				// INSERT
				if (AbstractService.ActionType.INSERT.equals(newHist.actionType)){
					flgFirst = false;
					oldHist = newHist;
//					addCompList(compList, 0, newHist.updDatetm,"顧客","追加","","" );
					try {
						addCustomerHist( compList, null, newHist );
					} catch (Exception e) {
						e.printStackTrace();
						throw new ServiceException( e );
					}
					continue;
				}
				// 先頭
				if( flgFirst){
					flgFirst = false;
					oldHist = newHist;
					continue;
				}
				try {
					addCustomerHist( compList, oldHist, newHist );
				} catch (SecurityException e) {
					e.printStackTrace();
					throw new ServiceException( e );
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					throw new ServiceException( e );
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
					throw new ServiceException( e );
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					throw new ServiceException( e );
				}
				oldHist = newHist;
			}

			/**
			 * 納品先
			 */
			//SQLの結果リストから、それぞれの差分を取得する
			List<DeliveryHist> resultDelivery = this.selectBySqlFile(
					DeliveryHist.class, "customer/FindDeliveryHistByCode.sql",
					param).getResultList();

			// 1レコードづつ比較する
			DeliveryHist oldDeliHist = null;
			flgFirst = true;
			for (DeliveryHist newDelyHist : resultDelivery) {
				// INSERT
				if (AbstractService.ActionType.INSERT.equals(newDelyHist.actionType)){
					flgFirst = false;
					oldDeliHist = newDelyHist;
//					addCompList(compList, 1000, newDelyHist.updDatetm,newDelyHist.deliverDeliveryName,"納入先情報追加","","" );
					try {
						addDeliveryHist( compList, null, newDelyHist );
					} catch (Exception e) {
						throw new ServiceException( e );
					}
					continue;
				}else if(AbstractService.ActionType.DELETE.equals(newDelyHist.actionType)){
					oldDeliHist = newDelyHist;
					addCompList(compList, 2000, newDelyHist.updDatetm,newDelyHist.deliverDeliveryName,"** 納入先情報 **","","** 削除  **" );
					continue;
				}
				// 先頭
				if( flgFirst){
					flgFirst = false;
					oldDeliHist = newDelyHist;
					continue;
				}
				try {
					addDeliveryHist( compList, oldDeliHist, newDelyHist );
				} catch (SecurityException e) {
					e.printStackTrace();
					throw new ServiceException( e );
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					throw new ServiceException( e );
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
					throw new ServiceException( e );
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					throw new ServiceException( e );
				}
				oldDeliHist = newDelyHist;
			}

			/**
			 * 請求先
			 */
			//SQLの結果リストから、それぞれの差分を取得する
			List<DeliveryBillHist> resultBill = this.selectBySqlFile(
					DeliveryBillHist.class, "customer/FindBillHistByCode.sql",
					param).getResultList();

			// 1レコードづつ比較する
			DeliveryBillHist oldBillHist = null;
			flgFirst = true;
			for (DeliveryBillHist newBillHist : resultBill) {
				// INSERT
				if (AbstractService.ActionType.INSERT.equals(newBillHist.actionType)){
					flgFirst = false;
					oldBillHist = newBillHist;
//					addCompList(compList, 3000, newBillHist.updDatetm, "請求先", "追加","","" );
					try {
						addBillHist( compList, null, newBillHist );
					} catch (Exception e) {
						throw new ServiceException( e );
					}
					continue;
				}else if(AbstractService.ActionType.DELETE.equals(newBillHist.actionType)){
					oldBillHist = newBillHist;
					addCompList(compList, 4000,newBillHist.updDatetm, CustomerHistoryService.KIND_BILL, "削除","","" );
					continue;
				}
				// 先頭
				if( flgFirst){
					flgFirst = false;
					oldBillHist = newBillHist;
					continue;
				}
				try {
					addBillHist( compList, oldBillHist, newBillHist );
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				oldBillHist = newBillHist;
			}

			// 更新日順にソート
			BeanMap[] array=(BeanMap[])compList.toArray(new BeanMap[0]);
			java.util.Arrays.sort( array, new MyComparator());

			compList = Arrays.asList(array);
			return compList;

		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * 顧客の履歴情報を比較し、差分をリストに追加します.
	 * @param compList 履歴出力用のリスト
	 * @param oldHist 旧顧客情報
	 * @param newHist 新顧客情報
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	protected void addCustomerHist( List<BeanMap> compList, CustomerHist oldHist, CustomerHist newHist )
		throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field[] fs = ( oldHist != null ) ? oldHist.getClass().getFields() : newHist.getClass().getFields();

		for(int i = 4; i < fs.length-1; i++) {
			Field f2 = newHist.getClass().getField(fs[i].getName());
			String columnName = fs[i].getName();

			// 比較処理
			Object o1 = (oldHist == null ) ? null : fs[i].get(oldHist);
			Object o2 = f2.get(newHist);

			if(o1 == null || o2 == null) {
				if(o1 == null && o2 == null) {
					// nullで一致
				}
				else {
					// 不一致
					if( columnName.indexOf("Cdx")!=-1 ){
						i++;
						columnName = fs[i].getName();
						o1 = ( oldHist == null ) ? null : fs[i].get(oldHist);
						f2 = newHist.getClass().getField(fs[i].getName());
						o2 = f2.get(newHist);
					}
					if(o1 == null && (o2 == null || "".equals(o2)) ) {
						continue;
					}
					addCompList(compList, i, newHist.updDatetm,CustomerHistoryService.KIND_CUSTOMER,
							MessageResourcesUtil.getMessage("labels.report.mst.customermst." + columnName),
							o1,o2);
				}
				continue;
			}

			if(o1.equals(o2)) {
				// 一致
			}
			else {
				// 不一致
				// コードで不一致なら、名称を出力する
				if( columnName.indexOf("Cdx")!=-1 ){
					i++;
					columnName = fs[i].getName();
					o1 = fs[i].get(oldHist);
					f2 = newHist.getClass().getField(fs[i].getName());
					o2 = f2.get(newHist);
				}
				addCompList(compList, i, newHist.updDatetm,CustomerHistoryService.KIND_CUSTOMER,
						MessageResourcesUtil.getMessage("labels.report.mst.customermst." + columnName),
						o1,o2 );
			}
			// コードなら、名称を飛ばす
			if( columnName.indexOf("Cdx")!=-1 ){
				i++;
			}
		}

	}

	/**
	 * 納入先の履歴情報を比較し、差分をリストに追加します.
	 * @param compList 履歴出力用のリスト
	 * @param oldDeliHist 旧納入先情報
	 * @param newDelyHist 新納入先情報
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	protected void addDeliveryHist( List<BeanMap> compList, DeliveryHist oldDeliHist, DeliveryHist newDelyHist )
	throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException{
		Field[] fs = ( oldDeliHist != null ) ? oldDeliHist.getClass().getFields() : newDelyHist.getClass().getFields();
		for(int i = 4; i < fs.length-1; i++) {
			Field f2 = newDelyHist.getClass().getField(fs[i].getName());
			String columnName = fs[i].getName();

			// 比較処理
			Object o1 = ( oldDeliHist == null ) ? null : fs[i].get(oldDeliHist);
			Object o2 = f2.get(newDelyHist);

			if(o1 == null || o2 == null) {
				if(o1 == null && o2 == null) {
					// nullで一致
				}
				else {
					// 不一致
					if( columnName.indexOf("Cdx")!=-1 ){
						i++;
						columnName = fs[i].getName();
						o1 = ( oldDeliHist == null ) ? null : fs[i].get(oldDeliHist);
						f2 = newDelyHist.getClass().getField(fs[i].getName());
						o2 = f2.get(newDelyHist);
					}
					if(o1 == null && (o2 == null || "".equals(o2)) ) {
						continue;
					}
					addCompList(compList, 1000+i, newDelyHist.updDatetm,newDelyHist.deliverDeliveryName,
							MessageResourcesUtil.getMessage("labels.report.mst.customermst." + columnName),
							o1,o2);
				}
				continue;
			}

			if(o1.equals(o2)) {
				// 一致
			}
			else {
				// 不一致
				// コードで不一致なら、名称を出力する
				if( columnName.indexOf("Cdx")!=-1 ){
					i++;
					columnName = fs[i].getName();
					o1 = fs[i].get(oldDeliHist);
					f2 = newDelyHist.getClass().getField(fs[i].getName());
					o2 = f2.get(newDelyHist);
				}
				addCompList(compList, 1000+i,  newDelyHist.updDatetm,newDelyHist.deliverDeliveryName,
						MessageResourcesUtil.getMessage("labels.report.mst.customermst." + columnName),
						o1,o2 );
			}
			// コードなら、名称を飛ばす
			if( columnName.indexOf("Cdx")!=-1 ){
				i++;
			}
		}
	}

	/**
	 * 請求先の履歴情報を比較し、差分をリストに追加します.
	 * @param compList 履歴出力用のリスト
	 * @param oldBillHist 旧請求先情報
	 * @param newBillHist 新請求先情報
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	protected void addBillHist( List<BeanMap> compList, DeliveryBillHist oldBillHist, DeliveryBillHist newBillHist )
		throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException{
		Field[] fs = ( oldBillHist != null ) ? oldBillHist.getClass().getFields() : newBillHist.getClass().getFields();
		for(int i = 4; i < fs.length-1; i++) {
			Field f2 = newBillHist.getClass().getField(fs[i].getName());
			String columnName = fs[i].getName();

			// 比較処理
			Object o1 = ( oldBillHist == null ) ? null : fs[i].get(oldBillHist);
			Object o2 = f2.get(newBillHist);

			if(o1 == null || o2 == null) {
				if(o1 == null && o2 == null) {
					// nullで一致
				}
				else {
					// 不一致
					if( columnName.indexOf("Cdx")!=-1 ){
						i++;
						columnName = fs[i].getName();
						o1 = ( oldBillHist == null ) ? null : fs[i].get(oldBillHist);
						f2 = newBillHist.getClass().getField(fs[i].getName());
						o2 = f2.get(newBillHist);
					}
					if(o1 == null && (o2 == null || "".equals(o2)) ) {
						continue;
					}
					addCompList(compList, 3000+i, newBillHist.updDatetm, CustomerHistoryService.KIND_BILL,
							MessageResourcesUtil.getMessage("labels.report.mst.customermst." + columnName),
							o1,o2);
				}
				continue;
			}

			if(o1.equals(o2)) {
				// 一致
			}
			else {
				// 不一致
				// コードで不一致なら、名称を出力する
				if( columnName.indexOf("Cdx")!=-1 ){
					i++;
					columnName = fs[i].getName();
					o1 = fs[i].get(oldBillHist);
					f2 = newBillHist.getClass().getField(fs[i].getName());
					o2 = f2.get(newBillHist);
				}
				addCompList(compList, 3000+i, newBillHist.updDatetm, CustomerHistoryService.KIND_BILL,
						MessageResourcesUtil.getMessage("labels.report.mst.customermst." + columnName),
						o1,o2 );
			}
			// コードなら、名称を飛ばす
			if( columnName.indexOf("Cdx")!=-1 ){
				i++;
			}
		}
	}

	/**
	 * 出力条件を指定して、結果リストを返します.
	 * @param sql SQLファイル名
	 * @param conditions 検索条件のマップ
	 * @return 検索結果のリスト
	 * @throws ServiceException
	 */
	public List<BeanMap> findRecordByCondition(String sql, Map<String, Object> conditions)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			setEmptyCondition(param);
			setConditionParam(conditions, param);

			return this.selectBySqlFile(BeanMap.class, sql, param).getResultList();

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 空の検索条件マップを作成します.
	 * @param param 検索条件のマップ
	 * @return 検索条件キーのみ設定した検索条件マップ
	 */
	private Map<String, Object> setEmptyCondition(Map<String, Object> param) {
		for (int i=0;i<CustomerHistoryService.paramArray.length;i++) {
			param.put(CustomerHistoryService.paramArray[i], null);
		}
		return param;
	}

	/**
	 * 検索条件を設定します.
	 * @param conditions 検索条件値のマップ
	 * @param param 検索条件のマップ
	 * @return 検索条件値を設定した検索条件マップ
	 */
	private Map<String, Object> setConditionParam(
			Map<String, Object> conditions, Map<String, Object> param) {
		for (int i=0;i<CustomerHistoryService.paramArray.length;i++) {
			String key = CustomerHistoryService.paramArray[i];
			if (conditions.containsKey(key)) {
				if (StringUtil.hasLength((String)conditions.get(key))) {
					param.put(key,(String)conditions.get(key));
				}
			}
		}

		return param;
	}

	/**
	 * 履歴出力用リストに追加します.
	 * @param compList 履歴出力用のリスト
	 * @param keyValue キー値
	 * @param updDatetm 更新日時
	 * @param kind 種別
	 * @param colName カラム名
	 * @param before 変更前オブジェクト
	 * @param after 変更後オブジェクト
	 */
	private void addCompList(List<BeanMap>compList, int keyValue,Timestamp updDatetm, String kind,
								String colName, Object before , Object after){
		String beforeStr = "";
		String afterStr = "";

		if( before != null){
			beforeStr = before.toString();
		}

		if( after != null){
			afterStr = after.toString();
		}


		BeanMap compData = new BeanMap();
		compData.put("sortKey", Integer.valueOf(keyValue));
		compData.put("updDatetm",updDatetm);
		compData.put("kind",kind);
		compData.put("colName",colName);
		compData.put("before", beforeStr);
		compData.put("after", afterStr);

		compList.add( compData );
	}
}

/**
 * 履歴出力用のコンパレータです.
 * @author Ark Information Systems
 *
 */
class MyComparator implements Comparator<BeanMap>, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 比較を行います.
	 * @param o1 比較対象1
	 * @param o2 比較対象2
	 * @return 比較結果
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(BeanMap o1,BeanMap  o2){
    	Timestamp t1 = (Timestamp)o1.get("updDatetm");
    	Timestamp t2 = (Timestamp)o2.get("updDatetm");
    	if( !t1.equals(t2)){
    		return t1.before(t2) ? -1 : 1;
    	}

    	int key1 = ((Integer) o1.get("sortKey")).intValue();
    	int key2 = ((Integer) o2.get("sortKey")).intValue();

	    return key1 < key2 ? -1 : 1;
    }
}