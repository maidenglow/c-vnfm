package com.eluon.vepc.mano.vo;

import java.util.Date;

/**
 * AccountGrade VO (AccountGradeVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountGradeVO.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
public class AccountGradeVO extends BaseVO {
	private String gradeCode;
	private String gradeName;
	private String status;
	private Date registerDate;

	public AccountGradeVO() {
	}

	public String getGradeCode() {
		return gradeCode;
	}

	public String getGradeName() {
		return gradeName;
	}

	public String getStatus() {
		return status;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

}