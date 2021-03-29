package com.eluon.vepc.mano.vo;

import org.openstack4j.openstack.networking.domain.NeutronNetQuota;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("quota")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetQuotaVO extends NeutronNetQuota {

    @JsonProperty("floatingip_used")
    private int floatingIpUsed;

	public int getFloatingIpUsed() {
		return floatingIpUsed;
	}

	public void setFloatingIpUsed(int floatingIpUsed) {
		this.floatingIpUsed = floatingIpUsed;
	}
    
}
