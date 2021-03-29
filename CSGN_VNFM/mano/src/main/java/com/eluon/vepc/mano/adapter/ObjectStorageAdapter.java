package com.eluon.vepc.mano.adapter;

import org.apache.commons.configuration.CompositeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eluon.vepc.mano.service.CommonService;

/**
 * ObjectStorage Adapter (ObjectStorageAdapter)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: ObjectStorageAdapter.java,v1.0 2015/01/13 00:00:00 SimByungChul Express $
 */
@Component
public class ObjectStorageAdapter {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private CompositeConfiguration config;

	public ObjectStorageAdapter() {
	}
	
	
}