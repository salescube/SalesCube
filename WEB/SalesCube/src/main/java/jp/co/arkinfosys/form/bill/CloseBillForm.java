/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.form.bill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.bill.CloseBillLineDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.util.LabelValueBean;

/**
 * 請求締処理画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class CloseBillForm extends AbstractSearchForm<CloseBillLineDto> {

	
	public String cutOffDate;

	
	public String cutoffGroupCategory;

	
	public String cutoffGroup;

	
	public String paybackCycleCategory;

	
	public Boolean notYetRequestedCheck;

	
	public String customerCode;

	
	public String customerName;

	
	public boolean closeCheck;

	public String lastCutOffDate;

	public CloseBillLineDto otherUser = new CloseBillLineDto();

	
	public List<LabelValueBean> cutoffGroupCategoryList = new ArrayList<LabelValueBean>();

	
	public boolean initCategory;

	
	public int lineElementCount = 1;

	/**
	 * アクションフォームの初期化を行います.
	 */
	public void reset() {
		cutoffGroupCategory = "";
		cutoffGroup = "";
		paybackCycleCategory = "";
		customerCode = "";
		customerName = "";

		closeCheck = false;
		lastCutOffDate = "";

		SimpleDateFormat DF_YMD = new SimpleDateFormat(Constants.FORMAT.DATE);
		cutOffDate = DF_YMD.format(new Date());
	}
}
