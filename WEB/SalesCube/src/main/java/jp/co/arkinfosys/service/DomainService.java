/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.Map;

import jp.co.arkinfosys.entity.Domain;
import jp.co.arkinfosys.service.exception.ServiceException;

/**
 * ドメインサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class DomainService extends AbstractService<Domain> {

	/**
	 * パラメータマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Param {
		public static final String DOMAIN_ID = "domainId";
	}

	/**
	 * ユーザIDを指定して、ドメイン情報を返します.
	 * @param userId ユーザID
	 * @return ドメイン情報
	 * @throws ServiceException
	 */
	public Domain findById(String userId) throws ServiceException {
		try {
			// SQLパラメータを構築する
			Map<String, Object> param = super.createSqlParam();
			param.put(DomainService.Param.DOMAIN_ID, userId);

			return this.selectBySqlFile(Domain.class, "domain/FindById.sql",
					param).getSingleResult();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
