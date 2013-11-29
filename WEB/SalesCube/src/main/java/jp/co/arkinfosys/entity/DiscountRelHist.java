/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 割引マスタ履歴と割引関連データ履歴のリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class DiscountRelHist implements Serializable {

		private static final long serialVersionUID = 1L;
		@Transient

		public static final String TABLE_NAME = "DISCOUNT_REL_HIST";

		@Id
		@GeneratedValue
		@Column(name = "HIST_ID")
		public Integer histId;
		/**
		 *
		 */
		@Column(name = "ACTION_TYPE")
		public String actionType;
		/**
		 *
		*/
		public String productCode;
		public String discountIdCdx;
		public String discountNm;

		public Timestamp updDatetm;

}
