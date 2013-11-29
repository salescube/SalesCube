/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto;

import java.io.Serializable;

/**
* 郵便番号を管理するDTOクラスです.
*
* @author Ark Information Systems
*
*/
public class ZipDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public String zipId;

	public String zipCode;

	public String zipAddress1;

	public String zipAddress2;

	public String creFunc;

	public String creDatetm;

	public String creUser;

	public String updFunc;

	public String updDatetm;

	public String updUser;
}
