/*
 * Copyright 2009-2010 Ark Information Systems.
 */
package jp.co.arkinfosys.entity.join;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import jp.co.arkinfosys.entity.Warehouse;
import jp.co.arkinfosys.form.master.DeleteRackForm;
import jp.co.arkinfosys.form.master.EditRackForm;

/**
 * 倉庫マスタと区分データのリレーションエンティティクラスです.
 *
 * @author Ark Information Systems
 *
 */
@Entity
public class WarehouseJoin extends Warehouse  {
	private static final long serialVersionUID = 1L;
	
	public List<EditRackForm> editRackList = new ArrayList<EditRackForm>();

	public List<DeleteRackForm> rackCodesHist = new ArrayList<DeleteRackForm>();


}