/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 社員マスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * テーブル名
     */
    public static final String TABLE_NAME = "USER_MST";

    @Id
    public String userId;

    public String nameKnj;

    public String nameKana;

    public String deptId;

    public String email;

    public String password;

    public Date expireDate;

    public String creFunc;

    public Timestamp creDatetm;

    public String creUser;

    public String updFunc;

    public Timestamp updDatetm;

    public String updUser;

    public Integer failCount;

    public String lockflg;

    //public Timestamp lockDatetm;
    public String lockDatetm;

    public String tokenKey;

    public String tokenIv;

}