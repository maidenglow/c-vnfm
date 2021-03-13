package com.eluon.pim.snmp.value.server;

import java.util.Date;

public class PimServerEventVO {
	
	private int no;
	private int serverId;
	private Date eventTime;
	private int profileNo;
	private String description;
	private String serverIp;
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverInfoId) {
		this.serverId = serverInfoId;
	}
	public Date getEventTime() {
		return eventTime;
	}
	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}
	public int getProfileNo() {
		return profileNo;
	}
	public void setProfileNo(int profileNo) {
		this.profileNo = profileNo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	@Override
	public String toString() {
		return "PimServerAlramVO [no=" + no + ", serverId=" + serverId + ", eventTime=" + eventTime
				+ ", profileNo=" + profileNo + ", description=" + description + "]";
	}
}
