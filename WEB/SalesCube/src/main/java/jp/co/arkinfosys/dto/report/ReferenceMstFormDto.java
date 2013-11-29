/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.report;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * マスタリスト画面の情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Component(instance = InstanceType.SESSION)
public class ReferenceMstFormDto implements Serializable  {
	    private static final long serialVersionUID = 1L;

		/**
		 * 検索対象
		 */
	    public String outputTarget;

	    ////////// 顧客マスタ
	    /**
	     * 顧客コードFrom
	     */
	    public String customerCodeFrom9;

	    /**
	     * 顧客コードTo
	     */
	    public String customerCodeTo9;

	    /**
	     * 登録日From
	     */
	    public String creDateFrom9;

	    /**
	     * 登録日To
	     */
	    public String creDateTo9;

}
