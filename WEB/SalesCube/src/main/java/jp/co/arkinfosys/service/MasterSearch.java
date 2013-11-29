/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.util.List;
import java.util.Map;

import jp.co.arkinfosys.service.exception.ServiceException;

/**
 * マスタ検索インターフェースクラスです.
 * @param <ENTITY>
 *
 * @author Ark Information Systems
 */
public interface MasterSearch<ENTITY> {

	/**
	 * 検索条件に合致する件数を返します.
	 * @param conditions 検索条件のマップ
	 * @return 検索結果件数
	 * @throws ServiceException
	 */
	int countByCondition(Map<String, Object> conditions)
			throws ServiceException;

	/**
	 * 検索条件、ソート条件、取得件数および取得開始位置を指定して、マスタ情報のリストを返します.
	 * @param conditions 検索条件のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @param rowCount 取得件数
	 * @param offset 取得開始位置
	 * @return マスタ情報のリスト
	 * @throws ServiceException
	 */
	List<ENTITY> findByConditionLimit(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc, int rowCount, int offset)
			throws ServiceException;

	/**
	 * 検索条件およびソート条件を指定して、マスタ情報のリストを返します.
	 * @param conditions 検索条件のマップ
	 * @param sortColumn ソートカラム名
	 * @param sortOrderAsc 昇順にソートするか否か
	 * @return マスタ情報のリスト
	 * @throws ServiceException
	 */
	List<ENTITY> findByCondition(Map<String, Object> conditions,
			String sortColumn, boolean sortOrderAsc) throws ServiceException;

	/**
	 * IDを指定して、マスタ情報を取得します.
	 * @param id ID
	 * @return マスタ情報
	 * @throws ServiceException
	 */
	ENTITY findById(String id) throws ServiceException;

}
