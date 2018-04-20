/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.common;

import java.util.HashMap;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * appconfig.diconの設定値を取得するユーティリティクラスです.
 *
 * @author Ark Information Systems
 *
 */
public final class ConfigUtil {

	private static final String COMPONENT_NAME = "AppConfig";

	public static final class KEY {
		/**
		 * 検索系ダイアログの検索結果表示件数上限
		 */
		public static final String DIALOG_SEARCH_RESULT_THRESHOLD = "SearchResultThreshold";

		/**
		 * ファイルアップロードフォルダのパス
		 */
		public static final String FILE_UPLOAD_DIR_PATH = "FileUploadDirPath";

		/**
		 * 商品マスタCSV出力・取込カラムリスト
		 */
		public static final String PRODUCT_CSV_COLUMNS = "ProductCSVColumns";

		/**
		 * パスワード暗号化方式
		 */
		public static final String PASSWORD_ENCRYPT_STYLE = "PasswordEncryptStyle";

		/**
		 * デモ環境フラグ
		 */
		public static final String DEMO_FLAG = "demoFlag";
	}

	/**
	 * キーから設定値を取得します.
	 * @param key キー
	 * @return 設定値
	 */
	@SuppressWarnings("unchecked")
	public static Object getConfigValue(String key) {
		S2Container s2container = SingletonS2ContainerFactory.getContainer();
		if (s2container == null) {
			return null;
		}

		HashMap<String, Object> config = (HashMap<String, Object>) s2container
				.getComponent(ConfigUtil.COMPONENT_NAME);
		if (config == null) {
			return null;
		}

		return config.get(key);
	}

}
