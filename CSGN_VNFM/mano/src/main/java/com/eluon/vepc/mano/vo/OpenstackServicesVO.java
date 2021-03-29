package com.eluon.vepc.mano.vo;

import java.util.List;

/**
 * OpenstackServices VO (OpenstackServicesVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: OpenstackServicesVO.java,v 1.0 2015/03/31 00:00:00 SimByungChul Express $
 */
public class OpenstackServicesVO extends BaseVO {
	private List<OpenstackServiceVO> osKsadmServcies;

	public List<OpenstackServiceVO> getOsKsadmServcies() {
		return osKsadmServcies;
	}

	public void setOsKsadmServcies(List<OpenstackServiceVO> osKsadmServcies) {
		this.osKsadmServcies = osKsadmServcies;
	}

}