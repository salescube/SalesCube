package jp.co.arkinfosys.action.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractSearchAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.WarehouseDto;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.master.SearchWarehouseForm;
import jp.co.arkinfosys.service.WarehouseService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.struts.annotation.ActionForm;

public class SearchWarehouseAction extends AbstractSearchAction<WarehouseDto> {

	@ActionForm
	@Resource
	public SearchWarehouseForm searchWarehouseForm;

	@Resource
	public WarehouseService warehouseService;

	/**
	 * ソート条件を設定します.
	 * 
	 * @see jp.co.arkinfosys.action.AbstractSearchAction#doAfterIndex()
	 */
	@Override
	protected void doAfterIndex() {
		this.searchWarehouseForm.sortColumn = WarehouseService.Param.WAREHOUSE_CODE;
		this.searchWarehouseForm.sortOrderAsc = true;
	}
	
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.MASTER_WAREHOUSE;
	}

	@Override
	protected void createList() throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected AbstractSearchForm<WarehouseDto> getActionForm() {
		return this.searchWarehouseForm;
	}

}
