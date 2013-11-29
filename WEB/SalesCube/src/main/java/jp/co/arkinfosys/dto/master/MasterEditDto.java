/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.master;

/**
 * マスタのキー情報を管理するインターフェースクラスです.
 *
 * @author Ark Information Systems
 *
 */

public interface MasterEditDto {

	/**
	 * キー値を返す抽象メソッドです.
	 * @return　キー値の文字配列
	 */
	String[] getKeys();

}
