/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.action.master;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.arkinfosys.common.CategoryTrns;
import jp.co.arkinfosys.common.Constants;
import jp.co.arkinfosys.common.StringUtil;
import jp.co.arkinfosys.dto.ExistsCheckStateDto;
import jp.co.arkinfosys.dto.master.ProductSetDto;
import jp.co.arkinfosys.entity.AuditInfo;
import jp.co.arkinfosys.entity.join.ProductJoin;
import jp.co.arkinfosys.entity.join.ProductSetJoin;
import jp.co.arkinfosys.form.master.AbstractEditForm;
import jp.co.arkinfosys.form.master.EditProductSetForm;
import jp.co.arkinfosys.s2extend.NumberConverter;
import jp.co.arkinfosys.service.AbstractMasterEditService;
import jp.co.arkinfosys.service.ProductService;
import jp.co.arkinfosys.service.ProductSetService;
import jp.co.arkinfosys.service.exception.ServiceException;
import jp.co.arkinfosys.service.exception.UnabledLockException;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 *
 * セット商品編集画面のアクションクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class EditProductSetAction extends
		AbstractEditAction<ProductSetDto, ProductSetJoin> {

	/**
	 * 画面遷移用のマッピングクラス
	 */
	public static class Mapping {
		public static final String INPUT = "editProductSet.jsp";
	}

	@ActionForm
	@Resource
	public EditProductSetForm editProductSetForm;

	@Resource
	private ProductSetService productSetService;

	@Resource
	private ProductService productService;

	/**
	 * 編集画面の初期表示を行います.<br>
	 * 処理実行後、{@link EditProductSetAction#getInputURL()}で取得したURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, input = EditProductSetAction.Mapping.INPUT, stopOnValidationError = false)
	public String edit() throws Exception {
		return super.doEdit(this.getKey());
	}

	/**
	 * 編集画面を開きます
	 * 
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = false, urlPattern = "edit/{setProductCode}")
	public String editOne() throws Exception {
		this.editProductSetForm.setProductCode = StringUtil.decodeSL(this.editProductSetForm.setProductCode);
		return super.doEdit(this.getKey());
	}
	
	/**
	 * 更新処理を行います.<br>
	 * 処理終了後、{@link Mapping#INPUT}で定義されたURIに遷移します.
	 * @return 画面遷移先のURI文字列
	 * @throws Exception
	 */
	@Execute(validator = true, validate = "validate", input = EditProductSetAction.Mapping.INPUT, stopOnValidationError = false)
	public String update() throws Exception {
		try {
			// 商品コードの存在チェック
			boolean existsCodeError = false;

			List<String> productCodeList = new ArrayList<String>();
			for (ProductSetDto dto : this.editProductSetForm.childProductList) {
				if (!StringUtil.hasLength(dto.productCode) || dto.deleted) {
					continue;
				}
				productCodeList.add(dto.productCode);
			}

			List<ExistsCheckStateDto> resultList = this.productService
					.existsProductCode(productCodeList);
			for (ExistsCheckStateDto dto : resultList) {
				if (!dto.exists) {
					super.messages.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.dataNotExist",
									MessageResourcesUtil
											.getMessage("labels.productCode"),
									dto.code));
					existsCodeError = true;
				}
			}
			if (existsCodeError) {
				// 存在しない
				ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
				return EditProductSetAction.Mapping.INPUT;
			}

			for (ProductSetDto dto : this.editProductSetForm.childProductList) {
				if (!StringUtil.hasLength(dto.productCode)) {
					continue;
				}
				if (dto.deleted) {
					// 削除時
					if(!dto.productCode.equals(dto.originalProductCode)) {
						dto.productCode = dto.originalProductCode;
					}
					this.productSetService.deleteRecord(dto);
				} else {
					if (StringUtil.hasLength(dto.updDatetm)) {
						if(!dto.productCode.equals(dto.originalProductCode)) {
							ProductSetDto temp = Beans.createAndCopy(ProductSetDto.class, dto).execute();
							temp.productCode = temp.originalProductCode;
							this.productSetService.deleteRecord(temp);
							this.productSetService.insertRecord(dto);
						}
						else {
							this.productSetService.updateRecord(dto);
						}
					} else {
						this.productSetService.insertRecord(dto);
					}
				}
			}

			super.init(this.getKey());

			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("infos.update"));
			ActionMessagesUtil.addMessages(super.httpRequest, super.messages);
		} catch (UnabledLockException e) {
			super.errorLog(e);
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage(e.getKey()));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);
		} catch (ServiceException e) {
			super.errorLog(e);
			throw e;
		} catch (Exception e) {
			super.errorLog(e);
			throw e;
		}

		return EditProductSetAction.Mapping.INPUT;
	}

	/**
	 *
	 * @param key セット商品コード
	 * @return {@link ProductJoin}
	 * @throws ServiceException
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#loadData(java.lang.String)
	 */
	@Override
	protected AuditInfo loadData(String key) throws ServiceException {
		this.editProductSetForm.reset();

		// セット商品情報を取得する
		ProductJoin product = this.productService.findById(key);
		if (product == null
				|| !CategoryTrns.PRODUCT_SET_TYPE_SET
						.equals(product.setTypeCategory)) {
			// セット商品が存在しない
			super.messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("errors.notExist", MessageResourcesUtil
							.getMessage("labels.setProduct")));
			ActionMessagesUtil.addErrors(super.httpRequest, super.messages);

			this.editProductSetForm.isUpdate = false;
		}

		// セット商品コード
		this.editProductSetForm.setProductCode = product.productCode;
		// セット商品名
		this.editProductSetForm.setProductName = product.productName;

		// 商品端数処理、数量小数桁
		this.editProductSetForm.productFractCategory = super.mineDto.productFractCategory;
		this.editProductSetForm.numDecAlignment = String
				.valueOf(super.mineDto.numDecAlignment);

		// 数量の小数桁処理
		NumberConverter conv = new NumberConverter(
				super.mineDto.productFractCategory,
				super.mineDto.numDecAlignment, false);

		List<ProductSetJoin> productSetJoinList = productSetService
				.findProductSetByProductCode(key);
		for (ProductSetJoin productSetJoin : productSetJoinList) {
			// 親商品の更新日を子商品の更新日に設定する
			product.updDatetm = productSetJoin.updDatetm;

			ProductSetDto dto = Beans.createAndCopy(ProductSetDto.class,
					productSetJoin).dateConverter(Constants.FORMAT.DATE)
					.timestampConverter(Constants.FORMAT.TIMESTAMP).converter(
							conv, "quantity").execute();
			dto.originalProductCode = dto.productCode;

			this.editProductSetForm.childProductList.add(dto);
		}

		this.editProductSetForm.childProductCount = this.editProductSetForm.childProductList
				.size();

		return product;
	}

	/**
	 *
	 * @return {@link EditProductSetForm}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getActionForm()
	 */
	@Override
	protected AbstractEditForm getActionForm() {
		return this.editProductSetForm;
	}

	/**
	 *
	 * @return {@link Mapping#INPUT}で定義されたURI
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getInputURL()
	 */
	@Override
	protected String getInputURL() {
		return Mapping.INPUT;
	}

	/**
	 *
	 * @return セット商品コード
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getKey()
	 */
	@Override
	protected String getKey() {
		return this.editProductSetForm.setProductCode;
	}

	/**
	 *
	 * @return {@link MENU_ID#MASTER_PRODUCT_SET}で定義されたID
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getMenuId()
	 */
	@Override
	protected String getMenuId() {
		return Constants.MENU_ID.MASTER_PRODUCT_SET;
	}

	/**
	 *
	 * @return {@link ProductSetDto}
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getDtoClass()
	 */
	@Override
	protected Class<ProductSetDto> getDtoClass() {
		return null;
	}

	/**
	 * 未実装です.
	 * @return null
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getService()
	 */
	@Override
	protected AbstractMasterEditService<ProductSetDto, ProductSetJoin> getService() {
		return null;
	}

	/**
	 * 未実装です.
	 * @return null
	 * @see jp.co.arkinfosys.action.master.AbstractEditAction#getAlreadyExistsErrorKey()
	 */
	@Override
	protected String getAlreadyExistsErrorKey() {
		return null;
	}
}
