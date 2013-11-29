/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.sales;

/**
 * 出力する売上帳票を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class OutputReportDataDto {
	// 受注番号
	public String roSlipId;

	// 売上番号
	public String salesSlipId;

	// レポートファイル
	public String reportFile;

	// 出力ファイル名
	public String outputFileName = null;

	// 日付表示フラグ
	public Boolean dispDateFlag = Boolean.FALSE;


}
