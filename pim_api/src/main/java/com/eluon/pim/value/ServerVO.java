package com.eluon.pim.value;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

public class ServerVO {
	@JsonProperty("server_name")
	private String serverName;
	@JsonProperty("server_name")
	private String serverIP;
	@JsonProperty("server_port") @JsonInclude(Include.NON_EMPTY)
	private String serverPort;
	@JsonProperty("server_id")
	private int serverId;
	@JsonProperty("server_id")
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
	
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getServerIP() {
		return serverIP;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public String getServerPort() {
		return serverPort;
	}
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
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
	public void setCpuUsage(String cpuUsage) {
		this.cpuUsage = new Gson().fromJson(cpuUsage, Object.class);
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
	public void setNicInfo(String nicInfo) {
		this.nicInfo = new Gson().fromJson(nicInfo, Object.class);
	}
	@Override
	public String toString() {
		return "ServerVO [serverName=" + serverName + ", serverIP=" + serverIP + ", serverPort=" + serverPort
				+ ", serverId=" + serverId + ", status=" + status + ", statusDate=" + statusDate + ", regDate="
				+ regDate + ", cpuInfo=" + cpuInfo + ", cpuUsage=" + cpuUsage + ", memUsage=" + memUsage + ", memTotal="
				+ memTotal + ", hddUsage=" + hddUsage + ", hddTotal=" + hddTotal + ", nicInfo=" + nicInfo + "]";
	}
	
	
}
