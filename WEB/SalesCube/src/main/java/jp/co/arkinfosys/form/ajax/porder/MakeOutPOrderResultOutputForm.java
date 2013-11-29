/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.form.ajax.porder;

import java.io.Serializable;
import java.util.List;
/**
 * 発注書発行のアクションフォームクラスです.
 * @author Ark Information Systems
 *
 */
public class MakeOutPOrderResultOutputForm implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 伝票番号リスト
	 */
	public List<String> slipIdList;

}
