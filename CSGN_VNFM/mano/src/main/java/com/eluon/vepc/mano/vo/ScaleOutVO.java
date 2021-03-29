package com.eluon.vepc.mano.vo;

import java.util.Date;

public class ScaleOutVO extends BaseVO {
	private String out_no;
	private String group_no;
	private int out_cpu;
	private int out_memory;
	private int out_cps;
	private Date reg_dttm;
	private Date upd_dttm;
	public String getOut_no() {
		return out_no;
	}
	public void setOut_no(String out_no) {
		this.out_no = out_no;
	}
	public String getGroup_no() {
		return group_no;
	}
	public void setGroup_no(String group_no) {
		this.group_no = group_no;
	}
	public int getOut_cpu() {
		return out_cpu;
	}
	public void setOut_cpu(int out_cpu) {
		this.out_cpu = out_cpu;
	}
	public int getOut_memory() {
		return out_memory;
	}
	public void setOut_memory(int out_memory) {
		this.out_memory = out_memory;
	}
	public int getOut_cps() {
		return out_cps;
	}
	public void setOut_cps(int out_cps) {
		this.out_cps = out_cps;
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
