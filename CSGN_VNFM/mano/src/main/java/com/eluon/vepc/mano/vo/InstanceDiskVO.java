package com.eluon.vepc.mano.vo;

import java.util.Date;

/**
 * InstanceDisk VO (InstanceDiskVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: InstanceDiskVO.java,v 1.0 2015/03/23 00:00:00 SimByungChul Express $
 */
public class InstanceDiskVO extends BaseVO {
	private String diskId;
	private String instanceId;
	private String mount;
	private String device;
	private String blocks;
	private String used;
	private Date registerDate;
	
	public InstanceDiskVO() {
		super();
	}

	public String getDiskId() {
		return diskId;
	}

	public void setDiskId(String diskId) {
		this.diskId = diskId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getMount() {
		return mount;
	}

	public void setMount(String mount) {
		this.mount = mount;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getBlocks() {
		return blocks;
	}

	public void setBlocks(String blocks) {
		this.blocks = blocks;
	}

	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	
}