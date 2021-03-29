package com.eluon.vepc.mano.rest.controller;


import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.model.identity.v2.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.vepc.mano.rest.validator.IdentityValidator;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.IdentityService;

/**
 * Identity Controller (IdentityController)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: IdentityController.java,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@RestController
@RequestMapping("/identity")
public class IdentityController{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	protected final static Logger loggerSin = LoggerFactory.getLogger("MANO_PROCESS_SIN");
	@Autowired
	private IdentityService identityService;
    
	/**
	 * @return
	 */
	@RequestMapping(value = "/OS-KSADM/services", method = RequestMethod.GET)
	public ResponseEntity<String> listServices() {
		String listServices = identityService.listServices();
		loggerTra.debug("listServices="+ listServices);
		return new ResponseEntity<String>(listServices, HttpStatus.OK);
	}
	
	@RequestMapping(value = "{tenantName}/tenants", method = RequestMethod.GET)
	public List<? extends Tenant> listTenants(@PathVariable String tenantName) {
		return identityService.listTenant();
	}

	@RequestMapping(value = "/tenants", method = RequestMethod.GET)
	public ResponseEntity<String> allListTenants(@RequestParam Map<String, String> allRequestParams) {
		return identityService.listAllTenant();
	}
}