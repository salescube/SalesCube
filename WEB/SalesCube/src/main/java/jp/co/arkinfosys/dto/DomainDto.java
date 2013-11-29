/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * ドメイン情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Component(instance = InstanceType.SESSION)
public class DomainDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public String domainId;

}
