/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.dto.deposit;

import jp.co.arkinfosys.dto.AbstractLineDto;
import jp.co.arkinfosys.dto.AbstractSlipDto;

/**
 * 入金伝票行のDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class DepositSlipDto extends AbstractSlipDto<DepositLineDto> {

	
	public String depositSlipId;	
	public String status;			
	public String depositDate;		
	public String inputPdate;			
	public String depositAnnual;		
	public String depositMonthly;	
	public String depositYm;		
	public String userId;			
	public String userName;			
	public String depositAbstract;	
	public String remarks;			
	public String customerCode;		
	public String customerName;		
	public String cutoffGroup;		
	public String paybackCycleCategory;		
	public String customerRemarks;	
	public String customerCommentData; 
	public String baCode;			
	public String baName;			
	public String baKana;			
	public String baOfficeName;		
	public String baOfficeKana;		
	public String baDeptName;		
	public String baZipCode;		
	public String baAddress1;		
	public String baAddress2;		
	public String baPcName;			
	public String baPcKana;			
	public String baPcPreCatrgory;	
	public String baPcPre;			
	public String baTel;			
	public String baFax;			
	public String baEmail;			
	public String baUrl;			
	public String salesCmCategory;	
	public String salesCmCategoryName;	
	public String depositCategory;	
	public String depositTotal;	
	public String billId;			
	public String billCutoffDate;		
	public String billCutoffPdate;	
	public String artId;			
	public String salesSlipId;		
	public String depositMethodTypeCategory;	
	public String creFunc;			
	public String creDatetm;		
	public String creUser;			
	public String updFunc;			
	public String updDatetm;		
	public String updUser;			
	public String salesCutoffDate;	
	public String salesCutoffPdate;	
	public String taxFractCategory;		
	public String priceFractCategory;	


	
	public String lastBillingPrice;			
	public String nowPaybackPrice;			
	public String nowSalesPrice;			
	public String billingBalancePrice;		


	
	public String copySlipName;		
	public String copySlipId;		

	public String inputBillId;			

	
	public String customerIsExist;

	/**
	 * 入金伝票明細行を１行作成します.
	 * @return 入金伝票明細行
	 */
	@Override
	public AbstractLineDto createLineDto() {
		DepositLineDto dto = new DepositLineDto();
		dto.initialize();
		return dto;
	}

	/**
	 * 入金伝票番号を取得します.
	 * @return 入金伝票番号
	 */
	@Override
	public String getKeyValue() {
		return this.depositSlipId;
	}
}
