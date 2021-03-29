package com.eluon.vepc.mano.vo;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class RealTimeScaleVO extends BaseVO {
	private String history_no;
	private String instance_no;
	
	@JsonProperty("instance_id")
	private String instance_id;
	@JsonProperty("instance_name")
	private String instance_name;
	@JsonProperty("instance_ip")
	private String instance_ip;
	@JsonProperty("cpu")
	private int cpu;
	@JsonProperty("memory")
	private int memory;
	@JsonProperty("cps")
	private int cps;
	
	private String scale_info;
	private Date reg_dttm;
	private Date upd_dttm;
	
	public String getHistory_no() {
		return history_no;
	}
	public void setHistory_no(String history_no) {
		this.history_no = history_no;
	}
	
	public String getInstance_no() {
		return instance_no;
	}
	public void setInstance_no(String instance_no) {
		this.instance_no = instance_no;
	}
	
	public String getInstance_id() {
		return instance_id;
	}
	public void setInstance_id(String instance_id) {
		this.instance_id = instance_id;
	}
	
	public String getInstance_name() {
		return instance_name;
	}
	public void setInstance_name(String instance_name) {
		this.instance_name = instance_name;
	}
	
	public String getInstance_ip() {
		return instance_ip;
	}
	public void setInstance_ip(String instance_ip) {
		this.instance_ip = instance_ip;
	}
	
	public int getCpu() {
		return cpu;
	}
	public void setCpu(int cpu) {
		this.cpu = cpu;
	}
	
	public int getMemory() {
		return memory;
	}
	public void setMemory(int memory) {
		this.memory = memory;
	}
	
	public int getCps() {
		return cps;
	}
	public void setCps(int cps) {
		this.cps = cps;
	}
	
	public String getScale_info() {
		return scale_info;
	}
	public void setScale_info(String scale_info) {
		this.scale_info = scale_info;
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
