package com.eluon.vepc.mano.vo;

import java.util.Date;

/**
 * Alarm VO (AlarmVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AlarmVO.java,v 1.0 2015/01/27 00:00:00 SimByungChul Express $
 */
public class AlarmVO extends PaginationVO {
	private String alarmId;
	private String alarmNo;
	private String alarmName;
	private String alarmContents;
	private String alarmRoute;
	private String alarmDetailRoute;
	private String alarmStatus;
	private String alarmGrade;
	private String alarmGradeCdNm;
	private String alarmOwner;
	private String alarmMusicPath;
	private String alarmImagePath;
	private String registerDate;
	private String regDate;
	private String registerTime;

	public AlarmVO() {
		super();
	}

	public String getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}

	public String getAlarmNo() {
		return alarmNo;
	}

	public void setAlarmNo(String alarmNo) {
		this.alarmNo = alarmNo;
	}

	public String getAlarmName() {
		return alarmName;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public String getAlarmContents() {
		return alarmContents;
	}

	public void setAlarmContents(String alarmContents) {
		this.alarmContents = alarmContents;
	}

	public String getAlarmRoute() {
		return alarmRoute;
	}

	public void setAlarmRoute(String alarmRoute) {
		this.alarmRoute = alarmRoute;
	}

	public String getAlarmDetailRoute() {
		return alarmDetailRoute;
	}

	public void setAlarmDetailRoute(String alarmDetailRoute) {
		this.alarmDetailRoute = alarmDetailRoute;
	}

	public String getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(String alarmStatus) {
		this.alarmStatus = alarmStatus;
	}

	public String getAlarmGrade() {
		return alarmGrade;
	}

	public void setAlarmGrade(String alarmGrade) {
		this.alarmGrade = alarmGrade;
	}

	public String getAlarmOwner() {
		return alarmOwner;
	}

	public void setAlarmOwner(String alarmOwner) {
		this.alarmOwner = alarmOwner;
	}

	public String getAlarmMusicPath() {
		return alarmMusicPath;
	}

	public void setAlarmMusicPath(String alarmMusicPath) {
		this.alarmMusicPath = alarmMusicPath;
	}

	public String getAlarmImagePath() {
		return alarmImagePath;
	}

	public void setAlarmImagePath(String alarmImagePath) {
		this.alarmImagePath = alarmImagePath;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public String getAlarmGradeCdNm() {
		return alarmGradeCdNm;
	}

	public void setAlarmGradeCdNm(String alarmGradeCdNm) {
		this.alarmGradeCdNm = alarmGradeCdNm;
	}
	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}
	
}
