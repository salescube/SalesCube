/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax;

import java.util.HashMap;

import javax.annotation.Resource;

import jp.co.arkinfosys.entity.Warehouse;
import jp.co.arkinfosys.form.ajax.CommonWarehouseForm;
import jp.co.arkinfosys.service.WarehouseService;
import jp.co.arkinfosys.service.exception.ServiceException;
import net.arnx.jsonic.JSON;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

/**
 * 倉庫情報を取得するアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class CommonWarehouseAction extends CommonAjaxResources {

	@ActionForm
	@Resource
	protected CommonWarehouseForm commonWarehouseForm;

	@Resource
	protected WarehouseService warehouseService;

	/**
	 * 倉庫コードから倉庫情報を取得します.
	 * @return 倉庫情報
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String getWarehouseInfos() throws Exception {
		try {
			HashMap<String, String> map = new HashMap<String, String>();
			Warehouse warehouse = warehouseService.findById(commonWarehouseForm.warehouseCode);

			if (warehouse != null) {
				map.put(WarehouseService.Param.WAREHOUSE_CODE, warehouse.warehouseCode==null?"":warehouse.warehouseCode);
				map.put(WarehouseService.Param.WAREHOUSE_NAME, warehouse.warehouseName==null?"":warehouse.warehouseName);
				map.put(WarehouseService.Param.WAREHOUSE_ZIP_CODE, warehouse.warehouseZipCode==null?"":warehouse.warehouseZipCode);
				map.put(WarehouseService.Param.WAREHOUSE_ADDRESS1, warehouse.warehouseAddress1==null?"":warehouse.warehouseAddress1);
				map.put(WarehouseService.Param.WAREHOUSE_ADDRESS2, warehouse.warehouseAddress2==null?"":warehouse.warehouseAddress2);
				map.put(WarehouseService.Param.WAREHOUSE_TEL, warehouse.warehouseTel==null?"":warehouse.warehouseTel);
				map.put(WarehouseService.Param.WAREHOUSE_FAX, warehouse.warehouseFax==null?"":warehouse.warehouseFax);
				map.put(WarehouseService.Param.MANAGER_NAME, warehouse.managerName==null?"":warehouse.managerName);
				map.put(WarehouseService.Param.MANAGER_KANA, warehouse.managerKana==null?"":warehouse.managerKana);
				map.put(WarehouseService.Param.MANAGER_TEL, warehouse.managerTel==null?"":warehouse.managerTel);
				map.put(WarehouseService.Param.MANAGER_FAX, warehouse.managerFax==null?"":warehouse.managerFax);
				map.put(WarehouseService.Param.MANAGER_EMAIL, warehouse.managerEmail==null?"":warehouse.managerEmail);
				map.put(WarehouseService.Param.WAREHOUSE_STATE, warehouse.warehouseState==null?"":warehouse.warehouseState);
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
