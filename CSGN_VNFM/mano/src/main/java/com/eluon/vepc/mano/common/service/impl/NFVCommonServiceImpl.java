package com.eluon.vepc.mano.common.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eluon.vepc.mano.common.dao.NFVCommonDAO;
import com.eluon.vepc.mano.common.service.NFVCommonService;

/**
 * @author rochas
 * @version 2015/10/13
 */

@Service("NFVCommonService")
public class NFVCommonServiceImpl implements NFVCommonService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected final static Logger loggerTra = LoggerFactory.getLogger("MANO_PROCESS_TRA");

	@Autowired
	private NFVCommonDAO nfvCommonDAO;
	
	@Override
	public String getSeq(String seq_name) {
		
		return null;
	}

}
