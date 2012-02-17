/*
 *  Copyright 2009-2010 Ark Information Systems.
 */

package jp.co.arkinfosys.action.payment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.form.payment.ClosePaymentForm;
import jp.co.arkinfosys.service.AptBalanceService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.payment.ClosePaymentService;

/**
 * 支払実績締処理画面のアクションです.
 * @author Ark Information Systems
 *
 */
public class ClosePaymentAction  extends CommonResources {
    /**
     * 画面遷移用のマッピングクラスです.
     */
    public static class Mapping {
    	public static final String INIT = "/payment/closePayment";
    	public static final String INPUT = "closePayment.jsp";
		public static final String CLOSE = "/payment/closePayment/close/";
		public static final String REOPEN = "/payment/closePayment/reopen/";
    }

    @ActionForm
    @Resource
    public ClosePaymentForm closePaymentForm;

    @Resource
    private AptBalanceService aptBalanceService;

    @Resource
    private ClosePaymentService closePaymentService;

	/**
	 * 初期表示処理を行います.
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception{
		
		init();

		return ClosePaymentAction.Mapping.INPUT;
	}

	/**
	 * 締処理を実行します.
	 * @throws Exception
	 */
	@Execute(validator = true,validate = "validate",input="closePayment.jsp")
	public String close() throws Exception{
		Date closeDate = DateFormat.getDateInstance().parse(closePaymentForm.closeDate);

		try{
			
			closePaymentService.closePayment(closeDate);
		}catch(ServiceException se){
			super.errorLog(se);

			throw se;
		}
		
		super.messages.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("infos.payment.close"));
		ActionMessagesUtil.addMessages(super.httpSession, super.messages);


		
		init();

		return ClosePaymentAction.Mapping.INPUT;
	}

	/**
	 * 締解除処理を実行します.
	 * @return 遷移先URI
	 * @throws Exception
	 */
	@Execute(validator = true,input="closePayment.jsp")
	public String reopen() throws Exception{
		
		if("".equals(closePaymentForm.latestAptCutoffDate)){
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.closePayment.noData"));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
			return ClosePaymentAction.Mapping.INPUT;
		}

		try{
			
			closePaymentService.reopenPayment();
		}catch(ServiceException se){
			super.errorLog(se);

			throw se;
		}

		
		super.messages.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("infos.payment.reopen"));
		ActionMessagesUtil.addMessages(super.httpSession, super.messages);

		
		init();

		return ClosePaymentAction.Mapping.INPUT;
	}


	/**
	 * 初期化処理を行います.
	 * @throws Exception
	 */
	public void init() throws Exception{
		
		

		
		Date cutoffDate = aptBalanceService.findLatestAptBalance();

		
		if(cutoffDate!=null){
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORMAT.DATE);
			closePaymentForm.latestAptCutoffDate = sdf.format(cutoffDate);
		}else{
			
			closePaymentForm.latestAptCutoffDate = "";
		}
	}


	/**
	 * 締年月日と最終締日の論理チェックを行います.
	 * @return アクションメッセージ
	 * @throws Exception
	 */
	public ActionMessages validate() throws Exception {
		ActionMessages errors = new ActionMessages();

		/**
		 * 支払締日付のフォーマットと必須チェックはアノテーションで行う
		 *
 		 */

		Date closeDate = DateFormat.getDateInstance().parse(closePaymentForm.closeDate);

		if(StringUtil.hasLength(closePaymentForm.latestAptCutoffDate)){
			Date latestAptCutoffDate = DateFormat.getDateInstance().parse(closePaymentForm.latestAptCutoffDate);

			
			if(closeDate.before(latestAptCutoffDate) ||
					closeDate.compareTo(latestAptCutoffDate) == 0){
				errors.add(ActionMessages.GLOBAL_MESSAGE,
						new ActionMessage("errors.closePayment.closeDate"));
			}

		}

		return errors;
	}
}
