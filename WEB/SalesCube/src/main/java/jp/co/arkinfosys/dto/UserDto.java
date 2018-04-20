/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.dto.master.MasterEditDto;
import jp.co.arkinfosys.dto.setting.MenuDto;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
/**
 * 社員情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Component(instance = InstanceType.SESSION)
public class UserDto implements Serializable, MasterEditDto {

	private static final long serialVersionUID = 1L;

	public String userId;

	public String nameKnj;

	public String nameKana;

	public String deptId;

	public String deptName;

	public String email;

	public String password;

	public String expireDate;

	public String lockflg;

	public String lockDatetm;

	public Integer failCount;

	public String creFunc;

	public String creDatetm;

	public String creUser;

	public String updFunc;

	public String updDatetm;

	public String updUser;

	public String tolenIv;

	public String token;

	public String tokenKey;

	public String detoken;

	public String tokeEexpireDate;

	public String lastRequestFunc;

	public List<RoleDto> roleDtoList;

	public List<MenuDto> menuDtoList;

	public List<MenuDto> originalMenuDtoList;


	/**
	 * ファイル参照権限
	 */
	public String fileOpenLevel;

	/**
	 * 商品ダウンロード/アップロード権限
	 */
	public boolean validUpDwnProducts = false;

	/**
	 * パスワードが有効期限切れであるかどうかを返します.
	 *
	 * @return true:有効期限切れ false:有効期限中
	 */
	public boolean isPasswordExpired() throws ParseException {
		if (this.expireDate == null) {
			// NULLの場合は期限管理しない
			return false;
		}
		DateFormat df = new SimpleDateFormat(Constants.FORMAT.DATE);
		df.setLenient(true);
		Date dt = df.parse(expireDate);

		Calendar expire = Calendar.getInstance();
		expire.setTime(dt);

		// 本日0時との比較を行う
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		return expire.before(today);
	}


	/**
	 * メニューIDを指定して、ユーザーが利用可能なメニューのリストに含まれているかどうかを返します.
	 *
	 * @param menuId　メニューID
	 * @return true:利用可能なメニュー  false: 利用可能でないメニュー
	 */
	public boolean isMenuValid(String menuId) {
		if (menuId == null) {
			return false;
		}

		// 親メニューから検索
		for (MenuDto menuDto : this.menuDtoList) {
			if (menuId.equals(menuDto.menuId)) {
				return true;
			}
			if (menuDto.subMenuDtoList == null) {
				return false;
			}

			// サブメニューを検索
			for (MenuDto subMenuDto : menuDto.subMenuDtoList) {
				if (menuId.equals(subMenuDto.menuId)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * メニューIDを指定して、更新可能かを返します.
	 *
	 * @param menuId　メニューID
	 * @return　true:更新可能　false:更新不可
	 */
	public boolean isMenuUpdate(String menuId) {
		if (menuId == null) {
			return false;
		}

		// 親メニューから検索
		for (MenuDto menuDto : this.menuDtoList) {
			if (menuDto.subMenuDtoList == null) {
				return false;
			}

			// サブメニューを検索
			for (MenuDto subMenuDto : menuDto.subMenuDtoList) {
				if (menuId.equals(subMenuDto.menuId)) {
					if (Constants.MENU_VALID_LEVEL.VALID_FULL
							.equals(subMenuDto.validFlag)) {
						// 2：有効
						return true;
					} else if (Constants.MENU_VALID_LEVEL.VALID_LIMITATION
							.equals(subMenuDto.validFlag)) {
						// 1：閲覧のみ
						return false;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 社員コードを返します.
	 * @return　社員コード
	 */
	@Override
	public String[] getKeys() {
		return new String[] { this.userId };
	}

	/**
	 * トークンが有効期限切れであるかどうかを返します.
	 *
	 * @return true:有効期限切れ false:有効期限中
	 */
	public boolean isTokenExpired() throws ParseException {

		if (this.tokeEexpireDate == null) {
			// NULLの場合は期限管理しない
			return false;
		}
		DateFormat df = new SimpleDateFormat(Constants.FORMAT.DATE);
		df.setLenient(true);
		Date dt = df.parse(tokeEexpireDate);

		Calendar expire = Calendar.getInstance();
		expire.setTime(dt);

		// 本日0時との比較を行う
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		return expire.before(today);
	}

}