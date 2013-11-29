/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto;

import java.io.Serializable;

/**
 * 年月度を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class YmDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 年度 */
    public Integer annual;

    /** 月度 */
    public Integer monthly;

    /** 年月度 */
    public Integer ym;
}