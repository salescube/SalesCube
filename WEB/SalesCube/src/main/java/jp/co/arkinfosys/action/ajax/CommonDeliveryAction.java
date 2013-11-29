/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.entity.join.DeliveryAndPre;
import jp.co.arkinfosys.form.ajax.CommonDeliveryForm;
import jp.co.arkinfosys.service.DeliveryService;
import jp.co.arkinfosys.service.exception.ServiceException;
import net.arnx.jsonic.JSON;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

/**
 * 納入先リスト情報を取得するアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class CommonDeliveryAction extends CommonAjaxResources {

	@ActionForm
	@Resource
	protected CommonDeliveryForm commonDeliveryForm;

	@Resource
	protected DeliveryService deliveryService;

	/**
	 * 納入先コードから納入先情報を取得します.
	 * @return 納入先情報（１件）
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "getDeliveryInfosByDeliveryCode/{deliveryCode}")
	public String getDeliveryInfosByDeliveryCode() throws Exception {

		// 納入先コードを指定しない場合は検索しません
		if (!StringUtil.hasLength(commonDeliveryForm.deliveryCode)) {
			ResponseUtil.write("", "text/javascript");
			return null;
		}

		List<DeliveryAndPre> deliveryList;
		try {

			deliveryList = searchDeliveryAndPreByDeliveryCode(commonDeliveryForm.deliveryCode);

		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}

		// 納入先コードを指定した検索なので複数はかえらない
		if (deliveryList.size() == 1) {
			// エンティティの内容をマップに展開
			BeanMap map = super.createBeanMapWithNullToEmpty(deliveryList
					.get(0));
			ResponseUtil.write(JSON.encode(map), "text/javascript");
		} else {
			ResponseUtil.write("", "text/javascript");
		}
		return null;
	}

	/**
	 * 納入先コードから納入先情報を取得します.
	 * @param deliveryCode 納入先コード
	 * @return 納入先情報のリスト
	 * @throws ServiceException
	 */
	protected List<DeliveryAndPre> searchDeliveryAndPreByDeliveryCode(
			String deliveryCode) throws ServiceException {

		LinkedHashMap<String, Object> conditions = new LinkedHashMap<String, Object>();

		// 条件設定
		conditions.put(DeliveryService.Param.DELIVERY_CODE,
				commonDeliveryForm.deliveryCode);
		String sortColumn = DeliveryService.Param.DELIVERY_CODE;
		boolean sortOrderAsc = true;

		// 検索実行
		return deliveryService.findDeliveryAndPreByCompleteCode(conditions,
				sortColumn, sortOrderAsc);
	}

	/**
	 * 顧客コードから顧客と請求先情報を取得します（ 完全一致版）.<BR>
	 * 顧客コードが完全に一致しない場合は値が返りません.
	 * @return 納入先情報（１件）
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "getCustomerAndBillInfosByCustomerCode/{customerCode}")
	public String getCustomerAndBillInfosByCustomerCode() throws Exception {

		// 顧客コードを指定しない場合は検索しません
		if (!StringUtil.hasLength(commonDeliveryForm.customerCode)) {
			ResponseUtil.write("", "text/javascript");
			return null;
		}

		List<DeliveryAndPre> deliveryList;
		try {
			deliveryList = deliveryService
					.searchDeliveryByCompleteCustomerCode(commonDeliveryForm.customerCode);

		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}

		// 納入先コードを指定した検索なので複数はかえらない
		if (deliveryList.size() == 1) {

			BeanMap map = super.createBeanMapWithNullToEmpty(deliveryList
					.get(0));
			ResponseUtil.write(JSON.encode(map), "text/javascript");

		} else {
			ResponseUtil.write("", "text/javascript");
		}
		return null;
	}

	/**
	 * 顧客コードから納入先リストを返します.<BR>
	 * 顧客コードが完全に一致しない場合は値が返りません.<BR>
	 * 返す値は、納入先コードと納入先名で、請求先は除外しています.<BR>
	 * mapの内容は	 * key = "value" + No name = 納入先コード、納入先名の順番です.
	 * @return 納入先リスト情報
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String getDeliveryListByCustomerCode() throws Exception {

		// 顧客コードを指定しない場合は検索しません
		if (!StringUtil.hasLength(commonDeliveryForm.customerCode)) {
			ResponseUtil.write("", "text/javascript");
			return null;
		}

		List<DeliveryAndPre> deliveryList;
		try {
			deliveryList = deliveryService
					.searchDeliveryListByCompleteCustomerCode(commonDeliveryForm.customerCode);

			// 納入先コードと納入先名を返す
			int i = 0;
			String key;
			Map<String, Object> param = new HashMap<String, Object>();
			for (DeliveryAndPre dap : deliveryList) {
				key = "value" + Integer.toString(i);
				param.put(key, dap.deliveryCode);
				key = "name" + Integer.toString(i);
				param.put(key, dap.deliveryName);
				i++;
			}
			if (deliveryList.size() != 0) {
				BeanMap map = super.createBeanMapWithNullToEmpty(param);
				ResponseUtil.write(JSON.encode(map), "text/javascript");
			} else {
				ResponseUtil.write("", "text/javascript");
			}
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}

		return null;
	}

	/**
	 * 顧客コードから納入先リストを返します.<BR>
	 * 顧客コードが完全に一致しない場合は値が返りません.<BR>
	 * 返す値は、納入先コードと納入先名のリスト、かつ、作成日の昇順で、請求先は除外しています.<BR>
	 * mapの内容は key = "value" + No name = 納入先コード、納入先名の順番です.
	 * @return 納入先リスト情報
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String getDeliveryListByCustomerCodeSortedByCreDate()
			throws Exception {

		// 顧客コードを指定しない場合は検索しません
		if (!StringUtil.hasLength(commonDeliveryForm.customerCode)) {
			ResponseUtil.write("", "text/javascript");
			return null;
		}

		List<DeliveryAndPre> deliveryList;
		try {
			deliveryList = deliveryService
					.searchDeliveryByCompleteCustomerCodeSortedByCreDate(commonDeliveryForm.customerCode);

			// 納入先コードと納入先名を返す
			int i = 0;
			String key;
			Map<String, Object> param = new HashMap<String, Object>();
			for (DeliveryAndPre dap : deliveryList) {
				key = "value" + Integer.toString(i);
				param.put(key, dap.deliveryCode);
				key = "name" + Integer.toString(i);
				param.put(key, dap.deliveryName);
				i++;
			}
			if (deliveryList.size() != 0) {
				BeanMap map = super.createBeanMapWithNullToEmpty(param);
				ResponseUtil.write(JSON.encode(map), "text/javascript");
			} else {
				ResponseUtil.write("", "text/javascript");
			}
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}

		return null;
	}

}
