package com.eluon.vepc.mano.vo;

import java.util.Date;

/**
 * InstanceMemory VO (InstanceMemoryVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: InstanceMemoryVO.java,v 1.0 2015/03/23 00:00:00 SimByungChul Express $
 */
public class InstanceMemoryVO extends BaseVO {
	private String memoryId;
	private String instanceId;
	private String totalMemory;
	private String usedMemory;
	private String freeMemory;
	private String usedMemoryPer;
	private Date registerDate;
	
	public InstanceMemoryVO() {
		super();
	}

	public String getMemoryId() {
		return memoryId;
	}

	public void setMemoryId(String memoryId) {
		this.memoryId = memoryId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
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
