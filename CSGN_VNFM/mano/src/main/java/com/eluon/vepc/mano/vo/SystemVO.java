package com.eluon.vepc.mano.vo;

import java.util.Date;
import java.util.List;

/**
 * System VO (SystemVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: SystemVO.java,v 1.0 2015/01/27 00:00:00 SimByungChul Express $
 */
public class SystemVO extends BaseVO {
	private String systemId;
	private String hostName;
	private String hostType;
	private String privateIp;
	private String publicIp;
	private String osName;
	private String totalCoreCpu;
	private String usedCoreCpu;
	private String freeCoreCpu;
	private String usedCpuPer;
	private String totalMemory;
	private String usedMemory;
	private String freeMemory;
	private String usedMemoryPer;
	private String totalDisk;
	private String usedDisk;
	private String freeDisk;
	private String usedDiskPer;
	private String totalNetwork;
	private String totalSendNetwork;
	private String totalRecvNetwork;
	private String status;
	private Date registerDate;
	private List<SystemCpuVO> systemCpuList;
	private List<SystemMemoryVO> systemMemoryList;
	private List<SystemDiskVO> systemDiskList;
	private List<SystemNetworkVO> systemNetworkList;
	
	public SystemVO() {
		super();
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

	public String getHostType() {
		return hostType;
	}

	public void setHostType(String hostType) {
		this.hostType = hostType;
	}

	public String getPrivateIp() {
		return privateIp;
	}

	public void setPrivateIp(String privateIp) {
		this.privateIp = privateIp;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
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

	public String getTotalDisk() {
		return totalDisk;
	}

	public void setTotalDisk(String totalDisk) {
		this.totalDisk = totalDisk;
	}

	public String getUsedDisk() {
		return usedDisk;
	}

	public void setUsedDisk(String usedDisk) {
		this.usedDisk = usedDisk;
	}

	public String getFreeDisk() {
		return freeDisk;
	}

	public void setFreeDisk(String freeDisk) {
		this.freeDisk = freeDisk;
	}

	public String getUsedDiskPer() {
		return usedDiskPer;
	}

	public void setUsedDiskPer(String usedDiskPer) {
		this.usedDiskPer = usedDiskPer;
	}

	public String getTotalNetwork() {
		return totalNetwork;
	}

	public void setTotalNetwork(String totalNetwork) {
		this.totalNetwork = totalNetwork;
	}

	public String getTotalSendNetwork() {
		return totalSendNetwork;
	}

	public void setTotalSendNetwork(String totalSendNetwork) {
		this.totalSendNetwork = totalSendNetwork;
	}

	public String getTotalRecvNetwork() {
		return totalRecvNetwork;
	}

	public void setTotalRecvNetwork(String totalRecvNetwork) {
		this.totalRecvNetwork = totalRecvNetwork;
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

	public List<SystemCpuVO> getSystemCpuList() {
		return systemCpuList;
	}

	public void setSystemCpuList(List<SystemCpuVO> systemCpuList) {
		this.systemCpuList = systemCpuList;
	}
	
	public void addSystemCpuList(SystemCpuVO systemCpuVO) {
		this.systemCpuList.add(systemCpuVO);
	}

	public List<SystemMemoryVO> getSystemMemoryList() {
		return systemMemoryList;
	}

	public void setSystemMemoryList(List<SystemMemoryVO> systemMemoryList) {
		this.systemMemoryList = systemMemoryList;
	}

	public void addSystemMemoryList(SystemMemoryVO systemMemoryVO) {
		this.systemMemoryList.add(systemMemoryVO);
	}
	
	public List<SystemDiskVO> getSystemDiskList() {
		return systemDiskList;
	}

	public void setSystemDiskList(List<SystemDiskVO> systemDiskList) {
		this.systemDiskList = systemDiskList;
	}

	public void addSystemDiskList(SystemDiskVO systemDiskVO) {
		this.systemDiskList.add(systemDiskVO);
	}
	
	public List<SystemNetworkVO> getSystemNetworkList() {
		return systemNetworkList;
	}

	public void setSystemNetworkList(List<SystemNetworkVO> systemNetworkList) {
		this.systemNetworkList = systemNetworkList;
	}
	
	public void addSystemNetworkList(SystemNetworkVO systemNetworkVO) {
		this.systemNetworkList.add(systemNetworkVO);
	}
}