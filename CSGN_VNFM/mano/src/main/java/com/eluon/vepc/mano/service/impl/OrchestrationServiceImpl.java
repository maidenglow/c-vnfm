package com.eluon.vepc.mano.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.configuration.CompositeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eluon.vepc.mano.adapter.OrchestrationAdapter;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.OrchestrationService;

/**
 * OrchestrationServiceImpl (OrchestrationServiceImpl)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: OrchestrationServiceImpl.java,v 1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@Service("orchestrationService")
public class OrchestrationServiceImpl implements OrchestrationService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private OrchestrationAdapter orchestrationAdapter;
	@Autowired
	private CompositeConfiguration config;
	@Autowired
	private CommonService commonService;

	public OrchestrationServiceImpl() {
	}

	@PostConstruct
	public void init() throws Exception {
	}

	@PreDestroy
	public void destroy() throws Exception {
	}


}
