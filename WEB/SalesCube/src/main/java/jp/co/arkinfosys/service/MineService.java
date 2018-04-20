/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import jp.co.arkinfosys.dto.setting.MineDto;
import jp.co.arkinfosys.entity.Mine;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.beans.util.BeanMap;

/**
 * 自社マスタサービスクラスです.
 *
 * @author tochitani
 *
 */
public class MineService extends AbstractService<Mine> {

	/**
	 * パラメータマッピングクラスです.
	 *
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String COMPANY_NAME = "companyName";
		public static final String COMPANY_KANA = "companyKana";
		public static final String COMPANY_ABBR = "companyAbbr";
		public static final String COMPANY_CEO_NAME = "companyCeoName";
		public static final String COMPANY_CEO_TITLE = "companyCeoTitle";
		public static final String LOGO_IMG_PATH = "logoImgPath";
		public static final String COMPANY_ZIP_CODE = "companyZipCode";
		public static final String COMPANY_ADDRESS_1 = "companyAddress1";
		public static final String COMPANY_ADDRESS_2 = "companyAddress2";
		public static final String COMPANY_TEL = "companyTel";
		public static final String COMPANY_FAX = "companyFax";
		public static final String COMPANY_EMAIL = "companyEmail";
		public static final String COMPANY_WEB_SITE = "companyWebSite";
		public static final String CUTOFF_GROUP = "cutoffGroup";
		public static final String CLOSE_MONTH = "closeMonth";

		public static final String INI_POSTAGE_TYPE = "iniPostageType";
		public static final String TARGET_POSTAGE_CHARGES = "targetPostageCharges";
		public static final String POSTAGE = "postage";

		public static final String PASSWORD_VALID_DAYS = "passwordValidDays";
		public static final String TOTAL_FAIL_COUNT = "totalFailCount";
		public static final String PASSWORD_HIST_COUNT = "passwordHistCount";
		public static final String PASSWORD_LENGTH = "passwordLength";
		public static final String PASSWORD_CHAR_TYPE = "passwordCharType";

		public static final String STOCK_HOLD_TERM_CALC_CATEGORY = "stockHoldTermCalcCategory";
		public static final String STOCK_HOLD_DAYS = "stockHoldDays";
		public static final String MIN_PO_LOT_CALC_DAYS = "minPoLotCalcDays";
		public static final String MIN_PO_LOT_NUM = "minPoLotNum";
		public static final String MAX_PO_NUM_CALC_DAYS = "maxPoNumCalcDays";
		public static final String MIN_PO_NUM = "minPoNum";
		public static final String DEFICIENCY_RATE = "deficiencyRate";
		public static final String MAX_ENTRUST_PO_TIMELAG = "maxEntrustPoTimelag";
		public static final String SAFETY_COEFFICIENT = "safetyCoefficient";

		public static final String UPD_DATETM = "updDatetm";
	}

	/**
	 * アップロードファイルのプレフィックス
	 */
	private static final String FILE_PREFIX = "logo";

	/**
	 * デフォルトファイルアップロードパス
	 */
	private static final String DEFAULT_FILE_UPLOAD_PATH = "/images/logos";

	/**
	 * デフォルトロゴパス
	 */
	private static final String DEFAULT_LOGO_PATH = "/images/logo.png";

	@Resource
	private ServletContext application;

	/**
	 * 自社マスタ情報を返します.
	 *
	 * @return 自社マスタ情報
	 * @throws ServiceException
	 */
	public Mine getMine() throws ServiceException {
		try {
			return this.selectBySqlFile(Mine.class, "mine/GetMine.sql",
					super.createSqlParam()).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 自社マスタ情報を返します.
	 *
	 * @return 自社マスタ情報の{@link BeanMap}
	 * @throws ServiceException
	 */
	public BeanMap getMineSimple() throws ServiceException {
		try {
			return this.selectBySqlFile(BeanMap.class, "mine/GetMine.sql",
					super.createSqlParam()).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 自社マスタを更新します.
	 *
	 * @param Name
	 *            会社名
	 * @param Abbr
	 *            会社略称
	 * @param Kana
	 *            会社名カナ
	 * @param CeoName
	 *            代表取締役名
	 * @param CeoTitle
	 *            代表取締役肩書
	 * @param LogoImgPath
	 *            LOGOパス
	 * @param ZipCode
	 *            郵便番号
	 * @param Addr1
	 *            住所1
	 * @param Addr2
	 *            住所2
	 * @param Tel
	 *            電話番号
	 * @param Fax
	 *            Fax番号
	 * @param EMail
	 *            EMailアドレス
	 * @param WebSite
	 *            Webサイト
	 * @param CutffGroup
	 *            締日
	 * @param CloseMonth
	 *            決算月
	 * @param iniPostageType
	 *             送料区分
	 * @param targetPostageCharges
	 *             送料対象金額
	 * @param postage
	 *             送料
	 * @throws ServiceException
	 */
	public void updateMine(String Name, String Abbr, String Kana,
			String CeoName, String CeoTitle,FormFile LogoImgPath, boolean LogoInit, String ZipCode, String Addr1,
			String Addr2, String Tel, String Fax, String EMail, String WebSite,
			String CutffGroup, String CloseMonth, String iniPostageType, String targetPostageCharges, String postage) throws ServiceException {
		try {

			/** アップロードファイルパス */
			String uploadedFilePath = null;

			if(LogoInit){
				uploadedFilePath = DEFAULT_LOGO_PATH;
			} else if (LogoImgPath != null && 0 < LogoImgPath.getFileSize()) {
				// 内部管理用ファイルLOGO
				/** アップロードディレクトリ */
				File logoDir = new File(
						this.application
								.getRealPath(MineService.DEFAULT_FILE_UPLOAD_PATH));

				/** アップロードファイル */
				File uploadedFile = File.createTempFile(
						MineService.FILE_PREFIX, "", logoDir);

				// ドメインフォルダが存在しなかったら作成する
				if (!logoDir.exists()) {
					logoDir.mkdir();
				}
				// ファイルが存在しなかったら作成する
				if (!uploadedFile.exists()) {
					uploadedFile.createNewFile();
				}

				BufferedInputStream is = null;
				BufferedOutputStream os = null;

				try {
					// アップロードファイルをコピー
					is = new BufferedInputStream(LogoImgPath.getInputStream());
					os = new BufferedOutputStream(new FileOutputStream(
							uploadedFile));
					this.transferIO(is, os);
				} finally {
					is.close();
					os.close();
				}

				uploadedFilePath = MineService.DEFAULT_FILE_UPLOAD_PATH + "/"
						+ uploadedFile.getName();

			}

			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(MineService.Param.COMPANY_NAME, Name);
			param.put(MineService.Param.COMPANY_KANA, Kana);
			param.put(MineService.Param.COMPANY_ABBR, Abbr);
			param.put(MineService.Param.COMPANY_CEO_NAME, CeoName);
			param.put(MineService.Param.COMPANY_CEO_TITLE, CeoTitle);
			if (uploadedFilePath == null) {
				param.put(MineService.Param.LOGO_IMG_PATH, super.mineDto.logoImgPath);
			} else {
				param.put(MineService.Param.LOGO_IMG_PATH, uploadedFilePath);
			}
			param.put(MineService.Param.COMPANY_ZIP_CODE, ZipCode);
			param.put(MineService.Param.COMPANY_ADDRESS_1, Addr1);
			param.put(MineService.Param.COMPANY_ADDRESS_2, Addr2);
			param.put(MineService.Param.COMPANY_TEL, Tel);
			param.put(MineService.Param.COMPANY_FAX, Fax);
			param.put(MineService.Param.COMPANY_EMAIL, EMail);
			param.put(MineService.Param.COMPANY_WEB_SITE, WebSite);
			param.put(MineService.Param.CUTOFF_GROUP, CutffGroup);
			param.put(MineService.Param.CLOSE_MONTH, CloseMonth);
			param.put(MineService.Param.INI_POSTAGE_TYPE, iniPostageType);
			param.put(MineService.Param.TARGET_POSTAGE_CHARGES, targetPostageCharges);
			param.put(MineService.Param.POSTAGE, postage);


			this.updateBySqlFile("mine/UpdateMine.sql", param).execute();

			// ロゴを更新する
			if (uploadedFilePath != null) {
				// アップロードが無事完了したら、古いロゴを削除する。
				File deleteFile = new File(
						this.application.getRealPath(super.mineDto.logoImgPath));
				// デフォルトのロゴでなく、ファイルが存在する場合、そのファイルを削除する。
				if (!super.mineDto.logoImgPath.equals(DEFAULT_LOGO_PATH)
						&& deleteFile.exists()) {
					deleteFile.delete();
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public void updateMineSecurity(MineDto dto) throws ServiceException {
		try {

			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(MineService.Param.PASSWORD_VALID_DAYS, dto.passwordValidDays);
			param.put(MineService.Param.TOTAL_FAIL_COUNT, dto.totalFailCount);
			param.put(MineService.Param.PASSWORD_HIST_COUNT , dto.passwordHistCount);
			param.put(MineService.Param.PASSWORD_LENGTH  , dto.passwordLength);
			param.put(MineService.Param.PASSWORD_CHAR_TYPE , dto.passwordCharType);

			this.updateBySqlFile("mine/UpdateMineSecurity.sql", param).execute();

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	/**
	 * 在庫管理情報を更新します.
	 *
	 * @param stockHoldTermCalcCategory
	 *            月平均出荷数の計算期間
	 * @param stockHoldDays
	 *            最大保有数
	 * @param minPoLotCalcDays
	 *            発注ロット
	 * @param minPoLotNum
	 *            最小発注ロット
	 * @param maxPoNumcalcDays
	 *            単位発注限度数
	 * @param minPoNum
	 *            最小発注点
	 * @param deficiencyRate
	 *            欠品率
	 * @param maxEntrustPoTimelag
	 *            委託在庫発注の最大タイムラグ
	 * @throws ServiceException
	 */
	public void updateMineByStock(String stockHoldTermCalcCategory,
			int stockHoldDays, int minPoLotCalcDays, int minPoLotNum,
			int maxPoNumcalcDays, int minPoNum, double deficiencyRate,
			int maxEntrustPoTimelag) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(MineService.Param.STOCK_HOLD_TERM_CALC_CATEGORY,
					stockHoldTermCalcCategory);
			param.put(MineService.Param.STOCK_HOLD_DAYS, stockHoldDays);
			param.put(MineService.Param.MIN_PO_LOT_CALC_DAYS, minPoLotCalcDays);
			param.put(MineService.Param.MIN_PO_LOT_NUM, minPoLotNum);
			param.put(MineService.Param.MAX_PO_NUM_CALC_DAYS, maxPoNumcalcDays);
			param.put(MineService.Param.MIN_PO_NUM, minPoNum);
			param.put(MineService.Param.DEFICIENCY_RATE, deficiencyRate);
			param.put(MineService.Param.MAX_ENTRUST_PO_TIMELAG,
					maxEntrustPoTimelag);

			NormalDistributionImpl ndi = new NormalDistributionImpl();
			BigDecimal baseRateObj = new BigDecimal(1.0);
			BigDecimal deficiencyRateObj = new BigDecimal(deficiencyRate);
			double safetyCoefficient = ndi
					.inverseCumulativeProbability(baseRateObj.subtract(
							deficiencyRateObj).doubleValue());
			BigDecimal safetyCoefficientObj = new BigDecimal(safetyCoefficient);
			param.put(
					MineService.Param.SAFETY_COEFFICIENT,
					safetyCoefficientObj.setScale(
							super.mineDto.statsDecAlignment,
							RoundingMode.HALF_UP).doubleValue());

			this.updateBySqlFile("mine/UpdateMineByStock.sql", param).execute();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
