/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service.rorder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.rorder.OnlineOrderWorkDto;
import jp.co.arkinfosys.dto.rorder.OnlineOrderWorkRelDto;
import jp.co.arkinfosys.entity.OnlineOrderWork;
import jp.co.arkinfosys.entity.join.OnlineOrderRelJoin;
import jp.co.arkinfosys.service.AbstractService;
import jp.co.arkinfosys.service.OnlineOrderService;
import jp.co.arkinfosys.service.exception.ServiceException;

import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * オンライン受注データ取込みサービスクラスです.
 * @author Ark Information Systems
 *
 */
public class ImportOnlineOrderService extends AbstractService<OnlineOrderWork> {

    @Resource
    private OnlineOrderService onlineOrderService;

	/**
	 * オンライン受注情報を作成します.
	 * @param userId ユーザID
	 * @param sortColumn ソート対象カラム
	 * @param sortOrderAsc 昇順か否か
	 * @return オンライン受注情報
	 */
	public List<OnlineOrderWorkRelDto> createSearchResultList(String userId, String sortColumn, boolean sortOrderAsc)
			throws ServiceException {
		try {
			// 検索結果を取得する
			List<OnlineOrderRelJoin> resultList =
				onlineOrderService.findRoWorkRel( null, null, sortColumn, sortOrderAsc);

			// Dtoリストを生成する
			List<OnlineOrderWorkRelDto> dtoList = new ArrayList<OnlineOrderWorkRelDto>();
			for(OnlineOrderRelJoin onlineOrderRel : resultList) {
				OnlineOrderWorkRelDto dto = Beans.createAndCopy(OnlineOrderWorkRelDto.class, onlineOrderRel).execute();
				dtoList.add(dto);
			}

			return dtoList;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * オンライン受注情報の検索結果件数を返します.
	 * @param userId ユーザID
	 * @return 検索結果件数
	 * @throws ServiceException
	 */
	public Integer getSearchResultCount(String userId) throws ServiceException {
		try {
			return onlineOrderService.findRoWorkRelCnt();
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * オンライン受注を登録します.
	 * @param onlineOrderWorkDto オンライン受注データ
	 * @return 登録件数
	 * @throws ServiceException
	 */
	public int insertWork(OnlineOrderWorkDto onlineOrderWorkDto) throws ServiceException {
		return onlineOrderService.insertWork(onlineOrderWorkDto);
	}

	/**
	 * オンライン受注をすべて削除します.
	 * @return 削除件数
	 * @throws ServiceException
	 */
	public int deleteWorksAll() throws ServiceException {
		return onlineOrderService.deleteWorksAll();
	}

	/**
	 * 受注伝票番号に紐付くオンライン受注をすべて削除します.
	 * @param roId 受注伝票番号
	 * @return 削除件数
	 * @throws ServiceException
	 */
	public int deleteWorksByRoId(String roId) throws ServiceException {
		return onlineOrderService.deleteWorksByRoId(roId);
	}

	/**
	 * String配列からオンライン受注DTOを作成します.
	 *
	 * @param values String配列
	 * @return OnlineOrderWorkDto オンライン受注DTO
	 * @throws ServiceException
	 */
	public OnlineOrderWorkDto createOnlineOrderWorkDto(String[] values) throws ServiceException {
		// 引数チェック
		if(values == null || values.length != Constants.ONLINE_ORDER_FILE.COLUMN_COUNT) {
			ServiceException se =
				new ServiceException(MessageResourcesUtil.getMessage("errors.system"));
			se.setStopOnError(true);
			throw se;
		}

		// オンライン受注
		OnlineOrderWorkDto dto = new OnlineOrderWorkDto();

		// 読み取り位置を設定する
		Integer pos = 0;

		// 値を設定
		dto.userId = userDto.userId;
		dto.onlineOrderId = values[pos];
		pos++;
		dto.onlineItemId = values[pos];
		pos++;
		dto.supplierDate = StringUtil.removeTimeZone(values[pos]);
		pos++;
		dto.paymentDate = StringUtil.removeTimeZone(values[pos]);
		pos++;
		dto.customerEmail = values[pos];
		pos++;
		dto.customerName = values[pos];
		pos++;
		dto.customerTel = values[pos];
		pos++;
		dto.sku = values[pos];
		pos++;
		dto.productName = values[pos];
		pos++;
		dto.quantity = values[pos];
		pos++;
		dto.currency = values[pos];
		pos++;
		dto.price = values[pos];
		pos++;
		dto.taxPrice = values[pos];
		pos++;
		dto.shippingPrice = values[pos];
		pos++;
		dto.shippingTax = values[pos];
		pos++;
		dto.shipServiceLevel = values[pos];
		pos++;
		dto.recipientName = values[pos];
		pos++;
		dto.address1 = values[pos];
		pos++;
		dto.address2 = values[pos];
		pos++;
		dto.address3 = values[pos];
		pos++;
		dto.city = values[pos];
		pos++;
		dto.state = values[pos];
		pos++;
		dto.zipCode = values[pos];
		pos++;
		dto.country = values[pos];
		pos++;
		dto.shipTel = values[pos];
		pos++;
		dto.deliveryStartDate = StringUtil.removeTimeZone(values[pos]);
		pos++;
		dto.deliveryEndDate = StringUtil.removeTimeZone(values[pos]);
		pos++;
		dto.deliveryTimeZone = values[pos];
		pos++;
		dto.deliveryInst = values[pos];
		pos++;

		return dto;
	}
}
