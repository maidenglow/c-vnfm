package com.eluon.vepc.mano.vo;

import java.util.Date;

/**
 * SystemMemory VO (SystemMemoryVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: SystemMemoryVO.java,v 1.0 2015/03/23 00:00:00 SimByungChul Express $
 */
public class SystemMemoryVO extends BaseVO {
	private String memoryId;
	private String systemId;
	private String totalMemory;
	private String usedMemory;
	private String freeMemory;
	private String usedMemoryPer;
	private Date registerDate;
	
	public SystemMemoryVO() {
		super();
	}

	public String getMemoryId() {
		return memoryId;
	}

	public void setMemoryId(String memoryId) {
		this.memoryId = memoryId;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(String totalMemory) {
		this.totalMemory = totalMemory;
	}

	public String getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(String usedMemory) {
		this.usedMemory = usedMemory;
	}

	public String getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(String freeMemory) {
		this.freeMemory = freeMemory;
	}

	public String getUsedMemoryPer() {
		return usedMemoryPer;
	}

	public void setUsedMemoryPer(String usedMemoryPer) {
		this.usedMemoryPer = usedMemoryPer;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	
}
