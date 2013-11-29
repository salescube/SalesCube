/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.bill;

import java.util.List;

import jp.co.arkinfosys.dto.bill.MakeOutBillSearchResultDto;

/**
 * 請求書発行画面の印刷用アクションフォームクラスです.
 *
 * @author Ark Information Systems
 */
public class MakeOutBillPrintForm {

	public String billId;

	public String bankId;

	public List<MakeOutBillSearchResultDto> rowDataPrint;

}
