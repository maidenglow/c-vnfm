package com.eluon.vepc.mano.rest.validator;

import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.Server;
import org.openstack4j.openstack.compute.domain.NovaFlavor;
import org.openstack4j.openstack.compute.domain.NovaServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.eluon.vepc.mano.service.ComputeService;

/**
 * Compute Validator (ComputeValidator)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: ComputeValidator,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@Component
public class ComputeValidator implements Validator {
	@Autowired
	private ComputeService computeService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub
	}
	
	public Server validateServer(NovaServer novaServer) {
		if (novaServer == null ||
			novaServer.getName() == null ||
			novaServer.getImageId() == null ||
			novaServer.getFlavorId() == null
			) return null;
		return novaServer;
	}
	
	public Flavor validateFlavor(NovaFlavor novaFlavor) {
		if (novaFlavor == null ||
			novaFlavor.getName() == null ||
			novaFlavor.getRam() == 0 ||
			novaFlavor.getVcpus() == 0 ||
			novaFlavor.getDisk() == 0 ||
			novaFlavor.getRxtxFactor() == 0 
			) return null;
		return novaFlavor;
	}


}