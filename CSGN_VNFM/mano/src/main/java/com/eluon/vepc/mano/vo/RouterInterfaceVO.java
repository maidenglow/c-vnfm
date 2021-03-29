package com.eluon.vepc.mano.vo;

import org.openstack4j.model.network.Subnet;
import org.openstack4j.openstack.networking.domain.NeutronSubnet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * RouterInterface VO (RouterInterfaceVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: NetworkVO.java,v 1.0 2015/01/27 00:00:00 SimByungChul Express $
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouterInterfaceVO extends BaseVO {
	
	@JsonProperty("subnet")
	private NeutronSubnet subnet;
	
	@JsonProperty("ip_address")
	private String ipAddress;
	
	@JsonProperty("router_id")
	private String routerId;
	
	public RouterInterfaceVO() {
		super();
	}

	public Subnet getSubnet() {
		return subnet;
	}

	public void setSubnet(NeutronSubnet subnet) {
		this.subnet = subnet;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getRouterId() {
		return routerId;
	}

	public void setRouterId(String routerId) {
		this.routerId = routerId;
	}
}
