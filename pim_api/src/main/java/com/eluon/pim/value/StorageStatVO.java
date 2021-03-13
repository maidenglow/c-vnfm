package com.eluon.pim.value;

public class StorageStatVO {
	
	private int no;
	private int storageInfoNo;
	private int statType;
	private String statTime;
	private Object cpuUsageAvg;	
	private float memUsageAvg;
	private float hddUsageAvg;
	private Object cpuUsagePeak;
	private float memUsagePeak;
	private float hddUsagePeak;
	private Object nicUsage;	
	
	
	@Override
	public String toString() {
		return "StorageStatVO [no=" + no + ", storageInfoNo=" + storageInfoNo + ", statType=" + statType + ", statTime="
				+ statTime + ", cpuUsageAvg=" + cpuUsageAvg + ", memUsageAvg=" + memUsageAvg + ", hddUsageAvg="
				+ hddUsageAvg + ", cpuUsagePeak=" + cpuUsagePeak + ", memUsagePeak=" + memUsagePeak + ", hddUsagePeak="
				+ hddUsagePeak + ", nicUsage=" + nicUsage + "]";
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getStorageInfoNo() {
		return storageInfoNo;
	}
	public void setStorageInfoNo(int storageInfoNo) {
		this.storageInfoNo = storageInfoNo;
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
	public void setCpuUsageAvg(Object cpuUsageAvg) {
		this.cpuUsageAvg = cpuUsageAvg;
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
	public void setCpuUsagePeak(Object cpuUsagePeak) {
		this.cpuUsagePeak = cpuUsagePeak;
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
	public void setNicUsage(Object nicUsage) {
		this.nicUsage = nicUsage;
	}

}
