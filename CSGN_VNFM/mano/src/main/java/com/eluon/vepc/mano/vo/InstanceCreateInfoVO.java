package com.eluon.vepc.mano.vo;

import java.util.Date;

public class InstanceCreateInfoVO extends BaseVO {
	private String create_no;
	private String group_no;
	private String img_id;
	private String img_name;
	private String flavor_id;
	private String flavor_name;
	private String network_id;
	private String network_name;
	private String keypair_name;
	private Date reg_dttm;
	private Date upd_dttm;
	
	public InstanceCreateInfoVO() {
		super();
	}

	public String getCreate_no() {
		return create_no;
	}

	public void setGroup_no(String group_no) {
		this.group_no = group_no;
	}
	
	public String getGroup_no() {
		return group_no;
	}

	public void setCreate_no(String create_no) {
		this.create_no = create_no;
	}

	public String getImg_id() {
		return img_id;
	}

	public void setImg_id(String img_id) {
		this.img_id = img_id;
	}

	public String getImg_name() {
		return img_name;
	}

	public void setImg_name(String img_name) {
		this.img_name = img_name;
	}

	public String getFlavor_id() {
		return flavor_id;
	}

	public void setFlavor_id(String flavor_id) {
		this.flavor_id = flavor_id;
	}

	public String getFlavor_name() {
		return flavor_name;
	}

	public void setFlavor_name(String flavor_name) {
		this.flavor_name = flavor_name;
	}

	public String getNetwork_id() {
		return network_id;
	}

	public void setNetwork_id(String network_id) {
		this.network_id = network_id;
	}

	public String getNetwork_name() {
		return network_name;
	}

	public void setNetwork_name(String network_name) {
		this.network_name = network_name;
	}

	public String getKeypair_name() {
		return keypair_name;
	}

	public void setKeypair_name(String keypair_name) {
		this.keypair_name = keypair_name;
	}
	
	public Date getReg_dttm() {
		return reg_dttm;
	}

	public void setReg_dttm(Date reg_dttm) {
		this.reg_dttm = reg_dttm;
	}

	public Date getUpd_dttm() {
		return upd_dttm;
	}

	public void setUpd_dttm(Date upd_dttm) {
		this.upd_dttm = upd_dttm;
	}
	
	
}
