/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.dto.deposit;

/**
 * 配送業者入金データ取込画面の結果リスト行ソート用のDTOクラスです.
 *
 * @author Ark Information Systems
 *
 */
public class ImportDeliveryDepositResultSortDto extends ImportDeliveryDepositResultDto implements Comparable<Object>{
    private static final long serialVersionUID = 1L;

    public String key;
    public String sortColumn;
    public boolean sortOrderAsc;

	/**
	 * ソート順を考慮して行を比較します.
	 * @param object　比較対象行
	 * @return =0:同じ　<0：比較対象行が前　>0:比較対象行が後ろ
	 */
	public int compareTo(Object object) {
		ImportDeliveryDepositResultSortDto operand = (ImportDeliveryDepositResultSortDto) object;
		if( "salesSlipId".equals(this.sortColumn) || "depositSlipId".equals(this.sortColumn) ||
			"productPrice".equals(this.sortColumn) || "salesMoney".equals(this.sortColumn)	){

			int thisValue = 0;
			if( this.key != null && this.key.length() > 0) {
				Double dPrice = Double.parseDouble(this.key);
				thisValue  =  dPrice.intValue();
			}

			int opValue = 0;
			if( operand.key != null && operand.key.length() > 0) {
				Double dPrice = Double.parseDouble(operand.key);
				opValue = dPrice.intValue();
			}

			if( this.sortOrderAsc ){
				return thisValue - opValue;
			}
			return opValue - thisValue;
		}
		if( this.sortOrderAsc ){
			return this.key.compareTo(operand.key);
		}
		return operand.key.compareTo(this.key);
	}
}

