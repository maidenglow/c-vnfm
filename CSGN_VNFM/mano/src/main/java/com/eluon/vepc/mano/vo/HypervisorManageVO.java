package com.eluon.vepc.mano.vo;

import java.util.List;

import org.openstack4j.model.compute.ext.Hypervisor;

/**
 * System VO (SystemVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: SystemVO.java,v 1.0 2015/01/27 00:00:00 SimByungChul Express $
 */
public class HypervisorManageVO extends BaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private List<String> items;
	private List<String> selectItems;
	private String pass;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getItems() {
		return items;
	}
	public void setItems(List<String> items) {
		this.items = items;
	}
	public List<String> getSelectItems() {
		return selectItems;
	}
	public void setSelectItems(List<String> selectItems) {
		this.selectItems = selectItems;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}

}