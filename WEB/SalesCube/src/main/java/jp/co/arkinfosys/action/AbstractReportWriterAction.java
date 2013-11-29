/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants.VALID_FLAG;
import jp.co.arkinfosys.entity.join.BankDwb;
import jp.co.arkinfosys.service.BankService;
import jp.co.arkinfosys.service.MineService;
import jp.co.arkinfosys.service.ReportTemplateService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

/**
 * 帳票出力機能の基底アクションクラスです.
 * @author Ark Information Systems
 *
 */
public abstract class AbstractReportWriterAction extends CommonResources {

	@Resource
	protected ReportTemplateService reportTemplateService;

	@Resource
	protected MineService mineService;

	@Resource
	protected BankService bankService;

	/**
	 * 自社マスタのデータ
	 */
	protected BeanMap mineMst = null;

	/**
	 * 銀行マスタのデータをキャッシュする(銀行マスタの内、1つしか帳票出力しないので、1つしか持たないことに注意)
	 */
	protected BeanMap bankMst = null;
	/**
	 * 銀行マスタのREMARKSはマップから除外する（伝票側のREMARKSと重複するため）
	 */
	private static final String EXCLUDE_REMARKS = "remarks";

	/**
	 * ドキュメントタイプ定義クラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static final class DocType {
		public static final int PDF = 1;
		public static final int XLS = 2;
	}
	protected int docType = DocType.PDF;

	/**
	 * 一時ディレクトリ名
	 */
	protected String tempDir = null;

	/**
	 * PDF形式でレポートを作成し、レスポンスに出力します.<br>
	 * デフォルト実装ではnullを返します.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @return 処理結果文字列
	 * @throws Exception
	 */
	protected String pdf() throws Exception {
		this.docType = DocType.PDF;
		this.createReport();
		return null;
	}

	/**
	 * XLS形式でレポートを作成し、レスポンスに出力します.<br>
	 * デフォルト実装ではnullを返します.<br>
	 * 必要に応じてサブクラスでオーバーライドしてください.
	 * @return 処理結果文字列
	 * @throws Exception
	 */
	protected String excel() throws Exception {
		this.docType = DocType.XLS;
		this.createReport();
		return null;
	}

	/**
	 * レポートを作成します.<br>
	 * 伝票、明細情報、自社、銀行マスタを取得して、レポートを作成します.<br>
	 * 一時ディレクトリ名が設定されている場合はzip圧縮、設定されていない場合は直接レスポンスに出力します.
	 * @throws ServiceException
	 */
	protected void createReport() throws ServiceException {
		int reportCnt = 0;	// フラグでもいいけど何かに使えるかもなのでカウンタにする

		// レポート作成
		for (int index=0;;index++) {
			// レポート取得
			String reportId = this.getReportId(index);
			if (reportId==null) {
				break;
			}

			// 伝票データ取得
			BeanMap slip = this.getSlip(index);
			if (slip==null) {
				// 次の帳票を出力
				continue;
			}

			// 明細データを取得
			List<BeanMap> detail = this.getDetailList(index);
			if (detail==null) {
				// 次の帳票を出力
				continue;
			}

			// 自社情報を取得して伝票データを追加する
			BeanMap mine = this.getMine();
			mine.putAll(slip);

			// 銀行情報を取得して自社データに追加する
			BeanMap bank = this.getBank();
			if(bank != null) {
				mine.putAll(bank);
			}

			// レポート作成
			this.reportTemplateService.createReport(reportId, mine, detail);
			reportCnt++;

			// 実ファイル名を取得
			String realFileName = this.getRealFilePreffix(index);
			if (realFileName==null || realFileName.length()<1) {
				continue;
			}

			// 一時ディレクトリ作成
			if (this.tempDir==null) {
				String path = this.reportTemplateService.getOutputDirectoryPath()+"/"+this.httpSession.getId();
				File dir = new File(path);
				dir.mkdir();
				this.tempDir = dir.getAbsolutePath();
			}

			// 一時ディレクトリに出力
			realFileName = this.tempDir + "/" + realFileName;
			if (this.docType==DocType.PDF) {
				this.reportTemplateService.outputToPDF(realFileName+ReportTemplateService.FileSuffix.PDF);
			}
			else {
				this.reportTemplateService.outputToXLS(realFileName+ReportTemplateService.FileSuffix.XLS);
			}
			this.reportTemplateService.disposeReport();
		}

		// レポート未作成の場合はここで終了
		if (reportCnt==0) {
			return;
		}

		// 直接レスポンスに返却する場合
		if (this.tempDir==null) {
			if (this.docType==DocType.PDF) {
				this.outputToPDF();
			}
			else {
				this.outputToXLS();
			}
			return;
		}

		// 一時ファイルをzip圧縮してレスポンスに返却
		try {
			String zipFile = this.createZipFile();
			String resFile = this.getFilePreffix() + ReportTemplateService.FileSuffix.ZIP;
			this.reportTemplateService.writeResponse(this.httpResponse, zipFile, ReportTemplateService.MimeType.ZIP, resFile);

			// 一時ディレクトリごと削除
			File[] files = {new File(this.tempDir)};
			this.deleteFiles(files);
			this.tempDir = null;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * PDF形式でレスポンスに出力します.
	 * @throws ServiceException
	 */
	protected void outputToPDF() throws ServiceException {
		String attachmentFileName = this.getFilePreffix() + ReportTemplateService.FileSuffix.PDF;
		this.reportTemplateService.outputToPDF(this.httpResponse, attachmentFileName);
		this.reportTemplateService.disposeReport();	// クリアしちゃう
	}

	/**
	 * XLS形式でレスポンスに出力します.
	 * @throws ServiceException
	 */
	protected void outputToXLS() throws ServiceException {
		String attachmentFileName = this.getFilePreffix() + ReportTemplateService.FileSuffix.XLS;
		this.reportTemplateService.outputToXLS(this.httpResponse, attachmentFileName);
		this.reportTemplateService.disposeReport();	// クリアしちゃう
	}

	/**
	 * 自社マスタ情報を返します.<br>
	 * 未取得の場合のみデータベースから返します.<br>
	 * 未取得、取得済みに関わらず、コピーした自社マスタ情報を返します.
	 * @return コピーした自社マスタ情報
	 * @throws ServiceException
	 */
	protected BeanMap getMine() throws ServiceException {
		// 未取得の場合は取得する
		if (mineMst==null) {
			mineMst = this.mineService.getMineSimple();
		}

		// コピーを返却
		BeanMap ret = new BeanMap();
		ret.putAll(mineMst);

		return ret;
	}

	/**
	 * 銀行マスタ情報を返します.<br>
	 * 未取得の場合のみデータベースから返します.このとき、取得結果の先頭1件のみ結果に使用します.<br>
	 * 未取得、取得済みに関わらず、コピーした銀行マスタ情報を返します.
	 * @return コピーした銀行マスタ情報
	 * @throws ServiceException
	 */
	protected BeanMap getBank() throws ServiceException {
		// 未取得の場合は取得する
		if (bankMst==null) {
			Map<String, Object> param = new HashMap<String, Object>();
//			param.put(BankService.Param.BANK_ID, this.getBankId());

			// 有効な銀行のみ
			param.put(BankService.Param.VALID, VALID_FLAG.VALID);

			
			List<BankDwb> bankList;
			bankList = this.bankService.findByConditionLimit( param, BankService.Param.BANK_CODE, true, 1, 0);

			if(bankList.size() > 0) {
				bankMst = Beans.createAndCopy(BeanMap.class, bankList.get(0)).execute();
			} else {
				return null;
			}
		}

		// コピーを返却
		BeanMap ret = new BeanMap();
		ret.putAll(bankMst);
		ret.remove(EXCLUDE_REMARKS);	// 銀行マスタの適用は除外
		
		return ret;
	}


	/**
	 * zipファイルを作成します.
	 * @return 作成したzipファイルの絶対パス
	 * @throws Exception
	 */
	private String createZipFile() throws Exception{
		// テンポラリファイルを作成
		File dir = new File(this.reportTemplateService.getOutputDirectoryPath());
		File tmpFile = File.createTempFile(this.domainDto.domainId, null, dir);
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tmpFile));

		// 保存ファイルをzipファイルに追加する
		File[] files = {new File(this.tempDir)};
		try {
			encode(zos, files);
		} finally {
			zos.close();
		}

		return tmpFile.getAbsolutePath();
	}

	/**
	 * 指定したファイルをzipエントリに追加します.
	 * @param zos zipエントリ
	 * @param files 追加するファイルの配列
	 * @throws Exception
	 */
	private void encode(ZipOutputStream zos, File[] files) throws Exception {
		byte[] buf = new byte[ReportTemplateService.BUFF_SIZE];

		for (File f : files) {
			if (f.isDirectory()) {
				encode(zos, f.listFiles());
			} else {
				int flen = f.getPath().length() - f.getName().length();
				String entry = f.getPath().replaceAll("\\\\", "/").substring(flen);
				ZipEntry ze = new ZipEntry(entry);
				zos.putNextEntry(ze);
				InputStream is = null;
				try {
					is = new BufferedInputStream(new FileInputStream(f));
					for (;;) {
						int len = is.read(buf);
						if (len < 0) {
							break;
						}
						zos.write(buf, 0, len);
					}
				} finally {
					is.close();
				}
			}
		}
	}

	/**
	 * 指定されたファイルおよびディレクトリを削除します.
	 * @param files 削除するファイルまたはディレクトリの配列
	 */
	private void deleteFiles(File[] files) {
		for (File f : files) {
			if (f.isDirectory()) {
				deleteFiles(f.listFiles());
			}
			f.delete();
		}
	}

	/**
	 * レポートテンプレートIDを返します.
	 * @param index 取得するテンプレートのインデックス
	 * @return レポートテンプレートID
	 */
	protected abstract String getReportId(int index);

	/**
	 * 拡張子を除いたファイル名を返します.
	 * @return 拡張子なしのファイル名
	 */
	protected abstract String getFilePreffix();

	/**
	 * 伝票情報を返します.
	 * @param index 取得する帳票のインデックス
	 * @return 伝票情報
	 * @throws ServiceException
	 */
	protected abstract BeanMap getSlip(int index) throws ServiceException;

	/**
	 * 明細情報を返します.
	 * @param index 取得する帳票のインデックス
	 * @return 明細情報のリスト
	 * @throws ServiceException
	 */
	protected abstract List<BeanMap> getDetailList(int index) throws ServiceException;

	/**
	 * 実ファイル名を返します.<br>
	 * デフォルト実装ではnullを返します.<br>
	 * ファイルを複数作成し、zipファイルとして返却する場合にオーバーライドしてください.
	 * @param index 取得する帳票のインデックス
	 * @return 出力ファイル名
	 */
	protected String getRealFilePreffix(int index) {
		return null;
	}
}
