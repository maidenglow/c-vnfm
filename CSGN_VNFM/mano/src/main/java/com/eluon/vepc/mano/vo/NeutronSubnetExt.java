package com.eluon.vepc.mano.vo;

import org.openstack4j.openstack.networking.domain.NeutronSubnet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("subnet")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronSubnetExt extends NeutronSubnet {

	@JsonInclude
	@JsonProperty("gateway_ip")
	private String gateway;
	

	@Override
	public String getGateway() {
		return gateway;
	}
}
