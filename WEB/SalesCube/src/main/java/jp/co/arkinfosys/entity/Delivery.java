/*
 * Copyright 2009-2010 Ark Information Systems.
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
		// 納入先コード
		if( deliveryCode != null
			&& !deliveryCode.equals( src.deliveryCode ) ){
			return false;
		}
		// 納入先名
		if( deliveryName != null
			&& !deliveryName.equals( src.deliveryName ) ){
			return false;
		}
		// 納入先名カナ
		if( deliveryKana != null
			&& !deliveryKana.equals( src.deliveryKana ) ){
			return false;
		}
		// 事業所名
		if( deliveryOfficeName != null
			&& !deliveryOfficeName.equals( src.deliveryOfficeName ) ){
			return false;
		}
		// 事業所名カナ
		if( deliveryOfficeKana != null
			&& !deliveryOfficeKana.equals( src.deliveryOfficeKana ) ){
			return false;
		}
		// 部署名
		if( deliveryDeptName != null
			&& !deliveryDeptName.equals( src.deliveryDeptName ) ){
			return false;
		}
		// 郵便番号
		if( deliveryZipCode != null
			&& !deliveryZipCode.equals( src.deliveryZipCode ) ){
			return false;
		}
		// 住所１
		if( deliveryAddress1 != null
			&& !deliveryAddress1.equals( src.deliveryAddress1 ) ){
			return false;
		}
		// 住所２
		if( deliveryAddress2 != null
			&& !deliveryAddress2.equals( src.deliveryAddress2 ) ){
			return false;
		}
		// 担当者
		if( deliveryPcName != null
			&& !deliveryPcName.equals( src.deliveryPcName ) ){
			return false;
		}
		// 担当者カナ
		if( deliveryPcKana != null
			&& !deliveryPcKana.equals( src.deliveryPcKana ) ){
			return false;
		}
		// 敬称
		if( deliveryPcPreCategory != null
			&& !deliveryPcPreCategory.equals( src.deliveryPcPreCategory ) ){
			return false;
		}
		// TEL
		if( deliveryTel != null
			&& !deliveryTel.equals( src.deliveryTel ) ){
			return false;
		}
		// FAX
		if( deliveryFax != null
			&& !deliveryFax.equals( src.deliveryFax ) ){
			return false;
		}
		// E-MAIL
		if( deliveryEmail != null
			&& !deliveryEmail.equals( src.deliveryEmail ) ){
			return false;
		}
		return true;
	}

}
