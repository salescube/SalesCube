/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.form.ajax;


/**
 * 納入先情報と入金情報を保持するアクションフォームクラスです.
 * @author Ark Information Systems
 */
public class CommonDepositForm extends AbstractDeliveryForm  {

	
	public String depositSlipId;			

	
	public String cutoffGroup;

	
	public String paybackCycleCategory;

	
	public String taxShiftCategory;

	
	public String customerRoCategory;

	
	public String lastBillingPrice;			
	public String nowPaybackPrice;			
	public String nowSalesPrice;			

	public String taxFractCategory;		
	public String priceFractCategory;	

}
