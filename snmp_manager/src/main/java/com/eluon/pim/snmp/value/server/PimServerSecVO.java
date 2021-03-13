package com.eluon.pim.snmp.value.server;

import java.util.Date;

public class PimServerSecVO {
	private int serverId;
	private String cpuUsage;
	private float memUsage;
	private float memTotal;
	private float hddUsage;
	private float hddTotal;
	private String nicUsage;
	private Date statusDate;
	
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public String getCpuUsage() {
		return cpuUsage;
	}
	public void setCpuUsage(String cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	public float getMemUsage() {
		return memUsage;
	}
	public void setMemUsage(float memUsage) {
		this.memUsage = memUsage;
	}
	public float getMemTotal() {
		return memTotal;
	}
	public void setMemTotal(float memTotal) {
		this.memTotal = memTotal;
	}
	public float getHddUsage() {
		return hddUsage;
	}
	public void setHddUsage(float hddUsage) {
		this.hddUsage = hddUsage;
	}
	public float getHddTotal() {
		return hddTotal;
	}
	public void setHddTotal(float hddTotal) {
		this.hddTotal = hddTotal;
	}
	public String getNicUsage() {
		return nicUsage;
	}
	public void setNicUsage(String nicUsage) {
		this.nicUsage = nicUsage;
	}
	public Date getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	@Override
	public String toString() {
		return "PimServer10sVO [serverId=" + serverId + ", cpuUsage=" + cpuUsage + ", memUsage=" + memUsage
				+ ", memTotal=" + memTotal + ", hddUsage=" + hddUsage + ", hddTotal=" + hddTotal + ", nicUsage="
				+ nicUsage + ", statusDate=" + statusDate + "]";
	}
}
