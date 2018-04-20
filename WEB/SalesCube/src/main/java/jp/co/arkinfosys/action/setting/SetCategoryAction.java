package jp.co.arkinfosys.action.setting;


import javax.annotation.Resource;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

import jp.co.arkinfosys.action.CommonResources;
import jp.co.arkinfosys.form.setting.SetCategoryTrnForm;
import jp.co.arkinfosys.service.CategoryService;


/**
 * 区分データ権限設定画面のアクションクラスです.
 * @author Ark Information Systems
 *
 */
public class SetCategoryAction extends CommonResources {
	/**
	 * 画面遷移用のマッピングクラスです.
	 * @author Ark Information Systems
	 *
	 */
	public static class Mapping {
		public static final String INPUT = "inputCategory.jsp";
	}

	@ActionForm
	@Resource
	private SetCategoryTrnForm setCategoryTrnForm;

	@Resource
	private CategoryService categoryService;


	/**
	 * 初期表示を行います.
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String index() throws Exception {

		init();

		return Mapping.INPUT;
	}


	/**
	 * 更新処理を行います.<br>
	 * 更新完了時および何かしらの問題があった場合に、画面にメッセージを表示します.<br>
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String update() throws Exception {
		// 更新
		this.categoryService.updateCategoryMst(this.setCategoryTrnForm.categoryDtoList);

		// メッセージ表示
		super.messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"infos.update"));
		ActionMessagesUtil.addMessages(super.httpRequest, super.messages);

		init();

		return Mapping.INPUT;

	}

	/**
	 * リセット処理を行います.<br>
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false)
	public String reset() throws Exception {
		init();

		return Mapping.INPUT;
	}


	/**
	 * 初期化処理を行います.
	 * @throws Exception
	 */
	private void init() throws Exception {
		// カテゴリマスタの全情報を取得してformに設定
		this.setCategoryTrnForm.categoryDtoList = categoryService.findAllCategoryMst();
	}
}
