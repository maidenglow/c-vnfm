package com.eluon.pim.value;

public class SwitchEventVO {
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
	public int getTargetNo() {
		return targetNo;
	}
	public void setTargetNo(int targetNo) {
		this.targetNo = targetNo;
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
		return "SwitchEventVO [no=" + no + ", targetNo=" + targetNo + ", eventTime=" + eventTime + ", profileNo="
				+ profileNo + ", description=" + description + "]";
	}
}
