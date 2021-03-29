package com.eluon.vepc.mano.vo;

import java.util.Date;

/**
 * Network VO (NetworkVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: NetworkVO.java,v 1.0 2015/01/27 00:00:00 SimByungChul Express $
 */
public class NetworkVO extends BaseVO {
	private String networkId;
	private String systemId;
	private String hostName;
	private String osName;
	private String nicName;
	private String ipAddress;
	private String mac;
	private String sendBytes;
	private String sendErrorBytes;
	private String recvBytes;
	private String recvErrorBytes;	
	private Date registerDate;
	
	public NetworkVO() {
		super();
	}

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
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