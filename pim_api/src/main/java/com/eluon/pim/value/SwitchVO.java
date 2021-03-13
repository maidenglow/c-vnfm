package com.eluon.pim.value;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SwitchVO {

	private int switchNo;
	
	@JsonProperty("switch_name")
	private String switchName;
	@JsonProperty("switch_ip")
	private String switchIP;
	@JsonProperty("switch_port") @JsonInclude(Include.NON_EMPTY)
	private String switchPort;
	@JsonProperty("status")
	private int status;
	@JsonProperty("status_time")
	private String statusTime;
	@JsonProperty("reg_time")
	private String regTime;
	@JsonProperty("nic_info")
	private Object nicInfo;
	
	public int getSwitchNo() {
		return switchNo;
	}
	public void setSwitchNo(int switchNo) {
		this.switchNo = switchNo;
	}
	public String getSwitchName() {
		return switchName;
	}
	public void setSwitchName(String switchName) {
		this.switchName = switchName;
	}
	public String getSwitchIP() {
		return switchIP;
	}
	public void setSwitchIP(String switchIP) {
		this.switchIP = switchIP;
	}
	public String getSwitchPort() {
		return switchPort;
	}
	public void setSwitchPort(String switchPort) {
		this.switchPort = switchPort;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusTime() {
		return statusTime;
	}
	public void setStatusTime(String statusTime) {
		this.statusTime = statusTime;
	}
	public String getRegTime() {
		return regTime;
	}
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}
	public Object getNicInfo() {
		return nicInfo;
	}
	public void setNicInfo(Object nicInfo) {
		this.nicInfo = nicInfo;
	}

	@Override
	public String toString() {
		return "SwitchVO [switchNo=" + switchNo + ", switchName=" + switchName + ", switchIP=" + switchIP + ", status="
				+ status + ", statusTime=" + statusTime + ", regTime=" + regTime + ", nicInfo=" + nicInfo + "]";
	}

}
