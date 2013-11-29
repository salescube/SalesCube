/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.setting;

import java.io.Serializable;
import java.util.List;

/**
 * メニュー情報を管理するDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class MenuDto implements Serializable {

    private static final long serialVersionUID = 1L;

    public String menuId;

    public String caption;

    public String description;

    public String url;

    public String parentId;

    public Integer seq;

    public String validType;

    public String validFlag;

    public String fontColor;

    public String bgColor;

    public MenuDto parent;

    public List<MenuDto> subMenuDtoList;
}
