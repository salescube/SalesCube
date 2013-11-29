/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.co.arkinfosys.dto.setting.MenuDto;
import jp.co.arkinfosys.entity.join.MenuJoin;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.extension.jdbc.exception.OrderByNotFoundRuntimeException;
import org.seasar.framework.beans.util.Beans;

/**
 * メニューサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class MenuService extends AbstractService<MenuJoin> {

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String USER_ID = "userId";

		public static final String MENU_ID = "menuId";

		public static final String VALID_FLAG = "validFlag";
	}

	/**
	 * ユーザIDを指定して、すべてのメニュー情報のリストを返します.
	 * @param userId ユーザID
	 * @return メニュー情報{@link MenuJoin}のリスト
	 * @throws ServiceException
	 */
	public List<MenuJoin> findMenuByUserId(String userId)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(MenuService.Param.USER_ID, userId);

			return this.selectBySqlFile(MenuJoin.class,
					"menu/FindMenuByUserId.sql", param).getResultList();
		} catch (OrderByNotFoundRuntimeException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * ユーザIDを指定して、メニュー権限情報を返します.<br>
	 * ユーザが存在しない場合でも実行可能です.
	 * @param userId ユーザID
	 * @return メニュー権限情報{@link MenuJoin}のリスト
	 * @throws ServiceException
	 */
	public List<MenuJoin> findAllMenuValidFlagForUser(String userId)
			throws ServiceException {
		try {
			Map<String, Object> param = super.createSqlParam();
			param.put(MenuService.Param.USER_ID, userId);

			return this.selectBySqlFile(MenuJoin.class,
					"menu/FindAllMenuValidFlagForUser.sql", param).getResultList();
		} catch (OrderByNotFoundRuntimeException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * エンティティからDTOへの変換を行います.
	 * @param menuJoinList メニューエンティティのリスト
	 * @return メニューDTOのリスト
	 */
	public List<MenuDto> convertMenuJoinToDto(List<MenuJoin> menuJoinList) {
		if (menuJoinList == null) {
			return new ArrayList<MenuDto>();
		}

		// メニューIDとMenuDtoのマップを作成
		Map<String, MenuDto> tempMap = new HashMap<String, MenuDto>();
		if (menuJoinList == null || menuJoinList.size() == 0) {
			return new ArrayList<MenuDto>();
		}
		for (MenuJoin menuRoleJoin : menuJoinList) {
			MenuDto menuDto = Beans.createAndCopy(MenuDto.class, menuRoleJoin)
					.execute();
			tempMap.put(menuRoleJoin.menuId, menuDto);
		}

		// 子メニューを親メニューに従属させ、階層構造を作成する
		for (Entry<String, MenuDto> entry : tempMap.entrySet()) {
			MenuDto menuDto = entry.getValue();
			if (menuDto.parentId == null) {
				continue;
			}
			// 子メニューの場合
			MenuDto parentMenuDto = tempMap.get(menuDto.parentId);
			menuDto.parent = parentMenuDto;

			if (parentMenuDto.subMenuDtoList == null) {
				parentMenuDto.subMenuDtoList = new ArrayList<MenuDto>();
			}
			parentMenuDto.subMenuDtoList.add(menuDto);
		}

		// 親メニューのリストを作成する
		List<MenuDto> menuDtoList = new ArrayList<MenuDto>();
		for (Entry<String, MenuDto> entry : tempMap.entrySet()) {
			MenuDto menuDto = entry.getValue();
			if (menuDto.parentId != null) {
				// 子メニュー
				continue;
			}

			if (menuDto.subMenuDtoList == null) {
				// 子メニューを持たない親メニュー
				continue;
			}
			Collections.sort(menuDto.subMenuDtoList, new MenuComparator());
			menuDtoList.add(menuDto);
		}
		Collections.sort(menuDtoList, new MenuComparator());

		return menuDtoList;
	}

	/**
	 * メニュー用コンパレータです.
	 * @author Ark Information Systems
	 *
	 */
	private static class MenuComparator implements Comparator<MenuDto>, Serializable {

		private static final long serialVersionUID = 1L;

		/**
		 * 比較を行います.
		 * @param menu1 比較用メニューDTO1
		 * @param menu2 比較用メニューDTO2
		 * @return 比較結果
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(MenuDto menu1, MenuDto menu2) {
			return menu1.seq.compareTo(menu2.seq);
		}
	}
}
