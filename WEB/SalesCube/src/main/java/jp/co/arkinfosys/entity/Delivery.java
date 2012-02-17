/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.entity;

import javax.persistence.Id;

/**
 * 納入先のエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class Delivery extends AuditInfo {

	public static final String TABLE_NAME="DELIVERY_MST";

	@Id
	public String deliveryCode;

	public String deliveryName;

	public String deliveryKana;

	public String deliveryOfficeName;

	public String deliveryOfficeKana;

	public String deliveryDeptName;

	public String deliveryZipCode;

	public String deliveryAddress1;

	public String deliveryAddress2;

	public String deliveryPcName;

	public String deliveryPcKana;

	public String deliveryPcPreCategory;

	public String deliveryTel;

	public String deliveryFax;

	public String deliveryEmail;

	public String deliveryUrl;

	public String remarks;

	/**
	 * エンティティを比較します.
	 * @param src　比較するエンティティ
	 * @return　true:一致する　false：一致しない
	 */
	public boolean equals( Delivery src ) {
		
		if( deliveryCode != null
			&& !deliveryCode.equals( src.deliveryCode ) ){
			return false;
		}
		
		if( deliveryName != null
			&& !deliveryName.equals( src.deliveryName ) ){
			return false;
		}
		
		if( deliveryKana != null
			&& !deliveryKana.equals( src.deliveryKana ) ){
			return false;
		}
		
		if( deliveryOfficeName != null
			&& !deliveryOfficeName.equals( src.deliveryOfficeName ) ){
			return false;
		}
		
		if( deliveryOfficeKana != null
			&& !deliveryOfficeKana.equals( src.deliveryOfficeKana ) ){
			return false;
		}
		
		if( deliveryDeptName != null
			&& !deliveryDeptName.equals( src.deliveryDeptName ) ){
			return false;
		}
		
		if( deliveryZipCode != null
			&& !deliveryZipCode.equals( src.deliveryZipCode ) ){
			return false;
		}
		
		if( deliveryAddress1 != null
			&& !deliveryAddress1.equals( src.deliveryAddress1 ) ){
			return false;
		}
		
		if( deliveryAddress2 != null
			&& !deliveryAddress2.equals( src.deliveryAddress2 ) ){
			return false;
		}
		
		if( deliveryPcName != null
			&& !deliveryPcName.equals( src.deliveryPcName ) ){
			return false;
		}
		
		if( deliveryPcKana != null
			&& !deliveryPcKana.equals( src.deliveryPcKana ) ){
			return false;
		}
		
		if( deliveryPcPreCategory != null
			&& !deliveryPcPreCategory.equals( src.deliveryPcPreCategory ) ){
			return false;
		}
		
		if( deliveryTel != null
			&& !deliveryTel.equals( src.deliveryTel ) ){
			return false;
		}
		
		if( deliveryFax != null
			&& !deliveryFax.equals( src.deliveryFax ) ){
			return false;
		}
		
		if( deliveryEmail != null
			&& !deliveryEmail.equals( src.deliveryEmail ) ){
			return false;
		}
		return true;
	}

}
