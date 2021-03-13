package com.eluon.pim.value;

import com.google.gson.Gson;

public class ServerStatVO {
	private int no;
	private int serverId;
	private int statType;
	private String statTime;
	private Object cpuUsageAvg;	
	private float memUsageAvg;
	private float hddUsageAvg;
	private Object cpuUsagePeak;
	private float memUsagePeak;
	private float hddUsagePeak;
	private Object nicUsage;

	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public int getStatType() {
		return statType;
	}
	public void setStatType(int statType) {
		this.statType = statType;
	}
	public String getStatTime() {
		return statTime;
	}
	public void setStatTime(String statTime) {
		this.statTime = statTime;
	}
	public Object getCpuUsageAvg() {
		return cpuUsageAvg;
	}
	public void setCpuUsageAvg(String cpuUsageAvg) {
		this.cpuUsageAvg = new Gson().fromJson(cpuUsageAvg, Object.class);
	}
	public float getMemUsageAvg() {
		return memUsageAvg;
	}
	public void setMemUsageAvg(float memUsageAvg) {
		this.memUsageAvg = memUsageAvg;
	}
	public float getHddUsageAvg() {
		return hddUsageAvg;
	}
	public void setHddUsageAvg(float hddUsageAvg) {
		this.hddUsageAvg = hddUsageAvg;
	}
	public Object getCpuUsagePeak() {
		return cpuUsagePeak;
	}
	public void setCpuUsagePeak(String cpuUsagePeak) {
		this.cpuUsagePeak =  new Gson().fromJson(cpuUsagePeak, Object.class); 
	}
	public float getMemUsagePeak() {
		return memUsagePeak;
	}
	public void setMemUsagePeak(float memUsagePeak) {
		this.memUsagePeak = memUsagePeak;
	}
	public float getHddUsagePeak() {
		return hddUsagePeak;
	}
	public void setHddUsagePeak(float hddUsagePeak) {
		this.hddUsagePeak = hddUsagePeak;
	}
	public Object getNicUsage() {
		return nicUsage;
	}
	public void setNicUsage(String nicUsage) {
		this.nicUsage =  new Gson().fromJson(nicUsage, Object.class);
	}
	@Override
	public String toString() {
		return "PimServerStatVO [no=" + no + ", serverId=" + serverId + ", statType=" + statType + ", statTime="
				+ statTime + ", cpuUsageAvg=" + cpuUsageAvg + ", memUsageAvg=" + memUsageAvg + ", hddUsageAvg="
				+ hddUsageAvg + ", cpuUsagePeak=" + cpuUsagePeak + ", memUsagePeak=" + memUsagePeak + ", hddUsagePeak="
				+ hddUsagePeak + ", nicUsage=" + nicUsage + "]";
	}
	
	
}
