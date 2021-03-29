package com.eluon.vepc.mano.vo;

import java.util.Date;

/**
 * Account VO (AccountVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountVO.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
public class AccountVO extends BaseVO {
	private String userId;
	private String password;
	private String userName;
	private String company;
	private String dept;
	private String phone;
	private String handphone;
	private String email;
	private String gradeCode;
	private String gradeName;
	private String status;
	private String makeId;
	private Date registerDate;
	private String updateUser;
	private Date updateDate;
	private Date lastLoginDate;
	private int listNum;
	private String updateFlag;

	public AccountVO() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getHandphone() {
		return handphone;
	}

	public void setHandphone(String handphone) {
		this.handphone = handphone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMakeId() {
		return makeId;
	}

	public void setMakeId(String makeId) {
		this.makeId = makeId;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public int getListNum() {
		return listNum;
	}

	public void setListNum(int listNum) {
		this.listNum = listNum;
	}

	public String getUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
	}

}