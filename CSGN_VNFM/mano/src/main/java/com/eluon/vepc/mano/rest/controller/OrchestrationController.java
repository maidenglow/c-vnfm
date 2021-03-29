package com.eluon.vepc.mano.rest.controller;

import org.apache.commons.configuration.CompositeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.vepc.mano.rest.validator.OrchestrationValidator;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.OrchestrationService;

/**
 * Orchestration Controller (OrchestrationController)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: OrchestrationController.java,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@RestController
@RequestMapping("/orchestration")
public class OrchestrationController{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	protected final static Logger loggerSin = LoggerFactory.getLogger("MANO_PROCESS_SIN");
	
	@Autowired
	private OrchestrationService orchestrationService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private OrchestrationValidator orchestrationValidator;

    @RequestMapping("/page1.do")
    public String page1() {
        return "orchestration/page1";
    }
	
}