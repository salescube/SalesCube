/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.dto.deposit;

import jp.co.arkinfosys.dto.AbstractLineDto;
import jp.co.arkinfosys.entity.DepositLine;
/**
 * 入金伝票明細行のDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DepositLineDto extends AbstractLineDto {
	public String depositLineId;		
	public String status;				
	public String depositSlipId;		
	public String depositCategory;		
	public String price;				
	public String instDate;				
	public String instNo;				
	public String bankId;				
	public String bankInfo;				
	public String remarks;				
	public String salesLineId;			
	public String creFunc;				
	public String creDatetm;			
	public String creUser;				
	public String updFunc;				
	public String updDatetm;			
	public String updUser;				

	/**
	 * 金額がnull又は空白かどうか調べます.
	 * @return　true：null又は空 false:nullでも空白でもない
	 */
	@Override
	public boolean isBlank() {
		return price == null || price.length() == 0;
	}

	/**
	 * 明細行をクリアします(伝票複写用）.
	 */
	public void initForCopy() {
		depositLineId = "";					
		status = DepositLine.STATUS_INIT;	
		depositSlipId = "";					
	}

	/**
	 * 初期化します.
	 */
	public void initialize(){
		depositLineId = "";					
		status = DepositLine.STATUS_INIT;	
		depositSlipId = "";					
		lineNo = "";					
		price = "";							
		bankId = "";						
		bankInfo = "";						
		remarks = "";						
	}
}
