package com.eluon.pim.value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class StorageVO {
	@JsonProperty("storage_no")
	private int storageNo;
	@JsonProperty("storage_name")
	private String storageName;
	@JsonProperty("storage_ip")
	private String storageIP;
	@JsonProperty("storage_port") @JsonInclude(Include.NON_EMPTY)
	private String storagePort;
	@JsonProperty("status")
	private int status;
	@JsonProperty("status_date")
	private String statusDate;
	@JsonProperty("reg_date")
	private String regDate;
	@JsonProperty("cpu_info")
	private String cpuInfo;
	@JsonProperty("cpu_usage")
	private Object cpuUsage;
	@JsonProperty("mem_usage")
	private float memUsage;
	@JsonProperty("mem_total")
	private float memTotal;
	@JsonProperty("hdd_usage")
	private float hddUsage;
	@JsonProperty("hdd_total")
	private float hddTotal;
	@JsonProperty("nic_info")
	private Object nicInfo;
	@JsonProperty("nic_usage")
	private Object nicUsage;
	
	public int getStorageNo() {
		return storageNo;
	}
	public void setStorageNo(int storageNo) {
		this.storageNo = storageNo;
	}
	public String getStorageName() {
		return storageName;
	}
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
	public String getStorageIP() {
		return storageIP;
	}
	public void setStorageIP(String storageIP) {
		this.storageIP = storageIP;
	}
	public String getStoragePort() {
		return storagePort;
	}
	public void setStoragePort(String storagePort) {
		this.storagePort = storagePort;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getCpuInfo() {
		return cpuInfo;
	}
	public void setCpuInfo(String cpuInfo) {
		this.cpuInfo = cpuInfo;
	}
	public Object getCpuUsage() {
		return cpuUsage;
	}
	public void setCpuUsage(Object cpuUsage) {
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
	public Object getNicInfo() {
		return nicInfo;
	}
	public void setNicInfo(Object nicInfo) {
		this.nicInfo = nicInfo;
	}
	public Object getNicUsage() {
		return nicUsage;
	}
	public void setNicUsage(Object nicUsage) {
		this.nicUsage = nicUsage;
	}


	@Override
	public String toString() {
		return "StorageVO [storageNo=" + storageNo + ", storageName=" + storageName + ", storageIP=" + storageIP
				+ ", status=" + status + ", statusDate=" + statusDate + ", regDate=" + regDate + ", cpuInfo=" + cpuInfo
				+ ", cpuUsage=" + cpuUsage + ", memUsage=" + memUsage + ", memTotal=" + memTotal + ", hddUsage="
				+ hddUsage + ", hddTotal=" + hddTotal + ", nicInfo=" + nicInfo + ", nicUsage=" + nicUsage + "]";
	}
}
