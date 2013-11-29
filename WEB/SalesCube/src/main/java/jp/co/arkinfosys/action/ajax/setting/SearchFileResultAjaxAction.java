/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.ajax.setting;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.ajax.AbstractSearchResultAjaxAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.setting.FileInfoDto;
import jp.co.arkinfosys.entity.join.FileInfoJoin;
import jp.co.arkinfosys.form.AbstractSearchForm;
import jp.co.arkinfosys.form.setting.SearchFileForm;
import jp.co.arkinfosys.service.FileInfoService;
import jp.co.arkinfosys.service.MasterSearch;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * ファイル登録画面の検索実行アクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class SearchFileResultAjaxAction extends
		AbstractSearchResultAjaxAction<FileInfoDto, FileInfoJoin> {

	@ActionForm
	@Resource
	public SearchFileForm searchFileForm;

	@Resource
	private FileInfoService fileInfoService;

	/**
	 * 検索を実行します.
	 * @param params 検索条件
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順でソートするか否か
	 * @param rowCount 取得件数(LIMIT)
	 * @param offset 取得開始位置(OFFSET)
	 * @return {@link FileInfoJoin}のリスト
	 * @throws ServiceException
	 *
	 */
	@Override
	protected List<FileInfoJoin> execSearch(BeanMap params, String sortColumn,
			boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException {
		return this.fileInfoService.findByCondition(params, sortColumn, sortOrderAsc);
	}
	/**
	 * ENTITYのリストをDTOのリストに変換します.
	 * @param entityList {@link FileInfoJoin}のリスト
	 * @return {@link FileInfoDto}のリスト
	 * @throws Exception
	 */
	@Override
	protected List<FileInfoDto> exchange(List<FileInfoJoin> entityList)
			throws Exception {
		List<FileInfoDto> dtoList = new ArrayList<FileInfoDto>();

		for (FileInfoJoin fileInfoJoin : entityList) {
			FileInfoDto dto = Beans.createAndCopy(FileInfoDto.class,
					fileInfoJoin)
					.timestampConverter(Constants.FORMAT.TIMESTAMP)
					.dateConverter(Constants.FORMAT.DATE).execute();

			dto.creDate = StringUtil.getDateString(Constants.FORMAT.DATE,
					fileInfoJoin.creDatetm);

			if (Constants.MENU_VALID_LEVEL.VALID_LIMITATION
					.equals(dto.openLevel)) {
				dto.openLevelName = MessageResourcesUtil
						.getMessage("labels.file.valid.limitation");
			} else if (Constants.MENU_VALID_LEVEL.VALID_FULL
					.equals(dto.openLevel)) {
				dto.openLevelName = MessageResourcesUtil
						.getMessage("labels.file.valid.full");
			}

			dtoList.add(dto);
		}
		return dtoList;
	}

	/**
	 * 検索画面のアクションフォームを返します.
	 * @return {@link SearchFileForm}
	 */
	@Override
	protected AbstractSearchForm<FileInfoDto> getActionForm() {
		return this.searchFileForm;
	}

	/**
	 * ファイル登録情報DTOを返します.
	 * @return {@link FileInfoDto}
	 */
	@Override
	protected Class<FileInfoDto> getDtoClass() {
		return FileInfoDto.class;
	}

	@Override
	protected String getResultURIString() {
		return Mapping.RESULT;
	}

	/**
	 * 検索画面のメニューIDを返します.
	 * @return ファイル登録画面のメニューID
	 */
	@Override
	protected String getSearchMenuID() {
		return Constants.MENU_ID.SETTING_FILE;
	}

	/**
	 * 検索処理を行うファイル操作サービスを返します.
	 * @return {@link FileInfoService}
	 */

	@Override
	protected MasterSearch<FileInfoJoin> getService() {
		return this.fileInfoService;
	}

}
