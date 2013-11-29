/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.setting;

import java.io.Serializable;

/**
 * お知らせ情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class NewsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public String description;

    public String creDatetm;

    public String updDatetm;

}
