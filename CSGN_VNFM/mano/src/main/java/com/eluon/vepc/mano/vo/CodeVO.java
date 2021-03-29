package com.eluon.vepc.mano.vo;

import java.util.Date;

/**
 * Code VO (CodeVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: CodeVO.java,v 1.0 2015/02/13 00:00:00 SimByungChul Express $
 */
public class CodeVO extends BaseVO {
	private String code;
	private String parentCode;
	private String description;
	private String status;
	private Date registerDate;
	
	public CodeVO() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

}