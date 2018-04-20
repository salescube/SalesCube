/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.CODE_SIZE;

import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 棚番マスタ管理（登録・編集）のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class EditRackForm extends AbstractEditForm {

	/** 倉庫コード */
	public String warehouseCode;

	/** 倉庫名 */
	public String warehouseName;

	/** 倉庫状態 */
	public String warehouseState;

	/** 棚番コード */
	@Required(arg0 = @Arg(key = "labels.master.rackCode", resource = true))
	@Mask(mask = Constants.CODE_MASK.HANKAKU_MASK)
	public String rackCode;

	/** 棚番名 */
	@Required(arg0 = @Arg(key = "labels.master.rackName", resource = true))
	public String rackName;

	/** 重複可能フラグ */
	public String multiFlag;

	/** 郵便番号 */
	public String zipCode;

	/** 住所１ */
	public String address1;

	/** 住所２ */
	public String address2;

	/** 棚番担当者名 */
	public String rackPcName;

	/** TEL */
	public String rackTel;

	/** FAX */
	public String rackFax;

	/** E-MAIL */
	public String rackEmail;

	/** 棚番の存在 倉庫空の棚番作成で利用 */
	public boolean exist;

	public void initialize() {
		warehouseCode = "";
		warehouseName = "";
		rackCode = "";
		rackName = "";
		multiFlag = "0";
		zipCode = "";
		address1 = "";
		address2 = "";
		rackPcName = "";
		rackTel = "";
		rackFax = "";
		rackEmail = "";
	}

	/**
	 * 初期値を設定します.
	 */
	public void reset() {
		multiFlag = "0";
	}

	/**
	 * 登録・編集時のバリデートを行います.
	 *
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		String labelRackCode = MessageResourcesUtil
				.getMessage("labels.master.rackCode");
		String labelRackName = MessageResourcesUtil
				.getMessage("labels.master.rackName");
		String labelWarehouseCode = MessageResourcesUtil
				.getMessage("labels.master.warehouseCode");
		String labelWarehouseName = MessageResourcesUtil
				.getMessage("labels.master.warehouseName");
		String labelZipCode = MessageResourcesUtil.getMessage("labels.zipCode");
		String labelAddress1 = MessageResourcesUtil
				.getMessage("labels.address1");
		String labelAddress2 = MessageResourcesUtil
				.getMessage("labels.address2");
		String labelTel = MessageResourcesUtil.getMessage("labels.tel");
		String labelFax = MessageResourcesUtil.getMessage("labels.fax");
		String labelEmail = MessageResourcesUtil.getMessage("labels.email");
		String labelPcName = MessageResourcesUtil.getMessage("labels.pcName");

		ActionMessages errors = new ActionMessages();

		// 必須チェックは@Requiredで済んでいる

		// 長さチェック
		// 棚番コード　7文字
		checkMaxLength(rackCode, CODE_SIZE.RACK, labelRackCode, errors);
		// 棚番名　60文字
		checkMaxLength(rackName, 60, labelRackName, errors);
		// 倉庫コード　10文字
		checkMaxLength(warehouseCode, CODE_SIZE.WAREHOUSE, labelWarehouseCode, errors);
		// 倉庫名　60文字
		checkMaxLength(warehouseName, 60, labelWarehouseName, errors);
		// 住所１　50文字
		checkMaxLength(address1, 50, labelAddress1, errors);
		// 住所２　50文字
		checkMaxLength(address2, 50, labelAddress2, errors);
		// TEL　15文字
		checkMaxLength(rackTel, 15, labelTel, errors);
		// FAX　15文字
		checkMaxLength(rackFax, 15, labelFax, errors);
		// E-MAIL　255文字
		checkMaxLength(rackEmail, 255, labelEmail, errors);
		// 担当者名　60文字
		checkMaxLength(rackPcName, 60, labelPcName, errors);

		// 郵便番号チェック
		checkMaxLength(zipCode, 8, labelZipCode, errors);

		return errors;
	}
}
