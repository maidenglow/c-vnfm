package com.eluon.pim.snmp.value.switches;

public class PimSwitchSecVO {


	private int no;
	private String statusTime;
	private String nicUsage;
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getStatusTime() {
		return statusTime;
	}
	public void setStatusTime(String statusTime) {
		this.statusTime = statusTime;
	}
	public String getNicUsage() {
		return nicUsage;
	}
	public void setNicUsage(String nicUsage) {
		this.nicUsage = nicUsage;
	}
	@Override
	public String toString() {
		return "PimSwitchSecVO [no=" + no + ", statusTime=" + statusTime + ", nicUsage=" + nicUsage + "]";
	}
}
