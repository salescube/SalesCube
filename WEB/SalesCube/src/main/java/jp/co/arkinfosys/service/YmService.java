/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.YmDto;
import jp.co.arkinfosys.service.exception.ServiceException;

/**
 * 年月度サービスクラスです.
 * @author Ark Information Systems
 *
 */
public class YmService extends AbstractService {

	/**
	 * 年度、月度、年月度を返します.
	 * @param inputDate 日付(String)
	 * @return 年度、月度、年月度（YmDto）
	 * @throws ServiceException
	 */
	public YmDto getYm(String inputDate) throws ServiceException {
		try {
			if(!StringUtil.hasLength(inputDate)) {
				return null;
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.FORMAT.DATE);
			Date date = dateFormat.parse(inputDate);

			return getYm(date);
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}

	/**
	 * 年度、月度、年月度を返します.
	 * @param inputDate 日付(java.util.Date)
	 * @return 年度、月度、年月度（YmDto）
	 * @throws ServiceException
	 */
	public YmDto getYm(Date inputDate) throws ServiceException {
		try {
			if(inputDate == null) {
				return null;
			}
			YmDto ymDto = new YmDto();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(inputDate);

			// 年度
			ymDto.annual = calendar.get(Calendar.YEAR);
			// 月度
			ymDto.monthly = calendar.get(Calendar.MONTH) + 1;
			// 年月度
			ymDto.ym = ymDto.annual * 100 + ymDto.monthly;

			return ymDto;
		} catch (Exception e) {
			ServiceException se = new ServiceException(e);
			se.setStopOnError(true);
			throw se;
		}
	}
}
