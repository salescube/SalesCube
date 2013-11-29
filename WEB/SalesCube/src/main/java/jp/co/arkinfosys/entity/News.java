/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;

/**
 * お知らせマスタのエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class News implements Serializable {

    private static final long serialVersionUID = 1L;

    public String description;

    public String creFunc;

    public Timestamp creDatetm;

    public String creUser;

    public String updFunc;

    public Timestamp updDatetm;

    public String updUser;
}