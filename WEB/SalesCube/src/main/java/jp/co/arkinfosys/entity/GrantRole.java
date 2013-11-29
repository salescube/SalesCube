/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * ロール付与マスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class GrantRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * テーブル名
     */
    public static final String TABLE_NAME = "GRANT_ROLE";

    @Id
    public String userId;

    @Id
    public String roleId;

    public String creFunc;

    public Timestamp creDatetm;

    public String creUser;

    public String updFunc;

    public Timestamp updDatetm;

    public String updUser;

}