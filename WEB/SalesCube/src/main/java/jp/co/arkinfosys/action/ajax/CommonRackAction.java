/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.util.HashMap;

import javax.annotation.Resource;

import jp.co.arkinfosys.entity.Rack;
import jp.co.arkinfosys.form.ajax.CommonRackForm;
import jp.co.arkinfosys.service.RackService;
import jp.co.arkinfosys.service.exception.ServiceException;
import net.arnx.jsonic.JSON;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

/**
 * 棚情報を取得するアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class CommonRackAction extends CommonAjaxResources {

	@ActionForm
	@Resource
	protected CommonRackForm commonRackForm;

	@Resource
	protected RackService rackService;

	/**
	 * 棚番コードから棚番情報を取得します.
	 * @return 棚番情報
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String getRackInfos() throws Exception {
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			Rack rack = rackService.findById(commonRackForm.rackCode);

			if (rack != null) {
				map.put(RackService.Param.WAREHOUSE_CODE, rack.warehouseCode==null?"":rack.warehouseCode);
				map.put(RackService.Param.WAREHOUSE_NAME, rack.warehouseName==null?"":rack.warehouseName);
				map.put(RackService.Param.RACK_CODE, rack.rackCode==null?"":rack.rackCode);
				map.put(RackService.Param.RACK_NAME, rack.rackName==null?"":rack.rackName);
				map.put(RackService.Param.RACK_CATEGORY, rack.rackCategory==null?"":rack.rackCategory);
				map.put(RackService.Param.ZIP_CODE, rack.zipCode==null?"":rack.zipCode);
				map.put(RackService.Param.ADDRESS_1, rack.address1==null?"":rack.address1);
				map.put(RackService.Param.ADDRESS_2, rack.address2==null?"":rack.address2);
				map.put(RackService.Param.RACK_PC_NAME, rack.rackPcName==null?"":rack.rackPcName);
				map.put(RackService.Param.RACK_TEL, rack.rackTel==null?"":rack.rackTel);
				map.put(RackService.Param.RACK_FAX, rack.rackFax==null?"":rack.rackFax);
				map.put(RackService.Param.RACK_EMAIL, rack.rackEmail==null?"":rack.rackEmail);
			} else {
				// 検索結果が空=Nullなら空文字列で返却する
				ResponseUtil.write("", "text/javascript");
				return null;
			}

			ResponseUtil.write(JSON.encode(map), "text/javascript");
			return null;
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		}
	}
}
