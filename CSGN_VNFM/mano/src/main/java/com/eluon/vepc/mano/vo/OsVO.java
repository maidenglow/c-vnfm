package com.eluon.vepc.mano.vo;

import java.util.Date;

/**
 * Os VO (OsVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: OsVO.java,v 1.0 2015/01/27 00:00:00 SimByungChul Express $
 */
public class OsVO extends BaseVO {
	private String osId;
	private String systemId;
	private String hostName;
	private String osName;
	private String coreTotalCpu;
	private String coreFreeCpu;
	private String systemCpu;
	private String userCpu;
	private String idleCpu;
	private String realTotalMemory;
	private String realFreeMemory;
	private String swapTotalMemory;
	private String swapFreeMemory;
	private String totalFreeMemory;
	private Date registerDate;
	
	public OsVO() {
		super();
	}

	public String getOsId() {
		return osId;
	}

	public void setOsId(String osId) {
		this.osId = osId;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getCoreTotalCpu() {
		return coreTotalCpu;
	}

	public void setCoreTotalCpu(String coreTotalCpu) {
		this.coreTotalCpu = coreTotalCpu;
	}

	public String getCoreFreeCpu() {
		return coreFreeCpu;
	}

	public void setCoreFreeCpu(String coreFreeCpu) {
		this.coreFreeCpu = coreFreeCpu;
	}

	public String getSystemCpu() {
		return systemCpu;
	}

	public void setSystemCpu(String systemCpu) {
		this.systemCpu = systemCpu;
	}

	public String getUserCpu() {
		return userCpu;
	}

	public void setUserCpu(String userCpu) {
		this.userCpu = userCpu;
	}

	public String getIdleCpu() {
		return idleCpu;
	}

	public void setIdleCpu(String idleCpu) {
		this.idleCpu = idleCpu;
	}

	public String getRealTotalMemory() {
		return realTotalMemory;
	}

	public void setRealTotalMemory(String realTotalMemory) {
		this.realTotalMemory = realTotalMemory;
	}

	public String getRealFreeMemory() {
		return realFreeMemory;
	}

	public void setRealFreeMemory(String realFreeMemory) {
		this.realFreeMemory = realFreeMemory;
	}

	public String getSwapTotalMemory() {
		return swapTotalMemory;
	}

	public void setSwapTotalMemory(String swapTotalMemory) {
		this.swapTotalMemory = swapTotalMemory;
	}

	public String getSwapFreeMemory() {
		return swapFreeMemory;
	}

	public void setSwapFreeMemory(String swapFreeMemory) {
		this.swapFreeMemory = swapFreeMemory;
	}

	public String getTotalFreeMemory() {
		return totalFreeMemory;
	}

	public void setTotalFreeMemory(String totalFreeMemory) {
		this.totalFreeMemory = totalFreeMemory;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

}
