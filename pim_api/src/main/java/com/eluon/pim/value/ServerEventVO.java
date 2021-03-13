package com.eluon.pim.value;

public class ServerEventVO {
	
	private int no;
	private int targetNo;
	private String eventTime;
	private int profileNo;
	private String description;
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getServerId() {
		return targetNo;
	}
	public void setServerId(int serverInfoId) {
		this.targetNo = serverInfoId;
	}
	public String getEventTime() {
		return eventTime;
	}
	public void setEventTime(String eventTime) {
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

	@Override
	public String toString() {
		return "ServerEventVO [no=" + no + ", targetNo=" + targetNo + ", eventTime=" + eventTime
				+ ", profileNo=" + profileNo + ", description=" + description + "]";
	}
}
