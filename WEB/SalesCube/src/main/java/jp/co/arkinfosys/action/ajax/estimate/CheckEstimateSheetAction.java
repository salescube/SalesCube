/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.estimate;

import java.util.HashMap;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.CommonAjaxResources;
import jp.co.arkinfosys.action.estimate.OutputEstimateSheetSingleAction;
import jp.co.arkinfosys.dto.estimate.InputEstimateDto;
import jp.co.arkinfosys.service.EstimateSheetService;
import jp.co.arkinfosys.service.exception.ServiceException;
import net.arnx.jsonic.JSON;

import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;
/**
 *
 * 見積番号の存在を確認するアクションクラスです.
 *
 *  @author Ark Information Systems
 *
 */
public class CheckEstimateSheetAction extends CommonAjaxResources {

	@Resource
	protected EstimateSheetService estimateSheetService;

	public String estimateSheetId;

	/**
	 * 見積番号の存在を確認します.
	 *
	 * @return チェック結果(JSON文字列)
	 * @exception ServiceException
	 */
	@Execute(validator = false)
	public String exists() throws ServiceException {
		try {
			// 見積情報の存在チェック
			InputEstimateDto dto = (InputEstimateDto) estimateSheetService
					.loadBySlipId(this.estimateSheetId);
			if (dto == null) {
				return null;
			}

			HashMap<String, String> map = new HashMap<String, String>();
			map.put(OutputEstimateSheetSingleAction.Param.ESTIMATE_SHEETID,
					dto.estimateSheetId);

			ResponseUtil.write(JSON.encode(map), "text/javascript");
		} catch (ServiceException e) {
			super.writeSystemErrorToResponse();
		}

		return null;
	}
}
