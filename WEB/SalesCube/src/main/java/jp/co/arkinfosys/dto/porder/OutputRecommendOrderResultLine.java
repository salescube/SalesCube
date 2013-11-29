/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.porder;

import java.io.Serializable;
import java.util.List;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
/**
 * 補充発注推奨リスト出力の発注結果画面の情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Component(instance = InstanceType.SESSION)
public class OutputRecommendOrderResultLine implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 商品情報を管理するDTOクラスです.
	 *
	 * @author Ark Information Systems
	 *
	 */
	@Component(instance = InstanceType.SESSION)
	public static class OutputRecommendOrderResultSlipLine implements Serializable {
		private static final long serialVersionUID = 1L;
		//商品コード
		public String productCode;
		//発注数量
		public String  pOrderQuantity;
	}

	//発注伝票番号
	public String poSlipId;

	//発注書ファイル名
	public String xlsFileName;
	public String pdfFileName;

	//伝票が持つ明細行数
	public Integer lineCount;

	//伝票内明細行
	public List<OutputRecommendOrderResultSlipLine> lines;

}
