package com.eluon.vepc.mano.vo;

import java.util.Date;

/**
 * InstanceCpu VO (InstanceCpuVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: InstanceCpuVO.java,v 1.0 2015/03/23 00:00:00 SimByungChul Express $
 */
public class InstanceCpuVO extends BaseVO {
	private String cpuId;
	private String instanceId;
	private String totalCoreCpu;
	private String usedCoreCpu;
	private String freeCoreCpu;
	private String usedCpuPer;
	private Date registerDate;
	
	public InstanceCpuVO() {
		super();
	}

	public String getCpuId() {
		return cpuId;
	}

	public void setCpuId(String cpuId) {
		this.cpuId = cpuId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getTotalCoreCpu() {
		return totalCoreCpu;
	}

	public void setTotalCoreCpu(String totalCoreCpu) {
		this.totalCoreCpu = totalCoreCpu;
	}

	public String getUsedCoreCpu() {
		return usedCoreCpu;
	}

	public void setUsedCoreCpu(String usedCoreCpu) {
		this.usedCoreCpu = usedCoreCpu;
	}

	public String getFreeCoreCpu() {
		return freeCoreCpu;
	}

	public void setFreeCoreCpu(String freeCoreCpu) {
		this.freeCoreCpu = freeCoreCpu;
	}

	public String getUsedCpuPer() {
		return usedCpuPer;
	}

	public void setUsedCpuPer(String usedCpuPer) {
		this.usedCpuPer = usedCpuPer;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

}
