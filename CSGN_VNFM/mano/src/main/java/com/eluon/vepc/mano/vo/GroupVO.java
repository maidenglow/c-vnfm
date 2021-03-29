package com.eluon.vepc.mano.vo;

import java.util.Date;

public class GroupVO extends BaseVO {
	private String group_no;
	private String group_name;
	private String img_id;
	private String img_name;
	private Date reg_dttm;
	private Date upd_dttm;	

	public String getGroup_no() {
		return group_no;
	}
	public void setGroup_no(String group_no) {
		this.group_no = group_no;
	}
	
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
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
