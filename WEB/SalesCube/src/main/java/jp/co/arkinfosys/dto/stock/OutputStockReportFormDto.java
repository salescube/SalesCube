/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.stock;

import java.io.Serializable;
import java.util.List;


import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 在庫残高表画面情報を管理するDTOクラスです.
 * @author Ark Information Systems
 *
 */
@Component(instance = InstanceType.SESSION)
public class OutputStockReportFormDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 表出力対象期間（月単位）
     */
    public String targetYm;

    /**
     * 検索結果件数
     */
    public Integer searchResultCount;

	/**
	 * 検索結果リスト
	 */
	public List<ProductStockJoinDto> searchResultList;
}
