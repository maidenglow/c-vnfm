package com.eluon.vepc.mano.vo;

import java.util.Date;

/**
 * InstanceNetwork VO (InstanceNetworkVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: InstanceNetworkVO.java,v 1.0 2015/03/23 00:00:00 SimByungChul Express $
 */
public class InstanceNetworkVO extends BaseVO {
	private String networkId;
	private String instanceId;
	private String totalSendBytes;
	private String totalRecvBytes;
	private String nicName;
	private String ipAddress;
	private String mac;
	private String sendBytes;
	private String sendErrorBytes;
	private String recvBytes;
	private String recvErrorBytes;
	private Date registerDate;
	
	public InstanceNetworkVO() {
		super();
	}

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getTotalSendBytes() {
		return totalSendBytes;
	}

	public void setTotalSendBytes(String totalSendBytes) {
		this.totalSendBytes = totalSendBytes;
	}

	public String getTotalRecvBytes() {
		return totalRecvBytes;
	}

	public void setTotalRecvBytes(String totalRecvBytes) {
		this.totalRecvBytes = totalRecvBytes;
	}

	public String getNicName() {
		return nicName;
	}

	public void setNicName(String nicName) {
		this.nicName = nicName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getSendBytes() {
		return sendBytes;
	}

	public void setSendBytes(String sendBytes) {
		this.sendBytes = sendBytes;
	}

	public String getSendErrorBytes() {
		return sendErrorBytes;
	}

	public void setSendErrorBytes(String sendErrorBytes) {
		this.sendErrorBytes = sendErrorBytes;
	}

	public String getRecvBytes() {
		return recvBytes;
	}

	public void setRecvBytes(String recvBytes) {
		this.recvBytes = recvBytes;
	}

	public String getRecvErrorBytes() {
		return recvErrorBytes;
	}

	public void setRecvErrorBytes(String recvErrorBytes) {
		this.recvErrorBytes = recvErrorBytes;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

}
