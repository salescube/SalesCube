/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.master;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.Constants.CODE_SIZE;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

public class EditWarehouseForm extends AbstractEditForm {

	/** 倉庫コード */
	@Required(arg0=@Arg(key = "labels.master.warehouseCode", resource = true))
	@Mask(mask = Constants.CODE_MASK.HANKAKU_MASK)
	public String warehouseCode;

	/** 倉庫名 */
	@Required(arg0=@Arg(key = "labels.master.warehouseName", resource = true))
	public String warehouseName;

	/** 倉庫郵便番号 */
	public String warehouseZipCode;

	/** 倉庫住所１ */
	public String warehouseAddress1;

	/** 倉庫住所２ */
	public String warehouseAddress2;

	/** 倉庫電話番号 */
	public String warehouseTel;

	/** 倉庫FAX */
    public String warehouseFax;

	/** 管理者名 */
	public String managerName;

	/** 管理者カナ */
	public String managerKana;

	/** 管理者電話番号 */
	public String managerTel;

	/** 管理者FAX */
	public String managerFax;

	/** 管理者E-MAIL */
	public String managerEmail;

	/** 倉庫状態 */
    @Required(arg0=@Arg(key = "labels.master.warehouseState", resource = true))
	public String warehouseState;

    /** 変更前の棚番情報 */
    public List<DeleteRackForm> rackCodesHist = new ArrayList<DeleteRackForm>();

    /** 棚番情報 */
    public List<EditRackForm> editRackList = new ArrayList<EditRackForm>();

	/** 表示時点の行数 */
	public int defaultRowCount;

	@Override
	public void initialize() {
	}

	/**
	 * 登録・編集時のバリデートを行います.
	 * @return 表示するメッセージ
	 */
	public ActionMessages validate() {
		String labelWarehouseCode = MessageResourcesUtil
				.getMessage("labels.master.warehouseCode");
		String labelWarehouseName = MessageResourcesUtil
				.getMessage("labels.master.warehouseName");
		String labelZipCode = MessageResourcesUtil.getMessage("labels.zipCode");
		String labelAddress1 = MessageResourcesUtil
				.getMessage("labels.address1");
		String labelAddress2 = MessageResourcesUtil
				.getMessage("labels.address2");
		String labelWarehouseTel = MessageResourcesUtil
				.getMessage("labels.master.warehouseTel");
		String labelWarehouseFax = MessageResourcesUtil
				.getMessage("labels.master.warehouseFax");
		String labelManagerName = MessageResourcesUtil
				.getMessage("labels.master.managerName");
		String labelManagerKana = MessageResourcesUtil
				.getMessage("labels.master.managerKana");
		String labelManagerTel = MessageResourcesUtil
				.getMessage("labels.master.managerTel");
		String labelManagerFax = MessageResourcesUtil
				.getMessage("labels.master.managerFax");
		String labelManagerEmail = MessageResourcesUtil
				.getMessage("labels.master.managerEmail");
		String labelWarehouseState = MessageResourcesUtil
				.getMessage("labels.master.warehouseState");

		String labelRackCode = MessageResourcesUtil
				.getMessage("labels.master.rackCode");
		String labelRackName = MessageResourcesUtil
				.getMessage("labels.master.rackName");

		ActionMessages errors = new ActionMessages();

		// 必須チェックは@Requiredで済んでいる

		// 長さチェック
		// 倉庫コード　10文字
		checkMaxLength(warehouseCode, CODE_SIZE.WAREHOUSE, labelWarehouseCode, errors);
		// 倉庫名　60文字
		checkMaxLength(warehouseName, 60, labelWarehouseName, errors);
		// 　8文字
		checkMaxLength(warehouseZipCode, 8, labelZipCode, errors);
		// 住所１　50文字
		checkMaxLength(warehouseAddress1, 50, labelAddress1, errors);
		// 住所２　50文字
		checkMaxLength(warehouseAddress2, 50, labelAddress2, errors);
		// TEL　15文字
		checkMaxLength(warehouseTel, 15, labelWarehouseTel, errors);
		// FAX　15文字
		checkMaxLength(warehouseFax, 15, labelWarehouseFax, errors);
		// 管理者名　60文字
		checkMaxLength(managerName, 60, labelManagerName, errors);
		// 管理者カナ　60文字
		checkMaxLength(managerKana, 60, labelManagerKana, errors);
		// Tel　15文字
		checkMaxLength(managerTel, 15, labelManagerTel, errors);
		// FAX　15文字
		checkMaxLength(managerFax, 15, labelManagerFax, errors);
		// E-MAIL　60文字
		checkMaxLength(managerEmail, 60, labelManagerEmail, errors);
		// 状態　10文字
		checkMaxLength(warehouseState, 10, labelWarehouseState, errors);

		/** 倉庫から削除しないrackCodeリスト */
		Set<String> rackCodes = new HashSet<String>();

		//必須・型チェック
		int index = 0;
		for (EditRackForm editRackForm : editRackList) {
			index++;
			// 棚番コード
			checkRequired(index, editRackForm.rackCode, labelRackCode, errors);
			checkMaxLength(index, editRackForm.rackCode, 10, labelRackCode, errors);
			// 棚番名
			checkRequired(index, editRackForm.rackName, labelRackName, errors);
			checkMaxLength(index, editRackForm.rackName, 60, labelRackName, errors);

			if(editRackForm.multiFlag == null) {
				editRackForm.multiFlag = "0";
			}

			editRackForm.warehouseCode=warehouseCode;

			// 棚番名追加
			rackCodes.add(editRackForm.rackCode);
		}

		// 棚番コードの重複チェック
		for (int i = 0; i < editRackList.size(); i++) {
			for (int j = i+1; j < editRackList.size(); j++) {
				String srcCode = editRackList.get(i).rackCode;
				String tgtCode = editRackList.get(j).rackCode;
				if(srcCode.isEmpty()) {
					continue;
				}
				if(srcCode.equals(tgtCode)) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.line.rackCode.cross",  i+1 , j+1 ));
				}
			}
		}

		// 削除された棚番の抽出
		int count = rackCodesHist.size();
		for(int i = 0; i < count ; i++) {
			if(rackCodes.contains(rackCodesHist.get(i).rackCode)) {
				rackCodesHist.remove(i);
				count --;
				i --;
			}
		}
		return errors;
	}

}
