/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.seasar.framework.beans.util.BeanMap;

import jp.co.arkinfosys.entity.ReportTemplate;
import jp.co.arkinfosys.service.exception.ServiceException;

/**
 *
 * 帳票テンプレートサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class ReportTemplateService extends AbstractService<ReportTemplate> {
    @Resource
    protected ServletContext application;

    /**
	 * 帳票テンプレートマップ
	 */
	protected Map<String, ReportTemplate> reportTemplateMap = null;

	/**
	 * 帳票オブジェクト
	 */
	protected JasperPrint print = null;

	/**
	 * 帳票テンプレートディレクトリのベースパス
	 */
	private static final String REPORT_TEMPLATE_PATH = "/WEB-INF/report_template/";

	/**
	 * 帳票出力出力先ディレクトリのベースパス
	 */
	private static final String REPORT_OUTPUT_PATH = "/WEB-INF/report_output/";

	/**
	 * ファイル転送時のバッファサイズ
	 */
	public static final int BUFF_SIZE = 1024*8;

	/**
	 * 実ファイル削除フラグ
	 */
	private boolean deleteFlag = true;


	/**
	 * ファイル拡張子定義クラスです.
	 */
	public static final class FileSuffix {
		public static final String JASPER = ".jasper";	// コンパイル済みテンプレートファイルの拡張子
		public static final String PDF = ".pdf";
		public static final String XLS = ".xls";
		public static final String ZIP = ".zip";
	}

	/**
	 * MIMEタイプ定義手クラスです.
	 */
	public static final class MimeType {
		public static final String PDF = "application/pdf";
		public static final String XLS = "application/vnd.ms-excel";
		public static final String ZIP = "application/zip";
	}

	/**
	 * 帳票を作成します.
	 * @param reportId 帳票ID
	 * @param param 帳票パラメータ
	 * @param detail 帳票詳細パラメータ
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public void createReport(String reportId, BeanMap param, List<BeanMap>detail) throws ServiceException {
		try {
			// テンプレートファイルのパスを取得
			String path = this.getTemplateRealPath(reportId);

			// データソースを作成
			JRMapCollectionDataSource ds = new JRMapCollectionDataSource(convertBeanMapList(detail));

			// コンパイル済み？
			JasperPrint jasperPrint;
			if (path.endsWith(FileSuffix.JASPER)) {
				// レポートを埋める（コンパイル済み）
				jasperPrint = JasperFillManager.fillReport(path, convertBeanMap(param), ds);
			}
			else {
				// テンプレートのコンパイル
				JasperReport jasperReport = JasperCompileManager.compileReport(path);

				// レポートを埋める
				jasperPrint = JasperFillManager.fillReport(jasperReport, convertBeanMap(param), ds);
			}



			// 新規プリント？
			if (this.print==null) {
				this.print = jasperPrint;
			}
			else {
				Iterator<JRPrintPage> it = jasperPrint.getPages().iterator();
				while(it.hasNext()) {
					print.addPage(it.next());
				}
			}

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 帳票をPDF形式で出力します.
	 * @param filePath 出力ファイルパス（null指定の場合は自動的に設定）
	 * @return 出力されたファイルのフルパス
	 * @throws ServiceException
	 */
	public String outputToPDF(String filePath) throws ServiceException {
		try {
			// ファイル名を作成
			String path = this.createOutputFilePath(filePath, FileSuffix.PDF);

			// PDFファイルに出力
			JasperExportManager.exportReportToPdfFile(print, path);

			return path;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 帳票をxls形式で出力します.
	 * @param filePath 出力ファイルパス（null指定の場合は自動的に設定）
	 * @return 出力されたファイルのフルパス
	 * @throws ServiceException
	 */
	public String outputToXLS(String filePath) throws ServiceException {
		try {
			// ファイル名を作成
			String path = this.createOutputFilePath(filePath, FileSuffix.XLS);

			// xlsファイルに出力
			JExcelApiExporter exporterXLS = new JExcelApiExporter();
			exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, path);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
			exporterXLS.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
			exporterXLS.exportReport();

			return path;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 帳票をPDF形式でレスポンスに出力します.
	 * @param response HTTPレスポンス
	 * @param attachmentFileName 添付ファイル名
	 * @throws ServiceException
	 */
	public void outputToPDF(HttpServletResponse response, String attachmentFileName) throws ServiceException {
		try {
			// ファイルに出力する
			String path = this.outputToPDF(null);

			// レスポンスに書き出し
			this.writeResponse(response, path, MimeType.PDF, attachmentFileName);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * 帳票をxls形式でレスポンスに出力します.
	 * @param response HTTPレスポンス
	 * @param attachmentFileName 添付ファイル名
	 * @throws ServiceException
	 */
	public void outputToXLS(HttpServletResponse response, String attachmentFileName) throws ServiceException {
		try {
			// ファイルに出力する
			String path = this.outputToXLS(null);

			// レスポンスに書き出し
			this.writeResponse(response, path, MimeType.XLS, attachmentFileName);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * レスポンスにファイルを書き出します.
	 * @param response HTTPレスポンス
	 * @param targetFilePath 出力対象ファイルのフルパス
	 * @param mimeType 出力対象ファイルのMIMEタイプ
	 * @param attachmentFileName 添付ファイル名
	 * @throws Exception
	 */
	public void writeResponse(HttpServletResponse response, String targetFilePath, String mimeType, String attachmentFileName) throws Exception {
		FileInputStream is = null;
		ServletOutputStream os = null;
		try {
			// ファイルを開く
			File file = new File(targetFilePath);
			int len = (int)file.length();
			is = new FileInputStream(file);

			// レスポンスに書き出し
			os = response.getOutputStream();
			response.setContentType(mimeType);
			response.setContentLength(len);
			response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(attachmentFileName,"UTF-8") + "\"" );
			// キャッシュに保存され前回のファイルが表示される不具合対応として、キャッシュを無効にする
			Calendar objCal=Calendar.getInstance();
			objCal.set(1970,0,1,0,0,0);//過去日
			response.setDateHeader("Expires",objCal.getTime().getTime());

			byte[] buff = new byte[BUFF_SIZE];
			while (true) {
				int sz = is.read(buff);
				if (sz<=0) {
					break;
				}
				os.write(buff, 0, sz);
			}
			os.flush();
			os.close();
			os = null;
			is.close();
			is = null;
			if( isDeleteFlag() ){
				file.delete();
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			if (os!=null) {
				os.close();
			}
			if (is!=null) {
				is.close();
			}
		}
	}

	/**
	 * 帳票を破棄します.
	 */
	public void disposeReport() {
		this.print = null;
	}

	/**
	 * 帳票IDを指定して帳票テンプレートの絶対パスを取得します.
	 * @param reportId 帳票ID
	 * @return 帳票テンプレートの絶対パス
	 * @throws ServiceException
	 */
	protected String getTemplateRealPath(String reportId) throws ServiceException {
		// 帳票テンプレートマップの初期化
		if (reportTemplateMap==null) {
			// 帳票テンプレートの一覧を取得
			Map<String, Object> param = super.createSqlParam();
			List<ReportTemplate> list = this.selectBySqlFile(ReportTemplate.class,
					"reporttemplate/SelectAll.sql", param).getResultList();

			// マップに移し替え
			this.reportTemplateMap = new HashMap<String, ReportTemplate>();
			Iterator<ReportTemplate> it = list.iterator();
			while (it.hasNext()) {
				ReportTemplate item = it.next();
				this.reportTemplateMap.put(item.reportId, item);
			}
		}

		// 帳票テンプレートの存在チェック
		ReportTemplate template = this.reportTemplateMap.get(reportId);
		if (template==null) {
			return "";
		}

		// 帳票テンプレートの絶対パス取得
		return application.getRealPath(REPORT_TEMPLATE_PATH + template.path);
	}

	/**
	 * BeanMapからMap<String, Object>への変換を行います.<br>
	 * (JasperReportsを使用する上で必要な処理です)
	 * @param src 変換元BeanMap
	 * @return 変換後Map
	 */
	protected Map<String, Object> convertBeanMap(BeanMap src) {
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.putAll(src);
		return ret;
	}

	/**
	 * List<BeanMap>からList<Map<String,Object>>への変換を行います.<br>
	 * (JasperReportsを使用する上で必要な処理です)
	 *
	 * 2011/10/04 jasperreportのバージョンアップ(3.7.0⇒4.1.1)対応で作成
	 *
	 * @param src 変換元List<BeanMap>
	 * @return 変換後List<Map>
	 */
	protected Collection<Map<String, ?>> convertBeanMapList(List<BeanMap> src) {
		Collection<Map<String, ?>> ret = new ArrayList<Map<String,?>>();
		for(BeanMap bean : src){
			Map<String,Object> dest = new HashMap<String,Object>();
			dest.putAll(bean);
			ret.add(dest);
		}
		return ret;
	}


	/**
	 * 出力ファイル名を生成します.
	 * @param filePath ファイルパス
	 * @param suffix ファイル名のサフィックス
	 * @return 出力ファイル名
	 * @throws IOException
	 */
	protected String createOutputFilePath(String filePath, String suffix) throws IOException {
		// ファイルパスがnull以外の場合はそのまま返却
		if (filePath!=null) {
			return filePath;
		}

		// ユニークファイル名生成
		String path = this.getOutputDirectoryPath();
		File dir = new File(path);
		File tmpFile =  File.createTempFile(this.domainDto.domainId, suffix, dir);
		String ret = tmpFile.getAbsolutePath();
		tmpFile.delete();

		return ret;
	}

	/**
	 * 帳票出力ディレクトリの物理パスを返します.
	 * @return 帳票出力ディレクトリの物理パス
	 */
	public String getOutputDirectoryPath() {
		return application.getRealPath(REPORT_OUTPUT_PATH);
	}

	/**
	 * URLパスを物理パスに変換します.
	 * @param path URLパス
	 * @return 物理パス
	 */
	public String getRealPath(String path ) {
		return application.getRealPath(path);
	}

	/**
	 * レスポンス出力後の実ファイル削除指定フラグを設定します.
	 * @param deleteFlag 削除するか否か
	 */
	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	/**
	 * レスポンス出力後の実ファイル削除指定フラグを取得します.
	 * @return レスポンス出力後に実ファイルを削除するか否か
	 */
	public boolean isDeleteFlag() {
		return deleteFlag;
	}
}
