/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.deposit;

import jp.co.arkinfosys.dto.deposit.ImportBankDepositResultDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Required;
/**
 * 銀行入金データ画面のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class ImportBankDepositForm extends AbstractSearchForm<ImportBankDepositResultDto> {
	/**
	 * 入金CSVファイル CSVファイル(アップロードファイルオブジェクト)
	 */
	@Required(arg0 = @Arg(key = "labels.bank.deposit.csv", resource = true))
	@Binding(bindingType = BindingType.NONE)
	public FormFile csvFile;

	public String bankId;

	public String  newDepositSlipIdStr ;

	// 権限
	public boolean linkInputDeposit;

	public int importOKCount;	// 一致件数
	public int importNGCount;	// 不一致件数
	public int dispResultCount;

	 /**
     * 検索結果選択値
     */
	public int selectCount = 0; // 0:全件　1: 一致　2: 不一致
}
