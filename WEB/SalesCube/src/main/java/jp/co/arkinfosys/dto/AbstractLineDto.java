/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto;

/**
 * 明細行情報を管理するDTOの基底クラスです.
 *
 * @author Ark Information Systems
 *
 */
public abstract class AbstractLineDto {

	/** 行番号 */
	public String lineNo;

	/**
	 * 空白行の判定メソッドです.
	 * @return 空白行か否か
	 */
	public abstract boolean isBlank();
}
