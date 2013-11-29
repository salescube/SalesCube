/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.io.BufferedInputStream;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.form.master.ImportProductExcelForm;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.exception.FileImportException;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * 商品マスタExcelファイル取込を行うアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ImportProductExcelAction extends CommonResources {

	/**
	 * 画面遷移のためのマッピング定義クラスです.
	 *
	 */
	public static class Mapping {
		public static final String INPUT = "/master/searchProduct";
	}

	@ActionForm
	@Resource
	public ImportProductExcelForm importProductExcelForm;

	@Resource
	private ProductService productService;

	/**
	 * アップロードされたExcelファイルを処理して、商品マスタの情報を更新します.
	 *
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = ImportProductExcelAction.Mapping.INPUT)
	public String upload() throws Exception {
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(
					this.importProductExcelForm.productExcelFile
							.getInputStream());

			POIFSFileSystem fs = new POIFSFileSystem(in);
			HSSFWorkbook workbook = new HSSFWorkbook(fs);

			this.productService.updateProductsFromExcel(workbook);

			// メッセージ設定
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("infos.import"));
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);
		} catch (OfficeXmlFileException e) {
			super.errorLog(e);
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.file.format"));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (FileImportException e) {
			super.errorLog(e);

			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.file.import"));
			if (e.getMessageCount() > 0) {
				super.messages.add(e.getMessages());
			}

			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (UnabledLockException e) {
			super.errorLog(e);
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage(e.getKey()));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (ServiceException e) {
			super.errorLog(e);
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.file.import"));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (Exception e) {
			super.errorLog(e);
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.file.format"));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return ImportProductExcelAction.Mapping.INPUT;
	}

}
