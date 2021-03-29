package com.eluon.vepc.mano.vo;

import java.util.Date;

public class ScaleInVO extends BaseVO {
	private String in_no;
	private String group_no;
	private int in_cpu;
	private int in_memory;
	private int in_cps;
	private Date reg_dttm;
	private Date upd_dttm;
	
	public String getIn_no() {
		return in_no;
	}
	public void setIn_no(String in_no) {
		this.in_no = in_no;
	}
	public String getGroup_no() {
		return group_no;
	}
	public void setGroup_no(String group_no) {
		this.group_no = group_no;
	}
	public int getIn_cpu() {
		return in_cpu;
	}
	public void setIn_cpu(int in_cpu) {
		this.in_cpu = in_cpu;
	}
	public int getIn_memory() {
		return in_memory;
	}
	public void setIn_memory(int in_memory) {
		this.in_memory = in_memory;
	}
	public int getIn_cps() {
		return in_cps;
	}
	public void setIn_cps(int in_cps) {
		this.in_cps = in_cps;
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
