/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.service;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import jp.co.arkinfosys.service.ReportTemplateService.MimeType;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.BeanMap;

/**
 * レポート出力の基底サービスクラスです.
 * @author Ark Information Systems
 *
 * @param <ENTITY>
 */
public abstract class AbstractReportService<ENTITY> extends AbstractService<ENTITY> {
	@Resource
	protected ReportTemplateService reportTemplateService;

	@Resource
	protected MineService mineService;

	/**
	 * 自社マスタのデータ
	 */
	protected BeanMap mineMst = null;

	/**
	 * ドキュメントタイプ
	 */
	public static final class DocType {
		public static final int PDF = 1;
		public static final int XLS = 2;
	}
	protected int docType = DocType.PDF;

	/**
	 * 一時ディレクトリ名
	 */
	private static final String REPORT_BASE_PATH = "/WEB-INF/out/";
	private static final String REPORT_PDF_PATH = "report_pdf/";
	private static final String REPORT_EXCEL_PATH = "report_excel/";

	/**
	 * レポートの保存先パスを返します.
	 * @param docType 文書形式
	 * @param reportId レポートテンプレートID
	 * @param realFileName ファイル名
	 * @return 保存先パス
	 */
	protected String getBaseSaveDir( int docType, String reportId, String realFileName ) {
		if (docType==DocType.PDF) {
			return REPORT_BASE_PATH + domainDto.domainId + "/" + REPORT_PDF_PATH +  reportId + "/" + realFileName;
		}else if (docType==DocType.XLS) {
			return REPORT_BASE_PATH + domainDto.domainId + "/" + REPORT_EXCEL_PATH + reportId + "/" + realFileName;
		}
		return "";
	}

	/**
	 * PDF形式でレポートを作成し、レスポンスに出力します.
	 * @return null
	 * @throws Exception
	 */
	protected String pdf() throws Exception {
		this.docType = DocType.PDF;
		this.createReport();
		return null;
	}

	/**
	 * XLS形式でレポートを出力し、レスポンスに出力します.
	 * @return null
	 * @throws Exception
	 */
	protected String excel() throws Exception {
		this.docType = DocType.XLS;
		this.createReport();
		return null;
	}

	/**
	 * レポートを作成します.
	 * @throws ServiceException
	 */
	protected void createReport() throws ServiceException {
		int reportCnt = 0;	

		
		for (int index=0;;index++) {
			
			String reportId = this.getReportId(index);
			if (reportId==null) {
				break;
			}

			
			BeanMap slip = this.getSlip(index);
			if (slip==null) {
				
				continue;
			}

			
			List<BeanMap> detail = this.getDetailList(index);
			if (detail==null) {
				
				continue;
			}

			
			BeanMap mine = this.getMine();
			mine.putAll(slip);

			
			this.reportTemplateService.createReport(reportId, mine, detail);
			reportCnt++;

			
			String realFileName = this.getRealFilePreffix(index);
			if (realFileName==null || realFileName.length()<1) {
				continue;
			}


			
			realFileName = getBaseSaveDir( this.docType, reportId, realFileName );
			realFileName = this.reportTemplateService.getRealPath(realFileName);
			if (this.docType==DocType.PDF) {
				this.reportTemplateService.outputToPDF(realFileName+ReportTemplateService.FileSuffix.PDF);
			}else {
				this.reportTemplateService.outputToXLS(realFileName+ReportTemplateService.FileSuffix.XLS);
			}
			this.reportTemplateService.disposeReport();
		}

		
		if (reportCnt==0) {
			return;
		}

	}

	/**
	 * 自社情報を返します.<br>
	 * 未取得の場合のみ、データベースから取得します.<br>
	 * 取得の有無に関わらず、コピーを返します.
	 * @return コピーした自社情報
	 * @throws ServiceException
	 */
	protected BeanMap getMine() throws ServiceException {
		
		if (mineMst==null) {
			mineMst = this.mineService.getMineSimple();
		}

		
		BeanMap ret = new BeanMap();
		ret.putAll(mineMst);

		return ret;
	}

	/**
	 * ファイルおよびディレクトリを削除します.
	 * @param files 削除するファイルの配列
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
	 * @return レポートテンプレートID
	 */
	protected abstract String getReportId();

	/**
	 * 指定されたインデックスのレポートテンプレートIDを返します.
	 * @param index インデックス
	 * @return レポートテンプレートID
	 */
	protected abstract String getReportId(int index);

	/**
	 * 拡張子を除いたファイル名を返します.
	 * @return 拡張子を除いたファイル名
	 */
	protected abstract String getFilePreffix();

	/**
	 * 伝票情報を返します.
	 * @param index インデックス
	 * @return 伝票情報
	 * @throws ServiceException
	 */
	protected abstract BeanMap getSlip(int index) throws ServiceException;

	/**
	 * 明細行情報を返します.
	 * @param index インデックス
	 * @return 明細行情報のリスト
	 * @throws ServiceException
	 */
	protected abstract List<BeanMap> getDetailList(int index) throws ServiceException;

	/**
	 * 実ファイル名を返します.
	 * @param index インデックス
	 * @return 実ファイル名
	 */
	protected abstract String getRealFilePreffix(int index);

	/**
	 * PDF形式でレスポンスに出力します.
	 * @param hsr {@link HttpServletResponse}
	 * @param id 出力するレポートID
	 * @throws ServiceException
	 */
	public void outputToPDF(HttpServletResponse hsr, String id ) throws ServiceException {
		try {
			String attachmentFileName = id + ReportTemplateService.FileSuffix.PDF;

			String path = getBaseSaveDir( DocType.PDF, getReportId(), attachmentFileName );
			path = this.reportTemplateService.getRealPath(path);

			boolean prevFlag = this.reportTemplateService.isDeleteFlag();
			this.reportTemplateService.setDeleteFlag(false);
			this.reportTemplateService.writeResponse(hsr, path, MimeType.PDF, attachmentFileName);
			this.reportTemplateService.setDeleteFlag(prevFlag);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	/**
	 * Excel形式でレスポンスに出力します.
	 * @param hsr {@link HttpServletResponse}
	 * @param id 出力するレポートID
	 * @throws ServiceException
	 */
	public void outputToEXCEL(HttpServletResponse hsr, String id ) throws ServiceException {
		try {
			String attachmentFileName = id + ReportTemplateService.FileSuffix.XLS;

			String path = getBaseSaveDir( DocType.XLS, getReportId(), attachmentFileName );
			path = this.reportTemplateService.getRealPath(path);

			boolean prevFlag = this.reportTemplateService.isDeleteFlag();
			this.reportTemplateService.setDeleteFlag(false);
			this.reportTemplateService.writeResponse(hsr, path, MimeType.XLS, attachmentFileName);
			this.reportTemplateService.setDeleteFlag(prevFlag);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
}
