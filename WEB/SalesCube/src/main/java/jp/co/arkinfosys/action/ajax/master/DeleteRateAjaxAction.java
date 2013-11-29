/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.master;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractDeleteAjaxAction;
import jp.co.arkinfosys.dto.master.RateDto;
import jp.co.arkinfosys.entity.join.RateJoin;
import jp.co.arkinfosys.form.ajax.master.DeleteRateAjaxForm;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.RateService;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;

/**
 * レートマスタ画面（検索）の削除実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DeleteRateAjaxAction extends
		AbstractDeleteAjaxAction<RateDto, RateJoin> {

	@ActionForm
	@Resource
	public DeleteRateAjaxForm deleteRateAjaxForm;

	@Resource
	public RateService rateService;

	/**
	 * 削除レコードを識別する情報を持ったレートマスタDTOを返します.
	 * @return {@link RateDto}
	 */
	@Override
	protected RateDto getIdentifiedDto() {
		return Beans.createAndCopy(RateDto.class, this.deleteRateAjaxForm)
				.execute();
	}

	/**
	 * 削除処理を行うレートマスタサービスを返します.
	 * @return {@link RateService}
	 */
	@Override
	protected AbstractMasterEditService<RateDto, RateJoin> getService() {
		return this.rateService;
	}
}
