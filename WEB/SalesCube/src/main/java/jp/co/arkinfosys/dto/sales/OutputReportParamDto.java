/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.sales;

import java.util.ArrayList;
import java.util.Arrays;
/**
* 売上帳票発行で出力する帳票種類情報を管理するDTOクラスです.
*
* @author Ark Information Systems
*
*/
public class OutputReportParamDto {
	// 受注番号
	public String roSlipId;

	// 売上番号
	public String salesSlipId;

	// 帳票名
	public String reportFileComma;

	// 帳票名リスト
	public ArrayList<String> reportFileList = null;

	// 日付表示フラグ
	public boolean dispDateFlag = false;

	/**
     * 帳票名(カンマ区切り)⇒帳票名リスト　に変換します.
     *
     */
	public void createFileCommaToList(){
		String[] str = reportFileComma.split(",");
		if(str.length <= 0){
			reportFileList = null;
		}
		reportFileList = new ArrayList<String>(Arrays.asList(str));
	}
}
