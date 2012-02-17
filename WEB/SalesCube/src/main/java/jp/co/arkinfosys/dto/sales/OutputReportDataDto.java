/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.dto.sales;

/**
 * 出力する売上帳票を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class OutputReportDataDto {
	
	public String roSlipId;

	
	public String salesSlipId;

	
	public String reportFile;

	
	public String outputFileName = null;

	
	public Boolean dispDateFlag = Boolean.FALSE;


}
