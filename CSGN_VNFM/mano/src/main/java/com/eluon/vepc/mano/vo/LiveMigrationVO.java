package com.eluon.vepc.mano.vo;

import java.util.List;

public class LiveMigrationVO {

	private String hypervisor;
	private String id;
	private String name;
	private List<String> addressList;
	public String getName() {
		return name;
	}

	public void setHypervisor(String hypervisor) {
		this.hypervisor = hypervisor;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<String> getAddressList() {
		return addressList;
	}
	
	public void setAddressList(List<String> addressList) {
		this.addressList = addressList;
	}
}
