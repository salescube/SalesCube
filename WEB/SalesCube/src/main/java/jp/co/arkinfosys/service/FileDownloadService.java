/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.service.exception.ServiceException;

/**
 * ファイルダウンロードサービスクラスです.
 * @author Ark Information Systems
 *
 * @param <ENTITY>
 */
public class FileDownloadService<ENTITY> extends AbstractService<ENTITY> {

	/**
	 * ファイル転送時のバッファサイズ
	 */
	protected static final int BUFF_SIZE = 1024 * 8;

	/**
	 * エンコーディングタイプgzip
	 */
	protected static final String GZIP = "gzip";

	/**
	 * HTTPヘッダ定義クラスです.
	 * @author Ark Information Systems
	 *
	 */
	protected static class HTTP_HEADER {
		public static final String CONTENT_DISPOSITION = "Content-disposition";

		public static final String CONTENT_ENCODING = "Content-Encoding";

		public static final String ACCEPT_ENCODING = "accept-encoding";
	}

	/**
	 * ファイルをダウンロートします.
	 * @param filePath ファイルパス
	 * @param downloadFileName ダウンロード時ファイル名
	 * @param httpRequest {@link HttpServletRequest}
	 * @param httpResponse {@link HttpServletResponse}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.service.AbstractService#downloadFile(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void downloadFile(String filePath, String downloadFileName,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws ServiceException {
		try {
			File file = new File(filePath);
			if (!file.exists() || !file.canRead()) {
				return;
			}

			BufferedInputStream is = null;
			OutputStream os = null;
			try {
				is = new BufferedInputStream(new FileInputStream(file));
				os = this.createOutputStream(downloadFileName, httpRequest,
						httpResponse);
				this.transferIO(is, os);
			} finally {
				is.close();
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * ファイルダウンロード用に設定したレスポンス出力ストリームを返します.
	 * @param fileName ファイル名
	 * @param httpRequest {@link HttpServletRequest}
	 * @param httpResponse {@link HttpServletResponse}
	 * @return 出力ストリーム
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @see jp.co.arkinfosys.service.AbstractService#createOutputStream(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected OutputStream createOutputStream(String fileName,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws UnsupportedEncodingException, IOException {

		String acceptEncoding = httpRequest
				.getHeader(FileDownloadService.HTTP_HEADER.ACCEPT_ENCODING);
		boolean deflate = false;
		if (StringUtil.hasLength(acceptEncoding)
				&& acceptEncoding.contains(FileDownloadService.GZIP)) {
			// 圧縮可能であれば圧縮する
			httpResponse.setHeader(
					FileDownloadService.HTTP_HEADER.CONTENT_ENCODING,
					FileDownloadService.GZIP);
			deflate = true;
		}

		// Content-Type設定
		httpResponse.setContentType(Constants.MIME.BIN);
		// Content-Disposition設定
		httpResponse.setHeader(
				FileDownloadService.HTTP_HEADER.CONTENT_DISPOSITION,
				"attachment; filename=\""
						+ new String(fileName
								.getBytes(Constants.CHARSET.WINDOWS31J),
								Constants.CHARSET.LATIN1) + "\"");

		// OutputStreamを返す
		if (deflate) {
			return new GZIPOutputStream(new BufferedOutputStream(httpResponse
					.getOutputStream()));
		}
		return new BufferedOutputStream(httpResponse.getOutputStream());
	}

	/**
	 * 入力ストリームから出力ストリームへデータを転送します.
	 * @param is 入力ストリーム
	 * @param os 出力ストリーム
	 * @throws IOException
	 * @see jp.co.arkinfosys.service.AbstractService#transferIO(java.io.InputStream, java.io.OutputStream)
	 */
	protected void transferIO(InputStream is, OutputStream os)
			throws IOException {
		if (is == null || os == null) {
			return;
		}
		try {
			byte[] buffer = new byte[FileDownloadService.BUFF_SIZE];
			int len = 0;
			while ((len = is.read(buffer, 0, buffer.length)) != -1) {
				os.write(buffer, 0, len);
			}

			os.flush();
			if (os instanceof GZIPOutputStream) {
				((GZIPOutputStream) os).finish();
			}

		} catch (SocketException e) {
			if (!e.getMessage().contains("ClientAbortException")) {
				// ClientAbortExceptionはユーザキャンセルなので正常と判断
				throw e;
			}
		} finally {
			is.close();
			os.close();
		}
	}

}
