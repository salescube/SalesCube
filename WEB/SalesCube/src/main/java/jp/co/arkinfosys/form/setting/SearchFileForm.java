/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.setting;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;
import org.seasar.struts.annotation.Required;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.setting.FileInfoDto;
import jp.co.arkinfosys.form.AbstractSearchForm;

/**
 * ファイル登録（検索）画面のアクションフォームクラスです.
 * @author Ark Information Systems
 */
public class SearchFileForm extends AbstractSearchForm<FileInfoDto> {

	/**ファイルID*/
	public String fileId;

	/** タイトル */
	public String title;

	/** ファイル名 */
	public String fileName;

	/**
	 * 公開レベル
	 */
	public String openLevel;

	/**
	 * 公開範囲リスト
	 */
	public List<LabelValueBean> openLevelList = new ArrayList<LabelValueBean>();

}
