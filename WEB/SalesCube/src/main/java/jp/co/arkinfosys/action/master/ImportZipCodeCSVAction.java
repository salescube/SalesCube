/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import jp.co.arkinfosys.action.AbstractXSVUploadAction;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.form.master.ImportZipCodeCSVForm;
import jp.co.arkinfosys.service.ZipService;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.upload.S2MultipartRequestHandler;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * 郵便番号辞書画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class ImportZipCodeCSVAction extends AbstractXSVUploadAction {

	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static final class Mapping {
		public static final String INPUT = "importZipCodeCSV.jsp";
	}

	@ActionForm
	@Resource
	public ImportZipCodeCSVForm importZipCodeCSVForm;

	@Resource
	public ZipService zipService;

	/**
	 * 初期化処理を行います.<br>
	 * 何かしらの問題があった場合に、画面にメッセージを表示します.<br>
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移後のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {
		SizeLimitExceededException e = (SizeLimitExceededException) super.httpRequest
				.getAttribute(S2MultipartRequestHandler.SIZE_EXCEPTION_KEY);
		if (e != null) {
			// 他のメッセージをクリアする
			super.httpRequest.removeAttribute(Globals.ERROR_KEY);

			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.upload.size", e
							.getPermittedSize()));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		}
		return ImportZipCodeCSVAction.Mapping.INPUT;
	}

	/**
	 * アップロードしたCSVファイルを処理し、アクションフォームを初期化します.<br>
	 * 何かしらの問題があった場合に、画面にメッセージを表示します.<br>
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = "index")
	public String upload() throws Exception {
		try {
			// ZIP_MSTを全行削除
			zipService.deleteAllRecords();

			// アクションフォームを初期化する
			this.readXSV(this.importZipCodeCSVForm.zipCodeCSV);

			if (this.messages.size() > 0) {
				return ImportZipCodeCSVAction.Mapping.INPUT;
			}

			// メッセージ設定
			this.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"infos.insert"));
			ActionMessagesUtil.addMessages(this.httpRequest, this.messages);
		} catch (UnsupportedEncodingException e) {
			super.errorLog(e);

			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.file.encoding"));
			ActionMessagesUtil.addErrors(super.httpRequest, errors);
		} catch (Exception e) {
			super.errorLog(e);

			this.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"errors.zipcode.format"));
			ActionMessagesUtil.addErrors(super.httpRequest, this.messages);
		}

		return ImportZipCodeCSVAction.Mapping.INPUT;
	}


	/**
	 *
	 * @param index 行番号
	 * @param line セパレータで分割した値の配列
	 * @param values セパレータで分割した値の配列
	 * @throws Exception
	 * @see jp.co.arkinfosys.action.AbstractXSVUploadAction#processLine(int, java.lang.String, java.lang.String[])
	 */
	@Override
	protected void processLine(int index, String line, String[] values)
			throws Exception {
		// カラム数
		if (values == null
				|| values.length != Constants.ZIP_CODE_CSV.COLUMN_COUNT) {
			throw new Exception(line);
		}

		String zipCode = values[2]; // 郵便番号
		String addr1 = values[6]; // 住所１
		String addr2 = values[7]; // 住所１
		String addr3 = values[8]; // 住所１

		// 郵便番号を整形
		zipCode = zipCode.substring(0, 3) + "-" + zipCode.substring(3);

		// 住所を整形
		String address = addr1 + addr2;
		if (addr3 != null && !addr3.endsWith(Constants.ZIP_CODE_CSV.CASE_1)
				&& !addr3.endsWith(Constants.ZIP_CODE_CSV.CASE_2)) {
			address = address + addr3;
		}

		// 行追加
		zipService.insertRecord(index + 1, zipCode, address);
	}

	/**
	 *
	 * @return {@link SEPARATOR#COMMA}
	 * @see jp.co.arkinfosys.action.AbstractXSVUploadAction#getSeparator()
	 */
	@Override
	protected String getSeparator() {
		return SEPARATOR.COMMA;
	}

}
