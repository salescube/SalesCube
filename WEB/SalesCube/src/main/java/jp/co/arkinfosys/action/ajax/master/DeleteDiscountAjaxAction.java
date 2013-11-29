/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractDeleteAjaxAction;
import jp.co.arkinfosys.dto.master.DiscountDto;
import jp.co.arkinfosys.dto.master.DiscountTrnDto;
import jp.co.arkinfosys.entity.join.DiscountJoin;
import jp.co.arkinfosys.form.ajax.master.DeleteDiscountAjaxForm;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.DiscountService;
import jp.co.arkinfosys.service.DiscountTrnService;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;

/**
 * 数量割引画面（検索）の削除実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DeleteDiscountAjaxAction extends
		AbstractDeleteAjaxAction<DiscountDto, DiscountJoin> {

	@ActionForm
	@Resource
	public DeleteDiscountAjaxForm deleteDiscountAjaxForm;

	@Resource
	public DiscountService discountService;

	@Resource
	public DiscountTrnService discountTrnService;

	/**
	 * 削除後に行う処理を実行します.<BR>
	 * 割引データの削除処理を行います.
	 */
	@Override
	protected void doAfterDelete() throws Exception {
		List<DiscountTrnDto> dtoList = this.discountTrnService.findDiscountTrnByDiscountId(this.deleteDiscountAjaxForm.discountId);
		for(DiscountTrnDto dto: dtoList) {
			this.discountTrnService.deleteDiscountTrnByDiscountDataId(dto.discountDataId.toString());
		}
	}

	/**
	 * 削除レコードを識別する情報を持った数量割引マスタDTOを返します.
	 * @return {@link DiscountDto}
	 */
	@Override
	protected DiscountDto getIdentifiedDto() {
		return Beans.createAndCopy(DiscountDto.class,
				this.deleteDiscountAjaxForm).execute();
	}

	/**
	 * 削除処理を行う数量割引マスタサービスを返します.
	 * @return {@link DiscountService}
	 */
	@Override
	protected AbstractMasterEditService<DiscountDto, DiscountJoin> getService() {
		return this.discountService;
	}
}
