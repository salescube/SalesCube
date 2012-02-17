/*
 *  Copyright 2009-2010 Ark Information Systems.
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
		
		public String productCode;
		
		public String  pOrderQuantity;
	}

	
	public String poSlipId;

	
	public String xlsFileName;
	public String pdfFileName;

	
	public Integer lineCount;

	
	public List<OutputRecommendOrderResultSlipLine> lines;

}
