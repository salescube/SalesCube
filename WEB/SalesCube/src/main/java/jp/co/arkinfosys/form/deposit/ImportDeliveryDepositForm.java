/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.deposit;

import jp.co.arkinfosys.dto.deposit.ImportDeliveryDepositResultDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Required;

/**
 * 配送業者入金データ取込画面のアクションフォームクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ImportDeliveryDepositForm extends
		AbstractSearchForm<ImportDeliveryDepositResultDto> {

	@Required(arg0 = @Arg(key = "labels.delivery.deposit.csv", resource = true))
	@Binding(bindingType = BindingType.NONE)
	public FormFile infoBoxFile;

	public String bankId;

	/**
	 * 送り状データファイル(アップロードファイルオブジェクト)
	 */
	@Required(arg0 = @Arg(key = "labels.delivery.invoice.data", resource = true))
	@Binding(bindingType = BindingType.NONE)
	public FormFile invoiceFile;

	public String newDepositSlipIdStr;

	// 権限
	public boolean linkInputSales;
	public boolean linkInputDeposit;

	public int importOKCount; // 登録済件数
	public int importNGCount; // エラー件数
	public int importEtcCount; // その他件数
	public int dispResultCount;
	/**
	* 検索結果選択値
	*/
	public int selectCount = 0; // 0:全件　1: 登録済　2: エラー　3:送り状のみ

}
