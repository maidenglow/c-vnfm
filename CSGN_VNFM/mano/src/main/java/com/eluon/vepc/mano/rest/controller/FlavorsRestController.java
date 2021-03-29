package com.eluon.vepc.mano.rest.controller;

import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.model.compute.Flavor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.vepc.mano.service.CommonService;

@RestController
@RequestMapping("/flavors")
public class FlavorsRestController {
	
	/** Logger */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	@Autowired
	private CommonService commonService;
	
	@Autowired
	private CompositeConfiguration config;
	
	@RequestMapping("/list.do")
	public List<? extends Flavor> list() {
		
		OSClientV2 os = commonService.buildOSFactory();
		List<? extends Flavor> list = os.compute().flavors().list();
		logger.debug("list : {}", list);

		return list;
	}
}
