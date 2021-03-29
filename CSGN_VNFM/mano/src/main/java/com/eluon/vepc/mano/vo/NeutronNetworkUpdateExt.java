package com.eluon.vepc.mano.vo;

import org.openstack4j.openstack.networking.domain.NeutronNetworkUpdate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("network")
public class NeutronNetworkUpdateExt extends NeutronNetworkUpdate {
	
    private static final long serialVersionUID = 1L;

	@JsonProperty("router:external")
	private boolean routerExternal;
}
