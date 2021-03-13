package com.eluon.pim.snmp.value.switches;

public class PimSwitchAlarmVO {
	private int no;
	private int targetNo;
	private int status;
	private int grade;
	private String alarmTime;
	private String clearTime;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public String getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}
	public String getClearTime() {
		return clearTime;
	}
	public void setClearTime(String clearTime) {
		this.clearTime = clearTime;
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
		return "PimSwitchAlarmVO [no=" + no + ", targetNo=" + targetNo + ", status=" + status + ", grade=" + grade
				+ ", alarmTime=" + alarmTime + ", clearTime=" + clearTime + ", profileNo=" + profileNo
				+ ", description=" + description + "]";
	}
	
	
}
