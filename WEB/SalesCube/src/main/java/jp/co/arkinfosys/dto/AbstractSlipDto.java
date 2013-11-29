/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto;

import java.util.ArrayList;
import java.util.List;

import jp.co.arkinfosys.form.AbstractSlipEditForm;

/**
 * 伝票情報を管理するDTOの基底クラスです.
 *
 * @author Ark Information Systems
 *
 * @param <LINEDTOCLASS>
 */
public abstract class AbstractSlipDto<LINEDTOCLASS extends AbstractLineDto> {

	/** 明細行のリスト */
	protected List<LINEDTOCLASS> lineDtoList;

	/**
	 * 明細行リストを返します.
	 * @return　明細行リスト
	 */
	public List<LINEDTOCLASS> getLineDtoList() {
		return lineDtoList;
	}

	/**
	 * 明細行リストを設定します.
	 * @param lineDtoList　明細行リスト
	 */
	public void setLineDtoList(List<LINEDTOCLASS> lineDtoList) {
		this.lineDtoList = lineDtoList;
	}


	/**
	 * 初期化時のリストを埋める処理をします.
	 */
	@SuppressWarnings("unchecked")
	public void fillList() {
		if (lineDtoList == null) {
			lineDtoList = new ArrayList<LINEDTOCLASS>();
		}
		int currentSize = lineDtoList.size();
		for (int i = currentSize; i < AbstractSlipEditForm.INIT_LINE_SIZE; i++) {
			AbstractLineDto dto = this.createLineDto();
			dto.lineNo = Integer.toString(i + 1);
			lineDtoList.add((LINEDTOCLASS) dto);
		}
	}

	/**
	 * 明細リストで空の行を削除します.
	 */
	public void removeBlankLine() {
		if (lineDtoList != null && lineDtoList.size() > 0) {
			for (int i = lineDtoList.size() - 1; i >= 0; i--) {

				AbstractLineDto lineDto = (AbstractLineDto) lineDtoList.get(i);
				if (lineDto.isBlank()) {
					lineDtoList.remove(i);
				}
			}
		}
	}

	/**
	 * 明細行を作成します.
	 *
	 * @return　明細行オブジェクト
	 */
	public abstract AbstractLineDto createLineDto();

	/** 明細行リストをコピー先明細行リストにコピーします.
	 * @param dest　コピー先明細行リスト
	 */
	public void copyTo(List<LINEDTOCLASS> dest) {
		for (LINEDTOCLASS d : lineDtoList) {
			dest.add((LINEDTOCLASS) d);
		}
	}
	/**
	 * 明細行リストにコピー元明細行リストからコピーします.
	 * @param src　コピー元明細行リスト
	 */
	public void copyFrom(List<LINEDTOCLASS> src) {

		if (this.lineDtoList == src) {
			// 同じオブジェクトならコピーする必要なし
			return;
		}
		if (this.lineDtoList == null) {
			this.lineDtoList = new ArrayList<LINEDTOCLASS>();
		}

		for (LINEDTOCLASS d : src) {
			this.lineDtoList.add(d);
		}
	}
	/**
	 * 伝票番号を返します.
	 * @return 伝票番号
	 */
	public abstract String getKeyValue();
}
