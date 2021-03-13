package com.eluon.pim.snmp.value;

public class PimAlramProfileVO {
	private int no;
	private int hwType;
	private int statType;
	private String statTarget;
	private int statCritCond;
	private int statMajCond;
	private int statMinCond;
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getHwType() {
		return hwType;
	}
	public void setHwType(int hwType) {
		this.hwType = hwType;
	}
	public int getStatType() {
		return statType;
	}
	public void setStatType(int statType) {
		this.statType = statType;
	}
	public String getStatTarget() {
		return statTarget;
	}
	public void setStatTarget(String statTarget) {
		this.statTarget = statTarget;
	}
	public int getStatCritCond() {
		return statCritCond;
	}
	public void setStatCritCond(int statCritCond) {
		this.statCritCond = statCritCond;
	}
	public int getStatMajCond() {
		return statMajCond;
	}
	public void setStatMajCond(int statMajCond) {
		this.statMajCond = statMajCond;
	}
	public int getStatMinCond() {
		return statMinCond;
	}
	public void setStatMinCond(int statMinCond) {
		this.statMinCond = statMinCond;
	}
	@Override
	public String toString() {
		return "PimAlramProfileVO [no=" + no + ", hwType=" + hwType + ", statType=" + statType + ", statTarget="
				+ statTarget + ", statCritCond=" + statCritCond + ", statMajCond=" + statMajCond + ", statMinCond="
				+ statMinCond + "]";
	}
}
