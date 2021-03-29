package com.eluon.vepc.mano.rest.controller;

import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eluon.vepc.mano.rest.validator.TelemetryValidator;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.TelemetryService;
import com.eluon.vepc.mano.vo.InstanceVO;

/**
 * Telemetry Controller (TelemetryController)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: TelemetryController.java,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@RestController
@RequestMapping("/telemetry")
public class TelemetryController{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	protected final static Logger loggerSin = LoggerFactory.getLogger("MANO_PROCESS_SIN");
	
	@Autowired
	private TelemetryService telemetryService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private TelemetryValidator telemetryValidator;

	/**
	 * Instance List
	 * @method list
	 * @return
	 */
	@RequestMapping(value = "/instances", method = RequestMethod.GET)
	public List<InstanceVO> listInstances(@ModelAttribute InstanceVO instanceVO) {
		List<InstanceVO> listInstances = telemetryService.listInstances(instanceVO);
		loggerTra.debug("listInstances="+ listInstances);
		return listInstances;
	}

	/**
	 * @param instanceId
	 * @return
	 */
	@RequestMapping(value = "/instances/{instanceId}", method = RequestMethod.GET)
	public InstanceVO getInstance(@PathVariable("instanceId") String instanceId) {
		InstanceVO instanceVO = telemetryService.getInstance(instanceId);
		loggerTra.debug("instanceVO="+ instanceVO);
		return instanceVO;
	}
	
}