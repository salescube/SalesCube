/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.master.CustomerDto;
import jp.co.arkinfosys.entity.Customer;
import jp.co.arkinfosys.entity.join.CustomerJoin;
import jp.co.arkinfosys.form.ajax.CommonCustomerForm;
import jp.co.arkinfosys.service.CustomerService;
import jp.co.arkinfosys.service.exception.ServiceException;
import net.arnx.jsonic.JSON;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

/**
 * 顧客情報を取得するアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class CommonCustomerAction extends CommonAjaxResources {

	@ActionForm
	@Resource
	protected CommonCustomerForm commonCustomerForm;

	@Resource
	protected CustomerService customerService;

	/**
	 * 顧客コードから顧客情報を取得します（完全一致版）. <BR>
	 * 顧客コードが完全に一致しない場合は値が返りません.
	 * @return 顧客情報
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String getCustomerInfoByCustomerCode() throws Exception {

		// 顧客コードを指定しない場合は検索しません
		if (!StringUtil.hasLength(commonCustomerForm.customerCode)) {
			ResponseUtil.write("", "text/javascript");
			return null;
		}
		Customer customer = null;
		try {
			customer = this.customerService
					.findCustomerByCode(this.commonCustomerForm.customerCode);
		} catch (ServiceException e) {
			super.errorLog(e);
			super.writeSystemErrorToResponse();
			return null;
		}

		if (customer != null) {
			BeanMap map = super.createBeanMapWithNullToEmpty(customer);
			ResponseUtil.write(JSON.encode(map), "text/javascript");
		} else {
			ResponseUtil.write("", "text/javascript");
		}
		return null;
	}

	/**
	 * 顧客コードから顧客情報を取得します（完全一致版）. <BR>
	 * 顧客ランクや区分データ情報も取得します. <BR>
	 * 顧客コードが完全に一致しない場合は値が返りません.
	 * @return 顧客情報
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String searchByCustomerCode() throws Exception {
		try {
			// 検索を行う
			CustomerJoin customerJoin = customerService
					.findById(this.commonCustomerForm.customerCode);

			if (customerJoin != null) {

				CustomerDto customerDto = Beans.createAndCopy(
						CustomerDto.class, customerJoin).timestampConverter(
						"yyyy/MM/dd HH:mm:ss").dateConverter("yyyy/MM/dd")
						.execute();
				BeanMap map = super.createBeanMapWithNullToEmpty(customerDto);
				ResponseUtil.write(JSON.encode(map), "text/javascript");
			} else {
				ResponseUtil.write("", "text/javascript");
			}
		} catch (ServiceException e) {
			super.errorLog(e);
			super.writeSystemErrorToResponse();
		}

		return null;
	}

}
