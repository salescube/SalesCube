/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractDeleteAjaxAction;
import jp.co.arkinfosys.dto.master.SupplierDto;
import jp.co.arkinfosys.entity.join.SupplierJoin;
import jp.co.arkinfosys.form.ajax.master.DeleteSupplierAjaxForm;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.SupplierService;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;

/**
 * 仕入先マスタ画面（検索）の削除実行アクションクラスです.
 *
 * @author Ark Information Systems
 */
public class DeleteSupplierAjaxAction extends
		AbstractDeleteAjaxAction<SupplierDto, SupplierJoin> {

	@ActionForm
	@Resource
	public DeleteSupplierAjaxForm deleteSupplierAjaxForm;

	@Resource
	public SupplierService supplierService;

	/**
	 * 削除レコードを識別する情報を持った仕入先マスタDTOを返します.
	 * @return {@link SupplierDto}
	 */
	@Override
	protected SupplierDto getIdentifiedDto() {
		return Beans.createAndCopy(SupplierDto.class,
				this.deleteSupplierAjaxForm).execute();
	}

	/**
	 * 削除処理を行う仕入先マスタサービスを返します.
	 * @return {@link SupplierService}
	 */
	@Override
	protected AbstractMasterEditService<SupplierDto, SupplierJoin> getService() {
		return this.supplierService;
	}
}
