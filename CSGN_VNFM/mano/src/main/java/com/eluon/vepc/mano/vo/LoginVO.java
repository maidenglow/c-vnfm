package com.eluon.vepc.mano.vo;

/**
 * Login VO (LoginVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: LoginVO.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
public class LoginVO extends BaseVO {
	private String userId = null;
	private String password = null;

	public LoginVO() {
		super();
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

}
